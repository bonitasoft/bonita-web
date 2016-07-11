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
package org.bonitasoft.web.toolkit.client.ui.component.table;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Séverin Moussel
 * 
 */
public class ItemTreeTableFiller extends ItemTableFiller {

    private String parentIdAttributeName = null;

    private String indexAttributeName = null;

    public ItemTreeTableFiller(final String parentIdAttributeName, final String indexAttributeName) {
        super();
        this.parentIdAttributeName = parentIdAttributeName;
        this.indexAttributeName = indexAttributeName;
    }

    @Override
    protected void setData(final String json, final Map<String, String> headers) {
        final List<IItem> items = (List<IItem>) new JSonItemReader().getItems(json, this.target.getItemDefinition());

        // Sort 1 (by parent_id, index)
        Collections.sort(items, new Comparator<IItem>() {

            @Override
            public int compare(final IItem i1, final IItem i2) {
                final int index1 = Integer.valueOf(i1.getAttributeValue(ItemTreeTableFiller.this.indexAttributeName));
                final int index2 = Integer.valueOf(i2.getAttributeValue(ItemTreeTableFiller.this.indexAttributeName));
                final int parentId1 = Integer.valueOf(i1.getAttributeValue(ItemTreeTableFiller.this.parentIdAttributeName));
                final int parentId2 = Integer.valueOf(i2.getAttributeValue(ItemTreeTableFiller.this.parentIdAttributeName));

                if (parentId1 < parentId2) {
                    return -1;
                } else if (parentId1 > parentId2) {
                    return 1;
                } else if (index1 < index2) {
                    return -1;
                } else if (index1 > index2) {
                    return 1;
                }
                return 0;
            }
        });

        // Extract parents
        final LinkedList<IItem> rootItems = new LinkedList<IItem>();
        for (final IItem item : items) {
            final String parentId = item.getAttributeValue(ItemTreeTableFiller.this.parentIdAttributeName);
            if (parentId == null || "0".equals(parentId) || parentId.length() == 0) {
                rootItems.add(item);
            }
        }

        // Extract Children
        final LinkedHashMap<String, List<IItem>> children = new LinkedHashMap<String, List<IItem>>();
        for (final IItem item : items) {
            final String parentId = item.getAttributeValue(ItemTreeTableFiller.this.parentIdAttributeName);

            if (parentId != null && !"0".equals(parentId) && parentId.length() > 0) {
                if (!children.containsKey(parentId)) {
                    children.put(parentId, new LinkedList<IItem>());
                }
                children.get(parentId).add(item);
            }
        }

        // Inject children between parents
        final LinkedList<IItem> finalItemList = new LinkedList<IItem>();
        for (final IItem item : rootItems) {
            finalItemList.add(item);
            if (children.containsKey(item.getId())) {
                for (final IItem child : children.get(item.getId())) {
                    finalItemList.add(child);
                }
            }
        }

        // Display items
        this.target.setItems(finalItemList);

        this.target.setPager(
                this.target.getPage(),
                Integer.valueOf(headers.get("Content-Range").split("/")[1]),
                this.target.getNbLinesByPage()
                );
    }
}
