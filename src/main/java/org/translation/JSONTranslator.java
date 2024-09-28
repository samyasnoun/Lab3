package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {
    private List<String> countryCodeList = new ArrayList<>();
    private List<String> langCodeList = new ArrayList<>();
    private List<String[]> countryTranslationList = new ArrayList<>();

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject countryData = jsonArray.getJSONObject(i);
                String countrycodes = countryData.getString("alpha3");
                countryCodeList.add(countrycodes);
                if (langCodeList.isEmpty()) {
                    for (String languageKey : countryData.keySet()) {
                        if (!"alpha2".equals(languageKey) && !"alpha3".equals(languageKey)
                                && !"id".equals(languageKey)) {
                            langCodeList.add(languageKey);
                        }
                    }
                }
                String[] countryTranslations = new String[langCodeList.size()];
                for (int j = 0; j < langCodeList.size(); j++) {
                    String languageCode = langCodeList.get(j);
                    countryTranslations[j] = countryData.optString(languageCode, null);
                }
                countryTranslationList.add(countryTranslations);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        int countryi = countryCodeList.indexOf(country);
        if (countryi == -1) {
            return new ArrayList<>();
        }
        return new ArrayList<>(langCodeList);
    }

    @Override
    public List<String> getCountries() {
        return new ArrayList<>(countryCodeList);
    }

    @Override
    public String translate(String country, String language) {
        String translation = null;
        int countryi = countryCodeList.indexOf(country);
        int languagej = langCodeList.indexOf(language);
        if (countryi != -1 && languagej != -1) {
            translation = countryTranslationList.get(countryi)[languagej];
        }
        return translation;
    }
}

