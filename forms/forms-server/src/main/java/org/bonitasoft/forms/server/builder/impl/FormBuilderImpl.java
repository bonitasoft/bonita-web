/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.server.builder.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.forms.client.model.ActionType;
import org.bonitasoft.forms.client.model.FileWidgetInputType;
import org.bonitasoft.forms.client.model.FormType;
import org.bonitasoft.forms.client.model.ReducedFormSubtitle.SubTitlePosition;
import org.bonitasoft.forms.client.model.ReducedFormValidator.ValidatorPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget.SelectMode;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.server.builder.IFormBuilder;
import org.bonitasoft.forms.server.constants.XMLForms;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Implementation of the {@link IFormBuilder} interface generating an XML form definition file
 * 
 * @author Anthony Birembaut, Zhiheng Yang
 */
public class FormBuilderImpl implements IFormBuilder {

    /**
     * the product version constant
     */
    public static final String PRODUCT_VERSION = "6.3";

    /**
     * the product version
     */
    protected String productVersion;

    /**
     * DOM representation of the XML file to create
     */
    protected Document document;

    /**
     * the root element
     */
    private Element rootElement;

    /**
     * The current element
     */
    protected Element currentElement;

    /**
     * Logger
     */
    protected static Logger LOGGER = Logger.getLogger(FormBuilderImpl.class.getName());

    /**
     * XML tansformer factory
     */
    protected TransformerFactory transformerFactory = TransformerFactory.newInstance();

    /**
     * document buildeer factory
     */
    protected DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    /**
     * Instance attribute
     */
    private static FormBuilderImpl INSTANCE = null;

    private final Validator validator;

    /**
     * @return the FormExpressionsAPI instance
     */
    public static synchronized FormBuilderImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FormBuilderImpl();
        }
        return INSTANCE;
    }

    /**
     * Private constructor to prevent instantiation
     * 
     * @throws SAXException
     */
    protected FormBuilderImpl() {
        productVersion = PRODUCT_VERSION;

        documentBuilderFactory.setValidating(true);

        // ignore white space can only be set if parser is validating
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        // select xml schema as the schema language (a.o.t. DTD)
        documentBuilderFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        final URL xsdURL = Thread.currentThread().getContextClassLoader().getResource("forms.xsd");
        documentBuilderFactory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsdURL.toExternalForm());

        try {
            transformerFactory.setAttribute("indent-number", Integer.valueOf(2));
        } catch (final Exception e) {
            // Nothing to do: indent-number is not supported
        }

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema schema;
        InputStream schemaStream = null;
        try {
            schemaStream = this.getClass().getResourceAsStream("/forms.xsd");
            schema = factory.newSchema(new StreamSource(schemaStream));
        } catch (SAXException e) {
            throw new RuntimeException("unable to initialize the xsd validator form the forms", e);
        } finally {
            if (schemaStream != null) {
                try {
                    schemaStream.close();
                } catch (IOException e) {
                    LOGGER.log(Level.INFO, "Unable to close schema input stream", e);
                }
            }
        }
        validator = schema.newValidator();
    }

    /**
     * Build a XML form definition file.
     * This is the last method to call once the form has been built.
     * It perform the XSD validation and generates the XML file
     * 
     * @return a {@link File}
     * @throws InvalidFormDefinitionException
     *             if the generated document is not valid
     * @throws IOException
     */
    @Override
    public File done() throws IOException, InvalidFormDefinitionException {

        final File formsDefinitionFile = File.createTempFile("forms", ".xml", getTempFolder());
        formsDefinitionFile.deleteOnExit();

        document.appendChild(rootElement);

        final Source source = new DOMSource(document);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Result resultat = new StreamResult(new OutputStreamWriter(outputStream, "UTF-8"));
        try {
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, resultat);

            final byte[] xmlContent = outputStream.toByteArray();

            //commented because studio might generate forms that are not complient with xsd but still working
//            validateAgainstFormsXSD(xmlContent);

            outputStream.close();

            final FileOutputStream fileOutputStream = new FileOutputStream(formsDefinitionFile);
            try {
                fileOutputStream.write(xmlContent);
                fileOutputStream.flush();
            } finally {
                fileOutputStream.close();
            }
        } catch (final TransformerException e) {
            throw new InvalidFormDefinitionException("Error while generating the forms definition file.", e);
        }
        return formsDefinitionFile;
    }

    protected File getTempFolder() {
        return WebBonitaConstantsUtils.getInstance().getTempFolder();
    }

    void validateAgainstFormsXSD(final byte[] xmlContent) throws InvalidFormDefinitionException {

        String content = new String(xmlContent, Charset.forName("UTF-8"));
        try {
            StreamSource source = new StreamSource(new StringReader(content));
            validator.validate(source);
        } catch (SAXException e) {
            throw new InvalidFormDefinitionException("Unable to parse the forms.xml using the forms.xsd", e);
        } catch (IOException e) {
            throw new InvalidFormDefinitionException("Unable to read the forms.xml", e);
        }
    }

    /**
     * Initiate the form definition
     * 
     * @return an implementation of {@link IFormBuilder}
     */
    @Override
    public IFormBuilder createFormDefinition() {

        DocumentBuilder builder;
        try {
            builder = documentBuilderFactory.newDocumentBuilder();

            document = builder.newDocument();
            document.setXmlVersion("1.0");

            rootElement = document.createElement(XMLForms.FORMS_DEFINITION);
            rootElement.setAttribute(XMLForms.PRODUCT_VERSION, productVersion);
            final Element migrationVersionElement = document.createElement(XMLForms.MIGRATION_PRODUCT_VERSION);
            migrationVersionElement.setTextContent(productVersion);
            rootElement.appendChild(migrationVersionElement);
            currentElement = rootElement;
        } catch (final ParserConfigurationException e) {
            LOGGER.log(Level.WARNING, "Invalid parser configuration", e);
        }
        return this;
    }

    @Override
    @Deprecated
    public IFormBuilder addMigrationProductVersion(final String migrationProductVersion) throws InvalidFormDefinitionException {
        final String[] migrationProductVersionParentsNames = { XMLForms.FORMS_DEFINITION };
        try {
            peek(migrationProductVersionParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of migration product version property is only supported on elements of type "
                    + Arrays.asList(migrationProductVersionParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("migration product version", migrationProductVersion);
        final Element migrationProductVersionElement = document.createElement(XMLForms.MIGRATION_PRODUCT_VERSION);
        migrationProductVersionElement.setTextContent(migrationProductVersion);
        push(migrationProductVersionElement);
        return this;
    }

    /**
     * Add an action on an application and create the list of actions if it doesn't exist yet
     * 
     * @param actionType
     *            the action type
     * @param variableName
     *            the name of the variable (if it's a set variable action)
     * @param operator
     *            the operator for the action
     * @param operatorInputType
     *            the operator in put type (for java methods operations)
     * @param submitButtonId
     *            the submit button associated with the action
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addAction(final ActionType actionType, final String variableName, final String variableType,
            final String operator,
            final String operatorInputType, final String submitButtonId) throws InvalidFormDefinitionException {
        final String[] actionsParentsNames = { XMLForms.PAGE };
        try {
            peek(actionsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an action is only supported on elements of type " + Arrays.asList(actionsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("action type", actionType);
        Element actionsElement = findChildElement(currentElement, XMLForms.ACTIONS);
        if (actionsElement == null) {
            actionsElement = document.createElement(XMLForms.ACTIONS);
        }
        push(actionsElement);
        final Element actionElement = document.createElement(XMLForms.ACTION);
        actionElement.setAttribute(XMLForms.TYPE, actionType.name());
        addChild(actionElement, XMLForms.VARIABLE, variableName, false, true);
        addChild(actionElement, XMLForms.VARIABLE_TYPE, variableType, false, true);
        addChild(actionElement, XMLForms.OPERATOR, operator, false, true);
        addChild(actionElement, XMLForms.INPUT_TYPE, operatorInputType, false, true);
        addChild(actionElement, XMLForms.SUBMIT_BUTTON, submitButtonId, false, true);
        push(actionElement);
        return this;
    }

    /**
     * Add a label on an application, page or widget
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addActionExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] labelParentsNames = { XMLForms.ACTION };
        try {
            peek(labelParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an action expression is only supported on elements of type " + Arrays.asList(labelParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add available values array to a widget and create the array of available values if it doesn't exist yet
     * (for table widget).
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addAvailableValuesArray() throws InvalidFormDefinitionException {

        final String[] availableValuesParentsNames = { XMLForms.WIDGET };
        try {
            peek(availableValuesParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an array of available values is only supported on elements of type "
                    + Arrays.asList(availableValuesParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        Element availableValuesElement = findChildElement(currentElement, XMLForms.AVAILABLE_VALUES);
        if (availableValuesElement == null) {
            availableValuesElement = document.createElement(XMLForms.AVAILABLE_VALUES);
        }
        push(availableValuesElement);
        final Element availableValuesArrayElement = document.createElement(XMLForms.VALUES_ARRAY);
        push(availableValuesArrayElement);
        return this;
    }

    /**
     * Add initial values array to a widget and create the array if it doesn't exist yet
     * (for grid widget).
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addInitialValuesArray() throws InvalidFormDefinitionException {

        final String[] availableValuesParentsNames = { XMLForms.WIDGET };
        try {
            peek(availableValuesParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an array of initial values is only supported on elements of type "
                    + Arrays.asList(availableValuesParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        Element initialValueElement = findChildElement(currentElement, XMLForms.INITIAL_VALUE);
        if (initialValueElement == null) {
            initialValueElement = document.createElement(XMLForms.INITIAL_VALUE);
        }
        push(initialValueElement);
        final Element initilaValueArrayElement = document.createElement(XMLForms.EXPRESSION_ARRAY);
        push(initilaValueArrayElement);
        return this;
    }

    /**
     * Add a row to an available values array or to an initial value array
     * (for table widget and grid widget).
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addRow() throws InvalidFormDefinitionException {

        final String[] availableValuesParentsNames = { XMLForms.VALUES_ARRAY, XMLForms.EXPRESSION_ARRAY };
        try {
            peek(availableValuesParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a row available values is only supported on elements of type "
                    + Arrays.asList(availableValuesParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element rowElement = document.createElement(XMLForms.ROW);
        push(rowElement);
        return this;
    }

    /**
     * Add an available value to a widget and create the list of available values if it doesn't exist yet
     * (for radiobutton group, simple and multiple selectbox, checkbox group, table row).
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addAvailableValue() throws InvalidFormDefinitionException {

        final String[] availableValuesParentsNames = { XMLForms.WIDGET, XMLForms.ROW };
        try {
            peek(availableValuesParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an available value is only supported on elements of type "
                    + Arrays.asList(availableValuesParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        if (!currentElement.getNodeName().equals(XMLForms.ROW)) {
            Element availableValuesElement = findChildElement(currentElement, XMLForms.AVAILABLE_VALUES);
            if (availableValuesElement == null) {
                availableValuesElement = document.createElement(XMLForms.AVAILABLE_VALUES);
            }
            push(availableValuesElement);
            Element availableValuesListElement = findChildElement(currentElement, XMLForms.VALUES_LIST);
            if (availableValuesListElement == null) {
                availableValuesListElement = document.createElement(XMLForms.VALUES_LIST);
            }
            push(availableValuesListElement);
        }
        final Element availableValueElement = document.createElement(XMLForms.AVAILABLE_VALUE);
        push(availableValueElement);
        return this;
    }

    /**
     * Add a display format pattern for the display value of date widgets
     * 
     * @param displayFormat
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addDisplayFormat(final String displayFormat) throws InvalidFormDefinitionException {
        final String[] displayFormatParentsNames = { XMLForms.WIDGET };
        try {
            peek(displayFormatParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a display format is only supported on elements of type " + Arrays.asList(displayFormatParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("display format", displayFormat);
        final Element displayFormatElement = document.createElement(XMLForms.DISPLAY_FORMAT);
        displayFormatElement.setTextContent(displayFormat);
        push(displayFormatElement);
        return this;
    }

    /**
     * Add an error template on an application
     * 
     * @param templateUri
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addErrorTemplate(final String templateUri) throws InvalidFormDefinitionException {
        final String[] errorTemplateParentsNames = { XMLForms.APPLICATION };
        try {
            peek(errorTemplateParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an error template is only supported on elements of type " + Arrays.asList(errorTemplateParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("error template", templateUri);
        checkStringNotEmpty("error template", templateUri);
        final Element errorTemplateElement = document.createElement(XMLForms.ERROR_TEMPLATE);
        errorTemplateElement.setTextContent(templateUri);
        push(errorTemplateElement);
        return this;
    }

    /**
     * Add a confirmation layout on an application
     * 
     * @param layoutUri
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addConfirmationLayout(final String templateUri) throws InvalidFormDefinitionException {
        final String[] confirmationTemplateParentsNames = { XMLForms.FORM };
        try {
            peek(confirmationTemplateParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a confirmation layout is only supported on elements of type "
                    + Arrays.asList(confirmationTemplateParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("confirmation layout", templateUri);
        checkStringNotEmpty("confirmation layout", templateUri);
        final Element confirmationTemplateElement = document.createElement(XMLForms.CONFIRMATION_LAYOUT);
        confirmationTemplateElement.setTextContent(templateUri);
        push(confirmationTemplateElement);
        return this;
    }

    /**
     * Add a confirmation message on an application
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addConfirmationMessageExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] confirmationMessageParentsNames = { XMLForms.FORM };
        try {
            peek(confirmationMessageParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a confirmation message is only supported on elements of type "
                    + Arrays.asList(confirmationMessageParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element confirmationMessageElement = document.createElement(XMLForms.CONFIRMATION_MESSAGE);
        push(confirmationMessageElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add CSS class names to the items of a radiobutton or checkbox group widget
     * 
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addItemsStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] itemsStyleParentsNames = { XMLForms.WIDGET };
        try {
            peek(itemsStyleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an items style element is only supported on elements of type " + Arrays.asList(itemsStyleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("items style", cssClasses);
        final Element itemsStyleElement = document.createElement(XMLForms.ITEMS_STYLE);
        itemsStyleElement.setTextContent(cssClasses);
        push(itemsStyleElement);
        return this;
    }

    /**
     * Add a label on an application, page or widget
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addLabelExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] labelParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP, XMLForms.PAGE, XMLForms.APPLICATION, XMLForms.VALIDATOR,
                XMLForms.SUB_TITLE, XMLForms.AVAILABLE_VALUE };
        try {
            peek(labelParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a label is only supported on elements of type " + Arrays.asList(labelParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        Element labelElement = null;
        if (currentElement.getNodeName().equals(XMLForms.APPLICATION)) {
            labelElement = document.createElement(XMLForms.APPLICATION_LABEL);
        } else if (currentElement.getNodeName().equals(XMLForms.PAGE)) {
            labelElement = document.createElement(XMLForms.PAGE_LABEL);
        } else {
            labelElement = document.createElement(XMLForms.LABEL);
        }
        push(labelElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a Value expression
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addValueExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] labelParentsNames = { XMLForms.AVAILABLE_VALUE };
        try {
            peek(labelParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a value is only supported on elements of type " + Arrays.asList(labelParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element valueElement = document.createElement(XMLForms.VALUE);
        push(valueElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add CSS class names to a widget label
     * 
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addLabelStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] labelStyleParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(labelStyleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a label style is only supported on elements of type " + Arrays.asList(labelStyleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("label style", cssClasses);
        final Element labelStyleElement = document.createElement(XMLForms.LABEL_STYLE);
        labelStyleElement.setTextContent(cssClasses);
        push(labelStyleElement);
        return this;
    }

    /**
     * Specify the position of a widget label
     * 
     * @param labelPosition
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addLabelPosition(final ItemPosition labelPosition) throws InvalidFormDefinitionException {
        final String[] labelPositionParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(labelPositionParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a label position is only supported on elements of type " + Arrays.asList(labelPositionParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("label position", labelPosition);
        final Element labelPositionElement = document.createElement(XMLForms.LABEL_POSITION);
        labelPositionElement.setTextContent(labelPosition.name());
        push(labelPositionElement);
        return this;
    }

    /**
     * Add a mandatory field label on an application
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMandatoryLabelExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {
        final String[] mandatoryLabelParentsNames = { XMLForms.APPLICATION };
        try {
            peek(mandatoryLabelParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a mandatory label is only supported on elements of type " + Arrays.asList(mandatoryLabelParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element mandatoryLabelElement = document.createElement(XMLForms.MANDATORY_LABEL);
        push(mandatoryLabelElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a mandatory property to a widget
     * 
     * @param isMandatory
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMandatoryBehavior(final boolean isMandatory) throws InvalidFormDefinitionException {
        final String[] mandatoryBehaviorParentsNames = { XMLForms.WIDGET };
        try {
            peek(mandatoryBehaviorParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a mandatory behaviour is only supported on elements of type "
                    + Arrays.asList(mandatoryBehaviorParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("mandatory behavior", isMandatory);
        final Element mandatoryBehaviorElement = document.createElement(XMLForms.MANDATORY);
        mandatoryBehaviorElement.setTextContent(Boolean.toString(isMandatory));
        push(mandatoryBehaviorElement);
        return this;
    }

    /**
     * Indicates that the button should be displayed as a label instead of an html button
     * 
     * @param isLabelButton
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addLabelButtonBehavior(final boolean isLabelButton) throws InvalidFormDefinitionException {
        final String[] labelButtonBehaviorParentsNames = { XMLForms.WIDGET };
        try {
            peek(labelButtonBehaviorParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a label button behaviour is only supported on elements of type "
                    + Arrays.asList(labelButtonBehaviorParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("label button behavior", isLabelButton);
        final Element labelButtonBehaviorElement = document.createElement(XMLForms.LABEL_BUTTON);
        labelButtonBehaviorElement.setTextContent(Boolean.toString(isLabelButton));
        push(labelButtonBehaviorElement);
        return this;
    }

    /**
     * Add a mandatory field label and symbol style (css class names) on an application
     * 
     * @param label
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMandatoryStyle(final String mandatoryStyle) throws InvalidFormDefinitionException {
        final String[] mandatoryStyleParentsNames = { XMLForms.APPLICATION };
        try {
            peek(mandatoryStyleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a mandatory label style is only supported on elements of type "
                    + Arrays.asList(mandatoryStyleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("mandatory label style", mandatoryStyle);
        final Element mandatoryStyleElement = document.createElement(XMLForms.MANDATORY_STYLE);
        mandatoryStyleElement.setTextContent(mandatoryStyle);
        push(mandatoryStyleElement);
        return this;
    }

    /**
     * Add a mandatory field symbol expression on an application
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMandatorySymbolExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {
        final String[] mandatorySymbolParentsNames = { XMLForms.APPLICATION };
        try {
            peek(mandatorySymbolParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a mandatory symbol is only supported on elements of type "
                    + Arrays.asList(mandatorySymbolParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element mandatorySymbolElement = document.createElement(XMLForms.MANDATORY_SYMBOL);
        push(mandatorySymbolElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a max length number of characters property to a widget for textbox and textarea widgets
     * 
     * @param maxLength
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMaxHeight(final int maxHeight) throws InvalidFormDefinitionException {
        final String[] maxHeigthParentsNames = { XMLForms.WIDGET };
        try {
            peek(maxHeigthParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a max heigth is only supported on elements of type " + Arrays.asList(maxHeigthParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element maxHeigthElement = document.createElement(XMLForms.MAX_HEIGHT);
        maxHeigthElement.setTextContent(Integer.toString(maxHeight));
        push(maxHeigthElement);
        return this;
    }

    /**
     * Add a max length number of characters property to a widget for textbox and textarea widgets
     * 
     * @param maxLength
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMaxLength(final int maxLength) throws InvalidFormDefinitionException {
        final String[] maxLengthParentsNames = { XMLForms.WIDGET };
        try {
            peek(maxLengthParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a max length is only supported on elements of type " + Arrays.asList(maxLengthParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element maxLengthElement = document.createElement(XMLForms.MAX_LENGTH);
        maxLengthElement.setTextContent(Integer.toString(maxLength));
        push(maxLengthElement);
        return this;
    }

    /**
     * Add a page in the edition page flows and create the form if it doesn't exist yet
     * 
     * @param pageId
     * @return
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addPage(final String pageId) throws InvalidFormDefinitionException {
        final String[] pageParentsNames = { XMLForms.FORM };
        try {
            peek(pageParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an entry page is only supported on elements of type " + Arrays.asList(pageParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("page Id", pageId);
        checkStringNotEmpty("page Id", pageId);
        Element pagesElement = findChildElement(currentElement, XMLForms.PAGES);
        if (pagesElement == null) {
            pagesElement = document.createElement(XMLForms.PAGES);
        }
        push(pagesElement);
        final Element pageElement = document.createElement(XMLForms.PAGE);
        pageElement.setAttribute(XMLForms.ID, escapeSingleQuote(pageId));
        push(pageElement);
        return this;
    }

    /**
     * Add an entry form on an application
     * If an application has no entry form, it means that it hasn't been defined,
     * and the form for the application will be automatically generated.
     * Whereas if it has an empty entry form, the application will be automatically instantiated.
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addEntryForm(final String formId) throws InvalidFormDefinitionException {
        final String[] formParentsNames = { XMLForms.APPLICATION };
        try {
            peek(formParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an entry form is only supported on elements of type " + Arrays.asList(formParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("form Id", formId);
        checkStringNotEmpty("form Id", formId);
        Element entryFormsElement = findChildElement(currentElement, XMLForms.FORMS);
        if (entryFormsElement == null) {
            entryFormsElement = document.createElement(XMLForms.FORMS);
        }
        push(entryFormsElement);
        final Element entryFormElement = document.createElement(XMLForms.FORM);
        entryFormElement.setAttribute(XMLForms.ID, escapeSingleQuote(formId));
        addChild(entryFormElement, XMLForms.FORM_TYPE, FormType.entry.name(), true, true);
        push(entryFormElement);
        return this;
    }

    /**
     * Add a view form on an application
     * If an application has no form, it means that it hasn't been defined,
     * and the form for the application will be automatically generated.
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addViewForm(final String formId) throws InvalidFormDefinitionException {
        final String[] formParentsNames = { XMLForms.APPLICATION };
        try {
            peek(formParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a view form is only supported on elements of type " + Arrays.asList(formParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("form Id", formId);
        checkStringNotEmpty("form Id", formId);
        Element viewFormsElement = findChildElement(currentElement, XMLForms.FORMS);
        if (viewFormsElement == null) {
            viewFormsElement = document.createElement(XMLForms.FORMS);
        }
        push(viewFormsElement);
        final Element viewFormElement = document.createElement(XMLForms.FORM);
        viewFormElement.setAttribute(XMLForms.ID, escapeSingleQuote(formId));
        addChild(viewFormElement, XMLForms.FORM_TYPE, FormType.view.name(), true, true);
        push(viewFormElement);
        return this;
    }

    /**
     * Add an application
     * 
     * @param applicationName
     *            the name of the application
     * @param applicationVersion
     *            the version of the application
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addApplication(final String applicationName, final String applicationVersion) throws InvalidFormDefinitionException {
        final String[] applicationParentsNames = { XMLForms.FORMS_DEFINITION };
        try {
            peek(applicationParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a application is only supported on elements of type " + Arrays.asList(applicationParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("application name", applicationName);
        checkArgNotNull("application version", applicationVersion);
        final Element applicationElement = document.createElement(XMLForms.APPLICATION);
        applicationElement.setAttribute(XMLForms.NAME, applicationName);
        applicationElement.setAttribute(XMLForms.VERSION, applicationVersion);
        push(applicationElement);
        return this;
    }

    /**
     * Add CSS classes names to a widget
     * 
     * @param cssClasses
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("style", cssClasses);
        final Element styleElement = document.createElement(XMLForms.STYLE);
        styleElement.setTextContent(cssClasses);
        push(styleElement);
        return this;
    }

    /**
     * Add a layout on an application or a page
     * 
     * @param layoutUri
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addLayout(final String layoutUri) throws InvalidFormDefinitionException {
        final String[] layoutParentsNames = { XMLForms.APPLICATION, XMLForms.PAGE };
        try {
            peek(layoutParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a style is only supported on elements of type " + Arrays.asList(layoutParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("layout", layoutUri);
        checkStringNotEmpty("layout", layoutUri);
        Element templateElement = null;
        if (currentElement.getNodeName().equals(XMLForms.APPLICATION)) {
            templateElement = document.createElement(XMLForms.APPLICATION_LAYOUT);
        } else {
            templateElement = document.createElement(XMLForms.PAGE_LAYOUT);
        }
        templateElement.setTextContent(layoutUri);
        push(templateElement);
        return this;
    }

    /**
     * Add a title (tooltip) to a widget field
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addTitleExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] titleParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(titleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a title is only supported on elements of type " + Arrays.asList(titleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element titleElement = document.createElement(XMLForms.TITLE);
        push(titleElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a validator on a page or a widget and create the list of validators if it doesn't exist yet
     * 
     * @param validatorId
     *            the validator Id
     * @param className
     *            the classname of the validator
     * @param cssClasses
     *            the css classes for the error label
     * @param position
     *            the position of the error label
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addValidator(final String validatorId, final String className, final String cssClasses, final ValidatorPosition position)
            throws InvalidFormDefinitionException {
        final String[] validatorsParentsNames = { XMLForms.PAGE, XMLForms.WIDGET };
        try {
            peek(validatorsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a validator is only supported on elements of type " + Arrays.asList(validatorsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("validator id", validatorId);
        checkStringNotEmpty("validator id", validatorId);
        Element validatorsElement = null;
        if (currentElement.getNodeName().equals(XMLForms.PAGE)) {
            validatorsElement = findChildElement(currentElement, XMLForms.PAGE_VALIDATORS);
            if (validatorsElement == null) {
                validatorsElement = document.createElement(XMLForms.PAGE_VALIDATORS);
            }
        } else {
            validatorsElement = findChildElement(currentElement, XMLForms.VALIDATORS);
            if (validatorsElement == null) {
                validatorsElement = document.createElement(XMLForms.VALIDATORS);
            }
        }
        push(validatorsElement);
        final Element validatorElement = document.createElement(XMLForms.VALIDATOR);
        validatorElement.setAttribute(XMLForms.ID, validatorId);
        addChild(validatorElement, XMLForms.CLASSNAME, className, true, true);
        addChild(validatorElement, XMLForms.STYLE, cssClasses, false, false);
        if (position != null) {
            addChild(validatorElement, XMLForms.POSITION, position.name(), false, true);
        }
        push(validatorElement);
        return this;
    }

    /**
     * Add a parameter expression
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addParameterExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] titleParentsNames = { XMLForms.VALIDATOR };
        try {
            peek(titleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a parameter is only supported on elements of type " + Arrays.asList(titleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element parameterElement = document.createElement(XMLForms.PARAMETER);
        push(parameterElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a widget on pages
     * 
     * @param widgetId
     * @param widgetType
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addWidget(final String widgetId, final WidgetType widgetType) throws InvalidFormDefinitionException {
        final String[] widgetParentsNames = { XMLForms.PAGE, XMLForms.WIDGETS_GROUP };
        try {
            peek(widgetParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a widget is only supported on elements of type " + Arrays.asList(widgetParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("widget id", widgetId);
        checkStringNotEmpty("widget id", widgetId);
        checkArgNotNull("widget type", widgetType);
        Element widgetsElement = findChildElement(currentElement, XMLForms.WIDGETS);
        if (widgetsElement == null) {
            widgetsElement = document.createElement(XMLForms.WIDGETS);
        }
        push(widgetsElement);
        final Element widgetElement = document.createElement(XMLForms.WIDGET);
        widgetElement.setAttribute(XMLForms.ID, widgetId);
        widgetElement.setAttribute(XMLForms.TYPE, widgetType.name());
        push(widgetElement);
        return this;
    }

    /**
     * Add an attachement image behavior for the display of image previews on file download widgets or the display of attachments in image widgets
     * 
     * @param attachmentImage
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addAttachmentImageBehavior(final boolean attachmentImage) throws InvalidFormDefinitionException {
        final String[] imagePreviewParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(imagePreviewParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a image preview behavior is only supported on elements of type "
                    + Arrays.asList(imagePreviewParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("image preview", attachmentImage);
        final Element attachmentImageElement = document.createElement(XMLForms.DISPLAY_ATTACHMENT_IMAGE);
        attachmentImageElement.setTextContent(Boolean.toString(attachmentImage));
        push(attachmentImageElement);
        return this;
    }

    /**
     * Add allow HTML in field behavior
     * 
     * @param allowHTMLInField
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addAllowHTMLInFieldBehavior(final boolean allowHTMLInField) throws InvalidFormDefinitionException {
        final String[] allowHTMLInFiedParentsNames = { XMLForms.WIDGET };
        try {
            peek(allowHTMLInFiedParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a property to allow or not HTML in form fields is only supported on elements of type "
                    + Arrays.asList(allowHTMLInFiedParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("allow HTML in field", allowHTMLInField);
        final Element allowHTMLInFieldElement = document.createElement(XMLForms.ALLOW_HTML_IN_FIELD);
        allowHTMLInFieldElement.setTextContent(Boolean.toString(allowHTMLInField));
        push(allowHTMLInFieldElement);
        return this;
    }

    /**
     * Add allow HTML in label behavior
     * 
     * @param allowHTMLInLabel
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addAllowHTMLInLabelBehavior(final boolean allowHTMLInLabel) throws InvalidFormDefinitionException {
        final String[] allowHTMLInLabelParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP, XMLForms.PAGE };
        try {
            peek(allowHTMLInLabelParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a property to allow or not HTML in form labels is only supported on elements of type "
                    + Arrays.asList(allowHTMLInLabelParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("allow HTML in label", allowHTMLInLabel);
        final Element allowHTMLInLabelElement = document.createElement(XMLForms.ALLOW_HTML_IN_LABEL);
        allowHTMLInLabelElement.setTextContent(Boolean.toString(allowHTMLInLabel));
        push(allowHTMLInLabelElement);
        return this;
    }

    /**
     * Add a Html attribute to a widget
     * 
     * @param name
     *            the name of the attribute
     * @param value
     *            the valueof the attribute
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addHTMLAttribute(final String name, final String value) throws InvalidFormDefinitionException {
        final String[] htmlAttributesParentsNames = { XMLForms.WIDGET };
        try {
            peek(htmlAttributesParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an HTML attribute is only supported on elements of type " + Arrays.asList(htmlAttributesParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("HTML attribute name", name);
        checkArgNotNull("HTML attribute name value", value);
        Element htmlAttributesElement = findChildElement(currentElement, XMLForms.HTML_ATTRIBUTES);
        if (htmlAttributesElement == null) {
            htmlAttributesElement = document.createElement(XMLForms.HTML_ATTRIBUTES);
        }
        push(htmlAttributesElement);
        final Element htmlAttributeElement = document.createElement(XMLForms.HTML_ATTRIBUTE);
        htmlAttributeElement.setAttribute(XMLForms.NAME, name);
        htmlAttributeElement.setTextContent(value);
        push(htmlAttributeElement);
        return this;
    }

    /**
     * Add table style for table widgets
     * 
     * @param cssClasses
     *            the CSS classes for the table
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addTableStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a table style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("table style", cssClasses);
        final Element styleElement = document.createElement(XMLForms.TABLE_STYLE);
        styleElement.setTextContent(cssClasses);
        push(styleElement);
        return this;
    }

    /**
     * Add image style for image widgets
     * 
     * @param cssClasses
     *            the CSS classes for the image
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addImageStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an image style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("image style", cssClasses);
        final Element styleElement = document.createElement(XMLForms.IMAGE_STYLE);
        styleElement.setTextContent(cssClasses);
        push(styleElement);
        return this;
    }

    /**
     * Add cells style for table widgets
     * 
     * @param cssClasses
     *            the CSS classes for the cells
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addCellsStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a cells style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("cells style", cssClasses);
        final Element styleElement = document.createElement(XMLForms.CELL_STYLE);
        styleElement.setTextContent(cssClasses);
        push(styleElement);
        return this;
    }

    /**
     * Add headings style for table widgets
     * 
     * @param cssClasses
     *            the CSS classes for the headings
     * @param leftHeadings
     *            if true, indicates that the left column of the grid should be considered as a header
     * @param topHeadings
     *            if true, indicates that the top row of the grid should be considered as a header
     * @param rightHeadings
     *            if true, indicates that the right column of the grid should be considered as a header
     * @param bottomHeadings
     *            if true, indicates that the bottom row of the grid should be considered as a header
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addHeadingsStyle(final String cssClasses, final boolean leftHeadings, final boolean topHeadings, final boolean rightHeadings,
            final boolean bottomHeadings) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a headingss style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("headings style", cssClasses);
        final Element styleElement = document.createElement(XMLForms.HEADINGS_STYLE);
        styleElement.setTextContent(cssClasses);
        push(styleElement);
        peek(styleParentsNames);
        final Element headingsPositionsElement = document.createElement(XMLForms.HEADINGS_POSITIONS);
        addChild(headingsPositionsElement, XMLForms.LEFT_HEADINGS, Boolean.toString(leftHeadings), true, true);
        addChild(headingsPositionsElement, XMLForms.TOP_HEADINGS, Boolean.toString(topHeadings), true, true);
        addChild(headingsPositionsElement, XMLForms.RIGHT_HEADINGS, Boolean.toString(rightHeadings), true, true);
        addChild(headingsPositionsElement, XMLForms.BOTTOM_HEADINGS, Boolean.toString(bottomHeadings), true, true);
        push(headingsPositionsElement);
        return this;
    }

    /**
     * Add a readonly property to a widget
     * 
     * @param isReadOnly
     *            the readonly behavior
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addReadOnlyBehavior(final boolean isReadOnly) throws InvalidFormDefinitionException {
        final String[] readOnlyBehaviorParentsNames = { XMLForms.WIDGET };
        try {
            peek(readOnlyBehaviorParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a read-only behaviour is only supported on elements of type "
                    + Arrays.asList(readOnlyBehaviorParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("read-only behavior", isReadOnly);
        final Element mandatoryBehaviorElement = document.createElement(XMLForms.READ_ONLY);
        mandatoryBehaviorElement.setTextContent(Boolean.toString(isReadOnly));
        push(mandatoryBehaviorElement);
        return this;
    }

    /**
     * add a maximum number of columns to a widget (for editable tables)
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMaxColumnsExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] maxColumnsParentsNames = { XMLForms.WIDGET };
        try {
            peek(maxColumnsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a max columns property is only supported on elements of type " + Arrays.asList(maxColumnsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element maxColumnsElement = document.createElement(XMLForms.MAX_COLUMNS);
        push(maxColumnsElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * add a maximum number of rows to a widget (for tables)
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMaxRowsExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] maxRowsParentsNames = { XMLForms.WIDGET };
        try {
            peek(maxRowsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a max rows property is only supported on elements of type " + Arrays.asList(maxRowsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element maxRowsElement = document.createElement(XMLForms.MAX_ROWS);
        push(maxRowsElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * add a minimum number of columns to a widget (for editable tables)
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMinColumnsExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] minColumnsParentsNames = { XMLForms.WIDGET };
        try {
            peek(minColumnsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a min columns property is only supported on elements of type " + Arrays.asList(minColumnsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element minColumnsElement = document.createElement(XMLForms.MIN_COLUMNS);
        push(minColumnsElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * add a minimum number of rows to a widget (for editable tables)
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addMinRowsExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] minRowsParentsNames = { XMLForms.WIDGET };
        try {
            peek(minRowsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a min rows property is only supported on elements of type " + Arrays.asList(minRowsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element minRowsElement = document.createElement(XMLForms.MIN_ROWS);
        push(minRowsElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * add a variable columns number behavior (for editable tables)
     * 
     * @param variableColumnsNumber
     *            the variable columns number
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addVariableColumnsNumber(final boolean variableColumnsNumber) throws InvalidFormDefinitionException {
        final String[] variableColumnsNumberParentsNames = { XMLForms.WIDGET };
        try {
            peek(variableColumnsNumberParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a variable columns number behaviour is only supported on elements of type "
                    + Arrays.asList(variableColumnsNumberParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("variable columns number behaviour", variableColumnsNumber);
        final Element variableColumnsNumberElement = document.createElement(XMLForms.VARIABLE_COLUMNS);
        variableColumnsNumberElement.setTextContent(Boolean.toString(variableColumnsNumber));
        push(variableColumnsNumberElement);
        return this;
    }

    /**
     * add a variable rows number behavior (for editable tables)
     * 
     * @param variableRowsNumber
     *            the variable rows number
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addVariableRowsNumber(final boolean variableRowsNumber) throws InvalidFormDefinitionException {
        final String[] variableRowsNumberParentsNames = { XMLForms.WIDGET };
        try {
            peek(variableRowsNumberParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a variable rows number behaviour is only supported on elements of type "
                    + Arrays.asList(variableRowsNumberParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("variable rows number behaviour", variableRowsNumber);
        final Element variableRowsNumberElement = document.createElement(XMLForms.VARIABLE_ROWS);
        variableRowsNumberElement.setTextContent(Boolean.toString(variableRowsNumber));
        push(variableRowsNumberElement);
        return this;
    }

    /**
     * specify the index of column which is used as the value of the selected row(s)
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addValueColumnIndexExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] valueColumnIndexParentsNames = { XMLForms.WIDGET };
        try {
            peek(valueColumnIndexParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a value column index property is only supported on elements of type "
                    + Arrays.asList(valueColumnIndexParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element valueColumnIndexElement = document.createElement(XMLForms.VALUE_COLUMN_INDEX);
        push(valueColumnIndexElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * @param selectMode
     */
    @Override
    public IFormBuilder addSelectMode(final SelectMode selectMode) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a select mode is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("select mode", selectMode);
        final Element selectModeElement = document.createElement(XMLForms.SELECT_MODE);
        selectModeElement.setTextContent(selectMode.name());
        push(selectModeElement);
        return this;
    }

    /**
     * @param selectedItemsStyle
     */
    @Override
    public IFormBuilder addSelectedItemsStyle(final String selectedItemsStyle) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a selected items style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("selected items style", selectedItemsStyle);
        final Element styleElement = document.createElement(XMLForms.SELECTED_ITEMS_STYLE);
        styleElement.setTextContent(selectedItemsStyle);
        push(styleElement);
        return this;
    }

    /**
     * @param maxItems
     */
    @Override
    public IFormBuilder addMaxItems(final int maxItems) throws InvalidFormDefinitionException {
        final String[] maxItemsParentsNames = { XMLForms.WIDGET };
        try {
            peek(maxItemsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a max items property is only supported on elements of type " + Arrays.asList(maxItemsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element maxItemsElement = document.createElement(XMLForms.MAX_ITEMS);
        maxItemsElement.setTextContent(Integer.toString(maxItems));
        push(maxItemsElement);
        return this;
    }

    /**
     * Add a transient data on a page flow
     * 
     * @param name
     *            name of the transient data
     * @param className
     *            classnameof the transient data
     * @param value
     *            value of the transient data
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addTransientData(final String name, final String className) throws InvalidFormDefinitionException {
        final String[] pageflowParentsNames = { XMLForms.FORM };
        try {
            peek(pageflowParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a transient data is only supported on elements of type " + Arrays.asList(pageflowParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("data name", name);
        checkStringNotEmpty("data name", name);
        Element transientDataElement = findChildElement(currentElement, XMLForms.TRANSIENT_DATA);
        if (transientDataElement == null) {
            transientDataElement = document.createElement(XMLForms.TRANSIENT_DATA);
        }
        push(transientDataElement);
        final Element dataElement = document.createElement(XMLForms.DATA);
        dataElement.setAttribute(XMLForms.NAME, name);
        addChild(dataElement, XMLForms.CLASSNAME, className, true, true);
        push(dataElement);
        return this;
    }

    /**
     * Add a transient data expression
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addTransientDataExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {
        final String[] firstPageParentsNames = { XMLForms.DATA };
        try {
            peek(firstPageParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a transient data expression element is only supported on elements of type "
                    + Arrays.asList(firstPageParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element firstPageElement = document.createElement(XMLForms.VALUE);
        push(firstPageElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a first page Id expression
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addFirstPageIdExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {
        final String[] firstPageParentsNames = { XMLForms.FORM };
        try {
            peek(firstPageParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a first page element is only supported on elements of type " + Arrays.asList(firstPageParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element firstPageElement = document.createElement(XMLForms.FIRST_PAGE);
        push(firstPageElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a next page id on a page flow
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addNextPageIdExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] firstPageParentsNames = { XMLForms.PAGE };
        try {
            peek(firstPageParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a next page attribute is only supported on elements of type " + Arrays.asList(firstPageParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element nextPageElement = document.createElement(XMLForms.NEXT_PAGE);
        push(nextPageElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add a delay millisecond property to a widget for asynchronous suggestbox widgets
     * 
     * @param delayMillis
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addDelayMillis(final int delayMillis) throws InvalidFormDefinitionException {
        final String[] delayMillisParentsNames = { XMLForms.WIDGET };
        try {
            peek(delayMillisParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a delay millis property is only supported on elements of type "
                    + Arrays.asList(delayMillisParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element delayMillisElement = document.createElement(XMLForms.DELAY_MILLIS);
        delayMillisElement.setTextContent(Integer.toString(delayMillis));
        push(delayMillisElement);
        return this;
    }

    /**
     * Add a sub title property to a widget to accept an "example" parameter
     * 
     * @param position
     *            sub title position
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addSubTitle(final SubTitlePosition position) throws InvalidFormDefinitionException {
        final String[] subTitleParentsNames = { XMLForms.WIDGET };
        try {
            peek(subTitleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a sub title property is only supported on elements of type " + Arrays.asList(subTitleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element subTitleElement = document.createElement(XMLForms.SUB_TITLE);
        if (position != null) {
            addChild(subTitleElement, XMLForms.POSITION, position.name(), false, true);
        }
        push(subTitleElement);
        return this;
    }

    /**
     * Add a popup tooltip, that will be displayed to help the user when he clicks on the
     * bulb icon that is placed beside of a widget.
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder addPopupToolTipExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] popupToolTipParentsNames = { XMLForms.WIDGET };
        try {
            peek(popupToolTipParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a pupup tooltip property is only supported on elements of type "
                    + Arrays.asList(popupToolTipParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element popupToolTipElement = document.createElement(XMLForms.POPUP_TOOLTIP);
        push(popupToolTipElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * add permissions
     * 
     * @param permissions
     */
    @Override
    public IFormBuilder addPermissions(final String permissions) throws InvalidFormDefinitionException {
        final String[] permissionsParentsNames = { XMLForms.APPLICATION, XMLForms.FORM };
        try {
            peek(permissionsParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of permissions property is only supported on elements of type " + Arrays.asList(permissionsParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element permissionsElement = document.createElement(XMLForms.PERMISSIONS);
        permissionsElement.setTextContent(permissions);
        push(permissionsElement);
        return this;
    }

    /**
     * add next form id
     * 
     * @param name
     *            the name of the expression
     * @param content
     *            the real script code of the expression
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addNextFormIdExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] nextFormParentsNames = { XMLForms.FORM };
        try {
            peek(nextFormParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a next form attribute is only supported on elements of type " + Arrays.asList(nextFormParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element nextPageElement = document.createElement(XMLForms.NEXT_FORM);
        push(nextPageElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add an expression to the current element, as its the dependent expressions
     * 
     * @param name
     *            the name of the expression
     * @param content
     *            the real script code of the expression
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addDependentExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        return addDependentExpression(name, content, expressionType, returnType, interpreter, true);
    }

    /**
     * Add an expression to the current element, as its the dependent expressions
     * 
     * @param name
     *            the name of the expression
     * @param content
     *            the real script code of the expression
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @param isSameLevelDependency
     */
    @Override
    public IFormBuilder addDependentExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter, final boolean isSameLevelDependency) throws InvalidFormDefinitionException {

        if (isSameLevelDependency) {
            final String[] expressionElementNames = { XMLForms.EXPRESSION };
            try {
                peekParentFirst(expressionElementNames);
            } catch (final InvalidFormDefinitionException e) {
                final String errorMessage = "The method addDependentExpression can only be called once an expression has been created.";
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.log(Level.SEVERE, errorMessage, e);
                }
                throw new InvalidFormDefinitionException(errorMessage, e);
            }
        }
        Element dependenciesElement = findChildElement(currentElement, XMLForms.DEPENDENCIES);
        if (dependenciesElement == null) {
            dependenciesElement = document.createElement(XMLForms.DEPENDENCIES);
            push(dependenciesElement);
        } else {
            currentElement = dependenciesElement;
        }
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * End an expression dependencies group
     * 
     * @return an implementation of {@link IFormBuilder}
     * @throws InvalidFormDefinitionException
     */
    @Override
    public IFormBuilder endExpressionDependencies() throws InvalidFormDefinitionException {
        final String[] expressionElementNames = { XMLForms.EXPRESSION };
        try {
            currentElement = (Element) currentElement.getParentNode();
            peekParentFirst(expressionElementNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The method endExpressionDependencies can only be called once an expression dependency has been created.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        return this;
    }

    /**
     * Add an expression to the current element
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     * @throws InvalidFormDefinitionException
     */
    protected IFormBuilder addExpression(final String name, final String content, final String expressionType, final String returnType, final String interpreter)
            throws InvalidFormDefinitionException {

        final Element expressionElement = document.createElement(XMLForms.EXPRESSION);
        addChild(expressionElement, XMLForms.NAME, name, false, true);
        addChild(expressionElement, XMLForms.EXPRESSION_CONTENT, content, true, false);
        addChild(expressionElement, XMLForms.EXPRESSION_TYPE, expressionType, true, true);
        addChild(expressionElement, XMLForms.EXPRESSION_RETURN_TYPE, returnType, true, true);
        addChild(expressionElement, XMLForms.EXPRESSION_INTERPRETER, interpreter, false, true);
        push(expressionElement);
        return this;
    }

    /**
     * Add initial value
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addInitialValueExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] initialValueParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP, XMLForms.ROW, XMLForms.TRANSIENT_DATA };
        try {
            peek(initialValueParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an initial value is only supported on elements of type " + Arrays.asList(initialValueParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }

        if (!currentElement.getNodeName().equals(XMLForms.ROW)) {
            Element initialValueElement = findChildElement(currentElement, XMLForms.INITIAL_VALUE);
            if (initialValueElement == null) {
                initialValueElement = document.createElement(XMLForms.INITIAL_VALUE);
            } else {
                throw new InvalidFormDefinitionException("This element already has an initial value defined.");
            }
            push(initialValueElement);
        }
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add initial value Resource for file widgets on instantiation form
     * 
     * @param resourcePath
     */
    @Override
    public IFormBuilder addInitialValueResource(final String resourcePath) throws InvalidFormDefinitionException {

        final String[] initialValueParentsNames = { XMLForms.WIDGET };
        try {
            peek(initialValueParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a resource initial value is only supported on elements of type "
                    + Arrays.asList(initialValueParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull(XMLForms.PATH, resourcePath);
        checkStringNotEmpty(XMLForms.PATH, resourcePath);
        Element initialValueElement = findChildElement(currentElement, XMLForms.INITIAL_VALUE);
        if (initialValueElement == null) {
            initialValueElement = document.createElement(XMLForms.INITIAL_VALUE);
        } else {
            throw new InvalidFormDefinitionException("This element already has an initial value defined.");
        }
        push(initialValueElement);
        final Element resourceElement = document.createElement(XMLForms.RESOURCE);
        initialValueElement.appendChild(resourceElement);
        final Element element = document.createElement(XMLForms.PATH);
        element.setTextContent(resourcePath);
        resourceElement.appendChild(element);
        return this;
    }

    /**
     * Add initial values
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addAvailableValuesExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] availableValuesExpressionParentsNames = { XMLForms.WIDGET };
        try {
            peek(availableValuesExpressionParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an available values expression is only supported on elements of type "
                    + Arrays.asList(availableValuesExpressionParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        Element availableValuesElement = findChildElement(currentElement, XMLForms.AVAILABLE_VALUES);
        if (availableValuesElement == null) {
            availableValuesElement = document.createElement(XMLForms.AVAILABLE_VALUES);
        }
        push(availableValuesElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add vertical header expression list
     */
    @Override
    public IFormBuilder addVerticalHeaderExpressionList() throws InvalidFormDefinitionException {

        final String[] verticalHeaderParentsNames = { XMLForms.WIDGET };
        try {
            peek(verticalHeaderParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a vertical header is only supported on elements of type " + Arrays.asList(verticalHeaderParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element verticalHeaderElement = document.createElement(XMLForms.VERTICAL_HEADER);
        push(verticalHeaderElement);
        final Element rowElement = document.createElement(XMLForms.EXPRESSION_LIST);
        push(rowElement);
        return this;
    }

    /**
     * Add vertical header
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addVerticalHeaderExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] verticalHeaderParentsNames = { XMLForms.WIDGET, XMLForms.EXPRESSION_LIST };
        try {
            peek(verticalHeaderParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a vertical header is only supported on elements of type " + Arrays.asList(verticalHeaderParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        if (currentElement.getNodeName().equals(XMLForms.EXPRESSION_LIST)) {
            addExpression(name, content, expressionType, returnType, interpreter);
        } else {
            final Element verticalHeaderElement = document.createElement(XMLForms.VERTICAL_HEADER);
            push(verticalHeaderElement);
            addExpression(name, content, expressionType, returnType, interpreter);
        }
        return this;
    }

    /**
     * Add horizontal header
     */
    @Override
    public IFormBuilder addHorizontalHeaderExpressionList() throws InvalidFormDefinitionException {

        final String[] horizontalHeaderParentsNames = { XMLForms.WIDGET };
        try {
            peek(horizontalHeaderParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a horizontal header is only supported on elements of type "
                    + Arrays.asList(horizontalHeaderParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element horizontalHeaderElement = document.createElement(XMLForms.HORIZONTAL_HEADER);
        push(horizontalHeaderElement);
        final Element rowElement = document.createElement(XMLForms.EXPRESSION_LIST);
        push(rowElement);
        return this;
    }

    /**
     * Add horizontal header
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addHorizontalHeaderExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] horizontalHeaderParentsNames = { XMLForms.WIDGET, XMLForms.EXPRESSION_LIST };
        try {
            peek(horizontalHeaderParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a horizontal header is only supported on elements of type "
                    + Arrays.asList(horizontalHeaderParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        if (currentElement.getNodeName().equals(XMLForms.EXPRESSION_LIST)) {
            addExpression(name, content, expressionType, returnType, interpreter);
        } else {
            final Element horizontalHeaderElement = document.createElement(XMLForms.HORIZONTAL_HEADER);
            push(horizontalHeaderElement);
            addExpression(name, content, expressionType, returnType, interpreter);
        }
        return this;
    }

    /**
     * Add display condition
     * 
     * @param name
     * @param content
     * @param expressionType
     * @param returnType
     * @param interpreter
     */
    @Override
    public IFormBuilder addDisplayConditionExpression(final String name, final String content, final String expressionType, final String returnType,
            final String interpreter) throws InvalidFormDefinitionException {

        final String[] displayConditionParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(displayConditionParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a display condition is only supported on elements of type "
                    + Arrays.asList(displayConditionParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element displayConditionElement = document.createElement(XMLForms.DISPLAY_CONDITION);
        push(displayConditionElement);
        addExpression(name, content, expressionType, returnType, interpreter);
        return this;
    }

    /**
     * Add an input type to a file widget
     * 
     * @param fileWidgetInputType
     */
    @Override
    public IFormBuilder addFileWidgetInputType(final FileWidgetInputType fileWidgetInputType) throws InvalidFormDefinitionException {

        final String[] displayConditionParentsNames = { XMLForms.WIDGET };
        try {
            peek(displayConditionParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of an input type on a file widget is only supported on elements of type "
                    + Arrays.asList(displayConditionParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element fileInputTypeElement = document.createElement(XMLForms.FILE_INPUT_TYPE);
        fileInputTypeElement.setTextContent(fileWidgetInputType.name());
        currentElement.appendChild(fileInputTypeElement);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFormBuilder addFieldOutputType(final String fieldOutputType) throws InvalidFormDefinitionException {

        final String[] displayConditionParentsNames = { XMLForms.WIDGET };
        try {
            peek(displayConditionParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a field output type on a file widget is only supported on elements of type "
                    + Arrays.asList(displayConditionParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        final Element fieldOutputTypeElement = document.createElement(XMLForms.FIELD_OUTPUT_TYPE);
        fieldOutputTypeElement.setTextContent(fieldOutputType);
        currentElement.appendChild(fieldOutputTypeElement);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IFormBuilder addInputStyle(final String cssClasses) throws InvalidFormDefinitionException {
        final String[] styleParentsNames = { XMLForms.WIDGET, XMLForms.WIDGETS_GROUP };
        try {
            peek(styleParentsNames);
        } catch (final InvalidFormDefinitionException e) {
            final String errorMessage = "The addition of a style is only supported on elements of type " + Arrays.asList(styleParentsNames);
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new InvalidFormDefinitionException(errorMessage, e);
        }
        checkArgNotNull("input style", cssClasses);
        final Element styleElement = document.createElement(XMLForms.INPUT_STYLE);
        styleElement.setTextContent(cssClasses);
        push(styleElement);
        return this;
    }

    protected String escapeSingleQuote(final String str) {
        return str.replaceAll("'", XMLForms.SINGLE_QUOTE_ESCAPE);
    }

    /**
     * Add a child element
     * 
     * @param parentElement
     * @param childName
     * @param childValue
     * @param isMandatory
     * @throws InvalidFormDefinitionException
     */
    protected void addChild(final Element parentElement, final String childName, final String childValue, final boolean isMandatory,
            final boolean isEmptyForbidden) throws InvalidFormDefinitionException {
        if (isMandatory) {
            checkArgNotNull(childName, childValue);
        }
        if (isEmptyForbidden) {
            checkStringNotEmpty(childName, childValue);
        }
        if (childValue != null) {
            final Element element = document.createElement(childName);
            element.setTextContent(childValue);
            parentElement.appendChild(element);
        }
    }

    /**
     * Find the first element with the given tag name among an element children
     * 
     * @param parent
     *            the parent element
     * @param childName
     *            the tag name
     * @return an {@link Element} or null if there are no elements with thegiven tag name among the element's children
     */
    protected Element findChildElement(final Element parent, final String childName) {
        final NodeList nodeList = parent.getElementsByTagName(childName);
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Element element = (Element) nodeList.item(i);
            if (element.getParentNode().isSameNode(parent)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Add an element to the stack
     * 
     * @param element
     */
    protected void push(final Element element) {
        if (element.getParentNode() == null) {
            currentElement.appendChild(element);
        }
        currentElement = element;
    }

    /**
     * Retrieve the first element in the DOM whose type is among the element types provided
     * 
     * @param elementTypes
     *            array of required element types
     * @return the first {@link Element} in the stack whose type is among the element types provided
     * @throws InvalidFormDefinitionException
     *             if no element among the current element's parents has one of the required type
     */
    protected Element peek(final String[] elementTypes) throws InvalidFormDefinitionException {

        Element element = currentElement;
        final List<String> elementTypesList = Arrays.asList(elementTypes);
        while (element.getParentNode() != null && element.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
            if (elementTypesList.contains(element.getNodeName())) {
                currentElement = element;
                return currentElement;
            }
            element = (Element) element.getParentNode();
        }
        if (elementTypesList.contains(element.getNodeName())) {
            currentElement = element;
            return currentElement;
        } else {
            throw new InvalidFormDefinitionException("No required element present among the parents of the current element.");
        }
    }

    /**
     * Retrieve the first element in the DOM whose type is among the element types provided
     * 
     * @param elementTypes
     *            array of required element types
     * @return the first {@link Element} in the stack whose type is among the element types provided
     * @throws InvalidFormDefinitionException
     *             if no element among the current element's parents has one of the required type
     */
    protected Element peekParentFirst(final String[] elementTypes) throws InvalidFormDefinitionException {

        Element element = currentElement;
        final List<String> elementTypesList = Arrays.asList(elementTypes);
        while (element.getParentNode() != null && element.getParentNode().getNodeType() == Node.ELEMENT_NODE) {
            element = (Element) element.getParentNode();
            if (elementTypesList.contains(element.getNodeName())) {
                currentElement = element;
                return currentElement;
            }
        }
        if (elementTypesList.contains(currentElement.getNodeName())) {
            return currentElement;
        } else {
            throw new InvalidFormDefinitionException("No required element present among the parents of the current element.");
        }
    }

    /**
     * Verify that an element/attribute value is not null
     * 
     * @param name
     * @param value
     * @throws InvalidFormDefinitionException
     */
    protected void checkArgNotNull(final String name, final Object value) throws InvalidFormDefinitionException {
        if (value == null) {
            final String errorMessage = "The property " + name + " shouldn't be null.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new InvalidFormDefinitionException(errorMessage);
        }
    }

    /**
     * Verify that an element/attribute value is not an empty string
     * 
     * @param name
     * @param value
     * @throws InvalidFormDefinitionException
     */
    protected void checkStringNotEmpty(final String name, final String value) throws InvalidFormDefinitionException {
        if (value != null && value.length() == 0) {
            final String errorMessage = "The property " + name + " shouldn't be empty.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage);
            }
            throw new InvalidFormDefinitionException(errorMessage);
        }
    }

}
