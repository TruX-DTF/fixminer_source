package edu.lu.uni.serval.config;

public class Configuration {
	private static final String ROOT_PATH = "../";

	public static final String BUGGY_TREE_TOKEN = "BUGGY_TREE###";
	public static final String PATCH_TOKEN = "PATCH###";
	
	public static int MAXZ_SIZE = 0;
	public static final int VECTOR_SIZE_OF_EMBEDED_TOKEN = 100;
	
	// input path of GumTree. (i.e., Fix patterns parser)
	public static final String GUM_TREE_INPUT = ROOT_PATH + "GumTreeInput/";
	
	// the output path of GumTree results.
	private static final String GUM_TREE_OUTPUT = ROOT_PATH + "GumTreeResults/";
	public static final String EDITSCRIPTS_FILE_PATH = GUM_TREE_OUTPUT + "editScripts/";
	public static final String PATCH_SOURCECODE_FILE_PATH = GUM_TREE_OUTPUT + "sourceCode/";
	public static final String BUGGYTREE_FILE_PATH = GUM_TREE_OUTPUT + "buggyTrees/";
	public static final String EDITSCRIPT_SIZES_FILE_PATH = GUM_TREE_OUTPUT + "editScriptSizes/";

	public static final String EDITSCRIPTS_FILE = GUM_TREE_OUTPUT + "editScripts.list";
	public static final String PATCH_SOURCECODE_FILE = GUM_TREE_OUTPUT + "patchSourceCode.list";
	public static final String BUGGYTREES_FILE = GUM_TREE_OUTPUT + "buggyTrees.list";
	public static final String EDITSCRIPT_SIZES_FILE = GUM_TREE_OUTPUT + "editScriptSizes.list";
	
	// the input path of fix patterns mining.
	private static final String MINING_INPUT = ROOT_PATH + "MiningInput/";
	// the input path of token embedding.
	public static final String EMBEDDING_INPUT = MINING_INPUT + "Embedding/";
	public static final String SELECTED_PATCHES_SOURE_CODE_FILE = EMBEDDING_INPUT + "patchSourceCode.list";
	public static final String SELECTED_BUGGY_TREE_FILE = EMBEDDING_INPUT + "buggyTrees.list";
	public static final String SELECTED_EDITSCRIPTES_FILE = EMBEDDING_INPUT + "editScripts.list";
	// the input path of feature learning.
	public static final String FEATURE_LEARNING_INPUT = MINING_INPUT + "FeatureLearning/";
	public static final String EMBEDDED_EDIT_SCRIPT_TOKENS = FEATURE_LEARNING_INPUT + "embeddedEditScriptTokens.list";
	public static final String VECTORIED_EDIT_SCRIPTS = FEATURE_LEARNING_INPUT + "vectorizedEditScripts.csv";
	// the input path of clustering.
	public static final String EXTRACTED_FEATURES = MINING_INPUT + "ExtractedFeatures/";
	public static final String CLUSTER_INPUT = MINING_INPUT + "ClusteringInput/input.arff";

	// the output path of fix patterns mining.
	private static final String MINING_OUTPUT = ROOT_PATH + "MiningOutput/";
	public static final String CLUSTER_OUTPUT = MINING_OUTPUT + "ClusteringOutput/clusterResults.list";
}
