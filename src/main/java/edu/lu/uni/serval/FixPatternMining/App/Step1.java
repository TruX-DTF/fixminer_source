package edu.lu.uni.serval.FixPatternMining.App;

import edu.lu.uni.serval.FixPatternMining.DataPrepare.DataPreparation;
import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

/**
 * Prepare data for tokens embedding of edit scripts.
 * 
 * Input data: parsed results of patches with GumTree.
 * 
 * @author kui.liu
 *
 */
public class Step1 {
	public static void main(String[] args) {
		String editScriptsFile = Configuration.EDITSCRIPT_SIZES_FILE;
		String patchesSourceCodeFile = Configuration.PATCH_SOURCECODE_FILE;
		String buggyTokensFile = Configuration.BUGGY_CODE_TOKENS_FILE;
		String editScriptSizesFile = Configuration.EDITSCRIPT_SIZES_FILE;
		FileHelper.deleteFile(editScriptsFile);
		FileHelper.deleteFile(patchesSourceCodeFile);
		FileHelper.deleteFile(buggyTokensFile);
		FileHelper.deleteFile(editScriptSizesFile);
		
		String selectedEditScripts = Configuration.SELECTED_EDITSCRIPTES_FILE;
		String selectedPatches = Configuration.SELECTED_PATCHES_SOURE_CODE_FILE;
		String selectedBuggyTokens = Configuration.SELECTED_BUGGY_TOKEN_FILE;
		FileHelper.deleteFile(selectedEditScripts);
		FileHelper.deleteFile(selectedPatches);
		FileHelper.deleteFile(selectedBuggyTokens);
		
		DataPreparation.prepareDataForTokenEmbedding();
	}
}
