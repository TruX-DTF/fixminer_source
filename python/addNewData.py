from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]

def core():
    clusterPath = join(DATA_PATH, 'shapes')
    roots = listdir(clusterPath)
    roots = [i for i in roots if not (i.startswith('.') or i.endswith('.pickle'))]
    pattern = {}
    for root in roots:
        root
        sizes = listdir(join(clusterPath, root))
        for size in sizes:
            # actions = listdir(join(clusterPath,root,size))
            # for action in actions:
            clusters = listdir(join(clusterPath, root, size))
            for cluster in clusters:
                members = listdir(join(clusterPath, root, size, cluster))

                # pattern[root+'/'+size+'/'+cluster]= root +'/' +size +'/'+ members[0]
                pattern[root+'/'+size+'/'+cluster]=  members[0]
    pattern

    from pairs import shapePairs
    matches = shapePairs()
    # 'FFmpeg','curl','nginx','openssl','redis','tmux','vlc']
    matches = matches[matches.file.apply(lambda x: x in list(pattern.values()) or not ( x.startswith('linux_') or  x.startswith('FFmpeg_') or x.startswith('curl_') or  x.startswith('nginx_') or  x.startswith('openssl_') or  x.startswith('redis_') or x.startswith('tmux_') or  x.startswith('vlc_')))]
    from pairs import createPairs
    createPairs(matches)
    # # # elif job == 'importShapesPairs':
    from pairs import importShape
    importShape()

def checkWrongMembers():
    clusterPath = join(DATA_PATH, 'shapes')
    roots = listdir(clusterPath)
    roots = [i for i in roots if not (i.startswith('.') or i.endswith('.pickle'))]
    pattern = {}
    for root in roots:
        root
        sizes = listdir(join(clusterPath, root))
        for size in sizes:
            # actions = listdir(join(clusterPath,root,size))
            # for action in actions:
            clusters = listdir(join(clusterPath, root, size))
            for cluster in clusters:
                members = listdir(join(clusterPath, root, size, cluster))
                sizeDict = {}
                for s in [(i,os.path.getsize(join(clusterPath, root, size, cluster,i))) for i in members]:
                    sizeDict[s[1]] = s[0]
                sizeDict
                if len(sizeDict) > 1:
                    print(join(clusterPath, root, size, cluster))
                    print(sizeDict.values())

def cluster():
    clusterPath = join(DATA_PATH, 'shapes')
    roots = listdir(clusterPath)
    roots = [i for i in roots if not (i.startswith('.') or i.endswith('.pickle'))]
    pattern = {}
    for root in roots:
        root
        sizes = listdir(join(clusterPath, root))
        for size in sizes:
            # actions = listdir(join(clusterPath,root,size))
            # for action in actions:
            clusters = listdir(join(clusterPath, root, size))
            for cluster in clusters:
                members = listdir(join(clusterPath, root, size, cluster))

                # pattern[root+'/'+size+'/'+cluster]= root +'/' +size +'/'+ members[0]
                pattern[root+'/'+size+'/'+cluster]=  members[0]
    pattern

    pairsPath = join(DATA_PATH, 'pairs')
    from abstractPatch import loadPairMulti
    for root in roots:
        matches =loadPairMulti(root,'','shapes')
        matches
        sizes = matches['sizes'].unique().tolist()
        for s in sizes:
            match = matches[matches['sizes'] == s]
            match
            clusterCore(pattern,clusterPath, 'shapes', match, pairsPath, root, s, '')

def clusterCore(pattern,clusterPath, level, match, pairsPath, root, s,action ,token=''):
    col_combi = match.tuples.values.tolist()
    import networkx
    g = networkx.Graph(col_combi)
    cluster = []
    for subgraph in networkx.connected_component_subgraphs(g):
        logging.info('Cluster size %d',len(subgraph.nodes()))
        cluster.append(subgraph.nodes())
    cluster
    pathMapping = dict()
    if level == 'actions':
        indexFile = join(pairsPath, root, s,action+'.index')
    elif level == 'shapes':
        indexFile = join(pairsPath, root, s + '.index')
    else:
        indexFile =join(pairsPath, root, s,action,token+'.index')
    df = pd.read_csv(indexFile, header=None, usecols=[0, 1], index_col=[0])
    pathMapping = df.to_dict()

    workList = []

    exportCLusters ={}
    if not os.path.exists(join(clusterPath, root, s)):
        print()
        existingClusters = 0
    else:
        existingClusters = len(listdir(join(clusterPath, root, s)))

    for clus in cluster:
        members = [pathMapping[1][int(i)] for i in clus]
        members
        potentialClusters = [(key, value) for key, value in pattern.items() if key.startswith(root + '/' + s)]
        potentialClusters
        foundExisting = False
        for pc,pcMember in potentialClusters:
            if pcMember in members:
                pc
                foundExisting = True
                exportCLusters[pc.split('/')[-1]] = members
        if not foundExisting:
            exportCLusters[existingClusters] = members
            existingClusters= existingClusters+1
    exportCLusters
    for k,v in exportCLusters.items():
        for f in v:
            t = f, root, level, clusterPath, s, action, token, k
            workList.append(t)
    # for idx, clus in enumerate(cluster):
    #     logging.info('exporting cluster %s %s %s %d', root,s,action,idx)
    #     for f in clus:
    #         dumpFile = pathMapping[1][int(f)]
    #
    #         t = dumpFile,root,level,clusterPath,s,action,token,idx
    #         workList.append(t)
    from abstractPatch import dumpFilesCore
    parallelRun(dumpFilesCore,workList)
    # for wl in workList:
    #     dumpFilesCore(wl)
