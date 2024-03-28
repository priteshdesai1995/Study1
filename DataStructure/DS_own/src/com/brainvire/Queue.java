package com.brainvire;

public class Queue {
	int queue[];
	int front = -1;
	int rear = -1;
	int size;

	public Queue(int size) {
		this.size = size;
		queue = new int[size];
	}

	public void addElement(int item) {
		if (front == -1 && rear == -1) {
			queue[++front] = item;
			rear = front;
		} else {
			queue[++front] = item;
//			rear++;
		}

		System.out.println("Front : " + front + " rear : " + rear);
	}

	public void deleteElement() {
		if (rear == -1) {
			System.out.println("No Element are Found");
			return;
		}

		int temp = queue[rear];
		
		for (int i = 1; i <= front; i++) {
			queue[i-1] = queue[i];
		}
		System.out.println("Delete Element is : " + temp);
		front--;

		System.out.println("Front : " + front + " rear : " + rear);
	}

	public void displayQueue() {
		for (int i = 0; i <= front; i++) {
			System.out.println("Stack[ " + i + " ] = " + queue[i]);
		}
	}

	public static void main(String args[]) {
		Queue queue = new Queue(10);
		queue.addElement(1);
		queue.addElement(2);
		queue.addElement(3);
		queue.addElement(4);
		queue.addElement(5);

		queue.deleteElement();
		queue.displayQueue();
	}
}
