/*
 * Copyright (C) 2018 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 */
package org.bonitasoft.web.rest.server.api.bpm.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.SendEventException;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.web.rest.server.BonitaRestletApplication;
import org.bonitasoft.web.rest.server.utils.RestletTest;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.ServerResource;

/**
 * @author Emmanuel Duchastenier
 */
@RunWith(MockitoJUnitRunner.class)
public class BPMMessageResourceTest extends RestletTest {

    @Mock
    ProcessAPI processAPI;

    private BPMMessageResource restResource;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initializeMocks() {
        restResource = spy(new BPMMessageResource(processAPI));
    }

    @Override
    protected ServerResource configureResource() {
        return new BPMMessageResource(processAPI);
    }

    @Test
    public void sendMessage_should_call_engine_method_sendMessage() throws Exception {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("msg");
        bpmMessage.setTargetProcess("myProcess");
        bpmMessage.setTargetFlowNode("activity");

        // when:
        restResource.sendMessage(bpmMessage);

        // then:
        verify(processAPI).sendMessage("msg",
                new ExpressionBuilder().createConstantStringExpression("myProcess"),
                new ExpressionBuilder().createConstantStringExpression("activity"),
                new HashMap<>(), new HashMap<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void sendMessage_should_throw_exception_if_engine_throws_SendEventException() throws Exception {
        // given:
        doThrow(SendEventException.class).when(processAPI).sendMessage(anyString(), any(Expression.class),
                any(Expression.class), any(Map.class), any(Map.class));

        // when:
        restResource.sendMessage(new BPMMessage());
    }

    @Test
    public void sendMessage_should_throw_exception_if_messageName_is_not_set() {
        // given:
        final BPMMessage bpmMessage = new BPMMessage(); // message name not set
        bpmMessage.setTargetProcess("myProcess");
        bpmMessage.setTargetFlowNode("activity");

        // then:
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("messageName is mandatory");

        // when:
        restResource.sendMessage(bpmMessage);
    }

    @Test
    public void sendMessage_should_throw_exception_if_targetProcess_is_not_set() {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("my msg");
        bpmMessage.setTargetFlowNode("activity");

        // then:
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("targetProcess is mandatory");

        // when:
        restResource.sendMessage(bpmMessage);
    }

    @Test
    public void sendMessage_should_throw_exception_if_targetFlowNode_is_not_set() {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("my msg");
        bpmMessage.setTargetProcess("myProcess");

        // then:
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("targetFlowNode is mandatory");

        // when:
        restResource.sendMessage(bpmMessage);
    }

    @Test
    public void sendMessage_should_accept_primitive_types_in_message_content_values() throws Exception {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("msg");
        bpmMessage.setTargetProcess("myProcess");
        bpmMessage.setTargetFlowNode("activity");
        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("id", 123L);
        messageContent.put("name", "john");
        messageContent.put("amount", 1243.234d);
        messageContent.put("nbDay", 34);
        messageContent.put("validated", true);
        messageContent.put("rate", 3.0f);
        messageContent.put("expectedDate", "2018-09-09");
        messageContent.put("startDate", "2018-08-09T14:30:00");
        messageContent.put("globalEndDate", "2018-08-09T14:30:00+01:00");
        bpmMessage.setMessageContent(messageContent);

        // when:
        restResource.sendMessage(bpmMessage);

        // then:
        ArgumentCaptor<Map> msgContentCaptor = ArgumentCaptor.forClass(Map.class);

        verify(processAPI).sendMessage(eq("msg"),
                eq(expression("myProcess", "myProcess", String.class)),
                eq(expression("activity", "activity", String.class)),
                msgContentCaptor.capture(),
                eq(Collections.emptyMap()));
        
        assertThat(msgContentCaptor.getValue())
                .containsValue(expression("123", "123", Long.class))
                .containsValue(expression("john", "john", String.class))
                .containsValue(expression("1243.234", "1243.234", Double.class))
                .containsValue(expression("34", "34", Integer.class))
                .containsValue(expression("true", "true", Boolean.class))
                .containsValue(expression("3.0", "3.0", Float.class))
                .containsValue(expression("2018-09-09", "2018-09-09", LocalDate.class))
                .containsValue(expression("2018-08-09T14:30:00", "2018-08-09T14:30:00", LocalDateTime.class))
                .containsValue(
                        expression("2018-08-09T14:30:00+01:00", "2018-08-09T14:30:00+01:00", OffsetDateTime.class));
    }

    @Test
    public void sendMessage_should_accept_primitive_types_in_correlation_values() throws Exception {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("msg");
        bpmMessage.setTargetProcess("myProcess");
        bpmMessage.setTargetFlowNode("activity");
        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("id", 123L);
        messageContent.put("name", "john");
        bpmMessage.setCorrelations(messageContent);

        // when:
        restResource.sendMessage(bpmMessage);

        // then:
        ArgumentCaptor<Map> correlationsCaptor = ArgumentCaptor.forClass(Map.class);

        verify(processAPI).sendMessage(eq("msg"),
                eq(expression("myProcess", "myProcess", String.class)),
                eq(expression("activity", "activity", String.class)),
                eq(Collections.emptyMap()),
                correlationsCaptor.capture());

        assertThat(correlationsCaptor.getValue())
                .containsValue(expression("123", "123", Long.class))
                .containsValue(expression("john", "john", String.class));
    }

    @Test
    public void sendMessage_should_throw_exception_if_messageContent_has_unsupported_value_type() throws Exception {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("msg");
        bpmMessage.setTargetProcess("myProcess");
        bpmMessage.setTargetFlowNode("activity");
        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("array", new String[] { "a", "b" });
        bpmMessage.setMessageContent(messageContent);

        // then:
        expectedException.expect(APIException.class);
        expectedException.expectMessage(
                "BPM send message: unsupported value type 'java.lang.String[]' for key 'array'. Only primitive types are supported.");

        // when:
        restResource.sendMessage(bpmMessage);
    }

    @Test
    public void sendMessage_should_throw_exception_if_there_is_more_than_5_correlations() throws Exception {
        // given:
        final BPMMessage bpmMessage = new BPMMessage();
        bpmMessage.setMessageName("msg");
        bpmMessage.setTargetProcess("myProcess");
        bpmMessage.setTargetFlowNode("activity");
        Map<String, Object> messageContent = new HashMap<>();
        messageContent.put("k1", "1");
        messageContent.put("k2", "1");
        messageContent.put("k3", "1");
        messageContent.put("k4", "1");
        messageContent.put("k5", "1");
        messageContent.put("k6", "1");
        bpmMessage.setCorrelations(messageContent);

        // then:
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("A maximum of 5 correlations is supported. 6 found.");

        // when:
        restResource.sendMessage(bpmMessage);
    }

    @Test
    public void should_post_request_return_400_status_when_mandatory_attribute_is_missing() throws Exception {
        Response response = request(BonitaRestletApplication.BPM_MESSAGE_URL)
                .post("{\"messageName\": \"message\", \"targetProcess\": \"myProcess\" }");

        assertThat(response.getStatus()).isEqualTo(Status.CLIENT_ERROR_BAD_REQUEST);
    }

    @Test
    public void should_post_request_return_201_status() throws Exception {
        Response response = request(BonitaRestletApplication.BPM_MESSAGE_URL)
                .post("{\"messageName\": \"message\", \"targetProcess\": \"myProcess\", \"targetFlowNode\": \"wait\", \"messageContent\": { \"id\": 12 } }");

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
    }

    @Test
    public void should_support_date_type() throws Exception {
        Response response = request(BonitaRestletApplication.BPM_MESSAGE_URL)
                .post("{\"messageName\": \"message\", \"targetProcess\": \"myProcess\", \"targetFlowNode\": \"wait\", \"messageContent\": { \"updated\": \"2012-04-23T18:25:43.511Z\" } }");

        assertThat(response.getStatus()).isEqualTo(Status.SUCCESS_NO_CONTENT);
    }

    private Expression expression(String name, String content, Class<?> returnType) throws InvalidExpressionException {
        return new ExpressionBuilder().createExpression(name, content, returnType.getName(), ExpressionType.TYPE_CONSTANT);
    }

}
