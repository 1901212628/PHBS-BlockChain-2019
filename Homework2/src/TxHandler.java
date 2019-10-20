import java.util.*;

public class TxHandler {

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    //create a public Ledger,following the requirement
    public UTXOPool utxoPool;
    public TxHandler(UTXOPool uPool){
        this.utxoPool=new UTXOPool(uPool);
    }


    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        double SUMOFIN=0;
        double SUMOFOUT=0;
        //using a HashSet<>() to store existed utxo members
        Set<UTXO> used = new HashSet<>();
        for(int i=0;i<tx.numInputs();++i){
            Transaction.Input input=tx.getInput(i);
            UTXO utxo=new UTXO(input.prevTxHash,input.outputIndex);
            if(!utxoPool.contains(utxo))return false;//check whether the claimed outputs are in the current UTXO Pool
            Transaction.Output output=utxoPool.getTxOutput(utxo);
            SUMOFIN+=output.value;//for following check of input values and ouput values
            if(!Crypto.verifySignature(output.address,tx.getRawDataToSign(i),input.signature))return false;//to verify the valid signature
            if(!used.add(utxo))return false;//check whether there have double spend attack
        }
        for(Transaction.Output output:tx.getOutputs()){
            if(output.value<0)return false;//check if there have negative outputs
            SUMOFOUT+=output.value;
        }
        if(SUMOFIN<SUMOFOUT)return false;//check if outputs are greater than inputs
        return true;//after verifying,return true
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     * @param possibleTxs
     */
    public  Transaction[] handleTxs(Transaction[] possibleTxs) {
        HashSet<Transaction> txVis=new HashSet<>();//fixed point algorithm,enter until new transactions are all invalid
        while(true) {
            boolean trigger = false;  //set a trigger to ensure whether transactions are all invalid
            for (Transaction tx : possibleTxs) {
                if (txVis.contains(tx)) continue; //ignore passed transactions
                if (isValidTx(tx)) {
                    txVis.add(tx);
                    trigger = true; //valid transaction,not break the circulation
                    for (int i = 0; i < tx.numOutputs(); ++i) {
                        UTXO utxo = new UTXO(tx.getHash(), i);
                        utxoPool.addUTXO(utxo, tx.getOutput(i)); //add valid transaction into UTXOPool(updated)
                    }
                    for (int i = 0; i < tx.numInputs(); ++i) {
                        Transaction.Input input = tx.getInput(i);
                        UTXO utxo = new UTXO(input.prevTxHash, input.outputIndex);
                        utxoPool.removeUTXO(utxo); //remove previously valid transaction out of UTXOPool(updated)
                    }
                }
            }
            if(!trigger)break; //all transactions invalid
        }
        Transaction[] bunch=new Transaction[txVis.size()];
        int idx=0;
        for(Transaction tx:txVis)
            bunch[idx++]=tx;  //return the valid transaction[]
        return bunch;
    }
    public UTXOPool getUTXOPool(){
        return utxoPool;
    }
}