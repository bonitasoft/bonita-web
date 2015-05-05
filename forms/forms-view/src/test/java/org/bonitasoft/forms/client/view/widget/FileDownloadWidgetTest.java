package org.bonitasoft.forms.client.view.widget;

import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.forms.client.view.SupportedFieldTypes;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;

@RunWith(GwtMockitoTestRunner.class)
public class FileDownloadWidgetTest {

    private FileDownloadWidget fileDownloadWidget;

    @GwtMock
    Anchor anchor;

    @Test
    public void should_ImageServletURL_be_initialized() {

        final Map<String, Object> contextMap = new HashMap<String, Object>();
        fileDownloadWidget = new FileDownloadWidget("formID", contextMap, SupportedFieldTypes.JAVA_STRING_CLASSNAME, 1L, true);

        assertNotNull(fileDownloadWidget.attachmentServletURL);
        assertNotNull(fileDownloadWidget.imageServletURL);
    }
}
