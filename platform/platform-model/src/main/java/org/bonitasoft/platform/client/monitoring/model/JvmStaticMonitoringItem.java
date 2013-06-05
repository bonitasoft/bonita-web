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
package org.bonitasoft.platform.client.monitoring.model;

import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Ruiheng Fan
 * 
 */
public class JvmStaticMonitoringItem extends Item {

    public JvmStaticMonitoringItem() {
        super();
    }

    public JvmStaticMonitoringItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_JVMNAME = "jvmName";

    public static final String ATTRIBUTE_JVMVENDOR = "jvmVendor";

    public static final String ATTRIBUTE_JVMVERSION = "jvmVersion";

    public static final String ATTRIBUTE_OSARCH = "OSArch";

    public static final String ATTRIBUTE_OSNAME = "OSName";

    public static final String ATTRIBUTE_OSVERSION = "OSVersion";

    public static final String ATTRIBUTE_AVAILABLEPROCESSORS = "availableProcessors";

    // private Map<String, String> JVMSystemProperties;

    /**
     * 
     */
    private static final long serialVersionUID = -7329122730459385706L;

    /**
     * Default Constructor.
     * 
     * @param jvmName
     * @param jvmVendor
     * @param jvmVersion
     * @param OSArch
     * @param OSName
     * @param OSVersion
     * @param startTime
     * @param JVMSystemProperties
     * @param itemDefinition
     */
    protected JvmStaticMonitoringItem(final String jvmName, final String jvmVendor, final String jvmVersion, final String OSArch, final String OSName,
            final String OSVersion, final String availableProcessors) {
        super();
        this.setAttribute(ATTRIBUTE_JVMNAME, jvmName);
        this.setAttribute(ATTRIBUTE_JVMVENDOR, jvmVendor);
        this.setAttribute(ATTRIBUTE_JVMVERSION, jvmVersion);
        this.setAttribute(ATTRIBUTE_OSARCH, OSArch);
        this.setAttribute(ATTRIBUTE_OSNAME, OSName);
        this.setAttribute(ATTRIBUTE_OSVERSION, OSVersion);
        this.setAttribute(ATTRIBUTE_AVAILABLEPROCESSORS, availableProcessors);
        // this.JVMSystemProperties = JVMSystemProperties;
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new JvmStaticMonitoringDefinition();
    }

}
