/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.page;

import java.io.Serializable;


public class PageReference implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -1692145871057019847L;

    private Long pageId;

    private String url;

    private Long processId;

    public PageReference() {
    }

    public PageReference(final Long pageId, final String url) {
        this.pageId = pageId;
        this.url = url;
    }

    public Long getPageId() {
        return pageId;
    }

    public void setPageId(final Long pageId) {
        this.pageId = pageId;
    }

    public String getURL() {
        return url;
    }

    public void setURL(final String url) {
        this.url = url;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(final Long processId) {
        this.processId = processId;
    }


}
