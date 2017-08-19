package edu.lu.uni.serval.violation.parse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.lu.uni.serval.config.Configuration;
import edu.lu.uni.serval.utils.FileHelper;

public class TestViolationParser {

	private static final String REPO_PATH = "/Volumes/MacBook/repos/";
	
	public static void main(String[] args) {
		List<File> repositoriesList = new ArrayList<>();
		File repositories = new File(REPO_PATH);
		File[] subFiles = repositories.listFiles();
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				File[] repos = subFile.listFiles();
				for (File repo : repos) {
					if (repo.isDirectory()) {
						repositoriesList.add(repo);
					}
				}
			}
		}
		
		String unfixedAlarmFile = "Dataset/Unfixed-Alarms/";
		final String previousFilesPath = Configuration.GUM_TREE_INPUT + "unfixAlarms/";
		final String positionsFilePath = Configuration.GUM_TREE_INPUT + "un_positions/";
		FileHelper.createDirectory(previousFilesPath);
		FileHelper.createDirectory(positionsFilePath);
		
		List<File> unfixedAlarmFiles = FileHelper.getAllFilesInCurrentDiectory(unfixedAlarmFile, ".csv");
		
		for (File file : unfixedAlarmFiles) {
			ViolationParser parser = new ViolationParser();
			parser.parseViolations(file, repositoriesList, previousFilesPath, positionsFilePath);
		}
	
		
//		String fixedAlarmFile = "Dataset/fixed-alarms-v0.3.list.txt";

		/**
		 * 544 projects.
		 * 42322 files
		 * 84103 alarms.
		 * 19928 parsed instances.  1984

		 * 22 bugs
		 * 
NP_ALWAYS_NULL : org/jfree/chart/renderer/category/AbstractCategoryItemRenderer.java : 1800 : 1800
Chart_1.xml
UC_USELESS_CONDITION : org/jfree/data/KeyedObjects2D.java : 231 : 231
Chart_22.xml
DLS_DEAD_LOCAL_STORE : org/jfree/chart/renderer/GrayPaintScale.java : 125 : 125
Chart_24.xml
NP_NULL_ON_SOME_PATH : org/jfree/chart/plot/XYPlot.java : 4493 : 4493
Chart_4.xml
SF_SWITCH_NO_DEFAULT : com/google/javascript/jscomp/UnreachableCodeElimination.java : 151 : 171
Closure_127.xml
DLS_DEAD_LOCAL_STORE : com/google/javascript/jscomp/ScopedAliases.java : 276 : 276
Closure_24.xml
SF_SWITCH_NO_DEFAULT : com/google/javascript/jscomp/TypedScopeCreator.java : 550 : 580
Closure_43.xml
UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR : com/google/debugging/sourcemap/SourceMapConsumerV3.java : 488 : 488
Closure_47.xml
SF_SWITCH_NO_DEFAULT : com/google/javascript/jscomp/MakeDeclaredNamesUnique.java : 116 : 147
Closure_49.xml
DLS_DEAD_LOCAL_STORE : com/google/javascript/jscomp/CollapseProperties.java : 482 : 482
Closure_89.xml
SF_SWITCH_NO_DEFAULT : org/apache/commons/lang3/time/FastDateParser.java : 315 : 338
Lang_10.xml
DM_CONVERT_CASE : org/apache/commons/lang/StringUtils.java : 1048 : 1048
Lang_40.xml
SF_SWITCH_NO_DEFAULT : org/apache/commons/lang/math/NumberUtils.java : 449 : 491
Lang_58.xml
SF_SWITCH_NO_DEFAULT : org/apache/commons/lang/Entities.java : 916 : 919
Lang_62.xml
FE_FLOATING_POINT_EQUALITY : org/apache/commons/math3/util/FastMath.java : 1545 : 1545
Math_15.xml
DLS_DEAD_LOCAL_STORE : org/apache/commons/math/optimization/direct/BOBYQAOptimizer.java : 1658 : 1658
Math_38.xml
FE_FLOATING_POINT_EQUALITY : org/apache/commons/math/analysis/solvers/BaseSecantSolver.java : 187 : 187
Math_50.xml
CO_COMPARETO_INCORRECT_FLOATING : org/apache/commons/math/fraction/Fraction.java : 261 : 261
Math_91.xml
DLS_DEAD_LOCAL_STORE : org/mockito/internal/invocation/InvocationMatcher.java : 122 : 122
Mockito_1.xml
EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS : org/mockito/internal/creation/DelegatingMethod.java : 55 : 55
Mockito_11.xml
REC_CATCH_EXCEPTION : org/mockito/internal/creation/instance/ConstructorInstantiator.java : 26 : 26
Mockito_21.xml
DM_NUMBER_CTOR : org/joda/time/chrono/ZonedChronology.java : 469 : 469
Time_26.xml
		 */

//		final String previousFilesPath = Configuration.GUM_TREE_INPUT + "prevFiles/";
//		final String revisedFilesPath = Configuration.GUM_TREE_INPUT + "revFiles/";
//		final String positionsFilePath = Configuration.GUM_TREE_INPUT + "positions/";
//		final String diffentryFilePath = Configuration.GUM_TREE_INPUT + "diffentries/";
//		FileHelper.createDirectory(previousFilesPath);
//		FileHelper.createDirectory(revisedFilesPath);
//		FileHelper.createDirectory(positionsFilePath);
//		FileHelper.createDirectory(diffentryFilePath);
//		
//		ViolationParser parser = new ViolationParser();
//		parser.parseViolations(fixedAlarmFile, repositoriesList, previousFilesPath, revisedFilesPath, positionsFilePath, diffentryFilePath);
	}

}
