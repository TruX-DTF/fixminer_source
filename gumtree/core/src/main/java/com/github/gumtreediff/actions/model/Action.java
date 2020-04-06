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

import java.io.Serializable;

public abstract class Action implements Comparable<Action>,Serializable {

    protected ITree node;
    protected Integer position;
    protected int length = 0;

    public Action(ITree node, int pos, int length) {
    	this.node = node;
        this.length = length;
    	this.position = pos;
    }

    public ITree getNode() {
        return node;
    }

    public void setNode(ITree node) {
        this.node = node;
    }

    public int getPosition() {
        return position;
    }
    
    public int getLength() {
        return length;
    }

    public abstract String getName();

    @Override
    public abstract String toString();

	@Override
	public int compareTo(Action o) {
		int result = this.position.compareTo(o.position);
		if (result == 0) {
			result = this.length >= o.length ? -1 : 1;
		}
		return result;
	}


}
