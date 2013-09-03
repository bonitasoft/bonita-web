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
package org.bonitasoft.web.rest.server.framework;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.JSonUnserializerServer;
import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIMalformedUrlException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIMissingIdException;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemWriter;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.request.APISearchRequest;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ValidatorEngine;
import org.bonitasoft.web.toolkit.server.ServletCall;

/**
 * @author Séverin Moussel
 * 
 */
public class APIServletCall extends ServletCall {

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // REQUEST PARSING
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private API<? extends IItem> api;

    private String apiName;

    private String resourceName;

    private APIID id;

    public APIServletCall(final HttpServletRequest request, final HttpServletResponse response) {
        super(request, response);

        final Date expdate = new Date();
        expdate.setTime(expdate.getTime() - 3600000 * 24);
        final SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        head("Pragma", "No-cache");
        head("Cache-Control", "no-cache,no-store,no-transform,max-age=0");
        head("Expires", df.format(expdate));
    }

    // /**
    // * @param parameter
    // * @return
    // */
    // private List<ItemSearchOrder> parseOrders(final String parameter) {
    // if (this.parameters == null) {
    // return null;
    // }
    // final String[] split = parameter.split("\\s*,\\s*");
    // final List<ItemSearchOrder> results = new ArrayList<ItemSearchOrder>();
    // for (int i = 0; i < split.length; i++) {
    // final String[] orderSplit = split[i].split("\\s");
    // results.add(new ItemSearchOrder(orderSplit[0], orderSplit.length == 1 || orderSplit[1].equalsIgnoreCase("asc")));
    // }
    //
    // return results;
    // }

    /**
     * Read the inputStream and parse it as an IItem compatible with the called API.
     */
    private IItem getJSonStreamAsItem() {
        final IItem item = JSonItemReader.parseItem(getInputStream(), api.getItemDefinition());

        ValidatorEngine.validate(item, false);

        return item;
    }

    /**
     * Read elements form the request
     * <ul>
     * <li>API tokens</li>
     * <li>item id (if defined)</li>
     * <li>parameters</li>
     * </ul>
     * 
     * @param request
     */
    @Override
    protected final void parseRequest(final HttpServletRequest request) {

        final String[] path = request.getPathInfo().split("/");

        // Read API tokens
        if (path.length < 3) {
            throw new APIMalformedUrlException("Missing API or resource name [" + request.getRequestURL() + "]");
        }

        apiName = path[1];
        resourceName = path[2];

        api = APIs.get(apiName, resourceName);
        api.setCaller(this);

        // Read id (if defined)
        if (path.length > 3) {
            final List<String> pathList = Arrays.asList(path);
            id = APIID.makeAPIID(pathList.subList(3, pathList.size()));
        } else {
            id = null;
        }

        super.parseRequest(request);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EXECUTE METHODS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Entry point for GET and SEARCH
     */
    @Override
    public final void doGet() {
        try {
            // GET one
            if (id != null) {
                output(api.runGet(id, getParameterAsList(APISearchRequest.PARAMETER_DEPLOY), getParameterAsList(APISearchRequest.PARAMETER_COUNTER)));
            } else if (countParameters() == 0) {
                throw new APIMissingIdException(getRequestURL());
            }
            // Search
            else {

                final ItemSearchResult<?> result = api.runSearch(Integer.parseInt(getParameter(APISearchRequest.PARAMETER_PAGE, "0")),
                        Integer.parseInt(getParameter(APISearchRequest.PARAMETER_LIMIT, "10")), getParameter(APISearchRequest.PARAMETER_SEARCH),
                        getParameter(APISearchRequest.PARAMETER_ORDER), parseFilters(getParameterAsList(APISearchRequest.PARAMETER_FILTER)),
                        getParameterAsList(APISearchRequest.PARAMETER_DEPLOY), getParameterAsList(APISearchRequest.PARAMETER_COUNTER));

                head("Content-Range", result.getPage() + "-" + result.getLength() + "/" + result.getTotal());

                output(result.getResults());
            }
        } catch (final APIException e) {
            e.setApi(apiName);
            e.setResource(resourceName);
            throw e;
        }
    }

    /**
     * Entry point for CREATE
     */
    @Override
    public final void doPost() {
        try {
            final IItem jSonStreamAsItem = getJSonStreamAsItem();
            final IItem outputItem = api.runAdd(jSonStreamAsItem);

            output(JSonItemWriter.itemToJSON(outputItem));
        } catch (final APIException e) {
            e.setApi(apiName);
            e.setResource(resourceName);
            throw e;

        }
    }

    /**
     * Entry point for UPDATE
     */
    @Override
    public final void doPut() {
        try {
            if (id == null) {
                throw new APIMissingIdException(getRequestURL());
            }

            final String inputStream = getInputStream();
            if (inputStream.length() == 0) {
                api.runUpdate(id, new HashMap<String, String>());
                return;
            }

            Item.setApplyValidatorMandatoryByDefault(false);
            final Map<String, String> attributes = getJSonStreamAsItem().getAttributes();

            api.runUpdate(id, attributes);
        } catch (final APIException e) {
            e.setApi(apiName);
            e.setResource(resourceName);
            throw e;

        }
    }

    /**
     * Entry point for DELETE
     */
    @Override
    public final void doDelete() {
        try {
            final List<APIID> ids = new ArrayList<APIID>();

            // Using ids in json input stream
            if (id == null) {
                final String inputStream = getInputStream();
                if (inputStream.length() == 0) {
                    throw new APIMissingIdException("Id of the element to delete is missing [" + inputStream + "]");
                }

                // Parsing ids in Json input stream
                final AbstractTreeNode<String> tree = JSonUnserializerServer.unserializeTree(inputStream);

                if (tree instanceof Tree<?>) {
                    final List<AbstractTreeNode<String>> nodes = ((Tree<String>) tree).getNodes();

                    for (final AbstractTreeNode<String> node : nodes) {
                        if (node instanceof Tree<?>) {
                            ids.add(APIID.makeAPIID(((Tree<String>) node).getValues()));
                        } else if (node instanceof TreeLeaf<?>) {
                            ids.add(APIID.makeAPIID(((TreeLeaf<String>) node).getValue()));
                        } else {
                            throw new APIMissingIdException("Id of the elements to delete are missing or misswritten \"" + inputStream + "\"");
                        }
                    }
                } else {
                    throw new APIMissingIdException("Id of the elements to delete are missing or misswritten \"" + inputStream + "\"");
                }
            }

            // Using id in URL
            else {
                ids.add(id);
            }

            api.runDelete(ids);
        } catch (final APIException e) {
            e.setApi(apiName);
            e.setResource(resourceName);
            throw e;

        }
    }

    /**
     * @param parameter
     * @return
     */
    private Map<String, String> parseFilters(final List<String> parameters) {
        if (parameters == null) {
            return null;
        }
        final Map<String, String> results = new HashMap<String, String>();
        for (final String parameter : parameters) {
            final String[] split = parameter.split("=");
            if (split.length < 2) {
                results.put(split[0], null);
                // throw new APIMalformedUrlException("Malformed filter parameter : " + parameter);
            } else {
                results.put(split[0], split[1]);
            }
        }
        return results;
    }
}
