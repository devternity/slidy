package com.devternity.slidy;

import java.io.IOException;

public interface Output {

    void write() throws IOException;

    Url writtenTo();

}
