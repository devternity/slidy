package com.devternity.slidy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public interface Output {

    void write() throws IOException;

    URL writtenTo() throws MalformedURLException;

}
