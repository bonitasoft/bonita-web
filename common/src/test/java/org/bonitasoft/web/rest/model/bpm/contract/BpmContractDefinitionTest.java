package org.bonitasoft.web.rest.model.bpm.contract;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BpmContractDefinitionTest {

    @Test
    public void should_define_primary_key() throws Exception {
        //given
        final BpmContractDefinition bpmContractDefinition = new BpmContractDefinition();

        //when
        final ArrayList<String> primaryKeys = bpmContractDefinition.getPrimaryKeys();

        //then
        assertThat(primaryKeys).as("should define primary key").isNotNull().hasSize(1);

    }

    @Test
    public void should_define_item_definition() throws Exception {

        //when
        final BpmContractDefinition bpmContractDefinition = BpmContractDefinition.get();

        assertThat(bpmContractDefinition).as("should define item").isNotNull();
    }
}
