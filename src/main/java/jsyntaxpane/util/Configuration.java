/*
 * Copyright 2008 Ayman Al-Sairafi ayman.alsairafi@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License
 *       at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jsyntaxpane.util;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Wrapper around the Properties class with more functionality
 * This is mainly needed to provide easier support for getting values by an
 * optional prefix.
 * 
 * @author Ayman Al-Sairafi
 */
public class Configuration extends Properties {

    public Configuration(Properties defaults) {
        super(defaults);
    }

    /**
     * Gets the String value for the key prefix.key, or key, or default
     * @param prefix
     * @param key
     * @param Default
     * @return
     */
    public String getPrefixProperty(String prefix, String key, String Default) {
        String v = super.getProperty(prefix + "." + key);
        if (v != null) {
            return v;
        }
        return super.getProperty(key, Default);
    }

    /**
     * Gets a prefixed integer from the properties.  If number cannot be found
     * or if it cannot be decoded, the default is returned
     * The integer is decoded using {@link Integer.decode(String)}
     * @param prefix
     * @param key
     * @param Default
     * @return
     */
    public int getPrefixInteger(String prefix, String key, int Default) {
        String v = getPrefixProperty(prefix, key, null);
        if (v == null) {
            return Default;
        }
        try {
            int i = Integer.decode(v);
            return i;
        } catch (NumberFormatException e) {
            LOG.log(Level.WARNING, null, e);
            return Default;
        }
    }

    /**
     * Returns a String[] of the comma separated items in the value for
     * prefix.key or key
     * Does NOT return null.  If the prefix.key or key value is not found,
     * then an empty string array is returned.  So the return of this method
     * can be used directly in a foreach loop
     * @param prefix
     * @param key
     * @return non-null String[] 
     */
    public String[] getPrefixPropertyList(String prefix, String key) {
        String v = getProperty(prefix + "." + key);
        if (v == null) {
            v = getProperty(key);
        }
        if (v == null) {
            return EMPTY_LIST;
        }
        return COMMA_SEPARATOR.split(v);
    }

    /**
     * Returns a boolean from the config
     * @param prefix
     * @param key
     * @param Default
     * @return
     */
    public boolean getPrefixBoolean(String prefix, String key, boolean Default) {
        String b = getPrefixProperty(prefix, key, null);
        if (b == null) {
            return Default;
        }
        return Boolean.parseBoolean(b.trim());
    }
    
    
    private static String[] EMPTY_LIST = new String[0];
    private static Pattern COMMA_SEPARATOR = Pattern.compile("\\s*,\\s*");
    private static final Logger LOG = Logger.getLogger(Configuration.class.getName());

}
