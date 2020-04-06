import bugzoo
from bugzoo import server, Container
import csv
import os

introClassFile = './data/introClassData.txt'

def patch_validate():
    with bugzoo.server.ephemeral(port=8081,verbose=True,timeout_connection=3000) as client:
        # url = "http://127.0.0.1:6060"
        # client = bugzoo.Client(url)
        # bug = client.bugs['introclass:checksum:08c7ea:006']
        bugList = []
        with open(introClassFile,'r') as file:
            for line in file.readlines():
                bugList.append(line.strip())

        for i in range(0,len(bugList)):
            try:
                bugName = bugList[i]
                # if bugName != 'introclass:digits:070455:000':
                #     continue
                print("bugName: {}".format(bugName),end=' ')
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

                print("First_test:",end=' ')
                pre_test_outcomes = {}
                pre_failure_cases, pre_failure, total, pre_test_outcomes = test_all(bug, container, client)
                print("@fail:{}@total:{}".format(pre_failure, total),end=' ')
                print("@pre_failure_cases:{}".format(pre_failure_cases),end=' ')

                # print("patching...")
                diff_files = get_diff(bug.name, client, container)
                patch_result = patch_application(client, container, diff_files)
                if not patch_result:
                    print("@{}@".format(patch_result))
                    continue
                print("@{}@".format(patch_result), end=' ')

                print("Second_test:",end=' ')
                post_test_outcomes = {}
                post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client)
                print("@fail:{}@total:{}".format(post_failure, total),end=' ')
                print("@post_failure_cases:{}".format(post_failure_cases))
            except:
                print(" ")
                continue
        pass

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

def get_diff(bug, client, container):
    patch_list = []
    path = './data/introclass/' + bug
    for root, dirs, files in os.walk(path):
        for name in files:
            if name.endswith('.patch'):
                patch_list.append(os.path.join(root, name))

    diff_files = []
    for patch_file in patch_list:
        unidiff = ''
        with open(patch_file,'r') as file:
            for line in file.readlines():
                if line.startswith('\\'):
                    continue
                elif line.startswith('---'):
                    unidiff += '--- ' + bug.split(':')[1] + '.c\n'
                elif line.startswith('+++'):
                    unidiff += '+++ ' + bug.split(':')[1] + '.c\n'
                else:
                    unidiff += line
            diff_files.append(unidiff.strip())

    return diff_files



def patch_application(client: Client, container: Container, diff_files: list) -> None:
    result = None
    for unidiff in diff_files:
        if len(unidiff) < 5:
            continue
        # first, we build a Patch object using a unified-format diff
        patch = Patch.from_unidiff(unidiff)

        # we then attempt to apply the patch to the source code
        re = client.containers.patch(container, patch)
        if not re:
            return False
        else:
            result = True
    # finally, we rebuild the program inside the container
    client.containers.build(container)

    return result


if __name__ == "__main__":
    patch_validate()
