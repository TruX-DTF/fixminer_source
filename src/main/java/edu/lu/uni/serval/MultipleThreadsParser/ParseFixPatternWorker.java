package edu.lu.uni.serval.MultipleThreadsParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
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
	
	public ParseFixPatternWorker(String editScriptsFilePath, String patchesSourceCodeFilePath, 
			String buggyTokensFilePath, String editScriptSizesFilePath) {
		this.editScriptsFilePath = editScriptsFilePath;
		this.patchesSourceCodeFilePath = patchesSourceCodeFilePath;
		this.editScriptSizesFilePath = editScriptSizesFilePath;
		this.buggyTokensFilePath = buggyTokensFilePath;
	}

	public static Props props(final String editScriptsFile, final String patchesSourceCodeFile, final String buggyTokensFilePath, 
			final String editScriptSizesFilePath) {
		return Props.create(new Creator<ParseFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public ParseFixPatternWorker create() throws Exception {
				return new ParseFixPatternWorker(editScriptsFile, patchesSourceCodeFile, 
						buggyTokensFilePath, editScriptSizesFilePath);
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
			StringBuilder testingInfo = new StringBuilder();

			int id = msg.getId();
			int counter = 0;
			
			int nullGumTreeResults = 0;
			int noSourceCodeChanges = 0;
			int noStatementChanges = 0;
			int nullDiffEntry = 0;
			int nullMappingGumTreeResults = 0;
			int pureDeletion = 0;
			int largeHunk = 0;
			int nullSourceCode = 0;
			int testInfos = 0;
			int timeouts = 0;
			StringBuilder builder = new StringBuilder();

			for (MessageFile msgFile : files) {
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();
				File positionFile = msgFile.getPositionFile();
				if (revFile.getName().toLowerCase().contains("test")) {
					continue;
				}
				FixedViolationHunkParser parser =  new FixedViolationHunkParser();
				
				final ExecutorService executor = Executors.newSingleThreadExecutor();
				// schedule the work
				final Future<?> future = executor.submit(new RunnableParser(prevFile, revFile, diffentryFile, parser));
				try {
					// wait for task to complete
					future.get(Configuration.SECONDS_TO_WAIT, TimeUnit.SECONDS);

					nullDiffEntry += parser.nullMatchedDiffEntry;
					nullMappingGumTreeResults += parser.nullMappingGumTreeResult;
					pureDeletion += parser.pureDeletions;
					largeHunk += parser.largeHunk;
					nullSourceCode += parser.nullSourceCode;
					testInfos += parser.testInfos;
					testingInfo.append(parser.testingInfo);
					builder.append(parser.unfixedViolations);
					
					String editScript = parser.getAstEditScripts();
					if ("".equals(editScript)) {
						if (parser.resultType == 1) {
							nullGumTreeResults += countAlarms(positionFile, "");
						} else if (parser.resultType == 2) {
							noSourceCodeChanges += countAlarms(positionFile, "");
						} else if (parser.resultType == 3) {
							noStatementChanges += countAlarms(positionFile, "");
						}
					} else {
						editScripts.append(editScript);
						patchesSourceCode.append(parser.getPatchesSourceCode());
						sizes.append(parser.getSizes());
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
							log.info("Worker #" + id +" finialized parsing " + counter + " files...");
							FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
							testingInfo.setLength(0);
						}
					}
				} catch (TimeoutException e) {
					future.cancel(true);
					timeouts += countAlarms(positionFile, "#Timeout:");
					System.err.println("#Timeout: " + revFile.getName());
				} catch (InterruptedException e) {
					timeouts += countAlarms(positionFile, "#TimeInterrupted:");
					System.err.println("#TimeInterrupted: " + revFile.getName());
					e.printStackTrace();
				} catch (ExecutionException e) {
					timeouts += countAlarms(positionFile, "#TimeAborted:");
					System.err.println("#TimeAborted: " + revFile.getName());
					e.printStackTrace();
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
				
				FileHelper.outputToFile("OUTPUT/testingInfo_" + id + ".list", testingInfo, true);
				testingInfo.setLength(0);
			}
			String statistic = "\nNullGumTreeResults: " + nullGumTreeResults + "\nNoSourceCodeChanges: " + noSourceCodeChanges + 
					"\nNoStatementChanges: " + noStatementChanges + "\nNullDiffEntry: " + nullDiffEntry + "\nNullMatchedGumTreeResults: " + nullMappingGumTreeResults +
					"\nPureDeletion: " + pureDeletion + "\nLargeHunk: " + largeHunk + "\nNullSourceCode: " + nullSourceCode + 
					"\nTestingInfo: " + testInfos + "\nTimeout: " + timeouts;
			FileHelper.outputToFile("OUTPUT/statistic_" + id + ".list", statistic, false);
			FileHelper.outputToFile("OUTPUT/UnfixedV_" + id + ".list", builder, false);

			log.info("Worker #" + id +"finialized parsing " + counter + " files...");
			log.info("Worker #" + id + " finialized the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}

	private int countAlarms(File positionFile, String type) {//, List<Violation> uselessViolations) {
		int counter = 0;
		String content = FileHelper.readFile(positionFile);
		BufferedReader reader = new BufferedReader(new StringReader(content));
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				String[] elements = line.split(":");
				Violation v = new Violation(Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), elements[0]);
				String fileName = positionFile.getName().replace(".txt", ".java");
				v.setFileName(fileName);
				counter ++;
				if (!"".equals(type)) {
					System.err.println(type + fileName + ":" + elements[1] + ":" + elements[2] + ":" + elements[0]);
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
