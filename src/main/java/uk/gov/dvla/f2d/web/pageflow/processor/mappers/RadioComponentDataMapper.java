package uk.gov.dvla.f2d.web.pageflow.processor.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.dvla.f2d.web.pageflow.processor.components.RadioComponent;

import java.io.IOException;
import java.util.Map;

public class RadioComponentDataMapper extends JsonDeserializer<RadioComponent>
{
    @Override
    public RadioComponent deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

        Root root = p.readValueAs(Root.class);

        RadioComponent component = new RadioComponent();
        component.setOptions(root.options);

        return component;
    }

    private static class Root {
        @JsonProperty("options")
        public Map<String, String> options;
    }

}
