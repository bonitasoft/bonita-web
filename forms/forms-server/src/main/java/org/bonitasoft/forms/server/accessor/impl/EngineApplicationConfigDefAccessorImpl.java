/**
 * Copyright (C) 2011 BonitaSoft S.A.
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

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.utils.BPMEngineAPIUtil;
import org.bonitasoft.console.common.server.utils.BPMEngineException;
import org.bonitasoft.engine.bpm.process.ProcessDeploymentInfo;
import org.bonitasoft.engine.exception.BonitaException;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.forms.client.model.Expression;
import org.bonitasoft.forms.server.accessor.DefaultFormsPropertiesFactory;
import org.bonitasoft.forms.server.accessor.IApplicationConfigDefAccessor;
import org.bonitasoft.forms.server.exception.InvalidFormDefinitionException;
import org.bonitasoft.forms.server.provider.impl.util.FormServiceProviderUtil;

/**
 * @author Haojie Yuan, Yongtao Guo, Anthony Birembaut
 */
public class EngineApplicationConfigDefAccessorImpl implements IApplicationConfigDefAccessor {

    /**
     * The process definition ID of the process to which this instance is associated
     */
    protected ProcessDeploymentInfo processDeploymentInfo = null;

    /**
     * The engine API session
     */
    protected APISession session;

    /**
     * Util class allowing to work with the BPM engine API
     */
    protected BPMEngineAPIUtil bpmEngineAPIUtil = new BPMEngineAPIUtil();

    /**
     * Logger
     */
    private static Logger LOGGER = Logger.getLogger(EngineApplicationConfigDefAccessorImpl.class.getName());

    /**
     * @param processDefinitionID
     *            The process definition UUID of the process to which this instance is associated. This
     *            parameter is allowed be null because an instance of this class should be available to retrieve the default
     *            process template to display error pages
     */
    public EngineApplicationConfigDefAccessorImpl(final APISession session, final long processDefinitionID) {
        try {
            this.session = session;
            if (processDefinitionID != -1) {
                processDeploymentInfo = bpmEngineAPIUtil.getProcessAPI(session).getProcessDeploymentInfo(processDefinitionID);
            }
        } catch (final BPMEngineException e) {
            final String message = "Error while invoking the engine";
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        } catch (final BonitaException e) {
            final String message = "Unable to retrieve the process definition with ID " + processDefinitionID;
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, message, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationErrorTemplate() {
        return DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).getPageErrorTemplate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getApplicationLabelExpression() throws InvalidFormDefinitionException {
        final Expression expression = new Expression("getApplicationLabelExpression", processDeploymentInfo.getName(), ExpressionType.TYPE_CONSTANT.name(),
                String.class.getName(), null, null);
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getApplicationMandatoryLabelExpression() {
        final String mandatoryLabel = DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).getApplicationMandatoryLabel();
        final Expression expression = new Expression("getApplicationMandatoryLabelExpression", mandatoryLabel, ExpressionType.TYPE_CONSTANT.name(),
                String.class.getName(), null, null);
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Expression getApplicationMandatorySymbolExpression() {
        final String mandatorySymbol = DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).getApplicationMandatorySymbol();
        final Expression expression = new Expression("getApplicationMandatorySymbolExpression", mandatorySymbol, ExpressionType.TYPE_CONSTANT.toString(),
                String.class.getName(), null, null);
        return expression;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationMandatorySymbolStyle() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationName() throws InvalidFormDefinitionException {
        return processDeploymentInfo.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationVersion() throws InvalidFormDefinitionException {
        if (processDeploymentInfo != null) {
            return processDeploymentInfo.getVersion();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationLayout() {
        return DefaultFormsPropertiesFactory.getDefaultFormProperties(session.getTenantId()).getApplicationLayout();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationPermissions() {
        if (processDeploymentInfo != null) {
            return FormServiceProviderUtil.PROCESS_UUID + "#" + processDeploymentInfo.getName() + "--" + processDeploymentInfo.getVersion();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMigrationProductVersion() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProductVersion() {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getApplicationFormsList() {
        return Collections.emptyList();
    }

}
