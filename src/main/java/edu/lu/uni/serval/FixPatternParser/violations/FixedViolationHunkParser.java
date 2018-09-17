package edu.lu.uni.serval.FixPatternParser.violations;

import edu.lu.uni.serval.gumtree.regroup.HierarchicalActionSet;

import java.io.*;
import java.util.List;



/**
 * Parse fix violations with GumTree in terms of multiple statements.
 * 
 * @author kui.liu
 *
 */
public class FixedViolationHunkParser extends FixedViolationParser {
	
	public String testingInfo = "";
	
	public int nullMappingGumTreeResult = 0;
	public int pureDeletions = 0;
	public int largeHunk = 0;
	public int nullSourceCode = 0;
	public int nullMatchedDiffEntry = 0;
	public int testInfos = 0;

	public String unfixedViolations = "";
	
	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile,String project) {
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile);
		if (actionSets.size() != 0) {
//			boolean isUpdate =
//					actionSets.stream().allMatch(p -> p.getAction() instanceof Update);
//			boolean isInsert =
//					actionSets.stream().allMatch(p -> p.getAction() instanceof Insert);
//			boolean isDelete =
//					actionSets.stream().allMatch(p -> p.getAction() instanceof Delete);
//			boolean isMove =
//					actionSets.stream().allMatch(p -> p.getAction() instanceof Move);
			int hunkSet = 0;
//			if (isUpdate || isInsert || isDelete || isMove) {
				for (HierarchicalActionSet actionSet : actionSets) {
					String folder = "/ALL/";
//					if (isUpdate) {
//						folder = "/UPD/";
//					} else if (isDelete) {
//						folder = "/DEL/";
//					} else if (isInsert) {
//						folder = "/INS/";
//					} else if (isMove) {
//						folder = "/MOV/";
//					}


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

//			}
		}

	}



}
