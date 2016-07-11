package uk.gov.dvla.f2d.web.pageflow.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.exceptions.SystemException;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;

import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.utils.StringUtils.splitAndCapitalise;

public final class FormValidatorFactory
{
    private static final Logger logger = LoggerFactory.getLogger(FormValidatorFactory.class);

    private static final String FORMS_PACKAGE   = "uk.gov.dvla.f2d.web.pageflow.validation.forms.";

    private FormValidatorFactory() {
        super();
    }

    public static IFormValidator getValidator(MedicalQuestion question) {
        logger.debug("Get Validator: ["+question.getID()+"]");
        try {
            // Make sure we are only using form-based validation routines.
            if(!(question.getType().equalsIgnoreCase(Format.FORM.getName()))) {
                throw new IllegalArgumentException("Question is not a FORM type.");
            }

            // Define our implementation instance name for form validation.
            String implementation = (FORMS_PACKAGE + splitAndCapitalise(question.getID(), HYPHEN));

            logger.debug("Load Validator: ["+implementation+"]");

            // Instantiate and assign our data to our form valiator.
            Class validatorClass = Class.forName(implementation);

            IFormValidator validator = (IFormValidator)validatorClass.newInstance();
            validator.setQuestion(question);

            logger.debug("Validator Created: ["+validator.getClass().getName()+"]");

            return validator;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            throw new SystemException(ex);
        }
    }
}
