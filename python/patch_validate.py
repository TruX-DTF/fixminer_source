import bugzoo
from bugzoo import server, Container
import csv
import os
from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]

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
def myvalidateCore(t):
    bugName, port = t
    container = None
    with bugzoo.server.ephemeral(port=port, verbose=False, timeout_connection=30000) as client:
        try:

            bug = client.bugs[bugName]
            if client.bugs.is_installed(bug):
                pass
            else:
                client.bugs.build(bug)
            fix = 'failure'
            output = ''
            output += 'bugName:' + bugName + ', '
            container = client.containers.provision(bug)
            # output += 'First_test:'

            preId, postId = bugName.split(':')[-1].split('-')[-2:]
            originalBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), preId)
            # for ob in originalBugs:
            #     filepath = ob.split('diffs')[-1]
            #     cmd = 'cp /experiment/diffs' + filepath + ' src' + filepath.replace('-' + preId, '')
            #     client.containers.exec(container=container, command=cmd, context='/experiment/')

            validTests = readTestSuite(join(DATA_PATH, 'manybugs', bugName, 'test.sh'))
            output += 'total ' +  str(len(bug.tests._tests)) + ' newtotal ' + str(len(validTests))
            # client.containers.build(container)
            # pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all(bug, container, client,validTests)
            # if pre_failure == 0:
            #     logging.error(bugName + ' no failed test initially')
            #     return ''
            # output += '@fail:' + ','.join(pre_failure) + '@total:' + str(total) + ', ' + '@newtotal:' + str(len(validTests)) + ', '
            #
            # fixBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), postId)
            # for ob in fixBugs:
            #     filepath = ob.split('diffs')[-1]
            #     cmd = 'cp /experiment/diffs' + filepath + ' src' + filepath.replace('-' + postId, '')
            #     client.containers.exec(container=container, command=cmd, context='/experiment/')
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
            #     post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client,validTests)
            #
            #     output += ','.join(post_failure) + ' '
            #     if len(post_failure) == 0:
            #         # times += 1
            #         fix = 'success'
            #         # print("fix {} by {}".format(bugName, patch_name))
            #         output += 'fix {}  '.format(bugName)
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
            # docker stop $(docker ps -q)

def validateCore(t):
    bugName, port = t
    container = None
    with bugzoo.server.ephemeral(port=port, verbose=False, timeout_connection=30000) as client:
        try:

            bug = client.bugs[bugName]
            if client.bugs.is_installed(bug):
                pass
            else:
                client.bugs.build(bug)
            fix = 'failure'
            output = ''
            output += 'bugName:' + bugName + ', '
            container = client.containers.provision(bug)
            output += 'First_test:'


            preId, postId = bugName.split(':')[-1].split('-')[-2:]

            failing = join(DATA_PATH, 'manybugs', bugName, 'failing.tests.txt')
            passing = join(DATA_PATH, 'manybugs', bugName, 'passing.tests.txt')

            if not (isfile(failing)):
                return

            cmd = 'docker cp ' +failing+ ' '+container.id +':/experiment/failing.tests.txt '
            o,e = shellGitCheckout(cmd)
            cmd = 'sudo chown $(whoami):$(whoami) "{}"'
            cmd = cmd.format('failing.tests.txt')
            o = client.containers.exec(container=container, command=cmd, context='/experiment/')
            cmd = 'docker cp ' +passing+ ' '+container.id +':/experiment/passing.tests.txt '
            o,e = shellGitCheckout(cmd)
            cmd = 'sudo chown $(whoami):$(whoami) "{}"'
            cmd = cmd.format('passing.tests.txt')
            o = client.containers.exec(container=container, command=cmd, context='/experiment/')

            originalBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), preId)
            for ob in originalBugs:
                filepath = ob.split('diffs')[-1]
                cmd = 'cp diffs' + filepath + ' src' + filepath.replace('-' + preId, '')
                client.containers.exec(container=container, command=cmd, context='/experiment/')

            validTests = readTestSuite(join(DATA_PATH, 'manybugs', bugName, 'test.sh'))
            client.containers.build(container)
            pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all(bug, container, client,validTests)
            if pre_failure == 0:
                logging.error(bugName + ' no failed test initially')
                return ''
            output += '@fail:' + ','.join(pre_failure) + '@total:' + str(total) + ', ' + '@newtotal:' + str(len(validTests)) + ', '

            fixBugs = get_filepaths(join(DATA_PATH, 'manybugs', bugName, 'diffs'), postId)
            for ob in fixBugs:
                filepath = ob.split('diffs')[-1]
                cmd = 'cp diffs' + filepath + ' src' + filepath.replace('-' + postId, '')
                client.containers.exec(container=container, command=cmd, context='/experiment/')
            client.containers.build(container)
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
            post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client,validTests)
            output += ','.join(post_failure) + ' '
            if len(post_failure) == 0:
                # times += 1
                fix = 'success'
                # print("fix {} by {}".format(bugName, patch_name))
                output += 'fix {}  '.format(bugName)


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

    buglist = listdir(join(DATA_PATH,'manybugs'))

    # for i in range(0,len(bugList)):
    bugList = []
    port = 6000
    for b in buglist:
        if b== '.DS_Store':
            continue
        t = b, port
        if (b.startswith('manybugs:php:')):
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
    with open(join(DATA_PATH, 'mayBugsValidateNew'), 'w',
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

def test_all(bug, container,  client,validTests):
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
