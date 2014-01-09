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
package org.bonitasoft.forms.client.view.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SimpleHtmlSanitizer;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.WidgetType;

/**
 * @author Vincent Elcrin
 */
public class LabelWidget extends HTML {

    interface Templates extends SafeHtmlTemplates {

        @SafeHtmlTemplates.Template(
                "<div class='bonita_form_label'>{0}</div>")
        SafeHtml label(SafeHtml label);

        @SafeHtmlTemplates.Template(
                "<div class='bonita_form_label'>" +
                        "<div class='bonita_richTextArea'>{0}</div></div>")
        SafeHtml richTextAreaLabel(SafeHtml label);
    }

    private static Templates TEMPLATES = GWT.create(Templates.class);

    private InlineLabel mandatoryWidget;

    public LabelWidget(ReducedFormWidget widgetData, String mandatoryFieldSymbol, String mandatoryFieldClasses) {
        if (widgetData.getLabel() != null && widgetData.getLabel().length() > 0) {
            if(WidgetType.RICH_TEXTAREA.equals(widgetData.getType())) {
                setHTML(TEMPLATES.richTextAreaLabel(getSafeHtml(widgetData)));
            } else {
                setHTML(TEMPLATES.label(getSafeHtml(widgetData)));
            }
        }

        // mandatory fields symbol display
        if (isMandatory(widgetData)) {
            mandatoryWidget = createMandatorySymbol(mandatoryFieldSymbol, mandatoryFieldClasses);
            DOM.appendChild(getElement(), mandatoryWidget.getElement());
        }
    }

    public void setMandatoryText(String mandatoryText) {
        mandatoryWidget.setText(mandatoryText);
    }

    private SafeHtml getSafeHtml(ReducedFormWidget widgetData) {
        if (widgetData.allowHTMLInLabel()) {
            return SafeHtmlUtils.fromTrustedString(widgetData.getLabel() + " ");
        } else {
            return SimpleHtmlSanitizer.sanitizeHtml(widgetData.getLabel() + " ");
        }
    }

    private boolean isMandatory(final ReducedFormWidget widgetData) {
        return !WidgetType.HIDDEN.equals(widgetData.getType()) && widgetData.isMandatory();
    }

    private InlineLabel createMandatorySymbol(String mandatoryFieldSymbol, String mandatoryFieldClasses) {
        InlineLabel mandatoryWidget = new InlineLabel();
        if (mandatoryFieldSymbol == null || mandatoryFieldSymbol.equals("#defaultMandatoryFieldSymbol")) {
            mandatoryFieldSymbol = FormsResourceBundle.getMessages().defaultMandatoryFieldSymbol();
        }
        mandatoryWidget.setText(mandatoryFieldSymbol);
        mandatoryWidget.setStyleName("bonita_form_mandatory");
        if (mandatoryFieldClasses != null && mandatoryFieldClasses.length() > 0) {
            mandatoryWidget.addStyleName(mandatoryFieldClasses);
        }
        return mandatoryWidget;
    }
}
