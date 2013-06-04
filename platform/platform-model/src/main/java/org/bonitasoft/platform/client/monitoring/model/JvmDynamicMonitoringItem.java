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
public class JvmDynamicMonitoringItem extends Item {

    public JvmDynamicMonitoringItem() {
        super();
    }

    public JvmDynamicMonitoringItem(final IItem item) {
        super(item);
    }

    public static final String ATTRIBUTE_SYSYTEMLOADAVERAGE = "systemLoadAverage";

    public static final String ATTRIBUTE_CURRENTMAMORYUSAGE = "currentMemoryUsage";

    public static final String ATTRIBUTE_MEMORYUSAGEPERCENTAGE = "memoryUsagePercentage";

    public static final String ATTRIBUTE_THREADCOUNT = "threadCount";

    public static final String ATTRIBUTE_TOTALTHREADSCPUTIME = "totalThreadsCpuTime";

    public static final String ATTRIBUTE_UPTIME = "upTime";

    public static final String ATTRIBUTE_STARTTIME = "startTime";

    public static final String ATTRIBUTE_NOW = "now";

    /**
     * Default Constructor.
     * 
     * @param availableProcessors
     * @param systemLoadAverage
     * @param currentMemoryUsage
     * @param memoryUsagePercentage
     * @param threadCount
     * @param totalThreadsCpuTime
     * @param upTime
     * @param itemDefinition
     */
    protected JvmDynamicMonitoringItem(final String systemLoadAverage, final String currentMemoryUsage, final String memoryUsagePercentage,
            final String threadCount, final String totalThreadsCpuTime, final String upTime, final String startTime, final String now) {
        super();
        this.setAttribute(ATTRIBUTE_SYSYTEMLOADAVERAGE, systemLoadAverage);
        this.setAttribute(ATTRIBUTE_CURRENTMAMORYUSAGE, currentMemoryUsage);
        this.setAttribute(ATTRIBUTE_MEMORYUSAGEPERCENTAGE, memoryUsagePercentage);
        this.setAttribute(ATTRIBUTE_THREADCOUNT, threadCount);
        this.setAttribute(ATTRIBUTE_TOTALTHREADSCPUTIME, totalThreadsCpuTime);
        this.setAttribute(ATTRIBUTE_UPTIME, upTime);
        this.setAttribute(ATTRIBUTE_STARTTIME, startTime);
        this.setAttribute(ATTRIBUTE_NOW, now);
    }

    @Override
    public ItemDefinition getItemDefinition() {
        return new JvmDynamicMonitoringDefinition();
    }

}
