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
package org.bonitasoft.web.rest.server.api.theme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.themes.ThemeManager;
import org.bonitasoft.console.common.server.themes.ThemeStructureException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.web.rest.model.portal.theme.ThemeDefinition;
import org.bonitasoft.web.rest.model.portal.theme.ThemeItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Nicolas Tith
 * 
 */
public class APITheme extends ConsoleAPI<ThemeItem> {

    /**
     * Default theme name
     */
    private final String DEFAULT_THEME_ID = "0";

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(ThemeDefinition.TOKEN);
    }

    @Override
    public ThemeItem update(final APIID id, final Map<String, String> attributes) {
        final APISession apiSession = getEngineSession();
        final ThemeManager manager = new ThemeManager();
        try {
            manager.applyTheme(id.toLong(), getThemesFolder(apiSession.getId()));
            return get(id);
        } catch (final ThemeStructureException e) {
            throw new APIException(e);
        } catch (final FileNotFoundException e) {
            throw new APIException(e);
        } catch (final IOException e) {
            throw new APIException(e);
        }
    }

    @Override
    public ThemeItem get(final APIID id) {
        final long tenantId = getEngineSession().getTenantId();
        // TODO call the engine (ThemeDatastore) and build the theme item
        return new ThemeItem();
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    /**
     * @param themesFolder
     * @return
     */
    private File getThemesFolder(final long tenantId) {
        return WebBonitaConstantsUtils.getInstance(tenantId).getConsoleThemeFolder();
    }

    @Override
    public void delete(final List<APIID> ids) {
        // TODO Auto-generated method stub
        super.delete(ids);
    }

    @Override
    protected void fillDeploys(final ThemeItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final ThemeItem item, final List<String> counters) {
    }

}
