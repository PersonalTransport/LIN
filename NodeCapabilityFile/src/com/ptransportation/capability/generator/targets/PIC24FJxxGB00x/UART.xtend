package com.ptransportation.capability.generator.targets.PIC24FJxxGB00x

import com.ptransportation.capability.nodeCapabilityFile.Node
import com.ptransportation.capability.generator.Interface
import com.ptransportation.capability.nodeCapabilityFile.Slave
import com.ptransportation.capability.nodeCapabilityFile.Master

/**
 * Created by aaron on 2/8/16.
 */
class UART implements Interface {
	private int version;
	
	new(int version) {
		this.version = version;
	}
	
    override def CharSequence getName()'''UART«version»'''

    override def CharSequence headerIncludes(Node node)''''''
    
    override def CharSequence sourceIncludes(Node node)''''''

    override def CharSequence headerGlobals(Node node)''''''
    
    override def CharSequence sourceGlobals(Node node)'''
    «IF(node instanceof Slave)»
    static l_u16 break_ticks_UART«version» = 0;
    
    void __attribute__((interrupt,no_auto_psv)) _IC1Interrupt() {
        if(IFS0bits.IC1IF) {
            l_u16 time = TMR1;
            TMR1 = 0;
            if(IC1CON1bits.ICM == 0x02) {
                IC1CON1bits.ICM = 0x03;
            }
            else if(IC1CON1bits.ICM == 0x03) {
                if(break_ticks_UART«version» == 0) {
                    U«version»BRG = time/52-1; // TODO this does not look right
                    break_ticks_UART«version» = 44*U«version»BRG+44; // TODO this does not look right
                    U«version»MODEbits.UARTEN = 1;
                }
                if(time >= break_ticks_UART«version») {
                    rx_buffer.state = L_BREAK_SYNC;
                    U«version»MODEbits.ABAUD = 1;
                }
                IC1CON1bits.ICM = 0x02;
            }
            IFS0bits.IC1IF = 0;
        }
    }
    «ENDIF»
    '''

	// TODO add setup for more than UART1
	override def initialization(Node node) '''
	//========================================================================//
	AD1PCFGLbits.PCFG4 = 1; // Set RB2 to digital
	AD1PCFGLbits.PCFG5 = 1; // Set RB3 to digital
	TRISBbits.TRISB2  = 0;  // Turn RB2 into output for UART1 TX
	TRISBbits.TRISB3  = 1;  // Turn RB3 into input for UART1 RX
	//========================================================================//
	
	//========================================================================//
	//Setup peripheral pin select (http://ww1.microchip.com/downloads/en/DeviceDoc/70234B.pdf#page=12)
	//Input setup                 (http://ww1.microchip.com/downloads/en/DeviceDoc/39940d.pdf#page=130)
	//Output setup                (http://ww1.microchip.com/downloads/en/DeviceDoc/39940d.pdf#page=131)
	
	//Unlock the registers by setting bit 6 of OSCCON to 0 (http://ww1.microchip.com/downloads/en/DeviceDoc/39940d.pdf#page=110)
	__builtin_write_OSCCONL(OSCCON & ~(1<<6));
	
	//Configure Output Functions
	RPOR1bits.RP2R = 3; //Assign U1TX To Pin RP2
	
	//Configure Input Functions
	RPINR18bits.U1RXR = 3; //Assign U1RX To Pin RP3
	«IF(node instanceof Slave)»
	RPINR7bits.IC1R = 3; //Assign IC1 To Pin RP3
	«ENDIF»
	
	//Lock the registers by setting bit 6 of OSCCON to 1 (http://ww1.microchip.com/downloads/en/DeviceDoc/39940d.pdf#page=110)
	__builtin_write_OSCCONL(OSCCON | (1<<6));
	//========================================================================//
	
	//========================================================================//
	// Section 21. UART http://ww1.microchip.com/downloads/en/DeviceDoc/en026583.pdf
	
	// TODO make this adjustable.
	// BRG = FCY    /(4*BAUD_RATE) - 1
	// RGB = 2000000/(4*10000     ) - 1 = 49
	U1BRG = 49; // Set Baud Rate to 10000
	
	U1MODEbits.BRGH  = 1; // High Baud Rate
	U1MODEbits.PDSEL = 0; // 8-bit data, no parity
	U1MODEbits.STSEL = 0; // One stop bit
	
	U1STAbits.UTXISEL1 = 1; // Interrupt when the last character is shifted out of the Transmit Shift Register; all transmit operations are completed. Part(1/2)
	U1STAbits.UTXISEL0 = 0; // Part (2/2)
	
	U1STAbits.URXISEL1 = 0; // TODO add coment
	U1STAbits.URXISEL0 = 0;
	
	l_sys_irq_disable();
	«IF(node instanceof Master)»
	U1MODEbits.UARTEN = 1; // Enable the UART 1 module.
	«ELSE»
	U1MODEbits.UARTEN = 0; // Disable the UART 1 module until first break.
	«ENDIF»
	//========================================================================//
	
	«IF(node instanceof Slave)»
	//========================================================================//
	// Timer 1 for break detection.
	T1CONbits.TON = 1;
	T1CONbits.TCKPS = 0;
	T1CONbits.TCS = 0;
	T1CONbits.TGATE = 0;
	T1CONbits.TSYNC = 0;
	
	while(IC1CON1bits.ICBNE != 0)
	    (unsigned int)IC1BUF;
	
	IC1CON1bits.ICSIDL = 0x00;
	IC1CON1bits.ICTSEL = 0x07;
	IC1CON1bits.ICI    = 0x00;
	IC1CON1bits.ICM    = 0x02;
	
	IC1CON2bits.IC32 = 0x00;
	IC1CON2bits.ICTRIG = 0x01;
	IC1CON2bits.SYNCSEL = 0xB;
	
	IC1TMR = 0;
	//========================================================================//
	«ENDIF»
	'''
	
	override rxBreakSync() '''break_ticks_«name» = 44*U«version»BRG+44; // TODO this does not look right'''
	
	override rxDataAvailable() '''U«version»STAbits.URXDA'''
	
	override rxData(String cVarToStoreIn) '''«cVarToStoreIn» = (l_u8)U«version»RXREG;'''
	
	override txData(String cVarToTx) '''
	U«version»STAbits.UTXEN  = 1; // Signal the send 2 clock cycles from now.
	U«version»TXREG = (l_u8)«cVarToTx»;
	'''
	
	override txBreakAndSync() '''
	// Send the break and sync.
	while(!U«version»STAbits.TRMT); // Wait for tx to complete.
	U«version»STAbits.UTXBRK = 1; // Enable a break sequence.
	U«version»STAbits.UTXEN  = 1; // Signal the send 2 clock cycles from now.
	U«version»TXREG = 0x00;  // Value is ignored.
	U«version»TXREG = 0x55; // Send the sync character.
	'''
}