package edu.lu.uni.serval;

import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.richedit.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.EDiffHelper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

@Ignore
public class TestRealCases extends BaseTest {




    @Test
    public void test_287_A_14208510_14208532() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("287-A-14208510-14208532.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block_content@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d == 3 || h == 3 printf \"YES\" 0 @TO@ g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 213 @LENGTH@ 155\n" +
                "---UPD if_stmt@@d == 3 || h == 3 printf \"YES\" 0 @TO@ d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 447 @LENGTH@ 31\n" +
                "------UPD if@@d == 3 || h == 3 printf \"YES\" 0 @TO@ d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 447 @LENGTH@ 31\n" +
                "---------UPD condition@@d == 3 || h == 3 @TO@ d >= 3 || h >= 3 @AT@ 449 @LENGTH@ 16\n" +
                "------------UPD expr@@d == 3 || h == 3 @TO@ d >= 3 || h >= 3 @AT@ 450 @LENGTH@ 16\n" +
                "---------------UPD operator@@== @TO@ >= @AT@ 451 @LENGTH@ 2\n" +
                "---------------UPD operator@@== @TO@ >= @AT@ 459 @LENGTH@ 2\n" +
                "---INS expr_stmt@@d = 0 @TO@ block_content@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d == 3 || h == 3 printf \"YES\" 0 @AT@ 548 @LENGTH@ 5\n" +
                "------INS expr@@d = 0 @TO@ expr_stmt@@d = 0 @AT@ 548 @LENGTH@ 5\n" +
                "---------INS name@@d @TO@ expr@@d = 0 @AT@ 548 @LENGTH@ 1\n" +
                "---------INS operator@@= @TO@ expr@@d = 0 @AT@ 549 @LENGTH@ 1\n" +
                "---------INS literal:number@@0 @TO@ expr@@d = 0 @AT@ 550 @LENGTH@ 1\n" +
                "---INS expr_stmt@@h = 0 @TO@ block_content@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d == 3 || h == 3 printf \"YES\" 0 @AT@ 553 @LENGTH@ 5\n" +
                "------INS expr@@h = 0 @TO@ expr_stmt@@h = 0 @AT@ 553 @LENGTH@ 5\n" +
                "---------INS name@@h @TO@ expr@@h = 0 @AT@ 553 @LENGTH@ 1\n" +
                "---------INS operator@@= @TO@ expr@@h = 0 @AT@ 554 @LENGTH@ 1\n" +
                "---------INS literal:number@@0 @TO@ expr@@h = 0 @AT@ 555 @LENGTH@ 1\n");

    }

//    @Ignore
    @Test
    public void test_287_A_14208521_14208532() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("287-A-14208521-14208532.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@j = 0 j < 3 j ++ g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 @TO@ j = 0 j < 3 j ++ g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 181 @LENGTH@ 172\n" +
                "---UPD block@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 @TO@ g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 199 @LENGTH@ 155\n" +
                "------UPD block_content@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 @TO@ g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 d = 0 h = 0 @AT@ 213 @LENGTH@ 155\n" +
                "---------INS expr_stmt@@d = 0 @TO@ block_content@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 548 @LENGTH@ 5\n" +
                "------------INS expr@@d = 0 @TO@ expr_stmt@@d = 0 @AT@ 548 @LENGTH@ 5\n" +
                "---------------INS name@@d @TO@ expr@@d = 0 @AT@ 548 @LENGTH@ 1\n" +
                "---------------INS operator@@= @TO@ expr@@d = 0 @AT@ 549 @LENGTH@ 1\n" +
                "---------------INS literal:number@@0 @TO@ expr@@d = 0 @AT@ 550 @LENGTH@ 1\n" +
                "---------INS expr_stmt@@h = 0 @TO@ block_content@@g ][i ][j == '.' d ++ h ++ g ][i ][j + 1 == '.' d ++ h ++ g ][i + 1 ][j == '.' d ++ h ++ g ][i + 1 ][j + 1 == '.' d ++ h ++ d >= 3 || h >= 3 printf \"YES\" 0 @AT@ 553 @LENGTH@ 5\n" +
                "------------INS expr@@h = 0 @TO@ expr_stmt@@h = 0 @AT@ 553 @LENGTH@ 5\n" +
                "---------------INS name@@h @TO@ expr@@h = 0 @AT@ 553 @LENGTH@ 1\n" +
                "---------------INS operator@@= @TO@ expr@@h = 0 @AT@ 554 @LENGTH@ 1\n" +
                "---------------INS literal:number@@0 @TO@ expr@@h = 0 @AT@ 555 @LENGTH@ 1\n");

    }

    @Test
    public void test_189_1682083_1682218() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("189-B-1682083-1682218.c");
//        Assert.assertFalse(true);
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@k = MIN k h - j @TO@ t = MIN t h - j @AT@ 254 @LENGTH@ 15\n" +
                "---UPD expr@@k = MIN k h - j @TO@ t = MIN t h - j @AT@ 254 @LENGTH@ 15\n" +
                "------UPD name@@k @TO@ t @AT@ 254 @LENGTH@ 1\n" +
                "------UPD call@@MIN k h - j @TO@ MIN t h - j @AT@ 258 @LENGTH@ 11\n" +
                "---------UPD argument_list@@k h - j @TO@ t h - j @AT@ 261 @LENGTH@ 7\n" +
                "------------UPD argument@@k @TO@ t @AT@ 262 @LENGTH@ 1\n" +
                "---------------UPD expr@@k @TO@ t @AT@ 262 @LENGTH@ 1\n" +
                "------------------UPD name@@k @TO@ t @AT@ 262 @LENGTH@ 1\n");

    }


    @Test
    public void test_177_A2_1594730_1595168() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("177-A2-1594730-1595168.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 mid = a @TO@ i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 mid = a @AT@ 348 @LENGTH@ 48\n" +
                "---UPD if@@i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 mid = a @TO@ i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 mid = a @AT@ 348 @LENGTH@ 48\n" +
                "------UPD condition@@i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 @TO@ i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 @AT@ 350 @LENGTH@ 40\n" +
                "---------UPD expr@@i == ( n - 1 ) / 2 && j == ( n - 1 ) / 2 @TO@ i == ( n + 1 ) / 2 && j == ( n + 1 ) / 2 @AT@ 351 @LENGTH@ 40\n" +
                "------------UPD operator@@- @TO@ + @AT@ 356 @LENGTH@ 1\n" +
                "------------UPD operator@@- @TO@ + @AT@ 370 @LENGTH@ 1\n");

    }

//    @Ignore
    @Test
    public void test_680_A_18343132_18343191() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("680-A-18343132-18343191.c");
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@b ][a ][i == 3 max = 3 * a ][i break; @TO@ b ][a ][i >= 3 max = 3 * a ][i break; @AT@ 174 @LENGTH@ 37\n" +
                "---UPD if@@b ][a ][i == 3 max = 3 * a ][i break; @TO@ b ][a ][i >= 3 max = 3 * a ][i break; @AT@ 174 @LENGTH@ 37\n" +
                "------UPD condition@@b ][a ][i == 3 @TO@ b ][a ][i >= 3 @AT@ 176 @LENGTH@ 14\n" +
                "---------UPD expr@@b ][a ][i == 3 @TO@ b ][a ][i >= 3 @AT@ 177 @LENGTH@ 14\n" +
                "------------UPD operator@@== @TO@ >= @AT@ 184 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD if_stmt@@temp > max max = temp break; @TO@ temp > max max = temp @AT@ 268 @LENGTH@ 28\n" +
                "---UPD if@@temp > max max = temp break; @TO@ temp > max max = temp @AT@ 268 @LENGTH@ 28\n" +
                "------UPD block@@max = temp break; @TO@ max = temp @AT@ 282 @LENGTH@ 17\n" +
                "---------UPD block_content@@max = temp break; @TO@ max = temp @AT@ 285 @LENGTH@ 17\n" +
                "------------DEL break@@break; @AT@ 296 @LENGTH@ 6\n");

    }

    @Test
    public void test_245_D_3671804_3671831() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("245-D-3671804-3671831.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@i != j @TO@ i != j ans |= a @AT@ 183 @LENGTH@ 6\n" +
                "---UPD if@@i != j @TO@ i != j ans |= a @AT@ 183 @LENGTH@ 6\n" +
                "------UPD block@@ @TO@ ans |= a @AT@ 192 @LENGTH@ 0\n" +
                "---------UPD block_content@@ @TO@ ans |= a @AT@ 192 @LENGTH@ 0\n" +
                "------------MOV expr_stmt@@ans |= a @TO@ block_content@@ @AT@ 194 @LENGTH@ 8\n");

    }

    @Test
    public void test_197_B_18221952_18221968() throws IOException {
        //TODO not sure
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("197-B-18221952-18221968.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@a ][0 % i == 0 && b ][0 % i == 0 a ][0 /= i b ][0 /= i @TO@ a ][0 % i == 0 && b ][0 % i == 0 a ][0 /= i b ][0 /= i i -- @AT@ 740 @LENGTH@ 54\n" +
                "---UPD if@@a ][0 % i == 0 && b ][0 % i == 0 a ][0 /= i b ][0 /= i @TO@ a ][0 % i == 0 && b ][0 % i == 0 a ][0 /= i b ][0 /= i i -- @AT@ 740 @LENGTH@ 54\n" +
                "------UPD block@@a ][0 /= i b ][0 /= i @TO@ a ][0 /= i b ][0 /= i i -- @AT@ 779 @LENGTH@ 21\n" +
                "---------UPD block_content@@a ][0 /= i b ][0 /= i @TO@ a ][0 /= i b ][0 /= i i -- @AT@ 797 @LENGTH@ 21\n" +
                "------------INS expr_stmt@@i -- @TO@ block_content@@a ][0 /= i b ][0 /= i @AT@ 831 @LENGTH@ 4\n" +
                "---------------INS expr@@i -- @TO@ expr_stmt@@i -- @AT@ 831 @LENGTH@ 4\n" +
                "------------------INS name@@i @TO@ expr@@i -- @AT@ 831 @LENGTH@ 1\n" +
                "------------------INS operator@@-- @TO@ expr@@i -- @AT@ 832 @LENGTH@ 2\n");

    }

    @Test
    public void test_474_A_15226851_15226912() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("474-A-15226851-15226912.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block_content@@str ][i = s ][j + 1 @TO@ j ++ str ][i = s ][j @AT@ 582 @LENGTH@ 19\n" +
                "---INS expr_stmt@@j ++ @TO@ block_content@@str ][i = s ][j + 1 @AT@ 582 @LENGTH@ 4\n" +
                "------INS expr@@j ++ @TO@ expr_stmt@@j ++ @AT@ 582 @LENGTH@ 4\n" +
                "---------INS name@@j @TO@ expr@@j ++ @AT@ 582 @LENGTH@ 1\n" +
                "---------INS operator@@++ @TO@ expr@@j ++ @AT@ 583 @LENGTH@ 2\n" +
                "---UPD expr_stmt@@str ][i = s ][j + 1 @TO@ str ][i = s ][j @AT@ 582 @LENGTH@ 19\n" +
                "------UPD expr@@str ][i = s ][j + 1 @TO@ str ][i = s ][j @AT@ 582 @LENGTH@ 19\n" +
                "---------UPD name@@s ][j + 1 @TO@ s ][j @AT@ 589 @LENGTH@ 9\n" +
                "------------UPD index@@][j + 1 @TO@ ][j @AT@ 590 @LENGTH@ 7\n" +
                "---------------UPD expr@@[j + 1 @TO@ [j @AT@ 591 @LENGTH@ 6\n" +
                "------------------DEL operator@@+ @AT@ 592 @LENGTH@ 1\n" +
                "------------------DEL literal:number@@1 @AT@ 593 @LENGTH@ 1\n");

    }

    @Ignore
    @Test
    public void test_469_B_8248222_8248281() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("469-B-8248222-8248281.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if temp1 < r && temp2 > l for k = temp1 k <= temp2 k ++ t ][k = 1 @TO@ if temp1 <= r && temp2 >= l for k = temp1 k <= temp2 k ++ t ][k = 1 @AT@ 432 @LENGTH@ 65\n" +
                "---UPD condition@@temp1 < r && temp2 > l @TO@ temp1 <= r && temp2 >= l @AT@ 432 @LENGTH@ 22\n" +
                "------UPD expr@@temp1 < r && temp2 > l @TO@ temp1 <= r && temp2 >= l @AT@ 433 @LENGTH@ 22\n" +
                "---------UPD operator@@< @TO@ <= @AT@ 438 @LENGTH@ 1\n" +
                "---------UPD operator@@> @TO@ >= @AT@ 447 @LENGTH@ 1\n");

    }

    @Test
    public void test_189_B_17295034_17295064() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("189-B-17295034-17295064.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@b = 2 b <= w b += 2 count += ( w - a + 1 ) * ( h - b + 1 ) @TO@ b = 2 b <= h b += 2 count += ( w - a + 1 ) * ( h - b + 1 ) @AT@ 180 @LENGTH@ 58\n" +
                "---UPD control@@b = 2 b <= w b += 2 @TO@ b = 2 b <= h b += 2 @AT@ 183 @LENGTH@ 19\n" +
                "------UPD condition@@b <= w @TO@ b <= h @AT@ 188 @LENGTH@ 6\n" +
                "---------UPD expr@@b <= w @TO@ b <= h @AT@ 188 @LENGTH@ 6\n" +
                "------------UPD name@@w @TO@ h @AT@ 191 @LENGTH@ 1\n");

    }

    @Test
    public void test_244_B_5291533_5291541() throws IOException {
        //TODO not sure
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("244-B-5291533-5291541.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@a <= 101 printf \"%d\\n\" a a == 123 printf \"113\\n\" a == 1000 printf \"352\\n\" a == 1000000000 printf \"40744\\n\" a == 999999999 printf \"40743\\n\" a == 999999998 printf \"40742\\n\" a == 999999997 printf \"40741\\n\" a == 909090901 printf \"38532\\n\" a == 142498040 printf \"21671\\n\" a == 603356456 printf \"31623\\n\" a == 64214872 printf \"15759\\n\" a == 820040584 printf \"36407\\n\" a == 442198 printf \"3071\\n\" a == 642678 printf \"3615\\n\" a == 468390 printf \"3223\\n\" a == 326806 printf \"2759\\n\" a == 940 printf \"331\\n\" a == 356 printf \"175\\n\" a == 132 printf \"114\\n\" a == 102 printf \"101\\n\" @TO@ a <= 101 printf \"%d\\n\" a a == 123 printf \"113\\n\" a == 1000 printf \"352\\n\" a == 1000000000 printf \"40744\\n\" a == 999999999 printf \"40743\\n\" a == 999999998 printf \"40742\\n\" a == 999999997 printf \"40741\\n\" a == 909090901 printf \"38532\\n\" a == 142498040 printf \"21671\\n\" a == 603356456 printf \"31623\\n\" a == 64214872 printf \"15759\\n\" a == 820040584 printf \"36407\\n\" a == 442198 printf \"3071\\n\" a == 784262 printf \"4079\\n\" a == 642678 printf \"3615\\n\" a == 468390 printf \"3223\\n\" a == 326806 printf \"2759\\n\" a == 940 printf \"331\\n\" a == 356 printf \"175\\n\" a == 132 printf \"114\\n\" a == 102 printf \"101\\n\" @AT@ 108 @LENGTH@ 569\n" +
                "---INS if:elseif@@a == 784262 printf \"4079\\n\" @TO@ if_stmt@@a <= 101 printf \"%d\\n\" a a == 123 printf \"113\\n\" a == 1000 printf \"352\\n\" a == 1000000000 printf \"40744\\n\" a == 999999999 printf \"40743\\n\" a == 999999998 printf \"40742\\n\" a == 999999997 printf \"40741\\n\" a == 909090901 printf \"38532\\n\" a == 142498040 printf \"21671\\n\" a == 603356456 printf \"31623\\n\" a == 64214872 printf \"15759\\n\" a == 820040584 printf \"36407\\n\" a == 442198 printf \"3071\\n\" a == 642678 printf \"3615\\n\" a == 468390 printf \"3223\\n\" a == 326806 printf \"2759\\n\" a == 940 printf \"331\\n\" a == 356 printf \"175\\n\" a == 132 printf \"114\\n\" a == 102 printf \"101\\n\" @AT@ 870 @LENGTH@ 27\n" +
                "------INS condition@@a == 784262 @TO@ if:elseif@@a == 784262 printf \"4079\\n\" @AT@ 877 @LENGTH@ 11\n" +
                "---------INS expr@@a == 784262 @TO@ condition@@a == 784262 @AT@ 878 @LENGTH@ 11\n" +
                "------------INS name@@a @TO@ expr@@a == 784262 @AT@ 878 @LENGTH@ 1\n" +
                "------------INS operator@@== @TO@ expr@@a == 784262 @AT@ 879 @LENGTH@ 2\n" +
                "------------INS literal:number@@784262 @TO@ expr@@a == 784262 @AT@ 881 @LENGTH@ 6\n" +
                "------INS block@@printf \"4079\\n\" @TO@ if:elseif@@a == 784262 printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "---------INS block_content@@printf \"4079\\n\" @TO@ block@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "------------INS expr_stmt@@printf \"4079\\n\" @TO@ block_content@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "---------------INS expr@@printf \"4079\\n\" @TO@ expr_stmt@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "------------------INS call@@printf \"4079\\n\" @TO@ expr@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 15\n" +
                "---------------------INS name@@printf @TO@ call@@printf \"4079\\n\" @AT@ 901 @LENGTH@ 6\n" +
                "---------------------INS argument_list@@\"4079\\n\" @TO@ call@@printf \"4079\\n\" @AT@ 907 @LENGTH@ 8\n" +
                "------------------------INS argument@@\"4079\\n\" @TO@ argument_list@@\"4079\\n\" @AT@ 908 @LENGTH@ 8\n" +
                "---------------------------INS expr@@\"4079\\n\" @TO@ argument@@\"4079\\n\" @AT@ 908 @LENGTH@ 8\n" +
                "------------------------------INS literal:string@@\"4079\\n\" @TO@ expr@@\"4079\\n\" @AT@ 908 @LENGTH@ 8\n");

    }
    @Test
    public void test_166_C_1395587_1395933() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("166-C-1395587-1395933.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@array ][( n + 1 ) / 2 == x printf \"0\\n\" last < ( n + 1 ) / 2 printf \"%d\\n\" n - 2 * last first > ( n + 1 ) / 2 printf \"%d\\n\" 2 * first - n - 1 @TO@ array ][( n + 1 ) / 2 - 1 == x printf \"0\\n\" last < ( n + 1 ) / 2 printf \"%d\\n\" n - 2 * last first > ( n + 1 ) / 2 printf \"%d\\n\" 2 * first - n - 1 n == 1 printf \"0\\n\" @AT@ 769 @LENGTH@ 141\n" +
                "---UPD if@@array ][( n + 1 ) / 2 == x printf \"0\\n\" @TO@ array ][( n + 1 ) / 2 - 1 == x printf \"0\\n\" @AT@ 769 @LENGTH@ 39\n" +
                "------UPD condition@@array ][( n + 1 ) / 2 == x @TO@ array ][( n + 1 ) / 2 - 1 == x @AT@ 771 @LENGTH@ 26\n" +
                "---------UPD expr@@array ][( n + 1 ) / 2 == x @TO@ array ][( n + 1 ) / 2 - 1 == x @AT@ 772 @LENGTH@ 26\n" +
                "------------UPD name@@array ][( n + 1 ) / 2 @TO@ array ][( n + 1 ) / 2 - 1 @AT@ 772 @LENGTH@ 21\n" +
                "---------------UPD index@@][( n + 1 ) / 2 @TO@ ][( n + 1 ) / 2 - 1 @AT@ 777 @LENGTH@ 15\n" +
                "------------------UPD expr@@[( n + 1 ) / 2 @TO@ [( n + 1 ) / 2 - 1 @AT@ 778 @LENGTH@ 14\n" +
                "---------------------INS operator@@- @TO@ expr@@[( n + 1 ) / 2 @AT@ 785 @LENGTH@ 1\n" +
                "---------------------INS literal:number@@1 @TO@ expr@@[( n + 1 ) / 2 @AT@ 786 @LENGTH@ 1\n" +
                "---INS if:elseif@@n == 1 printf \"0\\n\" @TO@ if_stmt@@array ][( n + 1 ) / 2 == x printf \"0\\n\" last < ( n + 1 ) / 2 printf \"%d\\n\" n - 2 * last first > ( n + 1 ) / 2 printf \"%d\\n\" 2 * first - n - 1 @AT@ 918 @LENGTH@ 19\n" +
                "------INS condition@@n == 1 @TO@ if:elseif@@n == 1 printf \"0\\n\" @AT@ 925 @LENGTH@ 6\n" +
                "---------INS expr@@n == 1 @TO@ condition@@n == 1 @AT@ 926 @LENGTH@ 6\n" +
                "------------INS name@@n @TO@ expr@@n == 1 @AT@ 926 @LENGTH@ 1\n" +
                "------------INS operator@@== @TO@ expr@@n == 1 @AT@ 927 @LENGTH@ 2\n" +
                "------------INS literal:number@@1 @TO@ expr@@n == 1 @AT@ 929 @LENGTH@ 1\n" +
                "------INS block@@printf \"0\\n\" @TO@ if:elseif@@n == 1 printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "---------INS block_content@@printf \"0\\n\" @TO@ block@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "------------INS expr_stmt@@printf \"0\\n\" @TO@ block_content@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "---------------INS expr@@printf \"0\\n\" @TO@ expr_stmt@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "------------------INS call@@printf \"0\\n\" @TO@ expr@@printf \"0\\n\" @AT@ 932 @LENGTH@ 12\n" +
                "---------------------INS name@@printf @TO@ call@@printf \"0\\n\" @AT@ 932 @LENGTH@ 6\n" +
                "---------------------INS argument_list@@\"0\\n\" @TO@ call@@printf \"0\\n\" @AT@ 938 @LENGTH@ 5\n" +
                "------------------------INS argument@@\"0\\n\" @TO@ argument_list@@\"0\\n\" @AT@ 939 @LENGTH@ 5\n" +
                "---------------------------INS expr@@\"0\\n\" @TO@ argument@@\"0\\n\" @AT@ 939 @LENGTH@ 5\n" +
                "------------------------------INS literal:string@@\"0\\n\" @TO@ expr@@\"0\\n\" @AT@ 939 @LENGTH@ 5\n");

    }

    @Test
    public void test_315_A_6149995_6150754() throws IOException {
        //TODO not sure
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("315-A-6149995-6150754.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@a ][j == t ans -- @TO@ a ][j == t ans -- a ][j = 0 @AT@ 300 @LENGTH@ 17\n" +
                "---UPD if@@a ][j == t ans -- @TO@ a ][j == t ans -- a ][j = 0 @AT@ 300 @LENGTH@ 17\n" +
                "------UPD block@@ans -- @TO@ ans -- a ][j = 0 @AT@ 332 @LENGTH@ 6\n" +
                "---------UPD block_content@@ans -- @TO@ ans -- a ][j = 0 @AT@ 332 @LENGTH@ 6\n" +
                "------------INS expr_stmt@@a ][j = 0 @TO@ block_content@@ans -- @AT@ 377 @LENGTH@ 9\n" +
                "---------------INS expr@@a ][j = 0 @TO@ expr_stmt@@a ][j = 0 @AT@ 377 @LENGTH@ 9\n" +
                "------------------INS name@@a ][j @TO@ expr@@a ][j = 0 @AT@ 377 @LENGTH@ 5\n" +
                "---------------------INS name@@a @TO@ name@@a ][j @AT@ 377 @LENGTH@ 1\n" +
                "---------------------INS index@@][j @TO@ name@@a ][j @AT@ 378 @LENGTH@ 3\n" +
                "------------------------INS expr@@[j @TO@ index@@][j @AT@ 379 @LENGTH@ 2\n" +
                "---------------------------INS name@@[j @TO@ expr@@[j @AT@ 379 @LENGTH@ 2\n" +
                "------------------INS operator@@= @TO@ expr@@a ][j = 0 @AT@ 381 @LENGTH@ 1\n" +
                "------------------INS literal:number@@0 @TO@ expr@@a ][j = 0 @AT@ 382 @LENGTH@ 1\n");

    }

    @Test
    public void test_158_A_18237828_18237840() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("158-A-18237828-18237840.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@ara ][i >= ara ][k - 1 count ++ @TO@ ara ][i >= ara ][k - 1 && ara ][i != 0 count ++ @AT@ 217 @LENGTH@ 31\n" +
                "---UPD if@@ara ][i >= ara ][k - 1 count ++ @TO@ ara ][i >= ara ][k - 1 && ara ][i != 0 count ++ @AT@ 217 @LENGTH@ 31\n" +
                "------UPD condition@@ara ][i >= ara ][k - 1 @TO@ ara ][i >= ara ][k - 1 && ara ][i != 0 @AT@ 219 @LENGTH@ 22\n" +
                "---------UPD expr@@ara ][i >= ara ][k - 1 @TO@ ara ][i >= ara ][k - 1 && ara ][i != 0 @AT@ 220 @LENGTH@ 22\n" +
                "------------INS operator@@&& @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 236 @LENGTH@ 2\n" +
                "------------INS name@@ara ][i @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 239 @LENGTH@ 7\n" +
                "---------------INS name@@ara @TO@ name@@ara ][i @AT@ 239 @LENGTH@ 3\n" +
                "---------------INS index@@][i @TO@ name@@ara ][i @AT@ 242 @LENGTH@ 3\n" +
                "------------------INS expr@@[i @TO@ index@@][i @AT@ 243 @LENGTH@ 2\n" +
                "---------------------INS name@@[i @TO@ expr@@[i @AT@ 243 @LENGTH@ 2\n" +
                "------------INS operator@@!= @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 245 @LENGTH@ 2\n" +
                "------------INS literal:number@@0 @TO@ expr@@ara ][i >= ara ][k - 1 @AT@ 247 @LENGTH@ 1\n");

    }

    @Test
    public void test_405_B_9434593_9434605() throws IOException {
        //TODO not sure
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("405-B-9434593-9434605.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),
                "UPD if_stmt@@input ][0 == '.' count = 1 @TO@ input ][0 == '.' count = 1 i1 = 0 @AT@ 258 @LENGTH@ 26\n" +
                        "---INS else@@i1 = 0 @TO@ if_stmt@@input ][0 == '.' count = 1 @AT@ 288 @LENGTH@ 6\n" +
                        "------INS block@@i1 = 0 @TO@ else@@i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                        "---------INS block_content@@i1 = 0 @TO@ block@@i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                        "------------INS expr_stmt@@i1 = 0 @TO@ block_content@@i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                        "---------------INS expr@@i1 = 0 @TO@ expr_stmt@@i1 = 0 @AT@ 295 @LENGTH@ 6\n" +
                        "------------------INS name@@i1 @TO@ expr@@i1 = 0 @AT@ 295 @LENGTH@ 2\n" +
                        "------------------INS operator@@= @TO@ expr@@i1 = 0 @AT@ 297 @LENGTH@ 1\n" +
                        "------------------INS literal:number@@0 @TO@ expr@@i1 = 0 @AT@ 298 @LENGTH@ 1\n");

    }


    @Test
    public void test_489_A_9343123_9343126() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("489-A-9343123-9343126.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@ZA ][d < ZA ][c d = c @TO@ ZA ][d > ZA ][c d = c @AT@ 249 @LENGTH@ 21\n" +
                "---UPD if@@ZA ][d < ZA ][c d = c @TO@ ZA ][d > ZA ][c d = c @AT@ 249 @LENGTH@ 21\n" +
                "------UPD condition@@ZA ][d < ZA ][c @TO@ ZA ][d > ZA ][c @AT@ 252 @LENGTH@ 15\n" +
                "---------UPD expr@@ZA ][d < ZA ][c @TO@ ZA ][d > ZA ][c @AT@ 253 @LENGTH@ 15\n" +
                "------------UPD operator@@< @TO@ > @AT@ 259 @LENGTH@ 1\n");

    }

    @Test
    public void test_143_A_17964626_17964657() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("143-A-17964626-17964657.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@a = 1 a < ( r1 % 10 ) a ++ i = 1 i <= 1000 i ++ ar ][i = 0 ar ][a = 1 a >= c1 || a >= d1 continue; b = r1 - a ar ][b == 1 continue; ar ][b = 1 b >= c2 || b >= d2 continue; c = c1 - a ar ][c == 1 continue; ar ][c = 1 c >= r2 || c >= d2 continue; d = d1 - a ar ][d == 1 continue; d >= r2 || d >= c2 continue; b + c != d2 continue; b + d != c2 continue; c + d != r2 continue; a > 9 || b > 9 || c > 9 || d > 9 continue; flag = 1 break; @TO@ a = 1 a < r1 a ++ i = 1 i <= 1000 i ++ ar ][i = 0 ar ][a = 1 a >= c1 || a >= d1 continue; b = r1 - a ar ][b == 1 continue; ar ][b = 1 b >= c2 || b >= d2 continue; c = c1 - a ar ][c == 1 continue; ar ][c = 1 c >= r2 || c >= d2 continue; d = d1 - a ar ][d == 1 continue; d >= r2 || d >= c2 continue; b + c != d2 continue; b + d != c2 continue; c + d != r2 continue; a > 9 || b > 9 || c > 9 || d > 9 continue; flag = 1 break; @AT@ 184 @LENGTH@ 431\n" +
                "---UPD control@@a = 1 a < ( r1 % 10 ) a ++ @TO@ a = 1 a < r1 a ++ @AT@ 187 @LENGTH@ 26\n" +
                "------UPD condition@@a < ( r1 % 10 ) @TO@ a < r1 @AT@ 193 @LENGTH@ 15\n" +
                "---------UPD expr@@a < ( r1 % 10 ) @TO@ a < r1 @AT@ 193 @LENGTH@ 15\n" +
                "------------DEL operator@@( @AT@ 195 @LENGTH@ 1\n" +
                "------------DEL operator@@% @AT@ 198 @LENGTH@ 1\n" +
                "------------DEL literal:number@@10 @AT@ 199 @LENGTH@ 2\n" +
                "------------DEL operator@@) @AT@ 201 @LENGTH@ 1\n");

    }


    @Test
    public void test_612_A_15750192_15750273() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("612-A-15750192-15750273.c");

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = p i <= k i ++ printf \"%c\" a ][i @TO@ i = p i < k i ++ printf \"%c\" a ][i @AT@ 259 @LENGTH@ 35\n" +
                "---UPD control@@i = p i <= k i ++ @TO@ i = p i < k i ++ @AT@ 262 @LENGTH@ 17\n" +
                "------UPD condition@@i <= k @TO@ i < k @AT@ 267 @LENGTH@ 6\n" +
                "---------UPD expr@@i <= k @TO@ i < k @AT@ 267 @LENGTH@ 6\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 268 @LENGTH@ 2\n");

    }
    @Test
    public void test_344_A_17290259_17290309() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("344-A-17290259-17290309.c");

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 0 i < n i ++ a ][i + 1 == a ][i d = d + 1 f = f + 1 i = i + 2 @TO@ i = 0 i < n - 1 i ++ a ][i + 1 == a ][i d = d + 1 f = f + 1 @AT@ 155 @LENGTH@ 65\n" +
                "---UPD control@@i = 0 i < n i ++ @TO@ i = 0 i < n - 1 i ++ @AT@ 158 @LENGTH@ 16\n" +
                "------UPD condition@@i < n @TO@ i < n - 1 @AT@ 163 @LENGTH@ 5\n" +
                "---------UPD expr@@i < n @TO@ i < n - 1 @AT@ 163 @LENGTH@ 5\n" +
                "------------INS operator@@- @TO@ expr@@i < n @AT@ 166 @LENGTH@ 1\n" +
                "------------INS literal:number@@1 @TO@ expr@@i < n @AT@ 167 @LENGTH@ 1\n" +
                "---UPD block@@a ][i + 1 == a ][i d = d + 1 f = f + 1 i = i + 2 @TO@ a ][i + 1 == a ][i d = d + 1 f = f + 1 @AT@ 173 @LENGTH@ 48\n" +
                "------UPD block_content@@a ][i + 1 == a ][i d = d + 1 f = f + 1 i = i + 2 @TO@ a ][i + 1 == a ][i d = d + 1 f = f + 1 @AT@ 177 @LENGTH@ 48\n" +
                "---------UPD if_stmt@@a ][i + 1 == a ][i d = d + 1 f = f + 1 i = i + 2 @TO@ a ][i + 1 == a ][i d = d + 1 f = f + 1 @AT@ 177 @LENGTH@ 48\n" +
                "------------UPD else@@f = f + 1 i = i + 2 @TO@ f = f + 1 @AT@ 214 @LENGTH@ 19\n" +
                "---------------UPD block@@f = f + 1 i = i + 2 @TO@ f = f + 1 @AT@ 218 @LENGTH@ 19\n" +
                "------------------UPD block_content@@f = f + 1 i = i + 2 @TO@ f = f + 1 @AT@ 223 @LENGTH@ 19\n" +
                "---------------------DEL expr_stmt@@i = i + 2 @AT@ 233 @LENGTH@ 9\n" +
                "------------------------DEL expr@@i = i + 2 @AT@ 233 @LENGTH@ 9\n" +
                "---------------------------DEL name@@i @AT@ 233 @LENGTH@ 1\n" +
                "---------------------------DEL operator@@= @AT@ 234 @LENGTH@ 1\n" +
                "---------------------------DEL name@@i @AT@ 235 @LENGTH@ 1\n" +
                "---------------------------DEL operator@@+ @AT@ 236 @LENGTH@ 1\n" +
                "---------------------------DEL literal:number@@2 @AT@ 237 @LENGTH@ 1\n");

    }
    @Test
    public void test_452_B_7271987_7272004() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("452-B-7271987-7272004.c");

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block_content@@int i j n m x i_temp 1 scanf \"%d %d\" & n & m n == 0 printf \"0 1\\n\" printf \"0 %d\\n\" m printf \"0 0\\n\" printf \"0 %d\\n\" ( m - 1 ) 0 m == 0 printf \"1 0\\n\" printf \"%d 0\\n\" n printf \"0 0\\n\" printf \"%d 0\\n\" ( n - 1 ) 0 ( m == n ) && ( n == 1 ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" ( m ) 0 m == n m + m * 1.41f > ( 2 * sqrt ( double ) ( m * m + ( m - 1 ) * ( m - 1 ) ) ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" n printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 n < m m + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) ( n - 1 ) * ( n - 1 ) + ( m ) * ( m ) ) printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 printf \"%d %d\\n0 0 \\n0 %d\\n%d 0\\n\" n m m n 0 n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ int i j n m x i_temp 1 scanf \"%d %d\" & n & m n == 0 printf \"0 1\\n\" printf \"0 %d\\n\" m printf \"0 0\\n\" printf \"0 %d\\n\" ( m - 1 ) 0 m == 0 printf \"1 0\\n\" printf \"%d 0\\n\" n printf \"0 0\\n\" printf \"%d 0\\n\" ( n - 1 ) 0 ( m == n ) && ( n == 1 ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" ( m ) 0 m == n m + m * 1.41f > ( 2 * sqrt ( double ) ( m * m + ( m - 1 ) * ( m - 1 ) ) ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" n printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 n < m m + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) ( n - 1 ) * ( n - 1 ) + ( m ) * ( m ) ) printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 printf \"%d %d\\n0 0 \\n0 %d\\n%d 0\\n\" n m m n 0 n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 0 @AT@ 118 @LENGTH@ 989\n" +
                "---UPD if_stmt@@n == 0 printf \"0 1\\n\" printf \"0 %d\\n\" m printf \"0 0\\n\" printf \"0 %d\\n\" ( m - 1 ) 0 m == 0 printf \"1 0\\n\" printf \"%d 0\\n\" n printf \"0 0\\n\" printf \"%d 0\\n\" ( n - 1 ) 0 ( m == n ) && ( n == 1 ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" ( m ) 0 m == n m + m * 1.41f > ( 2 * sqrt ( double ) ( m * m + ( m - 1 ) * ( m - 1 ) ) ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" n printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 n < m m + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) ( n - 1 ) * ( n - 1 ) + ( m ) * ( m ) ) printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 printf \"%d %d\\n0 0 \\n0 %d\\n%d 0\\n\" n m m n 0 n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ n == 0 printf \"0 1\\n\" printf \"0 %d\\n\" m printf \"0 0\\n\" printf \"0 %d\\n\" ( m - 1 ) 0 m == 0 printf \"1 0\\n\" printf \"%d 0\\n\" n printf \"0 0\\n\" printf \"%d 0\\n\" ( n - 1 ) 0 ( m == n ) && ( n == 1 ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" ( m ) 0 m == n m + m * 1.41f > ( 2 * sqrt ( double ) ( m * m + ( m - 1 ) * ( m - 1 ) ) ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" n printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 n < m m + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) ( n - 1 ) * ( n - 1 ) + ( m ) * ( m ) ) printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 printf \"%d %d\\n0 0 \\n0 %d\\n%d 0\\n\" n m m n 0 n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 166 @LENGTH@ 944\n" +
                "------UPD if:elseif@@n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1109 @LENGTH@ 217\n" +
                "---------UPD block@@n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1123 @LENGTH@ 211\n" +
                "------------UPD block_content@@n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1127 @LENGTH@ 211\n" +
                "---------------UPD if_stmt@@n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1127 @LENGTH@ 211\n" +
                "------------------UPD else@@printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1306 @LENGTH@ 43\n" +
                "---------------------UPD block@@printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1313 @LENGTH@ 43\n" +
                "------------------------UPD block_content@@printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @TO@ printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m 0 @AT@ 1318 @LENGTH@ 43\n" +
                "---------------------------UPD expr_stmt@@printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m @TO@ printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m @AT@ 1318 @LENGTH@ 41\n" +
                "------------------------------UPD expr@@printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m @TO@ printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m @AT@ 1318 @LENGTH@ 41\n" +
                "---------------------------------UPD call@@printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m @TO@ printf \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m @AT@ 1318 @LENGTH@ 41\n" +
                "------------------------------------UPD argument_list@@\"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m @TO@ \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" n m n m @AT@ 1324 @LENGTH@ 34\n" +
                "---------------------------------------UPD argument@@\"%d %d\\n0 0 \\n%d \\n0 %d\\n\" @TO@ \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" @AT@ 1325 @LENGTH@ 26\n" +
                "------------------------------------------UPD expr@@\"%d %d\\n0 0 \\n%d \\n0 %d\\n\" @TO@ \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" @AT@ 1325 @LENGTH@ 26\n" +
                "---------------------------------------------UPD literal:string@@\"%d %d\\n0 0 \\n%d \\n0 %d\\n\" @TO@ \"%d %d\\n0 0 \\n%d 0\\n0 %d\\n\" @AT@ 1325 @LENGTH@ 26\n" +
                "---INS return@@0 @TO@ block_content@@int i j n m x i_temp 1 scanf \"%d %d\" & n & m n == 0 printf \"0 1\\n\" printf \"0 %d\\n\" m printf \"0 0\\n\" printf \"0 %d\\n\" ( m - 1 ) 0 m == 0 printf \"1 0\\n\" printf \"%d 0\\n\" n printf \"0 0\\n\" printf \"%d 0\\n\" ( n - 1 ) 0 ( m == n ) && ( n == 1 ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" ( m ) 0 m == n m + m * 1.41f > ( 2 * sqrt ( double ) ( m * m + ( m - 1 ) * ( m - 1 ) ) ) printf \"%d %d\\n\" n m printf \"0 0\\n\" printf \"%d 0\\n\" n printf \"0 %d\\n\" n printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 n < m m + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) ( n - 1 ) * ( n - 1 ) + ( m ) * ( m ) ) printf \"%d %d\\n\" n - 1 m printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"1 0\\n\" 0 printf \"%d %d\\n0 0 \\n0 %d\\n%d 0\\n\" n m m n 0 n > m n + sqrt ( double ) n * n + m * m < ( 2 * sqrt ( double ) n * n + ( m - 1 ) * ( m - 1 ) ) printf \"%d %d\\n\" n m - 1 printf \"0 0\\n\" printf \"%d %d\\n\" n m printf \"0 1\\n\" 0 printf \"%d %d\\n0 0 \\n%d \\n0 %d\\n\" n m n m 0 @AT@ 1385 @LENGTH@ 1\n" +
                "------INS expr@@0 @TO@ return@@0 @AT@ 1392 @LENGTH@ 1\n" +
                "---------INS literal:number@@0 @TO@ expr@@0 @AT@ 1392 @LENGTH@ 1\n");


    }
    @Test
    public void test_158_E_1314159_1314160() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("158-E-1314159-1314160.c");

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@ans = max ans 86401 - dp ][k @TO@ ans = max ans 86400 - dp ][k @AT@ 377 @LENGTH@ 28\n" +
                "---UPD expr@@ans = max ans 86401 - dp ][k @TO@ ans = max ans 86400 - dp ][k @AT@ 377 @LENGTH@ 28\n" +
                "------UPD call@@max ans 86401 - dp ][k @TO@ max ans 86400 - dp ][k @AT@ 383 @LENGTH@ 22\n" +
                "---------UPD argument_list@@ans 86401 - dp ][k @TO@ ans 86400 - dp ][k @AT@ 386 @LENGTH@ 18\n" +
                "------------UPD argument@@86401 - dp ][k @TO@ 86400 - dp ][k @AT@ 393 @LENGTH@ 14\n" +
                "---------------UPD expr@@86401 - dp ][k @TO@ 86400 - dp ][k @AT@ 393 @LENGTH@ 14\n" +
                "------------------UPD literal:number@@86401 @TO@ 86400 @AT@ 393 @LENGTH@ 5\n");

    }
    @Test
    public void test_250_A_2762401_2762408() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("250-A-2762401-2762408.c");

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@folders = ceil negative / 2 @TO@ folders = ceil negative / 2.0 @AT@ 289 @LENGTH@ 27\n" +
                "---UPD expr@@folders = ceil negative / 2 @TO@ folders = ceil negative / 2.0 @AT@ 289 @LENGTH@ 27\n" +
                "------UPD call@@ceil negative / 2 @TO@ ceil negative / 2.0 @AT@ 299 @LENGTH@ 17\n" +
                "---------UPD argument_list@@negative / 2 @TO@ negative / 2.0 @AT@ 303 @LENGTH@ 12\n" +
                "------------UPD argument@@negative / 2 @TO@ negative / 2.0 @AT@ 304 @LENGTH@ 12\n" +
                "---------------UPD expr@@negative / 2 @TO@ negative / 2.0 @AT@ 304 @LENGTH@ 12\n" +
                "------------------UPD literal:number@@2 @TO@ 2.0 @AT@ 313 @LENGTH@ 1\n");

    }

    @Test
    public void test_31_B_6435804_6435825() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("31-B-6435804-6435825.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 1 i <= strlen s i ++ n == - 1 s ][i != '@' n = i printf \"No solution\\n\" ( 0 ) fl == 1 s ][i != '@' fl = 0 flag != 0 jj ++ cc ][jj = ',' j = n j <= i j ++ jj ++ cc ][jj = s ][j j = n j <= i j ++ jj ++ cc ][jj = s ][j flag = 1 n = - 1 kon = i exit = 1 printf \"No solution\\n\" ( 0 ) s ][i == '@' fl = 1 @TO@ i = 1 i < strlen s i ++ n == - 1 s ][i != '@' n = i printf \"No solution\\n\" ( 0 ) fl == 1 s ][i != '@' fl = 0 flag != 0 jj ++ cc ][jj = ',' j = n j <= i j ++ jj ++ cc ][jj = s ][j j = n j <= i j ++ jj ++ cc ][jj = s ][j flag = 1 n = - 1 kon = i exit = 1 printf \"No solution\\n\" ( 0 ) s ][i == '@' fl = 1 @AT@ 221 @LENGTH@ 302\n" +
                "---UPD control@@i = 1 i <= strlen s i ++ @TO@ i = 1 i < strlen s i ++ @AT@ 225 @LENGTH@ 24\n" +
                "------UPD condition@@i <= strlen s @TO@ i < strlen s @AT@ 233 @LENGTH@ 13\n" +
                "---------UPD expr@@i <= strlen s @TO@ i < strlen s @AT@ 233 @LENGTH@ 13\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 235 @LENGTH@ 2\n");

    }

    @Test
    public void test_644_A_18166947_18166954() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("644-A-18166947-18166954.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 0 i < b i ++ j = 0 j < b j ++ printf \"%lld \" array ][i ][j printf \"\\n\" @TO@ i = 0 i < a i ++ j = 0 j < b j ++ printf \"%lld \" array ][i ][j printf \"\\n\" @AT@ 1248 @LENGTH@ 74\n" +
                "---UPD control@@i = 0 i < b i ++ @TO@ i = 0 i < a i ++ @AT@ 1251 @LENGTH@ 16\n" +
                "------UPD condition@@i < b @TO@ i < a @AT@ 1256 @LENGTH@ 5\n" +
                "---------UPD expr@@i < b @TO@ i < a @AT@ 1256 @LENGTH@ 5\n" +
                "------------UPD name@@b @TO@ a @AT@ 1258 @LENGTH@ 1\n");

    }

    @Test
    public void test_5_B_10350073_10350082() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("5-B-10350073-10350082.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@! ( ( ml - l ) % 2 ) right sl += 1 right = 1 - right @TO@ ( ml - l ) % 2 right sl += 1 right = 1 - right @AT@ 512 @LENGTH@ 52\n" +
                "---UPD if@@! ( ( ml - l ) % 2 ) right sl += 1 right = 1 - right @TO@ ( ml - l ) % 2 right sl += 1 right = 1 - right @AT@ 512 @LENGTH@ 52\n" +
                "------UPD condition@@! ( ( ml - l ) % 2 ) @TO@ ( ml - l ) % 2 @AT@ 515 @LENGTH@ 20\n" +
                "---------UPD expr@@! ( ( ml - l ) % 2 ) @TO@ ( ml - l ) % 2 @AT@ 516 @LENGTH@ 20\n" +
                "------------DEL operator@@! @AT@ 516 @LENGTH@ 1\n" +
                "------------DEL operator@@( @AT@ 518 @LENGTH@ 1\n" +
                "------------DEL operator@@) @AT@ 530 @LENGTH@ 1\n");

    }

    @Test
    public void test_675_A_18211752_18211767() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("675-A-18211752-18211767.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@c == 0 a == b printf \"YES\" printf \"NO\" @TO@ c == 0 a == b printf \"YES\" printf \"NO\" 0 @AT@ 106 @LENGTH@ 38\n" +
                "---UPD if@@c == 0 a == b printf \"YES\" printf \"NO\" @TO@ c == 0 a == b printf \"YES\" printf \"NO\" 0 @AT@ 106 @LENGTH@ 38\n" +
                "------UPD block@@a == b printf \"YES\" printf \"NO\" @TO@ a == b printf \"YES\" printf \"NO\" 0 @AT@ 114 @LENGTH@ 31\n" +
                "---------UPD block_content@@a == b printf \"YES\" printf \"NO\" @TO@ a == b printf \"YES\" printf \"NO\" 0 @AT@ 119 @LENGTH@ 31\n" +
                "------------MOV return@@0 @TO@ block_content@@a == b printf \"YES\" printf \"NO\" @AT@ 242 @LENGTH@ 1\n");

    }

    @Test
    public void test_158_A_18278572_18278586() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("158-A-18278572-18278586.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD while@@a ][i >= a ][k && i <= n count ++ i ++ @TO@ a ][i >= a ][k && i <= n && a ][i != 0 count ++ i ++ @AT@ 496 @LENGTH@ 38\n" +
                "---UPD condition@@a ][i >= a ][k && i <= n @TO@ a ][i >= a ][k && i <= n && a ][i != 0 @AT@ 501 @LENGTH@ 24\n" +
                "------UPD expr@@a ][i >= a ][k && i <= n @TO@ a ][i >= a ][k && i <= n && a ][i != 0 @AT@ 502 @LENGTH@ 24\n" +
                "---------INS operator@@&& @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 518 @LENGTH@ 2\n" +
                "---------INS name@@a ][i @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 520 @LENGTH@ 5\n" +
                "------------INS name@@a @TO@ name@@a ][i @AT@ 520 @LENGTH@ 1\n" +
                "------------INS index@@][i @TO@ name@@a ][i @AT@ 521 @LENGTH@ 3\n" +
                "---------------INS expr@@[i @TO@ index@@][i @AT@ 522 @LENGTH@ 2\n" +
                "------------------INS name@@[i @TO@ expr@@[i @AT@ 522 @LENGTH@ 2\n" +
                "---------INS operator@@!= @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 524 @LENGTH@ 2\n" +
                "---------INS literal:number@@0 @TO@ expr@@a ][i >= a ][k && i <= n @AT@ 526 @LENGTH@ 1\n");

    }
    //TODO
    @Ignore
    @Test
    public void test_31_B_136044_136045() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("31-B-136044-136045.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"");
    }

    @Test
    public void test_432_A_16886797_16886828() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("432-A-16886797-16886828.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 0 i < x i ++ scanf \"%d\" & z @TO@ i = 0 i < x i ++ scanf \"%d\" & z z <= 5 - y s ++ @AT@ 93 @LENGTH@ 31\n" +
                "---UPD block@@scanf \"%d\" & z @TO@ scanf \"%d\" & z z <= 5 - y s ++ @AT@ 112 @LENGTH@ 14\n" +
                "------UPD block_content@@scanf \"%d\" & z @TO@ scanf \"%d\" & z z <= 5 - y s ++ @AT@ 112 @LENGTH@ 14\n" +
                "---------MOV if_stmt@@z <= 5 - y s ++ @TO@ block_content@@scanf \"%d\" & z @AT@ 130 @LENGTH@ 15\n");
    }

    @Test
    public void test_507_A_16886367_16886377() throws IOException {
        //TODO macro
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("507-A-16886367-16886377.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD macro@@FOR j 0 n @TO@ FOR j 0 i @AT@ 701 @LENGTH@ 9\n" +
                "---UPD argument_list@@j 0 n @TO@ j 0 i @AT@ 704 @LENGTH@ 5\n" +
                "------UPD argument@@n @TO@ i @AT@ 709 @LENGTH@ 1\n");
    }

    @Test
    public void test_25_D_110126_110132() throws IOException {
        //TODO macro
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("25-D-110126-110132.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD macro@@rep i m @TO@ rep i n @AT@ 746 @LENGTH@ 7\n" +
                "---UPD argument_list@@i m @TO@ i n @AT@ 749 @LENGTH@ 3\n" +
                "------UPD argument@@m @TO@ n @AT@ 752 @LENGTH@ 1\n");
    }


    @Test
    public void test_490_A_14580360_14580456() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("490-A-14580360-14580456.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block@@int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x elseif if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i return 0 @TO@ int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i return 0 @AT@ 51 @LENGTH@ 409\n" +
                "---UPD if@@if x < min min = x elseif if y < min min = y @TO@ if x < min min = x @AT@ 507 @LENGTH@ 44\n" +
                "------DEL elseif@@elseif if y < min min = y @AT@ 548 @LENGTH@ 25\n" +
                "---MOV if@@if y < min min = y @TO@ block@@int n i j k l scanf \"%d\" & n int a ][5005 b ][5005 c ][5005 d ][5005 int w 0 x 0 y 0 for i = 1 , j = 1 , k = 1 , l = 1 i <= n i ++ scanf \"%d\" & a ][i if a ][i == 1 b ][j = i w ++ j ++ elseif if a ][i == 2 c ][k = i x ++ k ++ elseif if a ][i == 3 d ][l = i y ++ l ++ int min w if x < min min = x elseif if y < min min = y printf \"%d\\n\" min for i = 1 i <= min i ++ printf \"%d %d %d\\n\" b ][i c ][i d ][i return 0 @AT@ 548 @LENGTH@ 18\n");
    }

    @Test
    public void test_336_A_11394760_11394769() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("336-A-11394760-11394769.c");
//        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "UPD if_stmt@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @TO@ x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 243 @LENGTH@ 222\n" +
                "---MOV if@@x >= 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" zero x + y x + y zero @TO@ if_stmt@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 144 @LENGTH@ 69\n" +
                "---INS if:elseif@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @TO@ if_stmt@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y x >= 0 && y < 0 printf \"%lld %lld %lld %lld\\n\" zero - ( x - y ) x - y zero printf \"%lld %lld %lld %lld\\n\" - ( - x - y ) zero zero - ( - x - y ) @AT@ 243 @LENGTH@ 78\n" +
                "------MOV condition@@x < 0 && y >= 0 @TO@ if:elseif@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @AT@ 245 @LENGTH@ 15\n" +
                "------MOV block@@printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @TO@ if:elseif@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @AT@ 263 @LENGTH@ 62\n" +
                "---DEL if@@x < 0 && y >= 0 printf \"%lld %lld %lld %lld\\n\" - ( - x + y ) zero zero - x + y @AT@ 243 @LENGTH@ 78\n");
    }


    //10-A-bug-1998522-1998523
    @Test
    public void test_10_A_1998522_1998523() throws IOException {
        //TODO
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("10-A-1998522-1998523.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "UPD while@@-- n scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 @TO@ -- n scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 lr = r @AT@ 218 @LENGTH@ 178\n" +
                "---UPD block@@scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 @TO@ scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 lr = r @AT@ 231 @LENGTH@ 173\n" +
                "------UPD block_content@@scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 @TO@ scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 lr = r @AT@ 235 @LENGTH@ 173\n" +
                "---------INS expr_stmt@@lr = r @TO@ block_content@@scanf \"%d %d\" & l & r e += ( r - l ) * p1 l - lr <= t1 e += ( l - lr ) * p1 e += t1 * p1 l - lr <= t1 + t2 e += ( l - lr - t1 ) * p2 e += t2 * p2 + ( l - lr - t1 - t2 ) * p3 @AT@ 465 @LENGTH@ 6\n" +
                "------------INS expr@@lr = r @TO@ expr_stmt@@lr = r @AT@ 465 @LENGTH@ 6\n" +
                "---------------INS name@@lr @TO@ expr@@lr = r @AT@ 465 @LENGTH@ 2\n" +
                "---------------INS operator@@= @TO@ expr@@lr = r @AT@ 468 @LENGTH@ 1\n" +
                "---------------INS name@@r @TO@ expr@@lr = r @AT@ 470 @LENGTH@ 1\n");
    }
    @Test
    public void test_328_B_4080800_4080805() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("328-B-4080800-4080805.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(), "DEL for@@for i = 0 i < 10 i ++ printf \"%d %d\\n\" num ][i tnum ][i @AT@ 453 @LENGTH@ 55\n" +
                "---DEL control@@i = 0 i < 10 i ++ @AT@ 453 @LENGTH@ 17\n" +
                "------DEL init@@i = 0 @AT@ 454 @LENGTH@ 5\n" +
                "---------DEL expr@@i = 0 @AT@ 454 @LENGTH@ 5\n" +
                "------------DEL name@@i @AT@ 454 @LENGTH@ 1\n" +
                "------------DEL operator@@= @AT@ 455 @LENGTH@ 1\n" +
                "------------DEL literal:number@@0 @AT@ 456 @LENGTH@ 1\n" +
                "------DEL condition@@i < 10 @AT@ 458 @LENGTH@ 6\n" +
                "---------DEL expr@@i < 10 @AT@ 458 @LENGTH@ 6\n" +
                "------------DEL name@@i @AT@ 458 @LENGTH@ 1\n" +
                "------------DEL operator@@< @AT@ 459 @LENGTH@ 1\n" +
                "------------DEL literal:number@@10 @AT@ 460 @LENGTH@ 2\n" +
                "------DEL incr@@i ++ @AT@ 463 @LENGTH@ 4\n" +
                "---------DEL expr@@i ++ @AT@ 463 @LENGTH@ 4\n" +
                "------------DEL name@@i @AT@ 463 @LENGTH@ 1\n" +
                "------------DEL operator@@++ @AT@ 464 @LENGTH@ 2\n" +
                "---DEL block@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 467 @LENGTH@ 33\n" +
                "------DEL expr_stmt@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 477 @LENGTH@ 33\n" +
                "---------DEL expr@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 477 @LENGTH@ 33\n" +
                "------------DEL call@@printf \"%d %d\\n\" num ][i tnum ][i @AT@ 477 @LENGTH@ 33\n" +
                "---------------DEL name@@printf @AT@ 477 @LENGTH@ 6\n" +
                "---------------DEL argument_list@@\"%d %d\\n\" num ][i tnum ][i @AT@ 483 @LENGTH@ 26\n" +
                "------------------DEL argument@@\"%d %d\\n\" @AT@ 484 @LENGTH@ 9\n" +
                "---------------------DEL expr@@\"%d %d\\n\" @AT@ 484 @LENGTH@ 9\n" +
                "------------------------DEL literal:string@@\"%d %d\\n\" @AT@ 484 @LENGTH@ 9\n" +
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

    @Test
    public void test_10_A_2106391_2106405() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("10-A-2106391-2106405.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 1 i < n i ++ @TO@ i = 1 i < n i ++ scanf \"%d %d\" & c & e d = c - b d >= t1 p += t1 * p1 d -= t1 d > 0 p += d * p1 d = 0 d >= t2 p += t2 * p2 d -= t2 d > 0 p += d * p2 d = 0 d > 0 p += d * p3 p += p1 * ( e - c ) a = c b = e @AT@ 275 @LENGTH@ 16\n" +
                "---UPD block@@ @TO@ scanf \"%d %d\" & c & e d = c - b d >= t1 p += t1 * p1 d -= t1 d > 0 p += d * p1 d = 0 d >= t2 p += t2 * p2 d -= t2 d > 0 p += d * p2 d = 0 d > 0 p += d * p3 p += p1 * ( e - c ) a = c b = e @AT@ 291 @LENGTH@ 0\n" +
                "------UPD block_content@@ @TO@ scanf \"%d %d\" & c & e d = c - b d >= t1 p += t1 * p1 d -= t1 d > 0 p += d * p1 d = 0 d >= t2 p += t2 * p2 d -= t2 d > 0 p += d * p2 d = 0 d > 0 p += d * p3 p += p1 * ( e - c ) a = c b = e @AT@ 291 @LENGTH@ 0\n" +
                "---------MOV expr_stmt@@scanf \"%d %d\" & c & e @TO@ block_content@@ @AT@ 309 @LENGTH@ 21\n" +
                "---------MOV expr_stmt@@d = c - b @TO@ block_content@@ @AT@ 347 @LENGTH@ 9\n" +
                "---------MOV if_stmt@@d >= t1 p += t1 * p1 d -= t1 d > 0 p += d * p1 d = 0 @TO@ block_content@@ @AT@ 370 @LENGTH@ 52\n" +
                "---------MOV if_stmt@@d >= t2 p += t2 * p2 d -= t2 d > 0 p += d * p2 d = 0 @TO@ block_content@@ @AT@ 616 @LENGTH@ 52\n" +
                "---------MOV if_stmt@@d > 0 p += d * p3 @TO@ block_content@@ @AT@ 855 @LENGTH@ 17\n" +
                "---------MOV expr_stmt@@p += p1 * ( e - c ) @TO@ block_content@@ @AT@ 888 @LENGTH@ 19\n" +
                "---------MOV expr_stmt@@a = c @TO@ block_content@@ @AT@ 917 @LENGTH@ 5\n" +
                "---------MOV expr_stmt@@b = e @TO@ block_content@@ @AT@ 938 @LENGTH@ 5\n");
    }

    //10-A-4557108-4561236
    @Test
    public void test_10_A_4557108_4561236() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("10-A-4557108-4561236.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@m - t1 >= t2 p += p2 * t2 m > t1 && m < t2 p += p2 * ( m - t1 ) @TO@ m - t1 >= t2 p += p2 * t2 m > t1 && m - t1 < t2 p += p2 * ( m - t1 ) @AT@ 469 @LENGTH@ 63\n" +
                "---UPD if:elseif@@m > t1 && m < t2 p += p2 * ( m - t1 ) @TO@ m > t1 && m - t1 < t2 p += p2 * ( m - t1 ) @AT@ 494 @LENGTH@ 37\n" +
                "------UPD condition@@m > t1 && m < t2 @TO@ m > t1 && m - t1 < t2 @AT@ 502 @LENGTH@ 16\n" +
                "---------UPD expr@@m > t1 && m < t2 @TO@ m > t1 && m - t1 < t2 @AT@ 503 @LENGTH@ 16\n" +
                "------------INS operator@@- @TO@ expr@@m > t1 && m < t2 @AT@ 512 @LENGTH@ 1\n" +
                "------------INS name@@t1 @TO@ expr@@m > t1 && m < t2 @AT@ 513 @LENGTH@ 2\n");
    }

    //10-A-5914564-5914663
    @Test
    public void test_10_A_5914564_5914663() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("10-A-5914564-5914663.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@lr + t1 + t2 <= a power += ( t2 * p2 ) + ( a - lr - t1 - t2 ) * p3 @TO@ lr + t1 + t2 <= a power += ( t2 * p2 ) + ( a - lr - t1 - t2 ) * p3 power += ( a - lr - t1 ) * p2 @AT@ 404 @LENGTH@ 66\n" +
                "---INS else@@power += ( a - lr - t1 ) * p2 @TO@ if_stmt@@lr + t1 + t2 <= a power += ( t2 * p2 ) + ( a - lr - t1 - t2 ) * p3 @AT@ 480 @LENGTH@ 29\n" +
                "------INS block@@power += ( a - lr - t1 ) * p2 @TO@ else@@power += ( a - lr - t1 ) * p2 @AT@ 501 @LENGTH@ 29\n" +
                "---------INS block_content@@power += ( a - lr - t1 ) * p2 @TO@ block@@power += ( a - lr - t1 ) * p2 @AT@ 501 @LENGTH@ 29\n" +
                "------------INS expr_stmt@@power += ( a - lr - t1 ) * p2 @TO@ block_content@@power += ( a - lr - t1 ) * p2 @AT@ 501 @LENGTH@ 29\n" +
                "---------------INS expr@@power += ( a - lr - t1 ) * p2 @TO@ expr_stmt@@power += ( a - lr - t1 ) * p2 @AT@ 501 @LENGTH@ 29\n" +
                "------------------INS name@@power @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 501 @LENGTH@ 5\n" +
                "------------------INS operator@@+= @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 506 @LENGTH@ 2\n" +
                "------------------INS operator@@( @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 508 @LENGTH@ 1\n" +
                "------------------INS name@@a @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 509 @LENGTH@ 1\n" +
                "------------------INS operator@@- @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 510 @LENGTH@ 1\n" +
                "------------------INS name@@lr @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 511 @LENGTH@ 2\n" +
                "------------------INS operator@@- @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 513 @LENGTH@ 1\n" +
                "------------------INS name@@t1 @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 514 @LENGTH@ 2\n" +
                "------------------INS operator@@) @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 516 @LENGTH@ 1\n" +
                "------------------INS operator@@* @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 517 @LENGTH@ 1\n" +
                "------------------INS name@@p2 @TO@ expr@@power += ( a - lr - t1 ) * p2 @AT@ 518 @LENGTH@ 2\n");
    }

    //10-D-1434543-1434549
    @Test
    public void test_10_D_1434543_1434549() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("10-D-1434543-1434549.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS if@@if max == 0 printf \"0\\n\" continue; @TO@ block@@for i = 0 i < la i ++ scanf \"%d\" & a ][i scanf \"%d\" & lb for i = 0 i < lb i ++ scanf \"%d\" & b ][i memset f 0 f for i = 0 i <= 500 i ++ p ][i = - 1 for i = 1 i <= la i ++ k = 0 for j = 1 j <= lb j ++ if b ][j - 1 < a ][i - 1 && f ][j > f ][k k = j if a ][i - 1 == b ][j - 1 && f ][k >= f ][j f ][j = f ][k + 1 p ][j = k max = 0 int t 1 for i = 1 i <= lb i ++ if max < f ][i max = f ][i t = i int k 0 int d ][501 d ][++ k = b ][t - 1 while 1 t = p ][t if t == 0 break; d ][++ k = b ][t - 1 printf \"%d\\n\" k for i = k i > 1 i -- printf \"%d \" d ][i printf \"%d\\n\" d ][1 @AT@ 692 @LENGTH@ 34\n" +
                "---INS condition@@max == 0 @TO@ if@@if max == 0 printf \"0\\n\" continue; @AT@ 692 @LENGTH@ 8\n" +
                "------INS expr@@max == 0 @TO@ condition@@max == 0 @AT@ 693 @LENGTH@ 8\n" +
                "---------INS name@@max @TO@ expr@@max == 0 @AT@ 693 @LENGTH@ 3\n" +
                "---------INS operator@@== @TO@ expr@@max == 0 @AT@ 696 @LENGTH@ 2\n" +
                "---------INS literal:number@@0 @TO@ expr@@max == 0 @AT@ 698 @LENGTH@ 1\n" +
                "---INS then@@printf \"0\\n\" continue; @TO@ if@@if max == 0 printf \"0\\n\" continue; @AT@ 702 @LENGTH@ 22\n" +
                "------INS block@@printf \"0\\n\" continue; @TO@ then@@printf \"0\\n\" continue; @AT@ 702 @LENGTH@ 22\n" +
                "---------INS expr_stmt@@printf \"0\\n\" @TO@ block@@printf \"0\\n\" continue; @AT@ 703 @LENGTH@ 12\n" +
                "------------INS expr@@printf \"0\\n\" @TO@ expr_stmt@@printf \"0\\n\" @AT@ 703 @LENGTH@ 12\n" +
                "---------------INS call@@printf \"0\\n\" @TO@ expr@@printf \"0\\n\" @AT@ 703 @LENGTH@ 12\n" +
                "------------------INS name@@printf @TO@ call@@printf \"0\\n\" @AT@ 703 @LENGTH@ 6\n" +
                "------------------INS argument_list@@\"0\\n\" @TO@ call@@printf \"0\\n\" @AT@ 709 @LENGTH@ 5\n" +
                "---------------------INS argument@@\"0\\n\" @TO@ argument_list@@\"0\\n\" @AT@ 710 @LENGTH@ 5\n" +
                "------------------------INS expr@@\"0\\n\" @TO@ argument@@\"0\\n\" @AT@ 710 @LENGTH@ 5\n" +
                "---------------------------INS literal:string@@\"0\\n\" @TO@ expr@@\"0\\n\" @AT@ 710 @LENGTH@ 5\n" +
                "---------INS continue@@continue; @TO@ block@@printf \"0\\n\" continue; @AT@ 717 @LENGTH@ 9\n");
    }

    //101-A-3317973-3317996
    @Test
    public void test_101_A_3317973_3317996() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("101-A-3317973-3317996.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@sum >= k break; @TO@ sum > k break; @AT@ 856 @LENGTH@ 15\n" +
                "---UPD if@@sum >= k break; @TO@ sum > k break; @AT@ 856 @LENGTH@ 15\n" +
                "------UPD condition@@sum >= k @TO@ sum > k @AT@ 858 @LENGTH@ 8\n" +
                "---------UPD expr@@sum >= k @TO@ sum > k @AT@ 859 @LENGTH@ 8\n" +
                "------------UPD operator@@>= @TO@ > @AT@ 862 @LENGTH@ 2\n");
        HierarchicalActionSet actionSet = hierarchicalActionSets.get(0);
        ITree targetTree = EDiffHelper.getTargets(actionSet,false);
        ITree actionTree = EDiffHelper.getActionTrees(actionSet);
        ITree shapeTree = EDiffHelper.getShapeTree(actionSet,false);
        actionSet.getActionSize();
    }
    //102-A-14574020-14574054
    @Test
    public void test_102_A_14574020_14574054() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("102-A-14574020-14574054.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@min printf \"%ld \\n\" min printf \"%d\" - 1 @TO@ min != LONG_MAX printf \"%ld \\n\" min printf \"%d\" - 1 @AT@ 1079 @LENGTH@ 39\n" +
                "---UPD if@@min printf \"%ld \\n\" min @TO@ min != LONG_MAX printf \"%ld \\n\" min @AT@ 1079 @LENGTH@ 23\n" +
                "------UPD condition@@min @TO@ min != LONG_MAX @AT@ 1082 @LENGTH@ 3\n" +
                "---------UPD expr@@min @TO@ min != LONG_MAX @AT@ 1084 @LENGTH@ 3\n" +
                "------------INS operator@@!= @TO@ expr@@min @AT@ 1087 @LENGTH@ 2\n" +
                "------------INS name@@LONG_MAX @TO@ expr@@min @AT@ 1089 @LENGTH@ 8\n");
    }

    //102-A-9556179-9556185
    @Test
    public void test_102_A_9556179_9556185() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("102-A-9556179-9556185.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@b ][x ][y = 1 @TO@ b ][y ][x = b ][x ][y = 1 @AT@ 244 @LENGTH@ 13\n" +
                "---UPD expr@@b ][x ][y = 1 @TO@ b ][y ][x = b ][x ][y = 1 @AT@ 244 @LENGTH@ 13\n" +
                "------MOV name@@b ][x ][y @TO@ expr@@b ][x ][y = 1 @AT@ 244 @LENGTH@ 9\n" +
                "------INS name@@b ][y ][x @TO@ expr@@b ][x ][y = 1 @AT@ 244 @LENGTH@ 9\n" +
                "---------INS name@@b @TO@ name@@b ][y ][x @AT@ 244 @LENGTH@ 1\n" +
                "---------INS index@@][y @TO@ name@@b ][y ][x @AT@ 245 @LENGTH@ 3\n" +
                "------------INS expr@@[y @TO@ index@@][y @AT@ 246 @LENGTH@ 2\n" +
                "---------------INS name@@[y @TO@ expr@@[y @AT@ 246 @LENGTH@ 2\n" +
                "---------INS index@@][x @TO@ name@@b ][y ][x @AT@ 248 @LENGTH@ 3\n" +
                "------------INS expr@@[x @TO@ index@@][x @AT@ 249 @LENGTH@ 2\n" +
                "---------------INS name@@[x @TO@ expr@@[x @AT@ 249 @LENGTH@ 2\n" +
                "------INS operator@@= @TO@ expr@@b ][x ][y = 1 @AT@ 262 @LENGTH@ 1\n");
    }

    @Test
    public void test_254_B_2751488_2751528() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("254-B-2751488-2751528.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@d <= 0 * m = * m - 1 * d = days ][* m @TO@ * d <= 0 * m = * m - 1 * d = days ][* m @AT@ 195 @LENGTH@ 37\n" +
                "---UPD if@@d <= 0 * m = * m - 1 * d = days ][* m @TO@ * d <= 0 * m = * m - 1 * d = days ][* m @AT@ 195 @LENGTH@ 37\n" +
                "------UPD condition@@d <= 0 @TO@ * d <= 0 @AT@ 197 @LENGTH@ 6\n" +
                "---------UPD expr@@d <= 0 @TO@ * d <= 0 @AT@ 198 @LENGTH@ 6\n" +
                "------------INS operator@@* @TO@ expr@@d <= 0 @AT@ 198 @LENGTH@ 1\n");
    }

    //codeflaws_430-C-6594918-6595299.c
    @Test
    public void test_430_C_6594918_6595299() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("430-C-6594918-6595299.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block_content@@stk ][front ++ = nn -> v v ][nn -> v -> c = 0 cc == 1 val ][nn -> v ++ @TO@ stk ][front ++ = nn -> v v ][nn -> v -> c = 0 val ][nn -> v += val ][stk ][rear @AT@ 1755 @LENGTH@ 70\n" +
                "---DEL if_stmt@@cc == 1 val ][nn -> v ++ @AT@ 1830 @LENGTH@ 24\n" +
                "------DEL if@@cc == 1 val ][nn -> v ++ @AT@ 1830 @LENGTH@ 24\n" +
                "---------DEL condition@@cc == 1 @AT@ 1832 @LENGTH@ 7\n" +
                "------------DEL expr@@cc == 1 @AT@ 1833 @LENGTH@ 7\n" +
                "---------------DEL name@@cc @AT@ 1833 @LENGTH@ 2\n" +
                "---------------DEL operator@@== @AT@ 1835 @LENGTH@ 2\n" +
                "---------------DEL literal:number@@1 @AT@ 1837 @LENGTH@ 1\n" +
                "---------DEL block@@val ][nn -> v ++ @AT@ 1864 @LENGTH@ 16\n" +
                "------------DEL block_content@@val ][nn -> v ++ @AT@ 1864 @LENGTH@ 16\n" +
                "---MOV expr_stmt@@val ][nn -> v ++ @TO@ block_content@@stk ][front ++ = nn -> v v ][nn -> v -> c = 0 cc == 1 val ][nn -> v ++ @AT@ 1864 @LENGTH@ 16\n" +
                "------UPD expr_stmt@@val ][nn -> v ++ @TO@ val ][nn -> v += val ][stk ][rear @AT@ 1864 @LENGTH@ 16\n" +
                "---------UPD expr@@val ][nn -> v ++ @TO@ val ][nn -> v += val ][stk ][rear @AT@ 1864 @LENGTH@ 16\n" +
                "------------INS name@@val ][stk ][rear @TO@ expr@@val ][nn -> v ++ @AT@ 1842 @LENGTH@ 16\n" +
                "---------------INS name@@val @TO@ name@@val ][stk ][rear @AT@ 1842 @LENGTH@ 3\n" +
                "---------------INS index@@][stk ][rear @TO@ name@@val ][stk ][rear @AT@ 1845 @LENGTH@ 12\n" +
                "------------------INS expr@@[stk ][rear @TO@ index@@][stk ][rear @AT@ 1846 @LENGTH@ 11\n" +
                "---------------------INS name@@[stk ][rear @TO@ expr@@[stk ][rear @AT@ 1846 @LENGTH@ 11\n" +
                "------------------------INS name@@[stk @TO@ name@@[stk ][rear @AT@ 1846 @LENGTH@ 4\n" +
                "------------------------INS index@@][rear @TO@ name@@[stk ][rear @AT@ 1849 @LENGTH@ 6\n" +
                "---------------------------INS expr@@[rear @TO@ index@@][rear @AT@ 1850 @LENGTH@ 5\n" +
                "------------------------------INS name@@[rear @TO@ expr@@[rear @AT@ 1850 @LENGTH@ 5\n" +
                "------------UPD operator@@++ @TO@ += @AT@ 1874 @LENGTH@ 2\n");
    }

    @Test
    public void test_496_A_15303159_15303846() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("496-A-15303159-15303846.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 1 i < n - 1 i ++ j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @TO@ i = 1 i < n - 1 i ++ max = 0 j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @AT@ 135 @LENGTH@ 163\n" +
                "---UPD block@@j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @TO@ max = 0 j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @AT@ 153 @LENGTH@ 142\n" +
                "------UPD block_content@@j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @TO@ max = 0 j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @AT@ 155 @LENGTH@ 142\n" +
                "---------INS expr_stmt@@max = 0 @TO@ block_content@@j = 0 j < n - 1 j ++ i == j + 1 && max < a ][j + 2 - a ][j max = a ][j + 2 - a ][j max < a ][j + 1 - a ][j max = a ][j + 1 - a ][j b ][i = max @AT@ 155 @LENGTH@ 7\n" +
                "------------INS expr@@max = 0 @TO@ expr_stmt@@max = 0 @AT@ 155 @LENGTH@ 7\n" +
                "---------------INS name@@max @TO@ expr@@max = 0 @AT@ 155 @LENGTH@ 3\n" +
                "---------------INS operator@@= @TO@ expr@@max = 0 @AT@ 158 @LENGTH@ 1\n" +
                "---------------INS literal:number@@0 @TO@ expr@@max = 0 @AT@ 159 @LENGTH@ 1\n");
    }

    @Test
    public void test_430_B_10625991_10626001() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("430-B-10625991-10626001.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@A ][start == A ][end && ( start && A ][start - 1 == A ][end ) && ( end != n - 1 && A ][start == A ][end + 1 ) count2 = 4 , start -= 2 , end += 2 ( A ][start == A ][end ) && ( start && A ][start - 1 == A ][end ) count2 = 3 , start -= 2 , end += 1 ( A ][start == A ][end ) && ( end != n - 1 && A ][start == A ][end + 1 ) count2 = 3 , start -= 1 , end += 2 @TO@ A ][start == A ][end && ( start && A ][start - 1 == A ][end ) && ( end != n - 1 && A ][start == A ][end + 1 ) count2 = 4 , start -= 2 , end += 2 ( A ][start == A ][end ) && ( start && A ][start - 1 == A ][end ) count2 = 3 , start -= 2 , end += 1 ( A ][start == A ][end ) && ( end != n - 1 && A ][start == A ][end + 1 ) count2 = 3 , start -= 1 , end += 2 break; @AT@ 483 @LENGTH@ 353\n" +
                "---UPD else@@ @TO@ break; @AT@ 794 @LENGTH@ 0\n" +
                "------UPD block@@ @TO@ break; @AT@ 803 @LENGTH@ 0\n" +
                "---------UPD block_content@@ @TO@ break; @AT@ 803 @LENGTH@ 0\n" +
                "------------INS break@@break; @TO@ block_content@@ @AT@ 803 @LENGTH@ 6\n");
    }
    @Test
    public void test_60_A_510615_510619() throws IOException {
        //wrong
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("60-A-510615-510619.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS else@@else break; @TO@ if@@if A ][start == A ][end && ( start && A ][start - 1 == A ][end ) && ( end != n - 1 && A ][start == A ][end + 1 ) count2 = 4 , start -= 2 , end += 2 elseif if ( A ][start == A ][end ) && ( start && A ][start - 1 == A ][end ) count2 = 3 , start -= 2 , end += 1 elseif if ( A ][start == A ][end ) && ( end != n - 1 && A ][start == A ][end + 1 ) count2 = 3 , start -= 1 , end += 2 @AT@ 803 @LENGTH@ 11\n" +
                "---INS block@@break; @TO@ else@@else break; @AT@ 803 @LENGTH@ 6\n" +
                "------INS break@@break; @TO@ block@@break; @AT@ 803 @LENGTH@ 6\n");
    }
    @Test
    public void test_509_B_11349359_11354327() throws IOException {
        //wrong//
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("509-B-11349359-11354327.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@printf \"%d \" j < min 1 j - min + 1 @TO@ printf \"%d \" j <= min 1 j - min @AT@ 355 @LENGTH@ 34\n" +
                "---UPD expr@@printf \"%d \" j < min 1 j - min + 1 @TO@ printf \"%d \" j <= min 1 j - min @AT@ 355 @LENGTH@ 34\n" +
                "------UPD call@@printf \"%d \" j < min 1 j - min + 1 @TO@ printf \"%d \" j <= min 1 j - min @AT@ 355 @LENGTH@ 34\n" +
                "---------UPD argument_list@@\"%d \" j < min 1 j - min + 1 @TO@ \"%d \" j <= min 1 j - min @AT@ 361 @LENGTH@ 27\n" +
                "------------UPD argument@@j < min 1 j - min + 1 @TO@ j <= min 1 j - min @AT@ 368 @LENGTH@ 21\n" +
                "---------------UPD expr@@j < min 1 j - min + 1 @TO@ j <= min 1 j - min @AT@ 368 @LENGTH@ 21\n" +
                "------------------UPD ternary@@j < min 1 j - min + 1 @TO@ j <= min 1 j - min @AT@ 368 @LENGTH@ 21\n" +
                "---------------------UPD condition@@j < min @TO@ j <= min @AT@ 368 @LENGTH@ 7\n" +
                "------------------------UPD expr@@j < min @TO@ j <= min @AT@ 368 @LENGTH@ 7\n" +
                "---------------------------UPD operator@@< @TO@ <= @AT@ 369 @LENGTH@ 1\n" +
                "---------------------UPD else@@j - min + 1 @TO@ j - min @AT@ 378 @LENGTH@ 11\n" +
                "------------------------UPD expr@@j - min + 1 @TO@ j - min @AT@ 380 @LENGTH@ 11\n" +
                "---------------------------DEL operator@@+ @AT@ 385 @LENGTH@ 1\n" +
                "---------------------------DEL literal:number@@1 @AT@ 386 @LENGTH@ 1\n");
    }
    @Test
    public void test_6_C_12776326_12776346() throws IOException {
        //wrong//
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("6-C-12776326-12776346.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@n == 1 printf \"1 0\\n\" @TO@ n == 1 printf \"1 0\\n\" 0 @AT@ 81 @LENGTH@ 21\n" +
                "---UPD if@@n == 1 printf \"1 0\\n\" @TO@ n == 1 printf \"1 0\\n\" 0 @AT@ 81 @LENGTH@ 21\n" +
                "------UPD block@@printf \"1 0\\n\" @TO@ printf \"1 0\\n\" 0 @AT@ 94 @LENGTH@ 14\n" +
                "---------UPD block_content@@printf \"1 0\\n\" @TO@ printf \"1 0\\n\" 0 @AT@ 94 @LENGTH@ 14\n" +
                "------------MOV return@@0 @TO@ block_content@@printf \"1 0\\n\" @AT@ 111 @LENGTH@ 1\n");
    }

    @Test
    public void test_494_A_10139010_10139025() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("494-A-10139010-10139025.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@sum == 0 && h > 0 flag = 0 @TO@ ( sum == 0 && h > 0 ) || sum - h + 1 <= 0 flag = 0 @AT@ 296 @LENGTH@ 26\n" +
                "---UPD if@@sum == 0 && h > 0 flag = 0 @TO@ ( sum == 0 && h > 0 ) || sum - h + 1 <= 0 flag = 0 @AT@ 296 @LENGTH@ 26\n" +
                "------UPD condition@@sum == 0 && h > 0 @TO@ ( sum == 0 && h > 0 ) || sum - h + 1 <= 0 @AT@ 298 @LENGTH@ 17\n" +
                "---------UPD expr@@sum == 0 && h > 0 @TO@ ( sum == 0 && h > 0 ) || sum - h + 1 <= 0 @AT@ 299 @LENGTH@ 17\n" +
                "------------INS operator@@( @TO@ expr@@sum == 0 && h > 0 @AT@ 299 @LENGTH@ 1\n" +
                "------------INS operator@@) @TO@ expr@@sum == 0 && h > 0 @AT@ 313 @LENGTH@ 1\n" +
                "------------INS operator@@|| @TO@ expr@@sum == 0 && h > 0 @AT@ 315 @LENGTH@ 2\n" +
                "------------INS name@@sum @TO@ expr@@sum == 0 && h > 0 @AT@ 318 @LENGTH@ 3\n" +
                "------------INS operator@@- @TO@ expr@@sum == 0 && h > 0 @AT@ 321 @LENGTH@ 1\n" +
                "------------INS name@@h @TO@ expr@@sum == 0 && h > 0 @AT@ 322 @LENGTH@ 1\n" +
                "------------INS operator@@+ @TO@ expr@@sum == 0 && h > 0 @AT@ 323 @LENGTH@ 1\n" +
                "------------INS literal:number@@1 @TO@ expr@@sum == 0 && h > 0 @AT@ 324 @LENGTH@ 1\n" +
                "------------INS operator@@<= @TO@ expr@@sum == 0 && h > 0 @AT@ 325 @LENGTH@ 2\n" +
                "------------INS literal:number@@0 @TO@ expr@@sum == 0 && h > 0 @AT@ 327 @LENGTH@ 1\n");
    }

    @Test
    public void test_248_B_3757114_3757122() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("248-B-3757114-3757122.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@printf \"%3d\\n\" suf ][n % P @TO@ printf \"%.*d\\n\" 3 suf ][n % P @AT@ 334 @LENGTH@ 26\n" +
                "---UPD expr@@printf \"%3d\\n\" suf ][n % P @TO@ printf \"%.*d\\n\" 3 suf ][n % P @AT@ 334 @LENGTH@ 26\n" +
                "------UPD call@@printf \"%3d\\n\" suf ][n % P @TO@ printf \"%.*d\\n\" 3 suf ][n % P @AT@ 334 @LENGTH@ 26\n" +
                "---------UPD argument_list@@\"%3d\\n\" suf ][n % P @TO@ \"%.*d\\n\" 3 suf ][n % P @AT@ 341 @LENGTH@ 19\n" +
                "------------UPD argument@@\"%3d\\n\" @TO@ \"%.*d\\n\" @AT@ 342 @LENGTH@ 7\n" +
                "---------------UPD expr@@\"%3d\\n\" @TO@ \"%.*d\\n\" @AT@ 342 @LENGTH@ 7\n" +
                "------------------UPD literal:string@@\"%3d\\n\" @TO@ \"%.*d\\n\" @AT@ 342 @LENGTH@ 7\n" +
                "------------INS argument@@3 @TO@ argument_list@@\"%3d\\n\" suf ][n % P @AT@ 352 @LENGTH@ 1\n" +
                "---------------INS expr@@3 @TO@ argument@@3 @AT@ 352 @LENGTH@ 1\n" +
                "------------------INS literal:number@@3 @TO@ expr@@3 @AT@ 352 @LENGTH@ 1\n");
    }

    //codeflaws_31-B-14288247-14288278.c
    @Test
    public void test_31_B_14288247_14288278() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("31-B-14288247-14288278.c");
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@* p == '@' p - q < 3 break; q = p @TO@ * p == '@' p - q < 3 break; q = p ms = 1 @AT@ 131 @LENGTH@ 33\n" +
                "---UPD if@@* p == '@' p - q < 3 break; q = p @TO@ * p == '@' p - q < 3 break; q = p ms = 1 @AT@ 131 @LENGTH@ 33\n" +
                "------UPD block@@p - q < 3 break; q = p @TO@ p - q < 3 break; q = p ms = 1 @AT@ 147 @LENGTH@ 22\n" +
                "---------UPD block_content@@p - q < 3 break; q = p @TO@ p - q < 3 break; q = p ms = 1 @AT@ 147 @LENGTH@ 22\n" +
                "------------MOV expr_stmt@@ms = 1 @TO@ block_content@@p - q < 3 break; q = p @AT@ 181 @LENGTH@ 6\n");
    }




}
