package org.bonitasoft.reporting.form.components;

import java.util.HashMap;

import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.form.HtmlRenderer;
import org.bonitasoft.reporting.utils.BonitaSystem;

public class DateRangeComponent implements IComponent {

    public final static String DATE_RANGE_LABEL_FROM = "from:";

    public final static String DATE_RANGE_LABEL_TO = "to:";

    private String name;

    private String label;

    // special labels
    private String labelFrom;

    private String labelTo;

    private String type;

    private String id;

    private HashMap<String, String> values;

    private HashMap<String, String> defaultValues;

    private HashMap<String, String> cssLibraries;

    private HashMap<String, String> jsLibraries;

    private BonitaSystem bonitaSytem;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getLabel() {
        return label;
    }

    // special labels
    public String getLabelFrom() {
        return labelFrom;
    }

    public String getLabelTo() {
        return labelTo;
    }

    @Override
    public HashMap<String, String> getvalues() {
        return values;
    }

    @Override
    public HashMap<String, String> getDefaultValues() {
        return defaultValues;
    }

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void setId(final String id) {
        this.id = id;

    }

    @Override
    public void setType(final String type) {
        this.type = type;
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    // special labels
    public void setLabelFrom(final String label) {
        labelFrom = label;
    }

    public void setLabelTo(final String label) {
        labelTo = label;
    }

    @Override
    public void setvalues(final HashMap<String, String> values) {
        this.values = values;
    }

    @Override
    public void setDefaultValues(final HashMap<String, String> defaultValues) {
        this.defaultValues = defaultValues;
    }

    @Override
    public String getCssCode() {

        final StringBuffer sb = new StringBuffer();
        sb.append("#").append(getId()).append("_from{").append("\n");
        sb.append("    float: right;").append("\n");
        sb.append("    margin-bottom: 5px;").append("\n");
        sb.append("    width: 110px;").append("\n");
        sb.append("}").append("\n");
        sb.append("#").append(getId()).append("_to{").append("\n");
        sb.append("float: right;").append("\n");
        sb.append("width: 110px;").append("\n");
        sb.append("}").append("\n");

        sb.append("#form-report-ipt-dr-").append(getId()).append(" span {").append("\n");
        sb.append("    float: left;").append("\n");
        sb.append("}").append("\n");

        sb.append("#form-report-ipt-dr-").append(getId()).append(" > div {").append("\n");
        sb.append("    clear: both;").append("\n");
        sb.append("    width: 150px;").append("\n");
        sb.append("}").append("\n");

        return sb.toString();
    }

    @Override
    public String getJsCode() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<script type='text/javascript'>").append("\n");
        sb.append("$(function(){").append("\n");

        sb.append("$('#").append(getId()).append("_from, #").append(getId()).append("_to').daterangepicker(").append("\n");
        sb.append("	{").append("\n");
        sb.append("		presetRanges: [").append("\n");
        sb.append("			{text: 'Today', dateStart: 'today', dateEnd: 'today' },").append("\n");
        sb.append("			{text: 'Yesterday', dateStart: 'Yesterday', dateEnd: 'Yesterday' },").append("\n");
        sb.append("			{text: 'Last 7 days', dateStart: 'today-7days', dateEnd: 'today' },").append("\n");
        sb.append("			{text: 'Last 30 days', dateStart: 'today-30days', dateEnd: 'today' },").append("\n");
        sb.append("			{text: 'Month to date', dateStart: function(){ return Date.parse('today').moveToFirstDayOfMonth(); }, dateEnd: 'today' },").append("\n");
        sb.append(
                "			{text: 'The previous Month', dateStart: function(){ return Date.parse('1 month ago').moveToFirstDayOfMonth(); }, dateEnd: function(){ return Date.parse('1 month ago').moveToLastDayOfMonth(); } }")
                .append("\n");
        sb.append("	 	],").append("\n");
        sb.append("	 	presets: {").append("\n");
        sb.append("	 		specificDate: 'Specific Date',").append("\n");
        sb.append("	 		dateRange: 'Date Range'").append("\n");
        sb.append("	 	},").append("\n");

        // TODO be sure the dateJsFormat is set !!
        if (getJsDateFormat() != null) {
            sb.append("	 	dateFormat: '").append(getJsDateFormat()).append("', ").append("\n");
        }

        sb.append("	 	constrainDates: true, ").append("\n");
        sb.append("	 	onClose: function() {").append("\n");
        sb.append("	 		 setTimeout(function(){$('#").append(getId()).append("_from').closest('form').submit();},500);").append("\n");
        sb.append("	 	}").append("\n");
        sb.append("	});").append("\n");

        if (getDefaultValues() != null && getDefaultValues().get("from") != null) {
            sb.append("	$('#").append(getId()).append("_from').datepicker('setDate', Date.parseExact('").append(getDefaultValues().get("from")).append("', '")
                    .append(getLocaleDateFormat()).append("')); ").append("\n");
            sb.append("	$('#").append(getId()).append("_from').val($.datepicker.formatDate('").append(getJsDateFormat()).append("', Date.parseExact('")
                    .append(getDefaultValues().get("from")).append("', '").append(getLocaleDateFormat()).append("'))); ").append("\n");
        }
        if (getDefaultValues() != null && getDefaultValues().get("to") != null) {
            sb.append("	$('#").append(getId()).append("_to').datepicker('setDate', Date.parseExact('").append(getDefaultValues().get("to")).append("', '")
                    .append(getLocaleDateFormat()).append("')); ").append("\n");
            sb.append("	$('#").append(getId()).append("_to').val($.datepicker.formatDate('").append(getJsDateFormat()).append("', Date.parseExact('")
                    .append(getDefaultValues().get("to")).append("', '").append(getLocaleDateFormat()).append("'))); ").append("\n");

        }

        sb.append("});").append("\n");

        sb.append("</script>").append("\n");
        return sb.toString();
    }

    @Override
    public HashMap<String, String> getJsLibraries() {

        jsLibraries = new HashMap<String, String>();
        // TODO get a system JS PATH

        return jsLibraries;
    }

    @Override
    public HashMap<String, String> getCssLibraries() {
        cssLibraries = new HashMap<String, String>();

        return cssLibraries;
    }

    @Override
    public String getBodyHtml() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<div class='formentry formentry_duedate dueDate' id='form-report-ct-dr-").append(getId()).append("'>").append("\n");
        sb.append("	<div class='label'  id='form-report-lbl-dr-").append(getId()).append("'>").append("\n");
        sb.append("		<label for='").append(getId()).append("'>").append(getLabel()).append("</label>").append("\n");
        sb.append("	</div>").append("\n");
        sb.append("	<div class='input'  id='form-report-ipt-dr-").append(getId()).append("'>").append("\n");
        // from
        sb.append("		<div><span>").append(getLabelFrom()).append("</span><input type='text'  id='").append(getId()).append("_from' name='").append(getId())
                .append("_from'");

        sb.append(" class='' readonly='readonly'/></div>").append("\n");
        // to
        sb.append("		<div><span>").append(getLabelTo()).append("</span><input type='text'  id='").append(getId()).append("_to' name='").append(getId())
                .append("_to'");


        sb.append(" class='' readonly='readonly'/></div>").append("\n");

        sb.append("	</div>").append("\n");
        sb.append("</div>").append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("name:").append(name).append(", ");
        sb.append("label:").append(label).append(", ");
        sb.append("type:").append(type).append(", ");
        sb.append("id:").append(id).append(", ");
        if (values != null) {
            sb.append("values: {").append(values.toString()).append("}, ");
        } else {
            sb.append("values: {nil}, ");
        }
        if (defaultValues != null) {
            sb.append("defaultValues: {").append(defaultValues.toString()).append("}, ");
        }
        else {
            sb.append("defaultValues: {nil}, ");
        }
        if (jsLibraries != null) {
            sb.append("jsLibraries: {").append(jsLibraries.toString()).append("}, ");
        }
        else {
            sb.append("jsLibraries: {nil}, ");
        }

        return sb.toString();
    }

    /**
     * get the date format for java script
     * base on jquery date picker
     * Available formats: http://docs.jquery.com/UI/Datepicker/%24.datepicker.formatDate
     * 
     * @return
     */

    private String getLocaleDateFormat() {
        try {
            // TODO be sure the dateFormat is set !!
            return bonitaSytem.getCurrentLocaleDateFormat();
        } catch (final BonitaReportException e) {
            return null;
        }
    }

    /**
     * get the date format for java script
     * base on jquery date picker
     * Available formats: http://docs.jquery.com/UI/Datepicker/%24.datepicker.formatDate
     * 
     * @return
     */
    private String getJsDateFormat() {

        try {
            // TODO be sure the dateJsFormat is set !!
            return bonitaSytem.getCurrentLocaleJsDateFormat();
        } catch (final BonitaReportException e) {
            return null;
        }
    }

    /**
     * system properties
     */
    @Override
    public BonitaSystem getBonitaSystem() {
        return bonitaSytem;
    }

    public void setBonitaSystem(final BonitaSystem sys) {
        bonitaSytem = sys;
    }

}
