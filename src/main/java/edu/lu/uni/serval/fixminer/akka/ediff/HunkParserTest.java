package edu.lu.uni.serval.fixminer.akka.ediff;

import edu.lu.uni.serval.fixminer.akka.ediff.EDiffHunkParser;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class HunkParserTest {

    @Test
    public void testSimple() throws IOException {
//        String input = "/Users/anil.koyuncu/projects/test/fixminer-core/python/data/gumInputLinux/revFiles/7f52f3_3845d29_drivers#pci#host#pcie-altera.c";

        String root = "/Users/anilkoyuncu/projects/gumInputLinux/linux/";

//        String filename  = "8dd302_c4ef85_net#core#dev_ioctl.c";
        String filename = "6a28fd_93ad867_drivers#tty#goldfish.c";
//        String filename  = "5924f17_5925a05_net#ipv4#tcp.c";
//        String filename = "bd0b9ac_b237721_drivers#irqchip#irq-dw-apb-ictl.c";
//        String filename  = "052831_3985e8_include#net#ip_tunnels.h";
//        String filename  = "e76019_28647b_drivers#gpu#drm#i915#i915_drv.h";
//        String filename  = "4cbe4d_b124f4_include#linux#mlx4#device.h";  //enum case stops at block
//        String filename  = "7bf7eac_c01daf_include#linux#dax.h";
        File revFile = new File(root + "revFiles/"+ filename);
        File prevFile =new File(root + "prevFiles/prev_"+filename);
        EDiffHunkParser parser = new EDiffHunkParser();

        String srcMLPath = "/Users/anilkoyuncu/Downloads/srcML2/src2srcml";
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
        parser.parseFixPatterns(prevFile,revFile,diffFile,"gumInputLinux",null,srcMLPath);
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

}
