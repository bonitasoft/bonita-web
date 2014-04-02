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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import org.bonitasoft.web.toolkit.client.ui.JsId;

import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public class Text extends Input {

    public static final long DEFAULT_MAX_LENGTH = 50L;

    private String placeholder = null;

    private long maxLength = DEFAULT_MAX_LENGTH;

    public Text(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example) {
        super(jsid, label, tooltip, defaultValue, description, example);
        addClass("text");
    }

    public Text(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description, final String example,
            final Long maxLength) {
        super(jsid, label, tooltip, defaultValue, description, example);
        if (maxLength != null) {
            setMaxLength(maxLength);
        }
        addClass("text");
    }

    public Text(final JsId jsid, final String label, final String tooltip, final String defaultValue, final String description) {
        this(jsid, label, tooltip, defaultValue, description, null);
    }

    public Text(final JsId jsid, final String label, final String tooltip, final String defaultValue) {
        this(jsid, label, tooltip, defaultValue, null, null);
    }

    public Text(final JsId jsid, final String label, final String tooltip) {
        this(jsid, label, tooltip, null, null, null);
    }

    @Override
    protected String getInputType() {
        return "text";
    }

    public void setPlaceholder(final String pPlaceholder) {
        placeholder = pPlaceholder;
    }

    @Override
    protected Element makeInput(final String uid) {
        final Element input = super.makeInput(uid);
        input.setAttribute("maxlength", String.valueOf(maxLength));

        if (placeholder != null) {
            input.setAttribute("placeholder", placeholder);
        }

        return input;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (getInputElement() != null) {
            if (enabled == false) {
                getInputElement().setAttribute("disabled", "disabled");
            } else {
                getInputElement().removeAttribute("disabled");
            }
        }
    }

    public long getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(long maxLength) {
        this.maxLength = maxLength;
    }
}
