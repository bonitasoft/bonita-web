/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.ui.action.popup;

import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.action.ItemAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.view.DeleteItemPage;

/**
 * @author Julien Mege
 */
public class ItemDeletePopupAction extends ItemAction {

    public ItemDeletePopupAction(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    public ItemDeletePopupAction(final ItemDefinition itemDefinition, final String id) {
        this(null, null, itemDefinition, id);
    }

    public ItemDeletePopupAction(final String resourceName, final ItemDefinition itemDefinition) {
        this(null, null, itemDefinition, null);
    }

    public ItemDeletePopupAction(final String resourceName, final ItemDefinition itemDefinition, final String id) {
        this(resourceName, null, itemDefinition, id);
    }

    public ItemDeletePopupAction(final String resourceName, final String message, final ItemDefinition itemDefinition) {
        this(resourceName, message, itemDefinition, null);
    }

    public ItemDeletePopupAction(final String resourceName, final String message, final ItemDefinition itemDefinition, final String id) {
        super(itemDefinition);
        if (resourceName != null) {
            this.addParameter(DeleteItemPage.PARAM_RESOURCE_NAME, resourceName);
        }
        if (message != null) {
            this.addParameter(DeleteItemPage.PARAM_MESSAGE, message);
        }
        setItemId(id);
    }

    public void addRedirectTokenParameter(final String redirectToken) {
        this.addParameter(DeleteItemPage.PARAM_REDIRECT_TOKEN, redirectToken);
    }

    @Override
    public void execute() {
        ViewController.showPopup(ClientApplicationURL.TOKEN_DELETE, getParameters());
    }

    public void setMessage(String message) {
        addParameter(DeleteItemPage.PARAM_MESSAGE, message);
    }
    
    public void setTitle(String title) {
        addParameter(DeleteItemPage.PARAM_TITLE, title);
    }
    
    public void setConfirmButtonLabel(String label) {
        addParameter(DeleteItemPage.PARAM_CONFIRMBUTTON_LABEL, label);
    }
}
