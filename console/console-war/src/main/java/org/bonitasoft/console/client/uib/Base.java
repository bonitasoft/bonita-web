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
package org.bonitasoft.console.client.uib;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHTML;

public class Base extends Composite implements HasHTML {

    protected Base () {

    }

    public void setClazz(String clazz) {
        addStyleName(clazz);
    }

    @Override
    public void setHTML(String html) {
        SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.append(SafeHtmlUtils.fromTrustedString(getElement().getInnerHTML()));
        builder.append(SafeHtmlUtils.fromTrustedString(html));
        getElement().setInnerHTML(builder.toSafeHtml().asString());
    }

    @Override
    public String getHTML() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException();
    }

}
