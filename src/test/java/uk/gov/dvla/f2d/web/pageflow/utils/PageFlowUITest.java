package uk.gov.dvla.f2d.web.pageflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;

import java.io.IOException;
import java.util.List;

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
     * This method is used to transpose the response back into the data model.
     * @param data - The JSON data which represents our client state
     * @param target - The class we wish to convert the model back into.
     * @return Object representing the data model from the response.
     */
    private Object hydrateObjectUsingMapper(final String data, Class target) {
        try {
            return new ObjectMapper().readValue(data, target);

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * Test to see if a medical questionnaire has been loaded from resources
     */
    public void testRetrieveSupportedMedicalConditions() {
        try {
            final String fullData = PageFlowUI.getFullQuestionnaire();

            assertNotNull(fullData);
            assertTrue("Questionnaire was not pre-populated", fullData.length() > 0);

            Object result = hydrateObjectUsingMapper(fullData, MedicalForm.class);

            assertTrue("Data structure was an unexpected type", (result instanceof MedicalForm));

            List<MedicalCondition> conditions = ((MedicalForm)result).getSupportedConditions();

            assertTrue("No conditions were found in data structure", conditions.size() > 0);
            assertTrue("No questions were found in data structure", conditions.size() > 0);

        } catch(JsonProcessingException ex) {
            fail("A JsonProcessingException should not have been raised.");
        }
    }

    public void testFindMedicalConditionByIdentifier() {
        try {
            final String DIABETES_CONDITION = "1";

            final String condition = PageFlowUI.getConditionByID(DIABETES_CONDITION);

            assertNotNull(condition);
            assertTrue("Condition configuration should be populated.", condition.trim().length() > 0);

            Object result = hydrateObjectUsingMapper(condition, MedicalCondition.class);

            MedicalCondition diabetes = (MedicalCondition)result;

            assertNotNull(diabetes);
            assertNotNull(diabetes.getQuestions());
            assertEquals(diabetes.getQuestions().size(), 9);

        } catch(JsonProcessingException ex) {
            fail("A JsonProcessingException should not have been raised.");
        }
    }
}
