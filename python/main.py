from common.commons import *





if __name__ == '__main__':


    try:
        args = getRun()
        setLogg()


        setEnv(args)

        job = args.job
        job = 'indexClusters'
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
        jdk8 = os.environ["JDK8"]
        pd.options.mode.chained_assignment = None


        subject = 'ALL'
        rootType = 'if'

        print(job)


        if job == 'dataset':
            from javaDS import createDS
            createDS(subject)
        elif job == 'linuxDS':
            from linuxDataset import collectBugFixPatches
            collectBugFixPatches()
        elif job =='otherDS':
            from otherDatasets import core
            core()
        elif job =='richEditScript':
            cmd = "JAVA_HOME='" + jdk8 + "' java -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " RICHEDITSCRIPT " + 'L1'
            output = shellCallTemplate(cmd)
            logging.info(output)

        elif job =='loadRES':
            cmd = "JAVA_HOME='" + jdk8 + "' java -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " LOAD " + rootType
            output = shellCallTemplate(cmd)
            logging.info(output)

        elif job =='shapeSI':
            from pairs import shapePairs
            matches = shapePairs()

            from pairs import createPairs
            createPairs(matches)

            from pairs import importShape
            importShape()

        elif job =='compareShapes':
             # cmd = "mvn exec:java -f '/data/fixminer_source/' -Dexec.mainClass='edu.lu.uni.serval.fixminer.akka.compare.CompareTrees' -Dexec.args='"+ " shape " + join(DATA_PATH,"redis") +" ALLdumps-gumInput.rdb " + "clusterl0-gumInputALL.rdb /data/fixminer-core/python/data/richEditScript'"
            cmd = "JAVA_HOME='" + jdk8 + "' java  -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " COMPARE " + 'L1'
            output = shellCallTemplate(cmd)
            logging.info(output)

        elif job == 'clusterAdditional':
            from addNewData import cluster
            cluster()

        elif job == 'cluster':
            from abstractPatch import cluster

            dbDir = join(DATA_PATH, 'redis')
            startDB(dbDir, "6399", PROJECT_TYPE)
            cluster(join(DATA_PATH,'shapes'),join(DATA_PATH, 'pairs'),'shapes',rootType)

        elif job =='actionSI':
            from pairs import actionPairs
            actionPairs(rootType)

        # elif job =='importActionPairs':
            from pairs import importAction
            importAction(rootType)

        elif job =='compareActions':
            # cmd = "JAVA_HOME='"+jdk8+"' java -Xmx8096m -Djava.util.concurrent.ForkJoinPool.common.parallelism=64 -jar "+  join(DATA_PATH,'CompareTrees.jar') + " action " + join(DATA_PATH,"redis") +" ALLdumps-gumInput.rdb " + "clusterl1-gumInputALL.rdb"

            cmd = "JAVA_HOME='" + jdk8 + "' java -jar " + join(DATA_PATH, 'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH, 'app.properties') + " COMPARE " + 'L2'
            output = shellCallTemplate(cmd)
            logging.info(output)

        elif job == 'clusterActions':
            from abstractPatch import cluster

            dbDir = join(DATA_PATH, 'redis')
            startDB(dbDir, "6399", PROJECT_TYPE)
            cluster( join(DATA_PATH, 'actions'),join(DATA_PATH, 'pairsAction'),'actions',rootType)

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

        elif job == 'additional':
            from addNewData import core
            core()
            # from addNewData import checkWrongMembers
            # checkWrongMembers()

        elif job == 'codeflaws':
            from otherDatasets import codeflaws
            codeflaws()


        elif job =='indexClusters':
            # from sprinferIndex import runSpinfer
            # runSpinfer()
            #
            # from sprinferIndex import test
            # test()
            # from sprinferIndex import divideCoccis
            # divideCoccis()
            # from sprinferIndex import removeDuplicates
            # removeDuplicates()


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
