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

package com.github.gumtreediff.gen;

import com.github.gumtreediff.tree.TreeContext;

import java.io.*;

public abstract class TreeGenerator {

    protected abstract TreeContext generate(Reader r) throws IOException;
    
    protected abstract TreeContext generate(Reader r, int astParserType) throws IOException;

    public TreeContext generateFromReader(Reader r) throws IOException {
        TreeContext ctx = generate(r);
        ctx.validate();
        return ctx;
    }

    public TreeContext generateFromFile(String path) throws IOException {
        return generateFromReader(new FileReader(path));
    }

    public TreeContext generateFromFile(File file) throws IOException {
        return generateFromReader(new FileReader(file));
    }

    public TreeContext generateFromStream(InputStream stream) throws IOException {
        return generateFromReader(new InputStreamReader(stream));
    }

    public TreeContext generateFromString(String content) throws IOException {
        return generateFromReader(new StringReader(content));
    }
    
    public TreeContext generateFromReader(Reader r, int astParserType) throws IOException {
        TreeContext ctx = generate(r, astParserType);
        ctx.validate();
        return ctx;
    }

    public TreeContext generateFromFile(String path, int astParserType) throws IOException {
        return generateFromReader(new FileReader(path), astParserType);
    }

    public TreeContext generateFromFile(File file, int astParserType) throws IOException {
        return generateFromReader(new FileReader(file), astParserType);
    }

    public TreeContext generateFromStream(InputStream stream, int astParserType) throws IOException {
        return generateFromReader(new InputStreamReader(stream), astParserType);
    }

    public TreeContext generateFromString(String content, int astParserType) throws IOException {
        return generateFromReader(new StringReader(content), astParserType);
    }
}
