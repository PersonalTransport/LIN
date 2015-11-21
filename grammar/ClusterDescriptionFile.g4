grammar ClusterDescriptionFile;

description_file:
    'LIN_description_file' ';'
    'LIN_protocol_version' '=' VersionString ';'
    'LIN_language_version' '=' VersionString ';'
    'LIN_speed' '=' real_or_integer 'kbps' ';'
    ('Channel_name' '=' Identifier ';')?
    node_def
    (node_composition_def)?
    signal_def
    (diag_signal_def)?
    frame_def
    (sporadic_frame_def)?
    (event_triggered_frame_def)?
    (diag_frame_def)?
    node_attributes_def
    schedule_table_def
    (signal_groups_def)?
    (signal_encoding_type_def)?
    (signal_representation_def)?
    ;

node_Def:
    'Nodes' '{'
        'Master' ':' node_name ',' time_base 'ms' ',' jitter 'ms' ';'
        'Slaves' ':' node_name (',' node_name)* ';'
    '}';

node_name:
    Identifier;

time_base:
    real_or_integer;

jitter:
    real_or_integer;

node_attributes_def:
    'Node_attributes' '{'
        (node_name '{'
            'LIN_protocol' '=' protocol_version ';'
            'configured_NAD' '=' diag_address ';'
            ('initial_NAD' '=' diag_address ';')?
            attributes_def ';'
        '}')+
    '}';

protocol_version:
    VersionString;

diag_address:
    integer;

attributes_def:
    'product_id' '=' supplier_id ',' function_id ( ',' variant)? ';'
    'response_error' '=' signal_name ';'
    ('fault_state_signals' '=' signal_name(',' signal_name)* ';')
    ('P2_min' '=' real_or_integer 'ms' ';')
    ('ST_min' '=' real_or_integer 'ms' ';')
    ('N_As_timeout' '=' real_or_integer 'ms' ';')
    ('N_Cr_timeout' '=' real_or_integer 'ms' ';')
    (configurable_frames_20_def | configurable_frames_21_def);

supplier_id:
    integer;

function_id:
    integer;

variant:
    integer;

signal_name:
    Identifier;

configurable_frames_20_def:
    'configurable_frames' '{'
    (frame_name '=' message_id ';')+
    '}';

message_id:
    integer;

configurable_frames_21_def:
    'configurable_frames' '{'
        (frame_name ';')+
    '}';

node_composition_def:
    'composite' '{'
        ('configuration' configuration_name '{'
            (composite_node '{' logical_node (',' logical_node)* ';')+
        '}')+
    '}';

real_or_integer:
    Real |
    integer;

integer:
    DecInteger |
    HexInteger;

VersionString : '"'Real'"' ;

CharString: '"' (.)*?'"';

Identifier : NonDigit ( NonDigit | Digit )* ;

Real : Digit+'.'Digit+ ;

DecInteger: Digit+;

HexInteger: '0x'HexDigit+;

fragment NonDigit : [a-zA-Z_] ;

fragment Digit: [0-9];

fragment HexDigit: [0-9A-Fa-f];

Whitespace: [ \t]+ -> skip;

Newline: ( '\r' '\n'? | '\n') -> skip;

BlockComment: '/*'.*? '*/' -> skip;

LineComment: '//' ~[\r\n]* -> skip;
