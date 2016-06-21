package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class DataProcessorFactory
{
    private static final Logger logger = LoggerFactory.getLogger(DataProcessorFactory.class);

    private static final String IMPL_PREFIX = "DataProcessor";
    private static final String IMPL_SUFFIX = "Impl";

    public DataProcessorFactory() {
        super();
    }

    public IDataQuestionProcessor getQuestionProcessor(MedicalQuestion question) {
        logger.debug("Attempting to instantiate the required data processor: "+question.getType());

        try {
            String definition = this.getClass().getCanonicalName();
            String packageName = definition.substring(0, definition.indexOf(getClass().getSimpleName()));

            Class<?> temp = Class.forName(packageName + IMPL_PREFIX + question.getType() + IMPL_SUFFIX);
            Constructor<?> con = temp.getDeclaredConstructor(MedicalQuestion.class);

            return (IDataQuestionProcessor)con.newInstance(question);

        } catch(ClassNotFoundException | NoSuchMethodException ex) {
            logger.error("An unexpected error occurred: ", ex);
            throw new RuntimeException(ex);

        } catch(IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            logger.error("An unexpected error occurred: ", ex);
            throw new RuntimeException(ex);
        }
    }
}
