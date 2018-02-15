package edu.lu.uni.serval.config;

public class Configuration {
	
	public static final long SECONDS_TO_WAIT = 900L;

//	public static String ROOT_PATH = "/Volumes/data/bugStudy/dataset/"; // The root path of all output data.
	public static String ROOT_PATH = "/Users/anilkoyuncu/bugStudy/dataset/"; // The root path of all output data.

	public static int HUNK_SIZE = 10; // The limitation of source code lines of each DiffEntry, which will be selected as training data.
	public static final String BUGGY_TREE_SIGNAL = "BUGGY_TREE###"; // The starting signal of the tree of buggy source code .
	public static final String PATCH_SIGNAL = "PATCH###"; // Th starting signal of each patch.
	
	// input path of GumTree. (i.e., Fix patterns parser)
	public static final String GUM_TREE_INPUT = ROOT_PATH + "GumTreeInput/";// Buggy version file VS. Fixing version file, (DiffEntry File)
	

	// the output path of GumTree results.
	private static final String GUM_TREE_OUTPUT = ROOT_PATH + "GumTreeResults/";
	public static final String EDITSCRIPTS_FILE_PATH = GUM_TREE_OUTPUT + "editScripts/";
	public static final String PATCH_SOURCECODE_FILE_PATH = GUM_TREE_OUTPUT + "sourceCode/";
	public static final String BUGGYTREE_FILE_PATH = GUM_TREE_OUTPUT + "buggyTrees/";
	public static final String BUGGY_CODE_TOKEN_FILE_PATH = GUM_TREE_OUTPUT + "tokens/";
	public static final String EDITSCRIPT_SIZES_FILE_PATH = GUM_TREE_OUTPUT + "editScriptSizes/";
	public static final String ALARM_TYPES_FILE_PATH = GUM_TREE_OUTPUT + "alarmTypes/";

	public static final String EDITSCRIPTS_FILE = GUM_TREE_OUTPUT + "editScripts.list";
	public static final String PATCH_SOURCECODE_FILE = GUM_TREE_OUTPUT + "patchSourceCode.list";
	public static final String BUGGYTREES_FILE = GUM_TREE_OUTPUT + "buggyTrees.list";
	public static final String BUGGY_CODE_TOKENS_FILE = GUM_TREE_OUTPUT + "tokens.list";
	public static final String EDITSCRIPT_SIZES_FILE = GUM_TREE_OUTPUT + "editScriptSizes.csv";
	public static final String ALARM_TYPES_FILE = GUM_TREE_OUTPUT + "alarmTypes.list";
	
	public static final int VECTOR_SIZE_OF_EMBEDED_TOKEN1 = 100; // tokens of edit scripts.
	public static final int VECTOR_SIZE_OF_EMBEDED_TOKEN2 = 200; // tokens of source code
	
	// the input path of fix patterns mining.
	private static final String MINING_INPUT = ROOT_PATH + "MiningInput/";
	public static final String MAX_TOKEN_VECTORS_SIZE_OF_EDIT_SCRIPTS = MINING_INPUT + "/MaxTokenVectorSizeOfEditScripts.list"; // The max size of edit scripts: upper limitation of max size.
	public static final String MAX_TOKEN_VECTORS_SIZE_OF_SOURCE_CODE = MINING_INPUT + "/MaxTokenVectorSizeOfBuggySourceCode.list"; // The max size of all buggy source code token vectors.
	// the input path of token embedding.
	public static final String EMBEDDING_INPUT = MINING_INPUT + "Embedding/";
	public static final String SELECTED_PATCHES_SOURE_CODE_FILE = EMBEDDING_INPUT + "patchSourceCode.list";// Selected patches.
	public static final String SELECTED_BUGGY_TREE_FILE = EMBEDDING_INPUT + "buggyTrees.list";
	public static final String SELECTED_BUGGY_TOKEN_FILE = EMBEDDING_INPUT + "tokens.list"; // Selected token vectors of buggy source code.
	public static final String SELECTED_EDITSCRIPTES_FILE = EMBEDDING_INPUT + "editScripts.list"; // Selected edit script vectors.
	public static final String SELECTED_ALARM_TYPES_FILE = EMBEDDING_INPUT + "alarmTypes.list"; // Selected edit script vectors.
	// the input path of feature learning.
	public static final String FEATURE_LEARNING_INPUT = MINING_INPUT + "FeatureLearning/";
	public static final String EMBEDDED_EDIT_SCRIPT_TOKENS = FEATURE_LEARNING_INPUT + "embeddedEditScriptTokens.list"; // All embedded tokens of selected edit scripts.
	public static final String VECTORIED_EDIT_SCRIPTS = FEATURE_LEARNING_INPUT + "vectorizedEditScripts.csv"; // Embedded and vectorized edit script vectors.
	// the input path of clustering.
	public static final String EXTRACTED_FEATURES = MINING_INPUT + "ExtractedFeatures/"; // Extracted features of all edit scripts.
	public static final String CLUSTER_INPUT = MINING_INPUT + "ClusteringInput/input.arff";

	// the output path of fix patterns mining.
	private static final String MINING_OUTPUT = ROOT_PATH + "MiningOutput/";
	public static final String CLUSTER_OUTPUT = MINING_OUTPUT + "ClusteringOutput/clusterResults.list";
	public static final String CLUSTERED_PATCHES_FILE = MINING_OUTPUT + "ClusteredPatches/";
	public static final String CLUSTERED_TOKENSS_FILE = MINING_OUTPUT + "ClusteredTokens/"; // Token vectors of buggy source code.
	public static final String COMMON_CLUSTERS_SIZES = MINING_OUTPUT + "ClusteringOutput/CommonClusterSizes.list";
	
	// evaluation data
	public static final String TEST_INPUT = ROOT_PATH + "TestProjects/";
	public static final String TEST_POSITION_FILE = ROOT_PATH + "TestData/Positions/"; // Positions of all test statements.
	public static final String TEST_DATA_FILE = ROOT_PATH + "TestData/TestStatements/"; // Token vectors of all test statements.
	
	public static final String NUMBER_OF_TRAINING_DATA = ROOT_PATH + "TestData/NumberOfTrainingData.list";;
	
	// data of unsupervised learning
	public static final String EMBEDDING_DATA_TOKENS1 = ROOT_PATH + "TestData/AllTokenVectorsForEvaluation.list";
	public static final String EMBEDDED_ALL_TOKENS1 = ROOT_PATH + "TestData/AllEmbeddedTokens.list";
	public static final String VECTORIED_ALL_SOURCE_CODE1 = ROOT_PATH + "TestData/AllVectorizedSourceCode/";
	public static final String EXTRACTED_FEATURES_EVALUATION = ROOT_PATH + "TestDataExtractedFeatures/"; // extracted features of all source code (training data and testing data)
	
	// Data of supervised learning
	public static final String CLUSTERNUMBER_LABEL_MAP = ROOT_PATH + "TestData/clusterMappingLabel.list";
	public static final String EMBEDDING_DATA_TOKENS2 = ROOT_PATH + "TestData/AllTokenVectorsForSupervisedEvaluation.list";
	public static final String EMBEDDED_ALL_TOKENS2 = ROOT_PATH + "TestData/AllEmbeddedTokensForSuperVisedEvaluation.list";
	public static final String TRAINING_DATA = ROOT_PATH + "TestData/TrainingData.csv"; // Training data of supervised learning
	public static final String TESTING_DATA = ROOT_PATH + "TestData/SupervisedLearning/"; // testing data of supervised learning
	public static final String FEATURES_OF_TRAINING_DATA = ROOT_PATH + "TestingOutput/TraingFeatures/";
	public static final String FEATURES_OF_TESTING_DATA = ROOT_PATH + "TestingOutput/TestingFeatures/";
	public static final String POSSIBILITIES_OF_TESTING_DATA = ROOT_PATH + "TestingOutput/Posibilities/";
	public static final String PREDICTED_RESULTS_OF_TESTING_DATA = ROOT_PATH + "TestingOutput/Prediction/";

	public static final String SUPERVISED_LEARNING_MODEL = ROOT_PATH + "TestingOutput/SupervisedLearningModel.zip";
	
	public static final String FEATURES_OF_COMMON_CLUSTERS = ROOT_PATH + "FeaturesOfCommonClusters/";

}
