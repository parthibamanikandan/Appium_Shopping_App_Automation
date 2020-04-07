package ebay.automation.common;

import java.io.*;
import java.util.*;

class OnlyExt implements FilenameFilter {
    String ext;

    public OnlyExt(String ext) {
        this.ext = "." + ext;
    }

    public boolean accept(File dir, String name) {
        return name.endsWith(ext);
    }
}

public class ConfigReader {
    public static Properties config = new Properties();

    static {
        /*
         To load the testconfig properties.
         */
        try {
            String testConfig = "./src/test/resources/testconfig";
            loadConfig(testConfig);

            String uiElementsDir = System.getenv("UI_ELEMENTS");
            if (uiElementsDir == null) {
                uiElementsDir = config.getProperty("UI_ELEMENTS");
            }
            loadConfig(uiElementsDir);
        } catch (final Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }

    public ConfigReader() {
    }

    /**
     * Loads Properties file and takes the content in a HashSet Memory
     * @param dir
     * @throws Exception
     */
    public static void loadConfig(String dir) throws Exception {
        File dirFiles = new File(dir);
        FilenameFilter fileExtension = new OnlyExt("properties");
        String dirFile[] = dirFiles.list(fileExtension);
        List<Set<String>> propertiesinfile = new ArrayList<Set<String>>();
        for (int i = 0; i < dirFile.length; i++) {
            config.load(new FileInputStream(dir + "/" + dirFile[i]));
            Properties tempconfig = new Properties();
            tempconfig.load(new FileInputStream(dir + "/" + dirFile[i]));
            Set<String> configset = tempconfig.stringPropertyNames();
            propertiesinfile.add(i, configset);
            Set<String> oldset = new HashSet<String>(propertiesinfile.get(0));
            if (i != 0) {
                if (!propertiesinfile.get(0).removeAll(propertiesinfile.get(i))) {
                    Set<String> newSet = new HashSet<String>(propertiesinfile.get(0));
                    newSet.addAll(propertiesinfile.get(i));
                    propertiesinfile.set(0, newSet);
                } else {
                    oldset.retainAll(propertiesinfile.get(i));
                    throw new Exception("Duplicate property names found " + oldset.toString() + " in file " + dirFile[i]);
                }
            }

        }


    }

    /**
     * Loads FileName and returns value of one key as per the parameter passed.
     * @param key
     * @return
     */
    @SuppressWarnings("resource")
    public static String getPropertyFileName(String key) {
        try {
            String uiElementsDir = System.getenv("UI_ELEMENTS");
            if (uiElementsDir == null) {
                uiElementsDir = config.getProperty("UI_ELEMENTS");
            }

            FilenameFilter fileExtension = new OnlyExt("properties");
            File uiElementsFiles = new File(uiElementsDir);
            String uiElementsFile[] = uiElementsFiles.list(fileExtension);
            for (int i = 0; i < uiElementsFile.length; i++) {
                FileInputStream fstream = new FileInputStream(uiElementsDir + "/" + uiElementsFile[i]);

                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String strLine;

                // Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    if (strLine.contains(key)) {
                        return uiElementsFile[i];
                    }
                }

                // Close the input stream
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
