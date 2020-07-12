package com.example.mypassword2;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class FingerprintAuth {

    private KeyGenerator keyGenerator;
    private KeyStore keyStore;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private final String KEY_NAME = "myKey";
    private Context mContext;

    public void start(FingerprintManager fingerprintManager, Context mContext){
        try {
            generateKey();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(initCipher()){
            cryptoObject = new FingerprintManager.CryptoObject(cipher);
            FingerprintHandler helper = new FingerprintHandler(mContext);
            helper.startAuth(fingerprintManager, cryptoObject);
        }
    }

    private void generateKey(){
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);

            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();

        }
        catch(Exception e){

        }
    }

    private boolean initCipher(){
        try{
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES +
                    "/" + KeyProperties.BLOCK_MODE_CBC + "/"
                + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }

        try{
            keyStore.load(null);
            SecretKey secretKey = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
