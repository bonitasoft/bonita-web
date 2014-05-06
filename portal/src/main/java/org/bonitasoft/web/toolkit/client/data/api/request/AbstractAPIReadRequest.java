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
package org.bonitasoft.web.toolkit.client.data.api.request;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

/**
 * @author Séverin Moussel
 * 
 */
abstract class AbstractAPIReadRequest extends AbstractAPIRequest {

    public static final String PARAMETER_COUNTER = "n";

    public static final String PARAMETER_DEPLOY = "d";

    private final List<String> deploys = new ArrayList<String>();

    private final List<String> counters = new ArrayList<String>();

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AbstractAPIReadRequest(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the defined deploys.
     * 
     * @return the deploys
     */
    public List<String> getDeploys() {
        return this.deploys;
    }

    /**
     * Add a deploy.
     * 
     * @param deploy
     *            The deploy to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest addDeploy(final String deploy) {
        this.deploys.add(deploy);
        return this;
    }

    /**
     * Set multiple deploys.<br />
     * This method will remove previously defined deploys.
     * 
     * @param deploys
     *            The list of deploys to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest setDeploys(final String... deploys) {
        return setDeploys(Arrays.asList(deploys));
    }

    /**
     * Set multiple deploys.<br />
     * This method will remove previously defined deploys.
     * 
     * 
     * @param deploys
     *            The list of deploys to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest setDeploys(final List<String> deploys) {
        this.deploys.clear();
        return addDeploys(deploys);
    }

    /**
     * Add multiple deploys.
     * 
     * @param deploys
     *            The list of deploys to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest addDeploys(final String... deploys) {
        return addDeploys(Arrays.asList(deploys));
    }

    /**
     * Add multiple deploys.
     * 
     * @param deploys
     *            The list of deploys to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest addDeploys(final List<String> deploys) {
        if (deploys != null) {
            this.deploys.addAll(deploys);
        }
        return this;
    }

    /**
     * Remove an already defined deploy
     * 
     * @param deploy
     *            The deploy to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeDeploy(final String deploy) {
        this.deploys.remove(deploy);
        return this;
    }

    /**
     * Remove multiple deploys.
     * 
     * @param deploys
     *            The list of deploys to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeDeploys(final String... deploys) {
        removeDeploys(Arrays.asList(deploys));
        return this;
    }

    /**
     * Remove multiple deploys.
     * 
     * @param deploys
     *            The list of deploys to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeDeploys(final List<String> deploys) {
        this.deploys.removeAll(deploys);
        return this;
    }

    /**
     * Remove all deploys.
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest clearDeploys() {
        this.deploys.clear();
        return this;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Get the defined counters.
     * 
     * @return the counters
     */
    public List<String> getCounters() {
        return this.counters;
    }

    /**
     * Add a counter.
     * 
     * @param counter
     *            The counter to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest addCounter(final String counter) {
        this.counters.add(counter);
        return this;
    }

    /**
     * Set multiple counters.<br />
     * This method will remove previously defined counters.
     * 
     * @param counters
     *            The list of counters to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest setCounters(final String... counters) {
        return setCounters(Arrays.asList(counters));
    }

    /**
     * Set multiple counters.<br />
     * This method will remove previously defined counters.
     * 
     * 
     * @param counters
     *            The list of counters to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest setCounters(final List<String> counters) {
        this.counters.clear();
        return addCounters(counters);
    }

    /**
     * Add multiple counters.
     * 
     * @param counters
     *            The list of counters to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest addCounters(final String... counters) {
        return addCounters(Arrays.asList(counters));
    }

    /**
     * Add multiple counters.
     * 
     * @param counters
     *            The list of counters to add.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest addCounters(final List<String> counters) {
        if (counters != null) {
            this.counters.addAll(counters);
        }
        return this;
    }

    /**
     * Remove an already defined counter
     * 
     * @param counter
     *            The counter to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeCounter(final String counter) {
        this.counters.remove(counter);
        return this;
    }

    /**
     * Remove multiple counters.
     * 
     * @param counters
     *            The list of counters to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeCounters(final String... counters) {
        removeCounters(Arrays.asList(counters));
        return this;
    }

    /**
     * Remove multiple counters.
     * 
     * @param counters
     *            The list of counters to remove.
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest removeCounters(final List<String> counters) {
        this.counters.removeAll(counters);
        return this;
    }

    /**
     * Remove all counters.
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractAPIReadRequest clearCounters() {
        this.counters.clear();
        return this;
    }

}
