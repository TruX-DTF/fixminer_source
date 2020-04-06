import bugzoo
from bugzoo import server, Container
from common.commons import *

from common.commons import shellGitCheckout
DATA_PATH = os.environ["DATA_PATH"]
introClassFile = join(DATA_PATH,'introClassData.txt')

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
                path = os.path.abspath('data') + '/introclass_patched/' + bugName
                patch_path = path + '/patches'
                avaliable_patch = os.path.abspath('data') + '/introclass2/' + bugName + '/' + 'patches'
                patch_names = os.listdir(patch_path)
                for patch_name in patch_names:
                    # if patch_name not in os.listdir(avaliable_patch):
                    #     continue
                    patch = patch_path + '/' + patch_name
                    patch_result = patched_application(path, bug.name, patch, client, container)
                    if patch_result == -1 or patch_result.code != 0:
                        print("@{}@".format('False'), end='')
                        print("{}".format('F'), end=' ')
                        continue
                    print("@{}@".format('True'), end='')

                    print("Second_test:",end=' ')
                    post_test_outcomes = {}
                    post_failure_cases, post_failure, total, post_test_outcomes = test_all(bug, container, client)
                    print("{}".format(post_failure), end=' ')
                    if post_failure == 0:
                        print("fix {} by {}".format(bugName, patch_name))
                    # print("@fail:{}@total:{}".format(post_failure, total),end=' ')
                    # print("@post_failure_cases:{}".format(post_failure_cases))

                cmd = 'docker rm -fv {}'.format(container.id)
                output, e = shellGitCheckout(cmd)
                print(" ")
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

def patch_application(path, bugName, patch, client: Client, container: Container):
    buggroup = bugName.split(":")[1]
    # path = join(BUGDIR,bug)
    program = path + '/' + buggroup + '.c'
    fixedFile = path + '/oracle.c'
    patchPath = patch
    with open(patch, 'r+', encoding='utf-8') as file:
        result = ''
        for line in file:
            if line.startswith('---'):
                # result += '--- ' + program + '\n'
                result += '--- ' + buggroup + '.c' + '\n'
            elif line.startswith('+++'):
                # result += '+++ ' + fixedFile + '\n'
                result += '+++ ' + 'oracle.c' + '\n'
            else:
                result += line
    with open(patch, 'w', encoding='utf-8') as file:
        file.writelines(result)

    cmd = 'rm  ' + fixedFile
    output, e = shellGitCheckout(cmd)

    cmd = 'patch --ignore-whitespace -p0 < ' + '\"' + patchPath + '\"'
    # cmd = 'patch  -p0 < ' + '\"' + patchPath + '\"'
    output, e = shellGitCheckout(cmd)

    if os.path.exists(fixedFile):
        cmd = 'docker cp ' + fixedFile + ' ' + container.id + ':/experiment/'
        output, e = shellGitCheckout(cmd)

        cmd = 'rm .genprog_test_cache.json & gcc -o {} {}.c'.format(buggroup, 'oracle')
        output = client.containers.exec(container=container, command=cmd, context='/experiment/')
        return output
    else:
        return -1

def patched_application(path, bugName, patched, client: Client, container: Container):
    buggroup = bugName.split(":")[1]
    # path = join(BUGDIR,bug)
    program = path + '/' + buggroup + '.c'
    fixedFile = patched.split('/')[-1]

    cmd = 'docker cp ' + patched + ' ' + container.id + ':/experiment/'
    output, e = shellGitCheckout(cmd)

    cmd = 'rm .genprog_test_cache.json & mv {} {}.c & gcc -o {} {}'.format(fixedFile,fixedFile, buggroup, fixedFile+'.c')
    output = client.containers.exec(container=container, command=cmd, context='/experiment/')
    return output


if __name__ == "__main__":
    patch_validate()
