package nl.utwente.modgram.controller;

import nl.utwente.modgram.ModGramBaseVisitor;
import nl.utwente.modgram.ModGramParser;
import nl.utwente.modgram.model.ImportRule;
import nl.utwente.modgram.model.Module;
import nl.utwente.modgram.model.RemoveRule;
import nl.utwente.modgram.model.Rule;
import nl.utwente.modgram.model.rhs.*;

import java.util.ArrayList;

/**
 * An ANTLR tree visitor which converts a parse tree to a modular grammar model.
 *
 * @see nl.utwente.modgram.model.ModularGrammar
 */
public class ParseToASTConverter extends ModGramBaseVisitor {

    @Override
    public Object visitGram(ModGramParser.GramContext ctx) {
        ArrayList<Module> modules = new ArrayList<>();
        for (int i = 0; i < ctx.module().size(); i++) {
            Module module = (Module) this.visitModule(ctx.module(i));
            for (Module addedModule : modules) { // Check if module already exists
                if (addedModule.getName().equals(module.getName())) {
                    System.err.println("Module '" + module.getName() + "' is declared more than once!");
                    System.exit(1);
                }
            }
            modules.add(module);
        }
        return modules;
    }

    @Override
    public Object visitModule(ModGramParser.ModuleContext ctx) {
        Module module = new Module(ctx.LC_NAME(0).getText());
        for (int i = 1; i < ctx.LC_NAME().size(); i++) {
            module.addDependency(ctx.LC_NAME(i).getText());
        }

        for (int i = 0; i < ctx.gram_rule().size(); i++) {
            Object rule = this.visit(ctx.gram_rule(i));
            if (rule instanceof ImportRule) {
                module.addImportRule((ImportRule) rule);
            } else if (rule instanceof Rule){
                module.addGrammarRule((Rule) rule);
            } else if (rule instanceof RemoveRule) {
                module.addRemoveRule((RemoveRule) rule);
            }
        }
        return module;
    }

    @Override
    public Object visitRuleNormal(ModGramParser.RuleNormalContext ctx) {
        String lefHandSide = (String) this.visitLeft_hand_side(ctx.left_hand_side());
        ArrayList<ArrayList<RHSElem>> rightHandSides = (ArrayList<ArrayList<RHSElem>>) this.visitRight_hand_side(ctx.right_hand_side());
        return new Rule(lefHandSide, rightHandSides);
    }

    @Override
    public Object visitRuleImportByReference(ModGramParser.RuleImportByReferenceContext ctx) {
        String leftHandSide = (String) this.visitLeft_hand_side(ctx.left_hand_side(0));
        String moduleName = ctx.LC_NAME().getText();
        String ruleName = (String) this.visitLeft_hand_side(ctx.left_hand_side(1));
        return new ImportRule(ImportRule.Type.REFERENCE, leftHandSide, moduleName, ruleName, false);
    }

    @Override
    public Object visitRuleImportByClone(ModGramParser.RuleImportByCloneContext ctx) {
        String leftHandSide = (String) this.visitLeft_hand_side(ctx.left_hand_side(0));
        String moduleName = ctx.LC_NAME().getText();
        String ruleName = (String) this.visitLeft_hand_side(ctx.left_hand_side(1));
        return new ImportRule(ImportRule.Type.CLONE, leftHandSide, moduleName, ruleName, false);
    }

    @Override
    public Object visitRuleImportByCloneRecursive(ModGramParser.RuleImportByCloneRecursiveContext ctx) {
        String leftHandSide = (String) this.visitLeft_hand_side(ctx.left_hand_side(0));
        String moduleName = ctx.LC_NAME().getText();
        String ruleName = (String) this.visitLeft_hand_side(ctx.left_hand_side(1));
        return new ImportRule(ImportRule.Type.CLONE_RECURSIVE, leftHandSide, moduleName, ruleName, false);
    }

    @Override
    public Object visitRuleRemove(ModGramParser.RuleRemoveContext ctx) {
        String leftHandSide = (String) this.visitLeft_hand_side(ctx.left_hand_side());
        ArrayList<RHSElem> righHandSide = new ArrayList<>();
        for (int i = 0; i < ctx.right_hand_side().regexp().size(); i++) {
            righHandSide.add((RHSElem) this.visit(ctx.right_hand_side().regexp(i)));
        }
        return new RemoveRule(leftHandSide, righHandSide);
    }

    @Override
    public Object visitLeft_hand_side(ModGramParser.Left_hand_sideContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitRight_hand_side(ModGramParser.Right_hand_sideContext ctx) {
        ArrayList<ArrayList<RHSElem>> rightHandSides = new ArrayList<>();
        ArrayList<RHSElem> rightHandSide = new ArrayList<>();
        for (int i = 0; i < ctx.regexp().size(); i++) {
            RHSElem elem = (RHSElem) this.visit(ctx.regexp(i));
            if (elem instanceof Operator && ((Operator) elem).getType() == Operator.Type.OR) {
                rightHandSides.add(rightHandSide);
                rightHandSide = new ArrayList<>();
            } else {
                rightHandSide.add(elem);
            }
        }
        rightHandSides.add(rightHandSide);
        return rightHandSides;
    }

    @Override
    public Object visitRegexpNonTerm(ModGramParser.RegexpNonTermContext ctx) {
        return new NonTermExpr(ctx.LC_NAME().getText());
    }

    @Override
    public Object visitRegexpToken(ModGramParser.RegexpTokenContext ctx) {
        return new NonTermExpr(ctx.UC_NAME().getText());
    }

    @Override
    public Object visitRegexpImportedNonTerm(ModGramParser.RegexpImportedNonTermContext ctx) {
        return new NonTermExpr(ctx.LC_NAME(0).getText(), ctx.LC_NAME(1).getText());
    }

    @Override
    public Object visitRegexpImportedToken(ModGramParser.RegexpImportedTokenContext ctx) {
        return new NonTermExpr(ctx.LC_NAME().getText(), ctx.UC_NAME().getText());
    }

    @Override
    public Object visitRegexpWildCard(ModGramParser.RegexpWildCardContext ctx) {
        return new Operator(Operator.Type.WILDCARD);
    }

    @Override
    public Object visitRegexpString(ModGramParser.RegexpStringContext ctx) {
        String text = ctx.STRING().getText();
        text = text.substring(1, text.length()-1);
        return new StrExpr(text);
    }

    @Override
    public Object visitRegexpChars(ModGramParser.RegexpCharsContext ctx) {
        String text = ctx.CHARS().getText();
        text = text.substring(1, text.length()-1);
        return new CharsExpr(text);
    }

    @Override
    public Object visitRegexpPar(ModGramParser.RegexpParContext ctx) {
        ArrayList<RHSElem> elems = new ArrayList<>();
        for (int i = 0; i < ctx.regexp().size(); i++) {
            elems.add((RHSElem) this.visit(ctx.regexp(i)));
        }
        return new ParExpr(elems);
    }

    @Override
    public Object visitRegexpPlus(ModGramParser.RegexpPlusContext ctx) {
        return new Operator(Operator.Type.ONE_OR_MORE);
    }

    @Override
    public Object visitRegexpStar(ModGramParser.RegexpStarContext ctx) {
        return new Operator(Operator.Type.ZERO_OR_MORE);
    }

    @Override
    public Object visitRegexpQMark(ModGramParser.RegexpQMarkContext ctx) {
        return new Operator(Operator.Type.ZERO_OR_ONE);
    }

    @Override
    public Object visitRegexpNot(ModGramParser.RegexpNotContext ctx) {
        return new Operator(Operator.Type.NOT);
    }

    @Override
    public Object visitRegexpOr(ModGramParser.RegexpOrContext ctx) {
        return new Operator(Operator.Type.OR);
    }
}
