/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.server.validator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.bonitasoft.forms.client.model.FormFieldValue;

/**
 * Example of page validator used to test the Form validation API
 * @author Anthony Birembaut
 *
 */
public class DateOrderTestPageValidator implements IFormPageValidator{

    /* (non-Javadoc)
     * @see org.bonitasoft.forms.server.validator.IFormPageValidator#validate(java.util.Map)
     */
    public boolean validate(Map<String, FormFieldValue> fieldValues, Locale locale) {
        
        try {
            FormFieldValue fieldInput1 = fieldValues.get("fieldId1");
            String formatPattern1 = fieldInput1.getFormat();
            DateFormat df1 = new SimpleDateFormat(formatPattern1, locale);
            Date date1 = df1.parse((String)fieldInput1.getValue());
            
            FormFieldValue fieldInput2 = fieldValues.get("fieldId2");
            String formatPattern2 = fieldInput1.getFormat();
            DateFormat df2 = new SimpleDateFormat(formatPattern2, locale);
            Date date2 = df2.parse((String)fieldInput2.getValue());
            
            if (date1.compareTo(date2) < 0) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
    }

    /* (non-Javadoc)
     * @see org.bonitasoft.forms.server.validator.IFormPageValidator#getDisplayName()
     */
    public String getDisplayName() {
        return "Date Order validator";
    }
}
