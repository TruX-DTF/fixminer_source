from common.commons import *
REDIS_PORT = os.environ["REDIS_PORT"]
DATA_PATH = os.environ["DATA_PATH"]
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

movPattern = 'MOV (' + '|'.join(ast) + ')@@(.*)@TO@ (' + '|'.join(ast) + ')@@(.*)@AT@'
delPattern = 'DEL (' + '|'.join(ast) + ')@@(.*)@AT@'
insPattern = 'INS (' + '|'.join(ast) + ')@@(.*)@TO@ (' + '|'.join(ast) + ')@@(.*)@AT@'
updPattern = 'UPD (' + '|'.join(ast) + ')@@(.*)@TO@(.*)@AT@'

import redis
port = '6380'
redis_db = redis.StrictRedis(host="localhost", port=port, db=0)
redis_db1 = redis.StrictRedis(host="localhost", port=port, db=1)
redis_db2 = redis.StrictRedis(host="localhost", port=port, db=2)

redis_out  = redis.StrictRedis(host="localhost", port=REDIS_PORT, db=0)

def getTokens(prefix, i):
    dist2load = redis_db1.get(prefix + "-" + i);

    with open(join(DATA_PATH, 'actions', prefix.replace('-', '/'), dist2load.decode()), 'r') as rFile:
        lines = rFile.read()

    lines = re.split("@LENGTH@ \d+", lines)
    tokens = []
    for line in lines:
        line = line.strip().strip('-')
        if line is '':
            continue
        t = []
        searchPattern = ''
        if line.startswith('INS'):
            t = [2]
            searchPattern = insPattern
        elif line.startswith('UPD'):
            t = [2, 3]
            searchPattern = updPattern
        elif line.startswith('DEL'):
            t = [2]
            searchPattern = delPattern
        elif line.startswith('MOV'):
            t = [2]
            searchPattern = movPattern

        # MOV TryStatement@@try:[] @TO@ MethodDeclaration@@public, T, T, MethodName:lookupByNameAndType, String name, Class<T> type,  @AT@ 2164 @LENGTH@ 646
        # DEL VariableDeclarationStatement@@Path hfilePath=HFileLink.getHFileFromBackReference(getConf(),filePath); @AT@ 2474 @LENGTH@ 74
        # INS ThrowStatement@@MethodInvocation:convertJedisAccessException(ex) @TO@ CatchClause@@Exception ex @AT@ 12194 @LENGTH@ 38
        # UPD MethodInvocation@@getVectorExpression(elseDesc,mode) @TO@ getVectorExpression(elseDesc,VectorExpressionDescriptor.Mode.PROJECTION) @AT@ 136925 @LENGTH@ 35
        # from common.preprocessing import preprocessingCodeElementsList

        # lines = re.sub('@AT@\s*[0-9]+\s*', ' ', lines)
        # lines = re.sub('@LENGTH@\s*[0-9]+\s*', ' ', lines)
        # lines = re.sub('@TO@', ' ', lines)
        # lines = re.sub('@@', ' ', lines)
        # lines = re.sub('INS|UPD|MOV|DEL', ' ', lines)
        # lines = re.sub('MethodInvocation:', ' ', lines)
        # lines = re.sub('Name:', ' ', lines)
        # lines = re.sub('|'.join(ast),' ',lines)
        from common.preprocessing import preprocessingForSimi
        m = re.search(searchPattern, line, re.DOTALL)
        if t is None:
            print()
        if m:
            for k in t:
                token = m.group(k)
                token = re.sub('MethodInvocation:|Name:|MethodName:|SimpleName:|InfixExpression:', ' ',
                               token)
                # token = re.sub(' Name:', ' ', token)
                # token = re.sub(' MethodName:', ' ', token)
                # token = re.sub(' SimpleName:', ' ', token)
                tokens.append(token)


        else:
            return None

    tokens = preprocessingForSimi(tokens)
    return tokens
    # for key in keys:


def simiCore(key):
    split = key.split('_')
    prefix = split[0]
    i = split[1]
    j = split[2]


    # inner = innerPool.getResource();

    # preCorpusBug = preprocessingCodeElementsList(lines)
    # return preCorpusBug

    tokensi = getTokens(prefix, i)
    tokensj = getTokens(prefix, j)

    tokensi
    import textdistance
    # simi = textdistance.jaccard(tokensi,tokensj)
    # simi2 = textdistance.sorensen_dice(' '.join(tokensi), ' '.join(tokensj))
    simi2 = textdistance.sorensen_dice(list(unique_everseen(tokensi)), list(unique_everseen(tokensj)))
    # simi
    #
    # from common.preprocessing import calculateTfIdfNLList
    #
    # if len(tokensj) == 0:
    #     print()
    # if tokensi[0] != [] or tokensj[0] != []:
    #     v = calculateTfIdfNLList([tokensi])
    #     sourceDTM = v.transform([tokensi])
    #     bugDTM = v.transform([tokensj])
    #     from sklearn.metrics.pairwise import cosine_similarity
    #
    #     res = cosine_similarity(bugDTM, sourceDTM)
    #     simiScore =res[0][0]
    if simi2 >= 0.8:
        print(key,simi2)

        redis_db2.set(key, simi2)
    redis_db.delete(key)