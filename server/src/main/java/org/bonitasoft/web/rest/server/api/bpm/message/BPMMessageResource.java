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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
                for (Map.Entry<String, Object> entry : message.getMessageContent().entrySet()) {
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
                for (Map.Entry<String, Object> entry : message.getCorrelations().entrySet()) {
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

    private Expression getExpressionFromObject(Entry<String, Object> entry) throws InvalidExpressionException {
        Object s = entry.getValue();
        if (s instanceof String) {
            try {
                OffsetDateTime.parse((String) s);
                return new ExpressionBuilder().createExpression((String) s, (String) s, OffsetDateTime.class.getName(),
                        ExpressionType.TYPE_CONSTANT);
            } catch (DateTimeParseException e) {
                //Ignore
            }
            try {
                LocalDateTime.parse((String) s);
                return new ExpressionBuilder().createExpression((String) s, (String) s, LocalDateTime.class.getName(),
                        ExpressionType.TYPE_CONSTANT);
            } catch (DateTimeParseException e) {
                //Ignore
            }
            try {
                LocalDate.parse((String) s);
                return new ExpressionBuilder().createExpression((String) s, (String) s, LocalDate.class.getName(),
                        ExpressionType.TYPE_CONSTANT);
            } catch (DateTimeParseException e) {
                //Ignore
            }
            return new ExpressionBuilder().createConstantStringExpression((String) s);
        } else if (s instanceof Long) {
            return new ExpressionBuilder().createConstantLongExpression((Long) s);
        } else if (s instanceof Double) {
            return new ExpressionBuilder().createConstantDoubleExpression((Double) s);
        } else if (s instanceof Float) {
            return new ExpressionBuilder().createConstantFloatExpression((Float) s);
        } else if (s instanceof Integer) {
            return new ExpressionBuilder().createConstantIntegerExpression((Integer) s);
        } else if (s instanceof Boolean) {
            return new ExpressionBuilder().createConstantBooleanExpression((Boolean) s);
        }
        throw new InvalidExpressionException(
                String.format(
                        "BPM send message: unsupported value type '%s' for key '%s'. Only primitive types are supported.",
                        s.getClass().getCanonicalName(),
                        entry.getKey()));
    }

    private void validateMandatoryAttributes(BPMMessage message) {
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
