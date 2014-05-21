package org.bonitasoft.console.client.mvp.event;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Vincent Elcrin
 */
public class DirtyInputEvent<T> extends GwtEvent<DirtyInputHandler<T>> {

    private Type<DirtyInputHandler<T>> type;
    private final NativeEvent event;

    private final Cell.Context context;

    private final InputElement input;

    private T item;

    public DirtyInputEvent(Type<DirtyInputHandler<T>> type, NativeEvent event, Cell.Context context, InputElement input, T item) {
        this.type = type;
        this.event = event;
        this.context = context;
        this.input = input;
        this.item = item;
    }

    @Override
    public Type<DirtyInputHandler<T>> getAssociatedType() {
        return type;
    }

    @Override
    protected void dispatch(DirtyInputHandler<T> handler) {
        handler.onDirtyInput(this);
    }

    public NativeEvent getEvent() {
        return event;
    }

    public Cell.Context getContext() {
        return context;
    }

    public InputElement getInput() {
        return input;
    }

    public T getItem() {
        return item;
    }
}
