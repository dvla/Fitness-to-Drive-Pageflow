package uk.gov.dvla.f2d.web.pageflow.processor.components.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.dvla.f2d.web.pageflow.processor.mappers.config.CheckBoxComponentMapper;

import java.util.Map;

@JsonDeserialize(using = CheckBoxComponentMapper.class)
public final class CheckBoxComponentConfiguration
{
    private Map<String, String> options;

    public CheckBoxComponentConfiguration() {
        super();
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
