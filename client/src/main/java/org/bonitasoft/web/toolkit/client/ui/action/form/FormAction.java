/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.ui.action.form;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.form.AbstractForm;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public abstract class FormAction extends Action {

    protected AbstractForm form = null;

    public FormAction(final AbstractForm form) {
        super();
        this.form = form;
    }

    public FormAction() {
        super();
    }

    public void setForm(final AbstractForm form) {
        this.form = form;
    }

    protected AbstractForm getForm() {
        return this.form;
    }

    @Override
    public final void onStart() {
        if (!uploadFinished(this.form.getElement())) {
            addError(_("Please wait for your files to be uploaded."));
        }
    }

    private native boolean uploadFinished(Element form)
    /*-{
        return $wnd.$(form).autoUpload('finished');
    }-*/;

    protected String getParameter(final JsId jsid) {
        return this.form.getEntryValue(jsid);
    }

    @Override
    public String getParameter(final String name) {
        return this.form.getEntryValue(new JsId(name));
    }

    @Override
    public boolean hasParameter(final String name) {
        return this.getParameter(name) != null;
    }

    @Override
    public boolean hasParameter(final String name, final String value) {
        return value.equals(this.getParameter(name));
    }

    @Override
    public TreeIndexed<String> getParameters() {
        return this.form.getValues();
    }

    @Override
    public String getParameter(final String name, final String defaultValue) {
        String result = this.form.getEntryValue(new JsId(name));
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

    @Override
    public List<String> getArrayParameter(final String name) {
        return this.form.getEntryArrayValue(new JsId(name));
    }

    @Override
    public List<String> getArrayParameter(final String name, final List<String> defaultValue) {
        List<String> result = this.form.getEntryArrayValue(new JsId(name));
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

}
