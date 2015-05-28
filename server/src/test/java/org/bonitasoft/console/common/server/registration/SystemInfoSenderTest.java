/**
 * Copyright (C) 2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.common.server.registration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SystemInfoSenderTest {

    String serviceURL = "http://some/url";

    @Mock
    OutputStreamWriter outputStreamWriter;

    @Mock
    BufferedReader bufferedReader;

    @Mock
    URLConnection urlConnection;

    @Spy
    SystemInfoSender systemInfoSender = new SystemInfoSender(serviceURL);

    @Test
    public void call_should_return_true_when_service_response_is_OK() throws Exception {
        when(systemInfoSender.createConnection(new URL(serviceURL))).thenReturn(urlConnection);
        when(urlConnection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(urlConnection.getInputStream()).thenReturn(mock(InputStream.class));
        doReturn(outputStreamWriter).when(systemInfoSender).createOutputStreamWriter(urlConnection);
        when(bufferedReader.readLine()).thenReturn("1");
        doReturn(bufferedReader).when(systemInfoSender).createBufferedReaderForResponse(urlConnection);

        assertThat(systemInfoSender.call("data", "email")).isTrue();

        verify(outputStreamWriter).write("email");
        verify(outputStreamWriter).write("data");
        verify(outputStreamWriter).flush();
    }

    @Test
    public void call_should_return_false_when_service_response_is_KO() throws Exception {
        when(systemInfoSender.createConnection(new URL(serviceURL))).thenReturn(urlConnection);
        when(urlConnection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(urlConnection.getInputStream()).thenReturn(mock(InputStream.class));
        doReturn(outputStreamWriter).when(systemInfoSender).createOutputStreamWriter(urlConnection);
        when(bufferedReader.readLine()).thenReturn(null);
        doReturn(bufferedReader).when(systemInfoSender).createBufferedReaderForResponse(urlConnection);

        assertThat(systemInfoSender.call("data", "email")).isFalse();

        verify(outputStreamWriter).write("email");
        verify(outputStreamWriter).write("data");
        verify(outputStreamWriter).flush();
    }

}
