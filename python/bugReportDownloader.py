from common.commons import *

BUG_REPORT_PATH = os.environ["BUG_REPORT"]
DATA_PATH = os.environ["DATA_PATH"]
# BUG_POINT = os.environ["BUG_POINT"]
COMMIT_DFS = os.environ["COMMIT_DFS"]
from urllib.request import urlopen
from urllib import error
import urllib

import socket

timeout = 30
socket.setdefaulttimeout(timeout)

import logging
hdr = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11',
       'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
       'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.3',
       'Accept-Encoding': 'none',
       'Accept-Language': 'en-US,en;q=0.8',
       'Connection': 'keep-alive'}

def bugRepoDict():
    brDict={}
    brDict['CAMEL'] ='https://issues.apache.org/jira/browse/CAMEL-'#4748'
    brDict['HBASE'] ='https://issues.apache.org/jira/browse/HBASE-'#84'
    brDict['HIVE'] ='https://issues.apache.org/jira/browse/HIVE-'#3518'
    brDict['CODEC'] ='https://issues.apache.org/jira/browse/CODEC-'#103'
    brDict['COLLECTIONS'] ='https://issues.apache.org/jira/browse/COLLECTIONS-'#660'
    brDict['COMPRESS'] ='https://issues.apache.org/jira/browse/COMPRESS-'#261'
    brDict['CONFIGURATION'] ='https://issues.apache.org/jira/browse/CONFIGURATION-'#307'
    brDict['CRYPTO'] ='https://issues.apache.org/jira/browse/CRYPTO-'#85'
    brDict['CSV'] ='https://issues.apache.org/jira/browse/CSV-'#84'
    brDict['IO'] ='https://issues.apache.org/jira/browse/IO-'#193'
    brDict['LANG'] ='https://issues.apache.org/jira/browse/LANG-'#810'
    brDict['MATH'] ='https://issues.apache.org/jira/browse/MATH-'#790'
    brDict['WEAVER'] = 'https://issues.apache.org/jira/browse/WEAVER-'
    brDict['ENTESB'] ='https://issues.jboss.org/browse/ENTESB-'#80'
    brDict['JBMETA'] ='https://issues.jboss.org/browse/JBMETA-'#315'
    brDict['ELY'] ='https://issues.jboss.org/browse/ELY-'#515'
    brDict['SWARM'] ='https://issues.jboss.org/browse/SWARM-'#1710'
    brDict['WFARQ'] ='https://issues.jboss.org/browse/WFARQ-'#14'
    brDict['WFCORE'] ='https://issues.jboss.org/browse/WFCORE-'#1499'
    brDict['WFLY'] ='https://issues.jboss.org/browse/WFLY-'#3717'
    brDict['WFMP'] ='https://issues.jboss.org/browse/WFMP-'#85'
    brDict['AMQP'] ='https://jira.spring.io/browse/AMQP-'#32'
    brDict['ANDROID'] ='https://jira.spring.io/browse/ANDROID-'#131'
    brDict['BATCH'] ='https://jira.spring.io/browse/BATCH-'#84'
    brDict['BATCHADM'] ='https://jira.spring.io/browse/BATCHADM-'#22'
    brDict['DATACMNS'] ='https://jira.spring.io/browse/DATACMNS-'#43'
    brDict['DATAGRAPH'] ='https://jira.spring.io/browse/DATAGRAPH-'#869'
    brDict['DATAJPA'] ='https://jira.spring.io/browse/DATAJPA-'#869'
    brDict['DATAJPA'] ='https://jira.spring.io/browse/DATAJPA-'#43'
    brDict['DATAMONGO'] ='https://jira.spring.io/browse/DATAMONGO-'#634'
    brDict['DATAREDIS'] ='https://jira.spring.io/browse/DATAREDIS-'#680'
    brDict['DATAREST']='https://jira.spring.io/browse/DATAREST-'#44'
    brDict['LDAP'] ='https://jira.spring.io/browse/LDAP-'#64' #https://github.com/spring-projects/spring-ldap/issues/107
    brDict['MOBILE'] = 'https://jira.spring.io/browse/MOBILE-'
    brDict['ROO'] ='https://jira.spring.io/browse/ROO-'#260'
    brDict['SEC'] = 'https://jira.spring.io/browse/SEC-'#1880' #'https://github.com/spring-projects/spring-security/issues/2108'
    brDict['SECOAUTH'] ='https://jira.spring.io/browse/SECOAUTH-'#42'
    brDict['SGF'] ='https://jira.spring.io/browse/SGF-'#69'
    brDict['SHDP'] ='https://jira.spring.io/browse/SHDP-'#444'
    brDict['SHL'] ='https://jira.spring.io/browse/SHL-'#80'
    brDict['SOCIAL'] ='https://jira.spring.io/browse/SOCIAL-'#33'
    brDict['SOCIALFB'] ='https://jira.spring.io/browse/SOCIALFB-'#33'
    brDict['SOCIALLI'] ='https://jira.spring.io/browse/SOCIALLI-'#33'
    brDict['SOCIALTW'] ='https://jira.spring.io/browse/SOCIALTW-'#10'
    brDict['SPR'] ='https://jira.spring.io/browse/SPR-'#6132'
    brDict['SWF'] ='https://jira.spring.io/browse/SWF-'#80'
    brDict['SWS'] ='https://jira.spring.io/browse/SWS-'#510'
    brDict['AspectJ']='https://bugs.eclipse.org/bugs/show_bug.cgi?id='
    brDict['JDT'] ='https://bugs.eclipse.org/bugs/show_bug.cgi?id='#30113'
    brDict['SWT'] ='https://bugs.eclipse.org/bugs/show_bug.cgi?id='#231787'
    brDict['PDE'] ='https://bugs.eclipse.org/bugs/show_bug.cgi?id='#201369'
    return brDict

def downloadAll(x):
    try:
        pj,id = x.split('-')
        links = bugRepoDict()
        downloadLink = links[pj] + id
        webRequest(downloadLink)

    except Exception as e:
        print(e)
        logging.error(e)
        return False
def webRequest(x):
    url = x
    bugID = url.split('/')[-1:]
    url = url + '?redirect=false'

    brLocation = join(BUG_REPORT_PATH,bugID[0] + ".xml")
    if isfile(brLocation):
        with open(brLocation, 'rb') as f:
            the_page = p.load(f)
    else:
        try:
            logging.info(url)
            req = urllib.request.Request(url, headers=hdr)


            response = urlopen(req)
            the_page = response.read()
        except error.HTTPError as err:
            if err.code == 404:
                print("Error: %s, reason: %s." % (err.code, err.reason))
            return None
        p.dump(the_page, open(brLocation, "wb"))

def caseBRDownload(subject):

    if not os.path.exists(BUG_REPORT_PATH):
        os.mkdir(BUG_REPORT_PATH)
    bids=[]
    if subject == 'ALL':
        for i in listdir(COMMIT_DFS):
            commits = load_zipped_pickle(join(COMMIT_DFS, i))
            bids.extend(commits.fix.values.tolist())
    else:
        subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))

        subjects = subjects.query("Subject == '{0}'".format(subject))

        commits = load_zipped_pickle(join(COMMIT_DFS, subjects.iloc[0].Repo + '.pickle'))
        bids.extend(commits.fix.values.tolist())



    parallelRun(downloadAll,bids)
