package edu.lu.uni.serval.FixPatternParser.violations;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.matchers.Mapping;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import edu.lu.uni.serval.gumtree.regroup.SimplifyTree;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anilkoyuncu on 17/03/2018.
 */
public class TestTreeLoader {
    public static void main(String[] args) {


//        String inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput/ENTESB/ASTDumps";
        String inputPath = "/Users/anilkoyuncu/bugStudy/dataset/GumTreeOutput2/";
        File folder = new File(inputPath);
        File[] listOfFiles = folder.listFiles();
        Stream<File> stream = Arrays.stream(listOfFiles);
        List<File> pjs = stream
                .filter(x -> !x.getName().startsWith("."))
                .collect(Collectors.toList());

        List<File> fileToCompare = new ArrayList<>();
        for(File pj:pjs){
            File[] files = pj.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("ASTDumps");
                }
            });
            Collections.addAll(fileToCompare, files[0].listFiles());

        }
        System.out.println("a");
//        compareAll(fileToCompare);
        memoryFriendlyCompare(fileToCompare);


    }


    public static ITree getSimpliedTree(String fn) {
        ITree tree = null;
        try {
            FileInputStream fi = new FileInputStream(new File(fn));
            ObjectInputStream oi = new ObjectInputStream(fi);
            tree = (ITree) oi.readObject();
            oi.close();
            fi.close();


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tree.setLabel("");
        tree.setParent(null);
        List<ITree> descendants = tree.getDescendants();
        for (ITree descendant : descendants) {
            descendant.setLabel("");
        }

        return tree;

    }
    public static void memoryFriendlyCompare(List<File> folders){
        List<String> treesFileNames = new ArrayList<>();
//        HashMap<Integer, String> hmap = new HashMap<Integer, String>();

        for (File target : folders) {
//            hmap.put(folders.indexOf(target), target.toString());
            treesFileNames.add(target.toString());
        }

        for (int i = 0; i < treesFileNames.size(); i++) {
            for (int j = i + 1; j < treesFileNames.size(); j++) {
                // compare list.get(i) and list.get(j)
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("output2.txt", true));
                    ITree oldTree = getSimpliedTree(treesFileNames.get(i));

                    ITree newTree = getSimpliedTree(treesFileNames.get(j));

                    Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
                    m.match();

                    ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
                    ag.generate();
                    List<Action> actions = ag.getActions();
                    writer.write(String.valueOf(i));
                    writer.write("\t");
                    writer.write(String.valueOf(j));
                    writer.write("\t");

                    writer.write(String.format("%1.2f", m.chawatheSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.format("%1.2f", m.diceSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.format("%1.2f", m.jaccardSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.valueOf(actions.size()));
                    writer.write("\t");
                    writer.write(treesFileNames.get(i));
                    writer.write("\t");
                    writer.write(treesFileNames.get(j));
                    writer.write("\n");

                    writer.close();
                } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");

            }
            }
        }




    }
    public static void compareAll(List<File> folders){
        List<ITree> trees = new ArrayList<>();
        HashMap<Integer, String> hmap = new HashMap<Integer, String>();
        for (File target : folders) {

            try {
                FileInputStream fi = new FileInputStream(new File(target.toString()));
                ObjectInputStream oi = new ObjectInputStream(fi);
                ITree pr1 = (ITree) oi.readObject();
                oi.close();
                fi.close();
                trees.add(pr1);
                hmap.put(folders.indexOf(target), target.toString());

            } catch (FileNotFoundException e) {
                System.out.println("File not found");
            } catch (IOException e) {
                System.out.println("Error initializing stream");
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        for (ITree tree : trees) {
//            SimplifyTree simplifyTree = new SimplifyTree();
//            simplifyTree.canonicalizeSourceCodeTree(tree);
            tree.setLabel("");
            tree.setParent(null);
            List<ITree> descendants = tree.getDescendants();
            for (ITree descendant : descendants) {
                descendant.setLabel("");
            }

        }
        System.out.println("a");

        try {



            for (int i = 0; i < trees.size(); i++) {
                for (int j = i + 1; j < trees.size(); j++) {
                    // compare list.get(i) and list.get(j)
                    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt",true));
                    ITree oldTree = trees.get(i);

                    ITree newTree = trees.get(j);

                    Matcher m = Matchers.getInstance().getMatcher(oldTree, newTree);
                    m.match();

                    ActionGenerator ag = new ActionGenerator(oldTree, newTree, m.getMappings());
                    ag.generate();
                    List<Action> actions = ag.getActions();
                    writer.write(String.valueOf(i));
                    writer.write("\t");
                    writer.write(String.valueOf(j));
                    writer.write("\t");

                    writer.write(String.format("%1.2f", m.chawatheSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.format("%1.2f", m.diceSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.format("%1.2f", m.jaccardSimilarity(oldTree, newTree)));
                    writer.write("\t");
                    writer.write(String.valueOf(actions.size()));
                    writer.write("\t");
                    writer.write(hmap.get(i));
                    writer.write("\t");
                    writer.write(hmap.get(j));
                    writer.write("\n");

                    writer.close();
                }
            }


        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");

        }


//        if (actions.size() > 1) {
//            Matcher m1 = Matchers.getInstance().getMatcher(actions.get(0).getNode(), actions.get(0).getNode());
//            m1.match();
//            Set<Mapping> mappingSet1 = m1.getMappingSet();
//
//        }
    }

}
