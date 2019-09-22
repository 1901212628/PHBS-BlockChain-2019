import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.security.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestClass {
    private TxHandler txhandlertest7;
    private KeyPair ScroogeKey;
    private KeyPair BobKey;
    private KeyPair AliceKey;
    private KeyPair CatiKey;
    private KeyPair EustassKey;
    private KeyPair FigiKey;
    private KeyPair GooKey;
    byte[] starthash;


    @Before
    public void before() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        ScroogeKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        BobKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        AliceKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        CatiKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        EustassKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        FigiKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        GooKey = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        starthash = "starthash".getBytes();

    }

    @Test
    public void test1() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
      //test a simple valid transaction for verifying
        Transaction tx0 = new Transaction();
        tx0.addInput(starthash, 0);
        tx0.addOutput(20, ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo = new UTXO(tx0.getHash(), 0);
        UTXOPool utxoPool = new UTXOPool();
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        Transaction tx1 = new Transaction();
        tx1.addInput(tx0.getHash(), 0);
        tx1.addOutput(20, AliceKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo1 = new UTXO(tx1.getHash(), 0);
        utxoPool.addUTXO(utxo1, tx1.getOutput(0));
        TxHandler txhandlertest1 = new TxHandler(utxoPool);
        System.out.println("the transaction1 for txhandlertest1.IsValid(tx1) returns： " + txhandlertest1.isValidTx(tx1));
        assertEquals(1,txhandlertest1.handleTxs(new Transaction[]{tx1}).length);
    }

    @Test

    public void test2() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test the situation that you spend the same coin in two times(double spend attack)
        Transaction tx0 = new Transaction();
        tx0.addInput(starthash, 0);
        tx0.addOutput(20, ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo = new UTXO(tx0.getHash(), 0);
        UTXOPool utxoPool = new UTXOPool();
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        Transaction tx1 = new Transaction();
        tx1.addInput(tx0.getHash(), 0);
        tx1.addOutput(20, BobKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo1 = new UTXO(tx1.getHash(), 0);
        utxoPool.addUTXO(utxo1, tx1.getOutput(0));
        utxoPool.removeUTXO(utxo);
        Transaction tx2 = new Transaction();
        tx2.addInput(tx1.getHash(), 0);
        tx2.addOutput(20, AliceKey.getPublic());
        tx2.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo2 = new UTXO(tx2.getHash(), 0);
        utxoPool.addUTXO(utxo2, tx2.getOutput(0));
        utxoPool.removeUTXO(utxo1);
        TxHandler txhandlertest2 = new TxHandler(utxoPool);
        System.out.println("the transaction2 for txhandlertest1.IsValid(tx2) returns: " + txhandlertest2.isValidTx(tx2));
        assertEquals(0,txhandlertest2.handleTxs(new Transaction[]{tx1}).length);
    }

    @Test
    public void test3() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test if the output is negative
        Transaction tx0 = new Transaction();
        tx0.addInput(starthash, 0);
        tx0.addOutput(20, ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo = new UTXO(tx0.getHash(), 0);
        UTXOPool utxoPool = new UTXOPool();
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        Transaction tx1 = new Transaction();
        tx1.addInput(tx0.getHash(), 0);
        tx1.addOutput(-2, BobKey.getPublic());//negative output
        tx1.addInput(tx0.getHash(), 1);
        tx1.addOutput(5, AliceKey.getPublic());
        tx1.addInput(tx0.getHash(), 0);
        tx1.addOutput(17, ScroogeKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo1 = new UTXO(tx1.getHash(), 0);
        utxoPool.addUTXO(utxo1, tx1.getOutput(0));
        UTXO utxo2 = new UTXO(tx1.getHash(), 1);
        utxoPool.addUTXO(utxo2, tx1.getOutput(0));
        UTXO utxo3 = new UTXO(tx1.getHash(), 2);
        utxoPool.addUTXO(utxo3, tx1.getOutput(0));
        utxoPool.removeUTXO(utxo);
        TxHandler txhandlertest3 = new TxHandler(utxoPool);
        System.out.println("the transaction1 for txhandlertest3.IsValid(tx1) returns: " + txhandlertest3.isValidTx(tx1));
        assertEquals(0, txhandlertest3.handleTxs(new Transaction[]{tx1}).length);
    }
    @Test
    public void test4() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test if signature is invalid
        Transaction tx0 = new Transaction();
        tx0.addInput(starthash, 0);
        tx0.addOutput(20, ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) AliceKey.getPrivate(), 0);
        UTXO utxo = new UTXO(tx0.getHash(), 0);
        UTXOPool utxoPool = new UTXOPool();
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        TxHandler txhandlertest4 = new TxHandler(utxoPool);
        System.out.println("the transaction0 for txhandlertest4.IsValid(tx0) returns: " + txhandlertest4.isValidTx(tx0));
        assertEquals(0,txhandlertest4.handleTxs(new Transaction[]{tx0}).length);
    }

    @Test
    public void test5() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test if output is not claimed in current UTXO pool
        Transaction tx0 = new Transaction();
        tx0.addInput(starthash, 0);
        tx0.addOutput(20, ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) AliceKey.getPrivate(), 0);
        UTXO utxo = new UTXO(tx0.getHash(), 0);
        UTXOPool utxoPool = new UTXOPool();
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        TxHandler txhandlertest5 = new TxHandler(utxoPool);
        System.out.println("the transaction0 for txhandlertest5.IsValid(tx0) returns: " + txhandlertest5.isValidTx(tx0));
        assertEquals(0,txhandlertest5.handleTxs(new Transaction[]{tx0}).length);
    }

    @Test
    public void test6() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        //test if the output is greater than input
        Transaction tx0 = new Transaction();
        tx0.addInput(starthash, 0);
        tx0.addOutput(20, ScroogeKey.getPublic());
        tx0.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo = new UTXO(tx0.getHash(), 0);
        UTXOPool utxoPool = new UTXOPool();
        utxoPool.addUTXO(utxo, tx0.getOutput(0));
        Transaction tx1 = new Transaction();
        tx1.addInput(tx0.getHash(), 0);
        tx1.addOutput(3, BobKey.getPublic());//negative output
        tx1.addInput(tx0.getHash(), 1);
        tx1.addOutput(3, AliceKey.getPublic());
        tx1.addInput(tx0.getHash(), 0);
        tx1.addOutput(17, ScroogeKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(), 0);
        UTXO utxo1 = new UTXO(tx1.getHash(), 0);
        utxoPool.addUTXO(utxo1, tx1.getOutput(0));
        UTXO utxo2 = new UTXO(tx1.getHash(), 1);
        utxoPool.addUTXO(utxo2, tx1.getOutput(0));
        UTXO utxo3 = new UTXO(tx1.getHash(), 2);
        utxoPool.addUTXO(utxo3, tx1.getOutput(0));
        utxoPool.removeUTXO(utxo);
        TxHandler txhandlertest6 = new TxHandler(utxoPool);
        System.out.println("the transaction1 for txhandlertest6.IsValid(tx1) returns: " + txhandlertest6.isValidTx(tx1));
        assertEquals(0,txhandlertest6.handleTxs(new Transaction[]{tx1}).length);
    }
    @Test
    public void test7() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //test handleTx,now create a coinbase transaction
        Transaction tx0=new Transaction();
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
        tx1.addOutput(5, AliceKey.getPublic());
        tx1.TXsignature((RSAPrivateKey) ScroogeKey.getPrivate(),0);
        // the third block of scrooge chain
        Transaction tx2=new Transaction();
        tx2.addInput(tx1.getHash(),0);
        tx2.addOutput(8, CatiKey.getPublic());
        tx2.addOutput(7, EustassKey.getPublic());
        tx2.TXsignature((RSAPrivateKey) BobKey.getPrivate(),0);
        // the fourth block of scrooge chain
        Transaction tx3=new Transaction();
        tx3.addInput(tx2.getHash(),0);
        tx3.addOutput(2, EustassKey.getPublic());
        tx3.addOutput(6, FigiKey.getPublic());
        tx3.TXsignature((RSAPrivateKey) CatiKey.getPrivate(),0);
        // invalid transaction signed by Eustass
        Transaction tx4=new Transaction();
        tx4.addInput(tx3.getHash(),1);
        tx4.addOutput(8, AliceKey.getPublic());
        tx4.TXsignature((RSAPrivateKey) FigiKey.getPrivate(),0);
        // unrelated and invalid transaction signed by Goo
        Transaction tx5=new Transaction();
        tx5.addInput(starthash,0);
        tx5.addOutput(20,GooKey.getPublic());
        tx5.TXsignature((RSAPrivateKey) GooKey.getPrivate(),0);
        //create an unordered Transaction[]
        Transaction[] randomTx=new Transaction[]{tx3,tx5,tx1,tx3,tx0,tx4};
        txhandlertest7=new TxHandler(utxoPool);
        Transaction[] rightTx=txhandlertest7.handleTxs(randomTx);
        System.out.println();
        for(Transaction tx:rightTx)
            System.out.println("the included transaction are "+tx.getRawTx().toString());
        assertEquals(2,txhandlertest7.handleTxs(new Transaction[]{tx2,tx1,tx0,tx5,tx4,tx3}).length);
    }
}



