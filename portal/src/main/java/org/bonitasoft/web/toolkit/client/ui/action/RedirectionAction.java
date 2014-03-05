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
package org.bonitasoft.web.toolkit.client.ui.action;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.ClientApplicationURL;
import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.utils.PageToken;
import org.bonitasoft.web.toolkit.client.ui.utils.TypedString;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

import com.google.gwt.user.client.Window;

/**
 * @author Julien Mege
 * 
 */
public class RedirectionAction extends Action {

    private final TypedString token;

    private ClientApplicationURL.Target target = ClientApplicationURL.Target.ROOT;

    public RedirectionAction(Url relativeUrl) {
        token = relativeUrl;
    }
    
    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final TypedString token) {
        this.token = token;
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token) {
        this(new PageToken(token));
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final TypedString token, final ClientApplicationURL.Target target) {
        this(token);
        this.target = target;
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final ClientApplicationURL.Target target) {
        this(token);
        this.target = target;
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final Map<String, String> params) {
        this(token);
        this.setParameters(params);
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final Map<String, String> params, final ClientApplicationURL.Target target) {
        this(token, target);
        this.setParameters(params);
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final TreeIndexed<String> params) {
        this(token);
        this.setParameters(params);
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final TreeIndexed<String> params, final ClientApplicationURL.Target target) {
        this(token, target);
        this.setParameters(params);
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final Arg... params) {
        this(token);
        this.setParameters(params);
    }

    /**
     * @deprecated use ActionShowView or ActionShowPopup instead and add parameter in constructor of the page via addParameter method
     */
    public RedirectionAction(final String token, final ClientApplicationURL.Target target, final Arg... params) {
        this(token, target);
        this.setParameters(params);
    }

    @Override
    public void execute() {

        if (this.token instanceof PageToken) {

            switch (this.target) {
                case POPUP:
                    ViewController.showPopup(this.token.toString(), getParameters());
                    break;

                case ROOT:
                    ViewController.showView(this.token.toString(), getParameters());
                    break;
            }
        } else if (this.token instanceof Url) {
            Window.Location.replace(this.token.toString());
        } else {
            // TODO Convert Path to URL
        }
    }

}
