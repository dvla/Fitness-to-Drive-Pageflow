package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

public final class DataProcessorFactory
{
    public DataProcessorFactory() {
        super();
    }

    public IDataQuestionProcessor getQuestionProcessor(MedicalQuestion question) {
        IDataQuestionProcessor processor = null;

        if(Format.RADIO.equals(question.getFormat())) {
            processor = new DataProcessorRadioGroupImpl(question);
        } else if(Format.CHECKBOX.equals(question.getFormat())) {
            processor = new DataProcessorCheckboxGroupImpl(question);
        } else {
            throw new RuntimeException("Data processor is not supported!");
        }

        return processor;
    }
}
