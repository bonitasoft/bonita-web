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
package org.bonitasoft.web.rest.server.engineclient;

/**
 * @author Vincent Elcrin
 *
 */
public class EngineClientFactory {

    private final EngineAPIAccessor apiAccessor;

    public EngineClientFactory(final EngineAPIAccessor apiAccessor) {
        this.apiAccessor = apiAccessor;
    }

    public ProfileEngineClient createProfileEngineClient() {
        return new ProfileEngineClient(apiAccessor.getProfileAPI());
    }

    public ProfileEntryEngineClient createProfileEntryEngineClient() {
        return new ProfileEntryEngineClient(apiAccessor.getProfileAPI());
    }

    public ProfileMemberEngineClient createProfileMemberEngineClient() {
        return new ProfileMemberEngineClient(apiAccessor.getProfileAPI());
    }

    public ProcessEngineClient createProcessEngineClient() {
        return new ProcessEngineClient(apiAccessor.getProcessAPI());
    }

    public CaseEngineClient createCaseEngineClient() {
        return new CaseEngineClient(apiAccessor.getProcessAPI());
    }

    public HumanTaskEngineClient createHumanTaskEngineClient() {
        return new HumanTaskEngineClient(apiAccessor.getProcessAPI());
    }

    public ActivityEngineClient createActivityEngineClient() {
        return new ActivityEngineClient(apiAccessor.getProcessAPI());
    }

    public UserEngineClient createUserEngineClient() {
        return new UserEngineClient(apiAccessor.getIdentityAPI());
    }

    public GroupEngineClient createGroupEngineClient() {
        return new GroupEngineClient(apiAccessor.getGroupAPI());
    }

    public FlowNodeEngineClient createFlowNodeEngineClient() {
        return new FlowNodeEngineClient(apiAccessor.getProcessAPI());
    }

    public PageEngineClient createPageEngineClient() {
        return new PageEngineClient(apiAccessor.getPageAPI());
    }

    public TenantManagementEngineClient createTenantManagementEngineClient() {
        return new TenantManagementEngineClient(apiAccessor.getTenantAdministrationAPI());
    }
}
