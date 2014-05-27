/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.component.form;

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import com.google.gwt.query.client.GQuery;
import org.bonitasoft.web.toolkit.client.common.AbstractTreeNode;
import org.bonitasoft.web.toolkit.client.common.Tree;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.TreeLeaf;
import org.bonitasoft.web.toolkit.client.common.TreeNode;
import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.bonitasoft.web.toolkit.client.common.json.JSonUtil;
import org.bonitasoft.web.toolkit.client.common.json.JsonSerializable;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.common.texttemplate.TextTemplate;
import org.bonitasoft.web.toolkit.client.data.item.attribute.validator.Validator;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.core.Component;
import org.bonitasoft.web.toolkit.client.ui.component.event.InputCompleteEvent;
import org.bonitasoft.web.toolkit.client.ui.component.event.InputCompleteHandler;
import org.bonitasoft.web.toolkit.client.ui.component.form.button.FormButton;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.AutoCompleteEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FormEntries;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.FormEntry;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.Section;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.StaticText;
import org.bonitasoft.web.toolkit.client.ui.component.form.entry.ValuedFormEntry;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

/**
 * @author Séverin Moussel
 */
public abstract class AbstractForm extends Component implements JsonSerializable {

    private final TreeIndexed<String> hiddens = new TreeIndexed<String>();

    private HashMap<String, ValuedFormEntry> entriesIndex = new HashMap<String, ValuedFormEntry>();

    /**
     * A stack of opened container with at least the root container of FormEntries in it.
     */
    protected Stack<Container<FormNode>> containers = new Stack<Container<FormNode>>();

    /**
     * The container of FormActions.
     */
    private Container<FormButton> buttons = new Container<FormButton>(new JsId("formactions"));

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public AbstractForm() {
        super(null);
        resetEntries();
    }

    public AbstractForm(final JsId jsid) {
        super(jsid);
        resetEntries();
    }

    /**
     * Default Constructor.
     * 
     * @param jsid
     */
    public AbstractForm(final JsId jsid, final HashMap<String, String> hiddens) {
        super(jsid);
        this.hiddens.addValues(hiddens);
        resetEntries();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected final void addHidden(final String name, final String value) {
        AbstractTreeNode<String> node = this.hiddens.get(name);

        if (node == null) {
            this.hiddens.addNode(name, new TreeLeaf<String>(value));

        } else if (node instanceof TreeLeaf<?>) {
            final String oldValue = ((TreeLeaf<String>) node).getValue();

            if (!oldValue.equals(value)) {
                this.hiddens.removeNode(name);

                node = new Tree<String>();
                this.hiddens.addNode(name, node);

                ((Tree<String>) node).addValue(oldValue);
                ((Tree<String>) node).addValue(value);
            }

        } else if (node instanceof Tree<?>) {
            ((Tree<String>) this.hiddens.get(name)).addValue(value);
        }

    }

    protected final void addHidden(final String name, final List<String> values) {
        this.hiddens.addNode(name, new Tree<String>().addValues(values));
    }

    protected final void addHidden(final String name, final TreeNode<String> values) {
        this.hiddens.addNode(name, values);
    }

    /**
     * Get the value of an entry as an array using its JsId
     * 
     * @param jsid
     * @return This function returns the current value filled in the entry as a String.
     */
    public List<String> getEntryArrayValue(final JsId jsid) {
        final List<String> results = new ArrayList<String>();

        // Search in hidden fields
        final AbstractTreeNode<String> hiddenValue = this.hiddens.get(jsid.toString());
        if (hiddenValue != null) {
            if (hiddenValue instanceof Tree<?>) {
                results.addAll(((Tree<String>) hiddenValue).getValues());
            } else if (hiddenValue instanceof TreeLeaf<?>) {
                results.add(((TreeLeaf<String>) hiddenValue).getValue());
            }
        }

        // Search in input fields
        if (getEntry(jsid) != null) {
            results.addAll(getFormArrayParameter(getElement(), jsid.toString()));
        }

        return results;
    }

    /**
     * Get the value of an entry using its JsId
     * 
     * @param jsid
     * @return This function returns the current value filled in the entry as a String.
     */
    public String getEntryValue(final JsId jsid) {

        // Search in hidden fields
        final AbstractTreeNode<String> result = this.hiddens.get(jsid.toString());

        if (result != null) {

            if (result instanceof Tree<?>) {
                if (((Tree<String>) result).size() != 1) {
                    return null;
                }
                return ((Tree<String>) result).getValues().get(0);

            } else if (result instanceof TreeLeaf<?>) {
                return ((TreeLeaf<String>) result).getValue();

            } else {
                return null;
            }
        }

        // Search in input fields
        else {
            final ValuedFormEntry entry = getEntry(jsid);
            if (entry != null) {
                return entry.getValue();
            }
        }

        return null;
    }

    public List<String> getEntryArrayValue(final String name) {
        return this.getEntryArrayValue(new JsId(name));
    }

    public String getEntryValue(final String name) {
        return this.getEntryValue(new JsId(name));
    }

    public void setEntryValue(final String name, final String value) {
        if (this.entriesIndex.get(name) != null) {
            this.entriesIndex.get(name).setValue(value);

        } else if (this.hiddens.containsKey(name)) {
            this.hiddens.removeNode(name);
            this.addHidden(name, value);
        }
    }

    public AbstractForm resetEntries() {
        this.entriesIndex = new HashMap<String, ValuedFormEntry>();
        this.containers = new Stack<Container<FormNode>>();
        this.buttons = new Container<FormButton>(new JsId("formactions"));
        openSection(new FormEntries());
        return this;
    }

    /**
     * Empty all inputs
     * 
     * @return This method returns "this" to allow cascading calls.
     */
    public AbstractForm reset() {
        resetErrors();
        reset(getElement());

        return this;
    }

    private native void reset(Element form)
    /*-{
        $wnd.$("input:text, textarea", form).val("");

        // $wnd.$("select option", getElement()).removeAttr("selected")
        // .first().attr("selected", "selected");

        $wnd.$("input:checkbox, input:radio", form).removeAttr("checked");


    }-*/;

    /**
     * @return the entriesIndex
     */
    public HashMap<String, ValuedFormEntry> getEntriesIndex() {
        return this.entriesIndex;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SECTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Open a new section and make it the target of the next <i>addEntry</i> and <i>addSection</i> until <i>closeLastSection</i> is called.
     * 
     * @param section
     *            The section to add
     */
    public final AbstractForm openSection(final Section section) {
        // A section can't contain a section as direct child
        if (this.containers.size() > 1 && this.containers.lastElement() instanceof FormEntries) {
            closeSection();
        }

        if (this.containers.size() > 0) {
            getLastContainer().append(section);
        }
        this.containers.push(section);

        addValuableEntry(section);

        return this;
    }

    private void addValuableEntries(LinkedList<FormNode> nodes) {
        for (FormNode node : nodes) {
            addValuableEntry(node);
        }
    }

    private void addValuableEntry(FormNode node) {
        if (isValuable(node)) {
            this.entriesIndex.put(node.getJsId().toString(), (ValuedFormEntry) node);
        }

        if (isContainer(node)) {
            addValuableEntries(((Section) node).getComponents());
        }
    }

    private boolean isValuable(FormNode node) {
        return node instanceof ValuedFormEntry;
    }

    private boolean isContainer(FormNode node) {
        return node instanceof Section;
    }

    /**
     * Close the currently opened Section.<br>
     * If no section stacked, the function does nothing
     */
    public final AbstractForm closeSection() {
        this.containers.pop();
        return this;
    }

    /**
     * Retrieve the last opened entries container
     */
    protected Container<FormNode> getLastContainer() {
        // If no formEntries container exists, open a new one
        if (this.containers.size() == 0) {
            this.containers.push(new Section());
        }
        return this.containers.lastElement();
    }

    @SuppressWarnings("unchecked")
    protected Container<FormNode> getContainerFor(final JsId jsid) {
        return (Container<FormNode>) getEntry(jsid).getContainer();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ENTRIES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addEntry(final FormNode entry) {
        if (entry instanceof Section) {
            addEntry((Section) entry);
        } else if (entry instanceof FormEntry) {
            addEntry((FormEntry) entry);
        } else {
            if (entry instanceof Component) {
                ((Component) entry).addClass("formentry");
            }
            getLastContainer().append(entry);
        }
    }

    
    /**
     * Section is the highest container implementing FormNode
     * 
     * @param section
     */
    private void addEntry(final Section section) {
        openSection(section);
    }

    /**
     * Add an entry to the form in the last opened section if it exists
     * 
     * @param entry
     */
    private void addEntry(final FormEntry entry) {
        if (entry instanceof AutoCompleteEntry) {
            ((AutoCompleteEntry) entry).addInputHandler(createInputCompleteHandler(), InputCompleteEvent.TYPE);
        }
        getLastContainer().append(entry);
        this.entriesIndex.put(entry.getJsId().toString(), entry);
    }
    
    private InputCompleteHandler createInputCompleteHandler() {
        return new InputCompleteHandler() {

            @Override
            public void onComplete(InputCompleteEvent event) {
                AbstractForm.this.resetErrors();
            }
        };
    }

    /**
     * Add an entry to the form in the last opened section if it exists
     * 
     * @param entry
     */
    protected final void addEntryBefore(final FormEntry entry, final JsId jsid) {
        getLastContainer().prepend(entry);
        this.entriesIndex.put(entry.getJsId().toString(), entry);
    }

    /**
     * Add an entry to the form in the last opened section if it exists
     * 
     * @param entry
     */
    protected final void addEntryAfter(final FormEntry entry, final JsId jsid) {
        getLastContainer().append(entry);
        this.entriesIndex.put(entry.getJsId().toString(), entry);
    }

    /**
     * Get an entry by its jsid
     * 
     * @param jsid
     */
    public ValuedFormEntry getEntry(final JsId jsid) {
        return this.entriesIndex.get(jsid.toString());
    }

    /**
     * JQuery reading of an input value by its name
     * 
     * @param form
     * @param name
     * @return
     */
    private native String getFormParameter(final Element form, final String name)
    /*-{
        var input = $wnd.$(form).find('[name=' + name + ']');
        
        if (input.is(':checkbox') || input.is(':radio')) {
            input = $wnd.$(form).find('[name=' + name + ']:checked');
        }
        return input.val();
    }-*/;

    public boolean isChecked(final JsId jsid) {
        return this.isChecked(jsid.toString());
    }

    public boolean isChecked(final String jsid) {
        return _isChecked(this.element, jsid);
    }

    private native boolean _isChecked(final Element form, final String name)
    /*-{
        return $wnd.$(form).find('[name=' + name + ']').checked();
    }-*/;

    private List<String> getFormArrayParameter(final Element form, final String name) {
        final List<String> result = new ArrayList<String>();

        GQuery.$(form).find("[name=" + name + "]").each(new Function() {

            @Override
            public void f(final Element e) {
                final String value = AbstractForm.this._getFormInputParameter(e);
                if (value != null) {
                    result.add(value);
                }
            }
        });

        return result;
    }

    private native String _getFormInputParameter(final Element element)
    /*-{
        var input = $wnd.$(element);
        
        if ((input.is(':checkbox') || input.is(':radio')) && !input.is(':checked')) {
            return null;
        }

        return $wnd.$(element).val();
    }-*/;

    private native void setFormParameter(Element form, String name, String value)
    /*-{
         var input = $wnd.$(form).find('[name=' + name + ']');
        
        if (input.is(':checkbox')) {
            input.check(input.val() == value);
        } else if (input.is(':radio')) {
            input
                .uncheck()
                .filter('[value=' + value + ']').check();
        } else { 
            input.val(value);
        }
    }-*/;

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ACTIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    protected Action defaultAction = null;

    /**
     * @return the defaultAction
     */
    protected final Action getDefaultAction() {
        return this.defaultAction;
    }

    /**
     * @param defaultAction
     *            the defaultAction to set
     */
    protected final void setDefaultAction(final Action defaultAction) {
        this.defaultAction = defaultAction;
    }

    /**
     * Add an action to the action container
     * 
     * @param button
     */
    protected void addAction(final FormButton button) {
        if (this.defaultAction == null) {
            this.defaultAction = button.getAction();
        }
        this.buttons.append(button);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DOM GENERATION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected Element makeElement() {
        final Element form = DOM.createForm();
        form.addClassName("form");
        if (getJsId() != null) {
            form.addClassName(getJsId().toString("form"));
        }

        form.appendChild(this.containers.firstElement().getElement());
        form.appendChild(this.buttons.getElement());

        GQuery.$(form).submit(new Function() {

            @Override
            public boolean f(final Event e) {
                e.stopPropagation();
                AbstractForm.this.defaultAction.execute();
                return false;
            }

        });

        return form;
    }

    @Override
    public String toJson() {
        return JSonSerializer.serialize(getValues());
    }

    public TreeIndexed<String> getValues() {

        final TreeIndexed<String> allParameters = this.hiddens.copy();

        // Get the entries values
        for (final Entry<String, ValuedFormEntry> entry : this.entriesIndex.entrySet()) {
            if (entry.getValue() != null) {
                allParameters.addValue(entry.getKey(), getEntryValue(entry.getKey()));
            }
        }

        return allParameters;
    }

    public void setValues(final Map<String, String> values) {
        for (final Entry<String, String> entry : values.entrySet()) {
            setEntryValue(entry.getKey(), entry.getValue());
        }
    }

    public void setJson(final String json) {
        // TODO Improve filling multilevel arrays

        final JSONValue parsedJson = JSONParser.parseStrict(json);
        if (parsedJson.isArray() != null) {
            final JSONArray jsonArray = parsedJson.isArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                setJson((JSONObject) jsonArray.get(i));
            }
        } else if (parsedJson.isObject() != null) {
            setJson(parsedJson.isObject());
        } else {
            throw new IllegalArgumentException("Malformed JSON");
        }
    }

    public void setJson(final JSONObject item) {
        final Iterator<String> j = item.keySet().iterator();
        while (j.hasNext()) {
            final String key = j.next();
            if (item.get(key) instanceof JSONObject) {
                setJson(composeArrayKey(key, (JSONObject) item.get(key)));
            } else {
                String value;
                if (item.get(key) instanceof JSONNull) {
                    value = null;
                } else {
                    value = JSonUtil.unquote(item.get(key).isString().toString());
                }
                setEntryValue(key, value);
            }
        }
    }

    private JSONObject composeArrayKey(final String prefix, final JSONObject item) {
        final JSONObject composedArray = new JSONObject();
        final Iterator<String> j = item.keySet().iterator();
        while (j.hasNext()) {
            final String key = j.next();
            composedArray.put(prefix + "_" + key, item.get(key));
        }
        return composedArray;
    }

    private void setJson(final JSONValue json) {
        if (json.isArray() != null) {
            final int size = json.isArray().size();
            for (int i = 0; i < size; i++) {
                this.setJson(json.isArray().get(i));
            }
        } else if (json.isObject() != null) {
            final JSONObject item = json.isObject();

            for (final String key : item.keySet()) {
                String value;
                if (item.get(key) instanceof JSONNull) {
                    continue;
                } else {
                    value = item.get(key).isString().stringValue();
                }
                setEntryValue(key, value);
            }
        }

    }

    public boolean hasNonStaticEntry() {
        for (final ValuedFormEntry entry : this.entriesIndex.values()) {
            if (!(entry instanceof StaticText)) {
                return true;
            }
        }

        return false;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ERROR MESSAGES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addError(final JsId jsid, final String message) {
        getEntry(jsid).addError(message);
    }

    public void addError(final JsId jsid, final TextTemplate messageTemplate) {
        final ValuedFormEntry entry = getEntry(jsid);
        final List<Arg> args = new ArrayList<Arg>();

        // Fill args with the label of entries corresponding to parameters in the message;
        for (final String parameterName : messageTemplate.getExpectedParameters()) {
            if (entry instanceof FormEntry) {
                if (entry.getJsId().toString().equals(parameterName)) {
                    args.add(new Arg(parameterName, AbstractI18n._("this field")));
                } else {
                    args.add(new Arg(parameterName, ((FormEntry) entry).getLabel()));
                }
            } else {
                args.add(new Arg(parameterName, parameterName));
            }
        }

        final String messageOutput = messageTemplate.toString(args);
        entry.addError(messageOutput.substring(0, 1).toUpperCase() + messageOutput.substring(1));
    }

    public void resetErrors() {
        GQuery.$("div.alert_message", getElement()).remove();
    }

    public Map<String, List<Validator>> getValidators() {
        final Map<String, List<Validator>> validators = new HashMap<String, List<Validator>>();

        for (final java.util.Map.Entry<String, ValuedFormEntry> entry : this.entriesIndex.entrySet()) {
            validators.put(entry.getKey(), entry.getValue().getValidators());
        }

        return validators;
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // VALIDATORS AND MODIFIERS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @Deprecated
     *             Use {@link ValuedFormEntry#addValidator(Validator)}
     */
    @Deprecated
    public AbstractForm addValidator(final JsId entryJsId, final Validator validator) {
        getEntry(entryJsId).addValidator(validator);

        return this;
    }

    /**
     * @Deprecated
     *             Use {@link ValuedFormEntry#addValidator(Validator)}
     */
    @Deprecated
    public AbstractForm addValidator(final String entryJsId, final Validator validator) {
        return addValidator(new JsId(entryJsId), validator);
    }

}
