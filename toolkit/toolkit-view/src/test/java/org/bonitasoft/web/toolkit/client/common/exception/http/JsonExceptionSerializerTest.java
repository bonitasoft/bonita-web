/*
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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

package org.bonitasoft.web.toolkit.client.common.exception.http;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.i18n.I18nHook;
import org.junit.Test;

import java.util.Arrays;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by Vincent Elcrin
 * Date: 23/09/13
 * Time: 18:12
 */
public class JsonExceptionSerializerTest {

    I18nHook i18nHook = new I18nHook();

    @Test
    public void testWeCanConvertExceptionWithoutMessage() throws Exception {
        HttpException exception = new HttpException();
        JsonExceptionSerializer serializer = new JsonExceptionSerializer(exception);

        String json = serializer.end();

        assertEquals(exception, json);
    }

//    @Test
//    public void testWeCanConvertExceptionWithCause() throws Exception {
//        HttpException exception = new HttpException();
//        HttpException cause = new HttpException();
//        exception.initCause(cause);
//        JsonExceptionSerializer serializer = new JsonExceptionSerializer(exception);
//
//        String json = serializer.end();
//
//        assertEquals(exception, json);
//    }

    @Test
    public void testWeCanConvertExceptionWithMessageToJson() throws Exception {
        HttpException exception = new HttpException("message");
        JsonExceptionSerializer serializer = new JsonExceptionSerializer(exception);

        String json = serializer.end();

        assertEquals(exception, json);
    }

    @Test
    public void testWeCanAppendNewAttributeBeforeEndCall() throws Exception {
        HttpException exception = new HttpException();
        JsonExceptionSerializer serializer = new JsonExceptionSerializer(exception);

        String json = serializer
                .appendAttribute("attributeKey", "attributeValue")
                .end();

        assertThat(json, equalTo(
                "{\"exception\":\"" + exception.getClass().toString() + "\"," +
                        "\"message\":\"" + exception.getMessage() + "\"," +
                        "\"stacktrace\":\"" + Arrays.toString(exception.getStackTrace()) + "\"," +
                        "\"attributeKey\":\"attributeValue\"}"));
    }

    @Test
    public void testJsonContainsInternationalizationWhenLocalIsSet() throws Exception {
        APIException exception = new APIException("message");
        i18nHook.setL10n("localization");
        exception.setLocale(LOCALE.en);
        JsonExceptionSerializer serializer = new JsonExceptionSerializer(exception);

        String json = serializer.end();

        assertThat(json, equalTo(
                "{\"exception\":\"" + exception.getClass().toString() + "\"," +
                        "\"message\":\"" + exception.getMessage() + "\"," +
                        "\"localizedmessage\":\"localization\"," +
                        "\"stacktrace\":\"" + Arrays.toString(exception.getStackTrace()) + "\"}"));
    }

    @Test
    public void testJsonDoestContainsInternationalizationWhenIdenticalToMessage() throws Exception {
        APIException exception = new APIException("identicalMessage");
        JsonExceptionSerializer serializer = new JsonExceptionSerializer(exception);
        i18nHook.setL10n("identicalMessage");
        exception.setLocale(LOCALE.en);

        String json = serializer.end();

        assertEquals(exception, json);

    }

    private void assertEquals(Exception e, String json) {
        assertThat(json, equalTo("{\"exception\":\"" + e.getClass().toString() + "\"," +
                "\"message\":\"" + e.getMessage() + "\"," +
                "\"stacktrace\":\"" + Arrays.toString(e.getStackTrace()) + "\"}"));
    }

}
