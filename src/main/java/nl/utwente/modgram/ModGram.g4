grammar ModGram;

gram : module* ;

module : 'module' LC_NAME ';' ('using' LC_NAME ';')* (rule ';')*;

rule : left_hand_side '::=' regexp                      #ruleNormal
     | left_hand_side '<-' LC_NAME '.' left_hand_side   #ruleImportByReference
     | left_hand_side '<=' LC_NAME '.' left_hand_side   #ruleImportByClone
     | left_hand_side '<=*' LC_NAME '.' left_hand_side  #ruleImportByCloneRecursive
     ;

left_hand_side : LC_NAME | UC_NAME ;
regexp : LC_NAME            #regexpNonTerm
       | UC_NAME            #regexpTerm
       | '.'                #regexpWildCard
       | '\'' TEXT? '\''    #regexpString
       | '[' TEXT? ']'      #regexpChars
       | '(' regexp ')'     #regexpPar
       | regexp '+'         #regexpPlus
       | regexp '*'         #regexpStar
       | regexp '?'         #regexpQMark
       | '~' regexp         #regexpNot
       | regexp '|' regexp  #regexpOr
       ;

expr : expr '+' expr
     | expr '*' expr
     | NUMBER
     ;

NUMBER : [0123456789]+ ;
LC_NAME : [a-z] [a-z_0-9]* ;
UC_NAME : [A-Z] [A-Z_0-9]* ;
TEXT : (~'\'' | '\\\'')+ ;
WS : [ \t\n\r]+ -> skip ;