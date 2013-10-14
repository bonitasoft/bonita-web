/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.view.common.RpcFormsServices;
import org.bonitasoft.forms.client.view.common.URLUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Widget displaying an image
 * 
 * @author Anthony Birembaut
 */
public class ImageWidget extends Composite implements HasClickHandlers, ClickHandler {

    /**
     * The formID retrieved from the request as a String
     */
    protected String formID;

    /**
     * The context map
     */
    Map<String, Object> contextMap;

    /**
     * The image widget
     */
    protected Image image;

    /**
     * indicates if the image to display is an attachment
     */
    protected boolean displayAttachmentImage;

    /**
     * Click handlers registered for the widget
     */
    protected List<ClickHandler> clickHandlers;

    /**
     * Constructor
     * 
     * @param formID
     * @param contextMap
     * @param isArchived
     * @param value
     * @param ImageStyle
     * @param displayAttachmentImage
     */
    public ImageWidget(final String formID, final Map<String, Object> contextMap, final long documentId, final String value, final String imageStyle,
            final boolean displayAttachmentImage) {

        this.formID = formID;
        this.contextMap = contextMap;
        this.displayAttachmentImage = displayAttachmentImage;

        final FlowPanel flowPanel = new FlowPanel();

        image = new Image();
        if (value != null) {
            if (displayAttachmentImage) {
                final String imageServletURL = RpcFormsServices.getAttachmentImageURL();
                final String imageURL = URLUtils.getInstance().getAttachmentURL(imageServletURL, formID, contextMap, documentId, value);
                image.setUrl(imageURL);
            } else {
                image.setUrl(value);
            }
        }
        image.setStyleName("bonita_form_image");
        if (imageStyle != null && imageStyle.length() > 0) {
            image.addStyleName(imageStyle);
        }
        image.addClickHandler(this);

        flowPanel.add(image);
        initWidget(flowPanel);
    }

    /**
     * @return the URL of the image
     */
    public String getValue() {
        return image.getUrl();
    }

    /**
     * Set the value of the widget
     * 
     * @param documentId
     * @param value
     *            the URL of the image or the attachment name
     * @param fireEvents
     */
    public void setValue(final long documentId, final String value, final boolean fireEvents) {
        if (value != null) {
            if (displayAttachmentImage) {
                final String imageServletURL = RpcFormsServices.getAttachmentImageURL();
                final String imageURL = URLUtils.getInstance().getAttachmentURL(imageServletURL, formID, contextMap, documentId, value);
                image.setUrl(imageURL);
            } else {
                image.setUrl(value);
            }
        }
        if (fireEvents) {
            DomEvent.fireNativeEvent(Document.get().createChangeEvent(), image);
        }
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
    public HandlerRegistration addClickHandler(final ClickHandler clickHandler) {
        if (clickHandlers == null) {
            clickHandlers = new ArrayList<ClickHandler>();
        }
        clickHandlers.add(clickHandler);
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

        @Override
        public void removeHandler() {
            clickHandlers.remove(eventHandler);
        }
    }
}
