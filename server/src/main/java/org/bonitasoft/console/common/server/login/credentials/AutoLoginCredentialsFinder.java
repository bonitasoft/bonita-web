package org.bonitasoft.console.common.server.login.credentials;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.bonitasoft.console.common.server.login.filter.AutoLoginRule;
import org.bonitasoft.console.common.server.preferences.properties.ConfigurationFilesManager;
import org.bonitasoft.console.common.server.preferences.properties.ProcessIdentifier;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by julien.mege on 22/06/2016.
 */
public class AutoLoginCredentialsFinder {

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(AutoLoginCredentialsFinder.class.getName());
    private ConfigurationFilesManager configurationFilesManager;

    public AutoLoginCredentialsFinder(ConfigurationFilesManager configurationFilesManager) {
        this.configurationFilesManager = configurationFilesManager;
    }

    private AutoLoginCredentials getAutoLoginCredentials(ProcessIdentifier processIdentifier, long tenantId) {
        AutoLoginCredentials[] autoLoginCredentials = getAutoLoginCredentialsList(processIdentifier, tenantId);

        for(AutoLoginCredentials autoLoginCredential: autoLoginCredentials){
            ProcessIdentifier currentProcessIdentifier = new ProcessIdentifier(autoLoginCredential.getProcessName(), autoLoginCredential.getProcessVersion());
            if(currentProcessIdentifier.getIdentifier().equals(processIdentifier.getIdentifier())){
               return autoLoginCredential;
            }
        }

        return null;
    }

    private AutoLoginCredentials[] getAutoLoginCredentialsList(ProcessIdentifier processIdentifier, long tenantId) {

        if (processIdentifier != null) {
            //properties must be retrieve in the "autologin-v6.json" file
            //TODO First try to get from Cache

            File credentialsMappingFile =  configurationFilesManager.getTenantAutoLoginConfiguration(tenantId);

            try {
                return getObjectMapper().readValue(credentialsMappingFile, AutoLoginCredentials[].class);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Cannot read tenant auto login configuration : " + e.getMessage(), e);
                }
            }
        }

        return new AutoLoginCredentials[0];
    }

    protected ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public AutoLoginCredentials getCredential(ProcessIdentifier processIdentifier, long tenantId){
        AutoLoginCredentials autoLoginCredentials = getAutoLoginCredentials(processIdentifier, tenantId);
        return autoLoginCredentials;
    }

}
