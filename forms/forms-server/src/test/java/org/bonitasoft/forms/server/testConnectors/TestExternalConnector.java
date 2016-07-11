package org.bonitasoft.forms.server.testConnectors;

import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorValidationException;

/**
 * @author Nicolas Chabanoles
 */
public class TestExternalConnector extends AbstractConnector {

    private boolean hasBeenValidated = false;

    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        hasBeenValidated = true;
    }

    @Override
    protected void executeBusinessLogic() {
        final String input1Value = (String) getInputParameter("param1");
        setOutputParameter("param1", "To uppercase :" + input1Value.toUpperCase());
        setOutputParameter("hasBeenValidated", hasBeenValidated);
        final Object returnNotSerializableOutput = getInputParameter("returnNotSerializableOutput");
        if (returnNotSerializableOutput != null) {
            setOutputParameter("notSerializable", new Object());
        }
    }

}
