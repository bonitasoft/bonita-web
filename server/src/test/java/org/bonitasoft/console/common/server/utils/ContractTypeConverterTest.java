package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(MockitoJUnitRunner.class)
public class ContractTypeConverterTest {

    @Mock
    ContractDefinition contractDefinition;

    long maxSizeForTenant = 1000L;

    long tenantId = 1L;

    ContractTypeConverter contractTypeConverter = new ContractTypeConverter(ContractTypeConverter.ISO_8601_DATE_PATTERNS);

    @Test
    public void getProcessedInputs_with_empty_contract_should_return_unmodified_inputs() throws Exception {
        when(contractDefinition.getInputs()).thenReturn(Collections.<InputDefinition>emptyList());
        final Map<String, Serializable> input = new HashMap<>();
        input.put("input1", "value1");
        input.put("input2", "value2");

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId);

        assertThat(processedInput).isEqualTo(input);
    }

    @Test
    public void getProcessedInputs_with_simple_input_should_return_processed_input() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition();
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = generateInputMap();

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId);

        assertThat(processedInput).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(43200000L)),
                entry("inputInteger", 125686181L), entry("inputDecimal", 12.8));
    }

    @Test
    public void getProcessedInputs_with_complex_input_should_return_processed_input() throws Exception {
        final List<InputDefinition> inputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = new HashMap<>();
        final Map<String, Serializable> complexInput = generateInputMap();
        input.put("inputComplex", (Serializable) complexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId);
        assertThat(processedInput).containsKey("inputComplex");
        final Map<String, Serializable> processedComplexInput = (Map<String, Serializable>) processedInput.get("inputComplex");
        assertThat(processedComplexInput).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(43200000L)),
                entry("inputInteger", 125686181L), entry("inputDecimal", 12.8));
    }

    @Test
    public void getProcessedInputs_with_multiple_complex_input_should_return_processed_input() throws Exception {
        final List<InputDefinition> inputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = new HashMap<>();
        final Map<String, Serializable> complexInput = generateInputMap();
        final List<Serializable> multipleComplexInput = new ArrayList<>();
        multipleComplexInput.add((Serializable) complexInput);
        multipleComplexInput.add((Serializable) complexInput);
        input.put("inputComplex", (Serializable) multipleComplexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId);
        assertThat(processedInput).containsKey("inputComplex");
        final List<Serializable> processedMultipleComplexInput = (List<Serializable>) processedInput.get("inputComplex");
        assertThat(processedMultipleComplexInput).hasSize(2);
        for (final Serializable processedComplexInput : processedMultipleComplexInput) {
            final Map<String, Serializable> processedComplexInputMap = (Map<String, Serializable>) processedComplexInput;
            assertThat(processedComplexInputMap).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(43200000L)),
                    entry("inputInteger", 125686181L), entry("inputDecimal", 12.8));
        }
    }

    private Map<String, Serializable> generateInputMap() {
        final Map<String, Serializable> inputMap = new HashMap<>();
        inputMap.put("inputText", "text");
        inputMap.put("inputBoolean", "true");
        inputMap.put("inputDate", "1970-01-01T13:00:00.000Z");
        inputMap.put("inputInteger", 125686181);
        inputMap.put("inputDecimal", "12.8");
        return inputMap;
    }

    private List<InputDefinition> generateSimpleInputDefinition() {
        final List<InputDefinition> inputDefinitions = new ArrayList<>();
        final InputDefinition textInputDefinition = mock(InputDefinitionImpl.class);
        when(textInputDefinition.getType()).thenReturn(Type.TEXT);
        when(textInputDefinition.getName()).thenReturn("inputText");
        inputDefinitions.add(textInputDefinition);
        final InputDefinition booleanInputDefinition = mock(InputDefinitionImpl.class);
        when(booleanInputDefinition.getType()).thenReturn(Type.BOOLEAN);
        when(booleanInputDefinition.getName()).thenReturn("inputBoolean");
        inputDefinitions.add(booleanInputDefinition);
        final InputDefinition dateInputDefinition = mock(InputDefinitionImpl.class);
        when(dateInputDefinition.getType()).thenReturn(Type.DATE);
        when(dateInputDefinition.getName()).thenReturn("inputDate");
        inputDefinitions.add(dateInputDefinition);
        final InputDefinition integerInputDefinition = mock(InputDefinitionImpl.class);
        when(integerInputDefinition.getType()).thenReturn(Type.INTEGER);
        when(integerInputDefinition.getName()).thenReturn("inputInteger");
        inputDefinitions.add(integerInputDefinition);
        final InputDefinition decimalInputDefinition = mock(InputDefinitionImpl.class);
        when(decimalInputDefinition.getType()).thenReturn(Type.DECIMAL);
        when(decimalInputDefinition.getName()).thenReturn("inputDecimal");
        inputDefinitions.add(decimalInputDefinition);
        return inputDefinitions;
    }

    private List<InputDefinition> generateComplexInputDefinition() {
        final List<InputDefinition> inputDefinitions = new ArrayList<>();
        final InputDefinition inputDefinition = mock(InputDefinition.class);
        when(inputDefinition.getName()).thenReturn("inputComplex");
        when(inputDefinition.hasChildren()).thenReturn(true);
        final List<InputDefinition> childInputDefinitions = generateSimpleInputDefinition();
        when(inputDefinition.getInputs()).thenReturn(childInputDefinitions);
        inputDefinitions.add(inputDefinition);
        return inputDefinitions;
    }
}
