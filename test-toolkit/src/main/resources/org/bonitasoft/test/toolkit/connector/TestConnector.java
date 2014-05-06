package org.bonitasoft.test.toolkit.connector;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

/**
 * @author Colin PUY
 */
public class TestConnector extends AbstractConnector {

    public void validateInputParameters() throws ConnectorValidationException {
        // Do Nothing
    }

    @Override
    protected void executeBusinessLogic() throws ConnectorException {
        // Do Nothing
    }
}
