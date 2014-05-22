package org.bonitasoft.console.client.mvp;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
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
