/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.model.bpm.contract;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;

/**
 * @author Laurent Leseigneur
 */
public class BpmContractItem extends Item {

    public static final String ATTRIBUTE_INPUTS = "inputs";
    public static final String ATTRIBUTE_RULES = "rules";

    /**
     * Default Constructor.
     */
    public BpmContractItem() {
        super();
    }

    @Override
    public BpmContractDefinition getItemDefinition() {
        return new BpmContractDefinition();
    }

    public String getInputs() {
        return this.getAttributeValue(ATTRIBUTE_INPUTS);
    }

    public IItem setInputs(final String name) {
        this.setAttribute(ATTRIBUTE_INPUTS, name);
        return this;
    }

    public String getRules() {
        return this.getAttributeValue(ATTRIBUTE_RULES);
    }

    public IItem setRules(final String name) {
        this.setAttribute(ATTRIBUTE_RULES, name);
        return this;
    }


}
