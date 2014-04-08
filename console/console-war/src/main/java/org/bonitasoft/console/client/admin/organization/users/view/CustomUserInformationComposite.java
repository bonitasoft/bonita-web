package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;

import java.util.List;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationComposite extends Composite implements ModelLoadHandler<CustomUserInformationModel>, DirtyInputHandler {

    interface Template extends SafeHtmlTemplates {

        @Template("<div class=\"formentry formentry_firstname mandatory text\">" +
                "<div class=\"label\"><label title=\"{2}\">{1}</label></div>" +
                "<div class=\"input\"><input type=\"text\" name=\"{1}\" maxlength=\"50\" value=\"{3}\" tabindex=\"{0}\">" +
                "</div></div>")
        public SafeHtml line(int line, String name, String description, String value);
    }

    private ListDataProvider<CustomUserInfoItem> data = new ListDataProvider<CustomUserInfoItem>();

    private CustomUserInformationModel model;

    private static final Template TEMPLATE = GWT.create(Template.class);

    private LineTemplate<CustomUserInfoItem> line = new LineTemplate<CustomUserInfoItem>(new LineTemplate.Line<CustomUserInfoItem>() {

        @Override
        public SafeHtml render(Cell.Context context, CustomUserInfoItem information) {
            CustomUserInfoDefinitionItem definition = information.getDefinition();
            return TEMPLATE.line(context.getIndex() + 1, definition.getName(), definition.getDescription(), information.getValue());
        }
    });

    final TemplateList<CustomUserInfoItem> list = new TemplateList<CustomUserInfoItem>(line, "formentry");

    public CustomUserInformationComposite(CustomUserInformationModel model) {
        initWidget(list);
        this.model = model;
        model.observe(this);
        line.listen(this);
        data.addDataDisplay(list);
        data.setList(model.getInformation());
    }

    @Override
    public void onModelLoad(ModelLoadEvent<CustomUserInformationModel> event) {
        List<CustomUserInfoItem> information = event.getModel().getInformation();
        int size = event.getSize();
        if(information.size() < size) {
            size = information.size();
        }
        data.getList().addAll(size, information.subList(event.getPage() * size, size));
    }

    @Override
    public void onDirtyInput(DirtyInputEvent event) {
        model.update(event.getContext().getIndex(), event.getInput().getValue());
    }
}
