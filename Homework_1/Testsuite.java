import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.security.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Testsuite {

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        Testsuite.test1();
        Testsuite.test2();
        Testsuite.test3();
        Testsuite.test4();
        Testsuite.test5();
    }

    public static void test1() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //to test a simple and valid coinbase transaction and normal transaction that will work or not
        KeyPair ScroogeKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair AliceKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Transaction tx0=new Transaction();
        byte[] starthash=null;
        tx0.addInput(starthash,0);
        tx0.addOutput(20,ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo=new UTXO(tx0.getHash(),0);
        UTXOPool utxoPool=new UTXOPool();
        utxoPool.addUTXO(utxo,tx0.getOutput(0));
        Transaction tx1=new Transaction();
        tx1.addInput(tx0.getHash(),0);
        tx1.addOutput(20, AliceKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo1=new UTXO(tx1.getHash(),0);
        utxoPool.addUTXO(utxo1,tx1.getOutput(0));
        TxHandler txhandlertest1=new TxHandler(utxoPool);
        System.out.println("the transaction1 for txhandlertest1.IsValid(tx1) returns： "+txhandlertest1.isValidTx(tx1));
        System.out.println("for txhandlertest1.handleTxs(new Transaction[]{tx1}) return numbers of transactions: "+txhandlertest1.handleTxs(new Transaction[]{tx1}).length);
    }

    public static void test2() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test the situation that you spend the same coin in two times(double spend attack)
        KeyPair ScroogeKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair BobKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair AliceKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Transaction tx0=new Transaction();
        byte[] starthash=null;
        tx0.addInput(starthash,0);
        tx0.addOutput(20,ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo=new UTXO(tx0.getHash(),0);
        UTXOPool utxoPool=new UTXOPool();
        utxoPool.addUTXO(utxo,tx0.getOutput(0));
        Transaction tx1=new Transaction();
        tx1.addInput(tx0.getHash(),0);
        tx1.addOutput(20, BobKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo1=new UTXO(tx1.getHash(),0);
        utxoPool.addUTXO(utxo1,tx1.getOutput(0));
        utxoPool.removeUTXO(utxo);
        Transaction tx2=new Transaction();
        tx2.addInput(tx1.getHash(),0);
        tx2.addOutput(20, AliceKey.getPublic());
        tx2.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo2=new UTXO(tx2.getHash(),0);
        utxoPool.addUTXO(utxo2,tx2.getOutput(0));
        utxoPool.removeUTXO(utxo1);
        TxHandler txhandlertest2=new TxHandler(utxoPool);
        System.out.println("the transaction2 for txhandlertest1.IsValid(tx2) returns: "+txhandlertest2.isValidTx(tx2));
        System.out.println("for txhandlertest2.handleTxs(new Transaction[]{tx2}) return numbers of transactions: "+txhandlertest2.handleTxs(new Transaction[]{tx2}).length);
    }

    public static void test3() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test if the output is negative and all outputs are greater than all inputs
        KeyPair ScroogeKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair BobKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair AliceKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Transaction tx0=new Transaction();
        byte[] starthash=null;
        tx0.addInput(starthash,0);
        tx0.addOutput(20,ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo=new UTXO(tx0.getHash(),0);
        UTXOPool utxoPool=new UTXOPool();
        utxoPool.addUTXO(utxo,tx0.getOutput(0));
        Transaction tx1=new Transaction();
        tx1.addInput(tx0.getHash(),0);
        tx1.addOutput(-2, BobKey.getPublic());//negative output
        tx1.addInput(tx0.getHash(),1);
        tx1.addOutput(5, AliceKey.getPublic());
        tx1.addInput(tx0.getHash(),0);
        tx1.addOutput(20, ScroogeKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo1=new UTXO(tx1.getHash(),0);
        utxoPool.addUTXO(utxo1,tx1.getOutput(0));
        UTXO utxo2=new UTXO(tx1.getHash(),1);
        utxoPool.addUTXO(utxo2,tx1.getOutput(0));
        UTXO utxo3=new UTXO(tx1.getHash(),2);
        utxoPool.addUTXO(utxo3,tx1.getOutput(0));
        utxoPool.removeUTXO(utxo);
        TxHandler txhandlertest3=new TxHandler(utxoPool);
        System.out.println("the transaction1 for txhandlertest3.IsValid(tx1) returns: "+txhandlertest3.isValidTx(tx1));
        System.out.println("for txhandlertest3.handleTxs(new Transaction[]{tx1}) return numbers of transactions: "+txhandlertest3.handleTxs(new Transaction[]{tx1}).length);
    }

    public static void test4() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test if output is not claimed in current UTXO pool or invalid signature
        KeyPair ScroogeKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair BobKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair AliceKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Transaction tx0=new Transaction();
        byte[] starthash=null;
        tx0.addInput(starthash,0);
        tx0.addOutput(20,ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) AliceKey.getPrivate(),0);
        UTXO utxo=new UTXO(tx0.getHash(),0);
        UTXOPool utxoPool=new UTXOPool();
        utxoPool.addUTXO(utxo,tx0.getOutput(0));
        TxHandler txhandlertest4a=new TxHandler(utxoPool);
        System.out.println("the transaction0 for txhandlertest4a.IsValid(tx0) returns: "+txhandlertest4a.isValidTx(tx0));
        System.out.println("for txhandlertest4a.handleTxs(new Transaction[]{tx0}) return numbers of transactions: "+txhandlertest4a.handleTxs(new Transaction[]{tx0}).length);
        Transaction tx=new Transaction();
        byte[] starthash1=null;
        tx.addInput(starthash1,0);
        tx.addOutput(20,ScroogeKey.getPublic());
        tx.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo1=new UTXO(tx.getHash(),0);
        UTXOPool utxoPool1=new UTXOPool();
        utxoPool1.addUTXO(utxo1,tx0.getOutput(0));
        Transaction tx1=new Transaction();
        tx1.addInput(tx.getHash(),0);
        tx1.addOutput(20, BobKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo2=new UTXO(tx1.getHash(),0);
        utxoPool1.addUTXO(utxo2,tx1.getOutput(0));
        utxoPool1.removeUTXO(utxo1);
        Transaction tx2=new Transaction();
        tx2.addInput(tx1.getHash(),0);
        tx2.addOutput(5, BobKey.getPublic());//not claimed in UTXO pool
        tx2.TXsignature((RSAPrivateKey) AliceKey.getPrivate(),0);
        UTXO utxo3=new UTXO(tx2.getHash(),0);
        utxoPool1.addUTXO(utxo3,tx2.getOutput(0));
        TxHandler txhandlertest4b=new TxHandler(utxoPool);
        System.out.println("the transaction2 for txhandlertest4b.IsValid(tx2) returns: "+txhandlertest4b.isValidTx(tx2));
        System.out.println("for txhandlertest4b.handleTxs(new Transaction[]{tx2}) return numbers of transactions: "+txhandlertest4b.handleTxs(new Transaction[]{tx2}).length);
    }

    public static void test5() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //test handleTx
        KeyPair ScroogeKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair BobKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair AliceKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair CatiKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair EustassKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair FigiKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        KeyPair GooKey=KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Transaction tx0=new Transaction();
        byte[] starthash=null;
        tx0.addInput(starthash,0);
        tx0.addOutput(20,ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        UTXO utxo=new UTXO(tx0.getHash(),0);
        UTXOPool utxoPool=new UTXOPool();
        utxoPool.addUTXO(utxo,tx0.getOutput(0));
        // the second block of scrooge chain
        Transaction tx1=new Transaction();
        tx1.addInput(tx0.getHash(),0);
        tx1.addOutput(15, BobKey.getPublic());//negative output
        tx1.addInput(tx0.getHash(),1);
        tx1.addOutput(5, AliceKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        // the third block of scrooge chain
        Transaction tx2=new Transaction();
        tx2.addInput(tx1.getHash(),0);
        tx2.addOutput(8, CatiKey.getPublic());
        tx2.addInput(tx0.getHash(),1);
        tx2.addOutput(7, EustassKey.getPublic());
        tx2.TXsignature((RSAPrivateKey) BobKey.getPrivate(),0);
        // the fourth block of scrooge chain
        Transaction tx3=new Transaction();
        tx3.addInput(tx2.getHash(),0);
        tx3.addOutput(2, EustassKey.getPublic());
        tx3.addInput(tx0.getHash(),1);
        tx3.addOutput(6, FigiKey.getPublic());
        tx3.TXsignature((RSAPrivateKey) CatiKey.getPrivate(),0);
        // invalid transaction signed by Eustass
        Transaction tx4=new Transaction();
        tx4.addInput(tx3.getHash(),1);
        tx4.addOutput(8, AliceKey.getPublic());
        tx4.TXsignature((RSAPrivateKey) FigiKey.getPrivate(),0);
        // unrelated and invalid transaction signed by Goo
        Transaction tx5=new Transaction();
        tx5.addInput(null,0);
        tx5.addOutput(20,GooKey.getPublic());
        tx5.TXsignature((RSAPrivateKey) GooKey.getPrivate(),0);
        // create an unordered Transaction[]
        Transaction[] randomTx=new Transaction[]{tx3,tx5,tx1,tx3,tx0,tx4};
        TxHandler txhandlertest7=new TxHandler(utxoPool);
        Transaction[] rightTx=txhandlertest7.handleTxs(randomTx);
        System.out.println(" and their numbers are: "+txhandlertest7.handleTxs(new Transaction[]{tx0,tx1,tx2,tx4,tx5,tx3}).length);
        for(Transaction tx:rightTx)
            System.out.println("the included transaction are "+tx.getRawTx().toString());

    }
}





