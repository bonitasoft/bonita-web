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

import java.util.List;

import org.bonitasoft.web.toolkit.client.common.UrlBuilder;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;

import com.google.gwt.http.client.RequestBuilder;

/**
 * @author Séverin Moussel
 * 
 */
public class APIGetRequest extends AbstractAPIReadRequest {

    protected APIID id;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public APIGetRequest(final ItemDefinition itemDefinition) {
        super(itemDefinition);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param id
     *            the id to set
     */
    public APIGetRequest setId(final APIID id) {
        this.id = id;
        return this;
    }

    /**
     * @param id
     *            the id to set
     */
    public APIGetRequest setId(final String id) {
        return setId(APIID.makeAPIID(id));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public APIGetRequest addDeploy(final String deploy) {
        return (APIGetRequest) super.addDeploy(deploy);
    }

    @Override
    public APIGetRequest setDeploys(final String... deploys) {
        return (APIGetRequest) super.setDeploys(deploys);
    }

    @Override
    public APIGetRequest setDeploys(final List<String> deploys) {
        return (APIGetRequest) super.setDeploys(deploys);
    }

    @Override
    public APIGetRequest addDeploys(final String... deploys) {
        return (APIGetRequest) super.addDeploys(deploys);
    }

    @Override
    public APIGetRequest addDeploys(final List<String> deploys) {
        return (APIGetRequest) super.addDeploys(deploys);
    }

    @Override
    public APIGetRequest removeDeploy(final String deploy) {
        return (APIGetRequest) super.removeDeploy(deploy);
    }

    @Override
    public APIGetRequest removeDeploys(final String... deploys) {
        return (APIGetRequest) super.removeDeploys(deploys);
    }

    @Override
    public APIGetRequest removeDeploys(final List<String> deploys) {
        return (APIGetRequest) super.removeDeploys(deploys);
    }

    @Override
    public APIGetRequest clearDeploys() {
        return (APIGetRequest) super.clearDeploys();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // COUNTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public APIGetRequest addCounter(final String counter) {
        return (APIGetRequest) super.addCounter(counter);
    }

    @Override
    public APIGetRequest setCounters(final String... counters) {
        return (APIGetRequest) super.setCounters(counters);
    }

    @Override
    public APIGetRequest setCounters(final List<String> counters) {
        return (APIGetRequest) super.setCounters(counters);
    }

    @Override
    public APIGetRequest addCounters(final String... counters) {
        return (APIGetRequest) super.addCounters(counters);
    }

    @Override
    public APIGetRequest addCounters(final List<String> counters) {
        return (APIGetRequest) super.addCounters(counters);
    }

    @Override
    public APIGetRequest removeCounter(final String counter) {
        return (APIGetRequest) super.removeCounter(counter);
    }

    @Override
    public APIGetRequest removeCounters(final String... counters) {
        return (APIGetRequest) super.removeCounters(counters);
    }

    @Override
    public APIGetRequest removeCounters(final List<String> counters) {
        return (APIGetRequest) super.removeCounters(counters);
    }

    @Override
    public APIGetRequest clearCounters() {
        return (APIGetRequest) super.clearCounters();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // RUN
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void run() {
        final UrlBuilder url = new UrlBuilder(this.itemDefinition.getAPIUrl() + "/" + this.id.toString());
        if (getDeploys() != null && getDeploys().size() > 0) {
            url.addParameter(PARAMETER_DEPLOY, getDeploys());
        }
        if (getCounters() != null && getCounters().size() > 0) {
            url.addParameter(PARAMETER_COUNTER, getCounters());
        }

        this.request = new RequestBuilder(RequestBuilder.GET, url.toString());
        super.run();
    }

}
