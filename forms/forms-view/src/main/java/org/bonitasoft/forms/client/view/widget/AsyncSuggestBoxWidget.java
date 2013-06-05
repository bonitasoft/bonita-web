/**
 * Copyright (C) 2011 BonitaSoft S.A.
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

import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.model.FormFieldValue;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.model.ReducedFormWidget;
import org.bonitasoft.forms.client.model.WidgetType;
import org.bonitasoft.forms.client.rpc.FormsServiceAsync;
import org.bonitasoft.forms.client.view.FormsAsyncCallback;
import org.bonitasoft.forms.client.view.SupportedFieldTypes;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestBox.DefaultSuggestionDisplay;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Generic form flow AsyncSuggestBox widget
 * 
 * @author qixiang.zhang
 */
public class AsyncSuggestBoxWidget extends Composite {

    /**
     * The default refresh delay
     */
    protected static final int DEFAULT_REFRESH_DELAY = 1000;

    /**
     * the flow panel used to display the widget
     */
    protected FlowPanel flowPanel;

    /**
     * widget field type
     */
    protected WidgetType widgetType;

    /**
     * SuggestBox widget
     */
    protected SuggestBox asyncSuggestBox;

    /**
     * The form ID
     */
    protected String formID;

    /**
     * The URL context
     */
    protected Map<String, Object> contextMap;

    /**
     * Form Widget
     */
    protected ReducedFormWidget formWidget;

    /**
     * old asyncSuggestBox value
     */
    protected String oldValue;

    /**
     * Default constructor.
     * 
     * @param formAsyncSuggestBoxData
     *            the Suggestbox data
     * @param formID
     * @param contextMap
     * @param pageId
     * @param isCurrentValue
     * @param fieldValue
     *            the field's available value
     */
    public AsyncSuggestBoxWidget(final ReducedFormWidget formAsyncSuggestBoxData, final String formID, final Map<String, Object> contextMap,
            final FormFieldValue fieldValue) {
        this.formID = formID;
        this.contextMap = contextMap;
        this.formWidget = formAsyncSuggestBoxData;
        this.flowPanel = new FlowPanel();

        createWidget(formAsyncSuggestBoxData, fieldValue);

        initWidget(this.flowPanel);
    }

    /**
     * Create AsyncSuggestBox
     * 
     * @param formAsyncSuggestBoxData
     * @param fieldValue
     */
    protected void createWidget(final ReducedFormWidget formAsyncSuggestBoxData, final FormFieldValue fieldValue) {
        this.widgetType = formAsyncSuggestBoxData.getType();
        final TextBox textBox = new TextBox();
        textBox.setReadOnly(formAsyncSuggestBoxData.isReadOnly());
        final DefaultSuggestionDisplay suggestionDisplay = new DefaultSuggestionDisplay();
        final String popupStyle = formAsyncSuggestBoxData.getItemsStyle();
        if (popupStyle != null && popupStyle.length() > 0) {
            suggestionDisplay.setPopupStyleName(popupStyle);
        }
        final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
        for (final ReducedFormFieldAvailableValue availableValue : formAsyncSuggestBoxData.getAvailableValues()) {
            oracle.add(availableValue.getValue());
        }
        this.asyncSuggestBox = new SuggestBox(oracle, textBox, suggestionDisplay);
        int refreshDelay = formAsyncSuggestBoxData.getDelayMillis();
        if (refreshDelay <= 0) {
            refreshDelay = DEFAULT_REFRESH_DELAY;
        }
        this.asyncSuggestBox.addKeyUpHandler(new AsyncSuggestBoxKeyUpHandler(refreshDelay));
        this.asyncSuggestBox.addKeyPressHandler(new AsyncSuggestBoxKeyPressHandler());
        this.asyncSuggestBox.setValue(getStringValue(fieldValue));
        if (formAsyncSuggestBoxData.getMaxItems() > 0) {
            this.asyncSuggestBox.setLimit(formAsyncSuggestBoxData.getMaxItems() - 1);
        }
        this.flowPanel.add(this.asyncSuggestBox);
    }

    /**
     * @return AsyncSuggestBox type
     */
    public WidgetType getWidgetType() {
        return this.widgetType;
    }

    /**
     * @return the suggest box widget
     */
    public SuggestBox getAsyncSuggestBox() {
        return this.asyncSuggestBox;
    }

    /**
     * @return async SuggestBox of value
     */
    public String getValue() {
        return this.asyncSuggestBox.getValue();
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
     * Set the value of the AsyncSuggestBox
     * 
     * @param fieldValue
     */
    public void setValue(final String value, final boolean fireEvents) {
        this.asyncSuggestBox.setValue(value, fireEvents);
    }

    /**
     * Set the available values of the widget (for list widgets only)
     * 
     * @param availableValues
     */
    public void setAvailableValues(final List<ReducedFormFieldAvailableValue> availableValues, final boolean fireEvents) {

        final MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) this.asyncSuggestBox.getSuggestOracle();
        oracle.clear();
        for (final ReducedFormFieldAvailableValue availableValue : availableValues) {
            oracle.add(availableValue.getValue());
        }
        if (fireEvents) {
            DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this.asyncSuggestBox);
        }
    }

    /**
     * AsyncSuggestBox KeyPressHandler
     * 
     */
    protected class AsyncSuggestBoxKeyPressHandler implements KeyPressHandler {

        /**
         * {@inheritDoc}
         */
        @Override
        public void onKeyPress(final KeyPressEvent event) {
            AsyncSuggestBoxWidget.this.oldValue = AsyncSuggestBoxWidget.this.asyncSuggestBox.getText();
        }

    }

    /**
     * AsyncSuggestBox KeyUpHandler
     * 
     */
    protected class AsyncSuggestBoxKeyUpHandler implements KeyUpHandler {

        /**
         * DelayMillis to call RPC
         */
        int delayMillis;

        /**
         * Async Suggest Box of value
         */
        String content;

        /**
         * Default constructor.
         * 
         * @param delayMillis
         */
        public AsyncSuggestBoxKeyUpHandler(final int delayMillis) {
            this.delayMillis = delayMillis;
        }

        Timer timer = new Timer() {

            @Override
            public void run() {

                final FormsServiceAsync formsServiceAsync = RpcFormsServices.getFormsService();
                final FormFieldValue currentFieldValue = new FormFieldValue(AsyncSuggestBoxKeyUpHandler.this.content, String.class.getName());
                // RPC Call
                formsServiceAsync.getFormAsyncAvailableValues(AsyncSuggestBoxWidget.this.formID, AsyncSuggestBoxWidget.this.contextMap,
                        AsyncSuggestBoxWidget.this.formWidget, currentFieldValue, new GetAsyncAvailableValuesHandler());
            }

        };

        /**
         * {@inheritDoc}
         */
        @Override
        public void onKeyUp(final KeyUpEvent event) {
            if (!event.isUpArrow() && !event.isDownArrow()) {
                ((DefaultSuggestionDisplay) AsyncSuggestBoxWidget.this.asyncSuggestBox.getSuggestionDisplay()).hideSuggestions();
            }
            this.timer.cancel();
            this.content = AsyncSuggestBoxWidget.this.asyncSuggestBox.getText();
            if (this.content.trim().length() > 0 && !this.content.equals(AsyncSuggestBoxWidget.this.oldValue)) {
                this.timer.schedule(this.delayMillis);
            }

        }

    }

    /**
     * Handler for available values update
     * 
     */
    protected class GetAsyncAvailableValuesHandler extends FormsAsyncCallback<List<ReducedFormFieldAvailableValue>> {

        @Override
        public void onSuccess(final List<ReducedFormFieldAvailableValue> result) {
            final MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) AsyncSuggestBoxWidget.this.asyncSuggestBox.getSuggestOracle();
            oracle.clear();
            for (final ReducedFormFieldAvailableValue resultValue : result) {
                oracle.add(resultValue.getValue());
            }
            AsyncSuggestBoxWidget.this.asyncSuggestBox.showSuggestionList();
        }

        @Override
        public void onUnhandledFailure(Throwable caught) {
            final MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) AsyncSuggestBoxWidget.this.asyncSuggestBox.getSuggestOracle();
            oracle.clear();
            GWT.log("Async SuggestBox KeyUpHandler", caught);
        }
    }

}
