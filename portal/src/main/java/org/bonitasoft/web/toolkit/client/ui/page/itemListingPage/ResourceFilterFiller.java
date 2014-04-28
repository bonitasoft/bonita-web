package org.bonitasoft.web.toolkit.client.ui.page.itemListingPage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.ui.component.filler.ComponentFiller;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * Fill the resourceFilter section with the filters found in database.
 * 
 * @author Séverin Moussel
 */
public final class ResourceFilterFiller<T extends IItem> extends ComponentFiller {

    private final ItemListingPage<T> itemListingPage;

    private final ItemListingResourceFilter filter;

    public boolean isBodyEmpty = false;

    public ResourceFilterFiller(ItemListingPage<T> itemListingPage, final ItemListingResourceFilter filter) {
        this.itemListingPage = itemListingPage;
        this.filter = filter;
    }

    /**
     * Define how to read data.
     * 
     * @see Filler#getData(APICallback)
     * 
     */
    @Override
    protected void getData(final APICallback callback) {
        this.filter.getSearchRequest().run(callback);
    }

    /**
     * Define how to display data.
     * 
     * @see Filler#setData(String, Map)
     */
    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        final List<IItem> items = JSonItemReader.parseItems(json, this.filter.getSearchRequest().getItemDefinition());
        final List<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();

        if (items.size() != 0) {
            this.itemListingPage.addTitleToSection(itemListingPage.defineResourceFiltersTitle(), itemListingPage.resourceFilters);
        }

        for (final IItem item : items) {

            // Fill additional filters using filters mapping
            final Map<String, String> additionalFilters = new HashMap<String, String>();
            additionalFilters.putAll(this.filter.getFilters());

            for (final String tableResourceAttributeName : this.filter.getFiltersMapping().keySet()) {
                final String filterResourceAttributeName = this.filter.getFiltersMapping().get(tableResourceAttributeName);
                additionalFilters.put(tableResourceAttributeName, item.getAttributeValue(filterResourceAttributeName));
            }

            // Create resourceListingFilters
            final ItemListingFilter resourceFilter = new ItemListingFilter(
                    item.getId().toString(),
                    item.getAttributeValue(this.filter.getLabelAttributeName()),
                    item.getAttributeValue(this.filter.getLabelAttributeName()),
                    this.filter.getTablesToDisplay()
                    );
            resourceFilter.setIsResourceFilter(true);
            if (!StringUtil.isBlank(this.filter.getIconAttributeName())) {
                String iconUrl = item.getAttributeValue(this.filter.getIconAttributeName());
				resourceFilter.setImage(iconUrl != null ? iconUrl : "");
            }
            resourceFilter.setFilters(additionalFilters);

            filters.add(resourceFilter);
        }

        itemListingPage.displayFilters(filters);
        itemListingPage.selectRightResourceFilter();
    }
}
