package edu.lu.uni.serval.FixPattern.info;

//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;

import org.eclipse.jdt.core.dom.ASTParser;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import com.github.gumtreediff.actions.ActionGenerator;
//import com.github.gumtreediff.actions.model.Action;
//import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
//import com.github.gumtreediff.gen.jdt.cd.CdJdtTreeGenerator;
//import com.github.gumtreediff.matchers.Matcher;
//import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;
import edu.lu.uni.serval.gen.jdt.exp.ExpJdtTreeGenerator;

@Deprecated
public class GumTreeAnalysis {
	

//	private static void analyzeBugFixes(String gitRepoPath, String outputPath) {
//		log.info("Repo: " + gitRepoPath);
//		
//		GitTraveller gitTraveller = new GitTraveller(gitRepoPath, outputPath);
//        gitTraveller.travelGitRepo();
////        Map<String, List<CommitFile>> commitFiles = gitTraveller.getCommitFiles();
//
//        List<MyDiffEntry> allDiffEntries = gitTraveller.getAllDiffEntries();
//        String previousFilesPath = gitTraveller.getPreviousFilesPath();
//        String revisedFilesPath = gitTraveller.getRevisedFilesPath();
//        for (MyDiffEntry diff : allDiffEntries) {
//        	String fileA = previousFilesPath + diff.getPrevFile();
//            String fileB = revisedFilesPath + diff.getRevFile();
//            List<String> gumTreeResults = GumTreeAnalysis.compareTwoFilesWithGumTree(fileA, fileB);
//            if (gumTreeResults.size() == 0) {
//            	continue;
//            }
//            StringBuilder builder = new StringBuilder();
//            builder.append("Previous File: " + fileA + "\n");
//            builder.append("Revised File: " + fileB + "\n");
//            String diffs = "";
//            for (ModifiedDetails md : diff.getModifiedDetails()) {
//            	diffs += md.getLineNumber() + "\n";
//            	diffs += md.getFragment() + "\n";
//            }
//            builder.append("DiffEntry: " + diffs);
//            for (String gumTreeResult : gumTreeResults) {
//            	builder.append(gumTreeResult.toString() + "\n");
//            }
//            FileHelper.outputToFile("OUTPUT/GumTreeResults/" + FileHelper.getRepositoryName(gitRepoPath) + "/" + diff.getRevFile().replace(".java", ".txt"), builder, false);
//        }
//        
////        DiffEntryParser diffEntryParser = new DiffEntryParser(allDiffEntries);
////        diffEntryParser.parseDiffEntries();
////        
////        // <String, List>: String ==> revisedFileName.
////        Map<String, List<ModifiedFragment>> parsedDiffEntries = diffEntryParser.getParsedDiffEntries();
////        diffEntryParser = null;
////        allDiffEntries = null;
////        
////        gitTraveller = null;
////
////        for (Map.Entry<String, List<ModifiedFragment>> entry : parsedDiffEntries.entrySet()) {
////        	String revisedFileName = entry.getKey();
////        	String fileA = previousFilesPath + "prev_" + revisedFileName;
////            String fileB = revisedFilesPath + revisedFileName;
////            System.err.println("FileName" + fileA);
////            List<String> gumTreeResults = GumTreeAnalysis.compareTwoFilesWithGumTree(fileA, fileB);
////            StringBuilder builder = new StringBuilder();
////            builder.append("Previous File: " + fileA + "\n");
////            builder.append("Revised File: " + fileB + "\n");
////            builder.append("DiffEntry: ");
////            for (String gumTreeResult : gumTreeResults) {
////            	builder.append(gumTreeResult + "\n");
////            }
////            FileHelper.outputToFile("OUTPUT/GumTreeResults/" + FileHelper.getRepositoryName(gitRepoPath) + "/" + revisedFileName.replace(".java", ".txt"), builder, false);
////        }
//        
//	}
//
//	public static List<String> compareTwoFilesWithGumTree(String prevFile, String revFile) {
//		List<String> gumTreeResults = new ArrayList<String>();
//		
//		try {
////			TreeContext tc1 = new ExpJdtTreeGenerator().generateFromFile(prevFile);
////			TreeContext tc2 = new ExpJdtTreeGenerator().generateFromFile(revFile);
////			TreeContext tc1 = new JdtTreeGenerator().generateFromFile(prevFile);
////			TreeContext tc2 = new JdtTreeGenerator().generateFromFile(revFile);
//			TreeContext tc1 = new RowTokenJdtTreeGenerator().generateFromFile(prevFile);
//			TreeContext tc2 = new RowTokenJdtTreeGenerator().generateFromFile(revFile);
////			TreeContext tc1 = new CdJdtTreeGenerator().generateFromFile(prevFile);
////			TreeContext tc2 = new CdJdtTreeGenerator().generateFromFile(revFile);
//			ITree t1 = tc1.getRoot();
//			ITree t2 = tc2.getRoot();
//			
//			Matcher m = Matchers.getInstance().getMatcher(t1, t2);
//			m.match();
//			
//			ActionGenerator ag = new ActionGenerator(t1, t2, m.getMappings());
//			ag.generate();
//			
//			List<Action> actions = ag.getActions();
//			for(Action ac : actions){
//				String actionStr = parseAction(ac.toString());
//				gumTreeResults.add(actionStr);
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return gumTreeResults;
//	}

//	private static String parseAction(String actStr) {
//		// UPD 25@@!a from !a to isTrue(a) at 69
//		String[] actStrArrays = actStr.split("@@");
//		actStr = "";
//		int length = actStrArrays.length;
//		for (int i = 0; i < length - 1; i++) {
//			String actStrFrag = actStrArrays[i];
//			int index = actStrFrag.lastIndexOf(" ") + 1;
//			String nodeType = actStrFrag.substring(index);
//			String backup = nodeType;
//			try {
//				nodeType = ASTNodeMap.map.get(Integer.parseInt(nodeType));
//			} catch (NumberFormatException e) {
//				nodeType = backup;
//				log.info(actStr);
//			}
//			actStrFrag = actStrFrag.substring(0, index) + nodeType + "@@";
//			actStr += actStrFrag;
//		}
//		actStr += actStrArrays[length - 1];
//		return actStr;
//	}
	
	private static String parseAction(String actStr) {
		// UPD 25@@!a from !a to isTrue(a) at 69
		String[] actStrArrays = actStr.split("@@");
		actStr = "";
		int length = actStrArrays.length;
		for (int i =0; i < length - 1; i ++) {
			String actStrFrag = actStrArrays[i];
			int index = actStrFrag.lastIndexOf("	") + 1;
			String nodeType = actStrFrag.substring(index);
			nodeType = ASTNodeMap.map.get(Integer.parseInt(nodeType));
			actStrFrag = actStrFrag.substring(0, index) + nodeType + "@@";
			actStr += actStrFrag;
		}
		actStr += actStrArrays[length - 1];
		return actStr;
	}
	
}
