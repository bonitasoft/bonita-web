package org.bonitasoft.forms.server.validator;

import junit.framework.Assert;

import org.bonitasoft.forms.client.model.FormFieldValue;
import org.junit.Before;
import org.junit.Test;

public class MailValidatorTest {

	// Basic email
	private final static String VALID_EMAIL_SIMPLE = "graham.smith@something.photography";
	// TODO: test TLD with i18n encoded characters
	//private final static String VALID_EMAIL_I18N = "graham.smith@something.XN--VERMGENSBERATUNG-PWB";
	// TLD with 1 character
	private final static String INVALID_EMAIL_TOO_SHORT_DOMAIN = "graham.smith@something.a";
	// TLD with 64 characters
	private final static String INVALID_EMAIL_TOO_LONG_DOMAIN = "graham.smith@something.abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz1234567890aa";
	
	MailValidator validator;
	
	@Before
    public void before(){
		validator = new MailValidator();
    }
	
	@Test
	public void simple_email_should_be_valid() {
		Assert.assertTrue(isEmailValid(VALID_EMAIL_SIMPLE));
	}
	
	@Test
	public void email_should_not_be_valid_because_of_too_short_domain() {
		Assert.assertFalse(isEmailValid(INVALID_EMAIL_TOO_SHORT_DOMAIN));
	}
	
	@Test
	public void email_should_not_be_valid_because_of_too_long_domain() {
		Assert.assertFalse(isEmailValid(INVALID_EMAIL_TOO_LONG_DOMAIN));
	}
	
	private boolean isEmailValid(final String email)
	{
		return validator.validate(new FormFieldValue(email, null), null);
	}
}
