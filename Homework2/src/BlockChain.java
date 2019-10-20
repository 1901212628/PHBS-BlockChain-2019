// Block Chain should maintain only limited block nodes to satisfy the functions
// You should not have all the blocks added to the block chain in memory
// as it would cause a memory overflow.

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class BlockChain {
    public static int CUT_OFF_AGE = 10;
    public static int MAXIMUM_CUT_OFF = 100;
    private BlockChainNode CurrentNode;
    private Map<byte[], BlockChainNode> Blockchain;
    private TransactionPool TxPool;
    private UTXOPool utxoPool;
    /**
     * create an empty block chain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock){
        //First we create a BlockChain with Genesis Block!
        UTXOPool GenesisUTXOPool=CoinbaseUTXO(new UTXOPool(),genesisBlock);
        BlockChainNode GenesisNode= new BlockChainNode(genesisBlock, GenesisUTXOPool, 0);
        //in this code,I learn HashMap related function
        this.Blockchain = new HashMap<byte[], BlockChainNode>(){{ put(genesisBlock.getHash(), GenesisNode); }};
        this.CurrentNode = GenesisNode;
        this.TxPool = new TransactionPool();
    }

    private UTXOPool CoinbaseUTXO(UTXOPool utxoPool,Block block){
        //this function is for modifying the current UTXO after a CoinBase transaction
        Transaction CoinbaseTransaction=block.getCoinbase();
        utxoPool.addUTXO(new UTXO(CoinbaseTransaction.getHash(), 0), CoinbaseTransaction.getOutput(0));
        return utxoPool;
    }
    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
        return CurrentNode.getBlock();
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
        return CurrentNode.getUtxoPool();
    }

    /** Get the transaction pool to mine a new block */
    public TransactionPool getTransactionPool() {
        return new TransactionPool(TxPool);
    }

    /**
     * Add {@code block} to the block chain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}.
     *
     * <p>
     * For example, you can try creating a new block over the genesis block (block height 2) if the
     * block chain height is {@code <=
     * CUT_OFF_AGE + 1}. As soon as {@code height > CUT_OFF_AGE + 1}, you cannot create a new block
     * at height 2.
     *
     * @return true if block is successfully added
     */
    public boolean addBlock(Block block) {
        //illegally add a genesisblock into blockchain
        if(block.getPrevBlockHash()==null){
            return false;
        }
        //the transactions in the block all should be valid
        BlockChainNode ParentNode=Blockchain.get(block.getPrevBlockHash());
        Transaction[] allTx=new Transaction[block.getTransactions().size()];
        block.getTransactions().toArray(allTx);
        Transaction[] ValidTxs = new TxHandler(ParentNode.getUtxoPool()).handleTxs(allTx);
        ArrayList<Transaction> ValidTxs1=new ArrayList<Transaction>();
        for(int i=0;i<ValidTxs.length;i++){
            ValidTxs1.add(ValidTxs[i]);
        }
        if(!(ValidTxs1.size()==block.getTransactions().size())){
            return false;
        }
        //block add in blockchain should have a valid previous block hash
        if(!Blockchain.containsKey(block.getPrevBlockHash())){
            return false;
        }
        //block add in blockchain should be in a regulated position(in right position and don't exceed maximum storage)
        if(!(ParentNode.getHeight()+1>CurrentNode.getHeight()-CUT_OFF_AGE) || !(ParentNode.getHeight()+1>CurrentNode.getHeight()-MAXIMUM_CUT_OFF)){
            return false;
        }
        //After all verification,add block in the blockchain and update blockchain node information at the same time
        BlockChainNode currentnode = BuildNode(block);
        Blockchain.put(block.getHash(), currentnode);
        UpdateHighestNode(currentnode);
        RemoveTxsFromTxPool(block.getTransactions());
        //After doing all this,return true
        return true;
    }
    private void UpdateHighestNode(BlockChainNode currentnode) {
        //for simplify,create a private function for updating the CurrentNode
        if(CurrentNode.getHeight() < currentnode.getHeight()) {
            CurrentNode = currentnode;
            //Here I restrict my blockchain map storage limit in the recent height 100 to get rid of over storage
            if (CurrentNode.getHeight() >= MAXIMUM_CUT_OFF) {
                for (Map.Entry<byte[], BlockChainNode> entry : Blockchain.entrySet()) {
                    if (entry.getValue().getHeight()<=CurrentNode.getHeight()-(MAXIMUM_CUT_OFF)){
                        Blockchain.remove(entry);
                    }
                }
            }
        }
    }
    private void RemoveTxsFromTxPool(ArrayList<Transaction> txs) {
        //for simplify,create a private function for removing the CurrentNode
        txs.forEach(tx -> TxPool.removeTransaction(tx.getHash()));
       }
    private BlockChainNode BuildNode(Block block) {
        //for simplify,create a private function for building the CurrentNode
        BlockChainNode parentNode = Blockchain.get(block.getPrevBlockHash());
        TxHandler txHandler = new TxHandler(parentNode.getUtxoPool());
        Transaction[] allTx=new Transaction[block.getTransactions().size()];
        block.getTransactions().toArray(allTx);
        txHandler.handleTxs(allTx);
        return new BlockChainNode(block, CoinbaseUTXO(txHandler.getUTXOPool(), block), parentNode.getHeight() + 1);
    }
    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
        TxPool.addTransaction(tx);
    }
}

