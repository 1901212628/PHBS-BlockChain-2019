# PHBS_BlockChain_2019
**BlockChain Homework_2 : BlockChain**

Name : JiaXi Ren       
名字：任嘉曦

## Homework_2 description
* The homework mainly include three parts:add our TxHandler.java from Homewok_1 and make some appropriate modification;finsh blockchain.java class;create a test class to verify your work is correct.
* In this homework,we actually act as an miner in BlockChain net work.Here is some different:we verify heard transactions and blocks,but we don't verify proof-of-work section of a block.We maintain BlockChain and cut the very former ones.We don't compute nonce in this case,we act more like a Bitcoin Community volunteer rather than a miner.
## Homework_2 attention
* This project's test cases are TestClass. 
* For simplification,I add my KeyPair Generater`private KeyPair KeyPair()` and Signature Generater`private Signature SignatureForSingleInputTx` directly in the TestClass.java.
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
* After verifying all this,add the block into BlockChain,update BlockChain,UTXOPool and TransactionPool.Finally return true.
* Sixth task I simply return`TxPool.addTransaction(tx)`.
### 2.TestClass
**Eight independent tests are in TestClass.java for testing,here are their description:**
* **test1()** We verify a simple occasion that the BlockChain only contains a genesis block,verify all the conditions that are in BlockChian.java.
* **test2()** We add another valid block over genesis block for verification.The procedure of verification is the same as test1
* **test3()** As Homework_2 regulation,verifying the validation of the spend of coinbase transaction in the next block.
* **test4()** To verify valid insert of a block into BlockChain and invalid insert of a block into BlockChain(invalid insert position)，additional information is that we change the CUT_OFF_AGE for simplification,this will not affect the verification of the function.
* **test5()** To verify the validation of Transaction Pool after a transaction happened.
* **test6()** Test6 aim to verify whether a transaction is removed from TransactionPool after adding in a valid block.
* **test7()** Test7 aim to verify BlockChain.java can return the oldest block when valid equal longest chain occur.
* **test8()** Other three illegall block adding operation verification:add a genesis block,add an invalid block with invalid transaction,and add an invalid block with wrong previous hash.
## Test result
* **Testing Results:** The Functions I created for verifying the 7 tests all pass and get expected results. Here is a screenshot in IntelliJ IDEA IDE:
![Image of resultImage2](https://github.com/JiaXi-Ren/PHBS_BlockChain_2019/blob/master/resultImage2.png)
 <br />
 <br />
 <br /> 
 <br />    
 <br />        
 <br />
 <br />
 <br />
 <br />
 <br />
**BlockChain Homework_1 : Scrooge Coin**  

## Homework_1 description
* The homework need us to finish four part:create a public ledger that record current UTXO set by adding UTXOPool(upool);finish a function that verify the validation of the transactions;finish a function that digest unordered transactions and return legal ones;bulid a test class to test all your work.
* As we learn in the class,ScroogeCoin avoid double spend attack problem compared to GoofyCoin result in centralizing environment.Two kinds of transactions:CreateCoins transaction and PayCoins transaction are included.Every transaction's validation and implement are confirmed by Scrooge,and the chain release by Scrooge too.Only Scrooge can create new ScroogeCoins based on CreateCoins transaction.
## Homework_1 questions
* As a highly centralize currency system,every kinds of transactions should be signed by Scrooge before posting to the chain,while in the given codes of our homework seems to forget this step.
* The order of the arraylist of outputs,especially for multi-output transaction,is fuzzy for its rule.In which order should we place them?
## Homework_1 attention
* This project's test cases are TestClass.  
* For simplification,I add 2 methods in Transaction.java `public static void genSignature()`  for generating public key,private key and signature and  `public void TXsignature(RSAPrivateKey privatekey, int inputindex)` for adding signature and generate transaction hash.
## Preparing for homework solution
* Before coding,I read and realize the proposed java class and make notes bellow.I also add two methods in Transaction.java for simplifying my following solution of the homework.
## Sumary for Homework_1 solution
### 1.TxHandler
**Three functions in TxHandler.java and I complete them**
* for the first task,I create a public Ledger with specific UTXOPool as follow.
* for the second task,I learn and use a Hashset<>() for storing the existed utxo members,and verify five invalid situation in order.
* for the third task,I learn and use fixed point algorithm for decreasing a time difficulty of the function.Specially,we do not need to oversee the longest valid chain.Instead,we only verify the valid tansactions in a FCFS order.After verifying,a new utxo will add in utxopool and the previous one will be removed. 
### 2.TestClass
**Ten independent tests are in TestClass.java for testing,here are their description:**
* **test1()** simply create a valid coinbase transaction and a valid normal transaction,then verify its validation with`public boolean isValidTx(Transaction tx)`and return the numbers of valid tansaction[] with`public  Transaction[] handleTxs(Transaction[] possibleTxs)`. 
* **test2()** is a double spending attack test,suppose Scrooge spend the same coins paying to different people,then verify its validation with`public boolean isValidTx(Transaction tx)`and return the numbers of valid tansaction[] with`public  Transaction[] handleTxs(Transaction[] possibleTxs)`.
* **test3()** is a neative output test,which means that Scrooge give negative numbers of coins to others.Verifying methods are the same as above.
* **test4()** is an invalid signature test,which means that a transaction is signed by unrelated person.Verifying methods are the same as above.
* **test5()** test the situation when transaction's corresponding previous ouput is not in the utxopool.Verifying methods are the same as above.
* **test6()** is an outpus-greater-than-inputs test,which means someone give exceeding coins to others he don't own.Verifying methods are the same as above.
* **test7()**  specifily test `public  Transaction[] handleTxs(Transaction[] possibleTxs)` by adding a valid chain of transactions and some invalid or unrelated transactions in mess order,then verifying the valid Transaction[] and their numbers.
* **test8()** this is a one-in and multi-out test.Specially,I add Transaction.output information to ensure the correction of the codes.
* **test9()** this is a multi-in and one-out test,similar method as test8.
* **test10()** this is a multi-in and multi-out test,similar method as test8.
## Test result
* **Testing Results:** The Functions I created for verifying the 7 tests all pass and get expected results. Here is a screenshot in IntelliJ IDEA IDE:
 ![Image of resultImage](https://github.com/JiaXi-Ren/PHBS_BlockChain_2019/blob/master/resultImage.png)
