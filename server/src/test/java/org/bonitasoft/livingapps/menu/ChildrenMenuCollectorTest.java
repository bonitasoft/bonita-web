package org.bonitasoft.livingapps.menu;

import static org.assertj.core.api.Assertions.assertThat;

import org.bonitasoft.engine.business.application.impl.ApplicationMenuImpl;
import org.junit.Test;

public class ChildrenMenuCollectorTest {

    ChildrenMenuCollector collector = new ChildrenMenuCollector(1L);

    ApplicationMenuImpl menu = new ApplicationMenuImpl("name", 1L, 2L, 1);

    @Test
    public void should_be_collectible_when_menu_parentId_is_the_given_parentId() {
        menu.setParentId(1L);

        assertThat(collector.isCollectible(menu)).isTrue();
    }

    @Test
    public void should_not_be_collectible_when_menu_parentId_is_not_the_given_parentId() {
        menu.setParentId(2L);

        assertThat(collector.isCollectible(menu)).isFalse();
    }

    @Test
    public void should_not_be_collectible_when_menu_parentId_is_null() {
        menu.setParentId(null);

        assertThat(collector.isCollectible(menu)).isFalse();
    }
}