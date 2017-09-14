package edu.lu.uni.serval.MultipleThreadsParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.FixPatternParser.RunnableParser;
import edu.lu.uni.serval.FixPatternParser.violations.FixedViolationHunkParser;
import edu.lu.uni.serval.FixPatternParser.violations.Violation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseFixPatternWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(ParseFixPatternActor.class);
	
	private String editScriptsFilePath;
	private String patchesSourceCodeFilePath;
	private String editScriptSizesFilePath;
	private String buggyTokensFilePath;
	private String alarmTypesFilePath;
	
	public ParseFixPatternWorker(String editScriptsFilePath, String patchesSourceCodeFilePath, 
			String buggyTokensFilePath, String editScriptSizesFilePath, String alarmTypesFilePath) {
		this.editScriptsFilePath = editScriptsFilePath;
		this.patchesSourceCodeFilePath = patchesSourceCodeFilePath;
		this.editScriptSizesFilePath = editScriptSizesFilePath;
		this.buggyTokensFilePath = buggyTokensFilePath;
		this.alarmTypesFilePath = alarmTypesFilePath;
	}

	public static Props props(final String editScriptsFile, final String patchesSourceCodeFile, final String buggyTokensFilePath, 
			final String editScriptSizesFilePath, final String alarmTypesFilePath) {
		return Props.create(new Creator<ParseFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public ParseFixPatternWorker create() throws Exception {
				return new ParseFixPatternWorker(editScriptsFile, patchesSourceCodeFile, 
						buggyTokensFilePath, editScriptSizesFilePath, alarmTypesFilePath);
			}
			
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			WorkMessage msg = (WorkMessage) message;
			List<MessageFile> files = msg.getMsgFiles();
			StringBuilder editScripts = new StringBuilder();
			StringBuilder patchesSourceCode = new StringBuilder();
			StringBuilder sizes = new StringBuilder();
			StringBuilder tokens = new StringBuilder();
			StringBuilder alarmTypes = new StringBuilder();
			StringBuilder testingInfo = new StringBuilder();

			int id = msg.getId();
			int counter = 0;
			
			int testAlarms = 0;
			int nullGumTreeResults = 0;
			int nullMappingGumTreeResults = 0;
			int noSourceCodeChanges = 0;
			int noStatementChanges = 0;
			int pureDeletion = 0;
			int expNums = 0;
			int largeHunk = 0;
			int nullSourceCode = 0;
			int illegalV = 0;
			
			for (MessageFile msgFile : files) {
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();
				File positionFile = msgFile.getPositionFile();
				if (revFile.getName().toLowerCase().contains("test")) {
					testAlarms += countAlarms(positionFile, "#TestViolation:");
//					continue;
				}
//				Parser parser = null;
//				if (containsAlarmTypes || positionFile != null) {
//					parser = new FixedViolationHunkParser(positionFile);
//					containsAlarmTypes = true;
//				} else {
//					parser = new CommitPatchSingleStatementParser();
//				}
				FixedViolationHunkParser parser =  new FixedViolationHunkParser(positionFile);
				// Read violations with Null_Violation_Hunk or Illegal_Line_Position
				List<Violation> uselessViolations = readUselessViolations("logs/FixedViolationCodeParseResults.log");
				parser.setUselessViolations(uselessViolations);
				
				final ExecutorService executor = Executors.newSingleThreadExecutor();
				// schedule the work
				final Future<?> future = executor.submit(new RunnableParser(prevFile, revFile, diffentryFile, parser));
				try {
					// wait for task to complete
					future.get(Configuration.SECONDS_TO_WAIT, TimeUnit.SECONDS);
					
					nullMappingGumTreeResults += parser.nullMappingGumTreeResult;
					pureDeletion += parser.pureDeletions;
					largeHunk += parser.largeHunk;
					nullSourceCode += parser.nullSourceCode;
					testingInfo.append(parser.testingInfo);
					
					String editScript = parser.getAstEditScripts();
					if ("".equals(editScript)) {
						if (parser.resultType == 1) {
							nullGumTreeResults += countAlarms(positionFile, "");
//							System.err.println("#NullGumTreeResult:" + revFile.getName());
						} else if (parser.resultType == 2) {
							noSourceCodeChanges += countAlarms(positionFile, "");
						} else if (parser.resultType == 3) {
							noStatementChanges += countAlarms(positionFile, "");
						} else if (parser.resultType == 4) {
							illegalV += countAlarms(positionFile, "");
						}
					} else {
						editScripts.append(editScript);
						patchesSourceCode.append(parser.getPatchesSourceCode());
						sizes.append(parser.getSizes());
//						tokens.append(parser.getTokensOfSourceCode());
						alarmTypes.append(parser.getAlarmTypes());
						tokens.append(parser.getTokensOfSourceCode());
						
						counter ++;
						if (counter % 100 == 0) {
							FileHelper.outputToFile(editScriptsFilePath + "edistScripts_" + id + ".list", editScripts, true);
							FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
							FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
							FileHelper.outputToFile(buggyTokensFilePath + "tokens_" + id + ".list", tokens, true);
							editScripts.setLength(0);
							patchesSourceCode.setLength(0);
							sizes.setLength(0);
							tokens.setLength(0);
							FileHelper.outputToFile(alarmTypesFilePath + "alarmTypes_" + id + ".list", alarmTypes, true);
							alarmTypes.setLength(0);
							log.info("Worker #" + id +"Finish of parsing " + counter + " files...");
							FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
							testingInfo.setLength(0);
						}
					}
				} catch (TimeoutException e) {
//					err.println("task timed out");
					future.cancel(true);
					expNums += countAlarms(positionFile, "#Timeout:");
//					System.err.println("#Timeout: " + revFile.getName());
				} catch (InterruptedException e) {
					expNums += countAlarms(positionFile, "#TimeInterrupted:");
//					err.println("task interrupted");
//					System.err.println("#TimeInterrupted: " + revFile.getName());
				} catch (ExecutionException e) {
					expNums += countAlarms(positionFile, "#TimeAborted:");
//					err.println("task aborted");
//					System.err.println("#TimeAborted: " + revFile.getName());
				} finally {
					executor.shutdownNow();
				}
			}
			
			if (sizes.length() > 0) {
				FileHelper.outputToFile(editScriptsFilePath + "edistScripts_" + id + ".list", editScripts, true);
				FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
				FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
				FileHelper.outputToFile(buggyTokensFilePath + "tokens_" + id + ".list", tokens, true);
				editScripts.setLength(0);
				patchesSourceCode.setLength(0);
				sizes.setLength(0);
				tokens.setLength(0);
				
				FileHelper.outputToFile(alarmTypesFilePath + "alarmTypes_" + id + ".list", alarmTypes, true);
				alarmTypes.setLength(0);
				FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
				testingInfo.setLength(0);
			}
			String statistic = "testAlarms: " + testAlarms + "\nnullGumTreeResults: " + nullGumTreeResults + "\nnullMappingGumTreeResults: " + nullMappingGumTreeResults +
					"\nnoSourceCodeChanges: " + noSourceCodeChanges + "\npureDeletion: " + pureDeletion + "\nTimeout: " + expNums +
					"\nlargeHunk: " + largeHunk + "\nnullSourceCode: " + nullSourceCode + "\nnoStatementChanges: " + noStatementChanges + "\nIllegalV: " + illegalV;
			FileHelper.outputToFile("OUTPUT/statistic_" + id + ".list", statistic, false);

			log.info("Worker #" + id +"Finish of parsing " + counter + " files...");
			log.info("Worker #" + id + " finished the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}

	private List<Violation> readUselessViolations(String filePath) {
		List<Violation> uselessViolations = new ArrayList<>();
		
		String content = FileHelper.readFile(filePath);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("#")) {
					String[] elements = line.split(":");
					String fileName = elements[1];
					int startLine = Integer.parseInt(elements[2]);
					int endLine = Integer.parseInt(elements[3]);
					String violationType = elements[4];
					
					Violation violation = new Violation(startLine, endLine, violationType);
					violation.setFileName(fileName);
					uselessViolations.add(violation);
				}
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
		return uselessViolations;
	}

	private int countAlarms(File positionFile, String type) {
		int counter = 0;
		String content = FileHelper.readFile(positionFile);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				counter ++;
				if (!"".equals(type)) {
					String[] elements = line.split(":");
					System.err.println(type + positionFile.getName().replaceAll("#", "/").replace(".txt", ".java") + ":" + elements[1] + ":" + elements[2] + ":" + elements[0]);
				}
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
		return counter;
	}
}
