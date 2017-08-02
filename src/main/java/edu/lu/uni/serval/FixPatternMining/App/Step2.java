package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.TokenEmbedder;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Embed tokens of all selected edit scripts.
 * 
 * Input data: all tokens of selected edit scripts.
 * 
 * @author kui.liu
 *
 */
public class Step2 {
	
	public static void main(String[] args) {
		String outputFileName = Configuration.EMBEDDED_EDIT_SCRIPT_TOKENS;
		FileHelper.deleteFile(outputFileName);
		
		TokenEmbedder embedder = new TokenEmbedder();
		embedder.embedTokensOfEditScripts();
	}

}
