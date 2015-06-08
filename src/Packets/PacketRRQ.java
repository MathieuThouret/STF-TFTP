/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packets;

import com.sun.security.ntlm.Client;
import java.io.*;
import java.util.logging.*;

/**
 *
 * @author mathieu
 */
public class PacketRRQ extends PacketTFTP{

    private String file;
    private String mode;

    public PacketRRQ(String file, String mode) {//crée un paquet request
        super(1);
        this.file = file;
        this.mode = mode;
        createDatagram();
    }
    
    @Override
    public void makePackByte() {
        try {
            byte[] fileByte = file.getBytes("ascii");
            byte[] modeByte = mode.getBytes("ascii");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(fileByte);
            outputStream.write((byte)0);
            outputStream.write(modeByte);
            outputStream.write((byte)0);
            packByte = outputStream.toByteArray();
        }
        catch(IOException ex){
            System.out.println("Erreur lors de la création du paquet RRQ : " + ex);
        }
    }
}
