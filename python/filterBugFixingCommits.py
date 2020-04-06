from common.commons import *

ROOT_DIR = os.environ["ROOT_DIR"]
REPO_PATH = os.environ["REPO_PATH"]
DATA_PATH = os.environ["DATA_PATH"]
COMMIT_DFS = os.environ["COMMIT_DFS"]
BUG_POINT = os.environ["BUG_POINT"]
COMMIT_FOLDER = os.environ["COMMIT_FOLDER"]




def getLast(bugID):
    if isfile(join(BUG_POINT, bugID + ".pickle")):
        return
    else:
        subject = bugID.split('-')[0]
        subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
        repo = subjects.query("Subject == '{0}'".format(subject)).iloc[0].Repo
        sourceDF = load_zipped_pickle(join(COMMIT_DFS, repo + '.pickle'))
        sourceDF['fix'] = sourceDF['fix'].apply(lambda x: x.strip() if not x is None else x)
        aDf = sourceDF[sourceDF.fix == bugID]
        if len(aDf > 0):
            dateCheck = aDf.sort_values('commitDate').iloc[0].commitDate
            filtered = sourceDF.query("commitDate < '{0}'".format(dateCheck ))
            filtered['dateCheck'] = dateCheck
            filtered = filtered.head(1)
            save_zipped_pickle(filtered,join(BUG_POINT,bugID + ".pickle"))


def markFix(subject,repoName):
    subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    branch = subjects.query("Subject == '{0}'".format(subject))['Branch'].values.tolist()[0]
    cmd = 'git -C ' + join(REPO_PATH, repoName) + ' checkout -f '+branch

    output, err = shellGitCheckout(cmd,enc='latin1')

    m = re.search(branch, err)

    while not m:
        time.sleep(10)
        logging.info('Waiting for checkout')

    aDF = load_zipped_pickle(join(COMMIT_DFS, repoName + '.pickle'))
    aDF['fix'] = aDF['commit'].apply(lambda x: getBugIds(x, subject, repoName))
    return aDF

def getBugIds(x,subject,repoName):


    cmd = 'git -C ' + REPO_PATH+repoName + ' show --quiet ' + x

    output = shellCallTemplate(cmd)
    pattern = r"[\s:\[\(#\-/]("+subject+"\-[0-9]+)"
    match = re.search(pattern, output, re.IGNORECASE)
    if not match:
        return None
    if (len(match.groups()) == 1):
        matched = match.group(1)
    else:
        logging.error('too many match groups')
    matched
    return matched



# def getLasts(subjec,predict = False):
#     if not os.path.exists(BUG_POINT):
#         os.mkdir(BUG_POINT)
#
#     if predict:
#         bugIDS = load_zipped_pickle(join(CODE_PATH, subjec+'BugReportsExport.pickle'))
#         logging.info("Extracting bug points for prediction")
#         selectedIds = bugIDS.bugID.unique().tolist()
#     else:
#         subjects = pd.read_csv(join(CODE_PATH, 'subjects.csv'))
#         repo = subjects.query("Subject == '{0}'".format(subjec)).iloc[0].Repo
#
#         sourceDF = load_zipped_pickle(join(COMMIT_DFS, repo + '.pickle'))
#
#         selectedIds = sourceDF.fix.unique().tolist()
#         selectedIds = [i for i in selectedIds if i is not None]
#     if subjec != 'ALL':
#         selectedIds = [i for i in selectedIds if i.startswith(subjec)]
#
#     with concurrent.futures.ProcessPoolExecutor() as executor:
#         try:
#             futures = {executor.submit(getLast, bugID ): bugID for bugID in selectedIds }
#             for future in concurrent.futures.as_completed(futures):
#                 url = futures[future]
#                 try:
#                     data = future.result()
#
#                 except Exception as exc:
#                     logging.error('%r generated an exception: %s' % (url, exc))
#                     raise
#                 kwargs = {
#                     'total': len(futures),
#                     'unit': 'files',
#                     'unit_scale': True,
#                     'leave': False
#                 }
#                 # Print out the progress as tasks complete
#                 for f in tqdm(concurrent.futures.as_completed(futures), **kwargs):
#                     pass
#         except Exception as e:
#             logging.error(e)
#             executor.shutdown()
#             raise


def caseFix(subjec):
    cmd = 'git config --global diff.renamelimit 0'

    shellCallTemplate(cmd)



    logging.info("Marking fixes")
    subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    if subjec != 'ALL':
        subjects = subjects.query("Subject == '{0}'".format(subjec))




    with concurrent.futures.ProcessPoolExecutor() as executor:
        try:
            futures = {
            executor.submit(markFix, subject, subjects.query("Subject == '{0}'".format(subject)).iloc[0].Repo): subject
            for subject in subjects.Subject.tolist()}
            for future in concurrent.futures.as_completed(futures):
                url = futures[future]
                try:
                    data = future.result()
                    data = data[~data.fix.isna()]
                    data.fix = data.fix.apply(lambda x: x.strip().upper())
                    singleFix = data.fix.value_counts().loc[lambda x: x == 1].reset_index(name='count')['index']
                    singleCommits = data[data.fix.isin(singleFix)]
                    save_zipped_pickle(singleCommits,join(COMMIT_DFS, subjects.query("Subject == '{0}'".format(url)).iloc[0].Repo + ".pickle"))
                except Exception as exc:
                    logging.error('%r generated an exception: %s' % (url, exc))
                    raise
                kwargs = {
                    'total': len(futures),
                    'unit': 'subject',
                    'unit_scale': True,
                    'leave': False
                }
                # Print out the progress as tasks complete
                for f in tqdm(concurrent.futures.as_completed(futures), **kwargs):
                    pass
        except Exception as e:
            # logging.error(e)
            executor.shutdown()
            raise
