package org.bonitasoft.reporting.utils.widget;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class QueryUtilsTest {
	
	String query = "SELECT PROCESSID As id, NAME As label  " +
			"From PROCESS_DEFINITION " +
			"Where TENANTID = $P{BONITA_TENANT_ID} " +
			"Order By label";

	@Test
	public void testWeCanFindSelectDirectivesFormQuery() {
		List<String> expected = Arrays.asList("PROCESSID As id", "NAME As label");
		
		List<String> selectDirectives = QueryUtils.getSelectDirectives(query);

		Assert.assertEquals(expected.toArray(), 
				selectDirectives.toArray());
	}
	
	@Test
	public void testWeCanGetAliasValueFromSelectDirective() throws Exception {
		List<String> selectDirectives = Arrays.asList("PROCESSID As id", "NAME As label");
		
		String aliasValue = QueryUtils.getAliasValue(selectDirectives, "label");
		
		assertEquals("NAME".toLowerCase(), aliasValue);
	}

}
