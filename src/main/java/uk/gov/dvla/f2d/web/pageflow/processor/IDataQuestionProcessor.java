package uk.gov.dvla.f2d.web.pageflow.processor;

import uk.gov.dvla.f2d.model.pageflow.Notification;

import java.util.List;
import java.util.Map;

public interface IDataQuestionProcessor
{
    Map<String, String> getConfiguration();
    List<Notification> validate();
}
