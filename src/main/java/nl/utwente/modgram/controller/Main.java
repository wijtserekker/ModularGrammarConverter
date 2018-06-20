package nl.utwente.modgram.controller;

import nl.utwente.modgram.ModGramLexer;
import nl.utwente.modgram.ModGramParser;
import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("USAGE: <file> [file2 file3 ...] <module>.<non-term>\n" +
                    "- Where:\n" +
                    "    file:     A file containing on or more grammar modules\n" +
                    "    module:   The module containing the start non-terminal\n" +
                    "    non-term: The start non-terminal of the modular grammar");
            return;
        }

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

        System.out.println("DONE");

    }
}
