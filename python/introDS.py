from common.commons import *
DATA_PATH = os.environ["DATA_PATH"]
COMMIT_DFS = os.environ["COMMIT_DFS"]
# DATASET_PATH = '/Users/anilkoyuncu/projects/datasets'
DATASET_PATH = os.environ["REPO_PATH"]
DATASET = os.environ["dataset"]
ROOT = os.environ["ROOT_DIR"]
PROJECT_LIST = os.environ["PROJECT_LIST"]

def core():
    intros = listdir(join(DATA_PATH,'introclass'))
    intros
    if not os.path.exists(join(DATA_PATH,'patches','introclass')):
        os.makedirs(join(DATA_PATH,'patches','introclass'))
    if not os.path.exists(join(DATA_PATH,'patches','introclass','prevFiles')):
        os.makedirs(join(DATA_PATH,'patches','introclass','prevFiles'))
    if not os.path.exists(join(DATA_PATH,'patches','introclass','revFiles')):
        os.makedirs(join(DATA_PATH,'patches','introclass','revFiles'))
    if not os.path.exists(join(DATA_PATH,'patches','introclass','DiffEntries')):
        os.makedirs(join(DATA_PATH,'patches','introclass','DiffEntries'))
    for i in intros:
        if i == '.DS_Store':
            continue
        _,bugName,bid,submission = i.split(':')

        shutil.copy2(join(DATA_PATH,'introclass',i,bugName+'.c'),join(DATA_PATH,'patches','introclass','prevFiles','prev_'+bid+'_'+bid+submission+'_'+bugName+'.c'))
        shutil.copy2(join(DATA_PATH,'introclass',i,'oracle.c'),join(DATA_PATH,'patches','introclass','revFiles',bid+'_'+bid+submission+'_'+bugName+'.c'))
        shutil.copy2(join(DATA_PATH,'introclass',i,'oracle.c.patch'),join(DATA_PATH,'patches','introclass','DiffEntries',bid+'_'+bid+submission+'_'+bugName+'.c.txt'))


