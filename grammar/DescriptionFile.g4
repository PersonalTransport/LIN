grammar DescriptionFile;

import ConfigurationLanguageCommon;

descriptionFile:
    LINDescriptionFile ';'
    LINProtocolVersion '=' protocolVersion=CharString ';'
    LINLanguageVersion '=' languageVersion=CharString ';'
    LINSpeed '=' bitrate ';'
    (ChannelName '=' channelName=Identifier ';')?
    nodesDefinition
    (nodeCompositionDefinition)?
    signalsDefinition
    //(diagnosticSignalDefinition)?
    framesDefinition
    (sporadicFramesDefinition)?
    (eventTriggeredFramesDefinition)?
    //(diagnosticFramesDefinition)?
    nodesAttributesDefinition
    scheduleTablesDefinition
    (signalGroupsDefinition)?
    (signalEncodingTypesDefinition)?
    (signalRepresentationsDefinition)?
    EOF;

nodesDefinition:
    Nodes '{'
        Master ':' masterName=Identifier ',' timeBase=real Millisecond ',' jitter=real Millisecond ';'
        Slaves ':' slaves=identifierList ';'
    '}';

nodeCompositionDefinition:
    Composite '{'
        configurationDefinition+
    '}';

configurationDefinition:
    Configuration name=Identifier '{'
        compositeNodeDefinition+
    '}';

compositeNodeDefinition:
    compositeNode=Identifier '{' identifierList '}' ';'
    ;

signalsDefinition:
    Signals '{'
        signalDefinition+
    '}';

signalDefinition:
    signalName=Identifier ':' signalSize=integer ',' signalValue ',' publishedBy=Identifier ',' subscribers=identifierList ';'
    ;

// TODO add these back
/*diagnosticSignalDefinition:
    DiagnosticSignals '{'
        'MasterReqB0' ':' '8' ',' '0' ';'
        'MasterReqB1' ':' '8' ',' '0' ';'
        'MasterReqB2' ':' '8' ',' '0' ';'
        'MasterReqB3' ':' '8' ',' '0' ';'
        'MasterReqB4' ':' '8' ',' '0' ';'
        'MasterReqB5' ':' '8' ',' '0' ';'
        'MasterReqB6' ':' '8' ',' '0' ';'
        'MasterReqB7' ':' '8' ',' '0' ';'
        'SlaveRespB0' ':' '8' ',' '0' ';'
        'SlaveRespB1' ':' '8' ',' '0' ';'
        'SlaveRespB2' ':' '8' ',' '0' ';'
        'SlaveRespB3' ':' '8' ',' '0' ';'
        'SlaveRespB4' ':' '8' ',' '0' ';'
        'SlaveRespB5' ':' '8' ',' '0' ';'
        'SlaveRespB6' ':' '8' ',' '0' ';'
        'SlaveRespB7' ':' '8' ',' '0' ';'
    '}';*/

framesDefinition:
    Frames '{'
        frameDefinition+
    '}';

frameDefinition:
    frameName=Identifier ':' frameId=integer ',' publishedBy=Identifier ',' frameSize=integer '{'
        signalOffsetDefinition+
    '}';

signalOffsetDefinition:
    signalName=Identifier ',' signalOffset=integer ';' ;

sporadicFramesDefinition:
    SporadicFrames '{'
        sporadicFrameDefinition+
    '}';

sporadicFrameDefinition:
    sporadicFrameName=Identifier ':' frameNames=identifierList ';' ;

eventTriggeredFramesDefinition:
    EventTriggeredFrames '{'
        eventTriggeredFrameDefinition+
    '}';

eventTriggeredFrameDefinition:
    eventTriggeredFrameName=Identifier ':'
    collisionST=Identifier ',' frameId=integer ',' frameNames=identifierList? ';'
    ;

// TODO add these back
/*diagnosticFramesDefinition:
    DiagnosticFrames '{'
        MasterReq ':' '60' '{'
            'MasterReqB0' ',' '0' ';'
            'MasterReqB1' ',' '8' ';'
            'MasterReqB2' ',' '16' ';'
            'MasterReqB3' ',' '24' ';'
            'MasterReqB4' ',' '32' ';'
            'MasterReqB5' ',' '40' ';'
            'MasterReqB6' ',' '48' ';'
            'MasterReqB7' ',' '56' ';'
        '}'
        SlaveResp ':' '61' '{'
            'SlaveRespB0' ',' '0'  ';'
            'SlaveRespB1' ',' '8'  ';'
            'SlaveRespB2' ',' '16' ';'
            'SlaveRespB3' ',' '24' ';'
            'SlaveRespB4' ',' '32' ';'
            'SlaveRespB5' ',' '40' ';'
            'SlaveRespB6' ',' '48' ';'
            'SlaveRespB7' ',' '56' ';'
        '}'
    '}';*/

nodesAttributesDefinition:
    NodeAttributes '{'
        slaveDefinition+
    '}';

slaveDefinition:
    name=Identifier '{'
        LINProtocol '=' protocolVersion=CharString ';'
        ConfiguredNAD '=' configuredNAD=integer ';'
        (InitialNAD '=' initialNAD=integer ';')?
        //LIN >= 2.x only
        ProductId '=' supplierId=integer ',' functionId=integer  (',' variant=integer)? ';'
        ResponseError '=' responseErrorSignal=Identifier ';'
        (FaultStateSignals '=' faultStateSignals=identifierList ';')?
        p2MinStMinNAsTimeoutNCrTimeout
        configurableFrames21Definition
        //(configurableFrames21Definition | configurableFrames20Definition)
    '}';

configurableFrames21Definition:
    ConfigurableFrames '{'
        configurableFrame21Definition+
    '}';

configurableFrame21Definition:
    frameName=Identifier ';';

configurableFrames20Definition:
    ConfigurableFrames '{'
        configurableFrame20Definition+
    '}';

configurableFrame20Definition:
    frameName=Identifier '=' messageId=integer ';';

scheduleTablesDefinition:
    ScheduleTables '{'
        scheduleTableDefinition+
    '}';

scheduleTableDefinition:
    tableName=Identifier '{'
        scheduleTableEntry+
    '}';

scheduleTableEntry:
    unconditionalEntry |
    masterReqEntry |
    slaveRespEntry |
    assignNADEntry |
    conditionalChangeNADEntry |
    dataDumpEntry |
    saveConfigurationEntry |
    assignFrameIdRangeEntry |
    freeFormatEntry |
    assignFrameIdEntry
    ;

unconditionalEntry:
    frameName=Identifier Delay frameTime=real Millisecond ';'
    ;

masterReqEntry:
    MasterReq Delay frameTime=real Millisecond ';'
    ;

slaveRespEntry:
    SlaveResp Delay frameTime=real Millisecond ';'
    ;

assignNADEntry:
    AssignNAD '{' nodeName=Identifier '}'
    Delay frameTime=real Millisecond ';'
    ;

conditionalChangeNADEntry:
    ConditionalChangeNAD '{' NAD=integer ',' id=integer ',' byteV=integer ',' mask=integer ',' inv=integer ',' newNAD=integer '}'
    Delay frameTime=real Millisecond ';'
    ;

dataDumpEntry:
    DataDump '{' nodeName=Identifier ',' D1=integer ',' D2=integer ',' D3=integer ',' D4=integer ',' D5=integer '}'
    Delay frameTime=real Millisecond ';'
     ;

saveConfigurationEntry:
    SaveConfiguration '{' nodeName=Identifier '}'
    Delay frameTime=real Millisecond ';'
    ;

assignFrameIdRangeEntry:
    AssignFrameIdRange '{' nodeName=Identifier ',' startIndex=integer  (',' PIDIndexP0=integer ',' PIDIndexP1=integer ',' PIDIndexP2=integer ',' PIDIndexP3=integer)? '}'
    Delay frameTime=real Millisecond ';'
    ;

freeFormatEntry:
    FreeFormat '{' d1=integer ',' d2=integer ',' d3=integer ',' d4=integer ',' d5=integer ',' d6=integer ',' d7=integer ',' d8=integer '}'
    Delay frameTime=real Millisecond ';'
    ;

assignFrameIdEntry:
    AssignFrameId '{' nodeName=Identifier ',' frameName=Identifier '}'
    Delay frameTime=real Millisecond ';'
    ;

signalGroupsDefinition:
    SignalGroups '{'
        signalGroupDefinition+
    '}';

signalGroupDefinition:
    groupName=Identifier ':' groupSize=integer '{'
        signalGroupElement+
    '}';

signalGroupElement:
    signalName=Identifier ',' groupOffset=integer ';' ;

signalEncodingTypesDefinition:
    SignalEncodingTypes '{'
        encodingDefinition+
    '}';

signalRepresentationsDefinition:
    SignalRepresentation '{'
        signalRepresentationDefinition+
    '}';

signalRepresentationDefinition:
    signalEncodingTypeName=Identifier ':' signals=identifierList ';'
    ;

LINDescriptionFile: 'LIN_description_file';
LINSpeed: 'LIN_speed';
ChannelName: 'Channel_name';
Nodes: 'Nodes';
Master: 'Master';
Slaves: 'Slaves';
Composite: 'composite';
Configuration: 'configuration';
Signals: 'Signals';
DiagnosticSignals: 'Diagnostic_signals';
Frames: 'Frames';
SporadicFrames: 'Sporadic_frames';
EventTriggeredFrames: 'Event_triggered_frames';
DiagnosticFrames: 'Diagnostic_frames';
ProductId: 'product_id';
NodeAttributes: 'Node_attributes';
LINProtocol: 'LIN_protocol';
ConfiguredNAD: 'configured_NAD';
InitialNAD: 'initial_NAD';
ConfigurableFrames: 'configurable_frames';
ScheduleTables: 'Schedule_tables';
Delay: 'delay';
MasterReq: 'MasterReq';
SlaveResp: 'SlaveResp';
AssignNAD: 'AssignNAD';
ConditionalChangeNAD: 'ConditionalChangeNAD';
DataDump: 'DataDump';
SaveConfiguration: 'SaveConfiguration';
AssignFrameIdRange: 'AssignFrameIdRange';
FreeFormat: 'FreeFormat';
AssignFrameId: 'AssignFrameId';
SignalGroups: 'Signal_groups';
SignalEncodingTypes: 'Signal_encoding_types';
SignalRepresentation: 'Signal_representation';