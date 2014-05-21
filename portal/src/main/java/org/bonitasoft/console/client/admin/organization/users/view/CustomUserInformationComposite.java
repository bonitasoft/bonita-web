package org.bonitasoft.console.client.admin.organization.users.view;

import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import org.bonitasoft.console.client.mvp.Repeater;
import org.bonitasoft.console.client.mvp.TemplateRepeat;
import org.bonitasoft.console.client.mvp.event.DirtyInputEvent;
import org.bonitasoft.console.client.mvp.event.DirtyInputHandler;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoDefinitionItem;
import org.bonitasoft.web.rest.model.identity.CustomUserInfoItem;

/**
 * @author Vincent Elcrin
 */
public class CustomUserInformationComposite extends Composite {

    interface Template extends SafeHtmlTemplates {

        @Template("<div class=\"formentry formentry_firstname mandatory text\">" +
                "<div class=\"label\"><label title=\"{2}\">{1}</label></div>" +
                "<div class=\"input\"><input type=\"text\" name=\"{1}\" maxlength=\"50\" value=\"{3}\" tabindex=\"{0}\"></div>" +
                "</div>")
        public SafeHtml line(int line, String name, String description, String value);
    }

    private static final Template TEMPLATE = GWT.create(Template.class);

    TemplateRepeat<CustomUserInfoItem> template = new TemplateRepeat<CustomUserInfoItem>("formentry") {

        @Override
        public SafeHtml render(Cell.Context context, CustomUserInfoItem information) {
            CustomUserInfoDefinitionItem definition = information.getDefinition();
            return TEMPLATE.line(
                    context.getIndex() + 1,
                    definition.getName(),
                    definition.getDescription(),
                    information.getValue());
        }
    };

    private Repeater<CustomUserInfoItem> repeater = new Repeater<CustomUserInfoItem>(template);

    public CustomUserInformationComposite(final CustomUserInformationModel model) {
        initWidget(repeater);
        model.search(0, 10, new CustomUserInformationModel.Callback() {

            @Override
            void onSuccess(List<CustomUserInfoItem> information) {
                repeater.setRowData(information);
            }
        });

        template.listen(new DirtyInputHandler<CustomUserInfoItem>() {

            @Override
            public void onDirtyInput(DirtyInputEvent<CustomUserInfoItem> event) {
                model.update(event.getItem(), event.getInput().getValue());
            }
        });
    }
}
