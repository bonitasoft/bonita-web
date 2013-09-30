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
package org.bonitasoft.forms.server.accessor.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataDefinition;
import org.bonitasoft.engine.bpm.flownode.ActivityInstance;
import org.bonitasoft.engine.bpm.flownode.ActivityInstanceNotFoundException;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstance;
import org.bonitasoft.engine.bpm.flownode.ArchivedFlowNodeInstanceNotFoundException;
import org.bonitasoft.engine.bpm.process.ProcessDefinition;
import org.bonitasoft.engine.bpm.process.ProcessDefinitionNotFoundException;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.session.InvalidSessionException;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.client.model.FormAction;
import org.bonitasoft.forms.client.model.FormType;
import org.bonitasoft.forms.client.model.FormValidator;
import org.bonitasoft.forms.client.model.FormWidget;
import org.bonitasoft.forms.client.model.TransientData;
import org.bonitasoft.forms.client.model.exception.FormException;
import org.bonitasoft.forms.client.model.exception.SessionTimeoutException;
import org.bonitasoft.forms.server.accessor.DefaultFormsProperties;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.accessor.IApplicationFormDefAccessor;
import org.bonitasoft.forms.server.accessor.widget.IEngineWidgetBuilder;
import org.bonitasoft.forms.server.accessor.widget.WidgetBuilderFactory;
import org.bonitasoft.forms.server.api.impl.util.BPMEngineAPIUtil;
import org.bonitasoft.forms.server.exception.ApplicationFormDefinitionNotFoundException;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;

/**
 * Implementation of {@link IApplicationFormDefAccessor} allowing to generate the application config from the engine
 * 
 * @author Anthony Birembaut, Haojie Yuan, Vincent Elcrin, Julien Mege
 */
public class EngineApplicationFormDefAccessorImpl implements IApplicationFormDefAccessor {

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(EngineApplicationFormDefAccessorImpl.class.getName());

    /**
     * prefix for the process data widgets
     */
    protected static final String PROCESS_ELEMENTS_PREFIX = "process";

    /**
     * prefix for the activity data widgets
     */
    protected static final String ACTIVITY_ELEMENTS_PREFIX = "activity";

    /**
     * indicates whether the page has to be displayed in edit mode (for a ready task) or not (view mode)
     */
    protected boolean isEditMode;

    /**
     * The application name
     */
    protected String applicationName;

    /**
     * the activity Name
     */
    protected String activityName;

    /**
     * The activity display name
     */
    protected String activityDisplayName;

    /**
     * The application label
     */
    protected String applicationLabel;

    /**
     * The process definition UUID of the process to which this instance is associated
     */
    private final long processDefinitionID;

    /**
     * Accessor used to create the widgets and validators from a process definition retrieved from the engine
     */
    protected IEngineWidgetBuilder engineWidgetBuilder = WidgetBuilderFactory.getEngineWidgetBuilder();

    /**
     * the action for each widget
     */
    protected Map<String, FormAction> widgetsActions = new HashMap<String, FormAction>();

    /**
     * Widget data objects for the application
     */
    final List<FormWidget> applicationWidgets = new ArrayList<FormWidget>();

    /**
     * the pages widgets
     */
    protected Map<String, List<String>> pagesWidgets = new HashMap<String, List<String>>();

    /**
     * Util class allowing to work with the BPM engine API
     */
    protected BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    /**
     * Nb of pages in the form
     */
    protected int nbOfPages = -1;

    protected boolean isRecap = false;

    protected APISession session;

    protected boolean includeProcessVariables;

    protected long activityInstanceID;

    protected String processName;

    protected String processVersion;

    protected long tenantId;

    /**
     * Default constructor.
     * 
     * @param processDefinitionUUID
     * @param activityName
     * @param includeProcessVariables
     * @param isEditMode
     */
    public EngineApplicationFormDefAccessorImpl(final APISession session, final long processDefinitionID, final long activityInstanceID,
            final boolean includeProcessVariables, final boolean isEditMode, final boolean isCurrentValue, final boolean isConfirmationPage) {

        this.isEditMode = isEditMode;
        this.processDefinitionID = processDefinitionID;
        isRecap = false;
        if (isCurrentValue && activityInstanceID == -1) {
            isRecap = true;
        }
        this.session = session;
        this.includeProcessVariables = includeProcessVariables;
        this.activityInstanceID = activityInstanceID;
        tenantId = session.getTenantId();

        try {
            final ProcessAPI processAPI = bpmEngineAPIUtil.getProcessAPI(session);
            final ProcessDefinition processDefinition = processAPI.getProcessDefinition(processDefinitionID);
            processName = processDefinition.getName();
            processVersion = processDefinition.getVersion();
            applicationName = processName;
            // we don't need the activity name when trying to display the default confirmation page
            if (activityInstanceID != -1 && !isConfirmationPage) {
                if (isCurrentValue) {
                    final ActivityInstance activityInstance = processAPI.getActivityInstance(activityInstanceID);
                    activityName = activityInstance.getName();
                    activityDisplayName = activityInstance.getDisplayName();
                } else {
                    final ArchivedFlowNodeInstance activityInstance = processAPI.getArchivedFlowNodeInstance(activityInstanceID);
                    activityName = activityInstance.getName();
                    activityDisplayName = activityInstance.getDisplayName();
                }
                if (activityDisplayName == null) {
                    activityDisplayName = activityName;
                }
            }
        } catch (final InvalidSessionException e) {
            final String errorMessage = "session is invalid.";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, errorMessage, e);
            }
        } catch (final ProcessDefinitionNotFoundException e) {
            final String errorMessage = "process with " + processDefinitionID + "is not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        } catch (final ActivityInstanceNotFoundException e) {
            final String errorMessage = "Activity with " + activityInstanceID + " is not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        } catch (final ArchivedFlowNodeInstanceNotFoundException e) {
            final String errorMessage = "Archived activity with " + activityInstanceID + " is not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        } catch (final Exception e) {
            final String errorMessage = "error while invoking the engine";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        }
    }

    /**
     * build the form widgets
     * 
     * @param pageId
     * @throws Exception
     */
    protected void buildFormWidgets(final String pageId) throws Exception {

        List<DataDefinition> processDataFields = new ArrayList<DataDefinition>();
        final Set<DataDefinition> processDataFieldSet = new TreeSet<DataDefinition>(new Comparator<DataDefinition>() {

            @Override
            public int compare(final DataDefinition o1, final DataDefinition o2) {
                return o1.getName().compareTo(o2.getName());
            }

        });
        final Set<DataDefinition> activityDataFieldSet = new TreeSet<DataDefinition>(new Comparator<DataDefinition>() {

            @Override
            public int compare(final DataDefinition o1, final DataDefinition o2) {
                return o1.getName().compareTo(o2.getName());
            }

        });

        final DefaultFormsProperties defaultProperties = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantId);
        final int numberPerPage = defaultProperties.getMaxWigdetPerPage();
        nbOfPages = getNbOfPages();
        ProcessAPI processAPI;
        try {
            processAPI = bpmEngineAPIUtil.getProcessAPI(session);
            if (activityName == null) {
                // Instantiation Form
                processDataFields = processAPI.getProcessDataDefinitions(processDefinitionID, getFirstFieldIndex(pageId, numberPerPage), numberPerPage);
                processDataFieldSet.addAll(processDataFields);
                createWidgets(processDataFieldSet);
            } else {
                final int nbOfProcessDataDefinitionPages = getNbOfProcessDataDefinitionPages();
                if (includeProcessVariables && nbOfProcessDataDefinitionPages > 0) {
                    /*
                     * If we include process variables we need to check from which page of which data type the pageId belong to
                     */
                    if (Integer.valueOf(pageId) < nbOfProcessDataDefinitionPages) {
                        processDataFields = processAPI.getProcessDataDefinitions(processDefinitionID, getFirstFieldIndex(pageId, numberPerPage),
                                numberPerPage);
                        processDataFieldSet.addAll(processDataFields);
                    } else {
                        final List<DataDefinition> activityDataFields = processAPI.getActivityDataDefinitions(processDefinitionID, activityName,
                                getFirstFieldIndex(pageId, numberPerPage) - nbOfProcessDataDefinitionPages * numberPerPage, numberPerPage);
                        activityDataFieldSet.addAll(activityDataFields);
                    }
                    // attachments = queryDefinitionAPI.getAttachmentDefinitions(processDefinitionUUID);
                } else {
                    final List<DataDefinition> activityDataFields = processAPI.getActivityDataDefinitions(processDefinitionID, activityName,
                            getFirstFieldIndex(pageId, numberPerPage), numberPerPage);
                    activityDataFieldSet.addAll(activityDataFields);
                }

                createWidgets(processDataFieldSet, activityDataFieldSet);
            }
        } catch (final InvalidSessionException e) {
            final String errorMessage = "session is invalid.";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, errorMessage, e);
            }
            throw new FormException(errorMessage);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String errorMessage = "process with " + processDefinitionID + "is not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new FormException(errorMessage);
        } catch (final Exception e) {
            final String errorMessage = "error while invoking the engine";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new FormException(errorMessage);
        }

    }

    /**
     * Calculate the index of the first field of the form page to display
     * 
     * @param pageId
     * @param numberPerPage
     * return index of the first field of the form page to display 
     */
    private int getFirstFieldIndex(final String pageId, final int numberPerPage) {
        return Integer.valueOf(pageId) * numberPerPage;
    }

    /**
     * create the widgets data objects and put it in the applicationWidgets list
     * 
     * @param applicationDataFields
     * @param attachments
     * @param isEditMode
     */
    protected void createWidgets(final Set<DataDefinition> applicationDataFields) {
        final boolean includeInitialValues = !(activityName == null && isEditMode);
        final Map<FormWidget, FormAction> applicationFields = engineWidgetBuilder.createWidgets(applicationDataFields, PROCESS_ELEMENTS_PREFIX,
                isEditMode,
                includeInitialValues);
        for (final Entry<FormWidget, FormAction> applicationFieldEntry : applicationFields.entrySet()) {
            final FormWidget applicationWidget = applicationFieldEntry.getKey();
            if (isEditMode) {
                widgetsActions.put(applicationWidget.getId(), applicationFieldEntry.getValue());
            }
            applicationWidgets.add(applicationWidget);
        }
        Collections.sort(applicationWidgets);
    }

    /**
     * create the widgets data objects and put it in the activityWidgets list
     * 
     * @param applicationDataFields
     * @param attachments
     * @param activityDataFields
     * @param isEditMode
     */
    protected void createWidgets(final Set<DataDefinition> applicationDataFields, final Set<DataDefinition> activityDataFields) {
        createWidgets(applicationDataFields);
        final boolean includeInitialValues = !(activityName == null && isEditMode);
        final Map<FormWidget, FormAction> activityFields = engineWidgetBuilder.createWidgets(activityDataFields, ACTIVITY_ELEMENTS_PREFIX,
                isEditMode,
                includeInitialValues);
        final List<FormWidget> activityWidgets = new ArrayList<FormWidget>();
        for (final Entry<FormWidget, FormAction> activityFieldEntry : activityFields.entrySet()) {
            final FormWidget activityWidget = activityFieldEntry.getKey();
            if (isEditMode) {
                widgetsActions.put(activityWidget.getId(), activityFieldEntry.getValue());
            }
            activityWidgets.add(activityWidget);
        }
        Collections.sort(activityWidgets);
        applicationWidgets.addAll(activityWidgets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormPermissions() {
        if (activityName == null) {
            return FormServiceProviderUtil.PROCESS_UUID + "#" + processName + "--" + processVersion;
        } else {
            return FormServiceProviderUtil.ACTIVITY_UUID + "#" + processName + "--" + processVersion + "--" + activityName;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getFirstPageExpression() {
        if (nbOfPages == -1) {
            try {
                nbOfPages = getNbOfPages();
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        if (nbOfPages > 0) {
            return new Expression("firstPageExpression", "0", ExpressionType.TYPE_CONSTANT.toString(), String.class.getName(), null, null);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPages() {
        if (nbOfPages == -1) {
            try {
                nbOfPages = getNbOfPages();
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, e.getMessage(), e);
                }
            }
        }
        final List<String> pages = new ArrayList<String>();
        for (int i = 0; i < nbOfPages; i++) {
            pages.add(Integer.toString(i));
        }
        return pages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getNextPageExpression(final String pageId) throws InvalidFormDefinitionException {
        try {
            final int currentPageIndex = Integer.parseInt(pageId);
            return new Expression("nextPageExpression", Integer.toString(currentPageIndex + 1), ExpressionType.TYPE_CONSTANT.toString(),
                    String.class.getName(), null, null);
        } catch (final NumberFormatException e) {
            throw new InvalidFormDefinitionException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFormPageLayout(final String pageId) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationVersion() {
        String version = null;
        try {
            final ProcessAPI processAPI = bpmEngineAPIUtil.getProcessAPI(session);
            version = processAPI.getProcessDefinition(processDefinitionID).getVersion();
        } catch (final InvalidSessionException e) {
            final String errorMessage = "session is invalid.";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, errorMessage, e);
            }
        } catch (final ProcessDefinitionNotFoundException e) {
            final String errorMessage = "process with " + processDefinitionID + "is not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        } catch (final Exception e) {
            final String errorMessage = "error while invoking the engine";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        }

        return version;
    }

    /**
     * {@inheritDoc}
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormWidget> getPageWidgets(final String pageId) throws InvalidFormDefinitionException {
        try {
            if (nbOfPages == -1) {
                nbOfPages = getNbOfPages();
            }
            buildFormWidgets(pageId);
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
        final List<FormWidget> widgets = engineWidgetBuilder.getPageWidgets(pageId, nbOfPages, applicationWidgets, PROCESS_ELEMENTS_PREFIX,
                isEditMode);
        final List<String> widgetIds = new ArrayList<String>();
        for (final FormWidget formWidget : widgets) {
            widgetIds.add(formWidget.getId());
        }
        pagesWidgets.put(pageId, widgetIds);
        return widgets;
    }

    /**
     * Get number of process data definition pages
     * 
     * @return
     * @throws Exception
     */
    private int getNbOfProcessDataDefinitionPages() throws Exception {
        final int nbOfProcessDataDefinition = getNbOfPagesPerDataDefType()[0];
        if (nbOfProcessDataDefinition > 0) {
            final DefaultFormsProperties defaultProperties = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantId);
            return getRoundedNbOfPages(nbOfProcessDataDefinition, defaultProperties.getMaxWigdetPerPage());
        } else {
            return nbOfProcessDataDefinition;
        }
    }

    /**
     * Get number of activity data definition pages
     * 
     * @return
     * @throws Exception
     */
    private int getNbOfActivityDataDefinitionPages() throws Exception {
        final int nbOfProcessDataDefinition = getNbOfPagesPerDataDefType()[1];
        if (nbOfProcessDataDefinition > 0) {
            final DefaultFormsProperties defaultProperties = DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantId);
            return getRoundedNbOfPages(nbOfProcessDataDefinition, defaultProperties.getMaxWigdetPerPage());
        } else {
            return nbOfProcessDataDefinition;
        }
    }

    private int getRoundedNbOfPages(final int nbDataDefinition, final int nbMaxPerPage) {
        final int nbOfPage = nbDataDefinition / nbMaxPerPage;
        return nbDataDefinition % nbMaxPerPage > 0 ? nbOfPage + 1 : nbOfPage;
    }

    /**
     * Get number of pages for process and activity data definition
     * 
     * @return
     * @throws Exception
     */
    private int[] getNbOfPagesPerDataDefType() throws Exception {
        int nbActivityDataDefinition = 0;
        int nbProcessDataDefinition = 0;

        final ProcessAPI processAPI = bpmEngineAPIUtil.getProcessAPI(session);
        try {
            if (activityName != null) {
                nbActivityDataDefinition = processAPI.getNumberOfActivityDataDefinitions(processDefinitionID, activityName);
            }
            if (activityName == null || includeProcessVariables) {
                nbProcessDataDefinition = processAPI.getNumberOfProcessDataDefinitions(processDefinitionID);
            }
        } catch (final InvalidSessionException e) {
            final String errorMessage = "session is invalid.";
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, errorMessage, e);
            }
            throw new SessionTimeoutException(errorMessage);
        } catch (final ProcessDefinitionNotFoundException e) {
            final String errorMessage = "process with " + processDefinitionID + "is not found.";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
            throw new FormException(errorMessage);
        }

        final int[] nbOfPages = { nbProcessDataDefinition, nbActivityDataDefinition };
        return nbOfPages;
    }

    /**
     * @return the number of pages
     */
    protected int getNbOfPages() throws Exception {
        final int nbOfPage = getNbOfProcessDataDefinitionPages() + getNbOfActivityDataDefinitionPages();
        return nbOfPage == 0 ? 1 : nbOfPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormValidator> getPageValidators(final String pageId) throws InvalidFormDefinitionException {
        return new ArrayList<FormValidator>();
    }

    /**
     * {@inheritDoc}
     * 
     * @throws ProcessNotFoundException
     */
    @Override
    public Expression getPageLabelExpression(final String pageId) throws InvalidFormDefinitionException {

        applicationLabel = processName;
        int nbOfPages = 0;
        try {
            nbOfPages = getNbOfPages();
        } catch (final Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
        String pageLabelComplement = null;
        if (nbOfPages > 1) {
            pageLabelComplement = " : page " + (Integer.parseInt(pageId) + 1) + " / " + nbOfPages;
        } else {
            pageLabelComplement = "";
        }
        if (isRecap) {
            if (applicationLabel != null && applicationLabel.length() != 0) {
                return new Expression("pageLabelExpression", "Current state : " + toUpperCaseFirstLetter(applicationLabel + pageLabelComplement),
                        ExpressionType.TYPE_CONSTANT.toString(), String.class.getName(), null, null);
            } else {
                return new Expression("pageLabelExpression", "Current state : " + toUpperCaseFirstLetter(applicationName + pageLabelComplement),
                        ExpressionType.TYPE_CONSTANT.toString(), String.class.getName(), null, null);
            }
        } else if (activityName == null) {
            if (applicationLabel != null && applicationLabel.length() != 0) {
                return new Expression("pageLabelExpression", "#" + toUpperCaseFirstLetter(applicationLabel + pageLabelComplement),
                        ExpressionType.TYPE_CONSTANT.toString(), String.class.getName(), null, null);
            } else {
                return new Expression("pageLabelExpression", "#" + toUpperCaseFirstLetter(applicationName + pageLabelComplement),
                        ExpressionType.TYPE_CONSTANT.toString(), String.class.getName(), null, null);
            }
        } else {
            return new Expression("pageLabelExpression", toUpperCaseFirstLetter(activityDisplayName + pageLabelComplement),
                    ExpressionType.TYPE_CONSTANT.toString(),
                    String.class.getName(), null, null);
        }
    }

    /**
     * put set the fisrt letter of a label to uppercase
     * 
     * @param label
     *            the label
     * @return the new label
     */
    protected String toUpperCaseFirstLetter(final String label) {
        if (label.length() > 0) {
            final Character firstLetter = Character.toUpperCase(label.charAt(0));
            return firstLetter + label.substring(1, label.length());
        } else {
            return label;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHTMLAllowedInLabel(final String pageId) throws InvalidFormDefinitionException {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TransientData> getTransientData() throws InvalidFormDefinitionException {
        return new ArrayList<TransientData>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FormAction> getActions(final String pageId) throws InvalidFormDefinitionException, ApplicationFormDefinitionNotFoundException {
        final List<FormAction> actions = new ArrayList<FormAction>();

        List<String> widgetIds = pagesWidgets.get(pageId);
        if (widgetIds == null) {
            getPageWidgets(pageId);
            widgetIds = pagesWidgets.get(pageId);
        }
        for (final String widgetId : widgetIds) {
            final FormAction widgetAction = widgetsActions.get(widgetId);
            if (widgetAction != null) {
                actions.add(widgetAction);
            }
        }
        return actions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfirmationLayout() {
        return DefaultFormsPropertiesFactory.getDefaultFormProperties(tenantId).getPageConfirmationTemplate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getConfirmationMessageExpression() throws InvalidFormDefinitionException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNextForm() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FormType getFormType() throws InvalidFormDefinitionException {
        if (isEditMode) {
            return FormType.entry;
        } else {
            return FormType.view;
        }
    }

}
