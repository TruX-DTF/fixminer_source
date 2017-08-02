package edu.lu.uni.serval.config;

public class Configuration {
	private static final String ROOT_PATH = "../";  // The root path of all output data.

	public static final int HUNK_SIZE = 7; // The limitation of source code lines of each DiffEntry, which will be selected as training data.
	public static final String BUGGY_TREE_SIGNAL = "BUGGY_TREE###"; // The starting signal of the tree of buggy source code .
	public static final String PATCH_SIGNAL = "PATCH###"; // The starting signal of each patch.
	
	// input path of GumTree. (i.e., Fix patterns parser)
	public static final String GUM_TREE_INPUT = ROOT_PATH + "GumTreeInput/";// Buggy version file VS. Fixing version file, (DiffEntry File)
	
	// the output path of GumTree results.
	private static final String GUM_TREE_OUTPUT = ROOT_PATH + "GumTreeResults/";
	public static final String EDITSCRIPTS_FILE_PATH = GUM_TREE_OUTPUT + "editScripts/";
	public static final String PATCH_SOURCECODE_FILE_PATH = GUM_TREE_OUTPUT + "sourceCode/";
	public static final String BUGGYTREE_FILE_PATH = GUM_TREE_OUTPUT + "buggyTrees/";
	public static final String BUGGY_CODE_TOKEN_FILE_PATH = GUM_TREE_OUTPUT + "tokens/";
	public static final String EDITSCRIPT_SIZES_FILE_PATH = GUM_TREE_OUTPUT + "editScriptSizes/";

	public static final String EDITSCRIPTS_FILE = GUM_TREE_OUTPUT + "editScripts.list";
	public static final String PATCH_SOURCECODE_FILE = GUM_TREE_OUTPUT + "patchSourceCode.list";
	public static final String BUGGYTREES_FILE = GUM_TREE_OUTPUT + "buggyTrees.list";
	public static final String BUGGY_CODY_TOKENS_FILE = GUM_TREE_OUTPUT + "tokens.list";
	public static final String EDITSCRIPT_SIZES_FILE = GUM_TREE_OUTPUT + "editScriptSizes.list";
	
	public static int MAX_EDIT_SCRIPT_VECTOR_SIZE = 0; // The max size of edit script vectors.
	public static int MAX_SOURCE_CODE_TOKEN_VECTOR_SIZE = 0; // The max size of all buggy source code token vectors.
	public static final int VECTOR_SIZE_OF_EMBEDED_TOKEN1 = 100; // tokens of edit scripts.
	public static final int VECTOR_SIZE_OF_EMBEDED_TOKEN2 = 200; // tokens of source code
	
	// the input path of fix patterns mining.
	private static final String MINING_INPUT = ROOT_PATH + "MiningInput/";
	// the input path of token embedding.
	public static final String EMBEDDING_INPUT = MINING_INPUT + "Embedding/";
	public static final String SELECTED_PATCHES_SOURE_CODE_FILE = EMBEDDING_INPUT + "patchSourceCode.list";// Selected patches.
	public static final String SELECTED_BUGGY_TREE_FILE = EMBEDDING_INPUT + "buggyTrees.list";
	public static final String SELECTED_BUGGY_TOKEN_FILE = EMBEDDING_INPUT + "tokens.list"; // Selected token vectors of buggy source code.
	public static final String SELECTED_EDITSCRIPTES_FILE = EMBEDDING_INPUT + "editScripts.list"; // Selected edit script vectors.
	// the input path of feature learning.
	public static final String FEATURE_LEARNING_INPUT = MINING_INPUT + "FeatureLearning/";
	public static final String EMBEDDED_EDIT_SCRIPT_TOKENS = FEATURE_LEARNING_INPUT + "embeddedEditScriptTokens.list"; // All embedded tokens of selected edit scripts.
	public static final String VECTORIED_EDIT_SCRIPTS = FEATURE_LEARNING_INPUT + "vectorizedEditScripts.csv"; // Embedded and vectorized edit script vectors.
	// the input path of clustering.
	public static final String EXTRACTED_FEATURES = MINING_INPUT + "ExtractedFeatures/"; // Extracted features of edit scripts.
	public static final String CLUSTER_INPUT = MINING_INPUT + "ClusteringInput/input.arff";

	// the output path of fix patterns mining.
	private static final String MINING_OUTPUT = ROOT_PATH + "MiningOutput/";
	public static final String CLUSTER_OUTPUT = MINING_OUTPUT + "ClusteringOutput/clusterResults.list";
	public static final String CLUSTERED_PATCHES_FILE = MINING_OUTPUT + "ClusteredPatches/";
	public static final String CLUSTERED_TOKENSS_FILE = MINING_OUTPUT + "ClusteredTokens/"; // Token vectors of buggy source code.
	
	// evaluation data
	public static final String TEST_INPUT = ROOT_PATH + "TestProjects/";
	public static final String TEST_LOCALIZATION_FILE = ROOT_PATH + "TestData/Localization.list"; // Positions of all test statements.
	public static final String TEST_DATA_FILE = ROOT_PATH + "TestData/TestStatements.list"; // Token vectors of all test statements.
	
	// data of unsupervised learning
	public static final String EMBEDDING_DATA_TOKENS1 = ROOT_PATH + "TestData/AllTokenVectorsForEvaluation.list";
	public static final String EMBEDDED_ALL_TOKENS1 = ROOT_PATH + "TestData/AllEmbeddedTokens.list";
	public static final String VECTORIED_ALL_SOURCE_CODE1 = ROOT_PATH + "TestData/AllVectorizedSourceCode.list";
	public static final String EXTRACTED_FEATURES_TESTING = ROOT_PATH + "TestDataExtractedFeatures/";
	
	// Data of supervised learning
	public static final String CLUSTERNUMBER_LABEL_MAP = ROOT_PATH + "TestData/clusterMappingLabel.list";
	public static final String EMBEDDING_DATA_TOKENS2 = ROOT_PATH + "TestData/AllTokenVectorsForSupervisedEvaluation.list";
	public static final String EMBEDDED_ALL_TOKENS2 = ROOT_PATH + "TestData/AllEmbeddedTokensForSuperVisedEvaluation.list";
	public static final String TRAINING_DATA = ROOT_PATH + "TestData/TrainingData.csv"; // Training data of supervised learning
	public static final String TESTING_DATA = ROOT_PATH + "TestData/TestingData.csv";   // testing data of supervised learning
	
}
