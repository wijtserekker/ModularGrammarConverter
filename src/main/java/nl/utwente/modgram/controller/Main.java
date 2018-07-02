package nl.utwente.modgram.controller;

import nl.utwente.modgram.ModGramLexer;
import nl.utwente.modgram.ModGramParser;
import nl.utwente.modgram.controller.export.ANTLR4ExportModule;
import nl.utwente.modgram.controller.export.ExportModule;
import nl.utwente.modgram.misc.ReachabilityChecker;
import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import org.antlr.v4.misc.Graph;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Usage checking
        if (args.length < 3) {
            printUsage();
            return;
        }
        String[] splitLastArg = args[args.length-2].split("\\."); // '\\' to escape the dot as wildcard in regex
        if (splitLastArg.length != 2) {
            printUsage();
            return;
        }

        String mainModule = splitLastArg[0];
        String mainRule = splitLastArg[1];

        //Parsing and loading modules
        ArrayList<Module> modules = new ArrayList<>();
        for (int i = 0; i < args.length-2; i++) {
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
            System.err.println("The given main module '" + mainModule + "' does not exist!");
            return;
        }
        if (grammar.getModule(mainModule).getGrammarRule(mainRule) == null
                && !grammar.getModule(mainModule).containsImportRuleWithLHS(mainRule)) {
            System.err.println("The given start non-terminal '" + mainModule + "." + mainRule + "' does not exist!");
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
        if (DependencyGraphBuilder.graphIsCyclic(dependencyGraph.getNode(mainModule))) {
            System.err.println("The specified grammar contains cyclic module dependencies! This is not supported.");
            return;
        }

        // Determine the order of the modules of which the import rules must be resolved
        ArrayList<String> sortedNodes = new ArrayList<>();
        dependencyGraph.DFS(dependencyGraph.getNode(mainModule), new HashSet<>(), sortedNodes);

        // Resolve import rules
        ImportResolver.resolveImports(grammar, sortedNodes);

        // Export to regular grammar file
        Collections.reverse(sortedNodes);
        ExportModule exportModule = new ANTLR4ExportModule();
        String grammarName = args[args.length-1];

        File exportFile = new File(grammarName + exportModule.getFileExtension());
        if (exportFile.exists()) {
            System.out.print("Export file '" + grammarName + exportModule.getFileExtension() + "' already exists! Overwrite? [y/N]: ");
            Scanner scanner = new Scanner(System.in);
            String answer = scanner.nextLine().toLowerCase();
            scanner.close();
            if (!(answer.equals("yes") || answer.equals("y") || answer.equals("ye")))
                return;
            if (!exportFile.delete()) {
                System.out.println("Could not overwrite export file '" + grammarName + exportModule.getFileExtension() + "'!");
                return;
            }
        }
        try {
            if (!exportFile.createNewFile()) {
                System.out.println("Could not create export file '" + grammarName + exportModule.getFileExtension() + "'!");
                return;
            }
            PrintWriter writer = new PrintWriter(exportFile);
            writer.write(exportModule.exportGrammar(grammar, sortedNodes,
                    ReachabilityChecker.getReachableNonTerms(grammar, mainModule, mainRule),
                    grammarName));
            writer.flush();
            writer.close();
            System.out.println("Successfully written modular grammar to '" + grammarName + exportModule.getFileExtension() + "'!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("USAGE: <file> [file2 file3 ...] <module>.<non-term> <export-name>\n" +
                "- Where:\n" +
                "    file:        A file containing on or more grammar modules\n" +
                "    module:      The module containing the start non-terminal\n" +
                "    non-term:    The start non-terminal of the modular grammar\n" +
                "    export-name: The name of the export grammar");
    }
}
