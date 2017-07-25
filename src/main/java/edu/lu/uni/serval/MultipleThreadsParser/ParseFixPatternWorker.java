package edu.lu.uni.serval.MultipleThreadsParser;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.FixPatternParser.Parser;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseFixPatternWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(ParseFixPatternActor.class);
	
	private String editScriptsFilePath;
	private String patchesSourceCodeFilePath;
	
	public ParseFixPatternWorker(String editScriptsFilePath, String patchesSourceCodeFilePath) {
		this.editScriptsFilePath = editScriptsFilePath;
		this.patchesSourceCodeFilePath = patchesSourceCodeFilePath;
	}

	public static Props props(final String editScriptsFile, final String patchesSourceCodeFile) {
		return Props.create(new Creator<ParseFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public ParseFixPatternWorker create() throws Exception {
				return new ParseFixPatternWorker(editScriptsFile, patchesSourceCodeFile);
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
			int maxSize = 0;
			for (MessageFile msgFile : files) {
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();
				Parser miner = new Parser();
				log.info("Start to parse file: " + revFile.getPath());
				miner.parseFixPatterns(prevFile, revFile, diffentryFile);
				editScripts.append(miner.getAstEditScripts());
				patchesSourceCode.append(miner.getPatchesSourceCode());
				int size = miner.getMaxSize();
				if (size > maxSize) {
					maxSize = size;
				}
				log.info("Finish of parsing file: " + revFile.getPath());
			}
			
			int id = msg.getId();
			FileHelper.outputToFile(editScriptsFilePath + "edistScripts" + id + "_MaxSize=" + maxSize + ".list", editScripts, false);
			FileHelper.outputToFile(patchesSourceCodeFilePath + "patches" + id + ".list", patchesSourceCode, false);
			
			log.info("Worker #" + id + " finished the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}
}
