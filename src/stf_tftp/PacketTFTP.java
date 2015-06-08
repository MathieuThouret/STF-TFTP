/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stf_tftp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author mathieu
 */
public abstract class PacketTFTP {
    
    //Tous les packets auront la forme de ce paquet.
    //Toutes les classes qui hériteront de celle-ci feront d'elles-memes leur packByte.
    
    protected byte[] datagram;// datagram est formé de opcode puis de packByte
    protected byte[] packByte;// packByte est ce qui reste après l'opcode
    protected int opcode;
    
    public PacketTFTP(int opcode) {
        this.opcode = opcode;
    }

    public PacketTFTP(byte[] pack, int opcode) {
        this.packByte = pack;
        this.opcode = opcode;
        createDatagram();
    }

    public byte[] getDatagram() {
        return datagram;
    }

    public byte[] getPackByte() {
        return this.packByte;
    }

    public int getOpcode() {
        return opcode;
    }
    
    protected void createDatagram() {
        makePackByte();
        try {
            byte[] opcodeByte = intToBytes(opcode);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(opcodeByte);
            outputStream.write(packByte);
            datagram = outputStream.toByteArray();
        }
        catch(IOException ex){
            System.out.println("Impossible de créer le datagram : "+ ex);
        }
    }
    
    //Fonction qui permet le passage d'un int en byte
    public byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.putInt(i);
        return bb.array();
    }
    
    //cette fonction retourne l'opcode d'un datagram d
    //le premier byte etant 0, on recupère le deuxième 
    //et on enlève la valeur de 0 en ascii.
    public int getOpCode(byte[] d){
        int oc = d[1]-48;
        return oc;
    }
    
    //crée le packByte des datagram.
    public abstract void makePackByte();
}
