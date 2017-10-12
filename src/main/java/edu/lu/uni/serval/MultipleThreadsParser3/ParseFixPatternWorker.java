package edu.lu.uni.serval.MultipleThreadsParser3;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Creator;
import edu.lu.uni.serval.MultipleThreadsParser.MessageFile;
import edu.lu.uni.serval.MultipleThreadsParser.WorkMessage;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;
import edu.lu.uni.serval.violation.code.parser.ViolationCodeParser;

public class ParseFixPatternWorker extends UntypedActor {
	private static Logger log = LoggerFactory.getLogger(ParseFixPatternActor.class);
	
	private String sourceCodeFilesPath;
	
	public ParseFixPatternWorker(String sourceCodeFilesPath) {
		this.sourceCodeFilesPath = sourceCodeFilesPath;
	}

	public static Props props(final String sourceCodeFilesPath) {
		return Props.create(new Creator<ParseFixPatternWorker>() {

			private static final long serialVersionUID = -7615153844097275009L;

			@Override
			public ParseFixPatternWorker create() throws Exception {
				return new ParseFixPatternWorker(sourceCodeFilesPath);
			}
			
		});
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof WorkMessage) {
			WorkMessage msg = (WorkMessage) message;
			List<MessageFile> files = msg.getMsgFiles();
			StringBuilder sourceCode = new StringBuilder();
			StringBuilder sizes = new StringBuilder();
			StringBuilder tokens = new StringBuilder();
			
			List<String> violationTypes = readTypes();

			int id = msg.getId();
			int counter = 0;
			
			for (MessageFile msgFile : files) {
				File prevFile = msgFile.getPrevFile();
				File positionFile = msgFile.getPositionFile();
				if (prevFile.getName().toLowerCase().contains("test")) {
					continue;
				}
				ViolationCodeParser parser =  new ViolationCodeParser();
				parser.parse(prevFile, positionFile);
				parser.setTypes(violationTypes);
				
				String sourceCodeStr = parser.sourceCode;
				if ("".equals(sourceCodeStr)) {
				} else {
					sourceCode.append(sourceCodeStr);
					sizes.append(parser.sizes);
					tokens.append(parser.tokens);
					
					counter ++;
					if (counter % 100 == 0) {
						FileHelper.outputToFile(sourceCodeFilesPath + "SourceCode/worker_" + id + ".list", sourceCode, true);
						FileHelper.outputToFile(sourceCodeFilesPath + "Sizes/worker_" + id + ".list", sizes, true);
						FileHelper.outputToFile(sourceCodeFilesPath + "Tokens/worker_" + id + ".list", tokens, true);
						sourceCode.setLength(0);
						sizes.setLength(0);
						tokens.setLength(0);
					}
				}
			}
			
			if (sizes.length() > 0) {
				FileHelper.outputToFile(sourceCodeFilesPath + "SourceCode/worker_" + id + ".list", sourceCode, true);
				FileHelper.outputToFile(sourceCodeFilesPath + "Sizes/worker_" + id + ".list", sizes, true);
				FileHelper.outputToFile(sourceCodeFilesPath + "Tokens/worker_" + id + ".list", tokens, true);
				sourceCode.setLength(0);
				sizes.setLength(0);
				tokens.setLength(0);
			}

			log.info("Worker #" + id +"Finish of parsing " + counter + " files...");
			log.info("Worker #" + id + " finished the work...");
			this.getSender().tell("STOP", getSelf());
		} else {
			unhandled(message);
		}
	}

	private List<String> readTypes() {
		String fileName = Configuration.ROOT_PATH + "fixedViolations/types.list";
		String content = FileHelper.readFile(fileName);
		List<String> types = new ArrayList<>();
		BufferedReader reader = new BufferedReader(new StringReader(content));
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				types.add(line);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		System.err.println(types.size());
		return types;
	}

}
