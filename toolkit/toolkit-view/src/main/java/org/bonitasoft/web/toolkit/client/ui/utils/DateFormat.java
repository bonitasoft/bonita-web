/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.toolkit.client.ui.utils;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.common.CommonDateFormater;
import org.bonitasoft.web.toolkit.client.common.util.StringUtil;
import org.bonitasoft.web.toolkit.client.ui.utils.dateformat.RelativeStringDateFormatter;

/**
 * Available formats are
 * <ul>
 * <li>long : a simple long value representing the date in milliseconds</li>
 * <li>SQL (JSON compatible) : yyyy-MM-dd HH:mm:ss.SSS</li>
 * <li>Form input : MM/dd/YY (localized format)</li>
 * <li>Display as a short date : MM/dd/YY (localized format)</li>
 * <li>Display as a full date with time : MM/dd/YYYY HH:mm (localized format)</li>
 * <li>Display as time relative to current time : "1 hour ago", "in 5 minutes", "2 years ago"</li>
 * </ul>
 * 
 * @author Séverin Moussel
 * 
 */
// TODO : 
// * pull out all kind of date formatter in different classes like RelativeStringDateFormatter
// * make an interface implemented by all date formatter
// * make a switch return a polimorph DateFormatter and just call dateFormatter.format(...)
public abstract class DateFormat {

    private static RelativeStringDateFormatter relativeStringDateFormatter = new RelativeStringDateFormatter();
    
    public static enum UNIT {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, MILLISECOND
    };

    public static enum FORMAT {
        SQL("yyyy-MM-dd HH:mm:ss.SSS"),
        FORM(_("MM/dd/yyyy")),
        DISPLAY(_("MM/dd/yyyy 'at' HH:mm")),
        DISPLAY_SHORT(_("MMMM dd, yyyy")),
        LONG,
        DISPLAY_RELATIVE;

        private final String formatString;

        private FORMAT() {
            this("");
        }

        private FORMAT(final String formatString) {
            this.formatString = formatString;
        }

        public String getFormatString() {
            return this.formatString;
        }
    };

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERIC TO RELATIVE conversion
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String formatToDisplayRelative(final String date, final FORMAT format) {
        if (StringUtil.isBlank(date)) {
            return null;
        }

        switch (format) {
            case DISPLAY:
                return displayToDisplayRelative(date);
            case DISPLAY_SHORT:
                return displayShortToDisplayRelative(date);
            case FORM:
                return formToDisplayRelative(date);
            case LONG:
                return longToDisplayRelative(date);
            case SQL:
                return sqlToDisplayRelative(date);
            default:
                return date;
        }
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERIC TO DATE conversion
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Date formatToDate(final String date, final FORMAT format) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }

        if (format.equals(FORMAT.LONG)) {
            new Date(Long.parseLong(date));
        }

        return formatToDate(date, format.getFormatString());
    }

    public static Date formatToDate(final String date, final String format) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }

        return CommonDateFormater.parse(date, format);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERIC TO LONG CONVERSION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Long formatToLong(final String date, final FORMAT format) throws IllegalArgumentException {
        if (date == null || date.isEmpty()) {
            return null;
        }

        if (format.equals(FORMAT.LONG)) {
            return Long.parseLong(date);
        }

        return formatToLong(date, format.getFormatString());
    }

    public static Long formatToLong(final String date, final String format) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        final Date _date = CommonDateFormater.parse(date, format);
        return _date.getTime();
    }

    // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // GENERIC FORMAT CONVERSION
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String formatToFormat(final String date, final FORMAT inputFormat, final FORMAT outputFormat) throws IllegalArgumentException {
        if (FORMAT.DISPLAY_RELATIVE.equals(outputFormat)) {
            return formatToDisplayRelative(date, inputFormat);
        } else {
            return formatToFormat(date, inputFormat.getFormatString(), outputFormat.getFormatString());
        }
    }

    public static String formatToFormat(final String date, final String inputFormat, final FORMAT outputFormat) throws IllegalArgumentException {
        return formatToFormat(date, inputFormat, outputFormat.getFormatString());
    }

    public static String formatToFormat(final String date, final FORMAT inputFormat, final String outputFormat) throws IllegalArgumentException {
        return formatToFormat(date, inputFormat.getFormatString(), outputFormat);
    }

    public static String formatToFormat(final String date, final String inputFormat, final String outputFormat) throws IllegalArgumentException {
        if (date == null || inputFormat.equals(outputFormat)) {
            return date;
        }

        if ("".equals(outputFormat)) {
            return String.valueOf(CommonDateFormater.parse(date, inputFormat).getTime());
        }
        return CommonDateFormater.toString(CommonDateFormater.parse(date, inputFormat), outputFormat);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LONG TO ???
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String longToFormat(final Long date, final String format) {
        if (date == null) {
            return null;
        }
        return CommonDateFormater.toString(new Date(date), format);
    }

    public static String longToSql(final Long date) {
        return longToFormat(date, FORMAT.SQL.getFormatString());
    }

    public static String longToForm(final Long date) {
        return longToFormat(date, FORMAT.FORM.getFormatString());
    }

    public static String longToDisplayShort(final Long date) {
        return longToFormat(date, FORMAT.DISPLAY_SHORT.getFormatString());
    }

    public static String longToDisplay(final Long date) {
        return longToFormat(date, FORMAT.DISPLAY.getFormatString());
    }

    public static Date longToDate(final Long date) {
        return new Date(date);
    }

    // // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DATE OBJECT CONVERSIONS
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String dateToFormat(final Date date, final FORMAT format) {
        return dateToFormat(date, format.getFormatString());
    }

    public static String dateToFormat(final Date date, final String format) {
        if (date == null) {
            return null;
        }
        return CommonDateFormater.toString(date, format);
    }

    public static String dateToSql(final Date date) {
        return dateToFormat(date, FORMAT.SQL.getFormatString());
    }

    public static String dateToForm(final Date date) {
        return dateToFormat(date, FORMAT.FORM.getFormatString());
    }

    public static String dateToDisplayShort(final Date date) {
        return dateToFormat(date, FORMAT.DISPLAY_SHORT.getFormatString());
    }

    public static String dateToDisplay(final Date date) {
        return dateToFormat(date, FORMAT.DISPLAY.getFormatString());
    }

    public static String dateToDisplayRelative(final Date date) {
        return relativeStringDateFormatter.format(date.getTime());
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // LONG STRING TO ???
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String longToFormat(final String date, final String format) {
        if (date == null) {
            return null;
        }
        return longToFormat(Long.valueOf(date), format);
    }

    public static String longToSql(final String date) {
        if (date == null) {
            return null;
        }
        return longToSql(Long.valueOf(date));
    }

    public static String longToForm(final String date) {
        if (date == null) {
            return null;
        }
        return longToForm(Long.valueOf(date));
    }

    public static String longToDisplayShort(final String date) {
        if (date == null) {
            return null;
        }
        return longToDisplayShort(Long.valueOf(date));
    }

    public static String longToDisplay(final String date) {
        if (date == null) {
            return null;
        }
        return longToDisplay(Long.valueOf(date));
    }

    public static String longToDisplayRelative(final String date) {
        if (date == null) {
            return null;
        }
        return relativeStringDateFormatter.format(Long.valueOf(date));
    }

    public static Date longToDate(final String date) {
        return longToDate(Long.valueOf(date));
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SQL TO ???
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static Long sqlToLong(final String date) {
        return formatToLong(date, FORMAT.SQL);
    }

    public static String sqlToFormat(final String date, final String format) {
        return longToFormat(sqlToLong(date), format);
    }

    public static String sqlToForm(final String date) {
        return sqlToFormat(date, FORMAT.FORM.getFormatString());
    }

    public static String sqlToDisplayShort(final String date) {
        return sqlToFormat(date, FORMAT.DISPLAY_SHORT.getFormatString());
    }

    public static String sqlToDisplay(final String date) {
        return sqlToFormat(date, FORMAT.DISPLAY.getFormatString());
    }

    public static String sqlToDisplayRelative(final String date) {
        return relativeStringDateFormatter.format(sqlToLong(date));
    }

    public static Date sqlToDate(final String date) {
        return formatToDate(date, FORMAT.SQL);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FORM TO ???
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static long formToLong(final String date) {
        return formatToLong(date, FORMAT.FORM);
    }

    public static String formToFormat(final String date, final String format) {
        return longToFormat(formToLong(date), format);
    }

    public static String formToSql(final String date) {
        return formToFormat(date, FORMAT.SQL.getFormatString());
    }

    public static String formToDisplayShort(final String date) {
        return formToFormat(date, FORMAT.DISPLAY_SHORT.getFormatString());
    }

    public static String formToDisplay(final String date) {
        return formToFormat(date, FORMAT.DISPLAY.getFormatString());
    }

    public static String formToDisplayRelative(final String date) {
        return relativeStringDateFormatter.format(formToLong(date));
    }

    public static Date formToDate(final String date) {
        return formatToDate(date, FORMAT.FORM);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DISPLAY SHORT TO ???
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static long displayShortToLong(final String date) {
        return formatToLong(date, FORMAT.DISPLAY_SHORT);
    }

    public static String displayShortToFormat(final String date, final String format) {
        return longToFormat(displayShortToLong(date), format);
    }

    public static String displayShortToSql(final String date) {
        return displayShortToFormat(date, FORMAT.SQL.getFormatString());
    }

    public static String displayShortToForm(final String date) {
        return displayShortToFormat(date, FORMAT.FORM.getFormatString());
    }

    public static String displayShortToDisplay(final String date) {
        return displayShortToFormat(date, FORMAT.DISPLAY.getFormatString());
    }

    public static String displayShortToDisplayRelative(final String date) {
        return relativeStringDateFormatter.format(displayShortToLong(date));
    }

    public static Date displayShortToDate(final String date) {
        return formatToDate(date, FORMAT.DISPLAY_SHORT);
    }

    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DISPLAY TO ???
    // //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static long displayToLong(final String date) {
        return formatToLong(date, FORMAT.DISPLAY);
    }

    public static String displayToFormat(final String date, final String format) {
        return longToFormat(displayToLong(date), format);
    }

    public static String displayToSql(final String date) {
        return displayToFormat(date, FORMAT.SQL.getFormatString());
    }

    public static String displayToForm(final String date) {
        return displayToFormat(date, FORMAT.FORM.getFormatString());
    }

    public static String displayToDisplayShort(final String date) {
        return displayToFormat(date, FORMAT.DISPLAY_SHORT.getFormatString());
    }

    public static String displayToDisplayRelative(final String date) {
        return relativeStringDateFormatter.format(displayToLong(date));
    }

    public static Date displayToDate(final String date) {
        return formatToDate(date, FORMAT.DISPLAY);
    }

}
