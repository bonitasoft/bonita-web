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
package org.bonitasoft.web.toolkit.client.ui.component.form.entry;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import org.bonitasoft.web.toolkit.client.ui.JsId;
import org.bonitasoft.web.toolkit.client.ui.action.Action;
import org.bonitasoft.web.toolkit.client.ui.component.Button;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author Séverin Moussel
 */
public class Page extends Section {

    private String title = null;

    private boolean showPrevious = true;

    private String previousTitle = _("Previous");

    private String previousTooltip = _("Go to previous step");

    private String nextTitle = _("Next");

    private String nextTooltip = _("Go to previous step");

    private boolean showFinish = false;

    private String finishTitle = _("Finish");

    private String finishTooltip = _("Finish this page");

    private String cancelTitle = _("Finish");

    private String cancelTooltip = _("Finish this page");

    private Action onPreviousAction = null;

    private Action onNextAction = null;

    private Action onCancelAction = null;

    private Action onFinishAction = null;

    public Page(final String title) {
        this(null, title);
    }

    public Page(final JsId jsid, final String title) {
        super(jsid);
        this.title = title;
    }

    public Page(final String title, final Action onNextAction) {
        this(null, title, onNextAction, null, null, null);
    }

    public Page(final JsId jsid, final String title, final Action onNextAction) {
        this(jsid, title, onNextAction, null, null, null);
    }

    public Page(final String title, final Action onNextAction, final Action onPreviousAction, final Action onCancelAction, final Action onFinishAction) {
        this(null, title, onNextAction, onPreviousAction, onCancelAction, onFinishAction);
    }

    public Page(final JsId jsid, final String title, final Action onNextAction, final Action onPreviousAction, final Action onCancelAction,
            final Action onFinishAction) {
        this(jsid, title);
        this.onNextAction = onNextAction;
        this.onPreviousAction = onPreviousAction;
        this.onCancelAction = onCancelAction;
        this.onFinishAction = onFinishAction;
    }

    public String getTitle() {
        return this.title;
    }

    public void setPreviousButton(final String title, final String tooltip) {
        this.showPrevious = true;
        this.previousTitle = title;
        this.previousTooltip = tooltip;
    }

    public void setNextButton(final String title, final String tooltip) {
        this.nextTitle = title;
        this.nextTooltip = tooltip;
    }

    public void setFinishButton(final String title, final String tooltip) {
        this.showFinish = true;
        this.finishTitle = title;
        this.finishTooltip = tooltip;
    }

    public void setCancelButton(final String title, final String tooltip) {
        this.cancelTitle = title;
        this.cancelTooltip = tooltip;
    }

    public void previous() {
        if (this.onPreviousAction == null) {
            this.onPreviousAction.execute();
            jQueryPrevious(getElement());
        }
    }

    public void next() {
        if (this.onNextAction == null) {
            this.onNextAction.execute();
            jQueryNext(getElement());
        }
    }

    public void finish() {
        if (this.onFinishAction == null) {
            this.onFinishAction.execute();
            jQueryFinish(getElement());
        }
    }

    public void cancel() {
        if (this.onCancelAction == null) {
            this.onCancelAction.execute();
            jQueryCancel(getElement());
        }
    }

    public native void jQueryPrevious(Element e) /*-{
                                                 $wnd.$(e).wizard("previous");
                                                 }-*/;

    public native void jQueryNext(Element e) /*-{
                                             $wnd.$(e).wizard("next");
                                             }-*/;

    public native void jQueryFinish(Element e) /*-{
                                               $wnd.$(e).wizard("finish");
                                               }-*/;

    public native void jQueryCancel(Element e) /*-{
                                               $wnd.historyBack();
                                               }-*/;

    private abstract class WizardAction extends Action {

        protected Page page = null;

        public WizardAction(final Page page) {
            super();
            this.page = page;
        }

    }

    @Override
    protected void postProcessHtml() {

        this.element.addClassName("wizardpage");

        final Element title = DOM.createElement("h1");
        title.setInnerText(this.title);
        this.element.insertFirst(title);

        if (this.showPrevious) {
            this.element.appendChild(new Button(new JsId("previous"), this.previousTitle, this.previousTooltip, new WizardAction(this) {

                @Override
                public void execute() {
                    this.page.previous();
                }
            }).getElement());
        }

        this.element.appendChild(new Button(new JsId("next"), this.nextTitle, this.nextTooltip, new WizardAction(this) {

            @Override
            public void execute() {
                this.page.next();
            }
        }).getElement());

        if (this.showFinish) {
            this.element.appendChild(new Button(new JsId("finish"), this.finishTitle, this.finishTooltip, new WizardAction(this) {

                @Override
                public void execute() {
                    this.page.finish();
                }
            }).getElement());
        }

        this.element.appendChild(new Button(new JsId("cancel"), this.cancelTitle, this.cancelTooltip, new WizardAction(this) {

            @Override
            public void execute() {
                this.page.cancel();
            }
        }).getElement());

        super.postProcessHtml();
    }

    public void setShowPrevious(final boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public void setShowFinish(final boolean showFinish) {
        this.showFinish = showFinish;
    }

}
