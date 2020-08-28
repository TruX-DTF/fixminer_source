from common.commons import *





if __name__ == '__main__':


    try:
        args = getRun()
        setLogg()


        setEnv(args)

        job = args.job
        # job = "cluster"
        ROOT_DIR = os.environ["ROOT_DIR"]
        REPO_PATH = os.environ["REPO_PATH"]
        CODE_PATH = os.environ["CODE_PATH"]
        DATA_PATH = os.environ["DATA_PATH"]
        COMMIT_DFS = os.environ["COMMIT_DFS"]
        BUG_POINT = os.environ["BUG_POINT"]
        COMMIT_FOLDER = os.environ["COMMIT_FOLDER"]
        FEATURE_DIR = os.environ["FEATURE_DIR"]
        DATASET_DIR = os.environ["DATASET_DIR"]
        PROJECT_TYPE = os.environ["PROJECT_TYPE"]
        REDIS_PORT = os.environ["REDIS_PORT"]
        jdk8 = os.environ["JDK8"]
        pd.options.mode.chained_assignment = None


        # subject = 'ALL'
        # rootType = 'if'
        job = 'introRes'
        print(job)


        if job == 'dataset4j':
            from javaDS import createDS
            createDS()

        elif job == 'introRes':

            def readResultFile(resFile):
                with open(join(DATA_PATH,resFile),'r') as f:
                    lines = f.readlines()

                success = [i for i in lines if i.strip().endswith('success')]
                patchCandidates = {}
                def getPatterns(x):
                    regex = r"fix (.*) by (.*) times:1, success"
                    matches = re.finditer(regex, x, re.MULTILINE)
                    match = list(matches)
                    fixes = []
                    if len(match) >= 1:
                        patchCandidates[x.split(',')[0]] =re.findall(r"@True[a-zA-z\:0-9\.\-\_]+@", x, re.MULTILINE)
                        for m in match:
                            t = m.group(1), m.group(2)
                            fixes.append(t)
                    return fixes

                success = [getPatterns(i) for i in success]
                aDf = pd.DataFrame(columns=['bid','candidates'])
                idx = 0
                for k,v in patchCandidates.items():
                    aDf.loc[idx] = [k,[v]]
                    idx+=1
                aDf['noTested'] = aDf.candidates.apply(lambda x: len(x[0]))
                aDf['pos'] = aDf.candidates.apply(lambda x: x[0][-1].split(':')[1])
                aDf['pos'] = aDf['pos'].apply(lambda x: int(x))
                return aDf,success

            aDf,success =readResultFile('introTestResultsWhite')
            bDf,success =readResultFile('introTestResultsWhiteFunc')
            cDf,success =readResultFile('introTestResultsWhiteuFilenames')
            dDf,success =readResultFile('introTestResultsWhiteuPatch')
            eDf,success =readResultFile('introTestResultsWhiteuProject')




            labels = ['hunks','functions','files','patches','projects']

            data = [aDf['noTested'].values.tolist(), bDf['noTested'].values.tolist(),cDf['noTested'].values.tolist(),dDf['noTested'].values.tolist(),eDf['noTested'].values.tolist()]
            data = [aDf['pos'].values.tolist(), bDf['pos'].values.tolist(), cDf['pos'].values.tolist(),
                    dDf['pos'].values.tolist(), eDf['pos'].values.tolist()]

            plotBox(data, labels ,'test.pdf',yAxisLabel='',xAxisLabel='Position of the first plausible patch', limit=False)
            patterns  = pd.DataFrame(columns=['bug','pj','pattern'])
            for idx,suc in enumerate(success):
                bug,pattern =suc[0]
                pj =bug.split(':')[1]
                patterns.loc[idx] = [bug,pj,pattern.split(pj+'.c')[-1]]
            fixPatterns = patterns.groupby(by=['pattern'], as_index=False).agg(lambda x: x.tolist())
            fixPatterns['count'] = fixPatterns.bug.apply(lambda x: len(x))
            fixPatterns.sort_values(by=['count'], ascending=False, inplace=True)
            fixPatterns[['pattern','count']].to_latex(index=False)

            for i in fixPatterns.pattern.values.tolist():
                i = re.findall(r"((.*)\.c$)", i, re.MULTILINE)[0][1]

                shutil.copy2(join(DATA_PATH, 'patches', 'cocci', i), join(DATA_PATH, 'white', i))

            summary = patterns.groupby(by=['pj'], as_index=False).agg(lambda x: x.tolist())
            summary['bCount'] = summary.bug.apply(lambda x:len(x))


            success

        elif job =='dataset4c':
            from otherDatasets import core
            core()

        elif job =='datasetIntro':
            from introDS import core
            core()
        elif job =='richedit':
            dbDir = join(DATA_PATH, 'redis')
            stopDB(dbDir, REDIS_PORT)
            cmd = "JAVA_HOME='" + jdk8 + "' java  -jar " + join(Path(ROOT_DIR).parent, 'target','FixPatternMiner-1.0.0-jar-with-dependencies.jar') + " " + args.prop + " RICHEDITSCRIPT "
            output = shellCallTemplate(cmd)
            logging.info(output)


        elif job =='actionSI':
            from pairs import actionPairs
            matches = actionPairs()

            from pairs import createPairs
            createPairs(matches)

            from pairs import importAction
            importAction()

        elif job =='compare':
             # cmd = "mvn exec:java -f '/data/fixminer_source/' -Dexec.mainClass='edu.lu.uni.serval.richedit.akka.compare.CompareTrees' -Dexec.args='"+ " shape " + join(DATA_PATH,"redis") +" ALLdumps-gumInput.rdb " + "clusterl0-gumInputALL.rdb /data/richedit-core/python/data/richEditScript'"
            cmd = "JAVA_HOME='" + jdk8 + "' java  -jar " + join(Path(ROOT_DIR).parent, 'target','FixPatternMiner-1.0.0-jar-with-dependencies.jar') + " " + args.prop + " COMPARE "
            output = shellCallTemplate4jar(cmd)
            logging.info(output)


        elif job == 'cluster':
            from abstractPatch import cluster

            dbDir = join(ROOT_DIR,'data','redis')
            startDB(dbDir, REDIS_PORT, PROJECT_TYPE)
            cluster(join(DATA_PATH,'actions'),join(DATA_PATH, 'pairs'),'actions')

        elif job =='tokenSI':
            from pairs import tokenPairs
            tokenPairs()

            from pairs import importTokens
            importTokens()

        elif job == 'clusterTokens':
            from abstractPatch import cluster

            dbDir = join(DATA_PATH, 'redis')
            startDB(dbDir, REDIS_PORT, PROJECT_TYPE)
            cluster( join(DATA_PATH, 'tokens'),join(DATA_PATH, 'pairsToken'),'tokens')


        elif job == 'codeflaws':
            from otherDatasets import codeflaws
            codeflaws()


        elif job =='indexClusters':
            from sprinferIndex import runSpinfer
            runSpinfer()
            #
            # from sprinferIndex import test
            # test()
            # from sprinferIndex import divideCoccis
            # divideCoccis()
            # from sprinferIndex import removeDuplicates
            # removeDuplicates()

        elif job =='mergeCoccis':
            # from sprinferIndex import mergeCoccis
            # mergeCoccis()
            from sprinferIndex import removeDuplicates2
            removeDuplicates2()

        elif job == 'getPatterns':
            dbDir = join(ROOT_DIR,'data', 'redis')
            startDB(dbDir, REDIS_PORT, PROJECT_TYPE)
            from sprinferIndex import getPatternTypes
            getPatternTypes()


        elif job == 'evalManyBugs':
            # from patchManyBugs import patchCore
            # patchCore()
            ## from patchManyBugs import patched
            ## patched()
            # from patchManyBugs import exportSosPatches
            # exportSosPatches()
            from validate_manybugs import validate

            validate()

        elif job =='patternOperations':
            from sprinferIndex import patternOperations
            patternOperations()
        elif job == 'patchManyBugs':
            from patchManyBugs import buildAll
            buildAll()

            # from patchManyBugs import patchCore
            # patchCore()
            # # from patch_validate import patch_validate_mine
            # # patch_validate_mine()
            # from patchManyBugs import patched
            # patched()
            # from patchManyBugs import exportSosPatches
            # exportSosPatches()

        elif job =='patchIntro':
            from sprinferIndex import patchCoreIntro
            patchCoreIntro()
            # from sprinferIndex import patched
            # patched()

        elif job =='validateIntro':
            # from patch_validate_introClass2 import patch_validate
            # patch_validate()
            from test_patched_file import patch_validate
            patch_validate()
        elif job =='checkCorrectIntro':
            from test_patched_file import checkCorrect
            checkCorrect()
        elif job == 'manybugs':
            from getManybugs import export
            export()

        elif job == 'validateMany':
            from patch_validate import patch_validate
            patch_validate()
        elif job == 'validateCodeFlaws':
            from validateCodeFlaws import validate
            validate()
        elif job == 'introclass':
            from getIntroClass import export
            export()

        elif job =='stats':
            from stats import statsNormal
            statsNormal(True)


        elif job == 'datasetDefects4J':
            from defects4JDataset import core
            core()

        elif job =='bug':
            from bugstats import bStats
            bStats()
        elif job == 'defects4j':
            from stats import defects4jStats
            defects4jStats()
        elif job == 'patterns':

            # coccis = load_zipped_pickle(join(DATA_PATH, 'allCocciPatterns2.pickle'))
            # coccis['family'] = coccis.cid.apply(lambda x: x.split('.cocci')[0])

            from stats import exportAbstractPatterns
            exportAbstractPatterns()
        elif job == 'travis':

            if isfile(join(DATA_PATH,'repoList')):
                repoList = load_zipped_pickle(join(DATA_PATH,'repoList'))
            else:
                jobsPath = '/Users/anil.koyuncu/Downloads/jobs/'
                files = listdir(jobsPath)



                def findRepos(file):
                    a = pd.read_json(join(jobsPath, file))
                    repoList = a[a.config.apply(lambda x: x['language'] == 'c')].repository_slug.unique().tolist()
                    return repoList
                repos = parallelRunMerge(findRepos,files)
                repos
                repoList = list(itertools.chain.from_iterable(repos))
                repoList = list(set(repoList))

                save_zipped_pickle(repoList,join(DATA_PATH,'repoList'))
            repoList
            if not isfile(join(DATA_PATH,'repoDF')):
                import requests

                results = []

                start = 1
                prevDF = load_zipped_pickle( join(DATA_PATH, 'repoDF'))
                prevMap = prevDF.set_index('repo').to_dict()['commitCount']
                repoDF = pd.DataFrame(columns=['repo', 'commitCount'])

                for idx, repo in enumerate(prevDF.repo.values.tolist()):
                    # commit count

                    import time

                    # print("Printed immediately.")
                    # time.sleep(0.1)
                    # resp = requests.get('https://github.com/'+repo)
                    # if resp.status_code != 200:
                    #     continue
                        # repoDF.loc[idx] = [repo, '',False]
                    # else:
                    if repo in prevMap:
                        count = prevMap[repo]
                    if count == '':
                        resp = requests.get('https://github.com/' + repo)
                        print(repo)
                        repo = resp.url.split('https://github.com/')[-1]
                        print(repo)
                        cmd = 'curl -H "Authorization: token 39af4590fec0181ee47c17104d46aa179560b1d9" -I -k "https://api.github.com/repos/'+repo+'/commits?per_page=1"'+ " | sed -n '/^[Ll]ink:/ s/.*\"next\".*page=\([0-9]*\).*\"last\".*/\\1/p'"
                        o,e=shellGitCheckout(cmd)
                        print(e)
                        print(o)
                        count = o.strip()
                    repoDF.loc[idx] = [repo, count]
                        # import bs4 as bs
                        #
                        # from urllib.request import urlopen
                        # from urllib import error
                        # import urllib
                        #
                        # import socket
                        #
                        # timeout = 30
                        # socket.setdefaulttimeout(timeout)
                        #
                        # import logging
                        #
                        # hdr = {
                        #     'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11',
                        #     'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
                        #     'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.3',
                        #     'Accept-Encoding': 'none',
                        #     'Accept-Language': 'en-US,en;q=0.8',
                        #     'Connection': 'keep-alive'}
                        #
                        # req = urllib.request.Request('https://github.com/'+repo, headers=hdr)
                        #
                        # response = urlopen(req)
                        # the_page = response.read()
                        #
                        # soup = bs.BeautifulSoup(the_page, "html.parser")
                        # meta = soup.find('meta', {'name': 'octolytics-dimension-repository_is_fork'})
                        #
                        # repoDF.loc[idx] = [repo,count,meta['content']]

                repoDF

                def getGitHubApi(r):
                    headers = {'Authorization': 'token %s' % '39af4590fec0181ee47c17104d46aa179560b1d9'}
                    resp = requests.get('https://api.github.com/repos/' + r, headers=headers)
                    if resp.status_code != 200:
                        print(resp.status_code)
                    return resp.json()

                repoDF['info']= repoDF.repo.apply(lambda x:getGitHubApi(x))
            else:
                repoDF= load_zipped_pickle(join(DATA_PATH, 'repoDF'))

            repoDF['isFork'] = repoDF['info'].apply(lambda x: x['fork'])
            nonForks = repoDF[repoDF['isFork'] == False]
            nonForks['forks'] = nonForks['info'].apply(lambda x: x['forks'])
            nonForks['open_issues'] = nonForks['info'].apply(lambda x: x['open_issues'])
            nonForks['watchers'] = nonForks['info'].apply(lambda x: x['watchers'])
            nonForks['subscribers_count'] = nonForks['info'].apply(lambda x: x['subscribers_count'])



            nonForks.commitCount = nonForks.commitCount.apply(lambda x:int(x) if x!='' else 0)
            nonForks.sort_values(by='commitCount', ascending=False, inplace=True)
            nonForks['language'] = nonForks['info'].apply(lambda x: x['language'])
            nonForks[nonForks['language'] == 'C']
            # save_zipped_pickle(repoDF, join(DATA_PATH, 'repoDF'))
            resp = requests.get(
                'https://api.travis-ci.org/repos/php/php-src/builds?after_number=' + str(start * 25))
            # if resp.status_code != 200:
            #     # This means something went wrong.
            #     raise Exception('GET /tasks/ {}'.format(resp.status_code))
            # results.append(resp.json())
            # start += 1



        elif job == 'test':
            if isfile(join(DATA_PATH, 'buildDFprocessed.pickle')):
                res= load_zipped_pickle(join(DATA_PATH, 'buildDFprocessed.pickle'))
            else:
                if isfile(join(DATA_PATH, 'buildDF.pickle')):
                    buildDF = load_zipped_pickle(join(DATA_PATH, 'buildDF.pickle'))
                else:
                    import requests

                    results = []

                    start = 1
                    while( start* 25 < 9250):
                        resp = requests.get('https://api.travis-ci.org/repos/kamailio/kamailio/builds?after_number='+str(start*25))
                        if resp.status_code != 200:
                            # This means something went wrong.
                            raise Exception('GET /tasks/ {}'.format(resp.status_code))
                        results.append(resp.json())
                        start+=1

                    buildList = list(itertools.chain.from_iterable(results))
                    buildDF = pd.DataFrame(buildList)
                    save_zipped_pickle(buildDF, join(DATA_PATH, 'buildDF.pickle'))

                master = buildDF[buildDF.branch == 'master']
                master = master[master.result == 1]

                failingBuilds = master.number.values.tolist()
                master['inducing'] = master.number.apply(lambda x: str(int(x) - 1) not in failingBuilds)
                bugInducingBuilds = master[master.inducing == True]
                bugInducingBuilds['number'] = bugInducingBuilds.number.apply(lambda x: str(x).zfill(5))
                bugInducingBuilds.sort_values(by='number', inplace=True)

                pushes = bugInducingBuilds[bugInducingBuilds.event_type == 'push']

                DATASET_PATH = os.environ["REPO_PATH"]
                def checkStat(x, dsName):
                    # repo = '/Users/anil.koyuncu/projects/linux'
                    cmd = 'git -C ' + join(DATASET_PATH, dsName) + ' show ' + x + " --pretty=\"format:\" --stat -M100%"

                    out, err = shellGitCheckout(cmd, enc='latin1')
                    log = {}
                    lines = out.strip().split('\n')
                    for line in lines[:-1]:
                        fname,ftype= line.split('|')
                        fname = fname.strip()
                        ftype= ftype.strip()
                        if not (ftype == '0' or ftype == 'Bin'):

                            ftypes = set(ftype.split(' ')[1])
                            if len(ftypes) == 2:
                                ftype = 'M'
                            else:
                                if list(ftypes)[0] == '+':
                                    ftype = 'A'
                                else:
                                    ftype = 'D'

                        log[fname] = ftype
                    log
                    df = pd.DataFrame(data=[[log, x]], columns=['files', 'commit'])
                    return df

                from common.commons import parallelRunMergeNew

                gitrepo = 'kamailio'
                # checkStat('06e06f026d14e7d61f2ceb14445a683abbbf91bb', gitrepo)
                m = parallelRunMergeNew(checkStat, pushes['commit'].values.tolist(), gitrepo)
                res = pd.merge(pushes, m, on=['commit'])
                #remove empty commits
                res = res[res.files != {}]
                # c changes
                res = res[res.files.apply(lambda x: np.any([i.endswith('.c') for i in x.keys()]))]

                def getJobs(x):
                    import requests
                    resp = requests.get('https://api.travis-ci.org/repos/kamailio/kamailio/builds/'+str(x))
                    if resp.status_code != 200:
                        # This means something went wrong.
                        raise Exception('GET /tasks/ {}'.format(resp.status_code))
                    result = resp.json()

                    return result

                res['buildDetails'] = res['id'].apply(lambda x:getJobs(x))
                save_zipped_pickle(res, join(DATA_PATH, 'buildDFprocessed.pickle'))
            res

            def getMatrix(x):
                if len(x) == 1:
                    return False
                else:
                    for jobs in x:
                        if('arch' in jobs['config']):
                             if (jobs['config']['arch'] == 'amd64' and jobs['result'] == 1):
                                 return True
            res['goodBuild'] = res.buildDetails.apply(lambda x:getMatrix(x['matrix']))

            f = '/Users/anil.koyuncu/projects/datasets/kamailio'
            gitrepo = 'kamailio'

            if isfile(join(DATA_PATH,gitrepo + '.df')):
                res = load_zipped_pickle(join(DATA_PATH,gitrepo + '.df'))
            else:
                from commitCollector import makeDF

                #
                # cmd = 'git -C '+f +" log --no-merges --pretty=format:'{\"commit\":\"%H\",\"commitDate\":\"%ci\",\"title\":\"%f\",\"committer\":\"%ce\"}' --follow -- .travis.yml > "+join(DATA_PATH,gitrepo+'.travis')
                # output = shellCallTemplate(cmd,enc='latin1')
                #
                # commits = makeDF(join(DATA_PATH,gitrepo+'.travis'))

                from otherDatasets import checkCommitLog


                # cmd = 'git log --no-merges --pretty=format:'{"commit":"%H","commitDate":"%ci","title":"%f","committer":"%ce"}' > ../tcl.commits'
                cmd = 'git -C ' + f + " log --simplify-merges --date-order -m --pretty=format:'{\"commit\":\"%H\",\"commitDate\":\"%ci\",\"title\":\"%f\",\"committer\":\"%ce\"}' > " + join(DATA_PATH,gitrepo + '.commits')
                output = shellCallTemplate(cmd,enc='latin1')

                commits = makeDF(join(DATA_PATH,gitrepo + '.commits'))
                from common.commons import parallelRunMergeNew
                m = parallelRunMergeNew(checkCommitLog, commits['commit'].values.tolist(), gitrepo)
                res = pd.merge(commits, m, on=['commit'])
                # from common.commons import save_zipped_pickle
                # save_zipped_pickle(res,join(DATA_PATH,gitrepo + '.df'))


                def getRevParse(x):
                    cmd = 'git -C ' + f + ' rev-parse ' + x + '^@'
                    out, err = shellGitCheckout(cmd, enc='latin1')
                    rev = out.strip().split('\n')
                    # if len(rev) != 1:
                    #     print('error')

                    df = pd.DataFrame(data=[[rev, x]], columns=['parent', 'commit'])
                    return df


                m = parallelRunMergeNew(getRevParse, res['commit'].values.tolist())
                res1 = pd.merge(res, m, on=['commit'])
                save_zipped_pickle(res1, join(DATA_PATH, gitrepo + '.df'))

            # workList = []
            # for i,commit in enumerate(res['commit'].values.tolist()):
            #     os.makedirs(join(DATA_PATH, 'tcl_patches',str(i).zfill(5) +'_'+commit ))
            #     cmd = 'git -C ' + f + ' format-patch -M100% --text --full-index --binary -n '+ commit+'^..'+commit +' -o ' + join(DATA_PATH, 'tcl_patches',str(i).zfill(5) +'_'+commit)
            #     workList.append(cmd)
            #
            # parallelRun(shellGitCheckout,workList)


            # patches = listdir('/Users/anil.koyuncu/projects/datasets/test7')

            travisChanges = res[res.files.apply(lambda x: np.any([i == '.travis.yml' for i in x.keys()]))]
            travisCommits = travisChanges.commit.values.tolist()
            # for travis in travisCommits:
            #     cmd = 'git -C ' + f + ' checkout -f '+ travis
            #     o, e = shellGitCheckout(cmd)
            #     os.makedirs(join(DATA_PATH, 'travis_commits', travis), exist_ok=True)
            #     o,e
            #     print(o)
            #     print(e)
            #     cmd = 'cp '+ join(f,'.travis.yml') +" "+ join(DATA_PATH, 'travis_commits', travis)
            #     o, e = shellGitCheckout(cmd)
            #     o,e
            #     newContent = ''
            #     with open(join(DATA_PATH, 'travis_commits', travis,'.travis.yml'),'r') as file:
            #         travisFile = file.read()
            #         regex = r"include:(.*)before_install:"
            #         matches = re.finditer(regex, travisFile, re.MULTILINE|re.DOTALL)
            #
            #         for matchNum, match in enumerate(matches, start=1):
            #             grs = match.groups()
            #             if len(re.findall('- name:', match.group(1))) > 0:
            #                 newContent = travisFile.replace(grs[0],
            #                                    '\n    - name: "Linux/GCC/Shared"\n      os: linux\n      dist: bionic\n      compiler: gcc\n      env:\n        - BUILD_DIR=unix\n')
            #             else:
            #                 newContent = travisFile.replace(grs[0],'\n    - os: linux\n      dist: xenial\n      compiler: clang\n      env:\n        - BUILD_DIR=unix\n')
            #
            #     with open(join(DATA_PATH, 'travis_commits', travis,'.travis.yml'),'w') as file:
            #         file.write(newContent)


            res = res.head(res[res.commit == '1cb8068b970358e7b0f47935a2636ca6ae8eceb6'].iloc[0].name)
            res.sort_index(inplace=True, ascending=False)
            # res = res[res.parent.apply(lambda x: len(x) == 1)]
            commits = res.commit.values.tolist()

            branchName = 'test'

            for commit in commits:
                print(commit)
                cmd = 'git -C ' + f + ' checkout -f '+ commit
                o, e = shellGitCheckout(cmd)
                o,e
                print(e)

                if not isfile(join(f, '.travis.yml')):
                    continue


                # if commit in travisCommits:
                #     cmd = 'git -C ' + f + ' pull -Xtheirs origin ' + branchName
                #     o, e = shellGitCheckout(cmd)
                #     o,e
                #     print(e)
                #
                #     cmd = 'cp ' + join(DATA_PATH, 'travis_commits', commit,'.travis.yml')+ " " + f
                #     o, e = shellGitCheckout(cmd)
                #     o,e
                #     print(e)
                #     cmd = 'git -C ' + f + ' commit -a -m ' + "'travis'"
                #     o, e = shellGitCheckout(cmd)
                #     o,e
                #     print(e)
                # else:
                #
                #     cmd = 'git -C ' + f + ' pull -Xtheirs origin ' + branchName
                #     o, e = shellGitCheckout(cmd)
                #     o,e
                #     print(e)

                cmd = 'git -C ' + f + ' push origin HEAD:'+branchName
                o, e = shellGitCheckout(cmd)
                o,e
                print(e)
                if e.startswith('error'):
                    print(e)
                time.sleep(10)

            # ######export patches
            # res['path'] = res[['commit','parent']].apply(lambda x: [(i,x['commit']) for i in x['parent']] ,axis=1)
            # paths = (list(itertools.chain.from_iterable(res.path.values.tolist())))
            #
            #
            # # res['pairs'] = res.apply(lambda x: (x['commit'], x['parent']) if x['parent'] != None else None, axis=1)
            # # res['tuples'] = res.pairs.apply(lambda x: tuple(x))
            # # col_combi = res.tuples.values.tolist()
            # import networkx
            # # #
            # g = networkx.Graph(paths)
            # cluster = []
            # for subgraph in networkx.connected_component_subgraphs(g):
            #     logging.info('Cluster size %d', len(subgraph.nodes()))
            #     cluster.append(subgraph.nodes())
            # cluster
            # # p = networkx.shortest_path(g)
            # # p
            #
            # parentDictionary= dict(networkx.dfs_successors(g, source=''))
            # workList = []
            # for i,c in enumerate(list(pairwise(list(networkx.dfs_preorder_nodes(g, source=''))))):
            #     parent,commit = c
            #     if parent =='':
            #         continue
            #     os.makedirs(join(DATA_PATH, 'tcl_patches_',str(i).zfill(5) +'_'+parent ),exist_ok=True)
            #
            #     cmd = 'git -C ' + f + ' format-patch -M100% --text --full-index --binary -n '+ parent+'..'+commit +' -o ' + join(DATA_PATH, 'tcl_patches_',str(i).zfill(5) +'_'+parent)
            #     workList.append(cmd)
            #
            # parallelRun(shellGitCheckout,workList)
            #
            # ########end export patches
            #
            # # import matplotlib.pyplot as plt
            # # networkx.draw(g, with_labels=True, font_weight='bold')
            # # plt.subplot(122)
            # # cluster.sort(key=len, reverse=True)
            # # connectedCommits = [i for i in cluster[0]] #10280
            # # res['connected']=res.commit.apply(lambda x: x in connectedCommits)
            # #
            # # selectedPatches = res[['commit','rowIndex', 'parent', 'connected', 'parentPos']]
            # # selectedPatches.sort_values(by='parentPos', ascending=False, inplace=True)
            # # selectedPatches[selectedPatches['connected'] == True]
            #
            #
            # #
            # # # parentDict = res[['parent']].to_dict()
            # # # pDict = {value: key for key, value in parentDict['parent'].items()}
            # # # res['hasParent'] = res.commit.apply(lambda x: x in pDict)
            # # ## find good commits
            # # # orderDict = res[['commit']].to_dict()
            # # #
            # # # dict = {value: key for key, value in orderDict['commit'].items()}
            # # #
            # # # res['parentPos'] =res.parent.apply(lambda x: dict[x] if x in dict else None)
            # # #
            # # #
            # # #
            # # #
            # # # def rowIndex(row):
            # # #     return row.name
            # # #
            # # #
            # # # res['rowIndex'] = res.apply(rowIndex, axis=1)
            # # #
            # # # save_zipped_pickle(res, join(DATA_PATH, gitrepo + '.df'))
            # # ## end find good commits
            # # travisChanges = res[res.files.apply(lambda x: np.any([i == '.travis.yml' for i in x.keys()]))]
            # # travisCommits = travisChanges.commit.values.tolist()
            # #
            # #
            # # for i, commit in zip(selectedPatches['rowIndex'].values.tolist(),selectedPatches['commit'].values.tolist()):
            # #     print(i,commit)
            # #     if commit in travisCommits:
            # #         print()
            # #     patch = str(i).zfill(5) + "_" + commit
            # #     p = listdir(join(DATA_PATH, 'tcl_patches', patch))
            # #     if(len(p) == 0):
            # #         continue
            # #     if(len(p) > 1):
            # #         print('error')
            # #     cmd = 'git -C ' + f + ' am -q -3 --whitespace=nowarn ' + join(DATA_PATH, 'tcl_patches', patch, p[0])
            # #     o,e= shellGitCheckout(cmd)
            # #     # print(o)
            # #     # print(e)
            # #
            # #     if len(e)> 0:
            # #         print(patch)
            # #
            # #     cmd = 'git -C ' + f + ' push'
            # #     o, e = shellGitCheckout(cmd)
            # #     o,e
            #
            # patches = listdir(join(DATA_PATH, 'tcl_patches_'))
            # patches.sort()
            #
            # for patch in patches:
            #     if patch == '.DS_Store':
            #         continue
            #     # if patch.split('_')[1] in travisCommits:
            #     #     print()
            #     p = listdir(join(DATA_PATH, 'tcl_patches_', patch))
            #
            #     if(len(p) == 0):
            #         continue
            #     p.sort()
            #     if(len(p) > 1):
            #         print('error')
            #
            #     # if(p[0] == '0001-Initial-revision.patch'):
            #     #     continue
            #     for actualPatch in p:
            #         cmd = 'git -C ' + f + ' am -q -3 --whitespace=nowarn ' + join(DATA_PATH, 'tcl_patches_', patch, actualPatch)
            #         # cmd = 'git -C ' + f + ' am -q -3 --whitespace=nowarn ' + join('/Users/anil.koyuncu/projects/datasets/test7', patch)
            #         o,e= shellGitCheckout(cmd)
            #         # print(o)
            #         # print(e)
            #
            #         if len(e)> 0:
            #             print(patch)
            #
            #     # cmd = 'git -C ' + f + ' push'
            #     # o, e = shellGitCheckout(cmd)
            #     # o,e



            # for i,v in zip(travisChanges.commit.index.tolist(),travisChanges.commit.values.tolist()):
            #     i,v


             #            '''
             # git checkout master
             # git branch -D start
             # git checkout -f -b start c6a259aeeca4814a97cf6694814c63e74e4e18fa
             # git checkout -f -b start 1cb8068b970358e7b0f47935a2636ca6ae8eceb6
             # git gc --prune=now
             # git push --set-upstream origin startTravis

             #563 from test branch start
             #            '''
        else:
            logging.error('Unknown job %s',job)
    except Exception as e:
        logging.error(e)
