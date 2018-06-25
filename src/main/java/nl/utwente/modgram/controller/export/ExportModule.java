package nl.utwente.modgram.controller.export;

import nl.utwente.modgram.model.ModularGrammar;

import java.util.ArrayList;

public interface ExportModule {

    String exportGrammar(ModularGrammar grammar, ArrayList<String> modules, String grammarName);
    String getFileExtension();

}
