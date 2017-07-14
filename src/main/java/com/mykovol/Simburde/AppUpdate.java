package com.mykovol.Simburde;

/**
 * Created by MykoVol on 7/6/2017.
 */

import org.apache.log4j.Logger;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by MykoVol on 6/9/2017.
 */
public class AppUpdate {
    private static final Logger LOGGER = Logger.getLogger(AppUpdate.class);
    public final static int SOCKET_PORT = 5501;
    public final String SOCKET_HOST = "192.168.0.7";


    public AppUpdate() {
        try {
            Path p = Paths.get(System.getProperty("user.dir"));
//get folder with soft that needs to be updated
            FilesToUpdate filesToUpdate = new FilesToUpdate(p);

            File updateDirectory = new File(p.toString() + "/Update");

//                delete and recreate directory
            if (updateDirectory.exists()) {
                cleanDirectory(updateDirectory);
            } else {
                updateDirectory.mkdir();
            }

            InetAddress address = InetAddress.getByName(SOCKET_HOST);
            Socket socket = new Socket(address, SOCKET_PORT);

            DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            dos.writeUTF(filesToUpdate.getVersion());
            dos.flush();


            int filesCount = dis.readInt();
            if (filesCount == 0) {
                LOGGER.info("No updated found. Version - " + filesToUpdate.getVersion());
                SimburdeConfig.getInstance().setMsg("No new versions");
                return;
            }

            LOGGER.info("Start receiving files. Count - " + filesCount);
            File[] files = new File[filesCount];

            int n = 0;
            byte[] buf = new byte[4092];
            for (int i = 0; i < filesCount; i++) {
                String fileName = dis.readUTF();
                long fileSize = dis.readLong();
                files[i] = new File(updateDirectory.getPath() + "/" + fileName);
                FileOutputStream fos = new FileOutputStream(files[i]);
                while (fileSize > 0 && (n = dis.read(buf, 0, (int) Math.min(buf.length, fileSize))) != -1) {
                    fos.write(buf, 0, n);
                    fileSize -= n;
                }
                fos.close();

                LOGGER.info("File " + fileName + " received");
                SimburdeConfig.getInstance().updateButton.setVisible(true);
                SimburdeConfig.getInstance().setMsg("New version available. Press 'Update'");
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("AppUpdate should be in the same folder as a program", e);
            SimburdeConfig.getInstance().setMsg("Update is not available");
        } catch (IOException e) {
            LOGGER.error(": Cannot reach server " + SOCKET_HOST + ":" + SOCKET_PORT, e);
            SimburdeConfig.getInstance().setMsg("Update is not available");
        } catch (Exception ex) {
            LOGGER.error("error in socket", ex);
            SimburdeConfig.getInstance().setMsg("Update is not available");
        }

    }

    public static void removeDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    public static void cleanDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for (File aFile : files) {
                    removeDirectory(aFile);
                }
            }
        }
    }

}

