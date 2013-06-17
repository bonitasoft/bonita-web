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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.rest.api.model.bpm.process.ProcessItem;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessResolutionProblemDefinition;
import org.bonitasoft.web.rest.api.model.bpm.process.ProcessResolutionProblemItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Definition;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;


/**
 * @author Colin PUY
 *
 */
public class ConfigurationSection extends Section {

    public ConfigurationSection(ProcessItem process) {
        super(new JsId("configuration"), _("Configuration"));
        final ContainerStyled<Definition> definitions = new ContainerStyled<Definition>();
        definitions.addFiller(new ResolutionFiller(process));

        addBody(definitions);
    }

    private class ResolutionFiller extends Filler<ContainerStyled<Definition>> {

        private final ProcessItem process;

        public ResolutionFiller(final ProcessItem process) {
            super();
            this.process = process;
        }

        @Override
        protected void getData(final APICallback callback) {
            ProcessResolutionProblemDefinition.get().getAPICaller()
                    .search(0, 100, null, null, MapUtil.asMap(new Arg(ProcessResolutionProblemItem.FILTER_PROCESS_ID, this.process.getId())), callback);
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final List<ProcessResolutionProblemItem> processResolutionErrors = JSonItemReader.parseItems(json, ProcessResolutionProblemDefinition.get());
            fillResolutionProblems(new ProcessConfigurationStateResolver(processResolutionErrors), this.target);
        }

    }

    /**
     * Overridden in SP
     * @param processResolutionErrors
     */
    protected void fillResolutionProblems(ProcessConfigurationStateResolver stateResolver, final ContainerStyled<Definition> target) {
        // Add actors definition component
        final String actorsDescription = stateResolver.areActorsResolved() ? _("Resolved") : _("Unresolved");
        final Definition actorsDefinition = new Definition(_("Actors: %ok_ko%", new Arg("ok_ko", "")), actorsDescription);
        actorsDefinition.addClass(stateResolver.areActorsResolved() ? "ok" : "ko");
        actorsDefinition.setTooltip(actorsDescription);

        // Add connectors definition component
        final String connectorsDescription = stateResolver.areConnectorsResolved() ? _("Resolved") : _("Unresolved");
        final Definition connectorsDefinition = new Definition(_("Connectors: %ok_ko%", new Arg("ok_ko", "")), connectorsDescription);
        connectorsDefinition.addClass(stateResolver.areConnectorsResolved() ? "ok" : "ko");
        connectorsDefinition.setTooltip(connectorsDescription);

        // Insert definitions
        target.prepend(actorsDefinition, connectorsDefinition);
    }

}
