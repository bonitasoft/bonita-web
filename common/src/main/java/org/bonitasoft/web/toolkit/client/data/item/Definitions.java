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

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.ItemDefinitionFactory;

/**
 * @author Julien Mege
 */
public class Definitions {

    private final Map<String, ItemDefinition<?>> itemDefinitions = new HashMap<String, ItemDefinition<?>>();

    private static final Definitions INSTANCE = new Definitions();

    /**
     * Get the ViewController instance.
     *
     * @return the unique instance of the ViewController.
     */
    public static Definitions getInstance() {
        return INSTANCE;
    }

    public static ItemDefinition<?> get(final String token) {
        return getInstance().getDefinition(token);
    }

    public final ItemDefinition<?> getDefinition(final String token) {
        if (itemDefinitions.containsKey(token)) {
            return itemDefinitions.get(token);
        } else if (DummyItemDefinition.TOKEN.equals(token)) {
            return new DummyItemDefinition();
        } else {
            final ItemDefinition<?> itemDefinition = ItemDefinitionFactory.getDefaultFactory().defineItemDefinitions(token);
            if (itemDefinition != null) {
                itemDefinitions.put(token, itemDefinition);
                return itemDefinition;
            }
            // TODO Throw exception
            return null;
        }
    }

}
