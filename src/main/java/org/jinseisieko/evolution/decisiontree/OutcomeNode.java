/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package org.jinseisieko.evolution.decisiontree;

/**
 *
 * @author jinseisieko
 */
public class OutcomeNode extends Node implements DecisionTreeNode {

    private Status status;

    public OutcomeNode(Node father) {
        super(father, null, null);
        this.status = null;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Node next(Answerer answerer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isInitialized() {
        return 
            this.getFather() != null &&
            this.getStatus() != null;
    }
}
