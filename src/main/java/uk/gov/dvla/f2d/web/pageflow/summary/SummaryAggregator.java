package uk.gov.dvla.f2d.web.pageflow.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.model.MessageHeader;
import uk.gov.dvla.f2d.web.pageflow.utils.ServiceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

public class SummaryAggregator
{
    private static SummaryAggregator instance;

    private SummaryAggregator() {
        super();
    }

    public static synchronized SummaryAggregator getInstance() {
        if(instance == null) {
            instance = new SummaryAggregator();
        }
        return instance;
    }

    private Summary aggregate(MedicalForm form) {

        // Check to see if the quested service is supported?
        ServiceUtils.checkServiceSupported(form.getMessageHeader().getService());

        // Service is supported, so proceed to load the configuration.
        try {
            final String condition = form.getMedicalCondition().getID();
            final String service = form.getMessageHeader().getService();

            String resourceToLoad = (condition + HYPHEN_SYMBOL + service + JSON_SUFFIX);

            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceStream = classLoader.getResource(resourceToLoad).openStream();

            return new ObjectMapper().readValue(resourceStream, Summary.class);

        } catch(IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    public List<String> process(MedicalForm form) {
        List<String> response = new ArrayList<>();

        Summary summary = aggregate(form);

        MessageHeader header = form.getMessageHeader();
        MedicalCondition condition = form.getMedicalCondition();

        for(String breadcrumb : form.getMessageHeader().getBreadcrumb()) {
            for(MedicalQuestion question : condition.getQuestions().values()) {
                if(question.getStep().equals(breadcrumb) && !question.getAnswers().isEmpty()) {
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

    private List<String> processRadio(Summary summary, MessageHeader header, MedicalQuestion question) {
        List<String> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {
                Option option = summary.getQuestions().get(question.getID());
                Answer answer = option.getOptions().get(question.getAnswers().get(0));
                response.add(answer.getAnswers().get(header.getLanguage()));
            }
        }
        return response;
    }

    private List<String> processCheckBox(Summary summary, MessageHeader header, MedicalQuestion question) {
        List<String> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            if(!(question.getAnswers().isEmpty())) {
                for(String value : question.getAnswers()) {
                    Option option = summary.getQuestions().get(question.getID());
                    Answer answer = option.getOptions().get(value);
                    response.add(answer.getAnswers().get(header.getLanguage()));
                }
            }
        }
        return response;
    }

    private List<String> processForm(Summary summary, MessageHeader header, MedicalQuestion question) {
        return question.getAnswers().stream().collect(Collectors.toList());
    }

    private List<String> processContinue(Summary summary, MessageHeader header, MedicalQuestion question) {
        List<String> response = new ArrayList<>();
        if(summary.getQuestions().containsKey(question.getID())) {
            Option option = summary.getQuestions().get(question.getID());
            Answer answer = option.getOptions().get(YES);
            response.add(answer.getAnswers().get(header.getLanguage()));
        }
        return response;
    }
}
