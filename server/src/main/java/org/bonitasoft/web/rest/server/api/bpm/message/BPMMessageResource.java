/**
 * Copyright (C) 2018 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.rest.server.api.bpm.message;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.flownode.SendEventException;
import org.bonitasoft.engine.expression.Expression;
import org.bonitasoft.engine.expression.ExpressionBuilder;
import org.bonitasoft.engine.expression.ExpressionType;
import org.bonitasoft.engine.expression.InvalidExpressionException;
import org.bonitasoft.web.rest.server.api.resource.CommonResource;
import org.bonitasoft.web.toolkit.client.common.exception.api.APIException;
import org.restlet.resource.Post;

/**
 * REST resource to send BPM message to a defined process.
 *
 * @author Emmanuel Duchastenier
 */
public class BPMMessageResource extends CommonResource {

    private static final Set<String> SUPPORTED_TYPES = new HashSet<>();
    static {
        SUPPORTED_TYPES.add(String.class.getName());
        SUPPORTED_TYPES.add(Integer.class.getName());
        SUPPORTED_TYPES.add(Long.class.getName());
        SUPPORTED_TYPES.add(Float.class.getName());
        SUPPORTED_TYPES.add(Double.class.getName());
        SUPPORTED_TYPES.add(Boolean.class.getName());
        SUPPORTED_TYPES.add(Date.class.getName());
        SUPPORTED_TYPES.add(LocalDate.class.getName());
        SUPPORTED_TYPES.add(LocalDateTime.class.getName());
        SUPPORTED_TYPES.add(OffsetDateTime.class.getName());
    }

    private final ProcessAPI processAPI;

    public BPMMessageResource(final ProcessAPI processAPI) {
        this.processAPI = processAPI;
    }

    @Post("json")
    public void sendMessage(BPMMessage message) {
        validateMandatoryAttributes(message);
        try {
            Map<Expression, Expression> msgContent = new HashMap<>();
            if (message.getMessageContent() != null) {
                for (Map.Entry<String, BPMMessageValue> entry : message.getMessageContent().entrySet()) {
                    msgContent.put(new ExpressionBuilder().createConstantStringExpression(entry.getKey()),
                            getExpressionFromObject(entry));
                }
            }
            Map<Expression, Expression> correlations = new HashMap<>();
            if (message.getCorrelations() != null) {
                int nbCorrelations = message.getCorrelations().size();
                if (nbCorrelations > 5) {
                    throw new IllegalArgumentException(
                            String.format("A maximum of 5 correlations is supported. %s found.", nbCorrelations));
                }
                for (Map.Entry<String, BPMMessageValue> entry : message.getCorrelations().entrySet()) {
                    correlations.put(new ExpressionBuilder().createConstantStringExpression(entry.getKey()),
                            getExpressionFromObject(entry));
                }
            }
            processAPI.sendMessage(message.getMessageName(),
                    new ExpressionBuilder().createConstantStringExpression(message.getTargetProcess()),
                    new ExpressionBuilder().createConstantStringExpression(message.getTargetFlowNode()),
                    msgContent,
                    correlations);
        } catch (final SendEventException | InvalidExpressionException e) {
            throw new APIException(e);
        }
    }

    private Expression getExpressionFromObject(Entry<String, BPMMessageValue> entry) throws InvalidExpressionException {
        BPMMessageValue messageValue = entry.getValue();
        Object value = messageValue.getValue();
        String type = valueType(messageValue.getType(), value);
        if (!isSupportedType(type)) {
            throw new InvalidExpressionException(
                    String.format(
                            "BPM send message: unsupported value type '%s' for key '%s'. Only primitive types are supported.",
                            messageValue.getType(),
                            entry.getKey()));
        }
        return new ExpressionBuilder().createExpression(String.valueOf(value),
                String.valueOf(value), valueType(type, value), ExpressionType.TYPE_CONSTANT);
    }

    private String valueType(String type, Object value) {
        if (type != null) {
            return type;
        }
        return guessType(value);
    }

    private String guessType(Object value) {
        if (value instanceof String) {
            try {
                LocalDate.parse((String) value);
                return LocalDate.class.getName();
            } catch (DateTimeParseException e) {
                //Ignore
            }
            try {
                LocalDateTime.parse((String) value);
            } catch (DateTimeParseException e) {
                //Ignore
            }
            try {
                OffsetDateTime.parse((String) value);
                return OffsetDateTime.class.getName();
            } catch (DateTimeParseException e) {
                //Ignore
            }
            return String.class.getName();
        } else if (value instanceof Long) {
            return Long.class.getName();
        } else if (value instanceof Double) {
            return Double.class.getName();
        } else if (value instanceof Float) {
            return Float.class.getName();
        } else if (value instanceof Integer) {
            return Integer.class.getName();
        } else if (value instanceof Boolean) {
            return Boolean.class.getName();
        }
        return null;
    }

    private boolean isSupportedType(String type) {
        return SUPPORTED_TYPES.contains(type);
    }

    private void validateMandatoryAttributes(BPMMessage message) {
        if (message == null) {
            throw new IllegalArgumentException("message body is missing");
        }
        if (message.getMessageName() == null) {
            throw new IllegalArgumentException("messageName is mandatory");
        }
        if (message.getTargetProcess() == null) {
            throw new IllegalArgumentException("targetProcess is mandatory");
        }
        if (message.getTargetFlowNode() == null) {
            throw new IllegalArgumentException("targetFlowNode is mandatory");
        }
    }

}
