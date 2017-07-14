package com.mykovol.Simburde;

import org.apache.log4j.Logger;

import java.net.*;
import java.io.*;

/**
 * Created by MykoVol on 6/9/2017.
 */
public class Update {
    private static final Logger LOGGER = Logger.getLogger(Update.class);
    protected final static int SOCKET_PORT = 5502;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(SOCKET_PORT)) {
            LOGGER.info("SocketServer Initialized on port " + SOCKET_PORT);

            boolean exitToUpdate = false;
            while (true) {
                try (Socket sock = serverSocket.accept()) {
                    DataInputStream dis = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
                    DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(sock.getOutputStream()));

                    //exit to update
                    exitToUpdate = dis.readBoolean();
                    dos.writeBoolean(true);
                    if (exitToUpdate) break;

                } catch (IOException e) {
                    LOGGER.error("An Inbound Connection Was Not Resolved", e);
                }
            }
            if (exitToUpdate) System.exit(0);
        } catch (IOException e) {
            LOGGER.error("Server was not started", e);
        }
    }
}

