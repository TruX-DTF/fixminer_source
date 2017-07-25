package edu.lu.uni.serval.MultipleThreadsParser;

import java.io.File;
import java.util.List;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.FixPatternParser.Parser;
import edu.lu.uni.serval.utils.FileHelper;

public class ParseFixPatternWorker extends UntypedActor {
	
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
				miner.mineFixPatterns(prevFile, revFile, diffentryFile);
				editScripts.append(miner.getAstEditScripts());
				patchesSourceCode.append(miner.getPatchesSourceCode());
				int size = miner.getMaxSize();
				if (size > maxSize) {
					maxSize = size;
				}
			}
			
			FileHelper.outputToFile(editScriptsFilePath + "edistScripts" + msg.getId() + "_MaxSize=" + maxSize + ".list", editScripts, false);
			FileHelper.outputToFile(patchesSourceCodeFilePath + "patches" + msg.getId() + ".list", patchesSourceCode, false);
			
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}
}
