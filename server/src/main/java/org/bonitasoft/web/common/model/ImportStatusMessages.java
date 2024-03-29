/**
 * Copyright (C) 2013 BonitaSoft S.A.
 * BonitaSoft, 32 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.web.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bonitasoft.engine.api.ImportError;
import org.bonitasoft.engine.api.ImportStatus;

/**
 * @author Fabio Lombardi
 *
 */
public class ImportStatusMessages implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<ImportStatusMessage> errors = new ArrayList<>();

    private final List<ImportStatusMessage> imported = new ArrayList<>();

    private final List<ImportStatusMessage> skipped = new ArrayList<>();

    public ImportStatusMessages(final List<ImportStatus> statusMessages) {
        setImportStatus(statusMessages);
    }

    public void addResourceImported(final ImportStatusMessage profileImportStatusMessage) {
        imported.add(profileImportStatusMessage);
    }

    public void addResourceSkipped(final ImportStatusMessage profileImportStatusMessage) {
        skipped.add(profileImportStatusMessage);
    }

    public void addResourceInError(final ImportStatusMessage profileImportStatusMessage) {
        errors.add(profileImportStatusMessage);
    }

    public List<ImportStatusMessage> getErrors() {
        return errors;
    }

    public List<ImportStatusMessage> getImported() {
        return imported;
    }

    public List<ImportStatusMessage> getSkipped() {
        return skipped;
    }

    public void setImportStatus(final List<ImportStatus> statusMessages) {

        for (final ImportStatus statusMessage : statusMessages) {
            if (statusMessage.getErrors().size() > 0) {
                addResourceInError(convertImportStatus(statusMessage));
            } else if (statusMessage.getStatus().equals(ImportStatus.Status.REPLACED) || statusMessage.getStatus().equals(ImportStatus.Status.ADDED)) {
                addResourceImported(convertImportStatus(statusMessage));
            } else if (statusMessage.getStatus().equals(ImportStatus.Status.SKIPPED)) {
                addResourceSkipped(convertImportStatus(statusMessage));
            }
        }
    }

    private ImportStatusMessage convertImportStatus(final ImportStatus importStatus) {
        final ImportStatusMessage importStatusMessage = new ImportStatusMessage(importStatus.getName(), importStatus.getStatus().toString());
        final List<ImportError> errors = importStatus.getErrors();
        for (final ImportError error : errors) {
            importStatusMessage.addError(error.getType().toString(), error.getName());
        }
        return importStatusMessage;
    }

}
