/**
 * Problem: You’re given an input file. Each line consists of a timestamp (unix epoch in seconds) and
 * a url separated by ‘|’ (pipe operator). The entries are not in any chronological order. 
 * Your task is to produce a daily summarized report on url hit count, organized daily (mm/dd/yyyy GMT) 
 * with the earliest date appearing first. For each day, you should display the number of times each url 
 * is visited in the order of highest hit count to lowest count. Your program should take in one command 
 * line argument: input file name. The output should be printed to stdout. You can assume that the cardinality 
 * (i.e. number of distinct values) of hit count values and the number of days are much smaller than the number 
 * of unique URLs. You may also assume that number of unique URLs can 	y, but not necessarily the entire 
 * file.
 * 
 * input.txt
 * 1407564301|www.nba.com
 * 1407478021|www.facebook.com
 * 1407478022|www.facebook.com
 * 1407481200|news.ycombinator.com
 * 1407478028|www.google.com
 * 1407564301|sports.yahoo.com
 * 1407564300|www.cnn.com
 * 1407564300|www.nba.com
 * 1407564300|www.nba.com
 * 1407564301|sports.yahoo.com
 * 1407478022|www.google.com
 * 1407648022|www.twitter.com
 * 
 * Output
 * 08/08/2014 GMT
 * www.facebook.com 2
 * www.google.com 2
 * news.ycombinator.com 1
 * 08/09/2014 GMT
 * www.nba.com 3
 * sports.yahoo.com 2
 * www.cnn.com 1
 * 08/10/2014 GMT
 * www.twitter.com 1
 * 
 * Correctness, efficiency (speed and memory) and code cleanliness will be evaluated. 
 * Please provide a complexity analysis in Big-O notation for your program along with your source. 
 */


package webPageHits;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * @author gunna04
 *
 */
public class webPageHitsMoreSpaceLessTime {
	
	class dateComp implements Comparator<String> {
        public int compare(String paramT1, String paramT2) {
        	
        	if (paramT1.compareTo(paramT2) < 0) {
        		return -1;
        	} else if (paramT1.compareTo(paramT2) > 0) {
        		return 1;
        	} else
        		return 0;
       }
    }

	private SortedMap<String, webLinkHitCounts> displayData = new TreeMap<String, webLinkHitCounts> (new dateComp());
			
	String convertUTCSecondsToDate(long seconds) {
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
		String date = dateTime.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		
		return date;
	}
	
	class hitCountComparator implements Comparator<Integer> {
		public int compare(Integer i1, Integer i2) {
			if (i1.intValue() < i2.intValue()) {
				return 1;
			} else if (i1.intValue() > i2.intValue()) {
				return -1;
			}
			return 0;
		}
	}
	
	class webLinkHitCounts {
		private SortedMap<Integer, TreeSet<String>> webLinkHitCount;
		
		private HashMap<String, Integer> webLinkHitCountHashMap;

		public webLinkHitCounts() {
			super();
			this.webLinkHitCount = new TreeMap<Integer, TreeSet<String>> (new hitCountComparator());
			this.webLinkHitCountHashMap = new HashMap<String, Integer> ();
		}

		public Map<Integer, TreeSet<String>> getWebLinkHitCount() {
			return webLinkHitCount;
		}

		public void setWebLinkHitCount(SortedMap<Integer, TreeSet<String>> webLinkHitCount) {
			this.webLinkHitCount = webLinkHitCount;
		}
		
		public HashMap<String, Integer> getWebLinkHitCountHashMap() {
			return webLinkHitCountHashMap;
		}

		public void setWebLinkHitCountHashMap(HashMap<String, Integer> webLinkHitCountHashMap) {
			this.webLinkHitCountHashMap = webLinkHitCountHashMap;
		}
	}
	
	void displayData() {
		Map<String, webLinkHitCounts> dD = displayData;
		Set<String> dateSet = dD.keySet();
		Iterator it = dateSet.iterator();
 		
 		while (it.hasNext()) {
 			String dDate = (String)it.next();
 			
 			String delimit = "[/]";
 			String[] tokens = dDate.split(delimit);

			String date = new String(String.format("%02d", Integer.parseInt(tokens[0])) + "/" + String.format("%02d", Integer.parseInt(tokens[1])) + "/" + Integer.parseInt(tokens[2]) + " GMT");
			System.out.println(date);
			
			webLinkHitCounts obj = dD.get(dDate);
			Map<Integer, TreeSet<String>> webLHC = obj.getWebLinkHitCount();

			Set<Integer> sHitCount = webLHC.keySet();
			
			for (Integer val:sHitCount) {
				TreeSet<String> lWebLink = webLHC.get(val);
				for (String weblink:lWebLink) {
					System.out.println(" " + weblink.toString() + " " + val.intValue());
				}
			}
		}
	}
	
	void parseLine(String line) {
		String delimit = "[|]";
		String[] tokens = line.split(delimit);
		
		long seconds = Integer.parseInt(tokens[0]);
		String webLink = tokens[1];
		
		String date = convertUTCSecondsToDate(seconds);
		
		webLinkHitCounts webLinkCount = displayData.get(date);
		
		if (webLinkCount == null) {
			//No Entry for this key, create it and insert the entry
			webLinkCount = new webLinkHitCounts();
			TreeSet<String> lString = new TreeSet<String> ();
			lString.add(webLink);
			webLinkCount.webLinkHitCount.put(1, lString);
			displayData.put(date, webLinkCount);
			
		} else {
			Integer currentHitCount = webLinkCount.getWebLinkHitCountHashMap().get(webLink);
			Integer keyPlus = null;
			
			if (currentHitCount == null) {
				//webLink doesn't exists insert into hitcount 1 bucket
				keyPlus = 1;
				
			} else {
				//remove from current treeset
				TreeSet<String> sString = webLinkCount.getWebLinkHitCount().get(currentHitCount);
				sString.remove(webLink);
				
				keyPlus = currentHitCount.intValue() + 1;
			}
			
			TreeSet<String> lString = webLinkCount.getWebLinkHitCount().get(keyPlus);
			if (lString == null) {
				//this key doesn't exists
				lString = new TreeSet<String> ();
				lString.add(webLink);
				webLinkCount.webLinkHitCount.put(keyPlus, lString);
			} else {
				lString.add(webLink);
			}
			
			webLinkCount.getWebLinkHitCountHashMap().put(webLink, keyPlus);
		}
	}
	
	void readFile(String fileName) {
		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				parseLine(line);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		webPageHitsMoreSpaceLessTime wPHits = new webPageHitsMoreSpaceLessTime();
		
		if (args.length < 1) {
			System.out.println("Please provide a input file in the command line args");
			return;
		}
		
		String fileName = args[0]; 
		
		wPHits.readFile(fileName);
		
		wPHits.displayData();
	}
}
