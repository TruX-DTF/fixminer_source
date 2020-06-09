
import redis

from common.commons import *

DATA_PATH = os.environ["DATA_PATH"]
DATASET = os.environ["dataset"]
jdk8 = os.environ["JDK8"]
REDIS_PORT = os.environ["REDIS_PORT"]

# def localPairCore(aTuple):
#     redis_db = redis.StrictRedis(host="localhost", port=6380, db=1)
#     idx, key = aTuple
#
#
#
#     val = redis_db.get(key)
#
#     res = val.decode().split(',')
#     res.insert(0, key.decode().split('_')[1:])
#     res.insert(0, key.decode())
#     # matches.loc[idx] = res
#     return res
ast = ["AnonymousClassDeclaration", "ArrayAccess", "ArrayCreation", "ArrayInitializer", "ArrayType", "AssertStatement",
       "Assignment", "Block", "BooleanLiteral", "BreakStatement", "CastExpression", "CatchClause", "CharacterLiteral",
       "ClassInstanceCreation", "CompilationUnit", "ConditionalExpression", "ConstructorInvocation",
       "ContinueStatement", "DoStatement", "EmptyStatement", "ExpressionStatement", "FieldAccess", "FieldDeclaration",
       "ForStatement", "IfStatement", "ImportDeclaration", "InfixExpression", "Initializer", "Javadoc",
       "LabeledStatement", "MethodDeclaration", "MethodInvocation", "NullLiteral", "NumberLiteral",
       "PackageDeclaration", "ParenthesizedExpression", "PostfixExpression", "PrefixExpression", "PrimitiveType",
       "QualifiedName", "ReturnStatement", "SimpleName", "SimpleType", "SingleVariableDeclaration", "StringLiteral",
       "SuperConstructorInvocation", "SuperFieldAccess", "SuperMethodInvocation", "SwitchCase", "SwitchStatement",
       "SynchronizedStatement", "ThisExpression", "ThrowStatement", "TryStatement", "TypeDeclaration",
       "TypeDeclarationStatement", "TypeLiteral", "VariableDeclarationExpression", "VariableDeclarationFragment",
       "VariableDeclarationStatement", "WhileStatement", "InstanceofExpression", "LineComment", "BlockComment",
       "TagElement", "TextElement", "MemberRef", "MethodRef", "MethodRefParameter", "EnhancedForStatement",
       "EnumDeclaration", "EnumConstantDeclaration", "TypeParameter", "ParameterizedType", "QualifiedType",
       "WildcardType", "NormalAnnotation", "MarkerAnnotation", "SingleMemberAnnotation", "MemberValuePair",
       "AnnotationTypeDeclaration", "AnnotationTypeMemberDeclaration", "Modifier", "UnionType", "Dimension",
       "LambdaExpression", "IntersectionType", "NameQualifiedType", "CreationReference", "ExpressionMethodReference",
       "SuperMethodReference", "TypeMethodReference", "MethodName", "Operator", "New", "Instanceof"]

ast = ["unit","comment","name","type","condition","block","index","decltype","typename","atomic","assert","generic_selection","selector","association_list",
       "association","expr_stmt","expr","decl_stmt","decl","range","break","continue","goto","label","typedef","asm","enum","ternary","elseif","while","lock",
       "fixed","checked","unchecked","unsafe","do","switch","case","default","for","foreach","group","init","incr","function","function_decl","lambda","specifier",
       "return","call","sizeof","parameter_list","param","krparameter_list","krparam","argument_list","argument","capture","struct","struct_decl","union","union_decl",
       "class","class_decl","public","private","protected","namespace","using","try","catch","finally","throw","throws","noexcept","template","directive","file","number",
       "include","define","undef","line","if","ifdef","ifndef","else","elif","endif","then","pragma","error","macro","value","import","constructor_decl","empty_stmt","escape","annotation","alignof","forever","extern"]

movPattern = 'MOV (' + '|'.join(ast) + ')@@(.*)@TO@ (' + '|'.join(ast) + ')@@(.*)@AT@'
delPattern = 'DEL (' + '|'.join(ast) + ')@@(.*)@AT@'
insPattern = 'INS (' + '|'.join(ast) + ')@@(.*)@TO@ (' + '|'.join(ast) + ')@@(.*)@AT@'
updPattern = 'UPD (' + '|'.join(ast) + ')@@(.*)@TO@(.*)@AT@'

def loadPairMulti(root,clusterPath,level):

    # root = 'BreakStatement'
    logging.info(root)
    port = REDIS_PORT
    # if isfile(clusterPath +"/"+root+".pickle"):
    #     return load_zipped_pickle(clusterPath +"/"+root+".pickle")
    # else:
        # redis_db = redis.StrictRedis(host="localhost", port=port, db=1)  #L1
    if level == 'tokens':
        redis_db = redis.StrictRedis(host="localhost", port=port, db=3)
    else:
        redis_db = redis.StrictRedis(host="localhost", port=port, db=2)
    keys = redis_db.scan(0, match=root+'-*', count='100000000')
    # keys = redis_db.hkeys("dump")

    # tuples = []
    # for idx,key in enumerate(keys[1]):
    #     t = idx,key
    #     tuples.append(t)

    # coreNumber = 1600
    # print('Core number %s' % coreNumber)
    matches = pd.DataFrame(keys[1],columns=['pairs_key'])
    matches['pairs_key']=matches['pairs_key'].apply(lambda x:x.decode())
    # matches['pairs']=matches['pairs_key'].apply(lambda x:x.split('_')[1:])
    matches['pairs']=matches['pairs_key'].apply(lambda x:x.split(root)[1].split('/')[1:])
    matches['tuples'] = matches.pairs.apply(lambda x: tuple(x))
    # matches['path1']=matches['pairs_key'].apply(lambda x:x.split('_')[1])
    matches['path1']=matches['pairs'].apply(lambda x:x[0])
    # matches['path2']=matches['pairs_key'].apply(lambda x:x.split('_')[2])
    matches['path2']=matches['pairs'].apply(lambda x:x[1])
    # matches['sizes']=matches['pairs_key'].apply(lambda x:x.split('_')[0].split('-')[1])
    matches['sizes']=matches['pairs_key'].apply(lambda x:x.split(root)[1].split('/')[0].split('-')[1])
    if level == 'tokens':
        matches['tokens']=matches['pairs_key'].apply(lambda x:x.split('/')[0].split('-')[2])
    # if level == 'tokens':
    #     matches['actions'] = matches['pairs_key'].apply(lambda x: x.split('/')[0].split('-')[2])
    #     matches['tokens']=matches['pairs_key'].apply(lambda x:x.split('/')[0].split('-')[3])


    # save_zipped_pickle(matches,clusterPath +"/"+root+".pickle")
    return matches



def getMapping(pathMapping,x):
    pair1,pair2 = x['pairs']
    p1 = x['path1']
    p2 = x['path2']
    pathMapping[pair1] = p1
    pathMapping[pair2] = p2





def cluster(clusterPath,pairsPath, level):

        try:
            # logging.info('Parameters: \ninputPath %s \nclusterPath %s \nport %s \nmatchesName %s \nthreshold %s \n%indexFile',inputPath,clusterPath,str(port),matchesName,str(threshold),indexFile)
            os.makedirs(clusterPath, exist_ok=True)
            roots = listdir(pairsPath)
            roots = [i for i in roots if not i.startswith('.')]
            # roots = [rootType]
            # parallelRun(loadPairMulti,roots,clusterPath)
            for root in roots:
                matches = loadPairMulti(root,clusterPath,level)
                sizes = matches['sizes'].unique().tolist()
                for s in sizes:
                    match = matches[matches['sizes'] == s]

                    if level == 'tokens':
                        actions = match['tokens'].unique().tolist()
                        for action in actions:
                            match = match[match['tokens'] == action]
                            clusterCore(clusterPath,  level, match, pairsPath, root, s,action)
                    # elif level == 'tokens':
                    #     actions = match['actions'].unique().tolist()
                    #     for action in actions:
                    #         match = match[match['actions'] == action]
                    #         tokens = match['tokens'].unique().tolist()
                    #         for token in tokens:
                    #             match = match[match['tokens']==token]
                    #             clusterCore(clusterPath, level, match, pairsPath, root, s, action,token)
                    else:
                        clusterCore(clusterPath,  level, match, pairsPath, root, s,'')

            # redis_db = redis.StrictRedis(host="localhost", port=6399, db=2)
            # redis_db.flushdb()
            # redis_db = redis.StrictRedis(host="localhost", port=6399, db=1)
            # redis_db.flushdb()
            # redis_db = redis.StrictRedis(host="localhost", port=6399, db=0)
            # redis_db.delete("compare")



        except Exception as ex:
            logging.error(ex)


def clusterCore(clusterPath, level, match, pairsPath, root, s,action ,token=''):
    col_combi = match.tuples.values.tolist()
    import networkx
    g = networkx.Graph(col_combi)
    cluster = []
    for subgraph in networkx.connected_component_subgraphs(g):
        logging.info('Cluster size %d',len(subgraph.nodes()))
        cluster.append(subgraph.nodes())
    cluster
    pathMapping = dict()
    if level == 'tokens':
        indexFile = join(pairsPath, root, s,action+'.index')
    elif level == 'actions':
        indexFile = join(pairsPath, root, s + '.index')
    # else:
    #     indexFile =join(pairsPath, root, s,action,token+'.index')
    df = pd.read_csv(indexFile, header=None, usecols=[0, 1], index_col=[0])
    pathMapping = df.to_dict()

    workList = []
    for idx, clus in enumerate(cluster):
        logging.info('exporting cluster %s %s %s %d', root,s,action,idx)
        for f in clus:
            dumpFile = pathMapping[1][int(f)]

            t = dumpFile,root,level,clusterPath,s,action,token,idx
            workList.append(t)

    parallelRun(dumpFilesCore,workList)
    # for wl in workList:
    #     dumpFilesCore(wl)
    # dumpFilesCore(('hive_d65d5c_96c1dc_ql#src#gen#protobuf#gen-java#org#apache#hadoop#hive#ql#io#orc#OrcProto.txt_31', 'ReturnStatement', 'tokens', '/Users/anil.koyuncu/projects/richedit-all/enhancedASTDiff/python/data/tokens', '3', '1', '0', 10))


def dumpFilesCore(t):

    try:
        dumpFile, root, level, clusterPath, s, action, token, idx = t
        split = dumpFile.split('_')
        project = split[0]
        filename = "_".join(split[1:-1])
        filePath = join(DATASET, project, 'DiffEntries', filename)

        key = root + '/'+s+'/'+dumpFile
        jdk8 = os.environ["JDK8"]
        # cmd = "JAVA_HOME='"+jdk8+"' java -jar "+ join(DATA_PATH,'FixPatternMiner-1.0.1.jar') + " " + join(DATA_PATH,'app.properties')+" PATTERN " +key

        clusterSavePath = join(clusterPath, root, s, str(idx))
        os.makedirs(clusterSavePath, exist_ok=True)
        shutil.copy(filePath,join(clusterSavePath,dumpFile))
        # with open(join(clusterSavePath, dumpFile), 'w', encoding='utf-8') as writeFile:
        #     writeFile.write(lines)



        clusterSavePath = ''
        # if level == 'shapes':
        #     clusterSavePath = join(clusterPath, root,s, str(idx))
        #
        #     o, e = shellGitCheckout(cmd)
        #     lines = o
        # elif level == 'actions':
        #     clusterSavePath = join(clusterPath, root, s,action, str(idx))
        #
        #     o, e = shellGitCheckout(cmd)
        #     lines = o
        # else:
        #     clusterSavePath = join(clusterPath, root, s,action,token, str(idx))
        #     o, e = shellGitCheckout(cmd)
        #     lines = o
        #     # with open(filePath, 'r', encoding='utf-8') as fi:
        #     #     lines = fi.read()
        #
        # if level =='shapes' or level=='actions':
        #
        #     lines = re.split("@LENGTH@ \d+", lines)
        #     tokens = []
        #     for line in lines:
        #         # levelPatch  = len(re.findall('\w*---', line))
        #         match = re.search(r"^\w*---+", line,re.M)
        #
        #
        #         if match is not None:
        #             not_matched, matched = line[:match.start()], match.group()
        #             levelPatch  = int(len(matched) / 3)
        #         else:
        #             levelPatch = 0
        #         line = line.strip().strip('-')
        #         type = ''
        #         if line is '':
        #             continue
        #         t = []
        #         searchPattern = ''
        #         if line.startswith('INS'):
        #             if level =='actions':
        #                 t= [1,3]
        #             else:
        #                 t = [1]
        #             searchPattern = insPattern
        #             type =' INS '
        #         elif line.startswith('UPD'):
        #             t = [1]
        #             searchPattern = updPattern
        #             type = ' UPD '
        #         elif line.startswith('DEL'):
        #             t = [1]
        #             searchPattern = delPattern
        #             type = ' DEL '
        #         elif line.startswith('MOV'):
        #             if level == 'actions':
        #                 t = [1, 3]
        #             else:
        #                 t = [1]
        #             searchPattern = movPattern
        #             type = ' MOV '
        #     # from common.preprocessing import preprocessingForSimi
        #         m = re.search(searchPattern, line, re.DOTALL)
        #         if t is None:
        #             print()
        #         if m:
        #             for k in t:
        #                 prefix = '---' * levelPatch
        #                 if prefix != '':
        #                     prefix = '\n'+prefix
        #                 token = m.group(k)
        #                 if level =='actions':
        #                     if k ==3:
        #                         prefix = 'TO '
        #                     else:
        #                         prefix = prefix + type
        #
        #                 tokens.append(prefix+token)
        #
        #     os.makedirs(clusterSavePath, exist_ok=True)
        #     with open(join(clusterSavePath, dumpFile), 'w', encoding='utf-8') as writeFile:
        #         # if levelPatch == 0:
        #         writeFile.write(' '.join(tokens))
        #         # else:
        #         #     writeFile.write('\n'.join(tokens))
        # else:
        #     os.makedirs(clusterSavePath, exist_ok=True)
        #     with open(join(clusterSavePath, dumpFile), 'w', encoding='utf-8') as writeFile:
        #         writeFile.write(lines)

    except Exception as ex:
        logging.error(t)
        logging.error(ex)
        # raise ex








