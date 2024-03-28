public class LinkListDemo {
	public static void main(String[] args) {
		//insert the first
		Node e1 = new Node(1);
		
		if(head==null) {
			e1.next = head;
			head = e1;
			tail = head; 
		} 
		size++;
		
		
		Node temp;
		temp = head;
		while(temp.next!=null) {
			System.out.println("data -> " + temp.val + " -> ");
			temp=temp.next;
		}
	}
}
