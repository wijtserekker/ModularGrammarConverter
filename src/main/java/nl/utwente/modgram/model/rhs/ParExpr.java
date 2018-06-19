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
}
