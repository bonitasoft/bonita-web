package org.bonitasoft.web.rest.server;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Vincent Elcrin
 */
public class ListExtractorTest {

    class POJO {

        private String value;

        public POJO(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Test
    public void should_return_a_list_of_all_extracted_items() throws Exception {
        ListExtractor<String, POJO> extractor = new ListExtractor<String, POJO>();
        List<POJO> pojos = Arrays.asList(new POJO("value 1"), new POJO("value 2"));

        List<String> result = extractor.extract(pojos, new ListExtractor.Extractor<String, POJO>() {
            @Override
            public String extract(POJO entry) {
                return entry.getValue();
            }
        });

        assertThat(result).containsExactly("value 1", "value 2");
    }
}
