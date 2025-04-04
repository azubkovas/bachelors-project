grammar ChangeDefinitions;

@header {
    package org.defdiff;
}

definitions
    : definition (';' definition)* EOF | EOF;

definition: simpleDefinition | compoundDefinition;

compoundDefinition: '{' simpleDefinition (';' simpleDefinition)* '}';

simpleDefinition: changePattern ('|' condition)?;

changePattern
    : 'INSERT' nodePattern 'INTO' nodePattern ('AT' (NUMBER | ID))?   #InsertPattern
    | 'DELETE' nodePattern                      #DeletePattern
    | 'UPDATE' nodePattern ID '->' ID           #UpdatePattern
    | 'MOVE' nodePattern 'FROM' nodePattern 'TO' nodePattern  ('AT' (NUMBER | ID))?     #MovePattern
    ;

nodePattern
    : nodePattern ID   #VariablePattern
    | ID               #VariablePattern
    | 'ANY'             #AnyPattern
    | 'BLOCK'           #BlockPattern
    | 'CALL'            #CallPattern
    | 'IDENTIFIER'      #IdentifierPattern
    | 'FIELD ACCESS'    #FieldAccessPattern
    | 'LITERAL'         #LiteralPattern
    | 'LOCAL'           #LocalPattern
    | 'METHOD'          #MethodPattern
    | 'RETURN'          #ReturnPattern
    | 'MEMBER'          #MemberPattern
    | 'EXPR'            #ExprPattern
    | 'TYPE'            #TypePattern
    | 'PARAMETERS'      #ParametersPattern
    | 'ARGUMENTS'       #ArgumentsPattern
    | 'ARGUMENT'        #ArgumentPattern
    | 'PARAMETER'       #ParameterPattern
    | 'CONTROL STRUCTURE' ('(' controlStructureType ')')? #ControlStructurePattern
    | 'ASSIGNMENT(' nodePattern '=' nodePattern ')'      #AssignmentPattern
    ;

controlStructureType: 'IF' | 'ELSE' | 'WHILE' | 'FOR' | 'DO WHILE' | 'SWITCH' | 'TRY' | 'CATCH' | 'FINALLY';


condition
    : evaluatable 'IS' nodePattern          #NodeTypeCondition
    | 'EXISTS(' simpleDefinition ')'                   #ExistentialQuantification
    | evaluatable OPERATOR evaluatable      #BinaryCondition
    | condition 'AND' condition             #AndCondition
    | condition 'OR' condition              #OrCondition
    | 'NOT' condition                       #NotCondition
    | evaluatable 'REFERS TO' evaluatable                     #RefersToCondition
    | evaluatable 'IS SETTER FOR' evaluatable                            #SetterCondition
    | evaluatable 'IS GETTER FOR' evaluatable                            #GetterCondition
    ;

evaluatable
    : 'PARENT(' evaluatable ')'             #ParentEval
    | STRING               #LiteralEval
    | NUMBER               #LiteralEval
    | BOOL                 #LiteralEval
    | ID                   #VariableEval
    ;

ID: [a-z][a-zA-Z0-9_]*;
STRING: '"' (~["\\\r\n] | '\\' .)* '"';
NUMBER: [0-9]+ ('.' [0-9]+)?;
BOOL: 'true' | 'false';
OPERATOR: '==' | '!=' | '<' | '<=' | '>' | '>=';
WS: [ \t\r\n]+ -> skip;
