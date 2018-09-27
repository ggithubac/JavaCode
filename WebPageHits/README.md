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
   Go to target/classes and run this command:
    1. java -cp ./ webPageHits.webPageHits ../../src/main/resources/input.txt

  # Implementation:
    Given that there are fewer days and hit count values as compared to URLs. If we pick URL as the key and use 
    a hashtable then insertions and accesses would be of O(1). While displaying since we need to display based 
    on ascending order of dates so I picked TreeSet as the data structure to store the Dates, and also while 
    displaying we need to display based on the descending order of the hitcount so I picked a Priority Queue 
    to sort the hit count. So essentially I picked a HashMap of TreeSet combo datastructure while reading the 
    input from the file and just before displaying for each date I picked a Priority Queue to sort the URLs 
    using hitcount as the comparator. The Main class is webPageHits which has the main function, here is the 
    description of remaining sub-classes, datastructures and functions:

  1. webLinkHitCounts --> holds the hashmap on weblink and hitcount which is used while reading the data from input file.
  1. webLinkHitCount --> holds the two variables webLink and hitCount used inside the priority queue which is the datastructure to sort webLink in descending order of hitCounts
  1. dateClass --> holds the day, month and year variables and used to sort based on date and sorted in the TreeSet while reading from the file.
  1. dateComp --> comparator used by the TreeSet datastructure to sort the date in ascending order
  1. displayData --> The datastructure  used to store the date in ascending order and hashmap which stores all weblinks and hitcount
  1. convertUTCSecondsToDate --> converter function to convert from seconds to UTC date.
  1. buildDateObj --> dateClass builder function from the date String
  1. hitCountComparator --> the comparator used by ProrityQueue to sort the weblinks in descending order based on hitCount
  1. displayData --> the function to display the data based on ascending order dates and with dates descending order weblink and hitcount 
  1. parseLine --> parser function to parse a line read from the given input file
  1. readFile --> reader function read lines from a given input file
  1. main  --> main function from where the program starts

 # Complexity analysis
   While reading the data from file
