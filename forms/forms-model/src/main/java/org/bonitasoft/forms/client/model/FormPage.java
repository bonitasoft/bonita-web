/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.forms.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Form page data
 * 
 * @author Anthony Birembaut
 */
public class FormPage implements Serializable{

    /**
     * UID
     */
    private static final long serialVersionUID = -3003458547469536846L;
    
    /**
     * the page label
     */
    private Expression pageLabelExpression;
    
    /**
     * The expression to evaluate for the next page of the page flow
     */
    private Expression nextPageExpression;
    
    /**
     * the page template
     */
    private HtmlTemplate pageLayout;
    
    /**
     * the page widgets
     */
    private List<FormWidget> formWidgets;
    
    /**
     * the page validators
     */
    private List<FormValidator> pageValidators;
    
    /**
     * The reduced form page
     */
    private ReducedFormPage reducedFormPage;
    
    /**
     * Constructor
     * @param pageId
     * @param pageLabelExpression
     * @param pageLayout
     * @param formWidgets
     * @param pageValidators
     * @param formType
     * @param allowHTMLInLabel
     */
    public FormPage(final String pageId, final Expression pageLabelExpression, final HtmlTemplate pageLayout, final List<FormWidget> formWidgets, final List<FormValidator> pageValidators, final FormType formType, final boolean allowHTMLInLabel) {
        this.pageLayout = pageLayout;
        ReducedHtmlTemplate reducedPageTemplate = null;
        if (pageLayout != null) {
            reducedPageTemplate = pageLayout.getReducedHtmlTemplate();
        }
        reducedFormPage = new ReducedFormPage(pageId, reducedPageTemplate, formType, allowHTMLInLabel);
        List<ReducedFormWidget> reducedFormWidgets = new ArrayList<ReducedFormWidget>();
        this.formWidgets = formWidgets;
        if (formWidgets != null) {
            for (FormWidget formWidget : formWidgets) {
                reducedFormWidgets.add(formWidget.getReducedFormWidget());
            }
        }
        reducedFormPage.setFormWidgets(reducedFormWidgets);
        this.pageValidators = pageValidators;
        List<ReducedFormValidator> reducedValidators = new ArrayList<ReducedFormValidator>();
        if (pageValidators != null) {
            for (FormValidator formValidator : pageValidators) {
                reducedValidators.add(formValidator.getReducedFormValidator());
            }
        }
        reducedFormPage.setPageValidators(reducedValidators);
        this.pageLabelExpression = pageLabelExpression;
    }
    
    /**
     * Default Constructor
     * Mandatory for serialization
     */
    public FormPage() {
        super();
        reducedFormPage = new ReducedFormPage();
    }
    
    public String getPageId() {
        return reducedFormPage.getPageId();
    }

    public void setPageId(final String pageId) {
        reducedFormPage.setPageId(pageId);
    }
    
    public HtmlTemplate getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(final HtmlTemplate pageLayout) {
        this.pageLayout = pageLayout;
        ReducedHtmlTemplate reducedPageLayout = null;
        if (pageLayout != null) {
            reducedPageLayout = pageLayout.getReducedHtmlTemplate();
        }
        reducedFormPage.setPageTemplate(reducedPageLayout);
    }

    public List<FormWidget> getFormWidgets() {
        return formWidgets;
    }

    public void setFormWidgets(final List<FormWidget> formWidgets) {
        this.formWidgets = formWidgets;
        List<ReducedFormWidget> reducedFormWidgets = new ArrayList<ReducedFormWidget>();
        if (formWidgets != null) {
            for (FormWidget formWidget : formWidgets) {
                reducedFormWidgets.add(formWidget.getReducedFormWidget());
            }
        }
        reducedFormPage.setFormWidgets(reducedFormWidgets);
    }

    public List<FormValidator> getPageValidators() {
        return pageValidators;
    }

    public void setPageValidators(final List<FormValidator> pageValidators) {
        this.pageValidators = pageValidators;
        List<ReducedFormValidator> reducedValidators = new ArrayList<ReducedFormValidator>();
        if (pageValidators != null) {
            for (FormValidator formValidator : pageValidators) {
                reducedValidators.add(formValidator.getReducedFormValidator());
            }
        }
        reducedFormPage.setPageValidators(reducedValidators);
    }

	public String getPageLabel() {
        return reducedFormPage.getPageLabel();
    }

    public void setPageLabel(final String pageLabel) {
        reducedFormPage.setPageLabel(pageLabel);
    }

    public boolean allowHTMLInLabel() {
        return reducedFormPage.allowHTMLInLabel();
    }

    public void setAllowHTMLInLabel(final boolean allowHTMLInLabel) {
        reducedFormPage.setAllowHTMLInLabel(allowHTMLInLabel);
    }

    public Expression getNextPageExpression() {
        return nextPageExpression;
    }

    public void setNextPageExpression(final Expression nextPageExpression) {
        this.nextPageExpression = nextPageExpression;
    }

    public FormType getFormType() {
        return reducedFormPage.getFormType();
    }

    public void setFormType(FormType formType) {
        reducedFormPage.setFormType(formType);
    }

    public ReducedFormPage getReducedFormPage() {
        return reducedFormPage;
    }

    public void setReducedFormPage(ReducedFormPage reducedFormPage) {
        this.reducedFormPage = reducedFormPage;
    }

    public Expression getPageLabelExpression() {
        return pageLabelExpression;
    }

    public void setPageLabelExpression(Expression pageLabelExpression) {
        this.pageLabelExpression = pageLabelExpression;
    }

    public String getNextPageExpressionId() {
        return reducedFormPage.getNextPageExpressionId();
    }
    
    public void setNextPageExpressionId(final String nextPageExpressionId) {
        reducedFormPage.setNextPageExpressionId(nextPageExpressionId);
    }

    public String getPageValidatorsId() {
        return reducedFormPage.getPageValidatorsId();
    }

    public void setPageValidatorsId(final String pageValidatorsId) {
        reducedFormPage.setPageValidatorsId(pageValidatorsId);
    }
}
