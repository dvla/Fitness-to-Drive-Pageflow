package uk.gov.dvla.f2d.web.pageflow.processor.mappers.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.dvla.f2d.web.pageflow.processor.components.config.ControllerComponentConfiguration;

import java.io.IOException;
import java.util.Map;

public class ControllerComponentMapper extends JsonDeserializer<ControllerComponentConfiguration>
{
    @Override
    public ControllerComponentConfiguration deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException {

        Root root = p.readValueAs(Root.class);

        ControllerComponentConfiguration component = new ControllerComponentConfiguration();
        component.setOptions(root.options);

        return component;
    }

    private static class Root {
        @JsonProperty("options")
        public Map<String, String> options;
    }
}
