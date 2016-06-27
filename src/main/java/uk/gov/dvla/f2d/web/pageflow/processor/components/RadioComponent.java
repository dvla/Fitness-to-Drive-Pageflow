package uk.gov.dvla.f2d.web.pageflow.processor.components;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import uk.gov.dvla.f2d.web.pageflow.processor.mappers.RadioComponentDataMapper;

import java.util.Map;

@JsonDeserialize(using = RadioComponentDataMapper.class)
public final class RadioComponent
{
    private Map<String, String> options;

    public RadioComponent() {
        super();
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }
}
