package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;

public final class DataProcessorFactory
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public DataProcessorFactory() {
        super();
    }

    public IDataQuestionProcessor getQuestionProcessor(MedicalQuestion question) {
        logger.debug("Question Type: " + question.getType());

        IDataQuestionProcessor processor = null;

        switch(Format.lookup(question.getType())) {
            case RADIO:
                processor = new DataProcessorRadioGroupImpl(question); break;

            case CHECKBOX:
                processor = new DataProcessorCheckBoxGroupImpl(question); break;

            case FORM:
                processor = new DataProcessorFormPageImpl(question); break;

            case CONTINUE:
                processor = new DataProcessorContinuePageImpl(question); break;
        }

        return processor;
    }
}
