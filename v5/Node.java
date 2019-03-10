package v5;

public class Node {
	private Point data;
	private Node next;
	
	public Node()
	{
		data = null;
		next = null;
	}
	
	public Node(Point data)
	{
		this.data = data;
		next = null;
	}
	
	public Node(Node next)
	{
		this.data = null;
		this.next = next;
	}
	
	public Node(Point data, Node next)
	{
		this.data = data;
		this.next = next;
	}
	
	/** End Constructors **/
	
	
	/** Getters and Setters **/
	public void setData(Point p)
	{
		this.data = p;
	}
	
	public Point getData()
	{
		return this.data;
	}
	
	public void setNext(Node n)
	{
		this.next = n;
	}
	
	public Node getNext()
	{
		return this.next;
	}
}
