package uk.gov.dvla.f2d.web.pageflow.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.model.MessageHeader;
import uk.gov.dvla.f2d.web.pageflow.utils.LogUtils;
import uk.gov.dvla.f2d.web.pageflow.utils.ServiceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class SummaryAggregator
{
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
        // Check to see if the quested service is supported?
        ServiceUtils.checkServiceSupported(form.getMessageHeader().getService());

        // Service is supported, so proceed to load the configuration.
        try {
            final String condition = form.getMedicalCondition().getID();
            final String service = form.getMessageHeader().getService();

            String resourceToLoad = (condition + HYPHEN_SYMBOL + service + JSON_SUFFIX);

            LogUtils.debug("Load Resource ["+resourceToLoad+"]");

            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceStream = classLoader.getResource(resourceToLoad).openStream();

            summary = new ObjectMapper().readValue(resourceStream, Summary.class);

            LogUtils.debug("Resource Loaded [Questions="+summary.getQuestions().size()+"]");

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public List<Line> process(MedicalForm form) {
        List<Line> response = new ArrayList<>();

        if(summary == null) {
            initialise(form);
        }

        MessageHeader header = form.getMessageHeader();
        MedicalCondition condition = form.getMedicalCondition();

        LogUtils.debug("Breadcrumbs: "+header.getBreadcrumb());

        for(String breadcrumb : form.getMessageHeader().getBreadcrumb()) {
            LogUtils.debug("- Breadcrumb: "+breadcrumb);

            for(MedicalQuestion question : condition.getQuestions().values()) {
                if(question.getStep().equals(breadcrumb) && !question.getAnswers().isEmpty()) {
                    LogUtils.debug("  - Step: "+question.getStep()+", Answers: "+question.getAnswers());

                    if (question.getType().equals(RADIO)) {
                        response.addAll(processRadio(summary, header, question));
                    } else if (question.getType().equals(CHECKBOX)) {
                        response.addAll(processCheckBox(summary, header, question));
                    } else if (question.getType().equals(FORM)) {
                        response.addAll(processForm(summary, header, question));
                    } else if (question.getType().equals(CONTINUE)) {
                        response.addAll(processContinue(summary, header, question));
                    } else {
                        throw new IllegalArgumentException("Question type is not supported: " + question.getType());
                    }
                }
            }
        }

        return response;
    }

    private List<Line> processRadio(Summary summary, MessageHeader header, MedicalQuestion question) {
        LogUtils.debug("    - Radio: "+question.getID()+", Answers: "+question.getAnswers());
        List<Line> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {
                Option option = summary.getQuestions().get(question.getID());
                Answer answer = option.getOptions().get(question.getAnswers().get(0));

                Line line = new Line();
                line.setType(question.getType());
                line.setSubHeading(null);
                line.getLines().add(answer.getAnswers().get(header.getLanguage()));
                line.setLink(question.getID());

                response.add(line);
            }
        }
        return response;
    }

    private List<Line> processCheckBox(Summary summary, MessageHeader header, MedicalQuestion question) {
        LogUtils.debug("    - Check: "+question.getID()+", Answers: "+question.getAnswers());
        List<Line> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {

                String heading = null;

                Line line = new Line();
                line.setType(question.getType());
                line.setSubHeading(question.getText());

                for(String value : question.getAnswers()) {
                    String key = value.split(HYPHEN_SYMBOL)[0];
                    if(!(key.equals(heading))) {
                        line.getLines().add("{b}"+key+"{/b}");
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
        LogUtils.debug("    - Form: "+question.getID()+", Answers: "+question.getAnswers());

        List<Line> response = new ArrayList<>();

        Line line = new Line();
        line.setType(question.getType());
        line.setSubHeading(null);
        line.setLines(question.getAnswers());
        line.setLink(question.getID());
        response.add(line);

        return response;
    }

    private List<Line> processContinue(Summary summary, MessageHeader header, MedicalQuestion question) {
        LogUtils.debug("    - Continue: "+question.getID()+", Answers: "+question.getAnswers());
        List<Line> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            Option option = summary.getQuestions().get(question.getID());
            Answer answer = option.getOptions().get(YES);

            Line line = new Line();
            line.setType(question.getType());
            line.setSubHeading(null);
            line.getLines().add(answer.getAnswers().get(header.getLanguage()));
            line.setLink(question.getID());

            response.add(line);
        }
        return response;
    }
}
