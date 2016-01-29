package org.bonitasoft.console.common.server.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.FileInputValue;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class ContractTypeConverterTest {

    public static final long DATE_01_01_1970_13H_AS_LONG_GMT = 46800000L;
    
    public static final String DATE_01_01_1970_13H_AS_STRING_GMT = "1970-01-01T13:00:00.000Z";

    @Mock
    ContractDefinition contractDefinition;

    @Mock
    BonitaHomeFolderAccessor bonitaHomeFolderAccessor;

    long maxSizeForTenant = 1000L;

    long tenantId = 1L;

    String filename = "file.txt";

    String fileContentString = "content";

    @Spy
    @InjectMocks
    ContractTypeConverter contractTypeConverter = new ContractTypeConverter(ContractTypeConverter.ISO_8601_DATE_PATTERNS);

    private File generateTempFile() throws IOException {
        final File tempFile = File.createTempFile(this.getClass().getName(), null);
        tempFile.deleteOnExit();
        FileUtils.writeByteArrayToFile(tempFile, fileContentString.getBytes("UTF-8"));
        return tempFile;
    }

    @Test
    public void getProcessedInputs_with_empty_contract_should_return_unmodified_inputs() throws Exception {
        when(contractDefinition.getInputs()).thenReturn(Collections.<InputDefinition>emptyList());
        final Map<String, Serializable> input = new HashMap<>();
        input.put("input1", "value1");
        input.put("input2", "value2");

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).isEqualTo(input);
    }

    @Test
    public void getProcessedInput_when_no_contract() throws Exception {
        final Map<String, Serializable> input = new HashMap<>();

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(null, input, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).isEqualTo(input);
    }

    @Test
    public void getProcessedInput_when_null_input() throws Exception {
        when(contractDefinition.getInputs()).thenReturn(Collections.<InputDefinition> emptyList());

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, null, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).isEqualTo(new HashMap<>());
    }

    @Test
    public void getProcessedInputs_with_invalid_inputs_should_return_unmodified_inputs() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition(false);
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = generateInvalidInputMap();

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).containsOnly(entry("inputText", "0"), entry("inputBoolean", "hello"), entry("inputDate", 0),
                entry("inputInteger", "hello"), entry("inputDecimal", "hello"));
    }

    @Test
    public void getProcessedInputs_with_simple_input_should_return_processed_input() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition(true);
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final String tempFilePath = "tempFile";
        final File tempFile = generateTempFile();
        doReturn(tempFile).when(bonitaHomeFolderAccessor).getTempFile(tempFilePath, tenantId);
        final Map<String, Serializable> input = generateInputMap(tempFilePath);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(DATE_01_01_1970_13H_AS_LONG_GMT)),
                entry("inputInteger", 125686181), entry("inputDecimal", 12.8),
                entry("inputFile", new FileInputValue(filename, "", fileContentString.getBytes("UTF-8"))));
    }

    @Test
    public void getProcessedInputs_with_complex_input_should_return_processed_input() throws Exception {
        final List<InputDefinition> inputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = new HashMap<>();
        final String tempFilePath = "tempFile";
        final File tempFile = generateTempFile();
        doReturn(tempFile).when(bonitaHomeFolderAccessor).getTempFile(tempFilePath, tenantId);
        final Map<String, Serializable> complexInput = generateInputMap(tempFilePath);
        input.put("inputComplex", (Serializable) complexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);
        assertThat(processedInput).containsKey("inputComplex");
        final Map<String, Serializable> processedComplexInput = (Map<String, Serializable>) processedInput.get("inputComplex");
        assertThat(processedComplexInput).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(DATE_01_01_1970_13H_AS_LONG_GMT)),
                entry("inputInteger", 125686181), entry("inputDecimal", 12.8),
                entry("inputFile", new FileInputValue(filename, "", fileContentString.getBytes("UTF-8"))));
    }

    @Test
    public void getProcessedInputs_with_multiple_complex_input_should_return_processed_input() throws Exception {
        final List<InputDefinition> inputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = new HashMap<>();
        final Map<String, Serializable> complexInput = generateInputMapWithFile("tempFile");
        final Map<String, Serializable> complexInput2 = generateInputMapWithFile("tempFile2");
        final List<Serializable> multipleComplexInput = new ArrayList<>();
        multipleComplexInput.add((Serializable) complexInput);
        multipleComplexInput.add((Serializable) complexInput2);
        input.put("inputComplex", (Serializable) multipleComplexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);
        assertThat(processedInput).containsKey("inputComplex");
        final List<Serializable> processedMultipleComplexInput = (List<Serializable>) processedInput.get("inputComplex");
        assertThat(processedMultipleComplexInput).hasSize(2);
        for (final Serializable processedComplexInput : processedMultipleComplexInput) {
            final Map<String, Serializable> processedComplexInputMap = (Map<String, Serializable>) processedComplexInput;
            assertThat(processedComplexInputMap).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(DATE_01_01_1970_13H_AS_LONG_GMT)),
                    entry("inputInteger", 125686181), entry("inputDecimal", 12.8),
                    entry("inputFile", new FileInputValue(filename, "", fileContentString.getBytes("UTF-8"))));
        }
    }

    @Test
    public void getProcessedInputs_with_simple_input_should_return_processed_input_with_null() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition(true);
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = generateInputMapWithNull();

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).containsOnly(entry("inputText", null), entry("inputBoolean", null), entry("inputDate", null),
                entry("inputInteger", null), entry("inputDecimal", null), entry("inputFile", null));
    }

    @Test
    public void getProcessedInputs_with_simple_input_should_return_processed_input_with_empty_map() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition(true);
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = generateInputMapWithEmptyFileInput();

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);

        assertThat(processedInput).containsOnly(entry("inputText", null), entry("inputBoolean", null), entry("inputDate", null),
                entry("inputInteger", null), entry("inputDecimal", null), entry("inputFile", new HashMap<String, Serializable>()));
    }

    @Test
    public void getProcessedInputs_with_multiple_complex_input_should_return_processed_input_with_null() throws Exception {
        final List<InputDefinition> inputDefinition = generateComplexInputDefinition();
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final Map<String, Serializable> input = new HashMap<>();
        final Map<String, Serializable> complexInput = generateInputMapWithFile("tempFile");
        final Map<String, Serializable> complexInput2 = generateInputMapWithNull();
        final Map<String, Serializable> complexInput3 = null;
        final List<Serializable> multipleComplexInput = new ArrayList<>();
        multipleComplexInput.add((Serializable) complexInput);
        multipleComplexInput.add((Serializable) complexInput2);
        multipleComplexInput.add((Serializable) complexInput3);
        input.put("inputComplex", (Serializable) multipleComplexInput);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);
        assertThat(processedInput).containsKey("inputComplex");
        final List<Serializable> processedMultipleComplexInput = (List<Serializable>) processedInput.get("inputComplex");
        assertThat(processedMultipleComplexInput).hasSize(3);
        final Serializable processedComplexInput1 = processedMultipleComplexInput.get(0);
        final Map<String, Serializable> processedComplexInputMap1 = (Map<String, Serializable>) processedComplexInput1;
        assertThat(processedComplexInputMap1).containsOnly(entry("inputText", "text"), entry("inputBoolean", true), entry("inputDate", new Date(DATE_01_01_1970_13H_AS_LONG_GMT)),
                entry("inputInteger", 125686181), entry("inputDecimal", 12.8),
                entry("inputFile", new FileInputValue(filename, "", fileContentString.getBytes("UTF-8"))));
        final Serializable processedComplexInput2 = processedMultipleComplexInput.get(1);
        final Map<String, Serializable> processedComplexInputMap2 = (Map<String, Serializable>) processedComplexInput2;
        assertThat(processedComplexInputMap2).containsOnly(entry("inputText", null), entry("inputBoolean", null), entry("inputDate", null),
                entry("inputInteger", null), entry("inputDecimal", null), entry("inputFile", null));
        assertThat(processedMultipleComplexInput.get(2)).isNull();
    }

    @Test
    public void getProcessedInputs_without_deleting_contract_temp_files() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition(true);
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final String tempFilePath = "tempFile";
        final File tempFile = generateTempFile();
        doReturn(tempFile).when(bonitaHomeFolderAccessor).getTempFile(tempFilePath, tenantId);
        final Map<String, Serializable> input = generateInputMap(tempFilePath);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, false);

        //files should not have been deleted
        verify(contractTypeConverter, times(0)).deleteFile(any(File.class), anyString());
    }

    @Test
    public void getProcessedInputs_deleting_contract_temp_files() throws Exception {
        final List<InputDefinition> inputDefinition = generateSimpleInputDefinition(true);
        when(contractDefinition.getInputs()).thenReturn(inputDefinition);
        final String tempFilePath = "tempFile";
        final File tempFile = generateTempFile();
        doReturn(tempFile).when(bonitaHomeFolderAccessor).getTempFile(tempFilePath, tenantId);
        final Map<String, Serializable> input = generateInputMap(tempFilePath);

        final Map<String, Serializable> processedInput = contractTypeConverter.getProcessedInput(contractDefinition, input, maxSizeForTenant, tenantId, true);

        //files should not have been deleted
        verify(contractTypeConverter, times(1)).deleteFile(any(File.class), anyString());
    }


    @Test
    public void getAdaptedContractDefinition_should_return_a_converter_contract() throws IOException {
        //given
        final ContractDefinitionImpl processContract = new ContractDefinitionImpl();
        final List<InputDefinition> inputDefinitions = new ArrayList<InputDefinition>();
        inputDefinitions.add(new InputDefinitionImpl(InputDefinition.FILE_INPUT_FILENAME, Type.TEXT, "Name of the file", false));
        inputDefinitions.add(new InputDefinitionImpl(InputDefinition.FILE_INPUT_CONTENT, Type.BYTE_ARRAY, "Content of the file", false));
        processContract.addInput(new InputDefinitionImpl("inputFile", "this is a input file", false, Type.FILE, inputDefinitions));

        //when
        final ContractDefinition adaptedContractDefinition = contractTypeConverter.getAdaptedContractDefinition(processContract);

        //assert
        final InputDefinition tempPathFileInputDefinition = adaptedContractDefinition.getInputs().get(0).getInputs().get(1);
        assertThat(tempPathFileInputDefinition.getType()).isEqualTo(Type.TEXT);
        assertThat(tempPathFileInputDefinition.getName()).isEqualTo(ContractTypeConverter.FILE_TEMP_PATH);
        assertThat(tempPathFileInputDefinition.getDescription()).isEqualTo(ContractTypeConverter.TEMP_PATH_DESCRIPTION);
    }

    private Map<String, Serializable> generateInputMapWithFile(final String tempFilePath) throws IOException {
        final File tempFile = generateTempFile();
        doReturn(tempFile).when(bonitaHomeFolderAccessor).getTempFile(tempFilePath, tenantId);
        return generateInputMap(tempFilePath);
    }

    private Map<String, Serializable> generateInputMap(final String tempFilePath) {
        final Map<String, Serializable> inputMap = new HashMap<>();
        inputMap.put("inputText", "text");
        inputMap.put("inputBoolean", "true");
        inputMap.put("inputDate", DATE_01_01_1970_13H_AS_STRING_GMT);
        inputMap.put("inputInteger", "125686181");
        inputMap.put("inputDecimal", "12.8");
        final Map<String, Serializable> fileMap = new HashMap<>();
        fileMap.put(InputDefinition.FILE_INPUT_FILENAME, filename);
        fileMap.put(ContractTypeConverter.FILE_TEMP_PATH, tempFilePath);
        fileMap.put(ContractTypeConverter.CONTENT_TYPE, "contentType");
        inputMap.put("inputFile", (Serializable) fileMap);
        return inputMap;
    }

    private Map<String, Serializable> generateInvalidInputMap() {
        final Map<String, Serializable> inputMap = new HashMap<>();
        inputMap.put("inputText", 0);
        inputMap.put("inputBoolean", "hello");
        inputMap.put("inputDate", 0);
        inputMap.put("inputInteger", "hello");
        inputMap.put("inputDecimal", "hello");
        return inputMap;
    }

    private Map<String, Serializable> generateInputMapWithNull() {
        final Map<String, Serializable> inputMap = new HashMap<>();
        inputMap.put("inputText", null);
        inputMap.put("inputBoolean", null);
        inputMap.put("inputDate", null);
        inputMap.put("inputInteger", null);
        inputMap.put("inputDecimal", null);
        inputMap.put("inputFile", null);
        return inputMap;
    }

    private Map<String, Serializable> generateInputMapWithEmptyFileInput() {
        final Map<String, Serializable> inputMap = new HashMap<>();
        inputMap.put("inputText", null);
        inputMap.put("inputBoolean", null);
        inputMap.put("inputDate", null);
        inputMap.put("inputInteger", null);
        inputMap.put("inputDecimal", null);
        inputMap.put("inputFile", new HashMap<String, Serializable>());
        return inputMap;
    }

    private List<InputDefinition> generateSimpleInputDefinition(final boolean withFile) {
        final List<InputDefinition> inputDefinitions = generateSimpleInputDefinition();
        if (withFile) {
            final InputDefinition fileInputDefinition = mock(InputDefinitionImpl.class);
            when(fileInputDefinition.getType()).thenReturn(Type.FILE);
            when(fileInputDefinition.getName()).thenReturn("inputFile");
            inputDefinitions.add(fileInputDefinition);
        }
        return inputDefinitions;
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
        final List<InputDefinition> childInputDefinitions = generateSimpleInputDefinition(true);
        when(inputDefinition.getInputs()).thenReturn(childInputDefinitions);
        inputDefinitions.add(inputDefinition);
        return inputDefinitions;
    }
}
