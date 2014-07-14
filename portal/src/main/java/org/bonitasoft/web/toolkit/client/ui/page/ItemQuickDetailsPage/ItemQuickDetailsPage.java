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
package org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage;

import java.util.LinkedList;

import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.data.item.IItem;
import org.bonitasoft.web.toolkit.client.data.item.ItemDefinition;
import org.bonitasoft.web.toolkit.client.data.item.attribute.reader.AbstractAttributeReader;
import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItem;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemId;
import org.bonitasoft.web.toolkit.client.ui.action.ActionOnItemIds;
import org.bonitasoft.web.toolkit.client.ui.component.Button;
import org.bonitasoft.web.toolkit.client.ui.component.Paragraph;
import org.bonitasoft.web.toolkit.client.ui.component.Title;
import org.bonitasoft.web.toolkit.client.ui.component.containers.Container;
import org.bonitasoft.web.toolkit.client.ui.component.containers.ContainerStyled;
import org.bonitasoft.web.toolkit.client.ui.component.core.AbstractComponent;
import org.bonitasoft.web.toolkit.client.ui.component.core.Node;
import org.bonitasoft.web.toolkit.client.ui.page.PageOnItem;

/**
 * @author Séverin Moussel
 * 
 */
public abstract class ItemQuickDetailsPage<T extends IItem> extends PageOnItem<T> {

    private Container<Paragraph> descriptionContainer = null;

    public ItemQuickDetailsPage(final ItemDefinition itemDefinition) {
        super(itemDefinition);
        addClass("itemquickdetailspage");
    }

    @Override
    public void buildView(final T item) {
        buildToolbar(item);
        buildMetadatas(item);
        buildBody(item);
    }

    // FIXME separates metadatas data blocks
    protected boolean isDescriptionBeforeMetadatas() {
        return true;
    }

    protected void buildMetadatas(final T item) {
        final Container<AbstractComponent> metadatasSection = new Container<AbstractComponent>(new JsId("metadatas"));
        addTitle(metadatasSection);
        if (isDescriptionBeforeMetadatas()) {
            addDescription(metadatasSection);
        }

        final LinkedList<ItemDetailsMetadata> metadatas = this.defineMetadatas(item);
        if (metadatas != null && metadatas.size() > 0) {
            final ContainerStyled<Node> definitions = new ContainerStyled<Node>(new JsId("definitions"));
            for (final ItemDetailsMetadata metadata : metadatas) {
                addMetadata(item, definitions, metadata);
            }
            metadatasSection.append(definitions);
        }

        if (!isDescriptionBeforeMetadatas()) {
            addDescription(metadatasSection);
        }
        addBody(metadatasSection);
    }

    private void addDescription(final Container<AbstractComponent> metadatasSection) {
        if (this.descriptionContainer != null) {
            metadatasSection.append(this.descriptionContainer);
        }
    }

    private void addTitle(final Container<AbstractComponent> metadatasSection) {
        metadatasSection.append(new Title(getTitle().getText(), getTitle().getComponents()));
    }

    private void addMetadata(final T item, final ContainerStyled<Node> definitions, final ItemDetailsMetadata metadata) {
        final AbstractAttributeReader attributeReader = metadata.getAttributeReader();
        final String value = attributeReader.read(item);
        // the call below must be done after the call to the read method since the CSS class can be determined while reading the attribute
        final String cssClass = createMetadataCssClass(attributeReader, metadata);
        if (!StringUtil.isBlank(value)) {
            definitions.append(metadata.createDefinition(cssClass, value));
        }
    }

    /**
     * @return
     */
    private String createMetadataCssClass(final AbstractAttributeReader attributeReader, final ItemDetailsMetadata metadata) {
        if (attributeReader.getClassName() != null) {
            return attributeReader.getClassName().toLowerCase();
        } else if (metadata.getJsId() != null) {
            return metadata.getJsId().toString();
        } else {
            return null;
        }
    }

    /**
     * Build the toolbar by adding all links to it
     */
    protected void buildToolbar(final T item) {
        final LinkedList<ItemDetailsAction> actions = this.defineActions(item);
        if (actions != null) {
            for (final ItemDetailsAction itemDetailAction : actions) {
                final Action action = buildActionFromItemDetailAction(item, itemDetailAction);
                final Button button = buildToolBarButtonFromAction(itemDetailAction, action);
                addToolbarLink(button);
            }
        }

    }

    @SuppressWarnings("unchecked")
    private Action buildActionFromItemDetailAction(final T item, final ItemDetailsAction itemDetailAction) {
        final Action action = itemDetailAction.getAction();
        if (action instanceof ActionOnItem<?>) {
            ((ActionOnItem<T>) action).setItemId(item.getId());
        } else if (action instanceof ActionOnItemId) {
            ((ActionOnItemId) action).setItemId(item.getId());
        } else if (action instanceof ActionOnItemIds) {
            ((ActionOnItemIds) action).setItemId(item.getId());
        } else {
            action.addParameter("id", item.getId().toString());
        }

        action.setOnFinish(new Action() {

            @Override
            public void execute() {
                refresh();
            }
        });
        return action;
    }

    private Button buildToolBarButtonFromAction(final ItemDetailsAction action, final Action realAction) {
        final Button button = new Button(action.getJsId(), action.getLabel(), action.getTooltip(), realAction);
        button.setEnabled(action.isEnabled());
        return button;
    }

    /**
     * @deprecated prefer overriding {@link #buildToolbar(IItem)}
     */
    @Deprecated
    protected LinkedList<ItemDetailsAction> defineActions(final T item) {
        return new LinkedList<ItemDetailsAction>();
    }

    protected abstract LinkedList<ItemDetailsMetadata> defineMetadatas(T item);

    protected void buildBody(final T item) {
    }

    protected final void addDescription(final String description, final AbstractComponent... components) {
        if (this.descriptionContainer == null) {
            this.descriptionContainer = new Container<Paragraph>(new JsId("description"));
        }

        this.descriptionContainer.append(new Paragraph(description, components));
    }

}
