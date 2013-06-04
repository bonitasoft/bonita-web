/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.chart;

import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.utils.Filler;

/**
 * @author Séverin Moussel
 * 
 */
abstract class ChartFiller<T extends Chart> extends Filler<T> {

    protected ItemDefinition itemDefinition = null;

    public ChartFiller(final ItemDefinition itemDefinition) {
        super();
        this.itemDefinition = itemDefinition;
    }

    @Override
    protected void getData(final APICallback callback) {
        this.itemDefinition.getAPICaller().search(0, this.target.getMaxPoints(), callback);
    }

    abstract protected void setData(List<IItem> items);

    @Override
    protected final void setData(final String json, final Map<String, String> headers) {
        final List<IItem> items = JSonItemReader.parseItems(json, this.itemDefinition);

        final boolean previousIsAutoRedraw = ((Chart) this.target).isAutoRedraw();
        ((Chart) this.target).setAutoRedraw(false);

        this.setData(items);

        if (previousIsAutoRedraw) {
            ((Chart) this.target).refresh();
        }

        ((Chart) this.target).setAutoRedraw(previousIsAutoRedraw);
    }

}
