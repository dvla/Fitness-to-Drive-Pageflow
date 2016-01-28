package uk.gov.dvla.f2d.web.pageflow.summary;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class Main
{
    public void run(String[] args) {
        try {
            final String resourceToLoad = "diabetes-notify.json";

            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream resourceStream = classLoader.getResource(resourceToLoad).openStream();

            Summary summary = new ObjectMapper().readValue(resourceStream, Summary.class);

            System.out.println("Summary:\n==============\n\n");

            String text = summary.getQuestions().get("diabetes-with-insulin").getOptions().get("Y").getAnswers().get("en");

            System.out.println("Text: " + text);

        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Main main = new Main();
        main.run(args);
    }
}
