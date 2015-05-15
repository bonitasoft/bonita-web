/**
 * Copyright (C) 2015 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301, USA.
 */
package org.bonitasoft.console.common.server.utils;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.FileInputValue;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.document.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Anthony Birembaut
 */

public class ContractTypeConverter {

    /**
     * Logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ContractTypeConverter.class.getName());

    public static final String[] ISO_8601_DATE_PATTERNS = new String[]{"yyyy-MM-dd", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"};

    public static final String FILE_TEMP_PATH = "tempPath";

    protected BonitaHomeFolderAccessor bonitaHomeFolderAccessor = new BonitaHomeFolderAccessor();

    private final ConvertUtilsBean convertUtilsBean;

    private long maxSizeForTenant;

    private long tenantId;
    private boolean deleteFile = false;

    public ContractTypeConverter(final String[] datePatterns) {
        convertUtilsBean = new ConvertUtilsBean();
        final DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(datePatterns);
        convertUtilsBean.register(dateConverter, Date.class);
    }

    public Object convertToType(final Type type, final Serializable parameterValue) {
        final Class<? extends Serializable> clazz = getClassFromType(type);
        return convertToType(clazz, parameterValue);
    }

    public Map<String,Serializable> getProcessedInput(ContractDefinition processContract, Map<String, Serializable> inputs, long maxSizeForTenant, long tenantId, boolean deleteFile) throws FileNotFoundException {
        this.deleteFile = deleteFile;
        return this.getProcessedInput(processContract, inputs, maxSizeForTenant, tenantId);
    }

    public Map<String, Serializable> getProcessedInput(final ContractDefinition contractDefinition, final Map<String, Serializable> input, final long maxSizeForTenant, final long tenantId) throws FileNotFoundException {
        this.maxSizeForTenant = maxSizeForTenant;
        this.tenantId = tenantId;
        final Map<String, Serializable> processedInputs = new HashMap<String, Serializable>();
        final Map<String, Serializable> contractDefinitionMap = contractDefinition == null? Collections.<String, Serializable>emptyMap() : createContractInputMap(contractDefinition.getInputs());

        for (final Entry<String, Serializable> inputEntry : input.entrySet()) {
            processedInputs.put(inputEntry.getKey(), convertInputToExpectedType(inputEntry.getValue(), contractDefinitionMap.get(inputEntry.getKey())));
        }
        return processedInputs;
    }

    @SuppressWarnings("unchecked")
    protected Serializable convertInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition) throws FileNotFoundException {
        if (inputValue instanceof List) {
            final List<Serializable> listOfValues = (List<Serializable>) inputValue;
            final List<Serializable> convertedListOfValues = new ArrayList<Serializable>();
            for (final Serializable value : listOfValues) {
                convertedListOfValues.add(convertSingleInputToExpectedType(value, inputDefinition));
            }
            return (Serializable) convertedListOfValues;
        } else {
            return convertSingleInputToExpectedType(inputValue, inputDefinition);
        }
    }

    @SuppressWarnings("unchecked")
    protected Serializable convertSingleInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition) throws FileNotFoundException {
        if (inputDefinition == null) {
            return inputValue;
        } else if (inputDefinition instanceof Map) {
            final Map<String, Serializable> mapOfValues = (Map<String, Serializable>) inputValue;
            final Map<String, Serializable> mapOfInputDefinition = (Map<String, Serializable>) inputDefinition;
            return (Serializable) convertComplexInputToExpectedType(mapOfValues, mapOfInputDefinition);
        } else if (inputValue instanceof Map) {
            final Map<String, Serializable> mapOfValues = (Map<String, Serializable>) inputValue;
            final String inputDefinitionType = ((InputDefinition) inputDefinition).getType().name();
            if (Type.FILE.name().equals(inputDefinitionType)) {
                return convertFileInputToExpectedType(mapOfValues);
            }
        } else {
            final InputDefinition simpleInputDefinition = (InputDefinition) inputDefinition;
            return (Serializable) convertToType(simpleInputDefinition.getType(), inputValue);
        }
        return inputValue;
    }

    protected Map<String, Serializable> convertComplexInputToExpectedType(final Map<String, Serializable> mapOfValues, final Map<String, Serializable> mapOfInputDefinition) throws FileNotFoundException {
        final Map<String, Serializable> convertedMapOfValues = new HashMap<String, Serializable>();
        for (final Entry<String, Serializable> valueEntry : mapOfValues.entrySet()) {
            final Serializable childInputDefinition = mapOfInputDefinition.get(valueEntry.getKey());
            Serializable convertedValue;
            if (valueEntry.getValue() instanceof List) {
                convertedValue = convertInputToExpectedType(valueEntry.getValue(), childInputDefinition);
            } else {
                convertedValue = convertSingleInputToExpectedType(valueEntry.getValue(), childInputDefinition);
            }
            convertedMapOfValues.put(valueEntry.getKey(), convertedValue);
        }
        return convertedMapOfValues;
    }

    protected FileInputValue convertFileInputToExpectedType(final Map<String, Serializable> mapOfValues) throws FileNotFoundException {
        final String filename = (String) mapOfValues.get(InputDefinition.FILE_INPUT_FILENAME);
        final FileInputValue fileInputValue = new FileInputValue(filename, retrieveFileAndGetContent((String) mapOfValues.get(FILE_TEMP_PATH)));
        return fileInputValue;
    }

    protected byte[] retrieveFileAndGetContent(final String fileTempPath) throws FileNotFoundException {
        final File sourceFile;
        byte[] fileContent = null;
        try {
            sourceFile = bonitaHomeFolderAccessor.getTempFile(fileTempPath, tenantId);
            if (sourceFile.exists()) {
                fileContent = getFileContent(sourceFile, fileTempPath);
            } else {
                throw new FileNotFoundException("Cannot find " + fileTempPath + " in the tenant temp directory.");
            }
        } catch(final FileNotFoundException e) {
            throw new FileNotFoundException(e.getMessage());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final DocumentException e) {
            throw new RuntimeException(e);
        }
        return fileContent;
    }

    protected byte[] getFileContent(final File sourceFile, final String fileTempPath) throws DocumentException, IOException {
        byte[] fileContent;
        if (sourceFile.length() > maxSizeForTenant * 1048576) {
            final String errorMessage = "This document is exceeded " + maxSizeForTenant + "Mb";
            throw new DocumentException(errorMessage);
        }
        fileContent = DocumentUtil.getArrayByteFromFile(sourceFile);
        if (deleteFile) {
            deleteFile(sourceFile, fileTempPath);
        }
        return fileContent;
    }

    protected void deleteFile(File sourceFile, String fileTempPath) {
        if (!sourceFile.delete()){
            sourceFile.deleteOnExit();
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO, "Cannot delete " + fileTempPath + "in the tenant temp directory.");
            }
        }
    }

    protected Map<String, Serializable> createContractInputMap(final List<InputDefinition> inputDefinitions) {
        final Map<String, Serializable> contractDefinitionMap = new HashMap<String, Serializable>();
        for (final InputDefinition inputDefinition : inputDefinitions) {
            if (inputDefinition.hasChildren() && !Type.FILE.equals(inputDefinition.getType())) {
                contractDefinitionMap.put(inputDefinition.getName(),
                        (Serializable) createContractInputMap(inputDefinition.getInputs()));
            } else {
                contractDefinitionMap.put(inputDefinition.getName(), inputDefinition);
            }
        }
        return contractDefinitionMap;
    }

    protected Object convertToType(final Class<? extends Serializable> clazz, final Serializable parameterValue) {
        try {
            return convertUtilsBean.convert(parameterValue, clazz);
        } catch (final ConversionException e) {
            throw new IllegalArgumentException("unable to parse '" + parameterValue + "' to type " + clazz.getName());
        }
    }

    protected Class<? extends Serializable> getClassFromType(final Type type) {
        switch (type) {
            case BOOLEAN:
                return Boolean.class;
            case DATE:
                return Date.class;
            case INTEGER:
                return Long.class;
            case DECIMAL:
                return Double.class;
            case BYTE_ARRAY:
                return Byte[].class;
            default:
                return String.class;
        }
    }
}
