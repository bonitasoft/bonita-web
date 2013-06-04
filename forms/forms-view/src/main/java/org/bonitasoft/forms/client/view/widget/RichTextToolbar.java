/**
 * Copyright 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.bonitasoft.forms.client.view.widget;

import org.bonitasoft.forms.client.i18n.RichTextLabels;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * A toolbar for use with {@link RichTextArea}. It provides a simple UI for all
 * rich text formatting, dynamically displayed only for the available
 * functionality.
 */
public class RichTextToolbar extends Composite {

    /**
     * This {@link ClientBundle} is used for all the button icons. Using a
     * bundle allows all of these images to be packed into a single image, which
     * saves a lot of HTTP requests, drastically improving startup time.
     */
    public interface Images extends ClientBundle {

        ImageResource bold();

        ImageResource createLink();

        ImageResource hr();

        ImageResource indent();

        ImageResource insertImage();

        ImageResource italic();

        ImageResource justifyCenter();

        ImageResource justifyLeft();

        ImageResource justifyRight();

        ImageResource ol();

        ImageResource outdent();

        ImageResource removeFormat();

        ImageResource removeLink();

        ImageResource strikeThrough();

        ImageResource subscript();

        ImageResource superscript();

        ImageResource ul();

        ImageResource underline();
    }

    /**
     * We use an inner EventHandler class to avoid exposing event methods on the
     * RichTextToolbar itself.
     */
    private class EventHandler implements ClickHandler, ChangeHandler, KeyUpHandler {

        public void onChange(final ChangeEvent event) {
            final Widget sender = (Widget) event.getSource();

            if (sender == backColors) {
                formatter.setBackColor(backColors.getValue(backColors.getSelectedIndex()));
                backColors.setSelectedIndex(0);
            } else if (sender == foreColors) {
                formatter.setForeColor(foreColors.getValue(foreColors.getSelectedIndex()));
                foreColors.setSelectedIndex(0);
            } else if (sender == fonts) {
                formatter.setFontName(fonts.getValue(fonts.getSelectedIndex()));
                fonts.setSelectedIndex(0);
            } else if (sender == fontSizes) {
                formatter.setFontSize(fontSizesConstants[fontSizes.getSelectedIndex() - 1]);
                fontSizes.setSelectedIndex(0);
            }
        }

        public void onClick(final ClickEvent event) {
            final Widget sender = (Widget) event.getSource();

            if (sender == bold) {
                formatter.toggleBold();
            } else if (sender == italic) {
                formatter.toggleItalic();
            } else if (sender == underline) {
                formatter.toggleUnderline();
            } else if (sender == subscript) {
                formatter.toggleSubscript();
            } else if (sender == superscript) {
                formatter.toggleSuperscript();
            } else if (sender == strikethrough) {
                formatter.toggleStrikethrough();
            } else if (sender == indent) {
                formatter.rightIndent();
            } else if (sender == outdent) {
                formatter.leftIndent();
            } else if (sender == justifyLeft) {
                formatter.setJustification(RichTextArea.Justification.LEFT);
            } else if (sender == justifyCenter) {
                formatter.setJustification(RichTextArea.Justification.CENTER);
            } else if (sender == justifyRight) {
                formatter.setJustification(RichTextArea.Justification.RIGHT);
            } else if (sender == insertImage) {
                final String url = Window.prompt("Enter an image URL:", "http://");
                if (url != null) {
                    formatter.insertImage(url);
                }
            } else if (sender == createLink) {
                final String url = Window.prompt("Enter a link URL:", "http://");
                if (url != null) {
                    formatter.createLink(url);
                }
            } else if (sender == removeLink) {
                formatter.removeLink();
            } else if (sender == hr) {
                formatter.insertHorizontalRule();
            } else if (sender == ol) {
                formatter.insertOrderedList();
            } else if (sender == ul) {
                formatter.insertUnorderedList();
            } else if (sender == removeFormat) {
                formatter.removeFormat();
            } else if (sender == richText) {
                // We use the RichTextArea's onKeyUp event to update the toolbar
                // status.
                // This will catch any cases where the user moves the cursur
                // using the
                // keyboard, or uses one of the browser's built-in keyboard
                // shortcuts.
                updateStatus();
            }
        }

        public void onKeyUp(final KeyUpEvent event) {
            final Widget sender = (Widget) event.getSource();
            if (sender == richText) {
                // We use the RichTextArea's onKeyUp event to update the toolbar
                // status.
                // This will catch any cases where the user moves the cursur
                // using the
                // keyboard, or uses one of the browser's built-in keyboard
                // shortcuts.
                updateStatus();
            }
        }
    }

    private static final RichTextArea.FontSize[] fontSizesConstants = new RichTextArea.FontSize[] { RichTextArea.FontSize.XX_SMALL, RichTextArea.FontSize.X_SMALL, RichTextArea.FontSize.SMALL,
            RichTextArea.FontSize.MEDIUM, RichTextArea.FontSize.LARGE, RichTextArea.FontSize.X_LARGE, RichTextArea.FontSize.XX_LARGE };

    private final Images images = (Images) GWT.create(Images.class);
    private final RichTextLabels richTextLabels = (RichTextLabels) GWT.create(RichTextLabels.class);
    private final EventHandler handler = new EventHandler();

    private final RichTextArea richText;
    private final RichTextArea.Formatter formatter;

    private final FlowPanel flowPanel = new FlowPanel();
    private ToggleButton bold;
    private ToggleButton italic;
    private ToggleButton underline;
    private ToggleButton subscript;
    private ToggleButton superscript;
    private ToggleButton strikethrough;
    private PushButton indent;
    private PushButton outdent;
    private PushButton justifyLeft;
    private PushButton justifyCenter;
    private PushButton justifyRight;
    private PushButton hr;
    private PushButton ol;
    private PushButton ul;
    private PushButton insertImage;
    private PushButton createLink;
    private PushButton removeLink;
    private PushButton removeFormat;

    private ListBox backColors;
    private ListBox foreColors;
    private ListBox fonts;
    private ListBox fontSizes;

    /**
     * Creates a new toolbar that drives the given rich text area.
     * 
     * @param richText
     *            the rich text area to be controlled
     */
    public RichTextToolbar(final RichTextArea richText) {
        this.richText = richText;
        this.formatter = richText.getFormatter();

        initWidget(flowPanel);
        setStyleName("gwt-RichTextToolbar");

        if (formatter != null) {
            flowPanel.add(bold = createToggleButton(images.bold(), richTextLabels.bold()));
            flowPanel.add(italic = createToggleButton(images.italic(), richTextLabels.italic()));
            flowPanel.add(underline = createToggleButton(images.underline(), richTextLabels.underline()));
            flowPanel.add(subscript = createToggleButton(images.subscript(), richTextLabels.subscript()));
            flowPanel.add(superscript = createToggleButton(images.superscript(), richTextLabels.superscript()));
            flowPanel.add(justifyLeft = createPushButton(images.justifyLeft(), richTextLabels.justifyLeft()));
            flowPanel.add(justifyCenter = createPushButton(images.justifyCenter(), richTextLabels.justifyCenter()));
            flowPanel.add(justifyRight = createPushButton(images.justifyRight(), richTextLabels.justifyRight()));
            flowPanel.add(strikethrough = createToggleButton(images.strikeThrough(), richTextLabels.strikeThrough()));
            flowPanel.add(indent = createPushButton(images.indent(), richTextLabels.indent()));
            flowPanel.add(outdent = createPushButton(images.outdent(), richTextLabels.outdent()));
            flowPanel.add(hr = createPushButton(images.hr(), richTextLabels.hr()));
            flowPanel.add(ol = createPushButton(images.ol(), richTextLabels.ol()));
            flowPanel.add(ul = createPushButton(images.ul(), richTextLabels.ul()));
            flowPanel.add(insertImage = createPushButton(images.insertImage(), richTextLabels.insertImage()));
            flowPanel.add(createLink = createPushButton(images.createLink(), richTextLabels.createLink()));
            flowPanel.add(removeLink = createPushButton(images.removeLink(), richTextLabels.removeLink()));
            flowPanel.add(removeFormat = createPushButton(images.removeFormat(), richTextLabels.removeFormat()));

            flowPanel.add(backColors = createColorList("Background"));
            flowPanel.add(foreColors = createColorList("Foreground"));
            flowPanel.add(fonts = createFontList());
            flowPanel.add(fontSizes = createFontSizes());

            final FlowPanel toolbarBottom = new FlowPanel();
            toolbarBottom.setStyleName("bonita_clear_float");
            flowPanel.add(toolbarBottom);
            
            // We only use these handlers for updating status, so don't hook
            // them up unless at least basic editing is supported.
            richText.addKeyUpHandler(handler);
            richText.addClickHandler(handler);
        }
    }

    private ListBox createColorList(final String caption) {
        final ListBox lb = new ListBox();
        lb.addStyleName("bonita_toolbarElement");
        lb.addStyleName("bonita_toolbarList");
        lb.addChangeHandler(handler);
        lb.setVisibleItemCount(1);

        lb.addItem(caption);
        lb.addItem(richTextLabels.white(), "white");
        lb.addItem(richTextLabels.black(), "black");
        lb.addItem(richTextLabels.red(), "red");
        lb.addItem(richTextLabels.green(), "green");
        lb.addItem(richTextLabels.yellow(), "yellow");
        lb.addItem(richTextLabels.blue(), "blue");
        return lb;
    }

    private ListBox createFontList() {
        final ListBox lb = new ListBox();
        lb.addStyleName("bonita_toolbarElement");
        lb.addStyleName("bonita_toolbarList");
        lb.addChangeHandler(handler);
        lb.setVisibleItemCount(1);

        lb.addItem(richTextLabels.font(), "");
        lb.addItem(richTextLabels.normal(), "");
        lb.addItem("Times New Roman", "Times New Roman");
        lb.addItem("Arial", "Arial");
        lb.addItem("Courier New", "Courier New");
        lb.addItem("Georgia", "Georgia");
        lb.addItem("Trebuchet", "Trebuchet");
        lb.addItem("Verdana", "Verdana");
        return lb;
    }

    private ListBox createFontSizes() {
        final ListBox lb = new ListBox();
        lb.addStyleName("bonita_toolbarElement");
        lb.addStyleName("bonita_toolbarList");
        lb.addChangeHandler(handler);
        lb.setVisibleItemCount(1);

        lb.addItem(richTextLabels.size());
        lb.addItem(richTextLabels.xxsmall());
        lb.addItem(richTextLabels.xsmall());
        lb.addItem(richTextLabels.small());
        lb.addItem(richTextLabels.medium());
        lb.addItem(richTextLabels.large());
        lb.addItem(richTextLabels.xlarge());
        lb.addItem(richTextLabels.xxlarge());
        return lb;
    }

    private PushButton createPushButton(final ImageResource img, final String tip) {
        final PushButton pb = new PushButton(new Image(img));
        pb.addStyleName("bonita_toolbarElement");
        pb.addClickHandler(handler);
        pb.setTitle(tip);
        return pb;
    }

    private ToggleButton createToggleButton(final ImageResource img, final String tip) {
        final ToggleButton tb = new ToggleButton(new Image(img));
        tb.addStyleName("bonita_toolbarElement");
        tb.addClickHandler(handler);
        tb.setTitle(tip);
        return tb;
    }

    /**
     * Updates the status of all the stateful buttons.
     */
    private void updateStatus() {
        if (formatter != null) {
            bold.setDown(formatter.isBold());
            italic.setDown(formatter.isItalic());
            underline.setDown(formatter.isUnderlined());
            subscript.setDown(formatter.isSubscript());
            superscript.setDown(formatter.isSuperscript());
        }

        if (formatter != null) {
            strikethrough.setDown(formatter.isStrikethrough());
        }
    }
}