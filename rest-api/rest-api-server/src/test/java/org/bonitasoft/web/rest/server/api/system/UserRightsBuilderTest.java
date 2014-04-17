package org.bonitasoft.web.rest.server.api.system;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * @author Vincent Elcrin
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRightsBuilderTest {

    private SHA1Generator generator = new SHA1Generator();

    @Mock
    private org.bonitasoft.engine.session.APISession session;

    @InjectMocks
    private UserRightsBuilder builder;

    @Test
    public void should_build_rights_for_a_token_add_to_it() throws Exception {
        given(session.getId()).willReturn(56L);

        builder.add("token");

        assertEquals(builder.build().get(0), generator.getHash("token56"));
    }

    @Test
    public void should_build_rights_for_all_tokens_progressively_add_to_it() throws Exception {
        given(session.getId()).willReturn(85L);

        builder.add("token 1");
        builder.add("token 2");
        List<String> rights = builder.build();

        assertEquals(rights.get(0), generator.getHash("token 185"));
        assertEquals(rights.get(1), generator.getHash("token 285"));
    }

    @Test
    public void should_build_rights_for_all_tokens_add_to_it() throws Exception {
        given(session.getId()).willReturn(5L);

        builder.add(Arrays.asList("token 1", "token 2"));
        List<String> rights = builder.build();

        assertEquals(rights.get(0), generator.getHash("token 15"));
        assertEquals(rights.get(1), generator.getHash("token 25"));
    }
}
