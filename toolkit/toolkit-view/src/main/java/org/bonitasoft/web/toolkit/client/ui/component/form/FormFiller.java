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
package org.bonitasoft.web.toolkit.client.ui.component.form;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.http.HttpException;
import org.bonitasoft.web.toolkit.client.common.exception.http.ServerException;
import org.bonitasoft.web.toolkit.client.ui.page.MessageTyped;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;
import org.bonitasoft.web.toolkit.client.ui.utils.Message;

/**
 * @author Séverin Moussel
 */
public abstract class FormFiller extends Filler<AbstractForm> {

    public FormFiller() {
        super();
        setLoaderPosition(null);
    }

    public FormFiller(final int repeatEvery) {
        super(repeatEvery);
        setLoaderPosition(null);
    }

    public FormFiller(final Object target, final int repeatEvery) {
        super(target, repeatEvery);
        setLoaderPosition(null);
    }

    public FormFiller(final AbstractForm target) {
        super(target);
        setLoaderPosition(null);
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        try {
            this.target.setJson(json);
        } catch (final RuntimeException e) {
            onError(e);
        } catch (final Exception e) {
            onError(new RuntimeException(e));
        }
    }

    @Override
    protected void onError(final RuntimeException e) {
        // Use notifications for repeated fillers
        MessageTyped.TYPE type = MessageTyped.TYPE.ALERT;
        if (getRepeatEvery() > 0) {
            type = MessageTyped.TYPE.ERROR;
        }

        // API specific errors
        if (e instanceof APIException) {
            final APIException ex = (APIException) e;
            if (APIItemNotFoundException.class.toString().equals(ex.getOriginalClassName())) {
                Message.show(type, _("The element you try to edit doesn't exists."));
                return;
            }
        }

        // Server generic errors
        if (e instanceof ServerException) {
            Message.show(type, _("The element you try to edit can't be retrieved due to a server problem.") + "\n"
                    + _("Please contact your administrator."));
            Message.log(e);
            return;

        }

        // HTTP generic errors
        if (e instanceof HttpException) {
            Message.show(type, _("The element you try to edit can't be retrieved due to a connection problem.") + "\n"
                    + _("Please contact your administrator."));
            Message.log(e);
            return;
        }

        // Uncatched errors
        super.onError(e);
    }
}
