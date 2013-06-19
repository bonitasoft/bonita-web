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
package org.bonitasoft.web.rest.model.identity;

import java.io.Serializable;

/**
 * @author Yongtao Guo
 * 
 */
public class ClientContactData implements Serializable {

    private static final long serialVersionUID = 335912108646611181L;

    private String email;

    private String phoneNumber;

    private String mobileNumber;

    private String faxNumber;

    private String building;

    private String room;

    private String address;

    private String zipCode;

    private String city;

    private String state;

    private String country;

    private String website;

    public ClientContactData() {
        super();
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @param phoneNumber
     *            the phoneNumber to set
     */
    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the mobileNumber
     */
    public String getMobileNumber() {
        return this.mobileNumber;
    }

    /**
     * @param mobileNumber
     *            the mobileNumber to set
     */
    public void setMobileNumber(final String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * @return the faxNumber
     */
    public String getFaxNumber() {
        return this.faxNumber;
    }

    /**
     * @param faxNumber
     *            the faxNumber to set
     */
    public void setFaxNumber(final String faxNumber) {
        this.faxNumber = faxNumber;
    }

    /**
     * @return the building
     */
    public String getBuilding() {
        return this.building;
    }

    /**
     * @param building
     *            the building to set
     */
    public void setBuilding(final String building) {
        this.building = building;
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return this.room;
    }

    /**
     * @param room
     *            the room to set
     */
    public void setRoom(final String room) {
        this.room = room;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(final String address) {
        this.address = address;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return this.zipCode;
    }

    /**
     * @param zipCode
     *            the zipCode to set
     */
    public void setZipCode(final String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return this.state;
    }

    /**
     * @param state
     *            the state to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return this.country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * @return the website
     */
    public String getWebsite() {
        return this.website;
    }

    /**
     * @param website
     *            the website to set
     */
    public void setWebsite(final String website) {
        this.website = website;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
}
