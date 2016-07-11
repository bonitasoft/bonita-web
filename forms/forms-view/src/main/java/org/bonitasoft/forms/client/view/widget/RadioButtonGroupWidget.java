/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.forms.client.view.widget;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.view.common.DOMUtils;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget displaying a group of radio buttons (only one radio button can be selected inside a group)
 *
 * @author Anthony Birembaut
 */
public class RadioButtonGroupWidget extends Composite implements HasClickHandlers, ClickHandler, HasValueChangeHandlers<Boolean>, ValueChangeHandler<Boolean> {

    protected static int radioButtonGroupIndex = 0;

    /**
     * Css class added to groupWidgets panel
     */
    private static final String GROUP_WIDGETS = "group-widgets";

    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel inputDiv;

    /**
     * flow panel used to contain all the widget checkboxes
     */
    protected FlowPanel groupWidgets;

    /**
     * The set of radio buttons in the group
     */
    protected Set<RadioButton> radioButtons = new HashSet<RadioButton>();

    /**
     * items style
     */
    protected String itemsStyle;

    /**
     * The radio buttons group Id
     */
    protected String radioButtonGroupName;

    /**
     * click handlers registered for the widget
     */
    protected List<ClickHandler> clickHandlers;

    /**
     * value change handlers registered for the widget
     */
    protected List<ValueChangeHandler<Boolean>> valueChangeHandlers;

    /**
     * indicates if HTML is allowed as value of the widget
     */
    protected boolean allowHTML;

    /**
     * indicates if the widget instance is a child of a multiple element
     */
    protected boolean isElementOfMultipleWidget;

    /**
     * Constructor
     *
     * @param radioButtonGroupId
     *            Id of the radio button group
     * @param availableValues
     *            available values of the group
     * @param initialValue
     *            initial value
     * @param itemsStyle
     *            the css classes of each radio button
     * @param allowHTML
     *            allow HTML in the radio buttons labels
     *
     */
    public RadioButtonGroupWidget(final String radioButtonGroupId, final List<ReducedFormFieldAvailableValue> availableValues, final String initialValue,
            final String itemsStyle, final boolean allowHTML) {
        this(radioButtonGroupId, availableValues, initialValue, itemsStyle, allowHTML, false);
    }

    /**
     * Constructor
     *
     * @param radioButtonGroupId
     *            Id of the radio button group
     * @param availableValues
     *            available values of the group
     * @param initialValue
     *            initial value
     * @param itemsStyle
     *            the css classes of each radio button
     * @param allowHTML
     *            allow HTML in the radio buttons labels
     * @param isElementOfMultipleWidget
     *            indicates if the widget instance is a child of a multiple element
     *
     */
    public RadioButtonGroupWidget(final String radioButtonGroupId, final List<ReducedFormFieldAvailableValue> availableValues, final String initialValue,
            final String itemsStyle, final boolean allowHTML, final boolean isElementOfMultipleWidget) {

        this.itemsStyle = itemsStyle;
        this.allowHTML = allowHTML;
        this.isElementOfMultipleWidget = isElementOfMultipleWidget;
        inputDiv = new FlowPanel();
        groupWidgets = new FlowPanel();
        groupWidgets.addStyleName(GROUP_WIDGETS);
        radioButtonGroupName = getRadioButtonGroupName(radioButtonGroupId);

        for (final ReducedFormFieldAvailableValue availableValue : availableValues) {
            final RadioButton radioButton = createRadioButton(radioButtonGroupName, availableValue, allowHTML);
            addItemsStyle(radioButton, itemsStyle);
            setInitialValue(radioButton, initialValue, availableValue);
            saveRadioButton(radioButton);
            groupWidgets.add(radioButton);
        }

        inputDiv.add(groupWidgets);
        initWidget(inputDiv);
    }

    private RadioButton createRadioButton(final String groupName,
            final ReducedFormFieldAvailableValue availableValue,
            final boolean allowHTML) {
        final RadioButton radioButton = new RadioButton(radioButtonGroupName, availableValue.getLabel(), allowHTML);
        radioButton.addClickHandler(this);
        radioButton.addValueChangeHandler(this);
        radioButton.setFormValue(availableValue.getValue());
        radioButton.setStyleName("bonita_form_radio");
        return radioButton;
    }

    private void addItemsStyle(final RadioButton radioButton, final String itemsStyle) {
        if (itemsStyle != null && itemsStyle.length() > 0) {
            radioButton.addStyleName(itemsStyle);
        }
    }

    private void setInitialValue(final RadioButton radioButton, final String initialValue, final ReducedFormFieldAvailableValue availableValue) {
        if (initialValue != null && initialValue.equals(availableValue.getValue())) {
            radioButton.setValue(true);
        }
    }

    /**
     * Useful in case the groups of radio buttons is displayed in several widget group instances
     *
     * @param radioButtonGroupId
     *            the radio button definition Id
     * @return a name that can be used for the radio button group instance
     */
    protected String getRadioButtonGroupName(final String radioButtonGroupId) {

        String radioButtonGroupName = null;
        if (isElementOfMultipleWidget) {
            if (radioButtonGroupIndex == 0) {
                radioButtonGroupName = radioButtonGroupId;
            } else {
                radioButtonGroupName = radioButtonGroupId + Integer.toString(radioButtonGroupIndex);
            }
            radioButtonGroupIndex++;
        } else {
            radioButtonGroupName = radioButtonGroupId;
        }
        return radioButtonGroupName;
    }

    /**
     * @return the String value of the slected radio button of the group (null if no radio button is selected)
     */
    public String getValue() {

        String value = null;

        final Iterator<Widget> iterator = groupWidgets.iterator();
        while (iterator.hasNext()) {
            final RadioButton radioButton = (RadioButton) iterator.next();
            if (radioButton.getValue()) {
                value = radioButton.getFormValue();
                break;
            }
        }

        return value;
    }

    /**
     * Set the value of the widget
     *
     * @param value
     * @param fireEvents
     */
    public void setValue(final String value, final boolean fireEvents) {
        final String currentValue = getValue();
        if (!(currentValue == null && value == null || currentValue != null && currentValue.equals(value))) {
            //don't do anything if the value of the widget doesn't change
            for (final RadioButton radioButton : radioButtons) {
                final DOMUtils domUtils = DOMUtils.getInstance();
                final boolean newIsCheckedValue = value != null && value.equals(radioButton.getFormValue());
                radioButton.setValue(newIsCheckedValue, fireEvents);
                domUtils.overrideNativeInputAfterUpdate(radioButton, newIsCheckedValue);
            }
        }
    }

    /**
     * Set the wigdet available values
     *
     * @param availableValues
     */
    public void setAvailableValues(final List<ReducedFormFieldAvailableValue> availableValues, final boolean fireEvents) {
        clearRadioButtons();
        for (final ReducedFormFieldAvailableValue availableValue : availableValues) {
            final RadioButton radioButton = createRadioButton(radioButtonGroupName, availableValue, allowHTML);
            addItemsStyle(radioButton, itemsStyle);
            saveRadioButton(radioButton);
            groupWidgets.add(radioButton);
        }
        if (fireEvents) {
            ValueChangeEvent.fire(this, true);
        }
    }

    private void saveRadioButton(final RadioButton radioButton) {
        radioButtons.add(radioButton);
    }

    private void clearRadioButtons() {
        radioButtons.clear();
        groupWidgets.clear();
    }

    /**
     * Enable or disable the radiobuttons group
     *
     * @param isEnabled
     */
    public void setEnabled(final boolean isEnabled) {
        for (final RadioButton radioButton : radioButtons) {
            radioButton.setEnabled(isEnabled);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addClickHandler(final ClickHandler clickHandler) {
        if (clickHandlers == null) {
            clickHandlers = new ArrayList<ClickHandler>();
        }
        clickHandlers.add(clickHandler);
        return new EventHandlerRegistration(clickHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Boolean> valueChangeHandler) {
        if (valueChangeHandlers == null) {
            valueChangeHandlers = new ArrayList<ValueChangeHandler<Boolean>>();
        }
        valueChangeHandlers.add(valueChangeHandler);
        return new EventHandlerRegistration(valueChangeHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClick(final ClickEvent clickEvent) {
        if (clickHandlers != null) {
            for (final ClickHandler clickHandler : clickHandlers) {
                clickHandler.onClick(clickEvent);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onValueChange(final ValueChangeEvent<Boolean> valueChangeEvent) {
        if (valueChangeHandlers != null) {
            for (final ValueChangeHandler<Boolean> valueChangeHandler : valueChangeHandlers) {
                valueChangeHandler.onValueChange(valueChangeEvent);
            }
        }
    }

    /**
     * Custom Handler registration
     */
    protected class EventHandlerRegistration implements HandlerRegistration {

        protected EventHandler eventHandler;

        public EventHandlerRegistration(final EventHandler eventHandler) {
            this.eventHandler = eventHandler;
        }

        @Override
        public void removeHandler() {
            if (eventHandler instanceof ClickHandler) {
                clickHandlers.remove(eventHandler);
            } else if (eventHandler instanceof ValueChangeHandler<?>) {
                valueChangeHandlers.remove(eventHandler);
            }
        }
    }
}
