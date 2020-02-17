package edu.lu.uni.serval.fixminer.akka.ediff;

import com.github.gumtreediff.gen.srcml.NodeMap_new;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.fixminer.akka.compare.AkkaTreeParser;
import edu.lu.uni.serval.fixminer.akka.ediff.EDiffHunkParser;
import edu.lu.uni.serval.utils.ClusterToPattern;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HunkParserTest {

    @Test
    public void testSimple() throws IOException {
//        String input = "/Users/anil.koyuncu/projects/test/fixminer-core/python/data/gumInputLinux/revFiles/7f52f3_3845d29_drivers#pci#host#pcie-altera.c";

//        String root = "//Users/anilkoyuncu/projects/gumInputLinux/";
        String root = "/Users/anilkoyuncu/projects/fixminer/fixminer-core/python/data/gumInputLinux/";
        String filename ="";
//        filename ="freebsd_ceca9b8_b864ac4_sys#kern#sched_ule.c"; //too long
//        filename ="openbsd_e592ec_39c81a_sys#arch#i386#pci#pci_machdep.c"; //not parseable 56 "parameter_list" "" () ( (57 "parameter" "" () ( (22 "decl" "" () ())))
//        filename ="openbsd_cbb6d2_4cab495_sys#lib#libsa#printf.c";
//        filename ="freebsd_bb1ce4_10d4c2_sbin#gvinum#gvinum.c"; //too long
//        filename ="freebsd_253913_35ea52_sys#netinet#ip_carp.c"; //ok
//        filename ="FFmpeg_4c9d1c_3da860_libavutil#file_open.c"; //not sure ifdef
//        filename ="gstreamer_0af74c_e8bae0_libs#gst#net#gstptpclock.c"; //not sure ifder
//        filename ="freebsd_abdbcc6_030063_sys#netinet#ip_carp.c"; //ok
//        filename ="linux_80d348_5b394b_fs#overlayfs#inode.c"; //ok
//        filename ="openbsd_43b185_e7495b_usr.bin#cvs#rcs.c"; //okish
//        filename ="openbsd_e592ec_39c81a_sys#arch#i386#pci#pci_machdep.c"; //(56 "parameter_list" "" () ( (57 "parameter" "" () ( (22 "decl" "" () ())))
//        filename ="openbsd_cbb6d2_4cab495_sys#lib#libsa#printf.c"; //not parseable 56 "parameter_list" "" () ( (57 "parameter" "" () ( (22 "decl" "" () ())))
//        filename ="FFmpeg_9219ec_647696_libavfilter#trim.c"; //partial
//        filename ="vlc_92b7fd_f745f6_modules#control#dbus#dbus.c"; //okish
//        filename ="vlc_eeb662_966879_modules#video_chroma#copy.c"; //ok
//        filename ="omp_19fae3_1e4dcd_src#mca#mpool#sm#mpool_sm_mmap.c"; // cannot find
//        filename ="FFmpeg_a8343bf_2b2039_libavformat#riff.c"; // ok
//        filename ="freebsd_32766e4_200ff4_sbin#routed#parms.c"; // ok
//         filename ="openbsd_150ddd_cf0e20_usr.sbin#user#user.c"; //notok
//         filename ="openbsd_6fac1e_c3b383_usr.bin#tmux#window-copy.c"; //notok
//         filename ="freebsd_0cb6f2_b4c742_sys#dev#ipw#if_ipw.c"; //notok
//         filename ="php-src_7defd5_da06f7_ext#mbstring#mbstring.c"; //notok (19 "expr_stmt" "" () ()))))
//         filename ="libtiff_177169_71715f_tools#tiff2ps.c"; //notok (19 "expr_stmt" "" () ()))))
         filename ="linux_955c1dd_0aaee4_drivers#gpu#drm#i915#gvt#handlers.c"; //notok (19 "expr_stmt" "" () ()))))

//        filename ="FFmpeg_0726b2_66d2ff_libav#jpeg.c";
        String pj = filename.split("_")[0];
        filename = filename.replace(pj+"_","");
//        root = root + pj + "/";
        root = root + "codeflaws/";


        filename ="287-A-14208510-14208532.c";
//        filename = "474-A-15925943-15925951.c"; //mot ok
//        filename = "6-C-11536006-11536039.c"; //okish
//        filename = "500-A-18298071-18298124.c"; //ok
//        filename = "106-B-4027414-4027447.c"; //ok
//        filename = "572-B-12669194-12669278.c"; //ok
//        filename = "514-A-16254510-16254521.c"; //ok
//        filename = "405-B-12287356-12287584.c"; //ok
//        filename = "630-R-17825199-17825235.c"; //notok
        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);
//        File diffFile =new File();
        String path = root + "DiffEntries/"+filename + ".txt";
        System.out.println(path);
//        String data = "";
//        data = new String(Files.readAllBytes(Paths.get(root + "DiffEntries/"+filename + ".txt")));

        EDiffHunkParser parser = new EDiffHunkParser();

        String srcMLPath = "/usr/local/bin/srcml";
//        String srcMLPath = "/Users/anil.koyuncu/Downloads/srcML.0.9.5/bin/srcml";
        parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
//        ITree t = new SrcmlCppTreeGenerator().generateFromFile(input).getRoot();
//        Assert.assertEquals(148, t.getSize());

    }


    @Test
    public void dumpFnction() throws Exception {
        String pattern = "function/20/gstreamer_0af74c_e8bae0_libs#gst#net#gstptpclock.c.txt_0";
//        String pattern = "function/20/FFmpeg_4c9d1c_3da860_libavutil#file_open.c.txt_0";
        ClusterToPattern.main("6399","/Users/anil.koyuncu/projects/fixminer/fixminer-core/python/data/redis","ALLdumps-gumInput.rdb ",pattern);
    }

    @Test
    public void newCTest(){
        String root = "/Users/anilkoyuncu/projects/gumInputLinux/linux/";
        String filename = "6a28fd_93ad867_drivers#tty#goldfish.c";

        String srcMLPath = "/Users/anilkoyuncu/Downloads/srcML2/src2srcml";
        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);
        File diffFile = new File(root + "DiffEntries/"+filename+".txt");
        EDiffHunkParser parser = new EDiffHunkParser();
        parser.parseFixPatterns(prevFile,revFile,diffFile,"gumInputLinux",null,srcMLPath,null);
    }


    @Test
    public void testSimpleJava() throws IOException {


        String root = "/Users/anil.koyuncu/projects/test/fixminer-core/python/data/gumInput/spring-amqp/";

        String filename = "5d6e02_e597c5_spring-rabbit#src#main#java#org#springframework#amqp#rabbit#connection#ConnectionFactoryUtils.java";
        File revFile = new File(root +"/revFiles/"+filename);
//        File oldFile = new File(root +"first.c");
        File prevFile = new File(root +"/prevFiles/prev_"+filename);

        EDiffHunkParser parser = new EDiffHunkParser();

        parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile,"");
//        ITree t = new SrcmlCppTreeGenerator().generateFromFile(input).getRoot();
//        Assert.assertEquals(148, t.getSize());
    }



    @Test
    public void rich() throws IOException {
        Instant start = Instant.now();
        final JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "localhost",Integer.valueOf("6399"),20000000);

        EDiffHunkParser parser = new EDiffHunkParser();
        String root = "/Users/anil.koyuncu/projects/fixminer/gumInputLinux/linux/";
        String filename = "43f8987_f596c8_drivers#acpi#nfit#core.c";
        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);
        File diffFile = new File(root + "DiffEntries/"+filename+".txt");
        String srcMLPath = "/Users/anil.koyuncu/Downloads/srcML/src2srcml";
        parser.parseFixPatterns(prevFile,revFile,diffFile,"gumInputLinux",outerPool,srcMLPath,"if");
        String key = "if/3/linux_bb67dd_0922c7_sound#soc#sof#intel#hda.c.txt_0";
        File file2load = new File("/Users/anil.koyuncu/projects/fixminer/dumps/"+ key);
        byte[] dump = FileUtils.readFileToByteArray(file2load);

//        String line = FileHelper.readFile(file2load);
//        ITree parent = null;
//        ITree children = null;
////        if(line.isEmpty())
////            continue;
//        TreeContext tc = new TreeContext();
//        line = line.replace("\"", "");
//        String[] split1 = line.split("\n");
//        int length = split1.length;
//        List<String> strings = new LinkedList<String>(Arrays.asList(split1));
//        ITree treeFromString = null;
//        int prevLev = 0;
//        int childPosition = 0;
//        for (String l:strings) {
//            int level = 0;
//            Pattern pattern = Pattern.compile("---");
//            Matcher matcher = pattern.matcher(l);
//            while (matcher.find())
//                level++;
//
//            l = l.replace("---","");
//            l = l.trim();
//
//            String[] split2;
//            List<Integer> keysByValue;
//            split2 = l.split(" ");
//            keysByValue = NodeMap_new.getKeysByValue(NodeMap_new.map, split2[1]);
//
//
//
//            if(level == 0){
//                parent = tc.createTree(keysByValue.get(0), split2[0], null);
//                tc.setRoot(parent);
//
//            }else if (level > prevLev) {
//                if (children == null) {
//                    children = tc.createTree(keysByValue.get(0), split2[0], null);
//                    children.setParentAndUpdateChildren(parent);
//                } else {
//
//                    ITree tree = tc.createTree(keysByValue.get(0), split2[0], null);
//                    tree.setParentAndUpdateChildren(children);
//                    children = tree;
//                }
//            }else if (level == prevLev){
//                ITree innerParent = children.getParent();
//                children = tc.createTree(keysByValue.get(0), split2[0], null);
//                children.setParentAndUpdateChildren(innerParent);
//            }else {
//                ITree innerParent = children.getParent();
//                children = tc.createTree(keysByValue.get(0), split2[0], null);
//                children.setParentAndUpdateChildren(innerParent.getParent());
//            }
//            prevLev = level;
//        }
//        tc.validate();
//        parent.getLength();

//        HierarchicalActionSet actionSet = (HierarchicalActionSet)  EDiffHelper.kryoDeseerialize(dump);
//        HierarchicalActionSet actionSet = (HierarchicalActionSet)  EDiffHelper.commonsDeserialize(dump);
//        actionSet.toString();


// CODE HERE
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();
        System.out.println(timeElapsed);

//        try (Jedis outer = outerPool.getResource()) {
//            try {
//
//
//
//                byte[] s = outer.hget("dump".getBytes(), key.getBytes());
//                HierarchicalActionSet actionSet = (HierarchicalActionSet) EDiffHelper.kryoDeseerialize(s);
//                actionSet.getActionSize();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
    @Test
    public void testCompare(){
        final JedisPool outerPool = new JedisPool(PoolBuilder.getPoolConfig(), "localhost",Integer.valueOf("6399"),20000000);

        Pair<ITree, HierarchicalActionSet> oldPair = null;
        Pair<ITree, HierarchicalActionSet> newPair = null;
        String matchKey = null;
        ArrayList<String> samePairs = new ArrayList<>();

        String keyName = "if-3";
        String i = "2";
        String j = "21";
        HashMap<String, String> filenames = AkkaTreeParser.filenames(outerPool);

//        oldPair = EDiffHelper.getActions(keyName, i, outerPool, filenames);
//        newPair = EDiffHelper.getActions(keyName, j, outerPool, filenames);
        ITree oldActionTree = oldPair.getValue0();
        ITree newActionTree = newPair.getValue0();
        HierarchicalActionSet oldProject = oldPair.getValue1();
        HierarchicalActionSet newProject = newPair.getValue1();

        ITree oldShapeTree = EDiffHelper.getShapeTree(oldProject);
        ITree newShapeTree = EDiffHelper.getShapeTree(newProject);

        ITree oldTargetTree = EDiffHelper.getTargets(oldProject);
        ITree newTargetTree = EDiffHelper.getTargets(newProject);
        String oldShape = oldShapeTree.toStaticHashString();
        String newShape = newShapeTree.toStaticHashString();

        if(oldShape.equals(newShape)){
            if(oldActionTree.toStaticHashString().equals(newActionTree.toStaticHashString())){
                if(oldTargetTree.toStaticHashString().equals(newTargetTree.toStaticHashString())){
                    samePairs.add(matchKey);
                }
            }
        }
    }


}
