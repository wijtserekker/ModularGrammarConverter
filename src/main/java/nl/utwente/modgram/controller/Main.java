package nl.utwente.modgram.controller;

import nl.utwente.modgram.ModGramLexer;
import nl.utwente.modgram.ModGramParser;
import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import org.antlr.v4.misc.Graph;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Main {

    public static void main(String[] args) {
        // Usage checking
        if (args.length < 2) {
            printUsage();
            return;
        }
        String[] splitLastArg = args[args.length-1].split("\\."); // '\\' to escape the dot as wildcard in regex
        if (splitLastArg.length != 2) {
            printUsage();
            return;
        }

        String mainModule = splitLastArg[0];
        String mainRule = splitLastArg[1];

        //Parsing and loading modules
        ArrayList<Module> modules = new ArrayList<>();
        for (int i = 0; i < args.length-1; i++) {
            try {
                CharStream charstream = CharStreams.fromFileName(args[i]);
                ModGramLexer lexer = new ModGramLexer(charstream);
                TokenStream tokenStream = new CommonTokenStream(lexer);
                ModGramParser parser = new ModGramParser(tokenStream);
                ParseTree parseTree = parser.gram();
                ParseToASTConverter parseToASTConverter = new ParseToASTConverter();
                ArrayList<Module> fileModules = (ArrayList<Module>) parseTree.accept(parseToASTConverter);
                modules.addAll(fileModules);
            } catch (IOException e) {
                System.err.println("Error reading file '" + args[i] + "'\n" + e.toString());
                return;
            }
        }
        ModularGrammar grammar = new ModularGrammar();
        for (Module module : modules)
            grammar.addModule(module);


        // Error checking
        if (grammar.getModule(mainModule) == null) {
            System.err.println("Given main module '" + mainModule + "' does not exist!");
            return;
        }
        if (grammar.getModule(mainModule).getGrammarRule(mainRule) == null
                && !grammar.getModule(mainModule).containsImportRuleWithLHS(mainRule)) {
            System.err.println("Given start non-terminal '" + mainModule + "." + mainRule + "' does not exist!");
            return;
        }

        // Check usage errors
        ArrayList<String> usageErrors = new UsageChecker().checkUsageGrammar(grammar);
        if (usageErrors.size() > 0) {
            for (String error : usageErrors)
                System.err.println(error);
            return;
        }


        // Creating dependency graph
        Graph<String> dependencyGraph = DependencyGraphBuilder.buildDependencyGraph(grammar);

        // Error checking (Is dependency graph cyclic?)
        if (DependencyGraphBuilder.graphIsCyclic(dependencyGraph, dependencyGraph.getNode(mainModule))) {
            System.err.println("The specified grammar contains cyclic module dependencies! This is not supported.");
            return;
        }

        // Determine the order of the modules of which the import rules must be resolved
        ArrayList<String> sortedNodes = new ArrayList<>();
        dependencyGraph.DFS(dependencyGraph.getNode(mainModule), new HashSet<>(), sortedNodes);

        // Resolve import rules
        ImportResolver.resolveImports(grammar, sortedNodes);

        // Export to regular grammar file
        System.out.println(grammar.toString());
    }

    private static void printUsage() {
        System.out.println("USAGE: <file> [file2 file3 ...] <module>.<non-term>\n" +
                "- Where:\n" +
                "    file:     A file containing on or more grammar modules\n" +
                "    module:   The module containing the start non-terminal\n" +
                "    non-term: The start non-terminal of the modular grammar");
    }
}
