package org.bonitasoft.forms.server.testConnectors;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

/**
 * @author Baptiste Mesta
 */
public class TestConnector extends AbstractConnector {

    @Override
    public void validateInputParameters() throws ConnectorValidationException {

    }

    @Override
    protected void executeBusinessLogic() {
        final String input1Value = (String) getInputParameter("input1");
        VariableStorage.getInstance().setVariable("input1", input1Value);
    }

}
