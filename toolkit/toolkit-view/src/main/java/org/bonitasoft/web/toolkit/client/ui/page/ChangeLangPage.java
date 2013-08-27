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
package org.bonitasoft.web.toolkit.client.ui.page;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nLocaleDefinition;
import org.bonitasoft.web.toolkit.client.common.i18n.model.I18nLocaleItem;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormFiller;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Option;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Select;
import org.bonitasoft.web.toolkit.client.ui.utils.I18n;

/**
 * @author Séverin Moussel
 */
public class ChangeLangPage extends Page {

    public static final String TOKEN = "changelang";

    @Override
    public void defineTitle() {
        this.setTitle(_("Choose a language"));
    }

    @Override
    public void buildView() {
        Form form = new Form();
        form.addSelectEntry(new JsId("lang"), _("Choose a language"), _("Choose a language"));
        form.addButton(new JsId("ok"), _("Ok"), _("Change to selected language"), new ChangeLangFormAction());
        form.addCancelButton();
        form.setFiller(new ChangeLangSelectFiller());
        addBody(form);
    }
   
    @Override
    public String defineToken() {
        return TOKEN;
    }

    /**
     * Action called to change portal language 
     */
    private final class ChangeLangFormAction extends FormAction {
        @Override
        public void execute() {
            final String choosenLang = this.getParameter("lang");

            I18n.getInstance().loadLocale(AbstractI18n.stringToLocale(choosenLang), new Action() {

                @Override
                public void execute() {
                    ClientApplicationURL.setLang(AbstractI18n.stringToLocale(choosenLang));
                    ViewController.closePopup();

                }
            });

        }
    }

    /**
     * Fill select box with available languages
     */
    private final class ChangeLangSelectFiller extends FormFiller {
        
        @Override
        protected void getData(final APICallback callback) {
            new I18nLocaleDefinition().getAPICaller().search(0, 0, callback);
        }
    
        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            List<I18nLocaleItem> items = new JSonItemReader().getItems(json, new I18nLocaleDefinition());
            List<Option> options = buildSelectOptions(items);
            ((Select) this.target.getEntry(new JsId("lang"))).refreshOptions(options);
        }

        private List<Option> buildSelectOptions(List<I18nLocaleItem> items) {
            List<Option> options = new ArrayList<Option>();
            for (I18nLocaleItem item : items) {
                options.add(buildSelectOption(item));
            }
            return options;
        }

        private Option buildSelectOption(final I18nLocaleItem item) {
            return new Option(item.getName(), item.getLocale(), item.getLocale().equals(ClientApplicationURL.getLang()));
        }
    }
}
