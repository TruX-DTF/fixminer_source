package edu.lu.uni.serval.gumtree;

import java.io.File;
import java.io.IOException;

import org.eclipse.jdt.core.dom.ASTParser;

import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import edu.lu.uni.serval.gen.jdt.exp.ExpJdtTreeGenerator;
import edu.lu.uni.serval.gen.jdt.rawToken.RawTokenJdtTreeGenerator;

public class GumTreeGenerator {
	
	public enum GumTreeType {
		EXP_JDT,
		RAW_TOKEN,
	}
	
	public ITree generateITreeForJavaFile(File javaFile, GumTreeType type) {
		ITree gumTree = null;
		try {
			TreeContext tc = null;
			switch (type) {
			case EXP_JDT:
				tc = new ExpJdtTreeGenerator().generateFromFile(javaFile);
				break;
			case RAW_TOKEN:
				tc = new RawTokenJdtTreeGenerator().generateFromFile(javaFile);
				break;
			default:
				break;
			}
			
			if (tc != null){
				gumTree = tc.getRoot();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gumTree;
	}
	
	public ITree generateITreeForJavaFile(String javaFile, GumTreeType type) {
		ITree gumTree = null;
		try {
			TreeContext tc = null;
			switch (type) {
			case EXP_JDT:
				tc = new ExpJdtTreeGenerator().generateFromFile(javaFile);
				break;
			case RAW_TOKEN:
				tc = new RawTokenJdtTreeGenerator().generateFromFile(javaFile);
				break;
			default:
				break;
			}
			
			if (tc != null){
				gumTree = tc.getRoot();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gumTree;
	}
	
	public ITree generateITreeForCodeBlock(String codeBlock, GumTreeType type) {
		ITree gumTree = null;
		try {
			TreeContext tc = null;
			switch (type) {
			case EXP_JDT:
				tc = new ExpJdtTreeGenerator().generateFromString(codeBlock, ASTParser.K_STATEMENTS);
				break;
			case RAW_TOKEN:
				tc = new RawTokenJdtTreeGenerator().generateFromString(codeBlock, ASTParser.K_STATEMENTS);
				break;
			default:
				break;
			}
			
			if (tc != null){
				gumTree = tc.getRoot();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gumTree;
	}




	public ITree generateITreeForCFileForCode(File javaFile) {
		ITree gumTree = null;
		try {
			TreeContext tc = null;
			tc = new CTreeGenerator().generateFromFile(javaFile);

			if (tc != null){
				gumTree = tc.getRoot();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gumTree;
	}

}
