package org.bonitasoft.console.common.server.login.filter;

import static org.mockito.Mockito.doReturn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.filters.StringInputStream;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MultiReadHttpServletRequestTest {

    @Mock
    HttpServletRequest request;

    @Test
    public void should_getInputStream_work_when_called_twice() throws Exception {

        ServletInputStream fakeInputStream = null;
        try {
            fakeInputStream = new FakeServletInputStream();
            doReturn(fakeInputStream).when(request).getInputStream();
            final MultiReadHttpServletRequest multiReadHttpServletRequest = new MultiReadHttpServletRequest(request);

            final InputStream inputStream = multiReadHttpServletRequest.getInputStream();
            Assert.assertEquals("body content", IOUtils.toString(inputStream));

            final InputStream inputStream2 = multiReadHttpServletRequest.getInputStream();
            Assert.assertEquals("body content", IOUtils.toString(inputStream2));
        } finally {
            if (fakeInputStream != null) {
                fakeInputStream.close();
            }
        }
    }

    @Test
    public void should_getReader_work_when_called_twice() throws Exception {

        ServletInputStream fakeInputStream = null;
        try {
            fakeInputStream = new FakeServletInputStream();
            doReturn(fakeInputStream).when(request).getInputStream();
            final MultiReadHttpServletRequest multiReadHttpServletRequest = new MultiReadHttpServletRequest(request);

            final BufferedReader bufferedReader = multiReadHttpServletRequest.getReader();
            Assert.assertEquals("body content", IOUtils.toString(bufferedReader));

            final BufferedReader bufferedReader2 = multiReadHttpServletRequest.getReader();
            Assert.assertEquals("body content", IOUtils.toString(bufferedReader2));
        } finally {
            if (fakeInputStream != null) {
                fakeInputStream.close();
            }
        }
    }

    class FakeServletInputStream extends ServletInputStream {

        private final StringInputStream inputStream = new StringInputStream("body content");

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public int read(final byte[] b) throws IOException {
            return inputStream.read(b);
        }

        @Override
        public void close() throws IOException {
            inputStream.close();
            super.close();
        }

    }
}
