package edu.lu.uni.serval;

import edu.lu.uni.serval.richedit.ediff.HierarchicalActionSet;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;


public class TestJavaMiner extends BaseTest {

    //commons-codec_55a865_ecec1c_src#main#java#org#apache#commons#codec#StringEncoderComparator.java.txt_0

    @Test
    public void test_gzip_051ed8_8b83dc() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-codec_55a865_ecec1c_src#main#java#org#apache#commons#codec#StringEncoderComparator.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD TypeDeclaration@@[public]StringEncoderComparator, [Comparator] @TO@ [@SuppressWarnings(\"rawtypes\"), public]StringEncoderComparator, [Comparator] @AT@ 1206 @LENGTH@ 1756\n" +
                "---INS SingleMemberAnnotation@@@SuppressWarnings(\"rawtypes\") @TO@ TypeDeclaration@@[public]StringEncoderComparator, [Comparator] @AT@ 1206 @LENGTH@ 29\n");
    }

    //
    @Test
    public void test_commons_configuration_b24b7b_3ac9d8() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-configuration_b24b7b_3ac9d8_src#test#org#apache#commons#configuration#TestHierarchicalConfigurationXMLReader.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS TryStatement@@try {  trans.transform(source,result);} catch (NoSuchMethodError ex) {  return;} @TO@ MethodDeclaration@@public, void, MethodName:testParse, Exception,  @AT@ 1804 @LENGTH@ 284\n" +
                "---MOV ExpressionStatement@@MethodInvocation:trans.transform(source,result) @TO@ TryStatement@@try {  trans.transform(source,result);} catch (NoSuchMethodError ex) {  return;} @AT@ 1804 @LENGTH@ 32\n" +
                "---INS CatchClause@@catch (NoSuchMethodError ex) {  return;} @TO@ TryStatement@@try {  trans.transform(source,result);} catch (NoSuchMethodError ex) {  return;} @AT@ 2021 @LENGTH@ 67\n" +
                "------INS SingleVariableDeclaration@@NoSuchMethodError ex @TO@ CatchClause@@catch (NoSuchMethodError ex) {  return;} @AT@ 2027 @LENGTH@ 20\n" +
                "---------INS SimpleType@@NoSuchMethodError @TO@ SingleVariableDeclaration@@NoSuchMethodError ex @AT@ 2027 @LENGTH@ 17\n" +
                "---------INS SimpleName@@ex @TO@ SingleVariableDeclaration@@NoSuchMethodError ex @AT@ 2045 @LENGTH@ 2\n" +
                "------INS ReturnStatement@@ @TO@ CatchClause@@catch (NoSuchMethodError ex) {  return;} @AT@ 2071 @LENGTH@ 7\n");
    }

    //commons-collections_05b6c7_eedfe8_src#java#org#apache#commons#collections#BeanMap.java
    @Test
    public void test_commons_collections_05b6c7_eedfe8() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_05b6c7_eedfe8_src#java#org#apache#commons#collections#BeanMap.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ReturnStatement@@MethodInvocation:get((String)key) @TO@ MethodInvocation:get(key) @AT@ 18236 @LENGTH@ 27\n" +
                "---UPD MethodInvocation@@get((String)key) @TO@ get(key) @AT@ 18243 @LENGTH@ 19\n" +
                "------UPD SimpleName@@MethodName:get:[(String)key] @TO@ MethodName:get:[key] @AT@ 18243 @LENGTH@ 19\n" +
                "---------INS SimpleName@@key @TO@ SimpleName@@MethodName:get:[(String)key] @AT@ 18247 @LENGTH@ 3\n" +
                "---------DEL CastExpression@@(String)key @AT@ 18248 @LENGTH@ 12\n" +
                "------------DEL SimpleType@@String @AT@ 18249 @LENGTH@ 6\n" +
                "------------DEL SimpleName@@key @AT@ 18257 @LENGTH@ 3\n");
    }

    //commons-collections_906468_31bc59_src#java#org#apache#commons#collections#SequencedHashMap.java
    @Test
    public void test_commons_collections_906468_31bc59() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_906468_31bc59_src#java#org#apache#commons#collections#SequencedHashMap.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD IfStatement@@if (entry.equals(e)) return entry; else return null; @TO@ if (entry != null && entry.equals(e)) return entry; else return null; @AT@ 21117 @LENGTH@ 59\n" +
                "---INS InfixExpression@@entry != null && entry.equals(e) @TO@ IfStatement@@if (entry.equals(e)) return entry; else return null; @AT@ 21116 @LENGTH@ 32\n" +
                "------INS InfixExpression@@entry != null @TO@ InfixExpression@@entry != null && entry.equals(e) @AT@ 21116 @LENGTH@ 13\n" +
                "---------INS SimpleName@@entry @TO@ InfixExpression@@entry != null @AT@ 21116 @LENGTH@ 5\n" +
                "---------INS Operator@@!= @TO@ InfixExpression@@entry != null @AT@ 21121 @LENGTH@ 2\n" +
                "---------INS NullLiteral@@null @TO@ InfixExpression@@entry != null @AT@ 21125 @LENGTH@ 4\n" +
                "------MOV MethodInvocation@@entry.equals(e) @TO@ InfixExpression@@entry != null && entry.equals(e) @AT@ 21120 @LENGTH@ 15\n" +
                "------INS Operator@@&& @TO@ InfixExpression@@entry != null && entry.equals(e) @AT@ 21129 @LENGTH@ 2\n");
    }

    //commons-compress_5273bd_c25c8d_src#main#java#org#apache#commons#compress#archivers#sevenz#Coders.java
    @Test
    public void test_commons_compress_5273bd_c25c8d() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-compress_5273bd_c25c8d_src#main#java#org#apache#commons#compress#archivers#sevenz#Coders.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ReturnStatement@@MethodInvocation:init().read() @TO@ MethodInvocation:init().read(b,off,len) @AT@ 9806 @LENGTH@ 21\n" +
                "---UPD MethodInvocation@@init().read() @TO@ init().read(b,off,len) @AT@ 9813 @LENGTH@ 13\n" +
                "------UPD SimpleName@@MethodName:read:[] @TO@ MethodName:read:[b, off, len] @AT@ 9820 @LENGTH@ 6\n" +
                "---------INS SimpleName@@b @TO@ SimpleName@@MethodName:read:[] @AT@ 9825 @LENGTH@ 1\n" +
                "---------INS SimpleName@@off @TO@ SimpleName@@MethodName:read:[] @AT@ 9828 @LENGTH@ 3\n" +
                "---------INS SimpleName@@len @TO@ SimpleName@@MethodName:read:[] @AT@ 9833 @LENGTH@ 3\n");
    }

    //commons-compress_d8ca98_2ed556_src#main#java#org#apache#commons#compress#archivers#sevenz#AES256SHA256Decoder.java
    @Test
    public void test_commons_compress_d8ca98_2ed556() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-compress_d8ca98_2ed556_src#main#java#org#apache#commons#compress#archivers#sevenz#AES256SHA256Decoder.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD MethodDeclaration@@public, void, MethodName:close,  @TO@ public, void, MethodName:close, IOException,  @AT@ 5337 @LENGTH@ 35\n" +
                "---INS SimpleType@@IOException @TO@ MethodDeclaration@@public, void, MethodName:close,  @AT@ 5364 @LENGTH@ 11\n" +
                "---INS IfStatement@@if (cipherInputStream != null) {  cipherInputStream.close();} @TO@ MethodDeclaration@@public, void, MethodName:close,  @AT@ 5394 @LENGTH@ 97\n" +
                "------INS InfixExpression@@cipherInputStream != null @TO@ IfStatement@@if (cipherInputStream != null) {  cipherInputStream.close();} @AT@ 5398 @LENGTH@ 25\n" +
                "---------INS SimpleName@@cipherInputStream @TO@ InfixExpression@@cipherInputStream != null @AT@ 5398 @LENGTH@ 17\n" +
                "---------INS Operator@@!= @TO@ InfixExpression@@cipherInputStream != null @AT@ 5415 @LENGTH@ 2\n" +
                "---------INS NullLiteral@@null @TO@ InfixExpression@@cipherInputStream != null @AT@ 5419 @LENGTH@ 4\n" +
                "------INS Block@@ThenBody:{  cipherInputStream.close();} @TO@ IfStatement@@if (cipherInputStream != null) {  cipherInputStream.close();} @AT@ 5425 @LENGTH@ 66\n" +
                "---------INS ExpressionStatement@@MethodInvocation:cipherInputStream.close() @TO@ Block@@ThenBody:{  cipherInputStream.close();} @AT@ 5447 @LENGTH@ 26\n" +
                "------------INS MethodInvocation@@cipherInputStream.close() @TO@ ExpressionStatement@@MethodInvocation:cipherInputStream.close() @AT@ 5447 @LENGTH@ 25\n" +
                "---------------INS SimpleName@@Name:cipherInputStream @TO@ MethodInvocation@@cipherInputStream.close() @AT@ 5447 @LENGTH@ 17\n" +
                "---------------INS SimpleName@@MethodName:close:[] @TO@ MethodInvocation@@cipherInputStream.close() @AT@ 5465 @LENGTH@ 7\n");
    }

    //fuse_2006f48_4dad2f_insight#insight-camel#src#main#java#org#fusesource#insight#camel#audit#ScriptUtils.java
    @Test
    public void test_fuse_2006f48_4dad2f() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_2006f48_4dad2f_insight#insight-camel#src#main#java#org#fusesource#insight#camel#audit#ScriptUtils.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD TryStatement@@try {  return mapper.writeValueAsString(o);} catch (Exception e) {  throw new IllegalArgumentException(\"Could not serialize \" + o,e);} @TO@ try {  if (o instanceof Collection) {    StringBuilder sb=new StringBuilder();    sb.append(\"[\");    for (    Object c : (Collection)o) {      if (sb.length() > 1) {        sb.append(\",\");      }      sb.append(toJson(c));    }    sb.append(\"]\");    return sb.toString();  } else   if (o instanceof Map) {    StringBuilder sb=new StringBuilder();    sb.append(\"{\");    for (    Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {      if (sb.length() > 1) {        sb.append(\",\");      }      sb.append(toJson(e.getKey().toString()));      sb.append(\":\");      sb.append(toJson(e.getValue()));    }    sb.append(\"}\");    return sb.toString();  } else   if (o == null) {    return \"null\";  } else   if (o instanceof Date) {    return \"\\\"\" + toIso((Date)o) + \"\\\"\";  } else {    return mapper.writeValueAsString(o.toString());  }} catch (Exception e) {  throw new IllegalArgumentException(\"Could not serialize \" + o,e);} @AT@ 1282 @LENGTH@ 175\n" +
                "---DEL ReturnStatement@@MethodInvocation:mapper.writeValueAsString(o) @AT@ 1300 @LENGTH@ 36\n" +
                "---INS IfStatement@@if (o instanceof Collection) {  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} else if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ TryStatement@@try {  return mapper.writeValueAsString(o);} catch (Exception e) {  throw new IllegalArgumentException(\"Could not serialize \" + o,e);} @AT@ 1431 @LENGTH@ 1233\n" +
                "------INS InstanceofExpression@@o instanceof Collection @TO@ IfStatement@@if (o instanceof Collection) {  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} else if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 1435 @LENGTH@ 23\n" +
                "---------INS SimpleName@@o @TO@ InstanceofExpression@@o instanceof Collection @AT@ 1435 @LENGTH@ 1\n" +
                "---------INS Instanceof@@instanceof @TO@ InstanceofExpression@@o instanceof Collection @AT@ 1437 @LENGTH@ 10\n" +
                "---------INS SimpleType@@Collection @TO@ InstanceofExpression@@o instanceof Collection @AT@ 1448 @LENGTH@ 10\n" +
                "------INS Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} @TO@ IfStatement@@if (o instanceof Collection) {  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} else if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 1460 @LENGTH@ 388\n" +
                "---------INS VariableDeclarationStatement@@StringBuilder sb=new StringBuilder(); @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} @AT@ 1478 @LENGTH@ 39\n" +
                "------------INS SimpleType@@StringBuilder @TO@ VariableDeclarationStatement@@StringBuilder sb=new StringBuilder(); @AT@ 1478 @LENGTH@ 13\n" +
                "------------INS VariableDeclarationFragment@@sb=new StringBuilder() @TO@ VariableDeclarationStatement@@StringBuilder sb=new StringBuilder(); @AT@ 1492 @LENGTH@ 24\n" +
                "---------------INS SimpleName@@sb @TO@ VariableDeclarationFragment@@sb=new StringBuilder() @AT@ 1492 @LENGTH@ 2\n" +
                "---------------INS ClassInstanceCreation@@StringBuilder[] @TO@ VariableDeclarationFragment@@sb=new StringBuilder() @AT@ 1497 @LENGTH@ 19\n" +
                "------------------INS New@@new @TO@ ClassInstanceCreation@@StringBuilder[] @AT@ 1497 @LENGTH@ 3\n" +
                "------------------INS SimpleType@@StringBuilder @TO@ ClassInstanceCreation@@StringBuilder[] @AT@ 1501 @LENGTH@ 13\n" +
                "---------INS ExpressionStatement@@MethodInvocation:sb.append(\"[\") @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} @AT@ 1534 @LENGTH@ 15\n" +
                "------------INS MethodInvocation@@sb.append(\"[\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\"[\") @AT@ 1534 @LENGTH@ 14\n" +
                "---------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\"[\") @AT@ 1534 @LENGTH@ 2\n" +
                "---------------INS SimpleName@@MethodName:append:[\"[\"] @TO@ MethodInvocation@@sb.append(\"[\") @AT@ 1537 @LENGTH@ 11\n" +
                "------------------INS StringLiteral@@\"[\" @TO@ SimpleName@@MethodName:append:[\"[\"] @AT@ 1544 @LENGTH@ 3\n" +
                "---------INS EnhancedForStatement@@for (Object c : (Collection)o) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(c));} @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} @AT@ 1566 @LENGTH@ 198\n" +
                "------------INS SingleVariableDeclaration@@Object c @TO@ EnhancedForStatement@@for (Object c : (Collection)o) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(c));} @AT@ 1571 @LENGTH@ 8\n" +
                "---------------INS SimpleType@@Object @TO@ SingleVariableDeclaration@@Object c @AT@ 1571 @LENGTH@ 6\n" +
                "---------------INS SimpleName@@c @TO@ SingleVariableDeclaration@@Object c @AT@ 1578 @LENGTH@ 1\n" +
                "------------INS CastExpression@@(Collection)o @TO@ EnhancedForStatement@@for (Object c : (Collection)o) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(c));} @AT@ 1582 @LENGTH@ 14\n" +
                "---------------INS SimpleType@@Collection @TO@ CastExpression@@(Collection)o @AT@ 1583 @LENGTH@ 10\n" +
                "---------------INS SimpleName@@o @TO@ CastExpression@@(Collection)o @AT@ 1595 @LENGTH@ 1\n" +
                "------------INS IfStatement@@if (sb.length() > 1) {  sb.append(\",\");} @TO@ EnhancedForStatement@@for (Object c : (Collection)o) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(c));} @AT@ 1620 @LENGTH@ 84\n" +
                "---------------INS InfixExpression@@sb.length() > 1 @TO@ IfStatement@@if (sb.length() > 1) {  sb.append(\",\");} @AT@ 1624 @LENGTH@ 15\n" +
                "------------------INS MethodInvocation@@sb.length() @TO@ InfixExpression@@sb.length() > 1 @AT@ 1624 @LENGTH@ 11\n" +
                "---------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.length() @AT@ 1624 @LENGTH@ 2\n" +
                "---------------------INS SimpleName@@MethodName:length:[] @TO@ MethodInvocation@@sb.length() @AT@ 1627 @LENGTH@ 8\n" +
                "------------------INS Operator@@> @TO@ InfixExpression@@sb.length() > 1 @AT@ 1635 @LENGTH@ 1\n" +
                "------------------INS NumberLiteral@@1 @TO@ InfixExpression@@sb.length() > 1 @AT@ 1638 @LENGTH@ 1\n" +
                "---------------INS Block@@ThenBody:{  sb.append(\",\");} @TO@ IfStatement@@if (sb.length() > 1) {  sb.append(\",\");} @AT@ 1641 @LENGTH@ 63\n" +
                "------------------INS ExpressionStatement@@MethodInvocation:sb.append(\",\") @TO@ Block@@ThenBody:{  sb.append(\",\");} @AT@ 1667 @LENGTH@ 15\n" +
                "---------------------INS MethodInvocation@@sb.append(\",\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\",\") @AT@ 1667 @LENGTH@ 14\n" +
                "------------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\",\") @AT@ 1667 @LENGTH@ 2\n" +
                "------------------------INS SimpleName@@MethodName:append:[\",\"] @TO@ MethodInvocation@@sb.append(\",\") @AT@ 1670 @LENGTH@ 11\n" +
                "---------------------------INS StringLiteral@@\",\" @TO@ SimpleName@@MethodName:append:[\",\"] @AT@ 1677 @LENGTH@ 3\n" +
                "------------INS ExpressionStatement@@MethodInvocation:sb.append(toJson(c)) @TO@ EnhancedForStatement@@for (Object c : (Collection)o) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(c));} @AT@ 1725 @LENGTH@ 21\n" +
                "---------------INS MethodInvocation@@sb.append(toJson(c)) @TO@ ExpressionStatement@@MethodInvocation:sb.append(toJson(c)) @AT@ 1725 @LENGTH@ 20\n" +
                "------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(toJson(c)) @AT@ 1725 @LENGTH@ 2\n" +
                "------------------INS SimpleName@@MethodName:append:[toJson(c)] @TO@ MethodInvocation@@sb.append(toJson(c)) @AT@ 1728 @LENGTH@ 17\n" +
                "---------------------INS MethodInvocation@@toJson(c) @TO@ SimpleName@@MethodName:append:[toJson(c)] @AT@ 1735 @LENGTH@ 9\n" +
                "------------------------INS SimpleName@@MethodName:toJson:[c] @TO@ MethodInvocation@@toJson(c) @AT@ 1735 @LENGTH@ 9\n" +
                "---------------------------INS SimpleName@@c @TO@ SimpleName@@MethodName:toJson:[c] @AT@ 1742 @LENGTH@ 1\n" +
                "---------INS ExpressionStatement@@MethodInvocation:sb.append(\"]\") @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} @AT@ 1781 @LENGTH@ 15\n" +
                "------------INS MethodInvocation@@sb.append(\"]\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\"]\") @AT@ 1781 @LENGTH@ 14\n" +
                "---------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\"]\") @AT@ 1781 @LENGTH@ 2\n" +
                "---------------INS SimpleName@@MethodName:append:[\"]\"] @TO@ MethodInvocation@@sb.append(\"]\") @AT@ 1784 @LENGTH@ 11\n" +
                "------------------INS StringLiteral@@\"]\" @TO@ SimpleName@@MethodName:append:[\"]\"] @AT@ 1791 @LENGTH@ 3\n" +
                "---------INS ReturnStatement@@MethodInvocation:sb.toString() @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} @AT@ 1813 @LENGTH@ 21\n" +
                "------------INS MethodInvocation@@sb.toString() @TO@ ReturnStatement@@MethodInvocation:sb.toString() @AT@ 1820 @LENGTH@ 13\n" +
                "---------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.toString() @AT@ 1820 @LENGTH@ 2\n" +
                "---------------INS SimpleName@@MethodName:toString:[] @TO@ MethodInvocation@@sb.toString() @AT@ 1823 @LENGTH@ 10\n" +
                "------INS Block@@ElseBody:if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ IfStatement@@if (o instanceof Collection) {  StringBuilder sb=new StringBuilder();  sb.append(\"[\");  for (  Object c : (Collection)o) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(c));  }  sb.append(\"]\");  return sb.toString();} else if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 1854 @LENGTH@ 810\n" +
                "---------INS IfStatement@@if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ Block@@ElseBody:if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 1854 @LENGTH@ 810\n" +
                "------------INS InstanceofExpression@@o instanceof Map @TO@ IfStatement@@if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 1858 @LENGTH@ 16\n" +
                "---------------INS SimpleName@@o @TO@ InstanceofExpression@@o instanceof Map @AT@ 1858 @LENGTH@ 1\n" +
                "---------------INS Instanceof@@instanceof @TO@ InstanceofExpression@@o instanceof Map @AT@ 1860 @LENGTH@ 10\n" +
                "---------------INS SimpleType@@Map @TO@ InstanceofExpression@@o instanceof Map @AT@ 1871 @LENGTH@ 3\n" +
                "------------INS Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} @TO@ IfStatement@@if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 1876 @LENGTH@ 538\n" +
                "---------------INS VariableDeclarationStatement@@StringBuilder sb=new StringBuilder(); @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} @AT@ 1894 @LENGTH@ 39\n" +
                "------------------INS SimpleType@@StringBuilder @TO@ VariableDeclarationStatement@@StringBuilder sb=new StringBuilder(); @AT@ 1894 @LENGTH@ 13\n" +
                "------------------INS VariableDeclarationFragment@@sb=new StringBuilder() @TO@ VariableDeclarationStatement@@StringBuilder sb=new StringBuilder(); @AT@ 1908 @LENGTH@ 24\n" +
                "---------------------INS SimpleName@@sb @TO@ VariableDeclarationFragment@@sb=new StringBuilder() @AT@ 1908 @LENGTH@ 2\n" +
                "---------------------INS ClassInstanceCreation@@StringBuilder[] @TO@ VariableDeclarationFragment@@sb=new StringBuilder() @AT@ 1913 @LENGTH@ 19\n" +
                "------------------------INS New@@new @TO@ ClassInstanceCreation@@StringBuilder[] @AT@ 1913 @LENGTH@ 3\n" +
                "------------------------INS SimpleType@@StringBuilder @TO@ ClassInstanceCreation@@StringBuilder[] @AT@ 1917 @LENGTH@ 13\n" +
                "---------------INS ExpressionStatement@@MethodInvocation:sb.append(\"{\") @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} @AT@ 1950 @LENGTH@ 15\n" +
                "------------------INS MethodInvocation@@sb.append(\"{\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\"{\") @AT@ 1950 @LENGTH@ 14\n" +
                "---------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\"{\") @AT@ 1950 @LENGTH@ 2\n" +
                "---------------------INS SimpleName@@MethodName:append:[\"{\"] @TO@ MethodInvocation@@sb.append(\"{\") @AT@ 1953 @LENGTH@ 11\n" +
                "------------------------INS StringLiteral@@\"{\" @TO@ SimpleName@@MethodName:append:[\"{\"] @AT@ 1960 @LENGTH@ 3\n" +
                "---------------INS EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} @AT@ 1982 @LENGTH@ 348\n" +
                "------------------INS SingleVariableDeclaration@@Map.Entry<Object,Object> e @TO@ EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @AT@ 1987 @LENGTH@ 27\n" +
                "---------------------INS ParameterizedType@@Map.Entry<Object,Object> @TO@ SingleVariableDeclaration@@Map.Entry<Object,Object> e @AT@ 1987 @LENGTH@ 25\n" +
                "------------------------INS SimpleType@@Map.Entry @TO@ ParameterizedType@@Map.Entry<Object,Object> @AT@ 1987 @LENGTH@ 9\n" +
                "------------------------INS SimpleType@@Object @TO@ ParameterizedType@@Map.Entry<Object,Object> @AT@ 1997 @LENGTH@ 6\n" +
                "------------------------INS SimpleType@@Object @TO@ ParameterizedType@@Map.Entry<Object,Object> @AT@ 2005 @LENGTH@ 6\n" +
                "---------------------INS SimpleName@@e @TO@ SingleVariableDeclaration@@Map.Entry<Object,Object> e @AT@ 2013 @LENGTH@ 1\n" +
                "------------------INS MethodInvocation@@((Map<Object,Object>)o).entrySet() @TO@ EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @AT@ 2017 @LENGTH@ 36\n" +
                "---------------------INS ParenthesizedExpression@@((Map<Object,Object>)o) @TO@ MethodInvocation@@((Map<Object,Object>)o).entrySet() @AT@ 2017 @LENGTH@ 25\n" +
                "------------------------INS CastExpression@@(Map<Object,Object>)o @TO@ ParenthesizedExpression@@((Map<Object,Object>)o) @AT@ 2018 @LENGTH@ 23\n" +
                "---------------------------INS ParameterizedType@@Map<Object,Object> @TO@ CastExpression@@(Map<Object,Object>)o @AT@ 2019 @LENGTH@ 19\n" +
                "------------------------------INS SimpleType@@Map @TO@ ParameterizedType@@Map<Object,Object> @AT@ 2019 @LENGTH@ 3\n" +
                "------------------------------INS SimpleType@@Object @TO@ ParameterizedType@@Map<Object,Object> @AT@ 2023 @LENGTH@ 6\n" +
                "------------------------------INS SimpleType@@Object @TO@ ParameterizedType@@Map<Object,Object> @AT@ 2031 @LENGTH@ 6\n" +
                "---------------------------INS SimpleName@@o @TO@ CastExpression@@(Map<Object,Object>)o @AT@ 2040 @LENGTH@ 1\n" +
                "---------------------INS SimpleName@@MethodName:entrySet:[] @TO@ MethodInvocation@@((Map<Object,Object>)o).entrySet() @AT@ 2043 @LENGTH@ 10\n" +
                "------------------INS IfStatement@@if (sb.length() > 1) {  sb.append(\",\");} @TO@ EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @AT@ 2077 @LENGTH@ 84\n" +
                "---------------------INS InfixExpression@@sb.length() > 1 @TO@ IfStatement@@if (sb.length() > 1) {  sb.append(\",\");} @AT@ 2081 @LENGTH@ 15\n" +
                "------------------------INS MethodInvocation@@sb.length() @TO@ InfixExpression@@sb.length() > 1 @AT@ 2081 @LENGTH@ 11\n" +
                "---------------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.length() @AT@ 2081 @LENGTH@ 2\n" +
                "---------------------------INS SimpleName@@MethodName:length:[] @TO@ MethodInvocation@@sb.length() @AT@ 2084 @LENGTH@ 8\n" +
                "------------------------INS Operator@@> @TO@ InfixExpression@@sb.length() > 1 @AT@ 2092 @LENGTH@ 1\n" +
                "------------------------INS NumberLiteral@@1 @TO@ InfixExpression@@sb.length() > 1 @AT@ 2095 @LENGTH@ 1\n" +
                "---------------------INS Block@@ThenBody:{  sb.append(\",\");} @TO@ IfStatement@@if (sb.length() > 1) {  sb.append(\",\");} @AT@ 2098 @LENGTH@ 63\n" +
                "------------------------INS ExpressionStatement@@MethodInvocation:sb.append(\",\") @TO@ Block@@ThenBody:{  sb.append(\",\");} @AT@ 2124 @LENGTH@ 15\n" +
                "---------------------------INS MethodInvocation@@sb.append(\",\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\",\") @AT@ 2124 @LENGTH@ 14\n" +
                "------------------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\",\") @AT@ 2124 @LENGTH@ 2\n" +
                "------------------------------INS SimpleName@@MethodName:append:[\",\"] @TO@ MethodInvocation@@sb.append(\",\") @AT@ 2127 @LENGTH@ 11\n" +
                "---------------------------------INS StringLiteral@@\",\" @TO@ SimpleName@@MethodName:append:[\",\"] @AT@ 2134 @LENGTH@ 3\n" +
                "------------------INS ExpressionStatement@@MethodInvocation:sb.append(toJson(e.getKey().toString())) @TO@ EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @AT@ 2182 @LENGTH@ 41\n" +
                "---------------------INS MethodInvocation@@sb.append(toJson(e.getKey().toString())) @TO@ ExpressionStatement@@MethodInvocation:sb.append(toJson(e.getKey().toString())) @AT@ 2182 @LENGTH@ 40\n" +
                "------------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(toJson(e.getKey().toString())) @AT@ 2182 @LENGTH@ 2\n" +
                "------------------------INS SimpleName@@MethodName:append:[toJson(e.getKey().toString())] @TO@ MethodInvocation@@sb.append(toJson(e.getKey().toString())) @AT@ 2185 @LENGTH@ 37\n" +
                "---------------------------INS MethodInvocation@@toJson(e.getKey().toString()) @TO@ SimpleName@@MethodName:append:[toJson(e.getKey().toString())] @AT@ 2192 @LENGTH@ 29\n" +
                "------------------------------INS SimpleName@@MethodName:toJson:[e.getKey().toString()] @TO@ MethodInvocation@@toJson(e.getKey().toString()) @AT@ 2192 @LENGTH@ 29\n" +
                "---------------------------------INS MethodInvocation@@e.getKey().toString() @TO@ SimpleName@@MethodName:toJson:[e.getKey().toString()] @AT@ 2199 @LENGTH@ 21\n" +
                "------------------------------------INS MethodInvocation@@MethodName:getKey:[] @TO@ MethodInvocation@@e.getKey().toString() @AT@ 2199 @LENGTH@ 10\n" +
                "------------------------------------INS SimpleName@@Name:e @TO@ MethodInvocation@@e.getKey().toString() @AT@ 2199 @LENGTH@ 1\n" +
                "------------------------------------INS SimpleName@@MethodName:toString:[] @TO@ MethodInvocation@@e.getKey().toString() @AT@ 2210 @LENGTH@ 10\n" +
                "------------------------------INS SimpleName@@MethodName:toJson:[e.getKey().toString()] @TO@ MethodInvocation@@toJson(e.getKey().toString()) @AT@ 2192 @LENGTH@ 29\n" +
                "---------------------------------INS MethodInvocation@@e.getKey().toString() @TO@ SimpleName@@MethodName:toJson:[e.getKey().toString()] @AT@ 2199 @LENGTH@ 21\n" +
                "------------------------------------INS MethodInvocation@@MethodName:getKey:[] @TO@ MethodInvocation@@e.getKey().toString() @AT@ 2199 @LENGTH@ 10\n" +
                "------------------------------------INS SimpleName@@Name:e @TO@ MethodInvocation@@e.getKey().toString() @AT@ 2199 @LENGTH@ 1\n" +
                "------------------------------------INS SimpleName@@MethodName:toString:[] @TO@ MethodInvocation@@e.getKey().toString() @AT@ 2210 @LENGTH@ 10\n" +
                "------------------INS ExpressionStatement@@MethodInvocation:sb.append(\":\") @TO@ EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @AT@ 2244 @LENGTH@ 15\n" +
                "---------------------INS MethodInvocation@@sb.append(\":\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\":\") @AT@ 2244 @LENGTH@ 14\n" +
                "------------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\":\") @AT@ 2244 @LENGTH@ 2\n" +
                "------------------------INS SimpleName@@MethodName:append:[\":\"] @TO@ MethodInvocation@@sb.append(\":\") @AT@ 2247 @LENGTH@ 11\n" +
                "---------------------------INS StringLiteral@@\":\" @TO@ SimpleName@@MethodName:append:[\":\"] @AT@ 2254 @LENGTH@ 3\n" +
                "------------------INS ExpressionStatement@@MethodInvocation:sb.append(toJson(e.getValue())) @TO@ EnhancedForStatement@@for (Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {  if (sb.length() > 1) {    sb.append(\",\");  }  sb.append(toJson(e.getKey().toString()));  sb.append(\":\");  sb.append(toJson(e.getValue()));} @AT@ 2280 @LENGTH@ 32\n" +
                "---------------------INS MethodInvocation@@sb.append(toJson(e.getValue())) @TO@ ExpressionStatement@@MethodInvocation:sb.append(toJson(e.getValue())) @AT@ 2280 @LENGTH@ 31\n" +
                "------------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(toJson(e.getValue())) @AT@ 2280 @LENGTH@ 2\n" +
                "------------------------INS SimpleName@@MethodName:append:[toJson(e.getValue())] @TO@ MethodInvocation@@sb.append(toJson(e.getValue())) @AT@ 2283 @LENGTH@ 28\n" +
                "---------------------------INS MethodInvocation@@toJson(e.getValue()) @TO@ SimpleName@@MethodName:append:[toJson(e.getValue())] @AT@ 2290 @LENGTH@ 20\n" +
                "------------------------------INS SimpleName@@MethodName:toJson:[e.getValue()] @TO@ MethodInvocation@@toJson(e.getValue()) @AT@ 2290 @LENGTH@ 20\n" +
                "---------------------------------INS MethodInvocation@@e.getValue() @TO@ SimpleName@@MethodName:toJson:[e.getValue()] @AT@ 2297 @LENGTH@ 12\n" +
                "------------------------------------INS SimpleName@@Name:e @TO@ MethodInvocation@@e.getValue() @AT@ 2297 @LENGTH@ 1\n" +
                "------------------------------------INS SimpleName@@MethodName:getValue:[] @TO@ MethodInvocation@@e.getValue() @AT@ 2299 @LENGTH@ 10\n" +
                "------------------------------INS SimpleName@@MethodName:toJson:[e.getValue()] @TO@ MethodInvocation@@toJson(e.getValue()) @AT@ 2290 @LENGTH@ 20\n" +
                "---------------------------------INS MethodInvocation@@e.getValue() @TO@ SimpleName@@MethodName:toJson:[e.getValue()] @AT@ 2297 @LENGTH@ 12\n" +
                "------------------------------------INS SimpleName@@Name:e @TO@ MethodInvocation@@e.getValue() @AT@ 2297 @LENGTH@ 1\n" +
                "------------------------------------INS SimpleName@@MethodName:getValue:[] @TO@ MethodInvocation@@e.getValue() @AT@ 2299 @LENGTH@ 10\n" +
                "---------------INS ExpressionStatement@@MethodInvocation:sb.append(\"}\") @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} @AT@ 2347 @LENGTH@ 15\n" +
                "------------------INS MethodInvocation@@sb.append(\"}\") @TO@ ExpressionStatement@@MethodInvocation:sb.append(\"}\") @AT@ 2347 @LENGTH@ 14\n" +
                "---------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.append(\"}\") @AT@ 2347 @LENGTH@ 2\n" +
                "---------------------INS SimpleName@@MethodName:append:[\"}\"] @TO@ MethodInvocation@@sb.append(\"}\") @AT@ 2350 @LENGTH@ 11\n" +
                "------------------------INS StringLiteral@@\"}\" @TO@ SimpleName@@MethodName:append:[\"}\"] @AT@ 2357 @LENGTH@ 3\n" +
                "---------------INS ReturnStatement@@MethodInvocation:sb.toString() @TO@ Block@@ThenBody:{  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} @AT@ 2379 @LENGTH@ 21\n" +
                "------------------INS MethodInvocation@@sb.toString() @TO@ ReturnStatement@@MethodInvocation:sb.toString() @AT@ 2386 @LENGTH@ 13\n" +
                "---------------------INS SimpleName@@Name:sb @TO@ MethodInvocation@@sb.toString() @AT@ 2386 @LENGTH@ 2\n" +
                "---------------------INS SimpleName@@MethodName:toString:[] @TO@ MethodInvocation@@sb.toString() @AT@ 2389 @LENGTH@ 10\n" +
                "------------INS Block@@ElseBody:if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ IfStatement@@if (o instanceof Map) {  StringBuilder sb=new StringBuilder();  sb.append(\"{\");  for (  Map.Entry<Object,Object> e : ((Map<Object,Object>)o).entrySet()) {    if (sb.length() > 1) {      sb.append(\",\");    }    sb.append(toJson(e.getKey().toString()));    sb.append(\":\");    sb.append(toJson(e.getValue()));  }  sb.append(\"}\");  return sb.toString();} else if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2420 @LENGTH@ 244\n" +
                "---------------INS IfStatement@@if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ Block@@ElseBody:if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2420 @LENGTH@ 244\n" +
                "------------------INS InfixExpression@@o == null @TO@ IfStatement@@if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2424 @LENGTH@ 9\n" +
                "---------------------INS SimpleName@@o @TO@ InfixExpression@@o == null @AT@ 2424 @LENGTH@ 1\n" +
                "---------------------INS Operator@@== @TO@ InfixExpression@@o == null @AT@ 2425 @LENGTH@ 2\n" +
                "---------------------INS NullLiteral@@null @TO@ InfixExpression@@o == null @AT@ 2429 @LENGTH@ 4\n" +
                "------------------INS Block@@ThenBody:{  return \"null\";} @TO@ IfStatement@@if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2435 @LENGTH@ 46\n" +
                "---------------------INS ReturnStatement@@StringLiteral:\"null\" @TO@ Block@@ThenBody:{  return \"null\";} @AT@ 2453 @LENGTH@ 14\n" +
                "------------------------INS StringLiteral@@\"null\" @TO@ ReturnStatement@@StringLiteral:\"null\" @AT@ 2460 @LENGTH@ 6\n" +
                "------------------INS Block@@ElseBody:if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ IfStatement@@if (o == null) {  return \"null\";} else if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2487 @LENGTH@ 177\n" +
                "---------------------INS IfStatement@@if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @TO@ Block@@ElseBody:if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2487 @LENGTH@ 177\n" +
                "------------------------INS InstanceofExpression@@o instanceof Date @TO@ IfStatement@@if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2491 @LENGTH@ 17\n" +
                "---------------------------INS SimpleName@@o @TO@ InstanceofExpression@@o instanceof Date @AT@ 2491 @LENGTH@ 1\n" +
                "---------------------------INS Instanceof@@instanceof @TO@ InstanceofExpression@@o instanceof Date @AT@ 2493 @LENGTH@ 10\n" +
                "---------------------------INS SimpleType@@Date @TO@ InstanceofExpression@@o instanceof Date @AT@ 2504 @LENGTH@ 4\n" +
                "------------------------INS Block@@ThenBody:{  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} @TO@ IfStatement@@if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2510 @LENGTH@ 69\n" +
                "---------------------------INS ReturnStatement@@InfixExpression:\"\\\"\" + toIso((Date)o) + \"\\\"\" @TO@ Block@@ThenBody:{  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} @AT@ 2528 @LENGTH@ 37\n" +
                "------------------------------INS InfixExpression@@\"\\\"\" + toIso((Date)o) + \"\\\"\" @TO@ ReturnStatement@@InfixExpression:\"\\\"\" + toIso((Date)o) + \"\\\"\" @AT@ 2535 @LENGTH@ 29\n" +
                "---------------------------------INS StringLiteral@@\"\\\"\" @TO@ InfixExpression@@\"\\\"\" + toIso((Date)o) + \"\\\"\" @AT@ 2535 @LENGTH@ 4\n" +
                "---------------------------------INS Operator@@+ @TO@ InfixExpression@@\"\\\"\" + toIso((Date)o) + \"\\\"\" @AT@ 2539 @LENGTH@ 1\n" +
                "---------------------------------INS MethodInvocation@@toIso((Date)o) @TO@ InfixExpression@@\"\\\"\" + toIso((Date)o) + \"\\\"\" @AT@ 2542 @LENGTH@ 15\n" +
                "------------------------------------INS SimpleName@@MethodName:toIso:[(Date)o] @TO@ MethodInvocation@@toIso((Date)o) @AT@ 2542 @LENGTH@ 15\n" +
                "---------------------------------------INS CastExpression@@(Date)o @TO@ SimpleName@@MethodName:toIso:[(Date)o] @AT@ 2548 @LENGTH@ 8\n" +
                "------------------------------------------INS SimpleType@@Date @TO@ CastExpression@@(Date)o @AT@ 2549 @LENGTH@ 4\n" +
                "------------------------------------------INS SimpleName@@o @TO@ CastExpression@@(Date)o @AT@ 2555 @LENGTH@ 1\n" +
                "------------------------------------INS SimpleName@@MethodName:toIso:[(Date)o] @TO@ MethodInvocation@@toIso((Date)o) @AT@ 2542 @LENGTH@ 15\n" +
                "---------------------------------------INS CastExpression@@(Date)o @TO@ SimpleName@@MethodName:toIso:[(Date)o] @AT@ 2548 @LENGTH@ 8\n" +
                "------------------------------------------INS SimpleType@@Date @TO@ CastExpression@@(Date)o @AT@ 2549 @LENGTH@ 4\n" +
                "------------------------------------------INS SimpleName@@o @TO@ CastExpression@@(Date)o @AT@ 2555 @LENGTH@ 1\n" +
                "---------------------------------INS StringLiteral@@\"\\\"\" @TO@ InfixExpression@@\"\\\"\" + toIso((Date)o) + \"\\\"\" @AT@ 2560 @LENGTH@ 4\n" +
                "------------------------INS Block@@ElseBody:{  return mapper.writeValueAsString(o.toString());} @TO@ IfStatement@@if (o instanceof Date) {  return \"\\\"\" + toIso((Date)o) + \"\\\"\";} else {  return mapper.writeValueAsString(o.toString());} @AT@ 2585 @LENGTH@ 79\n" +
                "---------------------------INS ReturnStatement@@MethodInvocation:mapper.writeValueAsString(o.toString()) @TO@ Block@@ElseBody:{  return mapper.writeValueAsString(o.toString());} @AT@ 2603 @LENGTH@ 47\n" +
                "------------------------------MOV MethodInvocation@@mapper.writeValueAsString(o) @TO@ ReturnStatement@@MethodInvocation:mapper.writeValueAsString(o.toString()) @AT@ 1307 @LENGTH@ 28\n" +
                "---------------------------------UPD SimpleName@@MethodName:writeValueAsString:[o] @TO@ MethodName:writeValueAsString:[o.toString()] @AT@ 1314 @LENGTH@ 21\n" +
                "------------------------------------DEL SimpleName@@o @AT@ 1333 @LENGTH@ 1\n" +
                "------------------------------------INS MethodInvocation@@o.toString() @TO@ SimpleName@@MethodName:writeValueAsString:[o] @AT@ 2636 @LENGTH@ 12\n" +
                "---------------------------------------INS SimpleName@@Name:o @TO@ MethodInvocation@@o.toString() @AT@ 2636 @LENGTH@ 1\n" +
                "---------------------------------------INS SimpleName@@MethodName:toString:[] @TO@ MethodInvocation@@o.toString() @AT@ 2638 @LENGTH@ 10\n");
    }

    //commons-collections_045fda_add3a9_src#main#java#org#apache#commons#collections#map#AbstractHashedMap.java
    @Test
    public void test_collections_045fda_add3a9() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_045fda_add3a9_src#main#java#org#apache#commons#collections#map#AbstractHashedMap.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD CatchClause@@catch (final CloneNotSupportedException ex) {  return null;} @TO@ catch (final CloneNotSupportedException ex) {  throw new InternalError();} @AT@ 44401 @LENGTH@ 104\n" +
                "---DEL ReturnStatement@@NullLiteral:null @AT@ 44459 @LENGTH@ 12\n" +
                "------DEL NullLiteral@@null @AT@ 44466 @LENGTH@ 4\n" +
                "---INS ThrowStatement@@ClassInstanceCreation:new InternalError() @TO@ CatchClause@@catch (final CloneNotSupportedException ex) {  return null;} @AT@ 44530 @LENGTH@ 26\n" +
                "------INS ClassInstanceCreation@@InternalError[] @TO@ ThrowStatement@@ClassInstanceCreation:new InternalError() @AT@ 44536 @LENGTH@ 19\n" +
                "---------INS New@@new @TO@ ClassInstanceCreation@@InternalError[] @AT@ 44536 @LENGTH@ 3\n" +
                "---------INS SimpleType@@InternalError @TO@ ClassInstanceCreation@@InternalError[] @AT@ 44540 @LENGTH@ 13\n");
    }


    //metadata_a82665_e8bff4_appclient#src#main#java#org#jboss#metadata#appclient#parser#spec#ApplicationClientMetaDataParser.java
    @Test
    public void test_metadata_a82665_e8bff4() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("metadata_a82665_e8bff4_appclient#src#main#java#org#jboss#metadata#appclient#parser#spec#ApplicationClientMetaDataParser.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD SwitchStatement@@switch (ejbJarAttribute) {case ID:{    metaData.setId(value);    break;  }case VERSION:{  metaData.setVersion(value);}case METADATA_COMPLETE:{metaData.setMetadataComplete(true);break;}default :throw unexpectedAttribute(reader,i);} @TO@ switch (ejbJarAttribute) {case ID:{    metaData.setId(value);    break;  }case VERSION:{  metaData.setVersion(value);  break;}case METADATA_COMPLETE:{metaData.setMetadataComplete(Boolean.parseBoolean(value));break;}default :throw unexpectedAttribute(reader,i);} @AT@ 2859 @LENGTH@ 410\n" +
                "---INS BreakStatement@@ @TO@ SwitchStatement@@switch (ejbJarAttribute) {case ID:{    metaData.setId(value);    break;  }case VERSION:{  metaData.setVersion(value);}case METADATA_COMPLETE:{metaData.setMetadataComplete(true);break;}default :throw unexpectedAttribute(reader,i);} @AT@ 3074 @LENGTH@ 6\n" +
                "---UPD ExpressionStatement@@MethodInvocation:metaData.setMetadataComplete(true) @TO@ MethodInvocation:metaData.setMetadataComplete(Boolean.parseBoolean(value)) @AT@ 3128 @LENGTH@ 35\n" +
                "------UPD MethodInvocation@@metaData.setMetadataComplete(true) @TO@ metaData.setMetadataComplete(Boolean.parseBoolean(value)) @AT@ 3128 @LENGTH@ 34\n" +
                "---------UPD SimpleName@@MethodName:setMetadataComplete:[true] @TO@ MethodName:setMetadataComplete:[Boolean.parseBoolean(value)] @AT@ 3137 @LENGTH@ 25\n" +
                "------------DEL BooleanLiteral@@true @AT@ 3157 @LENGTH@ 4\n" +
                "------------INS MethodInvocation@@Boolean.parseBoolean(value) @TO@ SimpleName@@MethodName:setMetadataComplete:[true] @AT@ 3177 @LENGTH@ 27\n" +
                "---------------INS SimpleName@@Name:Boolean @TO@ MethodInvocation@@Boolean.parseBoolean(value) @AT@ 3177 @LENGTH@ 7\n" +
                "---------------INS SimpleName@@MethodName:parseBoolean:[value] @TO@ MethodInvocation@@Boolean.parseBoolean(value) @AT@ 3185 @LENGTH@ 19\n" +
                "------------------INS SimpleName@@value @TO@ SimpleName@@MethodName:parseBoolean:[value] @AT@ 3198 @LENGTH@ 5\n");
    }



    //fuse_6baf6f_ad4e95_fabric#fabric-core-agent-jclouds#src#main#java#org#fusesource#fabric#service#jclouds#JcloudsContainerProvider.java
    @Test
    public void test_fuse_6baf6f_ad4e95() throws IOException {
        //null pointer
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_6baf6f_ad4e95_fabric#fabric-core-agent-jclouds#src#main#java#org#fusesource#fabric#service#jclouds#JcloudsContainerProvider.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD MethodDeclaration@@public, Set<CreateJCloudsContainerMetadata>, MethodName:create, CreateJCloudsContainerOptions options, MalformedURLException, RunNodesException, URISyntaxException,  @TO@ public, Set<CreateJCloudsContainerMetadata>, MethodName:create, CreateJCloudsContainerOptions options, MalformedURLException, RunNodesException, URISyntaxException, InterruptedException,  @AT@ 2863 @LENGTH@ 4271\n" +
                "---INS SimpleType@@InterruptedException @TO@ MethodDeclaration@@public, Set<CreateJCloudsContainerMetadata>, MethodName:create, CreateJCloudsContainerOptions options, MalformedURLException, RunNodesException, URISyntaxException,  @AT@ 3021 @LENGTH@ 20\n" +
                "---UPD VariableDeclarationStatement@@Set<CreateJCloudsContainerMetadata> result=new LinkedHashSet<CreateJCloudsContainerMetadata>(); @TO@ final Set<CreateJCloudsContainerMetadata> result=new LinkedHashSet<CreateJCloudsContainerMetadata>(); @AT@ 3030 @LENGTH@ 97\n" +
                "------INS Modifier@@final @TO@ VariableDeclarationStatement@@Set<CreateJCloudsContainerMetadata> result=new LinkedHashSet<CreateJCloudsContainerMetadata>(); @AT@ 3051 @LENGTH@ 5\n" +
                "---INS ExpressionStatement@@MethodInvocation:Thread.sleep(5000) @TO@ MethodDeclaration@@public, Set<CreateJCloudsContainerMetadata>, MethodName:create, CreateJCloudsContainerOptions options, MalformedURLException, RunNodesException, URISyntaxException,  @AT@ 5007 @LENGTH@ 19\n" +
                "------INS MethodInvocation@@Thread.sleep(5000) @TO@ ExpressionStatement@@MethodInvocation:Thread.sleep(5000) @AT@ 5007 @LENGTH@ 18\n" +
                "---------INS SimpleName@@Name:Thread @TO@ MethodInvocation@@Thread.sleep(5000) @AT@ 5007 @LENGTH@ 6\n" +
                "---------INS SimpleName@@MethodName:sleep:[5000] @TO@ MethodInvocation@@Thread.sleep(5000) @AT@ 5014 @LENGTH@ 11\n" +
                "------------INS NumberLiteral@@5000 @TO@ SimpleName@@MethodName:sleep:[5000] @AT@ 5020 @LENGTH@ 4\n" +
                "---UPD IfStatement@@if (metadatas != null) {  for (  NodeMetadata nodeMetadata : metadatas) {    Credentials credentials=null;    if (options.getUser() != null) {      credentials=new Credentials(options.getUser(),nodeMetadata.getCredentials().credential);    } else {      credentials=nodeMetadata.getCredentials();    }    String id=nodeMetadata.getId();    Set<String> publicAddresses=nodeMetadata.getPublicAddresses();    for (    String pa : publicAddresses) {      if (first) {        first=false;      } else {        buffer.append(\",\");      }      buffer.append(pa + \":\" + options.getServicePort());    }    String containerName=options.getName();    if (options.getNumber() > 1) {      containerName+=suffix++;    }    String script=buildStartupScript(options.name(containerName));    if (credentials != null) {      computeService.runScriptOnNode(id,script,RunScriptOptions.Builder.overrideCredentialsWith(credentials).runAsRoot(false));    } else {      computeService.runScriptOnNode(id,script);    }    CreateJCloudsContainerMetadata jCloudsContainerMetadata=new CreateJCloudsContainerMetadata();    jCloudsContainerMetadata.setNodeId(nodeMetadata.getId());    jCloudsContainerMetadata.setContainerName(containerName);    jCloudsContainerMetadata.setPublicAddresses(nodeMetadata.getPublicAddresses());    jCloudsContainerMetadata.setHostname(nodeMetadata.getHostname());    result.add(jCloudsContainerMetadata);  }} @TO@ if (metadatas != null) {  for (  NodeMetadata nodeMetadata : metadatas) {    Credentials credentials=null;    if (options.getUser() != null) {      credentials=new Credentials(options.getUser(),nodeMetadata.getCredentials().credential);    } else {      credentials=nodeMetadata.getCredentials();    }    String id=nodeMetadata.getId();    Set<String> publicAddresses=nodeMetadata.getPublicAddresses();    String containerName=options.getName();    if (options.getNumber() > 1) {      containerName+=suffix++;    }    String script=buildStartupScript(options.name(containerName));    if (credentials != null) {      computeService.runScriptOnNode(id,script,RunScriptOptions.Builder.overrideCredentialsWith(credentials).runAsRoot(false));    } else {      computeService.runScriptOnNode(id,script);    }    CreateJCloudsContainerMetadata jCloudsContainerMetadata=new CreateJCloudsContainerMetadata();    jCloudsContainerMetadata.setNodeId(nodeMetadata.getId());    jCloudsContainerMetadata.setContainerName(containerName);    jCloudsContainerMetadata.setPublicAddresses(nodeMetadata.getPublicAddresses());    jCloudsContainerMetadata.setHostname(nodeMetadata.getHostname());    result.add(jCloudsContainerMetadata);  }} @AT@ 5086 @LENGTH@ 2018\n" +
                "------UPD Block@@ThenBody:{  for (  NodeMetadata nodeMetadata : metadatas) {    Credentials credentials=null;    if (options.getUser() != null) {      credentials=new Credentials(options.getUser(),nodeMetadata.getCredentials().credential);    } else {      credentials=nodeMetadata.getCredentials();    }    String id=nodeMetadata.getId();    Set<String> publicAddresses=nodeMetadata.getPublicAddresses();    for (    String pa : publicAddresses) {      if (first) {        first=false;      } else {        buffer.append(\",\");      }      buffer.append(pa + \":\" + options.getServicePort());    }    String containerName=options.getName();    if (options.getNumber() > 1) {      containerName+=suffix++;    }    String script=buildStartupScript(options.name(containerName));    if (credentials != null) {      computeService.runScriptOnNode(id,script,RunScriptOptions.Builder.overrideCredentialsWith(credentials).runAsRoot(false));    } else {      computeService.runScriptOnNode(id,script);    }    CreateJCloudsContainerMetadata jCloudsContainerMetadata=new CreateJCloudsContainerMetadata();    jCloudsContainerMetadata.setNodeId(nodeMetadata.getId());    jCloudsContainerMetadata.setContainerName(containerName);    jCloudsContainerMetadata.setPublicAddresses(nodeMetadata.getPublicAddresses());    jCloudsContainerMetadata.setHostname(nodeMetadata.getHostname());    result.add(jCloudsContainerMetadata);  }} @TO@ ThenBody:{  for (  NodeMetadata nodeMetadata : metadatas) {    Credentials credentials=null;    if (options.getUser() != null) {      credentials=new Credentials(options.getUser(),nodeMetadata.getCredentials().credential);    } else {      credentials=nodeMetadata.getCredentials();    }    String id=nodeMetadata.getId();    Set<String> publicAddresses=nodeMetadata.getPublicAddresses();    String containerName=options.getName();    if (options.getNumber() > 1) {      containerName+=suffix++;    }    String script=buildStartupScript(options.name(containerName));    if (credentials != null) {      computeService.runScriptOnNode(id,script,RunScriptOptions.Builder.overrideCredentialsWith(credentials).runAsRoot(false));    } else {      computeService.runScriptOnNode(id,script);    }    CreateJCloudsContainerMetadata jCloudsContainerMetadata=new CreateJCloudsContainerMetadata();    jCloudsContainerMetadata.setNodeId(nodeMetadata.getId());    jCloudsContainerMetadata.setContainerName(containerName);    jCloudsContainerMetadata.setPublicAddresses(nodeMetadata.getPublicAddresses());    jCloudsContainerMetadata.setHostname(nodeMetadata.getHostname());    result.add(jCloudsContainerMetadata);  }} @AT@ 5109 @LENGTH@ 1995\n" +
                "---------UPD EnhancedForStatement@@for (NodeMetadata nodeMetadata : metadatas) {  Credentials credentials=null;  if (options.getUser() != null) {    credentials=new Credentials(options.getUser(),nodeMetadata.getCredentials().credential);  } else {    credentials=nodeMetadata.getCredentials();  }  String id=nodeMetadata.getId();  Set<String> publicAddresses=nodeMetadata.getPublicAddresses();  for (  String pa : publicAddresses) {    if (first) {      first=false;    } else {      buffer.append(\",\");    }    buffer.append(pa + \":\" + options.getServicePort());  }  String containerName=options.getName();  if (options.getNumber() > 1) {    containerName+=suffix++;  }  String script=buildStartupScript(options.name(containerName));  if (credentials != null) {    computeService.runScriptOnNode(id,script,RunScriptOptions.Builder.overrideCredentialsWith(credentials).runAsRoot(false));  } else {    computeService.runScriptOnNode(id,script);  }  CreateJCloudsContainerMetadata jCloudsContainerMetadata=new CreateJCloudsContainerMetadata();  jCloudsContainerMetadata.setNodeId(nodeMetadata.getId());  jCloudsContainerMetadata.setContainerName(containerName);  jCloudsContainerMetadata.setPublicAddresses(nodeMetadata.getPublicAddresses());  jCloudsContainerMetadata.setHostname(nodeMetadata.getHostname());  result.add(jCloudsContainerMetadata);} @TO@ for (NodeMetadata nodeMetadata : metadatas) {  Credentials credentials=null;  if (options.getUser() != null) {    credentials=new Credentials(options.getUser(),nodeMetadata.getCredentials().credential);  } else {    credentials=nodeMetadata.getCredentials();  }  String id=nodeMetadata.getId();  Set<String> publicAddresses=nodeMetadata.getPublicAddresses();  String containerName=options.getName();  if (options.getNumber() > 1) {    containerName+=suffix++;  }  String script=buildStartupScript(options.name(containerName));  if (credentials != null) {    computeService.runScriptOnNode(id,script,RunScriptOptions.Builder.overrideCredentialsWith(credentials).runAsRoot(false));  } else {    computeService.runScriptOnNode(id,script);  }  CreateJCloudsContainerMetadata jCloudsContainerMetadata=new CreateJCloudsContainerMetadata();  jCloudsContainerMetadata.setNodeId(nodeMetadata.getId());  jCloudsContainerMetadata.setContainerName(containerName);  jCloudsContainerMetadata.setPublicAddresses(nodeMetadata.getPublicAddresses());  jCloudsContainerMetadata.setHostname(nodeMetadata.getHostname());  result.add(jCloudsContainerMetadata);} @AT@ 5123 @LENGTH@ 1971\n" +
                "------------DEL EnhancedForStatement@@for (String pa : publicAddresses) {  if (first) {    first=false;  } else {    buffer.append(\",\");  }  buffer.append(pa + \":\" + options.getServicePort());} @AT@ 5753 @LENGTH@ 291\n" +
                "---------------DEL SingleVariableDeclaration@@String pa @AT@ 5758 @LENGTH@ 9\n" +
                "------------------DEL SimpleType@@String @AT@ 5758 @LENGTH@ 6\n" +
                "------------------DEL SimpleName@@pa @AT@ 5765 @LENGTH@ 2\n" +
                "---------------DEL SimpleName@@publicAddresses @AT@ 5769 @LENGTH@ 15\n" +
                "---------------DEL IfStatement@@if (first) {  first=false;} else {  buffer.append(\",\");} @AT@ 5808 @LENGTH@ 146\n" +
                "------------------DEL SimpleName@@first @AT@ 5812 @LENGTH@ 5\n" +
                "------------------DEL Block@@ThenBody:{  first=false;} @AT@ 5819 @LENGTH@ 62\n" +
                "---------------------DEL ExpressionStatement@@Assignment:first=false @AT@ 5845 @LENGTH@ 14\n" +
                "------------------------DEL Assignment@@first=false @AT@ 5845 @LENGTH@ 13\n" +
                "---------------------------DEL SimpleName@@first @AT@ 5845 @LENGTH@ 5\n" +
                "---------------------------DEL Operator@@= @AT@ 5850 @LENGTH@ 1\n" +
                "---------------------------DEL BooleanLiteral@@false @AT@ 5853 @LENGTH@ 5\n" +
                "------------------DEL Block@@ElseBody:{  buffer.append(\",\");} @AT@ 5887 @LENGTH@ 67\n" +
                "---------------------DEL ExpressionStatement@@MethodInvocation:buffer.append(\",\") @AT@ 5913 @LENGTH@ 19\n" +
                "------------------------DEL MethodInvocation@@buffer.append(\",\") @AT@ 5913 @LENGTH@ 18\n" +
                "---------------------------DEL SimpleName@@Name:buffer @AT@ 5913 @LENGTH@ 6\n" +
                "---------------------------DEL SimpleName@@MethodName:append:[\",\"] @AT@ 5920 @LENGTH@ 11\n" +
                "------------------------------DEL StringLiteral@@\",\" @AT@ 5927 @LENGTH@ 3\n" +
                "---------------DEL ExpressionStatement@@MethodInvocation:buffer.append(pa + \":\" + options.getServicePort()) @AT@ 5975 @LENGTH@ 51\n" +
                "------------------DEL MethodInvocation@@buffer.append(pa + \":\" + options.getServicePort()) @AT@ 5975 @LENGTH@ 50\n" +
                "---------------------DEL SimpleName@@Name:buffer @AT@ 5975 @LENGTH@ 6\n" +
                "---------------------DEL SimpleName@@MethodName:append:[pa + \":\" + options.getServicePort()] @AT@ 5982 @LENGTH@ 43\n" +
                "------------------------DEL InfixExpression@@pa + \":\" + options.getServicePort() @AT@ 5989 @LENGTH@ 35\n" +
                "---------------------------DEL SimpleName@@pa @AT@ 5989 @LENGTH@ 2\n" +
                "---------------------------DEL Operator@@+ @AT@ 5991 @LENGTH@ 1\n" +
                "---------------------------DEL StringLiteral@@\":\" @AT@ 5994 @LENGTH@ 3\n" +
                "---------------------------DEL MethodInvocation@@options.getServicePort() @AT@ 6000 @LENGTH@ 24\n" +
                "------------------------------DEL SimpleName@@Name:options @AT@ 6000 @LENGTH@ 7\n" +
                "------------------------------DEL SimpleName@@MethodName:getServicePort:[] @AT@ 6008 @LENGTH@ 16\n");
    }



    //commons-collections_51c9ef_d65e29_src#java#org#apache#commons#collections#iterators#IteratorChain.java
    @Test
    public void test_collections_51c9ef_d65e29() throws IOException {
        //MOVE - DEL 2 action example
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_51c9ef_d65e29_src#java#org#apache#commons#collections#iterators#IteratorChain.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"INS IfStatement@@if (currentIterator == null) {  updateCurrentIterator();} @TO@ MethodDeclaration@@public, void, MethodName:remove,  @AT@ 9501 @LENGTH@ 78\n" +
                "---INS InfixExpression@@currentIterator == null @TO@ IfStatement@@if (currentIterator == null) {  updateCurrentIterator();} @AT@ 9505 @LENGTH@ 23\n" +
                "------INS SimpleName@@currentIterator @TO@ InfixExpression@@currentIterator == null @AT@ 9505 @LENGTH@ 15\n" +
                "------INS Operator@@== @TO@ InfixExpression@@currentIterator == null @AT@ 9520 @LENGTH@ 2\n" +
                "------INS NullLiteral@@null @TO@ InfixExpression@@currentIterator == null @AT@ 9524 @LENGTH@ 4\n" +
                "---INS Block@@ThenBody:{  updateCurrentIterator();} @TO@ IfStatement@@if (currentIterator == null) {  updateCurrentIterator();} @AT@ 9531 @LENGTH@ 48\n" +
                "------INS ExpressionStatement@@MethodInvocation:updateCurrentIterator() @TO@ Block@@ThenBody:{  updateCurrentIterator();} @AT@ 9545 @LENGTH@ 24\n" +
                "---------MOV MethodInvocation@@MethodName:updateCurrentIterator:[] @TO@ ExpressionStatement@@MethodInvocation:updateCurrentIterator() @AT@ 9522 @LENGTH@ 23\n");
    }



    //fuse_d6d40b_b37c85_fabric-zookeeper-commands#src#main#java#org#fusesource#fabric#zookeeper#commands#Export.java
    @Test
    public void test_fuse_d6d40b_b37c85() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_d6d40b_b37c85_fabric-zookeeper-commands#src#main#java#org#fusesource#fabric#zookeeper#commands#Export.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD IfStatement@@if (!matches(include,p) || matches(exclude,p)) {  continue;} @TO@ if (!matches(include,p,true) || matches(exclude,p,false)) {  continue;} @AT@ 3334 @LENGTH@ 90\n" +
                "---UPD InfixExpression@@!matches(include,p) || matches(exclude,p) @TO@ !matches(include,p,true) || matches(exclude,p,false) @AT@ 3338 @LENGTH@ 43\n" +
                "------UPD PrefixExpression@@!matches(include,p) @TO@ !matches(include,p,true) @AT@ 3338 @LENGTH@ 20\n" +
                "---------UPD MethodInvocation@@matches(include,p) @TO@ matches(include,p,true) @AT@ 3339 @LENGTH@ 19\n" +
                "------------UPD SimpleName@@MethodName:matches:[include, p] @TO@ MethodName:matches:[include, p, true] @AT@ 3339 @LENGTH@ 19\n" +
                "---------------INS BooleanLiteral@@true @TO@ SimpleName@@MethodName:matches:[include, p] @AT@ 3359 @LENGTH@ 4\n" +
                "------UPD MethodInvocation@@matches(exclude,p) @TO@ matches(exclude,p,false) @AT@ 3362 @LENGTH@ 19\n" +
                "---------UPD SimpleName@@MethodName:matches:[exclude, p] @TO@ MethodName:matches:[exclude, p, false] @AT@ 3362 @LENGTH@ 19\n" +
                "------------INS BooleanLiteral@@false @TO@ SimpleName@@MethodName:matches:[exclude, p] @AT@ 3388 @LENGTH@ 5\n");
    }

    //fuse_d6d40b_b37c85_fabric-zookeeper-commands#src#main#java#org#fusesource#fabric#zookeeper#commands#Import.java
    @Test
    public void test_fuse_d6d40b_b37c85_() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_d6d40b_b37c85_fabric-zookeeper-commands#src#main#java#org#fusesource#fabric#zookeeper#commands#Import.java");
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD IfStatement@@if (!matches(include,key) || matches(exclude,key)) {  continue;} @TO@ if (!matches(include,key,true) || matches(exclude,key,false)) {  continue;} @AT@ 5493 @LENGTH@ 94\n" +
                "---UPD InfixExpression@@!matches(include,key) || matches(exclude,key) @TO@ !matches(include,key,true) || matches(exclude,key,false) @AT@ 5497 @LENGTH@ 47\n" +
                "------UPD PrefixExpression@@!matches(include,key) @TO@ !matches(include,key,true) @AT@ 5497 @LENGTH@ 22\n" +
                "---------UPD MethodInvocation@@matches(include,key) @TO@ matches(include,key,true) @AT@ 5498 @LENGTH@ 21\n" +
                "------------UPD SimpleName@@MethodName:matches:[include, key] @TO@ MethodName:matches:[include, key, true] @AT@ 5498 @LENGTH@ 21\n" +
                "---------------INS BooleanLiteral@@true @TO@ SimpleName@@MethodName:matches:[include, key] @AT@ 5520 @LENGTH@ 4\n" +
                "------UPD MethodInvocation@@matches(exclude,key) @TO@ matches(exclude,key,false) @AT@ 5523 @LENGTH@ 21\n" +
                "---------UPD SimpleName@@MethodName:matches:[exclude, key] @TO@ MethodName:matches:[exclude, key, false] @AT@ 5523 @LENGTH@ 21\n" +
                "------------INS BooleanLiteral@@false @TO@ SimpleName@@MethodName:matches:[exclude, key] @AT@ 5551 @LENGTH@ 5\n");
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD IfStatement@@if (!matches(include,key) || matches(exclude,key)) {  continue;} @TO@ if (!matches(include,key,true) || matches(exclude,key,false)) {  continue;} @AT@ 5493 @LENGTH@ 94\n" +
                "---UPD InfixExpression@@!matches(include,key) || matches(exclude,key) @TO@ !matches(include,key,true) || matches(exclude,key,false) @AT@ 5497 @LENGTH@ 47\n" +
                "------UPD PrefixExpression@@!matches(include,key) @TO@ !matches(include,key,true) @AT@ 5497 @LENGTH@ 22\n" +
                "---------UPD MethodInvocation@@matches(include,key) @TO@ matches(include,key,true) @AT@ 5498 @LENGTH@ 21\n" +
                "------------UPD SimpleName@@MethodName:matches:[include, key] @TO@ MethodName:matches:[include, key, true] @AT@ 5498 @LENGTH@ 21\n" +
                "---------------INS BooleanLiteral@@true @TO@ SimpleName@@MethodName:matches:[include, key] @AT@ 5520 @LENGTH@ 4\n" +
                "------UPD MethodInvocation@@matches(exclude,key) @TO@ matches(exclude,key,false) @AT@ 5523 @LENGTH@ 21\n" +
                "---------UPD SimpleName@@MethodName:matches:[exclude, key] @TO@ MethodName:matches:[exclude, key, false] @AT@ 5523 @LENGTH@ 21\n" +
                "------------INS BooleanLiteral@@false @TO@ SimpleName@@MethodName:matches:[exclude, key] @AT@ 5551 @LENGTH@ 5\n");
    }

    //fuse_6d0e56_998321_fabric#fabric-agent#src#main#java#org#fusesource#fabric#agent#DeploymentAgent.java
    @Test
    public void test_fuse_6d0e56_998321() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_6d0e56_998321_fabric#fabric-agent#src#main#java#org#fusesource#fabric#agent#DeploymentAgent.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD CatchClause@@catch (IOException e) {  errors.add(e);} @TO@ catch (Throwable e) {  errors.add(e);} @AT@ 42072 @LENGTH@ 84\n" +
                "---UPD SingleVariableDeclaration@@IOException e @TO@ Throwable e @AT@ 42079 @LENGTH@ 13\n" +
                "------UPD SimpleType@@IOException @TO@ Throwable @AT@ 42079 @LENGTH@ 11\n");
    }

    //fuse_b0e8c7_602ea9_fabric#fabric-core#src#main#java#org#fusesource#fabric#service#AbstractDataStore.java
    @Test
    public void test_fuse_b0e8c7_602ea9() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_b0e8c7_602ea9_fabric#fabric-core#src#main#java#org#fusesource#fabric#service#AbstractDataStore.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ReturnStatement@@MethodInvocation:availableResolvers.get(scheme).resolve(pid,key,toSubstitute) @TO@ MethodInvocation:availableResolvers.get(scheme).resolve(configs,pid,key,toSubstitute) @AT@ 9506 @LENGTH@ 70\n" +
                "---UPD MethodInvocation@@availableResolvers.get(scheme).resolve(pid,key,toSubstitute) @TO@ availableResolvers.get(scheme).resolve(configs,pid,key,toSubstitute) @AT@ 9513 @LENGTH@ 62\n" +
                "------UPD SimpleName@@MethodName:resolve:[pid, key, toSubstitute] @TO@ MethodName:resolve:[configs, pid, key, toSubstitute] @AT@ 9544 @LENGTH@ 31\n" +
                "---------INS SimpleName@@configs @TO@ SimpleName@@MethodName:resolve:[pid, key, toSubstitute] @AT@ 9552 @LENGTH@ 7\n");
    }


    //commons-collections_2d2aef_fb3daa_src#java#org#apache#commons#collections#map#TransformedMap.java
    @Test
    public void test_collections_2d2aef_fb3daa() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_2d2aef_fb3daa_src#java#org#apache#commons#collections#map#TransformedMap.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ExpressionStatement@@MethodInvocation:result.put((K)transformKey(entry.getKey()),transformValue(entry.getValue())) @TO@ MethodInvocation:result.put(transformKey(entry.getKey()),transformValue(entry.getValue())) @AT@ 7727 @LENGTH@ 79\n" +
                "---UPD MethodInvocation@@result.put((K)transformKey(entry.getKey()),transformValue(entry.getValue())) @TO@ result.put(transformKey(entry.getKey()),transformValue(entry.getValue())) @AT@ 7727 @LENGTH@ 78\n" +
                "------UPD SimpleName@@MethodName:put:[(K)transformKey(entry.getKey()), transformValue(entry.getValue())] @TO@ MethodName:put:[transformKey(entry.getKey()), transformValue(entry.getValue())] @AT@ 7734 @LENGTH@ 71\n" +
                "---------DEL CastExpression@@(K)transformKey(entry.getKey()) @AT@ 7738 @LENGTH@ 32\n" +
                "------------DEL SimpleType@@K @AT@ 7739 @LENGTH@ 1\n" +
                "---------MOV MethodInvocation@@transformKey(entry.getKey()) @TO@ SimpleName@@MethodName:put:[(K)transformKey(entry.getKey()), transformValue(entry.getValue())] @AT@ 7742 @LENGTH@ 28\n");
    }


    //fuse_6d0e56_998321_fabric#fabric-agent#src#main#java#org#fusesource#fabric#agent#download#MavenDownloadTask.java
    @Test
    public void test_fuse_6d0e56_998321_() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_6d0e56_998321_fabric#fabric-agent#src#main#java#org#fusesource#fabric#agent#download#MavenDownloadTask.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @TO@ try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  file.getParentFile().mkdirs();  if (!file.getParentFile().isDirectory()) {    throw new IOException(\"Unable to create directory \" + file.getParentFile().toString());  }  File tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile());  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  is.close();  os.close();  if (file.exists() && !file.delete()) {    throw new IOException(\"Unable to delete file: \" + file.toString());  }  if (!tmp.renameTo(file)) {    throw new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString());  }  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 3516 @LENGTH@ 1028\n" +
                "---DEL VariableDeclarationStatement@@File tmp=new File(file.getAbsolutePath() + \".tmp\"); @AT@ 3971 @LENGTH@ 53\n" +
                "------DEL VariableDeclarationFragment@@tmp=new File(file.getAbsolutePath() + \".tmp\") @AT@ 3976 @LENGTH@ 47\n" +
                "---------DEL ClassInstanceCreation@@File[file.getAbsolutePath() + \".tmp\"] @AT@ 3982 @LENGTH@ 41\n" +
                "---INS ExpressionStatement@@MethodInvocation:file.getParentFile().mkdirs() @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 3972 @LENGTH@ 30\n" +
                "------INS MethodInvocation@@file.getParentFile().mkdirs() @TO@ ExpressionStatement@@MethodInvocation:file.getParentFile().mkdirs() @AT@ 3972 @LENGTH@ 29\n" +
                "---------INS MethodInvocation@@MethodName:getParentFile:[] @TO@ MethodInvocation@@file.getParentFile().mkdirs() @AT@ 3972 @LENGTH@ 20\n" +
                "---------INS SimpleName@@Name:file @TO@ MethodInvocation@@file.getParentFile().mkdirs() @AT@ 3972 @LENGTH@ 4\n" +
                "---------INS SimpleName@@MethodName:mkdirs:[] @TO@ MethodInvocation@@file.getParentFile().mkdirs() @AT@ 3993 @LENGTH@ 8\n" +
                "---INS IfStatement@@if (!file.getParentFile().isDirectory()) {  throw new IOException(\"Unable to create directory \" + file.getParentFile().toString());} @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 4019 @LENGTH@ 168\n" +
                "------INS PrefixExpression@@!file.getParentFile().isDirectory() @TO@ IfStatement@@if (!file.getParentFile().isDirectory()) {  throw new IOException(\"Unable to create directory \" + file.getParentFile().toString());} @AT@ 4023 @LENGTH@ 35\n" +
                "---------INS Operator@@! @TO@ PrefixExpression@@!file.getParentFile().isDirectory() @AT@ 4023 @LENGTH@ 1\n" +
                "---------INS MethodInvocation@@file.getParentFile().isDirectory() @TO@ PrefixExpression@@!file.getParentFile().isDirectory() @AT@ 4024 @LENGTH@ 34\n" +
                "------------INS MethodInvocation@@MethodName:getParentFile:[] @TO@ MethodInvocation@@file.getParentFile().isDirectory() @AT@ 4024 @LENGTH@ 20\n" +
                "------------INS SimpleName@@Name:file @TO@ MethodInvocation@@file.getParentFile().isDirectory() @AT@ 4024 @LENGTH@ 4\n" +
                "------------INS SimpleName@@MethodName:isDirectory:[] @TO@ MethodInvocation@@file.getParentFile().isDirectory() @AT@ 4045 @LENGTH@ 13\n" +
                "------INS Block@@ThenBody:{  throw new IOException(\"Unable to create directory \" + file.getParentFile().toString());} @TO@ IfStatement@@if (!file.getParentFile().isDirectory()) {  throw new IOException(\"Unable to create directory \" + file.getParentFile().toString());} @AT@ 4060 @LENGTH@ 127\n" +
                "---------INS ThrowStatement@@ClassInstanceCreation:new IOException(\"Unable to create directory \" + file.getParentFile().toString()) @TO@ Block@@ThenBody:{  throw new IOException(\"Unable to create directory \" + file.getParentFile().toString());} @AT@ 4082 @LENGTH@ 87\n" +
                "------------INS ClassInstanceCreation@@IOException[\"Unable to create directory \" + file.getParentFile().toString()] @TO@ ThrowStatement@@ClassInstanceCreation:new IOException(\"Unable to create directory \" + file.getParentFile().toString()) @AT@ 4088 @LENGTH@ 80\n" +
                "---------------INS New@@new @TO@ ClassInstanceCreation@@IOException[\"Unable to create directory \" + file.getParentFile().toString()] @AT@ 4088 @LENGTH@ 3\n" +
                "---------------INS SimpleType@@IOException @TO@ ClassInstanceCreation@@IOException[\"Unable to create directory \" + file.getParentFile().toString()] @AT@ 4092 @LENGTH@ 11\n" +
                "---------------INS InfixExpression@@\"Unable to create directory \" + file.getParentFile().toString() @TO@ ClassInstanceCreation@@IOException[\"Unable to create directory \" + file.getParentFile().toString()] @AT@ 4104 @LENGTH@ 63\n" +
                "------------------INS StringLiteral@@\"Unable to create directory \" @TO@ InfixExpression@@\"Unable to create directory \" + file.getParentFile().toString() @AT@ 4104 @LENGTH@ 29\n" +
                "------------------INS Operator@@+ @TO@ InfixExpression@@\"Unable to create directory \" + file.getParentFile().toString() @AT@ 4133 @LENGTH@ 1\n" +
                "------------------INS MethodInvocation@@file.getParentFile().toString() @TO@ InfixExpression@@\"Unable to create directory \" + file.getParentFile().toString() @AT@ 4136 @LENGTH@ 31\n" +
                "---------------------INS MethodInvocation@@MethodName:getParentFile:[] @TO@ MethodInvocation@@file.getParentFile().toString() @AT@ 4136 @LENGTH@ 20\n" +
                "---------------------INS SimpleName@@Name:file @TO@ MethodInvocation@@file.getParentFile().toString() @AT@ 4136 @LENGTH@ 4\n" +
                "---------------------INS SimpleName@@MethodName:toString:[] @TO@ MethodInvocation@@file.getParentFile().toString() @AT@ 4157 @LENGTH@ 10\n" +
                "---DEL ExpressionStatement@@MethodInvocation:tmp.getParentFile().mkdirs() @AT@ 4041 @LENGTH@ 29\n" +
                "------DEL MethodInvocation@@tmp.getParentFile().mkdirs() @AT@ 4041 @LENGTH@ 28\n" +
                "---------DEL MethodInvocation@@MethodName:getParentFile:[] @AT@ 4041 @LENGTH@ 19\n" +
                "---DEL ExpressionStatement@@MethodInvocation:file.delete() @AT@ 4178 @LENGTH@ 14\n" +
                "------DEL MethodInvocation@@file.delete() @AT@ 4178 @LENGTH@ 13\n" +
                "---INS VariableDeclarationStatement@@File tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile()); @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 4204 @LENGTH@ 76\n" +
                "------MOV SimpleType@@File @TO@ VariableDeclarationStatement@@File tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile()); @AT@ 3971 @LENGTH@ 4\n" +
                "------INS VariableDeclarationFragment@@tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile()) @TO@ VariableDeclarationStatement@@File tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile()); @AT@ 4209 @LENGTH@ 70\n" +
                "---------MOV SimpleName@@tmp @TO@ VariableDeclarationFragment@@tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile()) @AT@ 3976 @LENGTH@ 3\n" +
                "---------INS MethodInvocation@@File.createTempFile(\"fabric-agent-\",null,file.getParentFile()) @TO@ VariableDeclarationFragment@@tmp=File.createTempFile(\"fabric-agent-\",null,file.getParentFile()) @AT@ 4215 @LENGTH@ 64\n" +
                "------------INS SimpleName@@Name:File @TO@ MethodInvocation@@File.createTempFile(\"fabric-agent-\",null,file.getParentFile()) @AT@ 4215 @LENGTH@ 4\n" +
                "------------INS SimpleName@@MethodName:createTempFile:[\"fabric-agent-\", null, file.getParentFile()] @TO@ MethodInvocation@@File.createTempFile(\"fabric-agent-\",null,file.getParentFile()) @AT@ 4220 @LENGTH@ 59\n" +
                "---------------INS StringLiteral@@\"fabric-agent-\" @TO@ SimpleName@@MethodName:createTempFile:[\"fabric-agent-\", null, file.getParentFile()] @AT@ 4235 @LENGTH@ 15\n" +
                "---------------INS NullLiteral@@null @TO@ SimpleName@@MethodName:createTempFile:[\"fabric-agent-\", null, file.getParentFile()] @AT@ 4252 @LENGTH@ 4\n" +
                "---------------INS MethodInvocation@@file.getParentFile() @TO@ SimpleName@@MethodName:createTempFile:[\"fabric-agent-\", null, file.getParentFile()] @AT@ 4258 @LENGTH@ 20\n" +
                "------------------INS SimpleName@@Name:file @TO@ MethodInvocation@@file.getParentFile() @AT@ 4258 @LENGTH@ 4\n" +
                "------------------INS SimpleName@@MethodName:getParentFile:[] @TO@ MethodInvocation@@file.getParentFile() @AT@ 4263 @LENGTH@ 15\n" +
                "---DEL ExpressionStatement@@MethodInvocation:tmp.renameTo(file) @AT@ 4209 @LENGTH@ 19\n" +
                "---INS ExpressionStatement@@MethodInvocation:is.close() @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 4388 @LENGTH@ 11\n" +
                "------INS MethodInvocation@@is.close() @TO@ ExpressionStatement@@MethodInvocation:is.close() @AT@ 4388 @LENGTH@ 10\n" +
                "---------INS SimpleName@@Name:is @TO@ MethodInvocation@@is.close() @AT@ 4388 @LENGTH@ 2\n" +
                "---------INS SimpleName@@MethodName:close:[] @TO@ MethodInvocation@@is.close() @AT@ 4391 @LENGTH@ 7\n" +
                "---INS ExpressionStatement@@MethodInvocation:os.close() @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 4416 @LENGTH@ 11\n" +
                "------INS MethodInvocation@@os.close() @TO@ ExpressionStatement@@MethodInvocation:os.close() @AT@ 4416 @LENGTH@ 10\n" +
                "---------INS SimpleName@@Name:os @TO@ MethodInvocation@@os.close() @AT@ 4416 @LENGTH@ 2\n" +
                "---------INS SimpleName@@MethodName:close:[] @TO@ MethodInvocation@@os.close() @AT@ 4419 @LENGTH@ 7\n" +
                "---INS IfStatement@@if (file.exists() && !file.delete()) {  throw new IOException(\"Unable to delete file: \" + file.toString());} @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 4444 @LENGTH@ 144\n" +
                "------INS InfixExpression@@file.exists() && !file.delete() @TO@ IfStatement@@if (file.exists() && !file.delete()) {  throw new IOException(\"Unable to delete file: \" + file.toString());} @AT@ 4448 @LENGTH@ 31\n" +
                "---------INS MethodInvocation@@file.exists() @TO@ InfixExpression@@file.exists() && !file.delete() @AT@ 4448 @LENGTH@ 13\n" +
                "------------INS SimpleName@@Name:file @TO@ MethodInvocation@@file.exists() @AT@ 4448 @LENGTH@ 4\n" +
                "------------INS SimpleName@@MethodName:exists:[] @TO@ MethodInvocation@@file.exists() @AT@ 4453 @LENGTH@ 8\n" +
                "---------INS Operator@@&& @TO@ InfixExpression@@file.exists() && !file.delete() @AT@ 4461 @LENGTH@ 2\n" +
                "---------INS PrefixExpression@@!file.delete() @TO@ InfixExpression@@file.exists() && !file.delete() @AT@ 4465 @LENGTH@ 14\n" +
                "------------INS Operator@@! @TO@ PrefixExpression@@!file.delete() @AT@ 4465 @LENGTH@ 1\n" +
                "------------INS MethodInvocation@@file.delete() @TO@ PrefixExpression@@!file.delete() @AT@ 4466 @LENGTH@ 13\n" +
                "---------------INS SimpleName@@Name:file @TO@ MethodInvocation@@file.delete() @AT@ 4466 @LENGTH@ 4\n" +
                "---------------INS SimpleName@@MethodName:delete:[] @TO@ MethodInvocation@@file.delete() @AT@ 4471 @LENGTH@ 8\n" +
                "------INS Block@@ThenBody:{  throw new IOException(\"Unable to delete file: \" + file.toString());} @TO@ IfStatement@@if (file.exists() && !file.delete()) {  throw new IOException(\"Unable to delete file: \" + file.toString());} @AT@ 4481 @LENGTH@ 107\n" +
                "---------INS ThrowStatement@@ClassInstanceCreation:new IOException(\"Unable to delete file: \" + file.toString()) @TO@ Block@@ThenBody:{  throw new IOException(\"Unable to delete file: \" + file.toString());} @AT@ 4503 @LENGTH@ 67\n" +
                "------------INS ClassInstanceCreation@@IOException[\"Unable to delete file: \" + file.toString()] @TO@ ThrowStatement@@ClassInstanceCreation:new IOException(\"Unable to delete file: \" + file.toString()) @AT@ 4509 @LENGTH@ 60\n" +
                "---------------MOV New@@new @TO@ ClassInstanceCreation@@IOException[\"Unable to delete file: \" + file.toString()] @AT@ 3982 @LENGTH@ 3\n" +
                "---------------MOV SimpleType@@File @TO@ ClassInstanceCreation@@IOException[\"Unable to delete file: \" + file.toString()] @AT@ 3986 @LENGTH@ 4\n" +
                "---------------MOV InfixExpression@@file.getAbsolutePath() + \".tmp\" @TO@ ClassInstanceCreation@@IOException[\"Unable to delete file: \" + file.toString()] @AT@ 3991 @LENGTH@ 31\n" +
                "------------------UPD MethodInvocation@@file.getAbsolutePath() @TO@ file.toString() @AT@ 3991 @LENGTH@ 22\n" +
                "---------------------UPD SimpleName@@MethodName:getAbsolutePath:[] @TO@ MethodName:toString:[] @AT@ 3996 @LENGTH@ 17\n" +
                "------------------DEL Operator@@+ @AT@ 4013 @LENGTH@ 1\n" +
                "------------------DEL StringLiteral@@\".tmp\" @AT@ 4016 @LENGTH@ 6\n" +
                "------------------INS StringLiteral@@\"Unable to delete file: \" @TO@ InfixExpression@@file.getAbsolutePath() + \".tmp\" @AT@ 4525 @LENGTH@ 25\n" +
                "------------------INS Operator@@+ @TO@ InfixExpression@@file.getAbsolutePath() + \".tmp\" @AT@ 4550 @LENGTH@ 1\n" +
                "---INS IfStatement@@if (!tmp.renameTo(file)) {  throw new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString());} @TO@ TryStatement@@try {  configuration.enableProxy(artifact.getArtifactURL());  String repository=system.getFile().getAbsolutePath();  if (!repository.endsWith(Parser.FILE_SEPARATOR)) {    repository=repository + Parser.FILE_SEPARATOR;  }  InputStream is=artifact.getInputStream();  File file=new File(repository + parser.getArtifactPath());  File tmp=new File(file.getAbsolutePath() + \".tmp\");  tmp.getParentFile().mkdirs();  OutputStream os=new FileOutputStream(tmp);  copy(is,os);  file.delete();  tmp.renameTo(file);  return file;} catch (IOException ignore) {  LOG.debug(Ix2 + \"Could not download [\" + artifact+ \"]\");  LOG.trace(Ix2 + \"Reason [\" + ignore.getClass().getName()+ \": \"+ ignore.getMessage()+ \"]\");} @AT@ 4605 @LENGTH@ 157\n" +
                "------INS PrefixExpression@@!tmp.renameTo(file) @TO@ IfStatement@@if (!tmp.renameTo(file)) {  throw new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString());} @AT@ 4609 @LENGTH@ 19\n" +
                "---------MOV MethodInvocation@@tmp.renameTo(file) @TO@ PrefixExpression@@!tmp.renameTo(file) @AT@ 4209 @LENGTH@ 18\n" +
                "---------INS Operator@@! @TO@ PrefixExpression@@!tmp.renameTo(file) @AT@ 4609 @LENGTH@ 1\n" +
                "------INS Block@@ThenBody:{  throw new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString());} @TO@ IfStatement@@if (!tmp.renameTo(file)) {  throw new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString());} @AT@ 4630 @LENGTH@ 132\n" +
                "---------INS ThrowStatement@@ClassInstanceCreation:new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString()) @TO@ Block@@ThenBody:{  throw new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString());} @AT@ 4652 @LENGTH@ 92\n" +
                "------------INS ClassInstanceCreation@@IOException[\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString()] @TO@ ThrowStatement@@ClassInstanceCreation:new IOException(\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString()) @AT@ 4658 @LENGTH@ 85\n" +
                "---------------INS New@@new @TO@ ClassInstanceCreation@@IOException[\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString()] @AT@ 4658 @LENGTH@ 3\n" +
                "---------------INS SimpleType@@IOException @TO@ ClassInstanceCreation@@IOException[\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString()] @AT@ 4662 @LENGTH@ 11\n" +
                "---------------INS InfixExpression@@\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString() @TO@ ClassInstanceCreation@@IOException[\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString()] @AT@ 4674 @LENGTH@ 68\n" +
                "------------------INS StringLiteral@@\"Unable to rename file \" @TO@ InfixExpression@@\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString() @AT@ 4674 @LENGTH@ 24\n" +
                "------------------INS Operator@@+ @TO@ InfixExpression@@\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString() @AT@ 4698 @LENGTH@ 1\n" +
                "------------------INS MethodInvocation@@tmp.toString() @TO@ InfixExpression@@\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString() @AT@ 4701 @LENGTH@ 14\n" +
                "---------------------MOV SimpleName@@Name:tmp @TO@ MethodInvocation@@tmp.toString() @AT@ 4041 @LENGTH@ 3\n" +
                "---------------------MOV SimpleName@@MethodName:mkdirs:[] @TO@ MethodInvocation@@tmp.toString() @AT@ 4061 @LENGTH@ 8\n" +
                "------------------INS StringLiteral@@\" to \" @TO@ InfixExpression@@\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString() @AT@ 4718 @LENGTH@ 6\n" +
                "------------------INS MethodInvocation@@file.toString() @TO@ InfixExpression@@\"Unable to rename file \" + tmp.toString() + \" to \"+ file.toString() @AT@ 4727 @LENGTH@ 15\n" +
                "---------------------MOV SimpleName@@Name:file @TO@ MethodInvocation@@file.toString() @AT@ 4178 @LENGTH@ 4\n" +
                "---------------------MOV SimpleName@@MethodName:delete:[] @TO@ MethodInvocation@@file.toString() @AT@ 4183 @LENGTH@ 8\n");
    }


    //commons-collections_2d6bc8_506966_src#java#org#apache#commons#collections#ExtendedProperties.java
    @Test
    public void test_collections_2d6bc8_506966() throws IOException {
        //TODO seems ok but
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_2d6bc8_506966_src#java#org#apache#commons#collections#ExtendedProperties.java");
        Assert.assertEquals(hierarchicalActionSets.size(),3);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"DEL MethodDeclaration@@private, void, MethodName:init, ExtendedProperties exp, IOException,  @AT@ 13436 @LENGTH@ 104\n" +
                "---DEL Modifier@@private @AT@ 13436 @LENGTH@ 7\n" +
                "---DEL PrimitiveType@@void @AT@ 13444 @LENGTH@ 4\n" +
                "---DEL SimpleName@@MethodName:init @AT@ 13449 @LENGTH@ 4\n" +
                "---DEL SingleVariableDeclaration@@ExtendedProperties exp @AT@ 13455 @LENGTH@ 22\n" +
                "------DEL SimpleType@@ExtendedProperties @AT@ 13455 @LENGTH@ 18\n" +
                "------DEL SimpleName@@exp @AT@ 13474 @LENGTH@ 3\n" +
                "---DEL SimpleType@@IOException @AT@ 13487 @LENGTH@ 11\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"MOV ExpressionStatement@@Assignment:isInitialized=true @TO@ MethodDeclaration@@public, void, MethodName:addProperty, String key, Object token,  @AT@ 13513 @LENGTH@ 21\n");
        Assert.assertEquals(hierarchicalActionSets.get(2).toString(),"UPD TryStatement@@try {  while (true) {    String line=reader.readProperty();    int equalSign=line.indexOf('=');    if (equalSign > 0) {      String key=line.substring(0,equalSign).trim();      String value=line.substring(equalSign + 1).trim();      if (\"\".equals(value))       continue;      if (getInclude() != null && key.equalsIgnoreCase(getInclude())) {        File file=null;        if (value.startsWith(fileSeparator)) {          file=new File(value);        } else {          if (value.startsWith(\".\" + fileSeparator)) {            value=value.substring(2);          }          file=new File(basePath + value);        }        if (file != null && file.exists() && file.canRead()) {          load(new FileInputStream(file));        }      } else {        addProperty(key,value);      }    }  }} catch (NullPointerException e) {  return;} @TO@ try {  while (true) {    String line=reader.readProperty();    int equalSign=line.indexOf('=');    if (equalSign > 0) {      String key=line.substring(0,equalSign).trim();      String value=line.substring(equalSign + 1).trim();      if (\"\".equals(value))       continue;      if (getInclude() != null && key.equalsIgnoreCase(getInclude())) {        File file=null;        if (value.startsWith(fileSeparator)) {          file=new File(value);        } else {          if (value.startsWith(\".\" + fileSeparator)) {            value=value.substring(2);          }          file=new File(basePath + value);        }        if (file != null && file.exists() && file.canRead()) {          load(new FileInputStream(file));        }      } else {        addProperty(key,value);      }    }  }} catch (NullPointerException e) {  return;} finally {  isInitialized=true;} @AT@ 15296 @LENGTH@ 2508\n" +
                "---INS Block@@FinallyBody:{  isInitialized=true;} @TO@ TryStatement@@try {  while (true) {    String line=reader.readProperty();    int equalSign=line.indexOf('=');    if (equalSign > 0) {      String key=line.substring(0,equalSign).trim();      String value=line.substring(equalSign + 1).trim();      if (\"\".equals(value))       continue;      if (getInclude() != null && key.equalsIgnoreCase(getInclude())) {        File file=null;        if (value.startsWith(fileSeparator)) {          file=new File(value);        } else {          if (value.startsWith(\".\" + fileSeparator)) {            value=value.substring(2);          }          file=new File(basePath + value);        }        if (file != null && file.exists() && file.canRead()) {          load(new FileInputStream(file));        }      } else {        addProperty(key,value);      }    }  }} catch (NullPointerException e) {  return;} @AT@ 17503 @LENGTH@ 115\n" +
                "------INS ExpressionStatement@@Assignment:isInitialized=true @TO@ Block@@FinallyBody:{  isInitialized=true;} @AT@ 17587 @LENGTH@ 21\n" +
                "---------INS Assignment@@isInitialized=true @TO@ ExpressionStatement@@Assignment:isInitialized=true @AT@ 17587 @LENGTH@ 20\n" +
                "------------INS SimpleName@@isInitialized @TO@ Assignment@@isInitialized=true @AT@ 17587 @LENGTH@ 13\n" +
                "------------INS Operator@@= @TO@ Assignment@@isInitialized=true @AT@ 17600 @LENGTH@ 1\n" +
                "------------INS BooleanLiteral@@true @TO@ Assignment@@isInitialized=true @AT@ 17603 @LENGTH@ 4\n");
    }


    //commons-compress_b7de91_5273bd_src#main#java#org#apache#commons#compress#archivers#sevenz#SevenZFile.java
    @Test
    public void test_compress_b7de91_5273bd() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-compress_b7de91_5273bd_src#main#java#org#apache#commons#compress#archivers#sevenz#SevenZFile.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ForStatement@@for (int i=0; i < files.length; i++) {  files[i].setHasAcessDate(timesDefined.get(i));  if (files[i].getHasAcessDate()) {    files[i].setAccessDate(Long.reverseBytes(header.readLong()));  }} @TO@ for (int i=0; i < files.length; i++) {  files[i].setHasAccessDate(timesDefined.get(i));  if (files[i].getHasAccessDate()) {    files[i].setAccessDate(Long.reverseBytes(header.readLong()));  }} @AT@ 27930 @LENGTH@ 327\n" +
                "---UPD ExpressionStatement@@MethodInvocation:files[i].setHasAcessDate(timesDefined.get(i)) @TO@ MethodInvocation:files[i].setHasAccessDate(timesDefined.get(i)) @AT@ 27999 @LENGTH@ 46\n" +
                "------UPD MethodInvocation@@files[i].setHasAcessDate(timesDefined.get(i)) @TO@ files[i].setHasAccessDate(timesDefined.get(i)) @AT@ 27999 @LENGTH@ 45\n" +
                "---------UPD SimpleName@@MethodName:setHasAcessDate:[timesDefined.get(i)] @TO@ MethodName:setHasAccessDate:[timesDefined.get(i)] @AT@ 28008 @LENGTH@ 36\n" +
                "---UPD IfStatement@@if (files[i].getHasAcessDate()) {  files[i].setAccessDate(Long.reverseBytes(header.readLong()));} @TO@ if (files[i].getHasAccessDate()) {  files[i].setAccessDate(Long.reverseBytes(header.readLong()));} @AT@ 28074 @LENGTH@ 157\n" +
                "------UPD MethodInvocation@@files[i].getHasAcessDate() @TO@ files[i].getHasAccessDate() @AT@ 28078 @LENGTH@ 26\n" +
                "---------UPD SimpleName@@MethodName:getHasAcessDate:[] @TO@ MethodName:getHasAccessDate:[] @AT@ 28087 @LENGTH@ 17\n");
    }

    //metadata_017a3f_580eac_web#src#main#java#org#jboss#metadata#parser#jbossweb#ContainerListenerMetaDataParser.java
    @Test
    public void test_metadata_017a3f_580eac() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("metadata_017a3f_580eac_web#src#main#java#org#jboss#metadata#parser#jbossweb#ContainerListenerMetaDataParser.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD SwitchStatement@@switch (element) {case CLASS_NAME:  containerListener.setListenerClass(getElementText(reader));break;case MODULE:containerListener.setModule(getElementText(reader));break;case LISTENER_TYPE:containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));break;case PARAM:List<ParamValueMetaData> params=containerListener.getParams();if (params == null) {params=new ArrayList<ParamValueMetaData>();containerListener.setParams(params);}params.add(ParamValueMetaDataParser.parse(reader));break;default :throw unexpectedElement(reader);} @TO@ switch (element) {case CLASS_NAME:  containerListener.setListenerClass(getElementText(reader));break;case MODULE:containerListener.setModule(getElementText(reader));break;case LISTENER_TYPE:try {containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));} catch (IllegalArgumentException e) {throw unexpectedValue(reader,e);}break;case PARAM:List<ParamValueMetaData> params=containerListener.getParams();if (params == null) {params=new ArrayList<ParamValueMetaData>();containerListener.setParams(params);}params.add(ParamValueMetaDataParser.parse(reader));break;default :throw unexpectedElement(reader);} @AT@ 1995 @LENGTH@ 924\n" +
                "---INS TryStatement@@try {  containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));} catch (IllegalArgumentException e) {  throw unexpectedValue(reader,e);} @TO@ SwitchStatement@@switch (element) {case CLASS_NAME:  containerListener.setListenerClass(getElementText(reader));break;case MODULE:containerListener.setModule(getElementText(reader));break;case LISTENER_TYPE:containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));break;case PARAM:List<ParamValueMetaData> params=containerListener.getParams();if (params == null) {params=new ArrayList<ParamValueMetaData>();containerListener.setParams(params);}params.add(ParamValueMetaDataParser.parse(reader));break;default :throw unexpectedElement(reader);} @AT@ 2333 @LENGTH@ 258\n" +
                "------MOV ExpressionStatement@@MethodInvocation:containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader))) @TO@ TryStatement@@try {  containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));} catch (IllegalArgumentException e) {  throw unexpectedValue(reader,e);} @AT@ 2330 @LENGTH@ 89\n" +
                "------INS CatchClause@@catch (IllegalArgumentException e) {  throw unexpectedValue(reader,e);} @TO@ TryStatement@@try {  containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));} catch (IllegalArgumentException e) {  throw unexpectedValue(reader,e);} @AT@ 2475 @LENGTH@ 116\n" +
                "---------INS SingleVariableDeclaration@@IllegalArgumentException e @TO@ CatchClause@@catch (IllegalArgumentException e) {  throw unexpectedValue(reader,e);} @AT@ 2482 @LENGTH@ 26\n" +
                "------------INS SimpleType@@IllegalArgumentException @TO@ SingleVariableDeclaration@@IllegalArgumentException e @AT@ 2482 @LENGTH@ 24\n" +
                "------------INS SimpleName@@e @TO@ SingleVariableDeclaration@@IllegalArgumentException e @AT@ 2507 @LENGTH@ 1\n" +
                "---------INS ThrowStatement@@MethodInvocation:unexpectedValue(reader,e) @TO@ CatchClause@@catch (IllegalArgumentException e) {  throw unexpectedValue(reader,e);} @AT@ 2536 @LENGTH@ 33\n" +
                "------------INS MethodInvocation@@unexpectedValue(reader,e) @TO@ ThrowStatement@@MethodInvocation:unexpectedValue(reader,e) @AT@ 2542 @LENGTH@ 26\n" +
                "---------------INS SimpleName@@MethodName:unexpectedValue:[reader, e] @TO@ MethodInvocation@@unexpectedValue(reader,e) @AT@ 2542 @LENGTH@ 26\n" +
                "------------------INS SimpleName@@reader @TO@ SimpleName@@MethodName:unexpectedValue:[reader, e] @AT@ 2558 @LENGTH@ 6\n" +
                "------------------INS SimpleName@@e @TO@ SimpleName@@MethodName:unexpectedValue:[reader, e] @AT@ 2566 @LENGTH@ 1\n");
    }

    //metadata_674226_73eff3_common#src#main#java#org#jboss#metadata#parser#ee#ServiceReferenceMetaDataParser.java
    @Test
    public void test_metadata_674226_73eff3() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("metadata_674226_73eff3_common#src#main#java#org#jboss#metadata#parser#ee#ServiceReferenceMetaDataParser.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ExpressionStatement@@MethodInvocation:serviceReference.setServiceQname(QName.valueOf(getElementText(reader))) @TO@ MethodInvocation:serviceReference.setServiceQname(parseQName(reader,getElementText(reader))) @AT@ 4613 @LENGTH@ 72\n" +
                "---UPD MethodInvocation@@serviceReference.setServiceQname(QName.valueOf(getElementText(reader))) @TO@ serviceReference.setServiceQname(parseQName(reader,getElementText(reader))) @AT@ 4613 @LENGTH@ 71\n" +
                "------UPD SimpleName@@MethodName:setServiceQname:[QName.valueOf(getElementText(reader))] @TO@ MethodName:setServiceQname:[parseQName(reader,getElementText(reader))] @AT@ 4630 @LENGTH@ 54\n" +
                "---------UPD MethodInvocation@@QName.valueOf(getElementText(reader)) @TO@ parseQName(reader,getElementText(reader)) @AT@ 4646 @LENGTH@ 37\n" +
                "------------DEL SimpleName@@Name:QName @AT@ 4646 @LENGTH@ 5\n" +
                "------------UPD SimpleName@@MethodName:valueOf:[getElementText(reader)] @TO@ MethodName:parseQName:[reader, getElementText(reader)] @AT@ 4652 @LENGTH@ 31\n" +
                "---------------INS SimpleName@@reader @TO@ SimpleName@@MethodName:valueOf:[getElementText(reader)] @AT@ 4657 @LENGTH@ 6\n");
    }

    //commons-configuration_bad272_1c720e_src#java#org#apache#commons#configuration#DataConfiguration.java
    @Test
    public void test_configuration_bad272_1c720e() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-configuration_bad272_1c720e_src#java#org#apache#commons#configuration#DataConfiguration.java");
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ThrowStatement@@ClassInstanceCreation:new IllegalArgumentException(\"The type of the default value (\" + defaultValue.getClass() + \") is not an array of the specified class (\"+ cls+ \")\") @TO@ ClassInstanceCreation:new IllegalArgumentException(\"The type of the default value (\" + defaultValue.getClass() + \")\"+ \" is not an array of the specified class (\"+ cls+ \")\") @AT@ 13001 @LENGTH@ 155\n" +
                "---UPD ClassInstanceCreation@@IllegalArgumentException[\"The type of the default value (\" + defaultValue.getClass() + \") is not an array of the specified class (\"+ cls+ \")\"] @TO@ IllegalArgumentException[\"The type of the default value (\" + defaultValue.getClass() + \")\"+ \" is not an array of the specified class (\"+ cls+ \")\"] @AT@ 13007 @LENGTH@ 148\n" +
                "------UPD InfixExpression@@\"The type of the default value (\" + defaultValue.getClass() + \") is not an array of the specified class (\"+ cls+ \")\" @TO@ \"The type of the default value (\" + defaultValue.getClass() + \")\"+ \" is not an array of the specified class (\"+ cls+ \")\" @AT@ 13036 @LENGTH@ 118\n" +
                "---------UPD StringLiteral@@\") is not an array of the specified class (\" @TO@ \" is not an array of the specified class (\" @AT@ 13098 @LENGTH@ 44\n" +
                "---------INS StringLiteral@@\")\" @TO@ InfixExpression@@\"The type of the default value (\" + defaultValue.getClass() + \") is not an array of the specified class (\"+ cls+ \")\" @AT@ 13122 @LENGTH@ 3\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD ThrowStatement@@ClassInstanceCreation:new ConversionException('\\'' + key + \"' (\"+ arrayType+ \") doesn't map to a compatible array of \"+ cls) @TO@ ClassInstanceCreation:new ConversionException('\\'' + key + \"' (\"+ arrayType+ \")\"+ \" doesn't map to a compatible array of \"+ cls) @AT@ 15560 @LENGTH@ 112\n" +
                "---UPD ClassInstanceCreation@@ConversionException['\\'' + key + \"' (\"+ arrayType+ \") doesn't map to a compatible array of \"+ cls] @TO@ ConversionException['\\'' + key + \"' (\"+ arrayType+ \")\"+ \" doesn't map to a compatible array of \"+ cls] @AT@ 15566 @LENGTH@ 105\n" +
                "------UPD InfixExpression@@'\\'' + key + \"' (\"+ arrayType+ \") doesn't map to a compatible array of \"+ cls @TO@ '\\'' + key + \"' (\"+ arrayType+ \")\"+ \" doesn't map to a compatible array of \"+ cls @AT@ 15590 @LENGTH@ 80\n" +
                "---------UPD StringLiteral@@\") doesn't map to a compatible array of \" @TO@ \" doesn't map to a compatible array of \" @AT@ 15623 @LENGTH@ 41\n" +
                "---------INS StringLiteral@@\")\" @TO@ InfixExpression@@'\\'' + key + \"' (\"+ arrayType+ \") doesn't map to a compatible array of \"+ cls @AT@ 15672 @LENGTH@ 3\n");
    }

    //commons-compress_4ac67b_0eccda_src#main#java#org#apache#commons#compress#compressors#snappy#SnappyCompressorInputStream.java
    @Test
    public void test_compress_4ac67b_0eccda() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-compress_4ac67b_0eccda_src#main#java#org#apache#commons#compress#compressors#snappy#SnappyCompressorInputStream.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD SwitchStatement@@switch (state) {case NO_BLOCK:  fill();return read(b,off,len);case IN_LITERAL:int litLen=readLiteral(b,off,len);if (!hasMoreDataInBlock()) {state=State.NO_BLOCK;}return litLen;case IN_BACK_REFERENCE:int backReferenceLen=readBackReference(b,off,len);if (!hasMoreDataInBlock()) {state=State.NO_BLOCK;}return backReferenceLen;default :throw new IOException(\"Unknown stream state \" + state);} @TO@ switch (state) {case NO_BLOCK:  fill();return read(b,off,len);case IN_LITERAL:int litLen=readLiteral(b,off,len);if (!hasMoreDataInBlock()) {state=State.NO_BLOCK;}return litLen > 0 ? litLen : read(b,off,len);case IN_BACK_REFERENCE:int backReferenceLen=readBackReference(b,off,len);if (!hasMoreDataInBlock()) {state=State.NO_BLOCK;}return backReferenceLen > 0 ? backReferenceLen : read(b,off,len);default :throw new IOException(\"Unknown stream state \" + state);} @AT@ 3346 @LENGTH@ 621\n" +
                "---UPD ReturnStatement@@SimpleName:litLen @TO@ ConditionalExpression:litLen > 0 ? litLen : read(b,off,len) @AT@ 3627 @LENGTH@ 14\n" +
                "------INS ConditionalExpression@@litLen > 0 ? litLen : read(b,off,len) @TO@ ReturnStatement@@SimpleName:litLen @AT@ 3634 @LENGTH@ 39\n" +
                "---------INS InfixExpression@@litLen > 0 @TO@ ConditionalExpression@@litLen > 0 ? litLen : read(b,off,len) @AT@ 3634 @LENGTH@ 10\n" +
                "------------INS SimpleName@@litLen @TO@ InfixExpression@@litLen > 0 @AT@ 3634 @LENGTH@ 6\n" +
                "------------INS Operator@@> @TO@ InfixExpression@@litLen > 0 @AT@ 3640 @LENGTH@ 1\n" +
                "------------INS NumberLiteral@@0 @TO@ InfixExpression@@litLen > 0 @AT@ 3643 @LENGTH@ 1\n" +
                "---------INS SimpleName@@litLen @TO@ ConditionalExpression@@litLen > 0 ? litLen : read(b,off,len) @AT@ 3647 @LENGTH@ 6\n" +
                "---------INS MethodInvocation@@read(b,off,len) @TO@ ConditionalExpression@@litLen > 0 ? litLen : read(b,off,len) @AT@ 3656 @LENGTH@ 17\n" +
                "------------INS SimpleName@@MethodName:read:[b, off, len] @TO@ MethodInvocation@@read(b,off,len) @AT@ 3656 @LENGTH@ 17\n" +
                "---------------INS SimpleName@@b @TO@ SimpleName@@MethodName:read:[b, off, len] @AT@ 3661 @LENGTH@ 1\n" +
                "---------------INS SimpleName@@off @TO@ SimpleName@@MethodName:read:[b, off, len] @AT@ 3664 @LENGTH@ 3\n" +
                "---------------INS SimpleName@@len @TO@ SimpleName@@MethodName:read:[b, off, len] @AT@ 3669 @LENGTH@ 3\n" +
                "------DEL SimpleName@@litLen @AT@ 3634 @LENGTH@ 6\n" +
                "---UPD ReturnStatement@@SimpleName:backReferenceLen @TO@ ConditionalExpression:backReferenceLen > 0 ? backReferenceLen : read(b,off,len) @AT@ 3848 @LENGTH@ 24\n" +
                "------DEL SimpleName@@backReferenceLen @AT@ 3855 @LENGTH@ 16\n" +
                "------INS ConditionalExpression@@backReferenceLen > 0 ? backReferenceLen : read(b,off,len) @TO@ ReturnStatement@@SimpleName:backReferenceLen @AT@ 3888 @LENGTH@ 59\n" +
                "---------INS InfixExpression@@backReferenceLen > 0 @TO@ ConditionalExpression@@backReferenceLen > 0 ? backReferenceLen : read(b,off,len) @AT@ 3888 @LENGTH@ 20\n" +
                "------------INS SimpleName@@backReferenceLen @TO@ InfixExpression@@backReferenceLen > 0 @AT@ 3888 @LENGTH@ 16\n" +
                "------------INS Operator@@> @TO@ InfixExpression@@backReferenceLen > 0 @AT@ 3904 @LENGTH@ 1\n" +
                "------------INS NumberLiteral@@0 @TO@ InfixExpression@@backReferenceLen > 0 @AT@ 3907 @LENGTH@ 1\n" +
                "---------INS SimpleName@@backReferenceLen @TO@ ConditionalExpression@@backReferenceLen > 0 ? backReferenceLen : read(b,off,len) @AT@ 3911 @LENGTH@ 16\n" +
                "---------INS MethodInvocation@@read(b,off,len) @TO@ ConditionalExpression@@backReferenceLen > 0 ? backReferenceLen : read(b,off,len) @AT@ 3930 @LENGTH@ 17\n" +
                "------------INS SimpleName@@MethodName:read:[b, off, len] @TO@ MethodInvocation@@read(b,off,len) @AT@ 3930 @LENGTH@ 17\n" +
                "---------------INS SimpleName@@b @TO@ SimpleName@@MethodName:read:[b, off, len] @AT@ 3935 @LENGTH@ 1\n" +
                "---------------INS SimpleName@@off @TO@ SimpleName@@MethodName:read:[b, off, len] @AT@ 3938 @LENGTH@ 3\n" +
                "---------------INS SimpleName@@len @TO@ SimpleName@@MethodName:read:[b, off, len] @AT@ 3943 @LENGTH@ 3\n");
    }


    //commons-collections_b6d038_4bdb89_src#java#org#apache#commons#collections#iterators#ArrayListIterator.java
    @Test
    public void test_collections_b6d038_4bdb89() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_b6d038_4bdb89_src#java#org#apache#commons#collections#iterators#ArrayListIterator.java");
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD ReturnStatement@@FieldAccess:this.index @TO@ InfixExpression:this.index - this.startIndex @AT@ 7714 @LENGTH@ 18\n" +
                "---DEL FieldAccess@@this.index @AT@ 7721 @LENGTH@ 10\n" +
                "---INS InfixExpression@@this.index - this.startIndex @TO@ ReturnStatement@@FieldAccess:this.index @AT@ 7740 @LENGTH@ 28\n" +
                "------INS FieldAccess@@this.index @TO@ InfixExpression@@this.index - this.startIndex @AT@ 7740 @LENGTH@ 10\n" +
                "---------MOV ThisExpression@@this @TO@ FieldAccess@@this.index @AT@ 7721 @LENGTH@ 4\n" +
                "---------MOV SimpleName@@index @TO@ FieldAccess@@this.index @AT@ 7726 @LENGTH@ 5\n" +
                "------INS Operator@@- @TO@ InfixExpression@@this.index - this.startIndex @AT@ 7750 @LENGTH@ 1\n" +
                "------INS FieldAccess@@this.startIndex @TO@ InfixExpression@@this.index - this.startIndex @AT@ 7753 @LENGTH@ 15\n" +
                "---------INS ThisExpression@@this @TO@ FieldAccess@@this.startIndex @AT@ 7753 @LENGTH@ 4\n" +
                "---------INS SimpleName@@startIndex @TO@ FieldAccess@@this.startIndex @AT@ 7758 @LENGTH@ 10\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD ReturnStatement@@InfixExpression:this.index - 1 @TO@ InfixExpression:this.index - this.startIndex - 1 @AT@ 7947 @LENGTH@ 22\n" +
                "---UPD InfixExpression@@this.index - 1 @TO@ this.index - this.startIndex - 1 @AT@ 7954 @LENGTH@ 14\n" +
                "------INS FieldAccess@@this.startIndex @TO@ InfixExpression@@this.index - 1 @AT@ 8004 @LENGTH@ 15\n" +
                "---------INS ThisExpression@@this @TO@ FieldAccess@@this.startIndex @AT@ 8004 @LENGTH@ 4\n" +
                "---------INS SimpleName@@startIndex @TO@ FieldAccess@@this.startIndex @AT@ 8009 @LENGTH@ 10\n");
    }

    //commons-collections_3761b5_3639ab_src#java#org#apache#commons#collections#FastHashMap.java
    @Test
    public void test_collections_3761b5_3639ab() throws IOException {
        //TODO can be different
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("commons-collections_3761b5_3639ab_src#java#org#apache#commons#collections#FastHashMap.java");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD IfStatement@@if (fast) {  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Entry e=(Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} else {synchronized (map) {    if (mo.size() != map.size())     return (false);    Iterator i=map.entrySet().iterator();    while (i.hasNext()) {      Entry e=(Entry)i.next();      Object key=e.getKey();      Object value=e.getValue();      if (value == null) {        if (!(mo.get(key) == null && mo.containsKey(key)))         return (false);      } else {        if (!value.equals(mo.get(key)))         return (false);      }    }    return (true);  }} @TO@ if (fast) {  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Map.Entry e=(Map.Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} else {synchronized (map) {    if (mo.size() != map.size())     return (false);    Iterator i=map.entrySet().iterator();    while (i.hasNext()) {      Map.Entry e=(Map.Entry)i.next();      Object key=e.getKey();      Object value=e.getValue();      if (value == null) {        if (!(mo.get(key) == null && mo.containsKey(key)))         return (false);      } else {        if (!value.equals(mo.get(key)))         return (false);      }    }    return (true);  }} @AT@ 8632 @LENGTH@ 1375\n" +
                "---UPD Block@@ThenBody:{  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Entry e=(Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} @TO@ ThenBody:{  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Map.Entry e=(Map.Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} @AT@ 8642 @LENGTH@ 624\n" +
                "------UPD WhileStatement@@while (i.hasNext()) {  Entry e=(Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @TO@ while (i.hasNext()) {  Map.Entry e=(Map.Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @AT@ 8781 @LENGTH@ 448\n" +
                "---------UPD Block@@WhileBody:{  Entry e=(Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @TO@ WhileBody:{  Map.Entry e=(Map.Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @AT@ 8801 @LENGTH@ 428\n" +
                "------------UPD VariableDeclarationStatement@@Entry e=(Entry)i.next(); @TO@ Map.Entry e=(Map.Entry)i.next(); @AT@ 8819 @LENGTH@ 27\n" +
                "---------------UPD SimpleType@@Entry @TO@ Map.Entry @AT@ 8819 @LENGTH@ 5\n" +
                "---------------UPD VariableDeclarationFragment@@e=(Entry)i.next() @TO@ e=(Map.Entry)i.next() @AT@ 8825 @LENGTH@ 20\n" +
                "------------------UPD CastExpression@@(Entry)i.next() @TO@ (Map.Entry)i.next() @AT@ 8829 @LENGTH@ 16\n" +
                "---------------------UPD SimpleType@@Entry @TO@ Map.Entry @AT@ 8830 @LENGTH@ 5\n" +
                "---UPD Block@@ElseBody:{synchronized (map) {    if (mo.size() != map.size())     return (false);    Iterator i=map.entrySet().iterator();    while (i.hasNext()) {      Entry e=(Entry)i.next();      Object key=e.getKey();      Object value=e.getValue();      if (value == null) {        if (!(mo.get(key) == null && mo.containsKey(key)))         return (false);      } else {        if (!value.equals(mo.get(key)))         return (false);      }    }    return (true);  }} @TO@ ElseBody:{synchronized (map) {    if (mo.size() != map.size())     return (false);    Iterator i=map.entrySet().iterator();    while (i.hasNext()) {      Map.Entry e=(Map.Entry)i.next();      Object key=e.getKey();      Object value=e.getValue();      if (value == null) {        if (!(mo.get(key) == null && mo.containsKey(key)))         return (false);      } else {        if (!value.equals(mo.get(key)))         return (false);      }    }    return (true);  }} @AT@ 9272 @LENGTH@ 735\n" +
                "------UPD SynchronizedStatement@@synchronized (map) {  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Entry e=(Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} @TO@ synchronized (map) {  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Map.Entry e=(Map.Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} @AT@ 9286 @LENGTH@ 711\n" +
                "---------UPD Block@@SyncBody:{  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Entry e=(Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} @TO@ SyncBody:{  if (mo.size() != map.size())   return (false);  Iterator i=map.entrySet().iterator();  while (i.hasNext()) {    Map.Entry e=(Map.Entry)i.next();    Object key=e.getKey();    Object value=e.getValue();    if (value == null) {      if (!(mo.get(key) == null && mo.containsKey(key)))       return (false);    } else {      if (!value.equals(mo.get(key)))       return (false);    }  }  return (true);} @AT@ 9305 @LENGTH@ 692\n" +
                "------------UPD WhileStatement@@while (i.hasNext()) {  Entry e=(Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @TO@ while (i.hasNext()) {  Map.Entry e=(Map.Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @AT@ 9460 @LENGTH@ 492\n" +
                "---------------UPD Block@@WhileBody:{  Entry e=(Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @TO@ WhileBody:{  Map.Entry e=(Map.Entry)i.next();  Object key=e.getKey();  Object value=e.getValue();  if (value == null) {    if (!(mo.get(key) == null && mo.containsKey(key)))     return (false);  } else {    if (!value.equals(mo.get(key)))     return (false);  }} @AT@ 9480 @LENGTH@ 472\n" +
                "------------------UPD VariableDeclarationStatement@@Entry e=(Entry)i.next(); @TO@ Map.Entry e=(Map.Entry)i.next(); @AT@ 9502 @LENGTH@ 27\n" +
                "---------------------UPD SimpleType@@Entry @TO@ Map.Entry @AT@ 9502 @LENGTH@ 5\n" +
                "---------------------UPD VariableDeclarationFragment@@e=(Entry)i.next() @TO@ e=(Map.Entry)i.next() @AT@ 9508 @LENGTH@ 20\n" +
                "------------------------UPD CastExpression@@(Entry)i.next() @TO@ (Map.Entry)i.next() @AT@ 9512 @LENGTH@ 16\n" +
                "---------------------------UPD SimpleType@@Entry @TO@ Map.Entry @AT@ 9513 @LENGTH@ 5\n");

    }


    //fuse_74c02c_a4658a_fabric#fabric-core#src#main#java#org#fusesource#fabric#service#DataStoreManager.java
    @Test
    public void test_fuse_74c02c_a4658a() throws IOException {
        //mov position

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_74c02c_a4658a_fabric#fabric-core#src#main#java#org#fusesource#fabric#service#DataStoreManager.java");
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD IfStatement@@if (type.equals(dataStorePlugin.getName())) {  updateServiceRegistration();} @TO@ if (type != null && type.equals(dataStorePlugin.getName())) {  updateServiceRegistration();} @AT@ 5887 @LENGTH@ 104\n" +
                "---INS InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @TO@ IfStatement@@if (type.equals(dataStorePlugin.getName())) {  updateServiceRegistration();} @AT@ 5891 @LENGTH@ 54\n" +
                "------MOV MethodInvocation@@type.equals(dataStorePlugin.getName()) @TO@ InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @AT@ 5891 @LENGTH@ 38\n" +
                "------INS InfixExpression@@type != null @TO@ InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @AT@ 5891 @LENGTH@ 12\n" +
                "---------INS SimpleName@@type @TO@ InfixExpression@@type != null @AT@ 5891 @LENGTH@ 4\n" +
                "---------INS Operator@@!= @TO@ InfixExpression@@type != null @AT@ 5895 @LENGTH@ 2\n" +
                "---------INS NullLiteral@@null @TO@ InfixExpression@@type != null @AT@ 5899 @LENGTH@ 4\n" +
                "------INS Operator@@&& @TO@ InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @AT@ 5903 @LENGTH@ 2\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD IfStatement@@if (type.equals(dataStorePlugin.getName())) {  updateServiceRegistration();} @TO@ if (type != null && type.equals(dataStorePlugin.getName())) {  updateServiceRegistration();} @AT@ 6204 @LENGTH@ 104\n" +
                "---INS InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @TO@ IfStatement@@if (type.equals(dataStorePlugin.getName())) {  updateServiceRegistration();} @AT@ 6224 @LENGTH@ 54\n" +
                "------MOV MethodInvocation@@type.equals(dataStorePlugin.getName()) @TO@ InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @AT@ 6208 @LENGTH@ 38\n" +
                "------INS InfixExpression@@type != null @TO@ InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @AT@ 6224 @LENGTH@ 12\n" +
                "---------INS SimpleName@@type @TO@ InfixExpression@@type != null @AT@ 6224 @LENGTH@ 4\n" +
                "---------INS Operator@@!= @TO@ InfixExpression@@type != null @AT@ 6228 @LENGTH@ 2\n" +
                "---------INS NullLiteral@@null @TO@ InfixExpression@@type != null @AT@ 6232 @LENGTH@ 4\n" +
                "------INS Operator@@&& @TO@ InfixExpression@@type != null && type.equals(dataStorePlugin.getName()) @AT@ 6236 @LENGTH@ 2\n");
    }

    //fuse_cb3362_cfb295_fabric#fabric-features-service#src#main#java#org#fusesource#fabric#features#FabricFeaturesServiceImpl.java
    @Test
    public void test_fuse_cb3362_cfb295() throws IOException {
        //todo maye wrong but also true
        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("fuse_cb3362_cfb295_fabric#fabric-features-service#src#main#java#org#fusesource#fabric#features#FabricFeaturesServiceImpl.java");
        Assert.assertEquals(hierarchicalActionSets.size(),2);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"UPD EnhancedForStatement@@for (Repository repository : repositories) {  for (  Feature feature : repository.getFeatures()) {    if (!allfeatures.contains(feature)) {      allfeatures.add(feature);    }  }} @TO@ for (Repository repository : repositories) {  try {    for (    Feature feature : repository.getFeatures()) {      if (!allfeatures.contains(feature)) {        allfeatures.add(feature);      }    }  } catch (  Exception ex) {    LOGGER.debug(\"Could not load features from %s.\",repository.getURI());  }} @AT@ 8535 @LENGTH@ 273\n" +
                "---INS TryStatement@@try {  for (  Feature feature : repository.getFeatures()) {    if (!allfeatures.contains(feature)) {      allfeatures.add(feature);    }  }} catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repository.getURI());} @TO@ EnhancedForStatement@@for (Repository repository : repositories) {  for (  Feature feature : repository.getFeatures()) {    if (!allfeatures.contains(feature)) {      allfeatures.add(feature);    }  }} @AT@ 8596 @LENGTH@ 390\n" +
                "------MOV EnhancedForStatement@@for (Feature feature : repository.getFeatures()) {  if (!allfeatures.contains(feature)) {    allfeatures.add(feature);  }} @TO@ TryStatement@@try {  for (  Feature feature : repository.getFeatures()) {    if (!allfeatures.contains(feature)) {      allfeatures.add(feature);    }  }} catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repository.getURI());} @AT@ 8596 @LENGTH@ 198\n" +
                "------INS CatchClause@@catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repository.getURI());} @TO@ TryStatement@@try {  for (  Feature feature : repository.getFeatures()) {    if (!allfeatures.contains(feature)) {      allfeatures.add(feature);    }  }} catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repository.getURI());} @AT@ 8855 @LENGTH@ 131\n" +
                "---------INS SingleVariableDeclaration@@Exception ex @TO@ CatchClause@@catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repository.getURI());} @AT@ 8862 @LENGTH@ 12\n" +
                "------------INS SimpleType@@Exception @TO@ SingleVariableDeclaration@@Exception ex @AT@ 8862 @LENGTH@ 9\n" +
                "------------INS SimpleName@@ex @TO@ SingleVariableDeclaration@@Exception ex @AT@ 8872 @LENGTH@ 2\n" +
                "---------INS ExpressionStatement@@MethodInvocation:LOGGER.debug(\"Could not load features from %s.\",repository.getURI()) @TO@ CatchClause@@catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repository.getURI());} @AT@ 8898 @LENGTH@ 70\n" +
                "------------INS MethodInvocation@@LOGGER.debug(\"Could not load features from %s.\",repository.getURI()) @TO@ ExpressionStatement@@MethodInvocation:LOGGER.debug(\"Could not load features from %s.\",repository.getURI()) @AT@ 8898 @LENGTH@ 69\n" +
                "---------------INS SimpleName@@Name:LOGGER @TO@ MethodInvocation@@LOGGER.debug(\"Could not load features from %s.\",repository.getURI()) @AT@ 8898 @LENGTH@ 6\n" +
                "---------------INS SimpleName@@MethodName:debug:[\"Could not load features from %s.\", repository.getURI()] @TO@ MethodInvocation@@LOGGER.debug(\"Could not load features from %s.\",repository.getURI()) @AT@ 8905 @LENGTH@ 62\n" +
                "------------------INS StringLiteral@@\"Could not load features from %s.\" @TO@ SimpleName@@MethodName:debug:[\"Could not load features from %s.\", repository.getURI()] @AT@ 8911 @LENGTH@ 34\n" +
                "------------------INS MethodInvocation@@repository.getURI() @TO@ SimpleName@@MethodName:debug:[\"Could not load features from %s.\", repository.getURI()] @AT@ 8947 @LENGTH@ 19\n" +
                "---------------------INS SimpleName@@Name:repository @TO@ MethodInvocation@@repository.getURI() @AT@ 8947 @LENGTH@ 10\n" +
                "---------------------INS SimpleName@@MethodName:getURI:[] @TO@ MethodInvocation@@repository.getURI() @AT@ 8958 @LENGTH@ 8\n");
        Assert.assertEquals(hierarchicalActionSets.get(1).toString(),"UPD EnhancedForStatement@@for (Repository repo : repositories) {  for (  Feature f : repo.getFeatures()) {    if (features.get(f.getName()) == null) {      Map<String,Feature> versionMap=new TreeMap<String,Feature>();      versionMap.put(f.getVersion(),f);      features.put(f.getName(),versionMap);    } else {      features.get(f.getName()).put(f.getVersion(),f);    }  }} @TO@ for (Repository repo : repositories) {  try {    for (    Feature f : repo.getFeatures()) {      if (features.get(f.getName()) == null) {        Map<String,Feature> versionMap=new TreeMap<String,Feature>();        versionMap.put(f.getVersion(),f);        features.put(f.getName(),versionMap);      } else {        features.get(f.getName()).put(f.getVersion(),f);      }    }  } catch (  Exception ex) {    LOGGER.debug(\"Could not load features from %s.\",repo.getURI());  }} @AT@ 11093 @LENGTH@ 483\n" +
                "---INS TryStatement@@try {  for (  Feature f : repo.getFeatures()) {    if (features.get(f.getName()) == null) {      Map<String,Feature> versionMap=new TreeMap<String,Feature>();      versionMap.put(f.getVersion(),f);      features.put(f.getName(),versionMap);    } else {      features.get(f.getName()).put(f.getVersion(),f);    }  }} catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repo.getURI());} @TO@ EnhancedForStatement@@for (Repository repo : repositories) {  for (  Feature f : repo.getFeatures()) {    if (features.get(f.getName()) == null) {      Map<String,Feature> versionMap=new TreeMap<String,Feature>();      versionMap.put(f.getVersion(),f);      features.put(f.getName(),versionMap);    } else {      features.get(f.getName()).put(f.getVersion(),f);    }  }} @AT@ 11336 @LENGTH@ 608\n" +
                "------MOV EnhancedForStatement@@for (Feature f : repo.getFeatures()) {  if (features.get(f.getName()) == null) {    Map<String,Feature> versionMap=new TreeMap<String,Feature>();    versionMap.put(f.getVersion(),f);    features.put(f.getName(),versionMap);  } else {    features.get(f.getName()).put(f.getVersion(),f);  }} @TO@ TryStatement@@try {  for (  Feature f : repo.getFeatures()) {    if (features.get(f.getName()) == null) {      Map<String,Feature> versionMap=new TreeMap<String,Feature>();      versionMap.put(f.getVersion(),f);      features.put(f.getName(),versionMap);    } else {      features.get(f.getName()).put(f.getVersion(),f);    }  }} catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repo.getURI());} @AT@ 11144 @LENGTH@ 422\n" +
                "------INS CatchClause@@catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repo.getURI());} @TO@ TryStatement@@try {  for (  Feature f : repo.getFeatures()) {    if (features.get(f.getName()) == null) {      Map<String,Feature> versionMap=new TreeMap<String,Feature>();      versionMap.put(f.getVersion(),f);      features.put(f.getName(),versionMap);    } else {      features.get(f.getName()).put(f.getVersion(),f);    }  }} catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repo.getURI());} @AT@ 11827 @LENGTH@ 117\n" +
                "---------INS SingleVariableDeclaration@@Exception ex @TO@ CatchClause@@catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repo.getURI());} @AT@ 11834 @LENGTH@ 12\n" +
                "------------INS SimpleType@@Exception @TO@ SingleVariableDeclaration@@Exception ex @AT@ 11834 @LENGTH@ 9\n" +
                "------------INS SimpleName@@ex @TO@ SingleVariableDeclaration@@Exception ex @AT@ 11844 @LENGTH@ 2\n" +
                "---------INS ExpressionStatement@@MethodInvocation:LOGGER.debug(\"Could not load features from %s.\",repo.getURI()) @TO@ CatchClause@@catch (Exception ex) {  LOGGER.debug(\"Could not load features from %s.\",repo.getURI());} @AT@ 11866 @LENGTH@ 64\n" +
                "------------INS MethodInvocation@@LOGGER.debug(\"Could not load features from %s.\",repo.getURI()) @TO@ ExpressionStatement@@MethodInvocation:LOGGER.debug(\"Could not load features from %s.\",repo.getURI()) @AT@ 11866 @LENGTH@ 63\n" +
                "---------------INS SimpleName@@Name:LOGGER @TO@ MethodInvocation@@LOGGER.debug(\"Could not load features from %s.\",repo.getURI()) @AT@ 11866 @LENGTH@ 6\n" +
                "---------------INS SimpleName@@MethodName:debug:[\"Could not load features from %s.\", repo.getURI()] @TO@ MethodInvocation@@LOGGER.debug(\"Could not load features from %s.\",repo.getURI()) @AT@ 11873 @LENGTH@ 56\n" +
                "------------------INS StringLiteral@@\"Could not load features from %s.\" @TO@ SimpleName@@MethodName:debug:[\"Could not load features from %s.\", repo.getURI()] @AT@ 11879 @LENGTH@ 34\n" +
                "------------------INS MethodInvocation@@repo.getURI() @TO@ SimpleName@@MethodName:debug:[\"Could not load features from %s.\", repo.getURI()] @AT@ 11915 @LENGTH@ 13\n" +
                "---------------------INS SimpleName@@Name:repo @TO@ MethodInvocation@@repo.getURI() @AT@ 11915 @LENGTH@ 4\n" +
                "---------------------INS SimpleName@@MethodName:getURI:[] @TO@ MethodInvocation@@repo.getURI() @AT@ 11920 @LENGTH@ 8\n");
    }

    @Ignore
    @Test
    public void test_() throws IOException {

        List<HierarchicalActionSet> hierarchicalActionSets = getHierarchicalActionSets4java("");
        Assert.assertEquals(hierarchicalActionSets.size(),1);
        Assert.assertEquals(hierarchicalActionSets.get(0).toString(),"");
    }
}
