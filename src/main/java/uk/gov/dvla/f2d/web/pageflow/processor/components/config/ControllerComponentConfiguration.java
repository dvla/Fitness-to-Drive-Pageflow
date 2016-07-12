package uk.gov.dvla.f2d.web.pageflow.processor.components.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.dvla.f2d.web.pageflow.processor.mappers.config.ControllerComponentMapper;
import uk.gov.dvla.f2d.web.pageflow.processor.mappers.config.RadioComponentMapper;

import java.util.Map;

@JsonDeserialize(using = ControllerComponentMapper.class)
public final class ControllerComponentConfiguration
{
    private Map<String, String> options;

    public ControllerComponentConfiguration() {
        super();
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
