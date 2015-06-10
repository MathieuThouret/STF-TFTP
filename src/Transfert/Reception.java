/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Transfert;

import Packets.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Thibaud
 */

public class Reception extends TransfertPaquet {

    private int nb_tentative = 3;

    private String fileName;
    private final String path;

    public Reception(String _path) {
        super();
        this.port = 69;
        this.path = _path;
    }

    public byte[] receiveDataPacket() throws Exception {
        byte[] buffer = new byte[516];
        DatagramPacket dtg = new DatagramPacket(buffer, buffer.length);
        try {
            socket.receive(dtg);
        } catch (IOException ex) {
            throw new Exception("Aucun Packet re�u");
        }
        if (dtg.getPort() != port) {
            port = dtg.getPort();
        }
        return dtg.getData();
    }

    public void receiveDataPacket(PacketDATA packet) throws Exception {
        byte[] paquetrecu;
        try {
            paquetrecu = receiveDataPacket();
            if (!PacketACK.estACK(paquetrecu)) {
                if (!PacketERR.estERR(paquetrecu)) {
                    System.err.println("Packet non valide");
                } else {
                    PacketERR packetERR = new PacketERR(PacketERR.getErrCode(paquetrecu));
                    System.err.println(packetERR.getErrMsg());
                }
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        sendPacket(new PacketACK(packet.getBloc()));
    }

    public void receiveData(PacketDATA data) throws Exception {
        boolean receptionOK = true;
        int numData = data.getBloc(), i = 0;
        FileOutputStream f = new FileOutputStream(path + fileName);
        if (f != null) {
            try {
                f.write(data.getData());
            } catch (IOException ex) {
                System.err.println("Impossible d'�crire dans le fichier : " + fileName);
                throw ex;
            }
            do {
                for (i = 0; i < nb_tentative; i++) {
                    try {
                        receiveDataPacket(data);
                        receptionOK = data.getBloc() != numData;
                        numData = data.getBloc();
                        f.write(data.getData());
                        break;
                    } catch (IOException ex) {
                        System.err.println("Impossible d'�crire dans le fichier : " + fileName);
                        throw ex;
                    } catch (Exception er) {
                        if (i >= nb_tentative) {
                            System.err.println("Aucune r�ponse du serveur : Time Out");
                            throw er;
                        } else {
                            sendPacket(new PacketERR(data.getBloc() + 1));
                            //sendError(data.getBloc() + 1, er.getMessage());
                            System.out.println(er.getMessage());
                        }
                    }
                }
            } while (data.getData().length >= 512 && receptionOK);
        } else {
            System.err.println("Impossible de creer le fichier : " + fileName);
        }
        if (!closeWriteFile(f)) {
            System.err.println("Impossible de fermer le fichier : " + fileName);
        }
    }

    public int receiveFile(String nomFichier, String adresse) {
        PacketDATA data = new PacketDATA();

        this.fileName = nomFichier;
        try {
            this.IP = InetAddress.getByName(adresse);
        } catch (UnknownHostException ex) {
            System.out.println("Adresse Ip incorrecte");
            return 1;
        }

        try {
            PacketRRQ packet = new PacketRRQ(nomFichier, "netascii");
            sendAndDATA(packet);

        } catch (Exception er) {
            System.out.println("Demande RRQ refus�e : " + er.getMessage());
            return 2;
        }

        try {
            receiveData(data);
        } catch (Exception ex) {
            System.out.println("La reception a �chou� : " + ex.getMessage());
            return -1;
        }
        System.out.println("La reception a r�ussi");
        return 0;
    }

    public static void main(String args[]) {
        String username = System.getProperty("user.name");
        String dossier = "C:\\Users\\" + username + "\\Desktop";

        String fichierSelectionner = new String("url.png");

        String adresse = "127.0.0.1";
        System.out.println(dossier);

        // On essaye de r�cuperer l'addresse IP locale
        try {
            adresse = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {}

        File f = new File(fichierSelectionner);
        int codeRetour;
        Reception reception = new Reception(dossier + "\\");
        codeRetour = reception.receiveFile(fichierSelectionner, adresse);
    }
}
