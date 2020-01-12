package edu.lu.uni.serval.fixminer.akka.ediff;

import com.github.gumtreediff.gen.srcml.NodeMap_new;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import edu.lu.uni.serval.fixminer.akka.compare.AkkaTreeParser;
import edu.lu.uni.serval.fixminer.akka.ediff.EDiffHunkParser;
import edu.lu.uni.serval.utils.EDiffHelper;
import edu.lu.uni.serval.utils.PoolBuilder;
import org.apache.commons.io.FileUtils;
import org.javatuples.Pair;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HunkParserTest {

    @Test
    public void testSimple() throws IOException {
//        String input = "/Users/anil.koyuncu/projects/test/fixminer-core/python/data/gumInputLinux/revFiles/7f52f3_3845d29_drivers#pci#host#pcie-altera.c";

        String root = "/Users/anil.koyuncu/projects/test/fixminer-core/python/data/gumInputLinux/linux/";

//        String filename  = "8dd302_c4ef85_net#core#dev_ioctl.c"; //ok
//        String filename = "1e793f6_77f18a_drivers#scsi#megaraid#megaraid_sas_base.c"; //OK
//        String filename = "6a28fd_93ad867_drivers#tty#goldfish.c"; //m,issing
//        String filename = "b90f7c_ff51ff_kernel#sched#fair.c"; //wrong and wired
//        String filename = "ed8f68_b1c8047_fs#ext3#dir.c";//ok

//        String filename = "bc3d12_9a26653_drivers#scsi#libfc#fc_disc.c"; //okish
        String filename = "118154_0c5f81_arch#x86#kvm#svm.c";
//        String filename = "bcbd94f_43e43c9_drivers#md#dm-crypt.c"; //emin degilim
//        String filename = "f1727b4_6c1e7e_arch#x86#kvm#vmx#nested.c"; //komplex not sure
//        String filename  = "5924f17_5925a05_net#ipv4#tcp.c";
//        String filename = "bd0b9ac_b237721_drivers#irqchip#irq-dw-apb-ictl.c"; //missing
//        String filename  = "052831_3985e8_include#net#ip_tunnels.h";
//        String filename  = "e76019_28647b_drivers#gpu#drm#i915#i915_drv.h";
//        String filename  = "4cbe4d_b124f4_include#linux#mlx4#device.h";  //enum case stops at block
//        String filename  = "7bf7eac_c01daf_include#linux#dax.h";
        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);
        EDiffHunkParser parser = new EDiffHunkParser();

        String srcMLPath = "/Users/anil.koyuncu/Downloads/srcML/src2srcml";
        parser.parseChangedSourceCodeWithGumTree2(prevFile, revFile, srcMLPath);
//        ITree t = new SrcmlCppTreeGenerator().generateFromFile(input).getRoot();
//        Assert.assertEquals(148, t.getSize());

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

        HierarchicalActionSet actionSet = (HierarchicalActionSet)  EDiffHelper.kryoDeseerialize(dump);
//        HierarchicalActionSet actionSet = (HierarchicalActionSet)  EDiffHelper.commonsDeserialize(dump);
        actionSet.toString();


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

        oldPair = EDiffHelper.getActions(keyName, i, outerPool, filenames);
        newPair = EDiffHelper.getActions(keyName, j, outerPool, filenames);
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
