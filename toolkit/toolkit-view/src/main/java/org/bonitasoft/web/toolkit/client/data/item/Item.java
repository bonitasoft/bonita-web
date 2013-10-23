/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.data.item;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifierEngine;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorEngine;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualDescription;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasDualName;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Julien Mege, Séverin Moussel
 */
public abstract class Item implements IItem {

    public Item() {
        super();
    }

    public Item(final IItem item) {
        super();
        this.attributes.putAll(item.getAttributes());
    }

    @Override
    public List<String> getAPIIDOrder() {
        return getItemDefinition().getPrimaryKeys();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEFAULT FILTERS SUPERVISOR AND TEAM MANAGER
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final Map<String, String> attributes = new HashMap<String, String>();

    private final Map<String, IItem> deploys = new HashMap<String, IItem>();

    // private final Map<String, Long> counters = new HashMap<String, Long>();

    @Override
    public final void setId(final APIID id) {
        setAttribute(ItemHasUniqueId.ATTRIBUTE_ID, id);
    }

    @Override
    public final APIID getId() {
        APIID apiid = null;
        final ItemDefinition<?> itemDefinition = getItemDefinition();

        if (this instanceof ItemHasUniqueId) {
            apiid = getAttributeValueAsAPIID(ItemHasUniqueId.ATTRIBUTE_ID);
        } else {

            final List<String> primaryKeysValues = new ArrayList<String>();

            // Filling values
            final List<String> primaryKeys = itemDefinition.getPrimaryKeys();
            if (primaryKeys.isEmpty()) {
                primaryKeys.add(ItemHasUniqueId.ATTRIBUTE_ID);
            }

            for (final String key : primaryKeys) {
                primaryKeysValues.add(this.getAttributeValue(key));
            }

            apiid = APIID.makeAPIID(primaryKeysValues);
        }

        // Setting definition
        apiid.setItemDefinition(itemDefinition);

        return apiid;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEFAULT BEHAVIOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static boolean applyOutputModifiersByDefault = true;

    private static boolean applyInputModifiersByDefault = true;

    private static boolean applyValidatorsByDefault = true;

    private static boolean applyValidatorMandatoryByDefault = true;

    private Boolean applyOutputModifiers = null;

    private Boolean applyInputModifiers = null;

    private Boolean applyValidators = null;

    private Boolean applyValidatorMandatory = null;

    /**
     * @param applyOutputModifiersByDefault
     *            the applyOutputModifiersByDefault to set
     */
    public final static void setApplyOutputModifiersByDefault(final boolean applyOutputModifiersByDefault) {
        Item.applyOutputModifiersByDefault = applyOutputModifiersByDefault;
    }

    /**
     * @param applyInputModifiersByDefault
     *            the applyInputModifiersByDefault to set
     */
    public final static void setApplyInputModifiersByDefault(final boolean applyInputModifiersByDefault) {
        Item.applyInputModifiersByDefault = applyInputModifiersByDefault;
    }

    /**
     * @param applyValidatorsByDefault
     *            the applyValidatorsByDefault to set
     */
    public final static void setApplyValidatorsByDefault(final boolean applyValidatorsByDefault) {
        Item.applyValidatorsByDefault = applyValidatorsByDefault;
    }

    /**
     * @param applyValidatorMandatoryByDefault
     *            the applyValidatorMandatoryByDefault to set
     */
    public final static void setApplyValidatorMandatoryByDefault(final boolean applyValidatorMandatoryByDefault) {
        Item.applyValidatorMandatoryByDefault = applyValidatorMandatoryByDefault;
    }

    /**
     * @param applyOutputModifiers
     *            the applyOutputModifiers to set
     */
    @Override
    public final void setApplyOutputModifiers(final boolean applyOutputModifiers) {
        this.applyOutputModifiers = applyOutputModifiers;
    }

    /**
     * @param applyInputModifiers
     *            the applyInputModifiers to set
     */
    @Override
    public final void setApplyInputModifiers(final boolean applyInputModifiers) {
        this.applyInputModifiers = applyInputModifiers;
    }

    /**
     * @param applyValidators
     *            the applyValidators to set
     */
    @Override
    public final void setApplyValidators(final boolean applyValidators) {
        this.applyValidators = applyValidators;
    }

    /**
     * @param applyValidatorMandatory
     *            the applyValidatorMandatory to set
     */
    @Override
    public final void setApplyValidatorMandatory(final boolean applyValidatorMandatory) {
        this.applyValidatorMandatory = applyValidatorMandatory;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Set an attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final String value) {
        setAttribute(
                name,
                value,
                this.applyInputModifiers == null ? applyInputModifiersByDefault : this.applyInputModifiers,
                this.applyValidators == null ? applyValidatorsByDefault : this.applyValidators);
    }

    @Override
    public final void setAttribute(final String name, final Object value) {
        this.setAttribute(name, value != null ? value.toString() : null);
    }

    /**
     * Set a Date attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final Date value) {
        this.setAttribute(name, DateFormat.dateToSql(value));
    }

    /**
     * Set an attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final APIID value) {
        if (value != null) {
            this.setAttribute(name, value.toString());
        } else {
            setAttribute(name, (String) null);
        }
    }

    /**
     * Set an attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final String value, final boolean applyModifiers, final boolean applyValidators) {
        final ItemAttribute attribute = getItemDefinition().getAttribute(name);

        String realValue = value;
        if (attribute != null && applyModifiers) {
            realValue = ModifierEngine.modify(realValue, attribute.getInputModifiers());
        }

        this.attributes.put(name, realValue);
        if (applyValidators) {
            ValidatorEngine.validate(this, this.applyValidatorMandatory == null ? applyValidatorMandatoryByDefault : this.applyValidatorMandatory);
        }
    }

    /**
     * Set an attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final Object value, final boolean applyModifiers, final boolean applyValidators) {
        if (value != null) {
            setAttribute(name, value.toString(), applyModifiers, applyValidators);
        } else {
            setAttribute(name, (String) null, applyModifiers, applyValidators);
        }
    }

    /**
     * /**
     * Set an attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final APIID value, final boolean applyModifiers, final boolean applyValidators) {
        if (value != null) {
            setAttribute(name, value.toString(), applyModifiers, applyValidators);
        } else {
            setAttribute(name, (String) null, applyModifiers, applyValidators);
        }
    }

    /**
     * Set a Date attribute value.
     * <p>
     * The attribute is consider as non existent before the first call of this function. The JSonItemReader fills it.
     * 
     * @param name
     *            The name of the attribute. Must be the same as in the ItemDefinition.
     * @param value
     *            The value of the Item.
     */
    @Override
    public final void setAttribute(final String name, final Date value, final boolean applyModifiers, final boolean applyValidators) {
        setAttribute(name, DateFormat.dateToSql(value), applyModifiers, applyValidators);
    }

    /**
     * Set a deployed version of an attribute
     * 
     * @param attributeName
     *            The name of the attribute to deploy
     * @param item
     *            The deployed version of the attribute
     */
    @Override
    public final void setDeploy(final String attributeName, final IItem item) {
        this.deploys.put(attributeName, item);
    }

    /**
     * Remove a deployed version of an attribute
     * 
     * @param attributeName
     *            The name of the attribute deploy to remove
     */
    @Override
    public final void removeDeploy(final String attributeName) {
        this.deploys.remove(attributeName);
    }

    /**
     * Set a counter value.
     * 
     * @param counterName
     *            The name of the counter to set
     * @param value
     *            The value of the counter
     */
    // public final void setCounterValue(final String counterName, final Long value) {
    // this.counters.put(counterName, value);
    // }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Indicate if there are no attribute defined.
     * 
     * @return This methods returns TRUE if there are no attributes, otherwise FALSE.
     */
    @Override
    public final boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    /**
     * Indicate if the attribute exists even if its value is NULL or empty.
     * 
     * @param name
     *            The name of the attribute to check.
     * @return This method returns TRUE if the attribute exists, otherwise FALSE.
     */
    @Override
    public final boolean hasAttribute(final String name) {
        return this.attributes.containsKey(name);
    }

    /**
     * Get the value of an attribute
     * 
     * @param attributeName
     *            The name of the attribute
     * @param applyModifiers
     *            Indicate whether or not the output modifiers defined for this resource need to be apply.
     * @return This function returns the value of the attribute or the defaultValue set.
     */
    @Override
    public final String getAttributeValue(final String attributeName, final boolean applyModifiers) {

        // Detect deploy called using "thisAttributeToDeployName.deployedItemAttributeName"
        final String[] splittedAttribute = attributeName.split("\\.");

        // Read a deployed attribute
        if (splittedAttribute.length == 2) {
            final IItem deploy = getDeploy(splittedAttribute[0]);

            return deploy.getAttributeValue(splittedAttribute[1]);

        }

        // Read an id from a deployed attribute
        else if (this.deploys.containsKey(attributeName)) {

            final IItem deploy = getDeploy(attributeName);

            if (deploy == null) {
                return null;
            }

            return deploy.getId().toString();
        }

        // Read a local attribute
        else {

            final ItemAttribute attribute = getItemDefinition().getAttribute(attributeName);

            String realValue = this.attributes.get(attributeName);

            if (attribute != null && applyModifiers) {
                realValue = ModifierEngine.modify(realValue, attribute.getOutputModifiers());
            }

            if (this instanceof ItemHasDualName) {
                if (ItemHasDualName.ATTRIBUTE_DISPLAY_NAME.equals(attributeName) && StringUtil.isBlank(realValue)) {
                    realValue = this.getAttributeValue(ItemHasDualName.ATTRIBUTE_NAME);
                }
            } else if (this instanceof ItemHasDualDescription) {
                if (ItemHasDualDescription.ATTRIBUTE_DISPLAY_DESCRIPTION.equals(attributeName) && StringUtil.isBlank(realValue)) {
                    realValue = this.getAttributeValue(ItemHasDualDescription.ATTRIBUTE_DESCRIPTION);
                }
            }

            return realValue;
        }
    }

    /**
     * Get the value of an attribute
     * 
     * @param attributeName
     *            The name of the attribute
     * @return This function returns the value of the attribute or NULL if not set.
     */
    @Override
    public final String getAttributeValue(final String attributeName) {
        return this.getAttributeValue(attributeName, this.applyOutputModifiers == null ? applyOutputModifiersByDefault : this.applyOutputModifiers);
    }

    /**
     * Get the value of an attribute
     * 
     * @param itemAttribute
     *            The attribute
     * @return This function returns the value of the attribute or NULL if not set.
     */
    @Override
    public final String getAttributeValue(final ItemAttribute itemAttribute) {
        return this.getAttributeValue(itemAttribute.getName(), this.applyOutputModifiers == null ? applyOutputModifiersByDefault : this.applyOutputModifiers);
    }

    /**
     * Get the value of an attribute
     * 
     * @param itemAttribute
     *            The name of the attribute
     * @param applyModifiers
     *            Indicate whether or not the output modifiers defined for this resource need to be apply.
     * @return This function returns the value of the attribute or the defaultValue set.
     */
    @Override
    public final String getAttributeValue(final ItemAttribute itemAttribute, final boolean applyModifiers) {
        return this.getAttributeValue(itemAttribute.getName(), applyModifiers);
    }

    // AS APIID

    @Override
    public final APIID getAttributeValueAsAPIID(final String attributeName, final boolean applyModifiers) {
        return APIID.makeAPIID(getAttributeValue(attributeName, applyModifiers));
    }

    @Override
    public final APIID getAttributeValueAsAPIID(final String attributeName) {
        return APIID.makeAPIID(getAttributeValue(attributeName));
    }

    @Override
    public final APIID getAttributeValueAsAPIID(final ItemAttribute itemAttribute, final boolean applyModifiers) {
        return APIID.makeAPIID(getAttributeValue(itemAttribute, applyModifiers));
    }

    @Override
    public final APIID getAttributeValueAsAPIID(final ItemAttribute itemAttribute) {
        return APIID.makeAPIID(getAttributeValue(itemAttribute));
    }

    // AS Date

    @Override
    public final Date getAttributeValueAsDate(final String attributeName, final boolean applyModifiers) {
        return DateFormat.sqlToDate(getAttributeValue(attributeName, applyModifiers));
    }

    @Override
    public final Date getAttributeValueAsDate(final String attributeName) {
        return DateFormat.sqlToDate(getAttributeValue(attributeName));
    }

    @Override
    public final Date getAttributeValueAsDate(final ItemAttribute itemAttribute, final boolean applyModifiers) {
        return DateFormat.sqlToDate(getAttributeValue(itemAttribute, applyModifiers));
    }

    @Override
    public final Date getAttributeValueAsDate(final ItemAttribute itemAttribute) {
        return DateFormat.sqlToDate(getAttributeValue(itemAttribute));
    }

    // AS Long

    public final Long getAttributeValueAsLong(final String attributeName, final boolean applyModifiers) {
        return Long.valueOf(getAttributeValue(attributeName, applyModifiers));
    }

    public final Long getAttributeValueAsLong(final String attributeName) {
        return Long.valueOf(getAttributeValue(attributeName));
    }

    public final Long getAttributeValueAsLong(final ItemAttribute itemAttribute, final boolean applyModifiers) {
        return Long.valueOf(getAttributeValue(itemAttribute, applyModifiers));
    }

    public final Long getAttributeValueAsLong(final ItemAttribute itemAttribute) {
        return Long.valueOf(getAttributeValue(itemAttribute));
    }

    // Get all

    @Override
    public final Map<String, String> getAttributes() {
        return this.getAttributes(this.applyOutputModifiers == null ? applyOutputModifiersByDefault : this.applyOutputModifiers);
    }

    @Override
    public final Map<String, String> getAttributes(final boolean applyModifiers) {
        final Map<String, String> results = new HashMap<String, String>();

        for (final String attributeName : this.attributes.keySet()) {
            results.put(attributeName, this.getAttributeValue(attributeName, applyModifiers));
        }

        return results;
    }

    /**
     * Get a deployed version of an attribute
     * 
     * @param attributeName
     *            The name of the attribute to deploy
     * @return This method returns the deployed version of an attribute if it's available, otherwise NULL.
     */
    @Override
    public final IItem getDeploy(final String attributeName) {
        // TODO If not deployed, automatically call the API to deploy.

        return this.deploys.get(attributeName);
    }
    
    public Map<String, IItem> getDeploys() {
        return deploys;
    }

    /**
     * Get a counter value.
     * 
     * @param counterName
     *            The name of the counter to deploy
     * @return This method returns the counter value if it's available, otherwise NULL.
     */
    // public final Long getCounterValue(final String counterName) {
    // // TODO If not counted, automatically call the API to count.
    //
    // return this.counters.get(counterName);
    // }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final ArrayList<String> getAttributeNames() {
        return new ArrayList<String>(this.attributes.keySet());
    }

    @Override
    public final void setAttributes(final Map<String, String> attributes, final boolean applyModifiers, final boolean applyValidators) {
        if (attributes == null || attributes.size() == 0) {
            return;
        }

        for (final String attributeName : attributes.keySet()) {
            setAttribute(attributeName, attributes.get(attributeName), applyModifiers, false);
        }

        if (applyValidators) {
            ValidatorEngine.validate(this);
        }
    }

    @Override
    public final void setAttributes(final Map<String, String> attributes) {
        setAttributes(
                attributes,
                this.applyInputModifiers == null ? applyInputModifiersByDefault : this.applyInputModifiers,
                this.applyValidators == null ? applyValidatorsByDefault : this.applyValidators);
    }

    /**
     * Get the definition of an Item
     * <p>
     * This function must be overridden to return the definition corresponding to the Item type.b
     * 
     * @return This function return an instance of ItemDefinition for the current Item type
     */
    @Override
    abstract public ItemDefinition<?> getItemDefinition();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONVERT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String key : this.attributes.keySet()) {
            final String rawValue = this.attributes.get(key);
            final String cleanValue = this.getAttributeValue(key);

            sb.append(key).append(" : ").append(rawValue);

            if (rawValue != null && !rawValue.equals(cleanValue)) {
                sb.append(" >> ").append(cleanValue);
            }

            sb.append("\r\n");
        }

        return sb.toString();
    }

    @Override
    public final String toJson() {

        final StringBuilder json = new StringBuilder().append("{");

        boolean first = true;
        for (final String attribute : getAttributeNames()) {
            if (this.deploys.containsKey(attribute)) {
                json.append(!first ? "," : "").append(JSonSerializer.quote(attribute)).append(":")
                        .append(JSonSerializer.serialize(this.deploys.get(attribute)));
            } else {
                json.append(!first ? "," : "").append(JSonSerializer.quote(attribute)).append(":")
                        .append(JSonSerializer.quote(this.getAttributeValue(attribute)));
            }
            first = false;
        }
        // for (final String counter : this.counters.keySet()) {
        // json.append(!first ? "," : "").append(JSonSerializer.quote(counter)).append(":")
        // .append(JSonSerializer.serialize(Long.toString(this.counters.get(counter))));
        // }

        json.append("}");

        return json.toString();

    }
}
