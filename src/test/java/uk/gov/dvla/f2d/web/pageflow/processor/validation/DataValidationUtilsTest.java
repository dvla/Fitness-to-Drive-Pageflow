package uk.gov.dvla.f2d.web.pageflow.processor.validation;


import junit.framework.TestCase;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation.DataValidation;
import uk.gov.dvla.f2d.web.pageflow.utils.DataValidationUtils;

import java.lang.reflect.Field;
import java.util.List;

public class DataValidationUtilsTest extends TestCase {
    MedicalQuestion medicalQuestion;

    String field = "field";

    @DataValidation(max=30, notNullOrEmpty = true)
    String FIELD_1 = "noErrors";

    @DataValidation(max=10)
    String FIELD_2 = "greater than allowed maximum Length";

    @DataValidation(min=-1, max = -1)
    String FIELD_3 = "no checks done, maximum is -1 and minimum is -1.";

    @DataValidation(notNullOrEmpty = true, max = 10000)
    String FIELD_4 = null;

    @DataValidation(notNullOrEmpty = true, max = 10000)
    String FIELD_5 = "";

    @DataValidation(notNullOrEmpty = true, max = 10000)
    String FIELD_6 = "        ";

    @DataValidation(notNullOrEmpty = false, max = 10000)
    String FIELD_7 = null;

    @DataValidation(notNullOrEmpty = false, max = 10000)
    String FIELD_8 = "";

    @DataValidation(notNullOrEmpty = false, max = 10000)
    String FIELD_9 = "        ";

    @DataValidation(regex = "[0-9]*", max = 5)
    String FIELD_10 = "Two errors needed, regex fails and max length fails";

    @DataValidation(regex = "[0-9]*", min = 1000, max = 100000)
    String FIELD_11 = "Two errors needed, regex fails and minimum length fails";

    @DataValidation(regex = "[A-Za-z,\\s]*", max = 100000)
    String FIELD_12 = "Only characters spaces and comma expected, regex passes";

    public void setUp() {
        medicalQuestion = new MedicalQuestion();
        medicalQuestion.setID("testPage");
    }

    /**
     * value - x less than minimum length  - 2, a MIN_LENGTH notification needs to be raised.
     */
    public void testCheckMin() {
        String value = "x";
        int minimumLength = 2;
        Notification notification = DataValidationUtils.checkMin(medicalQuestion, field , value, minimumLength);
        assertNotNull(notification);
        assertEquals(ErrorCodes.MIN_LENGTH, notification.getCode());
        assertEquals(ErrorCodes.MIN_LENGTH_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - x equals minimum length 1, a MIN_LENGTH notification should not be raised.
     */
    public void testCheckMin2() {
        String value = "x";
        int minimumLength = 1;
        Notification notification = DataValidationUtils.checkMin(medicalQuestion, field , value, minimumLength);
        assertNull(notification);
    }

    /**
     * value - xy greater than minimum length 1, a MIN_LENGTH notification should not be raised.
     */
    public void testCheckMin3() {
        String value = "xy";
        int minimumLength = 1;
        Notification notification = DataValidationUtils.checkMin(medicalQuestion, field , value, minimumLength);
        assertNull(notification);
    }

    /**
     * value - x greater than minimum length 0, a MIN_LENGTH notification should not be raised.
     */
    public void testCheckMin4() {
        String value = "x";
        int minimumLength = 0;
        Notification notification = DataValidationUtils.checkMin(medicalQuestion, field , value, minimumLength);
        assertNull(notification);
    }

    /**
     * value - xyz greater than maximum length  - 2, a MAX_LENGTH notification needs to be raised.
     */
    public void testCheckMax() {
        String value = "xyz";
        int maximumLength = 2;
        Notification notification = DataValidationUtils.checkMax(medicalQuestion, field , value, maximumLength);
        assertNotNull(notification);
        assertEquals(ErrorCodes.MAX_LENGTH, notification.getCode());
        assertEquals(ErrorCodes.MAX_LENGTH_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - x equals maximum length 1, a MAX_LENGTH notification should not be raised.
     */
    public void testCheckMax2() {
        String value = "x";
        int maximumLength = 1;
        Notification notification = DataValidationUtils.checkMax(medicalQuestion, field , value, maximumLength);
        assertNull(notification);
    }

    /**
     * value - xy less than maximum length 3, a MAX_LENGTH notification should not be raised.
     */
    public void testCheckMax3() {
        String value = "xy";
        int maximumLength = 3;
        Notification notification = DataValidationUtils.checkMax(medicalQuestion, field , value, maximumLength);
        assertNull(notification);
    }

    /**
     * value - x greater than maximum length 0, a MAX_LENGTH notification should be raised.
     */
    public void testCheckMax4() {
        String value = "x";
        int maximumLength = 0;
        Notification notification = DataValidationUtils.checkMax(medicalQuestion, field , value, maximumLength);
        assertNotNull(notification);
        assertEquals(ErrorCodes.MAX_LENGTH, notification.getCode());
        assertEquals(ErrorCodes.MAX_LENGTH_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - null, a NULL_OR_EMPTY notification should be raised.
     */
    public void testCheckNullOrEmpty() {
        String value = null;
        Notification notification = DataValidationUtils.checkNullOrEmpty(medicalQuestion, field , value);
        assertNotNull(notification);
        assertEquals(ErrorCodes.NULL_OR_EMPTY_CODE, notification.getCode());
        assertEquals(ErrorCodes.NULL_OR_EMPTY_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - "", a NULL_OR_EMPTY notification should be raised.
     */
    public void testCheckNullOrEmpty2() {
        String value = "";
        Notification notification = DataValidationUtils.checkNullOrEmpty(medicalQuestion, field , value);
        assertNotNull(notification);
        assertEquals(ErrorCodes.NULL_OR_EMPTY_CODE, notification.getCode());
        assertEquals(ErrorCodes.NULL_OR_EMPTY_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - "        ", a NULL_OR_EMPTY notification should be raised.
     */
    public void testCheckNullOrEmpty3() {
        String value = "        ";
        Notification notification = DataValidationUtils.checkNullOrEmpty(medicalQuestion, field , value);
        assertNotNull(notification);
        assertEquals(ErrorCodes.NULL_OR_EMPTY_CODE, notification.getCode());
        assertEquals(ErrorCodes.NULL_OR_EMPTY_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - "value", a NULL_OR_EMPTY notification should not be raised.
     */
    public void testCheckNullOrEmpty4() {
        String value = "value";
        Notification notification = DataValidationUtils.checkNullOrEmpty(medicalQuestion, field , value);
        assertNull(notification);
    }

    /**
     * value - "value", regex [0-9]* only numbers, validation needs to fail
     * and INVALID_CHARACTERS notification needs to be raised.
     */
    public void testRegex() {
        String value = "value";
        String regex = "[0-9]*";
        Notification notification = DataValidationUtils.checkRegex(medicalQuestion, field , value, regex);
        assertNotNull(notification);
        assertEquals(ErrorCodes.INVALID_CHARACTERS, notification.getCode());
        assertEquals(ErrorCodes.INVALID_CHARACTERS_DESC, notification.getDescription());
        assertEquals("TestPage", notification.getPage());
        assertEquals(field, notification.getField());
    }

    /**
     * value - "1234567890", regex [0-9]* only numbers, validation will not fail
     * and no notifications will be raised.
     */
    public void testRegex2() {
        String value = "1234567890";
        String regex = "[0-9]*";
        Notification notification = DataValidationUtils.checkRegex(medicalQuestion, field , value, regex);
        assertNull(notification);
    }

    /**
     * DataValidation(max=30, notNullOrEmpty = true)
     * FIELD_1 = "noErrors";
     * No errors expected as field is less than max allowed and  FIELD_1 value is not null or empty.
    */
    public void testField1() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_1");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_1);
        assertEquals(0, notifications.size());
    }

    /**
     * DataValidation(max=10)
     * FIELD_2 = "greater than allowed maximum Length";
     * No errors expected as field is less than max allowed and  FIELD_1 value is not null or empty.
     */
    public void testField2() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_2");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_2);
        assertEquals(1, notifications.size());

        Notification notification = notifications.get(0);
        assertEquals(ErrorCodes.MAX_LENGTH, notification.getCode());
        assertEquals(ErrorCodes.MAX_LENGTH_DESC, notification.getDescription());
    }

    /**
     * DataValidation(min=-1, max = -1)
     * FIELD_3 = "no checks done, maximum is -1 and minimum is -1.";
     * No errors expected as both min and max are -1 and hence no checks will be carried out.
     */
    public void testField3() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_3");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_3);
        assertEquals(0, notifications.size());
    }

    /**
     * DataValidation(notNullOrEmpty = true, max = 10000)
     * FIELD_4 = null;
     * NOT_NULL_OR_EMPTY notification expected since FIELD_4 is null.
     */
    public void testField4() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_4");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_4);
        assertEquals(1, notifications.size());

        Notification notification = notifications.get(0);
        assertEquals(ErrorCodes.NULL_OR_EMPTY_CODE, notification.getCode());
        assertEquals(ErrorCodes.NULL_OR_EMPTY_DESC, notification.getDescription());
    }

    /**
     * DataValidation(notNullOrEmpty = true, max = 10000)
     * FIELD_5 = "";
     * NOT_NULL_OR_EMPTY notification expected since FIELD_5 is blank.
     */
    public void testField5() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_5");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_5);
        assertEquals(1, notifications.size());

        Notification notification = notifications.get(0);
        assertEquals(ErrorCodes.NULL_OR_EMPTY_CODE, notification.getCode());
        assertEquals(ErrorCodes.NULL_OR_EMPTY_DESC, notification.getDescription());
    }

    /**
     * DataValidation(notNullOrEmpty = true, max = 10000)
     * FIELD_6 = "        ";
     * NOT_NULL_OR_EMPTY notification expected since FIELD_6 is only spaces and after trimming it will be blank.
     */
    public void testField6() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_6");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_6);
        assertEquals(1, notifications.size());

        Notification notification = notifications.get(0);
        assertEquals(ErrorCodes.NULL_OR_EMPTY_CODE, notification.getCode());
        assertEquals(ErrorCodes.NULL_OR_EMPTY_DESC, notification.getDescription());
    }

    /**
     * DataValidation(notNullOrEmpty = false, max = 10000)
     * FIELD_7 = null;
     * NOT_NULL_OR_EMPTY notification not expected since notNullOrEmpty is false even though value is null.
     */
    public void testField7() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_7");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_7);
        assertEquals(0, notifications.size());
    }

    /**
     * DataValidation(notNullOrEmpty = false, max = 10000)
     * FIELD_8 = "";
     * NOT_NULL_OR_EMPTY notification not expected since notNullOrEmpty is false even though value is blank.
     */
    public void testField8() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_8");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_8);
        assertEquals(0, notifications.size());
    }

    /**
     * DataValidation(notNullOrEmpty = false, max = 10000)
     * FIELD_9 = "        ";
     * NOT_NULL_OR_EMPTY notification not expected since notNullOrEmpty is false even though value is blank (after trimming).
     */
    public void testField9() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_9");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_9);
        assertEquals(0, notifications.size());
    }

    /**
     * DataValidation(regex = "[0-9]*", max = 5)
     * FIELD_10 = "Two errors needed, regex fails and max length fails"
     * Both MAX_LENGTH and INVALID_CHARS notifications expected.
     */
    public void testField10() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_10");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_10);
        assertEquals(2, notifications.size());


        Notification notification = notifications.get(0);
        assertEquals(ErrorCodes.MAX_LENGTH, notification.getCode());
        assertEquals(ErrorCodes.MAX_LENGTH_DESC, notification.getDescription());

        notification = notifications.get(1);
        assertEquals(ErrorCodes.INVALID_CHARACTERS, notification.getCode());
        assertEquals(ErrorCodes.INVALID_CHARACTERS_DESC, notification.getDescription());

    }

    /**
     * DataValidation(regex = "[0-9]*", min = 1000, max = 100000)
     * FIELD_11 = "Two errors needed, regex fails and minimum length fails"
     * Both MIN_LENGTH and INVALID_CHARS notifications expected.
     */
    public void testField11() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_11");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_11);
        assertEquals(2, notifications.size());


        Notification notification = notifications.get(0);
        assertEquals(ErrorCodes.MIN_LENGTH, notification.getCode());
        assertEquals(ErrorCodes.MIN_LENGTH_DESC, notification.getDescription());

        notification = notifications.get(1);
        assertEquals(ErrorCodes.INVALID_CHARACTERS, notification.getCode());
        assertEquals(ErrorCodes.INVALID_CHARACTERS_DESC, notification.getDescription());
    }

    /**
     * DataValidation(regex = "[A-Za-z,\s]*", max = 100000)
     * FIELD_12 = "Only characters spaces and comma expected, regex passes"
     * No errors thrown since regular expression expects only characters and , and space character.
     */
    public void testField12() throws Exception {
        Field field = this.getClass().getDeclaredField("FIELD_12");
        DataValidation validation = field.getAnnotation(DataValidation.class);
        List<Notification> notifications = DataValidationUtils.validateData(validation, medicalQuestion, field.getName(), FIELD_12);
        assertEquals(0, notifications.size());
    }

}
