package org.bonitasoft.reporting.utils.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bonitasoft.reporting.exception.BonitaReportException;
import org.bonitasoft.reporting.utils.BonitaSystem;

public class ConverterToReportParameters {

    public static String getReportParamValues(
            final HttpServletRequest request,
            final BonitaSystem bonitaSystem,
            final Map<String, Object> parameters,
            final ArrayList<Widget> widgets,
            String pdfQueryString
            ) throws BonitaReportException {

        String paramValue;
        final SimpleDateFormat sdf = new SimpleDateFormat(
                bonitaSystem.getCurrentLocaleDateFormat(),
                BonitaSystem.getCurrentLocale(request));

        // set the time Zone
        sdf.setTimeZone(bonitaSystem.getCurrentTimeZone());

        // get all url parameters needed by the report
        String param = null;
        for (final Widget widget : widgets) {

            if (Parser.BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_DATE.equals(widget.getWidget()) ||
                    Parser.BONITA_JR_PROPERTY_FORM_WIDGET_TYPE_DATE_RANGE.equals(widget.getWidget())) {

                // date _from

                param = widget.getId() + "_from";
                paramValue = request.getParameter(param);

                String dateJs = null;

                if (paramValue != null && paramValue.indexOf("/") > 0) {

                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + paramValue;

                    final Date d = ConverterToReportParameters.strDateToDate(paramValue, sdf, bonitaSystem.getCurrentLocaleDateFormat());
                    final Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(bonitaSystem.getCurrentTimeZone());
                    cal.setTime(d);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);

                    parameters.put(param, cal.getTime());

                    dateJs = sdf.format(d);

                } else if (paramValue != null && paramValue.length() > 0) {

                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + paramValue;

                    final Date d = ConverterToReportParameters.strLongToDate(paramValue);

                    parameters.put(param, d);
                    dateJs = sdf.format(d);
                } else {
                    // set date to today
                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + sdf.format(new Date());
                    parameters.put(param, new Date());
                    dateJs = sdf.format(new Date());
                }

                // date _to
                param = widget.getId() + "_to";
                paramValue = request.getParameter(param);

                if (paramValue != null && paramValue.indexOf("/") > 0) {

                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + paramValue;

                    final Date d = ConverterToReportParameters.strDateToDate(paramValue, sdf, bonitaSystem.getCurrentLocaleDateFormat());
                    final Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(bonitaSystem.getCurrentTimeZone());
                    cal.setTime(d);
                    cal.add(Calendar.HOUR, 23);
                    cal.add(Calendar.MINUTE, 59);
                    cal.add(Calendar.SECOND, 59);

                    parameters.put(param, cal.getTime());

                    dateJs = dateJs != null ?
                            dateJs + " - " + sdf.format(d) : " - " + sdf.format(d);

                } else if (paramValue != null && paramValue.length() > 0) {

                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + paramValue;

                    final Date d = ConverterToReportParameters.strLongToDate(paramValue);

                    parameters.put(param, d);
                    dateJs = dateJs != null ?
                            dateJs + " - " + sdf.format(d) : " - " + sdf.format(d);
                } else {
                    // set date to today
                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + sdf.format(new Date());
                    parameters.put(param, new Date());
                    dateJs = sdf.format(new Date()) + " - " + sdf.format(new Date());
                }

                // set intial value to widget
                if (dateJs != null) {
                    widget.setInitialValue(dateJs);
                }

            } else {

                // TODO add other format Number
                // Long
                // Double
                param = widget.getId();
                paramValue = request.getParameter(param);

                if (paramValue != null) {

                    // save the param for pdf export
                    pdfQueryString = pdfQueryString + "&" + param + "=" + paramValue;

                    parameters.put(widget.getId(), paramValue);
                    widget.setInitialValue(paramValue);
                } else {
                    if (Parser.BONITA_JR_PROPERTY_WIDGET_HAS_ALL_TRUE.equals(widget.getHasAll())) {
                        // save the param for pdf export
                        pdfQueryString += "&" + param + "=" + widget.getHasAllValue();

                        parameters.put(widget.getId(), widget.getHasAllValue());
                        widget.setInitialValue(widget.getHasAllValue());
                    }
                }
            }
        }

        return pdfQueryString;
    }

    private static Date strDateToDate(final String val, final SimpleDateFormat sdf, final String dateFormat) throws BonitaReportException {

        try {
            final Date d = sdf.parse(val);
            return d;

        } catch (final ParseException e) {
            throw new BonitaReportException("An error occurs the date format is unappropriated. The date format expected is " + dateFormat, e);
        }
    }

    private static Date strLongToDate(final String val) throws BonitaReportException {

        try {

            Date d = null;
            final Long lds = Long.parseLong(val);

            if (lds != null) {
                d = new Date(lds.longValue());
            }

            return d;

        } catch (final NumberFormatException e) {
            throw new BonitaReportException("An error occurs the date format is unappropriated.", e);
        }
    }

}
