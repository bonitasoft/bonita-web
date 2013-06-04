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
package org.bonitasoft.web.toolkit.client.ui.component.table.formatter;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.data.item.attribute.ItemAttribute;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.CountedAttributeReader;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.DateAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.component.Image;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat;

/**
 * @author Séverin Moussel
 * 
 */
public class DefaultItemTableCellFormatter extends ItemTableCellFormatter {

    /**
     * @return
     */
    protected ItemAttribute getItemAttribute() {
        final ItemAttribute attribute = this.item.getItemDefinition().getAttribute(this.attributeReader.getLeadAttribute());
        return attribute;
    }

    @Override
    public void execute() {
        // Empty if no image set

        final String value = this.attributeReader.read(this.item);
        if (value == null || value.length() == 0) {
            this.table.addCell("");
            return;
        }

        if (isFormattable()) {
            switch (getItemAttribute().getType()) {
                case ENUM:
                    makeEnumCell();
                    break;
                case BOOLEAN:
                    makeBooleanCell();
                    break;
                case IMAGE:
                    makeImageCell();
                    break;
                case DATE:
                    makeDateCell();
                    break;
                case DATETIME:
                    makeDateTimeCell();
                    break;
                case STRING:
                case TEXT:
                default:
                    makeStringCell();
                    break;
            }
        } else {
            makeStringCell();
        }
    }

    protected boolean isFormattable() {
        return isItemAttributeExist()
                && this.attributeReader instanceof AttributeReader
                && !(this.attributeReader instanceof CountedAttributeReader)
                && !(this.attributeReader instanceof DateAttributeReader);
    }

    private boolean isItemAttributeExist() {
        return this.item.getItemDefinition().containsAttribute(this.attributeReader.getLeadAttribute());
    }

    /**
     * 
     */
    protected void makeBooleanCell() {

        boolean booleanValue = getBooleanValue();

        this.table.addCell(getBooleanText(booleanValue));
        final String markClass = getItemAttribute().getName().toLowerCase().replaceAll("\\s", "_") + "_" + (booleanValue ? "yes" : "no");
        this.table.getLastLine().addClass(markClass);
        this.table.getLastCell().addClass(markClass);
    }

    protected boolean getBooleanValue() {
        final String value = this.attributeReader.read(this.item).toLowerCase();
        boolean booleanValue = false;

        if (value != null) {
            try {
                final int intValue = Integer.parseInt(value);
                booleanValue = intValue > 0;
            } catch (final NumberFormatException e) {
                booleanValue = "true".equals(value) || "yes".equals(value) || "ok".equals(value);
            }
        }
        return booleanValue;
    }

    /**
     * @param booleanValue
     * @return
     */
    protected String getBooleanText(final boolean booleanValue) {
        return booleanValue ? _("Yes") : _("No");
    }

    /**
     * 
     */
    protected void makeStringCell() {
        this.table.addCell(getStringText());
    }

    /**
     * @return
     */
    protected String getStringText() {
        return this.attributeReader.read(this.item);
    }

    /**
     * 
     */
    protected void makeImageCell() {

        this.table.addCell(new Image(this.attributeReader.read(this.item), 0, 0, ""));
    }

    /**
     * 
     */
    protected void makeEnumCell() {
        final String mark = this.attributeReader.read(this.item);
        this.table.addCell(getEnumText(mark));
        final String markClass = getItemAttribute().getName().toLowerCase().replaceAll("\\s", "_") + "_" + mark.toLowerCase().replaceAll("\\s", "_");
        this.table.getLastLine().addClass(markClass);
        this.table.getLastCell().addClass(markClass);
        this.table.getLastCell().setTooltip(getEnumText(mark));
    }

    /**
     * @param value
     * @return
     */
    protected String getEnumText(final String value) {
        return _(value);
    }

    protected void makeDateCell() {
        this.table.addCell(getDateText());
    }

    /**
     * @return
     */
    protected String getDateText() {
        return DateFormat.sqlToDisplayShort(this.attributeReader.read(this.item));
    }

    protected void makeDateTimeCell() {
        this.table.addCell(getDateTimeText());
    }

    /**
     * @return
     */
    protected String getDateTimeText() {
        return DateFormat.sqlToDisplayRelative(this.attributeReader.read(this.item));
    }
}
