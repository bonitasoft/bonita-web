/**
 * Copyright (C) 2014 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.data.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.query.client.js.JsMap;

/**
 * 
 * @author Baptiste Mesta
 *
 */
public class OverlayImpl extends JavaScriptObject implements Overlay {

    protected OverlayImpl() {
    }

    @Override
    public final native String get(String key) /*-{
        return "" + this[key];
    }-*/;

    public final List<? extends Overlay> getList(String key){
        JsArray<OverlayImpl> nestedOverlay = (JsArray<OverlayImpl>) getNestedOverlay(key);
        
        return toList(nestedOverlay);
    }

    /**
     * @param nestedOverlay
     * @return
     */
    private List<? extends Overlay> toList(JsArray<OverlayImpl> nestedOverlay) {
        ArrayList<Overlay> arrayList = new ArrayList<Overlay>(nestedOverlay.length());
        for (int i = 0; i < nestedOverlay.length(); i++) {
            arrayList.add(nestedOverlay.get(i));
        }
        return arrayList;
    }

    @Override
    public final native JavaScriptObject getNestedOverlay(String key) /*-{
        return this[key];
    }-*/;

    @Override
    public final native <O extends Object> void set(String key, O value) /*-{
        this[key] = value;
    }-*/;

    @Override
    public final String[] keys() {
        return asMap().keys();
    }

    public final native JsMap<String, String> asMap() /*-{
        return this;
    }-*/;

}