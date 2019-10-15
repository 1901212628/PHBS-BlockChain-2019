## Homework-2 README is in the Homework2 file

# PHBS_BlockChain_2019
**BlockChain Homework_1 : Scrooge Coin**  
Name : JiaXi Ren       
名字：任嘉曦
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
