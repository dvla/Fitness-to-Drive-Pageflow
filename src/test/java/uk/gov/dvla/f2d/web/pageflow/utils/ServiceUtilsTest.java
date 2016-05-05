package uk.gov.dvla.f2d.web.pageflow.utils;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import static uk.gov.dvla.f2d.model.constants.Constants.*;

public class ServiceUtilsTest extends TestCase
{
    /**
     * Create the test case
     * @param testName name of the test case
     */
    public ServiceUtilsTest(String testName ) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ServiceUtilsTest.class);
    }

    public void testNotifyServiceSupported() {
        try {
            ServiceUtils.checkServiceSupported(NOTIFY_SERVICE);

        } catch(IllegalArgumentException ex) {
            fail("An IllegalArgumentException should not have been raised.");
        }
    }

    public void testRenewalServiceUnsupported() {
        try {
            ServiceUtils.checkServiceSupported("renewal");

            fail("An IllegalArgumentException should have been raised.");

        } catch(IllegalArgumentException ex) {
            // This is the expected behaviour.
        }
    }

    public void testEnglishLanguageSupported() {
        try {
            ServiceUtils.checkLanguageSupported(ENGLISH_LANGUAGE);

        } catch(IllegalArgumentException ex) {
            fail("An IllegalArgumentException should not have been raised.");
        }
    }

    public void testWelshLanguageSupported() {
        try {
            ServiceUtils.checkLanguageSupported(WELSH_LANGUAGE);

            fail("An IllegalArgumentException should have been raised.");

        } catch(IllegalArgumentException ex) {
            // Success
        }
    }

    public void testSpanishLanguageUnsupported() {
        try {
            ServiceUtils.checkLanguageSupported("es");

            fail("An IllegalArgumentException should not have been raised.");

        } catch(IllegalArgumentException ex) {
            // This is the expected behaviour.
        }
    }
}
