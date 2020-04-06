package com.github.gumtreediff.gen.srcml;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class NodeMap_new {



    public static Map<Integer, String> map;
    public static Map<Integer, String> StatementMap;
    public static Map<Integer, String> DeclarationMap;


        static {
            map = new HashMap<Integer, String>();
            map.put(1	,	"unit");
            map.put(2	,	"comment");
            map.put(3	,	"literal");
            map.put(4	,	"operator");
            map.put(5	,	"modifier");
            map.put(6	,	"name");
            map.put(7	,	"type");
            map.put(8	,	"condition");
            map.put(9	,	"block");
            map.put(10	,	"index");
            map.put(11	,	"decltype");
            map.put(12	,	"typename");
            map.put(13	,	"atomic");
            map.put(14	,	"assert");
            map.put(15	,	"generic_selection");
            map.put(16	,	"selector");
            map.put(17	,	"association_list");
            map.put(18	,	"association");
            map.put(19	,	"expr_stmt");
            map.put(20	,	"expr");
            map.put(21	,	"decl_stmt");
            map.put(22	,	"decl");
            map.put(23	,	"init");
            map.put(24	,	"range");
            map.put(25	,	"break");
            map.put(26	,	"continue");
            map.put(27	,	"goto");
            map.put(28	,	"label");
            map.put(29	,	"typedef");
            map.put(30	,	"asm");
            map.put(31	,	"macro");
            map.put(32	,	"enum");
            map.put(33	,	"enum_decl");
            map.put(34	,	"if");
            map.put(35	,	"ternary");
            map.put(36	,	"then");
            map.put(37	,	"else");
            map.put(38	,	"elseif");
            map.put(39	,	"while");
            map.put(40	,	"typeof");
            map.put(41	,	"do");
            map.put(42	,	"switch");
            map.put(43	,	"case");
            map.put(44	,	"default");
            map.put(45	,	"for");
            map.put(46	,	"foreach");
            map.put(47	,	"control");
            map.put(48	,	"incr");
            map.put(49	,	"function");
            map.put(50	,	"function_decl");
            map.put(51	,	"lambda");
            map.put(52	,	"specifier");
            map.put(53	,	"return");
            map.put(54	,	"call");
            map.put(55	,	"sizeof");
            map.put(56	,	"parameter_list");
            map.put(57	,	"parameter");
            map.put(58	,	"krparameter_list");
            map.put(59	,	"krparameter");
            map.put(60	,	"argument_list");
            map.put(61	,	"argument");
            map.put(62	,	"capture");
            map.put(63	,	"struct");
            map.put(64	,	"struct_decl");
            map.put(65	,	"union");
            map.put(66	,	"union_decl");
            map.put(67	,	"class");
            map.put(68	,	"class_decl");
            map.put(69	,	"public");
            map.put(70	,	"private");
            map.put(71	,	"protected");
            map.put(72	,	"signals");
            map.put(73	,	"forever");
            map.put(74	,	"emit");
            map.put(75	,	"member_init_list");
            map.put(76	,	"constructor");
            map.put(77	,	"constructor_decl");
            map.put(78	,	"destructor");
            map.put(79	,	"destructor_decl");
            map.put(80	,	"super");
            map.put(81	,	"friend");
            map.put(82	,	"extern");
            map.put(83	,	"namespace");
            map.put(84	,	"using");
            map.put(85	,	"try");
            map.put(86	,	"catch");
            map.put(87	,	"finally");
            map.put(88	,	"throw");
            map.put(89	,	"throws");
            map.put(90	,	"noexcept");
            map.put(91	,	"template");
            map.put(92	,	"directive");
            map.put(93	,	"file");
            map.put(94	,	"number");
            map.put(95	,	"include");
            map.put(96	,	"define");
            map.put(97	,	"undef");
            map.put(98	,	"line");
            map.put(99	,	"ifdef");
            map.put(100	,	"ifndef");
            map.put(101	,	"elif");
            map.put(102	,	"endif");
            map.put(103	,	"pragma");
            map.put(104	,	"error");
            map.put(105	,	"warning");
            map.put(106	,	"value");
            map.put(107	,	"empty");
            map.put(108	,	"region");
            map.put(109	,	"endregion");
            map.put(110	,	"import");
            map.put(111	,	"marker");
            map.put(112	,	"parse");
            map.put(113	,	"mode");
            map.put(114	,	"lock");
            map.put(115	,	"fixed");
            map.put(116	,	"checked");
            map.put(117	,	"unchecked");
            map.put(118	,	"unsafe");
            map.put(119	,	"using_stmt");
            map.put(120	,	"delegate");
            map.put(121	,	"event");
            map.put(122	,	"constraint");
            map.put(123	,	"extends");
            map.put(124	,	"implements");
            map.put(125	,	"package");
            map.put(126	,	"synchronized");
            map.put(127	,	"interface");
            map.put(128	,	"interface_decl");
            map.put(129	,	"annotation_defn");
            map.put(130	,	"static");
            map.put(131	,	"attribute");
            map.put(132	,	"target");
            map.put(133	,	"linq");
            map.put(134	,	"from");
            map.put(135	,	"select");
            map.put(136	,	"where");
            map.put(137	,	"let");
            map.put(138	,	"orderby");
            map.put(139	,	"group");
            map.put(140	,	"join");
            map.put(141	,	"in");
            map.put(142	,	"on");
            map.put(143	,	"equals");
            map.put(144	,	"by");
            map.put(145	,	"into");
            map.put(146	,	"escape");
            map.put(147	,	"annotation");
            map.put(148	,	"alignas");
            map.put(149	,	"alignof");
            map.put(150	,	"typeid");
            map.put(151	,	"ref_qualifier");
            map.put(152	,	"receiver");
            map.put(153	,	"message");
            map.put(154	,	"protocol_list");
            map.put(155	,	"category");
            map.put(156	,	"protocol");
            map.put(157	,	"required");
            map.put(158	,	"optional");
            map.put(159	,	"property");
            map.put(160	,	"attribute_list");
            map.put(161	,	"synthesize");
            map.put(162	,	"dynamic");
            map.put(163	,	"encode");
            map.put(164	,	"autoreleasepool");
            map.put(165	,	"compatibility_alias");
            map.put(166	,	"protocol_decl");
            map.put(167	,	"cast");
            map.put(168	,	"position");
            map.put(169	,	"clause");
            map.put(170	,	"empty_stmt");
            map.put(171	,	"cpp:if");
            map.put(172	,	"cpp:else");
            map.put(173	,	"literal:string");
            map.put(174	,	"literal:number");
            map.put(175	,	"literal:char");
            map.put(176	,	"literal:boolean");
            map.put(177	,	"literal:complex");
            map.put(178	,	"literal:null");



        }

    static {
        DeclarationMap = new HashMap<Integer, String>();
        DeclarationMap.put(21  ,       "decl_stmt");
        DeclarationMap.put(22  ,       "decl");
        DeclarationMap.put(50  ,       "function_decl");
        DeclarationMap.put(49  ,       "function");
        DeclarationMap.put(5   ,       "modifier");
        DeclarationMap.put(29  ,       "typedef");
        DeclarationMap.put(23  ,       "init");
        DeclarationMap.put(24  ,       "range");
        DeclarationMap.put(64  ,       "struct_decl");
        DeclarationMap.put(63  ,       "struct");
        DeclarationMap.put(65  ,       "union");
        DeclarationMap.put(66  ,       "union_decl");
        DeclarationMap.put(32  ,       "enum");
        DeclarationMap.put(33  ,       "enum_decl");
    }
    static {
        StatementMap = new HashMap<Integer, String>();
        StatementMap.put(34	,	"if");
//        StatementMap.put(8	,	"condition");
        StatementMap.put(36	,	"then");
        StatementMap.put(37	,	"else");
        StatementMap.put(38	,	"elseif");
        StatementMap.put(39	,	"while");
        StatementMap.put(45	,	"for");
        StatementMap.put(41	,	"do");
//        StatementMap.put(25	,	"break");
//        StatementMap.put(26	,	"continue");
        StatementMap.put(53	,	"return");
        StatementMap.put(42	,	"switch");
//        StatementMap.put(43	,	"case");
//        StatementMap.put(44	,	"default");
        StatementMap.put(9	,	"block");
        StatementMap.put(27	,	"goto");
        StatementMap.put(28	,	"label");

        StatementMap.put(21	,	"decl_stmt");
//        StatementMap.put(22	,	"decl");
        StatementMap.put(50	,	"function_decl");
        StatementMap.put(49	,	"function");
//        StatementMap.put(5	,	"modifier");
        StatementMap.put(29	,	"typedef");
//        StatementMap.put(23	,	"init");
//        StatementMap.put(24	,	"range");
        StatementMap.put(64	,	"struct_decl");
        StatementMap.put(63	,	"struct");
        StatementMap.put(65	,	"union");
        StatementMap.put(66	,	"union_decl");
        StatementMap.put(32	,	"enum");
        StatementMap.put(33	,	"enum_decl");
        StatementMap.put(19	,	"expr_stmt");
        StatementMap.put(82	,	"extern");
//        StatementMap.put(31	,	"macro");
    }
//    static {
//        StatementMap = new HashMap<Integer, String>();
//
//        StatementMap.put(9	,	"block");
//        StatementMap.put(14	,	"assert");
//        StatementMap.put(15	,	"generic_selection");
//        StatementMap.put(16	,	"selector");
//        StatementMap.put(17	,	"association_list");
//        StatementMap.put(18	,	"association");
//        StatementMap.put(19	,	"expr_stmt");
////        StatementMap.put(20	,	"expr");
//        StatementMap.put(21	,	"decl_stmt");
//        StatementMap.put(22	,	"decl");
//        StatementMap.put(23	,	"init");
//        StatementMap.put(24	,	"range");
//        StatementMap.put(25	,	"break");
//        StatementMap.put(26	,	"continue");
//        StatementMap.put(27	,	"goto");
//        StatementMap.put(28	,	"label");
//        StatementMap.put(29	,	"typedef");
//        StatementMap.put(30	,	"asm");
//        StatementMap.put(31	,	"macro");
//        StatementMap.put(32	,	"enum");
//        StatementMap.put(33	,	"enum_decl");
//        StatementMap.put(34	,	"if");
//        StatementMap.put(35	,	"ternary");
//        StatementMap.put(36	,	"then");
//        StatementMap.put(37	,	"else");
//        StatementMap.put(38	,	"elseif");
//        StatementMap.put(39	,	"while");
//        StatementMap.put(40	,	"typeof");
//        StatementMap.put(41	,	"do");
//        StatementMap.put(42	,	"switch");
//        StatementMap.put(43	,	"case");
//        StatementMap.put(44	,	"default");
//        StatementMap.put(45	,	"for");
//        StatementMap.put(46	,	"foreach");
//        StatementMap.put(47	,	"control");
//
//        StatementMap.put(49	,	"function");
//        StatementMap.put(50	,	"function_decl");
//        StatementMap.put(53	,	"return");
//
//        StatementMap.put(63	,	"struct");
//        StatementMap.put(64	,	"struct_decl");
//        StatementMap.put(65	,	"union");
//        StatementMap.put(66	,	"union_decl");
//        StatementMap.put(67	,	"class");
//        StatementMap.put(68	,	"class_decl");
//
//        StatementMap.put(73	,	"forever");
//        StatementMap.put(74	,	"emit");
//        StatementMap.put(88	,	"throw");
//
//        StatementMap.put(95	,	"include");
//        StatementMap.put(96	,	"define");
//
//        StatementMap.put(114	,	"lock");
//        StatementMap.put(115	,	"fixed");
//        StatementMap.put(116	,	"checked");
//        StatementMap.put(117	,	"unchecked");
//        StatementMap.put(118	,	"unsafe");
//        StatementMap.put(119	,	"using_stmt");
//
////        StatementMap.put(14	,"assert");
////        StatementMap.put(16	,"expr_stmt");
////        StatementMap.put(18	,"decl_stmt");
////        StatementMap.put(19	,"decl");
////        StatementMap.put(21	,"break");
////        StatementMap.put(22	,"continue");
////        StatementMap.put(23	,"goto");
////        StatementMap.put(24	,"label");
////        StatementMap.put(25	,"typedef");
//////        StatementMap.put(26	,"asm");
////        StatementMap.put(27	,"enum");
////        StatementMap.put(30	,"while");
////        StatementMap.put(31	,"lock");
////        StatementMap.put(32	,"fixed");
////        StatementMap.put(33	,"checked");
////        StatementMap.put(34	,"unchecked");
////        StatementMap.put(35	,"unsafe");
////        StatementMap.put(36	,"do");
////        StatementMap.put(37	,"switch");
////        StatementMap.put(38	,"case");
////        StatementMap.put(39	,"default");
////        StatementMap.put(40	,"for");
////        StatementMap.put(41	,"foreach");
////        StatementMap.put(45	,"function");
////        StatementMap.put(46	,"function_decl");
////        StatementMap.put(49	,"return");
////        StatementMap.put(59	,"struct");
////        StatementMap.put(60	,"struct_decl");
////        StatementMap.put(61	,"union");
////        StatementMap.put(62	,"union_decl");
////        StatementMap.put(63	,"class");
////        StatementMap.put(64	,"class_decl");
////        StatementMap.put(70	,"try");
////        StatementMap.put(71	,"catch");
////        StatementMap.put(72	,"finally");
////        StatementMap.put(73	,"throw");
////        StatementMap.put(74	,"throws");
////        StatementMap.put(80	,"include");
////        StatementMap.put(81	,"define");
////        StatementMap.put(82	,"undef");
////        StatementMap.put(84	,"if");
////        StatementMap.put(85	,"ifdef");
////        StatementMap.put(86	,"ifndef");
////        StatementMap.put(87	,"else");
////        StatementMap.put(88	,"elif");
////        StatementMap.put(89	,"endif");
////        StatementMap.put(90	,"then");
////        StatementMap.put(91	,"pragma");
////        StatementMap.put(92	,"error");
////        StatementMap.put(93	,"macro");
////        StatementMap.put(96	,"constructor_decl");
//
//
//
//    }

    public static <T, E> List<T> getKeysByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
