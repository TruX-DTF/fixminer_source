package edu.lu.uni.serval.livestudy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.lu.uni.serval.FixPatternParser.Tokenizer;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.gumtree.regroup.SimpleTree;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseViolations {

	private static final String OUTPUT_PATH = Configuration.ROOT_PATH + "LiveStudy/BugsInfo/";
	private static int i = 0;
	private static int j = 0;
	
	public static void main(String[] args) {
		// output path
//		FileHelper.deleteDirectory(OUTPUT_PATH);
		List<String> projects = new ArrayList<>();
		File[] files = new File(OUTPUT_PATH).listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				projects.add(file.getName());
			}
		}
		String projectsPath = Configuration.ROOT_PATH + "LiveStudy/projects/";
		String bugsListPath = Configuration.ROOT_PATH + "LiveStudy/BugsList/";
		List<File> bugsListFiles = FileHelper.getAllFiles(bugsListPath, ".list");
		for (File bugsListFile : bugsListFiles) {
			String fileName = FileHelper.getFileNameWithoutExtension(bugsListFile);
			if (projects.contains(fileName)) continue;
			ParseViolations parser = new ParseViolations();
			Map<String, Violation> violations = new HashMap<>(); // <ProjectName, Violations>, Violation: <projectName, List<Alarm>>.
			violations = parser.readViolations(bugsListFile);
			parser.parseViolationToTokens(violations, projectsPath);
		}

		System.out.println(i);
		System.out.println(j);
	}

	/**
	 * Get the source code tokens of all violation instances by visiting each java project.
	 * 
	 * @param violations
	 * @param projectsPath
	 */
	public void parseViolationToTokens(Map<String, Violation> violations, String projectsPath) {
		for (Map.Entry<String, Violation> entry : violations.entrySet()) {
			String projectName = entry.getKey();
			List<Alarm> alarms = entry.getValue().getAlarms();
			List<File> javaFiles = FileHelper.getAllFiles(projectsPath + projectName, ".java");
			
			//Each violation: commons-math : DLS_DEAD_LOCAL_STORE : org/apache/commons/math4/dfp/Dfp.java : 2049 : 2049
			/**
			 * @ProjectName
			 * @ViolationType
			 * @FileName
			 * @LineNumber
			 * @SourceCode
			 * @Tokens
			 * @NumberOfTokens
			 */
			// sizes file, tokens file, and bugs-info file
			StringBuilder sizesBuilder = new StringBuilder();
			StringBuilder tokensBuilder = new StringBuilder();
			StringBuilder bugsInfoBuilder = new StringBuilder();
			for (Alarm alarm : alarms) {
				String fileName = alarm.getFileName();
//				if (fileName.toLowerCase(Locale.ENGLISH).contains("test")) continue;
				
				File sourceCodeFile = locateSourceCodeFile(javaFiles, fileName);
				if (sourceCodeFile == null) {
					j ++;
					continue;
				}
				
				Parser parser = new Parser(alarm, sourceCodeFile);
				parser.parse();
				SimpleTree simpleTree = parser.getSimpleTree();
				
				if (simpleTree != null) {
					int finalStartLine = parser.getFinalStartLine();
					int finalEndLine = parser.getFinalEndLine();
					String violationType = alarm.getAlarmType();
					
					String tokens = Tokenizer.getTokensDeepFirst(simpleTree);
					String[] tokensArray = tokens.split(" ");
					int length = tokensArray.length;
					StringBuilder sourceCode = readSourceCode(sourceCodeFile, finalStartLine, finalEndLine);
					
					sizesBuilder.append(length).append("\n");
					tokensBuilder.append(tokens).append("\n");
					bugsInfoBuilder.append("###BugInstance###\n##Info:");
					bugsInfoBuilder.append(violationType).append(":");
					bugsInfoBuilder.append(projectName).append(":");
					bugsInfoBuilder.append(fileName).append(":");
					bugsInfoBuilder.append(finalStartLine).append(":");
					bugsInfoBuilder.append(finalEndLine).append("\n");
					bugsInfoBuilder.append(sourceCode).append("\n");
					i ++;
				} else {
					j ++;
				}
			}

			FileHelper.outputToFile(OUTPUT_PATH + "sizes.list", sizesBuilder, true);
			FileHelper.outputToFile(OUTPUT_PATH + "tokens.list", tokensBuilder, true);
			FileHelper.outputToFile(OUTPUT_PATH + "bugsInfo.list", bugsInfoBuilder, true);
		}
	}

	private StringBuilder readSourceCode(File javaFile, int startLine, int endLine) {
		StringBuilder sourceCode = new StringBuilder("##Source_Code:\n");
		FileInputStream fis = null;
		Scanner scanner = null;
		
		try {
			fis = new FileInputStream(javaFile);
			scanner = new Scanner(fis);
			int counter = 0;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				counter ++;
				if (startLine <= counter && counter <= endLine) {
					sourceCode.append(line + "\n");
				}
				if (counter == endLine) break;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sourceCode;
	}

	private File locateSourceCodeFile(List<File> javaFiles, String fileName) {
		for (File javaFile : javaFiles) {
			if (javaFile.getPath().endsWith(fileName)) return javaFile;
		}
		return null;
	}

	public Map<String, Violation> readViolations(String violationFile) {
		return readViolations(new File(violationFile));
	}
	
	public Map<String, Violation> readViolations(File violationFile) {
		Map<String, Violation> violations = new HashMap<>();
		
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(violationFile);
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				//commons-math : DLS_DEAD_LOCAL_STORE : org/apache/commons/math4/dfp/Dfp.java : 2049 : 2049
				String line = scanner.nextLine();
				String[] elements = line.split(" : ");
				String projectName = elements[0];
				String alarmType = elements[1];
				String fileName = elements[2];
				int startLine = Integer.parseInt(elements[3]);
				int endLine = Integer.parseInt(elements[4]);
				
				Alarm alarm = new Alarm(alarmType, fileName, startLine, endLine);
				addAlarmToViolations(projectName, alarm, violations);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				scanner.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return violations;
	}

	private void addAlarmToViolations(String projectName, Alarm alarm, Map<String, Violation> violations) {
		Violation violation = null;
		if (violations.containsKey(projectName)) {
			violation = violations.get(projectName);
		} else {
			violation = new Violation(projectName);
			violations.put(projectName, violation);
		}
		
		List<Alarm> alarms = violation.getAlarms();
		alarms.add(alarm);
	}

}
