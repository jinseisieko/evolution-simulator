/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.decisiontree;

/**
 *
 * @author jinseisieko
 */
public class RootQuestionNode extends Node implements DecisionTreeNode {

    private Question question;

    public RootQuestionNode(Node leftSon, Node rightSon, Question question) {
        super(null, leftSon, rightSon);
    }

    @Override
    public Node next(Answerer answerer) {
        return this.traverse(answerer.answer(this.question));
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public boolean isInitialized() {
        return 
            this.getLeftSon() != null &&
            this.getRightSon() != null &&
            this.getQuestion() != null;
    }

    @Override
    public void setFather(Node father) {
        // throw
    }

}
