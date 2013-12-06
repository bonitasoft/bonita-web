/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.toolkit.client.ui.page;

import static com.google.gwt.query.client.GQuery.$;
import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.web.toolkit.client.ViewController;
import org.bonitasoft.web.toolkit.client.common.TreeIndexed;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.json.JSonItemReader;
import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.data.APIID;
import org.bonitasoft.web.toolkit.client.data.api.callback.APICallback;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.ui.Page;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class PageOnItem<T extends IItem> extends Page {

    protected ItemDefinition itemDefinition = null;

    public final static String PARAMETER_ITEM_ID = "id";

    public PageOnItem(final ItemDefinition itemDefinition) {
        super();
        this.itemDefinition = itemDefinition;
    }

    public PageOnItem(final String itemId, final ItemDefinition itemDefinition) {
        this(itemDefinition);
        setItemId(itemId);
    }

    public PageOnItem(final APIID itemId, final ItemDefinition itemDefinition) {
        this(itemDefinition);
        setItemId(itemId);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ITEM ID
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private APIID itemId = null;

    public final void setItemId(final String itemId) {
        this.itemId = APIID.makeAPIID(itemId);
    }

    public final void setItemId(final APIID itemId) {
        this.itemId = itemId;
    }

    protected APIID getItemId() {
        return this.itemId;
    }

    private T __pload = null;

    public final void __pload(final T item) {
        this.__pload = item;
    }

    protected List<String> defineDeploys() {
        return new ArrayList<String>();
    }

    protected List<String> defineCounters() {
        return new ArrayList<String>();
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // OVERRIDES
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void setParameters(final Map<String, String> params) {
        super.setParameters(params);

        if (params.containsKey(PARAMETER_ITEM_ID)) {
            setItemId(params.get(PARAMETER_ITEM_ID));
        }
    }

    @Override
    public void setParameters(final TreeIndexed<String> params) {
        super.setParameters(params);

        if (params.containsKey(PARAMETER_ITEM_ID)) {
            setItemId(params.getValue(PARAMETER_ITEM_ID));
        }
    }

    @Override
    public void setParameters(final Arg... params) {
        super.setParameters(params);

        for (final Arg param : params) {
            if (PARAMETER_ITEM_ID.equals(param.getName())) {
                setItemId(param.getValue());
                break;
            }
        }
    }

    @Override
    public void addParameter(final String name, final String value) {
        super.addParameter(name, value);
        if (PARAMETER_ITEM_ID.equals(name)) {
            setItemId(value);
        }
    }

    @Override
    public void addParameter(final String name, final String... values) {
        super.addParameter(name, values);
        if (PARAMETER_ITEM_ID.equals(name)) {
            setItemId(APIID.makeAPIID(values));
        }
    }

    @Override
    public void refresh() {
        ViewController.showView(getToken(), getParentElement(), getParameters());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // EXECUTION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private T item = null;

    @Override
    protected final void _fillWidget(final Element rootElement) {
        if (this.__pload != null) {
            this.item = this.__pload;
            super._fillWidget(rootElement);
            return;
        }
        this.itemDefinition.getAPICaller().get(this.itemId, defineDeploys(), defineCounters(), new APICallback() {

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(final int httpStatusCode, final String response, final Map<String, String> headers) {

                // fix for IE. Sometime IE add element evenif not in parent anymore.
                if (!GQuery.contains(PageOnItem.this.getParentElement(), PageOnItem.this.getElement())) {
                    return;
                }

                try {
                    PageOnItem.this.item = (T) JSonItemReader.parseItem(response, PageOnItem.this.itemDefinition);
                } catch (final Exception e) {
                    fail(e.getMessage());
                    return;
                }

                $(PageOnItem.this.getParentElement()).children().not(PageOnItem.this.getElement()).remove();
                $(PageOnItem.this.getElement()).show();

                PageOnItem.super._fillWidget(rootElement);

                ViewController.getInstance().triggerLoad();

                ViewController.updateUI(PageOnItem.this.getElement());
            }

            @Override
            public void onError(final String message, final Integer errorCode) {
                if (isNotFoundItem(message, errorCode)) {
                    onItemNotFound();
                } else {
                    fail(message);
                }
            }

        });

    }

    private boolean isNotFoundItem(final String message, final Integer errorCode) {
        return errorCode.equals(HttpServletResponse.SC_NOT_FOUND) && message.isEmpty();
    }

    protected void onItemNotFound() {
        fail(_("Item not found"));
    }

    protected abstract void defineTitle(T item);

    @Override
    public final void defineTitle() {
        defineTitle(this.item);
    }

    protected abstract void buildView(T item);

    @Override
    public final void buildView() {
        buildView(this.item);
    }

    protected void fail(final String message) {
        throw new APIException(message);
    }

}
