package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.nio.file.Path;


/**
 * Created by MykoVol on 7/3/2017.
 */
public class FilesToUpdate {
    private static final Logger LOGGER = Logger.getLogger(FilesToUpdate.class);
    private File[] files;
    private String version;
    private Path path;

    public File[] getFiles() {
        return files;
    }

    public String getVersion() {
        return version;
    }

    public Path getPath() {
        return path;
    }

    public FilesToUpdate(Path FilePath) throws FileNotFoundException {
        path = FilePath;
//        to do add working with folders also
        files = new File(path.toString()).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return !pathname.isDirectory();
            }
        });

        for (File file : files) {
//                if (file.isDirectory()) continue;
            try (java.util.jar.JarFile jar = new java.util.jar.JarFile(file)) {
                java.util.jar.Manifest manifest = jar.getManifest();

                java.util.jar.Attributes attributes = manifest.getMainAttributes();
                if (attributes != null) {
                    java.util.Iterator it = attributes.keySet().iterator();
                    while (it.hasNext()) {
                        java.util.jar.Attributes.Name key = (java.util.jar.Attributes.Name) it.next();
                        String keyword = key.toString();
                        if (keyword.equals("Implementation-Version") || keyword.equals("Bundle-Version")) {
                            version = (String) attributes.get(key);
                            LOGGER.trace("Fount! " + "file version - " + version);
                            break;
                        }
                    }
                }
                if (version != null) break;
            } catch (Exception ex) {
                LOGGER.trace("Skip! " + file.getName() + " is not a jar file");
            }
        }
        if (version == null) throw new FileNotFoundException("No program found in folder " + path);
    }
}

