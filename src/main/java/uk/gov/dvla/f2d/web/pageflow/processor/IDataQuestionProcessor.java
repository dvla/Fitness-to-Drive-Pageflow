package uk.gov.dvla.f2d.web.pageflow.processor;

import uk.gov.dvla.f2d.model.pageflow.Notification;

import java.util.List;

public interface IDataQuestionProcessor
{
    List<Notification> validate();
}