package edu.lu.uni.serval.fixminer.ediff;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TestInputCases {


    @Test
    public void test_189_B_17295034_17295064() throws IOException {
        //cpython_b3a601_63d5c1_Objects#unicodeobject.c
        //linux_659d8c_fd2a50a_tools#perf#builtin-kmem.c
        //openssl_6a14fe7_0ff368_crypto#LPdir_win.c
        //linux_d1dc69_cc2115c_sound#soc#codecs#wm8776.c
        //linux_ff244c6_a0917e0_drivers#net#tun.c .
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("linux_cf36a65_fa6ce9_drivers#video#console#vgacon.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"");

    }



    public List<HierarchicalActionSet> getHierarchicalActionSets(String s) throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");
        String root = appProps.getProperty("inputPath");
        String project = s.split("_")[0];
        root = root + "/"+project+"/";
        String filename = s.replace(project+"_","");

        File revFile = new File(root + "revFiles/" + filename);
        File prevFile = new File(root + "prevFiles/prev_" + filename);

        EDiffHunkParser parser = new EDiffHunkParser();


        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
        return hierarchicalActionSets;
    }


}
