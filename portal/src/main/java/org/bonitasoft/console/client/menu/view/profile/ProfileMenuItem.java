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
package org.bonitasoft.console.client.menu.view.profile;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.List;

import org.bonitasoft.console.client.menu.action.ChangeProfileAction;
import org.bonitasoft.console.client.menu.view.navigation.MenuListCreator;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.CssId;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;

/**
 * @author Vincent Elcrin
 * 
 */
public class ProfileMenuItem extends MenuFolder {

    private static final String SELECT_MODE = "selectMode";
    private MenuListCreator menuListCreator;

    public ProfileMenuItem(MenuListCreator menuListCreator) {
        super(new JsId("profile"), _("Select profile"));
        this.menuListCreator = menuListCreator;
        configureMenu();
    }

    private void configureMenu() {
        addJsOption(SELECT_MODE, true);
    }

    public void addItems(List<ProfileItem> profileItems) {
        for (final IItem profile : profileItems) {
            ensureCurrentProfileName(profile);
            addMenu((ProfileItem) profile);
        }
    }

    private void ensureCurrentProfileName(final IItem profile) {
        if (isCurrentProfile((ProfileItem) profile)) {
            updateProfileLabel((ProfileItem) profile);
        }
    }

    private boolean isCurrentProfile(final ProfileItem profile) {
        return profile.getId().equals(APIID.makeAPIID(ClientApplicationURL.getProfileId()));
    }

    private void updateProfileLabel(final ProfileItem profile) {
        setLabel(_(profile.getName()));
    }

    private void addMenu(final ProfileItem profile) {
        addLink(createMenuLink(profile, _(profile.getName())));
    }

    private MenuLink createMenuLink(ProfileItem profile, final String profileName) {
        MenuLink menuLink = new MenuLink(
                profileName,
                profile.getAttributeValue(ProfileItem.ATTRIBUTE_DESCRIPTION),
                new ChangeProfileAction(profile, this, menuListCreator));
        menuLink.setLinkId(CssId.PROFILE_LINK_PREFIX + profile.getName().replaceAll("\\W", ""));
        return menuLink;
    }
}
