package org.bonitasoft.forms.server.util;

import java.util.logging.Level;

public interface IFormLogger {

    public void log(Level level, String message);

    public void log(Level level, String message, Throwable e);

    public boolean isLoggable(Level level);

}
