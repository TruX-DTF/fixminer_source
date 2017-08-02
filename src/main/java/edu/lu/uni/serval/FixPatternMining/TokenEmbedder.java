package edu.lu.uni.serval.FixPatternMining;

import java.io.File;
import java.io.IOException;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.deeplearner.Word2VecEncoder;

/**
 * Encode tokens of edit scripts with Word2Vec.
 * 
 * @author kui.liu
 *
 */
public class TokenEmbedder {

	/**
	 * Embed tokens for fix patterns mining.
	 */
	public void embedTokensOfEditScripts() {
		Word2VecEncoder encoder = new Word2VecEncoder();
		int windowSize = 2;
		encoder.setWindowSize(windowSize);
		try {
			File inputFile = new File(Configuration.SELECTED_EDITSCRIPTES_FILE);
			int minWordFrequency = 1;
			int layerSize = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN1;
			String outputFileName = Configuration.EMBEDDED_EDIT_SCRIPT_TOKENS;
			encoder.embedTokens(inputFile, minWordFrequency, layerSize, outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void embedTokensOfSourceCodeForSupervisedTesting() {
		Word2VecEncoder encoder = new Word2VecEncoder();
		int windowSize = 2;
		encoder.setWindowSize(windowSize);
		try {
			File inputFile = new File(Configuration.EMBEDDING_DATA_TOKENS2);
			int minWordFrequency = 1;
			int layerSize = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN2;
			String outputFileName = Configuration.EMBEDDED_ALL_TOKENS2;
			encoder.embedTokens(inputFile, minWordFrequency, layerSize, outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void embedTokensOfSourceCodeForUnsupervisedTesting() {
		Word2VecEncoder encoder = new Word2VecEncoder();
		int windowSize = 2;
		encoder.setWindowSize(windowSize);
		try {
			File inputFile = new File(Configuration.EMBEDDING_DATA_TOKENS1);
			int minWordFrequency = 1;
			int layerSize = Configuration.VECTOR_SIZE_OF_EMBEDED_TOKEN2;
			String outputFileName = Configuration.EMBEDDED_ALL_TOKENS1;
			encoder.embedTokens(inputFile, minWordFrequency, layerSize, outputFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
