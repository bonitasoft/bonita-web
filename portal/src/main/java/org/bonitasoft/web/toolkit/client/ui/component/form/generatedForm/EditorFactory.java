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

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.ui.component.form.FormNode;

/**
 * @author Nicolas Tith
 * 
 */
public class EditorFactory {

    private static final String NON_EDITABLE_TYPE = "nonEditableType";

    private static Map<String, FormNodeCreator> mapping = new HashMap<String, FormNodeCreator>();

    static {
        mapping.put(NON_EDITABLE_TYPE, new FormNonEditableCreator());
        mapping.put(EditableTypes.JAVA_STRING_CLASSNAME, new FormTextCreator());
        mapping.put(EditableTypes.JAVA_BOOLEAN_CLASSNAME, new FormBooleanCreator());
        mapping.put(EditableTypes.JAVA_LONG_CLASSNAME, new FormLongCreator());
        mapping.put(EditableTypes.JAVA_INTEGER_CLASSNAME, new FormLongCreator());
        mapping.put(EditableTypes.JAVA_DOUBLE_CLASSNAME, new FormDoubleCreator());
    }

    public FormNode createEntryFor(String type, ItemBinding item) {
        return getCreator(type).create(item);
    }

    public FormNode createEntryFor(String type, ItemBinding item, int maxLength) {
        return getCreator(type).create(item, maxLength);
    }

    /**
     * @param type
     * @return
     */
    private FormNodeCreator getCreator(String type) {
        FormNodeCreator fnc = mapping.get(type);
        if (fnc == null) {
            fnc = mapping.get(NON_EDITABLE_TYPE);
        }
        return fnc;
    }

    public boolean isTypeEditable(String type) {
        return mapping.containsKey(type);
    }

}
