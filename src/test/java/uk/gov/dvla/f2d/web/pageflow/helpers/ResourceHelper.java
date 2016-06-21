package uk.gov.dvla.f2d.web.pageflow.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ResourceHelper
{
    private ResourceHelper() {
        super();
    }

    public static String load(final String path, final String file) throws IOException {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path + file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        reader.close();

        return builder.toString().trim();
    }
}
