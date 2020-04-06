from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
COMMIT_DFS = os.environ["COMMIT_DFS"]
DATASET_PATH = '/Users/anil.koyuncu/projects/datasets'
repo = join(DATASET_PATH,'linux')

def filetype_fileter(filename):
    # return filename.endswith(u'.java') and not bool(re.search('test.*\/', filename))
    return filename.endswith(u'.c') or filename.endswith(u'.h')



def checkoutFiles(sha,shaOld, filePath,type, repo=None):
    try:
        # folderDiff = join(DATA_PATH, 'gumInput',repoName, 'DiffEntries')
        folderDiff = join(DATA_PATH, type, 'DiffEntries')
        folderPrev = join(DATA_PATH, type, 'prevFiles')
        folderRev = join(DATA_PATH, type, 'revFiles')
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

            with open(folderDiff + '/' + sha + '_' + shaOld + '_' + savePath + '.txt',
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


def prepareFiles(t):
    try:
        sha,files = t

        shaOld = sha + '^'

        # repo = join(REPO_PATH,repoName)
        gumInputRepo = join(DATA_PATH, 'gumInputLinux')
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
                if k.endswith('.c') or k.endswith(u'.h'):
                   nonTest.append(k)
        # nonTest = [f for f in files.keys() if f.endswith('.c') or f.endswith(u'.h')]

        cmd = 'git -C ' + repo + ' rev-parse --short=6 ' + shaOld

        output, errors = shellGitCheckout(cmd, 'latin1')
        shaOld = output.strip()

        cmd = 'git -C ' + repo + ' rev-parse --short=6 ' + sha
        output, errors = shellGitCheckout(cmd, 'latin1')
        sha = output.strip()

        if isinstance(nonTest, list):
            for file in nonTest:
                checkoutFiles(sha,shaOld,  file,'gumInputLinux',repo)



    except Exception as e:
        print(e)


def checkCommitLog(x):

    cmd= 'git -C ' + repo + ' show ' + x + " --pretty=\"format:\" --name-status -M100%"

    out, err = shellGitCheckout(cmd, enc='latin1')
    log = {}
    lines = out.strip().split('\n')
    for line in lines:
        fname = line[2:].strip()
        ftype = line[:1]
        log[fname] = ftype
    log
    return log

def getCommitLog(x):

    # commit, repo = x

    cmd = 'git -C ' + repo + '/ ' + " show --pretty=format:'%B' --no-patch " + x

    output = shellCallTemplate(cmd, 'latin-1')

    matches = re.finditer('Fixes: [0-9a-f]{6,40}', output)
    match = list(matches)
    fixes = []
    if len(match) >= 1:
        for m in match:
            fixes.append(m.group())
    #     return match[0].group()

    matches = re.finditer('http[s]?:\/\/.*show_bug\.cgi\?id=[0-9]*',output)
    match = list(matches)
    links = []
    if len(match) >= 1:
        for m in match:
            links.append(m.group())

    df = pd.DataFrame(data=[[links,fixes, output,x]], columns=['links','fixes','log','commit'])
    # df = df.T
    # df.columns = ['log', 'commit']

    return df



    output

def collectBugFixPatches():
    commits = getAllCommits()
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
    # bugs = set(fixes).union(coccis)
    bugs = set(fixes)
    a = commits[commits.commit.isin(bugs)]
    # for s in a.commit.values.tolist():
    print(len(a))
    parallelRun(prepareFiles,a[['commit','files']].values.tolist())
        # prepareFiles(s)


def markBugFixingPatches(commits):
    # from pandarallel import pandarallel
    #
    # pandarallel.initialize()
    # commits['files'] = commits.commit.parallel_apply(checkCommitLog)
    # commits

    # commits['isC'] = commits.files.apply(lambda x:np.any([i.endswith('.c') or i.endswith('.h') for i in x.keys() ]))

    commits = commits[commits.isC == True]

    # commits.commit.parallel_apply(getCommitLog)
    f = parallelRunMergeNew(getCommitLog, commits['commit'].values.tolist())

    res = pd.merge(commits, f, on=['commit'])

    save_zipped_pickle(res, join(COMMIT_DFS, 'linuxFix' + ".pickle"))


def getAllCommits():
    if isfile(join(COMMIT_DFS,'linuxFix.pickle')):
        return load_zipped_pickle(join(COMMIT_DFS,'linuxFix.pickle'))
    else:

        if isfile(join(COMMIT_DFS,'linux.pickle')):
            commits =  load_zipped_pickle(join(COMMIT_DFS,'linux.pickle'))
        else:
            if not os.path.exists(COMMIT_DFS):
                os.mkdir(COMMIT_DFS)


            cmd = 'git -C ' + repo + " log --no-merges --pretty=format:'{\"commit\":\"%H\",\"commitDate\":\"%ci\",\"title\":\"%f\",\"committer\":\"%ce\"}' > " + join(DATA_PATH,'linux' + '.commits')
            output = shellCallTemplate(cmd, enc='latin1')

            from commitCollector import makeDF
            rDF = makeDF(join(DATA_PATH,'linux' + '.commits'))
            save_zipped_pickle(rDF, join(COMMIT_DFS, 'linux' + ".pickle"))
            return rDF
        markBugFixingPatches(commits)


