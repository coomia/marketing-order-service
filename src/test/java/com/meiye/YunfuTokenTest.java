package com.meiye;

import org.apache.shiro.crypto.hash.Sha1Hash;
import org.junit.Test;

/**
 * Created by Administrator on 2018/9/11 0011.
 */
public class YunfuTokenTest {

//    @Test
//    public void endcode(){
//        String generate= Password.create().generate("1001","123456");
//        String encode= Token.getEncoder().encode("1001",generate);
//        System.out.println(encode);
//    }
//
//    public void decode(){
//        String generate= Password.create().generate("1001","123456");
//        Token.getDecoder().decode("");
//    }

    @Test
    public void encode(){
        String firstPasswordNum = new Sha1Hash("123456", "admin", 100).toHex();
        System.out.println("Frist Password:"+firstPasswordNum+",length:"+firstPasswordNum.length());
        String secondPasswordNum = new Sha1Hash("123456111sdsdasdasfasdfasdfasdfasdf11", "adminssss", 100).toHex();
        System.out.println("Sencode Password:"+secondPasswordNum+",length:"+secondPasswordNum.length());
    }

}
