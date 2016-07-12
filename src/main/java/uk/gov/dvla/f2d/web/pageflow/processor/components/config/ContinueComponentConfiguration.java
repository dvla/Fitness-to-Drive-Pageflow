package uk.gov.dvla.f2d.web.pageflow.processor.components.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.dvla.f2d.web.pageflow.processor.mappers.config.ContinueComponentMapper;

import java.util.Map;

@JsonDeserialize(using = ContinueComponentMapper.class)
public final class ContinueComponentConfiguration
{
    private Map<String, String> options;

    public ContinueComponentConfiguration() {
        super();
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
