/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * @author Qixiang Zhang
 * @version 1.0
 */
public abstract class PaginatedListWidget extends Composite {

    protected HorizontalPanel panel = new HorizontalPanel();

    protected final FlexTable flexTable = new FlexTable();

    private HTML showMore = new HTML("Show More (2) &gt; ");

    private HTML goToList = new HTML("Go To List &gt;&gt;");

    private HTML firstPage = new HTML("|&lt;&lt;&nbsp;");

    private HTML nextPage = new HTML("Next Page &gt;");

    private HTML prePage = new HTML("&lt; Pre Page");

    private HTML lastPage = new HTML("&nbsp;&gt;&gt;|");

    private int showMorePageSize = 2;

    private int goToListPageSize = 10;

    private int goToListColumnNumber;

    private int showMoreColumnNumber;

    private int pageIndex = 1;

    private int pageCount;

    private int totalCount;

    private HTML pagination = new HTML("Page 1 of 10 &nbsp;");

    /**
     * 
     * Default constructor.
     */
    public PaginatedListWidget() {
        this.showMore.setStylePrimaryName("link_label");
        this.goToList.setStylePrimaryName("link_label");
        this.nextPage.setStylePrimaryName("link_label");
        this.prePage.setStylePrimaryName("link_label");
        this.firstPage.setStylePrimaryName("link_label");
        this.lastPage.setStylePrimaryName("link_label");
        this.pagination.setVisible(false);
        this.pagination.setStylePrimaryName("gray_label");
        this.nextPage.setVisible(false);
        this.firstPage.setVisible(false);
        this.lastPage.setVisible(false);
        this.prePage.setVisible(false);
        this.showMore.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                showMore();

            }
        });
        this.goToList.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                goToList();

            }
        });

        this.firstPage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                firstPage();
            }
        });

        this.prePage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                prePage();
            }
        });

        this.nextPage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                nextPage();

            }

        });

        this.lastPage.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                lastPage();

            }
        });

        this.panel.add(this.showMore);
        this.panel.add(this.goToList);
        this.panel.add(this.pagination);
        this.panel.add(this.firstPage);
        this.panel.add(this.prePage);
        this.panel.add(this.nextPage);
        this.panel.add(this.lastPage);
        // initWidget(panel);
    }

    public abstract void goToListReload();

    public abstract void showMoreReload();

    private void firstPage() {
        this.pageIndex = 1;
        this.prePage.setVisible(false);
        this.firstPage.setVisible(false);
        this.nextPage.setVisible(true);
        this.lastPage.setVisible(true);
        this.pagination.setHTML("Page " + ((this.pageIndex - 1) * 10 + 1) + " of " + this.pageIndex * 10 + " &nbsp; ");
        goToListReload();
    }

    private void prePage() {
        this.pageIndex--;
        if (this.pageIndex == 1) {
            this.prePage.setVisible(false);
            this.firstPage.setVisible(false);
        }
        this.nextPage.setVisible(true);
        this.lastPage.setVisible(true);
        this.pagination.setHTML("Page " + ((this.pageIndex - 1) * 10 + 1) + " of " + this.pageIndex * 10 + " &nbsp; ");
        goToListReload();
    }

    private void nextPage() {
        this.pageIndex++;
        if (this.pageIndex == this.pageCount) {
            this.nextPage.setVisible(false);
            this.lastPage.setVisible(false);
            this.pagination.setHTML("Page " + ((this.pageIndex - 1) * 10 + 1) + " of " + this.totalCount + " &nbsp; ");
        } else {
            this.pagination.setHTML("Page " + ((this.pageIndex - 1) * 10 + 1) + " of " + this.pageIndex * 10 + " &nbsp; ");
        }
        this.prePage.setVisible(true);
        this.firstPage.setVisible(true);
        goToListReload();
    }

    private void lastPage() {

        this.prePage.setVisible(true);
        this.firstPage.setVisible(true);
        this.nextPage.setVisible(false);
        this.lastPage.setVisible(false);
        this.pageIndex = this.pageCount;
        this.pagination.setHTML("Page " + ((this.pageCount - 1) * 10 + 1) + " of " + this.totalCount + " &nbsp; ");
        goToListReload();
    }

    /**
     * Send the name from the nameField to the server and wait for a response.
     */
    private void showMore() {
        showMoreReload();
        this.showMore.setHTML("Show More (" + this.showMoreColumnNumber + ") &gt; ");

    }

    private void goToList() {
        if (this.pageCount != 1) {
            this.nextPage.setVisible(true);
            this.lastPage.setVisible(true);
        } else {
            this.pagination.setHTML("Page 1 of " + this.totalCount + " &nbsp; ");
        }
        this.goToList.setVisible(false);
        this.showMore.setVisible(false);
        this.pagination.setVisible(true);
        goToListReload();
    }

    /**
     * @return the showMore
     */
    public HTML getShowMore() {
        return this.showMore;
    }

    /**
     * @param showMore
     *            the showMore to set
     */
    public void setShowMore(final HTML showMore) {
        this.showMore = showMore;
    }

    /**
     * @return the goToList
     */
    public HTML getGoToList() {
        return this.goToList;
    }

    /**
     * @param goToList
     *            the goToList to set
     */
    public void setGoToList(final HTML goToList) {
        this.goToList = goToList;
    }

    /**
     * @return the firstPage
     */
    public HTML getFirstPage() {
        return this.firstPage;
    }

    /**
     * @param firstPage
     *            the firstPage to set
     */
    public void setFirstPage(final HTML firstPage) {
        this.firstPage = firstPage;
    }

    /**
     * @return the nextPage
     */
    public HTML getNextPage() {
        return this.nextPage;
    }

    /**
     * @param nextPage
     *            the nextPage to set
     */
    public void setNextPage(final HTML nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * @return the prePage
     */
    public HTML getPrePage() {
        return this.prePage;
    }

    /**
     * @param prePage
     *            the prePage to set
     */
    public void setPrePage(final HTML prePage) {
        this.prePage = prePage;
    }

    /**
     * @return the lastPage
     */
    public HTML getLastPage() {
        return this.lastPage;
    }

    /**
     * @param lastPage
     *            the lastPage to set
     */
    public void setLastPage(final HTML lastPage) {
        this.lastPage = lastPage;
    }

    /**
     * @return the showMorePageSize
     */
    public int getShowMorePageSize() {
        return this.showMorePageSize;
    }

    /**
     * @param showMorePageSize
     *            the showMorePageSize to set
     */
    public void setShowMorePageSize(final int showMorePageSize) {
        this.showMorePageSize = showMorePageSize;
    }

    /**
     * @return the goToListPageSize
     */
    public int getGoToListPageSize() {
        return this.goToListPageSize;
    }

    /**
     * @param goToListPageSize
     *            the goToListPageSize to set
     */
    public void setGoToListPageSize(final int goToListPageSize) {
        this.goToListPageSize = goToListPageSize;
    }

    /**
     * @return the pageIndex
     */
    public int getPageIndex() {
        return this.pageIndex;
    }

    /**
     * @param pageIndex
     *            the pageIndex to set
     */
    public void setPageIndex(final int pageIndex) {
        this.pageIndex = pageIndex;
    }

    /**
     * @return the pageCount
     */
    public int getPageCount() {
        return this.pageCount;
    }

    /**
     * @param pageCount
     *            the pageCount to set
     */
    public void setPageCount(final int pageCount) {
        this.pageCount = pageCount;
    }

    /**
     * @return the totalCount
     */
    public int getTotalCount() {
        return this.totalCount;
    }

    /**
     * @param totalCount
     *            the totalCount to set
     */
    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * @return the pagination
     */
    public HTML getPagination() {
        return this.pagination;
    }

    /**
     * @param pagination
     *            the pagination to set
     */
    public void setPagination(final HTML pagination) {
        this.pagination = pagination;
    }

    /**
     * @return the goToListColumnNumber
     */
    public int getGoToListColumnNumber() {
        return this.goToListColumnNumber;
    }

    /**
     * @param goToListColumnNumber
     *            the goToListColumnNumber to set
     */
    public void setGoToListColumnNumber(final int goToListColumnNumber) {
        this.goToListColumnNumber = goToListColumnNumber;
    }

    /**
     * @return the showMoreColumnNumber
     */
    public int getShowMoreColumnNumber() {
        return this.showMoreColumnNumber;
    }

    /**
     * @param showMoreColumnNumber
     *            the showMoreColumnNumber to set
     */
    public void setShowMoreColumnNumber(final int showMoreColumnNumber) {
        this.showMoreColumnNumber = showMoreColumnNumber;
    }

}
