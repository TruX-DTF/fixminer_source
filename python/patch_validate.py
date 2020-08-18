import bugzoo
from bugzoo import server, Container
import csv
import os
from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
ROOT_DIR = os.environ["ROOT_DIR"]
DATASET = os.environ["dataset"]
COCCI_PATH = join(os.environ["coccinelle"],'spatch')
def patchSourceFile(bugPath,spfile,bugName):
    # print(bugPath)
    srcName = bugPath.split('/')[-1].split('-')[0]
    srcPath = bugPath
    patchName = srcName



    if(isfile(join(DATA_PATH,"manybugs",bugName,'patched',patchName+spfile+'.c'))):
        return join(DATA_PATH,"manybugs",bugName,'patched',patchName+spfile+'.c')

    if not (isfile(join(DATA_PATH,"manybugs",bugName,'patches',patchName+spfile+'.txt'))):
        cmd = COCCI_PATH + ' --sp-file ' + join(DATASET, 'cocci', spfile) + ' ' + srcPath + ' --patch -o' + join(
            DATA_PATH, "manybugs", bugName, 'patches', patchName) + ' > ' + join(DATA_PATH, "manybugs", bugName,
                                                                                   'patches',
                                                                                   patchName + spfile + '.txt')

        output, e = shellGitCheckout(cmd)
    # logging.info(output)
    patchSize = os.path.getsize(join(DATA_PATH,"manybugs",bugName,'patches',patchName+spfile+'.txt'))
    if patchSize == 0 :
        # os.remove(join(DATA_PATH,"introclass",bugName,'patches',patchName+spfile+'.txt'))
        return None
    else:

        cmd = 'patch -d '+'/'.join(srcPath.split('/')[:-1])+' -i '+join(DATA_PATH,"manybugs",bugName,'patches',patchName+spfile+'.txt')+' -o '+join(DATA_PATH,"manybugs",bugName,'patched',patchName+spfile+'.c')
        o,e = shellGitCheckout(cmd)
        return join(DATA_PATH, "manybugs", bugName, 'patched', patchName + spfile + '.c')

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


def readTestSuite(testPath):
    regex = r"([p|n0-9]+)\)"
    with open(testPath,mode='r') as testFile:
        test_str = testFile.read()
    matches = re.finditer(regex, test_str, re.MULTILINE)

    testList = []
    for matchNum, match in enumerate(matches, start=1):

         for groupNum in range(0, len(match.groups())):
            groupNum = groupNum + 1
            testList.append(match.group(groupNum))
    return testList
# def myvalidateCore(t):
#     bugName, port = t
#     container = None
#     with bugzoo.server.ephemeral(port=port, verbose=False, timeout_connection=30000) as client:
#         try:
#
#             bug = client.bugs[bugName]
#             if client.bugs.is_installed(bug):
#                 pass
#             else:
#                 client.bugs.build(bug)
#             fix = 'failure'
#             output = ''
#             output += 'bugName:' + bugName + ', '
#             container = client.containers.provision(bug)
#             # output += 'First_test:'
#
#             preId, postId = bugName.split(':')[-1].split('-')[-2:]
#             originalBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), preId)
#             # for ob in originalBugs:
#             #     filepath = ob.split('diffs')[-1]
#             #     cmd = 'cp /experiment/diffs' + filepath + ' src' + filepath.replace('-' + preId, '')
#             #     client.containers.exec(container=container, command=cmd, context='/experiment/')
#
#             validTests = readTestSuite(join(DATA_PATH, 'manybugs', bugName, 'test.sh'))
#             output += 'total ' +  str(len(bug.tests._tests)) + ' newtotal ' + str(len(validTests))
#             # client.containers.build(container)
#             # pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all(bug, container, client,validTests)
#             # if pre_failure == 0:
#             #     logging.error(bugName + ' no failed test initially')
#             #     return ''
#             # output += '@fail:' + ','.join(pre_failure) + '@total:' + str(total) + ', ' + '@newtotal:' + str(len(validTests)) + ', '
#             #
#             # fixBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), postId)
#             # for ob in fixBugs:
#             #     filepath = ob.split('diffs')[-1]
#             #     cmd = 'cp /experiment/diffs' + filepath + ' src' + filepath.replace('-' + postId, '')
#             #     client.containers.exec(container=container, command=cmd, context='/experiment/')
#             # client.containers.build(container)
#             # diff_files = get_diff(bug.name, client, container)
#             # patch_result = patch_application(client, container, diff_files)
#             # if not patch_result:
#             #     print("@{}@".format(patch_result))
#             #     continue
#             # print("@{}@".format(patch_result),end=' ')
#             #
#             #
#             # print("Second_test:",end=' ')
#             # post_test_outcomes = {}
#             # post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client,validTests)
#             # output += ','.join(post_failure) + ' '
#             # if len(post_failure) == 0:
#             #     # times += 1
#             #     fix = 'success'
#             #     # print("fix {} by {}".format(bugName, patch_name))
#             #     output += 'fix {}  '.format(bugName)
#
#
#             # patch_names = os.listdir(join(DATA_PATH,'manybugs',bugName,'patched'))
#             # patch_names = [i for i in patch_names if not i.endswith('.DS_Store')]
#             # if len(originalBugs) != 1:
#             #     members = []
#             #
#             #     for member in set([i.split('-')[0] for i in patch_names]):
#             #         members.append([i for i in patch_names if i.startswith(member)])
#             #
#             #     patch_names= list(itertools.product(*members))
#             #     patch_names
#             # times = 0
#             # for patch_name in patch_names:
#             #     if isinstance(patch_name, tuple):
#             #         for i in patch_name:
#             #             copyPatch(bugName, client, container, originalBugs, i, preId)
#             #     else:
#             #         copyPatch(bugName, client, container, originalBugs, patch_name, preId)
#             #
#             #     patch_result = client.containers.build(container)
#             #     if not patch_result.successful:
#             #         output += '@False@F '
#             #         continue
#             #     output += '@True@'
#             #
#             #     post_test_outcomes = {}
#             #     post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client,validTests)
#             #
#             #     output += ','.join(post_failure) + ' '
#             #     if len(post_failure) == 0:
#             #         # times += 1
#             #         fix = 'success'
#             #         # print("fix {} by {}".format(bugName, patch_name))
#             #         output += 'fix {}  '.format(bugName)
#             #
#             # output += 'times:{}, '.format(times) + fix
#
#             print(output)
#             return output
#
#         except Exception as e:
#             logging.error(e)
#             # continue
#         finally:
#             # ''
#             cmd = 'docker stop {}'.format(container.id)
#             out, e = shellGitCheckout(cmd)
#             # docker stop $(docker ps -q)

def checkDataset(bugName,preId,postId,container,client,output,bug):
    originalBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), preId)
    for ob in originalBugs:
        filepath = ob.split('diffs')[-1]
        cmd = 'cp diffs' + filepath + ' src' + filepath.replace('-' + preId, '')
        client.containers.exec(container=container, command=cmd, context='/experiment/')

    validTests = readTestSuite(join(DATA_PATH, 'manybugs', bugName, 'test.sh'))
    client.containers.build(container)
    pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all_(bug, container, client, validTests)
    if pre_failure == 0:
        logging.error(bugName + ' no failed test initially')
        return ''
    output += '@fail:' + ','.join(pre_failure) + '@total:' + str(total) + ', ' + '@newtotal:' + str(
        len(validTests)) + ', '

    fixBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), postId)
    for ob in fixBugs:
        filepath = ob.split('diffs')[-1]
        cmd = 'cp diffs' + filepath + ' src' + filepath.replace('-' + postId, '')
        client.containers.exec(container=container, command=cmd, context='/experiment/')

    times = 0
    name = ob
    patch_result = client.containers.build(container)
    if patch_result.successful:
        failure_cases, failure, total, test_outcomes = test_all_(bug, container, client, validTests)
        if len(failure) == 0:
            times += 1
            output += ' fixed by {}'.format(name)
        else:
            output += ' {}'.format(failure_cases)
    else:
        output += ' {}'.format('build error')
    output += ' times:{}'.format(times)
    if times > 0:
        output += ' success'
    else:
        output += ' failure'

    return output


def checkTemplates(bugName,preId,postId,container,client,output,bug):
    originalBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), preId)

    if not os.path.exists(join(DATA_PATH, 'manybugs', bugName, 'patches')):
        os.makedirs(join(DATA_PATH, 'manybugs', bugName, 'patches'))
    if not os.path.exists(join(join(DATA_PATH, 'manybugs', bugName, 'patched'))):
        os.makedirs(join(DATA_PATH, 'manybugs', bugName, 'patched'))

    fix = 'failure'

    # for ob in originalBugs:
    #     filepath = ob.split('diffs')[-1]
    #     cmd = 'cp diffs' + filepath + ' src' + filepath.replace('-' + preId, '')
    #     client.containers.exec(container=container, command=cmd, context='/experiment/')
    #
    # validTests = readTestSuite(join(DATA_PATH, 'manybugs', bugName, 'test.sh'))
    # client.containers.build(container)
    # pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all(bug, container, client, validTests)
    # if pre_failure == 0:
    #     logging.error(bugName + ' no failed test initially')
    #     return ''
    # output += '@fail:' + ','.join(pre_failure) + '@total:' + str(total) + ', ' + '@newtotal:' + str(
    #     len(validTests)) + ', '

    # fixBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), postId)

    spfiles = listdir(join(DATASET, 'cocci'))
    # print("patching... " + bugName)
    for idx, spfile in enumerate(spfiles):
        if spfile == '.DS_Store':
            continue

        # path = join(DATA_PATH, 'manybugs', bugName)

        path= originalBugs[0]
        patch = patchSourceFile(path, spfile, bugName)

        times = 0
        if patch is None:
            continue

        # for ob in fixBugs:

        cmd = 'docker cp ' + patch + ' ' + container.id + ':/experiment/src' + path.split(bugName + '/diffs')[-1].replace('-' + preId, '')
        o, e = shellGitCheckout(cmd)

        cmd = 'sudo chown $(whoami):$(whoami) "{}"'
        cmd = cmd.format('/experiment/src' + path.split(bugName + '/diffs')[-1].replace('-' + preId, ''))
        o = client.containers.exec(container=container, command=cmd, context='/experiment/')

        patch_result = client.containers.build(container)
        if patch_result.successful:

            output += '@True:' + str(idx) + ':' + patch.split('/')[-1] + '@'

            failure_cases, failure, total, test_outcomes = test_all(bug, container, client)
            if failure == 0:
                fix = 'success'
                # print("fix {} by {}".format(bugName, patch_name))
                output += 'fix {} by {} '.format(bugName, patch)
                break
            else:
                output += ' {}'.format(failure_cases)
        else:
            output += '@False:' + str(idx) + ':' + patch.split('/')[-1] + '@'
            # output += ' {}'.format('build error')
        # output += ' times:{}'.format(times)
        # if times > 0:
        #     output += ' success'
        # else:
        #     output += ' failure'

    output += 'times:{}, '.format(times) + fix

    return output


def validateCore(t):
    bugName, port = t
    container = None
    cmd = 'bash {} {}'.format(join(ROOT_DIR, 'data', 'startBugzoo.sh'), port)

    with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True) as myProcess:

        # o,e = shellGitCheckout(cmd)
        url = "http://127.0.0.1:{}".format(port)
        timeout_connection = 3000
        client = Client(url, timeout_connection=timeout_connection)
    # with bugzoo.server.ephemeral(port=port, verbose=False, timeout_connection=30000) as client:
        try:

            bug = client.bugs[bugName]
            if client.bugs.is_installed(bug):
                pass
            else:
                client.bugs.build(bug)

            output = ''
            output += 'bugName:' + bugName + ', '
            container = client.containers.provision(bug)
            output += 'First_test:'


            preId, postId = bugName.split(':')[-1].split('-')[-2:]

            # failing = join(DATA_PATH, 'manybugs', bugName, 'failing.tests.txt')
            # passing = join(DATA_PATH, 'manybugs', bugName, 'passing.tests.txt')
            #
            # if not (isfile(failing)):
            #     return
            #
            # cmd = 'docker cp ' +failing+ ' '+container.id +':/experiment/failing.tests.txt '
            # o,e = shellGitCheckout(cmd)
            # cmd = 'sudo chown $(whoami):$(whoami) "{}"'
            # cmd = cmd.format('failing.tests.txt')
            # o = client.containers.exec(container=container, command=cmd, context='/experiment/')
            # cmd = 'docker cp ' +passing+ ' '+container.id +':/experiment/passing.tests.txt '
            # o,e = shellGitCheckout(cmd)
            # cmd = 'sudo chown $(whoami):$(whoami) "{}"'
            # cmd = cmd.format('passing.tests.txt')
            # o = client.containers.exec(container=container, command=cmd, context='/experiment/')


            output = checkTemplates(bugName,preId,postId,container,client,output,bug)
            # output = checkDataset(bugName,preId,postId,container,client,output,bug)

            # client.containers.build(container)
            # diff_files = get_diff(bug.name, client, container)
            # patch_result = patch_application(client, container, diff_files)
            # if not patch_result:
            #     print("@{}@".format(patch_result))
            #     continue
            # print("@{}@".format(patch_result),end=' ')
            #
            #
            # print("Second_test:",end=' ')
            # post_test_outcomes = {}
            # post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client,validTests)
            # output += ','.join(post_failure) + ' '
            # if len(post_failure) == 0:
            #     # times += 1
            #     fix = 'success'
            #     # print("fix {} by {}".format(bugName, patch_name))
            #     output += 'fix {}  '.format(bugName)


            # patch_names = os.listdir(join(DATA_PATH,'manybugs',bugName,'patched'))
            # patch_names = [i for i in patch_names if not i.endswith('.DS_Store')]
            # if len(originalBugs) != 1:
            #     members = []
            #
            #     for member in set([i.split('-')[0] for i in patch_names]):
            #         members.append([i for i in patch_names if i.startswith(member)])
            #
            #     patch_names= list(itertools.product(*members))
            #     patch_names
            # times = 0
            # for patch_name in patch_names:
            #     if isinstance(patch_name, tuple):
            #         for i in patch_name:
            #             copyPatch(bugName, client, container, originalBugs, i, preId)
            #     else:
            #         copyPatch(bugName, client, container, originalBugs, patch_name, preId)
            #
            #     patch_result = client.containers.build(container)
            #     if not patch_result.successful:
            #         output += '@False@F '
            #         continue
            #     output += '@True@'
            #
            #     post_test_outcomes = {}
            #     post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client)
            #
            #     output += str(post_failure) + ' '
            #     if post_failure == 0:
            #         times += 1
            #         fix = 'success'
            #         output += 'fix {} by {} '.format(bugName, patch_name)
            #
            # output += 'times:{}, '.format(times) + fix

            print(output)
            return output

        except Exception as e:
            logging.error(e)
            # continue
        finally:
            # ''
            cmd = 'docker stop {}'.format(container.id)
            out, e = shellGitCheckout(cmd)
            try:
                client.shutdown()
            except Exception as e:
                e
                # logging.error(e)
            # docker stop $(docker ps -q)


def copyPatch(bugName, client, container, originalBugs, patch_name, preId):
    originalBugName = patch_name.split('-' + preId)[0]
    originalBugPaths = [i.split('diffs')[-1].split(originalBugName)[0] for i in originalBugs if
                        i.split('/')[-1].startswith(originalBugName)]
    if len(originalBugPaths) == 1:
        originalBugPath = originalBugPaths[0]
    else:
        logging.error('too many paths')
    cmd = 'docker cp ' + join(DATA_PATH, 'manybugs', bugName, 'patched',
                              patch_name) + ' ' + container.id + ':/experiment/src' + originalBugPath + originalBugName
    o, e = shellGitCheckout(cmd)
    cmd = 'sudo chown $(whoami):$(whoami) "{}"'
    cmd = cmd.format('src' + originalBugPath + originalBugName)
    o = client.containers.exec(container=container, command=cmd, context='/experiment/')

    cmd = 'rm -rf "{}"'
    cmd = cmd.format('src' + originalBugPath + originalBugName.replace('.c','.o'))
    o = client.containers.exec(container=container, command=cmd, context='/experiment/')


def patch_validate():
    logger = logging.getLogger()

    for k,v in logger.manager.loggerDict.items():
        if(k.startswith('bugzoo')):
            if isinstance(v,logging.Logger):
                v.setLevel(logging.ERROR)

    # buglist = listdir(join(DATA_PATH,'manybugs_sos'))
    buglist = ['manybugs:gmp:14166-14167', 'manybugs:gzip:2009-08-16-3fe0caeada-39a362ae9d',
               'manybugs:gzip:2009-10-09-1a085b1446-118a107f2d', 'manybugs:gzip:2010-02-19-3eb6091d69-884ef6d16c',
               'manybugs:libtiff:2005-12-21-3b848a7-3edb9cd', 'manybugs:libtiff:2006-02-23-b2ce5d8-207c78a',
               'manybugs:libtiff:2006-03-03-a72cf60-0a36d7f', 'manybugs:libtiff:2006-03-03-eec4c06-ee65c74',
               'manybugs:libtiff:2007-07-13-09e8220-f2d989d', 'manybugs:libtiff:2007-11-02-371336d-865f7b2',
               'manybugs:libtiff:2009-02-05-764dbba-2e42d63', 'manybugs:libtiff:2009-08-28-e8a47d4-023b6df',
               'manybugs:libtiff:2010-11-27-eb326f9-eec7ec0', 'manybugs:lighttpd:1948-1949',
               'manybugs:lighttpd:2254-2259', 'manybugs:lighttpd:2661-2662', 'manybugs:lighttpd:2785-2786',
               'manybugs:php:2011-03-25-8138f7de40-3acdca4703', 'manybugs:php:2011-04-02-70075bc84c-5a8c917c37',
               'manybugs:php:2011-11-19-eeba0b5681-d3b20b4058', 'manybugs:php:2011-12-04-1e6a82a1cf-dfa08dc325',
               'manybugs:php:2011-12-10-74343ca506-52c36e60c4', 'manybugs:php:2012-02-08-ff63c09e6f-6672171672']

    # buglist = ['manybugs:gzip:2010-02-19-3eb6091d69-884ef6d16c']

    # for i in range(0,len(bugList)):
    bugList = []
    port = 6000
    for b in sosbugs:
        if b== '.DS_Store':
            continue
        t = b, port
        if (b.startswith('manybugs:')):
            bugList.append(t)
            if port == 6300:
                port = 6000
            port += 1
    # bugList = [('manybugs:python:69783-69784', 6000)]
    # bugList = [('manybugs:python:69709-69710', 6027)]
    # bugList = [('manybugs:python:69223-69224', 6027)]
    # bugList = [('manybugs:php:2012-02-25-c1322d2505-cfa9c90b20', 6027)]


    # parallelRun(fixTests,bugList)
    # parallelRun(findTests,bugList,max_workers=1)
    results = parallelRunMerge(validateCore, bugList)
    # #
    print(results)
    with open(join(DATA_PATH, 'manyBugsValidateNewInitial'), 'w',
              encoding='utf-8') as writeFile:
        # if levelPatch == 0:
        writeFile.write('\n'.join(results))
    # print(results)

def findTests(t):
    bugName, port = t
    preId, postId = bugName.split(':')[-1].split('-')[-2:]

    preTests= join(DATA_PATH, 'manybugs', bugName,'test-' + preId + '.out')
    postTests  = join(DATA_PATH, 'manybugs', bugName,'test-' + postId + '.out')

    with open(preTests,mode='r') as preTestFile:
        preTestResults = preTestFile.read()

    with open(postTests,mode='r') as postTestFile:
        postTestResults = postTestFile.read()

    preTestResults
    preFails = findAllTests(r"FAIL .*\[(.*)\]", preTestResults)
    prePasses = findAllTests(r"PASS .*\[(.*)\]", preTestResults)
    postFails = findAllTests(r"FAIL .*\[(.*)\]", postTestResults)
    postPasses = findAllTests(r"PASS .*\[(.*)\]", postTestResults)
    existingTests = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'src'), '.phpt')
    existingTests = [i.split(join(DATA_PATH, 'manybugs', bugName, 'src/'))[-1] for i in existingTests]
    postPasses
    if( set(preFails).difference(set(postFails)) == set(postPasses).difference(set(prePasses))):
        failingList = list(set(preFails).difference(set(postFails)))

        passingList = list(set(postPasses).intersection(set(prePasses)))

        failingList = [i for i in failingList if i in existingTests]
        passingList = [i for i in passingList if i in existingTests]
        if( len(failingList) > 0 and len(passingList)> 0):
            with open(join(DATA_PATH, 'manybugs', bugName, 'failing.tests.txt'),mode='w') as failFile:
                failFile.write('\n'.join(failingList))
            with open(join(DATA_PATH, 'manybugs', bugName, 'passing.tests.txt'),mode='w') as passFile:
                passFile.write('\n'.join(passingList))
        else:
            print('something wrong ' + bugName)
    else:
        print('something wrong ' + bugName)




def findAllTests(regex,test_str):
    matches = re.finditer(regex, test_str, re.MULTILINE)
    res = []
    for matchNum, match in enumerate(matches, start=1):

        # print("Match {matchNum} was found at {start}-{end}: {match}".format(matchNum=matchNum, start=match.start(),
        #                                                                     end=match.end(), match=match.group()))

        for groupNum in range(0, len(match.groups())):
            groupNum = groupNum + 1
            res.append(match.group(groupNum))
            # print("Group {groupNum} found at {start}-{end}: {group}".format(groupNum=groupNum,
            #                                                                 start=match.start(groupNum),
            #                                                                 end=match.end(groupNum),
            #                                                                 group=match.group(groupNum)))
    return res

def patch_validate_mine():
    logger = logging.getLogger()

    for k,v in logger.manager.loggerDict.items():
        if(k.startswith('bugzoo')):
            if isinstance(v,logging.Logger):
                v.setLevel(logging.ERROR)

    buglist = listdir(join(DATA_PATH,'manybugs'))

    # for i in range(0,len(bugList)):
    bugList = []
    port = 6000
    for b in buglist:
        if b== '.DS_Store':
            continue
        t = b, port
        bugList.append(t)
        if port == 6300:
            port = 6000
        port += 1
    # bugList = [('manybugs:python:69783-69784', 6000)]
    # bugList = [('manybugs:python:69709-69710', 6027)]
    # bugList = [('manybugs:python:69223-69224', 6027)]
    results = parallelRunMerge(myvalidateCore, bugList,max_workers=12)

    with open(join(DATA_PATH, 'mayBugsPatchResults'), 'w',
              encoding='utf-8') as writeFile:
        # if levelPatch == 0:
        writeFile.write('\n'.join(results))
    print(results)



from bugzoo import Patch, Client

def test_all_(bug, container,  client,validTests):
    test_outcomes = {}  # type: Dict[TestCase, TestOutcome]
    failure_cases = []
    failure = []
    total = len(bug.tests._tests)
    for test in bug.tests:
        # if test.name in validTests:
        test_outcomes[test] = client.containers.test(container, test)
        # if test.expected_outcome != test_outcomes[test].passed:
        if test_outcomes[test].passed != True:
            failure.append(test.name)
            failure_cases.append(test.command)
            break
    return failure_cases, failure, total, test_outcomes


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


def fixTests(t):
    bugName, port = t
    container = None
    with bugzoo.server.ephemeral(port=port, verbose=False, timeout_connection=30000) as client:
        try:

            bug = client.bugs[bugName]
            if client.bugs.is_installed(bug):
                pass
            else:
                client.bugs.build(bug)

            container = client.containers.provision(bug)
            preId, postId = bugName.split(':')[-1].split('-')[-2:]

            cmd = 'make distclean'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'git reset --hard && git clean -fd'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'git checkout "{}"'.format(preId)
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')
            cmd ='cat ../libxml.patch | patch -p0'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'find . -name tests -type d -exec git checkout {} {{}} \;'.format(postId)
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd ='./buildconf'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')
            if preId in ['74343ca506', '8138f7de40']:
                cmd ='./configure --disable-phar "CFLAGS=-fprofile-arcs -ftest-coverage" "CXXFLAGS=-fprofile-arcs -ftest-coverage" "LDFLAGS=-lgcov"'
            else:
                cmd = './configure "CFLAGS=-fprofile-arcs -ftest-coverage" "CXXFLAGS=-fprofile-arcs -ftest-coverage" "LDFLAGS=-lgcov"'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'make -j{}'.format(4)
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'no | make test -s > ../test-'+preId+'.out'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')
            ###

            cmd = 'make distclean'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'git reset --hard && git clean -fd'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'git checkout "{}"'.format(postId)
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')
            cmd = 'cat ../libxml.patch | patch -p0'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = './buildconf'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')
            if preId in ['74343ca506', '8138f7de40']:
                cmd = './configure --disable-phar "CFLAGS=-fprofile-arcs -ftest-coverage" "CXXFLAGS=-fprofile-arcs -ftest-coverage" "LDFLAGS=-lgcov"'
            else:
                cmd = './configure "CFLAGS=-fprofile-arcs -ftest-coverage" "CXXFLAGS=-fprofile-arcs -ftest-coverage" "LDFLAGS=-lgcov"'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'make -j{}'.format(4)
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'no | make test -s > ../test-' + postId + '.out'
            o = client.containers.exec(container=container, command=cmd, context='/experiment/src')

            cmd = 'docker cp ' + container.id +':/experiment/test-'+preId+'.out ' + join(DATA_PATH,'manybugs',bugName,'test-'+preId+'.out')
            o,e = shellGitCheckout(cmd)
            print(o, e)
            cmd = 'docker cp ' + container.id +':/experiment/test-'+postId+'.out ' +join(DATA_PATH,'manybugs',bugName,'test-'+postId+'.out')
            o,e = shellGitCheckout(cmd)

            print(o,e)




        except Exception as e:
            logging.error(e)
            # continue
        finally:
            # ''
            cmd = 'docker stop {}'.format(container.id)
            out, e = shellGitCheckout(cmd)
            # docker stop $(docker ps -q)

if __name__ == "__main__":
    patch_validate()
