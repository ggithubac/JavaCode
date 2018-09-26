/**
 * Problem: You’re given an input file. Each line consists of a timestamp (unix epoch in seconds) and
 * a url separated by ‘|’ (pipe operator). The entries are not in any chronological order. 
 * Your task is to produce a daily summarized report on url hit count, organized daily (mm/dd/yyyy GMT) 
 * with the earliest date appearing first. For each day, you should display the number of times each url 
 * is visited in the order of highest hit count to lowest count. Your program should take in one command 
 * line argument: input file name. The output should be printed to stdout. You can assume that the cardinality 
 * (i.e. number of distinct values) of hit count values and the number of days are much smaller than the number 
 * of unique URLs. You may also assume that number of unique URLs can fit in memory, but not necessarily the entire 
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


/**
 * @author gunna04
 *
 */
public class webPageHits {
	
	class webLinkHitCounts {
		private Map<String, Integer> webLinkHitCount;

		public webLinkHitCounts() {
			super();
			this.webLinkHitCount = new HashMap<String, Integer>();
		}

		public Map<String, Integer> getWebLinkHitCount() {
			return webLinkHitCount;
		}

		public void setWebLinkHitCount(Map<String, Integer> webLinkHitCount) {
			this.webLinkHitCount = webLinkHitCount;
		}
	}
	
	class webLinkHitCount {
		String webLink = null;
		Integer hitCount = 0;
		
		public webLinkHitCount(String webLink, Integer hitCount) {
			super();
			this.webLink = webLink;
			this.hitCount = hitCount;
		}
		
		public String getWebLink() {
			return webLink;
		}
		public void setWebLink(String webLink) {
			this.webLink = webLink;
		}
		
		public Integer getHitCount() {
			return hitCount;
		}
		public void setHitCount(Integer hitCount) {
			this.hitCount = hitCount;
		}
	}
	
	class dateClass {
		Integer day = 0;
		Integer month = 0;
		Integer year = 0;
		
		public dateClass(Integer day, Integer month, Integer year) {
			super();
			this.day = day;
			this.month = month;
			this.year = year;
		}
		
		public Integer getDay() {
			return day;
		}
		public void setDay(Integer day) {
			this.day = day;
		}
		public Integer getMonth() {
			return month;
		}
		public void setMonth(Integer month) {
			this.month = month;
		}
		public Integer getYear() {
			return year;
		}
		public void setYear(Integer year) {
			this.year = year;
		}
	}
	
	class dateComp implements Comparator<dateClass> {
        public int compare(dateClass paramT1, dateClass paramT2) {
            if (paramT1.getYear().compareTo(paramT2.getYear()) < 0)
                return -1;
            else if (paramT1.getYear().compareTo(paramT2.getYear()) > 0)
                return 1;
            else {
            	if (paramT1.getMonth().compareTo(paramT2.getMonth()) < 0)
                    return -1;
                else if (paramT1.getMonth().compareTo(paramT2.getMonth()) > 0)
                    return 1;
                else {
                	if (paramT1.getDay().compareTo(paramT2.getDay()) < 0)
                        return -1;
                    else if (paramT1.getDay().compareTo(paramT2.getDay()) > 0)
                        return 1;
                    else 
                        return 0;
                }
            }
        }
    }
	
	class treeSetDate {
		SortedSet<dateClass> treeSet = new TreeSet<dateClass>(new dateComp());
	}
	
	class hitCountComp implements Comparator<Integer> {
        public int compare(Integer paramT1, Integer paramT2) {
            if (paramT1.compareTo(paramT2) > 0)
                return -1;
            else if (paramT1.compareTo(paramT2) < 0)
                return 1;
            else
                return 0;
        }
    }
	
	class treeMapHitCount {
		SortedMap<Integer, String> treeMap = new TreeMap<Integer, String>(new hitCountComp());
	}
	
	private SortedMap<dateClass, webLinkHitCounts> displayData = new TreeMap<dateClass, webLinkHitCounts> (new dateComp());
			
	String convertUTCSecondsToDate(long seconds) {
		LocalDateTime dateTime = LocalDateTime.ofEpochSecond(seconds, 0, ZoneOffset.UTC);
		int day = dateTime.getDayOfMonth();
		int month = dateTime.getMonthValue();
		int year = dateTime.getYear();

		String date = new String(month + "/" + day + "/" + year);
		
		return date;
	}
	
	dateClass buildDateObj(String date) {
		
		Integer day = 0, month = 0 , year = 0;
		String delimit = "[/]";
		String[] tokens = date.split(delimit);
		month = Integer.parseInt(tokens[0]);
		day = Integer.parseInt(tokens[1]);
		year = Integer.parseInt(tokens[2]);
		
		dateClass dClass = new dateClass(day, month, year);
		
		return dClass;
		
	}
	
	class hitCountComparator implements Comparator<webLinkHitCount> {
		public int compare(webLinkHitCount s1, webLinkHitCount s2) {
			if (s1.getHitCount() < s2.getHitCount()) {
				return 1;
			} else if (s1.getHitCount() > s2.getHitCount()) {
				return -1;
			}
			return 0;
		}
	}
	
	void displayData() {
		Map<dateClass, webLinkHitCounts> dD = displayData;
		Set<dateClass> dateSet = dD.keySet();
		Iterator it = dateSet.iterator();
 		
 		while (it.hasNext()) {
 			dateClass dClass = (dateClass)it.next();

			String date = new String(String.format("%02d", dClass.getMonth())+ "/" + String.format("%02d", dClass.getDay()) + "/" + dClass.getYear() + " GMT");
			System.out.println(date);
			
			webLinkHitCounts obj = dD.get(dClass);
			Map<String, Integer> webLHC = obj.getWebLinkHitCount();
			
			Iterator itW = webLHC.entrySet().iterator();
			
			PriorityQueue<webLinkHitCount> pq = new PriorityQueue<webLinkHitCount>(5, new hitCountComparator()); 
			while (itW.hasNext()) {
				Map.Entry pR = (Map.Entry)itW.next();
				webLinkHitCount wLHC = new webLinkHitCount((String)pR.getKey(), (Integer)pR.getValue());
				pq.add(wLHC);
			}
			
			while (!pq.isEmpty()) {
				webLinkHitCount wLink = pq.poll();
				System.out.println(" " + wLink.getWebLink() + " " + wLink.getHitCount());
			}
		}
	}
	
	void parseLine(String line) {
		String delimit = "[|]";
		String[] tokens = line.split(delimit);
		
		long seconds = Integer.parseInt(tokens[0]);
		String weblink = tokens[1];
		
		String date = convertUTCSecondsToDate(seconds);
		
		dateClass dateObj = buildDateObj(date);
		
		webLinkHitCounts webLinkCount = displayData.get(dateObj);
		
		if (webLinkCount == null) {
			//No Entry for this key, create it and insert the entry
			webLinkCount = new webLinkHitCounts();
			webLinkCount.webLinkHitCount.put(weblink, 1);
			displayData.put(dateObj, webLinkCount);
			
		} else {
			Integer val = webLinkCount.webLinkHitCount.get(weblink);
			if (val == null) {
				val = 0;
			}
			webLinkCount.getWebLinkHitCount().put(weblink, ++val);
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
		
		webPageHits wPHits = new webPageHits();
		
		if (args.length < 1) {
			System.out.println("Please provide a input file in the command line args");
			return;
		}
		
		String fileName = args[0]; 
		
		//System.out.println("FileName" + fileName);
		
		//wPHits.readFile("/Users/gunna04/Git-Repo/WebPageHits/src/main/resources/input.txt");
		
		wPHits.readFile(fileName);
		
		wPHits.displayData();
	}
}
