class LinkListDemo {
	Node head;
	Node tail;
	int size;
	
	public LinkListDemo() {
		this.size=0;
	}
}

class Node{
		int val;
		Node next;
		
		public Node(int val) {
			this.val = val;
		}

		public Node(int val, Node node) {
			this.val = val;
			this.next=node;
		}		
	}

public class LinkList {
	public static void main(String args[]) {
		LinkListDemo ll = new LinkListDemo();
		//insert the first
		Node e1 = new Node(1);
		
		if(ll.head==null) {
			e1.next = ll.head;
			ll.head = e1;
			ll.tail = ll.head; 
		} 
		ll.size++;
		
		
		Node temp;
		temp = ll.head;
		while(temp.next!=null) {
			System.out.println("data -> " + temp.val + " -> ");
			temp=temp.next;
		}
	}
}


