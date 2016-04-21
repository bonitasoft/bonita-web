package org.bonitasoft.console.common.server.i18n;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.bonitasoft.web.toolkit.client.common.i18n.AbstractI18n;
import org.junit.Test;

/**
 * @author Emmanuel Duchastenier
 */
public class I18nTest {

    @Test
    public void getStreams_should_read_from_classpath() throws Exception {
        final List<InputStream> streams = I18n.getInstance().getStreams(AbstractI18n.LOCALE.fr);
        assertThat(streams).hasSize(2);
        assertThat(streams.get(0)).isNotNull();
    }

    @Test
    public void getStreams_should_not_fail_if_no_resource_found() throws Exception {
        final List<InputStream> streams = I18n.getInstance().getStreams(AbstractI18n.LOCALE.es);
        assertThat(streams).isEmpty();
    }

    @Test
    public void getAvailableLocalesFor_should_look_in_classpath() throws Exception {
        final Map<String, String> availableLocales = I18n.getAvailableLocalesFor("test");
        assertThat(availableLocales).containsKey("fr");
    }

    @Test
    public void loadLocale_should_read_translation_from_classpath() {
        final I18n i18n = I18n.getInstance();
        i18n.refresh();
        i18n.loadLocale(AbstractI18n.LOCALE.fr);
        final Map<String, String> translations = i18n.getLocale(AbstractI18n.LOCALE.fr);

        assertThat(translations.get("test key")).isEqualTo("Valeur de test en français");
    }

    @Test
    public void loadLocale_should_read_from_FS_if_custom_property_set() throws Exception {
        System.setProperty(I18n.I18N_CUSTOM_DIR_PROPERTY, this.getClass().getResource("/custom_po_resource").getPath());

        final I18n i18n = I18n.getInstance();
        i18n.refresh();
        i18n.loadLocale(AbstractI18n.LOCALE.es);
        final Map<String, String> translations = i18n.getLocale(AbstractI18n.LOCALE.es);

        assertThat(translations.get("test key")).isEqualTo("valor de prueba en Espanol");

        System.clearProperty(I18n.I18N_CUSTOM_DIR_PROPERTY);
    }

    @Test
    public void loadLocale_should_override_value_if_custom_property_set() throws Exception {
        System.setProperty(I18n.I18N_CUSTOM_DIR_PROPERTY, this.getClass().getResource("/custom_po_resource").getPath());

        final I18n i18n = I18n.getInstance();
        i18n.refresh();
        i18n.loadLocale(AbstractI18n.LOCALE.fr);
        final Map<String, String> translations = i18n.getLocale(AbstractI18n.LOCALE.fr);

        assertThat(translations.get("test key")).isEqualTo("Valeur modifiée");

        System.clearProperty(I18n.I18N_CUSTOM_DIR_PROPERTY);
    }

    @Test
    public void loadLocale_should_merge_values_from_classpath_and_filesystem() throws Exception {
        System.setProperty(I18n.I18N_CUSTOM_DIR_PROPERTY, this.getClass().getResource("/custom_po_resource").getPath());

        final I18n i18n = I18n.getInstance();
        i18n.refresh();
        i18n.loadLocale(AbstractI18n.LOCALE.fr);
        final Map<String, String> translations = i18n.getLocale(AbstractI18n.LOCALE.fr);

        assertThat(translations.get("test key")).isEqualTo("Valeur modifiée");
        assertThat(translations.get("about")).isEqualTo("Copyright Bonitasoft 2016");
        assertThat(translations.get("web site title")).isEqualTo("Bienvenue dans Bonita BPM Portal 7+");

        System.clearProperty(I18n.I18N_CUSTOM_DIR_PROPERTY);
    }
}
