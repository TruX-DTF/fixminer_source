package edu.lu.uni.serval.fixminer.akka.ediff;

import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.fixminer.akka.compare.AkkaTreeParser;
import edu.lu.uni.serval.utils.ClusterToPattern;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class TestPredefinedCases {




    @Test
    public void testIFCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_if_example_1.c");
        File revFile = new File("src/main/resource/testFiles/if_example_1.c");


        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if x >= 5 y += 4 @TO@ if x > 5 y += 4 @AT@ 2 @LENGTH@ 16\n" +
                "---UPD condition@@x >= 5 @TO@ x > 5 @AT@ 2 @LENGTH@ 9\n" +
                "------UPD expr@@x >= 5 @TO@ x > 5 @AT@ 3 @LENGTH@ 6\n" +
                "---------UPD operator@@>= @TO@ > @AT@ 5 @LENGTH@ 2\n");

    }
    @Test
    public void testIFCase2() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_if_example_2.c");
        File revFile = new File("src/main/resource/testFiles/if_example_2.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@i > 0 j > i x = j x = i @TO@ i > 0 j >= i x = j x != i @AT@ 3 @LENGTH@ 23\n" +
                "---UPD then@@j > i x = j x = i @TO@ j >= i x = j x != i @AT@ 19 @LENGTH@ 17\n" +
                "------UPD block@@j > i x = j x = i @TO@ j >= i x = j x != i @AT@ 19 @LENGTH@ 17\n" +
                "---------UPD if@@j > i x = j x = i @TO@ j >= i x = j x != i @AT@ 19 @LENGTH@ 17\n" +
                "------------UPD condition@@j > i @TO@ j >= i @AT@ 19 @LENGTH@ 10\n" +
                "---------------UPD expr@@j > i @TO@ j >= i @AT@ 21 @LENGTH@ 5\n" +
                "------------------UPD operator@@> @TO@ >= @AT@ 23 @LENGTH@ 1\n" +
                "------------UPD else@@x = i @TO@ x != i @AT@ 61 @LENGTH@ 5\n" +
                "---------------UPD block@@x = i @TO@ x != i @AT@ 61 @LENGTH@ 5\n" +
                "------------------UPD expr_stmt@@x = i @TO@ x != i @AT@ 61 @LENGTH@ 5\n" +
                "---------------------UPD expr@@x = i @TO@ x != i @AT@ 61 @LENGTH@ 5\n" +
                "------------------------UPD operator@@= @TO@ != @AT@ 63 @LENGTH@ 1\n");

    }



    @Test
    public void testForCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_for_example_1.c");
        File revFile = new File("src/main/resource/testFiles/for_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 0 i < max i ++ if line ] == ' ' if line ] == '\\t' tab ++ @TO@ i = 0 i < max i ++ if line ] == ' ' space ++ if line ] == '\\t' tab ++ @AT@ 4 @LENGTH@ 60\n" +
                "---UPD block@@if line ] == ' ' if line ] == '\\t' tab ++ @TO@ if line ] == ' ' space ++ if line ] == '\\t' tab ++ @AT@ 26 @LENGTH@ 98\n" +
                "------UPD if@@if line ] == ' ' @TO@ if line ] == ' ' space ++ @AT@ 33 @LENGTH@ 16\n" +
                "---------UPD then@@ @TO@ space ++ @AT@ 54 @LENGTH@ 0\n" +
                "------------UPD block@@ @TO@ space ++ @AT@ 54 @LENGTH@ 22\n" +
                "---------------INS expr_stmt@@space ++ @TO@ block@@ @AT@ 62 @LENGTH@ 8\n" +
                "------------------INS expr@@space ++ @TO@ expr_stmt@@space ++ @AT@ 62 @LENGTH@ 8\n" +
                "---------------------INS name@@space @TO@ expr@@space ++ @AT@ 62 @LENGTH@ 5\n" +
                "---------------------INS operator@@++ @TO@ expr@@space ++ @AT@ 67 @LENGTH@ 2\n" +
                "---------------DEL continue@@ @AT@ 62 @LENGTH@ 0\n");

    }
    @Test
    public void testWhileCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");


        File revFile = new File("src/main/resource/testFiles/while_example_1.c");
        File prevFile = new File("src/main/resource/testFiles/prev_while_example_1.c");



        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(), 1);


        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "UPD do@@y = f x x -- print x x > 0 @TO@ y = f x x -- x > 0 @AT@ 0 @LENGTH@ 62\n" +
                "---UPD block@@y = f x x -- print x @TO@ y = f x x -- @AT@ 3 @LENGTH@ 42\n" +
                "------DEL expr_stmt@@print x @AT@ 33 @LENGTH@ 7\n" +
                "---------DEL expr@@print x @AT@ 33 @LENGTH@ 7\n" +
                "------------DEL call@@print x @AT@ 33 @LENGTH@ 7\n" +
                "---------------DEL name@@print @AT@ 33 @LENGTH@ 5\n" +
                "---------------DEL argument_list@@x @AT@ 38 @LENGTH@ 4\n" +
                "------------------DEL argument@@x @AT@ 39 @LENGTH@ 1\n" +
                "---------------------DEL expr@@x @AT@ 39 @LENGTH@ 1\n" +
                "------------------------DEL name@@x @AT@ 39 @LENGTH@ 1\n");
    }


        @Test
        public void testIFRetrun() throws IOException {
            Properties appProps = new Properties();
            appProps.load(new FileInputStream("src/main/resource/app.properties"));
            String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

            File revFile = new File("src/main/resource/testFiles/if_return.c");
            File prevFile =new File("src/main/resource/testFiles/prev_if_return.c");



            EDiffHunkParser parser = new EDiffHunkParser();

            List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
            hierarchicalActionSets.size();
//            Assert.assertEquals(hierarchicalActionSets.size(),1);
        }
        @Test
        public void testIfElse() throws IOException {
            Properties appProps = new Properties();
            appProps.load(new FileInputStream("src/main/resource/app.properties"));
            String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

            File revFile = new File("src/main/resource/testFiles/if_else.c");
            File prevFile =new File("src/main/resource/testFiles/prev_if_else.c");



            EDiffHunkParser parser = new EDiffHunkParser();


            List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
            hierarchicalActionSets.size();
    //            Assert.assertEquals(hierarchicalActionSets.size(),1);
        }

    @Test
    public void testStruct() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File revFile = new File("src/main/resource/testFiles/struct.c");
        File prevFile =new File("src/main/resource/testFiles/prev_struct.c");



        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        //            Assert.assertEquals(hierarchicalActionSets.size(),1);
    }

    @Test
    public void testUnionCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_union_example_1.c");
        File revFile = new File("src/main/resource/testFiles/union_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD union@@sign int svar unsigned uvar unsigned virus number @TO@ sign int svar unsigned uvar number @AT@ 0 @LENGTH@ 75\n" +
                "---UPD block@@int svar unsigned uvar unsigned virus @TO@ int svar unsigned uvar @AT@ 10 @LENGTH@ 57\n" +
                "------DEL decl_stmt@@unsigned virus @AT@ 49 @LENGTH@ 14\n" +
                "---------DEL decl@@unsigned virus @AT@ 49 @LENGTH@ 14\n" +
                "------------DEL type@@unsigned @AT@ 49 @LENGTH@ 8\n" +
                "---------------DEL name@@unsigned @AT@ 49 @LENGTH@ 8\n" +
                "------------DEL name@@virus @AT@ 58 @LENGTH@ 5\n");

    }

    @Test
    public void testEnumCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");

        File prevFile = new File("src/main/resource/testFiles/prev_enum_example_1.c");
        File revFile = new File("src/main/resource/testFiles/enum_example_1.c");


    }
}
