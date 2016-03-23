package uk.gov.dvla.f2d.web.pageflow.config;

import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MedicalQuestion;
import uk.gov.dvla.f2d.web.pageflow.enums.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageFlowCacheManager
{
    /**
     * Retrieve the entire medical questionnaire and all it's conditions.
     * @return MedicalQuestionnaire pre-populated with all it's details.
     */
    public static MedicalForm getMedicalForm(final String service) {
        return PageFlowDataCache.getMedicalForm(service);
    }

    /**
     * Determine a sorted list of questions for a particular condition, and by a question type.
     * @param condition - The medical condition in question we want to search for.
     * @param type - The type of questions we are interesting on retrieving.
     * @return List - questions found for this condition.
     */
    private static List<MedicalQuestion> getQuestionsByType(MedicalCondition condition, Page page) {
        List<MedicalQuestion> questions = new ArrayList<>();
        questions.addAll(condition.getQuestions().values().stream().filter(
                question -> question.getPage().equalsIgnoreCase(page.toString())).collect(Collectors.toList())
        );
        return questions;
    }

    public static List<MedicalQuestion> getEligibilityPages(MedicalCondition condition) {
        return getQuestionsByType(condition, Page.ELIGIBILITY);
    }

    public static List<MedicalQuestion> getQuestionPages(MedicalCondition condition) {
        return getQuestionsByType(condition, Page.QUESTION);
    }

    public static MedicalQuestion getQuestionByConditionAndID(MedicalCondition condition, final String questionID) {
        return condition.getQuestions().get(questionID);
    }
}
