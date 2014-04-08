package org.bonitasoft.console.client.admin.organization.users.view;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationComposite extends Composite implements ModelChangeHandler<CustomUserInformationModel>, DirtyInputHandler {

    private ListDataProvider<CustomUserInfoItem> data = new ListDataProvider<CustomUserInfoItem>();

    private CustomUserInformationModel model;

    interface Template extends SafeHtmlTemplates {

        @Template("<div class=\"formentry formentry_firstname mandatory text\">" +
                "<div class=\"label\"><label title=\"{0}\">{0}</label></div>" +
                "<div class=\"input\"><input type=\"text\" name=\"{0}\" title=\"{0}\" maxlength=\"50\" value=\"{1}\"></div></div>")
        public SafeHtml line(String name, String value);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);

    private LineTemplate<CustomUserInfoItem> line = new LineTemplate<CustomUserInfoItem>(new LineTemplate.Line<CustomUserInfoItem>() {

        @Override
        public SafeHtml render(Cell.Context context, CustomUserInfoItem information) {
            return TEMPLATE.line(information.getDefinition().getName(), information.getValue());
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
    public void onDirtyInput(DirtyInputEvent event) {
        model.update(event.getContext().getIndex(), event.getInput().getValue());
    }

    @Override
    public void onModelChange(ModelChangeEvent<CustomUserInformationModel> event) {
        data.setList(event.getModel().getInformation());
    }
}
