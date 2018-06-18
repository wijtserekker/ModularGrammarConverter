package nl.utwente.modgram.model;

import nl.utwente.modgram.model.regex.RegularExpr;

import java.util.ArrayList;

public class Rule {

    private String leftHandSide;
    private ArrayList<RegularExpr> rightHandSides;

    public String getLeftHandSide() {
        return leftHandSide;
    }
}
