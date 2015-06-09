package Transfert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import Packets.*;

/**
 *
 * @author Thibaud
 */

public abstract class TransfertPaquet {

    InetAddress IP;
    int port;
    DatagramSocket socket;

    public TransfertPaquet() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);
        } catch (SocketException ex) {
            System.out.println("Impossible de créer le socket");
        }
    }

    public void sendPacket(PacketTFTP packet) throws Exception {
        byte[] buffer = packet.getDatagram();

        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, IP, port);
        try {
            this.socket.send(dp);
        } catch (IOException ex) {
            System.out.println("Echec de l'envoi");
        }
    }

    public byte[] receivePacket() throws Exception {
        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(dp);
        } catch (IOException ex) {
            System.out.println("Aucun packet reçu");
        }
        if (dp.getPort() != port) {
            port = dp.getPort();
        }
        return dp.getData();
    }


    /**
     * Ferme un fichier en lecture
     *
     * @param f
     * @return
     */
    public boolean closeReadFile(FileInputStream f) {
        try {
            f.close();
            return true;
        } catch (IOException ex) {
            System.out.println("Impossible de fermer le fichier en lecture");
            return false;
        }
    }

    /**
     * Ferme un fichier en écriture
     *
     * @param f
     * @return
     */
    public boolean closeWriteFile(FileOutputStream f) {
        try {
            f.close();
            return true;
        } catch (IOException ex) {
            System.out.println("Impossible de fermer le fichier en écriture");
            return false;
        }
    }
}
