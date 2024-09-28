package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**
 * This class provides the service of converting country codes to their names.
 */

public class CountryCodeConverter {

    /**
     * Default constructor which will load the country codes from "country-codes.txt"
     * in the resources folder.
     */

    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
    public static List<String> codeList;
    @SuppressWarnings({"checkstyle:VisibilityModifier", "checkstyle:SuppressWarnings"})
    public static List<String> nameList;
    private int totalCountries;

    public CountryCodeConverter() {
        this("country-codes.txt");
    }

    /**
     * Overloaded constructor which allows us to specify the filename to load the country code data from.
     * @param filename the name of the file in the resources folder to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public CountryCodeConverter(String filename) {

        try {
            List<String> lines = Files.readAllLines(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));
            List<String> codes = new ArrayList<>();
            List<String> names = new ArrayList<>();
            totalCountries = lines.size();
            for (int i = 0; i < (totalCountries - 1); i++) {
                String[] lineElements = lines.get(i).split("\t");
                names.add(lineElements[0]);
                codes.add(lineElements[2].toLowerCase());
            }
            codeList = codes;
            nameList = names;
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Returns the name of the country for the given country code.
     * @param code the 3-letter code of the country
     * @return the name of the country corresponding to the code
     */
    public String fromCountryCode(String code) {
        int index1 = codeList.indexOf(code);
        if (index1 == -1) {
            return "Country code not found";
        }
        return nameList.get(index1);
    }

    /**
     * Returns the code of the country for the given country name.
     * @param name the name of the country
     * @return the 3-letter code of the country
     */
    public static String fromCountry(String name) {
        int index2 = nameList.indexOf(name);
        if (index2 == -1) {
            return "Country code not found";
        }
        return codeList.get(index2);
    }

    /**
     * Returns how many countries are included in this code converter.
     * @return how many countries are included in this code converter.
     */
    public int getNumCountries() {
        return totalCountries - 1;
    }
}
