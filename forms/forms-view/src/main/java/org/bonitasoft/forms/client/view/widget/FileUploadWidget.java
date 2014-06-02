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
import java.util.List;
import java.util.Map;

import org.bonitasoft.forms.client.i18n.FormsResourceBundle;
import org.bonitasoft.forms.client.model.FileWidgetInputType;
import org.bonitasoft.forms.client.model.ReducedFormFieldAvailableValue;
import org.bonitasoft.forms.client.view.SupportedFieldTypes;
import org.bonitasoft.forms.client.view.common.DOMUtils;
import org.bonitasoft.forms.client.view.common.RpcFormsServices;

import com.google.gwt.dom.client.FormElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Widget displaying either a file upload input if its initial value is null or a download link and file upload input to overide the current file
 * 
 * @author Anthony Birembaut
 */
public class FileUploadWidget extends Composite implements ValueChangeHandler<Boolean> {

    /**
     * The URL document type
     */
    public static final String URL_DOCUMENT_TYPE = "URL";

    /**
     * The file document type
     */
    public static final String FILE_DOCUMENT_TYPE = "file";

    /**
     * the flow panel used to display the widgets
     */
    protected FlowPanel flowPanel;

    /**
     * the form panel used to display the widget
     */
    protected FormPanel formPanel;

    protected FlowPanel buttonPanel;

    protected Label cancelLabel;

    protected Label modifyLabel;

    protected Label removeLabel;

    protected FileDownloadWidget fileDownloadWidget;

    protected FileUpload fileUpload;

    protected Image loadingImage;

    protected String uploadedFilePath;

    protected String fileUploadFormName;

    protected long attachmentId;

    protected String attachmentName;

    protected FlowPanel filePanel;

    protected TextBox urlTextBox;

    protected RadioButtonGroupWidget radioButtonGroupWidget;

    protected FileWidgetInputType fileWidgetInputType;

    /**
     * Constructor
     * 
     * @param contextMap
     * @param fieldId
     * @param fileWidgetInputType
     * @param valueType
     * @param attachmentId
     * @param attachmentName
     * @param value
     * @param hasImagePreview
     * @param isElementOfMultipleWidget
     */
    public FileUploadWidget(final String formID, final Map<String, Object> contextMap, final String fieldId, final FileWidgetInputType fileWidgetInputType,
            final String valueType, final long attachmentId, final String attachmentName, final String value, final boolean hasImagePreview) {

        this.fileWidgetInputType = fileWidgetInputType;
        this.attachmentId = attachmentId;
        this.attachmentName = attachmentName;
        if (attachmentName != null) {
            fileUploadFormName = attachmentName;
        } else {
            fileUploadFormName = fieldId;
        }

        flowPanel = new FlowPanel();

        if (FileWidgetInputType.ALL.equals(this.fileWidgetInputType)) {

            final List<ReducedFormFieldAvailableValue> availableValues = new ArrayList<ReducedFormFieldAvailableValue>();
            // FIXME i18n
            availableValues.add(new ReducedFormFieldAvailableValue("URL", URL_DOCUMENT_TYPE));
            availableValues.add(new ReducedFormFieldAvailableValue("File", FILE_DOCUMENT_TYPE));
            final String initialRadioButton;
            if (SupportedFieldTypes.JAVA_FILE_CLASSNAME.equals(valueType)) {
                initialRadioButton = FILE_DOCUMENT_TYPE;
            } else {
                initialRadioButton = URL_DOCUMENT_TYPE;
            }
            radioButtonGroupWidget = new RadioButtonGroupWidget(fieldId + "_document_type", availableValues, initialRadioButton, "bonita_form_radio_inline",
                    false, true);
            radioButtonGroupWidget.addValueChangeHandler(this);
            flowPanel.add(radioButtonGroupWidget);
            final FlowPanel clearFloatPanel = new FlowPanel();
            clearFloatPanel.setStyleName("bonita_clear_float");
            flowPanel.add(clearFloatPanel);
        }

        if (!FileWidgetInputType.URL.equals(fileWidgetInputType)) {
            createFileUploadForm(fileUploadFormName);

            filePanel = new FlowPanel();

            fileDownloadWidget = new FileDownloadWidget(formID, contextMap, valueType, attachmentId, hasImagePreview);

            loadingImage = new Image("themeResource?theme=portal&location=images/ajax-loader.gif");
            loadingImage.setTitle(FormsResourceBundle.getMessages().uploadingLabel());

            buttonPanel = new FlowPanel();
            cancelLabel = new Label();
            cancelLabel.setText(FormsResourceBundle.getMessages().cancelButtonLabel());
            cancelLabel.setTitle(FormsResourceBundle.getMessages().cancelButtonTitle());
            cancelLabel.setStyleName("bonita_upload_button");
            modifyLabel = new Label();
            modifyLabel.setText(FormsResourceBundle.getMessages().modifyButtonLabel());
            modifyLabel.setTitle(FormsResourceBundle.getMessages().modifyButtonTitle());
            modifyLabel.setStyleName("bonita_upload_button");
            removeLabel = new Label();
            removeLabel.setText(FormsResourceBundle.getMessages().removeButtonLabel());
            removeLabel.setTitle(FormsResourceBundle.getMessages().removeButtonTitle());
            removeLabel.setStyleName("bonita_upload_button");

            buttonPanel.add(cancelLabel);
            buttonPanel.add(modifyLabel);
            buttonPanel.add(removeLabel);

            buttonPanel.addStyleName("bonita_upload_button_group");

            loadingImage.setVisible(false);
            if (value != null && SupportedFieldTypes.JAVA_FILE_CLASSNAME.equals(valueType)) {
                fileDownloadWidget.setFileName(value);
                formPanel.setVisible(false);
            } else {
                fileDownloadWidget.setVisible(false);
                modifyLabel.setVisible(false);
                removeLabel.setVisible(false);
            }
            cancelLabel.setVisible(false);

            filePanel.add(formPanel);
            filePanel.add(loadingImage);
            filePanel.add(fileDownloadWidget);
            filePanel.add(buttonPanel);
            modifyLabel.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    formPanel.setVisible(true);
                    fileDownloadWidget.setVisible(false);
                    modifyLabel.setVisible(false);
                    removeLabel.setVisible(false);
                    cancelLabel.setVisible(true);
                }
            });

            removeLabel.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    formPanel.clear();
                    uploadedFilePath = null;
                    fileUpload = addFileUploalToFormPanel(fileUploadFormName);
                    formPanel.setVisible(true);
                    fileDownloadWidget.setVisible(false);
                    fileDownloadWidget.resetDownloadlink();
                    modifyLabel.setVisible(false);
                    removeLabel.setVisible(false);
                    cancelLabel.setVisible(false);
                }
            });

            cancelLabel.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(final ClickEvent event) {
                    formPanel.setVisible(false);
                    fileDownloadWidget.setVisible(true);
                    modifyLabel.setVisible(true);
                    removeLabel.setVisible(true);
                    cancelLabel.setVisible(false);
                }
            });
            flowPanel.add(filePanel);
        }

        if (!FileWidgetInputType.FILE.equals(fileWidgetInputType)) {
            urlTextBox = new TextBox();
            if (value != null && SupportedFieldTypes.JAVA_STRING_CLASSNAME.equals(valueType)) {
                urlTextBox.setValue(value);
            }
            flowPanel.add(urlTextBox);
        }

        if (radioButtonGroupWidget != null) {
            if (FILE_DOCUMENT_TYPE.equals(radioButtonGroupWidget.getValue())) {
                urlTextBox.setVisible(false);
                filePanel.setVisible(true);
            } else {
                filePanel.setVisible(false);
                urlTextBox.setVisible(true);
            }
        }

        initWidget(flowPanel);
    }

    protected void createFileUploadForm(final String FileUloadName) {

        formPanel = new FormPanel();
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPanel.setMethod(FormPanel.METHOD_POST);
        FormElement.as(formPanel.getElement()).setAcceptCharset("UTF-8");
        formPanel.setAction(RpcFormsServices.getFileUploadURL());
        fileUpload = addFileUploalToFormPanel(FileUloadName);
    }

    protected FileUpload addFileUploalToFormPanel(final String fileUploadName) {

        final FileUpload fileUpload = new FileUpload();
        fileUpload.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                formPanel.submit();
            }
        });
        fileUpload.setStyleName("bonita_file_upload");
        // mandatory because we are in a true form with a post action
        fileUpload.setName(fileUploadName);
        if (DOMUtils.getInstance().isIE8()) {
            fileUpload.getElement().setPropertyString("contentEditable", "false");
        }
        formPanel.add(fileUpload);
        final UploadSubmitHandler uploadHandler = new UploadSubmitHandler();
        formPanel.addSubmitHandler(uploadHandler);
        formPanel.addSubmitCompleteHandler(uploadHandler);

        return fileUpload;
    }

    protected class UploadSubmitHandler implements SubmitHandler, SubmitCompleteHandler {

        protected String filePath;

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSubmit(final SubmitEvent event) {
            filePath = fileUpload.getFilename();
            if (filePath == null || filePath.length() == 0) {
                event.cancel();
            } else {
                formPanel.setVisible(false);
                loadingImage.setVisible(true);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSubmitComplete(final SubmitCompleteEvent event) {
            String response = event.getResults();
            response = URL.decodeQueryString(response);
            response = response.replaceAll("&amp;", "&");
            response = response.replaceAll("&lt;", "<");
            response = response.replaceAll("&gt;", ">");
            uploadedFilePath = response;
            String realFileName = null;
            if (filePath.contains("\\")) {
                realFileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            } else if (filePath.contains("/")) {
                realFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            } else {
                realFileName = filePath;
            }
            final int divIndex = realFileName.indexOf("<div");
            if (divIndex > 0) {
                realFileName = realFileName.substring(0, divIndex);
            }
            loadingImage.setVisible(false);
            fileDownloadWidget.setFilePath(uploadedFilePath, realFileName);
            fileDownloadWidget.setVisible(true);
            modifyLabel.setVisible(true);
            removeLabel.setVisible(true);
            cancelLabel.setVisible(false);
        }
    }

    /**
     * @return the path to the uploaded file
     */
    public String getValue() {
        if (FileWidgetInputType.ALL.equals(fileWidgetInputType)) {
            if (FILE_DOCUMENT_TYPE.equals(radioButtonGroupWidget.getValue())) {
                return uploadedFilePath;
            } else {
                return urlTextBox.getValue();
            }
        } else if (FileWidgetInputType.FILE.equals(fileWidgetInputType)) {
            return uploadedFilePath;
        } else {
            return urlTextBox.getValue();
        }
    }

    /**
     * @return the type of the field value
     */
    public String getValueType() {
        if (FileWidgetInputType.ALL.equals(fileWidgetInputType)) {
            if (FILE_DOCUMENT_TYPE.equals(radioButtonGroupWidget.getValue())) {
                return SupportedFieldTypes.JAVA_FILE_CLASSNAME;
            } else {
                return SupportedFieldTypes.JAVA_STRING_CLASSNAME;
            }
        } else if (FileWidgetInputType.FILE.equals(fileWidgetInputType)) {
            return SupportedFieldTypes.JAVA_FILE_CLASSNAME;
        } else {
            return SupportedFieldTypes.JAVA_STRING_CLASSNAME;
        }
    }

    /**
     * Disable the fileupload
     */
    public void disable() {
        filePanel.remove(formPanel);
        filePanel.remove(buttonPanel);
        fileDownloadWidget.setVisible(true);
    }

    @Override
    public void onValueChange(final ValueChangeEvent<Boolean> event) {
        if (FILE_DOCUMENT_TYPE.equals(radioButtonGroupWidget.getValue())) {
            urlTextBox.setVisible(false);
            filePanel.setVisible(true);
        } else {
            filePanel.setVisible(false);
            urlTextBox.setVisible(true);
        }
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public long getAttachmentId() {
        return attachmentId;
    }

    public String getDisplayedValue() {
        String displayedValue = null;
        if (FileWidgetInputType.ALL.equals(fileWidgetInputType)) {
            if (FILE_DOCUMENT_TYPE.equals(radioButtonGroupWidget.getValue())) {
                displayedValue = fileDownloadWidget.getDisplayedValue();
            } else {
                displayedValue = urlTextBox.getValue();
            }
        } else if (FileWidgetInputType.FILE.equals(fileWidgetInputType)) {
            displayedValue = fileDownloadWidget.getDisplayedValue();
        } else {
            displayedValue = urlTextBox.getValue();
        }
        return displayedValue;
    }
}
