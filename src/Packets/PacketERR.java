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
public class PacketERR extends PacketTFTP {

    protected int errCode;
    protected String errMsg;
    
    public PacketERR (int errCode) {
        super (5);
        this.errCode=errCode;
        this.errMsg = createErrMsg();
        createDatagram();
    }
    
    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.errMsg = ErrMsg;
    }
    
    public static boolean estERR(byte[] packet){
        return getOpCode(packet)==5;
    }
    
    
    //retourne l'errorCode d'un paquet type erreur en byte
    //
    public static int getErrCode(byte[] paquet){
        int err = paquet[3];
        return err;
    }
    
    public String createErrMsg(){
        String msg = new String();
        switch (errCode){
            case 1: msg = "File not Found.";
                break;
            case 2:  msg = "Access violation.";
                break;
            case 3: msg = "Disk full or allocation exceeded.";
                break;
            case 4: msg = "Illegal TFTP operation.";
                break;
            case 5: msg = "Unknown transfer ID.";
                break;
            case 6: msg = "File already exists.";
                break;
            case 7: msg = "No such user.";
                break;
            default: msg = "Unknown error.";
                break;               
        }
        return msg;
    }

    @Override
    public void makePackByte() {
        try {
            byte[] codeByte = intToBytes(errCode);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(codeByte);
            outputStream.write(errMsg.getBytes("ascii"));
            outputStream.write(intToBytes(0));
            packByte = outputStream.toByteArray();
        } catch (IOException ex) {
            System.out.println("Erreur dans la création du paquet erreur : " + ex);
        }
    
    }
}
