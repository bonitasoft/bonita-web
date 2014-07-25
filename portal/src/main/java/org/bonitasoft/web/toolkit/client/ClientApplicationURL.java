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
package org.bonitasoft.web.toolkit.client;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.LOCALE;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.json.JSonUnserializerClient;
import org.bonitasoft.web.toolkit.client.common.session.SessionDefinition;
import org.bonitasoft.web.toolkit.client.common.url.UrlOption;
import org.bonitasoft.web.toolkit.client.common.url.UrlSerializer;
import org.bonitasoft.web.toolkit.client.common.url.UrlUnserializer;
import org.bonitasoft.web.toolkit.client.data.api.APICaller;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.utils.I18n;
import org.bonitasoft.web.toolkit.client.ui.utils.Loader;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;

/**
 * @author Anthony Birembaut, Julien Mege
 */
public class ClientApplicationURL {

    // Function, using GWT Generator Implementation
    // public abstract void createViewTokens();
    public static enum Target {
        POPUP, ROOT
    }

    protected static final String DEFAULT_LANG = "en";

    /**
     * The cookie name for the forms locale
     */
    public static final String FORM_LOCALE_COOKIE_NAME = "BOS_Locale";

    public static final String TOKEN_FORM_APP = "perform";

    public static final String TOKEN_EDIT = "edit";

    public static final String TOKEN_ADD = "add";

    public static final String TOKEN_DELETE = "delete";

    protected static final String ATTRIBUTE_TOKEN = UrlOption.PAGE;

    protected static final String ATTRIBUTE_LANG = UrlOption.LANG;

    protected static final String ATTRIBUTE_PROFILE = UrlOption.PROFILE;

    protected static final String ATTRIBUTE_TENANT = "tenant";

    private TreeIndexed<String> attributes = new TreeIndexed<String>();

    private static ClientApplicationURL self = null;

    /**
     * Default Constructor.
     */
    public ClientApplicationURL() {
        super();
        // Singleton
        self = this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // URL PARSING
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected TreeIndexed<String> parseToken() {
        return UrlUnserializer.unserializeTreeNodeIndexed(History.getToken());
    }

    protected void parseUrl() {
        // If Token or profile disappeared, keep the previous one
        final String token = _getPageToken();
        final String profileId = _getProfileId();

        attributes = parseToken();

        // If Token or profile disappeared, keep the previous one
        if (token != null && _getPageToken() == null) {
            this._setPageToken(token);
        }
        if (profileId != null && _getProfileId() == null) {
            this._setProfileId(profileId);
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected String _getPageToken() {
        return attributes.getValue(ATTRIBUTE_TOKEN);
    }

    protected String _getLang() {
        return attributes.getValue(ATTRIBUTE_LANG);
    }

    protected String _getProfileId() {
        return attributes.getValue(ATTRIBUTE_PROFILE);
    }

    protected String _getTenantId() {
        return Window.Location.getParameter(ATTRIBUTE_TENANT);
    }

    private void _setPageToken(final String pageToken) {
        this._setPageToken(pageToken, false);
    }

    private void _setPageToken(final String pageToken, final boolean refresh) {
        if (pageToken == null) {
            attributes.removeNode(ATTRIBUTE_TOKEN);
        } else {
            attributes.addValue(ATTRIBUTE_TOKEN, pageToken);
        }

        if (refresh) {
            this._refreshUrl();
        }
    }

    private void _setProfileId(final String profileId) {
        this._setProfileId(profileId, false);
    }

    private void _setProfileId(final String profileId, final boolean refresh) {
        if (profileId == null) {
            attributes.removeNode(ATTRIBUTE_PROFILE);
        } else {
            attributes.addValue(ATTRIBUTE_PROFILE, profileId);
        }

        if (refresh) {
            this._refreshUrl();
        }
    }

    public static TreeIndexed<String> getPageAttributes() {
        return self._getPageAttributes();
    }

    private TreeIndexed<String> _getPageAttributes() {
        final TreeIndexed<String> result = attributes.copy();
        result.removeNode(ATTRIBUTE_LANG);
        result.removeNode(ATTRIBUTE_PROFILE);
        result.removeNode(ATTRIBUTE_TOKEN);
        return result;
    }

    private void _setPageAttributes(final TreeIndexed<String> attributes, final boolean refresh) {
        final String token = _getPageToken();
        final String profileId = _getProfileId();

        this.attributes = attributes.copy();

        if (token != null) {
            this._setPageToken(token, false);
        }
        if (profileId != null) {
            this._setProfileId(profileId, false);
        }

        if (refresh) {
            this._refreshUrl();
        }
    }

    private void _addAttribute(final String key, final Tree<String> values) {
        self.attributes.addNode(key, values);
    }

    private void _addAttribute(final String key, final String... value) {
        self.attributes.addValue(key, value);
    }

    private void _removeAttribute(final String key) {
        self.attributes.removeNode(key);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS (static)
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getPageToken() {
        return self._getPageToken();
    }

    public static String getLang() {
        return self._getLang();
    }

    public static String getProfileId() {
        return self._getProfileId();
    }

    public static String getTenantId() {
        return self._getTenantId();
    }

    public static void setPageToken(final String pageToken) {
        self._setPageToken(pageToken, false);
    }

    public static void setPageToken(final String pageToken, final boolean refresh) {
        self._setPageToken(pageToken, refresh);
    }

    public static void setPageAttributes(final TreeIndexed<String> params) {
        self._setPageAttributes(params, false);
    }

    public static void setPageAttributes(final TreeIndexed<String> params, final boolean refresh) {
        self._setPageAttributes(params, refresh);
    }

    public static void setLang(final LOCALE lang) {
        setLang(lang, true);
    }

    private static void setLang(final LOCALE lang, final boolean refresh) {

        Session.addParameter(ATTRIBUTE_LANG, lang.toString());
        ParametersStorageWithCookie.addParameter(ATTRIBUTE_LANG, lang.toString());
        Cookies.setCookie(FORM_LOCALE_COOKIE_NAME, lang.toString());
        AbstractI18n.setDefaultLocale(lang);

        if (refresh) {
            Window.Location.reload();
        }
    }

    public static void setProfileId(final String profileId) {
        self._setProfileId(profileId, false);
    }

    public static void setProfileId(final String profileId, final boolean refresh) {
        self._setProfileId(profileId, refresh);
    }

    private void _refreshUrl() {
        this._refreshUrl(true);
    }

    private void _refreshUrl(final boolean refreshView) {

        if (parseToken().equals(attributes)) {
            // Same URL attributes, do nothing
            return;
        }

        History.newItem("?" + UrlSerializer.serialize(attributes), false);

        if (refreshView) {
            refreshView();
        }
    }

    private void _refresh() {
        refreshView();
    }

    public static void refreshUrl() {
        self._refreshUrl();
    }

    public static void refreshUrl(final boolean refreshView) {
        self._refreshUrl(refreshView);
    }

    public static void refresh() {
        self._refresh();
    }

    public static void addAttribute(final String key, final Tree<String> value) {
        self._addAttribute(key, value);
    }

    public static void addAttribute(final String key, final String... value) {
        self._addAttribute(key, value);
    }

    public static void removeAttribute(final String key) {
        self._removeAttribute(key);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT AND REFRESH
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected void initSession(final Action callback) {
        new APICaller(new SessionDefinition()).get("unusedId", new APICallback() {

            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {
                if (headers.get("X-Bonita-API-Token") != null) {
                    UserSessionVariables.addUserVariable(UserSessionVariables.API_TOKEN, headers.get("X-Bonita-API-Token"));
                }
                final IItem session = JSonItemReader.parseItem(response, new SessionDefinition());
                for (final String name : session.getAttributeNames()) {
                    if (name.equals("conf")) {
                        AvailableTokens.tokens.addAll(((Tree<String>) JSonUnserializerClient.unserializeTree(session.getAttributeValue(name))).getValues());
                        // Session.addParameter(name, ((Tree<String>) JSonUnserializerClient.unserializeTree(session.getAttributeValue(name))).getValues());
                    } else {
                        Session.addParameter(name, session.getAttributeValue(name));
                    }
                }
                // TODO Add here assertions on parameters mandatory for the toolkit
                // Example : assert Session.getUserId() != null;
                callback.execute();
            }
        });
    }

    protected void initLang(final Action callback) {
        // Check if the lang is in the URL
        String newLang = parseToken().getValue(ATTRIBUTE_LANG);

        // Clean
        if (newLang != null) {
            // TODO remove lang from URL and replace last history URL
        } else {
            // Check if the lang is in the session
            newLang = Session.getParameter(FORM_LOCALE_COOKIE_NAME);

            if (newLang == null) {
                // Check if the lang is in the cookie
                newLang = ParametersStorageWithCookie.getParameter(FORM_LOCALE_COOKIE_NAME);

                if (newLang == null) {
                    // else we set the default lang
                    newLang = DEFAULT_LANG;
                }
            }
        }

        // Save the currentLang
        if (!newLang.toString().equals(Session.getParameter(ATTRIBUTE_LANG))) {
            Session.addParameter(ATTRIBUTE_LANG, newLang.toString());
            ParametersStorageWithCookie.addParameter(ATTRIBUTE_LANG, newLang.toString());
        }

        // Load i18n data
        final LOCALE currentLocale = AbstractI18n.stringToLocale(Session.getParameter(ATTRIBUTE_LANG));
        AbstractI18n.setDefaultLocale(currentLocale);
        I18n.getInstance().loadLocale(currentLocale, callback);
    }

    // First display of a view
    protected void initView() {
        // Hide global loader
        Loader.hideLoader();

        if (_getPageToken() != null && _getProfileId() != null) {
            ViewController.showView(_getPageToken(), _getPageAttributes());
        }
    }

    protected void refreshView() {
        if (History.getToken().contains("_p=")) {
            parseUrl();
            ViewController.showView(_getPageToken(), _getPageAttributes());
        }
    }
}
