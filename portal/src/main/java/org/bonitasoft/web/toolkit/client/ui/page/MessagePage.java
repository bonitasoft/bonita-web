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
package org.bonitasoft.web.toolkit.client.ui.page;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.Page;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.popup.PopupCloseAction;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;

/**
 * @author Séverin Moussel
 * 
 */
public class MessagePage extends Page implements MessageTyped {

    public static final String TOKEN = "message";

    private final TYPE type;

    private final Action callback;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor
     * 
     * @param type
     *            The type of the message to display. The type will change the design of the message.
     * @param message
     *            The message to display. New lines will be kept on display.
     * @param callback
     *            The action to call while clicking on Ok (or YES for CONFIRM type)
     */
    public MessagePage(final TYPE type, final String message, final Action callback) {
        this(type, callback);
        addParameter("message", message);
    }

    /**
     * Constructor
     * 
     * @param type
     *            The type of the message to display. The type will change the design of the message.
     * @param message
     *            The message to display. New lines will be kept on display.
     */
    public MessagePage(final TYPE type, final String message) {
        this(type, message, (Action) null);
    }

    /**
     * Constructor
     * 
     * @param type
     *            The type of the message to display. The type will change the design of the message.
     * @param callback
     *            The action to call while clicking on Ok (or YES for CONFIRM type)
     */
    public MessagePage(final TYPE type, final Action callback) {
        super();

        this.type = type;

        if (this.type.equals(TYPE.CONFIRM) && callback == null) {
            throw new IllegalArgumentException("Callback for message type CONFIRM mustn't be NULL");
        }
        this.callback = callback == null ? new PopupCloseAction() : callback;
    }

    /**
     * Constructor
     * 
     * @param type
     *            The type of the message to display. The type will change the design of the message.
     */
    public MessagePage(final TYPE type) {
        this(type, (Action) null);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PAGES OVERRIDE
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void defineTitle() {
        switch (this.type) {
            case ALERT:
                setTitle(_("Alert"));
                break;
            case CONFIRM:
                setTitle(_("Confirm"));
                break;
            case ERROR:
                setTitle(_("Error"));
                break;
            case INFORMATION:
                setTitle(_("Notice"));
                break;
            case SUCCESS:
                setTitle(_("Success"));
                break;
            case WARNING:
                setTitle(_("Warning"));
                break;
        }

        addClass(this.type.toString());
    }

    

    @Override
    public String defineToken() {
        return TOKEN;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BODY
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void buildView() {
        addBody(new Text(getParameter("message")));

        final Container<Button> formactions = new Container<Button>();
        addBody(formactions.addClass("formactions"));

        if (TYPE.CONFIRM.equals(this.type)) {
            formactions.append(
                    new Button(_("Yes"), _("Confirm this action"), this.callback),
                    new Button(_("No"), _("Cancel this action"), new PopupCloseAction()));
        } else {
            formactions.append(new Button(_("Ok"), _("Cancel this action"), this.callback));

        }
    }
}
