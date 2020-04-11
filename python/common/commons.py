
import logging
import sys
import gzip
import numpy as np
from tqdm import tqdm
import shutil
import itertools

# COMMIT_FOLDER = 'commits/'
from os import listdir
import re
import pandas as pd
from os.path import isfile, join, isdir
import pickle as p
from subprocess import Popen, PIPE
from subprocess import CalledProcessError
from subprocess import TimeoutExpired
import pickle as p
import os
import concurrent.futures

import time
import math
from collections import Counter

import datetime
import subprocess
from pathlib import Path



sourceCodeColumns = ['packageName', 'className', 'methodNames', 'formalParameter',
                       'methodInvocation', 'memberReference', 'documentation', 'literal', 'rawSource', 'hunks',
                       'commitLogs', 'classNameExt']


def nap():
    time.sleep(1)

def setLogg():
    # logging.basicConfig(filename='app.log', filemode='w',level=logging.DEBUG)
    root = logging.getLogger()
    root.setLevel(logging.DEBUG)

    ch = logging.StreamHandler(sys.stdout)
    ch.setLevel(logging.WARNING)
    formatter = logging.Formatter('%(asctime)s - %(process)d - %(levelname)s - %(filename)s:%(funcName)s - %(message)s')
    ch.setFormatter(formatter)
    # ch.addFilter(lambda record: record.levelno <= logging.)
    root.addHandler(ch)

    # # 2 handlers for the same logger:
    # h1 = logging.StreamHandler(sys.stdout)
    # h1.setLevel(logging.DEBUG)
    # # filter out everything that is above INFO level (WARN, ERROR, ...)
    # h1.addFilter(lambda record: record.levelno <= logging.INFO)
    # h1.setFormatter(formatter)
    # root.addHandler(h1)

    h2 = logging.FileHandler(filename='app.log', mode='w')
    # take only warnings and error logs
    h2.setLevel(logging.DEBUG)
    h2.setFormatter(formatter)
    root.addHandler(h2)

def setEnv(args):
    # env = args.env

    # logging.info('Environment: %s',env)

    os.environ["ROOT_DIR"] = args.root
    sys.path.append(args.root)


    import yaml
    # if os.uname().nodename != '':
    #     with open(join(os.environ["ROOT_DIR"], os.uname().nodename + ".config.yml"), 'r') as ymlfile:
    #         cfg = yaml.load(ymlfile)
    # else:
    #     with open(join(os.environ["ROOT_DIR"], "config.yml"), 'r') as ymlfile:
    #         cfg = yaml.load(ymlfile)

    with open(args.prop, 'r') as ymlfile:
        cfg = yaml.load(ymlfile)
    # for section in cfg:
    #     print(section)
    # print(cfg['mysql'])
    # print(cfg['other'])

    # os.environ["JDK7"] = cfg['java']['7home']
    os.environ["JDK8"] = cfg['java']['8home']
    os.environ["spinfer"] = cfg['spinfer']['home']
    os.environ["coccinelle"] = cfg['coccinelle']['home']
    os.environ["dataset"] = cfg['dataset']['inputPath']
    os.environ["REPO_PATH"] = cfg['dataset']['repo']
    os.environ["DATA_PATH"] = cfg['fixminer']['datapath']
    os.environ["PROJECT_TYPE"] = cfg['fixminer']['projectType']
    os.environ["PROJECT_LIST"] = cfg['fixminer']['projectList']
    os.environ["REDIS_PORT"] = str(cfg['fixminer']['portDumps'])

    # import yaml
    #
    # with open(join(os.environ["ROOT_DIR"],"config.yml"), 'r') as ymlfile:
    #     cfg = yaml.load(ymlfile)
    #
    # # for section in cfg:
    # #     print(section)
    # # print(cfg['mysql'])
    # # print(cfg['other'])
    #
    # os.environ["JDK7"] = cfg['java']['7home']
    # os.environ["JDK8"] = cfg['java']['8home']
    # os.environ["D4JHOME"] = cfg['defects4j']['home']


    os.environ["CODE_PATH"] = join(os.environ["ROOT_DIR"],'code/')
    # os.environ["DATA_PATH"] = join(os.environ["ROOT_DIR"],'data/')
    # os.environ["REPO_PATH"] = join(os.environ["DATA_PATH"], 'gitrepo/')
    os.environ["COMMIT_DFS"]= join(os.environ["DATA_PATH"],'commitsDF/')
    os.environ["SIMI_DIR"]= join(os.environ["DATA_PATH"],'simi/')
    os.environ["DTM_PATH"] = join(os.environ["DATA_PATH"], 'dtm/')
    os.environ["SIMI_SINGLE"] = join(os.environ["DATA_PATH"], 'simiSingle/')
    os.environ["FEATURE_DIR"] = join(os.environ["DATA_PATH"],'features/')
    
    os.environ["BUG_POINT"] = join(os.environ["DATA_PATH"], 'bugPoints/')
    os.environ["DEFECTS4J"] = join(os.environ["DATA_PATH"], 'defects4jdata/')

    os.environ["BUG_REPORT"] = join(os.environ["DATA_PATH"], 'bugReports/')
    os.environ["BUG_REPORT_FEATURES"] = join(os.environ["DATA_PATH"], 'bugReportFeatures/')
    # os.environ["PARSED_DIR"] = join(os.environ["CODE_PATH"], 'parsedFilesSingle/')
    # os.environ["PARSED_M_DIR"] = join(os.environ["CODE_PATH"], 'parsedFilesMulti/')

    os.environ["PARSED"] = join(os.environ["DATA_PATH"], 'parsedPj/')
    os.environ["PARSED_DIR"] = join(os.environ["DATA_PATH"], 'parsedFilesSingle/')
    os.environ["COMMIT_FOLDER"] = join(os.environ["DATA_PATH"], 'commits/')
    os.environ["CLASSIFIER_DIR"] = join(os.environ["DATA_PATH"], 'classifiers/')
    os.environ["PREDICTION_DIR"] = join(os.environ["DATA_PATH"], 'predictions/')
    os.environ["DATASET_DIR"] = join(os.environ["DATA_PATH"], 'datasets/')
    os.environ["REMOTE_PATH"] = '/Volumes/Samsung_T5/data'





    logging.info('ROOT_DIR : %s', os.environ["ROOT_DIR"])
    logging.info('REPO_PATH : %s', os.environ["REPO_PATH"])
    logging.info('CODE_PATH : %s', os.environ["CODE_PATH"])
    logging.info('COMMIT_DFS : %s', os.environ["COMMIT_DFS"])
    # logging.info('SIMI_DIR : %s', os.environ["SIMI_DIR"])
    logging.info('BUG_POINT : %s', os.environ["BUG_POINT"])
    # logging.info('PARSED_DIR : %s', os.environ["PARSED_DIR"])
    logging.info('COMMIT_FOLDER : %s', os.environ["COMMIT_FOLDER"])
    # logging.info('DTM_PATH : %s', os.environ["DTM_PATH"])
    # logging.info('SIMI_SINGLE : %s', os.environ["SIMI_SINGLE"])
    logging.info('FEATURE_DIR : %s', os.environ["FEATURE_DIR"])
    logging.info('CLASSIFIER_DIR : %s', os.environ["CLASSIFIER_DIR"])
    logging.info('PREDICTION_DIR : %s', os.environ["PREDICTION_DIR"])
    logging.info('DATASET_DIR : %s', os.environ["DATASET_DIR"])



def getRun():
    import argparse
    parser = argparse.ArgumentParser(description='')
    # parser.add_argument('-subject', dest='subject', help='Environment')
    parser.add_argument('-root', dest='root', help='root folder')
    parser.add_argument('-job',dest='job',help='job name')
    parser.add_argument('-prop',dest='prop',help='property file')


    args = parser.parse_args()

    if args.root is None or args.job is None or args.prop is None:
        parser.print_help()
        raise AttributeError
    return args



def shellCallTemplate4jar(cmd,enc='utf-8'):
    process = subprocess.Popen(cmd,
                               stdout=subprocess.PIPE,stderr=PIPE, shell=True,encoding=enc,
                               universal_newlines=True)

    while True:
        output = process.stdout.readline()
        print(output.strip())
        # Do something else
        return_code = process.poll()
        if return_code is not None:
            print('RETURN CODE', return_code)
            # Process has finished, read rest of the output
            for output in process.stdout.readlines():
                print(output.strip())
            break

def shellCallTemplate(cmd,enc='utf-8'):
    try:
        logging.info(cmd)
        with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True,encoding=enc) as p:
            output, errors = p.communicate()
            # print(output)
            if errors:
                m = re.search('unknown revision or path not in the working tree', errors)
                if not m:
                    raise CalledProcessError(errors, '-1')
            output
    except CalledProcessError as e:
        logging.error(errors)
    except Exception as e:
        logging.error(e)
    return output

def getChildMem(pid,children):

    out = subprocess.Popen(['pgrep', '-P', str(pid)],
                           stdout=subprocess.PIPE).communicate()[0].split(b'\n')
    child = out[0].decode()
    if child !='':
        children.append(child)
        getChildMem(child,children)
    else:
        return children

def getAllChildMe(pid):

    childrenProcess = []
    getChildMem(pid,childrenProcess)

    # if child == '':
    return sum(map(memory_usage_ps,childrenProcess)) + memory_usage_ps(pid)
    # else:
    #     return memory_usage_ps(child) + memory_usage_ps(pid)
def memory_usage_ps(pid):
    import subprocess
    out = subprocess.Popen(['ps', 'v', '-p', str(pid)],
    stdout=subprocess.PIPE).communicate()[0].split(b'\n')
    vsz_index = out[0].split().index(b'RSS')
    if out[1].decode() != '':
        mem = float(out[1].split()[vsz_index]) / 1024
    else:
        mem = float(0)
    return mem

def raiseTime(cmd,timeout,my_timer):
    my_timer.cancel()
    raise TimeoutExpired(cmd, timeout)

def killP(pid):
    out = subprocess.Popen(['kill', str(pid)],
    stdout=subprocess.PIPE).communicate()[0].split(b'\n')
    out


def shellGitCheckout(cmd,timeout =600,enc='utf-8'):
    output = ''
    errors = ''
    # logging.debug(cmd)

    with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True,encoding=enc) as p:
        try:
            output, errors = p.communicate(timeout=timeout)
            # print(output)
            logging.debug(cmd + '\t' +output)
            # logging.info(errors)
            if errors:
                raise CalledProcessError(errors, '-1')
            output
        except CalledProcessError as e:
            logging.debug(cmd +'\t'+ errors)
        except TimeoutExpired as t:
            p.terminate()
            p.communicate()
            # p.kill()
            logging.warning(cmd +'\t'+str(t))
    return output,errors

def callSpinfer(cmd,timeout =600,enc='utf-8'):
    output = ''
    errors = ''
    # logging.debug(cmd)
    my_timer = None
    with Popen(cmd, stdout=PIPE, stderr=PIPE, shell=True,encoding=enc) as p:
        try:
            start = datetime.datetime.now()
            memusage = getAllChildMe(p.pid)
            # isExit = False
            while(memusage != 0.0):
                end = datetime.datetime.now()
                elapsed = end - start
                if(elapsed.seconds > timeout):
                    raise TimeoutExpired(cmd,timeout)
                memusage = getAllChildMe(p.pid)
                # print(str(p.pid) + " ; " + str(memusage))
                if memusage > 2000:
                    # isExit = True
                    raise TimeoutExpired(cmd,timeout)

            output, errors = p.communicate(timeout=timeout)
            # print(output)
            logging.debug(cmd + '\t' +output)
            # logging.info(errors)
            if errors:
                raise CalledProcessError(errors, '-1')
            output
        except CalledProcessError as e:
            logging.debug(cmd +'\t'+ errors)
        except TimeoutExpired as t:
            # my_timer.cancel()


            childrenProcess = []
            getChildMem(p.pid, childrenProcess)
            [killP(i) for i in childrenProcess]

            p.terminate()
            p.communicate()
            # p.kill()
            logging.warning(cmd +'\t'+str(t))
    return output,errors

def save_zipped_pickle(obj, filename, protocol=-1):
    with gzip.open(filename, 'wb') as f:
        p.dump(obj, f, protocol)

def load_zipped_pickle(filename):
    with gzip.open(filename, 'rb') as f:
        loaded_object = p.load(f)
        return loaded_object

def file2path(file):
    count = file.count(".") - 1
    file = file.replace('.', '/', count)
    return file

def isFileInList(file,checkList):
    for f in checkList:
        if f in file:
            return True
    return False
    # [i for i in ansFiles if 'org/fusesource/esb/itests/basic/fabric/EsbFeatureTest.java' in i]

def get_venn_sections(sets):
    """
    Given a list of sets, return a new list of sets with all the possible
    mutually exclusive overlapping combinations of those sets.  Another way
    to think of this is the mutually exclusive sections of a venn diagram
    of the sets.  If the original list has N sets, the returned list will
    have (2**N)-1 sets.

    Parameters
    ----------
    sets : list of set

    Returns
    -------
    combinations : list of tuple
        tag : str
            Binary string representing which sets are included / excluded in
            the combination.
        set : set
            The set formed by the overlapping input sets.
    """
    num_combinations = 2 ** len(sets)
    bit_flags = [2 ** n for n in range(len(sets))]
    flags_zip_sets = [z for z in zip(bit_flags, sets)]

    #combo_sets = []
    combo_sets = dict()
    for bits in range(num_combinations - 1, 0, -1):
        include_sets = [s for flag, s in flags_zip_sets if bits & flag]
        exclude_sets = [s for flag, s in flags_zip_sets if not bits & flag]
        combo = set.intersection(*include_sets)
        combo = set.difference(combo, *exclude_sets)
        tag = ''.join([str(int((bits & flag) > 0)) for flag in bit_flags])
        #combo_sets.append((tag, combo))
        combo_sets[tag] = combo
    return combo_sets

def pairwise(iterable):
    "s -> (s0,s1), (s1,s2), (s2, s3), ..."
    a, b = itertools.tee(iterable)
    next(b, None)
    return zip(a, b)

def RR_XGB(x,ao,column):
    if x[ao] == 1:
        return (1.0 / (x[column]))
    elif pd.isnull(x[ao]):
        return None
    else:
        return 0

def parallelRunNo(coreFun,elements,*args):
    with concurrent.futures.ProcessPoolExecutor(max_workers=int(8)) as executor:
        try:
            futures = {executor.submit(coreFun, l,*args): l for l in elements}

            kwargs = {
                'total': len(futures),
                'unit': 'files',
                'unit_scale': True,
                'leave': False
            }

            for future in tqdm(concurrent.futures.as_completed(futures), **kwargs):
                url = futures[future]
                try:
                    data = future.result(timeout=None)


                except Exception as exc:
                    logging.error('%r generated an exception: %s' % (url, exc))
                    raise

        except Exception as e:
            logging.error(e)
            executor.shutdown()
            raise


def parallelRun(coreFun,elements,*args,max_workers=os.cpu_count()):
    with concurrent.futures.ProcessPoolExecutor(max_workers=max_workers) as executor:
        try:
            futures = {executor.submit(coreFun, l,*args): l for l in elements}

            kwargs = {
                'total': len(futures),
                'unit': 'files',
                'unit_scale': True,
                'leave': False
            }

            for future in tqdm(concurrent.futures.as_completed(futures), **kwargs):
                url = futures[future]
                try:
                    data = future.result()
                except Exception as exc:
                    logging.error('%r generated an exception: %s' % (url, exc))
                    raise
        except Exception as e:
            logging.error(e)
            executor.shutdown()
            raise


def parallelRunMerge(coreFun,elements,*args,max_workers=os.cpu_count()):
    dataL = []
    with concurrent.futures.ProcessPoolExecutor(max_workers=max_workers) as executor:
        try:
            futures = {executor.submit(coreFun, l,*args): l for l in elements}
            kwargs = {
                'total': len(futures),
                'unit': 'files',
                'unit_scale': True,
                'leave': False
            }
            for future in tqdm(concurrent.futures.as_completed(futures), **kwargs):
                url = futures[future]
                try:
                    data = future.result()
                    dataL.append(data)

                except Exception as exc:
                    logging.error('%r generated an exception: %s' % (url, exc))
                    raise

            return dataL
        except Exception as e:
            logging.error(e)
            executor.shutdown()
            raise


def parallelRunMergeNew(coreFun,elements,*args,max_workers=os.cpu_count()):

    res = []
    with concurrent.futures.ProcessPoolExecutor(max_workers=max_workers) as executor:
        try:
            futures = {executor.submit(coreFun, l,*args): l for l in elements}

            kwargs = {
                'total': len(futures),
                'unit': 'files',
                'unit_scale': True,
                'leave': False
            }

            for future in tqdm(concurrent.futures.as_completed(futures), **kwargs):
                url = futures[future]
                try:
                    data = future.result()
                    res.append(data)
                except Exception as exc:
                    logging.error('%r generated an exception: %s' % (url, exc))
                    raise

        except Exception as e:
            logging.error(e)
            executor.shutdown()
            raise
    aDF = pd.concat(res)
    return aDF

def get_filepaths(directory,extension):

    file_paths = []  # List which will store all of the full filepaths.\n,
    exclude = '.git'
    # Walk the tree.\n,
    for root, directories, files in os.walk(directory):
        directories[:] = [d for d in directories if d not in exclude]
        # java = [i for i in files if i.endswith(extension)]
        java = [i for i in files if bool(re.search(extension, i))]

        for filename in java:
            # Join the two strings in order to form the full filepath.\n,
            filepath = os.path.join(root, filename)
            file_paths.append(filepath)  # Add it to the list.\n,

    return file_paths  # Self-explanatory.\n,

def get_class_weights(y):
    counter = Counter(y)
    majority = max(counter.values())
    return  {cls: round(float(majority)/float(count), 2) for cls, count in counter.items()}


def stopDB(dbDir,portInner):
    # cmd = "bash " + dbDir + "/" + "stopServer.sh " + " " + portInner;
    cmd = "redis-cli -p " + portInner + " shutdown save"
    o, e = shellGitCheckout(cmd)
    logging.info(o)



def startDB(dbDir,portInner,projectType):
    dbName = "dumps-"+projectType+".rdb"
    # portInner = '6380'
    cmd = "bash " + dbDir + "/" + "startServer.sh " + dbDir + " "+dbName+ " " + portInner;

    o, e = shellGitCheckout(cmd)
    ping = "redis-cli -p "+portInner+" ping"
    o, e = shellGitCheckout(ping)
    m = re.search('PONG', o)

    while not m:
        time.sleep(10)
        logging.info('Waiting for checkout')
        o, e = shellGitCheckout(ping)
        m = re.search('PONG', o)
    print(o)


def unique_everseen(iterable, key=None):
    "List unique elements, preserving order. Remember all elements ever seen."
    # unique_everseen('AAAABBBCCDAABBB') --> A B C D
    # unique_everseen('ABBCcAD', str.lower) --> A B C D
    seen = set()
    seen_add = seen.add
    if key is None:
        for element in itertools.filterfalse(seen.__contains__, iterable):
            seen_add(element)
            yield element
    else:
        for element in iterable:
            k = key(element)
            if k not in seen:
                seen_add(k)
                yield element

def plotBox(yList,labels, fn, rotate=False,limit=True):
    import matplotlib
    matplotlib.use("TkAgg")
    import matplotlib.pyplot as plt



    fig = plt.figure()
    ax1 = fig.add_subplot(111)
    meanpointsprops = dict(markeredgecolor ='blue',markerfacecolor=
                           'blue')

    flierprops = dict(markeredgecolor ='black',markerfacecolor=
                           'black',marker='.',markersize=2)
    box = ax1.boxplot(yList, 0, flierprops=flierprops,widths=0.5, showmeans=False, vert=True,meanprops=meanpointsprops)
    for line in box['medians']:
        x,y = line.get_xydata()[1]
        line.set(linewidth=3)
        line.set_color('blue')
    # plt.scatter(labels, yList, color='r')
    # plt.plot(labels, yList, '-o')
    # ax1.boxplot(yList,notch=False, sym='', vert=True, whis=1.5,
    #     positions=None, widths=None, patch_artist=True,
    #     bootstrap=None, usermedians=None, conf_intervals=None)
    if rotate:
        ax1.set_xticklabels(labels, rotation=45, ha='right')
    else:
        # ax1.set_xticklabels(labels)
        # ax1.set_xticklabels(None)
        ax1.get_xaxis().set_ticklabels([])
    # sns.boxplot(yList, ax=ax1)
    if limit:
        ax1.set_ylim(top=1.1,bottom=0)
        ax1.yaxis.set_ticks([0.0,1.0])
    else:
        ax1.set_yscale('log')
        ax1.set_xlabel('Cluster Member Size')
        ax1.set_ylabel('Folds')
    plt.ion()

    plt.subplots_adjust(wspace=0, hspace=0)
    fig = plt.gcf()

    # fig.tight_layout()
    fig.set_size_inches(7, 1, forward=True)
    fig.savefig(fn, dpi=100, bbox_inches='tight')


    plt.show()

def plotBox2(ys,labels, fn,means, rotate=False,limit=True):


    import matplotlib
    matplotlib.use("TkAgg")
    import matplotlib.pyplot as plt


    fig,axes = plt.subplots(nrows=3,ncols=1)

    for ax1,yList,l,l2,mean in zip(axes.flat,ys,labels,['Shapes','Actions','Tokens'],means):
        # plt.setp(ax1.get_xticks(),visible=False)
        # ax1 = fig.add_subplot(111)
        meanpointsprops = dict(markeredgecolor ='blue',markerfacecolor=
                               'blue')

        flierprops = dict(markeredgecolor ='black',markerfacecolor=
                               'black',marker='.',markersize=2)
        box = ax1.boxplot(yList, 0, flierprops=flierprops,widths=0.5, showmeans=False, vert=True,meanprops=meanpointsprops)

        ax1.axhline(linewidth=2, color='r',y=mean)

        for line in box['medians']:
            x,y = line.get_xydata()[1]
            line.set(linewidth=3)
            line.set_color('blue')
        # plt.scatter(labels, yList, color='r')
        # plt.plot(labels, yList, '-o')
        # ax1.boxplot(yList,notch=False, sym='', vert=True, whis=1.5,
        #     positions=None, widths=None, patch_artist=True,
        #     bootstrap=None, usermedians=None, conf_intervals=None)
        if rotate:
            ax1.set_xticklabels(l, rotation=45, ha='right')
        else:
            # ax1.set_xticklabels(labels)
            # ax1.set_xticklabels(None)
            ax1.get_xaxis().set_ticklabels([])
            # ax1.get_xaxis().set_ticks([])
        # sns.boxplot(yList, ax=ax1)
        if limit:
            if l2 !='Tokens':
                ax1.set_ylim(top=1,bottom=0)
            else:
                ax1.set_ylim(top=1.1, bottom=0)
            ax1.yaxis.set_ticks([0.0,mean,0.5,1.0])
            ax1.yaxis.set_ticklabels([0,'',0.5,1])
            ax1.tick_params(direction='out', length=6, width=2, axis='y',
                         grid_color='r', grid_alpha=0.5)

        else:
            # ax1.set_yscale('log')
            ax1.set_xlabel('Cluster Member Size')
            ax1.set_ylabel('Folds')
        ax1.set_aspect('auto')

        ax1.set_ylabel(l2)
    labels = ['C-'+str(i+1) for i in labels[0]]
    ax1.set_xticklabels(labels)
    ax1.set_xticklabels(labels, rotation=45, ha='right')
    # plt.setp(ax1.get_xticks(), visible=True)

    ax1.set_xlabel('Clusters')
    # ax1.set_xlabel('Similarity')
    plt.ion()

    plt.subplots_adjust(wspace=0, hspace=0.05)
    fig = plt.gcf()


    # fig.tight_layout()
    fig.set_size_inches(7, 7, forward=True)
    fig.savefig(fn, dpi=100, bbox_inches='tight')


    plt.show()


def plotScatter(s1,s2,vs,label,limits,type):
    import matplotlib
    matplotlib.use("TkAgg")
    import matplotlib.pyplot as plt
    plt.figure(figsize=(2.5, 2.5))

    # s1 = (df1[whichColumn]).apply(lambda x: round(Decimal(x), 3))
    # s2 = (df2[whichColumn]).apply(lambda x: round(Decimal(x), 3))
    plt.scatter(s1, s2, color='r')
    # plt.text(0.5, 0.25, '\\textbf{%s wins}' %
    #          vs, ha='center', va='center', rotation=45)
    # plt.text(0.2, 0.5, '\\textbf{%s wins}' %
    #          label, ha='center', va='center', rotation=45)
    # plt.title('%s vs. %s ' % (label, vs))
    ax = plt.gca()
    ax.grid(False)
    # ax.patch.set_alpha(0)
    ax.set_xlim([0, limits])
    ax.set_ylim([0, limits])
    start, end = ax.get_xlim()
    stepsize = 1
    ax.xaxis.set_ticks(np.arange(0, end, stepsize))
    ax.yaxis.set_ticks(np.arange(0, end, stepsize))
    x = np.linspace(start, end, limits+1)
    y = np.linspace(start, end, limits+1)
    ax.fill_between(x, y, end, facecolor='b', alpha=0.3)
    # plt.plot(np.linspace(0, 1, 10), np.linspace(0, 1, 10), lw=1)
    ax.spines['top'].set_visible(True)
    ax.spines['right'].set_visible(True)
    ax.spines['bottom'].set_visible(True)
    ax.spines['left'].set_visible(True)

    ax.set_xlabel(vs)
    ax.set_ylabel(label)

    plt.subplots_adjust(wspace=0, hspace=0)
    # fig = plt.gcf()
    # fig.set_size_inches(4, 4, forward=True)

    plt.savefig(
        os.path.join(
            'scatter',
            '{}.pdf'.format(
                type).replace(' ', '-')
        ),
        tight_bbox=True
    )

import threading
class BackgroundTask(object):
    """ Threading example class
    The run() method will be started and it will run in the background
    until the application exits.
    """

    def __init__(self, model,PATH, interval=1):
        """ Constructor
        :type interval: int
        :param interval: Check interval, in seconds
        """
        self.interval = interval
        self.model = model
        self.path = PATH

        thread = threading.Thread(target=self.run, args=())
        thread.daemon = True                            # Daemonize thread
        thread.start()                                  # Start the execution

    def run(self):
        """ Method that runs forever """
        self.model.save_model(self.path,
                     num_iteration=self.model.best_iteration)