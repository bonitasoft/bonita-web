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
package org.bonitasoft.web.toolkit.client.data;

import org.bonitasoft.console.common.FakeI18n;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.bonitasoft.web.toolkit.client.common.json.JSonSerializer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dumitru Corini
 *
 */
public class APIIDTest {

    @Test
    public void makeAPIID_with_list_should_return_correct_result() throws Exception {
        List<String> ids = new ArrayList<>();
        ids.add("1");
        APIID result = APIID.makeAPIID(ids);
        assertThat(result.toLong()).isEqualTo(1L);

        ids = new ArrayList<>();
        ids.add("-1");
        result = APIID.makeAPIID(ids);
        assertThat(result.toLong()).isEqualTo(-1L);

        ids = new ArrayList<>();
        ids.add("1");
        ids.add("6");
        ids.add("-1");
        result = APIID.makeAPIID(ids);
        assertThat(result.getIds()).isEqualTo(ids);

        ids = new ArrayList<>();
        ids.add("");
        ids.add("1");
        ids.add("-1");
        ids.add("instantiation");
        ids.add(null);
        result = APIID.makeAPIID(ids);
        assertThat(result.getIds()).isEqualTo(ids);
    }

    @Test
    public void makeAPIID_with_array_should_return_correct_result() throws Exception {
        Long[] ids = new Long[]{1L};
        APIID result = APIID.makeAPIID(ids);
        assertThat(result.toLong()).isEqualTo(1L);

        ids = new Long[]{1L, null, 6L, -1L};
        List<String> resultingAPIIDs = new ArrayList<>();
        resultingAPIIDs.add("1");
        resultingAPIIDs.add(null);
        resultingAPIIDs.add("6");
        resultingAPIIDs.add("-1");
        result = APIID.makeAPIID(ids);
        assertThat(result.getIds()).isEqualTo(resultingAPIIDs);
    }
}
