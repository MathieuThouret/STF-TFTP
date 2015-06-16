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
    int nb_tentative = 3;
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

    /*
     On essaie d'envoyer le packet donné en paramètre et de recevoir l'accusé de réception,
     si il y a une erreur on reessaie jusqu'à ce que l'envoie se déroule avec succès
     ou que le nombre de tentative atteigne le maximum définit.
     */
    public void sendAndAck(PacketTFTP packet) throws Exception {
        byte[] paquetRecu;
        int i;
        for (i = 0; i < nb_tentative; i++) {
            try {
                sendPacket(packet);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

            try {
                paquetRecu = receivePacket();
                if (!PacketACK.estACK(paquetRecu)) {
                    if (!PacketERR.estERR(paquetRecu)) {
                        System.out.println("Packet non valide");
                    } else {
                        PacketERR paquetErreur = new PacketERR(PacketERR.getErrCode(paquetRecu));
                        System.out.println(paquetErreur.getErrMsg());
                    }
                } else {
                    break;
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        if(i >= nb_tentative) {
            System.err.println("Impossible d'envoyer le paquet après " + nb_tentative + " tentatives...");
        }
    }

    public void sendPacket(PacketTFTP packet) throws Exception {
        byte[] buffer = packet.getDatagram();

        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, IP, port);
        try {
            this.socket.send(dp);
        } catch (IOException ex) {
             throw new Exception("Echec de l'envoi");
        }
    }

    public byte[] receivePacket() throws Exception {
        byte[] buffer = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(dp);
        } catch (IOException ex) {
             throw new Exception("Aucun packet reçu");
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
