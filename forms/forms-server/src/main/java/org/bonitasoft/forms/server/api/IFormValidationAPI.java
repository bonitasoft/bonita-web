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
package org.bonitasoft.forms.server.api;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.console.common.server.utils.BPMExpressionEvaluationException;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ArchivedProcessInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessInstanceNotFoundException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.server.exception.FileTooBigException;
import org.bonitasoft.forms.server.exception.FormValidationException;

/**
 * Forms validation API
 * 
 * @author Anthony Birembaut
 */
public interface IFormValidationAPI {

    /**
     * Validate a form field value using the validators provided
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param validators
     *            List of validators to use
     * @param fieldId
     *            the ID of the field
     * @param fieldValue
     *            to validate as a {@link FormFieldValue} object
     * @param submitButtonId
     *            the submit button ID
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ProcessDefinitionNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validateActivityField(APISession session, long activityInstanceID, List<FormValidator> validators, String fieldId,
            FormFieldValue fieldValue, String submitButtonId, Locale userLocale, Map<String, Serializable> transientDataContext)
            throws FormValidationException, ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, FileTooBigException, IOException, ArchivedProcessInstanceNotFoundException, ArchivedFlowNodeInstanceNotFoundException, BPMExpressionEvaluationException;

    /**
     * Validate a form page using the validators provided
     * 
     * @param session
     *            the API session
     * @param activityInstanceID
     *            the activity instance ID
     * @param validators
     *            List of validators to use
     * @param fields
     *            a map of the fields ids and values
     * @param submitButtonId
     *            the submit button ID
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ProcessDefinitionNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws ArchivedFlowNodeInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validateActivityPage(APISession session, long activityInstanceID, List<FormValidator> validators, Map<String, FormFieldValue> fields,
            String submitButtonId, Locale userLocale, Map<String, Serializable> transientDataContext) throws FormValidationException,
            ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException, FileTooBigException, IOException, ArchivedProcessInstanceNotFoundException, ArchivedFlowNodeInstanceNotFoundException, BPMExpressionEvaluationException;

    /**
     * Validate a form field value using the validators provided
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param validators
     *            List of validators to use
     * @param submitButtonId
     *            the submit button ID
     * @param value
     *            to validate as a {@link FormFieldValue} object
     * @param submitButtonId
     *            the submit button ID
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ProcessDefinitionNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws ProcessInstanceReadException
     * @throws IOException
     * @throws FileTooBigException
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validateInstanceField(APISession session, long processInstanceID, List<FormValidator> validators, String fieldId,
            FormFieldValue fieldValue, String submitButtonId, Locale userLocale, Map<String, Serializable> transientDataContext)
            throws FormValidationException, ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, FileTooBigException, IOException,
            ArchivedProcessInstanceNotFoundException, BPMExpressionEvaluationException;

    /**
     * Validate a form page using the validators provided
     * 
     * @param session
     *            the API session
     * @param processInstanceID
     *            the process instance ID
     * @param validators
     *            List of validators to use
     * @param fields
     *            a map of the fields ids and values
     * @param submitButtonId
     *            the submit button ID
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ProcessDefinitionNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws ArchivedProcessInstanceNotFoundException
     * @throws BPMExpressionEvaluationException 
     */

    List<FormValidator> validateInstancePage(APISession session, long processInstanceID, List<FormValidator> validators, Map<String, FormFieldValue> fields,
            String submitButtonId, Locale userLocale, Map<String, Serializable> transientDataContext) throws FormValidationException,
            ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException, FileTooBigException, IOException,
            ArchivedProcessInstanceNotFoundException, BPMExpressionEvaluationException;

    /**
     * Validate a form field value using the validators provided
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param validators
     *            List of validators to use
     * @param value
     *            to validate as a {@link FormFieldValue} object
     * @param submitButtonId
     *            the submit button ID
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ProcessDefinitionNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws BPMEngineException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validateProcessField(APISession session, long processDefinitionID, List<FormValidator> validators, String fieldId,
            FormFieldValue fieldValue, String submitButtonId, Locale userLocale, Map<String, Serializable> transientDataContext)
            throws FormValidationException, ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException,
            BPMEngineException, InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException;

    /**
     * Validate a form page using the validators provided
     * 
     * @param session
     *            the API session
     * @param processDefinitionID
     *            the process definition ID
     * @param validators
     *            List of validators to use
     * @param fields
     *            a map of the fields ids and values
     * @param submitButtonId
     *            the submit button ID
     * @param userLocale
     *            the user's locale
     * @param transientDataContext
     * @return a list of the validators for which the field value does not comply with the validation
     * @throws FormValidationException
     * @throws ProcessDefinitionNotFoundException
     * @throws ActivityInstanceNotFoundException
     * @throws ProcessInstanceNotFoundException
     * @throws InvalidSessionException
     * @throws IOException
     * @throws FileTooBigException
     * @throws BPMExpressionEvaluationException 
     */
    List<FormValidator> validateProcessPage(APISession session, long processDefinitionID, List<FormValidator> validators, Map<String, FormFieldValue> fields,
            String submitButtonId, Locale userLocale, Map<String, Serializable> transientDataContext) throws FormValidationException,
            ProcessInstanceNotFoundException, ActivityInstanceNotFoundException, ProcessDefinitionNotFoundException, BPMEngineException,
            InvalidSessionException, FileTooBigException, IOException, BPMExpressionEvaluationException;
}
