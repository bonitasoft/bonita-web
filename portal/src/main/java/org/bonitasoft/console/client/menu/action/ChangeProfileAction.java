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
package org.bonitasoft.console.client.menu.action;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.console.client.menu.view.navigation.MenuListCreator;
import org.bonitasoft.console.client.menu.view.navigation.NavigationMenuView;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ParameterStorageWithSessionStorage;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;

/**
 * @author Séverin Moussel
 *
 */
public final class ChangeProfileAction extends Action {

    private final String profileId;

    private final MenuFolder menu;

    private final IItem profileItem;

    private final MenuListCreator menuListCreator;

    public ChangeProfileAction(final IItem profileItem, final MenuFolder menu, final MenuListCreator menuListCreator) {
        this.profileItem = profileItem;
        this.menuListCreator = menuListCreator;
        profileId = profileItem.getId().toString();
        this.menu = menu;
    }

    @Override
    public void execute() {
        if (menu != null) {
            menu.setLabel(_(profileItem.getAttributeValue(ProfileItem.ATTRIBUTE_NAME)));
        }

        ParameterStorageWithSessionStorage.setParameter(UrlOption.PROFILE, profileId);
        ClientApplicationURL.setProfileId(profileId);
        // to use the default Token
        ClientApplicationURL.setPageToken(null, false);
        showMenuView();
    }

    private void showMenuView() {
        ViewController.showView(new NavigationMenuView(menuListCreator), "menu");
    }

}
