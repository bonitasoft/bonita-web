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
package org.bonitasoft.web.toolkit.client.data.item.attribute.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.web.toolkit.client.common.texttemplate.TextTemplate;
import org.bonitasoft.web.toolkit.client.data.item.IItem;

/**
 * @author Séverin Moussel
 * 
 */
public class CompoundAttributeReader extends AbstractAttributeReader implements HasReaders {

    private final Map<String, AbstractAttributeReader> readers = new HashMap<String, AbstractAttributeReader>();

    private String template;

    public CompoundAttributeReader(final String leadAttribute, final String template) {
        super(leadAttribute);
        this.template = template;
    }

    public CompoundAttributeReader(final String template) {
        this(null, template);
    }

    protected void setTemplate(final String template) {
        this.template = template;
    }

    public CompoundAttributeReader addReader(final String name, final AbstractAttributeReader reader) {
        this.readers.put(name, reader);
        return this;
    }

    public CompoundAttributeReader addReader(final String name, final String attributeName) {
        return addReader(name, new AttributeReader(attributeName));
    }

    @Override
    protected String _read(final IItem item) {
        final TextTemplate textTemplate = new TextTemplate(this.template);
        if (this.readers.size() == 0) {
            addAutoAttributeReader(textTemplate);
        }
        final Map<String, String> args = getTemplateArgs(item);
        return fillTemplate(textTemplate, args);
    }

    private String fillTemplate(final TextTemplate textTemplate, final Map<String, String> args) {
        return !args.isEmpty() ? textTemplate.toString(args) : null;
    }

    private Map<String, String> getTemplateArgs(final IItem item) {
        final Map<String, String> args = new HashMap<String, String>();
        for (final Entry<String, AbstractAttributeReader> reader : this.readers.entrySet()) {
            final String read = reader.getValue().read(item);
            if (read != null) {
                args.put(reader.getKey(), read);
            }
        }
        return args;
    }

    private void addAutoAttributeReader(final TextTemplate textTemplate) {
        for (final String attributeName : textTemplate.getExpectedParameters()) {
            addReader(attributeName, attributeName);
        }
    }

    public List<String> getAttributes() {
        return new TextTemplate(this.template).getExpectedParameters();
    }

    /*
     * (non-Javadoc)
     * @see org.bonitasoft.web.toolkit.client.data.item.attribute.reader.HasReaders#getAttributeReader()
     */
    @Override
    public Map<String, AbstractAttributeReader> getAttributeReaders() {
        return this.readers;
    }
}
