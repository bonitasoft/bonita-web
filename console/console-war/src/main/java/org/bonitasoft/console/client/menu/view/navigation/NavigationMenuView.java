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
package org.bonitasoft.console.client.menu.view.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.SHA1;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileEntryItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.eventbus.MainEventBus;
import org.bonitasoft.web.toolkit.client.eventbus.events.ChangeViewEvent;
import org.bonitasoft.web.toolkit.client.eventbus.events.ChangeViewHandler;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.page.Plugin;

/**
 * @author Vincent Elcrin
 */
public class NavigationMenuView extends RawView {

    private static final String MAIN_MENU_ID = "mainmenu";

    public static final String TOKEN = MAIN_MENU_ID;

    public static final String ONVIEWCHANGE_HANDLER_NAME = TOKEN + ".onviewchange";

    private NavigationMenu navigationMenu = new NavigationMenu(new JsId(MAIN_MENU_ID));

    private final MenuListCreator menuListCreator;

    private boolean isDefault;

    public NavigationMenuView(MenuListCreator menuListCreator) {
        this.menuListCreator = menuListCreator;
        MainEventBus.getInstance().unregisterNamedHandler(ONVIEWCHANGE_HANDLER_NAME);
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        isDefault();
        addBody(navigationMenu);
        getProfileEntries(ClientApplicationURL.getProfileId(), updateNavigationMenuOnCallback());
    }

    private void isDefault() {

        new APICaller(ProfileDefinition.get()).get(ClientApplicationURL.getProfileId(), new APICallback() {

            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {

                ProfileItem profile = (ProfileItem) JSonItemReader.parseItem(response, ProfileDefinition.get());
                if (Boolean.valueOf(profile.getAttributeValue(ProfileItem.ATTRIBUTE_IS_DEFAULT))) {
                    navigationMenu.addClass("notCustom");
                } else {
                    navigationMenu = (NavigationMenu) navigationMenu.removeClass("notCustom");
                }
            }
        });
    }

    private APICallback updateNavigationMenuOnCallback() {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                updateNavigationMenu(parseProfileEntries(response));
            }

        };
    }

    private void updateNavigationMenu(final List<ProfileEntryItem> items) {
        navigationMenu.addItems(menuListCreator.asList(items));
        listenViewChangeEvent(selectMenuOnChange());
        updateUI();
        navigationMenu.select(ViewController.getInstance().getCurrentPageToken());
    }

    private List<ProfileEntryItem> parseProfileEntries(final String response) {
        return JSonItemReader.parseItems(response, ProfileEntryDefinition.get());
    }

    private void getProfileEntries(String profileId, APICallback callback) {
        final Map<String, String> params = Collections.singletonMap(ProfileEntryItem.ATTRIBUTE_PROFILE_ID, profileId);
        ProfileEntryDefinition.get().getAPICaller()
                .search(0, 100, ProfileEntryItem.ATTRIBUTE_INDEX + " " + "ASC", null, params, Arrays.asList(ProfileEntryItem.ATTRIBUTE_PAGE), callback);
    }

    private void listenViewChangeEvent(ChangeViewHandler handler) {
        MainEventBus.getInstance().addNamedHandlerToSource(ChangeViewEvent.TYPE, ONVIEWCHANGE_HANDLER_NAME, ViewController.getInstance(), handler);
    }

    private ChangeViewHandler selectMenuOnChange() {
        return new ChangeViewHandler() {

            @Override
            public void onViewChange(final ChangeViewEvent event) {
                navigationMenu.select(getViewToken(event.getView()));
            }
        };
    }

    private String getViewToken(RawView view) {
        if (view instanceof Plugin) {
            return ((Plugin) view).getPluginToken();
        } else {
            return view.getToken();
        }
    }

}
