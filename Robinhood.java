package sp7;

/**
 * @author Garima
 *
 * @param <T>
 */
public class Robinhood<T extends Comparable<? super T>> {
	
	private int size;
	private Entry<T>[] arr;
	static final double LOADFACTOR=0.75;
	
	/**
	 * @author Garima
	 *
	 * @param <T>
	 */
	static class Entry<T extends Comparable<? super T>> {
		T element;
		int probe;

		public Entry(T x, int probe) {// element x and level at which the element is inserted
			element = x;
			this.probe = probe;
		}
		public T getElement() {
			return element;
		}
	}
	
	
	public Robinhood() {
		this.size=0;
		this.arr=new Entry[16];
	}
	public int getSize() {
		return this.size;
	}
	public boolean isEmtpy() {
		return this.size == 0;
	}
	public boolean isFull() {
		return this.size == this.arr.length;
	}
	/**
	 * @param x
	 * @return
	 */
	private Integer find(T x) {
		Integer loc=indexFor(hash(x.hashCode()), this.arr.length);
		if(this.arr[loc]==null) {
			return null;
		}
		int dist=0;
		while(this.arr[loc]!=null && this.arr[loc].element.compareTo(x)!=0 && dist<=this.arr[loc].probe) {
			dist++;
			 loc= (loc + 1) % this.arr.length;
		};
		if(this.arr[loc]!=null && this.arr[loc].element.compareTo(x)==0) {
			return loc;
		}
		return null;
	}
	/**
	 * @param x
	 * @return
	 */
	public boolean contains(T x) {
		if(find(x)==null) {
			return false;
		}
		return true;
	}
	
	/**
	 * @param x
	 * @return
	 */
	public boolean add(T x) {
		if(((double)size+1)/this.arr.length>=LOADFACTOR) {
			rehash();
		}
		if(contains(x)) {
			return false;
		}
		int  loc= indexFor(hash(x.hashCode()), this.arr.length);
//		System.out.println(x+"   "+loc);
		int dist=0;
		while(true) {
			if(this.arr[loc]==null) {
				this.arr[loc]=new Entry(x, dist);
				this.size++;
				break;
			}
			else if(dist<=this.arr[loc].probe) {
				dist++;
				loc= (loc + 1) % arr.length;
			}
			else {
				T temp= (T) this.arr[loc].element;
				this.arr[loc].element=x;
				x= temp;
				int tempDist=dist;
				dist=this.arr[loc].probe+1;
				this.arr[loc].probe=tempDist;
				loc= (loc + 1) % this.arr.length;
				
			}
		}
		return true;
	}
	/**
	 * @param x
	 * @return
	 */
	public boolean remove(T x) {
		Integer loc=find(x);
		if(loc==null) {
			return false;
		}
		int nextLoc=0;
		while(this.arr[((loc + 1) % this.arr.length)]!=null  &&this.arr[((loc + 1) % this.arr.length)].probe!=0) {
			nextLoc=(loc + 1) % this.arr.length;
			this.arr[loc].element=this.arr[nextLoc].element;
			this.arr[loc].probe=this.arr[nextLoc].probe-1;
			loc=nextLoc;
		}
		this.arr[loc]=null;
		size--;
		return true;
		
	}
	/**
	 * 
	 */
	private void rehash() {
		int len=this.arr.length;
		Entry<T> oldArr[]=this.arr;
		this.arr=new Entry[len*2];
		this.size=0;
		for(int i=0; i<len; i++) {
			if(oldArr[i]!=null) {
				add(oldArr[i].element);
			}
		}
	}
	static int hash(int h) {
		h ^= (h >>> 20) ^ (h >>> 12);
		return h ^ (h >>> 7) ^ (h >>> 4);
	}

	static int indexFor(int h, int length) { // length = table.length is a power of 2
		return h & (length - 1); 
	}
	static<T extends Comparable<? super T>> int distinctElements(T[ ] arr) { 
		Robinhood<T> hashMap = new Robinhood<T>();
		for(int i=0;i<arr.length;i++) {
			hashMap.add(arr[i]);
		}
		return hashMap.getSize();
	}
}
