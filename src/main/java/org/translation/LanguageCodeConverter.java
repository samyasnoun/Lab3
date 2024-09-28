package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class provides the service of converting language codes to their names.
 */
public class LanguageCodeConverter {
    private Map<String, String> langCodeToNameMap;

    private Map<String, String> langNameToCodeMap;

    /**
     * Default constructor which will load the language codes from "language-codes.txt"
     * in the resources folder.
     */
    public LanguageCodeConverter() {
        this("language-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the language code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public LanguageCodeConverter(String filename) {
        langCodeToNameMap = new HashMap<>();
        langNameToCodeMap = new HashMap<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            Iterator<String> lineIterator = lines.iterator();
            if (lineIterator.hasNext()) {
                lineIterator.next();
            }
            while (lineIterator.hasNext()) {
                String currentline = lineIterator.next().trim();
                if (currentline.isEmpty()) {
                    continue;
                }
                String[] lineParts = currentline.split("\\s+");
                if (lineParts.length < 2) {
                    continue;
                }
                String languageName = String.join(" ",
                        java.util.Arrays.copyOf(lineParts, lineParts.length - 1));
                String languageCode = lineParts[lineParts.length - 1].trim();
                langCodeToNameMap.put(languageCode, languageName);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the name of the language for the given language code.
     * @param code the language code
     * @return the name of the language corresponding to the code
     */
    public String fromLanguageCode(String code) {
        return langCodeToNameMap.getOrDefault(code, code);
    }

    /**
     * Returns the code of the language for the given language name.
     * @param language the name of the language
     * @return the 2-letter code of the language
     */
    public String fromLanguage(String language) {
        return langNameToCodeMap.getOrDefault(language, language);
    }

    /**
     * Returns how many languages are included in this code converter.
     * @return how many languages are included in this code converter.
     */
    public int getNumLanguages() {
        return langCodeToNameMap.size();
    }
}
