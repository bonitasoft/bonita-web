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
package org.bonitasoft.web.rest.model.portal.profile;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.Item;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.template.ItemHasUniqueId;

/**
 * @author Julien Mege
 * @author Séverin Moussel
 */
public class ProfileEntryItem extends Item implements ItemHasUniqueId {

    public ProfileEntryItem() {
        super();
    }

    public ProfileEntryItem(final IItem item) {
        super(item);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ATTRIBUTES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final String ATTRIBUTE_PARENT_ID = "parent_id";

    public static final String ATTRIBUTE_PROFILE_ID = "profile_id";

    public static final String ATTRIBUTE_INDEX = "index";

    public static final String ATTRIBUTE_NAME = "name";

    public static final String ATTRIBUTE_DESCRIPTION = "description";

    public static final String ATTRIBUTE_TYPE = "type";

    public static final String ATTRIBUTE_PAGE = "page";

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALUES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static enum VALUE_TYPE {
        link, folder
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GETTERS AND SETTERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setId(final String id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    @Override
    public void setId(final Long id) {
        setAttribute(ATTRIBUTE_ID, id);
    }

    public void setParentId(final APIID id) {
        setAttribute(ATTRIBUTE_PARENT_ID, id);
    }

    public void setParentId(final String id) {
        setAttribute(ATTRIBUTE_PARENT_ID, id);
    }

    public void setParentId(final Long id) {
        setAttribute(ATTRIBUTE_PARENT_ID, id);
    }

    public APIID getParentId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_PARENT_ID);
    }

    public void setProfileId(final APIID id) {
        setAttribute(ATTRIBUTE_PROFILE_ID, id);
    }

    public void setProfileId(final Long id) {
        setAttribute(ATTRIBUTE_PROFILE_ID, id);
    }

    public void setProfileId(final String id) {
        setAttribute(ATTRIBUTE_PROFILE_ID, id);
    }

    public APIID getProfileId() {
        return getAttributeValueAsAPIID(ATTRIBUTE_PROFILE_ID);
    }

    public void setIndex(final long index) {
        setAttribute(ATTRIBUTE_INDEX, index);
    }

    public Integer getIndex() {
        return StringUtil.toInteger(getAttributeValue(ATTRIBUTE_INDEX));
    }

    public void setName(final String name) {
        setAttribute(ATTRIBUTE_NAME, name);
    }

    public String getName() {
        return getAttributeValue(ATTRIBUTE_NAME);
    }

    public void setDescription(final String description) {
        setAttribute(ATTRIBUTE_DESCRIPTION, description);
    }

    public String getDescription() {
        return getAttributeValue(ATTRIBUTE_DESCRIPTION);
    }

    public void setType(final VALUE_TYPE type) {
        setAttribute(ATTRIBUTE_TYPE, type.name());
    }

    public void setType(final String type) {
        setAttribute(ATTRIBUTE_TYPE, VALUE_TYPE.valueOf(type));
    }

    public VALUE_TYPE getType() {
        final String type = getAttributeValue(ATTRIBUTE_TYPE);
        try {
            return VALUE_TYPE.valueOf(type);
        } catch (final Exception e) {
            return null;
        }
    }

    public void setPage(final String linkOrToken) {
        setAttribute(ATTRIBUTE_PAGE, linkOrToken);
    }

    public String getPage() {
        return getAttributeValue(ATTRIBUTE_PAGE);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEPLOYS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ProfileEntryItem getParentEntry() {
        return (ProfileEntryItem) ProfileEntryDefinition.get().createItem(getDeploy(ATTRIBUTE_PARENT_ID));
    }

    public ProfileItem getProfile() {
        return (ProfileItem) ProfileDefinition.get().createItem(getDeploy(ATTRIBUTE_PROFILE_ID));
    }

    public BonitaPageItem getBonitaPage() {
        return new BonitaPageItem(getDeploy(ATTRIBUTE_PAGE));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // UTILS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public ItemDefinition<ProfileEntryItem> getItemDefinition() {
        return ProfileEntryDefinition.get();
    }

    public boolean isFolder() {
        return VALUE_TYPE.folder.name().equals(getType());
    }

    public boolean isLink() {
        return VALUE_TYPE.link.name().equals(getType());
    }

    public boolean isParentProfileEntry() {
        return getParentId().toLong() == 0L;
    }
}
