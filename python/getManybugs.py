from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
bugList = ['manybugs:gmp:13420-13421','manybugs:gmp:14166-14167','manybugs:libtiff:2005-12-14-6746b87-0d3d51d','manybugs:libtiff:2005-12-21-3b848a7-3edb9cd','manybugs:libtiff:2005-12-27-6f76e76-5dac30f','manybugs:libtiff:2006-02-23-b2ce5d8-207c78a','manybugs:libtiff:2006-02-27-6074705-e6d0c32','manybugs:libtiff:2006-03-03-a72cf60-0a36d7f','manybugs:libtiff:2006-03-03-eec4c06-ee65c74','manybugs:libtiff:2007-07-13-09e8220-f2d989d','manybugs:libtiff:2007-07-19-ce4b7af-7d6e298','manybugs:libtiff:2007-08-24-827b6bc-22da1d6','manybugs:libtiff:2007-11-02-371336d-865f7b2','manybugs:libtiff:2007-11-23-82e378c-cf05a83','manybugs:libtiff:2008-04-15-2e8b2f1-0d27dc0','manybugs:libtiff:2008-09-05-d59e7df-5f42dba','manybugs:libtiff:2008-12-30-362dee5-565eaa2','manybugs:libtiff:2009-02-05-764dbba-2e42d63','manybugs:libtiff:2009-06-30-b44af47-e0b51f3','manybugs:libtiff:2009-08-28-e8a47d4-023b6df','manybugs:libtiff:2009-09-03-6406250-6b6496b','manybugs:libtiff:2010-06-30-1563270-1136bdf','manybugs:libtiff:2010-11-27-eb326f9-eec7ec0','manybugs:libtiff:2010-12-13-96a5fb4-bdba15c','manybugs:lighttpd:1794-1795','manybugs:lighttpd:1806-1807','manybugs:lighttpd:1913-1914','manybugs:lighttpd:1948-1949','manybugs:lighttpd:2225-2226','manybugs:lighttpd:2254-2259','manybugs:lighttpd:2330-2331','manybugs:lighttpd:2661-2662','manybugs:lighttpd:2785-2786','manybugs:php:2011-01-09-7c6310852e-478e5d1dd0','manybugs:php:2011-01-18-95388b7cda-b9b1fb1827','manybugs:php:2011-01-23-bc049ccb62-a6c0a4e474','manybugs:php:2011-01-24-a6c0a4e474-1a8b87d2c5','manybugs:php:2011-01-29-147382033a-5bb0a44e06','manybugs:php:2011-01-30-5bb0a44e06-1e91069eb4','manybugs:php:2011-02-01-01745fa657-1f49902999','manybugs:php:2011-02-01-1f49902999-f2329f1f4b','manybugs:php:2011-02-01-fefe9fc5c7-0927309852','manybugs:php:2011-02-04-793cfe1376-109b8e99e0','manybugs:php:2011-02-05-c50b3d7add-426f31e790','manybugs:php:2011-02-11-f912a2d087-b84967d3e2','manybugs:php:2011-02-14-86efc8e55e-d1d61ce612','manybugs:php:2011-02-16-eb0dd2b8ab-9bbc114b59','manybugs:php:2011-02-21-2a6968e43a-ecb9d8019c','manybugs:php:2011-02-21-b21f62eb2d-2a6968e43a','manybugs:php:2011-02-27-e65d361fde-1d984a7ffd','manybugs:php:2011-03-11-d890ece3fc-6e74d95f34','manybugs:php:2011-03-19-5d0c948296-8deb11c0c3','manybugs:php:2011-03-22-0de2e61cab-991ba13174','manybugs:php:2011-03-23-63673a533f-2adf58cfcf','manybugs:php:2011-03-27-11efb7295e-f7b7b6aa9e','manybugs:php:2011-03-27-2191af9546-b83e243c23','manybugs:php:2011-04-02-70075bc84c-5a8c917c37','manybugs:php:2011-04-06-187eb235fe-2e25ec9eb7','manybugs:php:2011-04-07-d3274b7f20-77ed819430','manybugs:php:2011-04-09-db01e840c2-09b990f499','manybugs:php:2011-04-12-465ffa7fa2-c4a8866abb','manybugs:php:2011-04-19-11941b3fd2-821d7169d9','manybugs:php:2011-04-27-53204a26d2-118695a4ea','manybugs:php:2011-10-15-0a1cc5f01c-05c5c8958e','manybugs:php:2011-10-16-1f78177e2b-d4ae4e79db','manybugs:php:2011-10-31-c4eb5f2387-2e5d5e5ac6','manybugs:php:2011-11-05-7888715e73-ff48763f4b','manybugs:php:2011-11-08-0ac9b9b0ae-cacf363957','manybugs:php:2011-11-08-6a42b41c3d-5063a77128','manybugs:php:2011-11-08-c3e56a152c-3598185a74','manybugs:php:2011-11-11-fcbfbea8d2-c1e510aea8','manybugs:php:2011-11-15-2568672691-13ba2da5f6','manybugs:php:2011-11-19-51a4ae6576-bc810a443d','manybugs:php:2011-11-19-eeba0b5681-d3b20b4058','manybugs:php:2011-11-19-eeba0b5681-f330c8ab4e','manybugs:php:2011-11-22-ecc6c335c5-b548293b99','manybugs:php:2011-11-25-3b1ce022f1-c2ede9a00a','manybugs:php:2011-11-26-7c2946f80e-dc6ecd21ee','manybugs:php:2011-12-04-1e6a82a1cf-dfa08dc325','manybugs:php:2011-12-06-5087b0ee42-ac631dd580','manybugs:php:2012-01-01-7c3177e5ab-e2a2ed348f','manybugs:php:2012-01-01-80dd931d40-7c3177e5ab','manybugs:php:2012-01-16-36df53421e-f32760bd40','manybugs:php:2012-01-17-032d140fd6-877f97cde1','manybugs:php:2012-01-17-e76c1cc03c-ebddf8a975','manybugs:php:2012-01-27-544e36dfff-acaf9c5227','manybugs:php:2012-01-29-d8b312845c-dc27324dd9','manybugs:php:2012-01-30-9de5b6dc7c-4dc8b1ad11','manybugs:php:2012-02-08-ff63c09e6f-6672171672','manybugs:php:2012-02-12-3d898cfa3f-af92365239','manybugs:php:2012-02-25-c1322d2505-cfa9c90b20','manybugs:php:2012-03-06-6237456cae-5e80c05deb','manybugs:php:2012-03-08-0169020e49-cdc512afb3','manybugs:php:2012-03-08-d54e6ce832-23e65a9dcc','manybugs:php:2012-03-10-23e65a9dcc-e6ec1fb166','manybugs:php:2012-03-12-438a30f1e7-7337a901b7','manybugs:php:2012-03-12-7aefbf70a8-efc94f3115','manybugs:php:2012-03-22-3efc9f2f78-2e19cccad7','manybugs:python:69223-69224','manybugs:python:69368-69372','manybugs:python:69470-69474','manybugs:python:69609-69616','manybugs:python:69709-69710','manybugs:python:69740-69743','manybugs:python:69783-69784','manybugs:python:69785-69789','manybugs:python:69831-69833','manybugs:python:69934-69935','manybugs:python:69945-69946','manybugs:python:70019-70023','manybugs:python:70056-70059','manybugs:python:70098-70101','manybugs:python:70120-70124','manybugs:wireshark:35419-35414','manybugs:wireshark:37112-37111','manbugs:wireshark:37122-37123']
# bugList = ['manybugs:php:2011-01-23-bc049ccb62-a6c0a4e474']
BUGDIR = join(DATA_PATH,'manybugs')

sosbugs = ['manybugs:gmp:14166-14167', 'manybugs:gzip:2009-08-16-3fe0caeada-39a362ae9d',
           'manybugs:gzip:2009-10-09-1a085b1446-118a107f2d', 'manybugs:gzip:2010-02-19-3eb6091d69-884ef6d16c',
           'manybugs:libtiff:2005-12-21-3b848a7-3edb9cd', 'manybugs:libtiff:2006-03-03-a72cf60-0a36d7f',
           'manybugs:libtiff:2006-03-03-eec4c06-ee65c74', 'manybugs:libtiff:2007-07-13-09e8220-f2d989d',
           'manybugs:libtiff:2007-11-02-371336d-865f7b2', 'manybugs:libtiff:2009-02-05-764dbba-2e42d63',
           'manybugs:libtiff:2009-08-28-e8a47d4-023b6df', 'manybugs:libtiff:2010-11-27-eb326f9-eec7ec0',
           'manybugs:libtiff:2006-02-23-b2ce5d8-207c78a', 'manybugs:lighttpd:2661-2662', 'manybugs:lighttpd:2254-2259',
           'manybugs:lighttpd:2785-2786', 'manybugs:lighttpd:1948-1949',
           'manybugs:php:2011-12-10-74343ca506-52c36e60c4', 'manybugs:php:2011-04-02-70075bc84c-5a8c917c37',
           'manybugs:php:2011-03-25-8138f7de40-3acdca4703', 'manybugs:php:2011-12-04-1e6a82a1cf-dfa08dc325',
           'manybugs:php:2012-02-08-ff63c09e6f-6672171672', 'manybugs:php:2011-11-19-eeba0b5681-d3b20b4058',
           'manybugs:php:2011-04-07-77ed819430-efcb9a71cd', 'manybugs:php:2011-02-01-01745fa657-1f49902999',
           'manybugs:php:2012-03-12-7aefbf70a8-efc94f3115', 'manybugs:php:2011-10-15-0a1cc5f01c-05c5c8958e',
           'manybugs:php:2011-01-30-5bb0a44e06-1e91069eb4', 'manybugs:php:2011-02-01-fefe9fc5c7-0927309852',
           'manybugs:php:2011-02-27-e65d361fde-1d984a7ffd', 'manybugs:php:2011-03-19-5d0c948296-8deb11c0c3',
           'manybugs:php:2011-03-23-63673a533f-2adf58cfcf', 'manybugs:php:2011-04-06-187eb235fe-2e25ec9eb7',
           'manybugs:php:2011-04-09-db01e840c2-09b990f499', 'manybugs:php:2011-05-17-453c954f8a-daecb2c0f4',
           'manybugs:php:2011-05-24-b60f6774dc-1056c57fa9', 'manybugs:php:2011-10-16-1f78177e2b-d4ae4e79db',
           'manybugs:php:2011-10-31-2e5d5e5ac6-b5f15ef561', 'manybugs:php:2011-10-31-c4eb5f2387-2e5d5e5ac6',
           'manybugs:php:2011-11-01-ceac9dc490-9b0d73af1d', 'manybugs:php:2011-11-11-fcbfbea8d2-c1e510aea8',
           'manybugs:php:2011-11-15-236120d80e-fb37f3b20d', 'manybugs:php:2011-11-16-55acfdf7bd-3c7a573a2c',
           'manybugs:php:2011-11-22-ecc6c335c5-b548293b99', 'manybugs:php:2011-11-23-eca88d3064-db0888dfc1',
           'manybugs:php:2012-01-27-544e36dfff-acaf9c5227', 'manybugs:php:2012-01-30-9de5b6dc7c-4dc8b1ad11',
           'manybugs:php:2012-02-25-c1322d2505-cfa9c90b20', 'manybugs:php:2012-03-04-60dfd64bf2-34fe62619d',
           'manybugs:php:2012-03-08-0169020e49-cdc512afb3', 'manybugs:php:2012-03-11-3954743813-d4f05fbffc',
           'manybugs:php:2012-03-12-438a30f1e7-7337a901b7', 'manybugs:python:69223-69224',
           'manybugs:python:69368-69372', 'manybugs:python:70098-70101', 'manybugs:python:70056-70059',
           'manybugs:wireshark:37112-37111', 'manybugs:wireshark:37122-37123', 'manybugs:gmp:13420-13421',
           'manybugs:gzip:2009-09-26-a1d3d4019d-f17cbd13a1', 'manybugs:lighttpd:1913-1914',
           'manybugs:php:2011-11-19-51a4ae6576-bc810a443d', 'manybugs:php:2011-03-11-d890ece3fc-6e74d95f34',
           'manybugs:php:2011-11-19-eeba0b5681-f330c8ab4e', 'manybugs:php:2012-01-01-80dd931d40-7c3177e5ab']

def get_filepaths(directory):

    file_paths = []  # List which will store all of the full filepaths.\n,
    exclude = '.git'
    # Walk the tree.\n,
    for root, directories, files in os.walk(directory):
        directories[:] = [d for d in directories if d not in exclude]
        java = [i for i in files if not (i.endswith('-diff') or i.endswith('.patch'))]

        for filename in java:
            # Join the two strings in order to form the full filepath.\n,
            filepath = os.path.join(root, filename)
            file_paths.append(filepath)  # Add it to the list.\n,

    return file_paths  # Self-explanatory.\n,

def export():
    if not os.path.exists(join(BUGDIR)):
        os.mkdir(join(BUGDIR))

    cmd = 'bugzoo bug list | grep Yes'
    o,e = shellGitCheckout(cmd)

    if o =='':
        with open(join(DATA_PATH,'bugzooList'),mode='r') as b:
            o = b.read()
    bugList = []
    for line in o.split('\n'):
        if(line.strip() == ''):
            continue
        bugList.append(line.split('|')[1].strip())
    bugList
    # bugList = [i.replace(':', '-').replace('manybugs-', 'squareslab/manybugs:') for i in bugList]
    # exportCore(bugList[0])
    # print("bugList length: {}".format(len(bugList)))



    for b in [b for b in bugList if b in sosbugs]:
        # cmd = 'bugzoo bug build ' + b
        # o,e = shellGitCheckout(cmd)
        # print(o)
        # print(e)
        exportCore(b)
    # exportCore('manybugs:php:2011-03-27-2191af9546-b83e243c23')
    # parallelRun(exportCore,bugList)
def exportCore(bugName):

    try:


        bugNameForDocker = bugName.replace(':', '-').replace('manybugs-', 'squareslab/manybugs:')
        cmd = 'docker images '+bugNameForDocker+' --format "{{.ID}}"'
        logging.info(cmd)
        output, e = shellGitCheckout(cmd)
        logging.info(output)
        # bugName = bugName.replace('manybugs:','')
        if not os.path.exists(join(BUGDIR,bugName)):
            os.makedirs(join(BUGDIR,bugName))

        if os.path.exists(join(BUGDIR, bugName,'diffs')):
            shutil.rmtree(join(BUGDIR, bugName,'diffs'))
            # os.makedirs(join(BUGDIR, bugName,'diffs'))
        if os.path.exists(join(BUGDIR, bugName, 'src')):
            shutil.rmtree(join(BUGDIR, bugName,'src'))
            # os.makedirs(join(BUGDIR, bugName, 'src'))
    #
        cmd = 'docker create -ti --name dummy '+output.strip()+' bash'
        logging.info(cmd)
        output, e = shellGitCheckout(cmd)
        logging.info(output)
        cmd = 'docker cp dummy:/experiment/test.sh  '+join(BUGDIR,bugName)
        logging.info(cmd)
        output, e = shellGitCheckout(cmd)
        logging.info(output)



        cmd = 'docker cp dummy:/experiment/src '+join(BUGDIR,bugName)
        logging.info(cmd)
        output, e = shellGitCheckout(cmd)
        logging.info(output)

        cmd = 'docker cp dummy:/experiment/diffs '+join(BUGDIR,bugName)
        logging.info(cmd)
        output, e = shellGitCheckout(cmd)
        logging.info(output)

        cmd = 'docker rm -fv dummy'
        logging.info(cmd)
        output, e = shellGitCheckout(cmd)
        logging.info(output)
        name = bugName.split(':')[1]
        # if name == 'libtiff' or name == 'php':
        #     preId = bugName.split('-')[3]
        #     postId = bugName.split('-')[4]
        # else:
        #     preId = bugName.split(':')[2].split('-')[0]
        #     postId = bugName.split(':')[2].split('-')[1]
        preId,postId = bugName.split(':')[-1].split('-')[-2:]

        res = get_filepaths(join(BUGDIR, bugName,'diffs'))
        res = [i for i in res if not i.endswith('.DS_Store')]
        fullName0 = res[0].split('-')[-1]
        fullName1 = res[1].split('-')[-1]
        if preId != fullName0 and preId != fullName1:
            if fullName0.startswith(preId):
                preId = fullName0
                postId =fullName1
            else:
                preId = fullName1
                postId = fullName0
        if len(res)!=2:
            patchNames = list(set([re.sub('c-[a-z0-9]+', 'c', i.split(join(BUGDIR, bugName, 'diffs'))[-1]) for i in res]))
            for pn in patchNames:
                pn = join(BUGDIR, bugName, 'diffs') + pn
                results = [i for i in res if i.startswith(pn)]
                cmd = 'diff -u ' + pn + '-' + preId + ' ' + pn + '-' + postId + '  > ' + pn + '.patch'
                logging.info(cmd)
                output, e = shellGitCheckout(cmd)
                logging.info(output)
        else:
            patchName = list(set([re.sub('c-[a-z0-9]+', 'c', i.split(join(BUGDIR, bugName, 'diffs'))[-1]) for i in res]))[0]
            patchName = join(BUGDIR, bugName, 'diffs') + patchName
            cmd = 'diff -u ' + patchName + '-' +preId + ' ' + ' ' + patchName + '-' + postId + '  > ' +patchName +'.patch'
            logging.info(cmd)
            output, e = shellGitCheckout(cmd)
            logging.info(output)
    except Exception as e:
        logging.error(bugName)
        logging.error(e)

