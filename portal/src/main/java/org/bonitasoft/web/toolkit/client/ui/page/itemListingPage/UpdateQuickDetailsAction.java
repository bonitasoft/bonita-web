package org.bonitasoft.web.toolkit.client.ui.page.itemListingPage;

import static com.google.gwt.query.client.GQuery.$;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.action.Action;

/**
 * Action that update the content of the details panel (right panel).
 * 
 * @author Séverin Moussel
 */
final class UpdateQuickDetailsAction<T extends IItem> extends Action {

    private final ItemListingTable table;
	private ItemListingPage<T> itemListingPage;

    public UpdateQuickDetailsAction(ItemListingPage<T> itemListingPage, ItemListingTable table) {
        super();
        this.table = table;
		this.itemListingPage = itemListingPage;
    }

    @Override
    public void execute() {
        $(".tr", this.table.getElement()).removeClass("current");
        $(".tr_" + getParameter("cell_index"), this.table.getElement()).addClass("current");

        itemListingPage.updateQuickDetailPanel(table.getQuickDetailsPage(), getParameter("id"), false);
    }
}