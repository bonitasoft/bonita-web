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
package org.bonitasoft.web.toolkit.client.data.item;

/**
 * @author Séverin Moussel
 * 
 */
public class DummyItemDefinition extends ItemDefinition<DummyItem> {

    /**
     * Singleton
     */
    public static DummyItemDefinition get() {
        return (DummyItemDefinition) Definitions.get(TOKEN);
    }

    public static final String TOKEN = "dummy";

    @Override
    protected String defineToken() {
        return TOKEN;
    }

    @Override
    protected String defineAPIUrl() {
        return null;
    }

    @Override
    protected void defineAttributes() {
    }

    @Override
    protected void definePrimaryKeys() {
    }

    @Override
    protected DummyItem _createItem() {
        return new DummyItem();
    }

}
