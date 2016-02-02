package uk.gov.dvla.f2d.web.pageflow.summary;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.dvla.f2d.web.pageflow.config.PageFlowCacheManager;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static uk.gov.dvla.f2d.web.pageflow.constants.Constants.*;

class Main
{
    private static final String[] DIABETES_QUESTIONS = {
            "diabetes-with-insulin",
            "non-notifiable-condition",
            "confirm-address",
            "change-address",
            "hypoglycaemia-know-symptoms",
            "hypoglycaemia-symptoms-info",
            "hypoglycaemia-blood-sugar",
            "hypoglycaemia-get-symptoms",
            "hypoglycaemia-multiple-episodes",
            "insulin-declaration",
            "legal-eyesight-standard",
            "functioning-eyes",
            "laser-treatment-one",
            "laser-treatment-two",
            "healthcare-practitioner-one",
            "healthcare-practitioner-two",
            "medical-consent-one",
            "medical-consent-two",
            "medical-practitioner",
            "gp-details",
            "consultant-details",
            "automatic-gears",
            "special-controls",
            "car-bike-moped"
    };

    private static final String[] OPTIONS = {YES, NO};

    private static final String[] ANSWERS = {ENGLISH_LANGUAGE, WELSH_LANGUAGE};

    public void generate() {
        Map<String, Option> questions = new TreeMap<>();
        for(String question : DIABETES_QUESTIONS) {

            Map<String, Answer> options = new TreeMap<>();
            for (String optionKey : OPTIONS) {

                Map<String, String> answers = new TreeMap<>();
                for (String answerKey : ANSWERS) {
                    String language = (answerKey.equals("en") ? "ENGLISH" : "WELSH");
                    String confirm = (optionKey.equals("Y") ? "YES" : "NO");
                    answers.put(answerKey, confirm + ", your " + language + " answer to [" + question + "]");
                }
                Answer myAnswer = new Answer();
                myAnswer.setAnswers(answers);

                options.put(optionKey, myAnswer);
            }

            Option myOption = new Option();
            myOption.setOptions(options);

            questions.put(question, myOption);
        }

        Summary summary = new Summary();
        summary.setQuestions(questions);

        try {
            // Sort the question in alphabetical order.
            new ObjectMapper().writeValue(new File("/home/james/diabetes-notify.json"), summary);

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    public void run(String[] args) {
        MedicalForm form = PageFlowCacheManager.getMedicalForm("notify");
        MedicalCondition condition = form.getSupportedConditions().get("diabetes");
        form.setMedicalCondition(condition);

        MedicalQuestion diabetes = form.getMedicalCondition().getQuestions().get("diabetes-with-insulin");
        diabetes.setAnswers(Arrays.asList(new String[]{YES}));

        MedicalQuestion eyesight = form.getMedicalCondition().getQuestions().get("legal-eyesight-standard");
        eyesight.setAnswers(Arrays.asList(new String[]{NO}));

        SummaryAggregator aggregator = SummaryAggregator.getInstance();
        List<Line> responses = aggregator.process(form);

        for(Line response : responses) {
            System.out.println("Line: "+response);
        }
    }

    static void main(String[] args) {
        Main main = new Main();
        main.run(args);
        //main.generate();
    }
}
