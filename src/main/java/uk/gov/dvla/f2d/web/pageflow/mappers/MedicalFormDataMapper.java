package uk.gov.dvla.f2d.web.pageflow.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.dvla.f2d.model.pageflow.MedicalCondition;
import uk.gov.dvla.f2d.model.pageflow.MessageHeader;
import uk.gov.dvla.f2d.model.pageflow.PersonalDetails;

import java.io.IOException;
import java.util.List;

public class MedicalFormDataMapper extends JsonDeserializer<MedicalFormTemplate>
{
    @Override
    public MedicalFormTemplate deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

        Root root = p.readValueAs(Root.class);

        return null;
    }
    private static class Root {
        @JsonProperty("messageHeader")
        public MessageHeader messageHeader;

        @JsonProperty("personalDetails")
        public PersonalDetails personalDetails;

        @JsonProperty("medicalCondition")
        public MedicalCondition medicalCondition;
    }
}
