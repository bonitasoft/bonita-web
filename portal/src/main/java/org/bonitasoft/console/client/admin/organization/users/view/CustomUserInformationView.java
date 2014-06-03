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

import java.util.List;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import org.bonitasoft.console.client.mvp.Paginate;
import org.bonitasoft.console.client.mvp.Repeater;
import org.bonitasoft.console.client.mvp.SimplePagination;
import org.bonitasoft.console.client.mvp.event.DirtyInputEvent;
import org.bonitasoft.console.client.mvp.event.DirtyInputHandler;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;

public class CustomUserInformationView extends Composite {

    private SimplePagination pagination;

    private Paginate paginate;

    public CustomUserInformationView(final CustomUserInformationModel model) {
        this(model, false);
    }

    public CustomUserInformationView(final CustomUserInformationModel model, boolean editable) {
        CustomUserInformationTemplate template = new CustomUserInformationTemplate(editable);
        final Repeater<CustomUserInfoItem> repeater = new Repeater<CustomUserInfoItem>(template);

        final FlowPanel panel = new FlowPanel();
        panel.add(repeater);
        initWidget(panel);

        paginate = model.search(0, 10, new CustomUserInformationModel.Callback() {

            @Override
            void onSuccess(List<CustomUserInfoItem> information, int page, int pageSize, int total) {
                repeater.setRowData(information);
                if (pagination == null) {
                    pagination = new SimplePagination(page, pageSize, total, paginate);
                    panel.add(pagination);
                }
                pagination.setPage(page);
            }
        });
        template.listen(new DirtyInputHandler<CustomUserInfoItem>() {

            @Override
            public void onDirtyInput(DirtyInputEvent<CustomUserInfoItem> event) {
                model.update(event.getItem(), event.getInput().getValue());
            }
        });
    }
}