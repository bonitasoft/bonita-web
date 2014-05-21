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
package org.bonitasoft.web.toolkit.client.ui.component.table.formatter;

import org.bonitasoft.web.toolkit.client.ui.component.Image;

/**
 * @author Vincent Elcrin
 * 
 */
public class ImageCellFormater extends ItemTableCellFormatter {

    private final String urlDefaultIcon;

    /**
     * Default Constructor.
     */
    public ImageCellFormater(final String urlDefaultIcon) {
        this.urlDefaultIcon = urlDefaultIcon;
    }

    /*
     * (non-Javadoc)
     * @see ComponentFormatter#execute()
     */
    @Override
    public void execute() {
        final String url = this.attributeReader.read(this.item);

        if (url == null || url.length() == 0) {
            this.table.addCell(new Image(this.urlDefaultIcon, 0, 0, "").addClass("icon-default"));
        } else {
            this.table.addCell(new Image(url, 0, 0, ""));
        }
    }

}
