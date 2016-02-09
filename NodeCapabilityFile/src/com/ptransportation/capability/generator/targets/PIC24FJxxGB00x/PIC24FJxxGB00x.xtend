package com.ptransportation.capability.generator.targets.PIC24FJxxGB00x

import com.ptransportation.capability.nodeCapabilityFile.Node
import com.ptransportation.capability.generator.Target
import com.ptransportation.capability.nodeCapabilityFile.Slave

class PIC24FJxxGB00x implements Target {
	override targetMatches(String targetDevice) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
	
	override getInterface(String targetInterface) {
		throw new UnsupportedOperationException("TODO: auto-generated method stub")
	}
				
    override def CharSequence headerIncludes(Node node) ''''''
    
    override def CharSequence sourceIncludes(Node node) '''
    #include <xc.h>
    #define MIN_BAUD 10000
    #define MAX_BAUD 20000
    
    #if (13*FCY/MIN_BAUD)  > 0xFFFF
        #error "FCY/MAX_BAUD is too large for a 16-bit timer!"
    #endif
	'''

    override def CharSequence headerGlobals(Node node) '''
    struct l_irqmask {
        unsigned int rx_level : 3;
        unsigned int tx_level : 3;
        «IF(node instanceof Slave)»
        unsigned int t1_level : 3;
        «ENDIF»
    };
    '''
    
    override def CharSequence sourceGlobals(Node node) ''''''
	
	override initialization(Node node) '''// TODO implement!'''
			
}