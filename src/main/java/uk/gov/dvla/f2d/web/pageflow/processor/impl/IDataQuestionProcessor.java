package uk.gov.dvla.f2d.web.pageflow.processor.impl;

import uk.gov.dvla.f2d.web.pageflow.model.Notification;

import java.util.List;

public interface IDataQuestionProcessor
{
    List<Notification> validate();
}
