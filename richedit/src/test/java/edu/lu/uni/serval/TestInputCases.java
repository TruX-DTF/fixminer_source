package edu.lu.uni.serval;

import edu.lu.uni.serval.richedit.ediff.EDiffHunkParser;
import edu.lu.uni.serval.richedit.ediff.HierarchicalActionSet;
import edu.lu.uni.serval.utils.CallShell;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
@Ignore
public class TestInputCases {


    @Test

    //libtiff_3ecb08_53fc4b_libtiff#tif_jpeg.c
    public void test_libtiff_3ecb08_53fc4b() throws IOException {
          List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_96ba09_755c08_epan#dissectors#packet-gsm_a_rr.c");//wrong

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" @TO@ gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157867 @LENGTH@ 89\n" +
                "---UPD if@@gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" @TO@ gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157867 @LENGTH@ 89\n" +
                "------UPD block@@ @TO@ item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157967 @LENGTH@ 0\n" +
                "---------MOV block_content@@item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @TO@ block@@ @AT@ 157983 @LENGTH@ 152\n");
    }


    @Test
    public void test_wireshark_96ba09_755c08() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_96ba09_755c08_epan#dissectors#packet-gsm_a_rr.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" @TO@ gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157867 @LENGTH@ 89\n" +
                "---UPD if@@gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" @TO@ gsm_rr_csn_HL_flag tvb subtree tvb_len bit_offset ++ \"Priority 2\" \"Present\" \"Not present\" item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157867 @LENGTH@ 89\n" +
                "------UPD block@@ @TO@ item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157967 @LENGTH@ 0\n" +
                "---------UPD block_content@@ @TO@ item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE bit_offset += 3 proto_item_append_text item2 \" for Mobile Identity 2\" @AT@ 157967 @LENGTH@ 0\n" +
                "------------MOV expr_stmt@@item2 = proto_tree_add_bits_item subtree hf_gsm_a_call_prio tvb bit_offset 3 FALSE @TO@ block_content@@ @AT@ 157983 @LENGTH@ 82\n" +
                "------------MOV expr_stmt@@bit_offset += 3 @TO@ block_content@@ @AT@ 158081 @LENGTH@ 15\n" +
                "------------MOV expr_stmt@@proto_item_append_text item2 \" for Mobile Identity 2\" @TO@ block_content@@ @AT@ 158106 @LENGTH@ 53\n");

    }
    @Test
    public void test_php_src_cf4a29_40becd() throws IOException {//todo upd mov with delete (get rid of delete
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_cf4a29_40becd_main#safe_mode.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@mode == CHECKUID_DISALLOW_FILE_NOT_EXISTS php_error E_WARNING \"Unable to access %s\" filename 0 mode == CHECKUID_ALLOW_FILE_NOT_EXISTS php_error E_WARNING \"Unable to access %s\" filename @TO@ mode == CHECKUID_DISALLOW_FILE_NOT_EXISTS php_error E_WARNING \"Unable to access %s\" filename 0 mode == CHECKUID_ALLOW_FILE_NOT_EXISTS php_error E_WARNING \"Unable to access %s\" filename 1 @AT@ 2516 @LENGTH@ 184\n" +
                "---UPD if:elseif@@mode == CHECKUID_ALLOW_FILE_NOT_EXISTS php_error E_WARNING \"Unable to access %s\" filename @TO@ mode == CHECKUID_ALLOW_FILE_NOT_EXISTS php_error E_WARNING \"Unable to access %s\" filename 1 @AT@ 2643 @LENGTH@ 89\n" +
                "------UPD block@@php_error E_WARNING \"Unable to access %s\" filename @TO@ php_error E_WARNING \"Unable to access %s\" filename 1 @AT@ 2696 @LENGTH@ 50\n" +
                "---------UPD block_content@@php_error E_WARNING \"Unable to access %s\" filename @TO@ php_error E_WARNING \"Unable to access %s\" filename 1 @AT@ 2696 @LENGTH@ 50\n" +
                "------------MOV return@@1 @TO@ block_content@@php_error E_WARNING \"Unable to access %s\" filename @AT@ 2756 @LENGTH@ 1\n");
    }
    @Test
    public void test_cpython_1a8501_c47253() throws IOException {//todo wrong
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_1a8501_c47253_Modules#signalmodule.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"");
    }
    @Test
    public void test_cpython_58fb90_c9e435() throws IOException {//TODO definitely wrong
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_58fb90_c9e435_Modules#_stringio.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"");
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"");
    }
    @Test
    public void test_cpython_1c76b7_d6056b() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_1c76b7_d6056b_Modules#faulthandler.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@st == PY_LOCK_ACQUIRED break; @TO@ st == PY_LOCK_ACQUIRED PyThread_release_lock thread . cancel_event break; @AT@ 10644 @LENGTH@ 29\n" +
                "---UPD if@@st == PY_LOCK_ACQUIRED break; @TO@ st == PY_LOCK_ACQUIRED PyThread_release_lock thread . cancel_event break; @AT@ 10644 @LENGTH@ 29\n" +
                "------UPD block@@break; @TO@ PyThread_release_lock thread . cancel_event break; @AT@ 10672 @LENGTH@ 6\n" +
                "---------UPD block_content@@break; @TO@ PyThread_release_lock thread . cancel_event break; @AT@ 10722 @LENGTH@ 6\n" +
                "------------MOV expr_stmt@@PyThread_release_lock thread . cancel_event @TO@ block_content@@break; @AT@ 11259 @LENGTH@ 43\n");
    }
    @Test
    public void test_php_src_acd809_1d3d39() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_acd809_1d3d39_ext#standard#scanf.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD switch@@* ch 'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @TO@ * ch 'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' 'X' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @AT@ 19166 @LENGTH@ 801\n" +
                "---UPD block@@'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @TO@ 'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' 'X' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @AT@ 19179 @LENGTH@ 796\n" +
                "------UPD block_content@@'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @TO@ 'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' 'X' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @AT@ 19184 @LENGTH@ 796\n" +
                "---------INS case@@'X' @TO@ block_content@@'n' ! ( flags & SCAN_SUPPRESS ) numVars zend_uint refcount current = args ][objIndex ++ refcount = ( * current ) -> refcount zval_dtor * current ZVAL_LONG * current ( long ) ( string - baseString ) ( * current ) -> refcount = refcount ( * current ) -> is_ref = 1 add_index_long * return_value objIndex ++ string - baseString nconversions ++ continue; 'd' 'D' op = 'i' base = 10 fn = ( long * ) strtol break; 'i' op = 'i' base = 0 fn = ( long * ) strtol break; 'o' op = 'i' base = 8 fn = ( long * ) strtol break; 'x' op = 'i' base = 16 fn = ( long * ) strtol break; 'u' op = 'i' base = 10 flags |= SCAN_UNSIGNED fn = ( long * ) strtoul break; 'f' 'e' 'E' 'g' op = 'f' break; 's' op = 's' break; 'c' op = 's' flags |= SCAN_NOSKIP 0 == width width = 1 break; '[' op = '[' flags |= SCAN_NOSKIP break; @AT@ 19912 @LENGTH@ 3\n" +
                "------------INS expr@@'X' @TO@ case@@'X' @AT@ 19917 @LENGTH@ 3\n" +
                "---------------INS literal:char@@'X' @TO@ expr@@'X' @AT@ 19917 @LENGTH@ 3\n");
    }
    @Test
    public void test_lighttpd14_f27b8d_4a86d3() throws IOException {//TODO not sure
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("lighttpd1.4_f27b8d_4a86d3_src#connections.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),3);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD switch@@con -> http_status 400 505 207 break; default: con -> http_status = 501 break; @TO@ con -> http_status 400 414 505 207 break; default: con -> http_status = 501 break; @AT@ 8360 @LENGTH@ 78\n" +
                "---UPD block@@400 505 207 break; default: con -> http_status = 501 break; @TO@ 400 414 505 207 break; default: con -> http_status = 501 break; @AT@ 8385 @LENGTH@ 59\n" +
                "------UPD block_content@@400 505 207 break; default: con -> http_status = 501 break; @TO@ 400 414 505 207 break; default: con -> http_status = 501 break; @AT@ 8390 @LENGTH@ 59\n" +
                "---------INS case@@414 @TO@ block_content@@400 505 207 break; default: con -> http_status = 501 break; @AT@ 8421 @LENGTH@ 3\n" +
                "------------INS expr@@414 @TO@ case@@414 @AT@ 8426 @LENGTH@ 3\n" +
                "---------------INS literal:number@@414 @TO@ expr@@414 @AT@ 8426 @LENGTH@ 3\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD expr_stmt@@log_error_write srv __FILE__ __LINE__ \"sd\" \"http-header larger then 64k -> disconnected\" chunkqueue_length cq @TO@ log_error_write srv __FILE__ __LINE__ \"s\" \"oversized request-header -> sending Status 414\" @AT@ 23540 @LENGTH@ 109\n" +
                "---UPD expr@@log_error_write srv __FILE__ __LINE__ \"sd\" \"http-header larger then 64k -> disconnected\" chunkqueue_length cq @TO@ log_error_write srv __FILE__ __LINE__ \"s\" \"oversized request-header -> sending Status 414\" @AT@ 23540 @LENGTH@ 109\n" +
                "------UPD call@@log_error_write srv __FILE__ __LINE__ \"sd\" \"http-header larger then 64k -> disconnected\" chunkqueue_length cq @TO@ log_error_write srv __FILE__ __LINE__ \"s\" \"oversized request-header -> sending Status 414\" @AT@ 23540 @LENGTH@ 109\n" +
                "---------UPD argument_list@@srv __FILE__ __LINE__ \"sd\" \"http-header larger then 64k -> disconnected\" chunkqueue_length cq @TO@ srv __FILE__ __LINE__ \"s\" \"oversized request-header -> sending Status 414\" @AT@ 23555 @LENGTH@ 93\n" +
                "------------UPD argument@@\"sd\" @TO@ \"s\" @AT@ 23581 @LENGTH@ 4\n" +
                "---------------UPD expr@@\"sd\" @TO@ \"s\" @AT@ 23581 @LENGTH@ 4\n" +
                "------------------UPD literal:string@@\"sd\" @TO@ \"s\" @AT@ 23581 @LENGTH@ 4\n" +
                "------------UPD argument@@\"http-header larger then 64k -> disconnected\" @TO@ \"oversized request-header -> sending Status 414\" @AT@ 23587 @LENGTH@ 45\n" +
                "---------------UPD expr@@\"http-header larger then 64k -> disconnected\" @TO@ \"oversized request-header -> sending Status 414\" @AT@ 23587 @LENGTH@ 45\n" +
                "------------------UPD literal:string@@\"http-header larger then 64k -> disconnected\" @TO@ \"oversized request-header -> sending Status 414\" @AT@ 23587 @LENGTH@ 45\n" +
                "------------DEL argument@@chunkqueue_length cq @AT@ 23634 @LENGTH@ 20\n" +
                "---------------DEL expr@@chunkqueue_length cq @AT@ 23634 @LENGTH@ 20\n" +
                "------------------DEL call@@chunkqueue_length cq @AT@ 23634 @LENGTH@ 20\n" +
                "---------------------DEL name@@chunkqueue_length @AT@ 23634 @LENGTH@ 17\n" +
                "---------------------DEL argument_list@@cq @AT@ 23651 @LENGTH@ 2\n" +
                "------------------------DEL argument@@cq @AT@ 23652 @LENGTH@ 2\n" +
                "---------------------------DEL expr@@cq @AT@ 23652 @LENGTH@ 2\n" +
                "------------------------------DEL name@@cq @AT@ 23652 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD expr_stmt@@connection_set_state srv con CON_STATE_REQUEST_END @TO@ connection_set_state srv con CON_STATE_HANDLE_REQUEST @AT@ 23741 @LENGTH@ 50\n" +
                "---UPD expr@@connection_set_state srv con CON_STATE_REQUEST_END @TO@ connection_set_state srv con CON_STATE_HANDLE_REQUEST @AT@ 23741 @LENGTH@ 50\n" +
                "------UPD call@@connection_set_state srv con CON_STATE_REQUEST_END @TO@ connection_set_state srv con CON_STATE_HANDLE_REQUEST @AT@ 23741 @LENGTH@ 50\n" +
                "---------UPD argument_list@@srv con CON_STATE_REQUEST_END @TO@ srv con CON_STATE_HANDLE_REQUEST @AT@ 23761 @LENGTH@ 29\n" +
                "------------UPD argument@@CON_STATE_REQUEST_END @TO@ CON_STATE_HANDLE_REQUEST @AT@ 23772 @LENGTH@ 21\n" +
                "---------------UPD expr@@CON_STATE_REQUEST_END @TO@ CON_STATE_HANDLE_REQUEST @AT@ 23772 @LENGTH@ 21\n" +
                "------------------UPD name@@CON_STATE_REQUEST_END @TO@ CON_STATE_HANDLE_REQUEST @AT@ 23772 @LENGTH@ 21\n");
    }
    @Test
    public void test_wireshark_87646b_2cc0fb() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_87646b_2cc0fb_epan#proto.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
    }
    @Test
    public void test_php_src_a0a56e_7afbb7() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_a0a56e_7afbb7_ext#com_dotnet#com_extension.c");//ok
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@( ) STD_PHP_INI_ENTRY \"com.allow_dcom\" \"0\" PHP_INI_SYSTEM OnUpdateBool allow_dcom zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_verbose\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_verbose zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_typelib\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_on zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_casesensitive\" \"1\" PHP_INI_ALL OnUpdateBool autoreg_case_sensitive zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.code_page\" \"\" PHP_INI_ALL OnUpdateLong code_page zend_com_dotnet_globals com_dotnet_globals PHP_INI_ENTRY \"com.typelib_file\" \"\" PHP_INI_SYSTEM OnTypeLibFileUpdate PHP_INI_END static PHP_GINIT_FUNCTION com_dotnet memset com_dotnet_globals 0 * com_dotnet_globals com_dotnet_globals -> code_page = CP_ACP PHP_MINIT_FUNCTION com_dotnet zend_class_entry ce * tmp REGISTER_INI_ENTRIES php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @TO@ ( ) STD_PHP_INI_ENTRY \"com.allow_dcom\" \"0\" PHP_INI_SYSTEM OnUpdateBool allow_dcom zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_verbose\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_verbose zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_typelib\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_on zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_casesensitive\" \"1\" PHP_INI_ALL OnUpdateBool autoreg_case_sensitive zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.code_page\" \"\" PHP_INI_ALL OnUpdateLong code_page zend_com_dotnet_globals com_dotnet_globals PHP_INI_ENTRY \"com.typelib_file\" \"\" PHP_INI_SYSTEM OnTypeLibFileUpdate PHP_INI_END static PHP_GINIT_FUNCTION com_dotnet memset com_dotnet_globals 0 * com_dotnet_globals com_dotnet_globals -> code_page = CP_ACP PHP_MINIT_FUNCTION com_dotnet zend_class_entry ce * tmp php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get REGISTER_INI_ENTRIES COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @AT@ 4914 @LENGTH@ 3378\n" +
                "---UPD expr@@( ) STD_PHP_INI_ENTRY \"com.allow_dcom\" \"0\" PHP_INI_SYSTEM OnUpdateBool allow_dcom zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_verbose\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_verbose zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_typelib\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_on zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_casesensitive\" \"1\" PHP_INI_ALL OnUpdateBool autoreg_case_sensitive zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.code_page\" \"\" PHP_INI_ALL OnUpdateLong code_page zend_com_dotnet_globals com_dotnet_globals PHP_INI_ENTRY \"com.typelib_file\" \"\" PHP_INI_SYSTEM OnTypeLibFileUpdate PHP_INI_END static PHP_GINIT_FUNCTION com_dotnet memset com_dotnet_globals 0 * com_dotnet_globals com_dotnet_globals -> code_page = CP_ACP PHP_MINIT_FUNCTION com_dotnet zend_class_entry ce * tmp REGISTER_INI_ENTRIES php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @TO@ ( ) STD_PHP_INI_ENTRY \"com.allow_dcom\" \"0\" PHP_INI_SYSTEM OnUpdateBool allow_dcom zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_verbose\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_verbose zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_typelib\" \"0\" PHP_INI_ALL OnUpdateBool autoreg_on zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.autoregister_casesensitive\" \"1\" PHP_INI_ALL OnUpdateBool autoreg_case_sensitive zend_com_dotnet_globals com_dotnet_globals STD_PHP_INI_ENTRY \"com.code_page\" \"\" PHP_INI_ALL OnUpdateLong code_page zend_com_dotnet_globals com_dotnet_globals PHP_INI_ENTRY \"com.typelib_file\" \"\" PHP_INI_SYSTEM OnTypeLibFileUpdate PHP_INI_END static PHP_GINIT_FUNCTION com_dotnet memset com_dotnet_globals 0 * com_dotnet_globals com_dotnet_globals -> code_page = CP_ACP PHP_MINIT_FUNCTION com_dotnet zend_class_entry ce * tmp php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get REGISTER_INI_ENTRIES COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @AT@ 4914 @LENGTH@ 3378\n" +
                "------UPD block@@zend_class_entry ce * tmp REGISTER_INI_ENTRIES php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @TO@ zend_class_entry ce * tmp php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get REGISTER_INI_ENTRIES COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @AT@ 5962 @LENGTH@ 2491\n" +
                "---------MOV expr@@REGISTER_INI_ENTRIES @TO@ block@@zend_class_entry ce * tmp REGISTER_INI_ENTRIES php_com_wrapper_minit INIT_FUNC_ARGS_PASSTHRU php_com_persist_minit INIT_FUNC_ARGS_PASSTHRU INIT_CLASS_ENTRY ce \"com_exception\" NULL php_com_exception_class_entry = zend_register_internal_class_ex &ce zend_exception_get_default(TSRMLS_C) NULL TSRMLS_CC php_com_exception_class_entry -> ce_flags |= ZEND_ACC_FINAL INIT_CLASS_ENTRY ce \"com_safearray_proxy\" NULL php_com_saproxy_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_saproxy_class_entry -> ce_flags |= ZEND_ACC_FINAL php_com_saproxy_class_entry -> get_iterator = php_com_saproxy_iter_get INIT_CLASS_ENTRY ce \"variant\" NULL ce . create_object = php_com_object_new php_com_variant_class_entry = zend_register_internal_class &ce TSRMLS_CC php_com_variant_class_entry -> get_iterator = php_com_iter_get INIT_CLASS_ENTRY ce \"com\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get zend_ts_hash_init & php_com_typelibraries 0 NULL php_com_typelibrary_dtor 1 HAVE_MSCOREE_H INIT_CLASS_ENTRY ce \"dotnet\" NULL ce . create_object = php_com_object_new tmp = zend_register_internal_class_ex & ce php_com_variant_class_entry \"variant\" TSRMLS_CC tmp -> get_iterator = php_com_iter_get COM_CONST x REGISTER_LONG_CONSTANT(#x, x, CONST_CS|CONST_PERSISTENT) COM_CONST CLSCTX_INPROC_SERVER COM_CONST CLSCTX_INPROC_HANDLER COM_CONST CLSCTX_LOCAL_SERVER COM_CONST CLSCTX_REMOTE_SERVER COM_CONST CLSCTX_SERVER COM_CONST CLSCTX_ALL 0 COM_CONST VT_NULL COM_CONST VT_EMPTY COM_CONST VT_UI1 COM_CONST VT_I1 COM_CONST VT_UI2 COM_CONST VT_I2 COM_CONST VT_UI4 COM_CONST VT_I4 COM_CONST VT_R4 COM_CONST VT_R8 COM_CONST VT_BOOL COM_CONST VT_ERROR COM_CONST VT_CY COM_CONST VT_DATE COM_CONST VT_BSTR COM_CONST VT_DECIMAL COM_CONST VT_UNKNOWN COM_CONST VT_DISPATCH COM_CONST VT_VARIANT COM_CONST VT_INT COM_CONST VT_UINT COM_CONST VT_ARRAY COM_CONST VT_BYREF COM_CONST CP_ACP COM_CONST CP_MACCP COM_CONST CP_OEMCP COM_CONST CP_UTF7 COM_CONST CP_UTF8 COM_CONST CP_SYMBOL COM_CONST CP_THREAD_ACP COM_CONST VARCMP_LT COM_CONST VARCMP_EQ COM_CONST VARCMP_GT COM_CONST VARCMP_NULL COM_CONST NORM_IGNORECASE COM_CONST NORM_IGNORENONSPACE COM_CONST NORM_IGNORESYMBOLS COM_CONST NORM_IGNOREWIDTH COM_CONST NORM_IGNOREKANATYPE NORM_IGNOREKASHIDA COM_CONST NORM_IGNOREKASHIDA COM_CONST DISP_E_DIVBYZERO COM_CONST DISP_E_OVERFLOW COM_CONST DISP_E_BADINDEX COM_CONST MK_E_UNAVAILABLE SUCCESS @AT@ 5994 @LENGTH@ 20\n");
    }
    @Test
    public void test_php_src_a29791_796ff1() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_a29791_796ff1_Zend#zend_API.c");//ok
        Assert.assertEquals(hierarchicalActionSets.size(),4);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD while@@param_count > 0 param = va_arg ptr zval * * param_ptr = * ( p - param_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * param = param_ptr param_count -- @TO@ param_count -- > 0 param = va_arg ptr zval * * param_ptr = * ( p - arg_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * param = param_ptr arg_count -- @AT@ 1886 @LENGTH@ 372\n" +
                "---UPD condition@@param_count > 0 @TO@ param_count -- > 0 @AT@ 1892 @LENGTH@ 15\n" +
                "------UPD expr@@param_count > 0 @TO@ param_count -- > 0 @AT@ 1893 @LENGTH@ 15\n" +
                "---------INS operator@@-- @TO@ expr@@param_count > 0 @AT@ 1904 @LENGTH@ 2\n" +
                "---UPD block@@param = va_arg ptr zval * * param_ptr = * ( p - param_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * param = param_ptr param_count -- @TO@ param = va_arg ptr zval * * param_ptr = * ( p - arg_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * param = param_ptr arg_count -- @AT@ 1908 @LENGTH@ 356\n" +
                "------UPD block_content@@param = va_arg ptr zval * * param_ptr = * ( p - param_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * param = param_ptr param_count -- @TO@ param = va_arg ptr zval * * param_ptr = * ( p - arg_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * param = param_ptr arg_count -- @AT@ 1912 @LENGTH@ 356\n" +
                "---------UPD expr_stmt@@param_ptr = * ( p - param_count ) @TO@ param_ptr = * ( p - arg_count ) @AT@ 1944 @LENGTH@ 33\n" +
                "------------UPD expr@@param_ptr = * ( p - param_count ) @TO@ param_ptr = * ( p - arg_count ) @AT@ 1944 @LENGTH@ 33\n" +
                "---------------UPD name@@param_count @TO@ arg_count @AT@ 1960 @LENGTH@ 11\n" +
                "---------UPD if_stmt@@! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @AT@ 1976 @LENGTH@ 259\n" +
                "------------UPD if@@! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @AT@ 1976 @LENGTH@ 259\n" +
                "---------------UPD block@@zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @AT@ 2031 @LENGTH@ 205\n" +
                "------------------UPD block_content@@zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @AT@ 2036 @LENGTH@ 205\n" +
                "---------------------UPD expr_stmt@@( ( zval * ) * ( p - param_count ) ) -> refcount -- @TO@ ( ( zval * ) * ( p - arg_count ) ) -> refcount -- @AT@ 2181 @LENGTH@ 51\n" +
                "------------------------UPD expr@@( ( zval * ) * ( p - param_count ) ) -> refcount -- @TO@ ( ( zval * ) * ( p - arg_count ) ) -> refcount -- @AT@ 2181 @LENGTH@ 51\n" +
                "---------------------------UPD name@@param_count @TO@ arg_count @AT@ 2195 @LENGTH@ 11\n" +
                "---------UPD expr_stmt@@param_count -- @TO@ arg_count -- @AT@ 2283 @LENGTH@ 14\n" +
                "------------UPD expr@@param_count -- @TO@ arg_count -- @AT@ 2283 @LENGTH@ 14\n" +
                "---------------UPD name@@param_count @TO@ arg_count @AT@ 2283 @LENGTH@ 11\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD while@@param_count > 0 param_ptr = * ( p - param_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * ( argument_array ++ ) = param_ptr param_count -- @TO@ param_count -- > 0 param_ptr = * ( p - arg_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr * ( argument_array ++ ) = param_ptr arg_count -- @AT@ 2605 @LENGTH@ 360\n" +
                "---UPD condition@@param_count > 0 @TO@ param_count -- > 0 @AT@ 2611 @LENGTH@ 15\n" +
                "------UPD expr@@param_count > 0 @TO@ param_count -- > 0 @AT@ 2612 @LENGTH@ 15\n" +
                "---------INS operator@@-- @TO@ expr@@param_count > 0 @AT@ 2619 @LENGTH@ 2\n" +
                "---UPD block@@param_ptr = * ( p - param_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * ( argument_array ++ ) = param_ptr param_count -- @TO@ param_ptr = * ( p - arg_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr * ( argument_array ++ ) = param_ptr arg_count -- @AT@ 2627 @LENGTH@ 344\n" +
                "------UPD block_content@@param_ptr = * ( p - param_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr * ( argument_array ++ ) = param_ptr param_count -- @TO@ param_ptr = * ( p - arg_count ) ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr * ( argument_array ++ ) = param_ptr arg_count -- @AT@ 2631 @LENGTH@ 344\n" +
                "---------UPD expr_stmt@@param_ptr = * ( p - param_count ) @TO@ param_ptr = * ( p - arg_count ) @AT@ 2631 @LENGTH@ 33\n" +
                "------------UPD expr@@param_ptr = * ( p - param_count ) @TO@ param_ptr = * ( p - arg_count ) @AT@ 2631 @LENGTH@ 33\n" +
                "---------------UPD name@@param_count @TO@ arg_count @AT@ 2647 @LENGTH@ 11\n" +
                "---------UPD if_stmt@@! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr @AT@ 2663 @LENGTH@ 259\n" +
                "------------UPD if@@! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ ! PZVAL_IS_REF param_ptr && param_ptr -> refcount > 1 zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr @AT@ 2663 @LENGTH@ 259\n" +
                "---------------UPD block@@zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr @AT@ 2718 @LENGTH@ 205\n" +
                "------------------UPD block_content@@zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - param_count ) ) -> refcount -- * ( p - param_count ) = param_ptr @TO@ zval * new_tmp ALLOC_ZVAL new_tmp * new_tmp = * param_ptr zval_copy_ctor new_tmp INIT_PZVAL new_tmp param_ptr = new_tmp ( ( zval * ) * ( p - arg_count ) ) -> refcount -- * ( p - arg_count ) = param_ptr @AT@ 2723 @LENGTH@ 205\n" +
                "---------------------UPD expr_stmt@@( ( zval * ) * ( p - param_count ) ) -> refcount -- @TO@ ( ( zval * ) * ( p - arg_count ) ) -> refcount -- @AT@ 2868 @LENGTH@ 51\n" +
                "------------------------UPD expr@@( ( zval * ) * ( p - param_count ) ) -> refcount -- @TO@ ( ( zval * ) * ( p - arg_count ) ) -> refcount -- @AT@ 2868 @LENGTH@ 51\n" +
                "---------------------------UPD name@@param_count @TO@ arg_count @AT@ 2882 @LENGTH@ 11\n" +
                "---------------------UPD expr_stmt@@* ( p - param_count ) = param_ptr @TO@ * ( p - arg_count ) = param_ptr @AT@ 2912 @LENGTH@ 33\n" +
                "------------------------UPD expr@@* ( p - param_count ) = param_ptr @TO@ * ( p - arg_count ) = param_ptr @AT@ 2912 @LENGTH@ 33\n" +
                "---------------------------UPD name@@param_count @TO@ arg_count @AT@ 2916 @LENGTH@ 11\n" +
                "---------UPD expr_stmt@@param_count -- @TO@ arg_count -- @AT@ 2983 @LENGTH@ 14\n" +
                "------------UPD expr@@param_count -- @TO@ arg_count -- @AT@ 2983 @LENGTH@ 14\n" +
                "---------------UPD name@@param_count @TO@ arg_count @AT@ 2983 @LENGTH@ 11\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD while@@param_count > 0 param = va_arg ptr zval * * * * param = ( zval * * ) p - ( param_count -- ) @TO@ param_count -- > 0 param = va_arg ptr zval * * * * param = ( zval * * ) p - ( arg_count -- ) @AT@ 3402 @LENGTH@ 91\n" +
                "---UPD condition@@param_count > 0 @TO@ param_count -- > 0 @AT@ 3408 @LENGTH@ 15\n" +
                "------UPD expr@@param_count > 0 @TO@ param_count -- > 0 @AT@ 3409 @LENGTH@ 15\n" +
                "---------INS operator@@-- @TO@ expr@@param_count > 0 @AT@ 3410 @LENGTH@ 2\n" +
                "---UPD block@@param = va_arg ptr zval * * * * param = ( zval * * ) p - ( param_count -- ) @TO@ param = va_arg ptr zval * * * * param = ( zval * * ) p - ( arg_count -- ) @AT@ 3424 @LENGTH@ 75\n" +
                "------UPD block_content@@param = va_arg ptr zval * * * * param = ( zval * * ) p - ( param_count -- ) @TO@ param = va_arg ptr zval * * * * param = ( zval * * ) p - ( arg_count -- ) @AT@ 3428 @LENGTH@ 75\n" +
                "---------UPD expr_stmt@@* param = ( zval * * ) p - ( param_count -- ) @TO@ * param = ( zval * * ) p - ( arg_count -- ) @AT@ 3461 @LENGTH@ 45\n" +
                "------------UPD expr@@* param = ( zval * * ) p - ( param_count -- ) @TO@ * param = ( zval * * ) p - ( arg_count -- ) @AT@ 3461 @LENGTH@ 45\n" +
                "---------------UPD name@@param_count @TO@ arg_count @AT@ 3483 @LENGTH@ 11\n");
        Assert.assertEquals(hierarchicalActionSets.get(3).toString(),"UPD while@@param_count > 0 * ( argument_array ++ ) = ( zval * * ) p - ( param_count -- ) @TO@ param_count -- > 0 * ( argument_array ++ ) = ( zval * * ) p - ( arg_count -- ) @AT@ 3783 @LENGTH@ 77\n" +
                "---UPD condition@@param_count > 0 @TO@ param_count -- > 0 @AT@ 3789 @LENGTH@ 15\n" +
                "------UPD expr@@param_count > 0 @TO@ param_count -- > 0 @AT@ 3790 @LENGTH@ 15\n" +
                "---------INS operator@@-- @TO@ expr@@param_count > 0 @AT@ 3791 @LENGTH@ 2\n" +
                "---UPD block@@* ( argument_array ++ ) = ( zval * * ) p - ( param_count -- ) @TO@ * ( argument_array ++ ) = ( zval * * ) p - ( arg_count -- ) @AT@ 3805 @LENGTH@ 61\n" +
                "------UPD block_content@@* ( argument_array ++ ) = ( zval * * ) p - ( param_count -- ) @TO@ * ( argument_array ++ ) = ( zval * * ) p - ( arg_count -- ) @AT@ 3809 @LENGTH@ 61\n" +
                "---------UPD expr_stmt@@* ( argument_array ++ ) = ( zval * * ) p - ( param_count -- ) @TO@ * ( argument_array ++ ) = ( zval * * ) p - ( arg_count -- ) @AT@ 3809 @LENGTH@ 61\n" +
                "------------UPD expr@@* ( argument_array ++ ) = ( zval * * ) p - ( param_count -- ) @TO@ * ( argument_array ++ ) = ( zval * * ) p - ( arg_count -- ) @AT@ 3809 @LENGTH@ 61\n" +
                "---------------UPD name@@param_count @TO@ arg_count @AT@ 3844 @LENGTH@ 11\n");
    }
    @Test
    public void test_wireshark_2cc860_12fddb() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_2cc860_12fddb_packet-rtps.c");//ok
        Assert.assertEquals(hierarchicalActionSets.size(),4);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS expr_stmt@@param_length -= 4 @TO@ block@@char * ip_string ip_string = IP_to_string offset tvb little_endian buff_tmp proto_item_append_text ti \"%c %s\" sep ip_string proto_tree_add_text rtps_parameter_tree tvb offset param_length \"Address[%d]: %s\" i ip_string ++ i offset += 4 sep = ',' @AT@ 43110 @LENGTH@ 17\n" +
                "---INS expr@@param_length -= 4 @TO@ expr_stmt@@param_length -= 4 @AT@ 43110 @LENGTH@ 17\n" +
                "------INS name@@param_length @TO@ expr@@param_length -= 4 @AT@ 43110 @LENGTH@ 12\n" +
                "------INS operator@@-= @TO@ expr@@param_length -= 4 @AT@ 43123 @LENGTH@ 2\n" +
                "------INS literal:number@@4 @TO@ expr@@param_length -= 4 @AT@ 43126 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"INS expr_stmt@@param_length -= 4 @TO@ block@@char * ip_string ip_string = IP_to_string offset tvb little_endian buff_tmp proto_item_append_text ti \"%c %s\" sep ip_string proto_tree_add_text rtps_parameter_tree tvb offset param_length \"Address[%d]: %s\" i ip_string ++ i offset += 4 sep = ',' @AT@ 43696 @LENGTH@ 17\n" +
                "---INS expr@@param_length -= 4 @TO@ expr_stmt@@param_length -= 4 @AT@ 43696 @LENGTH@ 17\n" +
                "------INS name@@param_length @TO@ expr@@param_length -= 4 @AT@ 43696 @LENGTH@ 12\n" +
                "------INS operator@@-= @TO@ expr@@param_length -= 4 @AT@ 43709 @LENGTH@ 2\n" +
                "------INS literal:number@@4 @TO@ expr@@param_length -= 4 @AT@ 43712 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"INS expr_stmt@@param_length -= 4 @TO@ block@@char * ip_string ip_string = IP_to_string offset tvb little_endian buff_tmp proto_item_append_text ti \"%c %s\" sep ip_string proto_tree_add_text rtps_parameter_tree tvb offset param_length \"Address[%d]: %s\" i ip_string ++ i offset += 4 @AT@ 46139 @LENGTH@ 17\n" +
                "---INS expr@@param_length -= 4 @TO@ expr_stmt@@param_length -= 4 @AT@ 46139 @LENGTH@ 17\n" +
                "------INS name@@param_length @TO@ expr@@param_length -= 4 @AT@ 46139 @LENGTH@ 12\n" +
                "------INS operator@@-= @TO@ expr@@param_length -= 4 @AT@ 46152 @LENGTH@ 2\n" +
                "------INS literal:number@@4 @TO@ expr@@param_length -= 4 @AT@ 46155 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(3).toString(),"INS expr_stmt@@param_length -= 4 @TO@ block@@guint32 manager_key manager_key = get_guint32 tvb offset little_endian proto_item_append_text ti \"%c 0x%X\" sep manager_key proto_tree_add_text rtps_parameter_tree tvb offset param_length \"Key[%d]: 0x%X\" i manager_key ++ i offset += 4 sep = ',' @AT@ 46723 @LENGTH@ 17\n" +
                "---INS expr@@param_length -= 4 @TO@ expr_stmt@@param_length -= 4 @AT@ 46723 @LENGTH@ 17\n" +
                "------INS name@@param_length @TO@ expr@@param_length -= 4 @AT@ 46723 @LENGTH@ 12\n" +
                "------INS operator@@-= @TO@ expr@@param_length -= 4 @AT@ 46736 @LENGTH@ 2\n" +
                "------INS literal:number@@4 @TO@ expr@@param_length -= 4 @AT@ 46739 @LENGTH@ 1\n");
    }
    @Test
    public void test_wireshark_3f2283_9e184d() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_3f2283_9e184d_epan#dissectors#packet-bacapp.c");//ok
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD switch@@fTagNo tvb offset 0 offset = fBitStringTag tvb subtree offset \"referenced-bitstring: \" break; 1 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags break; default: offset break; @TO@ fTagNo tvb offset 0 offset = fBitStringTag tvb subtree offset \"referenced-bitstring: \" break; 1 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags break; default: break; @AT@ 79064 @LENGTH@ 204\n" +
                "---UPD block@@0 offset = fBitStringTag tvb subtree offset \"referenced-bitstring: \" break; 1 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags break; default: offset break; @TO@ 0 offset = fBitStringTag tvb subtree offset \"referenced-bitstring: \" break; 1 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags break; default: break; @AT@ 79093 @LENGTH@ 186\n" +
                "------UPD block_content@@0 offset = fBitStringTag tvb subtree offset \"referenced-bitstring: \" break; 1 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags break; default: offset break; @TO@ 0 offset = fBitStringTag tvb subtree offset \"referenced-bitstring: \" break; 1 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags break; default: break; @AT@ 79098 @LENGTH@ 186\n" +
                "---------DEL return@@offset @AT@ 79332 @LENGTH@ 6\n" +
                "------------DEL expr@@offset @AT@ 79339 @LENGTH@ 6\n" +
                "---------------DEL name@@offset @AT@ 79339 @LENGTH@ 6\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD switch@@fTagNo tvb offset 0 offset = fEnumeratedTagSplit tvb subtree offset \"new-state: \" BACnetLifeSafetyState 256 break; 1 offset = fEnumeratedTagSplit tvb subtree offset \"new-mode: \" BACnetLifeSafetyState 256 break; 2 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags 3 offset = fEnumeratedTagSplit tvb subtree offset \"operation-expected: \" BACnetLifeSafetyOperation 64 default: offset break; @TO@ fTagNo tvb offset 0 offset = fEnumeratedTagSplit tvb subtree offset \"new-state: \" BACnetLifeSafetyState 256 break; 1 offset = fEnumeratedTagSplit tvb subtree offset \"new-mode: \" BACnetLifeSafetyState 256 break; 2 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags 3 offset = fEnumeratedTagSplit tvb subtree offset \"operation-expected: \" BACnetLifeSafetyOperation 64 default: break; @AT@ 84272 @LENGTH@ 416\n" +
                "---UPD block@@0 offset = fEnumeratedTagSplit tvb subtree offset \"new-state: \" BACnetLifeSafetyState 256 break; 1 offset = fEnumeratedTagSplit tvb subtree offset \"new-mode: \" BACnetLifeSafetyState 256 break; 2 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags 3 offset = fEnumeratedTagSplit tvb subtree offset \"operation-expected: \" BACnetLifeSafetyOperation 64 default: offset break; @TO@ 0 offset = fEnumeratedTagSplit tvb subtree offset \"new-state: \" BACnetLifeSafetyState 256 break; 1 offset = fEnumeratedTagSplit tvb subtree offset \"new-mode: \" BACnetLifeSafetyState 256 break; 2 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags 3 offset = fEnumeratedTagSplit tvb subtree offset \"operation-expected: \" BACnetLifeSafetyOperation 64 default: break; @AT@ 84301 @LENGTH@ 398\n" +
                "------UPD block_content@@0 offset = fEnumeratedTagSplit tvb subtree offset \"new-state: \" BACnetLifeSafetyState 256 break; 1 offset = fEnumeratedTagSplit tvb subtree offset \"new-mode: \" BACnetLifeSafetyState 256 break; 2 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags 3 offset = fEnumeratedTagSplit tvb subtree offset \"operation-expected: \" BACnetLifeSafetyOperation 64 default: offset break; @TO@ 0 offset = fEnumeratedTagSplit tvb subtree offset \"new-state: \" BACnetLifeSafetyState 256 break; 1 offset = fEnumeratedTagSplit tvb subtree offset \"new-mode: \" BACnetLifeSafetyState 256 break; 2 offset = fBitStringTagVS tvb subtree offset \"status-flags: \" BACnetStatusFlags 3 offset = fEnumeratedTagSplit tvb subtree offset \"operation-expected: \" BACnetLifeSafetyOperation 64 default: break; @AT@ 84306 @LENGTH@ 398\n" +
                "---------DEL return@@offset @AT@ 84806 @LENGTH@ 6\n" +
                "------------DEL expr@@offset @AT@ 84813 @LENGTH@ 6\n" +
                "---------------DEL name@@offset @AT@ 84813 @LENGTH@ 6\n");
    }
    @Test
    public void test_wireshark_1a76dd_1d1a48() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_1a76dd_1d1a48_epan#dissectors#packet-ua3g.c");//removeParentForSingle??
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD expr_stmt@@proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_NA @AT@ 62819 @LENGTH@ 129\n" +
                "---UPD expr@@proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_NA @AT@ 62819 @LENGTH@ 129\n" +
                "------UPD call@@proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_NA @AT@ 62819 @LENGTH@ 129\n" +
                "---------UPD argument_list@@ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ ua3g_param_tree hf_ua3g_ip_device_routing_redirect_parameter_value tvb offset parameter_length ENC_NA @AT@ 62838 @LENGTH@ 109\n" +
                "------------UPD argument@@ENC_BIG_ENDIAN @TO@ ENC_NA @AT@ 62939 @LENGTH@ 14\n" +
                "---------------UPD expr@@ENC_BIG_ENDIAN @TO@ ENC_NA @AT@ 62939 @LENGTH@ 14\n" +
                "------------------UPD name@@ENC_BIG_ENDIAN @TO@ ENC_NA @AT@ 62939 @LENGTH@ 14\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD expr_stmt@@proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_NA @AT@ 77911 @LENGTH@ 142\n" +
                "---UPD expr@@proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_NA @AT@ 77911 @LENGTH@ 142\n" +
                "------UPD call@@proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ proto_tree_add_item ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_NA @AT@ 77911 @LENGTH@ 142\n" +
                "---------UPD argument_list@@ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_BIG_ENDIAN @TO@ ua3g_param_tree hf_ua3g_ip_device_routing_start_stop_record_rtp_parameter_value tvb offset parameter_length ENC_NA @AT@ 77930 @LENGTH@ 122\n" +
                "------------UPD argument@@ENC_BIG_ENDIAN @TO@ ENC_NA @AT@ 78044 @LENGTH@ 14\n" +
                "---------------UPD expr@@ENC_BIG_ENDIAN @TO@ ENC_NA @AT@ 78044 @LENGTH@ 14\n" +
                "------------------UPD name@@ENC_BIG_ENDIAN @TO@ ENC_NA @AT@ 78044 @LENGTH@ 14\n");
    }
    @Test
    public void test_wireshark_bb4516_9597f6() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_bb4516_9597f6_epan#sigcomp-udvm.c");//removeParentForSingle?? and others maybe
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS if@@if k + handle_now >= UDVM_MEMORY_SIZE THROW ReportedBoundsError @TO@ block@@guint16 handle_now length if k < byte_copy_right && byte_copy_right <= k + ( length - n ) handle_now = byte_copy_right - position sha1_update & ctx & buff ][k handle_now k = ( k + handle_now ) & 0xffff n = ( n + handle_now ) & 0xffff if k >= byte_copy_right k = byte_copy_left @AT@ 30752 @LENGTH@ 63\n" +
                "---INS condition@@k + handle_now >= UDVM_MEMORY_SIZE @TO@ if@@if k + handle_now >= UDVM_MEMORY_SIZE THROW ReportedBoundsError @AT@ 30752 @LENGTH@ 34\n" +
                "------INS expr@@k + handle_now >= UDVM_MEMORY_SIZE @TO@ condition@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 30753 @LENGTH@ 34\n" +
                "---------INS name@@k @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 30753 @LENGTH@ 1\n" +
                "---------INS operator@@+ @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 30755 @LENGTH@ 1\n" +
                "---------INS name@@handle_now @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 30757 @LENGTH@ 10\n" +
                "---------INS operator@@>= @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 30768 @LENGTH@ 2\n" +
                "---------INS name@@UDVM_MEMORY_SIZE @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 30771 @LENGTH@ 16\n" +
                "---INS then@@THROW ReportedBoundsError @TO@ if@@if k + handle_now >= UDVM_MEMORY_SIZE THROW ReportedBoundsError @AT@ 30793 @LENGTH@ 25\n" +
                "------INS block@@THROW ReportedBoundsError @TO@ then@@THROW ReportedBoundsError @AT@ 30793 @LENGTH@ 25\n" +
                "---------INS expr_stmt@@THROW ReportedBoundsError @TO@ block@@THROW ReportedBoundsError @AT@ 30793 @LENGTH@ 25\n" +
                "------------INS expr@@THROW ReportedBoundsError @TO@ expr_stmt@@THROW ReportedBoundsError @AT@ 30793 @LENGTH@ 25\n" +
                "---------------INS call@@THROW ReportedBoundsError @TO@ expr@@THROW ReportedBoundsError @AT@ 30793 @LENGTH@ 25\n" +
                "------------------INS name@@THROW @TO@ call@@THROW ReportedBoundsError @AT@ 30793 @LENGTH@ 5\n" +
                "------------------INS argument_list@@ReportedBoundsError @TO@ call@@THROW ReportedBoundsError @AT@ 30798 @LENGTH@ 19\n" +
                "---------------------INS argument@@ReportedBoundsError @TO@ argument_list@@ReportedBoundsError @AT@ 30799 @LENGTH@ 19\n" +
                "------------------------INS expr@@ReportedBoundsError @TO@ argument@@ReportedBoundsError @AT@ 30799 @LENGTH@ 19\n" +
                "---------------------------INS name@@ReportedBoundsError @TO@ expr@@ReportedBoundsError @AT@ 30799 @LENGTH@ 19\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"INS if@@if k + handle_now >= UDVM_MEMORY_SIZE THROW ReportedBoundsError @TO@ block@@guint16 handle_now length - n if k < byte_copy_right && byte_copy_right <= k + ( length - n ) handle_now = byte_copy_right - k result = crc16_ccitt_seed & buff ][k handle_now result ^ 0xffff k = ( k + handle_now ) & 0xffff n = ( n + handle_now ) & 0xffff if k >= byte_copy_right k = byte_copy_left @AT@ 61659 @LENGTH@ 63\n" +
                "---INS condition@@k + handle_now >= UDVM_MEMORY_SIZE @TO@ if@@if k + handle_now >= UDVM_MEMORY_SIZE THROW ReportedBoundsError @AT@ 61659 @LENGTH@ 34\n" +
                "------INS expr@@k + handle_now >= UDVM_MEMORY_SIZE @TO@ condition@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 61660 @LENGTH@ 34\n" +
                "---------INS name@@k @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 61660 @LENGTH@ 1\n" +
                "---------INS operator@@+ @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 61662 @LENGTH@ 1\n" +
                "---------INS name@@handle_now @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 61664 @LENGTH@ 10\n" +
                "---------INS operator@@>= @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 61675 @LENGTH@ 2\n" +
                "---------INS name@@UDVM_MEMORY_SIZE @TO@ expr@@k + handle_now >= UDVM_MEMORY_SIZE @AT@ 61678 @LENGTH@ 16\n" +
                "---INS then@@THROW ReportedBoundsError @TO@ if@@if k + handle_now >= UDVM_MEMORY_SIZE THROW ReportedBoundsError @AT@ 61700 @LENGTH@ 25\n" +
                "------INS block@@THROW ReportedBoundsError @TO@ then@@THROW ReportedBoundsError @AT@ 61700 @LENGTH@ 25\n" +
                "---------INS expr_stmt@@THROW ReportedBoundsError @TO@ block@@THROW ReportedBoundsError @AT@ 61700 @LENGTH@ 25\n" +
                "------------INS expr@@THROW ReportedBoundsError @TO@ expr_stmt@@THROW ReportedBoundsError @AT@ 61700 @LENGTH@ 25\n" +
                "---------------INS call@@THROW ReportedBoundsError @TO@ expr@@THROW ReportedBoundsError @AT@ 61700 @LENGTH@ 25\n" +
                "------------------INS name@@THROW @TO@ call@@THROW ReportedBoundsError @AT@ 61700 @LENGTH@ 5\n" +
                "------------------INS argument_list@@ReportedBoundsError @TO@ call@@THROW ReportedBoundsError @AT@ 61705 @LENGTH@ 19\n" +
                "---------------------INS argument@@ReportedBoundsError @TO@ argument_list@@ReportedBoundsError @AT@ 61706 @LENGTH@ 19\n" +
                "------------------------INS expr@@ReportedBoundsError @TO@ argument@@ReportedBoundsError @AT@ 61706 @LENGTH@ 19\n" +
                "---------------------------INS name@@ReportedBoundsError @TO@ expr@@ReportedBoundsError @AT@ 61706 @LENGTH@ 19\n");
    }
    @Test
    public void test_php_src_a347ed_d2b435() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_a347ed_d2b435_ext#ereg#ereg.c");//removeParentForSingle?? and others maybe
//        Assert.fail(); //todo wrong
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 new_l ++ walk ++ @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 new_l ++ walk ++ @AT@ 8930 @LENGTH@ 228\n" +
                "---UPD if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 8930 @LENGTH@ 211\n" +
                "------UPD condition@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 8933 @LENGTH@ 130\n" +
                "---------UPD expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 8934 @LENGTH@ 130\n" +
                "------------INS name@@walk ][1 @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 8997 @LENGTH@ 8\n" +
                "---------------INS name@@walk @TO@ name@@walk ][1 @AT@ 8997 @LENGTH@ 4\n" +
                "---------------INS index@@][1 @TO@ name@@walk ][1 @AT@ 9001 @LENGTH@ 3\n" +
                "------------------INS expr@@[1 @TO@ index@@][1 @AT@ 9002 @LENGTH@ 2\n" +
                "---------------------INS literal:number@@[1 @TO@ expr@@[1 @AT@ 9002 @LENGTH@ 2\n" +
                "------------INS operator@@- @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9005 @LENGTH@ 1\n" +
                "------------INS literal:char@@'0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9007 @LENGTH@ 3\n" +
                "------------INS operator@@<= @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9011 @LENGTH@ 2\n" +
                "------------INS name@@re . re_nsub @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9014 @LENGTH@ 12\n" +
                "---------------INS name@@re @TO@ name@@re . re_nsub @AT@ 9014 @LENGTH@ 2\n" +
                "---------------INS operator@@. @TO@ name@@re . re_nsub @AT@ 9016 @LENGTH@ 1\n" +
                "---------------INS name@@re_nsub @TO@ name@@re . re_nsub @AT@ 9017 @LENGTH@ 7\n" +
                "------------DEL operator@@. @AT@ 9016 @LENGTH@ 1\n" +
                "------------INS operator@@&& @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9030 @LENGTH@ 2\n" +
                "------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9036 @LENGTH@ 21\n" +
                "------------INS operator@@. @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9052 @LENGTH@ 1\n" +
                "------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9675 @LENGTH@ 21\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD if_stmt@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 * walkbuf ++ = * walk ++ @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 * walkbuf ++ = * walk ++ @AT@ 9608 @LENGTH@ 380\n" +
                "---UPD if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 9608 @LENGTH@ 355\n" +
                "------UPD condition@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9611 @LENGTH@ 196\n" +
                "---------UPD expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @TO@ '\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9612 @LENGTH@ 196\n" +
                "------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 8997 @LENGTH@ 21\n" +
                "------------MOV name@@[walk ][1 @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9680 @LENGTH@ 9\n" +
                "---------------UPD name@@[walk ][1 @TO@ walk ][1 @AT@ 9680 @LENGTH@ 9\n" +
                "------------------UPD name@@[walk @TO@ walk @AT@ 9680 @LENGTH@ 5\n" +
                "------------MOV operator@@- @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9688 @LENGTH@ 1\n" +
                "------------MOV literal:char@@'0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9690 @LENGTH@ 3\n" +
                "------------DEL operator@@. @AT@ 9694 @LENGTH@ 1\n" +
                "------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9714 @LENGTH@ 21\n" +
                "------------INS operator@@<= @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9725 @LENGTH@ 2\n" +
                "------------INS name@@re . re_nsub @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9728 @LENGTH@ 12\n" +
                "---------------INS name@@re @TO@ name@@re . re_nsub @AT@ 9728 @LENGTH@ 2\n" +
                "---------------INS operator@@. @TO@ name@@re . re_nsub @AT@ 9730 @LENGTH@ 1\n" +
                "---------------INS name@@re_nsub @TO@ name@@re . re_nsub @AT@ 9731 @LENGTH@ 7\n" +
                "------------INS operator@@&& @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9744 @LENGTH@ 2\n" +
                "------------INS operator@@. @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9766 @LENGTH@ 1\n" +
                "------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9806 @LENGTH@ 21\n" +
                "------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9835 @LENGTH@ 21\n");
    }
    @Test//TODO
    public void test_php_src_f91b3d_5f1bff() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_f91b3d_5f1bff_ext#ereg#ereg.c");//removeParentForSingle?? and others maybe
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 new_l ++ walk ++ @TO@ '\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 new_l ++ walk ++ @AT@ 8930 @LENGTH@ 275\n" +
                "---UPD if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ '\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 8930 @LENGTH@ 258\n" +
                "------DEL condition@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 8933 @LENGTH@ 177\n" +
                "---------DEL expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 8934 @LENGTH@ 177\n" +
                "------------DEL literal:char@@'0' @AT@ 8956 @LENGTH@ 3\n" +
                "------------DEL operator@@<= @AT@ 8960 @LENGTH@ 2\n" +
                "------------DEL operator@@&& @AT@ 8971 @LENGTH@ 2\n" +
                "------------DEL literal:char@@'9' @AT@ 8974 @LENGTH@ 3\n" +
                "------------DEL operator@@>= @AT@ 8978 @LENGTH@ 2\n" +
                "------------DEL operator@@&& @AT@ 8994 @LENGTH@ 2\n" +
                "------------DEL name@@walk ][1 @AT@ 8997 @LENGTH@ 8\n" +
                "---------------DEL name@@walk @AT@ 8997 @LENGTH@ 4\n" +
                "---------------DEL index@@][1 @AT@ 9001 @LENGTH@ 3\n" +
                "------------------DEL expr@@[1 @AT@ 9002 @LENGTH@ 2\n" +
                "---------------------DEL literal:number@@[1 @AT@ 9002 @LENGTH@ 2\n" +
                "------INS condition@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @TO@ if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 8952 @LENGTH@ 81\n" +
                "---------INS expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @TO@ condition@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8953 @LENGTH@ 81\n" +
                "------------MOV literal:char@@'\\\\' @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8934 @LENGTH@ 4\n" +
                "------------MOV operator@@== @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8939 @LENGTH@ 2\n" +
                "------------MOV operator@@* @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8942 @LENGTH@ 1\n" +
                "------------MOV name@@walk @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8943 @LENGTH@ 4\n" +
                "------------MOV operator@@&& @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8953 @LENGTH@ 2\n" +
                "------------MOV name@@walk ][1 @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8963 @LENGTH@ 8\n" +
                "------------INS call@@isdigit walk ][1 @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8970 @LENGTH@ 16\n" +
                "---------------INS name@@isdigit @TO@ call@@isdigit walk ][1 @AT@ 8970 @LENGTH@ 7\n" +
                "---------------INS argument_list@@walk ][1 @TO@ call@@isdigit walk ][1 @AT@ 8977 @LENGTH@ 8\n" +
                "------------------INS argument@@walk ][1 @TO@ argument_list@@walk ][1 @AT@ 8978 @LENGTH@ 8\n" +
                "---------------------INS expr@@walk ][1 @TO@ argument@@walk ][1 @AT@ 8978 @LENGTH@ 8\n" +
                "------------------------MOV name@@walk ][1 @TO@ expr@@walk ][1 @AT@ 8981 @LENGTH@ 8\n" +
                "------------INS operator@@&& @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 8987 @LENGTH@ 2\n" +
                "------------MOV operator@@- @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9005 @LENGTH@ 1\n" +
                "------------MOV literal:char@@'0' @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9007 @LENGTH@ 3\n" +
                "------------MOV operator@@<= @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9011 @LENGTH@ 2\n" +
                "------------MOV operator@@( @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9014 @LENGTH@ 1\n" +
                "------------MOV operator@@( @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9015 @LENGTH@ 1\n" +
                "------------MOV name@@char @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9016 @LENGTH@ 4\n" +
                "------------MOV operator@@) @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9020 @LENGTH@ 1\n" +
                "------------MOV name@@re . re_nsub @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9022 @LENGTH@ 12\n" +
                "------------MOV operator@@) @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) @AT@ 9032 @LENGTH@ 1\n" +
                "------INS block@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= ( ( char ) re . re_nsub ) && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 9028 @LENGTH@ 155\n" +
                "---------INS block_content@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ block@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 9035 @LENGTH@ 155\n" +
                "------------INS if_stmt@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @TO@ block_content@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 9035 @LENGTH@ 145\n" +
                "---------------INS if@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @TO@ if_stmt@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @AT@ 9035 @LENGTH@ 145\n" +
                "------------------INS condition@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @TO@ if@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @AT@ 9038 @LENGTH@ 74\n" +
                "---------------------INS expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @TO@ condition@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9039 @LENGTH@ 74\n" +
                "------------------------MOV operator@@. @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9061 @LENGTH@ 1\n" +
                "------------------------MOV name@@rm_so @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9062 @LENGTH@ 5\n" +
                "------------------------MOV operator@@> @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9068 @LENGTH@ 1\n" +
                "------------------------MOV operator@@- @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9070 @LENGTH@ 1\n" +
                "------------------------MOV literal:number@@1 @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9071 @LENGTH@ 1\n" +
                "------------------------MOV operator@@&& @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9078 @LENGTH@ 2\n" +
                "------------------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9081 @LENGTH@ 21\n" +
                "------------------------MOV operator@@. @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9100 @LENGTH@ 1\n" +
                "------------------------MOV name@@rm_eo @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9101 @LENGTH@ 5\n" +
                "------------------------MOV operator@@> @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9107 @LENGTH@ 1\n" +
                "------------------------MOV operator@@- @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9109 @LENGTH@ 1\n" +
                "------------------------MOV literal:number@@1 @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9110 @LENGTH@ 1\n" +
                "------------------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 @AT@ 9916 @LENGTH@ 21\n" +
                "------------------MOV block@@new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ if@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @AT@ 9113 @LENGTH@ 80\n" +
                "------------MOV expr_stmt@@walk += 2 @TO@ block_content@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @AT@ 9195 @LENGTH@ 9\n" +
                "------UPD block@@new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @AT@ 9113 @LENGTH@ 80\n" +
                "---------UPD block_content@@new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so walk += 2 @TO@ new_l += subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so @AT@ 9120 @LENGTH@ 80\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD if_stmt@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 * walkbuf ++ = * walk ++ @TO@ '\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 * walkbuf ++ = * walk ++ @AT@ 9653 @LENGTH@ 414\n" +
                "---UPD if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ '\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 9653 @LENGTH@ 389\n" +
                "------INS condition@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @TO@ if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 9654 @LENGTH@ 68\n" +
                "---------INS expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @TO@ condition@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9655 @LENGTH@ 68\n" +
                "------------MOV literal:char@@'\\\\' @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9657 @LENGTH@ 4\n" +
                "------------MOV operator@@== @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9662 @LENGTH@ 2\n" +
                "------------MOV operator@@* @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9665 @LENGTH@ 1\n" +
                "------------MOV name@@walk @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9666 @LENGTH@ 4\n" +
                "------------INS call@@isdigit walk ][1 @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9672 @LENGTH@ 16\n" +
                "---------------INS name@@isdigit @TO@ call@@isdigit walk ][1 @AT@ 9672 @LENGTH@ 7\n" +
                "---------------INS argument_list@@walk ][1 @TO@ call@@isdigit walk ][1 @AT@ 9679 @LENGTH@ 8\n" +
                "------------------INS argument@@walk ][1 @TO@ argument_list@@walk ][1 @AT@ 9680 @LENGTH@ 8\n" +
                "---------------------INS expr@@walk ][1 @TO@ argument@@walk ][1 @AT@ 9680 @LENGTH@ 8\n" +
                "------------------------MOV name@@walk ][1 @TO@ expr@@walk ][1 @AT@ 9704 @LENGTH@ 8\n" +
                "------------MOV operator@@&& @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9676 @LENGTH@ 2\n" +
                "------------MOV name@@walk ][1 @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9686 @LENGTH@ 8\n" +
                "------------INS operator@@&& @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9689 @LENGTH@ 2\n" +
                "------------MOV operator@@- @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9728 @LENGTH@ 1\n" +
                "------------MOV literal:char@@'0' @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9730 @LENGTH@ 3\n" +
                "------------MOV operator@@<= @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9734 @LENGTH@ 2\n" +
                "------------MOV name@@re . re_nsub @TO@ expr@@'\\\\' == * walk && isdigit walk ][1 && walk ][1 - '0' <= re . re_nsub @AT@ 9737 @LENGTH@ 12\n" +
                "------DEL condition@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9656 @LENGTH@ 230\n" +
                "---------DEL expr@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9657 @LENGTH@ 230\n" +
                "------------DEL literal:char@@'0' @AT@ 9679 @LENGTH@ 3\n" +
                "------------DEL operator@@<= @AT@ 9683 @LENGTH@ 2\n" +
                "------------DEL operator@@&& @AT@ 9694 @LENGTH@ 2\n" +
                "------------DEL literal:char@@'9' @AT@ 9697 @LENGTH@ 3\n" +
                "------------DEL operator@@>= @AT@ 9701 @LENGTH@ 2\n" +
                "------------DEL operator@@&& @AT@ 9717 @LENGTH@ 2\n" +
                "------------DEL name@@walk ][1 @AT@ 9720 @LENGTH@ 8\n" +
                "---------------DEL name@@walk @AT@ 9720 @LENGTH@ 4\n" +
                "---------------DEL index@@][1 @AT@ 9724 @LENGTH@ 3\n" +
                "------------------DEL expr@@[1 @AT@ 9725 @LENGTH@ 2\n" +
                "---------------------DEL literal:number@@[1 @AT@ 9725 @LENGTH@ 2\n" +
                "------------DEL operator@@&& @AT@ 9753 @LENGTH@ 2\n" +
                "------INS block@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ if@@'\\\\' == * walk && '0' <= walk ][1 && '9' >= walk ][1 && walk ][1 - '0' <= re . re_nsub && subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 9721 @LENGTH@ 299\n" +
                "---------INS block_content@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ block@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 9728 @LENGTH@ 299\n" +
                "------------INS if_stmt@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @TO@ block_content@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 9728 @LENGTH@ 289\n" +
                "---------------INS if@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @TO@ if_stmt@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @AT@ 9728 @LENGTH@ 289\n" +
                "------------------INS condition@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @TO@ if@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @AT@ 9731 @LENGTH@ 140\n" +
                "---------------------INS expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @TO@ condition@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9732 @LENGTH@ 140\n" +
                "------------------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9042 @LENGTH@ 21\n" +
                "------------------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9756 @LENGTH@ 21\n" +
                "------------------------MOV operator@@. @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9775 @LENGTH@ 1\n" +
                "------------------------MOV name@@rm_so @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9776 @LENGTH@ 5\n" +
                "------------------------MOV operator@@> @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9782 @LENGTH@ 1\n" +
                "------------------------MOV operator@@- @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9784 @LENGTH@ 1\n" +
                "------------------------MOV literal:number@@1 @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9785 @LENGTH@ 1\n" +
                "------------------------MOV operator@@&& @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9792 @LENGTH@ 2\n" +
                "------------------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9795 @LENGTH@ 21\n" +
                "------------------------MOV operator@@. @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9814 @LENGTH@ 1\n" +
                "------------------------MOV name@@rm_eo @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9815 @LENGTH@ 5\n" +
                "------------------------MOV operator@@> @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9821 @LENGTH@ 1\n" +
                "------------------------MOV operator@@- @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9823 @LENGTH@ 1\n" +
                "------------------------MOV literal:number@@1 @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9824 @LENGTH@ 1\n" +
                "------------------------MOV operator@@&& @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9884 @LENGTH@ 2\n" +
                "------------------------MOV name@@subs ][walk ][1 - '0' @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9887 @LENGTH@ 21\n" +
                "------------------------MOV operator@@. @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9906 @LENGTH@ 1\n" +
                "------------------------MOV name@@rm_so @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9907 @LENGTH@ 5\n" +
                "------------------------MOV operator@@<= @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9913 @LENGTH@ 2\n" +
                "------------------------MOV operator@@. @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9935 @LENGTH@ 1\n" +
                "------------------------MOV name@@rm_eo @TO@ expr@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo @AT@ 9936 @LENGTH@ 5\n" +
                "------------------MOV block@@tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ if@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @AT@ 9943 @LENGTH@ 158\n" +
                "------------MOV expr_stmt@@walk += 2 @TO@ block_content@@subs ][walk ][1 - '0' . rm_so > - 1 && subs ][walk ][1 - '0' . rm_eo > - 1 && subs ][walk ][1 - '0' . rm_so <= subs ][walk ][1 - '0' . rm_eo tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @AT@ 10127 @LENGTH@ 9\n" +
                "------UPD block@@tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @AT@ 9943 @LENGTH@ 158\n" +
                "---------UPD block_content@@tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp walk += 2 @TO@ tmp = subs ][walk ][1 - '0' . rm_eo - subs ][walk ][1 - '0' . rm_so memcpy walkbuf & string ][pos + subs ][walk ][1 - '0' . rm_so tmp walkbuf += tmp @AT@ 9950 @LENGTH@ 158\n");
    }
    @Test
    public void test_wireshark_606866_c79cf4() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_606866_c79cf4_epan#dissectors#packet-atalk.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),3);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD for@@i = 1 i <= count i ++ len = tvb_get_guint8 tvb offset proto_tree_add_item sub_tree hf_zip_zone_name tvb offset 1 FALSE offset += len + 1 @TO@ i = 0 i < count i ++ len = tvb_get_guint8 tvb offset proto_tree_add_item sub_tree hf_zip_zone_name tvb offset 1 FALSE offset += len + 1 @AT@ 47199 @LENGTH@ 136\n" +
                "---UPD control@@i = 1 i <= count i ++ @TO@ i = 0 i < count i ++ @AT@ 47203 @LENGTH@ 21\n" +
                "------UPD init@@i = 1 @TO@ i = 0 @AT@ 47204 @LENGTH@ 5\n" +
                "---------UPD expr@@i = 1 @TO@ i = 0 @AT@ 47204 @LENGTH@ 5\n" +
                "------------UPD literal:number@@1 @TO@ 0 @AT@ 47207 @LENGTH@ 1\n" +
                "------UPD condition@@i <= count @TO@ i < count @AT@ 47210 @LENGTH@ 10\n" +
                "---------UPD expr@@i <= count @TO@ i < count @AT@ 47210 @LENGTH@ 10\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 47212 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD for@@i = 1 i <= count i ++ proto_tree_add_item sub_tree hf_zip_network tvb offset 2 FALSE offset += 2 @TO@ i = 0 i < count i ++ proto_tree_add_item sub_tree hf_zip_network tvb offset 2 FALSE offset += 2 @AT@ 48615 @LENGTH@ 96\n" +
                "---UPD control@@i = 1 i <= count i ++ @TO@ i = 0 i < count i ++ @AT@ 48619 @LENGTH@ 21\n" +
                "------UPD init@@i = 1 @TO@ i = 0 @AT@ 48620 @LENGTH@ 5\n" +
                "---------UPD expr@@i = 1 @TO@ i = 0 @AT@ 48620 @LENGTH@ 5\n" +
                "------------UPD literal:number@@1 @TO@ 0 @AT@ 48623 @LENGTH@ 1\n" +
                "------UPD condition@@i <= count @TO@ i < count @AT@ 48626 @LENGTH@ 10\n" +
                "---------UPD expr@@i <= count @TO@ i < count @AT@ 48626 @LENGTH@ 10\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 48628 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD for@@i = 1 i <= count i ++ net = tvb_get_ntohs tvb offset ti = proto_tree_add_text zip_tree tvb offset 2 \"Zone for network : %u\" net net_tree = proto_item_add_subtree ti ett_zip_network_list proto_tree_add_item net_tree hf_zip_network tvb offset 2 FALSE offset += 2 len = tvb_get_guint8 tvb offset proto_tree_add_item net_tree hf_zip_zone_name tvb offset 1 FALSE offset += len + 1 @TO@ i = 0 i < count i ++ net = tvb_get_ntohs tvb offset ti = proto_tree_add_text zip_tree tvb offset 2 \"Zone for network : %u\" net net_tree = proto_item_add_subtree ti ett_zip_network_list proto_tree_add_item net_tree hf_zip_network tvb offset 2 FALSE offset += 2 len = tvb_get_guint8 tvb offset proto_tree_add_item net_tree hf_zip_zone_name tvb offset 1 FALSE offset += len + 1 @AT@ 50108 @LENGTH@ 375\n" +
                "---UPD control@@i = 1 i <= count i ++ @TO@ i = 0 i < count i ++ @AT@ 50112 @LENGTH@ 21\n" +
                "------UPD init@@i = 1 @TO@ i = 0 @AT@ 50113 @LENGTH@ 5\n" +
                "---------UPD expr@@i = 1 @TO@ i = 0 @AT@ 50113 @LENGTH@ 5\n" +
                "------------UPD literal:number@@1 @TO@ 0 @AT@ 50116 @LENGTH@ 1\n" +
                "------UPD condition@@i <= count @TO@ i < count @AT@ 50119 @LENGTH@ 10\n" +
                "---------UPD expr@@i <= count @TO@ i < count @AT@ 50119 @LENGTH@ 10\n" +
                "------------UPD operator@@<= @TO@ < @AT@ 50121 @LENGTH@ 2\n");
    }
    @Test
    public void test_php_src_72da689_cba426() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_72da689_cba426_ext#dom#document.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD switch@@type DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; @TO@ type DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; default: @AT@ 38046 @LENGTH@ 300\n" +
                "---UPD block@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; @TO@ DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; default: @AT@ 38060 @LENGTH@ 295\n" +
                "------UPD block_content@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; @TO@ DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; default: @AT@ 38063 @LENGTH@ 295\n" +
                "---------INS default@@default: @TO@ block_content@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; @AT@ 38554 @LENGTH@ 8\n" +
                "---------INS return@@ @TO@ block_content@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid Schema file source\" RETURN_FALSE parser = xmlSchemaNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlSchemaNewMemParserCtxt source source_len break; @AT@ 38565 @LENGTH@ 0\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD switch@@type DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; @TO@ type DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; default: @AT@ 40360 @LENGTH@ 303\n" +
                "---UPD block@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; @TO@ DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; default: @AT@ 40374 @LENGTH@ 298\n" +
                "------UPD block_content@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; @TO@ DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; default: @AT@ 40377 @LENGTH@ 298\n" +
                "---------INS default@@default: @TO@ block_content@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; @AT@ 40891 @LENGTH@ 8\n" +
                "---------INS return@@ @TO@ block_content@@DOM_LOAD_FILE valid_file = _dom_get_valid_file_path source resolved_path MAXPATHLEN  TSRMLS_CC ! valid_file php_error E_WARNING \"Invalid RelaxNG file source\" RETURN_FALSE parser = xmlRelaxNGNewParserCtxt valid_file break; DOM_LOAD_STRING parser = xmlRelaxNGNewMemParserCtxt source source_len break; @AT@ 40902 @LENGTH@ 0\n");
}
    @Test
    public void test_php_src_81f05c1_b1d8f1() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_81f05c1_b1d8f1_ext#mysqli#mysqli_nonapi.c");//can write test
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@unsigned int port 0 @TO@ long port 0 @AT@ 1775 @LENGTH@ 19\n" +
                "---UPD decl@@unsigned int port 0 @TO@ long port 0 @AT@ 1775 @LENGTH@ 19\n" +
                "------UPD type@@unsigned int @TO@ long @AT@ 1775 @LENGTH@ 12\n" +
                "---------UPD name@@unsigned @TO@ long @AT@ 1775 @LENGTH@ 8\n" +
                "---------DEL name@@int @AT@ 1784 @LENGTH@ 3\n");
    }
    @Test
    public void test_wireshark_02dff2_2fba8c() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_02dff2_2fba8c_epan#dissectors#packet-usb.c");//entersan
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@static value_string_ext usb_langid_vals_ext VALUE_STRING_EXT_INIT usb_langid_vals @TO@ value_string_ext usb_langid_vals_ext VALUE_STRING_EXT_INIT usb_langid_vals @AT@ 11326 @LENGTH@ 81\n" +
                "---UPD decl@@static value_string_ext usb_langid_vals_ext VALUE_STRING_EXT_INIT usb_langid_vals @TO@ value_string_ext usb_langid_vals_ext VALUE_STRING_EXT_INIT usb_langid_vals @AT@ 11326 @LENGTH@ 81\n" +
                "------UPD type@@static value_string_ext @TO@ value_string_ext @AT@ 11326 @LENGTH@ 23\n" +
                "---------DEL specifier@@static @AT@ 11326 @LENGTH@ 6\n");
    }
    @Test
    public void test_php_src_5317e8_9068c2() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_5317e8_9068c2_main#php_variables.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@* ip == 0x5b is_array = 1 * ip = 0 is_array = 0 @TO@ * ip == 0x5b is_array = 1 * ip = 0 plain_var @AT@ 10111 @LENGTH@ 47\n" +
                "---UPD else@@is_array = 0 @TO@ plain_var @AT@ 10172 @LENGTH@ 12\n" +
                "------UPD block@@is_array = 0 @TO@ plain_var @AT@ 10177 @LENGTH@ 12\n" +
                "---------UPD block_content@@is_array = 0 @TO@ plain_var @AT@ 10183 @LENGTH@ 12\n" +
                "------------INS goto@@plain_var @TO@ block_content@@is_array = 0 @AT@ 10183 @LENGTH@ 9\n" +
                "---------------INS name@@plain_var @TO@ goto@@plain_var @AT@ 10188 @LENGTH@ 9\n" +
                "------------DEL expr_stmt@@is_array = 0 @AT@ 10183 @LENGTH@ 12\n" +
                "---------------DEL expr@@is_array = 0 @AT@ 10183 @LENGTH@ 12\n" +
                "------------------DEL name@@is_array @AT@ 10183 @LENGTH@ 8\n" +
                "------------------DEL operator@@= @AT@ 10192 @LENGTH@ 1\n" +
                "------------------DEL literal:number@@0 @AT@ 10194 @LENGTH@ 1\n");
    }
    @Test
    public void test_cpython_09705f_db2a0f() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_09705f_db2a0f_Modules#_sre.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@static char copyright [] \" SRE 2.2.1 Copyright (c) 1997-2001 by Secret Labs AB \" @TO@ static char copyright [] \" SRE 2.2.2 Copyright (c) 1997-2002 by Secret Labs AB \" @AT@ 2421 @LENGTH@ 80\n" +
                "---UPD decl@@static char copyright [] \" SRE 2.2.1 Copyright (c) 1997-2001 by Secret Labs AB \" @TO@ static char copyright [] \" SRE 2.2.2 Copyright (c) 1997-2002 by Secret Labs AB \" @AT@ 2421 @LENGTH@ 80\n" +
                "------UPD init@@\" SRE 2.2.1 Copyright (c) 1997-2001 by Secret Labs AB \" @TO@ \" SRE 2.2.2 Copyright (c) 1997-2002 by Secret Labs AB \" @AT@ 2445 @LENGTH@ 55\n" +
                "---------UPD expr@@\" SRE 2.2.1 Copyright (c) 1997-2001 by Secret Labs AB \" @TO@ \" SRE 2.2.2 Copyright (c) 1997-2002 by Secret Labs AB \" @AT@ 2451 @LENGTH@ 55\n" +
                "------------UPD literal:string@@\" SRE 2.2.1 Copyright (c) 1997-2001 by Secret Labs AB \" @TO@ \" SRE 2.2.2 Copyright (c) 1997-2002 by Secret Labs AB \" @AT@ 2451 @LENGTH@ 55\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD return@@PyString_FromString \"\" @TO@ PySequence_GetSlice pattern 0 0 @AT@ 52066 @LENGTH@ 22\n" +
                "---UPD expr@@PyString_FromString \"\" @TO@ PySequence_GetSlice pattern 0 0 @AT@ 52073 @LENGTH@ 22\n" +
                "------UPD call@@PyString_FromString \"\" @TO@ PySequence_GetSlice pattern 0 0 @AT@ 52073 @LENGTH@ 22\n" +
                "---------UPD name@@PyString_FromString @TO@ PySequence_GetSlice @AT@ 52073 @LENGTH@ 19\n" +
                "---------UPD argument_list@@\"\" @TO@ pattern 0 0 @AT@ 52092 @LENGTH@ 2\n" +
                "------------INS argument@@0 @TO@ argument_list@@\"\" @AT@ 51100 @LENGTH@ 1\n" +
                "---------------INS expr@@0 @TO@ argument@@0 @AT@ 51100 @LENGTH@ 1\n" +
                "------------------INS literal:number@@0 @TO@ expr@@0 @AT@ 51100 @LENGTH@ 1\n" +
                "------------INS argument@@0 @TO@ argument_list@@\"\" @AT@ 51103 @LENGTH@ 1\n" +
                "---------------INS expr@@0 @TO@ argument@@0 @AT@ 51103 @LENGTH@ 1\n" +
                "------------------INS literal:number@@0 @TO@ expr@@0 @AT@ 51103 @LENGTH@ 1\n" +
                "------------UPD argument@@\"\" @TO@ pattern @AT@ 52093 @LENGTH@ 2\n" +
                "---------------UPD expr@@\"\" @TO@ pattern @AT@ 52093 @LENGTH@ 2\n" +
                "------------------INS name@@pattern @TO@ expr@@\"\" @AT@ 51091 @LENGTH@ 7\n" +
                "------------------DEL literal:string@@\"\" @AT@ 52093 @LENGTH@ 2\n");
    }
    @Test
    public void test_cpython_03897e_8fcc8e() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_03897e_8fcc8e_Python#sysmodule.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS if@@if builtins == NULL PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @TO@ block@@PyObject * o * outf PyInterpreterState * interp PyThreadState_Get -> interp PyObject * modules interp -> modules PyObject * builtins PyDict_GetItemString modules \"__builtin__\" if ! PyArg_ParseTuple args \"O:displayhook\" & o return NULL if o == Py_None Py_INCREF Py_None return Py_None if PyObject_SetAttrString builtins \"_\" Py_None != 0 return NULL if Py_FlushLine != 0 return NULL outf = PySys_GetObject \"stdout\" if outf == NULL PyErr_SetString PyExc_RuntimeError \"lost sys.stdout\" return NULL if PyFile_WriteObject o outf 0 != 0 return NULL PyFile_SoftSpace outf 1 if Py_FlushLine != 0 return NULL if PyObject_SetAttrString builtins \"_\" o != 0 return NULL Py_INCREF Py_None return Py_None @AT@ 1681 @LENGTH@ 85\n" +
                "---INS condition@@builtins == NULL @TO@ if@@if builtins == NULL PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @AT@ 1681 @LENGTH@ 16\n" +
                "------INS expr@@builtins == NULL @TO@ condition@@builtins == NULL @AT@ 1682 @LENGTH@ 16\n" +
                "---------INS name@@builtins @TO@ expr@@builtins == NULL @AT@ 1682 @LENGTH@ 8\n" +
                "---------INS operator@@== @TO@ expr@@builtins == NULL @AT@ 1691 @LENGTH@ 2\n" +
                "---------INS name@@NULL @TO@ expr@@builtins == NULL @AT@ 1694 @LENGTH@ 4\n" +
                "---INS then@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @TO@ if@@if builtins == NULL PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @AT@ 1700 @LENGTH@ 65\n" +
                "------INS block@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @TO@ then@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @AT@ 1700 @LENGTH@ 65\n" +
                "---------INS expr_stmt@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @TO@ block@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @AT@ 1704 @LENGTH@ 53\n" +
                "------------INS expr@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @TO@ expr_stmt@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @AT@ 1704 @LENGTH@ 53\n" +
                "---------------INS call@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @TO@ expr@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @AT@ 1704 @LENGTH@ 53\n" +
                "------------------INS name@@PyErr_SetString @TO@ call@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @AT@ 1704 @LENGTH@ 15\n" +
                "------------------INS argument_list@@PyExc_RuntimeError \"lost __builtin__\" @TO@ call@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" @AT@ 1719 @LENGTH@ 37\n" +
                "---------------------INS argument@@PyExc_RuntimeError @TO@ argument_list@@PyExc_RuntimeError \"lost __builtin__\" @AT@ 1720 @LENGTH@ 18\n" +
                "------------------------INS expr@@PyExc_RuntimeError @TO@ argument@@PyExc_RuntimeError @AT@ 1720 @LENGTH@ 18\n" +
                "---------------------------INS name@@PyExc_RuntimeError @TO@ expr@@PyExc_RuntimeError @AT@ 1720 @LENGTH@ 18\n" +
                "---------------------INS argument@@\"lost __builtin__\" @TO@ argument_list@@PyExc_RuntimeError \"lost __builtin__\" @AT@ 1740 @LENGTH@ 18\n" +
                "------------------------INS expr@@\"lost __builtin__\" @TO@ argument@@\"lost __builtin__\" @AT@ 1740 @LENGTH@ 18\n" +
                "---------------------------INS literal:string@@\"lost __builtin__\" @TO@ expr@@\"lost __builtin__\" @AT@ 1740 @LENGTH@ 18\n" +
                "---------INS return@@return NULL @TO@ block@@PyErr_SetString PyExc_RuntimeError \"lost __builtin__\" return NULL @AT@ 1763 @LENGTH@ 11\n" +
                "------------INS expr@@NULL @TO@ return@@return NULL @AT@ 1770 @LENGTH@ 4\n" +
                "---------------INS name@@NULL @TO@ expr@@NULL @AT@ 1770 @LENGTH@ 4\n");
    }
    @Test
    public void test_cpython_b110da_5abaa2() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_b110da_5abaa2_Modules#_cursesmodule.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@chtype cch @TO@ chtype cch 0 @AT@ 20374 @LENGTH@ 10\n" +
                "---UPD decl@@chtype cch @TO@ chtype cch 0 @AT@ 20374 @LENGTH@ 10\n" +
                "------INS init@@0 @TO@ decl@@chtype cch @AT@ 20385 @LENGTH@ 1\n" +
                "---------INS expr@@0 @TO@ init@@0 @AT@ 20387 @LENGTH@ 1\n" +
                "------------INS literal:number@@0 @TO@ expr@@0 @AT@ 20387 @LENGTH@ 1\n");
    }
    @Test
    public void test_cpython_516999_437567() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_516999_437567_Modules#posixmodule.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@BOOL result @TO@ BOOL result FALSE @AT@ 42461 @LENGTH@ 11\n" +
                "---UPD decl@@BOOL result @TO@ BOOL result FALSE @AT@ 42461 @LENGTH@ 11\n" +
                "------INS init@@FALSE @TO@ decl@@BOOL result @AT@ 42475 @LENGTH@ 5\n" +
                "---------INS expr@@FALSE @TO@ init@@FALSE @AT@ 42475 @LENGTH@ 5\n" +
                "------------INS name@@FALSE @TO@ expr@@FALSE @AT@ 42475 @LENGTH@ 5\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD block@@if wFileData . cFileName ][0 == L' .' && ( wFileData . cFileName ][1 == L' \\0' || wFileData . cFileName ][1 == L' .' && wFileData . cFileName ][2 == L' \\0') continue; v = PyUnicode_FromUnicode wFileData . cFileName wcslen wFileData . cFileName if v == NULL Py_DECREF d d = NULL break; if PyList_Append d v != 0 Py_DECREF v Py_DECREF d d = NULL break; Py_DECREF v Py_BEGIN_ALLOW_THREADS result FindNextFileW hFindFile & wFileData Py_END_ALLOW_THREADS @TO@ if wFileData . cFileName ][0 == L' .' && ( wFileData . cFileName ][1 == L' \\0' || wFileData . cFileName ][1 == L' .' && wFileData . cFileName ][2 == L' \\0') loop_w v = PyUnicode_FromUnicode wFileData . cFileName wcslen wFileData . cFileName if v == NULL Py_DECREF d d = NULL break; if PyList_Append d v != 0 Py_DECREF v Py_DECREF d d = NULL break; Py_DECREF v loop_w Py_BEGIN_ALLOW_THREADS result FindNextFileW hFindFile & wFileData Py_END_ALLOW_THREADS @AT@ 43591 @LENGTH@ 449\n" +
                "---UPD if@@if wFileData . cFileName ][0 == L' .' && ( wFileData . cFileName ][1 == L' \\0' || wFileData . cFileName ][1 == L' .' && wFileData . cFileName ][2 == L' \\0') continue; @TO@ if wFileData . cFileName ][0 == L' .' && ( wFileData . cFileName ][1 == L' \\0' || wFileData . cFileName ][1 == L' .' && wFileData . cFileName ][2 == L' \\0') loop_w @AT@ 43600 @LENGTH@ 166\n" +
                "------UPD then@@continue; @TO@ loop_w @AT@ 43761 @LENGTH@ 9\n" +
                "---------UPD block@@continue; @TO@ loop_w @AT@ 43761 @LENGTH@ 9\n" +
                "------------DEL continue@@continue; @AT@ 43761 @LENGTH@ 9\n" +
                "------------INS goto@@loop_w @TO@ block@@continue; @AT@ 43769 @LENGTH@ 6\n" +
                "---------------INS name@@loop_w @TO@ goto@@loop_w @AT@ 43774 @LENGTH@ 6\n" +
                "---INS label@@loop_w @TO@ block@@if wFileData . cFileName ][0 == L' .' && ( wFileData . cFileName ][1 == L' \\0' || wFileData . cFileName ][1 == L' .' && wFileData . cFileName ][2 == L' \\0') continue; v = PyUnicode_FromUnicode wFileData . cFileName wcslen wFileData . cFileName if v == NULL Py_DECREF d d = NULL break; if PyList_Append d v != 0 Py_DECREF v Py_DECREF d d = NULL break; Py_DECREF v Py_BEGIN_ALLOW_THREADS result FindNextFileW hFindFile & wFileData Py_END_ALLOW_THREADS @AT@ 44061 @LENGTH@ 6\n" +
                "------INS name@@loop_w @TO@ label@@loop_w @AT@ 44061 @LENGTH@ 6\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD block@@if FileData . cFileName ][0 == '.' && ( FileData . cFileName ][1 == '\\0' || FileData . cFileName ][1 == '.' && FileData . cFileName ][2 == '\\0' ) continue; v = PyString_FromString FileData . cFileName if v == NULL Py_DECREF d d = NULL break; if PyList_Append d v != 0 Py_DECREF v Py_DECREF d d = NULL break; Py_DECREF v Py_BEGIN_ALLOW_THREADS result FindNextFile hFindFile & FileData Py_END_ALLOW_THREADS @TO@ if FileData . cFileName ][0 == '.' && ( FileData . cFileName ][1 == '\\0' || FileData . cFileName ][1 == '.' && FileData . cFileName ][2 == '\\0' ) loop_a v = PyString_FromString FileData . cFileName if v == NULL Py_DECREF d d = NULL break; if PyList_Append d v != 0 Py_DECREF v Py_DECREF d d = NULL break; Py_DECREF v loop_a Py_BEGIN_ALLOW_THREADS result FindNextFile hFindFile & FileData Py_END_ALLOW_THREADS @AT@ 44990 @LENGTH@ 404\n" +
                "---UPD if@@if FileData . cFileName ][0 == '.' && ( FileData . cFileName ][1 == '\\0' || FileData . cFileName ][1 == '.' && FileData . cFileName ][2 == '\\0' ) continue; @TO@ if FileData . cFileName ][0 == '.' && ( FileData . cFileName ][1 == '\\0' || FileData . cFileName ][1 == '.' && FileData . cFileName ][2 == '\\0' ) loop_a @AT@ 44997 @LENGTH@ 155\n" +
                "------UPD then@@continue; @TO@ loop_a @AT@ 45151 @LENGTH@ 9\n" +
                "---------UPD block@@continue; @TO@ loop_a @AT@ 45151 @LENGTH@ 9\n" +
                "------------DEL continue@@continue; @AT@ 45151 @LENGTH@ 9\n" +
                "------------INS goto@@loop_a @TO@ block@@continue; @AT@ 45170 @LENGTH@ 6\n" +
                "---------------INS name@@loop_a @TO@ goto@@loop_a @AT@ 45175 @LENGTH@ 6\n" +
                "---INS label@@loop_a @TO@ block@@if FileData . cFileName ][0 == '.' && ( FileData . cFileName ][1 == '\\0' || FileData . cFileName ][1 == '.' && FileData . cFileName ][2 == '\\0' ) continue; v = PyString_FromString FileData . cFileName if v == NULL Py_DECREF d d = NULL break; if PyList_Append d v != 0 Py_DECREF v Py_DECREF d d = NULL break; Py_DECREF v Py_BEGIN_ALLOW_THREADS result FindNextFile hFindFile & FileData Py_END_ALLOW_THREADS @AT@ 45404 @LENGTH@ 6\n" +
                "------INS name@@loop_a @TO@ label@@loop_a @AT@ 45404 @LENGTH@ 6\n");

    }
    @Test
    public void test_php_src_499b55_595741() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_499b55_595741_Zend#zend_execute_API.c");//wrong

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block_content@@const char * function_name ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name -> val function_name function_name \"main\" @TO@ zend_string * function_name ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name function_name function_name -> val \"main\" @AT@ 11776 @LENGTH@ 162\n" +
                "---UPD decl_stmt@@const char * function_name ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name -> val @TO@ zend_string * function_name ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name @AT@ 11776 @LENGTH@ 127\n" +
                "------UPD decl@@const char * function_name ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name -> val @TO@ zend_string * function_name ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name @AT@ 11776 @LENGTH@ 127\n" +
                "---------UPD type@@const char * @TO@ zend_string * @AT@ 11776 @LENGTH@ 12\n" +
                "------------DEL specifier@@const @AT@ 11776 @LENGTH@ 5\n" +
                "------------UPD name@@char @TO@ zend_string @AT@ 11782 @LENGTH@ 4\n" +
                "---------UPD init@@( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name -> val @TO@ ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name @AT@ 11802 @LENGTH@ 100\n" +
                "------------UPD expr@@( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name -> val @TO@ ( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name @AT@ 11804 @LENGTH@ 100\n" +
                "---------------DEL name@@function_name -> val @AT@ 11875 @LENGTH@ 20\n" +
                "------------------DEL name@@function_name @AT@ 11875 @LENGTH@ 13\n" +
                "------------------DEL operator@@-> @AT@ 11888 @LENGTH@ 2\n" +
                "------------------DEL name@@val @AT@ 11890 @LENGTH@ 3\n" +
                "---------------INS name@@function_name @TO@ expr@@( ( zend_op_array * ) EG current_execute_data -> function_state . function ) -> function_name -> val @AT@ 11876 @LENGTH@ 13\n" +
                "---UPD if_stmt@@function_name function_name \"main\" @TO@ function_name function_name -> val \"main\" @AT@ 11900 @LENGTH@ 34\n" +
                "------UPD if@@function_name function_name @TO@ function_name function_name -> val @AT@ 11900 @LENGTH@ 27\n" +
                "---------UPD block@@function_name @TO@ function_name -> val @AT@ 11919 @LENGTH@ 13\n" +
                "------------UPD block_content@@function_name @TO@ function_name -> val @AT@ 11926 @LENGTH@ 13\n" +
                "---------------UPD return@@function_name @TO@ function_name -> val @AT@ 11926 @LENGTH@ 13\n" +
                "------------------UPD expr@@function_name @TO@ function_name -> val @AT@ 11933 @LENGTH@ 13\n" +
                "---------------------INS name@@function_name -> val @TO@ expr@@function_name @AT@ 11929 @LENGTH@ 20\n" +
                "------------------------INS name@@function_name @TO@ name@@function_name -> val @AT@ 11929 @LENGTH@ 13\n" +
                "------------------------INS operator@@-> @TO@ name@@function_name -> val @AT@ 11942 @LENGTH@ 2\n" +
                "------------------------INS name@@val @TO@ name@@function_name -> val @AT@ 11944 @LENGTH@ 3\n" +
                "---------------------DEL name@@function_name @AT@ 11933 @LENGTH@ 13\n");
    }
    @Test
    public void test_php_src_52ffe2_9b50cf() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_52ffe2_9b50cf_ext#fileinfo#libmagic#cdf.c");//can write test case
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@( size_t ) - 1 @TO@ - 1 @AT@ 10242 @LENGTH@ 14\n" +
                "---UPD expr@@( size_t ) - 1 @TO@ - 1 @AT@ 10249 @LENGTH@ 14\n" +
                "------DEL operator@@( @AT@ 10249 @LENGTH@ 1\n" +
                "------DEL name@@size_t @AT@ 10250 @LENGTH@ 6\n" +
                "------DEL operator@@) @AT@ 10256 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD return@@( size_t ) - 1 @TO@ - 1 @AT@ 11065 @LENGTH@ 14\n" +
                "---UPD expr@@( size_t ) - 1 @TO@ - 1 @AT@ 11072 @LENGTH@ 14\n" +
                "------DEL operator@@( @AT@ 11072 @LENGTH@ 1\n" +
                "------DEL name@@size_t @AT@ 11073 @LENGTH@ 6\n" +
                "------DEL operator@@) @AT@ 11079 @LENGTH@ 1\n");
    }
    @Test
    public void test_cpython_1fc238a_a33099() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_1fc238a_a33099_Objects#dictobject.c");//can write test case
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 17150 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 17157 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 17157 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 17157 @LENGTH@ 4\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 17158 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 17461 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 17468 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 17466 @LENGTH@ 1\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 17467 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 17468 @LENGTH@ 4\n");
    }
    @Test
    public void test_php_src_ea27fd_5645de() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_ea27fd_5645de_ext#fileinfo#libmagic#cdf.c");//can write test case
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@( size_t ) - 1 @TO@ - 1 @AT@ 10242 @LENGTH@ 14\n" +
                "---UPD expr@@( size_t ) - 1 @TO@ - 1 @AT@ 10249 @LENGTH@ 14\n" +
                "------DEL operator@@( @AT@ 10249 @LENGTH@ 1\n" +
                "------DEL name@@size_t @AT@ 10250 @LENGTH@ 6\n" +
                "------DEL operator@@) @AT@ 10256 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD return@@( size_t ) - 1 @TO@ - 1 @AT@ 11065 @LENGTH@ 14\n" +
                "---UPD expr@@( size_t ) - 1 @TO@ - 1 @AT@ 11072 @LENGTH@ 14\n" +
                "------DEL operator@@( @AT@ 11072 @LENGTH@ 1\n" +
                "------DEL name@@size_t @AT@ 11073 @LENGTH@ 6\n" +
                "------DEL operator@@) @AT@ 11079 @LENGTH@ 1\n");
    }
    @Test
    public void test_cpython_ed6219_939667() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_ed6219_939667_Objects#abstract.c");//can write test case
        Assert.assertEquals(hierarchicalActionSets.size(),5);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 18828 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 18835 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 18835 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 18835 @LENGTH@ 4\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 18836 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 19249 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 19256 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 19254 @LENGTH@ 1\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 19255 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 19256 @LENGTH@ 4\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 19728 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 19735 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 19731 @LENGTH@ 1\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 19732 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 19735 @LENGTH@ 4\n");
        Assert.assertEquals(hierarchicalActionSets.get(3).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 20231 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 20238 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 20232 @LENGTH@ 1\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 20233 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 20238 @LENGTH@ 4\n");
        Assert.assertEquals(hierarchicalActionSets.get(4).toString(),"UPD return@@NULL @TO@ - 1 @AT@ 25175 @LENGTH@ 4\n" +
                "---UPD expr@@NULL @TO@ - 1 @AT@ 25182 @LENGTH@ 4\n" +
                "------INS operator@@- @TO@ expr@@NULL @AT@ 25174 @LENGTH@ 1\n" +
                "------INS literal:number@@1 @TO@ expr@@NULL @AT@ 25175 @LENGTH@ 1\n" +
                "------DEL name@@NULL @AT@ 25182 @LENGTH@ 4\n");
    }

    @Test
    public void test_php_src_470a39_b38730() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_470a39_b38730_Zend#zend_stream.c");//can write test case
        Assert.assertEquals(hierarchicalActionSets.size(),0);
//        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD block@@ buf . st_size @TO@  S_ISREG if ! S_ISREG  buf . st_mode  0  buf . st_size @AT@ 2021 @LENGTH@ 27\n" +
//                "---INS ifdef@@S_ISREG @TO@ block@@ buf . st_size @AT@ 2024 @LENGTH@ 7\n" +
//                "------INS directive@@ @TO@ ifdef@@S_ISREG @AT@ 2024 @LENGTH@ 0\n" +
//                "------INS name@@S_ISREG @TO@ ifdef@@S_ISREG @AT@ 2030 @LENGTH@ 7\n" +
//                "---INS if@@if ! S_ISREG  buf . st_mode  0 @TO@ block@@ buf . st_size @AT@ 2043 @LENGTH@ 30\n" +
//                "------INS condition@@ ! S_ISREG  buf . st_mode @TO@ if@@if ! S_ISREG  buf . st_mode  0 @AT@ 2043 @LENGTH@ 24\n" +
//                "---------INS expr@@! S_ISREG  buf . st_mode @TO@ condition@@ ! S_ISREG  buf . st_mode @AT@ 2044 @LENGTH@ 24\n" +
//                "------------INS operator@@! @TO@ expr@@! S_ISREG  buf . st_mode @AT@ 2044 @LENGTH@ 1\n" +
//                "------------INS call@@S_ISREG  buf . st_mode @TO@ expr@@! S_ISREG  buf . st_mode @AT@ 2045 @LENGTH@ 22\n" +
//                "---------------INS name@@S_ISREG @TO@ call@@S_ISREG  buf . st_mode @AT@ 2045 @LENGTH@ 7\n" +
//                "---------------INS argument_list@@ buf . st_mode @TO@ call@@S_ISREG  buf . st_mode @AT@ 2052 @LENGTH@ 14\n" +
//                "------------------INS argument@@buf . st_mode @TO@ argument_list@@ buf . st_mode @AT@ 2053 @LENGTH@ 13\n" +
//                "---------------------INS expr@@buf . st_mode @TO@ argument@@buf . st_mode @AT@ 2053 @LENGTH@ 13\n" +
//                "------------------------INS name@@buf . st_mode @TO@ expr@@buf . st_mode @AT@ 2053 @LENGTH@ 13\n" +
//                "---------------------------INS name@@buf @TO@ name@@buf . st_mode @AT@ 2053 @LENGTH@ 3\n" +
//                "---------------------------INS operator@@. @TO@ name@@buf . st_mode @AT@ 2056 @LENGTH@ 1\n" +
//                "---------------------------INS name@@st_mode @TO@ name@@buf . st_mode @AT@ 2057 @LENGTH@ 7\n" +
//                "------INS then@@ 0 @TO@ if@@if ! S_ISREG  buf . st_mode  0 @AT@ 2067 @LENGTH@ 2\n" +
//                "---------INS block@@ 0 @TO@ then@@ 0 @AT@ 2067 @LENGTH@ 19\n" +
//                "------------INS return@@ 0 @TO@ block@@ 0 @AT@ 2072 @LENGTH@ 10\n" +
//                "---------------INS expr@@0 @TO@ return@@ 0 @AT@ 2079 @LENGTH@ 1\n" +
//                "------------------INS literal@@0 @TO@ expr@@0 @AT@ 2079 @LENGTH@ 1\n" +
//                "---INS endif@@ @TO@ block@@ buf . st_size @AT@ 2087 @LENGTH@ 0\n" +
//                "------INS directive@@ @TO@ endif@@ @AT@ 2087 @LENGTH@ 0\n");
//        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD block@@ buf . st_size @TO@  S_ISREG if ! S_ISREG  buf . st_mode  0  buf . st_size @AT@ 3287 @LENGTH@ 27\n" +
//                "---INS ifdef@@S_ISREG @TO@ block@@ buf . st_size @AT@ 3360 @LENGTH@ 7\n" +
//                "------INS directive@@ @TO@ ifdef@@S_ISREG @AT@ 3360 @LENGTH@ 0\n" +
//                "------INS name@@S_ISREG @TO@ ifdef@@S_ISREG @AT@ 3366 @LENGTH@ 7\n" +
//                "---INS if@@if ! S_ISREG  buf . st_mode  0 @TO@ block@@ buf . st_size @AT@ 3379 @LENGTH@ 30\n" +
//                "------INS condition@@ ! S_ISREG  buf . st_mode @TO@ if@@if ! S_ISREG  buf . st_mode  0 @AT@ 3379 @LENGTH@ 24\n" +
//                "---------INS expr@@! S_ISREG  buf . st_mode @TO@ condition@@ ! S_ISREG  buf . st_mode @AT@ 3380 @LENGTH@ 24\n" +
//                "------------INS operator@@! @TO@ expr@@! S_ISREG  buf . st_mode @AT@ 3380 @LENGTH@ 1\n" +
//                "------------INS call@@S_ISREG  buf . st_mode @TO@ expr@@! S_ISREG  buf . st_mode @AT@ 3381 @LENGTH@ 22\n" +
//                "---------------INS name@@S_ISREG @TO@ call@@S_ISREG  buf . st_mode @AT@ 3381 @LENGTH@ 7\n" +
//                "---------------INS argument_list@@ buf . st_mode @TO@ call@@S_ISREG  buf . st_mode @AT@ 3388 @LENGTH@ 14\n" +
//                "------------------INS argument@@buf . st_mode @TO@ argument_list@@ buf . st_mode @AT@ 3389 @LENGTH@ 13\n" +
//                "---------------------INS expr@@buf . st_mode @TO@ argument@@buf . st_mode @AT@ 3389 @LENGTH@ 13\n" +
//                "------------------------INS name@@buf . st_mode @TO@ expr@@buf . st_mode @AT@ 3389 @LENGTH@ 13\n" +
//                "---------------------------INS name@@buf @TO@ name@@buf . st_mode @AT@ 3389 @LENGTH@ 3\n" +
//                "---------------------------INS operator@@. @TO@ name@@buf . st_mode @AT@ 3392 @LENGTH@ 1\n" +
//                "---------------------------INS name@@st_mode @TO@ name@@buf . st_mode @AT@ 3393 @LENGTH@ 7\n" +
//                "------INS then@@ 0 @TO@ if@@if ! S_ISREG  buf . st_mode  0 @AT@ 3403 @LENGTH@ 2\n" +
//                "---------INS block@@ 0 @TO@ then@@ 0 @AT@ 3403 @LENGTH@ 19\n" +
//                "------------INS return@@ 0 @TO@ block@@ 0 @AT@ 3408 @LENGTH@ 10\n" +
//                "---------------INS expr@@0 @TO@ return@@ 0 @AT@ 3415 @LENGTH@ 1\n" +
//                "------------------INS literal@@0 @TO@ expr@@0 @AT@ 3415 @LENGTH@ 1\n" +
//                "---INS endif@@ @TO@ block@@ buf . st_size @AT@ 3423 @LENGTH@ 0\n" +
//                "------INS directive@@ @TO@ endif@@ @AT@ 3423 @LENGTH@ 0\n");
    }
    @Test
    public void test_php_src_9baa92_f9c232() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_9baa92_f9c232_sapi#apache2handler#sapi_apache2.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),3);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD function@@static void php_apache_request_ctor request_rec * r php_struct * ctx TSRMLS_DC char * content_type char * content_length const char * auth SG sapi_headers . http_response_code = ! r -> status HTTP_OK r -> status SG request_info . content_type = apr_table_get r -> headers_in \"Content-Type\" SG request_info . query_string = apr_pstrdup r -> pool r -> args SG request_info . request_method = r -> method SG request_info . request_uri = apr_pstrdup r -> pool r -> uri SG request_info . path_translated = apr_pstrdup r -> pool r -> filename r -> no_local_copy = 1 content_type = sapi_get_default_content_type TSRMLS_C ap_set_content_type r apr_pstrdup r -> pool content_type efree content_type content_length = ( char * ) apr_table_get r -> headers_in \"Content-Length\" SG request_info . content_length = ( content_length atoi content_length 0 ) apr_table_unset r -> headers_out \"Content-Length\" apr_table_unset r -> headers_out \"Last-Modified\" apr_table_unset r -> headers_out \"Expires\" apr_table_unset r -> headers_out \"ETag\" ! PG safe_mode || ( PG safe_mode && ! ap_auth_type r ) auth = apr_table_get r -> headers_in \"Authorization\" php_handle_auth_data auth TSRMLS_CC ctx -> r -> user = apr_pstrdup ctx -> r -> pool SG request_info . auth_user SG request_info . auth_user = NULL SG request_info . auth_password = NULL php_request_startup TSRMLS_C @TO@ static int php_apache_request_ctor request_rec * r php_struct * ctx TSRMLS_DC char * content_type char * content_length const char * auth SG sapi_headers . http_response_code = ! r -> status HTTP_OK r -> status SG request_info . content_type = apr_table_get r -> headers_in \"Content-Type\" SG request_info . query_string = apr_pstrdup r -> pool r -> args SG request_info . request_method = r -> method SG request_info . request_uri = apr_pstrdup r -> pool r -> uri SG request_info . path_translated = apr_pstrdup r -> pool r -> filename r -> no_local_copy = 1 content_type = sapi_get_default_content_type TSRMLS_C ap_set_content_type r apr_pstrdup r -> pool content_type efree content_type content_length = ( char * ) apr_table_get r -> headers_in \"Content-Length\" SG request_info . content_length = ( content_length atoi content_length 0 ) apr_table_unset r -> headers_out \"Content-Length\" apr_table_unset r -> headers_out \"Last-Modified\" apr_table_unset r -> headers_out \"Expires\" apr_table_unset r -> headers_out \"ETag\" ! PG safe_mode || ( PG safe_mode && ! ap_auth_type r ) auth = apr_table_get r -> headers_in \"Authorization\" php_handle_auth_data auth TSRMLS_CC ctx -> r -> user = apr_pstrdup ctx -> r -> pool SG request_info . auth_user SG request_info . auth_user = NULL SG request_info . auth_password = NULL php_request_startup TSRMLS_C @AT@ 11008 @LENGTH@ 1345\n" +
                "---UPD type@@static void @TO@ static int @AT@ 11008 @LENGTH@ 11\n" +
                "------UPD name@@void @TO@ int @AT@ 11015 @LENGTH@ 4\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"INS return@@php_request_startup TSRMLS_C @TO@ block_content@@char * content_type char * content_length const char * auth SG sapi_headers . http_response_code = ! r -> status HTTP_OK r -> status SG request_info . content_type = apr_table_get r -> headers_in \"Content-Type\" SG request_info . query_string = apr_pstrdup r -> pool r -> args SG request_info . request_method = r -> method SG request_info . request_uri = apr_pstrdup r -> pool r -> uri SG request_info . path_translated = apr_pstrdup r -> pool r -> filename r -> no_local_copy = 1 content_type = sapi_get_default_content_type TSRMLS_C ap_set_content_type r apr_pstrdup r -> pool content_type efree content_type content_length = ( char * ) apr_table_get r -> headers_in \"Content-Length\" SG request_info . content_length = ( content_length atoi content_length 0 ) apr_table_unset r -> headers_out \"Content-Length\" apr_table_unset r -> headers_out \"Last-Modified\" apr_table_unset r -> headers_out \"Expires\" apr_table_unset r -> headers_out \"ETag\" ! PG safe_mode || ( PG safe_mode && ! ap_auth_type r ) auth = apr_table_get r -> headers_in \"Authorization\" php_handle_auth_data auth TSRMLS_CC ctx -> r -> user = apr_pstrdup ctx -> r -> pool SG request_info . auth_user SG request_info . auth_user = NULL SG request_info . auth_password = NULL php_request_startup TSRMLS_C @AT@ 12380 @LENGTH@ 28\n" +
                "---MOV expr@@php_request_startup TSRMLS_C @TO@ return@@php_request_startup TSRMLS_C @AT@ 12381 @LENGTH@ 28\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD if_stmt@@ctx == NULL ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC parent_req = ctx -> r ctx -> r = r brigade = ctx -> brigade @TO@ ctx == NULL ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout parent_req = ctx -> r ctx -> r = r brigade = ctx -> brigade @AT@ 14086 @LENGTH@ 390\n" +
                "---UPD if@@ctx == NULL ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC @TO@ ctx == NULL ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @AT@ 14086 @LENGTH@ 330\n" +
                "------UPD block@@ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC @TO@ ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @AT@ 14103 @LENGTH@ 318\n" +
                "---------UPD block_content@@ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC @TO@ ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @AT@ 14107 @LENGTH@ 318\n" +
                "------------INS if_stmt@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @TO@ block_content@@ctx = SG server_context = apr_pcalloc r -> pool * ctx apr_pool_cleanup_register r -> pool ( void * ) & SG server_context php_server_context_cleanup apr_pool_cleanup_null ctx -> r = r brigade = apr_brigade_create r -> pool r -> connection -> bucket_alloc ctx -> brigade = brigade php_apache_request_ctor r ctx TSRMLS_CC @AT@ 14605 @LENGTH@ 63\n" +
                "---------------INS if@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @TO@ if_stmt@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @AT@ 14605 @LENGTH@ 63\n" +
                "------------------INS condition@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS @TO@ if@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @AT@ 14608 @LENGTH@ 50\n" +
                "---------------------INS expr@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS @TO@ condition@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS @AT@ 14609 @LENGTH@ 50\n" +
                "------------------------MOV macro@@php_apache_request_ctor r ctx TSRMLS_CC @TO@ expr@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS @AT@ 14599 @LENGTH@ 39\n" +
                "------------------------INS operator@@!= @TO@ expr@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS @AT@ 14650 @LENGTH@ 2\n" +
                "------------------------INS name@@SUCCESS @TO@ expr@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS @AT@ 14652 @LENGTH@ 7\n" +
                "------------------INS block@@zend_bailout @TO@ if@@php_apache_request_ctor r ctx TSRMLS_CC != SUCCESS zend_bailout @AT@ 14661 @LENGTH@ 12\n" +
                "---------------------INS block_content@@zend_bailout @TO@ block@@zend_bailout @AT@ 14666 @LENGTH@ 12\n" +
                "------------------------INS expr_stmt@@zend_bailout @TO@ block_content@@zend_bailout @AT@ 14666 @LENGTH@ 12\n" +
                "---------------------------INS expr@@zend_bailout @TO@ expr_stmt@@zend_bailout @AT@ 14666 @LENGTH@ 12\n" +
                "------------------------------INS call@@zend_bailout @TO@ expr@@zend_bailout @AT@ 14666 @LENGTH@ 12\n" +
                "---------------------------------INS name@@zend_bailout @TO@ call@@zend_bailout @AT@ 14666 @LENGTH@ 12\n" +
                "---------------------------------INS argument_list@@ @TO@ call@@zend_bailout @AT@ 14678 @LENGTH@ 0\n");
    }
    @Test
    public void test_cpython_719f5f_dcc6ef() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_719f5f_dcc6ef_Objects#intobject.c");//wrong

        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS return@@newintobject m @TO@ block_content@@long d m i_divmod x y & d & m < 0 NULL newintobject m @AT@ 5112 @LENGTH@ 14\n" +
                "---MOV expr@@newintobject m @TO@ return@@newintobject m @AT@ 5056 @LENGTH@ 14\n");
    }
    @Test
    public void test_php_src_b88698_34b3dc() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_b88698_34b3dc_ext#standard#reg.c");//wrong
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@( char * ) string @TO@ estrndup \"\" 0 @AT@ 9219 @LENGTH@ 17\n" +
                "---UPD expr@@( char * ) string @TO@ estrndup \"\" 0 @AT@ 9226 @LENGTH@ 17\n" +
                "------INS call@@estrndup \"\" 0 @TO@ expr@@( char * ) string @AT@ 9226 @LENGTH@ 13\n" +
                "---------INS name@@estrndup @TO@ call@@estrndup \"\" 0 @AT@ 9226 @LENGTH@ 8\n" +
                "---------INS argument_list@@\"\" 0 @TO@ call@@estrndup \"\" 0 @AT@ 9234 @LENGTH@ 4\n" +
                "------------INS argument@@\"\" @TO@ argument_list@@\"\" 0 @AT@ 9235 @LENGTH@ 2\n" +
                "---------------INS expr@@\"\" @TO@ argument@@\"\" @AT@ 9235 @LENGTH@ 2\n" +
                "------------------INS literal:string@@\"\" @TO@ expr@@\"\" @AT@ 9235 @LENGTH@ 2\n" +
                "------------INS argument@@0 @TO@ argument_list@@\"\" 0 @AT@ 9239 @LENGTH@ 1\n" +
                "---------------INS expr@@0 @TO@ argument@@0 @AT@ 9239 @LENGTH@ 1\n" +
                "------------------INS literal:number@@0 @TO@ expr@@0 @AT@ 9239 @LENGTH@ 1\n" +
                "------DEL operator@@( @AT@ 9226 @LENGTH@ 1\n" +
                "------DEL name@@char @AT@ 9227 @LENGTH@ 4\n" +
                "------DEL operator@@* @AT@ 9232 @LENGTH@ 1\n" +
                "------DEL operator@@) @AT@ 9233 @LENGTH@ 1\n" +
                "------DEL name@@string @AT@ 9234 @LENGTH@ 6\n");
    }

    @Test
    public void test_php_src_395434_d3a0db() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_395434_d3a0db_ext#date#lib#parse_tz.c");//write test case
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD return@@t -> z * 60 @TO@ ( t -> z + t -> dst ) * - 60 @AT@ 10151 @LENGTH@ 11\n" +
                "---UPD expr@@t -> z * 60 @TO@ ( t -> z + t -> dst ) * - 60 @AT@ 10158 @LENGTH@ 11\n" +
                "------INS operator@@( @TO@ expr@@t -> z * 60 @AT@ 10158 @LENGTH@ 1\n" +
                "------INS operator@@+ @TO@ expr@@t -> z * 60 @AT@ 10164 @LENGTH@ 1\n" +
                "------INS name@@t -> dst @TO@ expr@@t -> z * 60 @AT@ 10166 @LENGTH@ 8\n" +
                "---------INS name@@t @TO@ name@@t -> dst @AT@ 10166 @LENGTH@ 1\n" +
                "---------INS operator@@-> @TO@ name@@t -> dst @AT@ 10167 @LENGTH@ 2\n" +
                "---------INS name@@dst @TO@ name@@t -> dst @AT@ 10169 @LENGTH@ 3\n" +
                "------INS operator@@) @TO@ expr@@t -> z * 60 @AT@ 10172 @LENGTH@ 1\n" +
                "------INS operator@@- @TO@ expr@@t -> z * 60 @AT@ 10176 @LENGTH@ 1\n");
    }
    @Test
    public void test_cpython_0aed3a4_404cdc() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_0aed3a4_404cdc_Objects#obmalloc.c");//write test case
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS expr_stmt@@_PyMem_DebugRawFree ctx ptr @TO@ block_content@@_PyMem_DebugCheckGIL _PyMem_DebugRawFree ctx ptr @AT@ 70846 @LENGTH@ 27\n" +
                "---MOV expr@@_PyMem_DebugRawFree ctx ptr @TO@ expr_stmt@@_PyMem_DebugRawFree ctx ptr @AT@ 70853 @LENGTH@ 27\n");
    }

    @Test
    public void test_cpython_671582_f8e322() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_671582_f8e322_PC#_msi.c");//
        Assert.assertEquals(hierarchicalActionSets.size(), 4);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD decl_stmt@@UINT result ( UINT ) _read hf memory cb @TO@ UINT result ( UINT ) _read ( int ) hf memory cb @AT@ 1430 @LENGTH@ 39\n" +
                "---UPD decl@@UINT result ( UINT ) _read hf memory cb @TO@ UINT result ( UINT ) _read ( int ) hf memory cb @AT@ 1430 @LENGTH@ 39\n" +
                "------UPD init@@( UINT ) _read hf memory cb @TO@ ( UINT ) _read ( int ) hf memory cb @AT@ 1442 @LENGTH@ 27\n" +
                "---------UPD expr@@( UINT ) _read hf memory cb @TO@ ( UINT ) _read ( int ) hf memory cb @AT@ 1444 @LENGTH@ 27\n" +
                "------------UPD call@@_read hf memory cb @TO@ _read ( int ) hf memory cb @AT@ 1450 @LENGTH@ 18\n" +
                "---------------UPD argument_list@@hf memory cb @TO@ ( int ) hf memory cb @AT@ 1455 @LENGTH@ 12\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1456 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1456 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1456 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1457 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1460 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD expr_stmt@@static FNFCIWRITE cb_write UINT result = ( UINT ) _write hf memory cb result != cb * err = errno @TO@ static FNFCIWRITE cb_write UINT result = ( UINT ) _write ( int ) hf memory cb result != cb * err = errno @AT@ 1539 @LENGTH@ 96\n" +
                "---UPD expr@@static FNFCIWRITE cb_write UINT result = ( UINT ) _write hf memory cb result != cb * err = errno @TO@ static FNFCIWRITE cb_write UINT result = ( UINT ) _write ( int ) hf memory cb result != cb * err = errno @AT@ 1539 @LENGTH@ 96\n" +
                "------UPD block@@UINT result = ( UINT ) _write hf memory cb result != cb * err = errno @TO@ UINT result = ( UINT ) _write ( int ) hf memory cb result != cb * err = errno @AT@ 1567 @LENGTH@ 69\n" +
                "---------UPD expr@@UINT result = ( UINT ) _write hf memory cb @TO@ UINT result = ( UINT ) _write ( int ) hf memory cb @AT@ 1573 @LENGTH@ 42\n" +
                "------------UPD call@@_write hf memory cb @TO@ _write ( int ) hf memory cb @AT@ 1593 @LENGTH@ 19\n" +
                "---------------UPD argument_list@@hf memory cb @TO@ ( int ) hf memory cb @AT@ 1599 @LENGTH@ 12\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1600 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1600 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1605 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1606 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1609 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD decl_stmt@@int result _close hf @TO@ int result _close ( int ) hf @AT@ 1717 @LENGTH@ 20\n" +
                "---UPD decl@@int result _close hf @TO@ int result _close ( int ) hf @AT@ 1717 @LENGTH@ 20\n" +
                "------UPD init@@_close hf @TO@ _close ( int ) hf @AT@ 1728 @LENGTH@ 9\n" +
                "---------UPD expr@@_close hf @TO@ _close ( int ) hf @AT@ 1730 @LENGTH@ 9\n" +
                "------------UPD call@@_close hf @TO@ _close ( int ) hf @AT@ 1730 @LENGTH@ 9\n" +
                "---------------UPD argument_list@@hf @TO@ ( int ) hf @AT@ 1736 @LENGTH@ 2\n" +
                "------------------UPD argument@@hf @TO@ ( int ) hf @AT@ 1737 @LENGTH@ 2\n" +
                "---------------------UPD expr@@hf @TO@ ( int ) hf @AT@ 1737 @LENGTH@ 2\n" +
                "------------------------INS operator@@( @TO@ expr@@hf @AT@ 1747 @LENGTH@ 1\n" +
                "------------------------INS name@@int @TO@ expr@@hf @AT@ 1748 @LENGTH@ 3\n" +
                "------------------------INS operator@@) @TO@ expr@@hf @AT@ 1751 @LENGTH@ 1\n");
        Assert.assertEquals(hierarchicalActionSets.get(3).toString(),"UPD expr_stmt@@static FNFCISEEK cb_seek long result = ( long ) _lseek hf dist seektype result == - 1 * err = errno @TO@ static FNFCISEEK cb_seek long result = ( long ) _lseek ( int ) hf dist seektype result == - 1 * err = errno @AT@ 1807 @LENGTH@ 99\n" +
                "---UPD expr@@static FNFCISEEK cb_seek long result = ( long ) _lseek hf dist seektype result == - 1 * err = errno @TO@ static FNFCISEEK cb_seek long result = ( long ) _lseek ( int ) hf dist seektype result == - 1 * err = errno @AT@ 1807 @LENGTH@ 99\n" +
                "------UPD block@@long result = ( long ) _lseek hf dist seektype result == - 1 * err = errno @TO@ long result = ( long ) _lseek ( int ) hf dist seektype result == - 1 * err = errno @AT@ 1833 @LENGTH@ 74\n" +
                "---------UPD expr@@long result = ( long ) _lseek hf dist seektype @TO@ long result = ( long ) _lseek ( int ) hf dist seektype @AT@ 1839 @LENGTH@ 46\n" +
                "------------UPD call@@_lseek hf dist seektype @TO@ _lseek ( int ) hf dist seektype @AT@ 1859 @LENGTH@ 23\n" +
                "---------------UPD argument_list@@hf dist seektype @TO@ ( int ) hf dist seektype @AT@ 1865 @LENGTH@ 16\n" +
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
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@( code >= 0x80 && code < 0xa0 ) || code > 0xff invalid_code = 1 * ( q ++ ) = code @TO@ ( code >= 0x80 && code < 0xa0 ) || code > 0xff invalid_code = 1 code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @AT@ 31473 @LENGTH@ 81\n" +
                "---INS else@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @TO@ if_stmt@@( code >= 0x80 && code < 0xa0 ) || code > 0xff invalid_code = 1 * ( q ++ ) = code @AT@ 31562 @LENGTH@ 62\n" +
                "------INS block@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @TO@ else@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @AT@ 31567 @LENGTH@ 62\n" +
                "---------INS block_content@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @TO@ block@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @AT@ 31578 @LENGTH@ 62\n" +
                "------------INS if_stmt@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @TO@ block_content@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @AT@ 31578 @LENGTH@ 62\n" +
                "---------------MOV else@@* ( q ++ ) = code @TO@ if_stmt@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @AT@ 31562 @LENGTH@ 17\n" +
                "---------------INS if@@code == 39 || ! quote_style invalid_code = 1 @TO@ if_stmt@@code == 39 || ! quote_style invalid_code = 1 * ( q ++ ) = code @AT@ 31578 @LENGTH@ 44\n" +
                "------------------INS condition@@code == 39 || ! quote_style @TO@ if@@code == 39 || ! quote_style invalid_code = 1 @AT@ 31581 @LENGTH@ 27\n" +
                "---------------------INS expr@@code == 39 || ! quote_style @TO@ condition@@code == 39 || ! quote_style @AT@ 31582 @LENGTH@ 27\n" +
                "------------------------INS name@@code @TO@ expr@@code == 39 || ! quote_style @AT@ 31582 @LENGTH@ 4\n" +
                "------------------------INS operator@@== @TO@ expr@@code == 39 || ! quote_style @AT@ 31587 @LENGTH@ 2\n" +
                "------------------------INS literal:number@@39 @TO@ expr@@code == 39 || ! quote_style @AT@ 31590 @LENGTH@ 2\n" +
                "------------------------INS operator@@|| @TO@ expr@@code == 39 || ! quote_style @AT@ 31593 @LENGTH@ 2\n" +
                "------------------------INS operator@@! @TO@ expr@@code == 39 || ! quote_style @AT@ 31596 @LENGTH@ 1\n" +
                "------------------------INS name@@quote_style @TO@ expr@@code == 39 || ! quote_style @AT@ 31597 @LENGTH@ 11\n" +
                "------------------INS block@@invalid_code = 1 @TO@ if@@code == 39 || ! quote_style invalid_code = 1 @AT@ 31610 @LENGTH@ 16\n" +
                "---------------------INS block_content@@invalid_code = 1 @TO@ block@@invalid_code = 1 @AT@ 31622 @LENGTH@ 16\n" +
                "------------------------INS expr_stmt@@invalid_code = 1 @TO@ block_content@@invalid_code = 1 @AT@ 31622 @LENGTH@ 16\n" +
                "---------------------------INS expr@@invalid_code = 1 @TO@ expr_stmt@@invalid_code = 1 @AT@ 31622 @LENGTH@ 16\n" +
                "------------------------------INS name@@invalid_code @TO@ expr@@invalid_code = 1 @AT@ 31622 @LENGTH@ 12\n" +
                "------------------------------INS operator@@= @TO@ expr@@invalid_code = 1 @AT@ 31635 @LENGTH@ 1\n" +
                "------------------------------INS literal:number@@1 @TO@ expr@@invalid_code = 1 @AT@ 31637 @LENGTH@ 1\n");
    }
    @Test
    public void test_wireshark_c3a535_1153ff() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("wireshark_c3a535_1153ff_print.c");//
        Assert.assertEquals(hierarchicalActionSets.size(), 1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@fi -> hfinfo -> id != proto_data fi -> hfinfo -> type == FT_PROTOCOL fputs \"</proto>\\n\" pdata -> fh fputs \"</field>\\n\" pdata -> fh @TO@ fi -> hfinfo -> id != proto_data fi -> hfinfo -> type == FT_PROTOCOL fputs \"</proto>\\n\" pdata -> fh fputs \"</field>\\n\" pdata -> fh fputs \"</field>\\n\" pdata -> fh @AT@ 13773 @LENGTH@ 130\n" +
                "---INS else@@fputs \"</field>\\n\" pdata -> fh @TO@ if_stmt@@fi -> hfinfo -> id != proto_data fi -> hfinfo -> type == FT_PROTOCOL fputs \"</proto>\\n\" pdata -> fh fputs \"</field>\\n\" pdata -> fh @AT@ 13986 @LENGTH@ 30\n" +
                "------INS block@@fputs \"</field>\\n\" pdata -> fh @TO@ else@@fputs \"</field>\\n\" pdata -> fh @AT@ 13991 @LENGTH@ 30\n" +
                "---------INS block_content@@fputs \"</field>\\n\" pdata -> fh @TO@ block@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 30\n" +
                "------------INS expr_stmt@@fputs \"</field>\\n\" pdata -> fh @TO@ block_content@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 30\n" +
                "---------------INS expr@@fputs \"</field>\\n\" pdata -> fh @TO@ expr_stmt@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 30\n" +
                "------------------INS call@@fputs \"</field>\\n\" pdata -> fh @TO@ expr@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 30\n" +
                "---------------------INS name@@fputs @TO@ call@@fputs \"</field>\\n\" pdata -> fh @AT@ 13996 @LENGTH@ 5\n" +
                "---------------------INS argument_list@@\"</field>\\n\" pdata -> fh @TO@ call@@fputs \"</field>\\n\" pdata -> fh @AT@ 14001 @LENGTH@ 24\n" +
                "------------------------INS argument@@\"</field>\\n\" @TO@ argument_list@@\"</field>\\n\" pdata -> fh @AT@ 14002 @LENGTH@ 12\n" +
                "---------------------------INS expr@@\"</field>\\n\" @TO@ argument@@\"</field>\\n\" @AT@ 14002 @LENGTH@ 12\n" +
                "------------------------------INS literal:string@@\"</field>\\n\" @TO@ expr@@\"</field>\\n\" @AT@ 14002 @LENGTH@ 12\n" +
                "------------------------INS argument@@pdata -> fh @TO@ argument_list@@\"</field>\\n\" pdata -> fh @AT@ 14016 @LENGTH@ 11\n" +
                "---------------------------INS expr@@pdata -> fh @TO@ argument@@pdata -> fh @AT@ 14016 @LENGTH@ 11\n" +
                "------------------------------INS name@@pdata -> fh @TO@ expr@@pdata -> fh @AT@ 14016 @LENGTH@ 11\n" +
                "---------------------------------INS name@@pdata @TO@ name@@pdata -> fh @AT@ 14016 @LENGTH@ 5\n" +
                "---------------------------------INS operator@@-> @TO@ name@@pdata -> fh @AT@ 14021 @LENGTH@ 2\n" +
                "---------------------------------INS name@@fh @TO@ name@@pdata -> fh @AT@ 14023 @LENGTH@ 2\n");
    }
    @Test
    public void test_php_src_a10e77_634012() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_a10e77_634012_ext#phar#tar.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@! last_was_longlink && hdr -> typeflag == 'L' last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; ! last_was_longlink && ! old && hdr -> prefix ][0 != 0 char name ][256 int i j i = 0 i < 155 i ++ name ][i = hdr -> prefix ][i name ][i == '\\0' break; name ][i ++ = '/' j = 0 j < 100 j ++ name ][i + j = hdr -> name ][j name ][i + j == '\\0' break; entry . filename_len = i + j name ][entry . filename_len - 1 == '/' entry . filename_len -- entry . filename = pestrndup name entry . filename_len myphar -> is_persistent ! last_was_longlink int i i = 0 i < 100 i ++ hdr -> name ][i == '\\0' break; entry . filename_len = i entry . filename = pestrndup hdr -> name i myphar -> is_persistent entry . filename ][entry . filename_len - 1 == '/' entry . filename ][entry . filename_len - 1 = '\\0' entry . filename_len -- @TO@ ! last_was_longlink && hdr -> typeflag == 'L' last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; ! last_was_longlink && ! old && hdr -> prefix ][0 != 0 char name ][256 int i j i = 0 i < 155 i ++ name ][i = hdr -> prefix ][i name ][i == '\\0' break; name ][i ++ = '/' j = 0 j < 100 j ++ name ][i + j = hdr -> name ][j name ][i + j == '\\0' break; entry . filename_len = i + j name ][entry . filename_len - 1 == '/' entry . filename_len -- entry . filename = pestrndup name entry . filename_len myphar -> is_persistent ! last_was_longlink int i i = 0 i < 100 i ++ hdr -> name ][i == '\\0' break; entry . filename_len = i entry . filename = pestrndup hdr -> name i myphar -> is_persistent entry . filename ][entry . filename_len - 1 == '/' entry . filename ][entry . filename_len - 1 = '\\0' entry . filename_len -- @AT@ 11266 @LENGTH@ 1759\n" +
                "---UPD if@@! last_was_longlink && hdr -> typeflag == 'L' last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @TO@ ! last_was_longlink && hdr -> typeflag == 'L' last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @AT@ 11266 @LENGTH@ 1047\n" +
                "------UPD block@@last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @TO@ last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @AT@ 11314 @LENGTH@ 1001\n" +
                "---------UPD block_content@@last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @TO@ last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @AT@ 11319 @LENGTH@ 1001\n" +
                "------------INS if_stmt@@entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ block_content@@last_was_longlink = 1 entry . filename_len = entry . uncompressed_filesize entry . filename = pemalloc entry . filename_len + 1 myphar -> is_persistent read = php_stream_read fp entry . filename entry . filename_len read != entry . filename_len efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE entry . filename ][entry . filename_len = '\\0' size = ( ( size + 511 ) & ~ 511 ) - size php_stream_seek fp size SEEK_CUR ( uint ) php_stream_tell fp > totalsize efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE read = php_stream_read fp buf buf read != buf efree entry . filename error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (truncated)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE continue; @AT@ 11508 @LENGTH@ 198\n" +
                "---------------INS if@@entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ if_stmt@@entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11508 @LENGTH@ 198\n" +
                "------------------INS condition@@entry . filename_len == UINT_MAX @TO@ if@@entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11511 @LENGTH@ 32\n" +
                "---------------------INS expr@@entry . filename_len == UINT_MAX @TO@ condition@@entry . filename_len == UINT_MAX @AT@ 11512 @LENGTH@ 32\n" +
                "------------------------INS name@@entry . filename_len @TO@ expr@@entry . filename_len == UINT_MAX @AT@ 11512 @LENGTH@ 20\n" +
                "---------------------------INS name@@entry @TO@ name@@entry . filename_len @AT@ 11512 @LENGTH@ 5\n" +
                "---------------------------INS operator@@. @TO@ name@@entry . filename_len @AT@ 11517 @LENGTH@ 1\n" +
                "---------------------------INS name@@filename_len @TO@ name@@entry . filename_len @AT@ 11518 @LENGTH@ 12\n" +
                "------------------------INS operator@@== @TO@ expr@@entry . filename_len == UINT_MAX @AT@ 11531 @LENGTH@ 2\n" +
                "------------------------INS name@@UINT_MAX @TO@ expr@@entry . filename_len == UINT_MAX @AT@ 11534 @LENGTH@ 8\n" +
                "------------------INS block@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ if@@entry . filename_len == UINT_MAX error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11544 @LENGTH@ 165\n" +
                "---------------------INS block_content@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @TO@ block@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11550 @LENGTH@ 165\n" +
                "------------------------INS if_stmt@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ block_content@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11550 @LENGTH@ 97\n" +
                "---------------------------INS if@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ if_stmt@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11550 @LENGTH@ 97\n" +
                "------------------------------INS condition@@error @TO@ if@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11553 @LENGTH@ 5\n" +
                "---------------------------------INS expr@@error @TO@ condition@@error @AT@ 11554 @LENGTH@ 5\n" +
                "------------------------------------INS name@@error @TO@ expr@@error @AT@ 11554 @LENGTH@ 5\n" +
                "------------------------------INS block@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ if@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11561 @LENGTH@ 91\n" +
                "---------------------------------INS block_content@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ block@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 91\n" +
                "------------------------------------INS expr_stmt@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ block_content@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 91\n" +
                "---------------------------------------INS expr@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ expr_stmt@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 91\n" +
                "------------------------------------------INS call@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ expr@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 91\n" +
                "---------------------------------------------INS name@@spprintf @TO@ call@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11568 @LENGTH@ 8\n" +
                "---------------------------------------------INS argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @TO@ call@@spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11576 @LENGTH@ 82\n" +
                "------------------------------------------------INS argument@@error @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11577 @LENGTH@ 5\n" +
                "---------------------------------------------------INS expr@@error @TO@ argument@@error @AT@ 11577 @LENGTH@ 5\n" +
                "------------------------------------------------------INS name@@error @TO@ expr@@error @AT@ 11577 @LENGTH@ 5\n" +
                "------------------------------------------------INS argument@@4096 @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11584 @LENGTH@ 4\n" +
                "---------------------------------------------------INS expr@@4096 @TO@ argument@@4096 @AT@ 11584 @LENGTH@ 4\n" +
                "------------------------------------------------------INS literal:number@@4096 @TO@ expr@@4096 @AT@ 11584 @LENGTH@ 4\n" +
                "------------------------------------------------INS argument@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11590 @LENGTH@ 65\n" +
                "---------------------------------------------------INS expr@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @TO@ argument@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @AT@ 11590 @LENGTH@ 65\n" +
                "------------------------------------------------------INS literal:string@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @TO@ expr@@\"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" @AT@ 11590 @LENGTH@ 65\n" +
                "------------------------------------------------INS argument@@fname @TO@ argument_list@@error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname @AT@ 11657 @LENGTH@ 5\n" +
                "---------------------------------------------------INS expr@@fname @TO@ argument@@fname @AT@ 11657 @LENGTH@ 5\n" +
                "------------------------------------------------------INS name@@fname @TO@ expr@@fname @AT@ 11657 @LENGTH@ 5\n" +
                "------------------------INS expr_stmt@@php_stream_close fp @TO@ block_content@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11675 @LENGTH@ 19\n" +
                "---------------------------INS expr@@php_stream_close fp @TO@ expr_stmt@@php_stream_close fp @AT@ 11675 @LENGTH@ 19\n" +
                "------------------------------INS call@@php_stream_close fp @TO@ expr@@php_stream_close fp @AT@ 11675 @LENGTH@ 19\n" +
                "---------------------------------INS name@@php_stream_close @TO@ call@@php_stream_close fp @AT@ 11675 @LENGTH@ 16\n" +
                "---------------------------------INS argument_list@@fp @TO@ call@@php_stream_close fp @AT@ 11691 @LENGTH@ 2\n" +
                "------------------------------------INS argument@@fp @TO@ argument_list@@fp @AT@ 11692 @LENGTH@ 2\n" +
                "---------------------------------------INS expr@@fp @TO@ argument@@fp @AT@ 11692 @LENGTH@ 2\n" +
                "------------------------------------------INS name@@fp @TO@ expr@@fp @AT@ 11692 @LENGTH@ 2\n" +
                "------------------------INS macro@@phar_destroy_phar_data myphar TSRMLS_CC @TO@ block_content@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11701 @LENGTH@ 39\n" +
                "---------------------------INS name@@phar_destroy_phar_data @TO@ macro@@phar_destroy_phar_data myphar TSRMLS_CC @AT@ 11701 @LENGTH@ 22\n" +
                "---------------------------INS argument_list@@myphar TSRMLS_CC @TO@ macro@@phar_destroy_phar_data myphar TSRMLS_CC @AT@ 11723 @LENGTH@ 16\n" +
                "------------------------------INS argument@@myphar TSRMLS_CC @TO@ argument_list@@myphar TSRMLS_CC @AT@ 11724 @LENGTH@ 16\n" +
                "------------------------INS return@@FAILURE @TO@ block_content@@error spprintf error 4096 \"phar error: \\\"%s\\\" is a corrupted tar file (invalid entry size)\" fname php_stream_close fp phar_destroy_phar_data myphar TSRMLS_CC FAILURE @AT@ 11747 @LENGTH@ 7\n" +
                "---------------------------INS expr@@FAILURE @TO@ return@@FAILURE @AT@ 11754 @LENGTH@ 7\n" +
                "------------------------------INS name@@FAILURE @TO@ expr@@FAILURE @AT@ 11754 @LENGTH@ 7\n");
    }
    @Test//TODO
    public void test_php_src_c71358_283565() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c71358_283565_main#main.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) FAILURE @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) FAILURE @AT@ 8469 @LENGTH@ 151\n" +
                "---UPD if@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) FAILURE @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value PG safe_mode && ( ! php_checkuid new_value NULL CHECKUID_CHECK_FILE_AND_DIR ) FAILURE @AT@ 8469 @LENGTH@ 151\n" +
                "------UPD condition@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value @AT@ 8472 @LENGTH@ 65\n" +
                "---------UPD expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @TO@ ( stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS ) && new_value @AT@ 8473 @LENGTH@ 65\n" +
                "------------INS operator@@( @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8484 @LENGTH@ 1\n" +
                "------------INS operator@@) @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8550 @LENGTH@ 1\n" +
                "------------INS operator@@&& @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8552 @LENGTH@ 2\n" +
                "------------INS name@@new_value @TO@ expr@@stage == PHP_INI_STAGE_RUNTIME || stage == PHP_INI_STAGE_HTACCESS @AT@ 8555 @LENGTH@ 9\n");
    }

    @Test
    public void test_php_src_920358_bd306c() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_920358_bd306c_ext#dom#xpath.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@xpathobjp -> type == XPATH_NODESET int i xmlNodeSetPtr nodesetp NULL == ( nodesetp = xpathobjp -> nodesetval ) xmlXPathFreeObject xpathobjp RETURN_FALSE MAKE_STD_ZVAL retval array_init retval i = 0 i < nodesetp -> nodeNr i ++ xmlNodePtr node nodesetp -> nodeTab ][i zval * child MAKE_STD_ZVAL child node -> type == XML_NAMESPACE_DECL xmlNsPtr curns xmlNodePtr nsparent nsparent = node -> _private curns = xmlNewNs NULL node -> name NULL node -> children curns -> prefix = xmlStrdup ( char * ) node -> children node -> children node = xmlNewDocNode docp NULL ( char * ) node -> children node -> name node = xmlNewDocNode docp NULL \"xmlns\" node -> name node -> type = XML_NAMESPACE_DECL node -> parent = nsparent node -> ns = curns child = php_dom_create_object node &ret NULL child intern TSRMLS_CC add_next_index_zval retval child @TO@ xpathobjp -> type == XPATH_NODESET int i xmlNodeSetPtr nodesetp NULL == ( nodesetp = xpathobjp -> nodesetval ) xmlXPathFreeObject xpathobjp RETURN_FALSE MAKE_STD_ZVAL retval array_init retval i = 0 i < nodesetp -> nodeNr i ++ xmlNodePtr node nodesetp -> nodeTab ][i zval * child MAKE_STD_ZVAL child node -> type == XML_NAMESPACE_DECL xmlNsPtr curns xmlNodePtr nsparent nsparent = node -> _private curns = xmlNewNs NULL node -> name NULL node -> children curns -> prefix = xmlStrdup ( char * ) node -> children node -> children node = xmlNewDocNode docp NULL ( char * ) node -> children node -> name node = xmlNewDocNode docp NULL \"xmlns\" node -> name node -> type = XML_NAMESPACE_DECL node -> parent = nsparent node -> ns = curns child = php_dom_create_object node &ret NULL child intern TSRMLS_CC add_next_index_zval retval child RETURN_FALSE @AT@ 5404 @LENGTH@ 830\n" +
                "---INS else@@RETURN_FALSE @TO@ if_stmt@@xpathobjp -> type == XPATH_NODESET int i xmlNodeSetPtr nodesetp NULL == ( nodesetp = xpathobjp -> nodesetval ) xmlXPathFreeObject xpathobjp RETURN_FALSE MAKE_STD_ZVAL retval array_init retval i = 0 i < nodesetp -> nodeNr i ++ xmlNodePtr node nodesetp -> nodeTab ][i zval * child MAKE_STD_ZVAL child node -> type == XML_NAMESPACE_DECL xmlNsPtr curns xmlNodePtr nsparent nsparent = node -> _private curns = xmlNewNs NULL node -> name NULL node -> children curns -> prefix = xmlStrdup ( char * ) node -> children node -> children node = xmlNewDocNode docp NULL ( char * ) node -> children node -> name node = xmlNewDocNode docp NULL \"xmlns\" node -> name node -> type = XML_NAMESPACE_DECL node -> parent = nsparent node -> ns = curns child = php_dom_create_object node &ret NULL child intern TSRMLS_CC add_next_index_zval retval child @AT@ 6422 @LENGTH@ 12\n" +
                "------INS block@@RETURN_FALSE @TO@ else@@RETURN_FALSE @AT@ 6427 @LENGTH@ 12\n" +
                "---------INS block_content@@RETURN_FALSE @TO@ block@@RETURN_FALSE @AT@ 6431 @LENGTH@ 12\n" +
                "------------INS expr_stmt@@RETURN_FALSE @TO@ block_content@@RETURN_FALSE @AT@ 6431 @LENGTH@ 12\n" +
                "---------------INS expr@@RETURN_FALSE @TO@ expr_stmt@@RETURN_FALSE @AT@ 6431 @LENGTH@ 12\n" +
                "------------------INS name@@RETURN_FALSE @TO@ expr@@RETURN_FALSE @AT@ 6431 @LENGTH@ 12\n");
    }
    @Test
    public void test_php_src_c3c87e_c9c279() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c3c87e_c9c279_ext#zip#php_zip.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD if_stmt@@stream php_stream_to_zval stream return_value @TO@ stream php_stream_to_zval stream return_value RETURN_FALSE @AT@ 64714 @LENGTH@ 45\n" +
                "---INS else@@RETURN_FALSE @TO@ if_stmt@@stream php_stream_to_zval stream return_value @AT@ 64775 @LENGTH@ 12\n" +
                "------INS block@@RETURN_FALSE @TO@ else@@RETURN_FALSE @AT@ 64780 @LENGTH@ 12\n" +
                "---------INS block_content@@RETURN_FALSE @TO@ block@@RETURN_FALSE @AT@ 64784 @LENGTH@ 12\n" +
                "------------INS expr_stmt@@RETURN_FALSE @TO@ block_content@@RETURN_FALSE @AT@ 64784 @LENGTH@ 12\n" +
                "---------------INS expr@@RETURN_FALSE @TO@ expr_stmt@@RETURN_FALSE @AT@ 64784 @LENGTH@ 12\n" +
                "------------------INS name@@RETURN_FALSE @TO@ expr@@RETURN_FALSE @AT@ 64784 @LENGTH@ 12\n");
    }
    @Test
    public void test_php_src_c4ee76_2a1218() throws IOException {
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_c4ee76_2a1218_ext#ftp#ftp.c");//
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ block@@ctx = SSL_CTX_new SSLv23_client_method if ctx == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"failed to create the SSL context\" return 0 ftp -> ssl_handle = SSL_new ctx if ftp -> ssl_handle == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"failed to create the SSL handle\" SSL_CTX_free ctx return 0 SSL_set_fd ftp -> ssl_handle ftp -> fd if SSL_connect ftp -> ssl_handle <= 0 php_error_docref NULL TSRMLS_CC E_WARNING \"SSL/TLS handshake failed\" SSL_shutdown ftp -> ssl_handle return 0 ftp -> ssl_active = 1 if ! ftp -> old_ssl if ! ftp_putcmd ftp \"PBSZ\" \"0\" return 0 if ! ftp_getresp ftp return 0 if ! ftp_putcmd ftp \"PROT\" \"P\" return 0 if ! ftp_getresp ftp return 0 ftp -> use_ssl_for_data = ( ftp -> resp >= 200 && ftp -> resp <= 299 ) @AT@ 6309 @LENGTH@ 34\n" +
                "---INS expr@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6309 @LENGTH@ 34\n" +
                "------INS call@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6309 @LENGTH@ 34\n" +
                "---------INS name@@SSL_CTX_set_options @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6309 @LENGTH@ 19\n" +
                "---------INS argument_list@@ctx SSL_OP_ALL @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 6328 @LENGTH@ 14\n" +
                "------------INS argument@@ctx @TO@ argument_list@@ctx SSL_OP_ALL @AT@ 6329 @LENGTH@ 3\n" +
                "---------------INS expr@@ctx @TO@ argument@@ctx @AT@ 6329 @LENGTH@ 3\n" +
                "------------------INS name@@ctx @TO@ expr@@ctx @AT@ 6329 @LENGTH@ 3\n" +
                "------------INS argument@@SSL_OP_ALL @TO@ argument_list@@ctx SSL_OP_ALL @AT@ 6334 @LENGTH@ 10\n" +
                "---------------INS expr@@SSL_OP_ALL @TO@ argument@@SSL_OP_ALL @AT@ 6334 @LENGTH@ 10\n" +
                "------------------INS name@@SSL_OP_ALL @TO@ expr@@SSL_OP_ALL @AT@ 6334 @LENGTH@ 10\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"INS expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ block@@ctx = SSL_CTX_new SSLv23_client_method if ctx == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"data_accept: failed to create the SSL context\" return 0 data -> ssl_handle = SSL_new ctx if data -> ssl_handle == NULL php_error_docref NULL TSRMLS_CC E_WARNING \"data_accept: failed to create the SSL handle\" SSL_CTX_free ctx return 0 SSL_set_fd data -> ssl_handle data -> fd if ftp -> old_ssl SSL_copy_session_id data -> ssl_handle ftp -> ssl_handle if SSL_connect data -> ssl_handle <= 0 php_error_docref NULL TSRMLS_CC E_WARNING \"data_accept: SSL/TLS handshake failed\" SSL_shutdown data -> ssl_handle return 0 data -> ssl_active = 1 @AT@ 28312 @LENGTH@ 34\n" +
                "---INS expr@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr_stmt@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28312 @LENGTH@ 34\n" +
                "------INS call@@SSL_CTX_set_options ctx SSL_OP_ALL @TO@ expr@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28312 @LENGTH@ 34\n" +
                "---------INS name@@SSL_CTX_set_options @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28312 @LENGTH@ 19\n" +
                "---------INS argument_list@@ctx SSL_OP_ALL @TO@ call@@SSL_CTX_set_options ctx SSL_OP_ALL @AT@ 28331 @LENGTH@ 14\n" +
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
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS expr_stmt@@tsrm_free_alloca tmp use_heap @TO@ block@@return - 1 @AT@ 28559 @LENGTH@ 29\n" +
                "---INS expr@@tsrm_free_alloca tmp use_heap @TO@ expr_stmt@@tsrm_free_alloca tmp use_heap @AT@ 28559 @LENGTH@ 29\n" +
                "------INS call@@tsrm_free_alloca tmp use_heap @TO@ expr@@tsrm_free_alloca tmp use_heap @AT@ 28559 @LENGTH@ 29\n" +
                "---------INS name@@tsrm_free_alloca @TO@ call@@tsrm_free_alloca tmp use_heap @AT@ 28559 @LENGTH@ 16\n" +
                "---------INS argument_list@@tmp use_heap @TO@ call@@tsrm_free_alloca tmp use_heap @AT@ 28575 @LENGTH@ 12\n" +
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
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS if@@if ( ll + n ) >= dlen n = dlen - ( ll + 1 ) @TO@ block@@n = cp ][ll memcpy tp + ll cp + ll + 1 n add_next_index_stringl entries cp + ll + 1 n 1 ll = ll + n + 1 @AT@ 13114 @LENGTH@ 43\n" +
                "---INS condition@@( ll + n ) >= dlen @TO@ if@@if ( ll + n ) >= dlen n = dlen - ( ll + 1 ) @AT@ 13114 @LENGTH@ 18\n" +
                "------INS expr@@( ll + n ) >= dlen @TO@ condition@@( ll + n ) >= dlen @AT@ 13115 @LENGTH@ 18\n" +
                "---------INS operator@@( @TO@ expr@@( ll + n ) >= dlen @AT@ 13115 @LENGTH@ 1\n" +
                "---------INS name@@ll @TO@ expr@@( ll + n ) >= dlen @AT@ 13116 @LENGTH@ 2\n" +
                "---------INS operator@@+ @TO@ expr@@( ll + n ) >= dlen @AT@ 13119 @LENGTH@ 1\n" +
                "---------INS name@@n @TO@ expr@@( ll + n ) >= dlen @AT@ 13121 @LENGTH@ 1\n" +
                "---------INS operator@@) @TO@ expr@@( ll + n ) >= dlen @AT@ 13122 @LENGTH@ 1\n" +
                "---------INS operator@@>= @TO@ expr@@( ll + n ) >= dlen @AT@ 13124 @LENGTH@ 2\n" +
                "---------INS name@@dlen @TO@ expr@@( ll + n ) >= dlen @AT@ 13127 @LENGTH@ 4\n" +
                "---INS then@@n = dlen - ( ll + 1 ) @TO@ if@@if ( ll + n ) >= dlen n = dlen - ( ll + 1 ) @AT@ 13133 @LENGTH@ 21\n" +
                "------INS block@@n = dlen - ( ll + 1 ) @TO@ then@@n = dlen - ( ll + 1 ) @AT@ 13133 @LENGTH@ 21\n" +
                "---------INS expr_stmt@@n = dlen - ( ll + 1 ) @TO@ block@@n = dlen - ( ll + 1 ) @AT@ 13181 @LENGTH@ 21\n" +
                "------------INS expr@@n = dlen - ( ll + 1 ) @TO@ expr_stmt@@n = dlen - ( ll + 1 ) @AT@ 13181 @LENGTH@ 21\n" +
                "---------------INS name@@n @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13181 @LENGTH@ 1\n" +
                "---------------INS operator@@= @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13183 @LENGTH@ 1\n" +
                "---------------INS name@@dlen @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13185 @LENGTH@ 4\n" +
                "---------------INS operator@@- @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13190 @LENGTH@ 1\n" +
                "---------------INS operator@@( @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13192 @LENGTH@ 1\n" +
                "---------------INS name@@ll @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13193 @LENGTH@ 2\n" +
                "---------------INS operator@@+ @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13196 @LENGTH@ 1\n" +
                "---------------INS literal:number@@1 @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13198 @LENGTH@ 1\n" +
                "---------------INS operator@@) @TO@ expr@@n = dlen - ( ll + 1 ) @AT@ 13199 @LENGTH@ 1\n");

    }

    @Test
    public void test_php_src_e76cf8_39753f() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("php-src_e76cf8_39753f_Zend#zend.c");
        Assert.assertEquals(hierarchicalActionSets.size(),0);
    }
    @Test
    public void test_cpython_f62026_71eb864() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_f62026_71eb864_Python#ceval.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
    }
    @Test
    public void test_cpython_df0d00_1254d7() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_df0d00_1254d7_Python#ceval.c");
        Assert.assertEquals(hierarchicalActionSets.size(),0);
    }
    @Test
    public void test_cpython_6a619f_46ab6d() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("cpython_6a619f_46ab6d_Modules#posixmodule.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
    }
    @Test
    public void test_gzip_051ed8_8b83dc() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets("gzip_051ed8_8b83dc_gzip.c");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
    }

    public List<HierarchicalActionSet> getHierarchicalActionSets(String s) throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream("src/main/resource/app.properties"));
        String srcMLPath = appProps.getProperty("srcMLPath", "FORKJOIN");
//        String root = appProps.getProperty("inputPath");
        String root = "src/main/resource/testFiles";
        String project = s.split("_")[0];
        root = root + "/"+project+"/";
        String filename = s.replace(project+"_","");
    try{
        File revFile = new File(root + "revFiles/" + filename);
        File prevFile = new File(root + "prevFiles/prev_" + filename);

        EDiffHunkParser parser = new EDiffHunkParser();

//        srcMLPath = "/usr/local/bin/srcml";
        List<HierarchicalActionSet> hierarchicalActionSets = parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath,false);
        return hierarchicalActionSets;
    }catch (NullPointerException n){
        String cmd = "cp /Users/anil.koyuncu/projects/test/richedit-data/patches/"+project+"/"+n.getMessage().split(root)[1] + " /Users/anil.koyuncu/projects/test/fixminerC/"+n.getMessage();
        CallShell cs = new CallShell();
        try {
            cs.runShell(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    }


}
