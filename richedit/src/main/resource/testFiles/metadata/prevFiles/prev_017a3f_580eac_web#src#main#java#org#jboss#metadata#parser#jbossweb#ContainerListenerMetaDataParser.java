/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.metadata.parser.jbossweb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.jboss.metadata.javaee.spec.ParamValueMetaData;
import org.jboss.metadata.parser.ee.ParamValueMetaDataParser;
import org.jboss.metadata.parser.util.MetaDataElementParser;
import org.jboss.metadata.web.jboss.ContainerListenerMetaData;
import org.jboss.metadata.web.jboss.ContainerListenerType;

/**
 * @author Remy Maucherat
 */
public class ContainerListenerMetaDataParser extends MetaDataElementParser {

    public static ContainerListenerMetaData parse(XMLStreamReader reader) throws XMLStreamException {
    	ContainerListenerMetaData containerListener = new ContainerListenerMetaData();

        // Handle elements
        while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
            final Element element = Element.forName(reader.getLocalName());
            switch (element) {
                case CLASS_NAME:
                	containerListener.setListenerClass(getElementText(reader));
                    break;
                case MODULE:
                	containerListener.setModule(getElementText(reader));
                    break;
                case LISTENER_TYPE:
                	containerListener.setListenerType(ContainerListenerType.valueOf(getElementText(reader)));
                    break;
                case PARAM:
                    List<ParamValueMetaData> params = containerListener.getParams();
                    if (params == null) {
                    	params = new ArrayList<ParamValueMetaData>();
                    	containerListener.setParams(params);
                    }
                    params.add(ParamValueMetaDataParser.parse(reader));
                    break;
                default: throw unexpectedElement(reader);
            }
        }

        return containerListener;
    }

}
