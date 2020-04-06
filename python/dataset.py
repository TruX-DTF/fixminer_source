from common.commons import *

DATA_PATH = os.environ["DATA_PATH"]
REPO_PATH = os.environ["REPO_PATH"]
def prepareFiles(t):
    try:
        sha, repoName = t

        shaOld = sha + '^'

        repo = join(REPO_PATH,repoName)
        gumInputRepo = join(DATA_PATH, 'gumInput', repoName)
        if not os.path.exists(join(gumInputRepo)):
            os.makedirs(gumInputRepo)

        cmd = 'git -C ' + repo + ' diff --name-only ' + shaOld + '..'+sha

        output, errors = shellGitCheckout(cmd, 'latin1')
        files = output.strip().split('\n')

        # if len(nonJava) > 0:
        #     logging.warning('Skipping commit %s',sha)
        #     return
        # if len(files) != 1:
        #     return

        nonTest = [f for f in files if not re.search('test', f, re.I) and f.endswith('java')]

        if len(nonTest) > 1:
            return

        cmd = 'git -C ' + repo + ' rev-parse --short=6 ' + shaOld

        output, errors = shellGitCheckout(cmd, 'latin1')
        shaOld = output.strip()

        cmd = 'git -C ' + repo + ' rev-parse --short=6 ' + sha
        output, errors = shellGitCheckout(cmd, 'latin1')
        sha = output.strip()

        if isinstance(nonTest, list):
            for file in nonTest:
                checkoutFiles(sha,shaOld, repoName, file,'gumInput')



    except Exception as e:
        print(e)


def prepareFilesDefects4J(repo,repoName,shaOld):
    try:
        # sha, repoName = t

        sha = shaOld + '^'

        # repo = join(REPO_PATH,repoName)
        gumInputRepo = join(DATA_PATH, 'Defects4J2', repoName)
        if not os.path.exists(join(gumInputRepo)):
            os.makedirs(gumInputRepo)

        cmd = 'git -C ' + repo + ' diff --name-only ' + shaOld + '..'+sha
        output, errors = shellGitCheckout(cmd, 'latin1')
        files = output.strip().split('\n')
        # nonJava = [f for f in files if not f.endswith('.java')]
        nonTest = [f for f in files if not re.search('test',f,re.I)]
        # if len(nonJava) > 0:
        #     logging.warning('Skipping commit %s',sha)
        #     return
        # if len(files) != 1:
        #     return

        nonTest = [f for f in files if not re.search('test', f, re.I) and f.endswith('java')]

        if len(nonTest) > 1:
            return

        cmd = 'git -C ' + repo + ' rev-parse --short=6 ' + shaOld
        output, errors = shellGitCheckout(cmd, 'latin1')
        shaOld = output.strip()

        cmd = 'git -C ' + repo + ' rev-parse --short=6 ' + sha
        output, errors = shellGitCheckout(cmd, 'latin1')
        sha = output.strip()

        if isinstance(nonTest, list):
            for file in nonTest:
                checkoutFiles(sha,shaOld, repoName, file,'Defects4J2',repo)


    except Exception as e:
        print(e)




def checkoutFiles(sha,shaOld,repoName, filePath,type, repo=None):
    try:
        # folderDiff = join(DATA_PATH, 'gumInput',repoName, 'DiffEntries')
        folderDiff = join(DATA_PATH, type,repoName, 'DiffEntries')
        folderPrev = join(DATA_PATH, type,repoName, 'prevFiles')
        folderRev = join(DATA_PATH, type,repoName, 'revFiles')
        if not os.path.exists(folderDiff):
            os.mkdir(folderDiff)

        if not os.path.exists(folderPrev):
            os.mkdir(folderPrev)

        if not os.path.exists(folderRev):
            os.mkdir(folderRev)

        if repo is None:
            repo = join(REPO_PATH,repoName)


        savePath = filePath.replace('/','#')
        if not isfile(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath.replace('.java', '.txt')):

            cmd = 'git -C ' + repo + ' diff -U ' + shaOld + ':' + filePath + '..' + sha + ':' + filePath  # + '> ' + folderDiff + '/' + sha + '_' + shaOld + '_' + savePath.replace('.java','.txt')

            output,errors = shellGitCheckout(cmd,'latin1')
            if errors:
                # print(errors)
                raise FileNotFoundError

            regex = r"@@\s\-\d+,*\d*\s\+\d+,*\d*\s@@ ?(.*\n)*"
            match = re.search(regex, output)
            if not match:
                return
                # print()
            not_matched, matched = output[:match.start()], match.group()
            numberOfHunks = re.findall('@@\s\-\d+,*\d*\s\+\d+,*\d*\s@@', matched)
            if len(numberOfHunks) == 0:
                return
            diffFile = shaOld + '\n' + matched.replace(' @@ ', ' @@\n')
            with open(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath.replace('.java', '.txt'),
                      'w') as writeFile:
                writeFile.writelines(diffFile)



            cmd = 'git -C ' + repo + ' show ' + sha + ':' + filePath + '> ' + folderRev + '/' + sha + '_' + shaOld + '_' +savePath

            if errors:
                # print(errors)
                raise FileNotFoundError
            o,errors= shellGitCheckout(cmd,'latin1')
            cmd = 'git -C ' + repo + ' show ' + shaOld + ':' + filePath + '> ' + folderPrev + '/' + 'prev_'+sha + '_' + shaOld + '_' +savePath
            if errors:
                # print(errors)
                raise FileNotFoundError

            o,errors = shellGitCheckout(cmd,'latin1')
            if errors:
                # print(errors)
                raise FileNotFoundError

    except FileNotFoundError as fnfe:
        if isfile(folderRev + '/' + sha + '_' + shaOld + '_' +savePath):
            os.remove(folderRev + '/' + sha + '_' + shaOld + '_' +savePath)
        if isfile(folderPrev + '/' + 'prev_'+sha + '_' + shaOld + '_' +savePath):
            os.remove(folderPrev + '/' + 'prev_'+sha + '_' + shaOld + '_' +savePath)
        if isfile(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath.replace('.java','.txt')):
            os.remove(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath.replace('.java','.txt'))
        # print(fnfe)
        # raise Exception(fnfe)
    except Exception as e:
        # print(e)
        raise Exception(e)
