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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.component.Text;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;

import com.google.gwt.user.client.Element;

/**
 * @author Julien Mege
 */
class Pager extends Component {

    private final Table table;

    private final Container<AbstractComponent> pagerPanel = new Container<AbstractComponent>();

    private int currentPage = 1;

    private final int nbResultsByPage;

    private final int nbResults;

    private static final int nb_displayed_pages = 7;

    public Pager(final Table table, final int currentPage, final int nbResults, final int nbResultsByPage) {
        this.table = table;
        this.currentPage = currentPage;
        this.nbResults = nbResults;
        this.nbResultsByPage = nbResultsByPage;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getTotalPages() {
        final int nbpages = (int) Math.ceil((float) this.nbResults / (float) this.nbResultsByPage);
        return this.nbResults > 0 && nbpages == 0 ? 1 : nbpages;
    }

    @Override
    protected Element makeElement() {

        feedPagerPanel();

        this.pagerPanel.setRootTagClass("pager");
        return this.pagerPanel.getElement();
    }

    private void feedPagerPanel() {
        int startPage = this.currentPage;
        int endPage = this.currentPage;
        int pagesBefore = 0;
        int pagesAfter = 0;

        if (this.currentPage <= 0 || nbResults == 0) {
            return;
        }

        // Calculate the pages to display
        // Number of pages to display before and after the current one
        pagesBefore = (nb_displayed_pages - 1) / 2;
        pagesAfter = nb_displayed_pages - 1 - pagesBefore;

        if (this.currentPage + pagesAfter > getTotalPages()) {
            pagesAfter = getTotalPages() - this.currentPage;
            pagesBefore = nb_displayed_pages - 1 - pagesAfter;
        }
        if (this.currentPage - pagesBefore < 1) {
            pagesBefore = this.currentPage - 1;
            pagesAfter = nb_displayed_pages - 1 - pagesBefore;
        }

        // First and last page number to display
        startPage = this.currentPage - pagesBefore;
        endPage = this.currentPage + pagesAfter;

        // PAGE COUNT
        this.pagerPanel.append(
                new Text(
                        _("%current_page% of %total_pages%",
                                new Arg("current_page", this.currentPage),
                                new Arg("total_pages", getTotalPages())
                        )
                ).addClass("page_count")
                );

        // FIRST PAGE
        // this.pagerPanel.append(new PagerLink(this.table, 1, _("First"), needPreviousBtn).addClass("first"));

        // RESULTS COUNT
        this.pagerPanel.append(getResultCount());

        boolean needPreviousBtn = this.currentPage > 1;
        boolean needNextBtn = this.currentPage < getTotalPages();

        if (needPreviousBtn || needNextBtn) {
            // PREVIOUS PAGE
            this.pagerPanel.append(new PagerLink(this.table, this.currentPage - 1, _("Previous"), needPreviousBtn).addClass("prev"));

            if (startPage > 1) {
                this.pagerPanel.append(new Text("...").addClass("ellipsis"));
            }

            // PAGE NUMBERS
            for (int i = startPage; i <= endPage && i <= getTotalPages(); i++) {
                this.pagerPanel.append(new PagerLink(this.table, i, String.valueOf(i), !(this.currentPage == i), this.currentPage == i).addClass("pagenum"));
            }

            if (endPage < getTotalPages()) {
                this.pagerPanel.append(new Text("...").addClass("ellipsis"));
            }

            // NEXT PAGE
            this.pagerPanel.append(new PagerLink(this.table, this.currentPage + 1, _("Next"), needNextBtn).addClass("next"));

            // LAST PAGE
            // this.pagerPanel.append(new PagerLink(this.table, getTotalPages(), _("Last"), needNextBtn).addClass("last"));
        }
    }

    private Text getResultCount() {
        int startResult = this.nbResults <= 0 ? 0 : (this.currentPage - 1) * this.nbResultsByPage + 1;
        int endResult = Math.min(this.currentPage * this.nbResultsByPage, this.nbResults);
        Text resultCount;
        if (nbResults == startResult) {
            resultCount = new Text(
                    _("%start_result% of %total_results%",
                            new Arg("start_result", startResult),
                            new Arg("total_results", this.nbResults)
                    )
                    );
        } else {
            resultCount = new Text(
                    _("%start_result% - %end_result% of %total_results%",
                            new Arg("start_result", startResult),
                            new Arg("end_result", endResult),
                            new Arg("total_results", this.nbResults)
                    )
                    );
        }

        resultCount.addClass("results_count");
        return resultCount;
    }

    @Override
    public Pager addClass(final String className) {
        super.addClass(className);

        return this;
    }

}
