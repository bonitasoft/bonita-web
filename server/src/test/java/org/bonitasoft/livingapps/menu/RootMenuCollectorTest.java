package org.bonitasoft.livingapps.menu;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.business.application.impl.ApplicationMenuImpl;
import org.junit.Test;

public class RootMenuCollectorTest {

    RootMenuCollector collector = new RootMenuCollector();

    ApplicationMenuImpl menu = new ApplicationMenuImpl("name", 1L, 2L, 1);

    @Test
    public void should_be_collectible_when_the_menu_has_no_parentId() {
        menu.setParentId(null);

        assertThat(collector.isCollectible(menu)).isTrue();
    }

    @Test
    public void should_not_be_collectible_when_the_menu_has_a_parentId() {
        menu.setParentId(3L);

        assertThat(collector.isCollectible(menu)).isFalse();
    }
}