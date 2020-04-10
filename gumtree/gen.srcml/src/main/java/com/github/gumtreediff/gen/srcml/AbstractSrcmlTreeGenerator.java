/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016 Jean-Rémy Falleri <jr.falleri@gmail.com>
 */

package com.github.gumtreediff.gen.srcml;

import com.github.gumtreediff.gen.TreeGenerator;
import com.github.gumtreediff.io.LineReader;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;


public abstract class AbstractSrcmlTreeGenerator extends TreeGenerator {




    private  String SRCML_CMD = System.getProperty("gt.srcml.path", "srcml");
//    private String SRCML_CMD = "/Users/anil.koyuncu/Downloads22/srcML/src2srcml";
    //    private static String namespace = "http://www.sdml.info/srcML/position";
    private static String namespace = "http://www.srcML.org/srcML/position";
    private static final QName START = new  QName(namespace, "start", "pos");

    private static final QName END = new  QName(namespace, "end", "pos");
    private static final QName COMMENT_BLOCK = new  QName("", "type", "");

    private LineReader lr;

    private HashSet<String> removeType = new HashSet<>(Arrays.asList("empty_stmt","comment"));
    private HashSet<String> labeled = new HashSet<String>(
//            Arrays.asList("comment"));
            Arrays.asList("specifier", "name",  "argument","expr","type","value","index","operator","literal","incr","modifier","break","continue","default","literal:string","literal:number","literal:char","literal:boolean","literal:complex","literal:null"));


    private StringBuilder currentLabel;

    private TreeContext context;

//    Type position = type("position");

    @Override
    public TreeContext generate(Reader r) throws IOException {
        lr = new LineReader(r);
        String xml = readStandardOutput(lr);
        return getTreeContext(xml);
    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public TreeContext getTreeContext(String xml) {
        XMLInputFactory fact = XMLInputFactory.newInstance();
        context = new TreeContext();
        currentLabel = new StringBuilder();
        boolean isBlock = false;
        try {
            ArrayDeque<ITree> trees = new ArrayDeque<>();
            XMLEventReader r = fact.createXMLEventReader(new StringReader(xml));
            while (r.hasNext()) {
                XMLEvent ev = r.nextEvent();

                if (ev.isStartElement()) {
                    StartElement s = ev.asStartElement();
                    String typeLabel = s.getName().getLocalPart();
                    String prefix = s.getName().getPrefix();
                    if(removeType.contains(typeLabel) || isBlock){
                        if(s.getName().getLocalPart().equals("comment") && s.getAttributeByName(COMMENT_BLOCK).getValue().equals("block")){
                            isBlock = true;
                        }
                        continue;
                    }

                    if (typeLabel.equals("position"))
//                    Type type = type(s.getName().getLocalPart());
//                    if (type.equals(position))
                        setLength(trees.peekFirst(), s);
                    else {
                        if (prefix.equals("cpp") && (typeLabel.equals("if") || typeLabel.equals("else"))){
                            typeLabel = prefix + ":"+typeLabel;
                        }

                        if(typeLabel.equals("literal")){
                            if(s.getAttributeByName(COMMENT_BLOCK) != null){
                                String value = s.getAttributeByName(COMMENT_BLOCK).getValue();
                                typeLabel = typeLabel + ":"+value;
                            }
                        }
                        List<Integer> keysByValue = getKeysByValue(NodeMap_new.map, typeLabel);
                        if(keysByValue == null || keysByValue.size() ==0){
                            System.out.println(typeLabel);
                        }
                        int type = keysByValue.get(0);

                        ITree t = context.createTree(type, "", typeLabel);


                        if (trees.isEmpty()) {
                            context.setRoot(t);
                            t.setPos(0);
                        } else {
                            t.setParentAndUpdateChildren(trees.peekFirst());
                            setPos(t, s);
                        }
                        trees.addFirst(t);
                    }
                } else if (ev.isEndElement()) {
                    EndElement end = ev.asEndElement();
                    if(removeType.contains(end.getName().getLocalPart() ) ){
                        if(end.getName().getLocalPart().equals("comment") && isBlock){
                            isBlock = false;
                        }
                        continue;
                    }
                    if (!end.getName().getLocalPart().equals("position") && !isBlock){
                        if (isLabeled(trees)){
                            trees.peekFirst().setLabel(currentLabel.toString());
                        }
                        trees.removeFirst();
                        currentLabel = new StringBuilder();
                    }
                } else if (ev.isCharacters() && !isBlock) {
                    Characters chars = ev.asCharacters();
                    if(chars.getData().trim().startsWith("\"This module provides access to some")){
                        chars.getData();
                    }
                    if (!chars.isWhiteSpace() && isLabeled(trees))
                        currentLabel.append(chars.getData().replace("\n",""));
                }
            }
            fixPos(context);
            return context; //TODO check way validate is removed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isLabeled(ArrayDeque<ITree> trees) {
        return labeled.contains(context.getTypeLabel(trees.peekFirst().getType()));
    }

    private void fixPos(TreeContext ctx) {
        for (ITree t : ctx.getRoot().postOrder()) {
            if (!t.isLeaf()) {
                //put the keywords as labels
//                if(t.getType() == 34 || t.getType() ==37 || t.getType() ==38 || t.getType()==39 || t.getType() == 41 || t.getType()==45 || t.getType() ==55 || t.getType()==14){
//                    t.setLabel(NodeMap_new.map.get(t.getType())+" "  +t.getLabel());
//                }
                if (t.getPos() == ITree.NO_VALUE || t.getLength() == ITree.NO_VALUE ) {

                    ITree firstChild = t.getChild(0);
//                    t.setPos(firstChild.getPos());

                    if (t.getChildren().size() == 1) {
                        t.setLabel(t.getLabel() + firstChild.getLabel());
                    }
                    else {

                        ITree lastChild = t.getChild(t.getChildren().size() - 1);
                        if(t.getPos() == -1){

                            if(t.getChild(0).getChildren().size() == 0) {
                                t.getChild(0).setPos(t.getChild(1).getPos());
//                                t.setPos(lastChild.getPos());
                            }

                        }else{
                            t.getPos();
                        }

                        if(t.getType() != 1){
                            t.setLabel(t.getLabel() + t.getChildrenLabels());
                        }
                    }
                }else if (t.getLabel().equals("")){
//                    if(t.getType() == 60 || t.getType() == 56 || t.getType() == 47 || t.getType() == 8 || t.getType() == 43 || NodeMap_new.StatementMap.containsKey(t.getType())){
                    if(NodeMap_new.StatementMap.containsKey(t.getType())){
                        String childrenLabels = t.getChildrenLabels();
//                        if(t.getType() == 53){
//                            t.setLabel("return "+childrenLabels);
//                        }else{
                            if (!childrenLabels.equals("")){
                                t.setLabel(childrenLabels);
                            }
//                        }
                    }
                }

            }
            else{


//                if (t.getPos() == ITree.NO_VALUE ) {
//
////                    if(t.getType() == 7){
////                        ITree parent = t.getParent();
////                        if(parent.getType() == 22){
////                            ITree child = parent.getParent().getChild(0);
////                            if (child.getType() == 22){
////                                ITree child1 = child.getChild(0);
////                                if(child1.getType() == 7){
////                                    t.setLabel(child1.getLabel());
////                                    t.setPos(child1.getPos());
////                                }
////                            }
////                        }
////                    }
//
//                    int childPosition = t.getParent().getChildPosition(t);
//                    if(childPosition != 0){
//                        ITree child = t.getParent().getChild(childPosition - 1);
//                        t.setPos(child.getPos()+child.getLength()+1);
//
//                    }else{
//                        if(t.getParent().getChildren().size() > 1){
//                            ITree child = t.getParent().getChild(childPosition + 1);
//                            t.setPos(child.getPos()-1);
//                        }else{
//
//                            /*
//                            (34 "if" "" ((183 -1)) (
//                                (8 "condition" "i != j" ((186 6)) (
//                                    (20 "expr" "i != j" ((187 6)) (
//                                        (6 "name" "i" ((187 1)) ()
//                                        (4 "operator" "!=" ((188 2)) ()
//                                        (6 "name" "j" ((190 1)) ()))
//                                (36 "then" "" () (
//                                    (9 "block" "" () ()))
//                                    * */
////                            if (t.getPos() == ITree.NO_VALUE ){
////
////                                    ITree firstParent = t.getParent();
////                                    if(firstParent.getType() == 36){//then
////                                        ITree secondParent = firstParent.getParent();
////                                        if(secondParent.getType() == 34){//
////                                            if(secondParent.getChildren().size()>1){
////                                                int childPosition1 = secondParent.getChildPosition(firstParent);
////                                                if (childPosition1 !=0){
////                                                    ITree child = secondParent.getChild(childPosition1 - 1);
////                                                    firstParent.setPos(child.getPos()+child.getLength());
//////                                                    firstParent.setLength(0);
////                                                    firstParent.getChild(0).setPos(child.getPos()+child.getLength());
//////                                                    firstParent.getChild(0).setLength(0);
////                                                }
////                                            }
////                                        }
////                                    }else if(firstParent.getType() == 55){//sizeof
////                                        ITree secondParent = firstParent.getParent();
////                                        if(secondParent.getType() == 20){//
////                                            if(secondParent.getChildren().size()>1){
////                                                int childPosition1 = secondParent.getChildPosition(firstParent);
////                                                if (childPosition1 ==0){
////                                                    ITree child = secondParent.getChild(childPosition1 + 1);
////                                                    firstParent.setPos(child.getPos()-child.getLength());
//////                                                    firstParent.setLength(0);
////                                                    firstParent.getChild(0).setPos(child.getPos()-child.getLength());
//////                                                    firstParent.getChild(0).setLength(0);
////                                                }
////                                            }
////                                        }
////                                    }
////                                    ITree child = t.getParent().getChild(childPosition);
////                                    if(child.getType() ==61){ //condition
////                                        t.setPos(child.getPos()-1);
////                                    }
////
////                            }
//                        }
//
//                    }
//                }

            }
            t.setLength(t.getLabel().length());

        }
    }

    private void setPos(ITree t, StartElement e) {
        if (e.getAttributeByName(START) != null) {
            String[] start = e.getAttributeByName(START).getValue().split(":");

            int line = Integer.parseInt(start[0]);
            int column = Integer.parseInt(start[1]);
//            int column = Integer.parseInt(e.getAttributeByName(END).getValue());
            t.setPos(lr.positionFor(line, column));
        }
    }

    private void setLength(ITree t, StartElement e) {
        if (t.getPos() == -1)
            return;
//        if (e.getAttributeByName(LINE) != null) {
//            int line = Integer.parseInt(e.getAttributeByName(LINE).getValue());
//            int column = Integer.parseInt(e.getAttributeByName(COLUMN).getValue());
//            t.setLength(lr.positionFor(line, column) - t.getPos() + 1);
//        }
    }

    public String getXml(Reader r) throws IOException {
        //FIXME this is not efficient but I am not sure how to speed up things here.
        File f = File.createTempFile("gumtree", "");
        FileWriter w = new FileWriter(f);
        BufferedReader br = new BufferedReader(r);
        String line = br.readLine();
        while (line != null) {
            w.append(line);
            w.append(System.lineSeparator());
            line = br.readLine();
        }
        w.close();
        br.close();
        ProcessBuilder b = new ProcessBuilder(getArguments(f.getAbsolutePath()));
        b.directory(f.getParentFile());
        try {
            Process p = b.start();
            StringBuffer buf = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // TODO Why do we need to read and bufferize everything, when we could/should only use generateFromStream
            line = null;
            while ((line = br.readLine()) != null)
                buf.append(line + "\n");
            p.waitFor();
            if (p.exitValue() != 0) throw new RuntimeException();
            r.close();
            String xml = buf.toString();
            return xml;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            f.delete();
        }
    }

    public abstract String getLanguage();



    public void setSRCML_CMD(String SRCML_CMD) {
        this.SRCML_CMD = SRCML_CMD;
    }

    public String getSRCML_CMD() {
        return SRCML_CMD;
    }

    public String[] getCommandLine(String file) {
        return new String[]{SRCML_CMD, "-l", getLanguage(), "--position", file, "--tabs=1"};
    }

    public String[] getArguments(String file) {
        return new String[]{getSRCML_CMD(), "-l", getLanguage(), "--position", file, "--tabs=1"};
    }
    public String readStandardOutput(Reader r) throws IOException {
        // TODO avoid recreating file if supplied reader is already a file
        File f = dumpReaderInTempFile(r);
        ProcessBuilder b = new ProcessBuilder(getCommandLine(f.getAbsolutePath()));
        b.directory(f.getParentFile());
        Process p = b.start();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null){

////                String e = "<else pos:line=\"[0-9]+\" pos:column=\"[0-9]+\">else\\W*<block type=\"pseudo\"><empty_stmt pos:line=\"[0-9]+\" pos:column=\"[0-9]+\">;<pos:position pos:line=\"[0-9]+\" pos:column=\"[0-9]+\"/></empty_stmt></block></else>";
//                String e = "<else pos:line=\\\\\\\"[0-9]+\\\\\\\" pos:column=\\\\\\\"[0-9]+\\\\\\\">else\\\\W*<block type=\\\\\\\"pseudo\\\\\\\"><empty_stmt pos:line=\\\\\\\"[0-9]+\\\\\\\" pos:column=\\\\\\\"[0-9]+\\\\\\\">;<pos:position pos:line=\\\\\\\"[0-9]+\\\\\\\" pos:column=\\\\\\\"[0-9]+\\\\\\\"/></empty_stmt></block></else>";
//                //<else pos:line="37" pos:column="4">else <block type="pseudo"><empty_stmt pos:line="37" pos:column="9">;<pos:position pos:line="37" pos:column="10"/></empty_stmt></block></else></if>
//                line = line.replaceAll(e,"");
//                String s = "<expr_stmt><pos:position pos:line=\"[0-9]+\" pos:column=\"[0-9]+\"/></expr_stmt>";
//                line = line.replaceAll(s,"");
                buf.append(line + System.lineSeparator());
            }
            p.waitFor();
            if (p.exitValue() != 0)
                throw new RuntimeException(buf.toString());
            r.close();
            p.destroy();
            String s = buf.toString();
            String p1 = "<expr_stmt><pos:position pos:line=\"[0-9]+\" pos:column=\"[0-9]+\"/></expr_stmt>";
            String p2 = "<else pos:line=\"[0-9]+\" pos:column=\"[0-9]+\">else\\W*<block type=\"pseudo\"><empty_stmt pos:line=\"[0-9]+\" pos:column=\"[0-9]+\">;<pos:position pos:line=\"[0-9]+\" pos:column=\"[0-9]+\"/></empty_stmt></block></else>";
            s = s.replaceAll(p2,"");
            s = s.replaceAll(p1,"");
            return s;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            f.delete();
        }
    }

    private File dumpReaderInTempFile(Reader r) throws IOException {
        File f = File.createTempFile("gumtree", "");
        try (
                Writer w = Files.newBufferedWriter(f.toPath(), Charset.forName("UTF-8"))
        ) {
            char[] buf = new char[8192];
            while (true) {
                int length = r.read(buf);
                if (length < 0)
                    break;
                w.write(buf, 0, length);
            }
        }
        return f;
    }
}
