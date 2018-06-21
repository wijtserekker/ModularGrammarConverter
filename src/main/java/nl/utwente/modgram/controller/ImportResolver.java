package nl.utwente.modgram.controller;

import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;

import java.util.ArrayList;

public class ImportResolver {

    public static void resolveImports(ModularGrammar grammar, ArrayList<String> order) {
        for (String moduleName : order) {
            Module module = grammar.getModule(moduleName);
            resolveImportByCloneRecursive(module, grammar);
            resolveOtherImportRules(module, grammar);
            resolveRemoveRules(module, grammar);
        }
    }

    private static void resolveImportByCloneRecursive(Module module, ModularGrammar grammar) {

    }

    private static void resolveOtherImportRules(Module module, ModularGrammar grammar) {

    }

    private static void resolveRemoveRules(Module module, ModularGrammar grammar) {

    }

}
