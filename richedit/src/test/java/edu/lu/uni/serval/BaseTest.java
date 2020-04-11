package edu.lu.uni.serval;



import edu.lu.uni.serval.richedit.ediff.EDiffHunkParser;
import edu.lu.uni.serval.richedit.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.CallShell;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
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
//            String cmd = "cp /Users/anil.koyuncu/projects/test/richedit-data/patches/codeflaws/"+n.getMessage().split(root)[1] + " /Users/anil.koyuncu/projects/test/fixminerC/"+n.getMessage();
//            CallShell cs = new CallShell();
//            try {
//                cs.runShell(cmd);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return null;
        }

    }

    public List<HierarchicalActionSet> getHierarchicalActionSets4java(String s) throws IOException {
        Properties appProps = new Properties();


        Yaml yaml = new Yaml();
//        InputStream inputStream = this.getClass()
//                .getClassLoader()
//                .getResourceAsStream("customer.yaml");
        Map<String, Object> obj = yaml.load(new FileInputStream("src/main/resource/config.yml"));

//        appProps.load(new FileInputStream(appConfigPath));
        Map<String, Object> fixminer = (Map<String, Object>) obj.get("fixminer");
        Map<String, Object> dataset = (Map<String, Object>) obj.get("dataset");
        String srcMLPath = (String) fixminer.get("srcMLPath");
        String inputPath = (String) dataset.get("inputPath");
//        appProps.load(new FileInputStream("src/main/resource/app.properties"));
//        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");
//        String root = appProps.getProperty("inputPath");

        String root = "src/main/resource/testFiles";
        String project = s.split("_")[0];
        root = root + "/"+project+"/";
        String filename = s.replace(project+"_","");
        try{
            File revFile = new File(root + "revFiles/" + filename);
            File prevFile = new File(root + "prevFiles/prev_" + filename);

            EDiffHunkParser parser = new EDiffHunkParser();


            List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,true);
            return hierarchicalActionSets;
        }catch (NullPointerException n){

            String cmd1 = "mkdir -p " + System.getProperty("user.dir")+"/" + root + n.getMessage().split(root)[1].split("/")[0];
            String cmd = "cp " +inputPath+"/" +project+"/"+n.getMessage().split(root)[1] + " "+System.getProperty("user.dir")+"/"+n.getMessage();
            CallShell cs = new CallShell();
            try {
                cs.runShell(cmd1);
                cs.runShell(cmd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

    }
}
