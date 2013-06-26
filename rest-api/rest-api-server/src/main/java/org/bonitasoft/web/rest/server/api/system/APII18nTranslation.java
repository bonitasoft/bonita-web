/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.api.system;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.common.server.i18n.I18n;
import org.bonitasoft.web.rest.server.framework.API;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nTranslationDefinition;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nTranslationItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 */
public class APII18nTranslation extends API<I18nTranslationItem> {

    @Override
    protected ItemDefinition defineItemDefinition() {
        return Definitions.get(I18nTranslationDefinition.TOKEN);
    }

    @Override
    public String defineDefaultSearchOrder() {
        return "";
    }

    @Override
    public ItemSearchResult<I18nTranslationItem> search(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters) {

        final Map<String, String> translations = I18n.getInstance().getLocale(AbstractI18n.stringToLocale(filters.get("locale")));
        final LinkedList<I18nTranslationItem> items = new LinkedList<I18nTranslationItem>();

        for (final String key : translations.keySet()) {
            items.add(new I18nTranslationItem(key, translations.get(key)));
        }

        return new ItemSearchResult<I18nTranslationItem>(page * resultsByPage, resultsByPage, new Long(items.size()).longValue(), items);

    }

    @Override
    protected void fillDeploys(final I18nTranslationItem item, final List<String> deploys) {
    }

    @Override
    protected void fillCounters(final I18nTranslationItem item, final List<String> counters) {
    }

}
