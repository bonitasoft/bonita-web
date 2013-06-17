/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.console.server;

import org.bonitasoft.console.common.server.CommonRestAPIFactory;
import org.bonitasoft.console.common.server.api.system.APISession;
import org.bonitasoft.console.common.server.document.APIArchivedDocument;
import org.bonitasoft.console.common.server.document.APIDocument;
import org.bonitasoft.console.server.api.bpm.cases.APIArchivedCase;
import org.bonitasoft.console.server.api.bpm.cases.APIArchivedComment;
import org.bonitasoft.console.server.api.bpm.cases.APICase;
import org.bonitasoft.console.server.api.bpm.cases.APICaseDocument;
import org.bonitasoft.console.server.api.bpm.cases.APICaseVariable;
import org.bonitasoft.console.server.api.bpm.cases.APIComment;
import org.bonitasoft.console.server.api.bpm.connector.APIArchivedConnectorInstance;
import org.bonitasoft.console.server.api.bpm.connector.APIConnectorInstance;
import org.bonitasoft.console.server.api.bpm.flownode.APIActivity;
import org.bonitasoft.console.server.api.bpm.flownode.APIFlowNode;
import org.bonitasoft.console.server.api.bpm.flownode.APIHiddenUserTask;
import org.bonitasoft.console.server.api.bpm.flownode.APIHumanTask;
import org.bonitasoft.console.server.api.bpm.flownode.APITask;
import org.bonitasoft.console.server.api.bpm.flownode.APIUserTask;
import org.bonitasoft.console.server.api.bpm.flownode.archive.APIArchivedActivity;
import org.bonitasoft.console.server.api.bpm.flownode.archive.APIArchivedFlowNode;
import org.bonitasoft.console.server.api.bpm.flownode.archive.APIArchivedHumanTask;
import org.bonitasoft.console.server.api.bpm.flownode.archive.APIArchivedTask;
import org.bonitasoft.console.server.api.bpm.flownode.archive.APIArchivedUserTask;
import org.bonitasoft.console.server.api.bpm.process.APIActor;
import org.bonitasoft.console.server.api.bpm.process.APIActorMember;
import org.bonitasoft.console.server.api.bpm.process.APICategory;
import org.bonitasoft.console.server.api.bpm.process.APIProcess;
import org.bonitasoft.console.server.api.bpm.process.APIProcessCategory;
import org.bonitasoft.console.server.api.bpm.process.APIProcessConnector;
import org.bonitasoft.console.server.api.bpm.process.APIProcessConnectorDependency;
import org.bonitasoft.console.server.api.bpm.process.APIProcessResolutionProblem;
import org.bonitasoft.console.server.api.organization.APIGroup;
import org.bonitasoft.console.server.api.organization.APIMembership;
import org.bonitasoft.console.server.api.organization.APIPersonalContactData;
import org.bonitasoft.console.server.api.organization.APIProfessionalContactData;
import org.bonitasoft.console.server.api.organization.APIRole;
import org.bonitasoft.console.server.api.organization.APIUser;
import org.bonitasoft.console.server.api.theme.impl.APITheme;
import org.bonitasoft.console.server.api.userXP.profile.APIProfile;
import org.bonitasoft.console.server.api.userXP.profile.APIProfileEntry;
import org.bonitasoft.console.server.api.userXP.profile.APIProfileMember;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.server.API;
import org.bonitasoft.web.toolkit.server.api.system.APII18nLocale;
import org.bonitasoft.web.toolkit.server.api.system.APII18nTranslation;

/**
 * @author Séverin Moussel
 * 
 */
public class ConsoleRestAPIFactory extends CommonRestAPIFactory {

    @Override
    public API<? extends IItem> defineApis(final String apiToken, final String resourceToken) {

        if ("identity".equals(apiToken)) {
            if ("user".equals(resourceToken)) {
                return new APIUser();
            } else if ("role".equals(resourceToken)) {
                return new APIRole();
            } else if ("group".equals(resourceToken)) {
                return new APIGroup();
            } else if ("membership".equals(resourceToken)) {
                return new APIMembership();
            } else if ("professionalcontactdata".equals(resourceToken)) {
                return new APIProfessionalContactData();
            } else if ("personalcontactdata".equals(resourceToken)) {
                return new APIPersonalContactData();
            }
        } else if ("system".equals(apiToken)) {
            if ("i18nlocale".equals(resourceToken)) {
                return new APII18nLocale();
            } else if ("i18ntranslation".equals(resourceToken)) {
                return new APII18nTranslation();
            } else if ("session".equals(resourceToken)) {
                return new APISession();
            }
        } else if ("userXP".equals(apiToken)) {
            if ("profile".equals(resourceToken)) {
                return new APIProfile();
            } else if ("profileEntry".equals(resourceToken)) {
                return new APIProfileEntry();
            } else if ("profileMember".equals(resourceToken)) {
                return new APIProfileMember();
            } else if ("theme".equals(resourceToken)) {
                return new APITheme();
            }
        } else if ("bpm".equals(apiToken)) {
            if ("humanTask".equals(resourceToken)) {
                return new APIHumanTask();
            } else if ("userTask".equals(resourceToken)) {
                return new APIUserTask();
            } else if ("archivedHumanTask".equals(resourceToken)) {
                return new APIArchivedHumanTask();
            } else if ("archivedUserTask".equals(resourceToken)) {
                return new APIArchivedUserTask();
            } else if ("process".equals(resourceToken)) {
                return new APIProcess();
            } else if ("category".equals(resourceToken)) {
                return new APICategory();
            } else if ("processCategory".equals(resourceToken)) {
                return new APIProcessCategory();
            } else if ("processConnector".equals(resourceToken)) {
                return new APIProcessConnector();
            } else if ("case".equals(resourceToken)) {
                return new APICase();
            } else if ("archivedCase".equals(resourceToken)) {
                return new APIArchivedCase();
            } else if ("comment".equals(resourceToken)) {
                return new APIComment();
            } else if ("archivedComment".equals(resourceToken)) {
                return new APIArchivedComment();
            } else if ("document".equals(resourceToken)) {
                return new APIDocument();
            } else if ("archiveddocument".equals(resourceToken)) {
                return new APIArchivedDocument();
            } else if ("actor".equals(resourceToken)) {
                return new APIActor();
            } else if ("actorMember".equals(resourceToken)) {
                return new APIActorMember();
            } else if ("delegation".equals(resourceToken)) {
                return new APIActorMember();
            } else if ("hiddenUserTask".equals(resourceToken)) {
                return new APIHiddenUserTask();
            } else if ("activity".equals(resourceToken)) {
                return new APIActivity();
            } else if ("archivedActivity".equals(resourceToken)) {
                return new APIArchivedActivity();
            } else if ("task".equals(resourceToken)) {
                return new APITask();
            } else if ("archivedTask".equals(resourceToken)) {
                return new APIArchivedTask();
            } else if ("flowNode".equals(resourceToken)) {
                return new APIFlowNode();
            } else if ("archivedFlowNode".equals(resourceToken)) {
                return new APIArchivedFlowNode();
            } else if ("processResolutionProblem".equals(resourceToken)) {
                return new APIProcessResolutionProblem();
            } else if ("caseDocument".equals(resourceToken)) {
                return new APICaseDocument();
            } else if ("connectorInstance".equals(resourceToken)) {
                return new APIConnectorInstance();
            } else if ("archivedConnectorInstance".equals(resourceToken)) {
                return new APIArchivedConnectorInstance();
            } else if ("processConnectorDependency".equals(resourceToken)) {
                return new APIProcessConnectorDependency();
            } else if ("caseVariable".equals(resourceToken)) {
                return new APICaseVariable();
            }
        } 
        return super.defineApis(apiToken, resourceToken);
    }
}
