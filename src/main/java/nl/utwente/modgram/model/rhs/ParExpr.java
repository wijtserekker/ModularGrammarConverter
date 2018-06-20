package nl.utwente.modgram.model.rhs;

import java.util.ArrayList;

public class ParExpr extends RHSElem {

    private ArrayList<RHSElem> elems;

    public ParExpr(ArrayList<RHSElem> elems) {
        this.elems = elems;
    }

    public ArrayList<RHSElem> getElems() {
        return elems;
    }

    @Override
    public String toString() {
        return "(" + arrayToString(elems) + ")";
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
