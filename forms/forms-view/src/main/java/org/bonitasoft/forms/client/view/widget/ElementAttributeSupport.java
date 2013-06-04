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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.view.widget;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import org.bonitasoft.forms.client.view.common.DOMUtils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.HasFocusHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

/**
 * @author Anthony Birembaut
 *
 */
public class ElementAttributeSupport  {

    /**
     * Supported HTML events on form fields and buttons
     *
     */
    private static enum SUPPORTED_EVENT {onclick, ondbclick, onmousedown, onmousemove, onmouseout, onmouseover, onmouseup, onchange, onblur, onfocus, onkeydown, onkeypress, onkeyup};
    
    
    /**
     * Set the HTML attributes of a widget.
     * @param fieldWidget the field widget
     * @param widgetData the widget data object
     */
    public void addHtmlAttributes(final Widget fieldWidget, final Map<String, String> htmlAttributes) {
        if (htmlAttributes.size() > 0) {
            for (final Entry<String, String> htmlAttribute : htmlAttributes.entrySet()) {
                addHtmlAttribute(fieldWidget, htmlAttribute.getKey(), htmlAttribute.getValue());
            }
        }
    }
    
    protected void addHtmlAttribute(Widget fieldWidget, final String htmlAttributeName, final String htmlAttributeValue) {
        //Working for bug 4991: the "Html Attributes" of date widget
        if(fieldWidget instanceof DateBox){
            fieldWidget = ((DateBox) fieldWidget).getTextBox();
        }
        try {
            final SUPPORTED_EVENT event = SUPPORTED_EVENT.valueOf(htmlAttributeName.toLowerCase());
            switch (event) {
            case onclick:
                 ((HasClickHandlers)fieldWidget).addClickHandler(new ClickHandler() {
                    
                    public void onClick(final ClickEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case ondbclick:
                ((HasDoubleClickHandlers)fieldWidget).addDoubleClickHandler(new DoubleClickHandler() {
                    
                    public void onDoubleClick(final DoubleClickEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onblur:
                ((HasBlurHandlers)fieldWidget).addBlurHandler(new BlurHandler() {
                    
                    public void onBlur(final BlurEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onchange:
                if (fieldWidget instanceof HasChangeHandlers) {
                    ((HasChangeHandlers)fieldWidget).addChangeHandler(new ChangeHandler() {
                        
                        public void onChange(final ChangeEvent event) {
                            DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                        }
                    });
                } else if (fieldWidget instanceof HasValueChangeHandlers<?>) {
                    final HasValueChangeHandlers<Serializable> widget = (HasValueChangeHandlers<Serializable>)fieldWidget;
                    widget.addValueChangeHandler(new ValueChangeHandler<Serializable>() {
                        
                        public void onValueChange(final ValueChangeEvent<Serializable> event) {
                            DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                        }
                    });
                }
                break;
            case onfocus:
                ((HasFocusHandlers)fieldWidget).addFocusHandler(new FocusHandler() {
                    
                    public void onFocus(final FocusEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onkeydown:
                ((HasKeyDownHandlers)fieldWidget).addKeyDownHandler(new KeyDownHandler() {
                    
                    public void onKeyDown(final KeyDownEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onkeyup:
                ((HasKeyUpHandlers)fieldWidget).addKeyUpHandler(new KeyUpHandler() {
                    
                    public void onKeyUp(final KeyUpEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onkeypress:
                ((HasKeyUpHandlers)fieldWidget).addKeyUpHandler(new KeyUpHandler() {
                    
                    public void onKeyUp(final KeyUpEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onmousedown:
                ((HasMouseDownHandlers)fieldWidget).addMouseDownHandler(new MouseDownHandler() {
                    
                    public void onMouseDown(final MouseDownEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onmouseup:
                ((HasMouseUpHandlers)fieldWidget).addMouseUpHandler(new MouseUpHandler() {
                    
                    public void onMouseUp(final MouseUpEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onmouseover:
                ((HasMouseOverHandlers)fieldWidget).addMouseOverHandler(new MouseOverHandler() {
                    
                    public void onMouseOver(final MouseOverEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onmouseout:
                ((HasMouseOutHandlers)fieldWidget).addMouseOutHandler(new MouseOutHandler() {
                    
                    public void onMouseOut(final MouseOutEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            case onmousemove:
                ((HasMouseMoveHandlers)fieldWidget).addMouseMoveHandler(new MouseMoveHandler() {
                    
                    public void onMouseMove(final MouseMoveEvent event) {
                        DOMUtils.getInstance().javascriptEval(htmlAttributeValue);
                    }
                });
                break;
            default:
                break;
            }
        } catch (final Exception e) {
            if (fieldWidget instanceof CheckBox || fieldWidget instanceof CheckboxGroupWidget || fieldWidget instanceof RadioButtonGroupWidget || fieldWidget instanceof FileUploadWidget || fieldWidget instanceof SuggestBox || fieldWidget instanceof AsyncSuggestBoxWidget || fieldWidget instanceof DateBox) {
                final NodeList<Element> inputs = fieldWidget.getElement().getElementsByTagName("input");
                if (inputs != null) {
                    for (int i = 0; i < inputs.getLength(); i++) {
                        inputs.getItem(i).setAttribute(htmlAttributeName, htmlAttributeValue);
                    }
                }
            } else if (fieldWidget instanceof DurationWidget) {  
                final NodeList<Element> selects = fieldWidget.getElement().getElementsByTagName("select");
                if (selects != null) {
                    for (int i = 0; i < selects.getLength(); i++) {
                        selects.getItem(i).setAttribute(htmlAttributeName, htmlAttributeValue);
                    }
                }
            } else if (fieldWidget instanceof ImageWidget) {  
                final NodeList<Element> images = fieldWidget.getElement().getElementsByTagName("img");
                if (images != null) {
                    for (int i = 0; i < images.getLength(); i++) {
                        images.getItem(i).setAttribute(htmlAttributeName, htmlAttributeValue);
                    }
                }
            } else if (fieldWidget instanceof FileDownloadWidget) {  
                final NodeList<Element> links = fieldWidget.getElement().getElementsByTagName("a");
                if (links != null) {
                    for (int i = 0; i < links.getLength(); i++) {
                        links.getItem(i).setAttribute(htmlAttributeName, htmlAttributeValue);
                    }
                }
            } else if (fieldWidget instanceof TableWidget || fieldWidget instanceof EditableGridWidget) {  
                final NodeList<Element> tables = fieldWidget.getElement().getElementsByTagName("table");
                if (tables != null) {
                    for (int i = 0; i < tables.getLength(); i++) {
                        tables.getItem(i).setAttribute(htmlAttributeName, htmlAttributeValue);
                    }
                }
            } else if (fieldWidget instanceof RichTextWidget) {  
                final NodeList<Element> iframes = fieldWidget.getElement().getElementsByTagName("iframe");
                if (iframes != null) {
                    for (int i = 0; i < iframes.getLength(); i++) {
                        iframes.getItem(i).setAttribute(htmlAttributeName, htmlAttributeValue);
                    }
                }
            } else {
                fieldWidget.getElement().setAttribute(htmlAttributeName, htmlAttributeValue);
            }
        }
    }
}
