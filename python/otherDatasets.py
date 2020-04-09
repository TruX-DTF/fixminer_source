from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
COMMIT_DFS = os.environ["COMMIT_DFS"]
# DATASET_PATH = '/Users/anilkoyuncu/projects/datasets'
DATASET_PATH = os.environ["REPO_PATH"]
DATASET = os.environ["dataset"]
ROOT = os.environ["ROOT_DIR"]
PROJECT_LIST = os.environ["PROJECT_LIST"]

def filetype_fileter(filename):
    # return filename.endswith(u'.java') and not bool(re.search('test.*\/', filename))
    return filename.endswith(u'.c') or filename.endswith(u'.h')



def checkoutFiles(sha,shaOld, filePath,type, repo=None):
    try:
        # folderDiff = join(DATA_PATH, 'gumInput',repoName, 'DiffEntries')
        folderDiff = join(type, 'DiffEntries')
        folderPrev = join(type, 'prevFiles')
        folderRev = join( type, 'revFiles')
        if not os.path.exists(folderDiff):
            os.mkdir(folderDiff)

        if not os.path.exists(folderPrev):
            os.mkdir(folderPrev)

        if not os.path.exists(folderRev):
            os.mkdir(folderRev)

        # if repo is None:
        #     repo = join(REPO_PATH,repoName)


        savePath = filePath.replace('/','#')

        if not isfile(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath + '.txt'):

            cmd = 'git -C ' + repo + ' diff -U ' + shaOld + ':' + filePath + '..' + sha + ':' + filePath  # + '> ' + folderDiff + '/' + sha + '_' + shaOld + '_' + savePath.replace('.java','.txt')

            output,errors = shellGitCheckout(cmd,enc='latin1')
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

            with open(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath + '.txt',
                      'w') as writeFile:
                writeFile.writelines(diffFile)



            cmd = 'git -C ' + repo + ' show ' + sha + ':' + filePath + '> ' + folderRev + '/' + sha + '_' + shaOld + '_' +savePath

            if errors:
                # print(errors)
                raise FileNotFoundError
            o,errors= shellGitCheckout(cmd,enc='latin1')
            cmd = 'git -C ' + repo + ' show ' + shaOld + ':' + filePath + '> ' + folderPrev + '/' + 'prev_'+sha + '_' + shaOld + '_' +savePath
            if errors:
                # print(errors)
                raise FileNotFoundError

            o,errors = shellGitCheckout(cmd,enc='latin1')
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


def prepareFiles(t,dsName):
    try:
        sha,files = t

        shaOld = sha + '^'
        # repo = '/Users/anil.koyuncu/projects/linux'
        # repo = join(REPO_PATH,repoName)
        gumInputRepo = join(DATASET,dsName)
        if not os.path.exists(join(gumInputRepo)):
            os.makedirs(gumInputRepo)

        # cmd = 'git -C ' + repo + ' diff --name-only ' + shaOld + '..'+sha
        #
        # output, errors = shellGitCheckout(cmd, 'latin1')
        # files = output.strip().split('\n')

        # if len(nonJava) > 0:
        #     logging.warning('Skipping commit %s',sha)
        #     return
        # if len(files) != 1:
        #     return

        # nonTest = [f for f in files if not re.search('test', f, re.I) and f.endswith('.c') or f.endswith(u'.h')]
        #
        # if len(nonTest) > 1:
        #     return

        nonTest = []
        for k,v in files.items():
            if v == 'M':
                nonTest.append(k)
                # if k.endswith('.c') or k.endswith(u'.h'):
                #    nonTest.append(k)
        # nonTest = [f for f in files.keys() if f.endswith('.c') or f.endswith(u'.h')]

        cmd = 'git -C ' + join(DATASET_PATH,dsName) + ' rev-parse --short=6 ' + shaOld

        output, errors = shellGitCheckout(cmd, enc='latin1')
        shaOld = output.strip()

        cmd = 'git -C ' + join(DATASET_PATH,dsName) + ' rev-parse --short=6 ' + sha
        output, errors = shellGitCheckout(cmd, enc='latin1')
        sha = output.strip()

        if isinstance(nonTest, list):
            for file in nonTest:
                checkoutFiles(sha,shaOld,  file,gumInputRepo,join(DATASET_PATH,dsName))



    except Exception as e:
        print(e)


def checkCommitLog(x,dsName):
    # repo = '/Users/anil.koyuncu/projects/linux'
    cmd= 'git -C ' + join(DATASET_PATH,dsName) + ' show ' + x + " --pretty=\"format:\" --name-status -M100%"

    out, err = shellGitCheckout(cmd, enc='latin1')
    log = {}
    lines = out.strip().split('\n')
    for line in lines:
        fname = line[2:].strip()
        ftype = line[:1]
        log[fname] = ftype
    log
    df = pd.DataFrame(data=[[log,  x]], columns=['files', 'commit'])
    return df

def getCommitLog(x,dsName):
    # repo = '/Users/anil.koyuncu/projects/linux'
    # commit, repo = x

    cmd = 'git -C ' + join(DATASET_PATH,dsName) + '/ ' + " show --pretty=format:'%B' --no-patch " + x

    output = shellCallTemplate(cmd, 'latin-1')

    # matches = re.finditer(r"\bfix[a-zA-Z]*", output,re.I)
    matches = re.finditer(r"\bfix[a-zA-Z]*|\bbug[a-zA-Z]*", output,re.I)
    match = list(matches)
    fixes = []
    if len(match) >= 1:
        for m in match:
            fixes.append(m.group())
    #     return match[0].group()

    # matches = re.finditer('http[s]?:\/\/.*show_bug\.cgi\?id=[0-9]*',output)
    # match = list(matches)
    # links = []
    # if len(match) >= 1:
    #     for m in match:
    #         links.append(m.group())

    df = pd.DataFrame(data=[[fixes, output,x]], columns=['fixes','log','commit'])
    # df = df.T
    # df.columns = ['log', 'commit']

    return df



    output

def collectBugFixPatches(dsName):
    commits = getAllCommits(dsName)
    # remove commits that are only deleting or adding files
    commits = commits[commits.files.apply(lambda x: np.any([i == 'M' for i in x.values()]))]
    # keep only commits that are changing c files (.c)
    commits = commits[commits.files.apply(lambda x: np.all([i.endswith('.c') for i in x.keys()]))]
    #not a revert commit
    # commits = commits[~commits.log.apply(lambda x: x.startswith('Revert'))]
    # commits = commits[commits.files.apply(lambda x: len(x) == 1)]
    # commits['cocci'] = commits.log.apply(lambda x: True if re.search('cocci|coccinelle', x) else False)
    # coccis = commits[commits.cocci].commit.values.tolist()
    fixes = commits[commits.fixes.str.len()!=0].commit.values.tolist()
    # links = commits[commits.links.str.len()!=0].commit.values.tolist()

    # bugs = set(fixes).union(links).union(coccis)
    # bugs = set(fixes)#.union(coccis)
    commits = commits[commits.commit.isin(fixes)]
    print(len(commits))
    # for s in a.commit.values.tolist():

    parallelRun(prepareFiles,commits[['commit','files']].values.tolist(),dsName)
        # prepareFiles(s)


def markBugFixingPatches(commits,dsName):
    # from pandarallel import pandarallel
    #
    # pandarallel.initialize()
    # commits['files'] = commits.commit.parallel_apply(checkCommitLog)
    # commits

    f = parallelRunMergeNew(checkCommitLog, commits['commit'].values.tolist(), dsName)
    res = pd.merge(commits, f, on=['commit'])
    commits=res
    #
    # # commits['isC'] = commits.files.apply(lambda x:np.any([i.endswith('.c') or i.endswith('.h') for i in x.keys() ]))
    # commits['isC'] = commits.files.apply(lambda x:np.all([i.endswith('.c') for i in x.keys() ]))
    #
    # commits = commits[commits.isC == True]

    # commits.commit.parallel_apply(getCommitLog)
    f = parallelRunMergeNew(getCommitLog, commits['commit'].values.tolist(),dsName)

    res = pd.merge(commits, f, on=['commit'])

    save_zipped_pickle(res, join(COMMIT_DFS, dsName+'Fix' + ".pickle"))
    return res


def getAllCommits(datasetName):
    if isfile(join(COMMIT_DFS,datasetName+'Fix.pickle')):
        return load_zipped_pickle(join(COMMIT_DFS,datasetName+'Fix.pickle'))
    else:

        if isfile(join(COMMIT_DFS,datasetName+'.pickle')):
            commits =  load_zipped_pickle(join(COMMIT_DFS,datasetName+'.pickle'))
        else:
            if not os.path.exists(COMMIT_DFS):
                os.mkdir(COMMIT_DFS)


            cmd = 'git -C ' + join(DATASET_PATH,datasetName) + " log --no-merges --pretty=format:'{\"commit\":\"%H\",\"commitDate\":\"%ci\",\"title\":\"%f\",\"committer\":\"%ce\"}' > " + join(COMMIT_DFS,datasetName + '.commits')
            output = shellCallTemplate(cmd, enc='latin1')

            from commitCollector import makeDF
            rDF = makeDF(join(COMMIT_DFS,datasetName + '.commits'))
            save_zipped_pickle(rDF, join(COMMIT_DFS, datasetName + ".pickle"))
            # return rDF
            commits = rDF
        return markBugFixingPatches(commits,datasetName)


def core():
    datasets = pd.read_csv(join(ROOT,'data', 'datasets.csv'))
    # repoList = ['FFmpeg','curl','nginx','openssl','redis','tmux','vlc']

    pjList = PROJECT_LIST.split(',')
    if not os.path.exists(DATASET_PATH):
        os.mkdir(DATASET_PATH)

    for repo,src in datasets.values.tolist():
        if(pjList != ['ALL']):
            if repo in pjList:
                 print(repo)
                 cmd = 'git config --global http.postBuffer 157286400'
                 shellCallTemplate(cmd)
                 cmd = 'git -C ' + DATASET_PATH + ' clone ' + src
                 shellCallTemplate(cmd)
                 logging.info(repo)
                 collectBugFixPatches(repo)
        else:
            cmd = 'git -C ' + DATASET_PATH + ' clone ' + src
            shellCallTemplate(cmd)
            logging.info(repo)
            collectBugFixPatches(repo)

def codeflaws():
    cf = listdir(join(DATASET_PATH,'codeflaws'))

    type = join(DATASET,'codeflaws')
    folderDiff = join(type, 'DiffEntries')
    folderPrev = join(type, 'prevFiles')
    folderRev = join(type, 'revFiles')
    if not os.path.exists(folderDiff):
        os.makedirs(folderDiff)

    if not os.path.exists(folderPrev):
        os.makedirs(folderPrev)

    if not os.path.exists(folderRev):
        os.makedirs(folderRev)
    cfBugs = [i for i in cf if os.path.isdir(join(DATASET_PATH,'codeflaws',i))]
    for cfBug in cfBugs:
        bugs = [i for i in listdir(join(DATASET_PATH,'codeflaws',cfBug)) if i.endswith('.c')]
        bugs.sort()
        if len(bugs) == 2:
            s1 = bugs[0].replace('.c', '').split('-')
            s2 = bugs[1].replace('.c', '').split('-')
            prev = s1[-1]
            rev = s2[-1]
            bugName = '-'.join(s1[: -1])
            shutil.copy(join(DATASET_PATH,'codeflaws',cfBug,bugs[0]),join(folderPrev,"prev_"+bugName+"-"+prev+"-"+rev+'.c'))
            shutil.copy(join(DATASET_PATH,'codeflaws',cfBug,bugs[1]),join(folderRev,bugName+"-"+prev+"-"+rev+'.c'))
            cmd = 'diff -u ' + join(DATASET_PATH,'codeflaws',cfBug,bugs[0]) + ' ' + join(DATASET_PATH,'codeflaws',cfBug,bugs[1])+ ' > ' + join(folderDiff,bugName+"-"+prev+"-"+rev+'.c.txt')
            logging.info(cmd)
            output, e = shellGitCheckout(cmd)
            logging.info(output)
        else:
            print()

