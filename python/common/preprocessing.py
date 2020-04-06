from nltk.tokenize import RegexpTokenizer
# from stop_words import get_stop_words
from nltk.stem.porter import PorterStemmer
from string import punctuation
import re
from nltk.corpus import stopwords
en_stop = stopwords.words('english')
from nltk.corpus import wordnet
import html

from common.commons import *
CODE_PATH = os.environ["CODE_PATH"]

# import spacy
# nlp = spacy.load('en_core_web_lg', disable=['parser', 'tagger', 'ner'])
# nlp.max_length =100000000
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity

import sys


def getTokensForPatterns(res):
    if isinstance(res, list):
        merged = str()
        for r in res:
            if isinstance(r, list):
                merged = merged + ' ' + ' '.join(r)
            else:
                merged = merged +' ' + r
    else:
        merged=res

    res = html.unescape(merged)

    tokens = getTokens(res,False)

    stripped = []
    for t in tokens:
        splits = re.split('\.|\(|\)|:|>|<|:|=|/|\\\\|\'|-|,|\]|\[|}|{|;',t)
        for s in splits:
            stripped.append(s)
    non_empty = [i for i in stripped if i != '']
    return non_empty

def preprocessingCodeElementsList(res):
    printDetail = False
    if isinstance(res, list):
        merged = str()
        for r in res:
            if isinstance(r, list):
                merged = merged + ' ' + ' '.join(r)
            else:
                merged = merged +' ' + r
    else:
        merged=res

    res = html.unescape(merged)

    tokens = getTokens(res,printDetail)

    stripped = []
    for t in tokens:
        splits = re.split('\.|\(|\)|:|>|<|:|=|/|\\\\|\'|-|,|\]|\[',t)
        for s in splits:
            stripped.append(s)
    punc = removeEndingPunct(stripped,printDetail)

    non_empty = [i for i in punc if i != '']

    stripped = removeEndingPunct(non_empty,printDetail)

    camelCase = handleCamelCase(stripped,printDetail,True)

    underScore = handleUnderScore(camelCase,printDetail,True)

    lower = [i.lower() for i in underScore]

    # stopped_tokens = [i for i in lower if not i in en_stop]

    stem2 = stem(lower,printDetail)
    if printDetail:
        print('=====CLEANED=========')
        print(stem2)

    return stem2

def preprocessingForSimi(res):
    printDetail = False
    if isinstance(res, list):
        merged = str()
        for r in res:
            if isinstance(r, list):
                merged = merged + ' ' + ' '.join(r)
            else:
                merged = merged +' ' + r
    else:
        merged=res

    res = html.unescape(merged)

    tokens = getTokens(res,printDetail)

    stripped = []
    for t in tokens:
        splits = re.split('\.|\(|\)|:|>|<|:|=|/|\\\\|\'|-|,|\]|\[',t)
        for s in splits:
            stripped.append(s)
    punc = removeEndingPunct(stripped,printDetail)

    non_empty = [i for i in punc if i != '']

    stripped = removeEndingPunct(non_empty,printDetail)

    camelCase = handleCamelCase(stripped,printDetail,False)

    underScore = handleUnderScore(camelCase,printDetail,False)

    # lower = [i.lower() for i in underScore]

    # stopped_tokens = [i for i in lower if not i in en_stop]

    # stem2 = stem(lower,printDetail)
    # if printDetail:
    #     print('=====CLEANED=========')
    #     print(stem2)

    return underScore

def preprocessingNL(res):
    try:
        printDetail = False

        if isinstance(res, list):
            merged = str()
            for r in res:
                if isinstance(r, list):
                    merged = merged + ' ' + ' '.join(r)
                else:
                    merged = merged +' ' + r
        else:
            merged=res

        res = html.unescape(merged)
        html_decoded_string = res.replace("&amp;", "&").replace("&quot;", '"').replace("&apos;", "'").replace("&gt;",
                                                                                                               ">").replace(
            "&lt;", "<")
        html_decoded_string = re.sub(r'http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\),]|(?:%[0-9a-fA-F][0-9a-fA-F]))+', '',html_decoded_string)

        tokens = getTokens(html_decoded_string,printDetail)

        stripped = []
        for t in tokens:
            splits = re.split('\.|\(|\)|:|>|<|:|=|/|\\\\|\'|-|,|\]|\[',t)
            # splits = re.split('\.|\(|\)|:|>|<|:|=|/|\\\\|\'|-',t)
            for s in splits:
                stripped.append(s)
        punc = removeEndingPunct(stripped,printDetail)

        non_empty = [i for i in punc if i != '']

        stripped = removeEndingPunct(non_empty,printDetail)

        camelCase = handleCamelCase(stripped,printDetail,True)

        underScore = handleUnderScore(camelCase,printDetail,True)

        lower = [i.lower() for i in underScore]

        stopped_tokens = [i for i in lower if not i in en_stop]

        nonDigit = [i for i in stopped_tokens if (not i.isdigit())]

        # doc = nlp(' '.join(nonDigit))
        # newWord = []
        # for token in doc:
        #     if(token.text in nlp.vocab):
        #         newWord.append(token.text)

        stem2 = stem(nonDigit,printDetail)

        if printDetail:
            print('=====CLEANED=========')
            print(stem2)

        return stem2
    except Exception as e:
        logging.error(e)

def getTokens(re,printDetail=False):
    tokenizer = RegexpTokenizer(r'\S+')
    tokens = tokenizer.tokenize(re)
    if printDetail:
        print('=====TOKENS=========')
        print(tokens)

    return tokens

def charLength(x, l=3):
    if x.isalpha() and len(x) >= l:
        return True
    else:
        return False


def removeEndingPunct(re,printDetail):
    stripped = [i.strip(punctuation) for i in re]
    if printDetail:
        print('=====removeEndingPunct=========')
        print(stripped)
    return stripped

def handleCamelCase(re,printDetail=False,keepOriginal = False):
    camelCased = list()

    for i in re:
        listOfCC = camel_case_split(i)
        camelCased.extend(listOfCC)
        if i not in listOfCC and keepOriginal:
            camelCased.append(i)

    if printDetail:
        print('=====CAMEL CASE=========')
        print(camelCased)
    return camelCased

def handleUnderScore(re,printDetail=False,keepOriginal = False):
    underScored = list()
    for i in re:
        listOfCC = i.split('_')
        underScored.extend(listOfCC)
        if i not in listOfCC and keepOriginal:
            underScored.append(i)

    if printDetail:
        print('=====UNDER SCORE=========')
        print(underScored)

    return underScored

def camel_case_split(identifier):
    matches = re.finditer('.+?(?:(?<=[a-z])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])|$)', identifier)
    res = [m.group(0) for m in matches]

    return res

def stem(res,printDetail):
    p_stemmer = PorterStemmer()
    stemmed_tokens = [p_stemmer.stem(i.strip()) for i in res if i]
    if printDetail:
        print('=====STEMMED=========')
        print(stemmed_tokens)
    return stemmed_tokens

def isEnglish(word_to_test):
    if not wordnet.synsets(word_to_test):
        #Not an English Word
        #TODO
        word_to_test
        #print word_to_test
    else:
        return word_to_test


def dummy_fun(doc):
    return doc

def calculateTfIdfCodeElementsList(aCorpus):
    global progress
    progress = 0
    v = TfidfVectorizer(tokenizer=dummy_fun,stop_words=None,lowercase=False,sublinear_tf=True)#,max_df=0.7,min_df=3)
    m = v.fit(aCorpus)
    return v

def calculateTfIdfNLList(aCorpus):
    global progress
    progress = 0
    v = TfidfVectorizer(tokenizer=dummy_fun,stop_words=None,lowercase=False,sublinear_tf=True)#,max_df=0.7,min_df=3)
    m = v.fit(aCorpus)
    return v

def getDTMNL(x,v,corpus):
    ind =x.name
    v.tokenizer = dummy_fun
    return v.transform([corpus[ind]])
def getDTMCE(x,v,corpus):
    ind =x.name
    v.tokenizer = dummy_fun
    return v.transform([corpus[ind]])

def getBRDTM(x,v,corpus):
    ind =x.name
    v.tokenizer = dummy_fun
    return v.transform([corpus[ind]])


def getBRDTMCEs(x,v,corpus):
    ind =x.name
    v.tokenizer = dummy_fun
    return v.transform([corpus[ind]])
