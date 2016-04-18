package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;

public final class DataProcessorFactory {
    private Logger logger = LoggerFactory.getLogger(DataProcessorFactory.class);

    public DataProcessorFactory() {
        super();
    }

    public IDataQuestionProcessor getQuestionProcessor(MedicalQuestion question) {
        IDataQuestionProcessor processor = null;

        logger.debug("Question Type: " + question.getType());

        if(Format.FORM.equals(question.getType())) {
            logger.debug("Form Processor");
            processor = new DataProcessorFormPageImpl(question);

        } else if(Format.RADIO.equals(question.getType())) {
            logger.debug("Radio Processor");
            processor = new DataProcessorRadioGroupImpl(question);

        } else if(Format.CHECKBOX.equals(question.getType())) {
            logger.debug("CheckBox Processor");
            processor = new DataProcessorCheckboxGroupImpl(question);

        } else if(Format.CONTINUE.equals(question.getType())) {
            logger.debug("Continue Processor");
            processor = new DataProcessorContinuePageImpl(question);

        } else {
            throw new IllegalArgumentException("Processor is not supported: "+question.getType());
        }

        return processor;
    }
}
