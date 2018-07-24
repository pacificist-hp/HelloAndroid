// ISecurer.aidl
package com.android.pacificist.helloandroid;

// Declare any non-default types here with import statements

interface ISecurer {
    String encrypt(String plaintext);
    String decrypt(String  ciphertext);
}
