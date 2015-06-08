/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stf_tftp;

import com.sun.security.ntlm.Client;
import java.io.*;
import java.util.logging.*;

/**
 *
 * @author mathieu
 */
public class PacketRRQ {

    byte[] p;

    public PacketRRQ(File file) {//crée un paquet request

        p = makeRRQ(file);

    }
    
    public byte[] makeRRQ(File file) {
        byte[] OpCode;
        byte[] nomFichier;
        byte[] Mode;
        byte[] bufferRRQ = null;
        try {

            OpCode = "\0\1".getBytes("ascii");
            nomFichier = file.getPath().getBytes("ASCII");
            Mode = "NETASCII".getBytes("ascii");

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(OpCode);
            outputStream.write(nomFichier);
            outputStream.write((byte)0);
            outputStream.write(Mode);
            outputStream.write((byte)0);
            bufferRRQ = outputStream.toByteArray();

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Erreur lors de la création du packet en byte[] : "+ ex);
        }
        return bufferRRQ;
    }
}
