package edu.lu.uni.serval.FixPatternParser.violations;

import static java.lang.System.err;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.lu.uni.serval.FixPatternParser.RunnableParser;
import edu.lu.uni.serval.MultipleThreadsParser.MessageFile;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class TestHunkParser {
	

	public static void main(String[] args) {
		// input data
		final List<MessageFile> msgFiles = getMessageFiles("GumTreeInput/");
		System.out.println(msgFiles.size());

		// output path
		final String editScriptsFilePath = "GumTreeResults/editScripts.list";
		final String patchesSourceCodeFilePath = "GumTreeResults/patchSourceCode.list";
		final String buggyTokensFilePath = "GumTreeResults/tokens.list";
		final String editScriptSizesFilePath = "GumTreeResults/editScriptSizes.list";
		final String alarmTypesFilePath = "GumTreeResults/alarmTypes.list";

		FileHelper.deleteDirectory(editScriptsFilePath);
		FileHelper.deleteDirectory(patchesSourceCodeFilePath);
		FileHelper.deleteDirectory(buggyTokensFilePath);
		FileHelper.deleteDirectory(editScriptSizesFilePath);
		FileHelper.deleteDirectory(alarmTypesFilePath);
		
		StringBuilder astEditScripts = new StringBuilder();
		StringBuilder tokens = new StringBuilder();
		StringBuilder sizes = new StringBuilder();
		StringBuilder patches = new StringBuilder();
		StringBuilder alarmTypes = new StringBuilder();
		
		int a = 0;
		int counter = 0;
		for (MessageFile msgFile : msgFiles) {
			FixedViolationHunkParser parser = new FixedViolationHunkParser();
			parser.counter = counter;
			parser.setPositionFile(msgFile.getPositionFile());
			
			final ExecutorService executor = Executors.newSingleThreadExecutor();
			// schedule the work
			final Future<?> future = executor.submit(new RunnableParser(msgFile.getPrevFile(), 
					msgFile.getRevFile(), msgFile.getDiffEntryFile(), parser));
			try {
				// where we wait for task to complete
//				future.get(Configuration.SECONDS_TO_WAIT, TimeUnit.SECONDS);
				future.get(20L, TimeUnit.SECONDS);
				String editScripts = parser.getAstEditScripts();
				if (!editScripts.equals("")) {
					astEditScripts.append(editScripts);
					tokens.append(parser.getTokensOfSourceCode());
					sizes.append(parser.getSizes());
					patches.append(parser.getPatchesSourceCode());
					alarmTypes.append(parser.getAlarmTypes());
					counter = parser.counter;

					a ++;
					if (a % 100 == 0) {
						FileHelper.outputToFile(editScriptsFilePath, astEditScripts, true);
						FileHelper.outputToFile(buggyTokensFilePath, tokens, true);
						FileHelper.outputToFile(editScriptSizesFilePath, sizes, true);
						FileHelper.outputToFile(patchesSourceCodeFilePath, patches, true);
						FileHelper.outputToFile(alarmTypesFilePath, alarmTypes, true);
						astEditScripts.setLength(0);
						tokens.setLength(0);
						sizes.setLength(0);
						patches.setLength(0);
						alarmTypes.setLength(0);
						System.out.println("Finish of parsing " + a + " files......");
					}
				}
			} catch (TimeoutException e) {
				err.println("task timed out");
				future.cancel(true /* mayInterruptIfRunning */ );
			} catch (InterruptedException e) {
				err.println("task interrupted");
			} catch (ExecutionException e) {
				err.println("task aborted");
			} finally {
				executor.shutdownNow();
			}
		}
		
		FileHelper.outputToFile(editScriptsFilePath, astEditScripts, true);
		FileHelper.outputToFile(buggyTokensFilePath, tokens, true);
		FileHelper.outputToFile(editScriptSizesFilePath, sizes, true);
		FileHelper.outputToFile(patchesSourceCodeFilePath, patches, true);
		FileHelper.outputToFile(alarmTypesFilePath, alarmTypes, true);
		astEditScripts.setLength(0);
		tokens.setLength(0);
		sizes.setLength(0);
		patches.setLength(0);
		alarmTypes.setLength(0);
		System.out.println(a);
		
//		classifyByAlarmTypes();
	}
	

	private static List<MessageFile> getMessageFiles(String gumTreeInput) {
		String inputPath = gumTreeInput; // prevFiles  revFiles diffentryFile positionsFile
		File revFilesPath = new File(inputPath + "revFiles/");
		File[] revFiles = revFilesPath.listFiles();   // project folders
		List<MessageFile> msgFiles = new ArrayList<>();
		
		for (File revFile : revFiles) {
			if (revFile.getName().endsWith(".java")) {
				String fileName = revFile.getName();
				File prevFile = new File(gumTreeInput + "prevFiles/prev_" + fileName);// previous file
				fileName = fileName.replace(".java", ".txt");
				File diffentryFile = new File(gumTreeInput + "diffentries/" + fileName); // DiffEntry file
				File positionFile = new File(gumTreeInput + "positions/" + fileName); // position file
				MessageFile msgFile = new MessageFile(revFile, prevFile, diffentryFile);
				msgFile.setPositionFile(positionFile);
				msgFiles.add(msgFile);
			}
		}
		
		return msgFiles;
	}
	
	public static void classifyByAlarmTypes() {

		final String alarmTypesFilePath = Configuration.ALARM_TYPES_FILE;
		List<String> alarmTypes = readStringList(alarmTypesFilePath);
		//edit scripts, sizes of edit scripts, buggy tokens, patches.
		classifyByAlarmTypes(alarmTypes, Configuration.EDITSCRIPT_SIZES_FILE);
		classifyByAlarmTypes(alarmTypes, Configuration.EDITSCRIPTS_FILE);
		classifyByAlarmTypes(alarmTypes, Configuration.BUGGY_CODE_TOKENS_FILE);
		classifyByAlarmTypes2(alarmTypes, Configuration.PATCH_SOURCECODE_FILE);
	}

	private static void classifyByAlarmTypes(List<String> alarmTypes, String file) {
		Map<String, StringBuilder> buildersMap = new HashMap<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(file);
			scanner = new Scanner(fis);
			int counter = 0;
			while (scanner.hasNextLine()) {
				String alarmType = alarmTypes.get(counter);
				StringBuilder builder = getBuilder(buildersMap, alarmType);
				builder.append(scanner.nextLine() + "\n");
				counter ++;
				if (counter % 1000 == 0) {
					outputBuilders(buildersMap, file);
				}
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
		outputBuilders(buildersMap, file);
	}
	
	private static void classifyByAlarmTypes2(List<String> alarmTypes, String patchSourcecodeFile) {
		Map<String, StringBuilder> buildersMap = new HashMap<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(patchSourcecodeFile);
			scanner = new Scanner(fis);
			int counter = 0;
			String singlePatch = "";
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (Configuration.PATCH_SIGNAL.equals(line)) {
					if (!"".equals(singlePatch)) {
						String alarmType = alarmTypes.get(counter);
						StringBuilder builder = getBuilder(buildersMap, alarmType);
						builder.append(scanner.nextLine() + "\n");
						counter ++;
						if (counter % 2000 == 0) {
							outputBuilders(buildersMap, patchSourcecodeFile);
						}
					}
					singlePatch = line + "\n";
				}
				singlePatch += line + "\n";
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
		
		outputBuilders(buildersMap, patchSourcecodeFile);
	}

	private static void outputBuilders(Map<String, StringBuilder> map, String fileNameStr) {
		File file = new File(fileNameStr);
		String fileName = file.getName();
		String parentPath = file.getParent();
		for (Map.Entry<String, StringBuilder> entry : map.entrySet()) {
			String alarmType = entry.getKey();
			StringBuilder builder = entry.getValue();
			
			FileHelper.outputToFile(parentPath + "/" + alarmType + "/" + fileName, builder, true);
			
			builder.setLength(0);
			entry.setValue(builder);
		}
	}

	public static List<String> readStringList(String inputFile) {
		List<String> list = new ArrayList<>();
		FileInputStream fis = null;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(inputFile);
			scanner = new Scanner(fis);
			while(scanner.hasNextLine()) {
				list.add(scanner.nextLine());
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
		return list;
	}

	private static StringBuilder getBuilder(Map<String, StringBuilder> buildersMap, String alarmType) {
		if (buildersMap.containsKey(alarmType)) {
			return buildersMap.get(alarmType);
		} else {
			StringBuilder builder = new StringBuilder();
			buildersMap.put(alarmType, builder);
			return builder;
		}
	}
}
