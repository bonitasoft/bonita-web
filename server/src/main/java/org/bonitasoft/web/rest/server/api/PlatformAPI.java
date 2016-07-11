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
package org.bonitasoft.web.rest.server.api;

import java.io.IOException;

import org.bonitasoft.console.common.server.utils.BonitaHomeFolderAccessor;
import org.bonitasoft.engine.session.PlatformSession;
import org.bonitasoft.web.rest.server.framework.API;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Julien Mege
 *
 */
public abstract class PlatformAPI<T extends IItem> extends API<T> {

    private PlatformSession sessionSingleton = null;

    /**
     * Get the session
     */
    protected final PlatformSession getPlatformSession() {
        if (this.sessionSingleton == null) {
            this.sessionSingleton = (PlatformSession) getHttpSession().getAttribute("platformSession");
        }
        return this.sessionSingleton;
    }

    @Override
    protected String getCompleteTempFilePath(final String path) throws IOException {
        final BonitaHomeFolderAccessor tempFolderAccessor = new BonitaHomeFolderAccessor();
        return tempFolderAccessor.getCompleteTempFilePath(path);
    }
}
