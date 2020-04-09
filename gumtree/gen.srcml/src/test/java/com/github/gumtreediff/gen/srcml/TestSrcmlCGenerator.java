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

import com.github.gumtreediff.tree.ITree;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

//TODO
@Ignore
public class TestSrcmlCGenerator {

    @Test
    public void testSimple() throws IOException {
        String input = "/Users/anilkoyuncu/Downloads/parmap-1.0-rc8/bytearray_stubs.c";
        ITree t = new SrcmlCTreeGenerator("/Users/anilkoyuncu/Downloads/srcML2/src2srcml").generateFromFile(input).getRoot();
        Assert.assertEquals(148, t.getSize());
    }

}
