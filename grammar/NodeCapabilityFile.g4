grammar NodeCapabilityFile;

node_capability_file:
    'node_capability_file' ';'
    'LIN_language_version' '=' VersionString ';'
    node_definition+
    EOF;

node_definition :
    'node' name=node_name '{'
        general_definition
        diagnostic_definition
        frame_definition
        encoding_definition
        status_management
        free_text_definition?
    '}';

node_name:
    Identifier;

general_definition:
    'general' '{'
        'LIN_protocol_version' '=' VersionString ';'
        'supplier' '=' supplier_id ';'
        'function' '=' function_id ';'
        'variant' '=' variant_id ';'
        'bitrate' '=' bitrate_definition ';'
        'sends_wake_up_signal' '=' YesOrNo ';'
    '}';

supplier_id:
    integer;

function_id:
    integer;

variant_id:
    integer;

bitrate_definition:
      'automatic' ('min' bitrate)? ('max' bitrate)?
    | 'select' '{' bitrate (',' bitrate)+ '}'
    | bitrate;

bitrate :  real_or_integer 'kbps';

diagnostic_definition:
    'diagnostic' '{'
         'NAD' '=' (integer (',' integer)* | (integer 'to' integer)) ';'
         'diagnostic_class' '=' integer ';'
         ('P2_min' '=' real_or_integer 'ms' ';')?
         ('ST_min' '=' real_or_integer 'ms' ';')?
         ('N_As_timeout' '=' real_or_integer 'ms' ';')?
         ('N_Cr_timeout' '=' real_or_integer 'ms' ';')?
         ('support_sid' '{' integer (',' integer)* '}' ';')?
         ('max_message_length' '=' integer ';')?
    '}';

frame_definition:
    'frames' '{'
        single_frame+
    '}';

single_frame:
    frame_kind frame_name '{'
        frame_properties
        signal_definition?
    '}';

frame_kind:
    'publish' |
    'subscribe';

frame_name:
    Identifier;

frame_properties:
    'length' '=' integer ';'
    ('min_period' '=' integer 'ms' ';')?
    ('max_period' '=' integer 'ms' ';')?
    ('event_triggered_frame' '=' Identifier';')? ;

signal_definition:
    'signals' '{'
        (signal_name '{' signal_properties '}')+
    '}';

signal_name:
    Identifier;

signal_properties:
    'size' '=' integer ';'
    init_value
    'offset' '=' integer ';'
    (encoding_name ';')?;

init_value:
     (init_value_scalar | init_value_array) ';';

init_value_scalar:
    'init_value' '=' integer;

init_value_array:
     'init_value' '=' '{' integer (',' integer)* '}';

encoding_definition:
    'encoding' '{'
        (encoding_name '{'
            (logical_value | physical_range | bcd_value | ascii_value)+
        '}')+
    '}';

encoding_name:
    Identifier;

logical_value:
     'logical_value' ',' signal_value (',' text_info)? ';';

physical_range:
    'physical_value' ',' min_value ',' max_value ',' scale ',' offset (',' text_info)? ';' ;

bcd_value:
    'bcd_value';

ascii_value:
    'ascii_value';

signal_value:
    integer;

min_value:
    integer;

max_value:
    integer;

scale:
    real_or_integer;

offset:
    real_or_integer;

text_info:
    CharString;

status_management:
    'status_management' '{'
        'response_error' '=' Identifier ';'
        ('fault_state_signals' '=' Identifier (',' Identifier)* ';')?
    '}';

free_text_definition:
    'free_text' '{'
        CharString
    '}';

real_or_integer:
    Real |
    integer;

integer:
    DecInteger |
    HexInteger;

YesOrNo:
     '"yes"'
    |'"no"';

VersionString : '"' Real '"' ;

CharString: '"' (.)*? '"';

Identifier : NonDigit ( NonDigit | Digit )* ;

Real : Digit+'.'Digit+ ;

DecInteger: Digit+;

HexInteger: '0x'HexDigit+;

fragment NonDigit : [a-zA-Z_] ;

fragment Digit: [0-9];

fragment HexDigit: [0-9A-Fa-f];

Whitespace: [ \t]+ -> skip;

Newline: ( '\r' '\n'? | '\n') -> skip;

BlockComment: '/*' .*? '*/' -> skip;

LineComment: '//' ~[\r\n]* -> skip;
