/**
 * Copyright (C) 2013 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.form.generatedForm;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Fieldset;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Radio;

/**
 * @author Nicolas Tith
 * 
 */
public class FormBooleanCreator implements FormNodeCreator {

    @Override
    public FormNode create(ItemBinding item) {
        String valueTrue = "true";
        String valueFalse = "false";
        Boolean checkedTrue = valueTrue.equals(item.getValue());
        Boolean checkedFalse = !checkedTrue;
        Radio radio = new Radio(new JsId(item.getAttributeName()), _("true"), item.getTooltip(), valueTrue, "", checkedTrue);
        Radio radio2 = new Radio(new JsId(item.getAttributeName()), _("false"), item.getTooltip(), valueFalse, "", checkedFalse);

        return (FormNode) new Fieldset(new JsId(item.getAttributeName() + "_fieldset"), item.getLabel()).append(
                radio
                , radio2
                );
    }

    public FormNode create(ItemBinding item, int maxLength) {
        return create(item);
    }
}
