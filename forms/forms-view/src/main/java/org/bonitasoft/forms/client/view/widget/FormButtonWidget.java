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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.WidgetType;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Generic form flow Button widget
 * 
 * @author Anthony Birembaut
 */
public class FormButtonWidget extends Composite implements HasClickHandlers, ClickHandler {
    
    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;
    
    /**
     * widget field type
     */
    protected WidgetType widgetType;
    
    /**
     * Button widget
     */
    protected Button button;
    
    /**
     * Label Button widget
     */
    protected HTML labelButton;
    
    /**
     * indicates whether the button is a label button or not
     */
    protected boolean isLabelButton;
    
    /**
     * Click handlers registered for the widget
     */
    protected Map<String, ClickHandler> clickHandlers;
    
    /**
     * Constructor
     * 
     * @param formWidget the widget data object
     */
    public FormButtonWidget(final ReducedFormWidget formButtonData) {

        flowPanel = new FlowPanel();
        
        isLabelButton = formButtonData.isLabelButton();
        
        createWidget(formButtonData);
        
        initWidget(flowPanel);
    }
    
    /**
     * Create the button widget
     * 
     * @param widgetData
     *            the widget data object
     */
    protected void createWidget(final ReducedFormWidget formButtonData) {
        
        widgetType = formButtonData.getType();
        
        if (isLabelButton) {
            labelButton = new HTML(formButtonData.getLabel());
            labelButton.setStyleName("bonita_form_buttonlabel");
            flowPanel.addStyleName("bonita_form_buttonlabel_entry");
            if (formButtonData.getInputStyle() != null && formButtonData.getInputStyle().length() > 0) {
                flowPanel.addStyleName(formButtonData.getInputStyle());
            }
            if (formButtonData.getTitle() != null) {
                labelButton.setTitle(formButtonData.getTitle());
            }
            labelButton.addClickHandler(this);
        } else {
            button = new Button(formButtonData.getLabel());
            flowPanel.addStyleName("bonita_form_button_entry");
            if (formButtonData.getInputStyle() != null && formButtonData.getInputStyle().length() > 0) {
                flowPanel.addStyleName(formButtonData.getInputStyle());
                String css = "bonita_form_button " + formButtonData.getInputStyle();
                button.setStyleName(css);
            } else {
                button.setStyleName("bonita_form_button");
            }
            if (formButtonData.getTitle() != null && formButtonData.getTitle().length() > 0) {
                button.setTitle(formButtonData.getTitle());
            }
            button.addClickHandler(this);
        }
        insertButtonInPanel(formButtonData);
    }
    
    /**
     * Insert the generated button in the panel and set the fieldWidget attribute to keep a reference to it
     * @param formButtonData the button data object
     */
    protected void insertButtonInPanel(final ReducedFormWidget formButtonData) {
        final Map<String, String> htmlAttributes = formButtonData.getHtmlAttributes();
        if (htmlAttributes.size() > 0) {
            final ElementAttributeSupport elementAttributeSupport = new ElementAttributeSupport();
            for (final Entry<String, String> htmlAttribute : htmlAttributes.entrySet()) {
                if (isLabelButton) {
                    elementAttributeSupport.addHtmlAttribute(labelButton, htmlAttribute.getKey(), htmlAttribute.getValue());
                } else {
                    elementAttributeSupport.addHtmlAttribute(button, htmlAttribute.getKey(), htmlAttribute.getValue());
                }
            }
        }
        if (isLabelButton) {
            flowPanel.add(labelButton);
        } else {
            flowPanel.add(button);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void onClick(final ClickEvent clickEvent) {
        if (clickHandlers != null) {
            for (final ClickHandler clickHandler : clickHandlers.values()) {
                clickHandler.onClick(clickEvent);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public HandlerRegistration addClickHandler(String id, final ClickHandler clickHandler) {
        if (clickHandlers == null) {
            clickHandlers = new HashMap<String, ClickHandler>();
        }
        clickHandlers.put(id, clickHandler);
        return new EventHandlerRegistration(clickHandler);
    }
    
    /**
     * {@inheritDoc}
     */
    public HandlerRegistration addClickHandler(final ClickHandler clickHandler) {
        if (clickHandlers == null) {
            clickHandlers = new HashMap<String, ClickHandler>();
        }
        clickHandlers.put(String.valueOf(clickHandler.hashCode()), clickHandler);
        return new EventHandlerRegistration(clickHandler);
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
            clickHandlers.remove(eventHandler);
        }
    }
    
    /**
     * @return the button type
     */
    public WidgetType getWidgetType() {
        return widgetType;
    }
    
    /**
     * @return the button widget
     */
    public Widget getButton() {
        if (isLabelButton) {
            return labelButton;
        } else {
            return button;
        }
    }
}
