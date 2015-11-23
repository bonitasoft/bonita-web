/*******************************************************************************
 * Copyright (C) 2014 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.utils;


import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Objects;
import org.restlet.Response;
import org.restlet.data.Header;
import org.restlet.data.Status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseAssert extends AbstractAssert<ResponseAssert, Response> {

    protected ResponseAssert(Response actual) {
        super(actual, ResponseAssert.class);
    }

    public static ResponseAssert assertThat(Response actual) {
        return new ResponseAssert(actual);
    }

    public ResponseAssert hasJsonEntityEqualTo(String json) {
        info.description("Response entity is not matching. WARNING This might introduce an API break");
        Objects.instance().assertEqual(info, getJson(actual.getEntityAsText()), getJson(json));
        return this;
    }

    private JsonNode getJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(json);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public ResponseAssert hasStatus(int status) {
        Objects.instance().assertEqual(info, actual.getStatus().getCode(), status);
        return this;
    }

    public ResponseAssert hasStatus(Status status) {
        Objects.instance().assertEqual(info, actual.getStatus(), status);
        return this;
    }

    public ResponseAssert hasHeader(Header header) {
        Objects.instance().assertIsIn(info, header, actual.getHeaders());
        return this;
    }
}