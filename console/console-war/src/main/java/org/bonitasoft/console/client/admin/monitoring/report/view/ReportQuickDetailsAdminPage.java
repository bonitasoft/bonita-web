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
package org.bonitasoft.console.client.admin.monitoring.report.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;

import org.bonitasoft.console.client.data.item.attribute.reader.DeployedUserReader;
import org.bonitasoft.web.rest.api.model.monitoring.report.ReportDefinition;
import org.bonitasoft.web.rest.api.model.monitoring.report.ReportItem;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionShowView;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Clickable;
import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.component.Link;
import org.bonitasoft.web.toolkit.client.ui.component.Section;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;
import org.bonitasoft.web.toolkit.client.ui.utils.Url;

/**
 * @author Paul AMAR, Rohart Bastien
 * 
 */
public class ReportQuickDetailsAdminPage extends ItemQuickDetailsPage<ReportItem> {

    public static final String TOKEN = "reportquickdetailsadmin";

    public ReportQuickDetailsAdminPage() {
        super(Definitions.get(ReportDefinition.TOKEN));
    }

    @Override
    protected void buildToolbar(final ReportItem report) {
        addToolbarLink(moreDetailButton(report));
    }

    private Clickable moreDetailButton(final ReportItem report) {
        return new Button("btn-more", _("More"),
                _("Show more details about this report"), new ActionShowView(
                        new ReportMoreDetailsAdminPage(report)));
    }

    @Override
    protected void defineTitle(final ReportItem item) {
        setTitle(_(item.getName()));
        addDescription(_(new DescriptionAttributeReader().read(item)));
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ReportItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        return metadatas;
    }

    private ItemDetailsMetadata installOn(final ReportItem item) {
        return new ItemDetailsMetadata(new DateAttributeReader(
                ReportItem.ATTRIBUTE_INSTALLED_ON),
                _("Installed on"),
                _("The date when this report has been installed"));
    }

    private ItemDetailsMetadata installBy(final ReportItem item) {
        return new ItemDetailsMetadata(new DeployedUserReader(
                item.getAttributeValue(ReportItem.ATTRIBUTE_INSTALLED_BY)),
                _("Installed by"),
                _("Name of the user who install this report"));
    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected void buildBody(final ReportItem report) {
        addBody(getReportScreenshot(report));
    }

    /**
     * @param report
     * @return
     */
    private AbstractComponent getReportScreenshot(ReportItem report) {
        Image screenshot = new Image(new Url("/console/ReportScreenshotServlet?reportName=" + report.getId()), 0, 0, _("Report's screenshot"));
        Link link = new Link(
                new JsId("screenshotReportLink"),
                _("Go to more details report view"),
                _("Go to more details report view"),
                new ActionShowView(new ReportMoreDetailsAdminPage(
                        report)));
        link.addClass("reportScreenshot");

        link.setImage(screenshot);
        return new Section(new JsId("section"), _("Screen shot"), link);

    }
}
