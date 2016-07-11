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
package org.bonitasoft.web.toolkit.client.data.item.attribute.reader;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Julien Mege
 * 
 */
public class AttributeReader extends AbstractAttributeReader {

    private boolean applyDefaultModifiers;

    public AttributeReader(final String attributeToRead) {
        this(attributeToRead, true);
    }

    public AttributeReader(final String attributeToRead, final boolean applyDefaultModifiers) {
        super(attributeToRead);
        this.applyDefaultModifiers = applyDefaultModifiers;
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.data.item.AbstractAttribute#read(org.bonitasoft.web.toolkit.client.data.item.Item)
     */
    @Override
    protected final String _read(final IItem item) {

        // Let output modifiers do the work
        if (getOutputModifiers().size() > 0 || !this.applyDefaultModifiers) {
            return item.getAttributeValue(getLeadAttribute());
        }

        final String value = item.getAttributeValue(getLeadAttribute());
        // Set default modifiers
        final ItemAttribute attribute = item.getItemDefinition().getAttribute(getLeadAttribute());
        if (attribute != null) {
            switch (attribute.getType()) {
                case DATE:
                    return DateFormat.sqlToDisplayShort(value);
                case DATETIME:
                    return DateFormat.sqlToDisplay(value);
            }
        }
        return value;
    }

    /**
     * @param applyDefaultModifiers
     *            the applyDefaultModifiers to set
     */
    public final void setApplyDefaultModifiers(final boolean applyDefaultModifiers) {
        this.applyDefaultModifiers = applyDefaultModifiers;
    }

}
