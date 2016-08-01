package uk.gov.dvla.f2d.web.pageflow.processor.validation.forms;
import junit.framework.TestCase;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.constants.ErrorCodes;
import uk.gov.dvla.f2d.web.pageflow.forms.PageForm;
import static org.mockito.Mockito.*;

import java.util.List;

public class ConsultantDetailsTest extends TestCase{

    public static final String VALUE_CONSULTANT = "Consultant";
    public static final String VALUE_CLINIC = "Clinic";
    public static final String VALUE_HOSPITAL = "Hospital";
    public static final String VALUE_POST_TOWN = "Swansea";
    public static final String VALUE_POSTCODE = "SA1 123";
    public static final String VALUE_PHONE_NUMBER = "0123456789";

    private final MedicalForm dummyForm = mock(MedicalForm.class);
    private final MedicalQuestion dummyQuestion = mock(MedicalQuestion.class);
    private final ConsultantDetails validator = new ConsultantDetails(dummyForm, dummyQuestion);

    public void setUp() throws Exception {
        when(dummyQuestion.getID()).thenReturn("consultant-details");
    }

    public void testExecuteWithProperAllMandatoryData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});
        pageForm.getEntities().put(ConsultantDetails.CLINIC_NAME, new String[]{VALUE_CLINIC});
        pageForm.getEntities().put(ConsultantDetails.HOSPITAL_NAME, new String[]{VALUE_HOSPITAL});
        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(0, notifications.size());
    }

    public void testExecuteWithProperAllMandatoryBesideHospitalAndClinicNameData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});
        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(2, notifications.size());
        Notification wrongNotificationClinic = notifications.get(0);
        Notification wrongNotificationHospital = notifications.get(1);
        assertEquals(ErrorCodes.AT_LEAST_ONE_FIELD, wrongNotificationClinic.getCode());
        assertEquals(ErrorCodes.AT_LEAST_ONE_FIELD, wrongNotificationHospital.getCode());
    }

    public void testExecuteWithProperAllMandatoryBesideHospitalNameData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});
        pageForm.getEntities().put(ConsultantDetails.CLINIC_NAME, new String[]{VALUE_CLINIC});

        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(0, notifications.size());

    }

    public void testExecuteWithProperAllMandatoryBesideClinicNameData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});

        pageForm.getEntities().put(ConsultantDetails.HOSPITAL_NAME, new String[]{VALUE_HOSPITAL});
        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(0, notifications.size());
    }

    public void testExecuteWithoutMandatoryData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.POST_CODE, new String[]{VALUE_POSTCODE});
        pageForm.getEntities().put(ConsultantDetails.PHONE_NUMBER, new String[]{VALUE_PHONE_NUMBER});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(4, notifications.size());
    }
}
