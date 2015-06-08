/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packets;

import static Packets.PacketERR.getErrCode;
import static Packets.PacketTFTP.getOpCode;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mathieu
 */
public class STF_TFTP {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        PacketERR p = new PacketERR(3);
        System.out.println(p.datagram.length);
        int i = getOpCode(p.datagram);
        System.out.println(i);
        System.out.println(getErrCode(p.datagram));
    }
    
}
