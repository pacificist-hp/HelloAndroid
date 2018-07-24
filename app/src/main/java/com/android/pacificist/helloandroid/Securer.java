package com.android.pacificist.helloandroid;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by pacificist on 2018/7/24.
 */
public class Securer extends ISecurer.Stub {
    private static final String TAG = "BinderPool/ISecurer";
    private static final char KEY = '^';

    @Override
    public String encrypt(String plaintext) throws RemoteException {
        Log.d(TAG, "encrypt in ISecurer.Stub: " + plaintext);
        char[] chars = plaintext.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= KEY;
        }
        return new String(chars);
    }

    @Override
    public String decrypt(String ciphertext) throws RemoteException {
        Log.d(TAG, "decrypt in ISecurer.Stub: " + ciphertext);
        char[] chars = ciphertext.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= KEY;
        }
        return new String(chars);
    }
}
