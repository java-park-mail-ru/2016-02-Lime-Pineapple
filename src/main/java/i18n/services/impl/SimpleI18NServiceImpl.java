package i18n.services.impl;

import i18n.services.I18NService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created: 01-Apr-16
 * package: i18n.services.impl
 */
public class SimpleI18NServiceImpl implements I18NService {
    private static final Logger LOGGER = LogManager.getLogger(SimpleI18NServiceImpl.class);
    private static final String LOCALIZATION_RESOURCE_NAME = "Strings";
    private static final Locale[] LOCALES = new Locale[]{ new Locale("en", "US"), new Locale("ru", "RU") };

    private Map<Locale, ResourceBundle> localizations = new ConcurrentHashMap<>();

    @Override
    public void init() {
        for (Locale loc : LOCALES) {
            try {
                localizations.put(loc, ResourceBundle.getBundle(LOCALIZATION_RESOURCE_NAME, loc));
            } catch (RuntimeException e) {
                LOGGER.error("[!] Error happened during initializing locale(): \n"+e.toString());
            }
        }
    }

    @Override
    public Set<Locale> getActiveLocales() {
        return localizations.keySet();
    }

    @Override
    public String get(String key, Locale locale, boolean allowFallback) {
        return null;
    }

    @Override
    public String[] get(String[] keys, Locale locale, boolean allowFallback) {
        return new String[0];
    }

    @Override
    public String getF(String key, Locale locale) {
        return get(key, locale, true);
    }

    @Override
    public String[] getF(String[] keys, Locale locale) {
        return get(keys, locale, true);
    }
}
