/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.forms.server.api.impl;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.engine.bpm.process.ArchivedProcessInstance;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.forms.server.accessor.api.ExpressionEvaluatorEngineClient;
import org.bonitasoft.forms.server.accessor.api.ProcessInstanceAccessorEngineClient;
import org.bonitasoft.forms.server.accessor.api.utils.ProcessInstanceAccessor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProcessInstanceExpressionsEvaluatorTest {

    @Mock
    ExpressionEvaluatorEngineClient engineEvaluator;

    @Mock
    ProcessInstanceAccessorEngineClient processInstanceAccessor;

    ProcessInstanceExpressionsEvaluator evaluator;

    @Mock
    ArchivedProcessInstance archivedProcessInstance;

    @Mock
    ProcessInstanceAccessor processInstance;

    private Map<Expression, Map<String, Serializable>> someExpressions =
            Collections.<Expression, java.util.Map<String, Serializable>> emptyMap();

    @Before
    public void setUp() {
        initMocks(this);

        evaluator = new ProcessInstanceExpressionsEvaluator(engineEvaluator);
    }

    @Test
    public void testEvaluateExpressionForCurrentProcessInstance() throws Exception {
        when(processInstance.getId()).thenReturn(1L);
        when(processInstance.isArchived()).thenReturn(false);

        when(engineEvaluator.evaluateExpressionsOnProcessInstance(1L, someExpressions))
                .thenReturn(aKnownResultSet());

        Map<String, Serializable> evaluated = evaluator.evaluate(processInstance,
                someExpressions,
                false);

        assertEquals(aKnownResultSet(), evaluated);
    }

    @Test
    public void testEvaluateExpressionOnCompletedProcessInstance() throws Exception {
        when(processInstance.getId()).thenReturn(2L);
        when(processInstance.isArchived()).thenReturn(true);

        when(engineEvaluator.evaluateExpressionsOnCompletedProcessInstance(2L, someExpressions))
                .thenReturn(aKnownResultSet());

        Map<String, Serializable> evaluated = evaluator.evaluate(processInstance,
                someExpressions,
                false);

        assertEquals(aKnownResultSet(), evaluated);
    }

    @Test
    public void testEvaluateExpressionAtProcessInstantiation() throws Exception {
        when(processInstance.getId()).thenReturn(3L);
        when(processInstance.isArchived()).thenReturn(true);

        when(engineEvaluator.evaluateExpressionsAtProcessInstanciation(3L, someExpressions))
                .thenReturn(aKnownResultSet());

        Map<String, Serializable> evaluated = evaluator.evaluate(processInstance,
                someExpressions,
                true);

        assertEquals(aKnownResultSet(), evaluated);
    }

    private Map<String, Serializable> aKnownResultSet() {
        Map<String, Serializable> aKnownResultSet = new HashMap<String, Serializable>();
        aKnownResultSet.put("key", "value");
        return aKnownResultSet;
    }
}
