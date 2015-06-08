/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packets;

import com.sun.security.ntlm.Client;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.*;

/**
 *
 * @author mathieu
 */
public class PacketACK extends PacketTFTP {

    private int bloc;

    public PacketACK(int bloc) {//crée un paquet ACK
        super(4);
        this.bloc = bloc;
        createDatagram();
    }
    
    @Override
    public void makePackByte() {
        packByte = intToBytes(this.bloc);
    }

    
    

}
