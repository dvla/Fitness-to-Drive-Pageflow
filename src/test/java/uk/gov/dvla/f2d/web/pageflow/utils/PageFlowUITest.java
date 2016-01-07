package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;

import java.io.IOException;
import java.util.Map;

public class PageFlowUITest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PageFlowUITest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( PageFlowUITest.class );
    }

    /**
     * Test to see if a medical questionnaire has been loaded from resources
     */
    public void testRetrieveSupportedMedicalConditions() {
        try {
            final String fullData = PageFlowUI.getMedicalForm();

            assertNotNull(fullData);
            assertTrue("Questionnaire was not pre-populated", fullData.length() > 0);

            MedicalForm form = MapUtils.mapStringToModel(fullData);

            assertNotNull(form);

            Map<String, MedicalCondition> conditions = form.getSupportedConditions();

            assertTrue("No conditions were found in data structure", conditions.size() > 0);
            assertTrue("No questions were found in data structure", conditions.size() > 0);

        } catch(JsonProcessingException ex) {
            fail("A JsonProcessingException should not have been raised.");

        } catch(IOException ex) {
            fail("An IOException should not have been raised.");
        }
    }
}
