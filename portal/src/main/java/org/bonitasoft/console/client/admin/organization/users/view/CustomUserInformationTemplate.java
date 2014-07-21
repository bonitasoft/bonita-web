/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import org.bonitasoft.console.client.mvp.TemplateRepeat;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;

public class CustomUserInformationTemplate extends TemplateRepeat<CustomUserInfoItem> {

    interface Template extends SafeHtmlTemplates {

        @Template("<div class=\"formentry mandatory text\">" +
                "<div class=\"label\"><label title=\"{2}\">{1}</label></div>" +
                "<div class=\"input\"><input type=\"text\" name=\"{1}\" maxlength=\"255\" value=\"{3}\" tabindex=\"{0}\"></div>" +
                "</div>")
        public SafeHtml editable(int line, String name, String description, String value);

        @Template("<div class=\"definition _1 odd\"><label title=\"{1}\">{0}: </label><span>{2}</span></div>")
        public SafeHtml readOnly(String name, String description, String value);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);

    private boolean editable;

    public CustomUserInformationTemplate(boolean editable) {
        super("formentry");
        this.editable = editable;
    }

    @Override
    public SafeHtml render(Context context, CustomUserInfoItem information) {
        CustomUserInfoDefinitionItem definition = information.getDefinition();
        if (editable) {
            return TEMPLATE.editable(context.getIndex() + 1, definition.getName(),
                    definition.getDescription(), information.getValue());
        }
        return TEMPLATE.readOnly(definition.getName(), definition.getDescription(), information.getValue());
    }

}
