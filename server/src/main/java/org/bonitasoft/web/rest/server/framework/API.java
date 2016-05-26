/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.rest.server.framework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.bonitasoft.console.common.server.utils.UnauthorizedFolderException;
import org.bonitasoft.web.rest.server.framework.api.Datastore;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasAdd;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasDelete;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasGet;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasSearch;
import org.bonitasoft.web.rest.server.framework.api.DatastoreHasUpdate;
import org.bonitasoft.web.rest.server.framework.exception.APIFileUploadNotFoundException;
import org.bonitasoft.web.rest.server.framework.exception.ForbiddenAttributesException;
import org.bonitasoft.web.rest.server.framework.search.ItemSearchResult;
import org.bonitasoft.web.rest.server.framework.utils.FilePathBuilder;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIForbiddenException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIItemNotFoundException;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIMethodNotAllowedException;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.DummyItem;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasCreator;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasLastUpdateDate;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasLastUpdater;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Séverin Moussel
 */
public abstract class API<ITEM extends IItem> {

    protected ItemDefinition<ITEM> itemDefinition = null;

    private final Map<String, Deployer> deployers = new HashMap<>();

    private static Logger LOGGER = Logger.getLogger(API.class.getName());

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public API() {
        this.itemDefinition = defineItemDefinition();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SETTERS AND GETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The ServletCall responsible of this service.
     */
    private APIServletCall caller = null;

    /**
     * Set the caller.
     *
     * @param caller
     *        The ServletCall responsible of this service.
     */
    public final void setCaller(final APIServletCall caller) {
        this.caller = caller;
    }

    /**
     * Get the caller.
     */
    protected final APIServletCall getCaller() {
        return this.caller;
    }

    /**
     * @return the itemDefinition
     */
    public final ItemDefinition<ITEM> getItemDefinition() {
        return this.itemDefinition;
    }

    /**
     * Define the ItemDefinition for current class
     */
    protected ItemDefinition<ITEM> defineItemDefinition() {
        // FIXME [API V2] Make this method abstract after suppression of API V1
        return null;
    }

    /**
     * @see org.bonitasoft.web.toolkit.server.ServletCall#getHttpSession()
     */
    protected final HttpSession getHttpSession() {
        return this.caller.getHttpSession();
    }

    protected String getLocale() {
        return this.caller.getLocale();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // INPUT / OUTPUT
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final String getParameterAsString(final String parameterName) {
        return parameterName;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ENTRY POINTS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    public ITEM runAdd(final IItem item) {
        // FIXME Activate at end of APIs refactoring
        // if (!(this instanceof APIHasAdd)) {
        // throw new APIMethodNotAllowedException("POST method not allowed.");
        // }

        // Stop there if forbidden attributes are set
        checkForbiddenAttributes(item.getAttributes());

        // Run specific implementation
        return add((ITEM) item);
    }

    @SuppressWarnings("unchecked")
    public ITEM add(final ITEM item) {
        final Datastore datastore = getDefaultDatastore();

        if (datastore == null || !(datastore instanceof DatastoreHasAdd<?>)) {
            throw new APIMethodNotAllowedException("POST method not allowed.");
        }

        return ((DatastoreHasAdd<ITEM>) datastore).add(item);
    }

    public ITEM runUpdate(final APIID id, final Map<String, String> attributes) {
        // FIXME Activate at end of APIs refactoring
        // if (!(this instanceof APIHasUpdate)) {
        // throw new APIMethodNotAllowedException("PUT method not allowed.");
        // }

        id.setItemDefinition(getItemDefinition());

        // Stop there if forbidden attributes are set
        checkForbiddenAttributes(attributes);

        // Run specific implementation
        return this.update(id, attributes);
    }

    @SuppressWarnings("unchecked")
    public ITEM update(final APIID id, final Map<String, String> attributes) {
        final Datastore datastore = getDefaultDatastore();

        if (datastore == null || !(datastore instanceof DatastoreHasUpdate<?>)) {
            throw new APIMethodNotAllowedException("PUT method not allowed.");
        }

        return ((DatastoreHasUpdate<ITEM>) datastore).update(id, attributes);

    }

    public ITEM runGet(final APIID id, final List<String> deploys, final List<String> counters) {
        // FIXME Activate at end of APIs refactoring
        // if (!(this instanceof APIHasGet)) {
        // throw new APIMethodNotAllowedException("GET method not allowed.");
        // }

        id.setItemDefinition(getItemDefinition());

        final ITEM item = get(id);
        if (item == null) {
            throw new APIItemNotFoundException(getItemDefinition().getToken(), id);
        }

        fillDeploys(item, deploys != null ? deploys : new ArrayList<String>());
        fillCounters(item, counters != null ? counters : new ArrayList<String>());

        return item;
    }

    @SuppressWarnings("unchecked")
    public ITEM get(final APIID id) {
        final Datastore datastore = getDefaultDatastore();

        if (datastore == null || !(datastore instanceof DatastoreHasGet<?>)) {
            throw new APIMethodNotAllowedException("GET method not allowed.");
        }

        return ((DatastoreHasGet<ITEM>) datastore).get(id);
    }

    public ItemSearchResult<ITEM> runSearch(final int page, final int resultsByPage, final String search, final String orders,
            final Map<String, String> filters, final List<String> deploys, final List<String> counters) {

        // FIXME Activate at end of APIs refactoring
        // if (!(this instanceof APIHasSearch)) {
        // throw new APIMethodNotAllowedException("SEARCH method not allowed.");
        // }

        String realOrders = orders;
        if (orders == null || orders.length() == 0) {
            realOrders = defineDefaultSearchOrder();

            // TODO remove this test and exception while the automated unit test over all APis
            if (realOrders == null) {
                throw new APIException("No default search order defined. Please, override the defineDefaultSearchOrder method in " + this.getClass().toString()
                        + ".");
            }
        }

        final ItemSearchResult<ITEM> searchResult = search(page, resultsByPage, search, realOrders, filters != null ? filters : new HashMap<String, String>());

        for (final ITEM item : searchResult.getResults()) {
            fillDeploys(item, deploys != null ? deploys : new ArrayList<String>());
            fillCounters(item, counters != null ? counters : new ArrayList<String>());
        }

        return searchResult;
    }

    @SuppressWarnings("unchecked")
    public ItemSearchResult<ITEM> search(final int page, final int resultsByPage, final String search, final String orders, final Map<String, String> filters) {

        final Datastore datastore = getDefaultDatastore();

        if (datastore == null || !(datastore instanceof DatastoreHasSearch<?>)) {
            throw new APIMethodNotAllowedException("SEARCH method not allowed.");
        }

        return ((DatastoreHasSearch<ITEM>) datastore).search(page, resultsByPage, search, orders, filters);
    }

    /**
     * Define the default search order.
     */
    public String defineDefaultSearchOrder() {
        return null;
    }

    public void runDelete(final List<APIID> ids) {
        // FIXME Activate at end of APIs refactoring
        // if (!(this instanceof APIHasDelete)) {
        // throw new APIMethodNotAllowedException("DELETE method not allowed.");
        // }

        for (final APIID id : ids) {
            id.setItemDefinition(getItemDefinition());
        }

        delete(ids);
    }

    public void delete(final List<APIID> ids) {

        final Datastore datastore = getDefaultDatastore();

        if (datastore == null || !(datastore instanceof DatastoreHasDelete)) {
            throw new APIMethodNotAllowedException("DELETE method not allowed.");
        }

        ((DatastoreHasDelete) datastore).delete(ids);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // AUTOMATED CRUDS BASED ON INTERFACES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Datastore defineDefaultDatastore() {
        return null;
    }

    public final Datastore getDefaultDatastore() {
        return this.defineDefaultDatastore();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // PARAMETERS TOOLS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final File getUploadedFile(final String attributeName, final String attributeValue) throws IOException {
        if (attributeValue == null || attributeValue.isEmpty()) {
            return null;
        }

        final String tmpIconPath = getCompleteTempFilePath(attributeValue);

        final File file = new File(tmpIconPath);
        if (!file.exists()) {
            throw new APIFileUploadNotFoundException(attributeName, attributeValue);
        }
        return file;
    }

    abstract protected String getCompleteTempFilePath(String path) throws IOException;

    /**
     * Upload the file to the defined directory and rename it to make sure its filename is unique.<br>
     * The original filename will be kept.
     *
     * @param attributeName
     *        The name of the attribute representing the file.
     * @param attributeValue
     *        The value of the attribute representing the file.
     * @param newDirectory
     *        The destination directory path.
     * @return This method return the file in the destination directory.
     */
    protected final File uploadAutoRename(final String attributeName, final String attributeValue, final String newDirectory) {
        try {

            final File destinationDirectory = new File(newDirectory);
            String destinationFilename = getUploadedFile(attributeName, attributeValue).getName();

            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs();
            }
            final String extension = this.getFileExtension(destinationFilename);
            final File destinationFile = File.createTempFile("avatar", extension, destinationDirectory);
            destinationFilename = destinationFile.getName().substring(0, destinationFile.getName().length() - extension.length());

            return upload(attributeName, attributeValue, newDirectory, destinationFilename);

        } catch (final UnauthorizedFolderException e) {
            throw new APIForbiddenException(e.getMessage());
        } catch (final IOException e) {
            throw new APIException(e);
        }
    }

    /**
     * Rename and upload the file to the defined directory.
     *
     * @param attributeName
     *        The name of the attribute representing the file.
     * @param attributeValue
     *        The value of the attribute representing the file.
     * @param newDirectory
     *        The destination directory path.
     * @param newName
     *        The name to set to the file without the extension (the original extension will be kept)
     * @return This method return the file in the destination directory.
     * @throws IOException
     */
    protected final File upload(final String attributeName, final String attributeValue, final String newDirectory, final String newName) throws IOException {

        // Check if the destination directory already exists. If not, creates it.
        final File destinationDirectory = new File(newDirectory);
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir();
        }

        // Construct the destination fileName
        final File file = getUploadedFile(attributeName, attributeValue);
        String destinationName = file.getName();
        if (newName != null) {
            destinationName = newName + getFileExtension(file.getName());
        }

        // Move the file
        final File destinationFile = new File(destinationDirectory.getAbsolutePath() + File.separator + destinationName);
        try {
            destinationFile.delete();
            Files.move(file.toPath(), destinationFile.toPath());
        } catch (final Exception e) {
            e.getMessage();
        }

        return destinationFile;
    }

    /**
     * @param fileName
     * @return
     */
    private String getFileExtension(final String fileName) {
        String extension = "";
        final int dotPos = fileName.lastIndexOf('.');
        if (dotPos >= 0) {
            extension = fileName.substring(dotPos);
        }
        return extension;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS AND COUNTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addDeployer(final Deployer deployer) {
        deployers.put(deployer.getDeployedAttribute(), deployer);
    }

    public Map<String, Deployer> getDeployers() {
        return Collections.unmodifiableMap(deployers);
    }

    protected void fillDeploys(final ITEM item, final List<String> deploys) {
        for (final String attribute : deploys) {
            deployAttribute(attribute, item);
        }
    }

    private void deployAttribute(final String attribute, final ITEM item) {
        if (deployers.containsKey(attribute)) {
            try {
                deployers.get(attribute).deployIn(item);
            } catch (final Exception e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, getFailedDeployMessage(attribute, item), e);
                } else if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO, getFailedDeployMessage(attribute, item));
                }
            }
        }
    }

    protected String getFailedDeployMessage(final String attribute, final ITEM item) {
        return "Could not deploy attribute '" + attribute + "' on item " + item.toString();
    }

    protected void fillCounters(final ITEM item, final List<String> counters) {
        // Do Nothing if not override
    }

    /**
     * @param attributeName
     * @param deploys
     * @param item
     */
    protected final boolean isDeployable(final String attributeName, final List<String> deploys, final IItem item) {
        final String attributeValue = item.getAttributeValue(attributeName);

        return deploys.contains(attributeName) && attributeValue != null && APIID.makeAPIID(attributeValue) != null;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UPLOADS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private List<String> getFileAttributes() {
        final List<String> results = new ArrayList<>();

        for (final ItemAttribute attribute : getItemDefinition().getAttributes()) {
            if (attribute.getType().equals(ItemAttribute.TYPE.IMAGE) || attribute.getType().equals(ItemAttribute.TYPE.FILE)) {
                results.add(attribute.getName());
            }
        }

        return results;
    }

    /**
     * Upload and replace a file in an item<br />
     * The resulted path will be "subFolder/filename.ext"
     *
     * @param attributeName
     *        The name of the attribute that contains a file to upload
     * @param item
     *        The item containing the attribute to upload
     * @param targetPath
     *        The path of the directory where the uploaded file will be stored
     * @param prefix
     *        The specific folder under targetFolderPath
     */
    protected final void uploadForAdd(final String attributeName, final IItem item, final String targetPath, final String prefix) {

        if (item.getAttributeValue(attributeName) == null || item.getAttributeValue(attributeName).isEmpty()) {
            return;
        }

        final String filename = uploadAutoRename(attributeName, item.getAttributeValue(attributeName), targetPath).getName();

        item.setAttribute(attributeName, new FilePathBuilder(prefix).append(filename).toString());
    }

    /**
     * @param attributeName
     * @param item
     * @param targetPath
     * @param prefix
     */
    private void deleteFile(final String attributeName, final IItem item, final String targetPath, final String prefix) {
        if (item == null || item.getAttributeValue(attributeName) == null || item.getAttributeValue(attributeName).isEmpty()) {
            return;
        }

        final String filePath = item.getAttributeValue(attributeName);
        filePath.substring(prefix.length());

        if (filePath != null && !filePath.isEmpty()) {
            new File(new FilePathBuilder(targetPath).append(filePath).toString()).delete();
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FORBIDDEN ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Override this method to define attributes that are not allowed to be set manually during ADD or UPDATE.
     *
     * @return This method must returns a List of forbidden attributes' name.
     */
    protected List<String> defineReadOnlyAttributes() {
        return null;
    }

    private void checkForbiddenAttributes(final Map<String, String> attributes) {

        // List forbidden attributes
        final List<String> forbiddenAttributes = new ArrayList<>();
        final List<String> definedForbiddenAttributes = defineReadOnlyAttributes();
        if (definedForbiddenAttributes != null) {
            forbiddenAttributes.addAll(definedForbiddenAttributes);
        }
        forbiddenAttributes.addAll(getForbiddenAttributesByInterfaces());

        // No forbidden attributes defined, no need to go further in the check process.
        if (forbiddenAttributes == null || forbiddenAttributes.size() == 0) {
            return;
        }

        // List forbidden attributes found in the request
        final List<String> errorAttributes = new ArrayList<>();
        for (final String forbiddenAttribute : forbiddenAttributes) {
            if (!MapUtil.isBlank(attributes, forbiddenAttribute)) {
                errorAttributes.add(forbiddenAttribute);
            }
        }

        // If at least one is found, throw a ForbiddenAttributesException
        if (errorAttributes.size() > 0) {
            throw new ForbiddenAttributesException(errorAttributes);
        }
    }

    /**
     * @return
     */
    private List<String> getForbiddenAttributesByInterfaces() {
        final List<String> forbiddenAttributes = new ArrayList<>();

        @SuppressWarnings("unchecked")
        // final T modelItem = (T) getItemDefinition().createItem();
        final ITEM modelItem = (ITEM) new DummyItem();

        if (modelItem instanceof ItemHasLastUpdateDate) {
            forbiddenAttributes.add(ItemHasLastUpdateDate.ATTRIBUTE_LAST_UPDATE_DATE);
        }

        if (modelItem instanceof ItemHasLastUpdater) {
            forbiddenAttributes.add(ItemHasLastUpdater.ATTRIBUTE_LAST_UPDATE_DATE);
            forbiddenAttributes.add(ItemHasLastUpdater.ATTRIBUTE_LAST_UPDATE_USER_ID);
        }

        if (modelItem instanceof ItemHasCreator) {
            forbiddenAttributes.add(ItemHasCreator.ATTRIBUTE_CREATED_BY_USER_ID);
            forbiddenAttributes.add(ItemHasCreator.ATTRIBUTE_CREATION_DATE);
        }

        if (modelItem instanceof ItemHasUniqueId) {
            forbiddenAttributes.add(ItemHasUniqueId.ATTRIBUTE_ID);
        }

        return forbiddenAttributes;
    }

}
