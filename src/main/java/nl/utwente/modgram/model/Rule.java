package nl.utwente.modgram.model;

import nl.utwente.modgram.model.rhs.RHSElem;

import java.nio.CharBuffer;
import java.util.ArrayList;

public class Rule {

    private String leftHandSide;
    private ArrayList<ArrayList<RHSElem>> rightHandSides;

    public Rule(String leftHandSide, ArrayList<ArrayList<RHSElem>> rightHandSides) {
        this.leftHandSide = leftHandSide;
        this.rightHandSides = rightHandSides;
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public ArrayList<ArrayList<RHSElem>> getRightHandSides() {
        return rightHandSides;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(leftHandSide + " ::= ");
        String spacing = CharBuffer.allocate(leftHandSide.length()+1).toString().replace('\0',' ');
        if (rightHandSides.size() > 1) {
            result.append(arrayToString(rightHandSides.get(0))).append("\n");
            for (int i = 1; i < rightHandSides.size(); i++)
                result.append(spacing).append("| ").append(arrayToString(rightHandSides.get(i))).append("\n");

            result.append(spacing).append(";");
        } else {
            if (rightHandSides.size() > 0)
                result.append(arrayToString(rightHandSides.get(0)));
            result.append(";");
        }
        return result.toString();
    }

    private String arrayToString(ArrayList<RHSElem> array) {
        StringBuilder result = new StringBuilder();
        if (array.size() > 0) {
            result.append(array.get(0).toString());
            for (int i = 1; i < array.size(); i++)
                result.append(" ").append(array.get(i).toString());
        }
        return result.toString();
    }
}
