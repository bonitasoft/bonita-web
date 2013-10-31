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

import java.util.List;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiableOutput;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList;
import org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier;
import org.bonitasoft.web.toolkit.client.ui.JsId;

/**
 * @author Julien Mege
 * 
 */
public abstract class AbstractAttributeReader implements ModifiableOutput {

    protected String leadAttribute = null;

    protected String className = null;

    private String defaultValue = "";

    public AbstractAttributeReader() {
        this(JsId.getRandom().toString());
    }

    public AbstractAttributeReader(final String leadAttribute) {
        this(leadAttribute, leadAttribute);
    }

    public AbstractAttributeReader(final String leadAttribute, final String className) {
        this.leadAttribute = leadAttribute;
        this.className = className;
    }

    public final String read(final IItem item) {

        final String value = _read(item);
        if (!StringUtil.isBlank(value)) {
            return outputModifiers.apply(value);
        } else {
            return defaultValue;
        }

    }

    abstract protected String _read(IItem item);

    /**
     * @return the leadAttribute
     */
    public final String getLeadAttribute() {
        return leadAttribute;
    }

    public final String getClassName() {
        return className;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OUTPUT MODIFIERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private final ModifiersList outputModifiers = new ModifiersList();

    /**
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#getModifiers()
     */
    @Override
    public List<Modifier> getOutputModifiers() {
        return outputModifiers.getModifiers();
    }

    /**
     * @param modifier
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifier(org.bonitasoft.web.toolkit.client.data.item.attribute.modifier.Modifier)
     * @return This method returns "this" to allow cascading calls.
     */
    @Override
    public AbstractAttributeReader addOutputModifier(final Modifier modifier) {
        outputModifiers.addModifier(modifier);
        return this;
    }

    /**
     * @param modifiers
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#addModifiers(java.util.List)
     * @return This method returns "this" to allow cascading calls.
     */
    @Override
    public AbstractAttributeReader addOutputModifiers(final List<Modifier> modifiers) {
        outputModifiers.addModifiers(modifiers);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#removeModifier(java.lang.String)
     * @return This method returns "this" to allow cascading calls.
     */
    @Override
    public AbstractAttributeReader removeOutputModifier(final String modifierClassName) {
        outputModifiers.removeModifier(modifierClassName);
        return this;
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#hasModifier(java.lang.String)
     */
    @Override
    public boolean hasOutputModifier(final String modifierClassName) {
        return outputModifiers.hasModifier(modifierClassName);
    }

    /**
     * @param modifierClassName
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.ModifiersList#getModifier(java.lang.String)
     */
    @Override
    public Modifier getOutputModifier(final String modifierClassName) {
        return outputModifiers.getModifier(modifierClassName);
    }

    /**
     * @return the defaultValue
     */
    protected final String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue
     *            the defaultValue to set
     */
    public final AbstractAttributeReader setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

}
