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
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Paul AMAR
 * 
 */
public abstract class AbstractContactDataItem extends Item implements ItemHasUniqueId {

    public static final String ATTRIBUTE_EMAIL = "email";

    public static final String ATTRIBUTE_PHONE = "phone_number";

    public static final String ATTRIBUTE_MOBILE = "mobile_number";

    public static final String ATTRIBUTE_FAX = "fax_number";

    public static final String ATTRIBUTE_BUILDING = "building";

    public static final String ATTRIBUTE_ROOM = "room";

    public static final String ATTRIBUTE_ADDRESS = "address";

    public static final String ATTRIBUTE_ZIPCODE = "zipcode";

    public static final String ATTRIBUTE_CITY = "city";

    public static final String ATTRIBUTE_STATE = "state";

    public static final String ATTRIBUTE_COUNTRY = "country";

    public static final String ATTRIBUTE_WEBSITE = "website";

    public static final Long MAX_EMAIL_LENGTH = 50L;// FIXME set to 254L when migration + tests on all DB

    public AbstractContactDataItem() {
        super();
    }

    public AbstractContactDataItem(final IItem item) {
        super(item);
    }

    // ///////////////////////////////////////////////////////
    // // GETTERS
    // //////////////////////////////////////////////////////

    public String getEmail() {
        return this.getAttributeValue(ATTRIBUTE_EMAIL);
    }

    public String getPhoneNumber() {
        return this.getAttributeValue(ATTRIBUTE_PHONE);
    }

    public String getMobileNumber() {
        return this.getAttributeValue(ATTRIBUTE_MOBILE);
    }

    public String getFaxNumber() {
        return this.getAttributeValue(ATTRIBUTE_FAX);
    }

    public String getBuilding() {
        return this.getAttributeValue(ATTRIBUTE_BUILDING);
    }

    public String getRoom() {
        return this.getAttributeValue(ATTRIBUTE_ROOM);
    }

    public String getAddress() {
        return this.getAttributeValue(ATTRIBUTE_ADDRESS);
    }

    public String getZipCode() {
        return this.getAttributeValue(ATTRIBUTE_ZIPCODE);
    }

    public String getCity() {
        return this.getAttributeValue(ATTRIBUTE_CITY);
    }

    public String getState() {
        return this.getAttributeValue(ATTRIBUTE_STATE);
    }

    public String getCountry() {
        return this.getAttributeValue(ATTRIBUTE_COUNTRY);
    }

    public String getWebsite() {
        return this.getAttributeValue(ATTRIBUTE_WEBSITE);
    }

    // ///////////////////////////////////////////////////////
    // // SETTERS
    // //////////////////////////////////////////////////////

    @Override
    public void setId(final String id) {
        setAttribute(ItemHasUniqueId.ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        setAttribute(ItemHasUniqueId.ATTRIBUTE_ID, id);
    }

    public void setEmail(final String Email) {
        this.setAttribute(ATTRIBUTE_EMAIL, Email);
    }

    public void setPhoneNumber(final String PhoneNumber) {
        this.setAttribute(ATTRIBUTE_PHONE, PhoneNumber);
    }

    public void setMobileNumber(final String MobileNumber) {
        this.setAttribute(ATTRIBUTE_MOBILE, MobileNumber);
    }

    public void setFaxNumber(final String FaxNumber) {
        this.setAttribute(ATTRIBUTE_FAX, FaxNumber);
    }

    public void setBuilding(final String Building) {
        this.setAttribute(ATTRIBUTE_BUILDING, Building);
    }

    public void setRoom(final String Room) {
        this.setAttribute(ATTRIBUTE_ROOM, Room);
    }

    public void setAddress(final String Address) {
        this.setAttribute(ATTRIBUTE_ADDRESS, Address);
    }

    public void setZipCode(final String ZipCode) {
        this.setAttribute(ATTRIBUTE_ZIPCODE, ZipCode);
    }

    public void setCity(final String City) {
        this.setAttribute(ATTRIBUTE_CITY, City);
    }

    public void setState(final String State) {
        this.setAttribute(ATTRIBUTE_STATE, State);
    }

    public void setCountry(final String Country) {
        this.setAttribute(ATTRIBUTE_COUNTRY, Country);
    }

    public void setWebsite(final String Website) {
        this.setAttribute(ATTRIBUTE_WEBSITE, Website);
    }

    @Override
    public abstract ItemDefinition getItemDefinition();

}
