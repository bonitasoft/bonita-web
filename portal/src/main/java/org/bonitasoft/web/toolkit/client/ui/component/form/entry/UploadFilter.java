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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.util.MapUtil;
import org.bonitasoft.web.toolkit.client.ui.utils.ListUtils;

/**
 * @author Vincent Elcrin
 * 
 */
public class UploadFilter implements JsonSerializable {

    private final static String TITLE_KEY = "title";

    private final static String EXTENSIONS_KEY = "extensions";

    private final static String EXTENSIONS_JSON_SEPARATOR = ",";

    private final static String PICKER_LABEL_SEPARATOR = ", ";

    private final static String PICKER_LABEL_LAST_SEPARATOR = PICKER_LABEL_SEPARATOR;

    private final static String PICKER_LABEL_EXT_SUFFIX = ".";

    private final static String PICKER_LABEL_EXT_PREFFIX = "";

    private String title;

    private List<String> extensions;

    public UploadFilter(final String title, final String... extensions) {
        this.title = title;
        this.extensions = new ArrayList<String>(Arrays.asList(extensions));
    }

    public String getTitle() {
        return title;
    }

    public List<String> getExtensions() {
        return Collections.unmodifiableList(extensions);
    }

    public UploadFilter addExtension(String extension) {
        extensions.add(extension);
        return this;
    }

    @Override
    public String toJson() {
        return JSonSerializer.serializeMap(createMapping());
    }

    private Map<String, String> createMapping() {
        return MapUtil.asMap(
                new Arg(TITLE_KEY, getTitle()),
                new Arg(EXTENSIONS_KEY, ListUtils.join(getExtensions(), EXTENSIONS_JSON_SEPARATOR)));
    }

    @Override
    public String toString() {
        return getTitle() + " ("
                + ListUtils.join(getExtensions(), PICKER_LABEL_SEPARATOR, PICKER_LABEL_LAST_SEPARATOR, PICKER_LABEL_EXT_SUFFIX, PICKER_LABEL_EXT_PREFFIX) + ")";
    }
}
