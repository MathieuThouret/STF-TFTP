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
public class PacketWRQ {

    byte[] p;

    public PacketWRQ(File file) {//crée un paquet request
        p = makeWRQ(file);
    }

    public byte[] makeWRQ(File file) {
        byte[] OpCode;
        byte[] nomFichier;
        byte[] mode;
        byte[] bufferWRQ = null;
        try {

            OpCode = "\0\2".getBytes();
            nomFichier = file.getPath().getBytes("ascii");
            mode = "NETASCII".getBytes();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(OpCode);
            outputStream.write(nomFichier);
            outputStream.write((byte)0);
            outputStream.write(mode);
            outputStream.write((byte)0);
            bufferWRQ = outputStream.toByteArray();

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Erreur lors de la création du packet en byte[] : "+ ex);
        }
        return bufferWRQ;
    }
    
    public String toString(){
        return p.toString();
    }
}
