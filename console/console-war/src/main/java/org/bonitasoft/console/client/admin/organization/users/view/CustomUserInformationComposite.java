package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import org.bonitasoft.console.client.mvp.LineTemplate;
import org.bonitasoft.console.client.mvp.TemplateList;
import org.bonitasoft.console.client.mvp.event.DirtyInputEvent;
import org.bonitasoft.console.client.mvp.event.DirtyInputHandler;
import org.bonitasoft.console.client.mvp.event.ModelLoadEvent;
import org.bonitasoft.console.client.mvp.event.ModelLoadHandler;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationComposite extends Composite implements ModelLoadHandler<CustomUserInformationModels>, DirtyInputHandler {

    interface Template extends SafeHtmlTemplates {

        @Template("<div class=\"formentry formentry_firstname mandatory text\">" +
                "<div class=\"label\"><label title=\"{2}\">{1}</label></div>" +
                "<div class=\"input\"><input type=\"text\" name=\"{1}\" maxlength=\"50\" value=\"{3}\" tabindex=\"{0}\"></div>" +
                "</div>")
        public SafeHtml line(int line, String name, String description, String value);
    }

    private CustomUserInformationModels model;

    private static final Template TEMPLATE = GWT.create(Template.class);

    private LineTemplate<CustomUserInfoItem> line = new LineTemplate<CustomUserInfoItem>(new LineTemplate.Line<CustomUserInfoItem>() {

        @Override
        public SafeHtml render(Cell.Context context, CustomUserInfoItem information) {
            CustomUserInfoDefinitionItem definition = information.getDefinition();
            return TEMPLATE.line(context.getIndex() + 1, definition.getName(), definition.getDescription(), information.getValue());
        }
    }, "formentry");

    final TemplateList<CustomUserInfoItem> list = new TemplateList<CustomUserInfoItem>(line);

    public CustomUserInformationComposite(CustomUserInformationModels model) {
        initWidget(list);
        this.model = model;
        model.observe(this);
        line.listen(this);
        list.setRowData(model.getInformation());
    }

    @Override
    public void onModelLoad(ModelLoadEvent<CustomUserInformationModels> event) {
        list.setRowData(event.getModel().getInformation());
    }

    @Override
    public void onDirtyInput(DirtyInputEvent event) {
        model.update(event.getContext().getIndex(), event.getInput().getValue());
    }
}
