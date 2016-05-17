package uk.gov.dvla.f2d.web.pageflow.config;

import uk.gov.dvla.f2d.model.enums.Language;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.*;
import uk.gov.dvla.f2d.web.pageflow.enums.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PageFlowCacheManager
{
    private static final PageFlowDataCache cache;
    static {
        cache = new PageFlowDataCache();
    }

    /**
     * Retrieve the entire medical questionnaire and all it's conditions.
     * @return MedicalQuestionnaire pre-populated with all it's details.
     */
    public static MedicalForm getMedicalForm(Service service) {
        MedicalForm form = new MedicalForm();

        Authentication authentication = new Authentication();
        authentication.setVerified(false);

        MessageHeader header = new MessageHeader();
        header.setAuthentication(authentication);
        header.setService(service.toString());
        header.setLanguage(Language.ENGLISH.toString());
        header.setNotifications(new ArrayList<>());
        header.setBreadcrumb(new ArrayList<>());

        form.setMessageHeader(header);
        form.setPersonalDetails(null);
        form.setMedicalCondition(null);

        return form;
    }

    public static Map<String, MedicalCondition> getSupportedConditions(final String service) {
        try {
            if (!(Service.isSupported(service))) {
                throw new IllegalArgumentException("Service ["+service.toString()+"] is not currently supported!");
            }

            return cache.getSupportedConditions(service);

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Determine a sorted list of questions for a particular condition, and by a question type.
     * @param condition - The medical condition in question we want to search for.
     * @param page - The type of questions we are interesting on retrieving.
     * @return List - questions found for this condition.
     */
    private static List<MedicalQuestion> getQuestionsByType(MedicalCondition condition, Page page) {
        List<MedicalQuestion> questions = new ArrayList<>();
        questions.addAll(condition.getQuestions().values().stream().filter(
                question -> question.getPage().equalsIgnoreCase(page.toString())).collect(Collectors.toList())
        );
        return questions;
    }

    public static List<MedicalQuestion> getUnverifiedPages(MedicalCondition condition) {
        return getQuestionsByType(condition, Page.UNVERIFIED);
    }

    public static List<MedicalQuestion> getVerifiedPages(MedicalCondition condition) {
        return getQuestionsByType(condition, Page.VERIFIED);
    }

    public static MedicalQuestion getQuestionByConditionAndID(MedicalCondition condition, final String questionID) {
        return condition.getQuestions().get(questionID);
    }
}
