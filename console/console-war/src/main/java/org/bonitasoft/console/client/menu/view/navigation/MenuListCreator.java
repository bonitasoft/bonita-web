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
package org.bonitasoft.console.client.menu.view.navigation;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.data.item.attribute.reader.ProfileEntryNameAttributeReader;
import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuItem;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;

/**
 * @author Vincent Elcrin
 * 
 */
public class MenuListCreator {

    private LinkedHashMap<APIID, List<ProfileEntryItem>> orphelineEntries = new LinkedHashMap<APIID, List<ProfileEntryItem>>();;

    public LinkedList<MenuItem> asList(List<ProfileEntryItem> profiles) {
        LinkedHashMap<APIID, MenuItem> menu = new LinkedHashMap<APIID, MenuItem>();
        for (final ProfileEntryItem profile : profiles) {
            addProfileEntry(menu, profile);
        }
        return new LinkedList<MenuItem>(menu.values());
    }

    protected void addProfileEntry(LinkedHashMap<APIID, MenuItem> menu, final ProfileEntryItem entry) {
        if (isParentMenu(entry)) {
            menu.put(entry.getId(), createMenuItem(entry));
        } else if (parentExist(menu, entry)) {
            getParent(menu, entry).addLink(createLink(entry));
        } else {
            saveOrphelineEntry(entry);
        }
    }

    private MenuFolder getParent(LinkedHashMap<APIID, MenuItem> menu, final ProfileEntryItem entry) {
        return (MenuFolder) menu.get(entry.getParentId());
    }

    private boolean parentExist(final LinkedHashMap<APIID, MenuItem> menu, final ProfileEntryItem profileEntry) {
        return menu.containsKey(profileEntry.getParentId());
    }

    private boolean isParentMenu(final ProfileEntryItem profileEntry) {
        return profileEntry.getParentId() == null || profileEntry.getParentId().toLong() == 0;
    }

    private MenuItem createMenuItem(final ProfileEntryItem entry) {
        switch (entry.getType()) {
            case link:
                return createLink(entry);
            case folder:
                return createFolder(entry);
            default:
                throw new RuntimeException("Profile entry type <" + entry.getType() + "> not supported");
        }
    }

    private MenuLink createLink(final ProfileEntryItem entry) {
        saveFirstPageMet(entry);
        return new MenuLink(
                createJsId(entry),
                _(getLinkName(entry)),
                _(entry.getDescription()),
                getEntryUrlToken(entry));
    }

    private JsId createJsId(final ProfileEntryItem entry) {
        if (!StringUtil.isBlank(getEntryUrlToken(entry))) {
            return new JsId(getEntryUrlToken(entry));
        } else {
            return JsId.getRandom();
        }
    }

    private MenuItem createFolder(ProfileEntryItem entry) {
        MenuFolder folder = new MenuFolder(new JsId(entry.getName()), _(entry.getName()));
        adoptOrphelineEntries(folder, entry);
        return folder;
    }

    private void saveFirstPageMet(final ProfileEntryItem entry) {
        if (ClientApplicationURL.getPageToken() == null) {
            ClientApplicationURL.setPageToken(getEntryUrlToken(entry), true);
        }
    }

    private void saveOrphelineEntry(final ProfileEntryItem entry) {
        if (!orphelineEntries.containsKey(entry.getParentId())) {
            orphelineEntries.put(entry.getParentId(), new LinkedList<ProfileEntryItem>());
        }
        orphelineEntries.get(entry.getParentId()).add(entry);
    }

    private void adoptOrphelineEntries(final MenuFolder folder, final ProfileEntryItem entry) {
        if (orphelineEntries.containsKey(entry.getId())) {
            for (final ProfileEntryItem orphelineProfileEntry : orphelineEntries.remove(entry.getId())) {
                folder.addLink(createLink(orphelineProfileEntry));
            }
        }
    }

    private String getLinkName(final ProfileEntryItem entry) {
        if (!StringUtil.isBlank(entry.getName())) {
            return entry.getName();
        }

        return new ProfileEntryNameAttributeReader(ProfileEntryItem.ATTRIBUTE_NAME, ProfileEntryItem.ATTRIBUTE_PAGE, BonitaPageItem.ATTRIBUTE_DISPLAY_NAME)
                .read(entry);
    }

    private String getEntryUrlToken(final ProfileEntryItem entry) {
        String urlToken;
        if (entry.isCustom()) {
            urlToken = "custompage_".concat(entry.getCustomPage().getUrlToken());
        } else {
            urlToken = entry.getPage();
        }
        return urlToken;
    }
}
