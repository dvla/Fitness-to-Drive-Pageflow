package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.model.MessageHeader;
import uk.gov.dvla.f2d.web.pageflow.utils.LogUtils;

public final class DataProcessorFactory
{
    public DataProcessorFactory() {
        super();
    }

    public IDataQuestionProcessor getQuestionProcessor(MedicalQuestion question) {
        IDataQuestionProcessor processor = null;

        LogUtils.debug(this.getClass(), "Question Type: " + question.getType());

        if(Format.FORM.equals(question.getType())) {
            LogUtils.debug(this.getClass(), "  + Form Processor");
            processor = new DataProcessorFormPageImpl(question);

        } else if(Format.RADIO.equals(question.getType())) {
            LogUtils.debug(this.getClass(), "  + Radio Processor");
            processor = new DataProcessorRadioGroupImpl(question);

        } else if(Format.CHECKBOX.equals(question.getType())) {
            LogUtils.debug(this.getClass(), "  + CheckBox Processor");
            processor = new DataProcessorCheckboxGroupImpl(question);

        } else if(Format.CONTINUE.equals(question.getType())) {
            LogUtils.debug(this.getClass(), "  + Continue Processor");
            processor = new DataProcessorContinuePageImpl(question);

        } else {
            throw new IllegalArgumentException("Processor is not supported: "+question.getType());
        }

        return processor;
    }
}
