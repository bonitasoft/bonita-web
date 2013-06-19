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
package org.bonitasoft.web.toolkit.server.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.server.utils.JSonUnserializerServer;
import org.bonitasoft.web.toolkit.server.utils.ServerDateFormater;

/**
 * Server side entry point
 * 
 * @author Julien Mege, Séverin Moussel
 */
public abstract class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final javax.servlet.ServletContextEvent arg0) {
        init();

        Item.setApplyInputModifiersByDefault(true);
        Item.setApplyValidatorsByDefault(true);
        Item.setApplyOutputModifiersByDefault(false);

        JSonItemReader.setUnserializer(new JSonUnserializerServer());

        CommonDateFormater.setDateFormater(new ServerDateFormater());
    }

    @Override
    public void contextDestroyed(final ServletContextEvent arg0) {
    }

    public abstract void init();
}
