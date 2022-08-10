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
package org.bonitasoft.web.rest.model.identity;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.StringRegexpValidator;

/**
 * @author Paul AMAR
 * 
 */
public abstract class AbstractContactDataDefinition extends ItemDefinition {

    @Override
    protected void defineAttributes() {
        createAttribute(AbstractContactDataItem.ATTRIBUTE_EMAIL, ItemAttribute.TYPE.EMAIL);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_PHONE, ItemAttribute.TYPE.STRING)
                .addValidator(new StringRegexpValidator("[\\d\\.\\(\\)#\\+]*"));
        createAttribute(AbstractContactDataItem.ATTRIBUTE_MOBILE, ItemAttribute.TYPE.STRING)
                .addValidator(new StringRegexpValidator("[\\d\\.\\(\\)#\\+]*"));
        createAttribute(AbstractContactDataItem.ATTRIBUTE_FAX, ItemAttribute.TYPE.STRING)
                .addValidator(new StringRegexpValidator("[\\d\\.\\(\\)#\\+]*"));
        createAttribute(AbstractContactDataItem.ATTRIBUTE_BUILDING, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_ROOM, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_ADDRESS, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_ZIPCODE, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_CITY, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_STATE, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_COUNTRY, ItemAttribute.TYPE.STRING);
        createAttribute(AbstractContactDataItem.ATTRIBUTE_WEBSITE, ItemAttribute.TYPE.URL);

    }

    @Override
    protected abstract IItem _createItem();
}
