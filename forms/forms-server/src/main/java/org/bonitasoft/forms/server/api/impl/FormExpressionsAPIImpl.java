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
package org.bonitasoft.forms.server.api.impl;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.console.common.server.utils.DocumentUtil;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.bpm.document.DocumentAttachmentException;
import org.bonitasoft.engine.bpm.document.DocumentNotFoundException;
import org.bonitasoft.engine.bpm.document.DocumentValue;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.DataFieldDefinition;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.InitialAttachment;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.accessor.api.EngineClientFactory;
import org.bonitasoft.forms.server.accessor.api.ExpressionEvaluatorEngineClient;
import org.bonitasoft.forms.server.accessor.api.ProcessInstanceAccessorEngineClient;
import org.bonitasoft.forms.server.accessor.api.utils.ProcessInstanceAccessor;
import org.bonitasoft.forms.server.api.IFormExpressionsAPI;
import org.bonitasoft.forms.server.api.impl.util.ExpressionAdapter;
import org.bonitasoft.forms.server.exception.FileTooBigException;

/**
 * Implementation of {@link IFormExpressionsAPI} allowing groovy
 * expressions evaluation and execution
 * 
 * @author Anthony Birembaut, Zhiheng Yang
 * 
 */
public class FormExpressionsAPIImpl implements IFormExpressionsAPI {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormExpressionsAPIImpl.class.getName());

    /**
     * Util class allowing to work with the BPM engine API
     */
    protected BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    /**
     * Field id regex
     */
    protected static final String FIELD_REGEX = FIELDID_PREFIX + ".*";

    private final EngineClientFactory engineClientFactory = new EngineClientFactory();

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
    @Override
    public Serializable evaluateActivityInitialExpression(final APISession session, final long activityInstanceID, final Expression expression,
            final Locale locale, final boolean isCurrentValue) throws InvalidSessionException, BPMExpressionEvaluationException, BPMEngineException {
        return evaluateActivityInitialExpression(session, activityInstanceID, expression, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

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
    @Override
    public Serializable evaluateActivityInitialExpression(final APISession session, final long activityInstanceID, final Expression expression,
            final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {
        Serializable result = null;
        if (expression != null) {
            final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
            context.put(IFormExpressionsAPI.USER_LOCALE, locale);
            final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
            expressionWithContext.put(expressionAdapter.getEngineExpression(expression), context);

            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            if (isCurrentValue) {
                final Map<String, Serializable> evaluatedExpressions = engineClient.evaluateExpressionsOnActivityInstance(activityInstanceID,
                        expressionWithContext);
                result = getFirstResult(evaluatedExpressions);
            } else {
                final Map<String, Serializable> evaluatedExpressions = engineClient.evaluateExpressionsOnCompletedActivityInstance(activityInstanceID,
                        expressionWithContext);
                result = getFirstResult(evaluatedExpressions);
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The expression or its type are null");
            }
        }
        return result;
    }

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
     */
    @Override
    public Serializable evaluateInstanceInitialExpression(final APISession session, final long processInstanceID, final Expression expression,
            final Locale locale, final boolean isCurrentValue) throws BPMEngineException, InvalidSessionException, BPMExpressionEvaluationException {
        return evaluateInstanceInitialExpression(session, processInstanceID, expression, locale, isCurrentValue, new HashMap<String, Serializable>());
    }

    /**
     * evaluate an initial value expression (at form construction)
     * 
     * @param processInstanceId
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
     */
    @Override
    public Serializable evaluateInstanceInitialExpression(final APISession session, final long processInstanceId, final Expression expression,
            final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context) throws BPMEngineException, InvalidSessionException, BPMExpressionEvaluationException {
        Serializable result = null;
        if (expression != null) {
            final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressions = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
            context.put(IFormExpressionsAPI.USER_LOCALE, locale);
            final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
            expressions.put(expressionAdapter.getEngineExpression(expression), context);

            final ProcessInstanceAccessor processInstanceAccessor = getProcessInstanceAccessor(session, processInstanceId);
            final ProcessInstanceExpressionsEvaluator processInstanceExpressionEvaluator = getProcessInstanceExpressionEvaluator(session);
            result = getFirstResult(processInstanceExpressionEvaluator.evaluate(processInstanceAccessor, expressions, !isCurrentValue));
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The expression or its type are null");
            }
        }
        return result;
    }

    protected Serializable getFirstResult(final Map<String, Serializable> evaluatedExpressions) {
        return evaluatedExpressions.values().iterator().next();
    }

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
     */
    @Override
    public Serializable evaluateProcessInitialExpression(final APISession session, final long processDefinitionID, final Expression expression,
            final Locale locale) throws BPMEngineException, InvalidSessionException, BPMExpressionEvaluationException {
        return evaluateProcessInitialExpression(session, processDefinitionID, expression, locale, new HashMap<String, Serializable>());
    }

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
    @Override
    public Serializable evaluateProcessInitialExpression(final APISession session, final long processDefinitionID, final Expression expression,
            final Locale locale, final Map<String, Serializable> context) throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {
        Serializable result = null;
        if (expression != null) {
            context.put(IFormExpressionsAPI.USER_LOCALE, locale);
            final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
            final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
            expressionWithContext.put(expressionAdapter.getEngineExpression(expression), context);

            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            result = engineClient.evaluateExpressionsOnProcessDefinition(processDefinitionID, expressionWithContext).values().iterator().next();
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The expression or its type are null");
            }
        }
        return result;
    }

    /**
     * Evaluate a field value
     * 
     * @param session
     * @param fieldId
     *            the field ID
     * @param fieldValues
     *            the form field values
     * @param deleteDocuments
     * @return the value of the field as an Serializable
     * @throws FileTooBigException
     * @throws IOException
     * @throws BPMExpressionEvaluationException
     * @throws InvalidSessionException
     */
    protected Serializable evaluateFieldValueActionExpression(final APISession session, final String fieldId, final Map<String, FormFieldValue> fieldValues,
            final Locale locale, final boolean deleteDocuments) throws FileTooBigException, IOException, InvalidSessionException, BPMEngineException {

        Serializable result = null;

        final FormFieldValue fieldValue = fieldValues.get(fieldId);
        if (fieldValue != null) {
            if (fieldValue.isDocument()) {
                result = getDocumentValue(session, fieldValue, deleteDocuments);
            } else {
                result = fieldValue.getValue();
            }
        }
        return result;
    }

    /**
     * Get the content of the document
     * 
     * @param session
     * @param fieldValue
     * @param deleteDocument
     * @return
     * @throws FileTooBigException
     * @throws IOException
     * @throws BPMExpressionEvaluationException
     * @throws InvalidSessionException
     */
    protected DocumentValue getDocumentValue(final APISession session, final FormFieldValue fieldValue, final boolean deleteDocument)
            throws FileTooBigException, IOException, InvalidSessionException, BPMEngineException {
        DocumentValue documentValue = null;
        final String uri = (String) fieldValue.getValue();
        if (File.class.getName().equals(fieldValue.getValueType())) {
            if (uri != null && uri.length() != 0) {
                final File theSourceFile = new File(uri);
                if (theSourceFile.exists()) {
                    final long maxSize = DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).getAttachmentMaxSize();
                    if (theSourceFile.length() > maxSize * 1048576) {
                        final String errorMessage = "file " + uri + " too big !";
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, errorMessage);
                        }
                        throw new FileTooBigException(errorMessage, uri, String.valueOf(maxSize));
                    }
                    final byte[] fileContent = DocumentUtil.getArrayByteFromFile(theSourceFile);
                    final String originalFileName = fieldValue.getDisplayedValue();
                    final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
                    final String contentType = mimetypesFileTypeMap.getContentType(theSourceFile);
                    documentValue = new DocumentValue(fileContent, contentType, originalFileName);
                    if (deleteDocument) {
                        theSourceFile.delete();
                    }
                } else {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "Error while retrieving the uploaded file " + uri + ": File not found.");
                    }
                }
            } else if (fieldValue.getDocumentId() != -1 && fieldValue.getDisplayedValue() != null) {
                final ProcessAPI processAPI = bpmEngineAPIUtil.getProcessAPI(session);
                try {
                    final Document document = processAPI.getDocument(fieldValue.getDocumentId());
                    if (document != null) {
                        if (document.hasContent()) {
                            documentValue = new DocumentValue(processAPI.getDocumentContent(document.getContentStorageId()), document.getContentMimeType(),
                                    document.getContentFileName());
                        } else {
                            documentValue = new DocumentValue(document.getUrl());
                        }
                    } else {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(Level.FINE, "The document with ID " + fieldValue.getDocumentId() + " is null.");
                        }
                    }
                } catch (final DocumentNotFoundException e) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE, "Error while retrieving the document with ID " + fieldValue.getDocumentId() + ": Document not found.");
                    }
                }
            }
        } else {
            documentValue = new DocumentValue(uri);
        }
        return documentValue;
    }

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
     */
    @Override
    public Map<String, Serializable> generateGroovyContext(final APISession session, final Map<String, FormFieldValue> fieldValues, final Locale locale,
            Map<String, Serializable> context, final boolean deleteDocuments) throws FileTooBigException, IOException, InvalidSessionException,
            BPMEngineException {
        if (context == null) {
            context = new HashMap<String, Serializable>();
        }
        for (final Entry<String, FormFieldValue> fieldValuesEntry : fieldValues.entrySet()) {
            final String fieldId = fieldValuesEntry.getKey();
            final Serializable fieldValue = evaluateFieldValueActionExpression(session, fieldId, fieldValues, locale, deleteDocuments);
            context.put(FIELDID_PREFIX + fieldId, fieldValue);
        }
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        return context;
    }

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
    @Override
    public Serializable evaluateActivityExpression(final APISession session, final long activityInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue) throws BPMExpressionEvaluationException,
            InvalidSessionException, FileTooBigException, IOException, BPMEngineException {
        return evaluateActivityExpression(session, activityInstanceID, expression, fieldValues, locale, isCurrentValue, null);
    }

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
    @Override
    public Serializable evaluateActivityExpression(final APISession session, final long activityInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context)
            throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException {
        Serializable result = null;
        if (expression != null) {
            final Map<String, Serializable> evalContext = generateGroovyContext(session, fieldValues, locale, context, false);
            final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
            final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
            expressionWithContext.put(expressionAdapter.getEngineExpression(expression), evalContext);

            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            if (isCurrentValue) {
                final Map<String, Serializable> evaluatedExpressions = engineClient.evaluateExpressionsOnActivityInstance(activityInstanceID,
                        expressionWithContext);
                result = getFirstResult(evaluatedExpressions);
            } else {
                final Map<String, Serializable> evaluatedExpressions = engineClient.evaluateExpressionsOnCompletedActivityInstance(activityInstanceID,
                        expressionWithContext);
                result = getFirstResult(evaluatedExpressions);
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The expression or its type are null");
            }
        }
        return result;
    }

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
     */
    @Override
    public Serializable evaluateInstanceExpression(final APISession session, final long processInstanceID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue) throws BPMEngineException,
            InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException {
        return evaluateInstanceExpression(session, processInstanceID, expression, fieldValues, locale, isCurrentValue, null);
    }

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param processInstanceId
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
     */
    @Override
    public Serializable evaluateInstanceExpression(final APISession session, final long processInstanceId, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context)
            throws BPMEngineException, InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException {
        Serializable result = null;
        if (expression != null) {
            final Map<String, Serializable> evalContext = generateGroovyContext(session, fieldValues, locale, context, false);
            final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressions = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
            final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
            expressions.put(expressionAdapter.getEngineExpression(expression), evalContext);

            result = getFirstResult(getProcessInstanceExpressionEvaluator(session).evaluate(getProcessInstanceAccessor(session, processInstanceId),
                    expressions, !isCurrentValue));
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The expression or its type are null");
            }
        }
        return result;

    }

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
    @Override
    public Serializable evaluateProcessExpression(final APISession session, final long processDefinitionID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale) throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException,
            IOException, BPMEngineException {
        return evaluateProcessExpression(session, processDefinitionID, expression, fieldValues, locale, null);
    }

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
    @Override
    public Serializable evaluateProcessExpression(final APISession session, final long processDefinitionID, final Expression expression,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final Map<String, Serializable> context) throws BPMExpressionEvaluationException,
            InvalidSessionException, FileTooBigException, IOException, BPMEngineException {
        Serializable result = null;
        if (expression != null) {
            final Map<String, Serializable> evalContext = generateGroovyContext(session, fieldValues, locale, context, false);
            final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();
            final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
            expressionWithContext.put(expressionAdapter.getEngineExpression(expression), evalContext);

            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            final Map<String, Serializable> evaluatedExpressions = engineClient.evaluateExpressionsOnProcessDefinition(processDefinitionID,
                    expressionWithContext);
            result = getFirstResult(evaluatedExpressions);
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "The expression or its type are null");
            }
        }
        return result;
    }

    /**
     * Get the right object value according to the datafield definition
     * 
     * @param value
     *            the value as extracted from the {@link FormFieldValue} object
     * @param dataTypeClassName
     *            the datafield classname
     * @return The object matching the {@link DataFieldDefinition}
     */
    @Override
    public Serializable getSerializableValue(final Serializable value, final String dataTypeClassName) {
        Serializable objectValue = null;
        if (value != null) {
            if (!String.class.getName().equals(dataTypeClassName) && value instanceof String && value.toString().length() == 0) {
                objectValue = null;
            } else if (Boolean.class.getName().equals(dataTypeClassName)) {
                objectValue = Boolean.parseBoolean(value.toString());
            } else if (Integer.class.getName().equals(dataTypeClassName)) {
                objectValue = Integer.parseInt(value.toString());
            } else if (Long.class.getName().equals(dataTypeClassName)) {
                objectValue = Long.parseLong(value.toString());
            } else if (Float.class.getName().equals(dataTypeClassName)) {
                objectValue = Float.parseFloat(value.toString());
            } else if (Double.class.getName().equals(dataTypeClassName)) {
                objectValue = Double.parseDouble(value.toString());
            } else if (Short.class.getName().equals(dataTypeClassName)) {
                objectValue = Short.parseShort(value.toString());
            } else if (Character.class.getName().equals(dataTypeClassName)) {
                objectValue = value.toString().charAt(0);
            } else {
                objectValue = value;
            }
        }
        return objectValue;
    }

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
     * 
     *             FIXME depending on the engine document data support
     */
    @Override
    public void performSetAttachmentAction(final APISession session, final long processInstanceID,
            final Set<org.bonitasoft.forms.client.model.InitialAttachment> attachments, final FormAction action, final Map<String, FormFieldValue> fieldValues,
            final Locale locale, final boolean setAttachment) throws FileTooBigException {
        String filePath = null;
        try {
            final String fieldId = action.getExpression().getContent().substring(FIELDID_PREFIX.length());
            final FormFieldValue fieldValue = fieldValues.get(fieldId);
            if (fieldValue != null) {
                filePath = (String) fieldValue.getValue();
                if (filePath != null && !filePath.isEmpty()) {
                    final String attachmentName = fieldId;
                    performSetAttachmentAction(session, processInstanceID, attachments, attachmentName, filePath, setAttachment);
                } else {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                "The attachment to set should be either a String or a groovy expression returning a String which is not the case of value : "
                                        + action.getDataName());
                    }
                }
            } else {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, "Error while setting the attachment. Unable to find a field with ID " + fieldId + " in the form.");
                }
            }
        } catch (final IndexOutOfBoundsException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Invalid action expression : " + action.getExpression().getName());
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * FIXME depending on the engine document data support
     */
    @Override
    public void performSetAttachmentAction(final APISession session, final long processInstanceID, final Set<InitialAttachment> attachments,
            String attachmentName, final String fileName, final boolean setAttachment) throws FileTooBigException {

        try {
            final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);
            if (fileName != null && fileName.length() != 0) {
                final File theSourceFile = new File(fileName);
                if (theSourceFile.exists()) {
                    final long maxSize = DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).getAttachmentMaxSize();
                    if (theSourceFile.length() > maxSize * 1048576) {
                        final String errorMessage = "file " + fileName + " too big !";
                        if (LOGGER.isLoggable(Level.SEVERE)) {
                            LOGGER.log(Level.SEVERE, errorMessage);
                        }
                        throw new FileTooBigException(errorMessage, fileName, String.valueOf(maxSize));
                    }
                    final byte[] fileContent = DocumentUtil.getArrayByteFromFile(theSourceFile);
                    final String originalFileName = theSourceFile.getName();
                    final FileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
                    final String contentType = mimetypesFileTypeMap.getContentType(theSourceFile);
                    // theSourceFile.delete();
                    if (attachmentName == null) {
                        attachmentName = "";
                    }
                    if (processInstanceID != -1 && setAttachment) {
                        processAPI.attachDocument(processInstanceID, attachmentName, originalFileName, contentType, fileContent);
                    } /*
                       * else {
                       * final InitialAttachment initialAttachment = new InitialAttachment();
                       * initialAttachment.setName(attachmentName);
                       * final Map<String, String> metadata = new HashMap<String, String>();
                       * metadata.put("content-type", contentType);
                       * initialAttachment.setFileName(originalFileName);
                       * initialAttachment.setMetaData(metadata);
                       * initialAttachment.setContent(fileContent);
                       * attachments.add(initialAttachment);
                       * }
                       */

                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "The file " + fileName + " does not exist. skipping the attachment creation/update.");
                    }
                }
            } else {
                final InitialAttachment initialAttachment = new InitialAttachment();
                initialAttachment.setName(attachmentName);
                attachments.add(initialAttachment);
            }
        } catch (final InvalidSessionException e) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Session timeout");
            }
        } catch (final IOException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "can't get the file content in the location: " + fileName);
            }
        } catch (final ProcessInstanceNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Process instance " + processInstanceID + " not found.");
            }
        } catch (final DocumentAttachmentException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while setting the attachment.");
            }
        } catch (final FileTooBigException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while setting the attachment: file too big!");
            }
            throw e;
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "Error while setting the attachment.");
            }

        }

    }

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
    @Override
    public Map<String, Serializable> evaluateActivityInitialExpressions(final APISession session, final long activityInstanceID,
            final List<Expression> expressions, final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context)
            throws BPMExpressionEvaluationException, InvalidSessionException, BPMEngineException {

        final Map<String, Serializable> result = new HashMap<String, Serializable>();
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionsWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        for (final Expression expression : expressions) {
            if (expression != null) {
                expressionsWithContext.put(expressionAdapter.getEngineExpression(expression), context);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The expression or its type are null");
                }
            }
        }
        if (!expressionsWithContext.isEmpty()) {
            Map<String, Serializable> evaluated;
            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            if (isCurrentValue) {
                evaluated = engineClient.evaluateExpressionsOnActivityInstance(activityInstanceID, expressionsWithContext);
            } else {
                evaluated = engineClient.evaluateExpressionsOnCompletedActivityInstance(activityInstanceID, expressionsWithContext);
            }
            result.putAll(evaluated);
        }
        return result;

    }

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
    @Override
    public Map<String, Serializable> evaluateActivityExpressions(final APISession session, final long activityInstanceID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue, Map<String, Serializable> context)
            throws BPMExpressionEvaluationException, InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        final Map<String, Serializable> result = new HashMap<String, Serializable>();
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionsWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        for (final Expression expression : expressions) {
            if (expression != null) {
                expressionsWithContext.put(expressionAdapter.getEngineExpression(expression), context);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The expression or its type are null");
                }
            }
        }

        if (!expressionsWithContext.isEmpty()) {
            context = generateGroovyContext(session, fieldValues, locale, context, false);
            Map<String, Serializable> evaluated;

            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            if (isCurrentValue) {
                evaluated = engineClient.evaluateExpressionsOnActivityInstance(activityInstanceID, expressionsWithContext);
            } else {
                evaluated = engineClient.evaluateExpressionsOnCompletedActivityInstance(activityInstanceID, expressionsWithContext);
            }
            result.putAll(evaluated);
        }
        return result;

    }

    /**
     * Evaluate an expression (at form construction)
     * 
     * @param processInstanceId
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
     */
    @Override
    public Map<String, Serializable> evaluateInstanceInitialExpressions(final APISession session, final long processInstanceId,
            final List<Expression> expressions, final Locale locale, final boolean isCurrentValue, final Map<String, Serializable> context)
            throws BPMEngineException, InvalidSessionException, BPMExpressionEvaluationException {

        final Map<String, Serializable> result = new HashMap<String, Serializable>();
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionsWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        for (final Expression expression : expressions) {
            if (expression != null) {
                expressionsWithContext.put(expressionAdapter.getEngineExpression(expression), context);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The expression or its type are null");
                }
            }
        }
        if (!expressionsWithContext.isEmpty()) {
            result.putAll(getProcessInstanceExpressionEvaluator(session).evaluate(getProcessInstanceAccessor(session, processInstanceId),
                    expressionsWithContext, !isCurrentValue));
        }
        return result;

    }

    protected ProcessInstanceAccessor getProcessInstanceAccessor(final APISession session, final long processInstanceId) throws BPMEngineException {
        return new ProcessInstanceAccessor(getEngineProcessInstanceAccessor(session), processInstanceId);
    }

    protected ProcessInstanceExpressionsEvaluator getProcessInstanceExpressionEvaluator(final APISession session) throws BPMEngineException {
        return new ProcessInstanceExpressionsEvaluator(getExpressionEvaluator(session));
    }

    private ExpressionEvaluatorEngineClient getExpressionEvaluator(final APISession session) throws BPMEngineException {
        return engineClientFactory.createExpressionEvaluatorEngineClient(session);
    }

    private ProcessInstanceAccessorEngineClient getEngineProcessInstanceAccessor(final APISession session) throws BPMEngineException {
        return engineClientFactory.createProcessInstanceEngineClient(session);
    }

    /**
     * Evaluate an expression (at form submission)
     * 
     * @param processInstanceId
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
     */
    @Override
    public Map<String, Serializable> evaluateInstanceExpressions(final APISession session, final long processInstanceId, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, final boolean isCurrentValue, Map<String, Serializable> context)
            throws BPMEngineException, InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException {

        final Map<String, Serializable> result = new HashMap<String, Serializable>();
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionsWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        for (final Expression expression : expressions) {
            if (expression != null) {
                expressionsWithContext.put(expressionAdapter.getEngineExpression(expression), context);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The expression or its type are null");
                }
            }
        }

        if (!expressionsWithContext.isEmpty()) {
            context = generateGroovyContext(session, fieldValues, locale, context, false);
            result.putAll(getProcessInstanceExpressionEvaluator(session).evaluate(getProcessInstanceAccessor(session, processInstanceId),
                    expressionsWithContext, !isCurrentValue));
        }
        return result;

    }

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
    @Override
    public Map<String, Serializable> evaluateProcessInitialExpressions(final APISession session, final long processDefinitionID,
            final List<Expression> expressions, final Locale locale, final Map<String, Serializable> context) throws BPMExpressionEvaluationException,
            InvalidSessionException, BPMEngineException {

        final Map<String, Serializable> result = new HashMap<String, Serializable>();
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionsWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        for (final Expression expression : expressions) {
            if (expression != null) {
                expressionsWithContext.put(expressionAdapter.getEngineExpression(expression), context);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The expression or its type are null");
                }
            }
        }
        if (!expressionsWithContext.isEmpty()) {

            final ExpressionEvaluatorEngineClient engineClient = getExpressionEvaluator(session);
            result.putAll(engineClient.evaluateExpressionsOnProcessDefinition(processDefinitionID, expressionsWithContext));
        }
        return result;

    }

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
    @Override
    public Map<String, Serializable> evaluateProcessExpressions(final APISession session, final long processDefinitionID, final List<Expression> expressions,
            final Map<String, FormFieldValue> fieldValues, final Locale locale, Map<String, Serializable> context) throws BPMExpressionEvaluationException,
            InvalidSessionException, FileTooBigException, IOException, BPMEngineException {

        final Map<String, Serializable> result = new HashMap<String, Serializable>();
        context.put(IFormExpressionsAPI.USER_LOCALE, locale);
        final Map<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>> expressionsWithContext = new HashMap<org.bonitasoft.engine.expression.Expression, Map<String, Serializable>>();

        final ExpressionAdapter expressionAdapter = new ExpressionAdapter();
        for (final Expression expression : expressions) {
            if (expression != null) {
                expressionsWithContext.put(expressionAdapter.getEngineExpression(expression), context);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "The expression or its type are null");
                }
            }
        }

        if (!expressionsWithContext.isEmpty()) {
            context = generateGroovyContext(session, fieldValues, locale, context, false);
            result.putAll(getExpressionEvaluator(session).evaluateExpressionsOnProcessDefinition(processDefinitionID, expressionsWithContext));
        }
        return result;

    }

}
