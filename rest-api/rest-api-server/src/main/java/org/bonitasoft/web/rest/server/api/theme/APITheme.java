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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.preferences.constants.WebBonitaConstantsUtils;
import org.bonitasoft.console.common.server.themes.ThemeConfigManager;
import org.bonitasoft.console.common.server.themes.ThemeManager;
import org.bonitasoft.console.common.server.themes.ThemeStructureException;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.theme.exception.ThemeDescriptorNotFoundException;
import org.bonitasoft.theme.model.ThemeDescriptor;
import org.bonitasoft.web.rest.api.model.portal.theme.ThemeDefinition;
import org.bonitasoft.web.rest.api.model.portal.theme.ThemeItem;
import org.bonitasoft.web.rest.server.api.ConsoleAPI;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.server.search.ItemSearchResult;

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
            final String themeName = ThemeConfigManager.getInstance(apiSession.getId()).getThemeNameById(Long.valueOf(id.toLong()));
            manager.applyTheme(apiSession.getId(), themeName, getThemesFolder(apiSession.getId()));
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
        final String themeName = ThemeConfigManager.getInstance(tenantId).getThemeNameById(id.toLong());
        ThemeDescriptor themeDescriptor;
        try {
            final ThemeManager manager = new ThemeManager();
            themeDescriptor = manager.getThemeDescriptor(themeName, getThemesFolder(tenantId));
            final String themeInfo = ThemeConfigManager.getInstance(tenantId).getThemeInfoByName(themeName);
            final String[] themeAttrs = themeInfo.split(":");
            final String currentTheme = ThemeConfigManager.getInstance(tenantId).getApplyTheme();
            ThemeItem themeItem = null;
            if (themeDescriptor != null) {
                if (currentTheme.equals(themeAttrs[1])) {
                    themeItem = new ThemeItem(themeAttrs[0], "/default/appliedLookFeel.png",
                            themeAttrs[1], themeDescriptor.getAuthor(), themeDescriptor.getDescription(), themeAttrs[2], themeAttrs[3],
                            "default".equals(themeAttrs[1]) ? "true" : "false");
                } else {
                    themeItem = new ThemeItem(themeAttrs[0], "/default/lookFeel.png",
                            themeAttrs[1], themeDescriptor.getAuthor(), themeDescriptor.getDescription(), themeAttrs[2], themeAttrs[3],
                            "default".equals(themeAttrs[1]) ? "true" : "false");
                }
            }
            return themeItem;
        } catch (final ThemeDescriptorNotFoundException e) {
            throw new APIException(e);
        } catch (final IOException e) {
            throw new APIException(e);
        }
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    public ItemSearchResult<ThemeItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {
        final APISession apiSession = getEngineSession();
        final List<ThemeItem> themeList = new ArrayList<ThemeItem>();
        final long tenantId = apiSession.getTenantId();
        File themesFolder = null;
        themesFolder = getThemesFolder(tenantId);
        if (themesFolder.exists()) {
            final List<String> themesIdList = ThemeConfigManager.getInstance(tenantId).getAllThemeId();
            if (themesIdList.indexOf(this.DEFAULT_THEME_ID) >= 0) {
                themesIdList.remove(this.DEFAULT_THEME_ID);
                themesIdList.add(0, this.DEFAULT_THEME_ID);
            }
            final int last = themesIdList.size() < (page + 1) * 20 ? themesIdList.size() : (page + 1) * 20;

            for (int i = 0; i < themesIdList.size(); i++) {// this loop is equivalent at the one in comment but it add the build of the all items list in one
                                                           // loop
                final String themeId = themesIdList.get(i);
                final ThemeItem themeItem = get(APIID.makeAPIID(themeId));
                if (i >= page * resultsByPage && i <= page * resultsByPage + resultsByPage) {// TODO voir pourquoi c'est ici et pourquoi 20 anciennement cf:
                                                                                             // themeapiimpl?
                    themeList.add(themeItem);
                }
            }
            /*
             * the previous loop
             * for (int i = page * 20; i < last; i++) {
             * final String themeId = themesIdList.get(i);
             * try {
             * final ThemeItem themeItem = get(APIID.makeAPIID(themeId));
             * themeList.add(themeItem);
             * } catch (final ThemeDescriptorNotFoundException e) {
             * throw new APIException(e);
             * } catch (final APIException e) {
             * throw new APIException(e);
             * }
             * }
             */
            return new ItemSearchResult<ThemeItem>(page, last, themesIdList.size(), themeList);
        } else {
            throw new APIException("Missing theme folder");
        }
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
