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
package org.bonitasoft.console.client.mvp;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import org.bonitasoft.console.client.mvp.event.DirtyInputEvent;
import org.bonitasoft.console.client.mvp.event.DirtyInputHandler;
import org.bonitasoft.web.toolkit.client.eventbus.MainEventBus;

/**
 * @author Vincent Elcrin
 */
public abstract class TemplateRepeat<T> extends AbstractCell<T> {

    private String style;

    private GwtEvent.Type<DirtyInputHandler<T>> dirtyInputEventType = new GwtEvent.Type<DirtyInputHandler<T>>();

    public TemplateRepeat(String style) {
        super(BrowserEvents.CLICK, BrowserEvents.KEYUP);
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public void render(Context context, T value, SafeHtmlBuilder sb) {
        if (value == null) {
            return;
        }
        sb.append(render(context, value));
    }

    public abstract SafeHtml render(Cell.Context context, T value);

    @Override
    public void onBrowserEvent(Context context, Element parent, T item, NativeEvent event, ValueUpdater<T> valueUpdater) {
        Element element = Element.as(event.getEventTarget());
        if (element.getTagName().equalsIgnoreCase(InputElement.TAG)) {
            final InputElement input = InputElement.as(element);
            notify(context, event, input, item);
        }
        super.onBrowserEvent(context, parent, item, event, valueUpdater);
    }

    @Override
    public boolean isEditing(Context context, Element parent, T value) {
        return true;
    }

    public void listen(DirtyInputHandler<T> handler) {
        MainEventBus.getInstance().addHandler(dirtyInputEventType, handler);
    }

    private void notify(Context context, NativeEvent event, InputElement input, T item) {
        MainEventBus.getInstance().fireEventFromSource(new DirtyInputEvent<T>(dirtyInputEventType, event, context, input, item), this);
    }

    @Override
    public boolean resetFocus(Context context, Element parent, T value) {
        final NodeList<Element> elements = parent.getElementsByTagName("input");
        if (elements.getLength() > 0) {
            InputElement.as(elements.getItem(0)).focus();
            return true;
        }
        return false;
    }
}
