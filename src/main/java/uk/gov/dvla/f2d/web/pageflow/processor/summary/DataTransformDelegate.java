package uk.gov.dvla.f2d.web.pageflow.processor.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.MessageHeader;
import uk.gov.dvla.f2d.web.pageflow.enums.Format;
import uk.gov.dvla.f2d.web.pageflow.loaders.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.model.constants.StringConstants.*;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class DataTransformDelegate
{
    private static final Logger logger = LoggerFactory.getLogger(DataTransformPipeline.class);

    private MedicalForm form;

    DataTransformDelegate(MedicalForm form) {
        this.form = form;
    }

    List<SummaryLine> process() {
        logger.info("Starting to transform summary information...");

        List<SummaryLine> response = new ArrayList<>();

        SummaryAggregate summary = loadSummaryIntoInternalCache();

        MessageHeader header = form.getMessageHeader();
        MedicalCondition condition = form.getMedicalCondition();

        // FIXME: Consider rewriting this, not very efficient code.
        for(String breadcrumb : form.getMessageHeader().getBreadcrumb()) {
            for(MedicalQuestion question : condition.getQuestions().values()) {
                if(question.getStep().equals(breadcrumb) && question.getSummary()) {
                    if(!(question.getAnswers().isEmpty())) {
                        if (question.getType().equals(Format.RADIO.getName())) {
                            response.addAll(processRadio(summary, header, question));
                        } else if (question.getType().equals(Format.CHECKBOX.getName())) {
                            response.addAll(processCheckBox(summary, header, question));
                        } else if (question.getType().equals(Format.FORM.getName())) {
                            response.addAll(processQuestion(question));
                        } else if (question.getType().equals(Format.CONTINUE.getName())) {
                            response.addAll(processContinue(summary, header, question));
                        } else {
                            throw new IllegalArgumentException("Type is not supported: " + question.getType());
                        }
                    }
                }
            }
        }

        logger.info("Finishing processing summary information...");

        return response;
    }

    private SummaryAggregate loadSummaryIntoInternalCache() {
        logger.info("Loading summary data into internal cache...");
        try {
            final String service = form.getMessageHeader().getService();
            final String condition = form.getMedicalCondition().getGlobal();

            final String resourceToLoad = (
                    service + FORWARD_SLASH + condition + FORWARD_SLASH + SUMMARY_TRANSFORM + JSON_SUFFIX
            );

            logger.debug("Preparing to load: ["+resourceToLoad+"]");

            InputStream resourceStream = ResourceLoader.load(resourceToLoad);
            SummaryAggregate summary = new ObjectMapper().readValue(resourceStream, SummaryAggregate.class);

            logger.info("SummaryAggregate data was loaded successfully...");

            return summary;

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }


    List<SummaryLine> processRadio(SummaryAggregate summary, MessageHeader header, MedicalQuestion question) {
        List<SummaryLine> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {
                SummaryOption option = summary.getQuestions().get(question.getID());
                SummaryAnswer answer = option.getOptions().get(question.getAnswers().get(0));

                if(answer != null) {
                    String text = answer.getAnswers().get(header.getLanguage());
                    if (text != null) {
                        SummaryLine line = new SummaryLine();
                        line.setType(question.getType());
                        line.setSubHeading(question.getText());
                        line.getLines().add(text);
                        line.setLink(question.getID());

                        response.add(line);
                    }
                }
            }
        }
        return response;
    }

    List<SummaryLine> processCheckBox(SummaryAggregate summary, MessageHeader header, MedicalQuestion question) {
        List<SummaryLine> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {

                String heading = null;

                SummaryLine line = new SummaryLine();
                line.setType(question.getType());
                line.setSubHeading(question.getText());

                for(String value : question.getAnswers()) {
                    String key = value.split(HYPHEN)[0];
                    if(!(key.equals(heading))) {
                        line.getLines().add(BOLD_ON + key + BOLD_OFF);
                        heading = key;
                    }
                    SummaryOption option = summary.getQuestions().get(question.getID());
                    SummaryAnswer answer = option.getOptions().get(value);

                    String text = answer.getAnswers().get(header.getLanguage());
                    if(text != null) {
                        line.getLines().add(text);
                    }
                }

                line.setLink(question.getID());

                response.add(line);
            }
        }
        return response;
    }

    List<SummaryLine> processQuestion(MedicalQuestion question) {
        List<SummaryLine> response = new ArrayList<>();

        SummaryLine line = new SummaryLine();
        line.setType(question.getType());
        line.setSubHeading(question.getText());
        line.setLines(question.getAnswers());
        line.setLink(question.getID());
        response.add(line);

        return response;
    }

    List<SummaryLine> processContinue(SummaryAggregate summary, MessageHeader header, MedicalQuestion question) {
        List<SummaryLine> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            SummaryOption option = summary.getQuestions().get(question.getID());
            SummaryAnswer answer = option.getOptions().get(YES);

            SummaryLine line = new SummaryLine();
            line.setType(question.getType());
            line.setSubHeading(question.getText());
            line.getLines().add(answer.getAnswers().get(header.getLanguage()));
            line.setLink(question.getID());

            response.add(line);
        }
        return response;
    }
}
