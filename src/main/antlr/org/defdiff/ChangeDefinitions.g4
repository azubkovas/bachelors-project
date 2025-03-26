grammar ChangeDefinitions;

@header {
    package org.defdiff;
}

definitions
    : definition (';' definition)*;

definition: simpleDefinition | compoundDefinition;

compoundDefinition: '{' simpleDefinition (';' simpleDefinition)* '}';

simpleDefinition: changePattern ('|' condition)?;

changePattern
    : 'INSERT' nodePattern 'INTO' nodePattern   #InsertPattern
    | 'DELETE' nodePattern                      #DeletePattern
    | 'UPDATE' nodePattern ID '->' ID           #UpdatePattern
    | 'MOVE' nodePattern 'FROM' nodePattern 'TO' nodePattern       #MovePattern
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
    | 'ASSIGNMENT(' nodePattern '=' nodePattern ')'      #AssignmentPattern
    ;


condition
    : evaluatable 'IS' nodePattern          #NodeTypeCondition
    | 'EXISTS(' simpleDefinition ')'                   #ExistentialQuantification
    | evaluatable OPERATOR evaluatable      #BinaryCondition
    | condition 'AND' condition             #AndCondition
    | condition 'OR' condition              #OrCondition
    | 'NOT' condition                       #NotCondition
    | ID 'REFERS TO' ID                     #RefersToCondition
    ;

evaluatable
    : 'PARENT(' evaluatable ')'             #ParentEval
    | STRING               #LiteralEval
    | NUMBER               #LiteralEval
    | BOOL                 #LiteralEval
    | ID                   #VariableEval
    ;

ID: [a-zA-Z_][a-zA-Z0-9_]*;
STRING: '"' (~["\\\r\n] | '\\' .)* '"';
NUMBER: [0-9]+ ('.' [0-9]+)?;
BOOL: 'true' | 'false';
OPERATOR: '==' | '!=' | '<' | '<=' | '>' | '>=';
WS: [ \t\r\n]+ -> skip;
