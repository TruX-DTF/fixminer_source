package edu.lu.uni.serval.fixminer.akka.ediff;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class TestRealCases {




    @Test
    public void test_287_A_14208510_14208532() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="287-A-14208510-14208532.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for j = 0 j < 3 j ++ if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d == 3 || h == 3 printf \"YES\" 0 @TO@ for j = 0 j < 3 j ++ if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 184 @LENGTH@ 211\n" +
                "---UPD block@@if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d == 3 || h == 3 printf \"YES\" 0 @TO@ if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 199 @LENGTH@ 347\n" +
                "------UPD if@@if d == 3 || h == 3 printf \"YES\" 0 @TO@ if d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 449 @LENGTH@ 34\n" +
                "---------UPD condition@@d == 3 || h == 3 @TO@ d >= 3 || h >= 3 @AT@ 449 @LENGTH@ 15\n" +
                "------------UPD expr@@d == 3 || h == 3 @TO@ d >= 3 || h >= 3 @AT@ 450 @LENGTH@ 16\n" +
                "---------------UPD operator@@== @TO@ >= @AT@ 451 @LENGTH@ 2\n" +
                "---------------UPD operator@@== @TO@ >= @AT@ 459 @LENGTH@ 2\n" +
                "------INS expr_stmt@@d = 0 @TO@ block@@if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d == 3 || h == 3 printf \"YES\" 0 @AT@ 548 @LENGTH@ 5\n" +
                "---------INS expr@@d = 0 @TO@ expr_stmt@@d = 0 @AT@ 548 @LENGTH@ 5\n" +
                "------------INS name@@d @TO@ expr@@d = 0 @AT@ 548 @LENGTH@ 1\n" +
                "------------INS operator@@= @TO@ expr@@d = 0 @AT@ 549 @LENGTH@ 1\n" +
                "------------INS literal@@0 @TO@ expr@@d = 0 @AT@ 550 @LENGTH@ 1\n" +
                "------INS expr_stmt@@h = 0 @TO@ block@@if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d == 3 || h == 3 printf \"YES\" 0 @AT@ 553 @LENGTH@ 5\n" +
                "---------INS expr@@h = 0 @TO@ expr_stmt@@h = 0 @AT@ 553 @LENGTH@ 5\n" +
                "------------INS name@@h @TO@ expr@@h = 0 @AT@ 553 @LENGTH@ 1\n" +
                "------------INS operator@@= @TO@ expr@@h = 0 @AT@ 554 @LENGTH@ 1\n" +
                "------------INS literal@@0 @TO@ expr@@h = 0 @AT@ 555 @LENGTH@ 1\n");

    }

    @Test
    public void test_287_A_14208521_14208532() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="287-A-14208521-14208532.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for j = 0 j < 3 j ++ if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 @TO@ for j = 0 j < 3 j ++ if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 184 @LENGTH@ 211\n" +
                "---UPD block@@if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 @TO@ if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 199 @LENGTH@ 347\n" +
                "------INS expr_stmt@@d = 0 @TO@ block@@if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 548 @LENGTH@ 5\n" +
                "---------INS expr@@d = 0 @TO@ expr_stmt@@d = 0 @AT@ 548 @LENGTH@ 5\n" +
                "------------INS name@@d @TO@ expr@@d = 0 @AT@ 548 @LENGTH@ 1\n" +
                "------------INS operator@@= @TO@ expr@@d = 0 @AT@ 549 @LENGTH@ 1\n" +
                "------------INS literal@@0 @TO@ expr@@d = 0 @AT@ 550 @LENGTH@ 1\n" +
                "------INS expr_stmt@@h = 0 @TO@ block@@if g ][i ][j == '.' d ++ else h ++ if g ][i ][j + 1 == '.' d ++ else h ++ if g ][i + 1 ][j == '.' d ++ else h ++ if g ][i + 1 ][j + 1 == '.' d ++ else h ++ if d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 553 @LENGTH@ 5\n" +
                "---------INS expr@@h = 0 @TO@ expr_stmt@@h = 0 @AT@ 553 @LENGTH@ 5\n" +
                "------------INS name@@h @TO@ expr@@h = 0 @AT@ 553 @LENGTH@ 1\n" +
                "------------INS operator@@= @TO@ expr@@h = 0 @AT@ 554 @LENGTH@ 1\n" +
                "------------INS literal@@0 @TO@ expr@@h = 0 @AT@ 555 @LENGTH@ 1\n");

    }

    @Test
    public void test_189_1682083_1682218() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="189-B-1682083-1682218.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
//        Assert.assertFalse(true);
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for j = 1 j < h j ++ k = i k = MIN k w - i t = j k = MIN k h - j ans += k * t @TO@ for j = 1 j < h j ++ k = i k = MIN k w - i t = j t = MIN t h - j ans += k * t @AT@ 174 @LENGTH@ 77\n" +
                "---UPD block@@k = i k = MIN k w - i t = j k = MIN k h - j ans += k * t @TO@ k = i k = MIN k w - i t = j t = MIN t h - j ans += k * t @AT@ 195 @LENGTH@ 104\n" +
                "------UPD expr_stmt@@k = MIN k h - j @TO@ t = MIN t h - j @AT@ 254 @LENGTH@ 15\n" +
                "---------UPD expr@@k = MIN k h - j @TO@ t = MIN t h - j @AT@ 254 @LENGTH@ 15\n" +
                "------------UPD name@@k @TO@ t @AT@ 254 @LENGTH@ 1\n" +
                "------------UPD call@@MIN k h - j @TO@ MIN t h - j @AT@ 258 @LENGTH@ 11\n" +
                "---------------UPD argument_list@@k h - j @TO@ t h - j @AT@ 261 @LENGTH@ 11\n" +
                "------------------UPD argument@@k @TO@ t @AT@ 262 @LENGTH@ 1\n" +
                "---------------------UPD expr@@k @TO@ t @AT@ 262 @LENGTH@ 1\n" +
                "------------------------UPD name@@k @TO@ t @AT@ 262 @LENGTH@ 1\n");

    }


    @Test
    public void test_177_A2_1594730_1595168() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="177-A2-1594730-1595168.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 mid = a @TO@ if i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 mid = a @AT@ 350 @LENGTH@ 51\n" +
                "---UPD condition@@i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 @TO@ i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 @AT@ 350 @LENGTH@ 27\n" +
                "------UPD expr@@i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 @TO@ i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 @AT@ 351 @LENGTH@ 40\n" +
                "---------UPD operator@@- @TO@ + @AT@ 356 @LENGTH@ 1\n" +
                "---------UPD operator@@- @TO@ + @AT@ 370 @LENGTH@ 1\n");

    }

    @Test
    public void test_680_A_18343132_18343191() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="680-A-18343132-18343191.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if b ][a ][i == 3 max = 3 * a ][i break; @TO@ if b ][a ][i >= 3 max = 3 * a ][i break; @AT@ 176 @LENGTH@ 40\n" +
                "---UPD condition@@b ][a ][i == 3 @TO@ b ][a ][i >= 3 @AT@ 176 @LENGTH@ 13\n" +
                "------UPD expr@@b ][a ][i == 3 @TO@ b ][a ][i >= 3 @AT@ 177 @LENGTH@ 14\n" +
                "---------UPD operator@@== @TO@ >= @AT@ 184 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD if@@if temp > max max = temp break; @TO@ if temp > max max = temp @AT@ 270 @LENGTH@ 31\n" +
                "---UPD then@@max = temp break; @TO@ max = temp @AT@ 282 @LENGTH@ 17\n" +
                "------UPD block@@max = temp break; @TO@ max = temp @AT@ 282 @LENGTH@ 24\n" +
                "---------DEL break@@break; @AT@ 296 @LENGTH@ 6\n");

    }

    @Test
    public void test_245_D_3671804_3671831() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="245-D-3671804-3671831.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if i != j @TO@ if i != j ans |= a @AT@ 186 @LENGTH@ 9\n" +
                "---UPD then@@ @TO@ ans |= a @AT@ 192 @LENGTH@ 0\n" +
                "------UPD block@@ @TO@ ans |= a @AT@ 192 @LENGTH@ 0\n" +
                "---------DEL empty_stmt@@ @AT@ 192 @LENGTH@ 0\n" +
                "---------MOV expr_stmt@@ans |= a @TO@ block@@ @AT@ 194 @LENGTH@ 8\n");

    }

    @Test
    public void test_197_B_18221952_18221968() throws IOException {
        //TODO not sure
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="197-B-18221952-18221968.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if a ][0 % i == 0 && b ][0 % i == 0 a ][0 /= i b ][0 /= i @TO@ if a ][0 % i == 0 && b ][0 % i == 0 a ][0 /= i b ][0 /= i i -- @AT@ 742 @LENGTH@ 57\n" +
                "---UPD then@@a ][0 /= i b ][0 /= i @TO@ a ][0 /= i b ][0 /= i i -- @AT@ 779 @LENGTH@ 21\n" +
                "------UPD block@@a ][0 /= i b ][0 /= i @TO@ a ][0 /= i b ][0 /= i i -- @AT@ 779 @LENGTH@ 66\n" +
                "---------INS expr_stmt@@i -- @TO@ block@@a ][0 /= i b ][0 /= i @AT@ 831 @LENGTH@ 4\n" +
                "------------INS expr@@i -- @TO@ expr_stmt@@i -- @AT@ 831 @LENGTH@ 4\n" +
                "---------------INS name@@i @TO@ expr@@i -- @AT@ 831 @LENGTH@ 1\n" +
                "---------------INS operator@@-- @TO@ expr@@i -- @AT@ 832 @LENGTH@ 2\n");

    }

    @Test
    public void test_474_A_15226851_15226912() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="474-A-15226851-15226912.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if str ][i == s ][j str ][i = s ][j + 1 @TO@ if str ][i == s ][j j ++ str ][i = s ][j @AT@ 546 @LENGTH@ 39\n" +
                "---UPD then@@str ][i = s ][j + 1 @TO@ j ++ str ][i = s ][j @AT@ 560 @LENGTH@ 19\n" +
                "------UPD block@@str ][i = s ][j + 1 @TO@ j ++ str ][i = s ][j @AT@ 560 @LENGTH@ 55\n" +
                "---------INS expr_stmt@@j ++ @TO@ block@@str ][i = s ][j + 1 @AT@ 582 @LENGTH@ 4\n" +
                "------------INS expr@@j ++ @TO@ expr_stmt@@j ++ @AT@ 582 @LENGTH@ 4\n" +
                "---------------INS name@@j @TO@ expr@@j ++ @AT@ 582 @LENGTH@ 1\n" +
                "---------------INS operator@@++ @TO@ expr@@j ++ @AT@ 583 @LENGTH@ 2\n" +
                "---------UPD expr_stmt@@str ][i = s ][j + 1 @TO@ str ][i = s ][j @AT@ 582 @LENGTH@ 19\n" +
                "------------UPD expr@@str ][i = s ][j + 1 @TO@ str ][i = s ][j @AT@ 582 @LENGTH@ 19\n" +
                "---------------UPD name@@s ][j + 1 @TO@ s ][j @AT@ 589 @LENGTH@ 9\n" +
                "------------------UPD index@@][j + 1 @TO@ ][j @AT@ 591 @LENGTH@ 7\n" +
                "---------------------UPD expr@@[j + 1 @TO@ [j @AT@ 591 @LENGTH@ 6\n" +
                "------------------------DEL operator@@+ @AT@ 592 @LENGTH@ 1\n" +
                "------------------------DEL literal@@1 @AT@ 593 @LENGTH@ 1\n");

    }

    @Test
    public void test_469_B_8248222_8248281() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="469-B-8248222-8248281.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if temp1 < r && temp2 > l for k = temp1 k <= temp2 k ++ t ][k = 1 @TO@ if temp1 <= r && temp2 >= l for k = temp1 k <= temp2 k ++ t ][k = 1 @AT@ 432 @LENGTH@ 65\n" +
                "---UPD condition@@temp1 < r && temp2 > l @TO@ temp1 <= r && temp2 >= l @AT@ 432 @LENGTH@ 19\n" +
                "------UPD expr@@temp1 < r && temp2 > l @TO@ temp1 <= r && temp2 >= l @AT@ 433 @LENGTH@ 22\n" +
                "---------UPD operator@@< @TO@ <= @AT@ 438 @LENGTH@ 1\n" +
                "---------UPD operator@@> @TO@ >= @AT@ 447 @LENGTH@ 1\n");

    }

    @Test
    public void test_189_B_17295034_17295064() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="189-B-17295034-17295064.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for b = 2 b <= w b += 2 count += ( w - a + 1 ) * ( h - b + 1 ) @TO@ for b = 2 b <= h b += 2 count += ( w - a + 1 ) * ( h - b + 1 ) @AT@ 183 @LENGTH@ 62\n" +
                "---UPD control@@b = 2 b <= w b += 2 @TO@ b = 2 b <= h b += 2 @AT@ 183 @LENGTH@ 16\n" +
                "------UPD condition@@b <= w @TO@ b <= h @AT@ 188 @LENGTH@ 6\n" +
                "---------UPD expr@@b <= w @TO@ b <= h @AT@ 188 @LENGTH@ 6\n" +
                "------------UPD name@@w @TO@ h @AT@ 191 @LENGTH@ 1\n");

    }

    @Test
    public void test_244_B_5291533_5291541() throws IOException {
        //TODO not sure
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="244-B-5291533-5291541.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if a <= 101 printf \"%d\\n\" a elseif if a == 123 printf \"113\\n\" elseif if a == 1000 printf \"352\\n\" elseif if a == 1000000000 printf \"40744\\n\" elseif if a == 999999999 printf \"40743\\n\" elseif if a == 999999998 printf \"40742\\n\" elseif if a == 999999997 printf \"40741\\n\" elseif if a == 909090901 printf \"38532\\n\" elseif if a == 142498040 printf \"21671\\n\" elseif if a == 603356456 printf \"31623\\n\" elseif if a == 64214872 printf \"15759\\n\" elseif if a == 820040584 printf \"36407\\n\" elseif if a == 442198 printf \"3071\\n\" elseif if a == 642678 printf \"3615\\n\" elseif if a == 468390 printf \"3223\\n\" elseif if a == 326806 printf \"2759\\n\" elseif if a == 940 printf \"331\\n\" elseif if a == 356 printf \"175\\n\" elseif if a == 132 printf \"114\\n\" elseif if a == 102 printf \"101\\n\" @TO@ if a <= 101 printf \"%d\\n\" a elseif if a == 123 printf \"113\\n\" elseif if a == 1000 printf \"352\\n\" elseif if a == 1000000000 printf \"40744\\n\" elseif if a == 999999999 printf \"40743\\n\" elseif if a == 999999998 printf \"40742\\n\" elseif if a == 999999997 printf \"40741\\n\" elseif if a == 909090901 printf \"38532\\n\" elseif if a == 142498040 printf \"21671\\n\" elseif if a == 603356456 printf \"31623\\n\" elseif if a == 64214872 printf \"15759\\n\" elseif if a == 820040584 printf \"36407\\n\" elseif if a == 442198 printf \"3071\\n\" elseif if a == 784262 printf \"4079\\n\" elseif if a == 642678 printf \"3615\\n\" elseif if a == 468390 printf \"3223\\n\" elseif if a == 326806 printf \"2759\\n\" elseif if a == 940 printf \"331\\n\" elseif if a == 356 printf \"175\\n\" elseif if a == 132 printf \"114\\n\" elseif if a == 102 printf \"101\\n\" @AT@ 110 @LENGTH@ 762\n" +
                "---INS elseif@@elseif if a == 784262 printf \"4079\\n\" @TO@ if@@if a <= 101 printf \"%d\\n\" a elseif if a == 123 printf \"113\\n\" elseif if a == 1000 printf \"352\\n\" elseif if a == 1000000000 printf \"40744\\n\" elseif if a == 999999999 printf \"40743\\n\" elseif if a == 999999998 printf \"40742\\n\" elseif if a == 999999997 printf \"40741\\n\" elseif if a == 909090901 printf \"38532\\n\" elseif if a == 142498040 printf \"21671\\n\" elseif if a == 603356456 printf \"31623\\n\" elseif if a == 64214872 printf \"15759\\n\" elseif if a == 820040584 printf \"36407\\n\" elseif if a == 442198 printf \"3071\\n\" elseif if a == 642678 printf \"3615\\n\" elseif if a == 468390 printf \"3223\\n\" elseif if a == 326806 printf \"2759\\n\" elseif if a == 940 printf \"331\\n\" elseif if a == 356 printf \"175\\n\" elseif if a == 132 printf \"114\\n\" elseif if a == 102 printf \"101\\n\" @AT@ 877 @LENGTH@ 37\n" +
                "------INS if@@if a == 784262 printf \"4079\\n\" @TO@ elseif@@elseif if a == 784262 printf \"4079\\n\" @AT@ 877 @LENGTH@ 30\n" +
                "---------INS condition@@a == 784262 @TO@ if@@if a == 784262 printf \"4079\\n\" @AT@ 877 @LENGTH@ 12\n" +
                "------------INS expr@@a == 784262 @TO@ condition@@a == 784262 @AT@ 878 @LENGTH@ 11\n" +
                "---------------INS name@@a @TO@ expr@@a == 784262 @AT@ 878 @LENGTH@ 1\n" +
                "---------------INS operator@@== @TO@ expr@@a == 784262 @AT@ 879 @LENGTH@ 2\n" +
                "---------------INS literal@@784262 @TO@ expr@@a == 784262 @AT@ 881 @LENGTH@ 6\n" +
                "---------INS then@@printf \"4079\\n\" @TO@ if@@if a == 784262 printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "------------INS block@@printf \"4079\\n\" @TO@ then@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "---------------INS expr_stmt@@printf \"4079\\n\" @TO@ block@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "------------------INS expr@@printf \"4079\\n\" @TO@ expr_stmt@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "---------------------INS call@@printf \"4079\\n\" @TO@ expr@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "------------------------INS name@@printf @TO@ call@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 6\n" +
                "------------------------INS argument_list@@\"4079\\n\" @TO@ call@@printf \"4079\\n\" @AT@ 907 @LENGTH@ 11\n" +
                "---------------------------INS argument@@\"4079\\n\" @TO@ argument_list@@\"4079\\n\" @AT@ 908 @LENGTH@ 8\n" +
                "------------------------------INS expr@@\"4079\\n\" @TO@ argument@@\"4079\\n\" @AT@ 908 @LENGTH@ 8\n" +
                "---------------------------------INS literal@@\"4079\\n\" @TO@ expr@@\"4079\\n\" @AT@ 908 @LENGTH@ 8\n");

    }
    @Test
    public void test_166_C_1395587_1395933() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="166-C-1395587-1395933.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if array ][( n + 1 ) / 2 == x printf \"0\\n\" elseif if last < ( n + 1 ) / 2 printf \"%d\\n\" n - 2 * last elseif if first > ( n + 1 ) / 2 printf \"%d\\n\" 2 * first - n - 1 @TO@ if array ][( n + 1 ) / 2 - 1 == x printf \"0\\n\" elseif if last < ( n + 1 ) / 2 printf \"%d\\n\" n - 2 * last elseif if first > ( n + 1 ) / 2 printf \"%d\\n\" 2 * first - n - 1 elseif if n == 1 printf \"0\\n\" @AT@ 771 @LENGTH@ 164\n" +
                "---UPD condition@@array ][( n + 1 ) / 2 == x @TO@ array ][( n + 1 ) / 2 - 1 == x @AT@ 771 @LENGTH@ 20\n" +
                "------UPD expr@@array ][( n + 1 ) / 2 == x @TO@ array ][( n + 1 ) / 2 - 1 == x @AT@ 772 @LENGTH@ 26\n" +
                "---------UPD name@@array ][( n + 1 ) / 2 @TO@ array ][( n + 1 ) / 2 - 1 @AT@ 772 @LENGTH@ 21\n" +
                "------------UPD index@@][( n + 1 ) / 2 @TO@ ][( n + 1 ) / 2 - 1 @AT@ 778 @LENGTH@ 15\n" +
                "---------------UPD expr@@[( n + 1 ) / 2 @TO@ [( n + 1 ) / 2 - 1 @AT@ 778 @LENGTH@ 14\n" +
                "------------------INS operator@@- @TO@ expr@@[( n + 1 ) / 2 @AT@ 785 @LENGTH@ 1\n" +
                "------------------INS literal@@1 @TO@ expr@@[( n + 1 ) / 2 @AT@ 786 @LENGTH@ 1\n" +
                "---INS elseif@@elseif if n == 1 printf \"0\\n\" @TO@ if@@if array ][( n + 1 ) / 2 == x printf \"0\\n\" elseif if last < ( n + 1 ) / 2 printf \"%d\\n\" n - 2 * last elseif if first > ( n + 1 ) / 2 printf \"%d\\n\" 2 * first - n - 1 @AT@ 925 @LENGTH@ 29\n" +
                "------INS if@@if n == 1 printf \"0\\n\" @TO@ elseif@@elseif if n == 1 printf \"0\\n\" @AT@ 925 @LENGTH@ 22\n" +
                "---------INS condition@@n == 1 @TO@ if@@if n == 1 printf \"0\\n\" @AT@ 925 @LENGTH@ 7\n" +
                "------------INS expr@@n == 1 @TO@ condition@@n == 1 @AT@ 926 @LENGTH@ 6\n" +
                "---------------INS name@@n @TO@ expr@@n == 1 @AT@ 926 @LENGTH@ 1\n" +
                "---------------INS operator@@== @TO@ expr@@n == 1 @AT@ 927 @LENGTH@ 2\n" +
                "---------------INS literal@@1 @TO@ expr@@n == 1 @AT@ 929 @LENGTH@ 1\n" +
                "---------INS then@@printf \"0\\n\" @TO@ if@@if n == 1 printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "------------INS block@@printf \"0\\n\" @TO@ then@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "---------------INS expr_stmt@@printf \"0\\n\" @TO@ block@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "------------------INS expr@@printf \"0\\n\" @TO@ expr_stmt@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "---------------------INS call@@printf \"0\\n\" @TO@ expr@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "------------------------INS name@@printf @TO@ call@@printf \"0\\n\" @AT@ 932 @LENGTH@ 6\n" +
                "------------------------INS argument_list@@\"0\\n\" @TO@ call@@printf \"0\\n\" @AT@ 938 @LENGTH@ 8\n" +
                "---------------------------INS argument@@\"0\\n\" @TO@ argument_list@@\"0\\n\" @AT@ 939 @LENGTH@ 5\n" +
                "------------------------------INS expr@@\"0\\n\" @TO@ argument@@\"0\\n\" @AT@ 939 @LENGTH@ 5\n" +
                "---------------------------------INS literal@@\"0\\n\" @TO@ expr@@\"0\\n\" @AT@ 939 @LENGTH@ 5\n");

    }

    @Test
    public void test_315_A_6149995_6150754() throws IOException {
        //TODO not sure
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="315-A-6149995-6150754.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if a ][j == t ans -- @TO@ if a ][j == t ans -- a ][j = 0 @AT@ 302 @LENGTH@ 20\n" +
                "---UPD then@@ans -- @TO@ ans -- a ][j = 0 @AT@ 332 @LENGTH@ 6\n" +
                "------UPD block@@ans -- @TO@ ans -- a ][j = 0 @AT@ 332 @LENGTH@ 6\n" +
                "---------INS expr_stmt@@a ][j = 0 @TO@ block@@ans -- @AT@ 377 @LENGTH@ 9\n" +
                "------------INS expr@@a ][j = 0 @TO@ expr_stmt@@a ][j = 0 @AT@ 377 @LENGTH@ 9\n" +
                "---------------INS name@@a ][j @TO@ expr@@a ][j = 0 @AT@ 377 @LENGTH@ 5\n" +
                "------------------INS name@@a @TO@ name@@a ][j @AT@ 377 @LENGTH@ 1\n" +
                "------------------INS index@@][j @TO@ name@@a ][j @AT@ 379 @LENGTH@ 3\n" +
                "---------------------INS expr@@[j @TO@ index@@][j @AT@ 379 @LENGTH@ 2\n" +
                "------------------------INS name@@[j @TO@ expr@@[j @AT@ 379 @LENGTH@ 2\n" +
                "---------------INS operator@@= @TO@ expr@@a ][j = 0 @AT@ 381 @LENGTH@ 1\n" +
                "---------------INS literal@@0 @TO@ expr@@a ][j = 0 @AT@ 382 @LENGTH@ 1\n");

    }

    @Test
    public void test_158_A_18237828_18237840() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="158-A-18237828-18237840.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if ara ][i >= ara ][k - 1 count ++ @TO@ if ara ][i >= ara ][k - 1 && ara ][i != 0 count ++ @AT@ 219 @LENGTH@ 34\n" +
                "---UPD condition@@ara ][i >= ara ][k - 1 @TO@ ara ][i >= ara ][k - 1 && ara ][i != 0 @AT@ 219 @LENGTH@ 19\n" +
                "------UPD expr@@ara ][i >= ara ][k - 1 @TO@ ara ][i >= ara ][k - 1 && ara ][i != 0 @AT@ 220 @LENGTH@ 22\n" +
                "---------INS operator@@&& @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 236 @LENGTH@ 2\n" +
                "---------INS name@@ara ][i @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 239 @LENGTH@ 7\n" +
                "------------INS name@@ara @TO@ name@@ara ][i @AT@ 239 @LENGTH@ 3\n" +
                "------------INS index@@][i @TO@ name@@ara ][i @AT@ 243 @LENGTH@ 3\n" +
                "---------------INS expr@@[i @TO@ index@@][i @AT@ 243 @LENGTH@ 2\n" +
                "------------------INS name@@[i @TO@ expr@@[i @AT@ 243 @LENGTH@ 2\n" +
                "---------INS operator@@!= @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 245 @LENGTH@ 2\n" +
                "---------INS literal@@0 @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 247 @LENGTH@ 1\n");

    }

    @Test
    public void test_405_B_9434593_9434605() throws IOException {
        //TODO not sure
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="405-B-9434593-9434605.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if input ][0 == '.' count = 1 @TO@ if input ][0 == '.' count = 1 else i1 = 0 @AT@ 260 @LENGTH@ 29\n" +
                "---INS else@@else i1 = 0 @TO@ if@@if input ][0 == '.' count = 1 @AT@ 295 @LENGTH@ 11\n" +
                "------INS block@@i1 = 0 @TO@ else@@else i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                "---------INS expr_stmt@@i1 = 0 @TO@ block@@i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                "------------INS expr@@i1 = 0 @TO@ expr_stmt@@i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                "---------------INS name@@i1 @TO@ expr@@i1 = 0 @AT@ 295 @LENGTH@ 2\n" +
                "---------------INS operator@@= @TO@ expr@@i1 = 0 @AT@ 297 @LENGTH@ 1\n" +
                "---------------INS literal@@0 @TO@ expr@@i1 = 0 @AT@ 298 @LENGTH@ 1\n");

    }


    @Test
    public void test_489_A_9343123_9343126() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="489-A-9343123-9343126.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if ZA ][d < ZA ][c d = c @TO@ if ZA ][d > ZA ][c d = c @AT@ 252 @LENGTH@ 24\n" +
                "---UPD condition@@ZA ][d < ZA ][c @TO@ ZA ][d > ZA ][c @AT@ 252 @LENGTH@ 16\n" +
                "------UPD expr@@ZA ][d < ZA ][c @TO@ ZA ][d > ZA ][c @AT@ 253 @LENGTH@ 15\n" +
                "---------UPD operator@@< @TO@ > @AT@ 259 @LENGTH@ 1\n");

    }

    @Test
    public void test_143_A_17964626_17964657() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="143-A-17964626-17964657.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for a = 1 a < ( r1 % 10 ) a ++ for i = 1 i <= 1000 i ++ ar ][i = 0 ar ][a = 1 if a >= c1 || a >= d1 continue; b = r1 - a if ar ][b == 1 continue; else ar ][b = 1 if b >= c2 || b >= d2 continue; c = c1 - a if ar ][c == 1 continue; else ar ][c = 1 if c >= r2 || c >= d2 continue; d = d1 - a if ar ][d == 1 continue; if d >= r2 || d >= c2 continue; if b + c != d2 continue; if b + d != c2 continue; if c + d != r2 continue; if a > 9 || b > 9 || c > 9 || d > 9 continue; flag = 1 break; @TO@ for a = 1 a < r1 a ++ for i = 1 i <= 1000 i ++ ar ][i = 0 ar ][a = 1 if a >= c1 || a >= d1 continue; b = r1 - a if ar ][b == 1 continue; else ar ][b = 1 if b >= c2 || b >= d2 continue; c = c1 - a if ar ][c == 1 continue; else ar ][c = 1 if c >= r2 || c >= d2 continue; d = d1 - a if ar ][d == 1 continue; if d >= r2 || d >= c2 continue; if b + c != d2 continue; if b + d != c2 continue; if c + d != r2 continue; if a > 9 || b > 9 || c > 9 || d > 9 continue; flag = 1 break; @AT@ 187 @LENGTH@ 482\n" +
                "---UPD control@@a = 1 a < ( r1 % 10 ) a ++ @TO@ a = 1 a < r1 a ++ @AT@ 187 @LENGTH@ 22\n" +
                "------UPD condition@@a < ( r1 % 10 ) @TO@ a < r1 @AT@ 193 @LENGTH@ 15\n" +
                "---------UPD expr@@a < ( r1 % 10 ) @TO@ a < r1 @AT@ 193 @LENGTH@ 15\n" +
                "------------DEL operator@@( @AT@ 195 @LENGTH@ 1\n" +
                "------------DEL operator@@% @AT@ 198 @LENGTH@ 1\n" +
                "------------DEL literal@@10 @AT@ 199 @LENGTH@ 2\n" +
                "------------DEL operator@@) @AT@ 201 @LENGTH@ 1\n");

    }


    @Test
    public void test_612_A_15750192_15750273() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="612-A-15750192-15750273.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for i = p i <= k i ++ printf \"%c\" a ][i @TO@ for i = p i < k i ++ printf \"%c\" a ][i @AT@ 262 @LENGTH@ 39\n" +
                "---UPD control@@i = p i <= k i ++ @TO@ i = p i < k i ++ @AT@ 262 @LENGTH@ 15\n" +
                "------UPD condition@@i <= k @TO@ i < k @AT@ 267 @LENGTH@ 6\n" +
                "---------UPD expr@@i <= k @TO@ i < k @AT@ 267 @LENGTH@ 6\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 268 @LENGTH@ 2\n");

    }

    @Test
    public void test_31_B_6435804_6435825() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="31-B-6435804-6435825.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for i = 1 i <= strlen s i ++ if n == - 1 if fl == 1 elseif if s ][i == '@' fl = 1 @TO@ for i = 1 i < strlen s i ++ if n == - 1 if fl == 1 elseif if s ][i == '@' fl = 1 @AT@ 225 @LENGTH@ 81\n" +
                "---UPD control@@i = 1 i <= strlen s i ++ @TO@ i = 1 i < strlen s i ++ @AT@ 225 @LENGTH@ 29\n" +
                "------UPD condition@@i <= strlen s @TO@ i < strlen s @AT@ 233 @LENGTH@ 13\n" +
                "---------UPD expr@@i <= strlen s @TO@ i < strlen s @AT@ 233 @LENGTH@ 13\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 235 @LENGTH@ 2\n");

    }

    @Test
    public void test_644_A_18166947_18166954() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="644-A-18166947-18166954.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@for i = 0 i < b i ++ for j = 0 j < b j ++ printf \"%lld \" array ][i ][j printf \"\\n\" @TO@ for i = 0 i < a i ++ for j = 0 j < b j ++ printf \"%lld \" array ][i ][j printf \"\\n\" @AT@ 1251 @LENGTH@ 82\n" +
                "---UPD control@@i = 0 i < b i ++ @TO@ i = 0 i < a i ++ @AT@ 1251 @LENGTH@ 14\n" +
                "------UPD condition@@i < b @TO@ i < a @AT@ 1256 @LENGTH@ 5\n" +
                "---------UPD expr@@i < b @TO@ i < a @AT@ 1256 @LENGTH@ 5\n" +
                "------------UPD name@@b @TO@ a @AT@ 1258 @LENGTH@ 1\n");

    }

    @Test
    public void test_5_B_10350073_10350082() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="5-B-10350073-10350082.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if ! ( ( ml - l ) % 2 ) if right sl += 1 right = 1 - right @TO@ if ( ml - l ) % 2 if right sl += 1 right = 1 - right @AT@ 515 @LENGTH@ 58\n" +
                "---UPD condition@@! ( ( ml - l ) % 2 ) @TO@ ( ml - l ) % 2 @AT@ 515 @LENGTH@ 18\n" +
                "------UPD expr@@! ( ( ml - l ) % 2 ) @TO@ ( ml - l ) % 2 @AT@ 516 @LENGTH@ 20\n" +
                "---------DEL operator@@! @AT@ 516 @LENGTH@ 1\n" +
                "---------DEL operator@@( @AT@ 518 @LENGTH@ 1\n" +
                "---------DEL operator@@) @AT@ 530 @LENGTH@ 1\n");

    }

    @Test
    public void test_675_A_18211752_18211767() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="675-A-18211752-18211767.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"MOV return@@0 @TO@ block@@ @AT@ 242 @LENGTH@ 10\n");

    }

    @Test
    public void test_158_A_18278572_18278586() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="158-A-18278572-18278586.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD while@@while a ][i >= a ][k && i <= n count ++ i ++ @TO@ while a ][i >= a ][k && i <= n && a ][i != 0 count ++ i ++ @AT@ 501 @LENGTH@ 44\n" +
                "---UPD condition@@a ][i >= a ][k && i <= n @TO@ a ][i >= a ][k && i <= n && a ][i != 0 @AT@ 501 @LENGTH@ 19\n" +
                "------UPD expr@@a ][i >= a ][k && i <= n @TO@ a ][i >= a ][k && i <= n && a ][i != 0 @AT@ 502 @LENGTH@ 24\n" +
                "---------INS operator@@&& @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 518 @LENGTH@ 2\n" +
                "---------INS name@@a ][i @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 520 @LENGTH@ 5\n" +
                "------------INS name@@a @TO@ name@@a ][i @AT@ 520 @LENGTH@ 1\n" +
                "------------INS index@@][i @TO@ name@@a ][i @AT@ 522 @LENGTH@ 3\n" +
                "---------------INS expr@@[i @TO@ index@@][i @AT@ 522 @LENGTH@ 2\n" +
                "------------------INS name@@[i @TO@ expr@@[i @AT@ 522 @LENGTH@ 2\n" +
                "---------INS operator@@!= @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 524 @LENGTH@ 2\n" +
                "---------INS literal@@0 @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 526 @LENGTH@ 1\n");

    }

    @Test
    public void test_31_B_136044_136045() throws IOException {

        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="31-B-136044-136045.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"MOV if@@if flag puts ans + 1 else printf \"No solution\\n\" @TO@ block@@long i l flag 0 tot 0 gets str + 1 l = strlen str + 1 if str ][1 == '@' || str ][l == '@' end for i = 1 i <= l - 2 i ++ if str ][i == '@' && ( str ][i + 1 == '@' || str ][i + 2 == '@' ) end for i = 1 i <= l i ++ if flag && str ][i + 1 == '@' ans ][++ tot = ',' if str ][i == '@' flag = 1 ans ][++ tot = str ][i if flag puts ans + 1 else printf \"No solution\\n\" end 0 @AT@ 937 @LENGTH@ 48\n");

    }

    @Test
    public void test_432_A_16886797_16886828() throws IOException {

        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="432-A-16886797-16886828.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();


        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"MOV if@@if z <= 5 - y s ++ @TO@ block@@scanf \"%d\" & z @AT@ 132 @LENGTH@ 18\n");
    }

    @Test
    public void test_507_A_16886367_16886377() throws IOException {
        //TODO macro
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="507-A-16886367-16886377.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@int main int argc char * argv [] int x 0 n 0 s 0 i j k h ][105 0 a ][105 0 c ][105 0 y 0 top 0 scanf \"%d %d\" & n & k FOR i 0 n scanf \"%d\" & c ][i FOR i 0 n a ][i = i + 1 FOR i 0 n FOR j 1 n if c ][j - 1 > c ][j s = c ][j c ][j = c ][j - 1 c ][j - 1 = s s = a ][j a ][j = a ][j - 1 a ][j - 1 = s FOR i 0 n top += c ][i if top > k break; printf \"%d\\n\" i FOR j 0 n printf \"%d \" a ][j 0 @TO@ int main int argc char * argv [] int x 0 n 0 s 0 i j k h ][105 0 a ][105 0 c ][105 0 y 0 top 0 scanf \"%d %d\" & n & k FOR i 0 n scanf \"%d\" & c ][i FOR i 0 n a ][i = i + 1 FOR i 0 n FOR j 1 n if c ][j - 1 > c ][j s = c ][j c ][j = c ][j - 1 c ][j - 1 = s s = a ][j a ][j = a ][j - 1 a ][j - 1 = s FOR i 0 n top += c ][i if top > k break; printf \"%d\\n\" i FOR j 0 i printf \"%d \" a ][j 0 @AT@ 237 @LENGTH@ 382\n" +
                "---UPD block@@int x 0 n 0 s 0 i j k h ][105 0 a ][105 0 c ][105 0 y 0 top 0 scanf \"%d %d\" & n & k FOR i 0 n scanf \"%d\" & c ][i FOR i 0 n a ][i = i + 1 FOR i 0 n FOR j 1 n if c ][j - 1 > c ][j s = c ][j c ][j = c ][j - 1 c ][j - 1 = s s = a ][j a ][j = a ][j - 1 a ][j - 1 = s FOR i 0 n top += c ][i if top > k break; printf \"%d\\n\" i FOR j 0 n printf \"%d \" a ][j 0 @TO@ int x 0 n 0 s 0 i j k h ][105 0 a ][105 0 c ][105 0 y 0 top 0 scanf \"%d %d\" & n & k FOR i 0 n scanf \"%d\" & c ][i FOR i 0 n a ][i = i + 1 FOR i 0 n FOR j 1 n if c ][j - 1 > c ][j s = c ][j c ][j = c ][j - 1 c ][j - 1 = s s = a ][j a ][j = a ][j - 1 a ][j - 1 = s FOR i 0 n top += c ][i if top > k break; printf \"%d\\n\" i FOR j 0 i printf \"%d \" a ][j 0 @AT@ 270 @LENGTH@ 491\n" +
                "------UPD macro@@FOR j 0 n @TO@ FOR j 0 i @AT@ 701 @LENGTH@ 9\n" +
                "---------UPD argument_list@@j 0 n @TO@ j 0 i @AT@ 704 @LENGTH@ 8\n" +
                "------------UPD argument@@n @TO@ i @AT@ 709 @LENGTH@ 1\n");
    }

    @Test
    public void test_25_D_110126_110132() throws IOException {
        //TODO macro
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="25-D-110126-110132.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@int main int argc char * argv [] int i j k l m n int a ][1200 b ][1200 unused ][1200 int ind ][1200 int cnt ][1200 int res_a ][1200 res_b ][1200 res_c ][1200 res_d ][1200 int shima int res bef scanf \"%d\" & n m = n - 1 rep i m scanf \"%d%d\" a + i b + i , a ][i -- , b ][i -- unionInit ind n rep i m unionConnect ind a ][i b ][i shima = 0 rep i m cnt ][unionGet ind i = 1 rep i n shima += cnt ][i res = 0 bef = - 1 rep i n if cnt ][i if bef >= 0 res_c ][res = bef res_d ][res ++ = i bef = i res = 0 rep k m unused ][k = 0 rep k m unionInit ind n rep i m if unused ][i == 0 if i != k unionConnect ind a ][i b ][i rep i n cnt ][i = 0 rep i n cnt ][unionGet ind i = 1 j = 0 rep i n j += cnt ][i if j == shima unused ][k = 1 res_a ][res = a ][k res_b ][res ++ = b ][k printf \"%d\\n\" res rep i res printf \"%d %d %d %d\\n\" res_a ][i + 1 res_b ][i + 1 res_c ][i + 1 res_d ][i + 1 0 @TO@ int main int argc char * argv [] int i j k l m n int a ][1200 b ][1200 unused ][1200 int ind ][1200 int cnt ][1200 int res_a ][1200 res_b ][1200 res_c ][1200 res_d ][1200 int shima int res bef scanf \"%d\" & n m = n - 1 rep i m scanf \"%d%d\" a + i b + i , a ][i -- , b ][i -- unionInit ind n rep i m unionConnect ind a ][i b ][i shima = 0 rep i n cnt ][unionGet ind i = 1 rep i n shima += cnt ][i res = 0 bef = - 1 rep i n if cnt ][i if bef >= 0 res_c ][res = bef res_d ][res ++ = i bef = i res = 0 rep k m unused ][k = 0 rep k m unionInit ind n rep i m if unused ][i == 0 if i != k unionConnect ind a ][i b ][i rep i n cnt ][i = 0 rep i n cnt ][unionGet ind i = 1 j = 0 rep i n j += cnt ][i if j == shima unused ][k = 1 res_a ][res = a ][k res_b ][res ++ = b ][k printf \"%d\\n\" res rep i res printf \"%d %d %d %d\\n\" res_a ][i + 1 res_b ][i + 1 res_c ][i + 1 res_d ][i + 1 0 @AT@ 379 @LENGTH@ 869\n" +
                "---UPD block@@int i j k l m n int a ][1200 b ][1200 unused ][1200 int ind ][1200 int cnt ][1200 int res_a ][1200 res_b ][1200 res_c ][1200 res_d ][1200 int shima int res bef scanf \"%d\" & n m = n - 1 rep i m scanf \"%d%d\" a + i b + i , a ][i -- , b ][i -- unionInit ind n rep i m unionConnect ind a ][i b ][i shima = 0 rep i m cnt ][unionGet ind i = 1 rep i n shima += cnt ][i res = 0 bef = - 1 rep i n if cnt ][i if bef >= 0 res_c ][res = bef res_d ][res ++ = i bef = i res = 0 rep k m unused ][k = 0 rep k m unionInit ind n rep i m if unused ][i == 0 if i != k unionConnect ind a ][i b ][i rep i n cnt ][i = 0 rep i n cnt ][unionGet ind i = 1 j = 0 rep i n j += cnt ][i if j == shima unused ][k = 1 res_a ][res = a ][k res_b ][res ++ = b ][k printf \"%d\\n\" res rep i res printf \"%d %d %d %d\\n\" res_a ][i + 1 res_b ][i + 1 res_c ][i + 1 res_d ][i + 1 0 @TO@ int i j k l m n int a ][1200 b ][1200 unused ][1200 int ind ][1200 int cnt ][1200 int res_a ][1200 res_b ][1200 res_c ][1200 res_d ][1200 int shima int res bef scanf \"%d\" & n m = n - 1 rep i m scanf \"%d%d\" a + i b + i , a ][i -- , b ][i -- unionInit ind n rep i m unionConnect ind a ][i b ][i shima = 0 rep i n cnt ][unionGet ind i = 1 rep i n shima += cnt ][i res = 0 bef = - 1 rep i n if cnt ][i if bef >= 0 res_c ][res = bef res_d ][res ++ = i bef = i res = 0 rep k m unused ][k = 0 rep k m unionInit ind n rep i m if unused ][i == 0 if i != k unionConnect ind a ][i b ][i rep i n cnt ][i = 0 rep i n cnt ][unionGet ind i = 1 j = 0 rep i n j += cnt ][i if j == shima unused ][k = 1 res_a ][res = a ][k res_b ][res ++ = b ][k printf \"%d\\n\" res rep i res printf \"%d %d %d %d\\n\" res_a ][i + 1 res_b ][i + 1 res_c ][i + 1 res_d ][i + 1 0 @AT@ 411 @LENGTH@ 955\n" +
                "------UPD macro@@rep i m @TO@ rep i n @AT@ 746 @LENGTH@ 7\n" +
                "---------UPD argument_list@@i m @TO@ i n @AT@ 749 @LENGTH@ 6\n" +
                "------------UPD argument@@m @TO@ n @AT@ 752 @LENGTH@ 1\n");
    }

    @Test
    public void test_490_A_14580360_14580456() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath","FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename ="490-A-14580360-14580456.c";

        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();



        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@int main int argc char * argv [] int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x elseif if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i 0 @TO@ int main int argc char * argv [] int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i 0 @AT@ 18 @LENGTH@ 435\n" +
                "---UPD block@@int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x elseif if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i 0 @TO@ int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i 0 @AT@ 51 @LENGTH@ 642\n" +
                "------UPD if@@if x < min min = x elseif if y < min min = y @TO@ if x < min min = x @AT@ 507 @LENGTH@ 44\n" +
                "---------DEL elseif@@elseif if y < min min = y @AT@ 548 @LENGTH@ 25\n" +
                "------MOV if@@if y < min min = y @TO@ block@@int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x elseif if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i 0 @AT@ 548 @LENGTH@ 18\n");
    }

    @Test
    public void test_336_A_11394760_11394769() throws IOException {
        //TODO
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename = "336-A-11394760-11394769.c";

        File revFile = new File(root + "revFiles/" + filename);
        File prevFile = new File(root + "prevFiles/prev_" + filename);

        EDiffHunkParser parser = new EDiffHunkParser();


        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "UPD function@@int main int argc char * argv [] ll x y ll zero 0 scanf \"%lld%lld\" & x & y if x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) 0 @TO@ int main int argc char * argv [] ll x y ll zero 0 scanf \"%lld%lld\" & x & y if x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero elseif if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) 0 @AT@ 39 @LENGTH@ 390\n" +
                "---UPD block@@ll x y ll zero 0 scanf \"%lld%lld\" & x & y if x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) 0 @TO@ ll x y ll zero 0 scanf \"%lld%lld\" & x & y if x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero elseif if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) 0 @AT@ 72 @LENGTH@ 493\n" +
                "------DEL if@@if x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero @AT@ 146 @LENGTH@ 72\n" +
                "------UPD if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @TO@ if x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero elseif if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 245 @LENGTH@ 240\n" +
                "---------MOV condition@@x >= 0 && y >= 0 @TO@ if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 146 @LENGTH@ 13\n" +
                "---------MOV then@@printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero @TO@ if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 165 @LENGTH@ 52\n" +
                "---------INS elseif@@elseif if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @TO@ if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y elseif if x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero else printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 250 @LENGTH@ 88\n" +
                "------------INS if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @TO@ elseif@@elseif if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @AT@ 250 @LENGTH@ 81\n" +
                "---------------MOV condition@@x < 0 && y >= 0 @TO@ if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @AT@ 245 @LENGTH@ 12\n" +
                "---------------MOV then@@printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @TO@ if@@if x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @AT@ 263 @LENGTH@ 62\n");
    }

    @Test
    public void test_328_B_4080800_4080805() throws IOException {

        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");
        String root = appProps.getProperty("inputPath");
        root = root + "/codeflaws/";
        String filename = "328-B-4080800-4080805.c";

        File revFile = new File(root + "revFiles/" + filename);
        File prevFile = new File(root + "prevFiles/prev_" + filename);

        EDiffHunkParser parser = new EDiffHunkParser();


        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
        hierarchicalActionSets.size();
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "DEL for@@for i = 0 i < 10 i ++ printf \"%d %d\\n\" num ][i tnum ][i @AT@ 453 @LENGTH@ 55\n" +
                "---DEL control@@i = 0 i < 10 i ++ @AT@ 453 @LENGTH@ 15\n" +
                "------DEL init@@i = 0 @AT@ 454 @LENGTH@ 5\n" +
                "---------DEL expr@@i = 0 @AT@ 454 @LENGTH@ 5\n" +
                "------------DEL name@@i @AT@ 454 @LENGTH@ 1\n" +
                "------------DEL operator@@= @AT@ 455 @LENGTH@ 1\n" +
                "------------DEL literal@@0 @AT@ 456 @LENGTH@ 1\n" +
                "------DEL condition@@i < 10 @AT@ 458 @LENGTH@ 6\n" +
                "---------DEL expr@@i < 10 @AT@ 458 @LENGTH@ 6\n" +
                "------------DEL name@@i @AT@ 458 @LENGTH@ 1\n" +
                "------------DEL operator@@< @AT@ 459 @LENGTH@ 1\n" +
                "------------DEL literal@@10 @AT@ 460 @LENGTH@ 2\n" +
                "------DEL incr@@i ++ @AT@ 463 @LENGTH@ 4\n" +
                "---------DEL expr@@i ++ @AT@ 463 @LENGTH@ 4\n" +
                "------------DEL name@@i @AT@ 463 @LENGTH@ 1\n" +
                "------------DEL operator@@++ @AT@ 464 @LENGTH@ 2\n" +
                "---DEL block@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 467 @LENGTH@ 50\n" +
                "------DEL expr_stmt@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 477 @LENGTH@ 33\n" +
                "---------DEL expr@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 477 @LENGTH@ 33\n" +
                "------------DEL call@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 477 @LENGTH@ 33\n" +
                "---------------DEL name@@printf @AT@ 477 @LENGTH@ 6\n" +
                "---------------DEL argument_list@@\"%d %d\\n\" num ][i tnum ][i @AT@ 483 @LENGTH@ 27\n" +
                "------------------DEL argument@@\"%d %d\\n\" @AT@ 484 @LENGTH@ 9\n" +
                "---------------------DEL expr@@\"%d %d\\n\" @AT@ 484 @LENGTH@ 9\n" +
                "------------------------DEL literal@@\"%d %d\\n\" @AT@ 484 @LENGTH@ 9\n" +
                "------------------DEL argument@@num ][i @AT@ 494 @LENGTH@ 7\n" +
                "---------------------DEL expr@@num ][i @AT@ 494 @LENGTH@ 7\n" +
                "------------------------DEL name@@num ][i @AT@ 494 @LENGTH@ 7\n" +
                "---------------------------DEL name@@num @AT@ 494 @LENGTH@ 3\n" +
                "---------------------------DEL index@@][i @AT@ 498 @LENGTH@ 3\n" +
                "------------------------------DEL expr@@[i @AT@ 498 @LENGTH@ 2\n" +
                "---------------------------------DEL name@@[i @AT@ 498 @LENGTH@ 2\n" +
                "------------------DEL argument@@tnum ][i @AT@ 501 @LENGTH@ 8\n" +
                "---------------------DEL expr@@tnum ][i @AT@ 501 @LENGTH@ 8\n" +
                "------------------------DEL name@@tnum ][i @AT@ 501 @LENGTH@ 8\n" +
                "---------------------------DEL name@@tnum @AT@ 501 @LENGTH@ 4\n" +
                "---------------------------DEL index@@][i @AT@ 506 @LENGTH@ 3\n" +
                "------------------------------DEL expr@@[i @AT@ 506 @LENGTH@ 2\n" +
                "---------------------------------DEL name@@[i @AT@ 506 @LENGTH@ 2\n");
    }




}
