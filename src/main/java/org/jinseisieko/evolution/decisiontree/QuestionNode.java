// // src/main/java/org/decisiontree/QNode.java
// package org.jinseisieko.evolution.decisiontree;

// /**
//  *
//  * @author jinseisieko
//  */
// public class QuestionNode extends Node implements DecisionTreeNode {

//     private Question question;

//     public QuestionNode(Node father, Node leftSon, Node rightSon) {
//         super(father, leftSon, rightSon);
//         this.question = null;
//     }

//     @Override
//     public Node next(Answerer answerer) {
//         return this.traverse(answerer.answer(this.question));
//     }

//     public Question getQuestion() {
//         return question;
//     }

//     public void setQuestion(Question question) {
//         if (question == null) {
//             throw new IllegalArgumentException("Question cannot be null");
//         }
//         this.question = question;
//     }

//     @Override
//     public boolean isInitialized() {
//         return 
//             this.getFather() != null &&
//             this.getLeftSon() != null &&
//             this.getRightSon() != null &&
//             this.getQuestion() != null;
//     }

    
// }
