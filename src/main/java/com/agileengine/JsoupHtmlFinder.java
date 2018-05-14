package com.agileengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.agileengine.Consts.*;

public class JsoupHtmlFinder {

    private static Logger LOGGER = LoggerFactory.getLogger(JsoupHtmlFinder.class);

    public Optional<String> findOriginalElement(File htmlFile, String targetText) {
        return findElementByText(htmlFile, targetText);
    }

    public void printAttributes(String filePath, Optional<String> buttons) {
        buttons.ifPresent(attrs -> LOGGER.info("File: {}, Target element attrs: {}", filePath, attrs));
    }

    public void findInDiff(String resourcePath, String targetText) {
        Optional<String> element = findElementByText(new File(resourcePath), targetText);
        element.ifPresent(el -> printAttributes(resourcePath, element));
    }

    private String buildFullPath(Element element) {
        Elements parents = element.parents();

        String fullPath = "";

        LinkedList<Element> lst = new LinkedList(parents);

        Iterator<Element> iterator = lst.descendingIterator();
        while (iterator.hasNext()) {
            Element current = iterator.next();
            String tagName = current.tagName();
            fullPath = fullPath.concat(tagName).concat(" > ");
        }
        fullPath = fullPath.concat(element.tagName());
        return fullPath;
    }

    public Optional<String> findElementByText(File htmlFile, String targetText) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            Elements elementsContainingText = doc.getElementsContainingOwnText(targetText);
            List<Element> filtered = elementsContainingText.stream().collect(Collectors.toList());
            Optional<Element> foundTag = filtered.stream()
                    .filter((el) -> el.text().contains("everything") && el.text().contains("OK")).findFirst();

            String tagFullPath = ELEMENTS_NOT_FOUND;
            if (foundTag.isPresent())
                tagFullPath = buildFullPath(foundTag.get());

            return Optional.of(tagFullPath);

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }
}
