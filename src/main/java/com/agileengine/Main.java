package com.agileengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

public class Main {

    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static String targetText = "Everything OK";

    /*private static final String resourcePathSource[] = {
            "./samples/sample-1-evil-gemini.html",
            "./samples/sample-2-container-and-clone.html",
            "./samples/sample-3-the-escape.html",
            "./samples/sample-4-the-mash.html"
    };*/

    public static void main(String[] args) {

        try {
            String resourcePathOriginal = args[0];
            String resoursePath = args[1];

            JsoupHtmlFinder finder = new JsoupHtmlFinder();

            Optional<String> btnOriginal = finder.findOriginalElement(new File(resourcePathOriginal), targetText);

            finder.printAttributes(resourcePathOriginal, btnOriginal);

            btnOriginal.ifPresent(btn -> finder.findInDiff(resoursePath, targetText));
        } catch (ArrayIndexOutOfBoundsException ex) {
            LOGGER.error("Error reading input data: IndexOutOfBounds");
        }
    }
}
