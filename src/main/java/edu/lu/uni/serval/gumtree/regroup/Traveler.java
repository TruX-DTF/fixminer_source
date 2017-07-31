package edu.lu.uni.serval.gumtree.regroup;

import java.util.ArrayList;
import java.util.List;

import com.github.gumtreediff.tree.ITree;

import edu.lu.uni.serval.FixPattern.utils.ASTNodeMap;

/**
 * A traveler to travel a tree-constructed object.
 * 
 * @author kui.liu
 *
 */
public class Traveler {

	public List<List<String>> list = new ArrayList<>();
	
	/**
	 * Get all action string by traveling HierarchicalActionSet in a deep-first way.
	 *  
	 * @param actionSet
	 * @param astNodeTypeActionQueue
	 */
	public void travelActionSetDeepFirstToASTNodeQueue(HierarchicalActionSet actionSet, List<String> astNodeTypeActionQueue) {
		if (actionSet == null) {
			System.err.println("Null Action set!");
		} else {
			if (astNodeTypeActionQueue == null) {
				astNodeTypeActionQueue = new ArrayList<>();
			}
			
			String actionStr = actionSet.getActionString();
			actionStr = actionStr.substring(0, actionStr.indexOf("@@"));
			astNodeTypeActionQueue.add(actionStr); // RawToken: TODO
			
			if (actionStr.startsWith("DEL")) {
				list.add(astNodeTypeActionQueue); // FIXME BUG: Change AST node type 1 to AST node type 2. Solve method: a list is one pattern.
			} else {
				List<HierarchicalActionSet> subActionSet = actionSet.getSubActions();
				int size = subActionSet.size();
				if (size > 0) {
					for (HierarchicalActionSet subAction : subActionSet) {
						List<String> astNodeTypeActionQueue_ = new ArrayList<>();
						astNodeTypeActionQueue_.addAll(astNodeTypeActionQueue);
						travelActionSetDeepFirstToASTNodeQueue(subAction, astNodeTypeActionQueue_);
					}
				} else {
					list.add(astNodeTypeActionQueue);
				}
			}
		}
	}
	
	/**
	 * Get all AST node types of a root tree by traveling the root tree in a deep-first way.
	 * 
	 * @param root
	 * @return
	 */
	public static List<String> travelTreeDeepFirstToASTNodeQueue(ITree root) {
		if (root == null) {
			System.err.println("Null tree!");
			return null;
		}
		
		List<String> astNodeTypeQueue = new ArrayList<>();
		astNodeTypeQueue.add(ASTNodeMap.map.get(root.getType())); // RawToken: root.getLabel();
		
		List<ITree> childrenTreeList = root.getChildren();
		
		if (childrenTreeList != null && childrenTreeList.size() > 0) {
			for (ITree childTree : childrenTreeList) {
				astNodeTypeQueue.addAll(travelTreeDeepFirstToASTNodeQueue(childTree));
			}
		}
		return astNodeTypeQueue;
	}
	
	/**
	 *  Get all AST node types of a root tree by traveling the root tree in a breadth-first way.
	 *  
	 * @param root
	 * @return
	 */
	public static List<String> travelTreeBreadthFirstToASTNodeQueue(ITree root) {
		if (root == null) {
			System.err.println("Null tree.");
			return null;
		}
		
		List<String> astNodeTypeQueue = new ArrayList<>();
		astNodeTypeQueue.add(ASTNodeMap.map.get(root.getType())); // RawToken: root.getLabel();
		
		List<ITree> treeList = new ArrayList<>();
		treeList.add(root);
		while (!treeList.isEmpty()) {
			List<ITree> childrenTreeList = new ArrayList<>();
			for (ITree tree : treeList) {
				astNodeTypeQueue.addAll(travelTreeBreadthFirstToASTNodeQueue(tree));
				childrenTreeList.addAll(tree.getChildren());
			}
			
			treeList.clear();
			treeList.addAll(childrenTreeList);
		}
		return astNodeTypeQueue;
	}
	
	/**
	 * Convert a root ITree into a SimpleTree by traveling the root tree in a deep-first way.
	 * 
	 * SimpleTree node label is root.toShortString().
	 * 
	 * @param root
	 * @param parent
	 * @return
	 */
	public static SimpleTree travelITreeDeepFirstToSimpleTree(ITree root, SimpleTree parent) {
		if (root == null) {
			System.err.println("Null tree!");
			return null;
		}
		SimpleTree simpleTree = new SimpleTree();
		simpleTree.setLabel(root.toShortString());
		simpleTree.setParent(parent);
		List<SimpleTree> children = new ArrayList<>();
		
		List<ITree> childrenTreeList = root.getChildren();
		if (childrenTreeList != null && childrenTreeList.size() > 0) {
			for (ITree childTree : childrenTreeList) {
				children.add(travelITreeDeepFirstToSimpleTree(childTree, simpleTree));
			}
		}
		simpleTree.setChildren(children);
		return simpleTree;
	}
	
}
