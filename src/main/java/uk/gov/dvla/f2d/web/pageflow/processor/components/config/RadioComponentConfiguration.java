package uk.gov.dvla.f2d.web.pageflow.processor.components.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.dvla.f2d.web.pageflow.processor.mappers.config.RadioComponentMapper;

import java.util.Map;

@JsonDeserialize(using = RadioComponentMapper.class)
public final class RadioComponentConfiguration
{
    private Map<String, String> options;

    public RadioComponentConfiguration() {
        super();
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
