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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.engine.api.IdentityAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.actor.ActorCriterion;
import org.bonitasoft.engine.bpm.actor.ActorInstance;
import org.bonitasoft.engine.bpm.bar.BusinessArchive;
import org.bonitasoft.engine.bpm.bar.BusinessArchiveBuilder;
import org.bonitasoft.engine.bpm.process.DesignProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.impl.ProcessDefinitionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.identity.User;
import org.bonitasoft.engine.identity.UserNotFoundException;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.server.FormsTestCase;
import org.bonitasoft.forms.server.api.FormAPIFactory;
import org.bonitasoft.forms.server.api.IFormValidationAPI;
import org.bonitasoft.forms.server.validator.CharFieldValidator;
import org.bonitasoft.forms.server.validator.DateOrderTestPageValidator;
import org.bonitasoft.forms.server.validator.InstanceIDTestFieldValidator;
import org.bonitasoft.forms.server.validator.RegexFieldValidator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the implementation of the form validation API
 * 
 * @author Anthony Birembaut
 * 
 */
public class FormValidationAPIImplIT extends FormsTestCase {

    protected static final String USERNAME = "dwight";

    protected static final String PASSWORD = "Schrute";

    private ProcessAPI processAPI = null;

    private ProcessDefinition bonitaProcess;

    private long processInstanceID;

    @Before
    public void setUp() throws Exception {

        super.setUp();
        final ProcessDefinitionBuilder processBuilder = new ProcessDefinitionBuilder().createNewInstance("firstProcess", "1.0");
        processBuilder.addUserTask("Request_Approval", "myActor");
        processBuilder.addActor("myActor")
                .addUserTask("Approval", "myActor");
        processBuilder.addTransition("Request_Approval", "Approval");

        DesignProcessDefinition designProcessDefinition = processBuilder.done();
        final BusinessArchiveBuilder businessArchiveBuilder = new BusinessArchiveBuilder().createNewBusinessArchive();
        final BusinessArchive businessArchive = businessArchiveBuilder.setProcessDefinition(designProcessDefinition).done();
        processAPI = TenantAPIAccessor.getProcessAPI(getSession());
        bonitaProcess = processAPI.deploy(businessArchive);

        IdentityAPI identityAPI = TenantAPIAccessor.getIdentityAPI(getSession());
        User user;
        try {
            user = identityAPI.getUserByUserName(USERNAME);
        } catch (UserNotFoundException e) {
            user = identityAPI.createUser(USERNAME, PASSWORD);
        }

        ActorInstance processActor = processAPI.getActors(bonitaProcess.getId(), 0, 1, ActorCriterion.NAME_ASC).get(0);
        processAPI.addUserToActor(processActor.getId(), user.getId());

        processAPI.enableProcess(bonitaProcess.getId());

        processInstanceID = processAPI.startProcess(bonitaProcess.getId()).getId();
    }

    @After
    public void tearDown() throws Exception {

        processAPI.disableProcess(bonitaProcess.getId());
        processAPI.deleteProcess(bonitaProcess.getId());
        super.tearDown();
    }

    @Test
    public void testValidateField() throws Exception {
        IFormValidationAPI api = FormAPIFactory.getFormValidationAPI();
        List<FormValidator> validators = new ArrayList<FormValidator>();
        FormValidator formValidator = new FormValidator("validatorId", CharFieldValidator.class.getName(), "");
        formValidator.setLabelExpression(new Expression(null, "validator label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        validators.add(formValidator);
        FormFieldValue value = new FormFieldValue("a", String.class.getName());
        Assert.assertEquals(0,
                api.validateInstanceField(getSession(), processInstanceID, validators, "field", value, null, Locale.ENGLISH, new HashMap<String, Serializable>())
                        .size());
    }

    @Test
    public void testValidateFieldUsingInstanceID() throws Exception {
        IFormValidationAPI api = FormAPIFactory.getFormValidationAPI();
        List<FormValidator> validators = new ArrayList<FormValidator>();
        FormValidator formValidator = new FormValidator("validatorId", InstanceIDTestFieldValidator.class.getName(), "");
        formValidator.setLabelExpression(new Expression(null, "validator label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        validators.add(formValidator);
        FormFieldValue value = new FormFieldValue(processInstanceID, String.class.getName());
        Assert.assertEquals(0,
                api.validateInstanceField(getSession(), processInstanceID, validators, "field", value, null, Locale.ENGLISH, new HashMap<String, Serializable>())
                        .size());
    }

    @Test
    public void testValidateFieldWithRegex() throws Exception {
        IFormValidationAPI api = FormAPIFactory.getFormValidationAPI();
        List<FormValidator> validators = new ArrayList<FormValidator>();
        FormValidator formValidator = new FormValidator("validatorId", RegexFieldValidator.class.getName(), "");
        formValidator.setLabelExpression(new Expression(null, "validator label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        formValidator.setParameterExpression(new Expression(null, "[a-z0-9_]*", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        validators.add(formValidator);
        FormFieldValue value = new FormFieldValue("abc123_def", null);
        Assert.assertEquals(0,
                api.validateInstanceField(getSession(), processInstanceID, validators, "field", value, null, Locale.ENGLISH, new HashMap<String, Serializable>())
                        .size());
        validators = new ArrayList<FormValidator>();
        FormValidator formValidator2 = new FormValidator("validatorId", RegexFieldValidator.class.getName(), null);
        formValidator2.setLabelExpression(new Expression(null, "validator label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        formValidator2.setParameterExpression(new Expression(null, "[a-z_]*", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        validators.add(formValidator2);
        Assert.assertEquals(1,
                api.validateInstanceField(getSession(), processInstanceID, validators, "field", value, null, Locale.ENGLISH, new HashMap<String, Serializable>())
                        .size());
    }

    @Test
    public void testValidatePage() throws Exception {
        IFormValidationAPI api = FormAPIFactory.getFormValidationAPI();
        List<FormValidator> validators = new ArrayList<FormValidator>();
        FormValidator formValidator = new FormValidator("validatorId", DateOrderTestPageValidator.class.getName(), null);
        formValidator.setLabelExpression(new Expression(null, "validator label", ExpressionType.TYPE_CONSTANT.name(), String.class.getName(), null,
                new ArrayList<Expression>()));
        validators.add(formValidator);
        Map<String, FormFieldValue> fieldValues = new HashMap<String, FormFieldValue>();
        FormFieldValue value1 = new FormFieldValue("07-15-2009", Date.class.getName(), "mm-dd-yyyy");
        fieldValues.put("fieldId1", value1);
        FormFieldValue value2 = new FormFieldValue("06-10-2010", Date.class.getName(), "mm-dd-yyyy");
        fieldValues.put("fieldId2", value2);
        Assert.assertEquals(0,
                api.validateInstancePage(getSession(), processInstanceID, validators, fieldValues, null, Locale.ENGLISH, new HashMap<String, Serializable>())
                        .size());
    }
}
