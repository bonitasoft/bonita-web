package org.bonitasoft.forms.server.api.impl;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.server.api.IFormExpressionsAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FormWorkflowAPIImplTest {

    private FormWorkflowAPIImpl formWorkflowAPIImpl;

    @Mock
    private APISession session;

    private final Locale locale = new Locale("en");

    @Mock
    private Map<String, Serializable> context;

    @Mock
    private List<FormAction> actions;

    @Mock
    private IFormExpressionsAPI formExpressionsAPI;

    @Mock
    private List<Expression> expressions;

    @Before
    public void setUp() throws Exception {
        formWorkflowAPIImpl = spy(new FormWorkflowAPIImpl());
        expressions = new ArrayList<Expression>();
    }

    @Test
    public void it_should_call_evaluateActivityInitialExpressions() throws Exception {
        // Given
        formWorkflowAPIImpl = spy(new FormWorkflowAPIImpl());
        final long processDefinitionID = -1;
        final long activityInstanceID = 1;
        expressions = new ArrayList<Expression>();
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
        verify(formExpressionsAPI, never()).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
    }

    @Test
    public void it_should_call_evaluateProcessInitialExpressions() throws Exception {
        // Given
        final long processDefinitionID = 1;
        final long activityInstanceID = -1;
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
        verify(formExpressionsAPI, never()).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
    }

    @Test
    public void it_should_not_call_evaluateProcessInitialExpressions_nor_evaluateActivityInitialExpressions() throws Exception {
        // Given
        final long processDefinitionID = -1;
        final long activityInstanceID = -1;
        // When
        formWorkflowAPIImpl.getEvaluateConditionExpressions(session, actions, locale, context,
                processDefinitionID, activityInstanceID, formExpressionsAPI);

        // Then
        verify(formExpressionsAPI, never()).evaluateProcessInitialExpressions(session, processDefinitionID, expressions, locale, context);
        verify(formExpressionsAPI, never()).evaluateActivityInitialExpressions(session, activityInstanceID, expressions, locale, true, context);
    }

}
