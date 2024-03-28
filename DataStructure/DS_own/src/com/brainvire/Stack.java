package com.brainvire;

public class Stack {
	int stack[];

	int top = -1;
	int size = 0;

	Stack(int size) {
		this.size = size;
		stack = new int[size];
	}

	public void push(int data) {
		if (top == size) {
			System.out.println("Stack is Full");
			return;
		}

		stack[++top] = data;

	}

	public void pop() {
		if (top == 0) {
			System.out.println("No Element are Found");
		}

		int temp = stack[top];
		top--;
//		return stack[--top];
	}
	
	public void peek() {
		System.out.println(stack[top]);
	}

	public void displayStack() {
		for (int i = 0; i <= top; i++) {
			System.out.println("Stack[ " + i + " ] = " + stack[i]);
		}
	}

	public static void main(String args[]) {
		Stack obj = new Stack(10);
		obj.push(1);
		obj.push(2);
		obj.push(3);
		obj.push(4);
		obj.push(5);
		obj.push(6);

		obj.pop();
		obj.peek();
		obj.displayStack();
	}
}
