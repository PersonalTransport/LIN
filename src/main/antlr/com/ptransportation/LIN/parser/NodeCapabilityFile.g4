grammar NodeCapabilityFile;

nodeCapabilityFile:
	'node_capability_file' ';'
	'LIN_language_version' '=' languageVersion=String ';'
	node;
node: slave | master;
slave:
	'node' name=Identifier '{'
		'general' '{'
			'LIN_protocol_version' '=' protocolVersion=String ';'
			'supplier' '=' supplier=integer ';'
			'function' '=' function=integer ';'
			'variant' '=' variant=integer ';'
			'bitrate' '=' bitrate=baudrate ';'
			'sends_wake_up_signal' '=' (sendsWakeUpSignal='"yes"' | '"no"') ';'
		'}'

		'diagnostic' '{'
            'NAD' '=' nadSet ';'
            'diagnostic_class' '=' diagnosticClass=integer ';'
            ('P2_min' '=' p2Min=number 'ms' ';')?
            ('ST_min' '=' stMin=number 'ms' ';')?
            ('N_As_timeout' '=' nAsTimeout=number 'ms' ';')?
            ('N_Cr_timeout' '=' nCrTimeout=number 'ms' ';')?
            ('support_sid' '{' supportedSIDS+=integer (',' supportedSIDS+=integer)* '}')?
            ('max_message_length' '=' maxMessageLength=integer)?
		'}'

		'frames' '{'
		    (frames+=frame)+
		'}'

		'encoding' '{'
		    (encodings+=encoding)*
		'}'

		'status_management' '{'
		    'response_error' '=' responseError=signalReference ';'
		    ('fault_state_signals' '=' faultStateSignals+=signalReference (',' faultStateSignals+=signalReference)* ';')?
		'}'

		('free_text' '{'
			freeText=String
		'}')?
	'}';

master:
	'master' name=Identifier '{'
		'general' '{'
			'LIN_protocol_version' '=' protocolVersion=String ';'
			'supplier' '=' supplier=integer ';'
			'function' '=' function=integer ';'
			'variant' '=' variant=integer ';'
			'bitrate' '=' bitrate=fixedBitrate ';'
			'timebase' '=' timebase=number 'ms' ';'
			'jitter' '=' jitter=number 'ms' ';'
		'}'

		'slaves' '{'
			(slaves+=Identifier ';')*
		'}'

		'frames' '{'
			(frames+=frame)*
		'}'

		'encoding' '{'
			(encodings+=encoding)*
		'}'

		'schedule_tables' '{'
			(scheduleTables+=scheduleTable)*
		'}'

		('free_text' '{'
			freeText=String
		'}')?
	'}';

baudrate: selectBitrate | fixedBitrate | automaticBitrate;

automaticBitrate:
	'automatic' ('min' minValue=number 'kbps')? ('max' maxValue=number 'kbps')? ;

selectBitrate:
	'select' '{' values+=number 'kbps' (',' values+=number 'kbps')* '}'
;

fixedBitrate:
	value=number 'kbps';

nadSet: nadList | nadRange;

nadList: values+=integer (',' values+=integer)*;

nadRange: minValue=integer 'to' maxValue=integer;

frame:
	(publishes='publish' | subscribes='subscribe') name=Identifier '{'
		'length' '=' length=integer ';'
		('min_period' '=' minPeriod=integer 'ms' ';')?
		('max_period' '=' maxPeriod=integer 'ms' ';')?
		('event_triggered_frame' '=' eventTriggeredFrame=frameReference ';')?
		('signals' '{'
			(signals+=signal)+
		'}')?
	'}';

signal:
	name=Identifier '{'
		'size' '=' size=integer ';'
		'init_value' '=' initialValue=signalValue ';'
		'offset' '=' offset=integer ';'
		(encodingReference ';')?
	'}';

signalValue: scalorSignalValue | arraySignalValue;
scalorSignalValue: value=integer;
arraySignalValue: '{' values+=integer (',' values+=integer)* '}';

encoding:
	name=Identifier '{'
	    (encodedValues+=encodedValue)+
	'}';

encodedValue:
	logicalEncodedValue ';' |
	physicalEncodedRange ';' |
	bcdEncodedValue ';' |
	asciiEncodedValue ';'
;

logicalEncodedValue:
	'logical_value' ',' value=integer (',' textInfo=String)?
;

physicalEncodedRange:
	'physical_value' ',' minValue=integer ',' maxValue=integer ',' scale=number ',' offset=number (',' textInfo=String)?
;

bcdEncodedValue:
	'bcd_value'
;

asciiEncodedValue:
	'ascii_value'
;

scheduleTable:
	name=Identifier '{'
		(entries+=scheduleTableEntry)+
	'}';

scheduleTableEntry:
	(frameEntry |
	masterReqEntry |
	slaveRespEntry |
	assignNADEntry |
	conditionalChangeNADEntry |
	dataDumpEntry |
	saveConfigurationEntry |
	assignFrameIdRangeEntry |
	freeFormatEntry |
	assignFrameIdEntry) 'delay' frameTime=number 'ms' ';'
;

frameEntry: frameReference;

masterReqEntry:
	'MasterReq';

slaveRespEntry:
	'SlaveResp';

assignNADEntry:
	'AssignNAD' '{' nodeReference '}';

conditionalChangeNADEntry:
	'ConditionalChangeNAD' '{'
		NAD=integer ',' id=integer ',' byte_=integer',' mask=integer',' inv=integer',' newNAD=integer
	'}';

dataDumpEntry:
	'DataDump' '{'
		nodeReference ',' d1=integer',' d2=integer',' d3=integer',' d4=integer',' d5=integer
	'}';

saveConfigurationEntry:
	'SaveConfiguration' '{' nodeReference '}';

assignFrameIdRangeEntry:
	'AssignFrameIdRange' '{'
		nodeReference ',' frameIndex=integer (',' frame0_PID=integer',' frame1_PID=integer',' frame2_PID=integer',' frame3_PID=integer)?
	'}';

freeFormatEntry:
	'FreeFormat' '{'
		d1=integer',' d2=integer',' d3=integer',' d4=integer',' d5=integer',' d6=integer',' d7=integer',' d8=integer
	'}';

assignFrameIdEntry:
	'AssignFrameId' '{' nodeReference ',' frameReference '}';

nodeReference:
    id=Identifier;

frameReference:
    id=Identifier;

signalReference:
    id=Identifier;

encodingReference:
    id=Identifier;

number: integer | Real;

integer: DecInteger | HexInteger;

Identifier: [a-zA-Z][a-zA-Z0-9_]*;

String: '"' .*? '"';

DecInteger: Digit+;

HexInteger: '0x'HexDigit+;

Real: Digit*'.'Digit+;

fragment NonDigit : [a-zA-Z_] ;

fragment Digit: [0-9];

fragment HexDigit: [0-9A-Fa-f];

Whitespace: [ \n\t\r]+ -> skip;

Newline: ( '\r' '\n'? | '\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\n\r]* -> skip;