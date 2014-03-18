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
package org.bonitasoft.console.client.admin.process.view.section.configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bonitasoft.web.rest.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Colin PUY
 * 
 */
public class ProcessConfigurationStateResolver {

    private Set<String> problems;

    public ProcessConfigurationStateResolver(List<ProcessResolutionProblemItem> processResolutionErrors) {
        problems = getProblemsType(processResolutionErrors);
    }

    private Set<String> getProblemsType(List<ProcessResolutionProblemItem> processResolutionErrors) {
        HashSet<String> errors = new HashSet<String>();
        for (final IItem item : processResolutionErrors) {
            errors.add(((ProcessResolutionProblemItem) item).getTargetType());
        }
        return errors;
    }
    
    public boolean hasProblems() {
        return !problems.isEmpty();
    }

    public boolean areActorsResolved() {
        return !problems.contains(ProcessResolutionProblemItem.VALUE_STATE_TARGET_TYPE_ACTOR);
    }

    public ConfigurationState getActorsConfigurationState() {
        return getConfigurationState(areActorsResolved());
    }

    public boolean areConnectorsResolved() {
        return !problems.contains(ProcessResolutionProblemItem.VALUE_STATE_TARGET_TYPE_CONNECTOR);
    }

    public ConfigurationState getConnectorsConfigurationState() {
        return getConfigurationState(areConnectorsResolved());
    }

    public boolean areParametersResolved() {
        return !problems.contains(ProcessResolutionProblemItem.VALUE_STATE_TARGET_TYPE_PARAMETER);
    }

    public ConfigurationState getParametersConfigurationState() {
        return getConfigurationState(areParametersResolved());
    }

    private ConfigurationState getConfigurationState(boolean isResolved) {
        return isResolved ? ConfigurationState.RESOLVED : ConfigurationState.UNRESOLVED;
    }
}
