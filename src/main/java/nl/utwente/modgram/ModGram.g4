grammar ModGram;

gram : module+ ;

module : 'module' LC_NAME ';' ('using' LC_NAME ';')* (gram_rule ';')*;

gram_rule : left_hand_side '::=' right_hand_side             #ruleNormal
          | left_hand_side '<-' LC_NAME '.' left_hand_side   #ruleImportByReference
          | left_hand_side '<=' LC_NAME '.' left_hand_side   #ruleImportByClone
          | left_hand_side '<=*' LC_NAME '.' left_hand_side  #ruleImportByCloneRecursive
          | left_hand_side ':/=' right_hand_side             #ruleRemove
          ;

left_hand_side : LC_NAME | UC_NAME ;
right_hand_side : regexp+ ;
regexp : LC_NAME        #regexpNonTerm
       | UC_NAME        #regexpToken
       | LC_NAME '.' LC_NAME #regexpImportedNonTerm
       | LC_NAME '.' UC_NAME #regexpImportedToken
       | '$'            #regexpWildCard
       | STRING         #regexpString
       | CHARS          #regexpChars
       | '(' regexp+ ')'#regexpPar
       | '+'            #regexpPlus
       | '*'            #regexpStar
       | '?'            #regexpQMark
       | '~'            #regexpNot
       | '|'            #regexpOr
       ;

LC_NAME: [a-z] [a-z_0-9]*;
UC_NAME: [A-Z] [A-Z_0-9]*;
STRING: '\'' ('\\'. | ~('\''|'\\'))* '\'';
CHARS: '[' (~('\\'| ']' | '[') | '\\'.)* ']';
WS: [ \t\n\r]+ -> skip;
