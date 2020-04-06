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
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.actions.model;

import com.github.gumtreediff.tree.ITree;

public class Move extends Addition {

	private ITree newNode;
	
    public Move(ITree node, ITree parent, int pos) {
        super(node, parent, pos);
    }
    
    /**
     * 
     * @param node, old node.
     * @param parent, parent of old node.
     * @param newNode, new node.
     * @param pos, position of the new node in the children array list of its corresponding old parent node.
     */
    public Move(ITree node, ITree parent, ITree newNode, int pos) {
        this(node, parent, pos);
        this.newNode = newNode;
    }
    
    public ITree getNewNode() {
    	return this.newNode;
    }
    
    @Override
    public String getName() {
        return "MOV";
    }

}
