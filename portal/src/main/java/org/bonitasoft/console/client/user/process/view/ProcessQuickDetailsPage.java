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
package org.bonitasoft.console.client.user.process.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.user.cases.view.snippet.ArchivedCaseWorkedOnSnippet;
import org.bonitasoft.console.client.user.cases.view.snippet.CaseWorkedOnSnippet;
import org.bonitasoft.console.client.user.cases.view.snippet.MyCasesSnippet;
import org.bonitasoft.console.client.user.process.action.CheckFormMappingAndDisplayProcessInstanciationFormAction;
import org.bonitasoft.console.client.user.task.view.PluginProcess;
import org.bonitasoft.web.rest.model.bpm.flownode.HumanTaskItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.ClosePopUpAction;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.button.ButtonPrimaryAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Paul AMAR
 * 
 */
public class ProcessQuickDetailsPage extends ItemQuickDetailsPage<ProcessItem> implements PluginProcess {

    public static String TOKEN = "processquickdetails";

    public static final List<String> PRIVILEGES = new ArrayList<String>();

    static {
        PRIVILEGES.add(ProcessListingPage.TOKEN);
    }

    public ProcessQuickDetailsPage() {
        super(ProcessDefinition.get());
    }

    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final ProcessItem item) {
        addToolbarLink(newStartButton(item));
        return new LinkedList<ItemDetailsAction>();
    }

    private Link newStartButton(final ProcessItem item) {
        final RawView view = new StartProcessFormPage();
        view.setParameters(StartProcessFormPage.getItemParams(item));
        final CheckFormMappingAndDisplayProcessInstanciationFormAction action = new CheckFormMappingAndDisplayProcessInstanciationFormAction(view);
        action.setOnFinish(new ClosePopUpAction());
        return new ButtonPrimaryAction("btn-start", _("Start"), _("Start this process"), action);
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProcessItem item) {
        return new LinkedList<ItemDetailsMetadata>();
    }

    @Override
    protected void defineTitle(final ProcessItem process) {
        setTitle(process.getDisplayName() + " (" + process.getVersion() + ")");

        addDescription(new DescriptionAttributeReader(HumanTaskItem.ATTRIBUTE_DISPLAY_DESCRIPTION, HumanTaskItem.ATTRIBUTE_DESCRIPTION)
                .read(process));
    }

    @Override
    protected void buildBody(final ProcessItem item) {
        addBody(new MyCasesSnippet(getItemId()).setNbLinesByPage(5).build());
        addBody(new CaseWorkedOnSnippet(item.getId()).setNbLinesByPage(5).build());
        addBody(new ArchivedCaseWorkedOnSnippet(item.getId()).setNbLinesByPage(5).build());
    }

    @Override
    protected List<String> defineDeploys() {
        final List<String> defineDeploys = new ArrayList<String>();
        defineDeploys.add(ProcessItem.ATTRIBUTE_DEPLOYED_BY_USER_ID);
        return defineDeploys;
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }

    @Override
    public String defineToken() {
        return TOKEN;

    }
}
