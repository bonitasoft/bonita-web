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
package org.bonitasoft.console.client.mvp;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;

public class SimplePagination extends Composite {

    private int page = 0, pageSize = 0, total = 0;

    interface Binder extends UiBinder<HTMLPanel, SimplePagination> {

    }

    private static Binder binder = GWT.create(Binder.class);
    @UiField
    SpanElement label;

    @UiConstructor
    public SimplePagination(int page, int pageSize, int total) {
        initWidget(binder.createAndBindUi(this));
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        label.setInnerText(labelizeProgress());
    }

    public void setPage(int page) {
        this.page = page;
        label.setInnerText(labelizeProgress());
    }

    public String labelizeProgress() {
        if (pageSize == total) {
            return _("%start_result% of %total_results%",
                            new Arg("start_result", page),
                            new Arg("total_results", total));
        }
        return _("%start_result% - %end_result% of %total_results%",
                            new Arg("start_result", page),
                            new Arg("end_result", pageSize),
                            new Arg("total_results", total));
    }
}