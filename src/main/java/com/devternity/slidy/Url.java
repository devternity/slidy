package com.devternity.slidy;

import com.devternity.slidy.infra.InvalidValueProvided;
import com.google.common.io.ByteSource;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.google.common.io.Files.asByteSink;
import static com.google.common.io.Resources.asByteSource;

class Url {

    private final String url;

    public Url(String url, String hint) {

        String[] schemes = {"http", "https"};
        UrlValidator validator = new UrlValidator(schemes);
        if (!validator.isValid(url)) {
            throw new InvalidValueProvided("Deck url (" + url + ")");
        }
        this.url = url;
    }

    public File downloaded() throws IOException {
        ByteSource bytes = asByteSource(new URL(url));
        File localFile = File.createTempFile("deck", ".deck");
        bytes.copyTo(asByteSink(localFile));
        return localFile;
    }

    @Override
    public String toString() {
        return url;
    }
}