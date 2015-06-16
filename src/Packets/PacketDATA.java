/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Packets;


import java.io.*;

/**
 *
 * @author mathieu
 */
public class PacketDATA extends PacketTFTP {

    private int bloc;
    private byte[] data;


    public PacketDATA() {
        super(3);
        this.data = new byte[512];
    }
    public PacketDATA(int bloc, byte[] data) {//crée un paquet DATA
        super(3);
        this.bloc = bloc;
        this.data = data;
        createDatagram();
    }
    
    public int getBloc() {
        return bloc;
    }

    public void setBloc(int bloc) {
        this.bloc = bloc;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
    
    public static boolean estDATA(byte[] packet){
        return getOpCode(packet)==3;
    }

    @Override
    public void makePackByte() {
        try {
            byte[] blocByte = intToBytes(bloc);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(blocByte);
            outputStream.write(data);
            packByte = outputStream.toByteArray();
        }
        catch(IOException ex){
            System.out.println("Impossible de créer le packet DATA : "+ ex);
        }
    }

    public boolean getDatagramPacket(byte[] _data) {
        if (this.estDATA(_data)) {
            datagram = _data;
            opcode = 3;
            bloc=byteToInt(_data);
            data = new byte[datagram.length - 4];
            System.arraycopy(datagram, 4, data, 0, datagram.length - 4);
            return true;
        } else {
            return false;
        }
    }
}
