/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.console.common.server.themes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Gai Cuisha
 */
public class ThemeConfigManager {

    /**
     * Instances attribute
     */
    private static Map<Long, ThemeConfigManager> INSTANCES = new HashMap<Long, ThemeConfigManager>();

    /**
     * Default name of the preferences file
     */
    public static final String XML_FILENAME = "theme-config.xml";

    public static final String THEME = "theme";

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String INSTALLEDBY = "installedby";

    public static final String INSTALLEDDATE = "installeddate";

    public static final String APPLY = "apply";

    /**
     * The xml file
     */
    protected static File xmlFile;

    protected static String THEME_HOME;

    protected final static String NAMEEXPRESSTION = "/themes/theme[name='ThemeNamePlace']";

    protected final static String THEME_NAME_PLACEHOLDER = "ThemeNamePlace";

    protected static long tenantId;

    protected static DocumentBuilderFactory factory;

    protected static DocumentBuilder db;

    protected static Document xmldoc;

    protected static Element root;

    public static synchronized ThemeConfigManager getInstance(final long tenantId) {
        ThemeConfigManager themeconfig = INSTANCES.get(tenantId);
        if (themeconfig == null) {
            themeconfig = new ThemeConfigManager(tenantId);
            INSTANCES.put(tenantId, themeconfig);
        }
        return themeconfig;
    }

    private ThemeConfigManager(final long tenantId) {
        try {
            xmlFile = new File(WebBonitaConstantsUtils.getInstance(tenantId).getConfFolder(), XML_FILENAME);

            getFileContent(xmlFile, (int) xmlFile.length(), xmlFile.getPath());

            THEME_HOME = WebBonitaConstantsUtils.getInstance(tenantId).getConsoleThemeFolder().getAbsolutePath();
            factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            db = factory.newDocumentBuilder();
            xmldoc = db.parse(xmlFile);
            root = xmldoc.getDocumentElement();

        } catch (final ServletException e) {
            e.printStackTrace();
        } catch (final SAXException e) {
            e.printStackTrace();
        } catch (final ParserConfigurationException e) {
            e.printStackTrace();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void createTheme(final String idStr, final String nameStr, final String installedByStr, final String installedDateStr, final String applyStr) {

        Element theme, id, name, installedBy, installedDate, apply;

        theme = xmldoc.createElement(THEME);
        id = xmldoc.createElement(ID);
        name = xmldoc.createElement(NAME);
        installedBy = xmldoc.createElement(INSTALLEDBY);
        installedDate = xmldoc.createElement(INSTALLEDDATE);
        apply = xmldoc.createElement(APPLY);

        id.setTextContent(idStr);
        name.setTextContent(nameStr);
        installedBy.setTextContent(installedByStr);
        installedDate.setTextContent(installedDateStr);
        apply.setTextContent(applyStr);

        theme.appendChild(id);
        theme.appendChild(name);
        theme.appendChild(installedBy);
        theme.appendChild(installedDate);
        theme.appendChild(apply);

        root.appendChild(theme);

        saveXml();
    }

    public void setApplyTheme(final String themeName) {
        // set the privious theme doAuthorize to false
        final NodeList applys = root.getElementsByTagName(APPLY);
        for (int i = 0; i < applys.getLength(); i++) {
            Node apply = applys.item(i).getFirstChild();
            if ("true".equals(apply.getNodeValue())) {
                final Element theme = (Element) apply.getParentNode().getParentNode();
                apply = theme.getElementsByTagName(APPLY).item(0).getFirstChild();
                apply.setNodeValue("false");
            }
        }
        // set the doAuthorize theme doAuthorize to true
        Element theme = null;
        final NodeList names = root.getElementsByTagName(NAME);
        for (int j = 0; j < names.getLength(); j++) {
            final Node name = names.item(j).getFirstChild();
            if (themeName.equals(name.getNodeValue())) {
                theme = (Element) name.getParentNode().getParentNode();
                final Node apply = theme.getElementsByTagName(APPLY).item(0).getFirstChild();
                apply.setNodeValue("true");
            }
        }
        saveXml();
    }

    public String getApplyTheme() {
        String themeName = null;
        final NodeList applys = root.getElementsByTagName(APPLY);
        for (int i = 0; i < applys.getLength(); i++) {
            final Node apply = applys.item(i).getFirstChild();
            if ("true".equals(apply.getNodeValue())) {
                final Element theme = (Element) apply.getParentNode().getParentNode();
                themeName = theme.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue();
            }
        }
        return themeName;
    }

    public String getThemeNameById(final long themeId) {
        String nameStr = null;
        final NodeList ids = root.getElementsByTagName(ID);
        for (int i = 0; i < ids.getLength(); i++) {
            final Node id = ids.item(i).getFirstChild();
            if (String.valueOf(themeId).equals(id.getNodeValue())) {
                final Element theme = (Element) id.getParentNode().getParentNode();
                final Node name = theme.getElementsByTagName(NAME).item(0).getFirstChild();
                nameStr = name.getNodeValue();
            }
        }
        return nameStr;

    }

    public List<String> getAllThemeId() {
        final List<String> themeIds = new ArrayList<String>();
        final NodeList ids = root.getElementsByTagName(ID);
        for (int i = 0; i < ids.getLength(); i++) {
            final Node id = ids.item(i).getFirstChild();
            themeIds.add(id.getNodeValue());
        }
        return themeIds;
    }

    public String getThemeInfoByName(final String themeName) {
        Element theme = null;
        final StringBuilder themeInfo = new StringBuilder();
        final NodeList names = root.getElementsByTagName(NAME);
        for (int i = 0; i < names.getLength(); i++) {
            final Node name = names.item(i).getFirstChild();
            if (themeName.equals(name.getNodeValue())) {
                theme = (Element) name.getParentNode().getParentNode();
            }
        }
        themeInfo.append(theme.getElementsByTagName(ID).item(0).getFirstChild().getNodeValue());
        themeInfo.append(":" + theme.getElementsByTagName(NAME).item(0).getFirstChild().getNodeValue());
        themeInfo.append(":" + theme.getElementsByTagName(INSTALLEDBY).item(0).getFirstChild().getNodeValue());
        themeInfo.append(":" + theme.getElementsByTagName(INSTALLEDDATE).item(0).getFirstChild().getNodeValue());
        return themeInfo.toString();
    }

    public void output(final Node node) {
        final TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            final Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("encoding", "utf-8");
            transformer.setOutputProperty("indent", "yes");

            final DOMSource source = new DOMSource();
            source.setNode(node);
            final StreamResult result = new StreamResult();
            result.setOutputStream(System.out);

            transformer.transform(source, result);
        } catch (final TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (final TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a single node.
     * 
     * @param express
     * @param source
     * @return
     */
    public Node selectSingleNode(final String express, final Object source) {
        Node result = null;
        final XPathFactory xpathFactory = XPathFactory.newInstance();
        final XPath xpath = xpathFactory.newXPath();
        try {
            result = (Node) xpath.evaluate(express, source, XPathConstants.NODE);
        } catch (final XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Search nodes
     * 
     * @param express
     * @param source
     * @return
     */
    public NodeList selectNodes(final String express, final Object source) {
        NodeList result = null;
        final XPathFactory xpathFactory = XPathFactory.newInstance();
        final XPath xpath = xpathFactory.newXPath();
        try {
            result = (NodeList) xpath.evaluate(express, source, XPathConstants.NODESET);
        } catch (final XPathExpressionException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Save the document to file
     * 
     * @param fileName
     * @param doc
     */
    public void saveXml() {
        final TransformerFactory transFactory = TransformerFactory.newInstance();
        try {
            final Transformer transformer = transFactory.newTransformer();
            transformer.setOutputProperty("indent", "yes");

            final DOMSource source = new DOMSource();
            source.setNode(xmldoc);
            final StreamResult result = new StreamResult();
            result.setOutputStream(new FileOutputStream(xmlFile));

            transformer.transform(source, result);
        } catch (final TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (final TransformerException e) {
            e.printStackTrace();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete gadget from gadget configuration file.
     * 
     * @param gadgetName
     */
    public void deleteThemeFromConfigFile(final String themeName) {
        Element theme;
        if (themeName != null) {
            theme = (Element) selectSingleNode(NAMEEXPRESSTION.replace(THEME_NAME_PLACEHOLDER, themeName), root);
            theme.getParentNode().removeChild(theme);
            saveXml();
        }
    }

    private static byte[] getFileContent(final File file, final int fileLength, final String filePath) throws ServletException {
        byte[] content = null;
        try {
            final InputStream fileInput = new FileInputStream(file);
            final byte[] fileContent = new byte[fileLength];
            try {
                int offset = 0;
                int length = fileLength;
                while (length > 0) {
                    final int read = fileInput.read(fileContent, offset, length);
                    if (read <= 0) {
                        break;
                    }
                    length -= read;
                    offset += read;
                }
                content = fileContent;
            } catch (final FileNotFoundException e) {
                final String errorMessage = "Error while getting the theme resource. The file " + filePath + " does not exist.";
                throw new ServletException(errorMessage);
            } finally {
                fileInput.close();
            }
        } catch (final IOException e) {
            final String errorMessage = "Error while reading resource: " + filePath;
            throw new ServletException(errorMessage, e);
        }
        return content;
    }

}
