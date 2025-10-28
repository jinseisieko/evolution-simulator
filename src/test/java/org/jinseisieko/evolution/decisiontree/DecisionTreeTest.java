// src/test/java/org/jinseisieko/evolution/decisiontree/DecisionTreeTest.java
package org.jinseisieko.evolution.decisiontree;

import org.jinseisieko.evolution.bindingcomponents.Answerer;
import org.jinseisieko.evolution.bindingcomponents.Question;
import org.jinseisieko.evolution.bindingcomponents.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

import org.jinseisieko.evolution.decisiontree.stubs.EnergyQuestion;
import org.jinseisieko.evolution.decisiontree.stubs.MockStatus;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Unit tests for the {@link DecisionTree} class, which represents a binary
 * decision tree structure with methods for execution and node indexing.
 *
 * @author jinseisieko
 */
class DecisionTreeTest {

    // === Test construction and initialization ===

    /**
     * Verifies that constructing a {@code DecisionTree} with zero depth throws an exception.
     */
    @Test
    void constructorWithZeroDepth_shouldThrowExceptionWithMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            DecisionTree tree = new DecisionTree(0);
            tree.getRoot();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    /**
     * Verifies that constructing a {@code DecisionTree} with root creates rigth tree.
     */
    @Test
    void constructorWithRoot_shouldCreateNewTreeExactly() {
        DecisionTree tree = new DecisionTree(1);
        tree.getRoot().setQuestion(new EnergyQuestion(0.5));
        Node root = tree.getRoot();
        OutcomeNode leftOutcome = (OutcomeNode) root.getLeftSon();
        OutcomeNode rightOutcome = (OutcomeNode) root.getRightSon();
        leftOutcome.setStatus(new MockStatus("1"));
        rightOutcome.setStatus(new MockStatus("2"));
        tree.rebuildIndex();

        OutcomeNode l = (OutcomeNode) tree.getRoot().getLeftSon();
        DecisionTree newTree = new DecisionTree(1, tree.getRoot());
        assertNotEquals(newTree.getRoot().getLeftSon(), l);
        OutcomeNode newL = (OutcomeNode) newTree.getRoot().getLeftSon();
        assertEquals("MockStatus{1}", newL.getStatus().toString());

    }

    /**
     * Verifies that constructing a {@code DecisionTree} with a negative depth throws an exception.
     */
    @Test
    void constructorWithNegativeDepth_shouldThrowExceptionWithMessage() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            DecisionTree tree = new DecisionTree(-1);
            tree.getRoot();
        });
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    /**
     * Verifies that constructing a {@code DecisionTree} with a positive depth creates a tree
     * of the specified depth along all branches (left, right, and a random path).
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20})
    void constructor_shouldCreateCorrectDepth(int depth) {
        DecisionTree tree = new DecisionTree(depth);
        Node node = tree.getRoot();
        int dl = 0;
        while (node.traverse(true) != null) {
            dl++;
            node = node.traverse(true);
        }
        int dr = 0;
        node = tree.getRoot();
        while (node.traverse(false) != null) {
            dr++;
            node = node.traverse(false);
        }
        node = tree.getRoot();
        int dRandom = 0;
        boolean left = Math.random() < 0.5;
        while (node.traverse(left) != null) {
            dRandom++;
            node = node.traverse(left);
            left = Math.random() < 0.5;
        }
        assertEquals(depth, dl);
        assertEquals(depth, dr);
        assertEquals(depth, dRandom);
        assertEquals(depth, tree.getDepth());
    }

    /**
     * Verifies that a {@code DecisionTree} constructed with a positive depth has the correct initial structure.
     */
    @Test
    void constructorWithDepth_shouldCreateCorrectStructure() {
        int depth = 2;
        DecisionTree tree = new DecisionTree(depth);

        // Check root type and initial state
        assertNotNull(tree.getRoot(), "Root node should not be null after construction.");
        assertTrue(tree.getRoot() instanceof RootQuestionNode, "Root node should be an instance of RootQuestionNode.");
        assertFalse(tree.isIndexValid(), "Index should be marked invalid after initial construction with depth.");

        // Manually verify structure for depth 2: Root(Q), L(Q), R(Q), LL(Outcome), LR(Outcome), RL(Outcome), RR(Outcome)
        // Total nodes = 2^(depth+1) - 1 = 2^3 - 1 = 7
        // Total outcome nodes = 2^depth = 4
        // Total internal question nodes = (Total nodes - Outcome nodes) = 7 - 4 = 3 (Root + 2 internal QNs)
        RootQuestionNode root = tree.getRoot();
        assertNotNull(root.getLeftSon());
        assertNotNull(root.getRightSon());
        assertTrue(root.getLeftSon() instanceof QuestionNode);
        assertTrue(root.getRightSon() instanceof QuestionNode);

        // Check left subtree (L)
        QuestionNode leftQN = (QuestionNode) root.getLeftSon();
        assertNotNull(leftQN.getLeftSon()); // LL
        assertNotNull(leftQN.getRightSon()); // LR
        assertTrue(leftQN.getLeftSon() instanceof OutcomeNode);
        assertTrue(leftQN.getRightSon() instanceof OutcomeNode);

        // Check right subtree (R)
        QuestionNode rightQN = (QuestionNode) root.getRightSon();
        assertNotNull(rightQN.getLeftSon()); // RL
        assertNotNull(rightQN.getRightSon()); // RR
        assertTrue(rightQN.getLeftSon() instanceof OutcomeNode);
        assertTrue(rightQN.getRightSon() instanceof OutcomeNode);
    }

    // === Test node indexing ===

    /**
     * Verifies that {@code rebuildIndex} correctly builds the index array and marks it as valid.
     */
    @Test
    void rebuildIndex_shouldBuildIndexAndMarkValid() {
        DecisionTree tree = new DecisionTree(2);
        // Initially, index is invalid
        assertFalse(tree.isIndexValid());

        tree.rebuildIndex();

        // After rebuild, index should be valid
        assertTrue(tree.isIndexValid());
        // The content of the index is tested in getNodeByIndex tests
    }

    /**
     * Verifies that {@code getNodeByIndex} correctly retrieves nodes after the index is rebuilt
     * and handles invalid indices appropriately.
     */
    @Test
    void getNodeByIndex_withValidIndex_shouldReturnCorrectNode() {
        DecisionTree tree = new DecisionTree(2); // Creates a tree like Root(1) -> L(2), R(3) -> LL(4), LR(5), RL(6), RR(7)
        tree.rebuildIndex(); // Build the index

        RootQuestionNode root = tree.getRoot();
        Node leftChild = root.getLeftSon();
        Node rightChild = root.getRightSon();
        Node leftLeftChild = leftChild != null ? leftChild.getLeftSon() : null;
        Node leftRightChild = leftChild != null ? leftChild.getRightSon() : null;
        Node rightLeftChild = rightChild != null ? rightChild.getLeftSon() : null;
        Node rightRightChild = rightChild != null ? rightChild.getRightSon() : null;

        // Test getting nodes by their expected indices
        assertEquals(root, tree.getNodeByIndex(1), "getNodeByIndex(1) should return the root node.");
        assertEquals(leftChild, tree.getNodeByIndex(2), "getNodeByIndex(2) should return the left child of the root.");
        assertEquals(rightChild, tree.getNodeByIndex(3), "getNodeByIndex(3) should return the right child of the root.");
        assertEquals(leftLeftChild, tree.getNodeByIndex(4), "getNodeByIndex(4) should return the left child of node 2.");
        assertEquals(leftRightChild, tree.getNodeByIndex(5), "getNodeByIndex(5) should return the right child of node 2.");
        assertEquals(rightLeftChild, tree.getNodeByIndex(6), "getNodeByIndex(6) should return the left child of node 3.");
        assertEquals(rightRightChild, tree.getNodeByIndex(7), "getNodeByIndex(7) should return the right child of node 3.");

        // Test invalid index (less than 1)
        IllegalArgumentException exZero = assertThrows(IllegalArgumentException.class, () -> tree.getNodeByIndex(0),
                "getNodeByIndex(0) should throw IllegalArgumentException.");
        assertNotNull(exZero.getMessage());
        assertFalse(exZero.getMessage().isBlank());

        IllegalArgumentException exNeg = assertThrows(IllegalArgumentException.class, () -> tree.getNodeByIndex(-1),
                "getNodeByIndex(-1) should throw IllegalArgumentException.");
        assertNotNull(exNeg.getMessage());
        assertFalse(exNeg.getMessage().isBlank());

        // Test valid index that points beyond the tree structure
        assertNull(tree.getNodeByIndex(8), "getNodeByIndex for an index beyond the tree should return null.");
        assertNull(tree.getNodeByIndex(10), "getNodeByIndex for an index beyond the tree should return null.");
    }

    /**
     * Verifies that {@code getNodeByIndex} throws an exception if the index is not up-to-date.
     */
    @Test
    void getNodeByIndex_withoutRebuiltIndex_shouldThrowIllegalStateException() {
        DecisionTree tree = new DecisionTree(1); // Creates a tree, index is invalid initially
        assertFalse(tree.isIndexValid(), "Index should be invalid after construction.");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> tree.getNodeByIndex(1),
                "getNodeByIndex should throw IllegalStateException if index is not up-to-date.");
        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isBlank());
    }

    // === Test tree execution logic ===

    /**
     * Verifies that the {@code apply} method correctly traverses the tree based on answers
     * from the Answerer and returns the Status of the final OutcomeNode.
     * Also checks that {@code apply} throws IllegalArgumentException if the answerer is null.
     */
    @Test
    void apply_shouldTraverseAndReturnOutcomeStatus() {
        DecisionTree tree = new DecisionTree(1); // Creates RootQuestionNode -> OutcomeNode, OutcomeNode
        RootQuestionNode root = tree.getRoot();

        // Manually set up a simple question and outcomes for the tree
        // Using a hypothetical stub question, similar to EnergyQuestion
        Question mockQuestion = new EnergyQuestion(0.5); // Assuming EnergyQuestion implements Question
        root.setQuestion(mockQuestion);

        OutcomeNode leftOutcome = (OutcomeNode) root.getLeftSon();
        OutcomeNode rightOutcome = (OutcomeNode) root.getRightSon();
        Status statusForFalse = new MockStatus("StatusForFalsePath"); // Assuming MockStatus exists
        Status statusForTrue = new MockStatus("StatusForTruePath");

        leftOutcome.setStatus(statusForFalse);
        rightOutcome.setStatus(statusForTrue);

        // Mock Answerer implementations that provide answers based on the question asked
        Answerer answererForFalse = (Question q) -> false; // This means QuestionNode.next will return getLeftSon()

        Answerer answererForTrue = (Question q) -> true; // This means QuestionNode.next will return getRightSon()

        // Execute apply with different answerers
        Status resultFalse = tree.apply(answererForFalse);
        Status resultTrue = tree.apply(answererForTrue);

        // Assert results
        // When answer is false, next(Answerer) on QuestionNode goes to LeftSon (index 2 in our numbering if root is 1).
        // The LeftSon in a depth-1 tree is an OutcomeNode.
        // The traversal path for 'false' is: Root -> Left Outcome Node
        assertEquals(statusForFalse, resultFalse, "Apply with false answerer should return status of left outcome node.");

        // When answer is true, next(Answerer) on QuestionNode goes to RightSon (index 3).
        // The traversal path for 'true' is: Root -> Right Outcome Node
        assertEquals(statusForTrue, resultTrue, "Apply with true answerer should return status of right outcome node.");

        // Verify apply throws IllegalArgumentException on null answerer
        // (Based on OutcomeNode's implementation, apply itself shouldn't throw NPE directly on null answerer
        //  because it calls Node.next(answerer), and QuestionNode.next checks for null answerer).
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class, () -> tree.apply(null));
        assertNotNull(iae.getMessage());
        assertFalse(iae.getMessage().isBlank());
    }

    /**
     * Verifies that the {@code apply} method correctly handles a tree where the root node's question is null.
     * This should result in an IllegalStateException being thrown during traversal.
     */
    @Test
    void apply_withRootQuestionNull_shouldThrowIllegalStateException() {
        DecisionTree tree = new DecisionTree(1); // Creates RootQuestionNode with null question initially

        Answerer mockAnswerer = q -> true; // Provide a non-null answerer

        // The apply method should call root.next(mockAnswerer), which in turn calls QuestionNode.next.
        // QuestionNode.next checks if its question is null and throws IllegalStateException.
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            tree.apply(mockAnswerer);
        });

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("question"), "Exception message should mention 'question'.");
    }

    /**
     * Verifies that the {@code apply} method correctly handles a tree with a valid structure
     * but where an internal QuestionNode (not the root) has its question set to null.
     */
    @Test
    void apply_withInternalQuestionNull_shouldThrowIllegalStateException() {
        DecisionTree tree = new DecisionTree(2); // Creates a deeper tree
        RootQuestionNode root = tree.getRoot();

        // Manually set up the root question so we can traverse to an internal node
        root.setQuestion(new EnergyQuestion(0.5));

        Answerer answererLeadingToInternalNode = q -> false; // This should lead to the internalNode

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            tree.apply(answererLeadingToInternalNode);
        });

        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("question"), "Exception message should mention 'question'.");
    }
    
    /**
     * Verifies that the {@code getNodeNumber} method correctly returns the number of all Nodes
     */
    @ParameterizedTest
    @CsvSource({
        "1, 3",
        "2, 7",
        "3, 15"
    })
    void getNodeNumber_shouldReturnCorrectValue(int depth, int expected) {
        DecisionTree tree = new DecisionTree(depth);
        assertEquals(expected, tree.getNodeNumber());
    }

    /**
     * Verifies that the {@code getStatusNumber} method correctly returns the number of all Status Nodes.
     * Status nodes are located in the last row of tree (leafs).
     */
    @ParameterizedTest
    @CsvSource({
        "1, 2",
        "2, 4",
        "3, 8"
    })
    void getStatusNumber_shouldReturnCorrectValue(int depth, int expected) {
        DecisionTree tree = new DecisionTree(depth);
        assertEquals(expected, tree.getStatusNumber());
    }

    /**
     * Verifies that the {@code isInitialized} method throws exeption when tree are not indexed.
     */
    @Test
    void isInitializedWithEmptyTree_shouldThrowExeptionWithMessage() {
        DecisionTree tree = new DecisionTree(3);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            tree.isInitialized();
        });

        assertNotNull(ex.getMessage());
        assertFalse(ex.getMessage().isEmpty());
    }

    /**
     * Verifies that the {@code isInitialized} method correctly returns value true only when all nodes are initialized. 
     */
    @Test
    void isInitialized_shouldWorkExactly() {
        DecisionTree tree = new DecisionTree(1);
        tree.rebuildIndex();
        assertFalse(tree.isInitialized());
        tree.getRoot().setQuestion(new EnergyQuestion(0.5));
        assertFalse(tree.isInitialized());
        Node root = tree.getRoot();
        OutcomeNode leftOutcome = (OutcomeNode) root.getLeftSon();
        assertFalse(tree.isInitialized());
        OutcomeNode rightOutcome = (OutcomeNode) root.getRightSon();
        leftOutcome.setStatus(new MockStatus("1"));
        rightOutcome.setStatus(new MockStatus("2"));
        tree.rebuildIndex();
        assertTrue(tree.isInitialized());
    }
}