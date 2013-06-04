/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.view.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.console.common.client.user.User;
import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.view.common.URLUtils;
import org.bonitasoft.forms.client.view.common.URLUtilsFactory;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

/**
 * Logout widget
 * 
 * @author Anthony Birembaut
 */
public class UserLogoutWidget extends FlowPanel {

    protected User user;
    
    protected Map<String, Object> context;
    
    /**
     * Default constructor.
     * @param urlContext 
     */
    public UserLogoutWidget(final User user, Map<String, Object> urlContext) {
        super();
        this.user = user;
        this.context = urlContext;
        // set the css style name
        this.setStyleName("bonita_userHeader");
        this.add(buildUserIdentityCard());
    }

    /**
     * Build the widget
     * @param user the logged in user
     * @return the {@link Widget}
     */
    protected Widget buildUserIdentityCard() {
        
        final FlowPanel theIDWidget = new FlowPanel();
        theIDWidget.setStylePrimaryName("bonita_identification");
        
        final FlowPanel theLeftAlignedBox = new FlowPanel();
        theLeftAlignedBox.setStylePrimaryName("bonita_identified-left");
        
        theIDWidget.add(theLeftAlignedBox);
        
        final Image theAvatar = new Image("images/avatar-default.gif");
        theAvatar.setStyleName("bonita_avatar");
        
        theLeftAlignedBox.add(theAvatar);
        
        final FlowPanel theRightAlignedBox = new FlowPanel();
        theRightAlignedBox.setStylePrimaryName("bonita_identified-right");
        
        theLeftAlignedBox.add(theRightAlignedBox);
        
        HTML theUserIdentity = null;
        if (user.isAnonymous()) {
            theUserIdentity = new HTML(FormsResourceBundle.getMessages().anonymousLabel());
        } else {
            theUserIdentity = new HTML(user.getUsername());
        }
        theUserIdentity.setStylePrimaryName("bonita_identif-1");
        
        theRightAlignedBox.add(theUserIdentity);
        
        Anchor theLogoutLink = null;
        if (user.isAnonymous()) {
            theLogoutLink = new Anchor(FormsResourceBundle.getMessages().loginButtonLabel());
        } else {
            theLogoutLink = new Anchor(FormsResourceBundle.getMessages().logoutButtonLabel());
        }
        theLogoutLink.setStylePrimaryName("bonita_identif-2");
        
        final URLUtils urlUtils = URLUtilsFactory.getInstance();
        final List<String> paramsToRemove = new ArrayList<String>();
        paramsToRemove.add(URLUtils.LOCALE_PARAM);
        final List<String> hashParamsToRemove = new ArrayList<String>();
        if (user.isAutoLogin()) {
            hashParamsToRemove.add(URLUtils.AUTO_LOGIN_PARAM);
        } else {
            hashParamsToRemove.add(URLUtils.USER_CREDENTIALS_PARAM);
        }
        if (!user.isAnonymous()) {
            for (final Entry<String, Object> hashParamEntry : context.entrySet()) {
                hashParamsToRemove.add(hashParamEntry.getKey());
            }
        }
        Map<String, String> hashParamsToAdd = new HashMap<String, String>();
        hashParamsToAdd.put(URLUtils.TODOLIST_PARAM, "true");
        hashParamsToAdd.put(URLUtils.VIEW_MODE_PARAM, "app");
        final String theRedirectURL = urlUtils.rebuildUrl(paramsToRemove, null, hashParamsToRemove, hashParamsToAdd);
        String theURL = "?" + URLUtils.REDIRECT_URL_PARAM + "=";
        try {
            theURL += URL.encodeQueryString(theRedirectURL);
        } catch (final Exception e) {
            Window.alert("Unable to redirect to login page: Invalid URL");
            theURL += GWT.getModuleBaseURL();
        }
        theLogoutLink.setHref(theURL);

        theRightAlignedBox.add(theLogoutLink);

        return theIDWidget;
    }
}

