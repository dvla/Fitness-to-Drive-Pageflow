package uk.gov.dvla.f2d.web.pageflow.processor.summary;

import java.util.ArrayList;
import java.util.List;

public class SummaryLine
{
    private String type;
    private String subHeading;
    private List<String> lines;
    private String link;

    SummaryLine() {
        lines = new ArrayList<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString() {
        return "[type="+type+"; subHeading="+subHeading+"; lines="+lines+"; link="+link+"]";
    }
}
