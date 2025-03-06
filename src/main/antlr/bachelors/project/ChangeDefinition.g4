grammar ChangeDefinition;

@header {
    package bachelors.project;
}

definition: changePattern ('|' condition)? ';' EOF;

changePattern
    : 'INSERT' nodePattern 'INTO' nodePattern   #InsertPattern
    | 'DELETE' nodePattern                      #DeletePattern
    | 'UPDATE' nodePattern ID '->' ID           #UpdatePattern
    | 'MOVE' nodePattern 'FROM' nodePattern 'TO' nodePattern       #MovePattern
    ;

nodePattern
    : nodePattern ID   #VariablePattern
    | 'ANY'             #AnyPattern
    | 'BLOCK'           #BlockPattern
    | 'CALL'            #CallPattern
    | 'IDENTIFIER'      #IdentifierPattern
    | 'LITERAL'         #LiteralPattern
    | 'LOCAL'           #LocalPattern
    | 'METHOD'          #MethodPattern
    | 'RETURN'          #ReturnPattern
    ;


condition
    : evaluatable 'IS' nodePattern          #NodeTypeCondition
    | '∃(' definition ')'                   #ExistentialQuantification
    | evaluatable operator evaluatable      #BinaryCondition
    | condition 'AND' condition             #AndCondition
    | condition 'OR' condition              #OrCondition
    | 'NOT' condition                       #NotCondition
    | ID 'REFERS TO' ID                     #RefersToCondition
    ;

operator
    : '==' | '!=' | '<' | '<=' | '>' | '>='
    ;

evaluatable
    : 'PARENT(' evaluatable ')'             #ParentEval
    | STRING               #LiteralEval
    | ID                   #VariableEval
    ;

ID: [a-zA-Z_][a-zA-Z0-9_]*;
STRING: '"' (~["])* '"';
WS: [ \t\r\n]+ -> skip;
