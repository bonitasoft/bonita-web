/**
 * Copyright (C) 2015 BonitaSoft S.A.
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
package org.bonitasoft.console.common.server.form;

import java.io.Serializable;

import org.bonitasoft.engine.form.FormMappingTarget;


public class FormReference implements Serializable {

    /**
     * UID
     */
    private static final long serialVersionUID = -1692145871057019847L;

    private String form;

    private FormMappingTarget target;

    public FormReference() {
    }

    public FormReference(final String form, final FormMappingTarget target) {
        this.form = form;
        this.target = target;
    }


    public String getForm() {
        return form;
    }


    public void setForm(final String form) {
        this.form = form;
    }


    public FormMappingTarget getTarget() {
        return target;
    }


    public void setTarget(final FormMappingTarget target) {
        this.target = target;
    }



}
