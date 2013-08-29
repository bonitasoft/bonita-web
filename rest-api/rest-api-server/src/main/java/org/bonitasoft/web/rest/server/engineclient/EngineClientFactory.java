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

import org.bonitasoft.engine.session.APISession;

/**
 * @author Vincent Elcrin
 * 
 */
public class EngineClientFactory {

    private EngineAPIAccessor apiAccessor;

    public EngineClientFactory(EngineAPIAccessor apiAccessor) {
        this.apiAccessor = apiAccessor;
    }

    public ProfileEngineClient createProfileEngineClient(APISession session) {
        return new ProfileEngineClient(apiAccessor.getProfileAPI(session));
    }

    public ProfileEntryEngineClient createProfileEntryEngineClient(APISession session) {
        return new ProfileEntryEngineClient(apiAccessor.getProfileAPI(session));
    }

    public ProfileMemberEngineClient createProfileMemberEngineClient(APISession session) {
        return new ProfileMemberEngineClient(apiAccessor.getProfileAPI(session));
    }

    public ProcessEngineClient createProcessEngineClient(APISession session) {
        return new ProcessEngineClient(apiAccessor.getProcessAPI(session));
    }
    
    public CaseEngineClient createCaseEngineClient(APISession session) {
        return new CaseEngineClient(apiAccessor.getProcessAPI(session));
    }
    
    public HumanTaskEngineClient createHumanTaskEngineClient(APISession session) {
        return new HumanTaskEngineClient(apiAccessor.getProcessAPI(session));
    }
    
    public ActivityEngineClient createActivityEngineClient(APISession session) {
        return new ActivityEngineClient(apiAccessor.getProcessAPI(session));
    }
    
    public UserEngineClient createUserEngineClient(APISession session) {
        return new UserEngineClient(apiAccessor.getIdentityAPI(session));
    }

    public GroupEngineClient createGroupEngineClient(APISession session) {
        return new GroupEngineClient(apiAccessor.getGroupAPI(session));
    }

}
