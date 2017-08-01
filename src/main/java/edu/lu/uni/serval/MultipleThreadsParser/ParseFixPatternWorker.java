package edu.lu.uni.serval.MultipleThreadsParser;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.FixPatternParser.SingleStatementParser;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseFixPatternWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(ParseFixPatternActor.class);
	
	private String editScriptsFilePath;
	private String patchesSourceCodeFilePath;
	private String editScriptSizesFilePath;
	private String buggyTreesFilePath;
	
	public ParseFixPatternWorker(String editScriptsFilePath, String patchesSourceCodeFilePath, String buggyTreesFilePath, String editScriptSizesFilePath) {
		this.editScriptsFilePath = editScriptsFilePath;
		this.patchesSourceCodeFilePath = patchesSourceCodeFilePath;
		this.editScriptSizesFilePath = editScriptSizesFilePath;
		this.buggyTreesFilePath = buggyTreesFilePath;
	}

	public static Props props(final String editScriptsFile, final String patchesSourceCodeFile, final String buggyTreesFilePath, final String editScriptSizesFilePath) {
		return Props.create(new Creator<ParseFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public ParseFixPatternWorker create() throws Exception {
				return new ParseFixPatternWorker(editScriptsFile, patchesSourceCodeFile, buggyTreesFilePath, editScriptSizesFilePath);
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
//			StringBuilder buggyTrees = new StringBuilder();
			StringBuilder tokens = new StringBuilder();

			int id = msg.getId();
			int counter = 0;
			for (MessageFile msgFile : files) {
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();
				SingleStatementParser parser = new SingleStatementParser();
				log.info("Start to parse file: " + revFile.getPath());
				parser.parseFixPatterns(prevFile, revFile, diffentryFile);
				editScripts.append(parser.getAstEditScripts());
				patchesSourceCode.append(parser.getPatchesSourceCode());
				sizes.append(parser.getSizes());
//				buggyTrees.append(parser.getBuggyTrees());
				tokens.append(parser.getTokensOfSourceCode());
				log.info("Finish of parsing file: " + revFile.getPath());
				counter ++;
				if (counter % 1000 == 0) {
					FileHelper.outputToFile(editScriptsFilePath + "edistScripts_" + id + ".list", editScripts, true);
					FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
					FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
//					FileHelper.outputToFile(buggyTreesFilePath + "buggyTrees_" + id + ".list", buggyTrees, true);
					FileHelper.outputToFile(buggyTreesFilePath + "tokens_" + id + ".list", tokens, true);
					editScripts.setLength(0);
					patchesSourceCode.setLength(0);
					sizes.setLength(0);
//					buggyTrees.setLength(0);
					tokens.setLength(0);
				}
			}
			
			FileHelper.outputToFile(editScriptsFilePath + "edistScripts_" + id + ".list", editScripts, true);
			FileHelper.outputToFile(patchesSourceCodeFilePath + "patches_" + id + ".list", patchesSourceCode, true);
			FileHelper.outputToFile(editScriptSizesFilePath + "sizes_" + id + ".list", sizes, true);
//			FileHelper.outputToFile(buggyTreesFilePath + "buggyTrees_" + id + ".list", buggyTrees, true);
			FileHelper.outputToFile(buggyTreesFilePath + "tokens_" + id + ".list", tokens, true);
			
			log.info("Worker #" + id + " finished the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}
}
