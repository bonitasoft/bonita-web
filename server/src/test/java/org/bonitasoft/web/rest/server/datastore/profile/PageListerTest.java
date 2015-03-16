package org.bonitasoft.web.rest.server.datastore.profile;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.web.rest.model.portal.profile.BonitaPageItem;
import org.bonitasoft.web.rest.server.APITestWithMock;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Fabio Lombardi
 * 
 */
public class PageListerTest extends APITestWithMock {

    private PageLister pageLister;

    @Before
    public void setUp() {
        pageLister = new PageLister();
    }

    @Test
    public void getPages_return_all_available_bonita_pages() {

        final List<BonitaPageItem> pages = pageLister.getPages();

        assertThat(pages, equalTo(PageLister.pages));
    }

    @Test
    public void getPages_return_all_pages_not_in_pagesToSkip_list() throws Exception {
        final BonitaPageItem pageToSkip1 = PageLister.pages.get(0);
        final BonitaPageItem pageToSkip2 = PageLister.pages.get(1);
        final List<BonitaPageItem> expectedPages = buildExpectedPagesList(pageToSkip1, pageToSkip2);
        final List<String> pagesTokenToSkip = asList(pageToSkip1.getToken(), pageToSkip2.getToken());

        final List<BonitaPageItem> pages = pageLister.getPages(pagesTokenToSkip);

        assertThat(pages, equalTo(expectedPages));
    }

    private List<BonitaPageItem> buildExpectedPagesList(final BonitaPageItem pageToSkip1, final BonitaPageItem pageToSkip2) {
        final List<BonitaPageItem> expectedPages = new ArrayList<BonitaPageItem>(PageLister.pages);
        expectedPages.remove(pageToSkip1);
        expectedPages.remove(pageToSkip2);
        return expectedPages;
    }
}
