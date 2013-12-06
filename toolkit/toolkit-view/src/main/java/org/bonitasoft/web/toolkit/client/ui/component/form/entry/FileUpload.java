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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.ui.JsId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

/**
 * @author Julien Mege
 */
public class FileUpload extends Input {

    private final static String OPTION_URL = "url";

    private final static String OPTION_TEXT_FILE_PICKER = "text.filepicker";

    private final static String OPTION_TEXT_EXTENSION_ERROR = "text.extensionerror";

    private final static String OPTION_FILTERS = "filters";

    private String submitionUrl = null;

    private List<UploadFilter> filters = new ArrayList<UploadFilter>();

    public FileUpload(final String submitionUrl, final JsId jsid, final String label, final String tooltip, final String description, final String example) {
        super(jsid, label, tooltip, null, description, example);
        this.submitionUrl = submitionUrl;

        addClass("fileupload");
    }

    public FileUpload(final String submitionUrl, final JsId jsid, final String label, final String tooltip, final String description) {
        this(submitionUrl, jsid, label, tooltip, description, null);
    }

    public FileUpload(final String submitionUrl, final JsId jsid, final String label, final String tooltip) {
        this(submitionUrl, jsid, label, tooltip, null, null);
    }

    @Override
    protected String getInputType() {
        return "file";
    }

    @Override
    protected Element makeInput(final String uid) {

        final Element input = DOM.createElement("input");
        input.setAttribute("type", getInputType());
        input.setAttribute("name", getJsId().toString());
        input.setAttribute("title", this.tooltip);
        input.setAttribute("rel", createRelOptions());
        input.setId(uid);
        return input;

    }

    public void addFilter(final UploadFilter filter) {
        filters.add(filter);
    }

    public List<UploadFilter> getFilters() {
        return filters;
    }

    private String createRelOptions() {
        Map<String, Object> relOptions = new HashMap<String, Object>();
        relOptions.put(OPTION_URL, this.submitionUrl);
        relOptions.put(OPTION_TEXT_FILE_PICKER, buildPickerLabel());
        relOptions.put(OPTION_TEXT_EXTENSION_ERROR, _("Unsupported file."));
        relOptions.put(OPTION_FILTERS, filters);
        return JSonSerializer.serialize(relOptions);
    }

    private String buildPickerLabel() {
        if (filters.isEmpty()) {
            return _("Click here to choose your file.");
        } else {
            return _("Click here to choose your file. ") + labelizeFilters(getFilters());
        }
    }

    private String labelizeFilters(final List<UploadFilter> filters) {
        StringBuilder builder = new StringBuilder();
        for (UploadFilter filter : filters) {
            builder.append(filter);
            builder.append(" ");
        }
        return builder.toString();
    }

    @Override
    public void _setValue(final String value) {
        // Do nothing
    }

    public void reset() {
        super._setValue(null);
    }

}
