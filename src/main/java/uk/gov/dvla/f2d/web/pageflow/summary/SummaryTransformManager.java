package uk.gov.dvla.f2d.web.pageflow.summary;

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

import static uk.gov.dvla.f2d.model.constants.StringConstants.FORWARD_SLASH;
import static uk.gov.dvla.f2d.model.constants.StringConstants.HYPHEN;
import static uk.gov.dvla.f2d.model.constants.StringConstants.YES;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public final class SummaryTransformManager
{
    private static final Logger logger = LoggerFactory.getLogger(SummaryTransformManager.class);

    public SummaryTransformManager() {
        super();
    }

    public List<Line> transform(MedicalForm form) {
        logger.info("Starting to transform summary information...");

        List<Line> response = new ArrayList<>();

        Summary summary = loadSummaryIntoInternalCache(form);

        MessageHeader header = form.getMessageHeader();
        MedicalCondition condition = form.getMedicalCondition();

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

    private Summary loadSummaryIntoInternalCache(MedicalForm form) {
        logger.info("Loading summary data into internal cache...");
        try {
            final String service = form.getMessageHeader().getService();
            final String condition = form.getMedicalCondition().getSlug();

            final String resourceToLoad = (
                    service + FORWARD_SLASH + condition + FORWARD_SLASH + SUMMARY_TRANSFORM + JSON_SUFFIX
            );

            logger.debug("Preparing to load: ["+resourceToLoad+"]");

            InputStream resourceStream = ResourceLoader.load(resourceToLoad);
            Summary summary = new ObjectMapper().readValue(resourceStream, Summary.class);

            logger.info("Summary data was loaded successfully...");

            return summary;

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private List<Line> processRadio(Summary summary, MessageHeader header, MedicalQuestion question) {
        List<Line> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {
                Option option = summary.getQuestions().get(question.getID());
                Answer answer = option.getOptions().get(question.getAnswers().get(0));

                if(answer != null) {
                    String text = answer.getAnswers().get(header.getLanguage());
                    if (text != null) {
                        Line line = new Line();
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

    private List<Line> processCheckBox(Summary summary, MessageHeader header, MedicalQuestion question) {
        List<Line> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {

                String heading = null;

                Line line = new Line();
                line.setType(question.getType());
                line.setSubHeading(question.getText());

                for(String value : question.getAnswers()) {
                    String key = value.split(HYPHEN)[0];
                    if(!(key.equals(heading))) {
                        line.getLines().add(BOLD_ON + key + BOLD_OFF);
                        heading = key;
                    }
                    Option option = summary.getQuestions().get(question.getID());
                    Answer answer = option.getOptions().get(value);

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

    private List<Line> processQuestion(MedicalQuestion question) {
        List<Line> response = new ArrayList<>();

        Line line = new Line();
        line.setType(question.getType());
        line.setSubHeading(question.getText());
        line.setLines(question.getAnswers());
        line.setLink(question.getID());
        response.add(line);

        return response;
    }

    private List<Line> processContinue(Summary summary, MessageHeader header, MedicalQuestion question) {
        List<Line> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            Option option = summary.getQuestions().get(question.getID());
            Answer answer = option.getOptions().get(YES);

            Line line = new Line();
            line.setType(question.getType());
            line.setSubHeading(question.getText());
            line.getLines().add(answer.getAnswers().get(header.getLanguage()));
            line.setLink(question.getID());

            response.add(line);
        }
        return response;
    }
}
