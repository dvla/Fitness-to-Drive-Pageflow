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

        assertEquals(notifications.size(), 0);
    }

    public void testExecuteWithProperAllMandatoryBesideHospitalAndClinicNameData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});
        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(notifications.size(), 2);
        Notification wrongNotificationClinic = notifications.get(0);
        Notification wrongNotificationHospital = notifications.get(1);
        assertEquals(wrongNotificationClinic.getCode(), ErrorCodes.AT_LEAST_ONE_FIELD);
        assertEquals(wrongNotificationHospital.getCode(), ErrorCodes.AT_LEAST_ONE_FIELD);
    }

    public void testExecuteWithProperAllMandatoryBesideHospitalNameData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});
        pageForm.getEntities().put(ConsultantDetails.CLINIC_NAME, new String[]{VALUE_CLINIC});

        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(notifications.size(), 0);

    }

    public void testExecuteWithProperAllMandatoryBesideClinicNameData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.CONSULTANT_NAME, new String[]{VALUE_CONSULTANT});

        pageForm.getEntities().put(ConsultantDetails.HOSPITAL_NAME, new String[]{VALUE_HOSPITAL});
        pageForm.getEntities().put(ConsultantDetails.POST_TOWN, new String[]{VALUE_POST_TOWN});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(notifications.size(), 0);

    }

    public void testExecuteWithoutMandatoryData() {
        PageForm pageForm = new PageForm();
        pageForm.getEntities().put(ConsultantDetails.POST_CODE, new String[]{VALUE_POSTCODE});
        final List<Notification> notifications = validator.validate(pageForm);

        assertEquals(notifications.size(), 4);
    }
}
