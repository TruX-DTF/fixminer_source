from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
BUGDIR = join(DATA_PATH,'introclass')

introClassFile = join(DATA_PATH,'introClassData.txt')

def get_filepaths(directory):

    file_paths = []  # List which will store all of the full filepaths.\n,
    exclude = '.git'
    # Walk the tree.\n,
    for root, directories, files in os.walk(directory):
        directories[:] = [d for d in directories if d not in exclude]
        java = [i for i in files if not (i.endswith('-diff') or i.endswith('.patch'))]

        for filename in java:
            # Join the two strings in order to form the full filepath.\n,
            filepath = os.path.join(root, filename)
            file_paths.append(filepath)  # Add it to the list.\n,

    return file_paths  # Self-explanatory.\n,

def export():
    if not os.path.exists(join(BUGDIR)):
        os.mkdir(join(BUGDIR))
    # bugList = [i.replace(':', '-').replace('manybugs-', 'squareslab/manybugs:') for i in bugList]
    # exportCore(bugList[0])
    cmd = 'bugzoo bug list'
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    bugList = []
    for line in output.splitlines():
        if line == '':
            continue
        potentialId = line.split('|')[1].strip()
        if potentialId.startswith('introclass'):
            if 'Yes' in line:
                bugList.append(potentialId)

    # with open(introClassFile, 'r') as file:
    #     for line in file.readlines():
    #         bugList.append(line.strip())
    print("bugList length: {}".format(len(bugList)))
    for b in bugList:
        exportCore(b)
def exportCore(bugName):

    bugNameForDocker = bugName.replace(':', '-').replace('introclass-', 'squareslab/introclass:')
    cmd = 'docker images '+bugNameForDocker+' --format "{{.ID}}"'
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

    cmd = 'docker create -ti --name dummy '+output.strip()+' bash'
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

    if not os.path.exists(join(BUGDIR,bugName)):
        os.makedirs(join(BUGDIR,bugName,))

    cmd = 'docker cp dummy:/experiment/whitebox_test.sh ' + join(BUGDIR,bugName)
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

    cmd = 'docker cp dummy:/experiment/'+ bugName.split(':')[1] + '.c ' + join(BUGDIR,bugName)
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

    cmd = 'docker cp dummy:/experiment/oracle.c ' + join(BUGDIR, bugName)
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

    cmd = 'docker rm -fv dummy'
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

    homework = join(BUGDIR, bugName) + '/' + bugName.split(':')[1] + '.c'
    patchName = join(BUGDIR, bugName) + '/oracle.c'
    cmd = 'diff -u ' + homework + ' ' + ' ' + patchName + '  > ' +patchName +'.patch'
    logging.info(cmd)
    output, e = shellGitCheckout(cmd)
    logging.info(output)

