package edu.lu.uni.serval.fixminer.akka.ediff;

import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.actions.model.Update;

import java.io.*;
import java.util.List;



/**
 * Parse fix violations with GumTree in terms of multiple statements.
 * 
 * @author kui.liu
 *
 */
public class EDiffHunkParser extends EDiffParser {
	
	public String testingInfo = "";
	
	public int nullMappingGumTreeResult = 0;
	public int pureDeletions = 0;
	public int largeHunk = 0;
	public int nullSourceCode = 0;
	public int nullMatchedDiffEntry = 0;
	public int testInfos = 0;

	public String unfixedViolations = "";
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile,String project,String actionType) {
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile);
		if (actionSets.size() != 0) {
			String folder= null;
			boolean processActionSet = false;
			switch (actionType){
				case "ALL":
					folder = "/ALL/";
					processActionSet = true;
					break;
				case "UPD":
					processActionSet =
							actionSets.stream().allMatch(p -> p.getAction() instanceof Update);
					folder = "/UPD/";
					break;
				case "INS":
					processActionSet =
							actionSets.stream().allMatch(p -> p.getAction() instanceof Insert);

					folder = "/INS/";
					break;
				case "DEL":
					processActionSet =
							actionSets.stream().allMatch(p -> p.getAction() instanceof Delete);
					folder = "/DEL/";
					break;
				case "MOV":
					processActionSet =
							actionSets.stream().allMatch(p -> p.getAction() instanceof Move);
					folder = "/MOV/";
					break;
				default:
					processActionSet = false;
					System.err.print(actionType + "not known");
					break;
			}


			int hunkSet = 0;
			if(processActionSet){
				for (HierarchicalActionSet actionSet : actionSets) {




					FileOutputStream f = null;

					try {
//					String pj = diffentryFile.getParent().split("Defects4J")[1];
						String datasetName = project;
						String[] split1 = diffentryFile.getParent().split(datasetName);
						String root = split1[0];
						String pj = split1[1].split("/")[1];

						String hunkTreeFileName = root + "EnhancedASTDiff" + datasetName + "/" + pj + folder + diffentryFile.getName() + "_" + String.valueOf(hunkSet);
						f = new FileOutputStream(new File(hunkTreeFileName));
						ObjectOutputStream o = new ObjectOutputStream(f);
						o.writeObject(actionSet);

						o.close();
						f.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					hunkSet++;
				}

			}
		}

	}



}
