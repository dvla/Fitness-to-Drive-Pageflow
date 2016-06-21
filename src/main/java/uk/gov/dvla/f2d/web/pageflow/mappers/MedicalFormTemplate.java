package uk.gov.dvla.f2d.web.pageflow.mappers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.dvla.f2d.model.pageflow.MedicalForm;

import java.io.Serializable;

@JsonDeserialize(using = MedicalFormDataMapper.class)
public final class MedicalFormTemplate extends MedicalForm implements Serializable, Cloneable
{
    public MedicalFormTemplate() {
        super();
    }
}
