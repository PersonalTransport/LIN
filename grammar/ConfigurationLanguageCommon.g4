grammar ConfigurationLanguageCommon;

p2MinStMinNAsTimeoutNCrTimeout:
    (P2Min '=' p2Min=real Millisecond ';')?
    (STMin '=' stMin=real Millisecond ';')?
    (NAsTimeout '=' nAsTimeout=real Millisecond ';')?
    (NCrTimeout '=' nCrTimeout=real Millisecond ';')?
    ;

signalValue:
    signalArrayValue |
    signalScalarValue
    ;

signalScalarValue:
    integer
    ;

signalArrayValue:
    '{' integerList '}'
    ;

encodingDefinition:
    encodingName=Identifier '{'
        encodedValue+
    '}';

encodedValue:
    logicalEncodedValue ';' |
    physicalEncodedRange ';' |
    bcdEncodedValue ';' |
    asciiEncodedValue ';'
    ;

logicalEncodedValue:
    LogicalValue ',' value=integer (',' textInfo=CharString)?
    ;

physicalEncodedRange:
    PhysicalValue ',' minValue=integer ',' maxValue=integer ',' scale=real ',' offset=real (',' textInfo=CharString)?
    ;

bcdEncodedValue:
    BCDValue
    ;

asciiEncodedValue:
    ASCIIValue
    ;

integerList:
    integer (',' integer)*
    ;

identifierList:
    Identifier (',' Identifier)*;

integer: DecInteger | HexInteger;

bitrate:
    value=real Kbps
    ;

real: integer | Real;

LINLanguageVersion: 'LIN_language_version';
LINProtocolVersion: 'LIN_protocol_version';
Millisecond: 'ms';
Kbps: 'kbps';
P2Min: 'P2_min';
STMin: 'ST_min';
NAsTimeout: 'N_As_timeout';
NCrTimeout: 'N_Cr_timeout';
ResponseError: 'response_error';
FaultStateSignals: 'fault_state_signals';
BCDValue: 'bcd_value';
ASCIIValue: 'ascii_value';
LogicalValue: 'logical_value';
PhysicalValue: 'physical_value';

Identifier: [a-zA-Z][a-zA-Z0-9_]*;

HexInteger: '0x'HexDigit+;

Real: Digit*'.'Digit+;

DecInteger: Digit+;

CharString: '"' .*? '"';

fragment NonDigit : [a-zA-Z_] ;

fragment Digit: [0-9];

fragment HexDigit: [0-9A-Fa-f];

Whitespace: [ \n\t\r]+ -> skip;

Newline: ( '\r' '\n'? | '\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\n\r]* -> skip;