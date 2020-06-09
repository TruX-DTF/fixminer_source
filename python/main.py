from common.commons import *





if __name__ == '__main__':


    try:
        args = getRun()
        setLogg()


        setEnv(args)

        job = args.job
        # job = "cluster"
        ROOT_DIR = os.environ["ROOT_DIR"]
        REPO_PATH = os.environ["REPO_PATH"]
        CODE_PATH = os.environ["CODE_PATH"]
        DATA_PATH = os.environ["DATA_PATH"]
        COMMIT_DFS = os.environ["COMMIT_DFS"]
        BUG_POINT = os.environ["BUG_POINT"]
        COMMIT_FOLDER = os.environ["COMMIT_FOLDER"]
        FEATURE_DIR = os.environ["FEATURE_DIR"]
        DATASET_DIR = os.environ["DATASET_DIR"]
        PROJECT_TYPE = os.environ["PROJECT_TYPE"]
        REDIS_PORT = os.environ["REDIS_PORT"]
        jdk8 = os.environ["JDK8"]
        pd.options.mode.chained_assignment = None


        # subject = 'ALL'
        # rootType = 'if'

        print(job)


        if job == 'dataset4j':
            from javaDS import createDS
            createDS()
        # elif job == 'linuxDS':
        #     from linuxDataset import collectBugFixPatches
        #     collectBugFixPatches()
        elif job =='dataset4c':
            from otherDatasets import core
            core()
        elif job =='richedit':
            dbDir = join(DATA_PATH, 'redis')
            stopDB(dbDir, REDIS_PORT)
            cmd = "JAVA_HOME='" + jdk8 + "' java  -jar " + join(Path(ROOT_DIR).parent, 'target','FixPatternMiner-1.0.0-jar-with-dependencies.jar') + " " + args.prop + " RICHEDITSCRIPT "
            output = shellCallTemplate(cmd)
            logging.info(output)

        # elif job =='loadRES':
        #     cmd = "JAVA_HOME='" + jdk8 + "' java -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " LOAD " + rootType
        #     output = shellCallTemplate(cmd)
        #     logging.info(output)

        elif job =='shapeSI':
            from pairs import shapePairs
            matches = shapePairs()

            from pairs import createPairs
            createPairs(matches)

            from pairs import importShape
            importShape()

        elif job =='compare':
             # cmd = "mvn exec:java -f '/data/fixminer_source/' -Dexec.mainClass='edu.lu.uni.serval.richedit.akka.compare.CompareTrees' -Dexec.args='"+ " shape " + join(DATA_PATH,"redis") +" ALLdumps-gumInput.rdb " + "clusterl0-gumInputALL.rdb /data/richedit-core/python/data/richEditScript'"
            cmd = "JAVA_HOME='" + jdk8 + "' java  -jar " + join(Path(ROOT_DIR).parent, 'target','FixPatternMiner-1.0.0-jar-with-dependencies.jar') + " " + args.prop + " COMPARE "
            output = shellCallTemplate4jar(cmd)
            logging.info(output)

        # elif job == 'clusterAdditional':
        #     from addNewData import cluster
        #     cluster()

        elif job == 'cluster':
            from abstractPatch import cluster

            dbDir = join(DATA_PATH, 'redis')
            startDB(dbDir, "6399", PROJECT_TYPE)
            cluster(join(DATA_PATH,'shapes'),join(DATA_PATH, 'pairs'),'shapes')

        # elif job =='actionSI':
        #     from pairs import actionPairs
        #     actionPairs(rootType)
        #
        # # elif job =='importActionPairs':
        #     from pairs import importAction
        #     importAction(rootType)
        #
        # elif job =='compareActions':
        #     # cmd = "JAVA_HOME='"+jdk8+"' java -Xmx8096m -Djava.util.concurrent.ForkJoinPool.common.parallelism=64 -jar "+  join(DATA_PATH,'CompareTrees.jar') + " action " + join(DATA_PATH,"redis") +" ALLdumps-gumInput.rdb " + "clusterl1-gumInputALL.rdb"
        #
        #     cmd = "JAVA_HOME='" + jdk8 + "' java -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " COMPARE " + 'L2'
        #     output = shellCallTemplate(cmd)
        #     logging.info(output)
        #
        # elif job == 'clusterActions':
        #     from abstractPatch import cluster
        #
        #     dbDir = join(DATA_PATH, 'redis')
        #     startDB(dbDir, "6399", PROJECT_TYPE)
        #     cluster( join(DATA_PATH, 'actions'),join(DATA_PATH, 'pairsAction'),'actions',rootType)

        elif job == 'tokenSI':
            from pairs import tokenPairs
            tokenPairs()
            from pairs import importToken
            importToken()

        elif job == 'compareTokens':
            # cmd = "JAVA_HOME='"+jdk8+"' java -jar "+  join(DATA_PATH,'CompareTrees.jar') + " token " + join(DATA_PATH,"redis") +" ALLdumps-gumInput.rdb " + "clusterl2-gumInputALL.rdb"
            cmd = "JAVA_HOME='" + jdk8 + "' java -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " COMPARE " + 'L3'
            output = shellCallTemplate(cmd)
            logging.info(output)

        elif job == 'clusterTokens':
            from abstractPatch import cluster

            dbDir = join(DATA_PATH, 'redis')
            startDB(dbDir, "6399", PROJECT_TYPE)
            startDB(dbDir, "6380", "clusterl2-gumInputALL.rdb")
            cluster(join(DATA_PATH, 'tokens'), join(DATA_PATH, 'pairsToken'),'tokens')
            stopDB(dbDir, "6380", "clusterl2-gumInputALL.rdb")

        # elif job == 'additional':
        #     from addNewData import core
        #     core()
        #     # from addNewData import checkWrongMembers
        #     # checkWrongMembers()

        elif job == 'codeflaws':
            from otherDatasets import codeflaws
            codeflaws()


        elif job =='indexClusters':
            from sprinferIndex import runSpinfer
            runSpinfer()

            from sprinferIndex import test
            test()
            from sprinferIndex import divideCoccis
            divideCoccis()
            from sprinferIndex import removeDuplicates
            removeDuplicates()

        elif job == 'evalManyBugs':
            from patchManyBugs import patchCore
            patchCore()
            # from patchManyBugs import patched
            # patched()
            from patchManyBugs import exportSosPatches
            exportSosPatches()
            from validate_manybugs import validate

            validate()

        elif job =='patternOperations':
            from sprinferIndex import patternOperations
            patternOperations()
        elif job == 'patchManyBugs':
            from patchManyBugs import buildAll
            buildAll()

            # from patchManyBugs import patchCore
            # patchCore()
            # # from patch_validate import patch_validate_mine
            # # patch_validate_mine()
            # from patchManyBugs import patched
            # patched()
            # from patchManyBugs import exportSosPatches
            # exportSosPatches()

        elif job =='patchIntro':
            from sprinferIndex import patchCoreIntro
            patchCoreIntro()
            # from sprinferIndex import patched
            # patched()

        elif job =='validateIntro':
            # from patch_validate_introClass2 import patch_validate
            # patch_validate()
            from test_patched_file import patch_validate
            patch_validate()
        elif job =='checkCorrectIntro':
            from test_patched_file import checkCorrect
            checkCorrect()
        elif job == 'manybugs':
            from getManybugs import export
            export()

        elif job == 'validateMany':
            from patch_validate import patch_validate
            patch_validate()

        elif job == 'introclass':
            from getIntroClass import export
            export()

        elif job =='stats':
            from stats import statsNormal
            statsNormal(True)


        elif job == 'datasetDefects4J':
            from defects4JDataset import core
            core()

        elif job =='bug':
            from bugstats import bStats
            bStats()
        elif job == 'defects4j':
            from stats import defects4jStats
            defects4jStats()
        elif job == 'exportPatterns':
            from stats import exportAbstractPatterns
            exportAbstractPatterns()
        elif job =='export':
            patternPath = join(DATA_PATH,'actions','ExpressionStatement','3','0','0')
            patterns = listdir(patternPath)
            for pattern in patterns:
                repo = pattern.split('_')[0]
                file = pattern.replace(repo+'_','')
                print(file)
                filename = file.rsplit('_',1)[0]
                print(join(DATA_PATH,'gumInput',repo,'DiffEntries',filename))
                break

        else:
            logging.error('Unknown job %s',job)
    except Exception as e:
        logging.error(e)
