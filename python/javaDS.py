def createDS(subject):
    # # if job == 'clone':
    from commitCollector import *

    caseClone(subject)
    # # elif job == 'collect':
    from commitCollector import *

    caseCollect(subject)
    # # elif job == 'fix':
    from filterBugFixingCommits import caseFix

    caseFix(subject)
    #
    # # elif job =='brDownload':
    from bugReportDownloader import caseBRDownload

    caseBRDownload(subject)
    # # elif job =='brParser':
    from bugReportParser import step1

    step1(subject)

    # elif job =='dataset':

    if not isfile(join(DATA_PATH, 'singleBR.pickle')):

        brs = load_zipped_pickle(join(DATA_PATH, subject + "bugReportsComplete.pickle"))

        subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))


        def getCommit(x):
            bid, project = x

            subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
            repo = subjects.query("Subject == '{0}'".format(project)).Repo.tolist()[0]
            commits = load_zipped_pickle(join(DATA_PATH, COMMIT_DFS, repo + '.pickle'))
            correspondingCommit = commits.query("fix =='{0}'".format(bid)).commit.tolist()
            if len(correspondingCommit) == 1:
                return [bid, correspondingCommit[0], project]
            else:
                return None
                print('error')


        wl = brs[['bid', 'project']].values.tolist()
        dataL = parallelRunMerge(getCommit, wl)

        commits = pd.DataFrame(
            columns=['bid', 'commit', 'project'],
            data=list(filter(None.__ne__, dataL)))

        save_zipped_pickle(commits, join(DATA_PATH, 'singleBR.pickle'))
    else:
        commits = load_zipped_pickle(join(DATA_PATH, 'singleBR.pickle'))
        subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    logging.info('done matching commits')
    commits['repo'] = commits.project.apply(lambda x: subjects.query("Subject == '{0}'".format(x)).Repo.tolist()[0])

    workList = commits[['commit', 'repo']].values.tolist()
    from dataset import prepareFiles

    parallelRun(prepareFiles, workList)