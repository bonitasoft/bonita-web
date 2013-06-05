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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.Map;

/**
 * @author Séverin Moussel
 * 
 */
public class ApiSearchResultPager {

    private static final String PAGER_HTTP_HEADER_KEY = "Content-Range";

    private final int currentPage;

    private final int nbResultsByPage;

    private final int nbTotalResults;

    ApiSearchResultPager(final int currentPage, final int nbResultsByPage, final int nbTotalResults) {
        super();
        this.currentPage = currentPage;
        this.nbResultsByPage = nbResultsByPage;
        this.nbTotalResults = nbTotalResults;
    }

    /**
     * @return the currentPage
     */
    public final int getCurrentPage() {
        return this.currentPage;
    }

    /**
     * @return the nbResultsByPage
     */
    public final int getNbResultsByPage() {
        return this.nbResultsByPage;
    }

    /**
     * @return the nbTotalResults
     */
    public final int getNbTotalResults() {
        return this.nbTotalResults;
    }

    public static final ApiSearchResultPager parse(final String header) {
        if (header == null) {
            return new ApiSearchResultPager(0, 0, 0);
        }

        final String[] split1 = header.split("-");
        final String[] split2 = split1[1].split("/");

        return new ApiSearchResultPager(
                Integer.valueOf(split1[0]),
                Integer.valueOf(split2[0]),
                Integer.valueOf(split2[1]));

    }

    public static final ApiSearchResultPager parse(final Map<String, String> headers) {
        return parse(headers.get(PAGER_HTTP_HEADER_KEY));
    }
}
