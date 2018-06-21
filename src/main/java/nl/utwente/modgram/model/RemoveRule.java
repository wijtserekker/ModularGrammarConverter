package nl.utwente.modgram.model;

import nl.utwente.modgram.model.rhs.RHSElem;

import java.util.ArrayList;

public class RemoveRule {

    private String leftHandSide;
    private ArrayList<RHSElem> righHandSide;


    public RemoveRule(String leftHandSide, ArrayList<RHSElem> righHandSide) {
        this.leftHandSide = leftHandSide;
        this.righHandSide = righHandSide;
    }

    public String getLeftHandSide() {
        return leftHandSide;
    }

    public ArrayList<RHSElem> getRighHandSide() {
        return righHandSide;
    }

    @Override
    public String toString() {
        return leftHandSide + " :/= " + arrayToString(righHandSide) + ";";
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
