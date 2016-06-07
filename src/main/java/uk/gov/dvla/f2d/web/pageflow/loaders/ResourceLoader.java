package uk.gov.dvla.f2d.web.pageflow.loaders;

import java.io.IOException;
import java.io.InputStream;

public final class ResourceLoader
{
    private ResourceLoader() {
        super();
    }

    public static InputStream load(final String filename) throws IOException {
        System.out.println("Loading Resource: ["+filename+"]");

        ClassLoader classLoader = ResourceLoader.class.getClassLoader();
        return classLoader.getResource(filename).openStream();
    }
}
