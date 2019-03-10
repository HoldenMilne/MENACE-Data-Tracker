package v5;

public class LinkedList {
	Node head;
	Node tail;
	int size;
	public LinkedList()
	{
		head = null;
		tail = null;
		size = 0;
	}
	
	//Always adds to end
	public void Add(Node n) 
	{
		if(tail==null)
		{
			head = n;
			tail = n;
			tail.setNext(null);
		}
		else {
			tail.setNext(n);
			tail=n;
		}

		n.setNext(null);
		size++;
	}
}
