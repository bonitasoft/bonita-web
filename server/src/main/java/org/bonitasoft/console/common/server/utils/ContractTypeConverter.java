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
 **/
package org.bonitasoft.console.common.server.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.bonitasoft.engine.bpm.contract.ComplexInputDefinition;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.SimpleInputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;

/**
 * @author Anthony Birembaut
 */

public class ContractTypeConverter {

    public static final String[] ISO_8601_DATE_PATTERNS = new String[] { "yyyy-MM-dd", "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MM-dd'T'hh:mm:ss'Z'",
            "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'" };

    private final ConvertUtilsBean convertUtilsBean;

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

    public Map<String, Serializable> getProcessedInput(final ContractDefinition contractDefinition, final Map<String, Serializable> input) {
        final Map<String, Serializable> processedInputs = new HashMap<String, Serializable>();
        final Map<String, Serializable> contractDefinitionMap = createContractInputMap(contractDefinition.getSimpleInputs(), contractDefinition.getComplexInputs());

        for (final Entry<String, Serializable> inputEntry : input.entrySet()) {
            processedInputs.put(inputEntry.getKey(), convertInputToExpectedType(inputEntry.getValue(), contractDefinitionMap.get(inputEntry.getKey())));
        }
        return processedInputs;
    }

    @SuppressWarnings("unchecked")
    protected Serializable convertInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition) {
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
    protected Serializable convertSingleInputToExpectedType(final Serializable inputValue, final Serializable inputDefinition) {
        if (inputDefinition == null) {
            return inputValue;
        } else if (inputValue instanceof Map) {
            final Map<String, Serializable> mapOfValues = (Map<String, Serializable>) inputValue;
            final Map<String, Serializable> convertedMapOfValues = new HashMap<String, Serializable>();
            final Map<String, Serializable> mapOfInputDefinition = (Map<String, Serializable>) inputDefinition;
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
            return (Serializable) convertedMapOfValues;
        } else {
            final SimpleInputDefinition simpleInputDefinition = (SimpleInputDefinition) inputDefinition;
            return (Serializable) convertToType(simpleInputDefinition.getType(), inputValue);
        }
    }

    protected Map<String, Serializable> createContractInputMap(final List<SimpleInputDefinition> simpleInputs, final List<ComplexInputDefinition> complexInputs) {
        final Map<String, Serializable> contractDefinitionMap = new HashMap<String, Serializable>();
        for (final SimpleInputDefinition simpleInputDefinition : simpleInputs) {
            contractDefinitionMap.put(simpleInputDefinition.getName(), simpleInputDefinition);
        }
        for (final ComplexInputDefinition complexInputDefinition : complexInputs) {
            contractDefinitionMap.put(complexInputDefinition.getName(),
                    (Serializable) createContractInputMap(complexInputDefinition.getSimpleInputs(), complexInputDefinition.getComplexInputs()));
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
