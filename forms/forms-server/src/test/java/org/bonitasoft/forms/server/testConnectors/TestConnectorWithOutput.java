package org.bonitasoft.forms.server.testConnectors;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

/**
 * @author Baptiste Mesta
 */
public class TestConnectorWithOutput extends AbstractConnector {

    @Override
    public void validateInputParameters() throws ConnectorValidationException {

    }

    @Override
    protected void executeBusinessLogic() {
        final String input1Value = (String) getInputParameter("input1");
        setOutputParameter("output1", input1Value);
    }

}
