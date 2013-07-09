/*******************************************************************************
 * Copyright (C) 2009, 2013 BonitaSoft S.A.
 * BonitaSoft is a trademark of BonitaSoft SA.
 * This software file is BONITASOFT CONFIDENTIAL. Not For Distribution.
 * For commercial licensing information, contact:
 * BonitaSoft, 32 rue Gustave Eiffel – 38000 Grenoble
 * or BonitaSoft US, 51 Federal Street, Suite 305, San Francisco, CA 94107
 *******************************************************************************/
package org.bonitasoft.web.rest.server.datastore.profile;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Fabio Lombardi
 * 
 */
public class PageListerTest {

    private PageLister pageLister;

    @Before
    public void setUp() {
        pageLister = new PageLister();
    }

    @Test
    public void getPages_return_all_available_bonita_pages() {

        List<BonitaPageItem> pages = pageLister.getPages();

        assertThat(pages, equalTo(PageLister.pages));
    }

    @Test
    public void getPages_return_all_pages_not_in_pagesToSkip_list() throws Exception {
        BonitaPageItem pageToSkip1 = PageLister.pages.get(0);
        BonitaPageItem pageToSkip2 = PageLister.pages.get(1);
        List<BonitaPageItem> expectedPages = buildExpectedPagesList(pageToSkip1, pageToSkip2);
        List<String> pagesTokenToSkip = asList(pageToSkip1.getToken(), pageToSkip2.getToken());

        List<BonitaPageItem> pages = pageLister.getPages(pagesTokenToSkip);

        assertThat(pages, equalTo(expectedPages));
    }

    private List<BonitaPageItem> buildExpectedPagesList(BonitaPageItem pageToSkip1, BonitaPageItem pageToSkip2) {
        List<BonitaPageItem> expectedPages = new ArrayList<BonitaPageItem>(PageLister.pages);
        expectedPages.remove(pageToSkip1);
        expectedPages.remove(pageToSkip2);
        return expectedPages;
    }
}
