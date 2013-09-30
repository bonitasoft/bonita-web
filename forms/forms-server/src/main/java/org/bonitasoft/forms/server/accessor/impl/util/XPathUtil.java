/**
 * Copyright (C) 2010 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.accessor.impl.util;

import org.bonitasoft.forms.server.constants.XMLForms;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anthony Birembaut
 * 
 */
public abstract class XPathUtil {

    /**
     * Xpath evaluation accessor
     */
    protected XPath xpathEvaluator = XPathFactory.newInstance().newXPath();

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(XPathUtil.class.getName());

    /**
     * Retrieve the child node of a node using XPath
     * 
     * @param parentNode
     *            the node
     * @param xPath
     *            the XPath expression
     * @return the child {@link Node}
     */
    protected Node getNodeByXpath(final Node parentNode, final String xPath) {
        Node node = null;
        try {
            node = (Node) xpathEvaluator.evaluate(xPath, parentNode, XPathConstants.NODE);
        } catch (final XPathExpressionException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error in Xpath expression", e);
            }
        }
        return node;
    }

    /**
     * Retrieve the child string of a node using XPath
     * 
     * @param parentNode
     *            the node
     * @param xPath
     *            the XPath expression
     * @return the child String
     */
    protected String getStringByXpath(final Node parentNode, final String xPath) {
        String string = null;
        final Node node = getNodeByXpath(parentNode, xPath);
        if (node != null) {
            string = node.getTextContent();
        }
        return string;
    }

    /**
     * Retrieve the child node list of a node using XPath
     * 
     * @param parentNode
     *            the node
     * @param xPath
     *            the XPath expression
     * @return the child {@link NodeList}
     */
    protected NodeList getNodeListByXpath(final Node parentNode, final String xPath) {
        NodeList nodeList = null;
        try {
            nodeList = (NodeList) xpathEvaluator.evaluate(xPath, parentNode, XPathConstants.NODESET);
        } catch (final XPathExpressionException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error in Xpath expression", e);
            }
        }
        return nodeList;
    }

    /**
     * Retrieve the integer content of a node (Needed for non mandatory nodes)
     * 
     * @param node
     *            the node
     * @return the int value of the node if it exists. 0 otherwise
     */
    protected int getIntValue(final Node node) {
        if (node != null) {
            try {
                return Integer.parseInt(node.getTextContent());
            } catch (final IllegalArgumentException nfe) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "the widget property " + node.getNodeName() + " should be numeric", nfe);
                }
            }
        }
        return 0;
    }

    /**
     * Retrieve the boolean content of a node (Needed for non mandatory nodes)
     * 
     * @param node
     *            the node
     * @return the boolean value of the node if it exists. false otherwise
     */
    protected boolean getBooleanValue(final Node node) {
        if (node != null) {
            return Boolean.valueOf(node.getTextContent());
        }
        return false;
    }

    protected String unescapeSingleQuote(final String str) {
        return str.replaceAll(XMLForms.SINGLE_QUOTE_ESCAPE, "'");
    }

    protected String escapeSingleQuote(final String str) {
        return str.replaceAll("'", XMLForms.SINGLE_QUOTE_ESCAPE);
    }
}
