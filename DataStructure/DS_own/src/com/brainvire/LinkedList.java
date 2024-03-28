package com.brainvire;

public class LinkedList {
	Node head;
	Node tail;
	int size;

	public LinkedList() {
		this.size = 0;
	}

	private class Node {
		int data;
		Node next;

		public Node(int data) {
			this.data = data;
		}

		public Node(int data, Node next) {
			this.data = data;
			this.next = null;
		}
	}

	public int insertNodeFirst(int data) {
		Node newNode = new Node(data);
		newNode.next = head;
		head = newNode;

		if (tail == null) {
			tail = head;
		}

		size++;
		return data;
	}

	public int insertNodeLast(int data) {
		Node newNode = new Node(data);

		if (tail == null) {
			insertNodeFirst(data);
		}

		Node temp = tail;
		temp.next = newNode;
		tail = newNode;
		size++;
		return data;
	}

	public int insertNode(int data) {
		Node newNode = new Node(data);

		if (head == null) {
			insertNodeFirst(data);
		}

		Node temp = head;
		Node prev = head;
		while (temp != null) {

			if (temp.data < data) {
				prev.next = newNode;
				newNode.next = temp;
				break;
			}
			prev = temp;
			System.out.println("prev : " + prev.data);
			temp = temp.next;
		}
		size++;
		return data;
	}

	public void deleteFirstNode() {
		if (head == null) {
			return;
		}

		if (head.next == null) {
			head = null;
		}

		Node temp = head;
		head = head.next;
		size--;
	}

	public void deleteLastNode() {
		System.out.println(tail.data);

		if (tail == null) {
			return;
		}

		if (head == tail) {
			head = null;
			tail = null;
		}

		Node temp = head;
		Node prev = head;
		while (temp.next != null) {
			prev = temp;
			temp = temp.next;
		}

		System.out.println("prev : " + prev.data);
		prev.next = null;
	}

	public void printLinkList() {
		Node temp = head;
		while (temp != null) {
			System.out.print(temp.data + " -> ");
			temp = temp.next;
		}

		System.out.println(" NULL");
	}

	public static void main(String args[]) {
		LinkedList obj = new LinkedList();
		obj.insertNodeFirst(3);
		obj.insertNodeFirst(4);
		obj.insertNodeFirst(11);
		obj.insertNodeFirst(12);
		obj.insertNodeLast(2);
		obj.insertNodeLast(1);

		obj.insertNode(8);

		obj.deleteFirstNode();
		obj.deleteLastNode();

		obj.printLinkList();
	}
}
