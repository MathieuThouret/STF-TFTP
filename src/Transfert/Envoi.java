/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transfert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Packets.*;

/**
 *
 * @author Thibaud
 */

public class Envoi extends TransfertPaquet {

    public Envoi() {
        super();
        port = 69;
    }

    /*
     On essaie d'envoyer le packet donné en paramètre et de recevoir l'accusé de réception, 
     si il y a une erreur on reessaie jusqu'à ce que l'envoie se déroule avec succès
     ou que le nombre de tentative atteigne le maximum définit.
     */
    public void sendAndAck(PacketTFTP packet, int n) throws Exception {
        byte[] paquetRecu;
        int i;
        for (i = 0; i < 5; i++) {

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
                }
                if (i >= 5) {
                    System.out.println("Echec de l'envoi");
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /*
     Permet l'envoi du contenue du fichier, renvoi une erreur si le fichier est protégé ou invalide
     */
    public void sendData(String nomFichier) throws Exception {
        PacketDATA data;
        byte[] buffer = new byte[512];
        int i = 1;
        try {
            FileInputStream fe = new FileInputStream(nomFichier);

            while (fe.read(buffer) != -1) {
                data = new PacketDATA(i, buffer);
                sendAndAck(data, i);
                i++;
            }
            if (fe.available() >= 0) {
                buffer = new byte[fe.available()];
                fe.read(buffer);
                data = new PacketDATA(i, buffer);
                sendAndAck(data, i);
            }

            if (!closeReadFile(fe)) {
                System.out.println("Echec lors de l'ouverture du fichier");
            }
        } catch (Exception ex) {
            System.out.println("Echec lors de la lecture du fichier");
        }
    }

    /*
     On établit la connexion puis on appelle les fonctions d'envoi du fichier
     */
    public int sendFile(String nomFichier, String adresse) {
        try {
            IP = InetAddress.getByName(adresse);
        } catch (UnknownHostException ex) {
            System.out.println("Adresse Ip incorrecte");
            return 1;
        }
        File f = new File(nomFichier);
        try {
            PacketWRQ packet = new PacketWRQ(nomFichier, "netascii");
            sendAndAck(packet, 0);
        } catch (Exception ex) {
            System.out.println("Permission d'écriture refusée");
            return 2;
        }
        try {
            sendData(nomFichier);
        } catch (Exception ex) {
            System.out.println("Echec lors de l'envoi du fichier");
            return 3;
        }
        System.out.println("Envoi effectué avec succès");
        return 0;
    }

    public static void main(String args[]) {
        Envoi envoi = new Envoi();

        String fichierSelectionner = new String("./src/Fichiers/daftpunk.png");
        String adresse = "127.0.0.1";
        try {
            adresse = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {

        }

        File f = new File(fichierSelectionner);
        int a;
        if (f.exists()) {
            a = envoi.sendFile(fichierSelectionner, adresse);
        } else {
            System.out.println("Fichier absent");

        }
    }
}
