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





    @Test
    public void testContinueCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_continue_example_1.c");
        File revFile = new File("src/main/resource/testFiles/continue_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD while@@i -- > 0 x = f i x == 1 y += x * x @TO@ i -- > 1 x = f i x == 0 y += x * x @AT@ 6 @LENGTH@ 34\n" +
                "---UPD condition@@i -- > 0 @TO@ i -- > 1 @AT@ 6 @LENGTH@ 12\n" +
                "------UPD expr@@i -- > 0 @TO@ i -- > 1 @AT@ 8 @LENGTH@ 8\n" +
                "---------UPD literal@@0 @TO@ 1 @AT@ 14 @LENGTH@ 1\n" +
                "---UPD block@@x = f i x == 1 y += x * x @TO@ x = f i x == 0 y += x * x @AT@ 17 @LENGTH@ 80\n" +
                "------UPD if@@x == 1 @TO@ x == 0 @AT@ 42 @LENGTH@ 6\n" +
                "---------UPD condition@@x == 1 @TO@ x == 0 @AT@ 42 @LENGTH@ 11\n" +
                "------------UPD expr@@x == 1 @TO@ x == 0 @AT@ 44 @LENGTH@ 6\n" +
                "---------------UPD literal@@1 @TO@ 0 @AT@ 49 @LENGTH@ 1\n");

    }

    @Test
    public void testReturnCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_return_example_1.c");
        File revFile = new File("src/main/resource/testFiles/return_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@( s * ( long long ) s ) @TO@ ( s * ( long int ) s ) @AT@ 0 @LENGTH@ 28\n" +
                "---UPD expr@@( s * ( long long ) s ) @TO@ ( s * ( long int ) s ) @AT@ 6 @LENGTH@ 23\n" +
                "------UPD name@@long @TO@ int @AT@ 18 @LENGTH@ 4\n");

    }

    @Test
    public void testSwitchCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_switch_example_1.c");
        File revFile = new File("src/main/resource/testFiles/switch_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@i > 0 i - 1 n ++ 0 z ++ 2 p -- 0 @TO@ i > 0 i - 1 n ++ 0 z ++ 1 p ++ 0 @AT@ 3 @LENGTH@ 32\n" +
                "---UPD then@@i - 1 n ++ 0 z ++ 2 p -- @TO@ i - 1 n ++ 0 z ++ 1 p ++ @AT@ 22 @LENGTH@ 24\n" +
                "------UPD block@@i - 1 n ++ 0 z ++ 2 p -- @TO@ i - 1 n ++ 0 z ++ 1 p ++ @AT@ 22 @LENGTH@ 24\n" +
                "---------UPD switch@@i - 1 n ++ 0 z ++ 2 p -- @TO@ i - 1 n ++ 0 z ++ 1 p ++ @AT@ 22 @LENGTH@ 24\n" +
                "------------UPD block@@- 1 n ++ 0 z ++ 2 p -- @TO@ - 1 n ++ 0 z ++ 1 p ++ @AT@ 32 @LENGTH@ 148\n" +
                "---------------UPD case@@2 @TO@ 1 @AT@ 148 @LENGTH@ 9\n" +
                "------------------UPD expr@@2 @TO@ 1 @AT@ 153 @LENGTH@ 1\n" +
                "---------------------UPD literal@@2 @TO@ 1 @AT@ 153 @LENGTH@ 1\n" +
                "---------------UPD expr_stmt@@p -- @TO@ p ++ @AT@ 169 @LENGTH@ 4\n" +
                "------------------UPD expr@@p -- @TO@ p ++ @AT@ 169 @LENGTH@ 4\n" +
                "---------------------UPD operator@@-- @TO@ ++ @AT@ 170 @LENGTH@ 2\n" +
                "---------------INS break@@ @TO@ block@@- 1 n ++ 0 z ++ 2 p -- @AT@ 186 @LENGTH@ 0\n");

    }

//    @Test
//    public void testExternCase1() throws IOException {
//        Properties appProps = new Properties();
//        appProps.load(new FileInputStream("src/main/resource/app.properties"));
//        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
//
//        File prevFile =new File("src/main/resource/testFiles/prev_extern_example_1.c");
//        File revFile = new File("src/main/resource/testFiles/extern_example_1.c");
//
//
//
//
//        EDiffHunkParser parser = new EDiffHunkParser();
//
//        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
//        hierarchicalActionSets.size();
//        Assert.assertEquals(hierarchicalActionSets.size(),1);
//
//        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD switch@@i - 1 n ++ 0 z ++ 2 p -- @TO@ i - 1 n ++ 0 z ++ 1 p ++ @AT@ 6 @LENGTH@ 24\n" +
//                "---UPD block@@- 1 n ++ 0 z ++ 2 p -- @TO@ - 1 n ++ 0 z ++ 1 p ++ @AT@ 11 @LENGTH@ 127\n" +
//                "------UPD case@@2 @TO@ 1 @AT@ 99 @LENGTH@ 9\n" +
//                "---------UPD expr@@2 @TO@ 1 @AT@ 104 @LENGTH@ 1\n" +
//                "------------UPD literal@@2 @TO@ 1 @AT@ 104 @LENGTH@ 1\n" +
//                "------UPD expr_stmt@@p -- @TO@ p ++ @AT@ 116 @LENGTH@ 4\n" +
//                "---------UPD expr@@p -- @TO@ p ++ @AT@ 116 @LENGTH@ 4\n" +
//                "------------UPD operator@@-- @TO@ ++ @AT@ 117 @LENGTH@ 2\n");
//
//    }

    @Test
    public void testFunctionCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_function_example_1.c");
        File revFile = new File("src/main/resource/testFiles/function_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@int func a b c int a int b int c @TO@ int func a b c int a int c int b @AT@ 122 @LENGTH@ 32\n" +
                "---MOV decl_stmt@@int b @TO@ function@@int func a b c int a int b int c @AT@ 153 @LENGTH@ 5\n");

    }

    @Test
    public void testStaticCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_static_example_1.c");
        File revFile = new File("src/main/resource/testFiles/static_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@static const int rpm_ranges [] 2000 4000 8000 16000 @TO@ static const string rpm_ranges [] 1000 4000 8000 16000 @AT@ 95 @LENGTH@ 51\n" +
                "---UPD decl@@static const int rpm_ranges [] 2000 4000 8000 16000 @TO@ static const string rpm_ranges [] 1000 4000 8000 16000 @AT@ 95 @LENGTH@ 51\n" +
                "------UPD type@@const int @TO@ const string @AT@ 102 @LENGTH@ 9\n" +
                "---------UPD name@@int @TO@ string @AT@ 108 @LENGTH@ 3\n" +
                "------UPD init@@2000 4000 8000 16000 @TO@ 1000 4000 8000 16000 @AT@ 127 @LENGTH@ 20\n" +
                "---------UPD expr@@2000 4000 8000 16000 @TO@ 1000 4000 8000 16000 @AT@ 127 @LENGTH@ 20\n" +
                "------------UPD block@@2000 4000 8000 16000 @TO@ 1000 4000 8000 16000 @AT@ 127 @LENGTH@ 28\n" +
                "---------------UPD expr@@2000 @TO@ 1000 @AT@ 129 @LENGTH@ 4\n" +
                "------------------UPD literal@@2000 @TO@ 1000 @AT@ 129 @LENGTH@ 4\n");

    }

    @Test
    public void testTypedefCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_typedef_example_1.c");
        File revFile = new File("src/main/resource/testFiles/typedef_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD typedef@@club char name ] GROUP @TO@ club char name ] int size year GROUP @AT@ 123 @LENGTH@ 50\n" +
                "---UPD type@@club char name ] @TO@ club char name ] int size year @AT@ 138 @LENGTH@ 16\n" +
                "------UPD struct@@club char name ] @TO@ club char name ] int size year @AT@ 138 @LENGTH@ 16\n" +
                "---------UPD block@@char name ] @TO@ char name ] int size year @AT@ 143 @LENGTH@ 23\n" +
                "------------INS decl_stmt@@int size year @TO@ block@@char name ] @AT@ 168 @LENGTH@ 13\n" +
                "---------------INS decl@@int size @TO@ decl_stmt@@int size year @AT@ 168 @LENGTH@ 8\n" +
                "------------------INS type@@int @TO@ decl@@int size @AT@ 168 @LENGTH@ 3\n" +
                "---------------------INS name@@int @TO@ type@@int @AT@ 168 @LENGTH@ 3\n" +
                "------------------INS name@@size @TO@ decl@@int size @AT@ 172 @LENGTH@ 4\n" +
                "---------------INS name@@year @TO@ decl_stmt@@int size year @AT@ 178 @LENGTH@ 4\n");

    }

    @Test
    public void testArrayCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_array_example_1.c");
        File revFile = new File("src/main/resource/testFiles/array_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD struct@@arr int a ] ] = 29 ] = 15 test @TO@ arr int a ] ] = 29 ] = 15 ] = 30 test @AT@ 0 @LENGTH@ 59\n" +
                "---UPD block@@int a ] ] = 29 ] = 15 @TO@ int a ] ] = 29 ] = 15 ] = 30 @AT@ 10 @LENGTH@ 43\n" +
                "------UPD decl_stmt@@int a ] ] = 29 ] = 15 @TO@ int a ] ] = 29 ] = 15 ] = 30 @AT@ 16 @LENGTH@ 21\n" +
                "---------UPD decl@@int a ] ] = 29 ] = 15 @TO@ int a ] ] = 29 ] = 15 ] = 30 @AT@ 16 @LENGTH@ 21\n" +
                "------------UPD init@@] = 29 ] = 15 @TO@ ] = 29 ] = 15 ] = 30 @AT@ 27 @LENGTH@ 13\n" +
                "---------------UPD expr@@] = 29 ] = 15 @TO@ ] = 29 ] = 15 ] = 30 @AT@ 27 @LENGTH@ 13\n" +
                "------------------UPD block@@] = 29 ] = 15 @TO@ ] = 29 ] = 15 ] = 30 @AT@ 27 @LENGTH@ 23\n" +
                "---------------------INS expr@@] = 30 @TO@ block@@] = 29 ] = 15 @AT@ 49 @LENGTH@ 6\n" +
                "------------------------INS index@@] @TO@ expr@@] = 30 @AT@ 49 @LENGTH@ 4\n" +
                "---------------------------INS expr@@[3 @TO@ index@@] @AT@ 50 @LENGTH@ 2\n" +
                "------------------------------INS literal@@[3 @TO@ expr@@[3 @AT@ 50 @LENGTH@ 2\n" +
                "------------------------INS operator@@= @TO@ expr@@] = 30 @AT@ 53 @LENGTH@ 1\n" +
                "------------------------INS literal@@30 @TO@ expr@@] = 30 @AT@ 55 @LENGTH@ 2\n");

    }

    @Test
    public void testBitfieldCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_bitfield_example_1.c");
        File revFile = new File("src/main/resource/testFiles/bitfield_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD struct@@employee char name ] int a 10 long cls temp @TO@ employee char name ] int a 5 long cls temp @AT@ 0 @LENGTH@ 74\n" +
                "---UPD block@@char name ] int a 10 long cls @TO@ char name ] int a 5 long cls @AT@ 15 @LENGTH@ 53\n" +
                "------UPD decl_stmt@@int a 10 @TO@ int a 5 @AT@ 40 @LENGTH@ 8\n" +
                "---------UPD decl@@int a 10 @TO@ int a 5 @AT@ 40 @LENGTH@ 8\n" +
                "------------UPD range@@10 @TO@ 5 @AT@ 48 @LENGTH@ 2\n" +
                "---------------UPD expr@@10 @TO@ 5 @AT@ 48 @LENGTH@ 2\n" +
                "------------------UPD literal@@10 @TO@ 5 @AT@ 48 @LENGTH@ 2\n");

    }




    @Test
    public void testTernaryCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_ternary_example_1.c");
        File revFile = new File("src/main/resource/testFiles/ternary_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@static int rpm_range_to_reg int range int i a x z @TO@ static int rpm_range_to_reg int range int i a x y @AT@ 0 @LENGTH@ 49\n" +
                "---UPD block@@int i a x z @TO@ int i a x y @AT@ 38 @LENGTH@ 31\n" +
                "------UPD return@@a x z @TO@ a x y @AT@ 49 @LENGTH@ 18\n" +
                "---------UPD expr@@a x z @TO@ a x y @AT@ 56 @LENGTH@ 5\n" +
                "------------UPD ternary@@a x z @TO@ a x y @AT@ 56 @LENGTH@ 5\n" +
                "---------------UPD else@@z @TO@ y @AT@ 64 @LENGTH@ 1\n" +
                "------------------UPD expr@@z @TO@ y @AT@ 64 @LENGTH@ 1\n" +
                "---------------------UPD name@@z @TO@ y @AT@ 64 @LENGTH@ 1\n");

    }

    @Test
    public void testDereferenceCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_dereference_example_1.c");
        File revFile = new File("src/main/resource/testFiles/dereference_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@static struct max6639_data * max6639_update_device struct device * dev struct max6639_data * data dev_get_drvdata dev struct i2c_client * client data -> client @TO@ static struct max6639_data * max6639_update_device struct device * dev struct max6639_data * data dev_get_drvdata dev struct i2c_client * client test -> client @AT@ 0 @LENGTH@ 159\n" +
                "---UPD block@@struct max6639_data * data dev_get_drvdata dev struct i2c_client * client data -> client @TO@ struct max6639_data * data dev_get_drvdata dev struct i2c_client * client test -> client @AT@ 69 @LENGTH@ 98\n" +
                "------UPD decl_stmt@@struct i2c_client * client data -> client @TO@ struct i2c_client * client test -> client @AT@ 123 @LENGTH@ 41\n" +
                "---------UPD decl@@struct i2c_client * client data -> client @TO@ struct i2c_client * client test -> client @AT@ 123 @LENGTH@ 41\n" +
                "------------UPD init@@data -> client @TO@ test -> client @AT@ 151 @LENGTH@ 14\n" +
                "---------------UPD expr@@data -> client @TO@ test -> client @AT@ 151 @LENGTH@ 14\n" +
                "------------------UPD name@@data -> client @TO@ test -> client @AT@ 151 @LENGTH@ 14\n" +
                "---------------------UPD name@@data @TO@ test @AT@ 151 @LENGTH@ 4\n");

    }

    @Test
    public void testSizeofCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");

        File prevFile =new File("src/main/resource/testFiles/prev_sizeof_example_1.c");
        File revFile = new File("src/main/resource/testFiles/sizeof_example_1.c");




        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@int main printf \"%lu\\n\" char printf \"%lu\\n\" int printf \"%lu\\n\" float printf \"%lu\" double 0 @TO@ int main printf \"%lu\\n\" char printf \"%lu\\n\" int printf \"%lu\\n\" float printf \"%lu\" int 0 @AT@ 19 @LENGTH@ 90\n" +
                "---UPD block@@printf \"%lu\\n\" char printf \"%lu\\n\" int printf \"%lu\\n\" float printf \"%lu\" double 0 @TO@ printf \"%lu\\n\" char printf \"%lu\\n\" int printf \"%lu\\n\" float printf \"%lu\" int 0 @AT@ 31 @LENGTH@ 164\n" +
                "------UPD expr_stmt@@printf \"%lu\" double @TO@ printf \"%lu\" int @AT@ 146 @LENGTH@ 19\n" +
                "---------UPD expr@@printf \"%lu\" double @TO@ printf \"%lu\" int @AT@ 146 @LENGTH@ 19\n" +
                "------------UPD call@@printf \"%lu\" double @TO@ printf \"%lu\" int @AT@ 146 @LENGTH@ 19\n" +
                "---------------UPD argument_list@@\"%lu\" double @TO@ \"%lu\" int @AT@ 152 @LENGTH@ 24\n" +
                "------------------UPD argument@@double @TO@ int @AT@ 166 @LENGTH@ 6\n" +
                "---------------------UPD expr@@double @TO@ int @AT@ 166 @LENGTH@ 6\n" +
                "------------------------UPD sizeof@@double @TO@ int @AT@ 166 @LENGTH@ 6\n" +
                "---------------------------UPD argument_list@@double @TO@ int @AT@ 166 @LENGTH@ 9\n" +
                "------------------------------UPD argument@@double @TO@ int @AT@ 167 @LENGTH@ 6\n" +
                "---------------------------------UPD expr@@double @TO@ int @AT@ 167 @LENGTH@ 6\n" +
                "------------------------------------UPD name@@double @TO@ int @AT@ 167 @LENGTH@ 6\n");

    }
}
