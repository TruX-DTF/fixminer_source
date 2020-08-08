import bugzoo
from bugzoo import server, Container
import csv
import os
from common.commons import *
import signal
DATA_PATH = os.environ["DATA_PATH"]
ROOT_DIR = os.environ["ROOT_DIR"]

BUGDIR = join(DATA_PATH,'manybugs')
PATCHES_DIR = join(DATA_PATH,'manybugs_sos')

def patch_validate(t):
    bugName, port = t
    # with bugzoo.server.ephemeral(port=8080,verbose=True,timeout_connection=3000) as client:
        # url = "http://127.0.0.1:6060"
        # client = bugzoo.Client(url)
        # bug = client.bugs['introclass:checksum:08c7ea:006']
    # client,process = getClient(port)
    container = None

    cmd = 'bash {} {}'.format(join(ROOT_DIR, 'data', 'startBugzoo.sh'), port)

    with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True) as process:
        # o,e = shellGitCheckout(cmd)
        url = "http://127.0.0.1:{}".format(port)
        timeout_connection = 3000
        client = Client(url, timeout_connection=timeout_connection)

        try:
            # bugName = 'manybugs:python:69223-69224'
            if bugName == 'manybugs:php:2011-03-25-8138f7de40-3acdca4703' or bugName == 'manybugs:php:2011-10-31-c4eb5f2387-2e5d5e5ac6':
                return

            #get tests-list
            group = bugName.split(':')[1]
            tail = bugName.split(':')[2]
            test_path = 'data/tests-list/' + group + '/' + group + '-bug-' + tail + '/' + 'tests-list.txt'
            test_path = 'data/tests-package/' + group + '/' + group + '-bug-' + tail + '/' + 'tests-list.txt'
            # test_path = 'data/tests-list-tian/' + group + '/' + group + '-bug-' + tail + '/' + 'tests-list.txt'
            test_path = join(DATA_PATH,'tests-list-tian',group + '/' + group + '-bug-' + tail + '/' + 'tests-list.txt')
            # test_path = 'data/tests-list-tian/' + group + '/' + group + '-bug-' + tail + '/' + 'tests-list.txt'
            tests_list = []
            if not os.path.exists(test_path):
                # print("no tests-list!")
                return
            output = ''
            output += "bugName: {}".format(bugName)

            with open(test_path,'r') as f:
                for line in f.readlines():
                    tests_list.append(line.strip())

            bug = client.bugs[bugName]
            if client.bugs.is_installed(bug):
                # print("the image is installed! :-)")
                pass
            else:
                client.bugs.build(bug)
                # client.bugs.download(bug)
                # print("the image is not installed :'(")

            # print("creating container...")
            container = client.containers.provision(bug)
            # print("container is ready")


            output = patch_application_our(client, container,bug,tests_list,output)
            print(output)
            return output

        except Exception as e:
            print(e)
            # continue
        finally:
            cmd = 'docker stop {}'.format(container.id)
            out, e = shellGitCheckout(cmd)
            client.shutdown()
            os.killpg(process.pid, signal.SIGTERM)

from bugzoo import Patch, Client

def test_part(bug, container,  client, tests_list):
    test_outcomes = []  # type: Dict[TestCase, TestOutcome]
    failure_cases = []
    failure = 0
    total = len(tests_list)
    if bug.name.startswith('manybugs:wireshark'):
        cmd = ' ./autogen.sh && ./configure && make'
        o = client.containers.exec(container=container, command=cmd, context='/experiment/src', time_limit=120)

        cmd = 'sudo mkdir scenario && sudo cp -r src/ scenario'
        o = client.containers.exec(container=container, command=cmd, context='/experiment', time_limit=120)
    for test in tests_list:
        cmd = './test.sh ' + test
        out = client.containers.exec(container=container, command=cmd, context='/experiment/', time_limit=120)
        # cmd = 'docker exec ' + container.id + ' ' + cmd
        # o,e = shellGitCheckout(cmd)
        # if test.expected_outcome != test_outcomes[test].passed:
        if 'PASS' not in out.output or out.code != 0:
            # o, e = shellGitCheckout(cmd)
            out = client.containers.exec(container=container, command=cmd, context='/experiment/', time_limit=120)
            if 'PASS' not in out.output or out.code != 0:
                failure += 1
                failure_cases.append(test)
                test_outcomes.append(out.output)
                break
    return failure_cases, failure, total, test_outcomes

# def test_all(bug, container,  client):
#     test_outcomes = {}  # type: Dict[TestCase, TestOutcome]
#     failure_cases = []
#     failure = 0
#     total = len(bug.tests._tests)
#     for test in bug.tests:
#         test_outcomes[test] = client.containers.test(container, test)
#         # if test.expected_outcome != test_outcomes[test].passed:
#         if test_outcomes[test].passed != True:
#             failure += 1
#             failure_cases.append(test.name)
#             break
#     return failure_cases, failure, total, test_outcomes

# def get_diff_sos(bug, client, container):
#     patch_list = []
#     group = bug.split(':')[1]
#     path = './data/manybugs_sos_patches/' + group + '/' + group + '-bug-' + bug.split(':')[2]
#     for root, dirs, files in os.walk(path):
#         for name in files:
#             if name == 'sos.patch':
#                 patch_list.append(os.path.join(root, name))
#
#     diff_files = []
#     for patch_file in patch_list:
#         unidiff = ''
#         with open(patch_file,'r') as file:
#             for line in file.readlines():
#                 if group == 'wireshark':
#                     if line.startswith('---'):
#                         # begin = line.index('diffs') + 6
#                         begin = line.index('a')
#                         unidiff += '--- ' + line[begin+2:]
#                     elif line.startswith('+++'):
#                         # begin = line.index('diffs') + 6
#                         begin = line.index('b')
#                         unidiff += '+++ ' + line[begin+2:]
#                     else:
#                         unidiff += line
#                 else:
#                     if line.startswith('---'):
#                         # begin = line.index('diffs') + 6
#                         ends = line.index('_orig')
#                         unidiff += line[:ends]+ ' \n'
#                     else:
#                         unidiff += line
#             diff_files.append(unidiff.strip())
#     return diff_files

# def get_diff_our(bug, client, container):
#     patch_list = []
#     group = bug.split(':')[1]
#     patches_path = './data/manybugs_anil_patches1/' + bug + '/patches'
#     for root, dirs, files in os.walk(patches_path):
#         for name in files:
#             if name.endswith('.txt'):
#                 patch_list.append(os.path.join(root, name))
#
#     diff_files = {}
#     for patch_file in patch_list:
#         unidiff = ''
#         with open(patch_file,'r') as file:
#             for line in file.readlines():
#                 if line.startswith('---'):
#                     begin = line.index('/diffs')
#                     unidiff += '--- /experiment' + line[begin:]
#                 elif line.startswith('+++'):
#                     begin = line.index('diffs') + 6
#                     ends = line.index('.c-')
#                     unidiff += line[:4] + line[begin:ends] + '.c' + '\n'
#                 else:
#                     unidiff += line
#             diff_files[patch_file.split('/')[-1]] = unidiff.strip()
#     return diff_files

# def get_diff(bug, client, container):
#     patch_list = []
#     path = './data/manybugs/' + bug + '/diffs'
#     for root, dirs, files in os.walk(path):
#         for name in files:
#             if name.endswith('.patch'):
#                 patch_list.append(os.path.join(root, name))
#
#     diff_files = []
#     for patch_file in patch_list:
#         unidiff = ''
#         with open(patch_file,'r') as file:
#             for line in file.readlines():
#                 if line.startswith('---'):
#                     begin = line.index('diffs') + 6
#                     ends = line.index('.c-')
#                     unidiff += line[:4] + line[begin:ends] + '.c' + '\n'
#                     preFile = line[begin:line.index('\t')]
#                 elif line.startswith('+++'):
#                     begin = line.index('diffs') + 6
#                     ends = line.index('.c-')
#                     unidiff += line[:4] + line[begin:ends] + '.c' + '\n'
#                 else:
#                     unidiff += line
#             cmd = 'cp '+ '../diffs/' + preFile + ' ' + preFile[:preFile.index('.c')+2]
#             # cmd += ' & ' + 'make -j$(nproc)'
#             client.containers.exec(container=container, command=cmd, context='/experiment/src')
#             diff_files.append(unidiff.strip())
#
#     return diff_files

# def patch_ours_test(bug, bugName, container, client, tests_list):
#     patch_target = []
#     path = './data/manybugs/' + bugName + '/diffs'
#     for root, dirs, files in os.walk(path):
#         for name in files:
#             if name.endswith('.patch'):
#                 file_path = os.path.join(root, name[:name.index('.patch')])
#                 patch_target.append(file_path[file_path.index('diffs')+6:])
#     if len(patch_target) != 1:
#         print('exceed! ')
#         return
#     patched = []
#     patched_path = './data/manybugs_tian_1/' + bugName + '/patched'
#     for root, dirs, files in os.walk(patched_path):
#         for name in files:
#             if name.endswith('.c'):
#                 patched.append(os.path.join(root, name))
#     if len(patched) == 0:
#         print('no patch! ')
#         return
#     times = 0
#     for p in patched:
#         if not p.endswith('origin.c'):
#             continue
#         cmd = 'docker cp ' + p + ' ' + container.id + ':/experiment/src/' + patch_target[0]
#         o, e = shellGitCheckout(cmd)
#         client.containers.build(container)
#         failure_cases, failure, total, test_outcomes = test_part(bug, container, client, tests_list)
#         if failure == 0:
#             times += 1
#             print('fixed by {}'.format(p),end=' ')
#         else:
#             print('{} failed'.format(failure_cases),end=' ')
#     print('times:{}'.format(times),end=' ')
#     if times > 0:
#         print('success ')
#     else:
#         print('failure ')


# def patch_application(client: Client, container: Container, diff_files: list) -> None:
#     result = None
#     for unidiff in diff_files:
#         if len(unidiff) < 5:
#             continue
#         # first, we build a Patch object using a unified-format diff
#         patch = Patch.from_unidiff(unidiff)
#
#         # we then attempt to apply the patch to the source code
#         re = client.containers.patch(container, patch)
#         if not re:
#             return False
#         else:
#             result = True
#     # finally, we rebuild the program inside the container
#     client.containers.build(container)
#
#     return result

def patch_application_our(client: Client, container: Container,bug,tests_list,output):
    result = None
    times = 0
    # patches_path = './data/manybugs_tian_patches4topC/' + bug.name + '/patches'
    patches_path = join(PATCHES_DIR,bug.name,'patches')
    # patches_path = './data/manybugs_tian_patches4topC/' + bug.name + '/patches'
    for root, dirs, files in os.walk(patches_path):
        for name in files:
            if name.endswith('.txt'):
                patch_file = os.path.join(root, name)
                unidiff = ''
                with open(patch_file, 'r') as file:
                    for line in file.readlines():
                        if line.startswith('---'):
                            begin = line.index('/diffs')
                            origin_begin = line.index('diffs') + 6
                            unidiff += '--- /experiment' + line[begin:]
                            preFile = line[origin_begin:line.index('\n')]
                        elif line.startswith('+++'):
                            begin = line.index('diffs') + 6
                            ends = line.index('.c-')
                            unidiff += line[:4] + line[begin:ends] + '.c' + '\n'
                        else:
                            unidiff += line
                    cmd = 'cp ' + '../diffs/' + preFile + ' ' + preFile[:preFile.index('.c') + 2]
                    # cmd += ' & ' + 'make -j$(nproc)'
                    client.containers.exec(container=container, command=cmd, context='/experiment/src')
                    diff_files = unidiff.strip()

                    if len(unidiff) < 5:
                        continue
                    # first, we build a Patch object using a unified-format diff
                    patch = Patch.from_unidiff(unidiff)

                    # we then attempt to apply the patch to the source code
                    re = client.containers.patch(container, patch)
                    if not re:
                        output += ' {} patch failed'.format(name)
                    else:
                        result = True
                        # finally, we rebuild the program inside the container
                        patch_result = client.containers.build(container)
                        if patch_result.successful:
                            failure_cases, failure, total, test_outcomes = test_part(bug, container, client, tests_list)
                            if failure == 0:
                                times += 1
                                output+= ' fixed by {}'.format(name)
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


def getClient(port):
    cmd = 'bash {} {}'.format(join(DATA_PATH, 'startBugzoo.sh'), port)

    with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True) as process:
        # o,e = shellGitCheckout(cmd)
        url = "http://127.0.0.1:{}".format(port)
        timeout_connection = 3000
        client = Client(url, timeout_connection=timeout_connection)
        return client,process

def validate():

    buglist = ['manybugs:gmp:14166-14167', 'manybugs:gzip:2009-08-16-3fe0caeada-39a362ae9d',
                 'manybugs:gzip:2009-10-09-1a085b1446-118a107f2d', 'manybugs:gzip:2010-02-19-3eb6091d69-884ef6d16c',
                 'manybugs:libtiff:2005-12-21-3b848a7-3edb9cd', 'manybugs:libtiff:2006-03-03-a72cf60-0a36d7f',
                 'manybugs:libtiff:2006-03-03-eec4c06-ee65c74', 'manybugs:libtiff:2007-07-13-09e8220-f2d989d',
                 'manybugs:libtiff:2007-11-02-371336d-865f7b2', 'manybugs:libtiff:2009-02-05-764dbba-2e42d63',
                 'manybugs:libtiff:2009-08-28-e8a47d4-023b6df', 'manybugs:libtiff:2010-11-27-eb326f9-eec7ec0',
                 'manybugs:libtiff:2006-02-23-b2ce5d8-207c78a', 'manybugs:lighttpd:2661-2662',
                 'manybugs:lighttpd:2254-2259', 'manybugs:lighttpd:2785-2786', 'manybugs:lighttpd:1948-1949',
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

    port = 6000
    bugList = []
    for b in buglist:
        if b == '.DS_Store':
            continue
        t = b, port
        bugList.append(t)
        if port == 6300:
            port = 6000
        port += 1

    results = parallelRunMerge(patch_validate,bugList)

    # #
    with open(join(DATA_PATH, 'manyBugsResults'), 'w',
              encoding='utf-8') as writeFile:
        # if levelPatch == 0:
        writeFile.write('\n'.join(results))
    # patch_validate()
    # pass
