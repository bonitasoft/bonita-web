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
package org.bonitasoft.web.toolkit.client.ui.utils;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nTranslationDefinition;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nTranslationItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Séverin Moussel
 * 
 */
public class I18n extends AbstractI18n {

    public static I18n getInstance() {
        if (I18N_instance == null) {
            I18N_instance = new I18n();
        }
        return (I18n) I18N_instance;
    }

    public void loadLocale(final LOCALE locale, final Action callback) {
        final I18nTranslationDefinition definition = new I18nTranslationDefinition();

        final Map<String, String> filter = new HashMap<String, String>();
        if (locale != null) {
            filter.put("locale", locale.toString());
        } else {
            filter.put("locale", getDefaultLocale().toString());
        }

        new APICaller(definition).search(0, 0, "", "", filter, new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                final List<IItem> items = JSonItemReader.parseItems(response, definition);

                final Map<String, String> localeMap = new TreeMap<String, String>();

                for (final IItem item : items) {
                    localeMap.put(item.getAttributeValue(I18nTranslationItem.ATTRIBUTE_KEY), item.getAttributeValue(I18nTranslationItem.ATTRIBUTE_VALUE));
                }
                getInstance().setLocale(locale, localeMap);

                if (callback != null) {
                    callback.execute();
                }
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                if (callback != null) {
                    callback.execute();
                }
            }

        });
    }

    @Override
    public void loadLocale(final LOCALE locale) {
        this.loadLocale(locale, null);
    }

}
