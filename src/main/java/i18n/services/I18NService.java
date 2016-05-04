package i18n.services;

import java.util.Locale;
import java.util.Set;

/**
 * created: 01-Apr-16
 * package: i18n.services
 */

/**
 * This class provides an interface for multi-language support in various applications.
 */
public interface I18NService {
    /**
     * Initializes the class. E.g. fetches localizations from disk or database, performs sanity checks, etc.
     */
    void init();

    /**
     * Method returns all supported and available locales at the moment. If there are no locales loaded (e.g.,
     *  init() wasn't called), then this method returns an empty array.
     * @return Returns List of all supported and loaded locales
     */
    Set<Locale> getActiveLocales();

    /**
     * Gets localization for a given string key
     * @param key Same as in get() - key to find localization for
     * @param locale - Locale for which we perform the search
     * @param allowFallback a booelan value whether to allow fallback (default) localization strings.
     *                      If set to false, will raise KeyNotFound exception if string key not found for a given locale
     * @return Localized or fallback string for a given key
     */
    String get(String key, Locale locale, boolean allowFallback);
    /**
     * Gets localizations for a given string keys.
     * @param keys keys to find localizations for
     * @param locale locale for which we perform the search
     * @param allowFallback a booelan value whether to allow fallback (default) localization strings.
     *                      If set to false, will raise KeyNotFound exception if string key not found for a given locale
     * @return Localized or fallback string for a given key
     */
    String[] get(String[] keys, Locale locale, boolean allowFallback);

    /**
     * Wrapper for get(String, Locale, boolean allowFallback), where allowFallback == true
     * @param key Same as in get() - key to find localization for
     * @param locale - Locale for which we perform the search
     * @return Localized or fallback string for a given key
     */
    String getF(String key, Locale locale);
    /**
     * Wrapper for get(String, Locale, boolean allowFallback), where allowFallback == true
     * @param keys Same as in get() - keys to find localizations for
     * @param locale - Locale for which we perform the search
     * @return Localized or fallback string for a given key
     */
    String[] getF(String[] keys, Locale locale);
}
