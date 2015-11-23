grammar NodeCapabilityFile;

import ConfigurationLanguageCommon;

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

diagnosticDefinition:
    Diagnostic '{'
        NAD '=' (nadList=integerList | startNAD=integer To endNAD=integer) ';'
        DiagnosticClass '=' diagnosticClass=integer ';'
        p2MinStMinNAsTimeoutNCrTimeout
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

encodingsDefinition:
    Encoding '{'
        encodingDefinition+
    '}';

statusManagement:
    StatusManagement '{'
        ResponseError '=' responseErrorSignal=Identifier ';'
        (FaultStateSignals '=' faultStateSignals=identifierList ';')?
    '}';

freeTextDefinition:
    FreeText '{'
        text=CharString
    '}';

NodeCapabilityfile: 'node_capability_file';
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
Diagnostic: 'diagnostic';
NAD: 'NAD';
To: 'to';
DiagnosticClass: 'diagnostic_class';

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
StatusManagement: 'status_management';
FreeText: 'free_text';
