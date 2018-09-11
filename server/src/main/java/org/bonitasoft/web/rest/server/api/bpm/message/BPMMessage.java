/*
 * Copyright (C) 2018 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.web.rest.server.api.bpm.message;

import java.util.Map;

/**
 * @author Emmanuel Duchastenier
 */
public class BPMMessage {

    private String messageName;
    private String targetProcess;
    private String targetFlowNode;
    private Map<String, Object> messageContent;
    private Map<String, Object> correlations;

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getTargetProcess() {
        return targetProcess;
    }

    public void setTargetProcess(String targetProcess) {
        this.targetProcess = targetProcess;
    }

    public String getTargetFlowNode() {
        return targetFlowNode;
    }

    public void setTargetFlowNode(String targetFlowNode) {
        this.targetFlowNode = targetFlowNode;
    }

    public Map<String, Object> getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(Map<String, Object> messageContent) {
        this.messageContent = messageContent;
    }

    public Map<String, Object> getCorrelations() {
        return correlations;
    }

    public void setCorrelations(Map<String, Object> correlations) {
        this.correlations = correlations;
    }

}
