package uk.gov.dvla.f2d.web.pageflow.processor.mappers.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.FormComponentConfiguration;

import java.io.IOException;
import java.util.Map;

public class FormComponentMapper extends JsonDeserializer<FormComponentConfiguration>
{
    @Override
    public FormComponentConfiguration deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {

        Root root = p.readValueAs(Root.class);

        FormComponentConfiguration component = new FormComponentConfiguration();
        component.setOptions(root.options);

        return component;
    }

    private static class Root {
        @JsonProperty("options")
        public Map<String, String> options;
    }

}
