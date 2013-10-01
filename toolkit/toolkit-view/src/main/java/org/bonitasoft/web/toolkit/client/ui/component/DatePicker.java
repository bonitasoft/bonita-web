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
package org.bonitasoft.web.toolkit.client.ui.component;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.Date;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Paul AMAR
 * 
 */
public class DatePicker extends Component {

    private String defaultValue;

    private Date startDate = null;

    private Date endDate = null;

    /**
     * Default Constructor.
     * 
     * @param jsid
     */
    public DatePicker(final JsId jsid) {
        super(jsid);
    }

    public DatePicker(final JsId jsid, final String defaultValue) {
        this(jsid);
        this.defaultValue = defaultValue;
    }

    @Override
    protected Element makeElement() {
        if (this.startDate != null) {
            this.addJsOption("startDate", DateFormat.dateToForm(this.startDate));
        }
        if (this.endDate != null) {
            this.addJsOption("endDate", DateFormat.dateToForm(this.endDate));
        }

        this.addJsOption("format", _("mm/dd/yyyy"));
        this.addJsOption("months", getMonths());
        this.addJsOption("daysMin", getDaysMin());
        
        this.element = DOM.createInputText();
        this.element.addClassName("inputDate");
        this.element.setAttribute("name", getJsId().toString());
        this.element.setAttribute("maxlength", "10"); // yyyy-mm-dd
        
        if (this.defaultValue != null) {
            this.element.setAttribute("value", this.defaultValue);
        }
        return this.element;
    }

    private ArrayList<String> getMonths() {
        ArrayList<String> months = new ArrayList<String>();
        months.add(_("January"));
        months.add(_("February"));
        months.add(_("March"));
        months.add(_("April"));
        months.add(_("May"));
        months.add(_("June"));
        months.add(_("July"));
        months.add(_("August"));
        months.add(_("September"));
        months.add(_("October"));
        months.add(_("November"));
        months.add(_("December"));
        return months;
    }

    private ArrayList<String> getDaysMin() {
        ArrayList<String> daysMin = new ArrayList<String>();
        daysMin.add(_("Su"));
        daysMin.add(_("Mo"));
        daysMin.add(_("Tu"));
        daysMin.add(_("We"));
        daysMin.add(_("Th"));
        daysMin.add(_("Fr"));
        daysMin.add(_("Sa"));
        daysMin.add(_("Su"));
        return daysMin;
    }

    public String getValue() {
        return this.getValue(this.element);
    }

    private native String getValue(Element e)
    /*-{
        return $wnd.$(e).val();
    }-*/;

    public DatePicker setValue(final String value) {
        if (isGenerated()) {
            this.setValue(this.element, value);
        } else {
            this.defaultValue = value;
        }
        return this;
    }

    private native void setValue(Element e, String value)
    /*-{
        $wnd.$(e).val(value);
    }-*/;

    /**
     * @param startDate
     *            the startDate to set
     */
    public void setStartDate(final Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(final Date endDate) {
        this.endDate = endDate;
    }
}
