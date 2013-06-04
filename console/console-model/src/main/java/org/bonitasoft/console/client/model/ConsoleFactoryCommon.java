/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.model;

import org.bonitasoft.console.client.model.bpm.cases.ArchivedCaseDefinition;
import org.bonitasoft.console.client.model.bpm.cases.ArchivedCommentDefinition;
import org.bonitasoft.console.client.model.bpm.cases.CaseDefinition;
import org.bonitasoft.console.client.model.bpm.cases.CaseDocumentDefinition;
import org.bonitasoft.console.client.model.bpm.cases.CaseVariableDefinition;
import org.bonitasoft.console.client.model.bpm.cases.CommentDefinition;
import org.bonitasoft.console.client.model.bpm.connector.ArchivedConnectorInstanceDefinition;
import org.bonitasoft.console.client.model.bpm.connector.ConnectorInstanceDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ActivityDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ArchivedActivityDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ArchivedFlowNodeDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ArchivedHumanTaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ArchivedTaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.ArchivedUserTaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.FlowNodeDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.HiddenUserTaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.HumanTaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.TaskDefinition;
import org.bonitasoft.console.client.model.bpm.flownode.UserTaskDefinition;
import org.bonitasoft.console.client.model.bpm.process.ActorDefinition;
import org.bonitasoft.console.client.model.bpm.process.ActorMemberDefinition;
import org.bonitasoft.console.client.model.bpm.process.CategoryDefinition;
import org.bonitasoft.console.client.model.bpm.process.DelegationDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessActorPrivilegeDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessCategoryDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessConnectorDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessConnectorDependencyDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessDefinition;
import org.bonitasoft.console.client.model.bpm.process.ProcessResolutionProblemDefinition;
import org.bonitasoft.console.client.model.bpm.process.UserActorPrivilegeDefinition;
import org.bonitasoft.console.client.model.identity.GroupDefinition;
import org.bonitasoft.console.client.model.identity.MembershipDefinition;
import org.bonitasoft.console.client.model.identity.PersonalContactDataDefinition;
import org.bonitasoft.console.client.model.identity.ProfessionalContactDataDefinition;
import org.bonitasoft.console.client.model.identity.RoleDefinition;
import org.bonitasoft.console.client.model.identity.UserDefinition;
import org.bonitasoft.console.client.model.monitoring.report.ReportDefinition;
import org.bonitasoft.console.client.model.portal.profile.ProfileDefinition;
import org.bonitasoft.console.client.model.portal.profile.ProfileEntryDefinition;
import org.bonitasoft.console.client.model.portal.profile.ProfileMemberDefinition;
import org.bonitasoft.console.client.model.portal.theme.ThemeDefinition;
import org.bonitasoft.console.common.client.CommonModelFactory;
import org.bonitasoft.console.common.client.document.model.ArchivedDocumentDefinition;
import org.bonitasoft.console.common.client.document.model.DocumentDefinition;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Yongtao Guo
 */
public class ConsoleFactoryCommon extends CommonModelFactory {

    @Override
    public ItemDefinition<?> defineItemDefinitions(final String token) {

        // organization
        if (UserDefinition.TOKEN.equals(token)) {
            return new UserDefinition();
        } else if (PersonalContactDataDefinition.TOKEN.equals(token)) {
            return new PersonalContactDataDefinition();
        } else if (ProfessionalContactDataDefinition.TOKEN.equals(token)) {
            return new ProfessionalContactDataDefinition();
        } else if (RoleDefinition.TOKEN.equals(token)) {
            return new RoleDefinition();
        } else if (GroupDefinition.TOKEN.equals(token)) {
            return new GroupDefinition();
        } else if (MembershipDefinition.TOKEN.equals(token)) {
            return new MembershipDefinition();
        }

        // bpm.process
        else if (ProcessDefinition.TOKEN.equals(token)) {
            return new ProcessDefinition();
        } else if (ProcessConnectorDefinition.TOKEN.equals(token)) {
            return new ProcessConnectorDefinition();
        } else if (ProcessConnectorDependencyDefinition.TOKEN.equals(token)) {
            return new ProcessConnectorDependencyDefinition();
        } else if (ProcessCategoryDefinition.TOKEN.equals(token)) {
            return new ProcessCategoryDefinition();
        } else if (ActorDefinition.TOKEN.equals(token)) {
            return new ActorDefinition();
        } else if (ActorMemberDefinition.TOKEN.equals(token)) {
            return new ActorMemberDefinition();
        } else if (CategoryDefinition.TOKEN.equals(token)) {
            return new CategoryDefinition();
        } else if (DelegationDefinition.TOKEN.equals(token)) {
            return new DelegationDefinition();
        } else if (ProcessActorPrivilegeDefinition.TOKEN.equals(token)) {
            return new ProcessActorPrivilegeDefinition();
        } else if (UserActorPrivilegeDefinition.TOKEN.equals(token)) {
            return new UserActorPrivilegeDefinition();
        } else if (ProcessResolutionProblemDefinition.TOKEN.equals(token)) {
            return new ProcessResolutionProblemDefinition();
        } 

        // bpm.cases
        else if (CaseDefinition.TOKEN.equals(token)) {
            return new CaseDefinition();
        } else if (CommentDefinition.TOKEN.equals(token)) {
            return new CommentDefinition();
        } else if (ArchivedCommentDefinition.TOKEN.equals(token)) {
            return new ArchivedCommentDefinition();
        } else if (ArchivedCaseDefinition.TOKEN.equals(token)) {
            return new ArchivedCaseDefinition();
        } else if (DocumentDefinition.TOKEN.equals(token)) {
            return new DocumentDefinition();
        } else if (ArchivedDocumentDefinition.TOKEN.equals(token)) {
            return new ArchivedDocumentDefinition();
        } else if (CaseVariableDefinition.TOKEN.equals(token)) {
            return new CaseVariableDefinition();
        }

        // bpm.flownode
        else if (FlowNodeDefinition.TOKEN.equals(token)) {
            return new FlowNodeDefinition();
        } else if (ActivityDefinition.TOKEN.equals(token)) {
            return new ActivityDefinition();
        } else if (TaskDefinition.TOKEN.equals(token)) {
            return new TaskDefinition();
        } else if (HumanTaskDefinition.TOKEN.equals(token)) {
            return new HumanTaskDefinition();
        } else if (UserTaskDefinition.TOKEN.equals(token)) {
            return new UserTaskDefinition();
        } else if (HiddenUserTaskDefinition.TOKEN.equals(token)) {
            return new HiddenUserTaskDefinition();
        }

        // bpm.flownode.archive
        else if (ArchivedFlowNodeDefinition.TOKEN.equals(token)) {
            return new ArchivedFlowNodeDefinition();
        } else if (ArchivedActivityDefinition.TOKEN.equals(token)) {
            return new ArchivedActivityDefinition();
        } else if (ArchivedTaskDefinition.TOKEN.equals(token)) {
            return new ArchivedTaskDefinition();
        } else if (ArchivedHumanTaskDefinition.TOKEN.equals(token)) {
            return new ArchivedHumanTaskDefinition();
        } else if (ArchivedUserTaskDefinition.TOKEN.equals(token)) {
            return new ArchivedUserTaskDefinition();
        } else if (HiddenUserTaskDefinition.TOKEN.equals(token)) {
            return new HiddenUserTaskDefinition();
        }

        // system
        else if (ProfileDefinition.TOKEN.equals(token)) {
            return new ProfileDefinition();
        } else if (ProfileEntryDefinition.TOKEN.equals(token)) {
            return new ProfileEntryDefinition();
        } else if (ProfileMemberDefinition.TOKEN.equals(token)) {
            return new ProfileMemberDefinition();
        } else if (ThemeDefinition.TOKEN.equals(token)) {
            return new ThemeDefinition();
        } else if (SessionDefinition.TOKEN.equals(token)) {
            return new SessionDefinition();
        } else if (ArchivedCaseDefinition.TOKEN.equals(token)) {
            return new ArchivedCaseDefinition();
        } else if (DocumentDefinition.TOKEN.equals(token)) {
            return new DocumentDefinition();
        } else if (CaseDocumentDefinition.TOKEN.equals(token)) {
            return new CaseDocumentDefinition();
        } else if (ArchivedDocumentDefinition.TOKEN.equals(token)) {
            return new ArchivedDocumentDefinition();
        } else if (ProcessActorPrivilegeDefinition.TOKEN.equals(token)) {
            return new ProcessActorPrivilegeDefinition();
        } else if (UserActorPrivilegeDefinition.TOKEN.equals(token)) {
            return new UserActorPrivilegeDefinition();
        } else if (ConnectorInstanceDefinition.TOKEN.equals(token)) {
            return new ConnectorInstanceDefinition();
        } else if (ArchivedConnectorInstanceDefinition.TOKEN.equals(token)) {
            return new ArchivedConnectorInstanceDefinition();
        }

        // monitoring
        else if (ReportDefinition.TOKEN.equals(token)) {
            return new ReportDefinition();
        }

        // default
        else {
            return super.defineItemDefinitions(token);
        }
    }

}
