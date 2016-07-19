package uk.gov.dvla.f2d.web.pageflow.processor.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.splitAndCapitalise;

public final class FormValidatorFactory
{
    private static final Logger logger = LoggerFactory.getLogger(FormValidatorFactory.class);

    private static final String FORMS_PACKAGE   = "uk.gov.dvla.f2d.web.pageflow.processor.validation.forms.";

    private MedicalForm form;
    private MedicalQuestion question;

    public FormValidatorFactory(MedicalForm form, MedicalQuestion question) {
        this.form = form;
        this.question = question;
    }

    public IFormValidator getValidator() {
        logger.debug("Attempting to instantiate the required form validator: "+question.getType());

        try {
            // Make sure we are only using form-based validation routines.
            if(!(question.getType().equalsIgnoreCase(Format.FORM.getName()))) {
                throw new IllegalArgumentException("Question is not a FORM type.");
            }

            // Determine which form validator implementation we should be using.
            Class<?> temp = Class.forName(FORMS_PACKAGE + splitAndCapitalise(question.getID(), HYPHEN));
            Constructor<?> con = temp.getDeclaredConstructor(MedicalForm.class, MedicalQuestion.class);

            logger.debug("Preparing to create validator: "+temp.getSimpleName());

            return (IFormValidator)con.newInstance(form, question);

        } catch(ClassNotFoundException | NoSuchMethodException ex) {
            logger.error("An unexpected error occurred: ", ex);
            throw new RuntimeException(ex);

        } catch(IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            logger.error("An unexpected error occurred: ", ex);
            throw new RuntimeException(ex);
        }
    }
}
