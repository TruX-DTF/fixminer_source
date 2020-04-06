from common.commons import *

# CODE_PATH = os.environ["CODE_PATH"]
DATA_PATH = os.environ["DATA_PATH"]
BUG_REPORT_PATH = os.environ["BUG_REPORT"]
COMMIT_DFS = os.environ["COMMIT_DFS"]

import bs4 as bs

import re
def parseCore(br):
    columns = ['bugReport', 'summary', 'description','created','updated','resolved','reporterDN','reporterEmail','hasAttachment','attachmentTime','hasPR','commentsCount']
    bugReport = pd.DataFrame(columns=columns)
    ind = 0
    if isfile(join(BUG_REPORT_PATH, br)):
        
        with open(join(BUG_REPORT_PATH, br), 'rb') as f:
            the_page = p.load(f)
        soup = bs.BeautifulSoup(the_page, "html.parser")

        if not (br.startswith('show')):

            type = soup.find('span', {'id': 'type-val'})
            status = soup.find('span', {'id': 'status-val'})

            if type is None:
                # print(br)
                return;
            if (type.text.strip() == 'Bug' or type.text.strip() =='Defect'):
                if (status.text.strip() == 'Resolved' or status.text.strip() == 'Closed'):

                    summary = soup.find('h1', {'id': 'summary-val'}).text
                    desc = soup.find('div', {'id': 'description-val'})

                    created= soup.find('span', {'data-name': 'Created'}).time['datetime']
                    updated = soup.find('span', {'data-name': 'Updated'}).time['datetime']
                    resolved = soup.find('span', {'data-name': 'Resolved'}).time['datetime']

                    # jboss reporter is different
                    reporterField = soup.find('span',{'id':'reporter-val'})
                    try:
                        reporterInfo= reporterField.findAll('span', {'class': 'user-hover'})[0]['data-user']
                        reporterDict = eval(reporterInfo)
                        reporterDN = reporterDict['displayName']
                        reporterEmail = reporterDict['emailAddress']
                    except KeyError as e:

                        e
                        reporterDN = reporterField.text.strip()
                        reporterEmail = None
                    except Exception as e:
                        e
                        reporterDN = reporterField.text.strip()
                        reporterEmail = None

                    attachment = soup.find('dd', {'class': 'attachment-date'})
                    isAttachment = False
                    isPR = False
                    attachmentTime = None
                    if attachment is not None and len(attachment) > 0:
                        attachmentTime = attachment.parent.time['datetime']
                        isAttachment = True

                    hasPullReq = soup.find('strong', {'title': 'Git Pull Request'})
                    if hasPullReq is not None:
                        haveAttachment = hasPullReq.parent.findAll('a')
                        if(haveAttachment is not None and len(haveAttachment)>0):
                            isPR = True
                        else:
                            linkPullRequest = soup.find('ul', {'id': 'issuedetails'}).find('a', {'title': 'PullRequest'})
                            if(linkPullRequest is not None and len(linkPullRequest)> 0):
                                isPR = True

                    commentsCount = len(re.findall(r'\"comment-[0-9]+', soup.getText()))
                    if desc is not None:
                        # bugReport.loc[ind] = [br, summary, desc.text,created,updated,resolved,reporterDN,reporterEmail,isAttachment,attachmentTime,isPR,commentsCount]
                        return [br, summary, desc.text, created, updated, resolved, reporterDN,
                                              reporterEmail, isAttachment, attachmentTime, isPR, commentsCount]
                        # ind += 1
                    else:
                        # bugReport.loc[ind] = [br, summary, None, created, updated, resolved, reporterDN, reporterEmail,
                        #                       isAttachment,attachmentTime,isPR,commentsCount]
                        return [br, summary, None, created, updated, resolved, reporterDN, reporterEmail,
                                              isAttachment,attachmentTime,isPR,commentsCount]
                        # ind += 1
            # else:
            #     print(type.text.strip())
        else:
            importance = (
                soup.find('a', {'href': 'page.cgi?id=fields.html#importance'}).parent.parent.parent.td.text.strip())
            status = soup.find('span', {'id': 'static_bug_status'}).text.strip()

            m1 = re.search('enhancement', importance, re.IGNORECASE)
            # if not m:
            #     print(br)
            if not m1:
                m = re.search('FIXED|DUPLICATE', status)
                if m:
                    summary = soup.find('span', {'id': 'short_desc_nonedit_display'}).text
                    comment0 = soup.find('div', {'id': 'c0'})
                    if comment0.find('a').text == 'Description':
                        desc = comment0.find('pre', {'class': 'bz_comment_text'}).text
                        bugReport.loc[ind] = [br, summary, desc]
                        ind += 1
                # desc
        # return  bugReport

def parallelRun(coreFun,elements,*args):
    with concurrent.futures.ProcessPoolExecutor() as executor:
        try:
            dataL = []
            futures = {executor.submit(coreFun, l,*args): l for l in elements}
            for future in concurrent.futures.as_completed(futures):
                url = futures[future]
                try:
                    data = future.result()
                    dataL.append(data)

                except Exception as exc:
                    logging.error('%r generated an exception: %s' % (url, exc))
                    raise

                kwargs = {
                    'total': len(futures),
                    'unit': 'files',
                    'unit_scale': True,
                    'leave': False
                }
                # Print out the progress as tasks complete
                for f in tqdm(concurrent.futures.as_completed(futures), **kwargs):
                    pass
            newData = pd.concat(dataL)
            return newData
        except Exception as e:
            executor.shutdown()
            raise

def getCommitter(x):
    subject = x.split('-')[0]
    subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    repo = subjects.query("Subject == '{0}'".format(subject)).Repo.tolist()[0]
    commits = load_zipped_pickle(join(COMMIT_DFS, repo+'.pickle'))
    commits
    committer = commits.query("fix =='{0}'".format(x)).committer.tolist()
    # print(len(committer))
    return committer



def step1(subject):

    if subject == 'ALL':
        bids = os.listdir(BUG_REPORT_PATH)
    else:
        bids = [f for f in os.listdir(BUG_REPORT_PATH) if f.startswith(subject)]
    bids = [i for i in bids if not i.startswith('.')]

    # bids = bids[:100]



    dataL = parallelRunMerge(parseCore,bids)
    logging.info('Finish parsing')
    # list(filter(None.__ne__, dataL))
    br = pd.DataFrame(
        columns=['bugReport', 'summary', 'description', 'created', 'updated', 'resolved', 'reporterDN', 'reporterEmail',
                 'hasAttachment', 'attachmentTime', 'hasPR', 'commentsCount'], data=list(filter(None.__ne__, dataL)))
    # br = pd.concat(dataL)
    logging.info('Finish parsing')
    br['project'] = br.bugReport.apply(lambda x: x.split('-')[0])
    br['bid'] = br.bugReport.apply(lambda x: x.split('.')[0])
    br['committerEmail'] = br.bid.apply(lambda x: getCommitter(x))

    br['sameEmail'] = br.apply(lambda x:x['reporterEmail'] in x['committerEmail'],axis=1)
    subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    subjects['Group']=subjects.Group.apply(lambda x:x.replace('Commons','Apache').replace('Wildfly','Jboss').upper())
    save_zipped_pickle(br,join(DATA_PATH,subject+"bugReportsComplete.pickle"))


def step3(subject):
    subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))
    subjects['Group']=subjects.Group.apply(lambda x:x.replace('Commons','Apache').replace('Wildfly','Jboss').upper())

    stats = load_zipped_pickle(join(DATA_PATH, subject + "bugReportsComplete.pickle"))

    stats = stats[['bugReport', 'sameEmail', 'hasAttachment','attachmentTime','hasPR', 'created','codeElements','stackTraces','summaryHints','descHints','commentsCount']]
    stats['project'] =stats.bugReport.apply(lambda x:x.split('-')[0])
    stats['project'] = stats.project.apply(lambda x: subjects.query("Subject == '{0}'".format(x)).Group.tolist()[0]+'-'+x)
    stats['created'] = stats['created'].apply(lambda x: pd.to_datetime(x))
    stats['attachmentTime'] = stats['attachmentTime'].apply(lambda x: pd.to_datetime(x))
    stats['isAttach'] = (stats['attachmentTime'] - stats['created']) < pd.Timedelta(1, unit='h')
    stats['hints'] = stats.apply(lambda x: True if len(x['summaryHints']) > 0 or len(x['descHints']) > 0 else False,
                                 axis=1)

    stats['commentsCount'].to_csv(subject+'Comments', index=False)

    a = stats.groupby('project').agg({'sameEmail': "sum", 'isAttach': "sum"})
    a['avgComments'] =stats['commentsCount'].mean()
    a['hasStackTraces'] = len([i for i in stats['stackTraces'].str.len().tolist() if i != 0])
    a['hasHints']= len([i for i in stats['hints'].tolist() if i == True])
    a['hasCE']= len([i for i in stats['codeElements'].str.len().tolist() if i != 0])

    a['no'] = len(stats)

    a.to_latex(join(DATA_PATH,subject+'datasetQuality.tex'))

def caseBRParser(subject):
    step1(subject)
    # step2()
    # step3()