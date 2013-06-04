/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.server;

import static org.junit.Assert.assertTrue;

import java.util.Date;

/**
 * @author Baptiste Mesta
 */
public abstract class WaitUntil {

    private final int timeout;

    private final int repeatEach;

    private final boolean throwExceptions;

    /**
     * @param repeatEach
     *            time to wait for before retrying, if condition not fullfilled, in milliseconds
     * @param timeout
     *            max time to wait for, in milliseconds
     */
    public WaitUntil(final int repeatEach, final int timeout) {
        this(repeatEach, timeout, true);
    }

    /**
     * @param repeatEach
     *            time to wait for before retrying, if condition not fullfilled, in milliseconds
     * @param timeout
     *            max time to wait for, in milliseconds
     * @param throwExceptions
     *            can the check condition throw exceptions?
     */
    public WaitUntil(final int repeatEach, final int timeout, final boolean throwExceptions) {
        this.throwExceptions = throwExceptions;
        assertTrue("timeout is not big enough", repeatEach < timeout);
        this.repeatEach = repeatEach;
        this.timeout = timeout;
    }

    public boolean waitUntil() throws Exception {
        final long limit = new Date().getTime() + timeout;
        while (new Date().getTime() < limit) {
            Thread.sleep(repeatEach);
            if (checkCondition()) {
                return true;
            }
        }
        return checkCondition();
    }

    protected boolean checkCondition() throws Exception {
        if (throwExceptions) {
            return check();
        } else {
            try {
                return check();
            } catch (final Exception e) {
                // do nothing
            }
            return false;
        }
    };

    /**
     * Condition to check for.
     * 
     * @return true if condition is true, false otherwise.
     * @throws Exception
     */
    protected abstract boolean check() throws Exception;
}
