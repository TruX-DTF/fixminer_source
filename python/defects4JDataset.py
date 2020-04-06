from common.commons import *






def core():

    projects = [('Math',106,'commons-math'),('Lang',65,'commons-lang'),('Closure',133,'closure-compiler'),('Time',27,'joda-time'),('Mockito',38,'mockito'),('Chart',26,'jfreechart')]
    path = '/Users/anil.koyuncu/KUI/Defects4JData/'
    with open('mapping.csv','a') as f:
        for p in projects:
            pjName,iterSize,repoName = p

            for idx in range(iterSize):
                idx+=1

                # cmd = '~/bugStudy/defects4j/framework/bin/defects4j info -p ' + pjDict[project]
                repoPath = join(path,pjName+'_'+str(idx))
                cmd = 'git -C ' +repoPath+ ' checkout -- .'
                output, errors = shellGitCheckout(cmd, 'latin1')
                cmd = 'git -C '+repoPath+ ' log --pretty=format:%H -1'
                output, errors = shellGitCheckout(cmd, 'latin1')

                line = repoName + ' ' + output + ' ' + str(idx) + '\n'
                f.write(line)
                #git diff df055bf81b7aa846530eadb7904d911cf84372c9..df055bf81b7aa846530eadb7904d911cf84372c9 ^

                from dataset import prepareFilesDefects4J
                prepareFilesDefects4J(repoPath,repoName,output)

                # id = getIssueId('defects/'+pjName+'/'+str(idx)+'.log',pjName)
