package org.bonitasoft.console.common.server.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Emmanuel Duchastenier
 */
public class AuthenticationManagerPropertiesTest {

    @Test
    public void isLogoutDisabled_should_return_FALSE_if_not_set() {
        // given:
        final AuthenticationManagerProperties properties = AuthenticationManagerProperties.getProperties(123L);

        // when:
        final boolean isLogoutDisabled = properties.isLogoutDisabled();

        // then:
        assertThat(isLogoutDisabled).isFalse();
    }
}