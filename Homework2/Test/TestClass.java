import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestClass {
    @Before
    @After

    @Test
    public void test1() throws Exception {
        //We verify a simple occasion that the blockchain only contain a genesis block
        KeyPair pair1 = KeyPair();
        Block genesisBlock = new Block(null, pair1.getPublic());
        BlockChain blockchain = new BlockChain(genesisBlock);
        Block maxHeightBlock = blockchain.getMaxHeightBlock();
        Transaction CoinbaseTx = new Transaction(25, pair1.getPublic());
        assertThat(maxHeightBlock.getHash(), equalTo(null));
        assertThat(maxHeightBlock.getCoinbase(), equalTo(CoinbaseTx));
        assertThat(blockchain.getMaxHeightUTXOPool().getAllUTXO().size(), equalTo(1));
        assertThat(blockchain.getMaxHeightUTXOPool().getAllUTXO().get(0), equalTo(new UTXO(CoinbaseTx.getHash(), 0)));
    }

    @Test
    public void test2() throws Exception {
        //We add another valid block over genesis block for verification
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockChain = new BlockChain(genesisBlock);
        KeyPair keyPair = KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), keyPair.getPublic());
        Transaction txSpendGenesisCoinBase = TransactionSpendingAllCoinBase(genesisBlock, genesisPair, keyPair);
        Block1.addTransaction(txSpendGenesisCoinBase);
        Block1.finalize();
        assertThat(blockChain.addBlock(Block1), equalTo(true));
        assertThat(blockChain.getMaxHeightBlock().getHash(), equalTo(Block1.getHash()));
        assertThat(blockChain.getMaxHeightBlock().getCoinbase(), equalTo(new Transaction(25, keyPair.getPublic())));
    }

    @Test
    public void test3() throws Exception {
        //As Homework2 regulation,verifying the spend of coinbase transaction in the next block
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockChain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        assertThat(blockChain.addBlock(Block1), equalTo(true));
        KeyPair KeyPair2 = KeyPair();
        Block Block2 = new Block(Block1.getHash(), KeyPair2.getPublic());
        Transaction txSpendingPreviousCoinBase = TransactionSpendingAllCoinBase(Block1, KeyPair1, KeyPair2);
        Block2.addTransaction(txSpendingPreviousCoinBase);
        Block2.finalize();
        assertThat(blockChain.addBlock(Block2), equalTo(true));
        assertThat(blockChain.getMaxHeightBlock().getHash(), equalTo(Block2.getHash()));
        assertThat(blockChain.getMaxHeightBlock().getCoinbase(), equalTo(new Transaction(25, KeyPair2.getPublic())));
    }

    @Test
    public void test4() throws Exception {
        //To verify invalid insert of a block into blockchain(invalid insert position)，additional information
        //is that we change the CUT_OFF_AGE for simplification,this will not affect the verification of the function.
        BlockChain.CUT_OFF_AGE = 1;
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        assertThat(blockchain.addBlock(Block1), equalTo(true));
        KeyPair KeyPair2 = KeyPair();
        Block Block2 = new Block(Block1.getHash(), KeyPair2.getPublic());
        Block2.finalize();
        assertThat(blockchain.addBlock(Block2), equalTo(true));
        Block validHeightBlock = new Block(Block1.getHash(), KeyPair().getPublic());
        validHeightBlock.finalize();
        assertThat(blockchain.addBlock(validHeightBlock), equalTo(true));
        Block invalidHeightBlock = new Block(genesisBlock.getHash(), KeyPair().getPublic());
        invalidHeightBlock.finalize();
        assertThat(blockchain.addBlock(invalidHeightBlock), equalTo(false));
    }

    @Test
    public void test5() throws Exception {
        //To verify the validation of Transaction Pool after a transaction happened
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        Transaction tx = TransactionSpendingAllCoinBase(genesisBlock, genesisPair, KeyPair());
        blockchain.addTransaction(tx);
        assertThat(blockchain.getTransactionPool().getTransactions(), equalTo(new ArrayList<>(Arrays.asList(tx))));
    }

    @Test
    public void test6() throws Exception {
        //test6 aim to verify whether a transaction is removed from Transaction Pool after adding in a valid block
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockChain = new BlockChain(genesisBlock);
        Transaction tx = TransactionSpendingAllCoinBase(genesisBlock, genesisPair, KeyPair());
        blockChain.addTransaction(tx);
        KeyPair miner = KeyPair();
        Block block = new Block(genesisBlock.getHash(), miner.getPublic());
        block.addTransaction(tx);
        block.finalize();
        assertThat(blockChain.addBlock(block), equalTo(true));
        assertThat(blockChain.getTransactionPool().getTransactions().isEmpty(), equalTo(true));
    }

    @Test
    public void test7() throws Exception{
        //test7 aim to verify BlockChain.java can return the oldest block when valid equal longest chain occur
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        KeyPair KeyPair2 = KeyPair();
        Block Block2 = new Block(genesisBlock.getHash(), KeyPair2.getPublic());
        Block2.finalize();
        KeyPair KeyPair3 = KeyPair();
        Block Block3 = new Block(genesisBlock.getHash(), KeyPair3.getPublic());
        Block2.finalize();
        assertThat(blockchain.addBlock(Block1), equalTo(true));
        assertThat(blockchain.addBlock(Block2), equalTo(true));
        assertThat(blockchain.addBlock(Block3), equalTo(true));
        assertThat(blockchain.getMaxHeightBlock().getHash(), equalTo(Block1.getHash()));
        assertThat(blockchain.getMaxHeightBlock().getCoinbase(), equalTo(new Transaction(25, KeyPair1.getPublic())));
    }

    @Test
    public void test8() throws Exception{
        //Other 3 illegall addblock operation verification:add a genesis block,add an invalid block with invalid
        //transaction,and add an invalid block with wrong previous hash
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = KeyPair();
        Block genesisBlock1 = new Block(null, KeyPair1.getPublic());
        genesisBlock1.finalize();
        assertThat(blockchain.addBlock(genesisBlock1), equalTo(false));
        //create an invalid transaction
        KeyPair KeyPair2=KeyPair();
        Block Block1=new Block(genesisBlock.getHash(), KeyPair2.getPublic());
        KeyPair KeyPair3 = KeyPair();
        Transaction tx = new Transaction(44,KeyPair3.getPublic());
        blockchain.addTransaction(tx);
        Block1.addTransaction(tx);
        Block1.finalize();
        assertThat(blockchain.addBlock(Block1), equalTo(false));
        KeyPair KeyPairtest = KeyPair();
        Block Blocktest=new Block(null, KeyPairtest.getPublic());
        KeyPair KeyPair4 = KeyPair();
        Block Block2=new Block(Blocktest.getHash(), KeyPair4.getPublic());
        Block2.finalize();
        assertThat(blockchain.addBlock(Block2), equalTo(false));
    }

    @Test
    public void test9() throws Exception{
        //To verify a validation records of the CurrentNode when new block add in a new valid equal height block
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        KeyPair KeyPair2 = KeyPair();
        Block Block2 = new Block(genesisBlock.getHash(), KeyPair2.getPublic());
        Block2.finalize();
        KeyPair KeyPair3 = KeyPair();
        Block Block3 = new Block(Block2.getHash(), KeyPair3.getPublic());
        Block3.finalize();
        assertThat(blockchain.addBlock(Block1), equalTo(true));
        assertThat(blockchain.addBlock(Block2), equalTo(true));
        assertThat(blockchain.addBlock(Block3), equalTo(true));
        assertThat(blockchain.getMaxHeightBlock().getHash(), equalTo(Block3.getHash()));
        assertThat(blockchain.getMaxHeightBlock().getCoinbase(), equalTo(new Transaction(25, KeyPair3.getPublic())));
    }

    @Test
    public void test10() throws Exception{
        //To verify my limited storage function in UpdateHighestNode is right
        BlockChain.MAXIMUM_CUT_OFF=1;
        KeyPair genesisPair = KeyPair();
        Block genesisBlock = new Block(null, genesisPair.getPublic());
        genesisBlock.finalize();
        BlockChain blockchain = new BlockChain(genesisBlock);
        KeyPair KeyPair1 = KeyPair();
        Block Block1 = new Block(genesisBlock.getHash(), KeyPair1.getPublic());
        Block1.finalize();
        KeyPair KeyPair2 = KeyPair();
        Block Block2 = new Block(Block1.getHash(), KeyPair2.getPublic());
        Block2.finalize();
        KeyPair KeyPair3 = KeyPair();
        Block Block3 = new Block(Block2.getHash(), KeyPair3.getPublic());
        Block3.finalize();
        KeyPair KeyPair4 = KeyPair();
        Block Block4 = new Block(genesisBlock.getHash(), KeyPair4.getPublic());
        Block4.finalize();
        assertThat(blockchain.addBlock(Block1), equalTo(true));
        assertThat(blockchain.addBlock(Block2), equalTo(true));
        assertThat(blockchain.addBlock(Block3), equalTo(true));
        assertThat(blockchain.addBlock(Block4), equalTo(false));
    }

    private Transaction TransactionSpendingAllCoinBase(Block block, KeyPair blockMiner, KeyPair receiver) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //A specific transaction that miner use his money from coinbase transaction
        Transaction tx = new Transaction();
        tx.addInput(block.getCoinbase().getHash(), 0);
        tx.addOutput(25, receiver.getPublic());
        Signature signature = TxSignature(tx, blockMiner);
        tx.addSignature(signature.sign(), 0);
        tx.finalize();
        return tx;
    }

    private Signature TxSignature(Transaction singleInputTx, KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Signature for a single transaction input
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(singleInputTx.getRawDataToSign(0));
        return signature;
    }

    private KeyPair KeyPair() throws NoSuchAlgorithmException {
        //New simple method about generating  a keypair
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }
}


