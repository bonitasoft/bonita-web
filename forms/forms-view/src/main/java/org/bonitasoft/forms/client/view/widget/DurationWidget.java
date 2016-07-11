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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.view.widget;

import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

/**
 * Widget displaying a group of selectbox allowing to define a time period
 * 
 * @author Anthony Birembaut
 */
public class DurationWidget extends Composite implements HasChangeHandlers, ChangeHandler {

    public static final int SECONDS_IN_A_DAY = 86400;
    
    public static final int SECONDS_IN_AN_HOUR = 3600;
    
    public static final int SECONDS_IN_A_MINUTE = 60;
    
    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;
    
    protected ListBox daysListBox;
    
    protected ListBox hoursListBox;
    
    protected ListBox minutesListBox;
    
    protected ListBox secondsListBox;
    
    protected List<ChangeHandler> changeHandlers;
    
    /**
     * Constructor
     * @param format
     * @param initialValue
     * @param itemsStyle
     */
    public DurationWidget(String format, final Long initialValue, final String itemsStyle) {
        
        flowPanel = new FlowPanel();
        
        Long duration = 0L;
        if (initialValue != null) {
            duration = initialValue / 1000;
        }
        final int days = duration.intValue() / SECONDS_IN_A_DAY;
        int remaining = duration.intValue() % SECONDS_IN_A_DAY;
        final int hours = remaining / SECONDS_IN_AN_HOUR;
        remaining = remaining % SECONDS_IN_AN_HOUR;
        final int minutes = remaining / SECONDS_IN_A_MINUTE;
        final int seconds = remaining % SECONDS_IN_A_MINUTE;
        
        daysListBox = new ListBox(false);
        daysListBox.addChangeHandler(this);
        hoursListBox = new ListBox(false);
        hoursListBox.addChangeHandler(this);
        minutesListBox = new ListBox(false);
        minutesListBox.addChangeHandler(this);
        secondsListBox = new ListBox(false);
        secondsListBox.addChangeHandler(this);
        for(int i = 0; i <= 999; i++) {
            final String str = Integer.toString(i);
            daysListBox.addItem(str, str);
        }
        daysListBox.setSelectedIndex(days);
        for(int i = 0; i <= 23; i++) {
            final String str = Integer.toString(i);
            hoursListBox.addItem(str, str);
        }
        hoursListBox.setSelectedIndex(hours);
        for(int i = 0; i <= 59; i++) {
            final String str = Integer.toString(i);
            minutesListBox.addItem(str, str);
        }
        minutesListBox.setSelectedIndex(minutes);
        for(int i = 0; i <= 59; i++) {
            final String str = Integer.toString(i);
            secondsListBox.addItem(str, str);
        }
        secondsListBox.setSelectedIndex(seconds);
        if (format == null || format.length() == 0) {
            format = "dhms";
        }
        if (format.contains("d")){
            flowPanel.add(addItem(FormsResourceBundle.getMessages().daysLabel(), daysListBox, itemsStyle));
        }
        if (format.contains("h")){
            flowPanel.add(addItem(FormsResourceBundle.getMessages().hoursLabel(), hoursListBox, itemsStyle));
        }
        if (format.contains("m")){
            flowPanel.add(addItem(FormsResourceBundle.getMessages().minutesLabel(), minutesListBox, itemsStyle));
        }
        if (format.contains("s")){
            flowPanel.add(addItem(FormsResourceBundle.getMessages().secondsLabel(), secondsListBox, itemsStyle));
        }
        initWidget(flowPanel);
    }
    
    protected FlowPanel addItem(final String listBoxLabel, final ListBox listBox, final String itemsStyle) {
        final FlowPanel itemFlowPanel = new FlowPanel();
        listBox.setStyleName("bonita_datetime_list_item");
        itemFlowPanel.add(listBox);
        final Label label = new Label(listBoxLabel);
        label.setStyleName("bonita_datetime_label_item");
        itemFlowPanel.add(label);
        itemFlowPanel.setStyleName("bonita_datetime_item");
        if (itemsStyle != null && itemsStyle.length() > 0) {
            itemFlowPanel.addStyleName(itemsStyle);
        }
        return itemFlowPanel;
    }
    
    /**
     * @return the time period as a string
     */
    public Long getValue(){

        final int days = Integer.parseInt(daysListBox.getValue(daysListBox.getSelectedIndex()));
        final int hours = Integer.parseInt(hoursListBox.getValue(hoursListBox.getSelectedIndex()));
        final int minutes = Integer.parseInt(minutesListBox.getValue(minutesListBox.getSelectedIndex()));
        final int seconds = Integer.parseInt(secondsListBox.getValue(secondsListBox.getSelectedIndex()));
        return (days * SECONDS_IN_A_DAY + hours * SECONDS_IN_AN_HOUR + minutes * SECONDS_IN_A_MINUTE + seconds) * 1000L;
    }
    
    /**
     * Set the widget value
     * @param value duration in milis
     * @param b 
     */
    public void setValue(Long value, final boolean fireEvents) {
        if (value == null) {
            value = 0L;
        }
        final Long duration = value/1000;
        final int days = duration.intValue() / SECONDS_IN_A_DAY;
        int remaining = duration.intValue() % SECONDS_IN_A_DAY;
        final int hours = remaining / SECONDS_IN_AN_HOUR;
        remaining = remaining % SECONDS_IN_AN_HOUR;
        final int minutes = remaining / SECONDS_IN_A_MINUTE;
        final int seconds = remaining % SECONDS_IN_A_MINUTE;
        
        boolean valueChanged = false;
        if (daysListBox.getSelectedIndex() != days) {
            valueChanged = true;
        }
        daysListBox.setSelectedIndex(days);
        if (hoursListBox.getSelectedIndex() != hours) {
            valueChanged = true;
        }
        hoursListBox.setSelectedIndex(hours);
        if (minutesListBox.getSelectedIndex() != minutes) {
            valueChanged = true;
        }
        minutesListBox.setSelectedIndex(minutes);
        if (secondsListBox.getSelectedIndex() != seconds) {
            valueChanged = true;
        }
        secondsListBox.setSelectedIndex(seconds);
        
        if (fireEvents && valueChanged) {
            DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
        }
    }
    
    /**
     * Enable or disable the listboxes
     * @param isEnabled
     */
    public void setEnabled(final boolean isEnabled) {
        daysListBox.setEnabled(isEnabled);
        hoursListBox.setEnabled(isEnabled);
        minutesListBox.setEnabled(isEnabled);
        secondsListBox.setEnabled(isEnabled);
    }

    /**
     * {@inheritDoc}
     */
    public HandlerRegistration addChangeHandler(final ChangeHandler changeHandler) {
        if (changeHandlers == null) {
            changeHandlers = new ArrayList<ChangeHandler>();
        }
        changeHandlers.add(changeHandler);
        return new EventHandlerRegistration(changeHandler);
    }

    /**
     * {@inheritDoc}
     */
    public void onChange(final ChangeEvent changeEvent) {
        for (final ChangeHandler changeHandler : changeHandlers) {
            changeHandler.onChange(changeEvent);
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
        
        public void removeHandler() {
            if (eventHandler instanceof ChangeHandler) {
                changeHandlers.remove(eventHandler);
            }
        }
    }
}
