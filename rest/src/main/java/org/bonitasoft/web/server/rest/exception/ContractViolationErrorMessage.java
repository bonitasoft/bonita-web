package org.bonitasoft.web.server.rest.exception;

import java.util.List;

import org.bonitasoft.engine.bpm.contract.ContractViolationException;

public class ContractViolationErrorMessage extends ErrorMessage {

    private List<String> explanations;
    
    public ContractViolationErrorMessage(ContractViolationException exception) {
        super(exception);
        this.explanations = exception.getExplanations();
    }

    public List<String> getExplanations() {
        return explanations;
    }

    public void setExplanations(List<String> explanations) {
        this.explanations = explanations;
    }
}