package uk.gov.dvla.f2d.web.pageflow.processor.implementation;

import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.model.pageflow.Notification;
import uk.gov.dvla.f2d.web.pageflow.processor.IDataQuestionProcessor;

import java.util.ArrayList;
import java.util.List;

public class DataProcessorFormImpl implements IDataQuestionProcessor
{
    //private static final Logger logger = LoggerFactory.getLogger(DataProcessorFormImpl.class);

    private MedicalQuestion question;

    public DataProcessorFormImpl(MedicalQuestion question) {
        this.question = question;
    }

    @Override
    public List<Notification> validate() {
        question.setDecision(question.getOptions().trim());

        List<Notification> notifications = new ArrayList<>();
        return notifications;
    }

    /*
    @Override
    public void validate() {
        final String[] options = question.getOptions().split(",");
        final Integer answer = findAnswer();

        for (String option : options) {
            String key = option.split("=")[0].trim();
            String value = option.split("=")[1].trim();

            if (key.equalsIgnoreCase(answer.getName())) {
                question.setDecision(value);
            }
        }
    }

    private Integer findAnswer() {
        return question.getAnswers().stream().mapToInt(s -> Integer.parseInt(s)).sum();
    }
    */
}
