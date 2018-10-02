# WebHitCount

 ## Problem: 
 You’re given an input file. Each line consists of a timestamp (unix epoch in seconds) and a url separated by ‘|’ (pipe operator). The entries are not in any chronological order.  Your task is to produce a daily summarized report on url hit count, organized daily (mm/dd/yyyy GMT) with the earliest date appearing first. For each day, you should display the number of times each url is visited in the order of highest hit count to lowest count. Your program should take in one command line argument: input file name. The output should be printed to stdout. You can assume that the cardinality (i.e. number of distinct values) of hit count values and the number of days are much smaller than the number of unique URLs. You may also assume that number of unique URLs can fit in memory, but not necessarily the entire file.
 
```
 Input.txt
   1407564301|www.nba.com
   1407478021|www.facebook.com
   1407478022|www.facebook.com
   1407481200|news.ycombinator.com
   1407478028|www.google.com
   1407564301|sports.yahoo.com
   1407564300|www.cnn.com
   1407564300|www.nba.com
   1407564300|www.nba.com
   1407564301|sports.yahoo.com
   1407478022|www.google.com
   1407648022|www.twitter.com
```

``` 
 Output
   08/08/2014 GMT
   www.facebook.com 2
   www.google.com 2
   news.ycombinator.com 1
   08/09/2014 GMT
   www.nba.com 3
   sports.yahoo.com 2
   www.cnn.com 1
   08/10/2014 GMT
   www.twitter.com 1
```
 
 Correctness, efficiency (speed and memory) and code cleanliness will be evaluated. 
 Please provide a complexity analysis in Big-O notation for your program along with your source. 

 ## Solution:
 The code exists in src/main/java/webPageHits/webPageHits.java

  # Run:
   1. From Eclipse IDE:
      * Import the project into a existing maven repo
      * Click on run button provided by the IDE
   1. From command line:
      * Go to target/classes and run this command:
      * java -cp ./ webPageHits.webPageHits ../../src/main/resources/input.txt

  # Implementation:
    Two implementations Time and Space Complexity: 
    # webPageHitsLessSpaceMoreTime.java: in this implementation the data is read from the
    input file and given there are few distinct dates a TreeMap datastructure is maintained with the date string
    as the key. The TreeMap will be relatively small with fewer nodes so searches which are O(logN) will be quicker. 
    Insertions which is O(NlogN) is also quicker. To take advantage of few unique hitcounts while reading the input 
    from the file itself again a TreeMap datastructure with HitCount as the key is maintained. Since there are many
    unique URLs compared to dates and hitcount in each of the hitcount bucket there will be large number of URLS, first
    implementation was done with linked list but the current implementation is a TreeSet datastructure. Although this 
    datastructure is large since its a tree the searches are O(logN) and insertions are O(NlogN). So while inserting first
    based on the date webLinkHitCount object is fetched. Now we search for the webLink under each of the HitCount buckets
    if the webLink hitCount is found lets say under HitCount 1, then since we found webLink again this webLink is moved from
    HitCount 1 bucket to HitCount 2 bucket and so on and so forth. With this implementation the most time consuming operation
    is searching and moving the webLink from one hitCount bucket to another but while displaying the data everything is already
    sorted.

    N1 --> unique Dates
    N2 --> unique HitCounts
    N3 --> unique webLinks

    * Time Complexity: Insertion of Date into TreeMap datastructure is O(N1logN1) and searches are O(logN1) but N1 is relatively 
      small here. Insertion of HitCount into TreeMap datastructure is O(N2logN2) and searches are O(logN2) again unique hitCount
      is relatively small. Removal and Insertion of webLinks is O(N3logN3)(This is the move operation) and searches are O(logN3).
    
    * Space Complexity: Space complexity is O(N1) for unique dates, O(N2) for unique HitCounts and O(N3) for unique webLinks.
      Off all the unique dates, HitCounts and webLinks webLinks is the most for over a large set of data O(N3) would be the largest.


    # webPageHitsMoreSpaceLessTime.java: This is a optimization over webPageHitsLessSpaceMoreTime.java since the webLinks is stored in 
    TreeSet datastructure, in this implementation another datastructure HashMap is maintained, which has webLink as the key and HitCount
    as the value. In this implementation once webLinkHitCounts object is located based on date string, first we fetch the hitCount using 
    the webLink as the key into the HashMap and use this hitCount to fetch the weblink from TreeMap. This way we don't have to search 
    through all the HitCount buckets to locate the webLink there by saving the O(logN) searches. We still need to do O(NlogN) move operation.
    The advantage with this implementation is that the overall performance improves, the disadvantage is another datastructure hashmap has
    to be maintained which takes another O(N) space.

    * Time Complexity: Besides what is already said with this program, we are doing a O(1) look up based on webLink and find out the hitcount.
      This will reduce the search time from O(logN3) to O(1) since N3 is a relatively larger number the time saving will be huge for a large set
      of data.
      
    
    * Space Complexity: Besides what is already stated there is addition space taken to hold the hashTable, which is O(N3) since N3 is a large number
      this can mean using lot of space to gain the speed.

    Here is the description of remaining sub-classes, datastructures and functions:

  1. dateComp --> String comparator used by displayData TreeMap datastructure to sort based on Date String
  1. displayData --> Sorted TreeMap datastructure which holds data based on input file
  1. convertUTCSecondsToDate --> converter function to convert from seconds to UTC date.
  1. hitCountComparator --> the comparator used by TreeMap datastructure to sort the weblinks in descending order based on hitCount
  1. webLinkHitCounts --> the class which holds TreeMap datastructure and also a hashmap in webPageHitsMoreSpaceLessTime.java code
  1. displayData --> the function to display the data based on ascending order dates and with descending order of hitCount
  1. parseLine --> parser function to parse a line read from the given input file
  1. readFile --> reader function read lines from a given input file
  1. main  --> main function from where the program starts


 # Sources
   My Sources are primarily google searches, stackoverflow and javadocs.
   1. Priority queue big-O analysis: 
         *  https://en.wikipedia.org/wiki/Priority_queue
         *  https://stackoverflow.com/questions/47420638/time-complexity-of-java-priorityqueue-heap-insertion-of-n-elements
