/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.console.client.menu.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bonitasoft.console.client.common.system.view.PopupAboutPage;
import org.bonitasoft.console.client.data.item.attribute.reader.UserAttributeReader;
import org.bonitasoft.console.client.menu.view.navigation.MenuListCreator;
import org.bonitasoft.console.client.menu.view.navigation.NavigationMenuView;
import org.bonitasoft.console.client.menu.view.profile.ProfileMenuItem;
import org.bonitasoft.web.rest.model.identity.UserDefinition;
import org.bonitasoft.web.rest.model.identity.UserItem;
import org.bonitasoft.web.rest.model.portal.profile.ProfileDefinition;
import org.bonitasoft.web.rest.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.ApplicationFactoryClient;
import org.bonitasoft.web.toolkit.client.AvailableTokens;
import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.UrlBuilder;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.RawView;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowPopup;
import org.bonitasoft.web.toolkit.client.ui.component.EmptyImage;
import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.menu.Menu;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuFolder;
import org.bonitasoft.web.toolkit.client.ui.component.menu.MenuLink;
import org.bonitasoft.web.toolkit.client.ui.page.ChangeLangPage;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;
import org.bonitasoft.web.toolkit.client.ui.utils.Path;

import com.google.gwt.user.client.Window;

/**
 * @author Julien Mege, Vincent Elcrin
 */
public class LoginBox extends RawView {

    public static final String TOKEN = "LoginBox";

    protected static final String NAVIGATION_MENU = "menu";

    public static final String LOGOUT_URL = "../logoutservice";

    protected MenuFolder userNameMenu = new MenuFolder(new JsId("userName"), "initializing");

    private final Image userNameAvatar = new EmptyImage(0, 0);

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    public void buildView() {
        addBody(new Text(_("Welcome: ")).addClass("welcomeMessage"));
        addBody(createGreetings());
        addProfileMenu();
        addBody(createSettingsMenu());
    }

    /**
     * Overridden in SP
     */
    protected TechnicalUserMenuView getTechnicalUserMenu() {
        return new TechnicalUserMenuView();
    }

    /**
     * Overridden in SP
     */
    protected MenuListCreator getMenuListCreator() {
        return new MenuListCreator();
    }

    /**
     * Overridden in SP
     */
    protected LogoutUrl getLogOutUrl(final String locale) {
        return new LogoutUrl(new UrlBuilder(), locale);
    }

    protected void addProfileMenu() {
        if (isTechUser()) {
            loadTechUserProfileMenu();
        } else {
            addCurrentUserProfileMenu();
        }
    }

    private boolean isTechUser() {
        return "true".equals(Session.getParameter("is_technical_user"));
    }

    protected String getTechUserName() {
        return Session.getParameter("user_name");
    }

    protected Container<AbstractComponent> createGreetings() {
        return new Container<AbstractComponent>(new JsId("userData"))
                .append(createUserNameMenu().addClass("userName"))
                .append(userNameAvatar.addClass("userAvatar"));
    }

    protected void loadTechUserProfileMenu() {
        userNameMenu.setLabel(getTechUserName());
        ViewController.showView(getTechnicalUserMenu(), NAVIGATION_MENU);
        final TechnicalUserMessageHandler handler = new TechnicalUserMessageHandler();
        handler.check();
    }

    private void addCurrentUserProfileMenu() {
        userNameMenu.addFiller(new CurrentUserAvatarFiller());
        final ProfileMenuItem profileMenuItem = new ProfileMenuItem(getMenuListCreator());
        addBody(new Menu(profileMenuItem));
        getProfiles(Session.getUserId(), fillProfileMenuOnCallback(profileMenuItem));
    }

    private APICallback fillProfileMenuOnCallback(final ProfileMenuItem profileMenuItem) {
        return new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                updateProfileMenu(profileMenuItem, parseProfiles(response));
            }

        };
    }

    private void updateProfileMenu(final ProfileMenuItem profileMenuItem, final List<ProfileItem> profiles) {
        if (!profiles.isEmpty()) {
            ensureProfileId(profiles.get(0));
            profileMenuItem.addItems(profiles);
            for (final ProfileItem profile : profiles) {
                if (profile.getId().toString().equals(ClientApplicationURL.getProfileId())) {
                    loadNavigationMenu();
                    return;
                }
            }
        } else {
            profileMenuItem.setVisible(false);
        }
    }

    private List<ProfileItem> parseProfiles(final String response) {
        return JSonItemReader.parseItems(response, ProfileDefinition.get());
    }

    private void getProfiles(final APIID userId, final APICallback callback) {
        final Map<String, String> filter = Collections.singletonMap(ProfileItem.FILTER_USER_ID, userId.toString());
        new APICaller(ProfileDefinition.get()).search(0, 100, null, null, filter, callback);
    }

    private void ensureProfileId(final ProfileItem profile) {
        if (ClientApplicationURL.getProfileId() == null) {
            ClientApplicationURL.setProfileId(profile.getId().toString());
        }
    }

    private void loadNavigationMenu() {
        ViewController.showView(new NavigationMenuView(getMenuListCreator()), NAVIGATION_MENU);
    }

    private Menu createUserNameMenu() {
        if (!AvailableTokens.tokens.contains("logout.link.hidden")) {
            userNameMenu.addLink(createLogoutLink());
            userNameMenu.addClass("downArrow");
        }
        return new Menu(userNameMenu);
    }

    protected Menu createSettingsMenu() {
        return new Menu(new MenuFolder(new JsId("options"), _("Settings"),
                createLanguageLink(),
                createAboutLink()));
    }

    private MenuLink createLogoutLink() {
        return new MenuLink(_("Logout"), _("Logout"), new Action() {

            @Override
            public void execute() {
                Window.Location.replace(
                        getLogOutUrl(Session.getParameter(UrlOption.LANG))
                        .toString());
            }
        });
    }

    private MenuLink createLanguageLink() {
        return new MenuLink(_("Language"), _("Language"), new ActionShowPopup(ApplicationFactoryClient.getDefaultFactory()
                .defineViewTokens(ChangeLangPage.TOKEN)));
    }

    private MenuLink createAboutLink() {
        return new MenuLink(_("About"), _("About"), new ActionShowPopup(ApplicationFactoryClient.getDefaultFactory()
                .defineViewTokens(PopupAboutPage.TOKEN)));
    }

    /**
     * @author Séverin Moussel
     */
    private final class CurrentUserAvatarFiller extends Filler<MenuFolder> {

        @Override
        protected void getData(final APICallback callback) {
            new APICaller(UserDefinition.get()).get(Session.getUserId(), callback);
        }

        @Override
        protected void setData(final String json, final Map<String, String> headers) {
            final UserItem user = JSonItemReader.parseItem(json, UserDefinition.get());

            final String displayName = new UserAttributeReader().read(user);

            userNameMenu.setLabel(displayName);

            if (!StringUtil.isBlank(user.getIcon())) {
                userNameAvatar
                .setUrl(new Path(user.getIcon()))
                .setTooltip(displayName);
            }
        }
    }
}
