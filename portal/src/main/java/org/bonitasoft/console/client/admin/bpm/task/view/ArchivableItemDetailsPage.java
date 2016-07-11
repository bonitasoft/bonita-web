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
package org.bonitasoft.console.client.admin.bpm.task.view;

import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Vincent Elcrin
 * 
 *         Handle archivable items in order to change the call to the API by the right one.
 * 
 */
public abstract class ArchivableItemDetailsPage<T extends IItem> extends ItemQuickDetailsPage<T> {

    public final static String PAGE_PARAMETER_IS_ARCHIVED = "isarchived";

    private final ItemDefinition localItemDefinition;

    private final ItemDefinition archiveItemDefinition;

    private boolean archived = false;

    /**
     * Default Constructor.
     * 
     * @param itemDefinition
     */
    public ArchivableItemDetailsPage(ItemDefinition itemDefinition, ItemDefinition archiveItemDefinition) {
        super(itemDefinition);
        this.localItemDefinition = itemDefinition;
        this.archiveItemDefinition = archiveItemDefinition;
    }

    /**
     * Set archived parameter. On this field depend the API call made to fill the widgets.
     * 
     * @param archived
     */
    public void setArchive(boolean archived) {
        addParameter(PAGE_PARAMETER_IS_ARCHIVED, String.valueOf(archived));
    }

    /**
     * Set archived field value.
     * Used locally by overridden parameter methods.
     * 
     * @param archived
     */
    private void setArchive(String archived) {
        this.archived = Boolean.parseBoolean(archived);
    }

    /**
     * Get archived field value. On this field depend the API call made to fill the widgets.
     * 
     * @return the archived
     */
    public boolean isArchived() {
        return archived;
    }

    /**
     * Catch & parse parameters to get item's archive information
     */
    @Override
    public void setParameters(final Map<String, String> params) {
        super.setParameters(params);

        if (params.containsKey(PAGE_PARAMETER_IS_ARCHIVED)) {
            setArchive(params.get(PAGE_PARAMETER_IS_ARCHIVED));
        }
    }

    /**
     * Catch & parse parameters to get item's archive information
     */
    @Override
    public void setParameters(final TreeIndexed<String> params) {
        super.setParameters(params);

        if (params.containsKey(PAGE_PARAMETER_IS_ARCHIVED)) {
            setArchive(params.getValue(PAGE_PARAMETER_IS_ARCHIVED));
        }
    }

    /**
     * Catch & parse parameters to get item's archive information
     */
    @Override
    public void setParameters(final Arg... params) {
        super.setParameters(params);

        for (final Arg param : params) {
            if (PAGE_PARAMETER_IS_ARCHIVED.equals(param.getName())) {
                setArchive(param.getValue());
                break;
            }
        }
    }

    /**
     * Catch & parse parameters to get item's archive information
     */
    @Override
    public void addParameter(final String name, final String value) {
        super.addParameter(name, value);

        if (PAGE_PARAMETER_IS_ARCHIVED.equals(name)) {
            setArchive(value);
        }
    }

    /**
     * Override fillWidget to change itemDefinition in order to call the right API (normal or archive)
     */
    @Override
    protected void fillWidget() {
        if (archived) {
            this.itemDefinition = this.archiveItemDefinition;
        } else {
            this.itemDefinition = this.localItemDefinition;
        }
        super.fillWidget();
    }
}
