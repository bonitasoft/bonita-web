package org.bonitasoft.console.client.admin.profile.view;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.LinkedList;
import java.util.List;

import org.bonitasoft.console.client.model.portal.profile.ProfileDefinition;
import org.bonitasoft.console.client.model.portal.profile.ProfileItem;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsAction;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemDetailsMetadata;
import org.bonitasoft.web.toolkit.client.ui.page.ItemQuickDetailsPage.ItemQuickDetailsPage;

/**
 * @author Bastien ROHART
 * 
 */
public abstract class AbstractProfileDetailsPage extends ItemQuickDetailsPage<ProfileItem> {
    
    public AbstractProfileDetailsPage() {
        super(ProfileDefinition.get());
    }

    @Override
    protected List<String> defineDeploys() {
        return null;
    }

    @Override
    protected LinkedList<ItemDetailsAction> defineActions(final ProfileItem item) {
        return new LinkedList<ItemDetailsAction>();
    }

    @Override
    protected void defineTitle(final ProfileItem item) {
        setTitle(item.getName());

        String displayDescription = item.getDescription();
        addDescription(StringUtil.isBlank(displayDescription) ? _("No description.") : displayDescription);
    }

    @Override
    protected LinkedList<ItemDetailsMetadata> defineMetadatas(final ProfileItem item) {
        final LinkedList<ItemDetailsMetadata> metadatas = new LinkedList<ItemDetailsMetadata>();
        return metadatas;
    }

}
