import bugzoo
from bugzoo import server, Container
from common.commons import *
import signal

# from common.commons import shellGitCheckout
DATA_PATH = os.environ["DATA_PATH"]
ROOT_DIR = os.environ["ROOT_DIR"]
DATASET = os.environ["dataset"]
introClassFile = join(DATA_PATH,'introClassData.txt')
COCCI_PATH = join(os.environ["coccinelle"],'spatch')
def patchSourceFile(bugPath,spfile,bugName):
    # print(bugPath)
    srcName = bugPath.split('/')[-1].split(':')[1]
    srcPath = join(bugPath,srcName + '.c')
    patchName = srcName+'.c'

    if(isfile(join(DATA_PATH,"introclass",bugName,'patched',patchName+spfile+'.c'))):
        return join(DATA_PATH,"introclass",bugName,'patched',patchName+spfile+'.c')

    if not (isfile(join(DATA_PATH,"introclass",bugName,'patches',patchName+spfile+'.txt'))):
        cmd = COCCI_PATH + ' --sp-file ' + join(DATASET, 'cocci', spfile) + ' ' + srcPath + ' --patch -o' + join(
            DATA_PATH, "introclass", bugName, 'patches', patchName) + ' > ' + join(DATA_PATH, "introclass", bugName,
                                                                                   'patches',
                                                                                   patchName + spfile + '.txt')

        output, e = shellGitCheckout(cmd)
    # logging.info(output)
    patchSize = os.path.getsize(join(DATA_PATH,"introclass",bugName,'patches',patchName+spfile+'.txt'))
    if patchSize == 0 :
        # os.remove(join(DATA_PATH,"introclass",bugName,'patches',patchName+spfile+'.txt'))
        return None
    else:
        cmd = 'patch -d '+join(DATA_PATH, "introclass", bugName)+' -i '+join(DATA_PATH,"introclass",bugName,'patches',patchName+spfile+'.txt')+' -o '+join(DATA_PATH,"introclass",bugName,'patched',patchName+spfile+'.c')
        o,e = shellGitCheckout(cmd)
        return join(DATA_PATH, "introclass", bugName, 'patched', patchName + spfile + '.c')

def testCore(t):
        bugName, port = t
        container = None
        # with bugzoo.server.ephemeral(port=port, verbose=False,bugzooPath="/Users/anil.koyuncu/anaconda3/envs/python36/bin/bugzood", timeout_connection=3000) as client:
        cmd = 'bash {} {}'.format(join(ROOT_DIR,'data','startBugzoo.sh'),port)

        with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True) as myProcess:

            # o,e = shellGitCheckout(cmd)
            url = "http://127.0.0.1:{}".format(port)
            timeout_connection = 3000
            client = Client(url, timeout_connection=timeout_connection)
            # with bugzoo.server.ephemeral(port=port, verbose=False, timeout_connection=3000) as client:
            try:
                # port = 6060
                # url = "http://127.0.0.1:{}".format(port)
                # client = Client(url, timeout_connection=300)
                # bugName = bugList[i]
                # if bugName != 'introclass:digits:070455:000':
                #     continue
                fix = 'failure'
                output = ''
                # print("bugName: {}".format(bugName), end=' ')
                output += 'bugName:' + bugName + ', '

                bug = client.bugs[bugName]
                if not client.bugs.is_installed(bug):
                    output += ' building'
                    client.bugs.build(bug)

                    # client.bugs.download(bug)
                    # print("the image is not installed :'(")

                # print("creating container...")
                container = client.containers.provision(bug)
                # print("container is ready")

                # print("First_test:", end=' ')
                output += 'First_test:'
                pre_test_outcomes = {}
                pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all(bug, container, client)
                # print("@fail:{}@total:{}".format(pre_failure, total), end=' ')
                # print("@pre_failure_cases:{}".format(pre_failure_cases), end=' ')
                if pre_failure == 0:
                    logging.error(bugName + ' no failed test initially')
                    return ''
                output += '@fail:' + str(pre_failure) + '@total:' + str(total) + ', '

                spfiles = listdir(join(DATASET, 'cocci'))
                # print("patching... " + bugName)
                for idx,spfile in enumerate(spfiles):
                    if spfile == '.DS_Store':
                        continue

                    path = join(DATA_PATH,'introclass',bugName)
                    patch = patchSourceFile(path,spfile,bugName)

                    if patch is None:
                        continue
                    # print(patch)
                    # print(str(idx) + '..'+str(len(spfiles)))
                # patch_path = join(path ,'patched')
                # # avaliable_patch = os.path.abspath('data') + '/introclass2/' + bugName + '/' + 'patches'
                # patch_names = os.listdir(patch_path)
                #
                    times = 0
                # for patch_name in patch_names:
                    # if patch_name not in os.listdir(avaliable_patch):
                    #     continue
                    # patch = join(patch_path,patch_name)

                    patch_result = patched_application(path, bug.name, patch, client, container)
                    if patch_result == -1 or patch_result.code != 0:
                        # print("@{}@".format('False'), end='')
                        # print("{}".format('F'), end=' ')
                        # output += '@False@F '
                        output += '@False:' + str(idx) + ':' + patch.split('/')[-1] + '@'
                        continue
                    # print("@{}@".format('True'), end='')
                    # output += '@True@'
                    output += '@True:' + str(idx) + ':' + patch.split('/')[-1] + '@'
                    # print("Second_test:", end=' ')
                    post_test_outcomes = {}
                    post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client)
                    # print("{}".format(post_failure), end=' ')
                    output += str(post_failure) + ' '
                    if post_failure == 0:
                        times += 1
                        fix = 'success'
                        # print("fix {} by {}".format(bugName, patch_name))
                        output += 'fix {} by {} '.format(bugName, patch)
                        break
                    # print("@fail:{}@total:{}".format(post_failure, total),end=' ')
                    # print("@post_failure_cases:{}".format(post_failure_cases))


                # cmd = 'docker rm -fv {}'.format(container.id)
                # out, e = shellGitCheckout(cmd)

                output += 'times:{}, '.format(times) + fix
                print(output)
                return output
            except Exception as e:
                logging.error(e)
            finally:
                # ''
                cmd = 'docker stop {}'.format(container.id)
                out, e = shellGitCheckout(cmd)
                try:
                    client.shutdown()
                except Exception as e:
                    logging.error(e)
                # print(myProcess.pid)
                # os.killpg(myProcess.pid, signal.SIGTERM)
                # docker stop $(docker ps -q)



def patch_validate():


    logger = logging.getLogger()

    for k,v in logger.manager.loggerDict.items():
        if(k.startswith('bugzoo')):
            if isinstance(v,logging.Logger):
                v.setLevel(logging.ERROR)

    # url = "http://127.0.0.1:6060"
    # client = bugzoo.Client(url)
    # bug = client.bugs['introclass:checksum:08c7ea:006']
    bugList = []

    nonFail = ['introclass:syllables:93f87b:005','introclass:smallest:f94e26:004','introclass:smallest:d25c71:001','introclass:median:d25c71:002','introclass:smallest:d25c71:000'
        ,'introclass:median:d6364e:007','introclass:median:489253:007','introclass:syllables:d12048:004','introclass:smallest:d9e7ea:002','introclass:syllables:035fe9:000'
     ,'introclass:syllables:c9d718:002','introclass:syllables:ea67b8:007','introclass:median:48b829:000','introclass:syllables:d9e7ea:001']

    # cmd = 'bash ' + join(DATA_PATH,'startBugzoo.sh')
    # cmd = "/Users/anil.koyuncu/anaconda3/envs/python36/bin/bugzood --debug -p " + str(port)
    # output, errors = shellGitCheckout(cmd)
    # with bugzoo.server.ephemeral(port=8082, verbose=True, timeout_connection=3000) as client:
    port = 6000
    bugs2test= listdir(join(DATA_PATH, "introclass"))
    for b in bugs2test:
        if b == '.DS_Store':
            continue

        if b in nonFail:
            continue
        t = b, port
        bugList.append(t)
        if port == 6300:
            port = 6000
        port += 1
    # with open(introClassFile, 'r') as file:
    #     for line in file.readlines():
    #
    #         t =line.strip(),port
    #         bugList.append(t)
    #         if port == 6300:
    #             port = 6000
    #         port +=1

        # for i in range(0,len(bugList)):
    # t = 'introclass:syllables:93f87b:005',6000
    # t = 'introclass:syllables:99cbb4:000',6000
    # testCore(t)
    # results = parallelRunMerge(testCore, bugList,max_workers=10)
    results = parallelRunMerge(testCore, bugList)
    print('\n'.join(results))
    with open(join(DATA_PATH, 'introTestResults'), 'w',
              encoding='utf-8') as writeFile:
        # if levelPatch == 0:
        writeFile.write('\n'.join(results))
        # for i in bugList:
        #     testCore(i)
        # pass


from bugzoo import Patch, Client

def test_all(bug, container,  client):
    test_outcomes = {}  # type: Dict[TestCase, TestOutcome]
    failure_cases = []
    failure = 0
    total = len(bug.tests._tests)
    for test in bug.tests:
        test_outcomes[test] = client.containers.test(container, test)
        # if test.expected_outcome != test_outcomes[test].passed:
        if test_outcomes[test].passed != True:
            failure += 1
            failure_cases.append(test.command)
            break
    return failure_cases, failure, total, test_outcomes


def patched_application(path, bugName, patched, client: Client, container: Container):
    buggroup = bugName.split(":")[1]
    # path = join(BUGDIR,bug)
    program = path + '/' + buggroup + '.c'
    fixedFile = patched.split('/')[-1]

    cmd = 'docker cp ' + patched + ' ' + container.id + ':/experiment/'
    output, e = shellGitCheckout(cmd)

    cmd = 'sudo chown $(whoami):$(whoami) "{}"'
    cmd = cmd.format(fixedFile)
    output = client.containers.exec(container=container, command=cmd, context='/experiment/')
    # cmd = 'rm .genprog_test_cache.json & mv {} {}.c & gcc -o {} {}'.format(fixedFile,fixedFile, buggroup, fixedFile+'.c')
    cmd = 'rm {} & rm .genprog_test_cache.json'.format(buggroup)
    output = client.containers.exec(container=container, command=cmd, context='/experiment/')

    cmd = 'setarch `uname -m` -R gcc -o {} {}'.format(buggroup, fixedFile)
    output = client.containers.exec(container=container, command=cmd, context='/experiment/')
    return output

def checkCorrect():
    regex = r"fix (.*) by (.*)\.c"
    if not os.path.exists(join(DATA_PATH, 'introclass_eval')):
        os.makedirs(join(DATA_PATH, 'introclass_eval'))

    with open(join(DATA_PATH, 'introTestResults'), 'r',
              encoding='utf-8') as readFile:
        res = readFile.readlines()
        results = []
        for line in res:
            if line.strip().endswith('success'):
                line = '\n'.join(line.split('@'))
                matches = re.finditer(regex, line, re.MULTILINE)

                for matchNum, match in enumerate(matches, start=1):


                    bug, patch = match.groups()
                    print(bug,patch)
                    t = bug,patch
                    results.append(t)

                    # for groupNum in range(0, len(match.groups())):
                    #     groupNum = groupNum + 1
                    #
                    #     print("Group {groupNum} found at {start}-{end}: {group}".format(groupNum=groupNum,
                    #                                                                     start=match.start(groupNum),
                    #                                                                     end=match.end(groupNum),
                    #                                                                     group=match.group(groupNum)))
                    #     line
        for t in results:
            bug, patch = t

            if not os.path.exists(join(DATA_PATH, 'introclass_eval',bug)):
                os.makedirs(join(DATA_PATH, 'introclass_eval',bug))

            buggyFile = join(DATA_PATH,'introclass',bug,patch.split('.')[0]+'.c')
            spinferFix = join(DATA_PATH,'introclass_patched',bug,'patches',patch + '.c')
            spinferPatch = join(DATA_PATH,'introclass_eval',bug,patch +'.patch')
            oracle = join(DATA_PATH,'introclass',bug,'oracle.c.patch')
            oracleEval = join(DATA_PATH,'introclass_eval',bug,'oracle.patch')
            cmd =  'diff -u ' +buggyFile + ' ' + spinferFix + ' > ' + spinferPatch
            o,e= shellGitCheckout(cmd)
            shutil.copy(oracle,oracleEval)


if __name__ == "__main__":
    patch_validate()

