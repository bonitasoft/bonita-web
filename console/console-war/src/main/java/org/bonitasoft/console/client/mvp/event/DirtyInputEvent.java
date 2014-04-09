package org.bonitasoft.console.client.mvp.event;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author Vincent Elcrin
 */
public class DirtyInputEvent extends GwtEvent<DirtyInputHandler> {

    private final NativeEvent event;

    private final Cell.Context context;

    private final InputElement input;

    public static final Type<DirtyInputHandler> TYPE = new Type<DirtyInputHandler>();

    public DirtyInputEvent(NativeEvent event, Cell.Context context, InputElement input) {
        this.event = event;
        this.context = context;
        this.input = input;
    }

    @Override
    public Type<DirtyInputHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DirtyInputHandler handler) {
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
}
