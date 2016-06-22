package uk.gov.dvla.f2d.web.pageflow.cache;

import uk.gov.dvla.f2d.model.enums.Language;
import uk.gov.dvla.f2d.model.enums.Service;
import uk.gov.dvla.f2d.model.pageflow.Authentication;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;
import uk.gov.dvla.f2d.model.pageflow.MessageHeader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public final class PageFlowCacheManager
{
    private static PageFlowCacheManager instance;

    private PageFlowDataCache cache;

    private PageFlowCacheManager() {
        try {
            cache = new PageFlowDataCache();
            cache.initialise();

        } catch(IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized PageFlowCacheManager getInstance() {
        if(instance == null) {
            instance = new PageFlowCacheManager();
        }
        return instance;
    }

    public MedicalForm createMedicalForm(Service service) {
        MedicalForm form = new MedicalForm();

        Authentication authentication = new Authentication();
        authentication.setVerified(false);

        MessageHeader header = new MessageHeader();
        header.setAuthentication(authentication);
        header.setService(service.getName());
        header.setLanguage(Language.ENGLISH.getName());
        header.setNotifications(new ArrayList<>());
        header.setBreadcrumb(new ArrayList<>());

        form.setMessageHeader(header);
        form.setPersonalDetails(null);
        form.setMedicalCondition(null);
        form.setAdditionalConditions(new ArrayList<>());

        return form;
    }

    public Map<String, MedicalCondition> getConditions(Service service) {
        return cache.getSupportedConditions(service);
    }

    public void destroy() {
        instance = null;
    }
}
