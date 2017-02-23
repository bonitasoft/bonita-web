package org.bonitasoft.web.toolkit.client.common.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.bonitasoft.web.toolkit.client.common.json.JSonUtil.escape;

import org.junit.Test;

public class JSonUtilTest {

    @Test
    public void should_escape_unsafe_html_characters() {
        assertThat(escape("<script>alert('bad')</script>"))
                .isEqualTo("\\u003cscript\\u003ealert(\\u0027bad\\u0027)\\u003c\\/script\\u003e");
    }
}