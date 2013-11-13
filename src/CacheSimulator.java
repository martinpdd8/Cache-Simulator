
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * CS3012 - Tutorial 5
 * @author martinpdd8@gmail.com
 * 10 Oct 2012
 */
public class CacheSimulator {
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		try {
			FileInputStream fstream = new FileInputStream("instructions.txt");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String address;		
			int count = 0, L = 16, N = 4, K = 2;
			LinkedList<Integer>[] cache = new LinkedList[N]; // Array of linked lists
			for (int i = 0; i < cache.length; i++) {
				cache[i] = new LinkedList<Integer>();
				for(int j =0; j < K; j++) cache[i].add(j, -1); // Initialise all to -1
			}
			while ((address = br.readLine()) != null){ // Read instructions from file line by line
				int a = Integer.parseInt(address, 16);
				int setNumber, tag, offsetSize, setSize; // This section gets these from the address
				offsetSize = log2(L);
				setSize = log2(N);
				setNumber = (a >> offsetSize);
				int t = 0;
				int mask = 1;
				while (t < setSize -1){
					mask <<= 1;
					mask++;
					t++;
				}
				tag = a >> offsetSize + setSize;			
				if (N == 1) setNumber = 0;
				else setNumber &= mask;
				if(!cache[setNumber].contains(tag)){ // If tag not on cache
					print(address + " miss");
					cache[setNumber].removeLast(); // Remove last/oldest 
					cache[setNumber].addFirst(tag); // Add tag to start/newest
				}
				else {
					print(address + " hit");
					int temp = cache[setNumber].indexOf(tag); // Get index of success tag
					int temp2 = cache[setNumber].remove(temp); // Remove success tag
					cache[setNumber].addFirst(temp2); // Moves success tag to front, now the newest
					count++; 
				}
			}
		in.close();
		print("Number of hits: " + count);
		}
		catch (Exception e){ // Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		}
	}
	public static void print(String stuff){
		System.out.println(stuff);
	}
	public static int log2(int n){
		return (int)(Math.log(n) / Math.log(2));
	}
}
