package org.bonitasoft.reporting.form.components;

import java.util.HashMap;

import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.form.HtmlRenderer;
import org.bonitasoft.reporting.utils.BonitaSystem;

public class DateComponent implements IComponent {

    private String name;

    private String label;

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
        sb.append("#").append(getId()).append("{").append("\n");
        sb.append("}").append("\n");
        return sb.toString();
    }

    @Override
    public String getJsCode() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<script type='text/javascript'>").append("\n");
        sb.append("$(function(){").append("\n");

        sb.append("$('#").append(getId()).append("').daterangepicker(").append("\n");
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
        sb.append("	 	presets: {},").append("\n");
        // Force date between -5 Years and +1 Year
        // earliestDate: Date.parse('-15years'), //earliest date allowed
        // latestDate: Date.parse('+15years'), //latest date allowed
        sb.append("	 	earliestDate: Date.parse('-5years'), ").append("\n");
        sb.append("	 	latestDate: Date.parse('+1years'),").append("\n");

        // TODO be sure the dateJsFormat is set !!
        if (getJsDateFormat() != null) {
            sb.append("	 	dateFormat: '").append(getJsDateFormat()).append("', ").append("\n");
        }

        sb.append("	 	onChange:function(){").append("\n");
        sb.append("	 		if($('#").append(getId()).append("').val().indexOf(' - ') > 0){").append("\n");
        sb.append("	 			dates =  $('#").append(getId()).append("').val().split(' - ');").append("\n");
        // sb.append("	 			alert(dates[0] + dates[1]);").append("\n");
        sb.append("	 			$('#").append(getId()).append("_from').val(dates[0]);").append("\n");
        sb.append("	 			$('#").append(getId()).append("_to').val(dates[1]);").append("\n");
        sb.append("				$('#").append(getId()).append("').closest('form').submit();").append("\n");
        sb.append("	 		}else{").append("\n");
        sb.append("	 			$('#").append(getId()).append("_from').val($('#").append(getId()).append("').val());").append("\n");
        sb.append("	 			$('#").append(getId()).append("_to').val($('#").append(getId()).append("').val());").append("\n");
        sb.append("				$('#").append(getId()).append("').closest('form').submit();").append("\n");
        sb.append("	 		}").append("\n");
        sb.append("	 	}").append("\n");
        sb.append("	 });").append("\n");
        sb.append("});").append("\n");

        sb.append("</script>").append("\n");
        return sb.toString();
    }

    @Override
    public HashMap<String, String> getCssLibraries() {

        cssLibraries = new HashMap<String, String>();

        return cssLibraries;
    }

    @Override
    public HashMap<String, String> getJsLibraries() {

        jsLibraries = new HashMap<String, String>();
        jsLibraries.put("date.js", HtmlRenderer.getJsPath() + "date.js");
        jsLibraries.put("daterangepicker.jQuery.js", HtmlRenderer.getJsPath() + "daterangepicker.jQuery.js");

        return jsLibraries;
    }

    @Override
    public String getBodyHtml() {
        final StringBuffer sb = new StringBuffer();
        sb.append("<div class='formentry formentry_duedate dueDate' id='form-report-ct-dt-").append(getId()).append("'>").append("\n");
        sb.append("	<div class='label'  id='form-report-lbl-dt-").append(getId()).append("'>").append("\n");
        sb.append("		<label for='").append(getId()).append("'>").append(getLabel()).append("</label>").append("\n");
        sb.append("	</div>").append("\n");
        sb.append("	<div class='input'  id='form-report-ipt-dt-").append(getId()).append("'>").append("\n");
        sb.append("		<input type='hidden'  id='").append(getId()).append("_from' name='").append(getId()).append("_from'/>").append("\n");
        sb.append("		<input type='hidden'  id='").append(getId()).append("_to' name='").append(getId()).append("_to'/>").append("\n");
        sb.append("		<input type='text'  id='").append(getId()).append("' name='").append(getId()).append("'");
        if (getDefaultValues() != null && getDefaultValues().get("default") != null) {
            sb.append(" value='").append(getDefaultValues().get("default")).append("'");
        }
        sb.append(" class='' readonly='readonly'/>").append("\n");
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
            sb.append(" values: {").append(values.toString()).append("}, ");
        } else {
            sb.append(" values: {nil}, ");
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
