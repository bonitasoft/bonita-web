/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Form field value
 * 
 * @author Anthony Birembaut
 */
public class FormFieldValue implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = 3232606675323235307L;

    /**
     * value
     */
    private Serializable value;

    /**
     * value type
     */
    private String valueType;

    /**
     * format (useful for some value types as dates)
     */
    private String format;

    /**
     * the name of the document if this field value represent a document
     */
    private String documentName;

    /**
     * the ID of the document
     */
    private long documentId = -1L;

    /**
     * indicate if this is the value of a document
     */
    private boolean isDocument;
    
    /**
     * indicate if this is the value of a document
     */
    private boolean isDocumentList;

    /**
     * indicates whether the form widget this value refers to has child widgets or not
     */
    private boolean hasChildWidgets;

    /**
     * indicates if the data field should be displayed
     */
    private boolean displayDataField;

    /**
     * Child widgets values (only used at form submission)
     */
    private Map<String, FormFieldValue> childFormFieldValues;

    /**
     * Modifier use to convert value type once submitted
     */
    private String modifier;

    /**
     * indicate the displayedValue of the field
     */
    private String displayedValue;

    /**
     * Constructor
     * 
     * @param value
     * @param valueType
     */
    public FormFieldValue(final Serializable value, final String valueType) {
        this.value = value;
        this.valueType = valueType;
    }

    /**
     * Constructor
     * 
     * @param value
     * @param valueType
     * @param format
     */
    public FormFieldValue(final Serializable value, final String valueType, final String format) {
        this.value = value;
        this.valueType = valueType;
        this.format = format;
    }

    /**
     * Constructor
     * 
     * @param value
     * @param valueType
     * @param format
     * @param modifier
     */
    public FormFieldValue(final Serializable value, final String valueType, final String format, final String modifier) {
        this(value, valueType, format);
        setModifier(modifier);
    }

    /**
     * Constructor
     * 
     * @param childFormFieldValues
     */
    public FormFieldValue(final Map<String, FormFieldValue> childFormFieldValues) {
        this.childFormFieldValues = childFormFieldValues;
        hasChildWidgets = true;
    }

    /**
     * Default constructor
     */
    public FormFieldValue() {
        super();
        // Mandatory for Serialization
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(final Serializable value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(final String valueType) {
        this.valueType = valueType;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(final String format) {
        this.format = format;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(final String modifier) {
        this.modifier = modifier;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(final String documentName) {
        this.documentName = documentName;
    }

    public void setHasChildWidgets(final boolean hasChildWidgets) {
        this.hasChildWidgets = hasChildWidgets;
    }

    public boolean hasChildWidgets() {
        return hasChildWidgets;
    }

    public void setChildFormFieldValues(final Map<String, FormFieldValue> childFormFieldValues) {
        this.childFormFieldValues = childFormFieldValues;
    }

    public Map<String, FormFieldValue> getChildFormFieldValues() {
        if (childFormFieldValues == null) {
            childFormFieldValues = new HashMap<String, FormFieldValue>();
        }
        return childFormFieldValues;
    }

    public void setDisplayDataField(final boolean displayDataField) {
        this.displayDataField = displayDataField;
    }

    public boolean isDisplayDataField() {
        return displayDataField;
    }

    public long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(final long documentId) {
        this.documentId = documentId;
    }

    public boolean isDocument() {
        return isDocument;
    }

    public void setDocument(final boolean isDocument) {
        this.isDocument = isDocument;
    }

    public String getDisplayedValue() {
        return displayedValue;
    }

    public void setDisplayedValue(final String displayedValue) {
        this.displayedValue = displayedValue;
    }

}
