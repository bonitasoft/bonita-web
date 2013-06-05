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

import java.util.Map;

/**
 * @author Ruiheng Fan
 * 
 */
public class MonitoringItemBuilder {

    private String jvmName;

    private String jvmVendor;

    private String jvmVersion;

    private String OSArch;

    private String OSName;

    private String OSVersion;

    private int availableProcessors;

    private Map<String, String> JVMSystemProperties;

    double systemLoadAverage;

    long currentMemoryUsage;

    float memoryUsagePercentage;

    int threadCount;

    long totalThreadsCpuTime;

    long upTime;

    private String startTime;

    long now;

    /**
     * Get JVMStaticMonitoringItem
     * 
     * @param itemDefinition
     * @return JVMStaticMonitoringItem
     */
    public JvmStaticMonitoringItem getJVMStaticMonitoringItem() {
        return new JvmStaticMonitoringItem(this.jvmName, this.jvmVendor, this.jvmVersion, this.OSArch, this.OSName, this.OSVersion,
                String.valueOf(this.availableProcessors));
    }

    /**
     * Get JVMDynamicMonitoringItem
     * 
     * @param itemDefinition
     * @return JVMDynamicMonitoringItem
     */
    public JvmDynamicMonitoringItem getJVMDynamicMonitoringItem() {
        return new JvmDynamicMonitoringItem(String.valueOf(this.systemLoadAverage), String.valueOf(this.currentMemoryUsage),
                String.valueOf(this.memoryUsagePercentage), String.valueOf(this.threadCount),
                String.valueOf(this.totalThreadsCpuTime), String.valueOf(this.upTime), this.startTime, String.valueOf(this.now));
    }

    /**
     * @param jvmName
     *            the jvmName to set
     */
    public void setJvmName(final String jvmName) {
        this.jvmName = jvmName;
    }

    /**
     * @param jvmVendor
     *            the jvmVendor to set
     */
    public void setJvmVendor(final String jvmVendor) {
        this.jvmVendor = jvmVendor;
    }

    /**
     * @param jvmVersion
     *            the jvmVersion to set
     */
    public void setJvmVersion(final String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    /**
     * @param oSArch
     *            the oSArch to set
     */
    public void setOSArch(final String oSArch) {
        this.OSArch = oSArch;
    }

    /**
     * @param oSName
     *            the oSName to set
     */
    public void setOSName(final String oSName) {
        this.OSName = oSName;
    }

    /**
     * @param oSVersion
     *            the oSVersion to set
     */
    public void setOSVersion(final String oSVersion) {
        this.OSVersion = oSVersion;
    }

    /**
     * @param jVMSystemProperties
     *            the jVMSystemProperties to set
     */
    public void setJVMSystemProperties(final Map<String, String> jVMSystemProperties) {
        this.JVMSystemProperties = jVMSystemProperties;
    }

    /**
     * @param d
     *            the systemLoadAverage to set
     */
    public void setSystemLoadAverage(final double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    /**
     * @param l
     *            the currentMemoryUsage to set
     */
    public void setCurrentMemoryUsage(final long currentMemoryUsage) {
        this.currentMemoryUsage = currentMemoryUsage;
    }

    /**
     * @param memoryUsagePercentage
     *            the memoryUsagePercentage to set
     */
    public void setMemoryUsagePercentage(final float memoryUsagePercentage) {
        this.memoryUsagePercentage = memoryUsagePercentage;
    }

    /**
     * @param i
     *            the threadCount to set
     */
    public void setThreadCount(final int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * @param l
     *            the totalThreadsCpuTime to set
     */
    public void setTotalThreadsCpuTime(final long totalThreadsCpuTime) {
        this.totalThreadsCpuTime = totalThreadsCpuTime;
    }

    /**
     * @param upTime
     *            the upTime to set
     */
    public void setUpTime(final long upTime) {
        this.upTime = upTime;
    }

    /**
     * @param availableProcessors
     *            the availableProcessors to set
     */
    public void setAvailableProcessors(final int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

    /**
     * @param startTime
     *            the startTime to set
     */
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }

    /**
     * @param now
     *            the now to set
     */
    public void setNow(final long now) {
        this.now = now;
    }

}
