/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.console.common.server.utils.FormsResourcesUtils;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.expression.ExpressionEvaluationException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormValidationAPI;
import org.bonitasoft.forms.server.api.IFormWorkflowAPI;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormValidationException;
import org.bonitasoft.forms.server.validator.AbstractFormFieldValidator;
import org.bonitasoft.forms.server.validator.AbstractFormValidator;
import org.bonitasoft.forms.server.validator.IFormFieldValidator;
import org.bonitasoft.forms.server.validator.IFormPageValidator;

/**
 * implementation of {@link IFormValidationAPI}
 * 
 * @author Anthony Birembaut
 */
public class FormValidationAPIImpl implements IFormValidationAPI {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(FormValidationAPIImpl.class.getName());

    /**
     * Validate a form field value using the validator whose name is provided
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param processInstanceID
     *            the process instance ID
     * @param processDefinitionID
     *            the process definition ID
     * @param validatorClassName
     *            class name of the validator to use
     * @param fieldID
     *            the ID of the field
     * @param value
     *            to validate
     * @param submitButtonId
     *            the submit button id
     * @param locale
     *            the user's locale
     * @param transientDataContext
     * @param parameter
     *            expression passed to the validator
     * @return true if the field value comply with the validation. false otherwise
     * @throws FormValidationException
     */
    protected boolean validateField(final APISession session, final long processDefinitionID, final long processInstanceID, final long activityInstanceID,
            final String validatorClassName, final String fieldID, final FormFieldValue value, final String submitButtonId, final Locale locale,
            final Map<String, Serializable> transientDataContext, final Expression parameter) throws FormValidationException {

        boolean valid = true;
        try {
            final ClassLoader processClassLoader = FormsResourcesUtils.getProcessClassLoader(session, processDefinitionID);
            Class<?> validatorClass;
            if (processClassLoader != null) {
                validatorClass = Class.forName(validatorClassName, true, processClassLoader);
            } else {
                validatorClass = Class.forName(validatorClassName);
            }
            final Object formFieldValidatorObject = validatorClass.newInstance();
            if (formFieldValidatorObject instanceof AbstractFormValidator) {
                final AbstractFormValidator formValidator = (AbstractFormValidator) formFieldValidatorObject;
                formValidator.setSession(session);
                formValidator.setParameter(parameter);
                formValidator.setProcessDefinitionID(processDefinitionID);
                formValidator.setProcessInstanceID(processInstanceID);
                formValidator.setActivityInstanceID(activityInstanceID);
                formValidator.setTransientDataContext(transientDataContext);
                formValidator.setSubmitButtonId(submitButtonId);
            }
            if (formFieldValidatorObject instanceof AbstractFormFieldValidator) {
                ((AbstractFormFieldValidator) formFieldValidatorObject).setFieldID(fieldID);
            }
            final IFormFieldValidator formFieldValidator = (IFormFieldValidator) formFieldValidatorObject;
            valid = formFieldValidator.validate(value, locale);
        } catch (final ClassNotFoundException e) {
            final String message = "The validator " + validatorClassName + " is not in the classpath";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormValidationException(message, e);
        } catch (final InstantiationException e) {
            final String message = "The validator " + validatorClassName + " cannot be instanciated";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormValidationException(message, e);
        } catch (final IllegalAccessException e) {
            final String message = "The validator " + validatorClassName + " does not have a public default constructor";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormValidationException(message, e);
        }
        return valid;
    }

    /**
     * Validate a form page using the validator whose name is provided
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param processInstanceID
     *            the process instance ID
     * @param processDefinitionID
     *            the process definition ID
     * @param validatorClassName
     *            class name of the validator to use
     * @param fields
     *            a map of the fields ids and values
     * @param submitButtonId
     *            the submit button id
     * @param locale
     *            the user's locale
     * @param transientDataContext
     * @param parameter
     *            expression passed as a parameter
     * @return true if the page's fields values comply with the validation. false otherwise
     * @throws FormValidationException
     */
    protected boolean validatePage(final APISession session, final long processDefinitionID, final long processInstanceID, final long activityInstanceID,
            final String validatorClassName, final Map<String, FormFieldValue> fields, final String submitButtonId, final Locale locale,
            final Map<String, Serializable> transientDataContext, final Expression parameter) throws FormValidationException {

        boolean valid = true;
        try {
            final ClassLoader processClassLoader = FormsResourcesUtils.getProcessClassLoader(session, processDefinitionID);
            Class<?> validatorClass;
            if (processClassLoader != null) {
                validatorClass = Class.forName(validatorClassName, true, processClassLoader);
            } else {
                validatorClass = Class.forName(validatorClassName);
            }
            final Object formPageValidatorObject = validatorClass.newInstance();
            if (formPageValidatorObject instanceof AbstractFormValidator) {
                final AbstractFormValidator formValidator = (AbstractFormValidator) formPageValidatorObject;
                formValidator.setSession(session);
                formValidator.setParameter(parameter);
                formValidator.setProcessDefinitionID(processDefinitionID);
                formValidator.setProcessInstanceID(processInstanceID);
                formValidator.setActivityInstanceID(activityInstanceID);
                formValidator.setTransientDataContext(transientDataContext);
                formValidator.setSubmitButtonId(submitButtonId);
            }
            final IFormPageValidator formPageValidator = (IFormPageValidator) formPageValidatorObject;
            valid = formPageValidator.validate(fields, locale);
        } catch (final ClassNotFoundException e) {
            final String message = "The validator " + validatorClassName + " is not in the classpath";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormValidationException(message, e);
        } catch (final InstantiationException e) {
            final String message = "The validator " + validatorClassName + " cannot be instanciated";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormValidationException(message, e);
        } catch (final IllegalAccessException e) {
            final String message = "The validator " + validatorClassName + " does not have a public default constructor";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
            throw new FormValidationException(message, e);
        }
        return valid;
    }

    /**
     * Validate a form field value using the validators whose name is provided
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param processInstanceID
     *            the process instance ID
     * @param processDefinitionID
     *            the process definition ID
     * @param validators
     *            the list of validators
     * @param fieldId
     *            the ID of the field
     * @param value
     *            the form field value
     * @param submitButtonId
     *            the submit button id
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws ProcessDefinitionNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException  
     */
    protected List<FormValidator> validateField(final APISession session, final long processDefinitionID, final long processInstanceID,
            final long activityInstanceID, final List<FormValidator> validators, final String fieldId, final FormFieldValue value, final String submitButtonId,
            final Locale userLocale, final Map<String, Serializable> transientDataContext) throws FormValidationException, ProcessInstanceNotFoundException,
            ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException, FileTooBigException,
            IOException, BPMExpressionEvaluationException {

        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        final List<FormValidator> nonCompliantValidators = new ArrayList<FormValidator>();
        for (final FormValidator fieldValidator : validators) {
            if (!validateField(session, processDefinitionID, processInstanceID, activityInstanceID, fieldValidator.getValidatorClass(), fieldId, value,
                    submitButtonId, userLocale, transientDataContext, fieldValidator.getParameterExpression())) {
                final Map<String, FormFieldValue> fields = new HashMap<String, FormFieldValue>();
                fields.put(fieldId, value);
                if (activityInstanceID != -1) {
                    fieldValidator.setLabel((String) formWorkflowAPI.getActivityFieldValue(session, activityInstanceID, fieldValidator.getLabelExpression(),
                            fields, userLocale, true, transientDataContext));
                } else if (processInstanceID != -1) {
                    fieldValidator.setLabel((String) formWorkflowAPI.getInstanceFieldValue(session, processInstanceID, fieldValidator.getLabelExpression(),
                            fields, userLocale, true, transientDataContext));
                } else {
                    fieldValidator.setLabel((String) formWorkflowAPI.getProcessFieldValue(session, processDefinitionID, fieldValidator.getLabelExpression(),
                            fields, userLocale, transientDataContext));
                }
                nonCompliantValidators.add(fieldValidator);
            }
        }
        return nonCompliantValidators;
    }

    /**
     * Validate a form page using the validators whose name is provided
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param processInstanceID
     *            the process instance ID
     * @param processDefinitionID
     *            the process definition ID
     * @param validators
     *            the list of validators
     * @param fields
     *            the form field value
     * @param submitButtonId
     *            the submit button id
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws ProcessDefinitionNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     */
    protected List<FormValidator> validatePage(final APISession session, final long processDefinitionID, final long processInstanceID,
            final long activityInstanceID, final List<FormValidator> validators, final Map<String, FormFieldValue> fields, final String submitButtonId,
            final Locale userLocale, final Map<String, Serializable> transientDataContext) throws FormValidationException, ProcessInstanceNotFoundException,
            ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException, FileTooBigException,
            IOException, BPMExpressionEvaluationException {

        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        final List<FormValidator> nonCompliantValidators = new ArrayList<FormValidator>();
        for (final FormValidator pageValidator : validators) {
            if (!validatePage(session, processDefinitionID, processInstanceID, activityInstanceID, pageValidator.getValidatorClass(), fields, submitButtonId,
                    userLocale, transientDataContext, pageValidator.getParameterExpression())) {
                if (activityInstanceID != -1) {
                    pageValidator.setLabel((String) formWorkflowAPI.getActivityFieldValue(session, activityInstanceID, pageValidator.getLabelExpression(),
                            fields, userLocale, true, transientDataContext));
                } else if (processInstanceID != -1) {
                    pageValidator.setLabel((String) formWorkflowAPI.getInstanceFieldValue(session, processInstanceID, pageValidator.getLabelExpression(),
                            fields, userLocale, true, transientDataContext));
                } else {
                    pageValidator.setLabel((String) formWorkflowAPI.getProcessFieldValue(session, processDefinitionID, pageValidator.getLabelExpression(),
                            fields, userLocale, transientDataContext));
                }
                nonCompliantValidators.add(pageValidator);
            }
        }
        return nonCompliantValidators;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ArchivedFlowNodeInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    @Override
    public List<FormValidator> validateActivityField(final APISession session, final long activityInstanceID, final List<FormValidator> validators,
            final String fieldId, final FormFieldValue fieldValue, final String submitButtonId, final Locale userLocale,
            final Map<String, Serializable> transientDataContext) throws FormValidationException, ProcessInstanceNotFoundException,
            ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException, FileTooBigException,
            IOException, ArchivedProcessInstanceNotFoundException, ArchivedFlowNodeInstanceNotFoundException, BPMExpressionEvaluationException {
        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        final long processInstanceID = formWorkflowAPI.getProcessInstanceIDFromActivityInstanceID(session, activityInstanceID);
        final long processDefinitionID = formWorkflowAPI.getProcessDefinitionIDFromProcessInstanceID(session, processInstanceID);
        return validateField(session, processDefinitionID, processInstanceID, activityInstanceID, validators, fieldId, fieldValue, submitButtonId, userLocale,
                transientDataContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    @Override
    public List<FormValidator> validateInstanceField(final APISession session, final long processInstanceID, final List<FormValidator> validators,
            final String fieldId, final FormFieldValue fieldValue, final String submitButtonId, final Locale userLocale,
            final Map<String, Serializable> transientDataContext) throws FormValidationException, ProcessInstanceNotFoundException,
            ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException, FileTooBigException,
            IOException, ArchivedProcessInstanceNotFoundException, BPMExpressionEvaluationException {
        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        final long processDefinitionID = formWorkflowAPI.getProcessDefinitionIDFromProcessInstanceID(session, processInstanceID);
        return validateField(session, processDefinitionID, processInstanceID, -1, validators, fieldId, fieldValue, submitButtonId, userLocale,
                transientDataContext);
    }

    @Override
    public List<FormValidator> validateProcessField(final APISession session, final long processDefinitionID, final List<FormValidator> validators,
            final String fieldId, final FormFieldValue fieldValue, final String submitButtonId, final Locale userLocale,
            final Map<String, Serializable> transientDataContext) throws FormValidationException, ProcessInstanceNotFoundException,
            ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException, InvalidSessionException, FileTooBigException,
            IOException, BPMExpressionEvaluationException {
        return validateField(session, processDefinitionID, -1, -1, validators, fieldId, fieldValue, submitButtonId, userLocale, transientDataContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    @Override
    public List<FormValidator> validateActivityPage(final APISession session, final long activityInstanceID, final List<FormValidator> validators,
            final Map<String, FormFieldValue> fields, final String submitButtonId, final Locale userLocale, final Map<String, Serializable> transientDataContext)
            throws FormValidationException, ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, FileTooBigException, IOException,
            ArchivedProcessInstanceNotFoundException, ArchivedFlowNodeInstanceNotFoundException, BPMExpressionEvaluationException {
        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        final long processInstanceID = formWorkflowAPI.getProcessInstanceIDFromActivityInstanceID(session, activityInstanceID);
        final long processDefinitionID = formWorkflowAPI.getProcessDefinitionIDFromProcessInstanceID(session, processInstanceID);
        return validatePage(session, processDefinitionID, processInstanceID, activityInstanceID, validators, fields, submitButtonId, userLocale,
                transientDataContext);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    @Override
    public List<FormValidator> validateInstancePage(final APISession session, final long processInstanceID, final List<FormValidator> validators,
            final Map<String, FormFieldValue> fields, final String submitButtonId, final Locale userLocale, final Map<String, Serializable> transientDataContext)
            throws FormValidationException, ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, FileTooBigException, IOException,
            ArchivedProcessInstanceNotFoundException, BPMExpressionEvaluationException {
        final IFormWorkflowAPI formWorkflowAPI = FormAPIFactory.getFormWorkflowAPI();
        final long processDefinitionID = formWorkflowAPI.getProcessDefinitionIDFromProcessInstanceID(session, processInstanceID);
        return validatePage(session, processDefinitionID, processInstanceID, -1, validators, fields, submitButtonId, userLocale, transientDataContext);
    }

    @Override
    public List<FormValidator> validateProcessPage(final APISession session, final long processDefinitionID, final List<FormValidator> validators,
            final Map<String, FormFieldValue> fields, final String submitButtonId, final Locale userLocale, final Map<String, Serializable> transientDataContext)
            throws FormValidationException, ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException {
        return validatePage(session, processDefinitionID, -1, -1, validators, fields, submitButtonId, userLocale, transientDataContext);
    }
}
