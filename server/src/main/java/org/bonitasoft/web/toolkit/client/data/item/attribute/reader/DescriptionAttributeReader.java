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

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n.t_;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualDescription;

/**
 * @author Julien Mege
 * 
 */
public class DescriptionAttributeReader extends FirstNotBlancAttributeReader {

    public DescriptionAttributeReader(final String DisplayDescriptionAttributeToRead, final String DescriptionAttributeToRead) {
        super(DisplayDescriptionAttributeToRead, DescriptionAttributeToRead);
        setDefaultValue(AbstractI18n.t_("No description."));
        this.leadAttribute = DisplayDescriptionAttributeToRead;
    }
    
    public DescriptionAttributeReader(final String DescriptionAttributeToRead) {
        super(DescriptionAttributeToRead);
        setDefaultValue(AbstractI18n.t_("No description."));
        this.leadAttribute = DescriptionAttributeToRead;
    }

    
    public DescriptionAttributeReader() {
        this(ItemHasDualDescription.ATTRIBUTE_DISPLAY_DESCRIPTION, ItemHasDualDescription.ATTRIBUTE_DESCRIPTION);
    }
}
