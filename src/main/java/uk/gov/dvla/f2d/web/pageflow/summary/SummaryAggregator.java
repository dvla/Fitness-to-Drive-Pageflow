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

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;
import static uk.gov.dvla.f2d.model.constants.StringConstants.*;

public class SummaryAggregator
{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static SummaryAggregator instance;

    private Summary summary;

    private SummaryAggregator() {
        super();
    }

    public static synchronized SummaryAggregator getInstance() {
        if(instance == null) {
            instance = new SummaryAggregator();
        }
        return instance;
    }

    private void initialise(MedicalForm form) {
        // Service is supported, so proceed to load the configuration.
        try {
            final String condition = form.getMedicalCondition().getSlug();
            final String service = form.getMessageHeader().getService();

            String resourceToLoad = (condition + HYPHEN + service + JSON_SUFFIX);

            logger.debug("Load Resource ["+resourceToLoad+"]");

            InputStream resourceStream = ResourceLoader.load(resourceToLoad);

            summary = new ObjectMapper().readValue(resourceStream, Summary.class);

            logger.debug("Resource Loaded [Questions="+summary.getQuestions().size()+"]");

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private void checkIntegrity(MedicalForm form) {
        initialise(form);
    }

    public List<Line> process(MedicalForm form) {
        List<Line> response = new ArrayList<>();

        checkIntegrity(form);

        MessageHeader header = form.getMessageHeader();
        MedicalCondition condition = form.getMedicalCondition();

        logger.debug("Breadcrumbs: "+header.getBreadcrumb());

        for(String breadcrumb : form.getMessageHeader().getBreadcrumb()) {
            logger.debug("Breadcrumb: "+breadcrumb);

            for(MedicalQuestion question : condition.getQuestions().values()) {
                if(question.getStep().equals(breadcrumb) && question.getSummary()) {

                    logger.debug("Step: " + question.getStep() + "-> " + question.getID());
                    logger.debug("Answers: " + question.getAnswers());
                    logger.debug("Size: " + question.getAnswers().size());
                    logger.debug("Empty: " + question.getAnswers().isEmpty());

                    if(!(question.getAnswers().isEmpty())) {
                        if (question.getType().equals(Format.RADIO.toString())) {
                            response.addAll(processRadio(summary, header, question));
                        } else if (question.getType().equals(Format.CHECKBOX.toString())) {
                            response.addAll(processCheckBox(summary, header, question));
                        } else if (question.getType().equals(Format.FORM.toString())) {
                            response.addAll(processForm(summary, header, question));
                        } else if (question.getType().equals(Format.CONTINUE.toString())) {
                            response.addAll(processContinue(summary, header, question));
                        } else {
                            throw new IllegalArgumentException("Type is not supported: " + question.getType());
                        }
                    }
                }
            }
        }

        return response;
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

    private List<Line> processForm(Summary summary, MessageHeader header, MedicalQuestion question) {
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
