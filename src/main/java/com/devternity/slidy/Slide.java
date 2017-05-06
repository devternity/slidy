package com.devternity.slidy;

import java.io.File;

public class Slide {

    private final File location;

    public Slide(File location) {
        if (!location.canRead()) {
            throw new IllegalArgumentException("Sorry, slide is not readable (" + location.getAbsolutePath() + ")");
        }
        this.location = location;
    }


    public File location() {
        return location;
    }
}
