package validation;

import junit.framework.Assert;
import junit.framework.TestCase;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.*;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import uk.gov.dvla.f2d.web.pageflow.processor.validation.FormValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes.*;

/**
 * Tests validation rules on Contact Details.
 */
public class TestContactDetailsValidator extends TestCase{
    private MedicalForm medicalForm;
    private MedicalQuestion medicalQuestion;

    Map<String, String[]> entities = new HashMap<>();

    private final String PHONE_NUMBER = "phoneNumber";
    private final String EMAIL_ADDRESS = "emailAddress";

    private final String[] VALID_PHONE_NUMBER = {"1234567890"};
    private final String[] VALID_EMAIL_ADDRESS = {"test@example.com"};

    PageForm pageForm = new PageForm();

    public void setUp(){
        medicalForm = new MedicalForm();


        medicalForm.setMessageHeader(new MessageHeader());
        medicalForm.getMessageHeader().setNotifications(new ArrayList<>());
        medicalForm.getMessageHeader().setService(Service.NOTIFY.getName());

        MedicalCondition condition = new MedicalCondition();
        condition.setID("diabetes");
        medicalForm.setMedicalCondition(condition);

        medicalQuestion = new MedicalQuestion();
        medicalQuestion.setID("contact-details");
        medicalQuestion.setValidate(true);
        medicalQuestion.setType(Format.FORM.getName());

        Map<String, MedicalQuestion> medicalQuestions = new HashMap<>();
        medicalQuestions.put(medicalQuestion.getID(), medicalQuestion);
        condition.setQuestions(medicalQuestions);

        entities = new HashMap<>();
        entities.put(PHONE_NUMBER, VALID_PHONE_NUMBER);
        entities.put(EMAIL_ADDRESS, VALID_EMAIL_ADDRESS);

        pageForm = new PageForm();
        pageForm.setQuestion("contact-details");
    }

    public void testValidPhoneNumber() {
        //valid - 0-9 and spaces, max 12 characters
        String answers[] = {"12345 67890"};

        entities.put(PHONE_NUMBER, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }

    public void testBlankPhoneNumber() {
        //valid - blank
        String answers[] = {""};

        entities.put(PHONE_NUMBER, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }

    public void testNullPhoneNumber() {
        //valid - null
        String answers[] = {null};

        entities.put(PHONE_NUMBER, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }

    public void testInvalidPhoneNumber() {
        //invalid - got characters ph
        String answers[] = {"ph123456"};

        entities.put(PHONE_NUMBER, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(1, medicalForm.getMessageHeader().getNotifications().size());
        Notification notification = medicalForm.getMessageHeader().getNotifications().get(0);
        Assert.assertEquals(INVALID_CHARACTERS, notification.getCode());
    }

    public void testInvalidPhoneNumberLength() {
        //invalid - length greater than 12
        String answers[] = {"123456789012345"};

        entities.put(PHONE_NUMBER, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(1, medicalForm.getMessageHeader().getNotifications().size());
        Notification notification = medicalForm.getMessageHeader().getNotifications().get(0);
        Assert.assertEquals(EXCEEDS_MAX_LENGTH, notification.getCode());
    }


    public void testInvalidCharsAndPhoneNumberLength() {
        //invalid - length greater than 12
        //invalid - characters in phone number
        String answers[] = {"invalid and longer than expected"};

        entities.put(PHONE_NUMBER, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(2, medicalForm.getMessageHeader().getNotifications().size());
        Notification notification = medicalForm.getMessageHeader().getNotifications().get(0);
        Assert.assertEquals(EXCEEDS_MAX_LENGTH, notification.getCode());
        notification = medicalForm.getMessageHeader().getNotifications().get(1);
        Assert.assertEquals(INVALID_CHARACTERS, notification.getCode());
    }

    public void testValidEmailAddress() {
        //optional - no validations, any text is valid
        String answers[] = {"test@test.com"};

        entities.put(EMAIL_ADDRESS, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }

    public void testBlankEmailAddress() {
        //valid - blank
        String answers[] = {""};

        entities.put(EMAIL_ADDRESS, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }

    public void testNullEmailAddress() {
        //valid - null
        String answers[] = {null};

        entities.put(EMAIL_ADDRESS, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }

    public void testInvalidEmailAddress() {
        //invalid - email address but no validations so should not throw error
        String answers[] = {"not a valid email address but there are no restrictions on content or length"};

        entities.put(EMAIL_ADDRESS, answers);
        pageForm.setEntities(entities);
        FormValidator validator = new FormValidator(medicalForm, medicalQuestion);
        validator.validate(pageForm);

        Assert.assertNotNull(medicalForm.getMessageHeader().getNotifications());
        Assert.assertEquals(0, medicalForm.getMessageHeader().getNotifications().size());
    }
}