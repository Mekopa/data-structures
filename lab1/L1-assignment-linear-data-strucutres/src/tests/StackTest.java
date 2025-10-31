/**
 * Unit tests for Stack implementations
 * Tests both ArrayStack and LinkedListStack with simple Integer type
 * This focuses on correctness of Stack operations (LIFO behavior)
 *
 * For integration tests with Car domain model, see ManualTest.java
 *
 * @author Mekopa
 */
package tests;

import java.util.Locale;
import util.Ks;
import util.Stack;
import util.ArrayStack;
import util.LinkedListStack;

public class StackTest {

    /**
     * Test basic Stack operations with Integer type
     * Verifies LIFO behavior, isEmpty, peek, and edge cases
     */
    void testBasicStackOperations() {
        Ks.oun("\n========================================");
        Ks.oun("  STACK UNIT TESTS (Integer)");
        Ks.oun("========================================\n");

        // Test ArrayStack
        Ks.oun("--- Testing ArrayStack<Integer> ---\n");
        testSingleStackImplementation(new ArrayStack<>());

        // Test LinkedListStack
        Ks.oun("\n--- Testing LinkedListStack<Integer> ---\n");
        testSingleStackImplementation(new LinkedListStack<>());

        Ks.oun("\n✓✓✓ ALL STACK UNIT TESTS PASSED ✓✓✓\n");
    }

    /**
     * Tests a single Stack implementation with comprehensive test cases
     *
     * @param stack the Stack implementation to test (ArrayStack or LinkedListStack)
     */
    private void testSingleStackImplementation(Stack<Integer> stack) {
        String className = stack.getClass().getSimpleName();
        Ks.oun("Testing: " + className);

        // Test 1: isEmpty on empty stack
        boolean emptyResult = stack.isEmpty();
        Ks.oun("1. Empty stack isEmpty(): " + emptyResult);
        if (!emptyResult) {
            Ks.oun("   ✗ FAILED: Expected true, got false");
        }

        // Test 2: Push 5 integers
        Ks.oun("\n2. Pushing 5 integers (10, 20, 30, 40, 50):");
        for (int i = 1; i <= 5; i++) {
            int value = i * 10;
            stack.push(value);
            Ks.oun("   Pushed: " + value);
        }

        // Test 3: isEmpty on non-empty stack
        boolean notEmptyResult = stack.isEmpty();
        Ks.oun("\n3. After pushes, isEmpty(): " + notEmptyResult);
        if (notEmptyResult) {
            Ks.oun("   ✗ FAILED: Expected false, got true");
        }

        // Test 4: Peek (view top without removing)
        Integer peekResult = stack.peek();
        Ks.oun("\n4. Peek (should be 50): " + peekResult);
        if (peekResult == null || peekResult != 50) {
            Ks.oun("   ✗ FAILED: Expected 50, got " + peekResult);
        }

        // Test 5: Pop all and verify LIFO order
        Ks.oun("\n5. Popping all elements (LIFO order):");
        int expectedValue = 50;
        while (!stack.isEmpty()) {
            Integer popped = stack.pop();
            Ks.oun("   Popped: " + popped);
            if (popped == null || popped != expectedValue) {
                Ks.oun("   ✗ FAILED: Expected " + expectedValue + ", got " + popped);
            }
            expectedValue -= 10;
        }

        // Test 6: isEmpty after popping all
        boolean emptyAfterPop = stack.isEmpty();
        Ks.oun("\n6. Stack empty after popping all: " + emptyAfterPop);
        if (!emptyAfterPop) {
            Ks.oun("   ✗ FAILED: Expected true, got false");
        }

        // Test 7: Edge cases - pop from empty
        Integer popEmpty = stack.pop();
        Ks.oun("\n7. Pop from empty stack: " + popEmpty);
        if (popEmpty != null) {
            Ks.oun("   ✗ FAILED: Expected null, got " + popEmpty);
        }

        // Test 8: Edge cases - peek from empty
        Integer peekEmpty = stack.peek();
        Ks.oun("8. Peek from empty stack: " + peekEmpty);
        if (peekEmpty != null) {
            Ks.oun("   ✗ FAILED: Expected null, got " + peekEmpty);
        }

        // Test 9: Push after emptying
        Ks.oun("\n9. Push after emptying:");
        stack.push(100);
        stack.push(200);
        Ks.oun("   Pushed: 100, 200");
        Integer peek100 = stack.peek();
        Ks.oun("   Peek: " + peek100);
        if (peek100 == null || peek100 != 200) {
            Ks.oun("   ✗ FAILED: Expected 200, got " + peek100);
        }

        // Test 10: Verify LIFO after re-push
        Ks.oun("\n10. Pop after re-push:");
        Integer pop200 = stack.pop();
        Integer pop100 = stack.pop();
        Ks.oun("   Popped: " + pop200 + ", " + pop100);
        if (pop200 == null || pop200 != 200 || pop100 == null || pop100 != 100) {
            Ks.oun("   ✗ FAILED: Expected 200, 100 but got " + pop200 + ", " + pop100);
        }

        Ks.oun("\n✓ " + className + " passed all tests");
    }

    /**
     * Main execution method
     */
    void execute() {
        testBasicStackOperations();
    }

    /**
     * Entry point
     */
    public static void main(String... args) {
        // Unify number formats according to LT locale
        Locale.setDefault(new Locale("LT"));
        new StackTest().execute();
    }
}
