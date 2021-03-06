package nl.utwente.modgram.controller.export;

import nl.utwente.modgram.model.ModularGrammar;
import nl.utwente.modgram.model.Module;
import nl.utwente.modgram.model.Rule;
import nl.utwente.modgram.model.rhs.*;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The ANTLR4 grammar export module. It converts the given modular grammar to a {@code String} which can be written to
 * a file.
 */
public class ANTLR4ExportModule implements ExportModule {
    /**
     * Generates a {@code String} representation of the given grammar in the ANTLR4 grammar syntax. The modular grammar
     * rules should all have been resolved in the given modular grammar.
     * @param grammar           The modular grammar.
     * @param moduleNames       The names of the modules that should be exported.
     * @param reachableNonTerms The non-terminals that are reachable from the start non-terminal.
     * @param grammarName       The name of the output grammar.
     * @return                  The {@code String} representation of the given modular grammar in the ANTLR4 grammar syntax
     */
    @Override
    public String exportGrammar(ModularGrammar grammar, ArrayList<String> moduleNames,
                                HashSet<NonTermExpr> reachableNonTerms, String grammarName) {
        StringBuilder result = new StringBuilder();
        result.append("grammar ").append(grammarName).append(";\n\n");
        for (String moduleName : moduleNames) {
            result.append(exportModule(grammar.getModule(moduleName), reachableNonTerms)).append('\n');
        }
        return result.toString();
    }

    @Override
    public String getFileExtension() {
        return ".g4";
    }

    private String exportModule(Module module, HashSet<NonTermExpr> reachableNonTerms) {
        StringBuilder result = new StringBuilder();
        for (Rule rule : module.getGrammarRules()) {
            if (reachableNonTerms.contains(new NonTermExpr(module.getName(), rule.getLeftHandSide())))
                result.append(exportRule(rule, module.getName())).append('\n');
        }
        return result.toString();
    }

    private String exportRule(Rule rule, String moduleName) {
        String lhs = rule.getLeftHandSide();
        StringBuilder result = new StringBuilder(Character.isLowerCase(lhs.charAt(0)) ? moduleName : moduleName.toUpperCase());
        String spacing = CharBuffer.allocate(rule.getLeftHandSide().length()+moduleName.length()+2).toString().replace('\0',' ');
        result.append('_').append(lhs).append(" : ");
        if (rule.getRightHandSides().size() > 1) {
            result.append(exportRHSelems(rule.getRightHandSides().get(0), moduleName)).append('\n');
            for (int i = 1; i < rule.getRightHandSides().size(); i++)
                result.append(spacing).append("| ").append(exportRHSelems(rule.getRightHandSides().get(i), moduleName)).append('\n');
            result.append(spacing);
        } else {
            if (rule.getRightHandSides().size() > 0)
                result.append(exportRHSelems(rule.getRightHandSides().get(0), moduleName));
        }
        result.append(";");
        return result.toString();
    }

    private String exportRHSelems(ArrayList<RHSElem> elems, String moduleName) {
        StringBuilder result = new StringBuilder();
        if (elems.size() > 0)
            result.append(exportRHSelem(elems.get(0), moduleName));
        for (int i = 1; i < elems.size(); i++)
            result.append(' ').append(exportRHSelem(elems.get(i), moduleName));
        return result.toString();
    }

    private String exportRHSelem(RHSElem elem, String moduleName) {
        if (elem instanceof CharsExpr) {
            return '[' + ((CharsExpr) elem).getText() + ']';
        } else if (elem instanceof StrExpr) {
            return '\'' + ((StrExpr) elem).getText() + '\'';
        } else if (elem instanceof ParExpr) {
            return '(' + exportRHSelems(((ParExpr) elem).getElems(), moduleName) + ')';
        } else if (elem instanceof Operator) {
            switch (((Operator) elem).getType()) {
                case WILDCARD:
                    return ".";
                case ONE_OR_MORE:
                    return "+";
                case ZERO_OR_MORE:
                    return "*";
                case ZERO_OR_ONE:
                    return "?";
                case NOT:
                    return "~";
                case OR:
                    return "|";
            }
        } else if (elem instanceof NonTermExpr) {
            NonTermExpr nonTerm = (NonTermExpr) elem;
            String module = nonTerm.getModule();
            if (module == null)
                module = moduleName;
            if (Character.isUpperCase(nonTerm.getName().charAt(0)))
                module = module.toUpperCase();
            return module + '_' + nonTerm.getName();
        }
        return null; // Should never happen
    }
}
