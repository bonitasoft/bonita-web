package org.bonitasoft.web.toolkit.client.ui.html;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Vincent Elcrin
 * Date: 11/10/13
 * Time: 14:21
 */
public class VariableUtilsTest {

    @Mock
    private HtmlAccessor accessor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void testWeCanReplaceASpecificVariable() throws Exception {
        doReturn("<p>This is %variable1% for variable1 but not for variable2</p>")
                .when(accessor)
                .getInnerHTML();

        VariableUtils.inject(accessor, "variable1", SafeHtmlUtils.fromTrustedString("working"));

        verify(accessor).setInnerHTML("<p>This is working for variable1 but not for variable2</p>");
    }

    @Test
    public void testWeHtmlWithoutVariableStayTheSame() throws Exception {
        doReturn("<p class='aside'>This is a test</p>")
                .when(accessor)
                .getInnerHTML();

        VariableUtils.inject(accessor, "variable", SafeHtmlUtils.fromTrustedString("value"));

        verify(accessor).setInnerHTML("<p class='aside'>This is a test</p>");
    }

    @Test
    public void testWeCanReplaceMultipleVariables() throws Exception {
        doReturn("<p class='%variable%'>This is %variable%</p>")
                .when(accessor)
                .getInnerHTML();

        VariableUtils.inject(accessor, "variable", SafeHtmlUtils.fromTrustedString("working"));

        verify(accessor).setInnerHTML("<p class='working'>This is working</p>");
    }
}
