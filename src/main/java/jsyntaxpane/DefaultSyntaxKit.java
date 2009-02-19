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
package jsyntaxpane;

import java.awt.Color;
import java.awt.Container;
import java.util.logging.Level;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import jsyntaxpane.actions.DefaultSyntaxAction;
import jsyntaxpane.actions.SyntaxAction;
import jsyntaxpane.components.SyntaxComponent;
import jsyntaxpane.util.Configuration;
import jsyntaxpane.util.JarServiceProvider;

/**
 * The DefaultSyntaxKit is the main entry to SyntaxPane.  To use the package, just 
 * set the EditorKit of the EditorPane to a new instance of this class.
 * 
 * You need to pass a proper lexer to the class.
 * 
 * @author ayman
 */
public class DefaultSyntaxKit extends DefaultEditorKit implements ViewFactory {

    public static final String CONFIG_CARETCOLOR = "CaretColor";
    public static final String CONFIG_SELECTION = "SelectionColor";
    public static final String CONFIG_COMPONENTS = "Components";
    public static final String CONFIG_MENU = "PopupMenu";
    public static final String CONFIG_MENU_ICONS = "PopupMenuIcons";
    public static final String PROPERTY_KEYMAP_JSYNTAXPANE = "jsyntaxpane";
    public static final Pattern EQUALS_REGEX = Pattern.compile("\\s*=\\s*");
    private static final Pattern ACTION_KEY_PATTERN = Pattern.compile("Action\\.(\\w+)");
    private static Font DEFAULT_FONT;
    private static Set<String> CONTENT_TYPES = new HashSet<String>();
    private static Boolean initialized = false;
    private Lexer lexer;
    private static final Logger LOG = Logger.getLogger(DefaultSyntaxKit.class.getName());
    private Map<JEditorPane, List<SyntaxComponent>> editorComponents =
            new WeakHashMap<JEditorPane, List<SyntaxComponent>>();
    private Map<JEditorPane, JPopupMenu> popupMenu =
            new WeakHashMap<JEditorPane, JPopupMenu>();
    /**
     * Main Configuration of JSyntaxPane EditorKits
     */
    private static Map<Class <? extends DefaultSyntaxKit>, Configuration> CONFIGS;


    static {
        // we only need to initialize once.
        if (!initialized) {
            initKit();
        }
    }

    /**
     * Create a new Kit for the given language 
     * @param lexer 
     */
    public DefaultSyntaxKit(Lexer lexer) {
        super();
        this.lexer = lexer;
    }

    /**
     * Adds UI components to the pane
     * @param editorPane
     */
    public void addComponents(JEditorPane editorPane) {
        // install the components to the editor:
        String[] components = getConfig().getPropertyList(CONFIG_COMPONENTS);
        for (String c : components) {
            installComponent(editorPane, c);
        }
    }

    public void installComponent(JEditorPane pane, String classname) {
        try {
            @SuppressWarnings(value = "unchecked")
            Class compClass = Class.forName(classname);
            SyntaxComponent comp = (SyntaxComponent) compClass.newInstance();
            comp.config(getConfig());
            comp.install(pane);
            if (editorComponents.get(pane) == null) {
                editorComponents.put(pane, new ArrayList<SyntaxComponent>());
            }
            editorComponents.get(pane).add(comp);
        } catch (InstantiationException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    public void deinstallComponent(JEditorPane pane, String classname) {
        for (SyntaxComponent c : editorComponents.get(pane)) {
            if (c.getClass().getName().equals(classname)) {
                c.deinstall(pane);
                editorComponents.remove(c);
                break;
            }
        }
    }

    public boolean isComponentInstalled(JEditorPane pane, String classname) {
        for (SyntaxComponent c : editorComponents.get(pane)) {
            if (c.getClass().getName().equals(classname)) {
                return true;
            }
        }
        return false;
    }

    public boolean toggleComponent(JEditorPane pane, String classname) {
        for (SyntaxComponent c : editorComponents.get(pane)) {
            if (c.getClass().getName().equals(classname)) {
                c.deinstall(pane);
                editorComponents.get(pane).remove(c);
                return false;
            }
        }
        installComponent(pane, classname);
        return true;
    }

    /**
     * Adds a popup menu to the editorPane if needed.
     * 
     * @param editorPane
     */
    public void addPopupMenu(JEditorPane editorPane) {
        String[] menuItems = getConfig().getPropertyList(CONFIG_MENU);
        if (menuItems == null || menuItems.length == 0) {
            return;
        }
        popupMenu.put(editorPane, new JPopupMenu());
        String menuIconsLocation = getConfig().getString(
                CONFIG_MENU_ICONS, "/META-INF/images/");
        JMenu stack = null;
        for (String menu : menuItems) {
            String[] menudata = EQUALS_REGEX.split(menu);
            //
            String menuText = menudata[0];

            // create the Popup menu
            if (menuText.equals("-")) {
                popupMenu.get(editorPane).addSeparator();
            } else if (menuText.startsWith(">")) {
                JMenu sub = new JMenu(menuText.substring(1));
                popupMenu.get(editorPane).add(sub);
                stack = sub;
            } else if (menuText.startsWith("<")) {
                Container parent = stack.getParent();
                if (parent instanceof JMenu) {
                    JMenu jMenu = (JMenu) parent;
                    stack = jMenu;
                } else {
                    stack = null;
                }
            } else {
                JMenuItem menuItem;
                menuItem = new JMenuItem();
                if (menudata.length < 2) {
                    throw new IllegalArgumentException("Invalid menu item data: " + menu);
                }
                String menuAction = menudata[1].trim();
                Action action = editorPane.getActionMap().get(menuAction);
                if (action == null) {
                    throw new IllegalArgumentException("Invalid action for menu item: " + menu);
                }
                menuItem.setAction(action);
                menuItem.setText(menuText);
                if (menudata.length > 2) {
                    URL loc = this.getClass().getResource(menuIconsLocation + menudata[2]);
                    if (loc == null) {
                        Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
                                "Unable to get icon at: " + menuIconsLocation + menudata[2]);
                    } else {
                        ImageIcon i = new ImageIcon(loc);
                        menuItem.setIcon(i);
                    }
                }
                if (stack == null) {
                    popupMenu.get(editorPane).add(menuItem);
                } else {
                    stack.add(menuItem);
                }
            }
        }
        editorPane.setComponentPopupMenu(popupMenu.get(editorPane));
    }

    @Override
    public ViewFactory getViewFactory() {
        return this;
    }

    @Override
    public View create(Element element) {
        return new SyntaxView(element, getConfig());
    }

    /**
     * Install the View on the given EditorPane.  This is called by Swing and
     * can be used to do anything you need on the JEditorPane control.  Here
     * I set some default Actions.
     * 
     * @param editorPane
     */
    @Override
    public void install(JEditorPane editorPane) {
        super.install(editorPane);
        // get our font
        String fontName = getProperty("DefaultFont");
        Font font = DEFAULT_FONT;
        if(fontName != null) {
            font = Font.decode(fontName);
        }
        editorPane.setFont(font);
        Configuration conf = getConfig();
        Color caretColor = conf.getColor(CONFIG_CARETCOLOR, Color.BLACK);
        editorPane.setCaretColor(caretColor);
        Color selectionColor = getConfig().getColor(CONFIG_SELECTION, new Color(0x99ccff));
        editorPane.setSelectionColor(selectionColor);
        addActions(editorPane);
        addComponents(editorPane);
        addPopupMenu(editorPane);
    }

    @Override
    public void deinstall(JEditorPane editorPane) {
        List<SyntaxComponent> l = editorComponents.get(editorPane);
        for (SyntaxComponent c : editorComponents.get(editorPane)) {
            c.deinstall(editorPane);
        }
        editorComponents.clear();

        // All the Actions were added directly to the editorPane, so we can remove
        // all of them with one call.  The Parents (defaults) will be intact
        editorPane.getActionMap().clear();
    }

    /**
     * Add keyboard actions to this control using the Configuration we have
     * @param editorPane
     */
    public void addActions(JEditorPane editorPane) {
        // look at all keys that either start with prefix.Action, or
        // that start with Action.

        Keymap km_parent = JTextComponent.getKeymap(JTextComponent.DEFAULT_KEYMAP);
        Keymap km_new = JTextComponent.addKeymap(PROPERTY_KEYMAP_JSYNTAXPANE, km_parent);


        for (Configuration.StringKeyMatcher m : getConfig().getKeys(ACTION_KEY_PATTERN)) {
            String[] values = Configuration.COMMA_SEPARATOR.split(
                    m.value);
            String actionClass = values[0];
            String actionName = m.group1;
            SyntaxAction action = createAction(actionClass);
            // The configuration keys will need to be prefixed by Action
            // to make it more readable in the COnfiguration files.
            action.config(getConfig(), DefaultSyntaxAction.ACTION_PREFIX + actionName);
            // Add the action to the component also
            editorPane.getActionMap().put(actionName, action);
            // Now bind all the keys to the Action we have:
            for (int i = 1; i < values.length; i++) {
                String keyStrokeString = values[i];
                KeyStroke ks = KeyStroke.getKeyStroke(keyStrokeString);
                // KeyEvent.VK_QUOTEDBL
                if (ks == null) {
                    throw new IllegalArgumentException("Invalid KeyStroke: " +
                            keyStrokeString);
                }
                km_new.addActionForKeyStroke(ks, action);
            }
        }
        editorPane.setKeymap(km_new);
    }

    private SyntaxAction createAction(String actionClassName) {
        SyntaxAction action = null;
        try {
            Class clazz = Class.forName(actionClassName);
            action = (SyntaxAction) clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException("Cannot create action class: " +
                    actionClassName + ". Ensure it has default constructor.", ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Cannot create action class: " +
                    actionClassName, ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Cannot create action class: " +
                    actionClassName, ex);
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Cannot create action class: " +
                    actionClassName, ex);
        }
        return action;
    }

    /**
     * This is called by Swing to create a Document for the JEditorPane document
     * This may be called before you actually get a reference to the control.
     * We use it here to create a proper lexer and pass it to the 
     * SyntaxDcument we return.
     * @return
     */
    @Override
    public Document createDefaultDocument() {
        return new SyntaxDocument(lexer);
    }

    /**
     * This is called to initialize the list of <code>Lexer</code>s we have.
     * You can call  this at initialization, or it will be called when needed.
     * The method will also add the appropriate EditorKit classes to the
     * corresponding ContentType of the JEditorPane.  After this is called,
     * you can simply call the editor.setCOntentType("text/java") on the 
     * control and you will be done.
     */
    public synchronized static void initKit() {
        // attempt to find a suitable default font
        String defaultFont = getConfig(DefaultSyntaxKit.class).getString("DefaultFont");
        if (defaultFont != null) {
            DEFAULT_FONT = Font.decode(defaultFont);
        } else {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] fonts = ge.getAvailableFontFamilyNames();
            Arrays.sort(fonts);
            if (Arrays.binarySearch(fonts, "Courier New") >= 0) {
                DEFAULT_FONT = new Font("Courier New", Font.PLAIN, 12);
            } else if (Arrays.binarySearch(fonts, "Courier") >= 0) {
                DEFAULT_FONT = new Font("Courier", Font.PLAIN, 12);
            } else if (Arrays.binarySearch(fonts, "Monospaced") >= 0) {
                DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 13);
            }
        }

        // read the Default Kits and their associated types
        Properties kitsForTypes = JarServiceProvider.readProperties("jsyntaxpane/kitsfortypes");
        for (Map.Entry e : kitsForTypes.entrySet()) {
            String type = e.getKey().toString();
            String classname = e.getValue().toString();
            registerContentType(type, classname);
        }
        initialized = true;
    }

    /**
     * Register the given content type to use the given class name as its kit
     * When this is called, an entry is added into the private HashMap of the
     * registered editors kits.  This is needed so that the SyntaxPane library
     * has it's own registration of all the EditorKits
     * @param type
     * @param classname
     */
    public static void registerContentType(String type, String classname) {
        try {
            // ensure the class is available and that it does supply a no args
            // constructor.  This saves debugging later if the classname is incorrect
            // or does not behave correctly:
            Class c = Class.forName(classname);
            // attempt to create the class, if we cannot with an empty argument
            // then the class is invalid
            Object kit = c.newInstance();
            if (!(kit instanceof EditorKit)) {
                throw new IllegalArgumentException("Cannot register class: " + classname +
                        ". It does not extend EditorKit");
            }
            JEditorPane.registerEditorKitForContentType(type, classname);
            CONTENT_TYPES.add(type);
        } catch (InstantiationException ex) {
            throw new IllegalArgumentException("Cannot register class: " + classname +
                    ". Ensure it has Default Constructor.", ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Cannot register class: " + classname, ex);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("Cannot register class: " + classname, ex);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Cannot register class: " + classname, ex);
        }
    }

    /**
     * Return all the content types supported by this library.  This will be the
     * content types in the file WEB-INF/services/resources/jsyntaxpane/kitsfortypes
     * @return sorted array of all registered content types
     */
    public static String[] getContentTypes() {
        String[] types = CONTENT_TYPES.toArray(new String[0]);
        Arrays.sort(types);
        return types;
    }

    /**
     * Merges the given properties with the configurations for this Object
     * 
     * @param config
     */
    public void setConfig(Properties config) {
        getConfig().putAll(config);
    }

    /**
     * Sets the given property to the given value.  If the kit is not
     * initialized,  then calls initKit
     * @param key
     * @param value
     */
    public void setProperty(String key, String value) {
        getConfig().put(key, value);
    }

    /**
     * Return the property with the given key.  If the kit is not
     * initialized,  then calls initKit
     * Be careful when changing property as the default property may be used 
     * @param key
     * @return value for given key
     */
    public String getProperty(String key) {
        return getConfig().getString(key);
    }

    /**
     * Get the configuration for this Object
     * @return
     */
    public Configuration getConfig() {
        return getConfig(this.getClass());
    }

    /**
     * Return the Configurations object for a Kit.  Perfrom lazy creation of a
     * Configuration object if nothing is created.
     * 
     * @param kit
     * @return
     */
    public static synchronized Configuration getConfig(Class<? extends DefaultSyntaxKit> kit) {
        if (CONFIGS == null) {
            CONFIGS = new WeakHashMap<Class<? extends DefaultSyntaxKit>, Configuration>();
            Configuration defaultConfig = new Configuration(DefaultSyntaxKit.class);
            loadConfig(defaultConfig, DefaultSyntaxKit.class);
            CONFIGS.put(DefaultSyntaxKit.class, defaultConfig);
        }

        if (CONFIGS.containsKey(kit)) {
            return CONFIGS.get(kit);
        } else {
            // recursive call until we read the Super duper DefaultSyntaxKit
            Class superKit = kit.getSuperclass();
            @SuppressWarnings("unchecked")
            Configuration defaults = getConfig(superKit);
            Configuration mine = new Configuration(kit, defaults);
            loadConfig(mine, kit);
            CONFIGS.put(kit, mine);
            return mine;
        }
    }

    private static void loadConfig(Configuration conf, Class<? extends EditorKit> kit) {
        String url = kit.getName().replace(".", "/") + "/config";
        Properties p = JarServiceProvider.readProperties(url);
        if (p.size() == 0) {
            LOG.info("unable to load configuration for: " + kit + " from: " + url + ".properties");
        } else {
            conf.putAll(p);
        }
    }
}
