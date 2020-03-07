package edu.lu.uni.serval.fixminer.ediff;

import edu.lu.uni.serval.utils.EDiffHelper;
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

    @Test
    //libtiff_3ecb08_53fc4b_libtiff#tif_jpeg.c
    public void test_libtiff_3ecb08_53fc4b() throws IOException {
        //cpython_b3a601_63d5c1_Objects#unicodeobject.c
        //linux_659d8c_fd2a50a_tools#perf#builtin-kmem.c
        //openssl_6a14fe7_0ff368_crypto#LPdir_win.c
        //linux_d1dc69_cc2115c_sound#soc#codecs#wm8776.c
        //linux_ff244c6_a0917e0_drivers#net#tun.c .
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("libtiff_3ecb08_53fc4b_libtiff#tif_jpeg.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("gmp_bdba4d_c2ebf1_tests#devel#addmul_1.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_92f011_647dac_Python#fileutils.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_0b5faa_596f48_ext#phar#phar_object.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_3d733c_85fcab_ext#curl#streams.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_177c1f_a1a124_ext#pdo#pdo_stmt.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c4ee76_2a1218_ext#ftp#ftp.c");//write test
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_bb5faa_c5152b_TSRM#tsrm_virtual_cwd.c");//write test
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_d4f9cf_1005c8_Modules#_pickle.c");
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_ec042f_9cfc55_ext#libxml#libxml.c");//wrong
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_f989a8_8f8576_ext#zlib#zlib.c");//wrong
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("lighttpd1.4_b6b6ed_0c6a56_src#md5.c");//
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_1ac8e9_072650_tools#lemon#lemon.c");//good
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_1a3f7f_1aa32a_ext#pdo#pdo_dbh.c");//wrong
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_039f42_968952_ext#pdo#pdo_dbh.c");//wrong
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_171fd9_75d681_ext#ereg#ereg.c");//wrong
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_85e8f3_fe9485_ext#dom#php_dom.c");//good
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_9979ac_895e79_ext#pdo#pdo_dbh.c");//good
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_7af331_b7311e_ext#date#php_date.c");//
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_6819ee_555ca8_main#main.c");//
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_671582_f8e322_PC#_msi.c");//

//        List<HierarchicalActionSet> hierarchicalActionSets1 = getHierarchicalActionSets("php-src_fc0349_0ed538_Zend#zend.c");//
//        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_477e2b3_98599a_main#fopen_wrappers.c");//
//        // good
//        String s = EDiffHelper.getShapeTree(hierarchicalActionSets.get(0), false).toStaticHashString();
//        String s1 = EDiffHelper.getShapeTree(hierarchicalActionSets1.get(0), false).toStaticHashString();
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS else@@else  @TO@ if@@if ( code >= 0x80 && code < 0xa0 ) || code > 0xff else @AT@ 31567 @LENGTH@ 5\n" +
                "---INS block@@ @TO@ else@@else  @AT@ 31567 @LENGTH@ 137\n" +
                "------INS if@@if code == 39 || ! quote_style else @TO@ block@@ @AT@ 31581 @LENGTH@ 35\n" +
                "---------MOV else@@else  @TO@ if@@if code == 39 || ! quote_style else @AT@ 31567 @LENGTH@ 5\n" +
                "---------INS condition@@code == 39 || ! quote_style @TO@ if@@if code == 39 || ! quote_style else @AT@ 31581 @LENGTH@ 29\n" +
                "------------INS expr@@code == 39 || ! quote_style @TO@ condition@@code == 39 || ! quote_style @AT@ 31582 @LENGTH@ 27\n" +
                "---------------INS name@@code @TO@ expr@@code == 39 || ! quote_style @AT@ 31582 @LENGTH@ 4\n" +
                "---------------INS operator@@== @TO@ expr@@code == 39 || ! quote_style @AT@ 31587 @LENGTH@ 2\n" +
                "---------------INS literal@@39 @TO@ expr@@code == 39 || ! quote_style @AT@ 31590 @LENGTH@ 2\n" +
                "---------------INS operator@@|| @TO@ expr@@code == 39 || ! quote_style @AT@ 31593 @LENGTH@ 2\n" +
                "---------------INS operator@@! @TO@ expr@@code == 39 || ! quote_style @AT@ 31596 @LENGTH@ 1\n" +
                "---------------INS name@@quote_style @TO@ expr@@code == 39 || ! quote_style @AT@ 31597 @LENGTH@ 11\n" +
                "---------INS then@@ @TO@ if@@if code == 39 || ! quote_style else @AT@ 31610 @LENGTH@ 0\n" +
                "------------INS block@@ @TO@ then@@ @AT@ 31610 @LENGTH@ 41\n" +
                "---------------INS expr_stmt@@invalid_code = 1 @TO@ block@@ @AT@ 31622 @LENGTH@ 16\n" +
                "------------------INS expr@@invalid_code = 1 @TO@ expr_stmt@@invalid_code = 1 @AT@ 31622 @LENGTH@ 16\n" +
                "---------------------INS name@@invalid_code @TO@ expr@@invalid_code = 1 @AT@ 31622 @LENGTH@ 12\n" +
                "---------------------INS operator@@= @TO@ expr@@invalid_code = 1 @AT@ 31635 @LENGTH@ 1\n" +
                "---------------------INS literal@@1 @TO@ expr@@invalid_code = 1 @AT@ 31637 @LENGTH@ 1\n");

    }

    @Test
    public void test_cpython_671582_f8e322() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_671582_f8e322_PC#_msi.c");//
        Assert.assertEquals(hierarchicalActionSets.size(), 4);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@UINT result ( UINT ) _read hf memory cb @TO@ UINT result ( UINT ) _read ( int ) hf memory cb @AT@ 1430 @LENGTH@ 39\n" +
                "---UPD decl@@UINT result ( UINT ) _read hf memory cb @TO@ UINT result ( UINT ) _read ( int ) hf memory cb @AT@ 1430 @LENGTH@ 39\n" +
                "------UPD init@@( UINT ) _read hf memory cb @TO@ ( UINT ) _read ( int ) hf memory cb @AT@ 1444 @LENGTH@ 27\n" +
                "---------UPD expr@@( UINT ) _read hf memory cb @TO@ ( UINT ) _read ( int ) hf memory cb @AT@ 1444 @LENGTH@ 27\n" +
                "------------UPD call@@_read hf memory cb @TO@ _read ( int ) hf memory cb @AT@ 1450 @LENGTH@ 18\n" +
                "---------------UPD argument_list@@hf memory cb @TO@ ( int ) hf memory cb @AT@ 1455 @LENGTH@ 17\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1456 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1456 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1456 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1457 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1460 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD expr_stmt@@static FNFCIWRITE cb_write UINT result = ( UINT ) _write hf memory cb if result != cb * err = errno @TO@ static FNFCIWRITE cb_write UINT result = ( UINT ) _write ( int ) hf memory cb if result != cb * err = errno @AT@ 1539 @LENGTH@ 99\n" +
                "---UPD expr@@static FNFCIWRITE cb_write UINT result = ( UINT ) _write hf memory cb if result != cb * err = errno @TO@ static FNFCIWRITE cb_write UINT result = ( UINT ) _write ( int ) hf memory cb if result != cb * err = errno @AT@ 1539 @LENGTH@ 99\n" +
                "------UPD block@@UINT result = ( UINT ) _write hf memory cb if result != cb * err = errno @TO@ UINT result = ( UINT ) _write ( int ) hf memory cb if result != cb * err = errno @AT@ 1573 @LENGTH@ 72\n" +
                "---------UPD expr@@UINT result = ( UINT ) _write hf memory cb @TO@ UINT result = ( UINT ) _write ( int ) hf memory cb @AT@ 1573 @LENGTH@ 42\n" +
                "------------UPD call@@_write hf memory cb @TO@ _write ( int ) hf memory cb @AT@ 1593 @LENGTH@ 19\n" +
                "---------------UPD argument_list@@hf memory cb @TO@ ( int ) hf memory cb @AT@ 1599 @LENGTH@ 17\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1600 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1600 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1605 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1606 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1609 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD decl_stmt@@int result _close hf @TO@ int result _close ( int ) hf @AT@ 1717 @LENGTH@ 20\n" +
                "---UPD decl@@int result _close hf @TO@ int result _close ( int ) hf @AT@ 1717 @LENGTH@ 20\n" +
                "------UPD init@@_close hf @TO@ _close ( int ) hf @AT@ 1730 @LENGTH@ 9\n" +
                "---------UPD expr@@_close hf @TO@ _close ( int ) hf @AT@ 1730 @LENGTH@ 9\n" +
                "------------UPD call@@_close hf @TO@ _close ( int ) hf @AT@ 1730 @LENGTH@ 9\n" +
                "---------------UPD argument_list@@hf @TO@ ( int ) hf @AT@ 1736 @LENGTH@ 5\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1737 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1737 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1747 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1748 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1751 @LENGTH@ 1");
        Assert.assertEquals(hierarchicalActionSets.get(3).toString(),"UPD expr_stmt@@static FNFCISEEK cb_seek long result = ( long ) _lseek hf dist seektype if result == - 1 * err = errno @TO@ static FNFCISEEK cb_seek long result = ( long ) _lseek ( int ) hf dist seektype if result == - 1 * err = errno @AT@ 1807 @LENGTH@ 102\n" +
                "---UPD expr@@static FNFCISEEK cb_seek long result = ( long ) _lseek hf dist seektype if result == - 1 * err = errno @TO@ static FNFCISEEK cb_seek long result = ( long ) _lseek ( int ) hf dist seektype if result == - 1 * err = errno @AT@ 1807 @LENGTH@ 102\n" +
                "------UPD block@@long result = ( long ) _lseek hf dist seektype if result == - 1 * err = errno @TO@ long result = ( long ) _lseek ( int ) hf dist seektype if result == - 1 * err = errno @AT@ 1839 @LENGTH@ 77\n" +
                "---------UPD expr@@long result = ( long ) _lseek hf dist seektype @TO@ long result = ( long ) _lseek ( int ) hf dist seektype @AT@ 1839 @LENGTH@ 46\n" +
                "------------UPD call@@_lseek hf dist seektype @TO@ _lseek ( int ) hf dist seektype @AT@ 1859 @LENGTH@ 23\n" +
                "---------------UPD argument_list@@hf dist seektype @TO@ ( int ) hf dist seektype @AT@ 1865 @LENGTH@ 21\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1866 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1866 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1881 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1882 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1885 @LENGTH@ 1\n");

    }

    @Test
    public void test_php_src_820983_90ee88() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_820983_90ee88_ext#standard#html.c");//
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS else@@else  @TO@ if@@if ( code >= 0x80 && code < 0xa0 ) || code > 0xff else @AT@ 31567 @LENGTH@ 5\n" +
                "---INS block@@ @TO@ else@@else  @AT@ 31567 @LENGTH@ 137\n" +
                "------INS if@@if code == 39 || ! quote_style else @TO@ block@@ @AT@ 31581 @LENGTH@ 35\n" +
                "---------MOV else@@else  @TO@ if@@if code == 39 || ! quote_style else @AT@ 31567 @LENGTH@ 5\n" +
                "---------INS condition@@code == 39 || ! quote_style @TO@ if@@if code == 39 || ! quote_style else @AT@ 31581 @LENGTH@ 29\n" +
                "------------INS expr@@code == 39 || ! quote_style @TO@ condition@@code == 39 || ! quote_style @AT@ 31582 @LENGTH@ 27\n" +
                "---------------INS name@@code @TO@ expr@@code == 39 || ! quote_style @AT@ 31582 @LENGTH@ 4\n" +
                "---------------INS operator@@== @TO@ expr@@code == 39 || ! quote_style @AT@ 31587 @LENGTH@ 2\n" +
                "---------------INS literal@@39 @TO@ expr@@code == 39 || ! quote_style @AT@ 31590 @LENGTH@ 2\n" +
                "---------------INS operator@@|| @TO@ expr@@code == 39 || ! quote_style @AT@ 31593 @LENGTH@ 2\n" +
                "---------------INS operator@@! @TO@ expr@@code == 39 || ! quote_style @AT@ 31596 @LENGTH@ 1\n" +
                "---------------INS name@@quote_style @TO@ expr@@code == 39 || ! quote_style @AT@ 31597 @LENGTH@ 11\n" +
                "---------INS then@@ @TO@ if@@if code == 39 || ! quote_style else @AT@ 31610 @LENGTH@ 0\n" +
                "------------INS block@@ @TO@ then@@ @AT@ 31610 @LENGTH@ 41\n" +
                "---------------INS expr_stmt@@invalid_code = 1 @TO@ block@@ @AT@ 31622 @LENGTH@ 16\n" +
                "------------------INS expr@@invalid_code = 1 @TO@ expr_stmt@@invalid_code = 1 @AT@ 31622 @LENGTH@ 16\n" +
                "---------------------INS name@@invalid_code @TO@ expr@@invalid_code = 1 @AT@ 31622 @LENGTH@ 12\n" +
                "---------------------INS operator@@= @TO@ expr@@invalid_code = 1 @AT@ 31635 @LENGTH@ 1\n" +
                "---------------------INS literal@@1 @TO@ expr@@invalid_code = 1 @AT@ 31637 @LENGTH@ 1\n");
    }
    @Test
    public void test_wireshark_c3a535_1153ff() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_c3a535_1153ff_print.c");//
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS else@@else  @TO@ if@@if fi -> hfinfo -> id != proto_data @AT@ 13991 @LENGTH@ 5\n" +
                "---INS block@@ @TO@ else@@else  @AT@ 13991 @LENGTH@ 41\n" +
                "------INS expr_stmt@@fputs \"</field>\\n\" pdata -> fh @TO@ block@@ @AT@ 13996 @LENGTH@ 30\n" +
                "---------INS expr@@fputs \"</field>\\n\" pdata -> fh @TO@ expr_stmt@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 30\n" +
                "------------INS call@@fputs \"</field>\\n\" pdata -> fh @TO@ expr@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 30\n" +
                "---------------INS name@@fputs @TO@ call@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 5\n" +
                "---------------INS argument_list@@\"</field>\\n\" pdata -> fh @TO@ call@@fputs \"</field>\\n\" pdata -> fh @AT@ 14001 @LENGTH@ 26\n" +
                "------------------INS argument@@\"</field>\\n\" @TO@ argument_list@@\"</field>\\n\" pdata -> fh @AT@ 14002 @LENGTH@ 12\n" +
                "---------------------INS expr@@\"</field>\\n\" @TO@ argument@@\"</field>\\n\" @AT@ 14002 @LENGTH@ 12\n" +
                "------------------------INS literal@@\"</field>\\n\" @TO@ expr@@\"</field>\\n\" @AT@ 14002 @LENGTH@ 12\n" +
                "------------------INS argument@@pdata -> fh @TO@ argument_list@@\"</field>\\n\" pdata -> fh @AT@ 14016 @LENGTH@ 11\n" +
                "---------------------INS expr@@pdata -> fh @TO@ argument@@pdata -> fh @AT@ 14016 @LENGTH@ 11\n" +
                "------------------------INS name@@pdata -> fh @TO@ expr@@pdata -> fh @AT@ 14016 @LENGTH@ 11\n" +
                "---------------------------INS name@@pdata @TO@ name@@pdata -> fh @AT@ 14016 @LENGTH@ 5\n" +
                "---------------------------INS operator@@-> @TO@ name@@pdata -> fh @AT@ 14021 @LENGTH@ 2\n" +
                "---------------------------INS name@@fh @TO@ name@@pdata -> fh @AT@ 14023 @LENGTH@ 2\n");
    }
    @Test
    public void test_php_src_a10e77_634012() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_a10e77_634012_ext#phar#tar.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS if@@if entry . filename_len == UINT_MAX if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ block@@last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len if read != entry . filename_len efree entry . filename if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR if ( uint ) php_stream_tell fp > totalsize efree entry . filename if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf if read != buf efree entry . filename if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @AT@ 11511 @LENGTH@ 112\n" +
                "---INS condition@@entry . filename_len == UINT_MAX @TO@ if@@if entry . filename_len == UINT_MAX if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11511 @LENGTH@ 33\n" +
                "------INS expr@@entry . filename_len == UINT_MAX @TO@ condition@@entry . filename_len == UINT_MAX @AT@ 11512 @LENGTH@ 32\n" +
                "---------INS name@@entry . filename_len @TO@ expr@@entry . filename_len == UINT_MAX @AT@ 11512 @LENGTH@ 20\n" +
                "------------INS name@@entry @TO@ name@@entry . filename_len @AT@ 11512 @LENGTH@ 5\n" +
                "------------INS operator@@. @TO@ name@@entry . filename_len @AT@ 11517 @LENGTH@ 1\n" +
                "------------INS name@@filename_len @TO@ name@@entry . filename_len @AT@ 11518 @LENGTH@ 12\n" +
                "---------INS operator@@== @TO@ expr@@entry . filename_len == UINT_MAX @AT@ 11531 @LENGTH@ 2\n" +
                "---------INS name@@UINT_MAX @TO@ expr@@entry . filename_len == UINT_MAX @AT@ 11534 @LENGTH@ 8\n" +
                "---INS then@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ if@@if entry . filename_len == UINT_MAX if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11544 @LENGTH@ 76\n" +
                "------INS block@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ then@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11544 @LENGTH@ 224\n" +
                "---------INS if@@if error @TO@ block@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11553 @LENGTH@ 8\n" +
                "------------INS condition@@error @TO@ if@@if error @AT@ 11553 @LENGTH@ 8\n" +
                "---------------INS expr@@error @TO@ condition@@error @AT@ 11554 @LENGTH@ 5\n" +
                "------------------INS name@@error @TO@ expr@@error @AT@ 11554 @LENGTH@ 5\n" +
                "------------INS then@@ @TO@ if@@if error @AT@ 11561 @LENGTH@ 0\n" +
                "---------------INS block@@ @TO@ then@@ @AT@ 11561 @LENGTH@ 110\n" +
                "------------------INS expr_stmt@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ block@@ @AT@ 11568 @LENGTH@ 91\n" +
                "---------------------INS expr@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ expr_stmt@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 91\n" +
                "------------------------INS call@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ expr@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 91\n" +
                "---------------------------INS name@@spprintf @TO@ call@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 8\n" +
                "---------------------------INS argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ call@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11576 @LENGTH@ 88\n" +
                "------------------------------INS argument@@error @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11577 @LENGTH@ 5\n" +
                "---------------------------------INS expr@@error @TO@ argument@@error @AT@ 11577 @LENGTH@ 5\n" +
                "------------------------------------INS name@@error @TO@ expr@@error @AT@ 11577 @LENGTH@ 5\n" +
                "------------------------------INS argument@@4096 @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11584 @LENGTH@ 4\n" +
                "---------------------------------INS expr@@4096 @TO@ argument@@4096 @AT@ 11584 @LENGTH@ 4\n" +
                "------------------------------------INS literal@@4096 @TO@ expr@@4096 @AT@ 11584 @LENGTH@ 4\n" +
                "------------------------------INS argument@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11590 @LENGTH@ 65\n" +
                "---------------------------------INS expr@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @TO@ argument@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @AT@ 11590 @LENGTH@ 65\n" +
                "------------------------------------INS literal@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @TO@ expr@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @AT@ 11590 @LENGTH@ 65\n" +
                "------------------------------INS argument@@fname @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11657 @LENGTH@ 5\n" +
                "---------------------------------INS expr@@fname @TO@ argument@@fname @AT@ 11657 @LENGTH@ 5\n" +
                "------------------------------------INS name@@fname @TO@ expr@@fname @AT@ 11657 @LENGTH@ 5\n" +
                "---------INS expr_stmt@@php_stream_close fp @TO@ block@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11675 @LENGTH@ 19\n" +
                "------------INS expr@@php_stream_close fp @TO@ expr_stmt@@php_stream_close fp @AT@ 11675 @LENGTH@ 19\n" +
                "---------------INS call@@php_stream_close fp @TO@ expr@@php_stream_close fp @AT@ 11675 @LENGTH@ 19\n" +
                "------------------INS name@@php_stream_close @TO@ call@@php_stream_close fp @AT@ 11675 @LENGTH@ 16\n" +
                "------------------INS argument_list@@fp @TO@ call@@php_stream_close fp @AT@ 11691 @LENGTH@ 5\n" +
                "---------------------INS argument@@fp @TO@ argument_list@@fp @AT@ 11692 @LENGTH@ 2\n" +
                "------------------------INS expr@@fp @TO@ argument@@fp @AT@ 11692 @LENGTH@ 2\n" +
                "---------------------------INS name@@fp @TO@ expr@@fp @AT@ 11692 @LENGTH@ 2\n" +
                "---------INS macro@@phar_destroy_phar_data myphar TSRMLS_CC @TO@ block@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11701 @LENGTH@ 39\n" +
                "------------INS name@@phar_destroy_phar_data @TO@ macro@@phar_destroy_phar_data myphar TSRMLS_CC @AT@ 11701 @LENGTH@ 22\n" +
                "------------INS argument_list@@myphar TSRMLS_CC @TO@ macro@@phar_destroy_phar_data myphar TSRMLS_CC @AT@ 11723 @LENGTH@ 19\n" +
                "---------------INS argument@@myphar TSRMLS_CC @TO@ argument_list@@myphar TSRMLS_CC @AT@ 11724 @LENGTH@ 16\n" +
                "---------INS empty_stmt@@ @TO@ block@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11741 @LENGTH@ 0\n" +
                "---------INS return@@FAILURE @TO@ block@@if error php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11747 @LENGTH@ 16\n" +
                "------------INS expr@@FAILURE @TO@ return@@FAILURE @AT@ 11754 @LENGTH@ 7\n" +
                "---------------INS name@@FAILURE @TO@ expr@@FAILURE @AT@ 11754 @LENGTH@ 7\n");
    }
    @Test
    public void test_php_src_c71358_283565() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c71358_283565_main#main.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if@@if ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && strcmp new_value \"syslog\" if PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) if PG open_basedir && php_check_open_basedir new_value TSRMLS_CC @TO@ if ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value && strcmp new_value \"syslog\" if PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) if PG open_basedir && php_check_open_basedir new_value TSRMLS_CC @AT@ 7907 @LENGTH@ 247\n" +
                "---UPD condition@@( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && strcmp new_value \"syslog\" @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value && strcmp new_value \"syslog\" @AT@ 7907 @LENGTH@ 103\n" +
                "------UPD expr@@( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && strcmp new_value \"syslog\" @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value && strcmp new_value \"syslog\" @AT@ 7908 @LENGTH@ 98\n" +
                "---------INS name@@new_value @TO@ expr@@( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && strcmp new_value \"syslog\" @AT@ 7979 @LENGTH@ 9\n" +
                "---------INS operator@@&& @TO@ expr@@( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && strcmp new_value \"syslog\" @AT@ 7989 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD if@@if stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS if PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) FAILURE @TO@ if ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value if PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) FAILURE @AT@ 8472 @LENGTH@ 157\n" +
                "---UPD condition@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value @AT@ 8472 @LENGTH@ 68\n" +
                "------UPD expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value @AT@ 8473 @LENGTH@ 65\n" +
                "---------INS operator@@( @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8484 @LENGTH@ 1\n" +
                "---------INS operator@@) @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8550 @LENGTH@ 1\n" +
                "---------INS operator@@&& @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8552 @LENGTH@ 2\n" +
                "---------INS name@@new_value @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8555 @LENGTH@ 9\n");
    }

    @Test
    public void test_php_src_920358_bd306c() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_920358_bd306c_ext#dom#xpath.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS else@@else  @TO@ if@@if xpathobjp -> type == XPATH_NODESET int i xmlNodeSetPtr nodesetp if NULL == ( nodesetp = xpathobjp -> nodesetval ) xmlXPathFreeObject xpathobjp RETURN_FALSE MAKE_STD_ZVAL retval array_init retval for i = 0 i < nodesetp -> nodeNr i ++ xmlNodePtr node nodesetp -> nodeTab ][i zval * child MAKE_STD_ZVAL child if node -> type == XML_NAMESPACE_DECL xmlNsPtr curns xmlNodePtr nsparent nsparent = node -> _private curns = xmlNewNs NULL node -> name NULL if node -> children if node -> children else node -> type = XML_NAMESPACE_DECL node -> parent = nsparent node -> ns = curns child = php_dom_create_object node &ret NULL child intern TSRMLS_CC add_next_index_zval retval child @AT@ 6427 @LENGTH@ 5\n" +
                "---INS block@@ @TO@ else@@else  @AT@ 6427 @LENGTH@ 21\n" +
                "------INS expr_stmt@@RETURN_FALSE @TO@ block@@ @AT@ 6431 @LENGTH@ 12\n" +
                "---------INS expr@@RETURN_FALSE @TO@ expr_stmt@@RETURN_FALSE @AT@ 6431 @LENGTH@ 12\n" +
                "------------INS name@@RETURN_FALSE @TO@ expr@@RETURN_FALSE @AT@ 6431 @LENGTH@ 12\n");
    }
    @Test
    public void test_php_src_c3c87e_c9c279() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c3c87e_c9c279_ext#zip#php_zip.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS else@@else  @TO@ if@@if stream @AT@ 64780 @LENGTH@ 5\n" +
                "---INS block@@ @TO@ else@@else  @AT@ 64780 @LENGTH@ 21\n" +
                "------INS expr_stmt@@RETURN_FALSE @TO@ block@@ @AT@ 64784 @LENGTH@ 12\n" +
                "---------INS expr@@RETURN_FALSE @TO@ expr_stmt@@RETURN_FALSE @AT@ 64784 @LENGTH@ 12\n" +
                "------------INS name@@RETURN_FALSE @TO@ expr@@RETURN_FALSE @AT@ 64784 @LENGTH@ 12\n");
    }
    @Test
    public void test_php_src_c4ee76_2a1218() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c4ee76_2a1218_ext#ftp#ftp.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ block@@ctx = SSL_CTX_new SSLv23_client_method if ctx == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"failed to create the SSL context\" 0 ftp -> ssl_handle = SSL_new ctx if ftp -> ssl_handle == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"failed to create the SSL handle\" SSL_CTX_free ctx 0 SSL_set_fd ftp -> ssl_handle ftp -> fd if SSL_connect ftp -> ssl_handle <= 0 php_error_docref NULL TSRMLS_CC E_WARNING \"SSL/TLS handshake failed\" SSL_shutdown ftp -> ssl_handle 0 ftp -> ssl_active = 1 if ! ftp -> old_ssl if ! ftp_putcmd ftp \"PBSZ\" \"0\" if ! ftp_getresp ftp if ! ftp_putcmd ftp \"PROT\" \"P\" if ! ftp_getresp ftp ftp -> use_ssl_for_data = ( ftp -> resp >= 200 && ftp -> resp <= 299 ) @AT@ 6309 @LENGTH@ 34\n" +
                "---INS expr@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6309 @LENGTH@ 34\n" +
                "------INS call@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6309 @LENGTH@ 34\n" +
                "---------INS name@@SSL_CTX_set_options @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6309 @LENGTH@ 19\n" +
                "---------INS argument_list@@ctx SSL_OP_ALL @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6328 @LENGTH@ 18\n" +
                "------------INS argument@@ctx @TO@ argument_list@@ctx SSL_OP_ALL @AT@ 6329 @LENGTH@ 3\n" +
                "---------------INS expr@@ctx @TO@ argument@@ctx @AT@ 6329 @LENGTH@ 3\n" +
                "------------------INS name@@ctx @TO@ expr@@ctx @AT@ 6329 @LENGTH@ 3\n" +
                "------------INS argument@@SSL_OP_ALL @TO@ argument_list@@ctx SSL_OP_ALL @AT@ 6334 @LENGTH@ 10\n" +
                "---------------INS expr@@SSL_OP_ALL @TO@ argument@@SSL_OP_ALL @AT@ 6334 @LENGTH@ 10\n" +
                "------------------INS name@@SSL_OP_ALL @TO@ expr@@SSL_OP_ALL @AT@ 6334 @LENGTH@ 10\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"INS expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ block@@ctx = SSL_CTX_new SSLv23_client_method if ctx == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"data_accept: failed to create the SSL context\" 0 data -> ssl_handle = SSL_new ctx if data -> ssl_handle == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"data_accept: failed to create the SSL handle\" SSL_CTX_free ctx 0 SSL_set_fd data -> ssl_handle data -> fd if ftp -> old_ssl if SSL_connect data -> ssl_handle <= 0 php_error_docref NULL TSRMLS_CC E_WARNING \"data_accept: SSL/TLS handshake failed\" SSL_shutdown data -> ssl_handle 0 data -> ssl_active = 1 @AT@ 28312 @LENGTH@ 34\n" +
                "---INS expr@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28312 @LENGTH@ 34\n" +
                "------INS call@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28312 @LENGTH@ 34\n" +
                "---------INS name@@SSL_CTX_set_options @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28312 @LENGTH@ 19\n" +
                "---------INS argument_list@@ctx SSL_OP_ALL @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28331 @LENGTH@ 18\n" +
                "------------INS argument@@ctx @TO@ argument_list@@ctx SSL_OP_ALL @AT@ 28332 @LENGTH@ 3\n" +
                "---------------INS expr@@ctx @TO@ argument@@ctx @AT@ 28332 @LENGTH@ 3\n" +
                "------------------INS name@@ctx @TO@ expr@@ctx @AT@ 28332 @LENGTH@ 3\n" +
                "------------INS argument@@SSL_OP_ALL @TO@ argument_list@@ctx SSL_OP_ALL @AT@ 28337 @LENGTH@ 10\n" +
                "---------------INS expr@@SSL_OP_ALL @TO@ argument@@SSL_OP_ALL @AT@ 28337 @LENGTH@ 10\n" +
                "------------------INS name@@SSL_OP_ALL @TO@ expr@@SSL_OP_ALL @AT@ 28337 @LENGTH@ 10\n");
    }
    @Test
    public void test_php_src_bb5faa_c5152b() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_bb5faa_c5152b_TSRM#tsrm_virtual_cwd.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS expr_stmt@@tsrm_free_alloca tmp use_heap @TO@ block@@ @AT@ 28559 @LENGTH@ 29\n" +
                "---INS expr@@tsrm_free_alloca tmp use_heap @TO@ expr_stmt@@tsrm_free_alloca tmp use_heap @AT@ 28559 @LENGTH@ 29\n" +
                "------INS call@@tsrm_free_alloca tmp use_heap @TO@ expr@@tsrm_free_alloca tmp use_heap @AT@ 28559 @LENGTH@ 29\n" +
                "---------INS name@@tsrm_free_alloca @TO@ call@@tsrm_free_alloca tmp use_heap @AT@ 28559 @LENGTH@ 16\n" +
                "---------INS argument_list@@tmp use_heap @TO@ call@@tsrm_free_alloca tmp use_heap @AT@ 28575 @LENGTH@ 16\n" +
                "------------INS argument@@tmp @TO@ argument_list@@tmp use_heap @AT@ 28576 @LENGTH@ 3\n" +
                "---------------INS expr@@tmp @TO@ argument@@tmp @AT@ 28576 @LENGTH@ 3\n" +
                "------------------INS name@@tmp @TO@ expr@@tmp @AT@ 28576 @LENGTH@ 3\n" +
                "------------INS argument@@use_heap @TO@ argument_list@@tmp use_heap @AT@ 28581 @LENGTH@ 8\n" +
                "---------------INS expr@@use_heap @TO@ argument@@use_heap @AT@ 28581 @LENGTH@ 8\n" +
                "------------------INS name@@use_heap @TO@ expr@@use_heap @AT@ 28581 @LENGTH@ 8\n");
    }
    @Test
    public void test_php_src_4f7339_317bcb() throws IOException {
        //cpython_b3a601_63d5c1_Objects#unicodeobject.c
        //linux_659d8c_fd2a50a_tools#perf#builtin-kmem.c
        //openssl_6a14fe7_0ff368_crypto#LPdir_win.c
        //linux_d1dc69_cc2115c_sound#soc#codecs#wm8776.c
        //linux_ff244c6_a0917e0_drivers#net#tun.c .
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_4f7339_317bcb_ext#standard#dns.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS if@@if ( ll + n ) >= dlen @TO@ block@@n = cp ][ll memcpy tp + ll cp + ll + 1 n add_next_index_stringl entries cp + ll + 1 n 1 ll = ll + n + 1 @AT@ 13114 @LENGTH@ 21\n" +
                "---INS condition@@( ll + n ) >= dlen @TO@ if@@if ( ll + n ) >= dlen @AT@ 13114 @LENGTH@ 19\n" +
                "------INS expr@@( ll + n ) >= dlen @TO@ condition@@( ll + n ) >= dlen @AT@ 13115 @LENGTH@ 18\n" +
                "---------INS operator@@( @TO@ expr@@( ll + n ) >= dlen @AT@ 13115 @LENGTH@ 1\n" +
                "---------INS name@@ll @TO@ expr@@( ll + n ) >= dlen @AT@ 13116 @LENGTH@ 2\n" +
                "---------INS operator@@+ @TO@ expr@@( ll + n ) >= dlen @AT@ 13119 @LENGTH@ 1\n" +
                "---------INS name@@n @TO@ expr@@( ll + n ) >= dlen @AT@ 13121 @LENGTH@ 1\n" +
                "---------INS operator@@) @TO@ expr@@( ll + n ) >= dlen @AT@ 13122 @LENGTH@ 1\n" +
                "---------INS operator@@>= @TO@ expr@@( ll + n ) >= dlen @AT@ 13124 @LENGTH@ 2\n" +
                "---------INS name@@dlen @TO@ expr@@( ll + n ) >= dlen @AT@ 13127 @LENGTH@ 4\n" +
                "---INS then@@ @TO@ if@@if ( ll + n ) >= dlen @AT@ 13133 @LENGTH@ 0\n" +
                "------INS block@@ @TO@ then@@ @AT@ 13133 @LENGTH@ 76\n" +
                "---------INS expr_stmt@@n = dlen - ( ll + 1 ) @TO@ block@@ @AT@ 13181 @LENGTH@ 21\n" +
                "------------INS expr@@n = dlen - ( ll + 1 ) @TO@ expr_stmt@@n = dlen - ( ll + 1 ) @AT@ 13181 @LENGTH@ 21\n" +
                "---------------INS name@@n @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13181 @LENGTH@ 1\n" +
                "---------------INS operator@@= @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13183 @LENGTH@ 1\n" +
                "---------------INS name@@dlen @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13185 @LENGTH@ 4\n" +
                "---------------INS operator@@- @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13190 @LENGTH@ 1\n" +
                "---------------INS operator@@( @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13192 @LENGTH@ 1\n" +
                "---------------INS name@@ll @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13193 @LENGTH@ 2\n" +
                "---------------INS operator@@+ @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13196 @LENGTH@ 1\n" +
                "---------------INS literal@@1 @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13198 @LENGTH@ 1\n" +
                "---------------INS operator@@) @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13199 @LENGTH@ 1\n");

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
