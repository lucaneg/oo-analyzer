class ArrayLoop {

	void main() {
	    Object[] arr = new Object[10];
	    arr[0] = new Object[10];
	
	    while (arr[0] != null)
	      arr = arr[0] as Object[];
	}
}