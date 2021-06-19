# PHBS_BlockChain_2021
**BlockChain Homework_2 : BlockChain**

Name : Yingjie Jiang       
名字：蒋英杰

## Homework_2 description
* The homework mainly include three parts:add our TxHandler.java from Homewok_1 and make some appropriate modification;finsh blockchain.java class;create a test class to verify whether my work is correct.
* In this homework,we actually act as an miner in BlockChain net work.Here is some different:we verify heard transactions and blocks,but we don't verify proof-of-work section of a block.We maintain BlockChain and cut the very former ones.We don't compute nonce in this case,we act more like a Bitcoin Community volunteer rather than a miner.
## Homework_2 attention
* This project's test cases are in the Test file. 
* For simplification,I add my KeyPair Generater`private KeyPair KeyPair()` and Signature Generater`private Signature SignatureForSingleInputTx` directly in the TestClass.java.
* For users,they should test BlockHandler.java at TestClass.While our task is to complete BlockChain.java.For simplification,I test BlockChain.java functions directly.This may not effect the final outcome.
## Problems before finding solution of Homework_2
* Take consideration of storage into account.
* In which way that we store the structure of a Blockchain?As we know,Blockchain form as a tree rather than a list.
* How can we assure the block we take is the oldest one when we get the current block?
## Preparing for Homework_2 solution
* Before coding,I read and realize the proposed java class and make notes bellow.
## Sumary for Homework_2 solution
### 1.BlockChain.java
**Six functions in BlockChain.java and I complete them**
* First task is to create a BlockChain that only maintains a genesis block.The difficulty of it is its structure.I create three object elements before this function:CurrentNode in BlockChainNode.java;a map connected block's hash and its BlockChainNode;and TransactionPool.
* BlockChainNode.java is a new class I add which consists of Block,Block's Height and modified UTXOPool and their get-method.
* I create a function ` private UTXOPool CoinbaseUTXO(UTXOPool utxoPool,Block block)` for modifying UTXOPool after receiving a coinbase transaction.
* In first task,I create the modified UTXOPool by `private UTXOPool CoinbaseUTXO(UTXOPool utxoPool,Block block)` ;create the BlockChainNode I mentioned before,and a new transactionpool.
* From second task to fourth task are easy,I simply return `CurrentNode.getBlock()`,`CurrentNode.getUtxoPool()`and`new TransactionPool(TxPool)` relatively.
* Five task,in my opinion,is the most difficult part in this homework.It requires us to verify block's validation and add the block into BlockChain if it pass the verification,and then return true.
* Four parts for us to verify:whether the block is a genesis block;whether the transactions in the block are all valid;whether the block have appropriate prevhash and whether it insert in a permitted position.
* Emphasis on inserting a block in permitted position,this means not only CUT_OF_AGE restriction but also MAXIMUM_CUT_OFF restriction,which means you can't add a block whose parent block has been cut off for saving storage space.
* After verifying all this,add the block into BlockChain,update BlockChain,UTXOPool and TransactionPool.Finally return true.
* Sixth task I simply return`TxPool.addTransaction(tx)`.
### 2.TestClass
**Ten independent tests are in TestClass.java for testing,here are their description:**
* **test1()** We verify a simple occasion that the BlockChain only contains a genesis block,verify all the conditions that are in BlockChian.java.
* **test2()** We add another valid block over genesis block for verification.The procedure of verification is the same as test1
* **test3()** As Homework_2 regulation,verifying the validation of the spend of coinbase transaction in the next block.
* **test4()** To verify valid insert of a block into BlockChain and invalid insert of a block into BlockChain(invalid insert position)，additional information is that we change the CUT_OFF_AGE for simplification,this will not affect the verification of the function.
* **test5()** To verify the validation of Transaction Pool after a transaction happened.
* **test6()** Test6 aim to verify whether a transaction is removed from TransactionPool after adding in a valid block.
* **test7()** Test7 aim to verify BlockChain.java can return the oldest block when valid equal longest chain occur.
* **test8()** Other three illegall block adding operation verification:add a genesis block,add an invalid block with invalid transaction,and add an invalid block with wrong previous hash.
* **test9()** To verify a validation records of the CurrentNode when new block add in a new valid equal height block.
* **test10()** To verify my limited storage function in UpdateHighestNode is right.Additional information is that we change the MAXIMUM_CUT_OFF for simplification,this will not affect the verification of the function.
