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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.ReducedFormSubtitle.SubTitlePosition;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.ReducedFormWidget.ItemPosition;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.client.view.SupportedFieldTypes;
import org.bonitasoft.forms.client.view.common.URLUtils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;
import com.google.gwt.user.datepicker.client.DateBox.Format;

/**
 * Generic form flow field widget
 * 
 * @author Anthony Birembaut
 */
@SuppressWarnings("rawtypes")
public class FormFieldWidget extends Composite implements HasChangeHandlers, ChangeHandler, HasClickHandlers, ClickHandler, HasValueChangeHandlers,
        ValueChangeHandler, HasSelectionHandlers<Suggestion>, SelectionHandler<Suggestion> {

    /**
     * the flow panel used to display the data field and its decorations
     */
    protected FlowPanel flowPanel;

    /**
     * the container panel used to display the data field and its label
     */
    protected FlowPanel fieldContainer;

    /**
     * the flow panel used to display the tooltip icon
     */
    protected FlowPanel theOuterToolTipPanel;

    /**
     * The widget Id
     */
    protected String widgetId;

    /**
     * widget field type
     */
    protected WidgetType widgetType;

    /**
     * the value type
     */
    protected String valueType;

    /**
     * the field ouput type
     */
    protected String fieldOutputType;

    /**
     * the widget label
     */
    private LabelWidget labelWidget;

    /**
     * the widget field
     */
    protected Widget fieldWidget;

    /**
     * Date format for date picker
     */
    protected DateTimeFormat displayFormat;

    /**
     * mandatory form field symbol
     */
    protected String mandatoryFieldSymbol;

    /**
     * mandatory form field symbol classes
     */
    protected String mandatoryFieldClasses;

    /**
     * the display format
     */
    protected String displayFormatPattern;

    /**
     * The formID retrieved from the request as a String
     */
    protected String formID = null;

    /**
     * The URL contextmap retrieved from the History taken
     */
    protected Map<String, Object> contextMap;

    /**
     * Click handlers registered for the widget
     */
    protected List<ClickHandler> clickHandlers;

    /**
     * Change handlers registered for the widget
     */
    protected List<ChangeHandler> changeHandlers;

    /**
     * value change handlers registered for the widget
     */
    protected List<ValueChangeHandler<Serializable>> valueChangeHandlers;

    /**
     * selection handlers registered for the widget
     */
    protected List<SelectionHandler<Suggestion>> selectionHandlers;

    /**
     * Map of suggestions for the suggestion box
     */
    protected TreeMap<String, String> suggestionsMap;

    /**
     * Constructor
     * 
     * @param widgetData
     *            the widget data object
     * @param contextMap
     * @param mandatoryFieldSymbol
     * @param mandatoryFieldClasses
     */
    public FormFieldWidget(final ReducedFormWidget widgetData, final Map<String, Object> contextMap, final String mandatoryFieldSymbol,
            final String mandatoryFieldClasses) {
        this(widgetData, widgetData.getInitialFieldValue(), contextMap, mandatoryFieldSymbol, mandatoryFieldClasses);
    }

    /**
     * Constructor
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     * @param contextMap
     * @param mandatoryFieldSymbol
     * @param mandatoryFieldClasses
     */
    public FormFieldWidget(final ReducedFormWidget widgetData, final FormFieldValue fieldValue, final Map<String, Object> contextMap,
            final String mandatoryFieldSymbol, final String mandatoryFieldClasses) {
        if (contextMap.get(URLUtils.FORM_ID) != null) {
            formID = (String) contextMap.get(URLUtils.FORM_ID);
        }
        this.contextMap = contextMap;
        this.mandatoryFieldSymbol = mandatoryFieldSymbol;
        this.mandatoryFieldClasses = mandatoryFieldClasses;

        init(widgetData, fieldValue);

        initWidget(flowPanel);

        createWidget(widgetData, fieldValue);
    }

    /**
     * Initialize the widget attributes
     * 
     * @param widgetData
     * @param fieldValue
     */
    protected void init(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {

        fieldContainer = new FlowPanel();

        flowPanel = new FlowPanel();

        widgetId = widgetData.getId();

        widgetType = widgetData.getType();

        displayFormatPattern = widgetData.getDisplayFormat();

        valueType = fieldValue.getValueType();

        fieldOutputType = widgetData.getFieldOutputType();
    }

    /**
     * Create a {@link TextBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link TextBox}
     */
    @SuppressWarnings("unchecked")
    protected TextBox createTextBox(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final TextBox textBox = new TextBox();
        textBox.addChangeHandler(this);
        textBox.addValueChangeHandler(this);
        if (widgetData.getMaxLength() != 0) {
            textBox.setMaxLength(widgetData.getMaxLength());
        }
        if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(valueType)) {
            textBox.setValue(getDateAsText(fieldValue));
        } else {
            textBox.setValue(getStringValue(fieldValue));
        }
        textBox.setEnabled(!widgetData.isReadOnly());
        return textBox;
    }

    /**
     * Create a {@link TextArea} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link TextArea}
     */
    @SuppressWarnings("unchecked")
    protected TextArea createTextArea(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final TextArea textArea = new TextArea();
        textArea.addChangeHandler(this);
        textArea.addValueChangeHandler(this);
        if (widgetData.getMaxLength() != 0) {
            textArea.setCharacterWidth(widgetData.getMaxLength());
        }
        if (widgetData.getMaxHeight() != 0) {
            textArea.setVisibleLines(widgetData.getMaxHeight());
        }
        if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(valueType)) {
            textArea.setValue(getDateAsText(fieldValue));
        } else {
            textArea.setValue(getStringValue(fieldValue));
        }
        textArea.setEnabled(!widgetData.isReadOnly());
        return textArea;
    }

    /**
     * Create a {@link RichTextWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link RichTextWidget}
     */
    protected RichTextWidget createRichTextArea(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final RichTextWidget richTextWidget = new RichTextWidget();
        if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(valueType)) {
            richTextWidget.setValue(getDateAsText(fieldValue));
        } else {
            richTextWidget.setValue(getStringValue(fieldValue));
        }
        richTextWidget.setEnabled(!widgetData.isReadOnly());
        return richTextWidget;
    }

    /**
     * Create a {@link PasswordTextBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link PasswordTextBox}
     */
    @SuppressWarnings("unchecked")
    protected PasswordTextBox createPasswordTextBox(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final PasswordTextBox passwordTextBox = new PasswordTextBox();
        passwordTextBox.addChangeHandler(this);
        passwordTextBox.addValueChangeHandler(this);
        if (widgetData.getMaxLength() != 0) {
            passwordTextBox.setMaxLength(widgetData.getMaxLength());
        }
        passwordTextBox.setValue(getStringValue(fieldValue));
        passwordTextBox.setEnabled(!widgetData.isReadOnly());
        return passwordTextBox;
    }

    /**
     * Create a {@link HTML} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link HTML}
     */
    protected HTML createText(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final HTML text = new HTML();
        if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(valueType)) {
            text.setText(getDateAsText(fieldValue));
        } else if (widgetData.allowHTMLInField()) {
            text.setHTML(getStringValue(fieldValue));
        } else {
            text.setText(getStringValue(fieldValue));
        }
        return text;
    }

    /**
     * Create a {@link CheckBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link CheckBox}
     */
    @SuppressWarnings("unchecked")
    protected CheckBox createCheckBox(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final CheckBox checkBox = new CheckBox();
        checkBox.addClickHandler(this);
        checkBox.addValueChangeHandler(this);
        try {
            checkBox.setValue((Boolean) fieldValue.getValue());
        } catch (final Exception e) {
            Window.alert("initial value for checkbox " + widgetData.getId() + " should be a boolean.");
            checkBox.setValue(false, false);
        }
        checkBox.setEnabled(!widgetData.isReadOnly());
        return checkBox;
    }

    /**
     * Create a {@link RadioButtonGroupWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link RadioButtonGroupWidget}
     */
    @SuppressWarnings("unchecked")
    protected RadioButtonGroupWidget createRadioButtonGroup(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final RadioButtonGroupWidget radioButtonGroupWidget = new RadioButtonGroupWidget(widgetData.getId(), widgetData.getAvailableValues(),
                getStringValue(fieldValue), widgetData.getItemsStyle(), widgetData.allowHTMLInField());
        radioButtonGroupWidget.addClickHandler(this);
        radioButtonGroupWidget.addValueChangeHandler(this);
        radioButtonGroupWidget.setEnabled(!widgetData.isReadOnly());
        return radioButtonGroupWidget;
    }

    /**
     * Create a {@link ListBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link ListBox}
     */
    protected ListBox createListBox(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final ListBox listBox = new ListBox(false);
        listBox.addChangeHandler(this);
        int index = 0;
        final String fieldValueStr = getStringValue(fieldValue);
        for (final ReducedFormFieldAvailableValue availableValue : widgetData.getAvailableValues()) {
            listBox.addItem(availableValue.getLabel(), availableValue.getValue());
            if (availableValue.getValue() != null && availableValue.getValue().equals(fieldValueStr)) {
                listBox.setItemSelected(index, true);
            }
            index++;
        }
        listBox.setEnabled(!widgetData.isReadOnly());
        return listBox;
    }

    /**
     * Create a multiple select {@link ListBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link ListBox}
     */
    protected ListBox createListBoxMulti(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final ListBox listBoxMulti = new ListBox(true);
        listBoxMulti.addChangeHandler(this);
        final Collection<String> initialValues = getStringValues(fieldValue);
        int indexMulti = 0;
        for (final ReducedFormFieldAvailableValue availableValue : widgetData.getAvailableValues()) {
            listBoxMulti.addItem(availableValue.getLabel(), availableValue.getValue());
            if (initialValues != null) {
                for (final String value : initialValues) {
                    if (availableValue.getValue() != null && availableValue.getValue().equals(value)) {
                        listBoxMulti.setItemSelected(indexMulti, true);
                    }
                }
            }
            indexMulti++;
        }
        if (widgetData.getMaxHeight() != 0) {
            listBoxMulti.setVisibleItemCount(widgetData.getMaxHeight());
        }
        listBoxMulti.setEnabled(!widgetData.isReadOnly());
        return listBoxMulti;
    }

    /**
     * Create a {@link SuggestBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link SuggestBox}
     */
    @SuppressWarnings("unchecked")
    protected SuggestBox createSuggestBox(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final TextBox textBox = new TextBox();
        textBox.setEnabled(!widgetData.isReadOnly());
        final DefaultSuggestionDisplay suggestionDisplay = new DefaultSuggestionDisplay();
        final String popupStyle = widgetData.getItemsStyle();
        if (popupStyle != null && popupStyle.length() > 0) {
            suggestionDisplay.setPopupStyleName(popupStyle);
        }
        suggestionsMap = new TreeMap<String, String>();
        final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();

        // this fix the reordering of numbers
        Comparator<String> numberComparator = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {

                if (stringIsDouble(o1) && stringIsDouble(o2)) {
                    return Double.valueOf(o1).compareTo(Double.valueOf(o2));
                }
                return o1.compareTo(o2);
            }

            private boolean stringIsDouble(String str) {
                return str.matches("^(([0-9]*)|(([0-9]*)[\\.,\\,]([0-9]*)))$");
            }

        };
        oracle.setComparator(numberComparator);

        String labelValue = null;
        final String fieldValueStr = getStringValue(fieldValue);
        widgetData.getAvailableValues();
        for (final ReducedFormFieldAvailableValue availableValue : widgetData.getAvailableValues()) {
            final String label = availableValue.getLabel();
            final String value = availableValue.getValue();
            suggestionsMap.put(label, value);
            oracle.add(label);
            if (value != null && value.equals(fieldValueStr)) {
                labelValue = label;
            }
        }
        final SuggestBox suggestBox = new SuggestBox(oracle, textBox, suggestionDisplay);
        suggestBox.addValueChangeHandler(this);
        suggestBox.addSelectionHandler(this);
        if (labelValue != null) {
            suggestBox.setValue(labelValue);
        } else {
            suggestBox.setValue(fieldValueStr);
        }
        if (widgetData.getMaxItems() > 0) {
            suggestBox.setLimit(widgetData.getMaxItems() - 1);
        }
        return suggestBox;
    }

    /**
     * @param widgetData
     * @param fieldValue
     * @return a {@link AsyncSuggestBoxWidget}
     */
    @SuppressWarnings("unchecked")
    protected AsyncSuggestBoxWidget createSuggestBoxAsync(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final AsyncSuggestBoxWidget formAsyncSuggestBoxWidget = new AsyncSuggestBoxWidget(widgetData, formID, contextMap, fieldValue);
        formAsyncSuggestBoxWidget.getAsyncSuggestBox().addValueChangeHandler(this);
        formAsyncSuggestBoxWidget.getAsyncSuggestBox().addSelectionHandler(this);
        return formAsyncSuggestBoxWidget;
    }

    /**
     * Create a {@link CheckboxGroupWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link CheckboxGroupWidget}
     */
    @SuppressWarnings("unchecked")
    protected CheckboxGroupWidget createCheckboxGroup(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final Collection<String> initialValues = getStringValues(fieldValue);
        final CheckboxGroupWidget checkBoxGroupWidget = new CheckboxGroupWidget(widgetData.getAvailableValues(), initialValues, widgetData.getItemsStyle(),
                widgetData.allowHTMLInField());
        checkBoxGroupWidget.addClickHandler(this);
        checkBoxGroupWidget.addValueChangeHandler(this);
        checkBoxGroupWidget.setEnabled(!widgetData.isReadOnly());
        return checkBoxGroupWidget;
    }

    /**
     * Create a {@link DateBox} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link DateBox}
     */
    @SuppressWarnings("unchecked")
    protected DateBox createDateBox(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final DateBox dateBox = new DateBox();
        dateBox.addValueChangeHandler(this);
        // display format
        if (displayFormatPattern != null && displayFormatPattern.length() > 0) {
            displayFormat = DateTimeFormat.getFormat(displayFormatPattern);
        } else {
            displayFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
        }
        final Format format = new DefaultFormat(displayFormat);
        dateBox.setFormat(format);
        if (fieldValue.getValue() != null) {
            final Date initialDate = (Date) fieldValue.getValue();
            dateBox.setValue(initialDate);
        }
        if (widgetData.isReadOnly()) {
            dateBox.getDatePicker().removeFromParent();
            dateBox.getTextBox().setEnabled(false);
        }
        return dateBox;
    }

    /**
     * Create a {@link DurationWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link DurationWidget}
     */
    protected DurationWidget createDuration(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        Long duration = null;
        try {
            duration = (Long) fieldValue.getValue();
        } catch (final Exception e) {
            duration = 0L;
            Window.alert("the initial value for duration widget " + widgetData.getId() + " should be a Long.");
        }
        final DurationWidget durationWidget = new DurationWidget(widgetData.getDisplayFormat(), duration, widgetData.getItemsStyle());
        durationWidget.addChangeHandler(this);
        durationWidget.setEnabled(!widgetData.isReadOnly());
        return durationWidget;
    }

    /**
     * Create a {@link FileUploadWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link FileUploadWidget}
     */
    protected FileUploadWidget createFileUpload(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final FileUploadWidget fileUploadWidget = new FileUploadWidget(formID, contextMap, widgetData.getId(), widgetData.getFileWidgetInputType(),
                fieldValue.getValueType(), fieldValue.getDocumentId(), fieldValue.getDocumentName(), getStringValue(fieldValue),
                widgetData.isDisplayAttachmentImage());
        if (widgetData.isReadOnly()) {
            fileUploadWidget.disable();
        }
        return fileUploadWidget;
    }

    /**
     * Create a {@link FileDownloadWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link FileDownloadWidget}
     */
    protected FileDownloadWidget createFileDownload(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        return new FileDownloadWidget(formID, contextMap, fieldValue.getValueType(), fieldValue.getDocumentId(), widgetData.isDisplayAttachmentImage(),
                getStringValue(fieldValue));
    }

    /**
     * Create a {@link ImageWidget} widget
     * 
     * @param widgetData
     * @param fieldValue
     *            the widget value
     * @param displayAttachmentImage
     * @return a {@link ImageWidget}
     */
    protected ImageWidget createImage(final ReducedFormWidget widgetData, final FormFieldValue fieldValue, final boolean displayAttachmentImage) {
        String value = null;
        long documentId = -1;
        if (fieldValue != null) {
            if (displayAttachmentImage) {
                value = fieldValue.getDocumentName();
                documentId = fieldValue.getDocumentId();
            } else {
                value = getStringValue(fieldValue);
            }
        }
        return new ImageWidget(formID, contextMap, documentId, value, widgetData.getImageStyle(), displayAttachmentImage);
    }

    /**
     * Create an {@link TableWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link TableWidget}
     */
    @SuppressWarnings("unchecked")
    protected TableWidget createTable(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final TableWidget tableWidget = new TableWidget(widgetData, getTableValue(fieldValue));
        tableWidget.addValueChangeHandler(this);
        return tableWidget;
    }

    /**
     * Create an {@link EditableGridWidget} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return an {@link EditableGridWidget}
     */
    @SuppressWarnings("unchecked")
    protected EditableGridWidget createEditableGrid(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        final EditableGridWidget gridWidget = new EditableGridWidget(formID, widgetData, getGridValue(fieldValue));
        gridWidget.addValueChangeHandler(this);
        return gridWidget;
    }

    /**
     * Create a {@link Hidden} widget
     * 
     * @param widgetData
     *            the widget data object
     * @param fieldValue
     *            the widget value
     * @return a {@link Hidden}
     */
    protected Hidden createHidden(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        return new Hidden(widgetData.getId(), getStringValue(fieldValue));
    }

    /**
     * Create the widget
     * 
     * @param widgetData
     *            the widget data object
     */
    protected void createWidget(final ReducedFormWidget widgetData, final FormFieldValue fieldValue) {
        // label creation
        if (widgetData.isMandatory() || (widgetData.getLabel() != null && widgetData.getLabel().length() > 0)) {
            labelWidget = new LabelWidget(widgetData, mandatoryFieldSymbol, mandatoryFieldClasses);
            if (ItemPosition.LEFT.equals(widgetData.getLabelPosition())) {
                addLabel(labelWidget, "bonita_form_label_left");
            } else if (ItemPosition.TOP.equals(widgetData.getLabelPosition())) {
                addLabel(labelWidget, "bonita_form_label_top");
            }
        }
        // field creation
        switch (widgetType) {
            case TEXTBOX:
                fieldWidget = createTextBox(widgetData, fieldValue);
                break;
            case TEXTAREA:
                fieldWidget = createTextArea(widgetData, fieldValue);
                break;
            case RICH_TEXTAREA:
                fieldWidget = createRichTextArea(widgetData, fieldValue);
                break;
            case PASSWORD:
                fieldWidget = createPasswordTextBox(widgetData, fieldValue);
                break;
            case TEXT:
                fieldWidget = createText(widgetData, fieldValue);
                break;
            case CHECKBOX:
                fieldWidget = createCheckBox(widgetData, fieldValue);
                break;
            case RADIOBUTTON_GROUP:
                fieldWidget = createRadioButtonGroup(widgetData, fieldValue);
                break;
            case LISTBOX_SIMPLE:
                fieldWidget = createListBox(widgetData, fieldValue);
                break;
            case LISTBOX_MULTIPLE:
                fieldWidget = createListBoxMulti(widgetData, fieldValue);
                break;
            case SUGGESTBOX:
                fieldWidget = createSuggestBox(widgetData, fieldValue);
                break;
            case SUGGESTBOX_ASYNC:
                fieldWidget = createSuggestBoxAsync(widgetData, fieldValue);
                break;
            case CHECKBOX_GROUP:
                fieldWidget = createCheckboxGroup(widgetData, fieldValue);
                break;
            case DATE:
                fieldWidget = createDateBox(widgetData, fieldValue);
                break;
            case DURATION:
                fieldWidget = createDuration(widgetData, fieldValue);
                break;
            case FILEUPLOAD:
                fieldWidget = createFileUpload(widgetData, fieldValue);
                break;
            case FILEDOWNLOAD:
                fieldWidget = createFileDownload(widgetData, fieldValue);
                break;
            case IMAGE:
                fieldWidget = createImage(widgetData, fieldValue, widgetData.isDisplayAttachmentImage());
                break;
            case TABLE:
                fieldWidget = createTable(widgetData, fieldValue);
                break;
            case EDITABLE_GRID:
                fieldWidget = createEditableGrid(widgetData, fieldValue);
                break;
            case HIDDEN:
                fieldWidget = createHidden(widgetData, fieldValue);
                fieldContainer.add(fieldWidget);
                break;
            default:
                Window.alert(widgetType + " widget not supported.");
                break;
        }

        if (widgetType == WidgetType.LISTBOX_SIMPLE) {
            final FlowPanel div = new FlowPanel();
            div.add(fieldWidget);
            addStandardWidgetFeatures(div, widgetData);
            div.addStyleName("list_container");
        } else {
            addStandardWidgetFeatures(fieldWidget, widgetData);
        }

        if (labelWidget != null) {
            if (ItemPosition.RIGHT.equals(widgetData.getLabelPosition())) {
                addLabel(labelWidget, "bonita_form_label_right");
                if (isStaticField()) {
                    fieldWidget.addStyleName("bonita_form_static_field_left");
                } else if (WidgetType.TEXTAREA.equals(widgetData.getType()) && widgetData.getMaxLength() != 0) {
                    fieldWidget.addStyleName("bonita_form_field_left_no_width");
                } else {
                    fieldWidget.addStyleName("bonita_form_field_left");
                }
            } else if (ItemPosition.BOTTOM.equals(widgetData.getLabelPosition())) {
                addLabel(labelWidget, "bonita_form_label_bottom");
                if (isStaticField()) {
                    fieldWidget.addStyleName("bonita_form_static_field_top");
                } else if (WidgetType.TEXTAREA.equals(widgetData.getType()) && widgetData.getMaxLength() != 0) {
                    fieldWidget.addStyleName("bonita_form_field_top_no_width");
                } else {
                    fieldWidget.addStyleName("bonita_form_field_top");
                }
            } else if (ItemPosition.TOP.equals(widgetData.getLabelPosition())) {
                if (isStaticField()) {
                    fieldWidget.addStyleName("bonita_form_static_field_bottom");
                } else if (WidgetType.TEXTAREA.equals(widgetData.getType()) && widgetData.getMaxLength() != 0) {
                    fieldWidget.addStyleName("bonita_form_field_bottom_no_width");
                } else {
                    fieldWidget.addStyleName("bonita_form_field_bottom");
                }
            } else {
                if (isStaticField()) {
                    fieldWidget.addStyleName("bonita_form_static_field_right");
                } else if (WidgetType.TEXTAREA.equals(widgetData.getType()) && widgetData.getMaxLength() != 0) {
                    fieldWidget.addStyleName("bonita_form_field_right_no_width");
                } else {
                    fieldWidget.addStyleName("bonita_form_field_right");
                }
            }
            if (widgetData.getLabelStyle() != null && widgetData.getLabelStyle().length() > 0) {
                labelWidget.addStyleName(widgetData.getLabelStyle());
            }
        } else {
            if (isStaticField()) {
                fieldWidget.addStyleName("bonita_form_static_field_top");
            } else {
                fieldWidget.addStyleName("bonita_form_field_top");
            }
        }
        if (widgetData.getInputStyle() != null && widgetData.getInputStyle().length() > 0) {
            fieldWidget.addStyleName(widgetData.getInputStyle());
        }

        // display sub title
        if (widgetData.getSubtitle() != null) {
            final String label = widgetData.getSubtitle().getLabel();
            if (label != null && label.trim().length() > 0) {
                if (SubTitlePosition.TOP.equals(widgetData.getSubtitle().getPosition())) {
                    final HTML subTitle = new HTML(widgetData.getSubtitle().getLabel());
                    subTitle.addStyleName("bonita_form_field_sub_title");
                    flowPanel.add(subTitle);
                    flowPanel.add(fieldContainer);
                } else {
                    final HTML subTitle = new HTML(widgetData.getSubtitle().getLabel());
                    subTitle.addStyleName("bonita_form_field_sub_title");
                    flowPanel.add(fieldContainer);
                    flowPanel.add(subTitle);
                }
            } else {
                flowPanel.add(fieldContainer);
            }
        } else {
            flowPanel.add(fieldContainer);
        }
        flowPanel.addStyleName("bonita_form_vertical_panel");

        // tool-tip widget display
        if (widgetData.getPopupTooltip() != null && widgetData.getPopupTooltip().length() > 0) {

            theOuterToolTipPanel = new FlowPanel();
            final PopupPanel toolTipPopupFrame = new PopupPanel(true);
            toolTipPopupFrame.setWidget(new Label(widgetData.getPopupTooltip()));
            toolTipPopupFrame.addStyleName("bonita_form_popup_tooltip");

            final Image toolTipIcon = new Image("themeResource?theme=portal&location=images/tooltip.gif");
            toolTipIcon.setStylePrimaryName("bonita_form_popup_tooltip_img");
            theOuterToolTipPanel.add(toolTipIcon);
            theOuterToolTipPanel.setStylePrimaryName("bonita_form_popup_tooltip_icon");

            toolTipIcon.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    final Widget source = (Widget) event.getSource();
                    final int left = source.getAbsoluteLeft();
                    final int top = source.getAbsoluteTop();
                    toolTipPopupFrame.setPopupPosition(left + 10, top + 5);
                    toolTipPopupFrame.show();
                }
            });
            if (ItemPosition.RIGHT.equals(widgetData.getLabelPosition())) {
                toolTipPopupFrame.addStyleName("bonita_form_popup_tooltip_left");
                fieldContainer.add(theOuterToolTipPanel);
            } else if (ItemPosition.LEFT.equals(widgetData.getLabelPosition())) {
                fieldContainer.add(theOuterToolTipPanel);
            } else if (ItemPosition.TOP.equals(widgetData.getLabelPosition())) {
                fieldContainer.add(theOuterToolTipPanel);
            } else if (ItemPosition.BOTTOM.equals(widgetData.getLabelPosition())) {
                fieldContainer.add(theOuterToolTipPanel);
            }
        }
    }

    private void addLabel(final HTML labelWidget, final String styleName) {
        labelWidget.addStyleName(styleName);
        fieldContainer.add(labelWidget);
    }

    /**
     * @return true if the field is static, false otherwise
     */
    protected boolean isStaticField() {
        if (widgetType.equals(WidgetType.TEXT) || widgetType.equals(WidgetType.FILEDOWNLOAD) || widgetType.equals(WidgetType.IMAGE)) {
            return true;
        }
        return false;
    }

    /**
     * Get the string display value for a date
     * 
     * @param fieldValue
     *            the field value object
     * @return the date as a String
     */
    protected String getDateAsText(final FormFieldValue fieldValue) {

        if (displayFormatPattern != null && displayFormatPattern.length() > 0) {
            displayFormat = DateTimeFormat.getFormat(displayFormatPattern);
        } else {
            displayFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
        }
        Date initialDate = null;
        if (fieldValue.getValue() != null) {
            initialDate = (Date) fieldValue.getValue();
        } else {
            initialDate = new Date();
        }
        return displayFormat.format(initialDate);
    }

    /**
     * @param fieldValue
     *            the data field value
     * @return a List<String>
     */
    @SuppressWarnings("unchecked")
    protected List<String> getTableValue(final FormFieldValue fieldValue) {
        List<String> valueList = null;
        try {
            valueList = (List<String>) fieldValue.getValue();
        } catch (final ClassCastException firstAttemptException) {
            try {
                final Collection<String> tableValue = (Collection<String>) fieldValue.getValue();
                valueList = new ArrayList<String>(tableValue);
            } catch (final ClassCastException secondAttemptException) {
                Window.alert("the initial value for table " + widgetId + " should be a Collection<String>.");
            }
        }
        return valueList;
    }

    /**
     * @param fieldValue
     *            the data field value
     * @return a List of List<String>
     */
    @SuppressWarnings("unchecked")
    protected List<List<String>> getGridValue(final FormFieldValue fieldValue) {
        List<List<String>> valueList = null;
        try {
            valueList = (List<List<String>>) fieldValue.getValue();
        } catch (final Exception firstAttemptException) {
            try {
                valueList = new ArrayList<List<String>>();
                final Collection<Collection<String>> tableValue = (Collection<Collection<String>>) fieldValue.getValue();
                for (final Collection rowValue : tableValue) {
                    valueList.add(new ArrayList<String>(rowValue));
                }
            } catch (final Exception secondAttemptException) {
                Window.alert("the initial value for grid " + widgetId + " should be a Collection<Collection<String>>.");
            }
        }
        return valueList;
    }

    /**
     * Get the string display value for a date
     * 
     * @param text
     *            the widget data object
     * @return the date as a String
     */
    protected Date getTextAsDate(final String text) {

        final DateTimeFormat initialFormat = DateTimeFormat.getFormat(displayFormatPattern);
        return initialFormat.parse(text);
    }

    /**
     * Get the string value of a {@link FormFieldValue}
     * 
     * @param fieldValue
     *            the {@link FormFieldValue}
     * @return a String
     */
    protected String getStringValue(final FormFieldValue fieldValue) {
        String value = null;
        if (SupportedFieldTypes.JAVA_STRING_CLASSNAME.equals(fieldValue.getValueType())) {
            value = (String) fieldValue.getValue();
        } else if (fieldValue.getValue() != null) {
            value = fieldValue.getValue().toString();
        }
        return value;
    }

    /**
     * Get the Collection<String> value of a {@link FormFieldValue}
     * 
     * @param fieldValue
     *            the {@link FormFieldValue}
     * @return a Collection<String>
     */
    @SuppressWarnings("unchecked")
    protected Collection<String> getStringValues(final FormFieldValue fieldValue) {
        Collection<String> value = null;
        try {
            value = (Collection<String>) fieldValue.getValue();
        } catch (final Exception e) {
            Window.alert("the initial value for a checkbox or radiobutton group should be a Collection<String>.");
            value = new HashSet<String>();
        }
        return value;
    }

    /**
     * add generic features to the field widgets
     * 
     * @param fieldWidget
     *            the GWT widget
     * @param widgetData
     *            the widget data object
     */
    protected void addStandardWidgetFeatures(final Widget fieldWidget, final ReducedFormWidget widgetData) {
        if (isStaticField()) {
            if (widgetType.equals(WidgetType.IMAGE)) {
                fieldWidget.setStyleName("bonita_form_image_container");
            } else {
                fieldWidget.setStyleName("bonita_form_text");
            }
        } else {
            fieldWidget.setStyleName("bonita_form_field");
            if (widgetType.equals(WidgetType.DATE)) {
                fieldWidget.addStyleName("bonita_form_date");
            }
        }
        if (widgetData.getTitle() != null && widgetData.getTitle().length() > 0) {
            fieldWidget.setTitle(widgetData.getTitle());
        }
        insertWidgetInPanel(fieldWidget, widgetData);
    }

    /**
     * Insert the generated widget in the panel and set the fieldWidget attribute to keep a reference to it Set the HTML
     * attributes before inserting the generated widget in the panel.
     * 
     * @param fieldWidget
     *            the field widget
     * @param widgetData
     *            the widget data object
     */
    protected void insertWidgetInPanel(final Widget fieldWidget, final ReducedFormWidget widgetData) {
        final ElementAttributeSupport elementAttributeSupport = new ElementAttributeSupport();
        elementAttributeSupport.addHtmlAttributes(fieldWidget, widgetData.getHtmlAttributes());
        fieldContainer.add(fieldWidget);
    }

    /**
     * Retrieve the value of the field under the form of a {@link FormFieldValue} object. This conversion is needed because RPC
     * calls do not support the type 'Object'.
     * 
     * @return a {@link FormFieldValue} object
     */
    @SuppressWarnings("unchecked")
    public FormFieldValue getValue() {

        long attachmentId = -1;
        String attachmentName = null;
        Serializable value = null;
        String valueType = null;
        String format = null;
        String displayedValue = null;
        switch (widgetType) {
            case TEXTBOX:
                final TextBox textBox = (TextBox) fieldWidget;
                value = textBox.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case TEXTAREA:
                final TextArea textArea = (TextArea) fieldWidget;
                value = textArea.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case TEXT:
                final HTML text = (HTML) fieldWidget;
                value = text.getHTML();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case RICH_TEXTAREA:
                final RichTextWidget richTextWidget = (RichTextWidget) fieldWidget;
                value = richTextWidget.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case PASSWORD:
                final PasswordTextBox passwordTextBox = (PasswordTextBox) fieldWidget;
                value = passwordTextBox.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case CHECKBOX:
                final CheckBox checkBox = (CheckBox) fieldWidget;
                value = checkBox.getValue();
                valueType = SupportedFieldTypes.JAVA_BOOLEAN_CLASSNAME;
                break;
            case RADIOBUTTON_GROUP:
                final RadioButtonGroupWidget radioButtonGroupWidget = (RadioButtonGroupWidget) fieldWidget;
                value = radioButtonGroupWidget.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case LISTBOX_SIMPLE:
                final ListBox listBox = (ListBox) fieldWidget;
                final int index = listBox.getSelectedIndex();
                if (index > -1) {
                    value = listBox.getValue(index);
                }
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case LISTBOX_MULTIPLE:
                value = new ArrayList<String>();
                final ListBox listBoxMulti = (ListBox) fieldWidget;
                for (int i = 0; i < listBoxMulti.getItemCount(); i++) {
                    if (listBoxMulti.isItemSelected(i)) {
                        ((List<String>) value).add(listBoxMulti.getValue(i));
                    }
                }
                valueType = SupportedFieldTypes.JAVA_COLLECTION_CLASSNAME;
                break;
            case SUGGESTBOX:
                final SuggestBox suggestBox = (SuggestBox) fieldWidget;
                displayedValue = suggestBox.getValue();
                value = suggestionsMap.get(displayedValue);
                if (value == null) {
                    value = displayedValue;
                }
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case SUGGESTBOX_ASYNC:
                final AsyncSuggestBoxWidget formAsyncSuggestBoxWidget = (AsyncSuggestBoxWidget) fieldWidget;
                value = formAsyncSuggestBoxWidget.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            case CHECKBOX_GROUP:
                value = new ArrayList<String>();
                final CheckboxGroupWidget checkboxGroupWidget = (CheckboxGroupWidget) fieldWidget;
                for (final String checkboxGroupValue : checkboxGroupWidget.getValue()) {
                    ((List<String>) value).add(checkboxGroupValue);
                }
                valueType = SupportedFieldTypes.JAVA_COLLECTION_CLASSNAME;
                break;
            case DATE:
                final DateBox dateBox = (DateBox) fieldWidget;
                final String strValue = dateBox.getTextBox().getValue();
                final Date dtValue = dateBox.getValue();
                if (strValue != null && strValue.length() > 0 && dtValue == null) {
                    value = strValue;
                    valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                } else {
                    value = dtValue;
                    valueType = SupportedFieldTypes.JAVA_DATE_CLASSNAME;
                }
                break;
            case DURATION:
                final DurationWidget duration = (DurationWidget) fieldWidget;
                value = duration.getValue();
                valueType = SupportedFieldTypes.JAVA_LONG_CLASSNAME;
                break;
            case FILEUPLOAD:
                final FileUploadWidget fileUpload = (FileUploadWidget) fieldWidget;
                attachmentName = fileUpload.getAttachmentName();
                attachmentId = fileUpload.getAttachmentId();
                value = fileUpload.getValue();
                displayedValue = fileUpload.getDisplayedValue();
                valueType = fileUpload.getValueType();
                break;
            case TABLE:
                final TableWidget table = (TableWidget) fieldWidget;
                value = (Serializable) table.getValue();
                valueType = SupportedFieldTypes.JAVA_COLLECTION_CLASSNAME;
                break;
            case EDITABLE_GRID:
                final EditableGridWidget grid = (EditableGridWidget) fieldWidget;
                value = (Serializable) grid.getValue();
                valueType = SupportedFieldTypes.JAVA_COLLECTION_CLASSNAME;
                break;
            case HIDDEN:
                final Hidden hidden = (Hidden) fieldWidget;
                value = hidden.getValue();
                valueType = SupportedFieldTypes.JAVA_STRING_CLASSNAME;
                break;
            default:
                break;
        }
        if (displayFormat != null) {
            format = displayFormat.getPattern();
        } else {
            format = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG).getPattern();
        }
        if (WidgetType.FILEUPLOAD.equals(widgetType)) {
            final FormFieldValue formFieldValue = new FormFieldValue(value, valueType, format);
            formFieldValue.setDocumentName(attachmentName);
            formFieldValue.setDocumentId(attachmentId);
            formFieldValue.setDisplayedValue(displayedValue);
            formFieldValue.setDocument(true);
            return formFieldValue;
        }
        return new FormFieldValue(value, valueType, format, fieldOutputType);
    }

    /**
     * @return Return the type of the widget
     */
    public WidgetType getType() {
        return widgetType;
    }

    /**
     * Set the value of the widget
     * 
     * @param fieldValue
     */
    public void setValue(final FormFieldValue fieldValue) {
        if (fieldValue.getValue() != null) {
            Collection<String> values = null;
            boolean fireEvents = true;
            switch (widgetType) {
                case TEXTBOX:
                    final TextBox textBox = (TextBox) fieldWidget;
                    if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(fieldValue.getValueType())) {
                        textBox.setValue(getDateAsText(fieldValue), fireEvents);
                    } else {
                        textBox.setValue(getStringValue(fieldValue), fireEvents);
                    }
                    break;
                case TEXTAREA:
                    final TextArea textArea = (TextArea) fieldWidget;
                    if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(fieldValue.getValueType())) {
                        textArea.setValue(getDateAsText(fieldValue), fireEvents);
                    } else {
                        textArea.setValue(getStringValue(fieldValue), fireEvents);
                    }
                    break;
                case RICH_TEXTAREA:
                    final RichTextWidget richTextWidget = (RichTextWidget) fieldWidget;
                    if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(fieldValue.getValueType())) {
                        richTextWidget.setValue(getDateAsText(fieldValue));
                    } else {
                        richTextWidget.setValue(getStringValue(fieldValue));
                    }
                    break;
                case TEXT:
                    final HTML text = (HTML) fieldWidget;
                    if (text.getHTML() != null && text.getHTML().equals(fieldValue.getValue()) || fieldValue.getValue() != null
                            && fieldValue.getValue().equals(text.getHTML())) {
                        fireEvents = false;
                    }
                    if (SupportedFieldTypes.JAVA_DATE_CLASSNAME.equals(fieldValue.getValueType())) {
                        text.setText(getDateAsText(fieldValue));
                    } else {
                        text.setHTML(getStringValue(fieldValue));
                    }
                    if (fireEvents) {
                        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), text);
                    }
                    break;
                case PASSWORD:
                    final PasswordTextBox passwordTextBox = (PasswordTextBox) fieldWidget;
                    passwordTextBox.setValue(getStringValue(fieldValue), fireEvents);
                    break;
                case CHECKBOX:
                    final CheckBox checkBox = (CheckBox) fieldWidget;
                    try {
                        checkBox.setValue((Boolean) fieldValue.getValue(), fireEvents);
                    } catch (final Exception e) {
                        checkBox.setValue(false, fireEvents);
                        Window.alert("initial value for checkbox " + widgetId + " should be a Boolean.");
                    }
                    break;
                case RADIOBUTTON_GROUP:
                    final RadioButtonGroupWidget radioButtonGroupWidget = (RadioButtonGroupWidget) fieldWidget;
                    radioButtonGroupWidget.setValue(getStringValue(fieldValue), fireEvents);
                    break;
                case LISTBOX_SIMPLE:
                    final ListBox listBox = (ListBox) fieldWidget;
                    final int index = listBox.getSelectedIndex();
                    if (index < 0) {
                        fireEvents = false;
                    } else {
                        if (listBox.getValue(index).equals(getStringValue(fieldValue))) {
                            fireEvents = false;
                        }
                    }
                    for (int i = 0; i < listBox.getItemCount(); i++) {
                        if (listBox.getItemText(i).equals(getStringValue(fieldValue))) {
                            listBox.setItemSelected(i, true);
                            break;
                        }
                    }
                    if (fireEvents) {
                        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
                    }
                    break;
                case LISTBOX_MULTIPLE:
                    final ListBox listBoxMulti = (ListBox) fieldWidget;
                    values = getStringValues(fieldValue);
                    boolean valueChanged = false;
                    for (int i = 0; i < listBoxMulti.getItemCount(); i++) {
                        if (values != null && values.contains(listBoxMulti.getItemText(i))) {
                            if (!listBoxMulti.isItemSelected(i)) {
                                valueChanged = true;
                                listBoxMulti.setItemSelected(i, true);
                            }
                        } else {
                            if (listBoxMulti.isItemSelected(i)) {
                                valueChanged = true;
                                listBoxMulti.setItemSelected(i, false);
                            }
                        }
                    }
                    if (fireEvents && valueChanged) {
                        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBoxMulti);
                    }
                    break;
                case SUGGESTBOX:
                    final SuggestBox suggestBox = (SuggestBox) fieldWidget;
                    final String stringValue = getStringValue(fieldValue);
                    String labelValue = null;
                    for (final Entry<String, String> suggestionEntry : suggestionsMap.entrySet()) {
                        if (suggestionEntry.getValue() != null && suggestionEntry.getValue().equals(stringValue)) {
                            labelValue = suggestionEntry.getKey();
                        }
                    }
                    if (labelValue != null) {
                        suggestBox.setValue(labelValue, fireEvents);
                    } else {
                        suggestBox.setValue(stringValue, fireEvents);
                    }
                    break;
                case SUGGESTBOX_ASYNC:
                    final AsyncSuggestBoxWidget formAsyncSuggestBoxWidget = (AsyncSuggestBoxWidget) fieldWidget;
                    formAsyncSuggestBoxWidget.setValue(getStringValue(fieldValue), fireEvents);
                    break;
                case CHECKBOX_GROUP:
                    final CheckboxGroupWidget checkboxGroupWidget = (CheckboxGroupWidget) fieldWidget;
                    values = getStringValues(fieldValue);
                    checkboxGroupWidget.setValue(values, fireEvents);
                    break;
                case DATE:
                    final DateBox dateBox = (DateBox) fieldWidget;
                    try {
                        dateBox.setValue((Date) fieldValue.getValue(), fireEvents);
                    } catch (final Exception e) {
                        dateBox.setValue(null, fireEvents);
                        Window.alert("initial value for date " + widgetId + " should be a Date.");
                    }
                    break;
                case DURATION:
                    final DurationWidget durationWidget = (DurationWidget) fieldWidget;
                    Long duration = null;
                    try {
                        duration = (Long) fieldValue.getValue();
                    } catch (final Exception e) {
                        duration = 0L;
                        Window.alert("The initial value for duration widget " + widgetId + " should be a Long.");
                    }
                    durationWidget.setValue(duration, fireEvents);
                    break;
                case IMAGE:
                    final ImageWidget image = (ImageWidget) fieldWidget;
                    image.setValue(fieldValue.getDocumentId(), getStringValue(fieldValue), fireEvents);
                    break;
                case TABLE:
                    final TableWidget table = (TableWidget) fieldWidget;
                    table.setValue(getTableValue(fieldValue), fireEvents);
                    if (fireEvents) {
                        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), table);
                    }
                    break;
                case EDITABLE_GRID:
                    final EditableGridWidget grid = (EditableGridWidget) fieldWidget;
                    grid.setValue(getGridValue(fieldValue));
                    if (fireEvents) {
                        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), grid);
                    }
                    break;
                case HIDDEN:
                    final Hidden hidden = (Hidden) fieldWidget;
                    if (hidden.getValue() != null && hidden.getValue().equals(fieldValue.getValue()) || fieldValue.getValue() != null
                            && fieldValue.getValue().equals(hidden.getValue())) {
                        fireEvents = false;
                    }
                    hidden.setValue(getStringValue(fieldValue));
                    if (fireEvents) {
                        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), hidden);
                    }
                    break;
                default:
                    Window.alert("The modification of the value of a " + widgetType + " widget is not supported.");
                    break;
            }
        }
    }

    /**
     * Set the available values of the widget (for list widgets only)
     * 
     * @param availableValues
     */
    public void setAvailableValues(final List<ReducedFormFieldAvailableValue> availableValues) {
        final boolean fireEvents = true;
        switch (widgetType) {
            case RADIOBUTTON_GROUP:
                final RadioButtonGroupWidget radioButtonGroupWidget = (RadioButtonGroupWidget) fieldWidget;
                radioButtonGroupWidget.setAvailableValues(availableValues, fireEvents);
                break;
            case LISTBOX_SIMPLE:
                final ListBox listBox = (ListBox) fieldWidget;
                listBox.clear();
                for (final ReducedFormFieldAvailableValue availableValue : availableValues) {
                    listBox.addItem(availableValue.getLabel(), availableValue.getValue());
                }
                if (fireEvents) {
                    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBox);
                }
                break;
            case LISTBOX_MULTIPLE:
                final ListBox listBoxMulti = (ListBox) fieldWidget;
                listBoxMulti.clear();
                for (final ReducedFormFieldAvailableValue availableValue : availableValues) {
                    listBoxMulti.addItem(availableValue.getLabel(), availableValue.getValue());
                }
                if (fireEvents) {
                    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), listBoxMulti);
                }
                break;
            case SUGGESTBOX:
                final SuggestBox suggestBox = (SuggestBox) fieldWidget;
                final MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) suggestBox.getSuggestOracle();
                oracle.clear();
                suggestionsMap.clear();
                for (final ReducedFormFieldAvailableValue availableValue : availableValues) {
                    suggestionsMap.put(availableValue.getLabel(), availableValue.getValue());
                    oracle.add(availableValue.getLabel());
                }
                if (fireEvents) {
                    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), suggestBox);
                }
                break;
            case SUGGESTBOX_ASYNC:
                final AsyncSuggestBoxWidget formAsyncSuggestBoxWidget = (AsyncSuggestBoxWidget) fieldWidget;
                formAsyncSuggestBoxWidget.setAvailableValues(availableValues, fireEvents);
                break;
            case CHECKBOX_GROUP:
                final CheckboxGroupWidget checkboxGroupWidget = (CheckboxGroupWidget) fieldWidget;
                checkboxGroupWidget.setAvailableValues(availableValues, fireEvents);
                break;
            default:
                Window.alert("The modification of the available values of a " + widgetType + " widget is not supported.");
                break;
        }
    }

    /**
     * Set the available values of the widget (for table widgets only)
     * 
     * @param newTableWidgetAvailableValues
     */
    public void setTableAvailableValues(final List<List<ReducedFormFieldAvailableValue>> newTableWidgetAvailableValues) {
        final boolean fireEvents = true;
        if (WidgetType.TABLE.equals(widgetType)) {
            final TableWidget formTableWidget = (TableWidget) fieldWidget;
            formTableWidget.setAvailableValues(newTableWidgetAvailableValues, fireEvents);
        } else {
            Window.alert("The modification of the table available values of a " + widgetType + " widget is not supported.");
        }
    }

    /**
     * set the mandatory label
     * 
     * @param mandatoryLabel
     */
    public void setMandatoryLabel(final String mandatoryLabel) {
        labelWidget.setMandatoryText(mandatoryLabel);
    }

    /**
     * Set the focus on this widget if it's focusable
     */
    public void setFocusOn() {

        if (fieldWidget instanceof Focusable) {
            ((Focusable) fieldWidget).setFocus(true);
        }
    }

    /**
     * @return the ID of the widget
     */
    public String getId() {
        return widgetId;
    }

    /**
     * @return the field widget
     */
    public Widget getFieldWidget() {
        return fieldWidget;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
    @SuppressWarnings("unchecked")
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler valueChangeHandler) {
        if (valueChangeHandlers == null) {
            valueChangeHandlers = new ArrayList<ValueChangeHandler<Serializable>>();
        }
        valueChangeHandlers.add(valueChangeHandler);
        return new EventHandlerRegistration(valueChangeHandler);
    }

    @Override
    public HandlerRegistration addSelectionHandler(final SelectionHandler<Suggestion> selectionHandler) {
        if (selectionHandlers == null) {
            selectionHandlers = new ArrayList<SelectionHandler<Suggestion>>();
        }
        selectionHandlers.add(selectionHandler);
        return new EventHandlerRegistration(selectionHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onChange(final ChangeEvent changeEvent) {
        if (changeHandlers != null) {
            for (final ChangeHandler changeHandler : changeHandlers) {
                changeHandler.onChange(changeEvent);
            }
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
    @SuppressWarnings("unchecked")
    public void onValueChange(final ValueChangeEvent valueChangeEvent) {
        if (valueChangeHandlers != null) {
            for (final ValueChangeHandler valueChangeHandler : valueChangeHandlers) {
                valueChangeHandler.onValueChange(valueChangeEvent);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSelection(final SelectionEvent<Suggestion> selectionEvent) {
        if (selectionHandlers != null) {
            for (final SelectionHandler<Suggestion> selectionHandler : selectionHandlers) {
                selectionHandler.onSelection(selectionEvent);
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
            } else if (eventHandler instanceof ChangeHandler) {
                changeHandlers.remove(eventHandler);
            } else if (eventHandler instanceof ValueChangeHandler) {
                valueChangeHandlers.remove(eventHandler);
            } else if (eventHandler instanceof SelectionHandler) {
                selectionHandlers.remove(eventHandler);
            }
        }
    }

}
