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
package org.bonitasoft.test.toolkit.bpm;

import java.util.List;

import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.test.toolkit.TestToolkitUtils;
import org.bonitasoft.test.toolkit.organization.TestToolkitCtx;

/**
 * @author Vincent Elcrin
 * 
 */
public abstract class AbstractManualTask {

    public abstract long getId();

    public abstract String getName();

    public abstract String getDescription();

    // /////////////////////////////////////////////////////////////////////////////
    // / Test state
    // /////////////////////////////////////////////////////////////////////////////

    /**
     * Using engine's process api, check that the human task is pending
     * 
     * @param apiSession
     * @return
     */
    private boolean isPending(final APISession apiSession) {
        boolean pending = false;
        List<HumanTaskInstance> result = TestToolkitUtils.getInstance().searchPendingTasksForUser(0, 100);
        for (HumanTaskInstance instance : result) {
            if (instance.getId() == getId()) {
                pending = true;
                break;
            }
        }

        return pending;
    }

    public boolean isPending() {
        return isPending(TestToolkitCtx.getInstance().getInitiator().getSession());
    }

}
