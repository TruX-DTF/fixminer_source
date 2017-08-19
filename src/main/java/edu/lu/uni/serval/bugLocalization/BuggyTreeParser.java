package edu.lu.uni.serval.bugLocalization;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * 
 * Testing data
 * localized bugs by FindBugs.
 * Use their java file, use GumTree to parse it, simplify the buggy code.
 * get all buggy code tokens.
 * 
 * training data
 * 
 * 
 * Word2Vec embeds data.
 * 
 * 
 * @author kui.liu
 *
 */
public class BuggyTreeParser {

	public static void main(String[] args) {
		List<String> data = readInfo("Dataset/localized-violations1.list");
		StringBuilder bugTokens = new StringBuilder();
		int i = 0;
        for (String bug : data) {
        	i ++;
        	String[] info = bug.split(" : ");
        	String bugId = info[0].trim();
        	String fileName = info[2].trim();
        	int startLine = Integer.parseInt(info[3].trim());
        	int endLine = Integer.parseInt(info[4].trim());
        	
        	bugId = bugId.substring(0, bugId.lastIndexOf("."));
        	String[] bugIdInfo = bugId.split("_");
        	String projectName = bugIdInfo[0];
        	String bugIdSt = bugIdInfo[1];
        	String path = "../FPM_Violations/Testing/CheckedOut/" + projectName + "/" + bugIdSt + "/buggy/";
        	if ("Chart".equals(projectName)) {
        		path += "source/";
        	} else {
        		path += "src/";
        	}
        	List<File> javaFiles = FileHelper.getAllFiles(path, ".java");
        	for (File javaFile : javaFiles) {
        		if (javaFile.getPath().endsWith(fileName)) {
        			AlarmTree alarmTree = new AlarmTree(javaFile, startLine, endLine);
        			alarmTree.extract();
        			List<ITree> matchedTrees = alarmTree.getAlarmTrees();
        			SimpleTree simpleTree = new SimpleTree();
        			simpleTree.setLabel("Block");
    				simpleTree.setNodeType("Block");
    				List<SimpleTree> children = new ArrayList<>();
    				
    				for (ITree matchedTree : matchedTrees) {
    					SimpleTree simpleT = new SimplifyTree().canonicalizeSourceCodeTree(matchedTree, null);
    					children.add(simpleT);
    				}
    				simpleTree.setChildren(children);
    				
    				String tokens = Tokenizer.getTokensDeepFirst(simpleTree);
    				bugTokens.append(tokens + "\n");
    				System.out.println(i + "### " + tokens);
        		}
        	}
        }
        
        FileHelper.outputToFile(Configuration.EMBEDDING_DATA_TOKENS2, bugTokens, false);
	}

	private static List<String> readInfo(String file) {
		List<String> info = new ArrayList<>();
		String content = FileHelper.readFile(file);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				info.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return info;
	}

}
