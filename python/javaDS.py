from common.commons import *
from commitCollector import *
DATA_PATH = os.environ["DATA_PATH"]
COMMIT_DFS = os.environ["COMMIT_DFS"]
# DATASET_PATH = '/Users/anilkoyuncu/projects/datasets'
REPO_PATH = os.environ["REPO_PATH"]
DATASET_PATH = os.environ["REPO_PATH"]
DATASET = os.environ["dataset"]
ROOT = os.environ["ROOT_DIR"]
PROJECT_LIST = os.environ["PROJECT_LIST"]

from otherDatasets import markBugFixingPatches


def createDS():

    pjList = PROJECT_LIST.split(',')
    if not os.path.exists(DATASET_PATH):
        os.mkdir(DATASET_PATH)
    if not os.path.exists(COMMIT_DFS):
        os.mkdir(COMMIT_DFS)

    subjects = pd.read_csv(join(ROOT,'data', 'dataset.csv'))


    if pjList == ['ALL']:
        tuples = subjects[['Repo','GitRepo','Branch']].values.tolist()
    else:
        # repos = subjects.query("Subject == '{0}'".format(subject)).Repo.tolist()
        tuples = subjects[subjects.Repo.isin(pjList)][['Repo', 'GitRepo','Branch']].values.tolist()

    for t in tuples:
        repo,src,branch = t
        logging.info(repo)
        if isfile(join(COMMIT_DFS,repo+'Fix.pickle')):
            commits = load_zipped_pickle(join(COMMIT_DFS,repo+'Fix.pickle'))
        else:
            cmd = 'git config --global http.postBuffer 157286400'
            shellCallTemplate(cmd)
            cmd = 'git -C ' + DATASET_PATH + ' clone ' + src
            shellCallTemplate(cmd)
            logging.info(repo)
            getCommitFromRepo(join(REPO_PATH, repo), join(COMMIT_DFS, repo),branch)
            rDF = makeDF(join(COMMIT_DFS,repo + '.commits'))
            save_zipped_pickle(rDF, join(COMMIT_DFS, repo + ".pickle"))
            # return rDF
            commits = rDF
            commits = markBugFixingPatches(commits,repo)
        commits = commits[commits.files.apply(lambda x: np.any([i == 'M' for i in x.values()]))]
        # keep only commits that are changing c files (.c)
        commits = commits[commits.files.apply(lambda x: np.all([i.endswith('.java') for i in x.keys()]))]
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
        from otherDatasets import prepareFiles
        parallelRun(prepareFiles,commits[['commit','files']].values.tolist(),repo)

    # # if job == 'clone':
    # for repo,src in subjects[['Repo','GitRepo']].values.tolist():
    #     if(pjList != ['ALL']):
    #         if repo in pjList:
    #             print(repo)
    #             cmd = 'git -C ' + DATASET_PATH + ' clone ' + src
    #             shellCallTemplate(cmd)
    #             logging.info(repo)

    # caseClone(subject)

    # caseCollect(subject)
    # # elif job == 'fix':
    # from filterBugFixingCommits import caseFix
    #
    # caseFix(subject)
    # #
    # # # elif job =='brDownload':
    # from bugReportDownloader import caseBRDownload
    #
    # caseBRDownload(subject)
    # # # elif job =='brParser':
    # from bugReportParser import step1
    #
    # step1(subject)
    #
    # # elif job =='dataset':
    #
    # if not isfile(join(DATA_PATH, 'singleBR.pickle')):
    #
    #     brs = load_zipped_pickle(join(DATA_PATH, subject + "bugReportsComplete.pickle"))
    #
    #     subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    #
    #
    #     def getCommit(x):
    #         bid, project = x
    #
    #         subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    #         repo = subjects.query("Subject == '{0}'".format(project)).Repo.tolist()[0]
    #         commits = load_zipped_pickle(join(DATA_PATH, COMMIT_DFS, repo + '.pickle'))
    #         correspondingCommit = commits.query("fix =='{0}'".format(bid)).commit.tolist()
    #         if len(correspondingCommit) == 1:
    #             return [bid, correspondingCommit[0], project]
    #         else:
    #             return None
    #             print('error')
    #
    #
    #     wl = brs[['bid', 'project']].values.tolist()
    #     dataL = parallelRunMerge(getCommit, wl)
    #
    #     commits = pd.DataFrame(
    #         columns=['bid', 'commit', 'project'],
    #         data=list(filter(None.__ne__, dataL)))
    #
    #     save_zipped_pickle(commits, join(DATA_PATH, 'singleBR.pickle'))
    # else:
    #     commits = load_zipped_pickle(join(DATA_PATH, 'singleBR.pickle'))
    #     subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    # logging.info('done matching commits')
    # commits['repo'] = commits.project.apply(lambda x: subjects.query("Subject == '{0}'".format(x)).Repo.tolist()[0])
    #
    # workList = commits[['commit', 'repo']].values.tolist()
    # from dataset import prepareFiles
    #
    # parallelRun(prepareFiles, workList)