## Welcome to IQSRec Pages
This repository matains the **Java implementation** of QoS-centric service recommendation on the incomplete QoS dataset.

You can read more information about the algorithm from our publication:

1. Yanjun Shu,  Jianhang Zhang, Wei Emma Zhang, Decheng Zuo, and Quan Z. Sheng. "Dimension-based Partition for Skyline Service Recommendation on Incomplete QoS", manuscript. 


## Dependencies

* Java 1.8 (<https://www.oracle.com/>)

## Usage

 The library implements MSPC and IQSRec algorithms for incomplete datasets, and SFS algorithm for complete datasets. 

### Setp 1. Create the corresponding class
   
* Complete datasets algorithm
  * SFS : ` SortedFilterSkyline cs = new SortedFilterSkyline();`
* Incomplete datasets algorithm
  * Native : `Native nativeSky = new NativeSkyline();`
  * MSPC : `MSPC mspc = new MSPC();`
  * PSkyline : `IQSRec iqsrec = new IQSRec();`

### Step 2. Init data (option)
There are two ways to init data:
1. Read data, then init 
```
float[][] data = new FileHandler().readFile(filePath, withHead);
PSkyline mspc = new MPSC();
mspc.initData(data);
```
2. Call the readFile function
```
MSPC mspc = new MSPC();
mspc.readFile(filePath, false);
```
### Setp 3. Execute query algorithm
* Complete 
  * SFS : `cs.getSkyline(data);`
* Incomplete 
  * Native : `nativeSky.getSkyline(topK);`
  * MSPC : `mspc.getSkyline(topK);`
  * IQSrec: `iqsrec.getSkyline(m, method);`
  

## FeedBack
If you find any bugs or errors, please post to our [issue page](https://github.com/jhzhang98/IntervalSkylineFinalTmp/issues). Also for any enquire, you can drop an e-mail to us (<jh.zhang98@qq.com>).
