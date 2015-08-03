/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.console.client.common.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.rest.model.bpm.cases.CommentItem;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.MandatoryValidator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.form.FormAction;
import org.bonitasoft.web.toolkit.client.ui.component.form.Form;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormButtonCancel;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormSubmitButton;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Textarea;

/**
 * @author Vincent Elcrin
 *
 */
public class CommentSubmitionForm extends Form {

    private String defaultValue = null;

    public CommentSubmitionForm(final APIID caseId, final FormAction callback) {
        this(caseId, callback, null, true);
    }

    public CommentSubmitionForm(final APIID caseId, final FormAction callback, final FormAction cancelCallback, final boolean isMandatory) {
        super(new JsId("commentForm"));
        buildContent(caseId, callback, cancelCallback, isMandatory);
    }

    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    private void buildContent(final APIID caseId, final FormAction callback, final FormAction cancelCallback, final boolean isMandatory) {
        addHiddenEntry(CommentItem.ATTRIBUTE_PROCESS_INSTANCE_ID, caseId.toString());
        addEntry(createTextArea(isMandatory));
        addAction(createSubmitionButton(callback));
        if (cancelCallback == null) {
            addCancelButton();
        } else {
            addAction(new FormButtonCancel(cancelCallback));
        }
    }

    private FormSubmitButton createSubmitionButton(final FormAction callback) {
        return new FormSubmitButton(new JsId("submit"), _("Submit"), _("Submit a comment"), wrappeCallback(callback));
    }

    private Textarea createTextArea(final boolean isMandatory) {
        final Textarea textarea = new Textarea(new JsId(CommentItem.ATTRIBUTE_CONTENT), _("Comment"), _("Add your comment"), defaultValue);
        if (isMandatory) {
            textarea.addValidator(new MandatoryValidator());
        }
        return textarea;
    }

    private FormSubmitAction wrappeCallback(final FormAction callback) {
        callback.setForm(this);
        return new FormSubmitAction(callback);
    }

}
