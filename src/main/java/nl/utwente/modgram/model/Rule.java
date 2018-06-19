package nl.utwente.modgram.model;

import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;

public class Rule {

    private String leftHandSide;
    private ArrayList<RHSElem> rightHandSides;

    public Rule(String leftHandSide, ArrayList<RHSElem> rightHandSides) {
        this.leftHandSide = leftHandSide;
        this.rightHandSides = rightHandSides;
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public ArrayList<RHSElem> getRightHandSides() {
        return rightHandSides;
    }
}
