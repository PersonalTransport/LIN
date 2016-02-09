package com.ptransportation.capability.generator

import com.ptransportation.capability.nodeCapabilityFile.Node
import com.ptransportation.capability.nodeCapabilityFile.Master
import com.ptransportation.capability.nodeCapabilityFile.Slave
import com.ptransportation.capability.nodeCapabilityFile.ScalorSignalValue
import com.ptransportation.capability.nodeCapabilityFile.Signal
import java.util.ArrayList
import com.ptransportation.capability.nodeCapabilityFile.Frame

class DriverHeaderGenerator {
	
	def CharSequence generateNode(Target target,Interface iii,Node node)'''
	#ifndef «node.name»_«iii.name»_DRIVER_H
	#define «node.name»_«iii.name»_DRIVER_H
	
	#include <stdlib.h>
	#include <stdint.h>
	#include <stdbool.h>
	«target.headerIncludes(node)»
	«iii.headerIncludes(node)»
	
	#define L_FRAME_PID(id) (~((id << 6) ^ (id << 4) ^ (id << 3) ^ (id << 2)) & 0x80) |\
	                         (((id << 6) ^ (id << 5) ^ (id << 4) ^ (id << 2)) & 0x40) |\
	                         (id & 0x3F)
	
	typedef bool l_bool;        //- 0 is false, and non-zero (>0) is true.
	typedef uint8_t l_u8;       //- Unsigned 8 bit integer.
	typedef uint16_t l_u16;     //- Unsigned 16 bit integer.
	
	struct l_ioctl_op;
	struct l_irqmask;
	
	«IF(node instanceof Master)»
	struct l_schedule;
	
	enum l_configuration_status {
	    LD_SERVICE_BUSY,
	    LD_REQUEST_FINISHED,
	    LD_SERVICE_IDLE,
	    LD_SERVICE_ERROR
	};
	
	extern struct l_schedule _L_NULL_SCHEDULE;
	«FOR schedule:node.scheduleTables»
	extern struct l_schedule _«schedule.name»;
	«ENDFOR»
	
	#define L_NULL_SCHEDULE &_L_NULL_SCHEDULE
	«FOR schedule:node.scheduleTables»
	#define «schedule.name» &_«schedule.name»
	«ENDFOR»
	«ENDIF»
	
	«target.headerGlobals(node)»
	«iii.headerGlobals(node)»
	
	/**
	* Initialize the core api.
	*
	* @NOTE l_sys_init must be called before
	*       any other Core API functions.
	*
	* @returns true if failed.
	*          false otherwise.
	**/
	l_bool l_sys_init();
	
	«IF(node instanceof Master)»
	/**
	* Advances the current schedule one tick.
	* When the end of a schedule is reached
	* the schedule starts over. It will also
	* update the signals that have been relieved
	* since the last call.
	*
	* @NOTE The frequency that this function is called
	*       sets the time base of the cluster, because of
	*       this make that frequency is as stable
	*       as possible.
	*
	* @returns next schedule entry's number if the next call to l_sch_tick_«iii.name»
	*               will start the transmission of a frame.
	*          zero if the next call to l_sch_tick_«iii.name»
	*               will not start a transmission of a frame.
	*
	*
	**/
	l_u8 l_sch_tick_«iii.name»();
	
	/**
	*
	* Sets the next schedule to be followed
	* once the current schedule starts it's
	* next entry.
	*
	* @NOTE be careful entries in schedules are
	*       one based indexing NOT zero!
	*
	* @param schedule the schedule to start.
	* @param entry the entry to start the schedule at 0 or 1
	               will start the schedule from the start.
	**/
	void l_sch_set_«iii.name»(struct l_schedule *schedule, l_u8 entry);
	«ENDIF»
	
	/**
	*
	* Setup the hardware for the interface
	* «iii.name», such as baud rate.
	* It also sets the current schedule
	* table to the L_NULL_SCHEDULE;
	*
	* @NOTE l_ifc_init_«iii.name» must be called before
	*       any other interface functions.
	*
	* @returns true if failed false otherwise.
	**/
	l_bool l_ifc_init_«iii.name»();
	
	«IF(node instanceof Master)»
	/**
	*
	* Will schedule a goto sleep command when the
	* next schedule entry is due. If the command
	* was successfully transmitted the goto sleep
	* bit will be set in the status register.
	*
	* @NOTE this does change the MCU's power state
	*       that is up to the application to do.
	**/
	void l_ifc_goto_sleep_«iii.name»();
	«ENDIF»
	«IF(node instanceof Slave && (node as Slave).sendsWakeUpSignal != null)»
	/**
	*
	* Transmit one wake up signal directly when
	* this function is called.
	*
	* @NOTE this only transmits one wake up signal
	*       it is up to the application to follow
	*       the wake up sequence defined in section
	*       2.6.2 of the LIN spec.
	**/
	void l_ifc_wake_up_«iii.name»();
	«ENDIF»
	
	/**
	*
	* l_ifc_ioctl_«iii.name» is used for hardware
	* specific parameters such as wakeup detection
	* and currently is not implemented.
	**/
	l_u16 l_ifc_ioctl_«iii.name»(struct l_ioctl_op op, void* pv);
	
	/**
	*
	* l_ifc_rx_«iii.name» must be called by the
	* application when data is received, on UART based
	* implementations it will be the reception of one
	* char of data.
	**/
	void l_ifc_rx_«iii.name»();
	
	/**
	*
	* l_ifc_tx_«iii.name» must be called by the
	* application when data is transmitted, on UART based
	* implementations it will be the transition of one
	* char of data.
	**/
	void l_ifc_tx_«iii.name»();
	
	/**
	* Synchronize to the break sync field
	* transmission.
	**/
	void l_ifc_aux_«iii.name»();
	
	/**
	* @returns status of the previous communication.
	*          for more details see section 7.2.5.8
	*          of the LIN spec.
	**/
	l_u16 l_ifc_read_status_«iii.name»();
	
	/**
	* l_sys_irq_disable MUST be provided
	* by the application and it must disable
	* any LIN specific interrupts.
	*
	* @returns the interrupt mask.
	**/
	struct l_irqmask l_sys_irq_disable();
	
	/**
	* l_sys_irq_restore MUST be provided
	* by the application and it must restore
	* any LIN specific interrupts.
	*
	* @param the interrupt mask.
	**/
	void l_sys_irq_restore(struct l_irqmask previous);
	
	«IF(node instanceof Master)»
	/**
	* @returns the status of the last configuration service.
	**/
	l_u8 ld_is_ready_«iii.name»();
	
	/**
	* returns the result of the last configuration service.
	* @param RSID where to store the response service identifier.
	* @param error_code where to store the error_code if any.
	**/
	void ld_check_response_«iii.name»(l_u8* const RSID,l_u8* const error_code);
	
	/**
	* Assign the PIDs of 4 frames with values or don't cares.
	* @param NAD the NAD address of the slave frames which to assign
	* @param start_index the index of the first frame in the slave to assing PID to.
	* @param PIDs an array of 4 PIDS to assign or don't care 0xFF.
	**/
	void ld_assign_frame_id_range_«iii.name»(l_u8 NAD,l_u8 start_index,const l_u8* const PIDs);
	
	/**
	* Assign the slave with supplier_id,function_id,and initial_NAD a new NAD address.
	* @param initial_NAD the initial NAD of the target slave.
	* @param supplier_id the supplier ID of the target slave.
	* @param function_id the function ID of the target slave.
	* @param new_NAD the new NAD to assign the target slave.
	**/
	void ld_assign_NAD_«iii.name»(l_u8 initial_NAD,l_u16 supplier_id,l_u16 function_id,l_u8 new_NAD);
	
	/**
	* Make a save configuration request to a slave, or all slaves
	* if NAD is the broadcast NAD(0x7F).
	* @param NAD the address of the slave.
	**/
	void ld_save_configuration_«iii.name»(l_u8 NAD);
	
	/**
	* TODO documentation!
	**/
	void ld_conditional_change_NAD_«iii.name»(l_u8 NAD,l_u8 id,l_u8 byte,l_u8 mask,l_u8 invert,l_u8 new_NAD);
	
	/**
	* TODO documentation!
	**/
	void ld_read_by_id_«iii.name»(l_u8 NAD,l_u16 supplier_id,l_u16 function_id,l_u8 id,l_u8* const data);
	«ELSE»
	
	/**
	* TODO documentation!
	**/
	l_u8 ld_read_configuration_«iii.name»(l_u8* const data,l_u8* const length);
	
	/**
	* TODO documentation!
	**/
	l_u8 ld_set_configuration_«iii.name»(const l_u8* const data,l_u16 length);
	«ENDIF»
	
	«FOR frame:getFrames(node)»
	«generateTestFlag(frame.name)»
	«FOR signal:frame.signals»
	
	«generateTestFlag(signal.name)»
	«ENDFOR»
	«ENDFOR»
	
	«FOR frame:getFrames(node)»
	«generateClearFlag(frame.name)»
	«FOR signal:frame.signals»
	
	«generateClearFlag(signal.name)»
	«ENDFOR»
	«ENDFOR»
	
	«FOR frame:getFrames(node)»
	«FOR signal:frame.signals»
	
	«generateSignalRead(signal)»
	«ENDFOR»
	«ENDFOR»
	
	«FOR frame:getFrames(node).filter[it.publishes != null]»
	«FOR signal:frame.signals»
	
	«generateSignalWrite(signal)»
	«ENDFOR»
	«ENDFOR»
	#endif //«node.name»_«iii.name»_DRIVER_H
	'''
	
	def ArrayList<Frame> getFrames(Node node) {
		// TODO save theses for performance
		var frames = new ArrayList<Frame>()
		frames.addAll(node.frames)
		if(node instanceof Master) {
			for(Slave slave:node.slaves)
				frames.addAll(slave.frames)
			
		}
		return frames;
	}

	def CharSequence generateSignalRead(Signal signal) '''
	«IF(signal.initialValue instanceof ScalorSignalValue)»
	/**
	*
	* @returns current value of «signal.name».
	**/
	«IF(Integer.decode(signal.size) <= 1)»
	l_bool l_bool_rd_«signal.name»();
	«ELSEIF(Integer.decode(signal.size) <= 8)»
	l_u8 l_u8_rd_«signal.name»();
	«ELSEIF(Integer.decode(signal.size) <= 16)»
	l_u16 l_u16_rd_«signal.name»();
	«ENDIF»
	«ELSE»
	/**
	*
	* Get the current value of «signal.name».
	*
	* @param start first byte to read from.
	* @param count number of bytes to read.
	* @param data where to store the data.
	**/
	void l_bytes_rd_«signal.name»(l_u8 start,l_u8 count,l_u8* const data);
	«ENDIF»
	'''


    def CharSequence generateSignalWrite(Signal signal) '''
    «IF(signal.initialValue instanceof ScalorSignalValue)»
    /**
    *
    * @param v the value to assign «signal.name» to.
    **/
    «IF(Integer.decode(signal.size) <= 1)»
    void l_bool_wr_«signal.name»(l_bool v);
    «ELSEIF(Integer.decode(signal.size) <= 8)»
    void l_u8_wr_«signal.name»(l_u8 v);
    «ELSEIF(Integer.decode(signal.size) <= 16)»
    void l_u16_wr_«signal.name»(l_u16 v);
    «ENDIF»
    «ELSE»
    /**
    *
    * Set the value of «signal.name».
    *
    * @param start first byte to write to.
    * @param count number of bytes to write.
    * @param data where to get the data from.
    **/
    void l_bytes_wr_«signal.name»(l_u8 start,l_u8 count,const l_u8* const data);
    «ENDIF»
    '''




    def CharSequence generateTestFlag(String name) '''
    /**
    * @returns if the «name» has been received/transmitted by the driver.
    **/
    l_bool l_flg_tst_«name»();
    '''



    def CharSequence generateClearFlag(String name) '''
    /**
    * Informs then driver that the application has consumed/published «name»'s new value.
    **/
    void l_flg_clr_«name»();
    '''
}
