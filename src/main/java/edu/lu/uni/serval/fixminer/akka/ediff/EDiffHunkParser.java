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
	

	@Override
	public void parseFixPatterns(File prevFile, File revFile, File diffentryFile,String project,String actionType) {
		List<HierarchicalActionSet> actionSets = parseChangedSourceCodeWithGumTree2(prevFile, revFile);
		if (actionSets.size() != 0) {
			String folder= null;
			boolean processActionSet = false;
//			switch (actionType){
//				case "ALL":
			if(actionType.equals("ALL")){
					folder = "/ALL/";
					processActionSet = true;
			}else if(actionType.equals("UPD") || actionType.equals("INS") || actionType.equals("DEL") || actionType.equals("MOV")|| actionType.equals("MIX")){
				boolean isUPD = actionSets.stream().allMatch(p -> p.getAction() instanceof Update);
				boolean isINS = actionSets.stream().allMatch(p -> p.getAction() instanceof Insert);
				boolean isDEL = actionSets.stream().allMatch(p -> p.getAction() instanceof Delete);
				boolean isMOV = actionSets.stream().allMatch(p -> p.getAction() instanceof Move);
				if(isUPD){
					folder = "/UPD/";
					processActionSet = true;
				}else if(isINS){
					folder = "/INS/";
					processActionSet = true;
				}else if(isDEL){
					folder = "/DEL/";
					processActionSet = true;
				}else if(isMOV){
					folder = "/MOV/";
					processActionSet = true;
				}else{
					folder = "/MIX/";
					processActionSet = true;
				}
			}else{


					processActionSet = false;
					System.err.print(actionType + "not known");

			}


			int hunkSet = 0;
			if(processActionSet){
				for (HierarchicalActionSet actionSet : actionSets) {




					FileOutputStream f = null;

					try {
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
