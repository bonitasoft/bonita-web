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
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.bonitasoft.engine.bpm.contract.ConstraintDefinition;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.FileInputValue;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.contract.impl.ContractDefinitionImpl;
import org.bonitasoft.engine.bpm.contract.impl.InputDefinitionImpl;
import org.bonitasoft.engine.bpm.document.DocumentException;

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

    public static final String CONTENT_TYPE = "contentType";
    public static final String FILE_TEMP_PATH = "tempPath";
    public static final String TEMP_PATH_DESCRIPTION = "file name in the temporary upload directory";

    protected BonitaHomeFolderAccessor bonitaHomeFolderAccessor = new BonitaHomeFolderAccessor();

    private final ConvertUtilsBean convertUtilsBean;

    private long maxSizeForTenant;

    private long tenantId;

    public ContractTypeConverter(final String[] datePatterns) {
        convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(true, false, 0);
        final DateConverter dateConverter = new DateConverter();
        dateConverter.setPatterns(datePatterns);
        dateConverter.setTimeZone(TimeZone.getTimeZone("GMT"));
        convertUtilsBean.register(dateConverter, Date.class);
    }

    public Object convertToType(final Type type, final Serializable parameterValue) {
        final Class<? extends Serializable> clazz = getClassFromType(type);
        return convertToType(clazz, parameterValue);
    }

    public Map<String,Serializable> getProcessedInput(final ContractDefinition processContract, final Map<String, Serializable> inputs, final long maxSizeForTenant, final long tenantId, final boolean deleteFile) throws FileNotFoundException {
        this.maxSizeForTenant = maxSizeForTenant;
        this.tenantId = tenantId;
        final Map<String, Serializable> processedInputs = new HashMap<String, Serializable>();
        final Map<String, Serializable> contractDefinitionMap = processContract == null? Collections.<String, Serializable>emptyMap() : createContractInputMap(processContract.getInputs());

        if (inputs != null) {
            for (final Entry<String, Serializable> inputEntry : inputs.entrySet()) {
                processedInputs.put(inputEntry.getKey(),
                        convertInputToExpectedType(inputEntry.getValue(), contractDefinitionMap.get(inputEntry.getKey()), deleteFile));
            }
        }
        return processedInputs;
    }

    protected Serializable convertInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition, final boolean deleteFile) throws FileNotFoundException {
        if (inputValue == null) {
            return null;
        } else if (inputValue instanceof List) {
            return convertMultipleInputToExpectedType(inputValue, inputDefinition, deleteFile);
        } else {
            return convertSingleInputToExpectedType(inputValue, inputDefinition, deleteFile);
        }
    }

    protected Serializable convertMultipleInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition, final boolean deleteFile)
            throws FileNotFoundException {
        @SuppressWarnings("unchecked")
        final List<Serializable> listOfValues = (List<Serializable>) inputValue;
        final List<Serializable> convertedListOfValues = new ArrayList<Serializable>();
        for (final Serializable value : listOfValues) {
            Serializable convertedValue = null;
            if (value != null) {
                convertedValue = convertSingleInputToExpectedType(value, inputDefinition, deleteFile);
            }
            convertedListOfValues.add(convertedValue);
        }
        return (Serializable) convertedListOfValues;
    }

    protected Serializable convertSingleInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition, final boolean deleteFile) throws FileNotFoundException {
        if (inputDefinition == null) {
            return inputValue;
        } else if (inputDefinition instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Serializable> mapOfInputDefinition = (Map<String, Serializable>) inputDefinition;
            return convertComplexInputToExpectedType(inputValue, mapOfInputDefinition, deleteFile);
        } else {
            final InputDefinition simpleInputDefinition = (InputDefinition) inputDefinition;
            if (Type.FILE.equals(simpleInputDefinition.getType())) {
                return convertFileInputToExpectedType(inputValue, deleteFile);
            } else {
                return (Serializable) convertToType(simpleInputDefinition.getType(), inputValue);
            }
        }
    }

    protected Serializable convertComplexInputToExpectedType(final Serializable inputValue, final Map<String, Serializable> mapOfInputDefinition,
            final boolean deleteFile) throws FileNotFoundException {
        if (inputValue instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Serializable> mapOfValues = (Map<String, Serializable>) inputValue;
            final Map<String, Serializable> convertedMapOfValues = new HashMap<String, Serializable>();
            for (final Entry<String, Serializable> valueEntry : mapOfValues.entrySet()) {
                final Serializable childInputDefinition = mapOfInputDefinition.get(valueEntry.getKey());
                final Serializable convertedValue = convertInputToExpectedType(valueEntry.getValue(), childInputDefinition, deleteFile);
                convertedMapOfValues.put(valueEntry.getKey(), convertedValue);
            }
            return (Serializable) convertedMapOfValues;
        } else {
            return inputValue;
        }
    }

    protected Serializable convertFileInputToExpectedType(final Serializable inputValue, final boolean deleteFile) throws FileNotFoundException {
        if (inputValue instanceof Map) {
            @SuppressWarnings("unchecked")
            final Map<String, Serializable> mapOfValues = (Map<String, Serializable>) inputValue;
            if (mapOfValues.containsKey(InputDefinition.FILE_INPUT_FILENAME) && mapOfValues.containsKey(FILE_TEMP_PATH)) {
                final String filename = (String) mapOfValues.get(InputDefinition.FILE_INPUT_FILENAME);
                final FileInputValue fileInputValue = new FileInputValue(
                        filename,
                        (String) mapOfValues.get(CONTENT_TYPE),
                        retrieveFileAndGetContent((String) mapOfValues.get(FILE_TEMP_PATH),
                        deleteFile));
                return fileInputValue;
            }
        }
        return inputValue;
    }

    protected byte[] retrieveFileAndGetContent(final String fileTempPath, final boolean deleteFile) throws FileNotFoundException {
        byte[] fileContent = null;
        if (fileTempPath != null) {
            try {
                final File sourceFile = bonitaHomeFolderAccessor.getTempFile(fileTempPath, tenantId);
                if (sourceFile.exists()) {
                    fileContent = getFileContent(sourceFile, fileTempPath, deleteFile);
                } else {
                    throw new FileNotFoundException("Cannot find " + fileTempPath + " in the tenant temp directory.");
                }
            } catch (final FileNotFoundException e) {
                throw new FileNotFoundException(e.getMessage());
            } catch (final IOException e) {
                throw new RuntimeException(e);
            } catch (final DocumentException e) {
                throw new RuntimeException(e);
            }
        }
        return fileContent;
    }

    protected byte[] getFileContent(final File sourceFile, final String fileTempPath, final boolean deleteFile) throws DocumentException, IOException {
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

    protected void deleteFile(final File sourceFile, final String fileTempPath) {
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

    public ContractDefinition getAdaptedContractDefinition(final ContractDefinition contract) {
        final List<ConstraintDefinition> constraints = contract.getConstraints();
        final List<InputDefinition> inputDefinitions = adaptContractInputList(contract.getInputs());
        final ContractDefinitionImpl contractDefinition = getContractDefinition(constraints, inputDefinitions);
        return contractDefinition;
    }

    protected List<InputDefinition> adaptContractInputList(final List<InputDefinition> inputDefinitions) {
        final List<InputDefinition> contractDefinition = new ArrayList<InputDefinition>();
        for (final InputDefinition inputDefinition : inputDefinitions) {
            List<InputDefinition> childInputDefinitions;
            if (Type.FILE.equals(inputDefinition.getType())) {
                childInputDefinitions = getFileChildInputDefinitions(inputDefinition);
            } else if (inputDefinition.hasChildren()) {
                childInputDefinitions = adaptContractInputList(inputDefinition.getInputs());
            } else {
                childInputDefinitions = new ArrayList<InputDefinition>();
            }
            final InputDefinition newInputDefinition = new InputDefinitionImpl(inputDefinition.getName(), inputDefinition.getDescription(), inputDefinition.isMultiple(), inputDefinition.getType(), childInputDefinitions);
            contractDefinition.add(newInputDefinition);
        }
        return contractDefinition;
    }

    private List<InputDefinition> getFileChildInputDefinitions(final InputDefinition inputDefinition) {
        List<InputDefinition> childInputDefinitions;
        childInputDefinitions = new ArrayList<InputDefinition>();
        for (final InputDefinition childInputDefinition : inputDefinition.getInputs()) {
            if(Type.BYTE_ARRAY.equals(childInputDefinition.getType())) {
                childInputDefinitions.add(new InputDefinitionImpl(FILE_TEMP_PATH, TEMP_PATH_DESCRIPTION,false, Type.TEXT, new ArrayList<InputDefinition>()));
            } else {
                childInputDefinitions.add(childInputDefinition);
            }
        }
        return childInputDefinitions;
    }

    protected ContractDefinitionImpl getContractDefinition(final List<ConstraintDefinition> constraints, final List<InputDefinition> inputDefinitions) {
        final ContractDefinitionImpl contractDefinition = new ContractDefinitionImpl();
        for (final ConstraintDefinition constraint: constraints) {
            contractDefinition.addConstraint(constraint);
        }

        for (final InputDefinition input: inputDefinitions) {
            contractDefinition.addInput(input);
        }
        return contractDefinition;
    }

    protected Object convertToType(final Class<? extends Serializable> clazz, final Serializable parameterValue) {
        try {
            return convertUtilsBean.convert(parameterValue, clazz);
        } catch (final ConversionException e) {
            LOGGER.log(Level.INFO, "unable to parse '" + parameterValue + "' to type " + clazz.getName(), e);
            return parameterValue;
        }
    }

    protected Class<? extends Serializable> getClassFromType(final Type type) {
        switch (type) {
            case BOOLEAN:
                return Boolean.class;
            case DATE:
                return Date.class;
            case INTEGER:
                return Integer.class;
            case DECIMAL:
                return Double.class;
            case BYTE_ARRAY:
                return Byte[].class;
            default:
                return String.class;
        }
    }
}
