package uk.gov.dvla.f2d.web.pageflow.config;

import uk.gov.dvla.f2d.web.pageflow.enums.Page;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalCondition;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalForm;
import uk.gov.dvla.f2d.web.pageflow.model.MedicalQuestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PageFlowCacheManager
{
    /**
     * Retrieve the entire medical questionnaire and all it's conditions.
     * @return MedicalQuestionnaire pre-populated with all it's details.
     */
    public static MedicalForm getMedicalForm() {
        return PageFlowDataCache.getMedicalForm();
    }

    /**
     * Retrieve all of the available medical conditions configured against the data cache.
     * @return Map<String, MedicalCondition> all supported medical conditions.
     */
    public static Map<String, MedicalCondition> getSupportedConditions() {
        return PageFlowDataCache.getMedicalForm().getSupportedConditions();
    }

    /**
     * Retrieve a particular medical condition using a specific condition ID key.
     * @param ID - The key of the medical condition to retrieve.
     * @return MedicalCondition - the condition based on this key.
     */
    public static MedicalCondition getConditionByID(final String ID) {
        return getSupportedConditions().get(ID);
    }

    /**
     * Determine a sorted list of questions for a particular condition, and by a question type.
     * @param condition - The medical condition in question we want to search for.
     * @param type - The type of questions we are interesting on retrieving.
     * @return List - questions found for this condition.
     */
    private static List<MedicalQuestion> getQuestionsByType(MedicalCondition condition, Page type) {
        List<MedicalQuestion> questions = new ArrayList<>();
        questions.addAll(condition.getQuestions().values().stream().filter(
                question -> question.getType().equalsIgnoreCase(type.toString())).collect(Collectors.toList())
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
