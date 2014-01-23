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
package org.bonitasoft.console.client.user.application.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.user.task.view.PluginProcess;
import org.bonitasoft.console.client.user.task.view.ProcessQuickDetailsPage;
import org.bonitasoft.console.client.user.task.view.TasksListingPage;
import org.bonitasoft.web.rest.model.bpm.process.CategoryDefinition;
import org.bonitasoft.web.rest.model.bpm.process.CategoryItem;
import org.bonitasoft.web.rest.model.bpm.process.ProcessDefinition;
import org.bonitasoft.web.rest.model.bpm.process.ProcessItem;
import org.bonitasoft.web.toolkit.client.Session;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.item.Definitions;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DescriptionAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.table.ItemTable;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingResourceFilter;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingSort;
import org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingTable;

/**
 * @author Paul AMAR
 * 
 */

public class ProcessListingPage extends ItemListingPage<ProcessItem> implements PluginProcess {

    public static final String TOKEN = "processlistinguser";    
    
    public static final List<String> PRIVILEGES = new ArrayList<String>();
    
    static {
        PRIVILEGES.add(ProcessListingPage.TOKEN);
    }

    private static String TABLE_ALL = "all";

    public static final String PROCESS_NAME = "processName";

    public static final String PROCESS_VERSION = "processVersion";

    public static final String FILTER_ALL = "all";

    @Override
    public void defineTitle() {
        setTitle(_("Apps"));

    }

    @Override
    public String defineToken() {
        return TOKEN;
    }

    @Override
    protected LinkedList<ItemListingFilter> definePrimaryFilters() {
        final LinkedList<ItemListingFilter> filters = new LinkedList<ItemListingFilter>();
        filters.add(allProcessesFilter());
        return filters;
    }

    private ItemListingFilter allProcessesFilter() {
        return new ItemListingFilter(FILTER_ALL, _("All"), _("Display a summary of all apps"), TABLE_ALL);
    }

    @Override
    protected ItemListingResourceFilter defineResourceFilters() {

        final ItemListingResourceFilter categories =
                new ItemListingResourceFilter(
                        new APISearchRequest(CategoryDefinition.get()),
                        CategoryItem.ATTRIBUTE_NAME,
                        "default-image", // fake attribute to display default image
                        TABLE_ALL)
                        .addFilterMapping(ProcessItem.FILTER_CATEGORY_ID, CategoryItem.ATTRIBUTE_ID);

        return categories;
    }
    
    @Override
    protected Title defineResourceFiltersTitle() {
        return new Title(_("Categories"));
    }

    // ////////////////////
    // TABLES
    // ////////////////////

    @Override
    protected LinkedList<ItemListingTable> defineTables() {
        final LinkedList<ItemListingTable> tables = new LinkedList<ItemListingTable>();

        // All processes
        tables.add(new ItemListingTable(new JsId(TABLE_ALL), _("All"),
                new ItemTable(Definitions.get(ProcessDefinition.TOKEN))
                        .addHiddenFilter(ProcessItem.FILTER_USER_ID, Session.getUserId())
                        .addColumn(ProcessItem.ATTRIBUTE_DISPLAY_NAME, _("Name"), true)
                        .addColumn(ProcessItem.ATTRIBUTE_VERSION, _("Version"))
                        .addColumn(new DescriptionAttributeReader(ProcessItem.ATTRIBUTE_DISPLAY_DESCRIPTION, ProcessItem.ATTRIBUTE_DESCRIPTION),
                                _("Description"), false)
                ,
                new ProcessQuickDetailsPage()
                ));
        return tables;

    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.ui.page.itemListingPage.ItemListingPage#defineDefaultSort()
     */
    @Override
    protected ItemListingSort defineDefaultSort() {
        return new ItemListingSort(ProcessItem.ATTRIBUTE_DISPLAY_NAME, true);
    }

    @Override
    public String getPluginToken() {
        return PLUGIN_TOKEN;
    }
}
