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
package org.bonitasoft.web.rest.server;

import java.util.logging.Logger;

import org.bonitasoft.web.rest.server.api.bpm.cases.APIArchivedCase;
import org.bonitasoft.web.rest.server.api.bpm.cases.APIArchivedComment;
import org.bonitasoft.web.rest.server.api.bpm.cases.APICase;
import org.bonitasoft.web.rest.server.api.bpm.cases.APICaseDocument;
import org.bonitasoft.web.rest.server.api.bpm.cases.APICaseVariable;
import org.bonitasoft.web.rest.server.api.bpm.cases.APIComment;
import org.bonitasoft.web.rest.server.api.bpm.connector.APIArchivedConnectorInstance;
import org.bonitasoft.web.rest.server.api.bpm.connector.APIConnectorInstance;
import org.bonitasoft.web.rest.server.api.bpm.flownode.APIActivity;
import org.bonitasoft.web.rest.server.api.bpm.flownode.APIFlowNode;
import org.bonitasoft.web.rest.server.api.bpm.flownode.APIHiddenUserTask;
import org.bonitasoft.web.rest.server.api.bpm.flownode.APIHumanTask;
import org.bonitasoft.web.rest.server.api.bpm.flownode.APITask;
import org.bonitasoft.web.rest.server.api.bpm.flownode.APIUserTask;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.APIArchivedActivity;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.APIArchivedFlowNode;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.APIArchivedHumanTask;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.APIArchivedTask;
import org.bonitasoft.web.rest.server.api.bpm.flownode.archive.APIArchivedUserTask;
import org.bonitasoft.web.rest.server.api.bpm.process.APIActor;
import org.bonitasoft.web.rest.server.api.bpm.process.APIActorMember;
import org.bonitasoft.web.rest.server.api.bpm.process.APICategory;
import org.bonitasoft.web.rest.server.api.bpm.process.APIProcess;
import org.bonitasoft.web.rest.server.api.bpm.process.APIProcessCategory;
import org.bonitasoft.web.rest.server.api.bpm.process.APIProcessConnector;
import org.bonitasoft.web.rest.server.api.bpm.process.APIProcessConnectorDependency;
import org.bonitasoft.web.rest.server.api.bpm.process.APIProcessResolutionProblem;
import org.bonitasoft.web.rest.server.api.document.APIArchivedDocument;
import org.bonitasoft.web.rest.server.api.document.APIDocument;
import org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoDefinition;
import org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoUser;
import org.bonitasoft.web.rest.server.api.organization.APICustomUserInfoValue;
import org.bonitasoft.web.rest.server.api.organization.APIGroup;
import org.bonitasoft.web.rest.server.api.organization.APIMembership;
import org.bonitasoft.web.rest.server.api.organization.APIPersonalContactData;
import org.bonitasoft.web.rest.server.api.organization.APIProfessionalContactData;
import org.bonitasoft.web.rest.server.api.organization.APIRole;
import org.bonitasoft.web.rest.server.api.organization.APIUser;
import org.bonitasoft.web.rest.server.api.platform.APIPlatform;
import org.bonitasoft.web.rest.server.api.profile.APIProfile;
import org.bonitasoft.web.rest.server.api.profile.APIProfileEntry;
import org.bonitasoft.web.rest.server.api.profile.APIProfileMember;
import org.bonitasoft.web.rest.server.api.system.APII18nLocale;
import org.bonitasoft.web.rest.server.api.system.APII18nTranslation;
import org.bonitasoft.web.rest.server.api.system.APISession;
import org.bonitasoft.web.rest.server.engineclient.CustomUserInfoEngineClientCreator;
import org.bonitasoft.web.rest.server.framework.API;
import org.bonitasoft.web.rest.server.framework.RestAPIFactory;
import org.bonitasoft.web.toolkit.client.common.exception.api.APINotFoundException;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Séverin Moussel
 * 
 */
public class BonitaRestAPIFactory extends RestAPIFactory {
	
	private static Logger LOGGER = Logger.getLogger(BonitaRestAPIFactory.class.getName());
	
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
        } else if("customuserinfo".equals(apiToken)) {
            if("definition".equals(resourceToken)) {
                return new APICustomUserInfoDefinition(new CustomUserInfoEngineClientCreator());
            } else if("user".equals(resourceToken)) {
                return new APICustomUserInfoUser(new CustomUserInfoEngineClientCreator());
            } else if("value".equals(resourceToken)) {
                return new APICustomUserInfoValue(new CustomUserInfoEngineClientCreator());
            }
        } else if ("system".equals(apiToken)) {
            if ("i18nlocale".equals(resourceToken)) {
                return new APII18nLocale();
            } else if ("i18ntranslation".equals(resourceToken)) {
                return new APII18nTranslation();
            } else if ("session".equals(resourceToken)) {
                return new APISession();
            }
            
        // FIXME : userXP deprecated    (BS-500)
        //    - replaced by 'portal'
        //    - Do not add any API here
        //    - userXP section must be deleted in 6.4.0 version
        //    - duplication not removed because userXp must stay like this
        } else if ("userXP".equals(apiToken)) {
            if ("profile".equals(resourceToken)) {
            	LOGGER.warning("Deprecated API path, please use /API/portal/profile instead");
                return new APIProfile();
            } else if ("profileEntry".equals(resourceToken)) {
            	LOGGER.warning("Deprecated API path, please use /API/portal/profileEntry instead");
                return new APIProfileEntry();
            } else if ("profileMember".equals(resourceToken)) {
            	LOGGER.warning("Deprecated API path, please use /API/portal/profileMember instead");
                return new APIProfileMember();
            }
        // --------------------------------------------------------
            
        } else if ("portal".equals(apiToken)) {
        	if ("profile".equals(resourceToken)) {
                return new APIProfile();
            } else if ("profileEntry".equals(resourceToken)) {
                return new APIProfileEntry();
            } else if ("profileMember".equals(resourceToken)) {
                return new APIProfileMember();
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
            } else if ("document".equals(resourceToken)) {
                return new APIDocument();
            } else if ("archiveddocument".equals(resourceToken)) {
                return new APIArchivedDocument();
            }
        } else if ("platform".equals(apiToken)) {
            if ("platform".equals(resourceToken)) {
                return new APIPlatform();
            }
        } 
        throw new APINotFoundException(apiToken, resourceToken);
    }
}
