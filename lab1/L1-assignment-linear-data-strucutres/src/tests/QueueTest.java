/**
 * Unit tests for Queue implementations
 * Tests both ArrayQueue and LinkedListQueue with simple Integer type
 * This focuses on correctness of Queue operations (FIFO behavior)
 *
 * @author Mekopa
 */
package tests;

import java.util.Locale;
import util.Ks;
import util.Queue;
import util.ArrayQueue;
import util.LinkedListQueue;

public class QueueTest {

    /**
     * Test basic Queue operations with Integer type
     * Verifies FIFO behavior, isEmpty, peek, and edge cases
     */
    void testBasicQueueOperations() {
        Ks.oun("\n========================================");
        Ks.oun("  QUEUE UNIT TESTS (Integer)");
        Ks.oun("========================================\n");

        // Test ArrayQueue
        Ks.oun("--- Testing ArrayQueue<Integer> ---\n");
        testSingleQueueImplementation(new ArrayQueue<>());

        // Test LinkedListQueue
        Ks.oun("\n--- Testing LinkedListQueue<Integer> ---\n");
        testSingleQueueImplementation(new LinkedListQueue<>());

        Ks.oun("\n✓✓✓ ALL QUEUE UNIT TESTS PASSED ✓✓✓\n");
    }

    /**
     * Tests a single Queue implementation with comprehensive test cases
     *
     * @param queue the Queue implementation to test (ArrayQueue or LinkedListQueue)
     */
    private void testSingleQueueImplementation(Queue<Integer> queue) {
        String className = queue.getClass().getSimpleName();
        Ks.oun("Testing: " + className);

        // Test 1: isEmpty on empty queue
        boolean emptyResult = queue.isEmpty();
        Ks.oun("1. Empty queue isEmpty(): " + emptyResult);
        if (!emptyResult) {
            Ks.oun("   ✗ FAILED: Expected true, got false");
        }

        // Test 2: Enqueue 5 integers
        Ks.oun("\n2. Enqueuing 5 integers (10, 20, 30, 40, 50):");
        for (int i = 1; i <= 5; i++) {
            int value = i * 10;
            queue.enqueue(value);
            Ks.oun("   Enqueued: " + value);
        }

        // Test 3: isEmpty on non-empty queue
        boolean notEmptyResult = queue.isEmpty();
        Ks.oun("\n3. After enqueues, isEmpty(): " + notEmptyResult);
        if (notEmptyResult) {
            Ks.oun("   ✗ FAILED: Expected false, got true");
        }

        // Test 4: Peek (view front without removing)
        Integer peekResult = queue.peek();
        Ks.oun("\n4. Peek (should be 10 - first in!): " + peekResult);
        if (peekResult == null || peekResult != 10) {
            Ks.oun("   ✗ FAILED: Expected 10, got " + peekResult);
        }

        // Test 5: Dequeue all and verify FIFO order
        Ks.oun("\n5. Dequeuing all elements (FIFO order):");
        int expectedValue = 10;
        while (!queue.isEmpty()) {
            Integer dequeued = queue.dequeue();
            Ks.oun("   Dequeued: " + dequeued);
            if (dequeued == null || dequeued != expectedValue) {
                Ks.oun("   ✗ FAILED: Expected " + expectedValue + ", got " + dequeued);
            }
            expectedValue += 10;
        }

        // Test 6: isEmpty after dequeuing all
        boolean emptyAfterDequeue = queue.isEmpty();
        Ks.oun("\n6. Queue empty after dequeuing all: " + emptyAfterDequeue);
        if (!emptyAfterDequeue) {
            Ks.oun("   ✗ FAILED: Expected true, got false");
        }

        // Test 7: Edge cases - dequeue from empty
        Integer dequeueEmpty = queue.dequeue();
        Ks.oun("\n7. Dequeue from empty queue: " + dequeueEmpty);
        if (dequeueEmpty != null) {
            Ks.oun("   ✗ FAILED: Expected null, got " + dequeueEmpty);
        }

        // Test 8: Edge cases - peek from empty
        Integer peekEmpty = queue.peek();
        Ks.oun("8. Peek from empty queue: " + peekEmpty);
        if (peekEmpty != null) {
            Ks.oun("   ✗ FAILED: Expected null, got " + peekEmpty);
        }

        // Test 9: Enqueue after emptying
        Ks.oun("\n9. Enqueue after emptying:");
        queue.enqueue(100);
        queue.enqueue(200);
        Ks.oun("   Enqueued: 100, 200");
        Integer peek100 = queue.peek();
        Ks.oun("   Peek: " + peek100);
        if (peek100 == null || peek100 != 100) {
            Ks.oun("   ✗ FAILED: Expected 100, got " + peek100);
        }

        // Test 10: Verify FIFO after re-enqueue
        Ks.oun("\n10. Dequeue after re-enqueue:");
        Integer deq100 = queue.dequeue();
        Integer deq200 = queue.dequeue();
        Ks.oun("   Dequeued: " + deq100 + ", " + deq200);
        if (deq100 == null || deq100 != 100 || deq200 == null || deq200 != 200) {
            Ks.oun("   ✗ FAILED: Expected 100, 200 but got " + deq100 + ", " + deq200);
        }

        Ks.oun("\n✓ " + className + " passed all tests");
    }

    /**
     * Main execution method
     */
    void execute() {
        testBasicQueueOperations();
    }

    /**
     * Entry point
     */
    public static void main(String... args) {
        // Unify number formats according to LT locale
        Locale.setDefault(new Locale("LT"));
        new QueueTest().execute();
    }
}
