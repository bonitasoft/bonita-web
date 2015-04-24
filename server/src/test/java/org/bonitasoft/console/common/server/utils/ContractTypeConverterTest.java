package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.engine.bpm.contract.ComplexInputDefinition;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.SimpleInputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ContractTypeConverterTest {

    @Mock
    ContractDefinition contractDefinition;

    ContractTypeConverter contractTypeConverter = new ContractTypeConverter(ContractTypeConverter.ISO_8601_DATE_PATTERNS);

    @Test
    public void getProcessedInputs_with_empty_contract_should_return_unmodified_inputs() throws Exception {
        when(contractDefinition.getComplexInputs()).thenReturn(Collections.<ComplexInputDefinition> emptyList());
        when(contractDefinition.getSimpleInputs()).thenReturn(Collections.<SimpleInputDefinition> emptyList());
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        input.put("input1", "value1");
        input.put("input2", "value2");

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input);

        assertThat(processedInput).isEqualTo(input);
    }

    @Test
    public void getProcessedInputs_with_simple_input_should_return_processed_input() throws Exception {
        when(contractDefinition.getComplexInputs()).thenReturn(Collections.<ComplexInputDefinition> emptyList());
        final List<SimpleInputDefinition> simpleInputDefinition = generateSimpleInputDefinition();
        when(contractDefinition.getSimpleInputs()).thenReturn(simpleInputDefinition);
        final Map<String, Serializable> input = generateInputMap();

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input);

        assertThat(processedInput).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(0L)),
                entry("inputInteger", 125686181L), entry("inputDecimal", new Double(12.8)));
    }

    @Test
    public void getProcessedInputs_with_complex_input_should_return_processed_input() throws Exception {
        final List<ComplexInputDefinition> complexInputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getComplexInputs()).thenReturn(complexInputDefinition);
        when(contractDefinition.getSimpleInputs()).thenReturn(Collections.<SimpleInputDefinition> emptyList());
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        final Map<String, Serializable> complexInput = generateInputMap();
        input.put("inputComplex", (Serializable) complexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input);
        assertThat(processedInput).containsKey("inputComplex");
        final Map<String, Serializable> processedComplexInput = (Map<String, Serializable>) processedInput.get("inputComplex");
        assertThat(processedComplexInput).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(0L)),
                entry("inputInteger", 125686181L), entry("inputDecimal", new Double(12.8)));
    }

    @Test
    public void getProcessedInputs_with_multiple_complex_input_should_return_processed_input() throws Exception {
        final List<ComplexInputDefinition> complexInputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getComplexInputs()).thenReturn(complexInputDefinition);
        when(contractDefinition.getSimpleInputs()).thenReturn(Collections.<SimpleInputDefinition> emptyList());
        final Map<String, Serializable> input = new HashMap<String, Serializable>();
        final Map<String, Serializable> complexInput = generateInputMap();
        final List<Serializable> multipleComplexInput = new ArrayList<Serializable>();
        multipleComplexInput.add((Serializable) complexInput);
        multipleComplexInput.add((Serializable) complexInput);
        input.put("inputComplex", (Serializable) multipleComplexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input);
        assertThat(processedInput).containsKey("inputComplex");
        final List<Serializable> processedMultipleComplexInput = (List<Serializable>) processedInput.get("inputComplex");
        assertThat(processedMultipleComplexInput).hasSize(2);
        for (final Serializable processedComplexInput : processedMultipleComplexInput) {
            final Map<String, Serializable> processedComplexInputMap = (Map<String, Serializable>) processedComplexInput;
            assertThat(processedComplexInputMap).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(0L)),
                    entry("inputInteger", 125686181L), entry("inputDecimal", new Double(12.8)));
        }
    }

    private Map<String, Serializable> generateInputMap() {
        final Map<String, Serializable> inputMap = new HashMap<String, Serializable>();
        inputMap.put("inputText", "text");
        inputMap.put("inputBoolean", "true");
        inputMap.put("inputDate", "1970-01-01T01:00:00.000Z");
        inputMap.put("inputInteger", 125686181);
        inputMap.put("inputDecimal", "12.8");
        return inputMap;
    }

    private List<SimpleInputDefinition> generateSimpleInputDefinition() {
        final List<SimpleInputDefinition> simpleInputDefinitions = new ArrayList<SimpleInputDefinition>();
        final SimpleInputDefinition textInputDefinition = mock(SimpleInputDefinition.class);
        when(textInputDefinition.getType()).thenReturn(Type.TEXT);
        when(textInputDefinition.getName()).thenReturn("inputText");
        simpleInputDefinitions.add(textInputDefinition);
        final SimpleInputDefinition booleanInputDefinition = mock(SimpleInputDefinition.class);
        when(booleanInputDefinition.getType()).thenReturn(Type.BOOLEAN);
        when(booleanInputDefinition.getName()).thenReturn("inputBoolean");
        simpleInputDefinitions.add(booleanInputDefinition);
        final SimpleInputDefinition dateInputDefinition = mock(SimpleInputDefinition.class);
        when(dateInputDefinition.getType()).thenReturn(Type.DATE);
        when(dateInputDefinition.getName()).thenReturn("inputDate");
        simpleInputDefinitions.add(dateInputDefinition);
        final SimpleInputDefinition integerInputDefinition = mock(SimpleInputDefinition.class);
        when(integerInputDefinition.getType()).thenReturn(Type.INTEGER);
        when(integerInputDefinition.getName()).thenReturn("inputInteger");
        simpleInputDefinitions.add(integerInputDefinition);
        final SimpleInputDefinition decimalInputDefinition = mock(SimpleInputDefinition.class);
        when(decimalInputDefinition.getType()).thenReturn(Type.DECIMAL);
        when(decimalInputDefinition.getName()).thenReturn("inputDecimal");
        simpleInputDefinitions.add(decimalInputDefinition);
        return simpleInputDefinitions;
    }

    private List<ComplexInputDefinition> generateComplexInputDefinition() {
        final List<ComplexInputDefinition> complexInputDefinitions = new ArrayList<ComplexInputDefinition>();
        final ComplexInputDefinition complexInputDefinition = mock(ComplexInputDefinition.class);
        when(complexInputDefinition.getName()).thenReturn("inputComplex");
        final List<SimpleInputDefinition> simpleInputDefinitions = generateSimpleInputDefinition();
        when(complexInputDefinition.getSimpleInputs()).thenReturn(simpleInputDefinitions);
        complexInputDefinitions.add(complexInputDefinition);
        return complexInputDefinitions;
    }
}
