package edu.lu.uni.serval.FixPatternMiner;

import java.io.File;
import java.util.List;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.utils.FileHelper;

public class MineFixPatternWorker extends UntypedActor {
	
	private String editScriptsFilePath;
	private String patchesSourceCodeFilePath;
	
	public MineFixPatternWorker(String editScriptsFilePath, String patchesSourceCodeFilePath) {
		this.editScriptsFilePath = editScriptsFilePath;
		this.patchesSourceCodeFilePath = patchesSourceCodeFilePath;
	}

	public static Props props(final String editScriptsFile, final String patchesSourceCodeFile) {
		return Props.create(new Creator<MineFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public MineFixPatternWorker create() throws Exception {
				return new MineFixPatternWorker(editScriptsFile, patchesSourceCodeFile);
			}
			
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof List<?>) {
			List<?> files = (List<?>) message;
			StringBuilder editScripts = new StringBuilder();
			StringBuilder patchesSourceCode = new StringBuilder();
			for (Object obj : files) {
				MessageFile msgFile = (MessageFile) obj;
				File revFile = msgFile.getRevFile();
				File prevFile = msgFile.getPrevFile();
				File diffentryFile = msgFile.getDiffEntryFile();
				if (!prevFile.exists()) {
					System.out.println(prevFile.getPath());
					continue;
				}
				Miner miner = new Miner();
				miner.mineFixPatterns(prevFile, revFile, diffentryFile);
				editScripts.append(miner.getAstEditScripts());
				patchesSourceCode.append(miner.getPatchesSourceCode());
			}
			
			List<File> subFiles1 = FileHelper.getAllFilesInCurrentDiectory(editScriptsFilePath, ".list");
			List<File> subFiles2 = FileHelper.getAllFilesInCurrentDiectory(patchesSourceCodeFilePath, ".list");
			FileHelper.outputToFile(editScriptsFilePath + "edistScripts" + subFiles1.size() + ".list", editScripts, false);
			FileHelper.outputToFile(patchesSourceCodeFilePath + "patches" + subFiles2.size() + ".list", patchesSourceCode, false);
			
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}
}
