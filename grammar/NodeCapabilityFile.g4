grammar NodeCapabilityFile;

nodeCapabilityFile:
    nodeCapabilityFileHeader
    nodeDefinition+
    EOF;

nodeCapabilityFileHeader:
    NodeCapabilityfile ';'
    LINLanguageVersion '=' version=CharString ';'
    ;

nodeDefinition:
    Node nodeName=Identifier '{'
        generalDefinition
        diagnosticDefinition
        framesDefinition
        encodingsDefinition
        statusManagement
        freeTextDefinition?
    '}'
    ;

generalDefinition:
    General '{'
        LINProtocolVersion '=' version=CharString ';'
        Supplier '=' supplier=integer ';'
        Function '=' function=integer ';'
        Variant '=' variant=integer ';'
        Bitrate '=' bitrateDefinition ';'
        SendsWakeUpSignal '=' sendsWakeUp=('"yes"'|'"no"') ';'
    '}';

bitrateDefinition:
    automaticBitrateDefinition
    | selectBitrateDefinition
    | fixedBitrateDefinition
    ;

automaticBitrateDefinition:
    Automatic (Min min=bitrate)? (Max max=bitrate)?
    ;

selectBitrateDefinition:
    Select '{' bitrate (',' bitrate)* '}'
    ;

fixedBitrateDefinition:
    bitrate
    ;

bitrate:
    value=real Kbps
    ;

diagnosticDefinition:
    Diagnostic '{'
        NAD '=' (nadList=integerList | startNAD=integer To endNAD=integer) ';'
        DiagnosticClass '=' diagnosticClass=integer ';'
        (P2Min '=' p2Min=real Millisecond ';')?
        (STMin '=' stMin=real Millisecond ';')?
        (NAsTimeout '=' nAsTimeout=real Millisecond ';')?
        (NCrTimeout '=' nCrTimeout=real Millisecond ';')?
        (SupportSid '{' supportSids=integerList '}'  ';')?
        (MaxMessageLength '=' maxMessageLength=integer ';')?
    '}';

framesDefinition:
    Frames '{'
        frameDefinition+
    '}';

frameDefinition:
    kind=(Publish | Subscribe) name=Identifier '{'
        Length '=' length=integer ';'
        (MinPeriod '=' minPeriod=integer Millisecond ';')?
        (MaxPeriod '=' maxPeriod=integer Millisecond ';')?
        (EventTriggeredFrame '=' eventTriggeredFrame=Identifier ';')?
        signalsDefinition?
    '}';

signalsDefinition:
    Signals '{'
        signalDefinition+
    '}';

signalDefinition:
    name=Identifier '{'
        Size '=' size=integer ';'
        InitValue '=' signalValue ';'
        Offset '=' offset=integer ';'
        (encoding=Identifier ';')?
    '}';

signalValue:
    signalScalarValue |
    signalArrayValue
    ;

signalScalarValue:
    integer
    ;

signalArrayValue:
    '{' integerList '}'
    ;

encodingsDefinition:
    Encoding '{'
        encodingDefinition+
    '}';

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

statusManagement:
    StatusManagement '{'
        ResponseError '=' responseErrorSignal=Identifier ';'
        (FaultStateSignals '=' faultStateSignals=identifierList ';')?
    '}';

freeTextDefinition:
    FreeText '{'
        text=CharString
    '}';

integerList:
    integer (',' integer)*
    ;

identifierList:
    Identifier (',' Identifier)*;

integer: DecInteger | HexInteger;

real: integer | Real;

NodeCapabilityfile: 'node_capability_file';
LINLanguageVersion: 'LIN_language_version';
LINProtocolVersion: 'LIN_protocol_version';
Node: 'node';
General: 'general';
Supplier: 'supplier';
Function: 'function';
Variant: 'variant';
Bitrate: 'bitrate';
SendsWakeUpSignal: 'sends_wake_up_signal';
Automatic: 'automatic';
Select: 'select';
Min: 'min';
Max: 'max';
Kbps: 'kbps';
Diagnostic: 'diagnostic';
NAD: 'NAD';
To: 'to';
DiagnosticClass: 'diagnostic_class';
P2Min: 'P2_min';
STMin: 'ST_min';
NAsTimeout: 'N_As_timeout';
NCrTimeout: 'N_Cr_timeout';
SupportSid: 'support_sid';
MaxMessageLength: 'max_message_length';
Frames: 'frames';
Publish: 'publish';
Subscribe: 'subscribe';
Length: 'length';
MinPeriod: 'min_period';
MaxPeriod: 'max_period';
Millisecond: 'ms';
EventTriggeredFrame: 'event_triggered_frame';
Signals: 'signals';
InitValue: 'init_value';
Size: 'size';
Offset: 'offset';
Encoding: 'encoding';
BCDValue: 'bcd_value';
ASCIIValue: 'ascii_value';
LogicalValue: 'logical_value';
PhysicalValue: 'physical_value';
StatusManagement: 'status_management';
ResponseError: 'response_error';
FaultStateSignals: 'fault_state_signals';
FreeText: 'free_text';


Identifier: [a-zA-Z][a-zA-Z0-9_]*;

Real: Digit+'.'Digit+;

DecInteger: Digit+;

HexInteger: '0x'HexDigit+;

CharString: '"' .*? '"';

fragment NonDigit : [a-zA-Z_] ;

fragment Digit: [0-9];

fragment HexDigit: [0-9A-Fa-f];

Whitespace: [ \n\t\r]+ -> skip;

Newline: ( '\r' '\n'? | '\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\n\r]* -> skip;