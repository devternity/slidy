package com.devternity.slidy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.print.PrinterException;
import java.io.IOException;

@SpringBootApplication
public class SlidyApplication {

    public static void main(String[] args) throws IOException, PrinterException, InterruptedException {
        SpringApplication.run(SlidyApplication.class, args);

    }
}

