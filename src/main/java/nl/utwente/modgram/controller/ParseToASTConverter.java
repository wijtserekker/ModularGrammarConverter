package nl.utwente.modgram.controller;

import nl.utwente.modgram.ModGramBaseVisitor;
import nl.utwente.modgram.ModGramParser;

public class ParseToASTConverter extends ModGramBaseVisitor {

    @Override
    public Object visitGram(ModGramParser.GramContext ctx) {
        return super.visitGram(ctx);
    }

    @Override
    public Object visitModule(ModGramParser.ModuleContext ctx) {
        System.out.println("MOD");
        return super.visitModule(ctx);
    }

    @Override
    public Object visitRuleNormal(ModGramParser.RuleNormalContext ctx) {
        return super.visitRuleNormal(ctx);
    }

    @Override
    public Object visitRuleImportByReference(ModGramParser.RuleImportByReferenceContext ctx) {
        return super.visitRuleImportByReference(ctx);
    }

    @Override
    public Object visitRuleImportByClone(ModGramParser.RuleImportByCloneContext ctx) {
        return super.visitRuleImportByClone(ctx);
    }

    @Override
    public Object visitRuleImportByCloneRecursive(ModGramParser.RuleImportByCloneRecursiveContext ctx) {
        return super.visitRuleImportByCloneRecursive(ctx);
    }

    @Override
    public Object visitLeft_hand_side(ModGramParser.Left_hand_sideContext ctx) {
        return super.visitLeft_hand_side(ctx);
    }

    @Override
    public Object visitRight_hand_side(ModGramParser.Right_hand_sideContext ctx) {
        return super.visitRight_hand_side(ctx);
    }

    @Override
    public Object visitRegexpNonTerm(ModGramParser.RegexpNonTermContext ctx) {
        return super.visitRegexpNonTerm(ctx);
    }

    @Override
    public Object visitRegexpToken(ModGramParser.RegexpTokenContext ctx) {
        return super.visitRegexpToken(ctx);
    }

    @Override
    public Object visitRegexpImportedNonTerm(ModGramParser.RegexpImportedNonTermContext ctx) {
        return super.visitRegexpImportedNonTerm(ctx);
    }

    @Override
    public Object visitRegexpImportedToken(ModGramParser.RegexpImportedTokenContext ctx) {
        return super.visitRegexpImportedToken(ctx);
    }

    @Override
    public Object visitRegexpWildCard(ModGramParser.RegexpWildCardContext ctx) {
        return super.visitRegexpWildCard(ctx);
    }

    @Override
    public Object visitRegexpString(ModGramParser.RegexpStringContext ctx) {
        return super.visitRegexpString(ctx);
    }

    @Override
    public Object visitRegexpChars(ModGramParser.RegexpCharsContext ctx) {
        return super.visitRegexpChars(ctx);
    }

    @Override
    public Object visitRegexpPar(ModGramParser.RegexpParContext ctx) {
        return super.visitRegexpPar(ctx);
    }

    @Override
    public Object visitRegexpPlus(ModGramParser.RegexpPlusContext ctx) {
        return super.visitRegexpPlus(ctx);
    }

    @Override
    public Object visitRegexpStar(ModGramParser.RegexpStarContext ctx) {
        return super.visitRegexpStar(ctx);
    }

    @Override
    public Object visitRegexpQMark(ModGramParser.RegexpQMarkContext ctx) {
        return super.visitRegexpQMark(ctx);
    }

    @Override
    public Object visitRegexpNot(ModGramParser.RegexpNotContext ctx) {
        return super.visitRegexpNot(ctx);
    }

    @Override
    public Object visitRegexpOr(ModGramParser.RegexpOrContext ctx) {
        return super.visitRegexpOr(ctx);
    }
}
