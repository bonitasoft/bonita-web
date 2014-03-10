package org.bonitasoft.forms.server.util;

import java.util.Map;
import java.util.logging.Level;

public interface IFormLogger {

    public void log(Level level, String message);

    public void log(Level level, String message, Map<String, Object> context);

    public void log(Level level, String message, Throwable e);

    public void log(Level level, String message, Throwable e, Map<String, Object> context);

    public boolean isLoggable(Level level);

}
