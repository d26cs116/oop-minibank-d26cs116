import java.util.EmptyStackException;

// Generic Stack implementation backed by an internal array
class ArrayStack<T> {
    private Object[] elements;
    private int top;
    private int capacity;

    // Suppress unchecked warning because internal array stores only type T elements
    @SuppressWarnings("unchecked")
    public ArrayStack(int initialCapacity) {
        this.capacity = initialCapacity;
        this.elements = new Object[initialCapacity];
        this.top = -1; // -1 indicates empty stack
    }

    public ArrayStack() {
        this(5); // default capacity of 5
    }

    // Pushes an item onto the stack; expands array capacity if full
    public void push(T item) {
        if (top == capacity - 1) {
            resize(capacity * 2); // Dynamic resizing when full
        }
        elements[++top] = item;
    }

    // Pops and returns the top item; throws EmptyStackException if empty
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            // Sensible empty case handling using standard EmptyStackException
            throw new EmptyStackException();
        }
        T item = (T) elements[top];
        elements[top] = null; // Prevent memory leak by clearing reference
        top--;
        return item;
    }

    // Peeks at top item without removing it
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return (T) elements[top];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public int size() {
        return top + 1;
    }

    // Helper method to dynamically resize the backing array
    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];
        System.arraycopy(elements, 0, newArray, 0, top + 1);
        elements = newArray;
        this.capacity = newCapacity;
    }
}

public class GenericStackDemo {
    public static void main(String[] args) {
        System.out.println("=== Generic Array-Backed Stack (ArrayStack<T>) ===");

        // --- 1. Reusing ArrayStack for String Type (Action Undo Stack) ---
        System.out.println("\n--- 1. Testing ArrayStack<String> (Undo Action History) ---");
        ArrayStack<String> undoStack = new ArrayStack<>(3);

        System.out.println("Pushing actions: 'Type Chapter 1', 'Insert Figure', 'Apply Header'");
        undoStack.push("Type Chapter 1");
        undoStack.push("Insert Figure");
        undoStack.push("Apply Header");
        System.out.println("Current Stack Size : " + undoStack.size());
        System.out.println("Top Action (peek)  : " + undoStack.peek());

        System.out.println("\nPopping actions (LIFO order):");
        while (!undoStack.isEmpty()) {
            System.out.println("  Popped: " + undoStack.pop());
        }
        System.out.println("Stack isEmpty after popping all: " + undoStack.isEmpty());

        // --- 2. Reusing ArrayStack for Integer Type (Operand Stack) ---
        System.out.println("\n--- 2. Testing ArrayStack<Integer> (Calculator Operands) ---");
        ArrayStack<Integer> operandStack = new ArrayStack<>(3);

        System.out.println("Pushing operands: 15, 30, 45, 60 (triggers dynamic resize)");
        operandStack.push(15);
        operandStack.push(30);
        operandStack.push(45);
        operandStack.push(60); // Resizes capacity from 3 to 6
        System.out.println("Stack Size after 4 pushes: " + operandStack.size());

        System.out.println("Popping 2 operands:");
        System.out.println("  Popped: " + operandStack.pop());
        System.out.println("  Popped: " + operandStack.pop());
        System.out.println("Remaining top item: " + operandStack.peek());

        // --- 3. Sensible Handling of Empty Stack Condition ---
        System.out.println("\n--- 3. Demonstrating Empty Stack Handling ---");
        ArrayStack<Double> emptyStack = new ArrayStack<>();
        System.out.println("Created fresh emptyStack<Double>. Calling pop() on empty stack:");
        try {
            emptyStack.pop();
        } catch (EmptyStackException e) {
            System.out.println("  Caught expected EmptyStackException: Stack is empty, cannot pop element.");
        }
    }
}
