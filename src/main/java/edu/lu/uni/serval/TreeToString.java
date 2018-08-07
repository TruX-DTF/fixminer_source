package edu.lu.uni.serval;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;

public class TreeToString {
	
	private List<String> strList = new ArrayList<>();

	public String toString(ITree tree) {
		List<ITree> children = tree.getChildren();
		String str = ASTNodeMap.map.get(tree.getType());
		if (strList.size() == 0) {
			strList.add(str);
			for (ITree child : children) {
				TreeToString t2s = new TreeToString();
				t2s.toString(child);
				List<String> strList1 = t2s.strList;
				for (String str1 : strList1) {
					strList.add("---" + str1);
				}
			}
		} else {
			strList.clear();
			strList.add(str);
			for (ITree child : children) {
				TreeToString t2s = new TreeToString();
				t2s.toString(child);
				List<String> strList1 = t2s.strList;
				for (String str1 : strList1) {
					strList.add("---" + str1);
				}
			}
		}
		
		str = "";
		for (String str1 : strList) {
			str += str1 + "\n";
		}
		 
		return str;
	}
}
