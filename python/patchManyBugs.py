from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
SPINFER_PATH = os.environ["spinfer"]
ROOT_DIR = os.environ["ROOT_DIR"]
SPINFER_INDEX_PATH = os.environ["dataset"]
COCCI_PATH = join(os.environ["coccinelle"],'spatch')
DATASET = os.environ["dataset"]

import bugzoo
from bugzoo import server, Container
from bugzoo import Patch, Client
import signal

sosbugs = ['manybugs:gmp:14166-14167', 'manybugs:gzip:2009-08-16-3fe0caeada-39a362ae9d',
           'manybugs:gzip:2009-10-09-1a085b1446-118a107f2d', 'manybugs:gzip:2010-02-19-3eb6091d69-884ef6d16c',
           'manybugs:libtiff:2005-12-21-3b848a7-3edb9cd', 'manybugs:libtiff:2006-03-03-a72cf60-0a36d7f',
           'manybugs:libtiff:2006-03-03-eec4c06-ee65c74', 'manybugs:libtiff:2007-07-13-09e8220-f2d989d',
           'manybugs:libtiff:2007-11-02-371336d-865f7b2', 'manybugs:libtiff:2009-02-05-764dbba-2e42d63',
           'manybugs:libtiff:2009-08-28-e8a47d4-023b6df', 'manybugs:libtiff:2010-11-27-eb326f9-eec7ec0',
           'manybugs:libtiff:2006-02-23-b2ce5d8-207c78a', 'manybugs:lighttpd:2661-2662', 'manybugs:lighttpd:2254-2259',
           'manybugs:lighttpd:2785-2786', 'manybugs:lighttpd:1948-1949',
           'manybugs:php:2011-12-10-74343ca506-52c36e60c4', 'manybugs:php:2011-04-02-70075bc84c-5a8c917c37',
           'manybugs:php:2011-03-25-8138f7de40-3acdca4703', 'manybugs:php:2011-12-04-1e6a82a1cf-dfa08dc325',
           'manybugs:php:2012-02-08-ff63c09e6f-6672171672', 'manybugs:php:2011-11-19-eeba0b5681-d3b20b4058',
           'manybugs:php:2011-04-07-77ed819430-efcb9a71cd', 'manybugs:php:2011-02-01-01745fa657-1f49902999',
           'manybugs:php:2012-03-12-7aefbf70a8-efc94f3115', 'manybugs:php:2011-10-15-0a1cc5f01c-05c5c8958e',
           'manybugs:php:2011-01-30-5bb0a44e06-1e91069eb4', 'manybugs:php:2011-02-01-fefe9fc5c7-0927309852',
           'manybugs:php:2011-02-27-e65d361fde-1d984a7ffd', 'manybugs:php:2011-03-19-5d0c948296-8deb11c0c3',
           'manybugs:php:2011-03-23-63673a533f-2adf58cfcf', 'manybugs:php:2011-04-06-187eb235fe-2e25ec9eb7',
           'manybugs:php:2011-04-09-db01e840c2-09b990f499', 'manybugs:php:2011-05-17-453c954f8a-daecb2c0f4',
           'manybugs:php:2011-05-24-b60f6774dc-1056c57fa9', 'manybugs:php:2011-10-16-1f78177e2b-d4ae4e79db',
           'manybugs:php:2011-10-31-2e5d5e5ac6-b5f15ef561', 'manybugs:php:2011-10-31-c4eb5f2387-2e5d5e5ac6',
           'manybugs:php:2011-11-01-ceac9dc490-9b0d73af1d', 'manybugs:php:2011-11-11-fcbfbea8d2-c1e510aea8',
           'manybugs:php:2011-11-15-236120d80e-fb37f3b20d', 'manybugs:php:2011-11-16-55acfdf7bd-3c7a573a2c',
           'manybugs:php:2011-11-22-ecc6c335c5-b548293b99', 'manybugs:php:2011-11-23-eca88d3064-db0888dfc1',
           'manybugs:php:2012-01-27-544e36dfff-acaf9c5227', 'manybugs:php:2012-01-30-9de5b6dc7c-4dc8b1ad11',
           'manybugs:php:2012-02-25-c1322d2505-cfa9c90b20', 'manybugs:php:2012-03-04-60dfd64bf2-34fe62619d',
           'manybugs:php:2012-03-08-0169020e49-cdc512afb3', 'manybugs:php:2012-03-11-3954743813-d4f05fbffc',
           'manybugs:php:2012-03-12-438a30f1e7-7337a901b7', 'manybugs:python:69223-69224',
           'manybugs:python:69368-69372', 'manybugs:python:70098-70101', 'manybugs:python:70056-70059',
           'manybugs:wireshark:37112-37111', 'manybugs:wireshark:37122-37123', 'manybugs:gmp:13420-13421',
           'manybugs:gzip:2009-09-26-a1d3d4019d-f17cbd13a1', 'manybugs:lighttpd:1913-1914',
           'manybugs:php:2011-11-19-51a4ae6576-bc810a443d', 'manybugs:php:2011-03-11-d890ece3fc-6e74d95f34',
           'manybugs:php:2011-11-19-eeba0b5681-f330c8ab4e', 'manybugs:php:2012-01-01-80dd931d40-7c3177e5ab']


def cocciCore(t):
    cmd, manybug, patchName, spfile,srcPath = t
    # logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    # logging.info(output)
    patchSize = os.path.getsize(join(DATA_PATH,"manybugs",manybug,'patches',patchName+spfile+'.txt'))
    if patchSize == 0 :
        os.remove(join(DATA_PATH,"manybugs",manybug,'patches',patchName+spfile+'.txt'))
    else:
        cmd = 'patch -d '+srcPath.replace(patchName,'') + ' -i ' + join(DATA_PATH,"manybugs",manybug,'patches',patchName+spfile+'.txt') + ' -o ' + join(DATA_PATH,"manybugs",manybug,'patched',patchName+spfile+'.c')
        output, e = shellGitCheckout(cmd)
        output,e
        print(output)


def buildAll():
    bugList = []
    port = 6000
    for i in sosbugs:
        t = i,port
        bugList.append(t)
        if port == 6300:
            port = 6000
        port += 1
    parallelRun(checkBuild, bugList, max_workers=4)


def checkBuild(t):
    bugName, port = t
    # port = 3000
    # cmd = 'bash {} {}'.format(join(DATA_PATH, 'startBugzoo.sh'), port)
    cmd = 'bash {} {}'.format(join(ROOT_DIR,'data', 'buildBugzoo.sh'), bugName)

    out, e = shellGitCheckout(cmd)

    # container = None
    # with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True) as process:
    #
    #     # o,e = shellGitCheckout(cmd)
    #     url = "http://127.0.0.1:{}".format(port)
    #     timeout_connection = 3000
    #     client = Client(url, timeout_connection=timeout_connection)
    #
    #     try:
    #         bug = client.bugs[bugName]
    #         if not client.bugs.is_installed(bug):
    #             print(' building ' + bugName)
    #             o = client.bugs.build(bug)
    #
    #             # client.bugs.download(bug)
    #             # print("the image is not installed :'(")
    #
    #         # print("creating container...")
    #         container = client.containers.provision(bug)
    #
    #     except Exception as e:
    #         logging.error(e)
    #         # continue
    #     finally:
    #         # ''
    #         cmd = 'docker stop {}'.format(container.id)
    #         out, e = shellGitCheckout(cmd)
    #         client.shutdown()
    #         os.killpg(process.pid, signal.SIGTERM)

def patchCore():
    try:

        pathManyBug = join(DATA_PATH, "manybugs")
        manybugs = listdir(pathManyBug)

        manybugs = [i for i in manybugs if i in sosbugs]
        spfiles = listdir(join(DATASET,'cocci'))

        # from sprinferIndex import filterPatterns
        # filteredPattern = filterPatterns()

        # spfiles = [i for i in spfiles if i in filteredPattern]
        workList = []
        for manybug in manybugs:
            if manybug == '.DS_Store':
                continue
            # files = listdir(join(join(DATA_PATH,"manybugs",manybug,'diffs')))
            if  os.path.exists(join(pathManyBug, manybug, 'patches')):
                shutil.rmtree(join(pathManyBug, manybug, 'patches'))

                os.mkdir(join(pathManyBug, manybug, 'patches'))
            else:
                os.mkdir(join(pathManyBug, manybug, 'patches'))

            if  os.path.exists(join(pathManyBug, manybug, 'patched')):
                shutil.rmtree(join(pathManyBug, manybug, 'patched'))

                os.mkdir(join(pathManyBug, manybug, 'patched'))
            else:
                os.mkdir(join(pathManyBug, manybug, 'patched'))

            bug, fix = manybug.split(':')[-1].split('-')[-2:]
            files = get_filepaths(join(pathManyBug, manybug,'diffs'), bug)
            sources = [i for i in files if not i.endswith('oracle.c')]


            for src in sources:
                # srcPath = src.replace('/diffs/','/src/')
                srcPath = src
                patchName = src.split('/')[-1]
                for spfile in spfiles:
                     cmd = COCCI_PATH + ' --sp-file ' + join(DATASET,'cocci',spfile) + ' ' + srcPath + ' --patch -o' + join(pathManyBug,manybug,'patches',patchName) + ' > ' + join(pathManyBug, manybug, 'patches', patchName + spfile + '.txt')
                     # cmd = COCCI_PATH + ' --sp-file ' + join(DATASET,'cocci',spfile) + ' ' + srcPath + ' -o ' + join(DATA_PATH,"introclass",manybug,'patches',patchName+spfile+'.txt')
                     t = cmd,manybug,patchName,spfile,srcPath
                     workList.append(t)
    except Exception as e:
        logging.error(e)
    parallelRun(cocciCore,workList)


def exportSosPatches():
    pathManyBug = join(DATA_PATH, "manybugs")
    pathManyBugSos = join(DATA_PATH, "manybugs_sos")
    manybugs = listdir(pathManyBug)
    manybugs = [i for i in manybugs if i in sosbugs]

    for manybug in manybugs:
        # shutil.copytree(join(pathManyBug,manybug,'patched'),join(pathManyBugSos,manybug,'patched'))
        shutil.copytree(join(pathManyBug,manybug,'patches'),join(pathManyBugSos,manybug,'patches'))

def patched():
    pathManyBug = join(DATA_PATH, "manybugs")
    manybugs = listdir(pathManyBug)
    manybugs = [i for i in manybugs if i in sosbugs]
    # spfiles = listdir(join(DATASET,'cocci'))
    workList = []
    for manybug in manybugs:
        if manybug == '.DS_Store':
            continue
        # files = listdir(join(join(DATA_PATH,"manybugs",manybug,'diffs')))
        if  os.path.exists(join(DATA_PATH, "manybugs_patched", manybug, 'patches')):
            shutil.rmtree(join(DATA_PATH, "manybugs_patched", manybug, 'patches'))

            os.makedirs(join(DATA_PATH, "manybugs_patched", manybug, 'patches'))
        else:
            os.makedirs(join(DATA_PATH, "manybugs_patched", manybug, 'patches'))
        files = get_filepaths(join(DATA_PATH, "manybugs", manybug), '.c')
        sources = [i for i in files if not i.endswith('oracle.c')]

        bug, fix = manybug.split(':')[-1].split('-')[-2:]
        files = get_filepaths(join(pathManyBug, manybug, 'diffs'), bug)
        sources = [i for i in files if not i.endswith('oracle.c')]

        for src in sources:
            # srcPath = src.replace('/diffs/','/src/')
            srcPath = src
            patchName = src.split('/')[-1]
            spfiles = listdir(join(DATA_PATH,"manybugs",manybug,'patches'))
            for spfile in spfiles:
                 spfile =spfile.replace(patchName,'').replace('.txt','')
                 cmd = COCCI_PATH + ' --sp-file ' + join(DATASET,'cocci',spfile) + ' ' + srcPath + ' -o ' + join(DATA_PATH,"manybugs_patched",manybug,'patches',patchName+spfile+'.c')
                 t = cmd,manybug,patchName,spfile
                 workList.append(t)
    parallelRun(cocciCore,workList)