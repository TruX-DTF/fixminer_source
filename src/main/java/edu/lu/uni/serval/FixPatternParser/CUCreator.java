package edu.lu.uni.serval.FixPatternParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * Creator of a CompilationUnit.
 * 
 * @author kui.liu
 *
 */
public class CUCreator {
	
	public CompilationUnit createCompilationUnit(File javaFile) {
		CompilationUnit unit = null;
		try {
			char[] sourceCode = readFileToCharArray(new FileReader(javaFile));
			ASTParser parser = createASTParser(sourceCode);
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			unit = (CompilationUnit) parser.createAST(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return unit;
	}

	private ASTParser createASTParser(char[] javaCode) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(javaCode);

		return parser;
	}
	
	private char[] readFileToCharArray(FileReader fileReader) throws IOException {
		StringBuilder fileData = new StringBuilder();
		BufferedReader br = new BufferedReader(fileReader);

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = br.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		br.close();

		return fileData.toString().toCharArray();
	}
}
