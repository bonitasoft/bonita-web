package org.bonitasoft.forms.server.accessor.widget.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xerces.impl.xs.opti.DefaultNode;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.server.constants.XMLForms;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@RunWith(MockitoJUnitRunner.class)
public class XMLWidgetBuilderImplTest {

    @Spy
    @InjectMocks
    XMLWidgetBuilderImpl xmlWidgetBuilderImpl;

    private Document document;
    DocumentBuilderFactory factory;

    @Before
    public void setUp() {
        //initMocks(this);

        factory = DocumentBuilderFactory.newInstance();
    }

    @Test
    public void shouldParseWidgetReturnThePathIfTheWidgetTypeIsFileDownloadTest() throws Exception {
        //given
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();
        Element e = document.createElement("el");
        Element initialValue = document.createElement(XMLForms.INITIAL_VALUE);

        doReturn("style").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.STYLE));
        doReturn("myId").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq("@" + XMLForms.ID));
        doReturn(WidgetType.FILEDOWNLOAD.toString()).when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq("@" + XMLForms.TYPE));
        doReturn(new DefaultNode()).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.MAX_LENGTH));
        doReturn(new DefaultNode()).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.MAX_HEIGHT));
        doReturn("labelstyle").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.LABEL_STYLE));
        doReturn("myClass").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.CLASSNAME));
        doReturn("labelStyle").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.LABEL_STYLE));
        doReturn("myClass").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.INPUT_STYLE));
        doReturn(new DefaultNode()).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.LABEL_POSITION));
        doReturn(false).when(xmlWidgetBuilderImpl).getBooleanValue(any(Node.class));
        doReturn("myOutputType").when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.FIELD_OUTPUT_TYPE));
        doReturn(100).when(xmlWidgetBuilderImpl).getIntValue(any(Node.class));
        doReturn(ItemPosition.TOP).when(xmlWidgetBuilderImpl).getItemPositionValue(any(Node.class));
        doReturn(ItemPosition.TOP).when(xmlWidgetBuilderImpl).getItemPositionValue(any(Node.class));

        doReturn(new DefaultNode()).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.MANDATORY));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.LABEL));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.TITLE));
        doReturn(initialValue).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.INITIAL_VALUE));
        doReturn(e).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.RESOURCE));
        doReturn("myFile.txt").when(xmlWidgetBuilderImpl).getStringByXpath(eq(e), eq(XMLForms.PATH));

        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.LABEL_BUTTON));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.ITEMS_STYLE));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeListByXpath(any(Node.class), eq(XMLForms.ITEMS_STYLE));
        doReturn(null).when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.ITEMS_STYLE));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.AVAILABLE_VALUES));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.EXPRESSION));
        doReturn(e.getChildNodes()).when(xmlWidgetBuilderImpl).getNodeListByXpath(any(Node.class), eq(XMLForms.VALIDATORS + "/" + XMLForms.VALIDATOR));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.DISPLAY_CONDITION));
        doReturn(null).when(xmlWidgetBuilderImpl).getStringByXpath(any(Node.class), eq(XMLForms.DISPLAY_FORMAT));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.DISPLAY_ATTACHMENT_IMAGE));

        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.ALLOW_HTML_IN_LABEL));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.ALLOW_HTML_IN_FIELD));
        doReturn(e.getChildNodes()).when(xmlWidgetBuilderImpl)
                .getNodeListByXpath(any(Node.class), eq(XMLForms.HTML_ATTRIBUTES + "/" + XMLForms.HTML_ATTRIBUTE));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.MAX_ITEMS));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.DELAY_MILLIS));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.POPUP_TOOLTIP));
        doReturn(null).when(xmlWidgetBuilderImpl).getNodeByXpath(any(Node.class), eq(XMLForms.SUB_TITLE));

        //when
        Node node = new DefaultNode();
        FormWidget formWidget = xmlWidgetBuilderImpl.parseWidget(node, false);

        //then
        assertThat("wrong path file", formWidget.getFilePaths().equals("myFile.txt"));

    }
}
