/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 **/
package org.bonitasoft.forms.server.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.DataFieldDefinition;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.InitialAttachment;
import org.bonitasoft.forms.server.exception.FileTooBigException;

/**
 * API dealing with expression evaluation and execution
 * 
 * @author Anthony Birembaut, Zhiheng Yang
 * 
 */
public interface IFormExpressionsAPI {

    /**
     * prefixes for fields values in the expressions
     */
    String FIELDID_PREFIX = "field_";

    String USER_LOCALE = "locale";

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 

     */
    Serializable evaluateActivityInitialExpression(APISession session, long activityInstanceID, Expression expression, Locale locale, boolean isCurrentValue)
            throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the expression
     * @param locale
     *            the user's locale
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Serializable evaluateInstanceInitialExpression(APISession osession, long processInstanceID, Expression expression, Locale locale, boolean isCurrentValue)
            throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the expression
     * @param locale
     *            the user's locale
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Serializable evaluateProcessInitialExpression(APISession session, long processDefinitionID, Expression expression, Locale locale)
            throws BPMExpressionEvaluationException, InvalidSessionException,
            BPMExpressionEvaluationException, BPMEngineException;

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException 
     * @throws BPMEngineException 
     */
    Serializable evaluateActivityInitialExpression(APISession session, long activityInstanceID, Expression expression, Locale locale, boolean isCurrentValue,
            Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the expression
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Serializable evaluateInstanceInitialExpression(APISession session, long processInstanceID, Expression expression, Locale locale, boolean isCurrentValue,
            Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the expression
     * @param locale
     *            the user's locale
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Serializable evaluateProcessInitialExpression(APISession session, long processDefinitionID, Expression expression, Locale locale,
            Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the expression
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at step end
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Serializable evaluateActivityExpression(APISession session, long activityInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the expression
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Serializable evaluateInstanceExpression(APISession session, long processInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException;

    /**
     * Evaluate an action expression (at form submission)
     * 
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the expression
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Serializable evaluateProcessExpression(APISession session, long processDefinitionID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param activityInstanceID
     *            the activity instance ID
     * @param expression
     *            the expression
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Serializable evaluateActivityExpression(APISession session, long activityInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException,
            IOException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param processInstanceID
     *            the process instance ID
     * @param expression
     *            the expression
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, value returned is the current value for the instance. otherwise, it's the value at instantiation
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Serializable evaluateInstanceExpression(APISession session, long processInstanceID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException,
            IOException, BPMEngineException;

    /**
     * Evaluate an action expression (at form submission)
     * 
     * @param processDefinitionID
     *            the process definition ID
     * @param expression
     *            the expression
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluation
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Serializable evaluateProcessExpression(APISession session, long processDefinitionID, Expression expression, Map<String, FormFieldValue> fieldValues,
            Locale locale, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException;

    /**
     * Generate the form fields context for a groovy evaluation
     * 
     * @param session
     * @param fieldValues
     * @param locale
     * @param deleteDocuments
     * @return the context
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException
     * @throws InvalidSessionException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> generateGroovyContext(APISession session, Map<String, FormFieldValue> fieldValues, Locale locale,
            Map<String, Serializable> context, boolean deleteDocuments) throws FileTooBigException, IOException, InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException;

    /**
     * Get the right object value according to the datafield definition
     * 
     * @param value
     *            the value as extracted from the {@link FormFieldValue} object
     * @param dataTypeClassName
     *            the datafield classname
     * @return The object matching the {@link DataFieldDefinition}
     */
    Serializable getSerializableValue(Serializable value, String dataTypeClassName);

    /**
     * Perform a set attachment action
     * 
     * @param processInstanceID
     * @param attachments
     * @param action
     * @param fieldValues
     * @param locale
     * @param setAttachment
     * @throws FileTooBigException
     */
    void performSetAttachmentAction(APISession session, long processInstanceID, Set<InitialAttachment> attachments, FormAction action,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean setAttachment) throws FileTooBigException;

    /**
     * Perform a set attachment action
     * 
     * @param processInstanceID
     * @param attachments
     * @param attachmentName
     * @param fileName
     * @param setAttachment
     * @throws FileTooBigException
     */
    void performSetAttachmentAction(APISession session, long processInstanceID, Set<InitialAttachment> attachments, String attachmentName, String fileName,
            boolean setAttachment) throws FileTooBigException;

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param activityInstanceID
     *            the activity instance ID
     * @param expressions
     *            the map of expressions to evaluate
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, values returned are the current values for the instance. otherwise, it's the values at step end
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluations as a Map
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> evaluateActivityInitialExpressions(APISession session, long activityInstanceID, List<Expression> expressions,
            Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param activityInstanceID
     *            the activity instance ID
     * @param expressions
     *            the map of expressions to evaluate
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, values returned are the current values for the instance. otherwise, it's the values at step end
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluations as a Map
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> evaluateActivityExpressions(APISession session, long activityInstanceID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws BPMExpressionEvaluationException,
            InvalidSessionException, FileTooBigException, IOException, BPMEngineException;

    /**
     * Evaluate an expression (at form construction)
     * 
     * @param processInstanceID
     *            the process instance ID
     * @param expressions
     *            the map of expressions to evaluate
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, values returned are the current values for the instance. otherwise, it's the values at process instantiation
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluations as a Map
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> evaluateInstanceInitialExpressions(APISession session, long processInstanceID, List<Expression> expressions,
            Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param processInstanceID
     *            the process instance ID
     * @param expressions
     *            the map of expressions to evaluate
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param isCurrentValue
     *            if true, values returned are the current values for the instance. otherwise, it's the values at process instantiation
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluations as a Map
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> evaluateInstanceExpressions(APISession session, long processInstanceID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, boolean isCurrentValue, Map<String, Serializable> context) throws BPMExpressionEvaluationException,
            InvalidSessionException, FileTooBigException, IOException, BPMEngineException;

    /**
     * Evaluate an expression (at form construction)
     * 
     * @param processDefinitionID
     *            the process definition ID
     * @param expressions
     *            the map of expressions to evaluate
     * @param locale
     *            the user's locale
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluations as a Map
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> evaluateProcessInitialExpressions(APISession session, long processDefinitionID, List<Expression> expressions,
            Locale locale, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException;

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param processDefinitionID
     *            the process definition ID
     * @param expressions
     *            the map of expressions to evaluate
     * @param fieldValues
     *            the form field values
     * @param locale
     *            the user's locale
     * @param context
     *            some additional context for groovy evaluation
     * @return The result of the evaluations as a Map
     * @throws BPMExpressionEvaluationException
     *             , InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMEngineException 
     */
    Map<String, Serializable> evaluateProcessExpressions(APISession session, long processDefinitionID, List<Expression> expressions,
            Map<String, FormFieldValue> fieldValues, Locale locale, Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException,
            FileTooBigException, IOException, BPMEngineException;
}
