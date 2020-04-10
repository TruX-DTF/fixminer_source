package edu.lu.uni.serval;


import edu.lu.uni.serval.richedit.ediff.EDiffHunkParser;
import edu.lu.uni.serval.richedit.ediff.HierarchicalActionSet;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TestPredefinedCases {




    @Test
    public void testIFCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = "";

        File revFile = new File("src/main/resource/testFiles/if_example_1.c");
        File prevFile =new File("src/main/resource/testFiles/prev_if_example_1.c");

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@x >= 5 y += 4 @TO@ x > 5 y += 4 @AT@ 0 @LENGTH@ 13\n" +
                "---UPD if@@x >= 5 y += 4 @TO@ x > 5 y += 4 @AT@ 0 @LENGTH@ 13\n" +
                "------UPD condition@@x >= 5 @TO@ x > 5 @AT@ 2 @LENGTH@ 6\n" +
                "---------UPD expr@@x >= 5 @TO@ x > 5 @AT@ 3 @LENGTH@ 6\n" +
                "------------UPD operator@@>= @TO@ > @AT@ 5 @LENGTH@ 2\n");

    }
    @Test
    public void testForCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = "";

        File revFile = new File("src/main/resource/testFiles/for_example_1.c");
        File prevFile =new File("src/main/resource/testFiles/prev_for_example_1.c");



        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@line ][i == ' ' continue; @TO@ line ][i == ' ' space ++ @AT@ 30 @LENGTH@ 25\n" +
                "---UPD if@@line ][i == ' ' continue; @TO@ line ][i == ' ' space ++ @AT@ 30 @LENGTH@ 25\n" +
                "------UPD block@@continue; @TO@ space ++ @AT@ 54 @LENGTH@ 9\n" +
                "---------UPD block_content@@continue; @TO@ space ++ @AT@ 62 @LENGTH@ 9\n" +
                "------------INS expr_stmt@@space ++ @TO@ block_content@@continue; @AT@ 62 @LENGTH@ 8\n" +
                "---------------INS expr@@space ++ @TO@ expr_stmt@@space ++ @AT@ 62 @LENGTH@ 8\n" +
                "------------------INS name@@space @TO@ expr@@space ++ @AT@ 62 @LENGTH@ 5\n" +
                "------------------INS operator@@++ @TO@ expr@@space ++ @AT@ 67 @LENGTH@ 2\n" +
                "------------DEL continue@@continue; @AT@ 62 @LENGTH@ 9\n");

    }

    @Test
    public void testWhileCase1() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = "";

        File revFile = new File("src/main/resource/testFiles/while_example_1.c");
        File prevFile = new File("src/main/resource/testFiles/prev_while_example_1.c");


        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(), 1);

        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "UPD do@@y = f x x -- print x x > 0 @TO@ y = f x x -- x > 0 @AT@ 0 @LENGTH@ 26\n" +
                "---UPD block@@y = f x x -- print x @TO@ y = f x x -- @AT@ 3 @LENGTH@ 20\n" +
                "------UPD block_content@@y = f x x -- print x @TO@ y = f x x -- @AT@ 8 @LENGTH@ 20\n" +
                "---------DEL expr_stmt@@print x @AT@ 33 @LENGTH@ 7\n" +
                "------------DEL expr@@print x @AT@ 33 @LENGTH@ 7\n" +
                "---------------DEL call@@print x @AT@ 33 @LENGTH@ 7\n" +
                "------------------DEL name@@print @AT@ 33 @LENGTH@ 5\n" +
                "------------------DEL argument_list@@x @AT@ 38 @LENGTH@ 1\n" +
                "---------------------DEL argument@@x @AT@ 39 @LENGTH@ 1\n" +
                "------------------------DEL expr@@x @AT@ 39 @LENGTH@ 1\n" +
                "---------------------------DEL name@@x @AT@ 39 @LENGTH@ 1\n");
    }


        @Test
        public void testIFRetrun() throws IOException {
            Properties appProps = new Properties();
            appProps.load(new FileInputStream("src/main/resource/app.properties"));
            String srcMLPath = "";

            File revFile = new File("src/main/resource/testFiles/if_return.c");
            File prevFile =new File("src/main/resource/testFiles/prev_if_return.c");



            EDiffHunkParser parser = new EDiffHunkParser();

            List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
            Assert.assertEquals(hierarchicalActionSets.size(),1);
            Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block_content@@field = ATOM TST PTR_ERROR_OR_ZERO fields -> mode @TO@ field = ATOM TST IS_ERR fields -> mode PTR_ERROR fields -> mode 0 @AT@ 22 @LENGTH@ 49\n" +
                    "---INS if_stmt@@IS_ERR fields -> mode PTR_ERROR fields -> mode @TO@ block_content@@field = ATOM TST PTR_ERROR_OR_ZERO fields -> mode @AT@ 45 @LENGTH@ 46\n" +
                    "------INS if@@IS_ERR fields -> mode PTR_ERROR fields -> mode @TO@ if_stmt@@IS_ERR fields -> mode PTR_ERROR fields -> mode @AT@ 45 @LENGTH@ 46\n" +
                    "---------INS condition@@IS_ERR fields -> mode @TO@ if@@IS_ERR fields -> mode PTR_ERROR fields -> mode @AT@ 47 @LENGTH@ 21\n" +
                    "------------MOV expr@@PTR_ERROR_OR_ZERO fields -> mode @TO@ condition@@IS_ERR fields -> mode @AT@ 52 @LENGTH@ 32\n" +
                    "---------INS block@@PTR_ERROR fields -> mode @TO@ if@@IS_ERR fields -> mode PTR_ERROR fields -> mode @AT@ 78 @LENGTH@ 24\n" +
                    "------------INS block_content@@PTR_ERROR fields -> mode @TO@ block@@PTR_ERROR fields -> mode @AT@ 78 @LENGTH@ 24\n" +
                    "---------------MOV return@@PTR_ERROR_OR_ZERO fields -> mode @TO@ block_content@@PTR_ERROR fields -> mode @AT@ 45 @LENGTH@ 32\n" +
                    "---UPD return@@PTR_ERROR_OR_ZERO fields -> mode @TO@ PTR_ERROR fields -> mode @AT@ 45 @LENGTH@ 32\n" +
                    "------INS expr@@PTR_ERROR fields -> mode @TO@ return@@PTR_ERROR_OR_ZERO fields -> mode @AT@ 85 @LENGTH@ 24\n" +
                    "---------INS call@@PTR_ERROR fields -> mode @TO@ expr@@PTR_ERROR fields -> mode @AT@ 85 @LENGTH@ 24\n" +
                    "------------INS name@@PTR_ERROR @TO@ call@@PTR_ERROR fields -> mode @AT@ 85 @LENGTH@ 9\n" +
                    "------------INS argument_list@@fields -> mode @TO@ call@@PTR_ERROR fields -> mode @AT@ 94 @LENGTH@ 14\n" +
                    "---------------INS argument@@fields -> mode @TO@ argument_list@@fields -> mode @AT@ 95 @LENGTH@ 14\n" +
                    "------------------INS expr@@fields -> mode @TO@ argument@@fields -> mode @AT@ 95 @LENGTH@ 14\n" +
                    "---------------------INS name@@fields -> mode @TO@ expr@@fields -> mode @AT@ 95 @LENGTH@ 14\n" +
                    "------------------------INS name@@fields @TO@ name@@fields -> mode @AT@ 95 @LENGTH@ 6\n" +
                    "------------------------INS operator@@-> @TO@ name@@fields -> mode @AT@ 101 @LENGTH@ 2\n" +
                    "------------------------INS name@@mode @TO@ name@@fields -> mode @AT@ 103 @LENGTH@ 4\n" +
                    "---INS return@@0 @TO@ block_content@@field = ATOM TST PTR_ERROR_OR_ZERO fields -> mode @AT@ 114 @LENGTH@ 1\n" +
                    "------INS expr@@0 @TO@ return@@0 @AT@ 121 @LENGTH@ 1\n" +
                    "---------INS literal:number@@0 @TO@ expr@@0 @AT@ 121 @LENGTH@ 1\n");
        }
        @Test
        public void testIfElse() throws IOException {
            Properties appProps = new Properties();
            appProps.load(new FileInputStream("src/main/resource/app.properties"));
            String srcMLPath = "";

            File revFile = new File("src/main/resource/testFiles/if_else.c");
            File prevFile =new File("src/main/resource/testFiles/prev_if_else.c");



            EDiffHunkParser parser = new EDiffHunkParser();

            List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
            Assert.assertEquals(hierarchicalActionSets.size(),3);
            Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@static ctl_serialize_action static int test field = ATOM TST IS_ERR fields -> mode PTR_ERROR fields -> mode a > 0 1 0 @TO@ const static ctl_serialize_action static int test field = ATOM TST IS_ERR fields -> mode PTR_ERROR fields -> mode a > 0 1 0 @AT@ 0 @LENGTH@ 117\n" +
                    "---UPD type@@static ctl_serialize_action static int @TO@ const static ctl_serialize_action static int @AT@ 0 @LENGTH@ 38\n" +
                    "------INS specifier@@const @TO@ type@@static ctl_serialize_action static int @AT@ 0 @LENGTH@ 5\n");
            Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD if_stmt@@IS_ERR fields -> mode PTR_ERROR fields -> mode a > 0 1 @TO@ IS_ERR fields -> mode PTR_ERROR fields -> mode @AT@ 73 @LENGTH@ 54\n");
            Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"INS if_stmt@@a > 0 1 @TO@ block_content@@field = ATOM TST IS_ERR fields -> mode PTR_ERROR fields -> mode a > 0 1 0 @AT@ 149 @LENGTH@ 7\n" +
                    "---MOV if@@a > 0 1 @TO@ if_stmt@@a > 0 1 @AT@ 142 @LENGTH@ 7\n");
        }

    @Test
    public void testStruct() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = "";

        File revFile = new File("src/main/resource/testFiles/struct.c");
        File prevFile =new File("src/main/resource/testFiles/prev_struct.c");



        EDiffHunkParser parser = new EDiffHunkParser();

        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD struct@@b int x double y float z @TO@ a int x double y float z @AT@ 0 @LENGTH@ 24\n" +
                "---UPD name@@b @TO@ a @AT@ 7 @LENGTH@ 1\n");
    }






}
