package org.bonitasoft.reporting.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bonitasoft.reporting.form.components.FormComponent;
import org.bonitasoft.reporting.form.components.IComponent;

public class HtmlRenderer {

    private FormComponent form;

    private ArrayList<IComponent> coponents;

    private final StringBuffer htmlBeforeContainer;

    private final StringBuffer htmlMainContainer;

    private final StringBuffer htmlAfterContainer;

    private final StringBuffer cssCode;

    private final StringBuffer jsCode;

    private final StringBuffer bodyHtml;

    private final HashMap<String, String> jsLibraries;

    private final HashMap<String, String> cssLibraries;

    public HtmlRenderer() {
        super();

        form = new FormComponent();
        coponents = new ArrayList<IComponent>();

        htmlBeforeContainer = new StringBuffer();
        htmlMainContainer = new StringBuffer();
        htmlAfterContainer = new StringBuffer();

        cssCode = new StringBuffer();
        jsCode = new StringBuffer();
        bodyHtml = new StringBuffer();
        jsLibraries = new HashMap<String, String>();
        cssLibraries = new HashMap<String, String>();
    }

    public void init(final ArrayList<IComponent> coponents) {

        this.coponents = coponents;
        buildParts();

    }

    public HtmlRenderer(final String formName, final String fomrAction, final String reportName,
            final HashMap<String, Object> paramters) {
        this();

        form.setName(formName);
        form.setAction(fomrAction);
        form.setReportName(reportName);
        form.setParamters(paramters);
    }

    private void buildParts() {

        // loop over components
        IComponent coponent;
        for (int i = 0; i < coponents.size(); i++) {

            coponent = coponents.get(i);
            // css
            if (coponent.getCssCode() != null) {
                cssCode.append(coponent.getCssCode()).append("\n");
            }

            // jscode
            if (coponent.getJsCode() != null) {
                jsCode.append(coponent.getJsCode()).append("\n");
            }

            // jsLibraries
            final HashMap<String, String> cssOfCoponent = coponent.getCssLibraries();
            if (cssOfCoponent != null && !cssOfCoponent.isEmpty()) {
                cssLibraries.putAll(cssOfCoponent);
            }

            // jsLibraries
            final HashMap<String, String> jsOfCoponent = coponent.getJsLibraries();
            if (jsOfCoponent != null && !jsOfCoponent.isEmpty()) {
                jsLibraries.putAll(jsOfCoponent);
            }

            // body
            if (coponent.getBodyHtml() != null) {
                bodyHtml.append(coponent.getBodyHtml()).append("\n");;
            }
        }
    }

    public String getHtmlBeforeContainer() {

        // default css libraries
        // htmlBeforeContainer.append("<link rel='stylesheet' href='css/styles.css' type='text/css' />").append("\n");
        // this.htmlBeforeContainer.append("<link rel='stylesheet' href='css/table_styles.css' type='text/css' />").append("\n");
        // css Libraries
        if (cssLibraries != null) {
            final Iterator<String> cssLibrariesPath = cssLibraries.values().iterator();
            while (cssLibrariesPath.hasNext()) {
                final String cssLibraryPath = cssLibrariesPath.next();
                htmlBeforeContainer.append("<link rel='stylesheet' href='").append(cssLibraryPath).append("' type='text/css' />").append("\n");
            }
        }

        // default js libraries
        // jquery
        //htmlBeforeContainer.append("<script type='text/javascript' src='js/jquery-1.9.1.js'></script>").append("\n");
        // jquery ui
        //htmlBeforeContainer.append("<script type='text/javascript' src='js/jquery-ui-1.10.3.custom.min.js'></script>").append("\n");

        // js Libraries
        if (jsLibraries != null) {
            final Iterator<String> jsLibrariesPath = jsLibraries.values().iterator();
            while (jsLibrariesPath.hasNext()) {
                final String jsLibraryPath = jsLibrariesPath.next();
                htmlBeforeContainer.append("<script type='text/javascript' src='").append(jsLibraryPath).append("'></script>").append("\n");
            }
        }

        // css
        htmlBeforeContainer.append("<style type='text/css'>").append("\n");
        htmlBeforeContainer.append(cssCode);

        // TODO Must be change
        htmlBeforeContainer.append(".report-form {").append("\n");
        htmlBeforeContainer.append("    text-align: left;").append("\n");
        htmlBeforeContainer.append("}").append("\n");
        htmlBeforeContainer.append("div .formentry {").append("\n");
        htmlBeforeContainer.append("    display: block;").append("\n");
        // this.htmlBeforeContainer.append("    float: left;").append("\n");
        htmlBeforeContainer.append("    height: 40px;").append("\n");
        htmlBeforeContainer.append("    margin: 5px !important;").append("\n");
        // this.htmlBeforeContainer.append("   width: 130px !important;").append("\n");
        htmlBeforeContainer.append("}").append("\n");
        htmlBeforeContainer.append("</style>").append("\n");

        // js code
        htmlBeforeContainer.append(jsCode);

        return htmlBeforeContainer.toString();

    }

    public String getHtmlMainContainer() {

        htmlMainContainer.append(getForm().getBodyBeginHtml());
        htmlMainContainer.append(bodyHtml);
        htmlMainContainer.append(getForm().getBodyEndHtml());
        return htmlMainContainer.toString();
    }

    public String getHtmlAfterContainer() {
        return "";
    }

    public String getHtmlForm() {

        return getHtmlBeforeContainer() + getHtmlMainContainer() + getHtmlAfterContainer();
    }

    // getter & setters
    public FormComponent getForm() {
        return form;
    }

    public void setForm(final FormComponent form) {
        this.form = form;
    }

    public ArrayList<IComponent> getCoponents() {
        return coponents;
    }

    public void setCoponents(final ArrayList<IComponent> coponents) {
        this.coponents = coponents;
    }

    public static String getJsPath() {
        return "scripts/includes/";
    }

    public static String getCssPath() {
        return "css/";
    }

}
