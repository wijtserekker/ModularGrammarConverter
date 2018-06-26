package nl.utwente.modgram.controller.export;

import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.rhs.NonTermExpr;

import java.util.ArrayList;
import java.util.HashSet;

public interface ExportModule {

    String exportGrammar(ModularGrammar grammar, ArrayList<String> modules,
                         HashSet<NonTermExpr> reachableNonTerms, String grammarName);
    String getFileExtension();

}
