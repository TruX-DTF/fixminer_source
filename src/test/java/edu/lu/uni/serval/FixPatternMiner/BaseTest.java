package edu.lu.uni.serval.FixPatternMiner;

import edu.lu.uni.serval.fixminer.ediff.EDiffHunkParser;
import edu.lu.uni.serval.fixminer.ediff.HierarchicalActionSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class BaseTest {

    public List<HierarchicalActionSet> getHierarchicalActionSets(String s) throws IOException {
        Properties appProps = new Properties();




        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");
//        String root = appProps.getProperty("inputPath");
        String root = "src/main/resource/testFiles";
        root = root + "/codeflaws/";
        String filename = s;
        try{
            File revFile = new File(root + "revFiles/" + filename);
            File prevFile = new File(root + "prevFiles/prev_" + filename);

            EDiffHunkParser parser = new EDiffHunkParser();


            List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
            return hierarchicalActionSets;
        }catch (NullPointerException n){
//            String cmd = "cp /Users/anil.koyuncu/projects/test/fixminer-data/patches/codeflaws/"+n.getMessage().split(root)[1] + " /Users/anil.koyuncu/projects/test/fixminerC/"+n.getMessage();
//            CallShell cs = new CallShell();
//            try {
//                cs.runShell(cmd);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return null;
        }

    }
}
