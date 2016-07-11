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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RichTextArea;

/**
 * Rich text widget
 * 
 * @author Anthony Birembaut
 * 
 */
public class RichTextWidget extends Composite {

    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;

    /**
     * the rich text toolbar
     */
    protected RichTextToolbar richTextToolbar;

    /**
     * the rich text area
     */
    protected RichTextArea richTextArea;

    /**
     * The content of the rich text area to record on detach
     */
    protected String value;

    /**
     * The container of the rich text toolbar and the rich text area
     */
    private FlowPanel richTextContainer;

    /**
     * indicates if the widget is enabled or not
     */
    private boolean isEnabled = true;

    /**
     * Constructor
     * 
     * @param content the content as HTML
     */
    public RichTextWidget() {

        flowPanel = new FlowPanel();

        createWidget();

        initWidget(flowPanel);
    }

    protected void createWidget() {
        richTextContainer = new FlowPanel();
        richTextArea = new RichTextArea();
        richTextArea.addBlurHandler(new BlurHandler() {
            public void onBlur(final BlurEvent event) {
                final RichTextArea source = (RichTextArea) event.getSource();
                value = source.getHTML();
            }
        });
        richTextArea.setStyleName("bonita_richTextArea");
        richTextToolbar = new RichTextToolbar(richTextArea);
        richTextContainer.add(richTextToolbar);
        richTextContainer.add(richTextArea);
        richTextContainer.setStyleName("bonita_rich_text");
        flowPanel.add(richTextContainer);
    }

    /**
     * Enable or disable the checkbox group
     * 
     * @param isEnabled
     */
    public void setEnabled(final boolean isEnabled) {
        this.isEnabled = isEnabled;
        richTextArea.setEnabled(isEnabled);
        richTextToolbar.setVisible(isEnabled);
        if (!isEnabled) {
            richTextArea.addStyleName("bonita_richTextArea_disabled");
        }
    }

    public void setValue(final String value) {
        this.value = value;
        richTextArea.setHTML(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void onUnload() {
        flowPanel.clear();
        super.onUnload();
    }

    @Override
    protected void onLoad() {
        super.onLoad();
        if (flowPanel.getWidgetIndex(richTextContainer) < 0) {
            createWidget();
            setEnabled(isEnabled);
            setValue(value);
        }
    }
}
