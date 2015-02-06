package org.bonitasoft.console.common.server.page;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public class DelegateClassLoader extends ClassLoader {

    ClassLoader delegate;

    public DelegateClassLoader(final ClassLoader delegate) {
        this.delegate = delegate;
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        return delegate.loadClass(name);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public URL getResource(final String name) {
        return delegate.getResource(name);
    }

    @Override
    public Enumeration<URL> getResources(final String name) throws IOException {
        return delegate.getResources(name);
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        return delegate.getResourceAsStream(name);
    }

    @Override
    public void setDefaultAssertionStatus(final boolean enabled) {
        delegate.setDefaultAssertionStatus(enabled);
    }

    @Override
    public void setPackageAssertionStatus(final String packageName, final boolean enabled) {
        delegate.setPackageAssertionStatus(packageName, enabled);
    }

    @Override
    public void setClassAssertionStatus(final String className, final boolean enabled) {
        delegate.setClassAssertionStatus(className, enabled);
    }

    @Override
    public void clearAssertionStatus() {
        delegate.clearAssertionStatus();
    }

}
