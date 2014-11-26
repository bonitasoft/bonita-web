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

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.operation.LeftOperand;
import org.bonitasoft.forms.client.model.ActionType;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FileWidgetInputType;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.server.accessor.impl.XMLApplicationConfigDefAccessorImpl;
import org.bonitasoft.forms.server.accessor.impl.XMLApplicationFormDefAccessorImpl;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 * Test for the implementation of the form builder
 *
 * @author Anthony Birembaut
 */
public class FormBuilderImplTest {

    public final static String GROOVY = "GROOVY";

    public final static String JAVASCRIPT = "JAVASCRIPT";

    public final static String TYPE_INPUT = "TYPE_INPUT";

    private FormBuilderImpl formBuilder = spy(formBuilder = new FormBuilderImpl());

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testGenerateSimpleFormXML() throws Exception {
        doReturn(new File(System.getProperty("java.io.tmpdir"))).when(formBuilder).getTempFolder();
        formBuilder.createFormDefinition();
        formBuilder.addApplication("processName", "1.0");
        formBuilder.addLabelExpression("addLabel", "process label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/process-template.html");
        Assert.assertNotNull(formBuilder.done());
    }

    private File buildComplexFormXML() throws Exception {
        doReturn(new File(System.getProperty("java.io.tmpdir"))).when(formBuilder).getTempFolder();
        formBuilder.createFormDefinition();
        formBuilder.addApplication("processName", "1.0");
        formBuilder.addLabelExpression("addLabel", "process label with accents éèà", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/process-template.html");
        formBuilder.addPermissions("application#test");

        formBuilder.addEntryForm("processName--1.0$entry");
        formBuilder.addPermissions("process#processName--1.0");
        formBuilder.addPage("processPage1");
        formBuilder.addLabelExpression("addLabel", "page1 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/process-page1-template.html");
        formBuilder.addWidget("processpage1widget1", WidgetType.TEXTBOX);
        formBuilder.addAllowHTMLInFieldBehavior(true);
        formBuilder.addLabelExpression("addLabel", "page1 widget1 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addAllowHTMLInLabelBehavior(true);
        formBuilder.addHTMLAttribute("readonly", "readonly");
        formBuilder.addLabelPosition(ItemPosition.RIGHT);
        formBuilder.addWidget("processpage1widget2", WidgetType.CHECKBOX);
        formBuilder.addLabelExpression("addLabel", "page1 widget2 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);

        formBuilder.addPage("processPage2");
        formBuilder.addLabelExpression("addLabel", "page2 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/process-page2-template.html");
        formBuilder.addValidator("processpage2validator1", "classname", null, null);
        formBuilder.addLabelExpression(null, "invalid page fields", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValidator("processpage2validator2", "classname", null, null);
        formBuilder.addLabelExpression(null, "invalid page fields", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addParameterExpression(null, "validator parameter", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);

        formBuilder.addWidget("processpage2widget1", WidgetType.MESSAGE);
        formBuilder.addLabelExpression("addLabel", "page2 widget1 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addWidget("processpage2widget2", WidgetType.BUTTON_SUBMIT);
        formBuilder.addLabelExpression("addLabel", "page2 widget2 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addWidget("processpage2widget3", WidgetType.FILEUPLOAD);
        formBuilder.addLabelExpression("addLabel", "page2 widget3 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addFileWidgetInputType(FileWidgetInputType.FILE);
        formBuilder.addAttachmentImageBehavior(true);
        formBuilder.addWidget("processpage2widget4", WidgetType.FILEUPLOAD);
        formBuilder.addFileWidgetInputType(FileWidgetInputType.URL);
        formBuilder.addWidget("processpage2widget5", WidgetType.FILEUPLOAD);
        formBuilder.addFileWidgetInputType(FileWidgetInputType.ALL);
        formBuilder.addWidget("processpage2widget6", WidgetType.TABLE);
        formBuilder.addCellsStyle("table-cellStyle");
        formBuilder.addHeadingsStyle("table-headings-cellStyle", true, true, false, false);

        formBuilder.addAction(ActionType.ASSIGNMENT, "variable1", LeftOperand.TYPE_DATA, "=", null, "button1");
        formBuilder.addActionExpression(null, "field_processpage1widget1", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(), GROOVY);
        formBuilder.addAction(ActionType.ASSIGNMENT, "variable2", LeftOperand.TYPE_TRANSIENT_DATA, "=", null, "button1");
        formBuilder.addActionExpression(null, "field_processpage1widget2", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(), GROOVY);
        formBuilder.addAction(ActionType.EXECUTE_CONNECTOR, "variable2", LeftOperand.TYPE_DATA, "=", null, "button1");
        formBuilder.addActionExpression(null, "field_processpage1widget2", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(), String.class.getName(), GROOVY);

        formBuilder.addConfirmationLayout("/process-confirmation-template.html");
        formBuilder.addConfirmationMessageExpression("ConfirmationMessage", "${\"confirmation message\"}", ExpressionType.TYPE_READ_ONLY_SCRIPT.name(),
                String.class.getName(), GROOVY);

        formBuilder.addEntryForm("processName--1.0--1--request$entry");
        formBuilder.addPermissions("process#processName--1.0");
        formBuilder.addTransientData("transientData", Boolean.class.getName());
        formBuilder.addTransientDataExpression(null, "true", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addPage("activitypage1");
        formBuilder.addLabelExpression("addLabel", "activitypage1 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/activity-page1-template.html");
        formBuilder.addWidget("activitypage1widget1", WidgetType.LISTBOX_SIMPLE);
        formBuilder.addAvailableValue();
        formBuilder.addLabelExpression("addLabel", "available value 1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValueExpression("addValue", "availablevalue1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addAvailableValue();
        formBuilder.addLabelExpression("addLabel", "available value 2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValueExpression("addValue", "availablevalue2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addItemsStyle("items_class_names");
        formBuilder.addWidget("activitypage1widget2", WidgetType.PASSWORD);
        formBuilder.addValidator("activitywidget2validator1", "classname", null, null);
        formBuilder.addLabelExpression(null, "activity widget2 validator1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValidator("activitywidget2validator2", "classname", null, null);
        formBuilder.addLabelExpression(null, "activity widget2 validator2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);

        formBuilder.addEntryForm("processName--1.0--1--validate$entry");
        formBuilder.addPermissions("process#processName--1.0");
        formBuilder.addTransientData("transientData", Boolean.class.getName());
        formBuilder.addTransientDataExpression(null, "true", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addPage("activitypage1");
        formBuilder.addLabelExpression("addLabel", "activitypage1 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/activity-page1-template.html");
        formBuilder.addWidget("activitypage1widget1", WidgetType.LISTBOX_SIMPLE);
        formBuilder.addAvailableValue();
        formBuilder.addLabelExpression("addLabel", "available value 1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValueExpression("addValue", "availablevalue1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addAvailableValue();
        formBuilder.addLabelExpression("addLabel", "available value 2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValueExpression("addValue", "availablevalue2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addItemsStyle("items_class_names");
        formBuilder.addWidget("activitypage1widget2", WidgetType.PASSWORD);
        formBuilder.addValidator("activitywidget2validator1", "classname", null, null);
        formBuilder.addLabelExpression(null, "activity widget2 validator1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addValidator("activitywidget2validator2", "classname", null, null);
        formBuilder.addLabelExpression(null, "activity widget2 validator2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);

        formBuilder.addPage("activitypage2");
        formBuilder.addLabelExpression("addLabel", "activitypage2 label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/activity-page2-template.html");
        formBuilder.addWidget("activitypage2widget1", WidgetType.TEXTAREA);
        formBuilder.addLabelExpression("addLabel", "activity page2 widget1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addMaxLength(30);
        formBuilder.addMaxHeight(3);
        formBuilder.addMandatoryBehavior(true);
        formBuilder.addStyle("css_class");
        formBuilder.addTitleExpression("addTitle", "title", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addInitialValueExpression("addInitialValue", "initial\nvalue", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLabelStyle("label_css_class");
        formBuilder.addFieldOutputType(Integer.class.toString());
        formBuilder.addWidget("activitypage2widget2", WidgetType.DATE);
        formBuilder.addInitialValueExpression("addInitialValue", "10-01-2009", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addDisplayFormat("mm/dd/yyyy");

        formBuilder.addWidget("activitypage2widget2", WidgetType.EDITABLE_GRID);
        formBuilder.addInitialValueExpression("ListOfList", "ListOfList", ExpressionType.TYPE_LIST.name(), List.class.getName(), null);
        formBuilder.addDependentExpression("row1", "row1", ExpressionType.TYPE_LIST.name(), List.class.getName(), null);
        formBuilder.addDependentExpression("column1", "row1column1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null, false);
        formBuilder.addDependentExpression("column2", "row1column2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.endExpressionDependencies();
        formBuilder.addDependentExpression("row2", "row2", ExpressionType.TYPE_LIST.name(), List.class.getName(), null);
        formBuilder.addDependentExpression("column1", "row2column1", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null, false);
        formBuilder.addDependentExpression("column2", "row2column2", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.endExpressionDependencies();

        formBuilder.addConfirmationLayout("/activity-confirmation-template.html");
        formBuilder.addConfirmationMessageExpression("ConfirmationMessage", "${\"confirmation message\"}", ExpressionType.TYPE_CONSTANT.name(),
                String.class.getName(), GROOVY);
        formBuilder.addErrorTemplate("/process-error-template.html");

        formBuilder.addMandatorySymbolExpression(null, "!", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addMandatoryLabelExpression(null, "must be set", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null);
        formBuilder.addMandatoryStyle("mandatory_style");

        return formBuilder.done();
    }

    @Test
    public void testGenerateComplexFormXML() throws Exception {
        Assert.assertNotNull(buildComplexFormXML());
    }

    private static String toString(final Document doc) {
        try {
            final StringWriter sw = new StringWriter();
            final TransformerFactory tf = TransformerFactory.newInstance();
            final Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (final Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    @Test
    public void testGenerateAndInterpreteComplexFormXML() throws Exception {
        final File complexProcessDefinition = buildComplexFormXML();
        final InputStream inputStream = new FileInputStream(complexProcessDefinition);
        final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        final Document document = builder.parse(inputStream);
        final String formXMLContent = toString(document);
        inputStream.close();
        final XMLApplicationConfigDefAccessorImpl applicationConfigDefAccessor = new XMLApplicationConfigDefAccessorImpl(1, document);
        Assert.assertEquals(FormBuilderImpl.PRODUCT_VERSION, applicationConfigDefAccessor.getMigrationProductVersion());
        Assert.assertEquals("process label with accents éèà", applicationConfigDefAccessor.getApplicationLabelExpression().getContent());
        Assert.assertEquals("/process-template.html", applicationConfigDefAccessor.getApplicationLayout());
        Assert.assertEquals("/process-error-template.html", applicationConfigDefAccessor.getApplicationErrorTemplate());
        Assert.assertEquals("!", applicationConfigDefAccessor.getApplicationMandatorySymbolExpression().getContent());
        Assert.assertEquals("must be set", applicationConfigDefAccessor.getApplicationMandatoryLabelExpression().getContent());
        Assert.assertEquals("mandatory_style", applicationConfigDefAccessor.getApplicationMandatorySymbolStyle());
        Assert.assertEquals("application#test", applicationConfigDefAccessor.getApplicationPermissions());

        XMLApplicationFormDefAccessorImpl applicationFormDefAccessor = new XMLApplicationFormDefAccessorImpl(1, document, "processName--1.0$entry", null, null);
        Assert.assertEquals("process#processName--1.0", applicationFormDefAccessor.getFormPermissions());
        Assert.assertEquals("page1 label", applicationFormDefAccessor.getPageLabelExpression("processPage1").getContent());
        Assert.assertEquals("/process-page1-template.html", applicationFormDefAccessor.getFormPageLayout("processPage1"));
        Assert.assertEquals(2, applicationFormDefAccessor.getPageWidgets("processPage1").size());
        final FormWidget textBoxWidget = applicationFormDefAccessor.getPageWidgets("processPage1").get(0);
        Assert.assertTrue(textBoxWidget.allowHTMLInLabel());
        Assert.assertTrue(textBoxWidget.allowHTMLInField());
        Assert.assertEquals(ItemPosition.RIGHT, textBoxWidget.getLabelPosition());
        Assert.assertEquals(3, applicationFormDefAccessor.getActions("processPage2").size());
        final FormAction action = applicationFormDefAccessor.getActions("processPage2").get(0);
        Assert.assertEquals(ActionType.ASSIGNMENT, action.getType());
        Assert.assertEquals("variable1", action.getVariableName());
        Assert.assertEquals(LeftOperand.TYPE_DATA, action.getVariableType());
        Assert.assertEquals("=", action.getOperator());
        Assert.assertEquals("field_processpage1widget1", action.getExpression().getContent());
        final FormAction action2 = applicationFormDefAccessor.getActions("processPage2").get(1);
        Assert.assertEquals(ActionType.ASSIGNMENT, action2.getType());
        Assert.assertEquals("variable2", action2.getVariableName());
        Assert.assertEquals(LeftOperand.TYPE_TRANSIENT_DATA, action2.getVariableType());
        Assert.assertEquals(2, applicationFormDefAccessor.getPageValidators("processPage2").size());
        final FormValidator validator = applicationFormDefAccessor.getPageValidators("processPage2").get(0);
        Assert.assertEquals("processpage2validator1", validator.getId());
        Assert.assertEquals("invalid page fields", validator.getLabelExpression().getContent());
        Assert.assertEquals("classname", validator.getValidatorClass());
        Assert.assertNull(validator.getStyle());
        final FormWidget uploadWidget = applicationFormDefAccessor.getPageWidgets("processPage2").get(2);
        Assert.assertTrue(uploadWidget.isDisplayAttachmentImage());
        Assert.assertTrue(FileWidgetInputType.FILE.equals(uploadWidget.getFileWidgetInputType()));
        final FormWidget uploadWidget2 = applicationFormDefAccessor.getPageWidgets("processPage2").get(3);
        Assert.assertTrue(FileWidgetInputType.URL.equals(uploadWidget2.getFileWidgetInputType()));
        final FormWidget uploadWidget3 = applicationFormDefAccessor.getPageWidgets("processPage2").get(4);
        Assert.assertTrue(FileWidgetInputType.ALL.equals(uploadWidget3.getFileWidgetInputType()));
        final FormWidget tableWidget = applicationFormDefAccessor.getPageWidgets("processPage2").get(5);
        Assert.assertEquals("table-cellStyle", tableWidget.getCellsStyle());
        Assert.assertEquals("table-headings-cellStyle", tableWidget.getHeadingsStyle());
        Assert.assertTrue(tableWidget.hasLeftHeadings());
        Assert.assertTrue(tableWidget.hasTopHeadings());
        Assert.assertFalse(tableWidget.hasRightHeadings());
        Assert.assertFalse(tableWidget.hasBottomHeadings());

        final XMLApplicationFormDefAccessorImpl applicationFormDefAccessorActivity = new XMLApplicationFormDefAccessorImpl(1, document,
                "processName--1.0--1--validate$entry", null, null);
        final FormWidget gridWidget = applicationFormDefAccessorActivity.getPageWidgets("activitypage2").get(2);
        final Expression gridInitialValueExpression = gridWidget.getInitialValueExpression();
        Assert.assertEquals(ExpressionType.TYPE_LIST.name(), gridInitialValueExpression.getExpressionType());
        final List<Expression> listOfListExpression = gridInitialValueExpression.getDependencies();
        Assert.assertEquals(2, listOfListExpression.size());
        final Expression row1Expression = listOfListExpression.get(0);
        final List<Expression> row1tExpressions = row1Expression.getDependencies();
        Assert.assertEquals(2, row1tExpressions.size());
        final Expression row2Expression = listOfListExpression.get(1);
        final List<Expression> row2Expressions = row2Expression.getDependencies();
        Assert.assertEquals(2, row2Expressions.size());

        applicationFormDefAccessor = new XMLApplicationFormDefAccessorImpl(1000, document, "processName--1.0--1--validate$entry", null, null);
        final List<TransientData> transientData = applicationFormDefAccessor.getTransientData();
        final TransientData transientData1 = transientData.get(0);
        Assert.assertEquals(Boolean.class.getName(), transientData1.getClassname());
        Assert.assertEquals("transientData", transientData1.getName());
    }

    @Test(expected = InvalidFormDefinitionException.class)
    public void testWrongHierarchyFormDefinition() throws Exception {
        formBuilder.createFormDefinition();
        formBuilder.addApplication("processName", "1.0");
        formBuilder.addLabelExpression("addLabel", "process label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("/process-template.html");
        formBuilder.addWidget("widget1", WidgetType.BUTTON_SUBMIT);
    }

    @Test(expected = InvalidFormDefinitionException.class)
    public void testNullValuesFormDefinition() throws Exception {
        formBuilder.createFormDefinition();
        formBuilder.addApplication("processName", "1.0");
        formBuilder.addLabelExpression("addLabel", "process label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout(null);
    }

    @Test(expected = InvalidFormDefinitionException.class)
    public void testEmptyValuesFormDefinition() throws Exception {
        formBuilder.createFormDefinition();
        formBuilder.addApplication("processName", "1.0");
        formBuilder.addLabelExpression("addLabel", "process label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), GROOVY);
        formBuilder.addLayout("");
    }
}
