from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
PROJECT_TYPE = os.environ["PROJECT_TYPE"]


def bStats():
    if isfile(join(DATA_PATH, 'studyBugReports.pickle')):
        studyBugReports = load_zipped_pickle(join(DATA_PATH, 'studyBugReports.pickle'))
    else:
        brs = load_zipped_pickle(join(DATA_PATH, args.subject + "bugReportsComplete.pickle"))
        commits = load_zipped_pickle(join(DATA_PATH, 'singleBR.pickle'))

        dbDir = join(DATA_PATH, 'redis')

        portInner = '6399'
        startDB(dbDir, portInner, PROJECT_TYPE)

        import redis

        redis_db = redis.StrictRedis(host="localhost", port=portInner, db=0)
        keys = redis_db.scan(0, match='*', count='1000000')

        matches = pd.DataFrame(keys[1], columns=['pairs_key'])

        # matches = load_zipped_pickle(join(DATA_PATH,'singleHunks'))
        matches['pairs_key'] = matches['pairs_key'].apply(lambda x: x.decode())
        matches['root'] = matches['pairs_key'].apply(lambda x: x.split('/')[0])
        matches['size'] = matches['pairs_key'].apply(lambda x: x.split('/')[1])
        matches['file'] = matches['pairs_key'].apply(lambda x: x.split('/')[2])
        matches['repo'] = matches['file'].apply(lambda x: x.split('_')[0])
        matches['commit'] = matches['file'].apply(lambda x: x.split('_')[1])

        subjects = pd.read_csv(join(DATA_PATH, 'subjects.csv'))

        def getBID(x):
            try:
                if x['repo'].endswith('.git'):
                    return None
                subject = subjects.query("Repo == '{0}'".format(x['repo'])).Subject.tolist()[0]
                bids = commits.query(
                    "commit.str.startswith('{0}') and project== '{1}'".format(x['commit'], subject)).bid.tolist()
                return bids[0]
            except Exception as e:
                logging.error(e)

        matches = matches[~matches.repo.apply(
            lambda i: (i.startswith('commons-math') or i.startswith('commons-lang') or i.startswith(
                'closure-compiler') or i.startswith('joda-time') or i.startswith('mockito') or i.startswith(
                'jfreechart')))]
        matches['bid'] = matches.apply(lambda x: getBID(x), axis=1)

        subjects
        # res = pd.merge(matches, brs, on=['bid'])
        save_zipped_pickle(matches, join(DATA_PATH, 'studyDataset.pickle'))
        studyBugReports = brs[brs.bid.isin(matches.bid.unique())]
        save_zipped_pickle(studyBugReports, join(DATA_PATH, 'studyBugReports.pickle'))
    if isfile(join(DATA_PATH, 'studyBR_DTM_index')):
        brIndexes = load_zipped_pickle(join(DATA_PATH, 'studyBR_DTM_index'))
        bugDTM = load_zipped_pickle(join(DATA_PATH, 'studyBR_DTM'))
        vectorDF = load_zipped_pickle(join(DATA_PATH, 'studyBR_vector'))
        matches = load_zipped_pickle(join(DATA_PATH, 'studyDataset.pickle'))
    else:
        studyBugReports['description'] = studyBugReports['description'].fillna("")
        studyBugReports['sumDesc'] = studyBugReports['summary'] + studyBugReports['description']
        # corpus['sumDesc'] = corpus['summary'] + corpus['desc']
        # from common.preprocessing import
        # result, aVector = getVectorAndDtm(corpus, 'summary')
        from common.preprocessing import calculateTfIdfNLList

        corpusBug = studyBugReports['sumDesc'].values.tolist()
        from common.preprocessing import preprocessingNL

        preCorpusBug = list(map(preprocessingNL, corpusBug))

        v = calculateTfIdfNLList(preCorpusBug)
        bugDTM = v.transform(preCorpusBug)
        bugDTM
        save_zipped_pickle(bugDTM, join(DATA_PATH, 'studyBR_DTM'))
        brIndexes = studyBugReports['bid'].values.tolist()

        save_zipped_pickle(brIndexes, join(DATA_PATH, 'studyBR_DTM_index'))
        # from sklearn.metrics.pairwise import cosine_similarity
        # cosine_similarity(bugDTM[11701], bugDTM[11111])
        vectorDF = pd.DataFrame(columns=['bid', 'dtm'])
        # idx = 0
        for idx, val in enumerate(brIndexes):
            vectorDF.loc[idx] = [val, bugDTM[idx]]
        vectorDF

        save_zipped_pickle(vectorDF, join(DATA_PATH, 'studyBR_vector'))

    matches
    if isfile(join(DATA_PATH, 'study_clusters')):
        clustersDF = load_zipped_pickle(join(DATA_PATH, 'study_clusters'))
    else:
        clustersDF = pd.DataFrame(columns=['cid', 'type', 'members'])
        idx = 0

        def statsCore(cs, type):
            global idx

            cs = [i for i in cs if not (i.startswith('commons-math') or i.startswith('commons-lang') or i.startswith(
                'closure-compiler') or i.startswith('joda-time') or i.startswith('mockito') or i.startswith(
                'jfreechart'))]
            # print('Cluster %s : member size %s' % (shape+"-"+size +"-"+cluster, len(cs)))
            if len(cs) > 0:
                if token is None:
                    if action is None:
                        t = shape + "-" + size + "-" + cluster

                        clustersDF.loc[idx] = [t, type, cs]
                        idx = idx + 1
                    else:
                        t = shape + "-" + size + "-" + cluster + "-" + action  # , len(cs)
                        clustersDF.loc[idx] = [t, type, cs]
                        idx = idx + 1
                else:
                    # clusterSize = len(cs)
                    # if clusterSize > 0:
                    #     clusterSize = len(set([re.split('.txt_[0-9]+', i)[0] for i in cs]))
                    t = shape + "-" + size + "-" + cluster + "-" + action + "-" + token  # , clusterSize
                    clustersDF.loc[idx] = [t, type, cs]
                    idx = idx + 1

        for type in ['tokens', 'actions', 'shapes']:
            shapesPath = join(DATA_PATH, type)
            shapes = listdir(shapesPath)
            shapes = [f for f in shapes if isdir(join(shapesPath, f))]
            shape = size = cluster = action = token = None

            for shape in shapes:
                if shape.startswith('.'):
                    continue
                sizes = listdir(join(shapesPath, shape))

                for size in sizes:
                    if size.startswith('.'):
                        continue
                    clusters = listdir(join(shapesPath, shape, size))
                    for cluster in clusters:
                        if cluster.startswith('.'):
                            continue
                        cs = listdir(join(shapesPath, shape, size, cluster))

                        if shapesPath.endswith('shapes'):
                            cs = listdir(join(shapesPath, shape, size, cluster))
                            statsCore(cs, 'shapes')
                        else:
                            # level3
                            for action in cs:
                                if action.startswith('.'):
                                    continue
                                tokens = listdir(join(shapesPath, shape, size, cluster, action))
                                if shapesPath.endswith('actions'):
                                    statsCore(tokens, 'actions')
                                else:
                                    for token in tokens:
                                        if token.startswith('.'):
                                            continue
                                        cs = listdir(join(shapesPath, shape, size, cluster, action, token))
                                        statsCore(cs, 'tokens')

        clustersDF
        save_zipped_pickle(clustersDF, join(DATA_PATH, 'study_clusters'))
        clustersDF

        # selected = clustersDF[clustersDF.type =='shapes']

        from sklearn.metrics.pairwise import cosine_similarity
        # cosine_similarity(bugDTM[11701], bugDTM[11111])
        def getSimilarity(x):
            try:
                if len(x) == 1:
                    return [1]
                else:
                    filenames = list(set([re.split('.txt_[0-9]+', i)[0] for i in x]))
                    if len(filenames) == 1:
                        return [1]
                    else:
                        bids2Compare = [matches[matches.file.str.startswith(fn)].bid.unique()[0] for fn in filenames]

                        pairs = list(itertools.combinations(bids2Compare, 2))
                        pairs
                        res = []
                        for p in pairs:
                            p
                            simi = cosine_similarity(vectorDF[vectorDF.bid == p[0]].iloc[0].dtm,
                                                     vectorDF[vectorDF.bid == p[1]].iloc[0].dtm)
                            res.append(simi[0][0])
                        return res
            except Exception as e:
                logging.error(e)

        # import swifter
        clustersDF['simi'] = clustersDF.members.apply(lambda x: getSimilarity(x))
        save_zipped_pickle(clustersDF, join(DATA_PATH, 'study_clusters'))

    clustersDF

    shapes = clustersDF[clustersDF.type == 'shapes']
    actions = clustersDF[clustersDF.type == 'actions']
    tokens = clustersDF[clustersDF.type == 'tokens']

    # shapes
    # yList = [list(itertools.chain.from_iterable(shapes.simi.values.tolist())),
    # list(itertools.chain.from_iterable(actions.simi.values.tolist())),
    # list(itertools.chain.from_iterable(tokens.simi.values.tolist()))]
    # colNames = ['shapes','actions','tokens']

    ys = []
    cols = []
    means = []
    # plotBox(yList, colNames, 'bugReport' + '.pdf', True)
    for ds in [shapes, actions, tokens]:
        ds['ms'] = ds.members.str.len()
        ds.sort_values(by=['ms'], ascending=False, inplace=True)
        top10 = ds.head(20)

        colNames = top10.cid.values.tolist()
        # colNames = [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19]
        colNames = list(range(len(colNames)))
        yList = yList = top10.simi.values.tolist()
        # yList = [np.mean(i) for i in yList]
        # colNames.insert(0,'ALL')
        # yList.insert(0,list(itertools.chain.from_iterable(ds.simi.values.tolist())))
        mean = np.mean(list(itertools.chain.from_iterable(ds.simi.values.tolist())))
        type = ds.type.iloc[0]
        # from common.commons import plotBox
        # plotBox(yList,colNames,type+'.pdf',False)
        ys.append(yList)
        cols.append(colNames)
        means.append(mean)
    plotBox2(ys, cols, 'test.pdf', means, False)
