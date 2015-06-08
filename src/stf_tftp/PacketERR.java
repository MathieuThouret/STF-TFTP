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
public class PacketERR extends PacketTFTP {

    protected int errCode;
    protected String errMsg;
    
    public PacketERR (int errCode, String errMsg) {
        super (5);
        this.errCode=errCode;
        this.errMsg = errMsg;
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
            Logger.getLogger(PacketERR.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
}
