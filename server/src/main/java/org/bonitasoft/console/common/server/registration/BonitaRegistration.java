/**
 * Copyright (C) 2010-2015 Bonitasoft S.A.
 * Bonitasoft, 32 rue Gustave Eiffel - 38000 Grenoble
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
package org.bonitasoft.console.common.server.registration;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.console.common.server.preferences.properties.PropertiesFactory;
import org.bonitasoft.console.common.server.preferences.properties.SimpleProperties;
import org.bonitasoft.web.rest.server.api.system.BonitaVersion;
import org.bonitasoft.web.rest.server.api.system.VersionFile;

/**
 * @author Baptiste Mesta
 * @author Aurelien Pupier
 * @author Anthony Birembaut
 */
public class BonitaRegistration {

    public static final String SERVICE_URL = "http://stats.bonitasoft.org/stats.php";
    public static final String BONITA_REGISTER_SYSTEM_PROPERTY = "bonita.web.register";
    public static final String BONITA_USER_REGISTER_TRY = "user.register.try";
    public static final String BONITA_INFO_SENT = "user.info.sent";
    public static final int BONITA_USER_REGISTER_MAXTRY = 6;
    public static final String[] SYSTEM_PROPERTIES_TO_SEND = new String[] { "java.version", "java.vendor", "os.name", "os.arch", "os.version" };
    protected static final String DEFAULT_EMAIL = "nowhere@nowhere.org";

    protected SystemInfoSender systemInfoSender = new SystemInfoSender(SERVICE_URL);

    /**
     * Logger
     */
    private static final Logger LOGGER = Logger.getLogger(BonitaRegistration.class.getName());

    protected Map<String, String> createSystemInfoMap() {
        final HashMap<String, String> infos = new HashMap<String, String>();
        infos.put("bonita.version", new BonitaVersion(new VersionFile()).getVersion());
        for (final String systemPropertyToSend : SYSTEM_PROPERTIES_TO_SEND) {
            addSystemPropertyToMap(infos, systemPropertyToSend);
        }
        infos.put("field_user_systeme_lang", Locale.getDefault().toString());
        infos.put("proc.number", String.valueOf(Runtime.getRuntime().availableProcessors()));
        infos.put("mem.total", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576) + "mo");
        infos.put("mem.max", String.valueOf(Runtime.getRuntime().maxMemory() / 1048576) + "mo");
        infos.put("origin", "living-app");
        addIpAdressInfo(infos);
        return infos;
    }

    protected String addSystemPropertyToMap(final Map<String, String> infos, final String systemPropertyKey) {
        return infos.put(systemPropertyKey, System.getProperty(systemPropertyKey));
    }

    protected void addIpAdressInfo(final Map<String, String> infos) {
        try {
            final Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                final NetworkInterface ni = e.nextElement();
                addIpAddressInfo(infos, ni);
            }
        } catch (final SocketException e1) {

        }
    }

    protected void addIpAddressInfo(final Map<String, String> infos, final NetworkInterface ni) {
        if (!ni.getName().contains("lo")) {
            final Enumeration<InetAddress> e = ni.getInetAddresses();
            while (e.hasMoreElements()) {
                final InetAddress ip = e.nextElement();
                addIpAdressInfo(infos, ni, ip);
            }
        }
    }

    protected void addIpAdressInfo(final Map<String, String> infos, final NetworkInterface ni, final InetAddress ip) {
        final String name = ip.getClass().getName();
        infos.put("netwrk." + ni.getName() + "." + (name.contains("Inet6") ? "ipv6" : "ipv4"), ip.getHostAddress());
    }

    protected boolean sendUserInfo() {
        final Map<String, String> infos = createSystemInfoMap();
        try {
            final String email = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(DEFAULT_EMAIL, "UTF-8");
            final String data = computeEncodedDataToSend(infos);
            return sendUserInfoEncoded(email, data);
        } catch (final UnsupportedEncodingException e) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE, e.getMessage(), e);
            }
            return false;
        }
    }

    protected String computeEncodedDataToSend(final Map<String, String> infos) throws UnsupportedEncodingException {
        final StringBuilder sb = new StringBuilder();
        final Set<Entry<String, String>> infosEntries = infos.entrySet();
        for (final Entry<String, String> infosEntry : infosEntries) {
            final String key = infosEntry.getKey();
            final String value = infosEntry.getValue();
            sb.append("<value key=\"")
                    .append(key)
                    .append("\">")
                    .append(value)
                    .append("</value>\n");
        }
        return "&data=" + URLEncoder.encode(sb.toString(), "UTF-8");
    }

    public void sendUserInfoIfNotSent() {
        final SimpleProperties platformPreferencesProperties = getPlatformPreferences();
        final String infoSent = platformPreferencesProperties.getProperty(BonitaRegistration.BONITA_INFO_SENT);
        if (infoSent == null || !infoSent.equals("1")) {
            int nbTry = getNbTry(platformPreferencesProperties);
            if (nbTry <= BonitaRegistration.BONITA_USER_REGISTER_MAXTRY) {
                platformPreferencesProperties.setProperty(BonitaRegistration.BONITA_USER_REGISTER_TRY, Integer.toString(++nbTry));
                sendUserInfoAndRecordInPreferences(platformPreferencesProperties);
            } else {
                platformPreferencesProperties.setProperty(BonitaRegistration.BONITA_INFO_SENT, "1");
            }
        }
    }

    protected SimpleProperties getPlatformPreferences() {
        return PropertiesFactory.getPlatformPreferencesProperties();
    }

    protected void sendUserInfoAndRecordInPreferences(final SimpleProperties platformPreferencesProperties) {
        if (sendUserInfo()) {
            platformPreferencesProperties.setProperty(BonitaRegistration.BONITA_INFO_SENT, "1");
        }
    }

    protected int getNbTry(final SimpleProperties platformPreferencesProperties) {
        int nbTry = 0;
        final String nbTryString = platformPreferencesProperties.getProperty(BonitaRegistration.BONITA_USER_REGISTER_TRY);
        if (nbTryString != null) {
            nbTry = Integer.parseInt(nbTryString);
        }
        return nbTry;
    }

    protected boolean sendUserInfoEncoded(final String email, final String data) {
        if (isDataValidToSend(email, data)) {
            return systemInfoSender.call(data, email);
        } else {
            return false;
        }
    }

    protected boolean isDataValidToSend(final String email, final String data) {
        return data != null
                && data.length() > 0
                && email != null
                && email.length() > 0;
    }

}
