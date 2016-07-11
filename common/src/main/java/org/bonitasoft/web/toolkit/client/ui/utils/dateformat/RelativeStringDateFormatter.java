/**
 * Copyright (C) 2012 BonitaSoft S.A.
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
package org.bonitasoft.web.toolkit.client.ui.utils.dateformat;

import static org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n._;

import java.util.Date;

import org.bonitasoft.web.toolkit.client.common.texttemplate.Arg;
import org.bonitasoft.web.toolkit.client.ui.utils.DateFormat.UNIT;

/**
 * @author Colin PUY
 */
public class RelativeStringDateFormatter {

    public String format(Long date) {
        long dateSpan = new Date().getTime() - date;
        boolean ago = dateSpan > 0;

        // In seconds
        long value = (long) Math.abs(Math.floor(dateSpan / 1000));
        if (value < 60) {
            return makeRelativeString(value, UNIT.SECOND, ago);
        }

        // In minutes
        value = (long) Math.abs(Math.floor(value / 60));
        if (value < 60) {
            return makeRelativeString(value, UNIT.MINUTE, ago);
        }

        // In hours
        value = (long) Math.abs(Math.floor(value / 60));
        if (value < 24) {
            return makeRelativeString(value, UNIT.HOUR, ago);
        }

        // In days
        value = (long) Math.abs(Math.floor(value / 24));
        if (value < 30) {
            return makeRelativeString(value, UNIT.DAY, ago);
        }

        // In months
        value = (long) Math.abs(Math.floor(value / 30));
        if (value < 12) {
            return makeRelativeString(value, UNIT.MONTH, ago);
        }

        // In years
        return makeRelativeString(value, UNIT.MONTH, ago);
    }

    protected String makeRelativeString(final long time, final UNIT unit, final boolean ago) {
        String unitString = null;
        switch (unit) {
            case YEAR:
                unitString = time <= 1 ? _("yr") : _("yr");
                break;
            case MONTH:
                unitString = time <= 1 ? _("mo") : _("mo");
                break;
            case DAY:
                unitString = time <= 1 ? _("day") : _("day");
                break;
            case HOUR:
                unitString = time <= 1 ? _("hr") : _("hr");
                break;
            case MINUTE:
                unitString = time <= 1 ? _("min") : _("min");
                break;
            case SECOND:
                unitString = time <= 1 ? _("sec") : _("sec");
                break;
            default:
                // FIXME : uncomment throw, delete break
                // throw new IllegalArgumentException("Unsupported UNIT " + unit);
                break;
        }
        if (ago) {
            return _("%time% ago", new Arg("time", time + " " + unitString));
        } else {
            return _("in %time%", new Arg("time", time + " " + unitString));
        }
    }
}
