package com.ptransportation.capability.generator

import com.ptransportation.capability.nodeCapabilityFile.Node
import com.ptransportation.capability.nodeCapabilityFile.Frame
import com.ptransportation.capability.nodeCapabilityFile.Master
import com.ptransportation.capability.nodeCapabilityFile.Slave
import com.ptransportation.capability.nodeCapabilityFile.Signal
import com.ptransportation.capability.nodeCapabilityFile.ScalorSignalValue
import java.util.List
import java.util.ArrayList
import com.ptransportation.capability.nodeCapabilityFile.ScheduleTableEntry
import com.ptransportation.capability.nodeCapabilityFile.AssignFrameIdEntry
import com.ptransportation.capability.nodeCapabilityFile.ScheduleTable
import com.ptransportation.capability.nodeCapabilityFile.AssignFrameIdRangeEntry
import com.ptransportation.capability.nodeCapabilityFile.AssignNADEntry
import com.ptransportation.capability.nodeCapabilityFile.MasterReqEntry
import com.ptransportation.capability.nodeCapabilityFile.SlaveRespEntry
import com.ptransportation.capability.nodeCapabilityFile.FrameEntry
import com.ptransportation.capability.nodeCapabilityFile.NadRange
import com.ptransportation.capability.nodeCapabilityFile.NadList

class DriverSourceGenerator {
	def List<Frame> getAllFrames(Node node) {
		var frames = new ArrayList<Frame>()
		if(node instanceof Master) {
			frames.addAll(node.frames.filter[it.publishes != null])
			for(Slave slave:node.slaves)
				frames.addAll(slave.frames.filter[it.publishes != null])
		}
		else {
			frames.addAll(node.frames)
		}
		return frames
	}

	def List<Frame> getPublishFrames(Node node) {
		var frames = new ArrayList<Frame>()
		frames.addAll(node.frames.filter[it.publishes != null])
		return frames;
	}

	def List<Frame> getSubscribeFrames(Node node) {
		var frames = new ArrayList<Frame>()
		if(node instanceof Master) {
			for(Slave slave:node.slaves)
				frames.addAll(slave.frames.filter[it.publishes != null])
		}
		else {
			frames.addAll(node.frames.filter[it.subscribes != null])
		}
		return frames;
	}

	def CharSequence generateNode(Target target,Interface iii,Node node)'''
	#include "«node.name».h"
	«target.sourceIncludes(node)»
	«iii.sourceIncludes(node)»

	enum frame_index {
	«FOR frame:getAllFrames(node) SEPARATOR ',\n'»
	«frame.name»_INDEX
	«ENDFOR»
	}

	enum l_buffer_state {
	    L_BREAK_SYNC,
	    L_PID,
	    L_DATA_CHECKSUM,
	    L_IDLE
	};

	enum service_identifier {
	    // 0 - 0xAF reserved
	    ASSIGN_NAD = 0xB0,
	    ASSIGN_FRAME_IDENTIFIER = 0xB1, // Obsolete??
	    READ_BY_IDENTIFIER = 0xB2,
	    CONDITIONAL_CHANGE_NAD = 0xB3,
	    DATA_DUMP = 0xB4,
	    // 0xB5 reserved
	    SAVE_CONFIGURATION = 0xB6,
	    ASSIGN_FRAME_IDENTIFIER_RANGE = 0xB7,
	    // 0xB8 - 0xFF reserved
	};

	union l_frame_data {
	    l_u8 array[8];
	    uint64_t scalar;
	    struct {
	        l_u8 NAD;
	        l_u8 PCI;
	        l_u8 SID;
	        l_u8 D1;
	        l_u8 D2;
	        l_u8 D3;
	        l_u8 D4;
	        l_u8 D5;
	    } PUD; // TODO is this the right order?
	};

	struct l_frame {
	    l_u8 PID;
	    union l_frame_data data;
	};

	struct l_ioctl_op {
	    // TODO add things here?
	};

	union l_status_register {
	    struct {
	        unsigned int error_in_response : 1;
	        unsigned int successful_transfer : 1;
	        unsigned int overrun : 1;
	        unsigned int goto_sleep: 1; // TODO update this value
	        unsigned int bus_activity : 1; // TODO update this value
	        unsigned int event_triggered_frame_collision : 1; // TODO update this value
	        unsigned int save_configuration : 1; // TODO update this value
	        unsigned int has_frame: 1;
	        unsigned int PID : 8;
	    } reg;
	    l_u16 raw_data;
	};

	«IF(node instanceof Master)»
	enum l_schedule_entry_type {
	    ASSIGN_FRAME_ID_ENTRY,//??
	    ASSIGN_FRAME_ID_RANGE_ENTRY,
	    ASSIGN_NAD_ENTRY,
	    CONDITIONAL_CHANGE_NAD_ENTRY,//??
	    //!!DATA_DUMP_ENTRY,
	    FREE_FORMAT_ENTRY,//??
	    MASTER_REQ_ENTRY,
	    SAVE_CONFIGURATION_ENTRY,
	    SLAVE_RESP_ENTRY,
	    UNCONDITIONAL_ENTRY
	};

	struct l_schedule_entry {
	    enum l_schedule_entry_type type; // TODO could be 4 bit unsigned
	    l_u8 ticks;
	    union {
	        struct l_frame *frame; // TODO change to PID!
	        l_u8 configured_NAD;
	        //TODO add others
	    } data;
	};

	struct l_schedule {
	    l_u8 size;
	    struct l_schedule_entry *entries;
	};

	struct l_schedule_instance {
	    struct l_schedule *schedule;
	    l_u8 tick;
	    l_u8 index;
	    struct l_schedule_entry *entry;
	};
	«ENDIF»

	union l_status_register «iii.name»_status = {.raw_data=0};

	static struct {
	    enum l_buffer_state state;
	    l_u8 PID;
	    union l_frame_data data;
	    l_u8 checksum;
	    l_u8 size;
	    l_u8 index;
	} tx_buffer, rx_buffer;

	static struct l_frame master_request_frame =
	{   // master request frame
	    .PID = 0x3C,
	    .data.scalar = 0ULL
	};

	static struct l_frame slave_response_frame =
	{   //  slave response frame
	    .PID = 0x3D,
	    .data.scalar = 0ULL
	};

	static struct l_frame frames[] = {
	«FOR frame:getAllFrames(node) SEPARATOR ',\n'»
	    {   // «frame.name»
	        .PID = «IF(node instanceof Master)»L_FRAME_PID(«frame.name»_INDEX)«ELSE»0x00«ENDIF»
	        .data.scalar = 0ULL // TODO initial data
	    }
	«ENDFOR»
	};

	«IF(node instanceof Master)»
	enum l_configuration_status «iii.name»_configuration_status = LD_SERVICE_IDLE;

	struct l_schedule _L_NULL_SCHEDULE = {0,NULL};

	«FOR schedule:node.scheduleTables SEPARATOR ',\n'»
	static struct l_schedule_entry _«schedule.name»_entries[] = {
		«FOR entry:schedule.entries SEPARATOR ',\n'»
		«entry.entryInitialization()»
		«ENDFOR»
	};
	struct l_schedule _<schedule.name» = {sizeof(_«schedule.name»_entries)/sizeof(struct l_schedule_entry),_«schedule.name»_entries};
	«ENDFOR»
	static struct l_schedule_instance current = {L_NULL_SCHEDULE,0,0,NULL};
	static struct l_schedule_instance next    = {NULL,0,0,NULL};

	«ELSEIF(node instanceof Slave)»
	static l_u8 NAD = «getInitialNAD(node as Slave)»; // TODO this needs to be implemented.
	static l_u16 supplier_id = «node.supplier»;
	static l_u16 function_id = «node.function»;
	static l_u8 variant_id = «node.variant»;
	«ENDIF»

	«FOR frame:getAllFrames(node)»
	static l_bool «frame.name»_flag = false;
	«FOR signal:frame.signals»
	static l_bool «signal.name»_flag = false;
	«ENDFOR»
	«ENDFOR»

	static void setup_tx_«iii.name»(l_u8 PID);
	static void setup_rx_«iii.name»(l_u8 PID);
	static void cleanup_tx_«iii.name»();
	static void cleanup_rx_«iii.name»();
	«target.sourceGlobals(node)»
	«iii.sourceGlobals(node)»

	l_bool l_sys_init() {
	    tx_buffer.PID = 0xFF;
	    tx_buffer.size = 0;
	    tx_buffer.index = 0;
	    tx_buffer.data.scalar = 0;
	    tx_buffer.state = L_IDLE;

	    rx_buffer.PID = 0xFF;
	    rx_buffer.size = 0;
	    rx_buffer.index = 0;
	    rx_buffer.data.scalar = 0;
	    rx_buffer.state = L_IDLE;

	    «target.initialization(node)»

	    return false;
	}
	«IF(node instanceof Master)»

	l_u8 l_sch_tick_«iii.name»() {
	    // TODO the spec hints at using l_sys_irq_disable
	    // but I do not see a need for it; maybe around setup/cleanup.
	    if(next.schedule != NULL && current.tick == 0) {
	        current = next;
	        next.schedule = NULL;
	    }

	    if(current.schedule == L_NULL_SCHEDULE) {
	        return 0;
	    }

	    struct l_schedule_entry *entry = current.entry;
	    switch(entry->type) {
	        case SLAVE_RESP_ENTRY:
	        case MASTER_REQ_ENTRY:
	        case UNCONDITIONAL_ENTRY: {
	            if(rx_buffer.state == L_IDLE && rx_buffer.size != 0)
	                cleanup_rx_«iii.name»();

	            if(tx_buffer.state == L_IDLE && tx_buffer.size != 0)
	                cleanup_tx_«iii.name»();

	            struct l_frame *frame = entry->data.frame;
	            if(current.tick == 0) {
	                setup_tx_«iii.name»(frame->PID);
	                setup_rx_«iii.name»(frame->PID);
	                l_ifc_tx_«iii.name»();
	            }

	            break;
	        }
	        // TODO add the rest!
	    }

	    if(current.tick >= entry->ticks-1) {
	        current.tick = 0;
	        current.index = (current.index < current.schedule->size-1) ? current.index+1 : 0;
	        current.entry = &current.schedule->entries[current.index];
	        return current.index+1;
	    }

	    current.tick++;
	    return 0;
	}

	void l_sch_set_«iii.name»(struct l_schedule *schedule, l_u8 entry) {
	    if(schedule != NULL) {
	        next.schedule = schedule;
	        next.tick     = 0;
	        next.index    = (entry > 0) ? entry-1 : 0; // TODO check if the entry is in the schedule??
	        next.entry    =  &schedule->entries[next.index];
	    }
	}
	«ENDIF»

	l_bool l_ifc_init_«iii.name»() {
	    «iii.initialization(node)»
	    return false;
	}
	«IF(node instanceof Master)»

	void l_ifc_goto_sleep_«iii.name»() {
	    // TODO implement!
	}
	«ENDIF»
	«IF(node instanceof Slave && (node as Slave).sendsWakeUpSignal != null)»

	void l_ifc_wake_up_«iii.name»()  {
	    // TODO implement!
	}
	«ENDIF»

	l_u16 l_ifc_ioctl_«iii.name»(struct l_ioctl_op op, void* pv) {
	    // TODO implement!
	}

	static inline void setup_rx_«iii.name»(l_u8 PID) {
	    rx_buffer.PID = PID;

	    if(PID == 0xff) {
	        // TODO special??
	    }
	«IF(node instanceof Slave)»
	    else if(PID == 0x3C) { // Master request frame
	        rx_buffer.state = L_DATA_CHECKSUM;
	        rx_buffer.size = 8;
	        rx_buffer.index = 0;
	    }
	«ELSE»
	    else if(PID == 0x3D) { // Slave response frame
	        rx_buffer.state = L_DATA_CHECKSUM;
	        rx_buffer.size = 8;
	        rx_buffer.index = 0;
	    }
	«ENDIF»
	«FOR frame:getSubscribeFrames(node)»
	    else if(PID == frames[«frame.name»_INDEX].PID) {
	        rx_buffer.state = L_DATA_CHECKSUM;
	        rx_buffer.size = «frame.length»;
	        rx_buffer.index = 0;
	    }
	«ENDFOR»
	    else {
	        rx_buffer.state = L_IDLE;
	        rx_buffer.size  = 0;
	        rx_buffer.index = 0;
	    }
	}

	static inline void cleanup_rx_«iii.name»() {
		l_u16 classic_checksum = 0;
		l_u16 enhanced_checksum = rx_buffer.PID;

		for(l_u8 i=0;i<rx_buffer.size;++i) {
		    classic_checksum += rx_buffer.data.array[i];
		    if(classic_checksum >= 256)
		        classic_checksum -= 255;

		    enhanced_checksum += rx_buffer.data.array[i];
		    if(enhanced_checksum >= 256)
		        enhanced_checksum -= 255;
		}
		classic_checksum = ~classic_checksum;
		enhanced_checksum = ~enhanced_checksum;

		if(rx_buffer.PID == 0xff) {
		    // TODO special??
		}
	«IF(node instanceof Slave)»
	    else if(rx_buffer.PID == 0x3C) { // Master request frame
	        if((l_u8)classic_checksum == rx_buffer.checksum) {
	            switch((enum service_identifier)rx_buffer.data.PUD.SID) {
	                case ASSIGN_FRAME_IDENTIFIER_RANGE: { // Assign frame identifier range (Mandatory)
	                    if(rx_buffer.data.PUD.NAD == NAD) {
	                        «iii.name»_status.reg.PID = rx_buffer.PID;
	                        «iii.name»_status.reg.overrun = «iii.name»_status.reg.has_frame;
	                        «iii.name»_status.reg.has_frame = true;

	                        «iii.name»_status.reg.successful_transfer = true;
	                        master_request_frame.data.scalar = rx_buffer.data.scalar; // TODO not really needed!

	                        l_u8 start_index = rx_buffer.data.PUD.D1;
	                        l_u8 *PIDs = &rx_buffer.data.PUD.D2;
	                        for(l_u8 i=0;i<4;++i) {
	                            if(PIDs[i] != 0xFF)
	                                frames[i+start_index].PID = PIDs[i];
	                        }

	                        slave_response_frame.data.PUD.NAD = NAD;
	                        slave_response_frame.data.PUD.PCI = 0x01;
	                        slave_response_frame.data.PUD.SID = 0xF7;
	                        slave_response_frame.data.PUD.D1 = 0xFF;
	                        slave_response_frame.data.PUD.D2 = 0xFF;
	                        slave_response_frame.data.PUD.D3 = 0xFF;
	                        slave_response_frame.data.PUD.D4 = 0xFF;
	                        slave_response_frame.data.PUD.D5 = 0xFF;
	                    }
	                    break;
	                }
	            }
	        }
	        else {
	            «iii.name»_status.reg.error_in_response = true;
	            «iii.name»_status.reg.successful_transfer = false;
	            // TODO should set response_error signal
	        }
	    }
		«ELSE»
	    else if(rx_buffer.PID == 0x3D) { // Slave response frame
	        «iii.name»_status.reg.PID = rx_buffer.PID;
	        «iii.name»_status.reg.overrun = «iii.name»_status.reg.has_frame;
	        «iii.name»_status.reg.has_frame = true;
	        if((l_u8)classic_checksum == rx_buffer.checksum) {
	            «iii.name»_status.reg.successful_transfer = true;
	            slave_response_frame.data.scalar = rx_buffer.data.scalar; // TODO not really needed!

	            if(tx_buffer.data.PUD.SID >= (ASSIGN_NAD + 0x40) && tx_buffer.data.PUD.SID \<= (ASSIGN_FRAME_IDENTIFIER_RANGE + 0x40)) { // Configuration
	                // TODO check PCI.
	                // TODO add checks that the NAD and RSID match the master request frame.
	                «iii.name»_configuration_status = LD_SERVICE_IDLE;
	            }
	        }
	        else {
	            «iii.name»_status.reg.error_in_response = true;
	            «iii.name»_status.reg.successful_transfer = false;
	            // TODO should set response_error signal
	            // TODO «iii.name»_configuration_status = LD_SERVICE_ERROR?
	        }
	    }
		«ENDIF»
		«FOR frame:getSubscribeFrames(node)»
		else if(rx_buffer.PID == frames[«frame.name»_INDEX].PID) {
	        «iii.name»_status.reg.PID = rx_buffer.PID;
	        «iii.name»_status.reg.overrun = «iii.name»_status.reg.has_frame;
	        «iii.name»_status.reg.has_frame = true;
			if((l_u8)«IF(frame.getUseClassicChecksum)»classic_checksum«ELSE»enhanced_checksum«ENDIF»
				«iii.name»_status.reg.successful_transfer = true;
				frames[«frame.name»_INDEX].data.scalar = rx_buffer.data.scalar;
				«frame.name»_flag = true;
				«FOR signal:frame.signals»
				«signal.name»_flag = true;
				«ENDFOR»
			}
			else {
			    «iii.name»_status.reg.error_in_response = true;
			    «iii.name»_status.reg.successful_transfer = false;
			    // TODO should set response_error signal
			}
		}
	    «ENDFOR»
	    rx_buffer.PID = 0xFF;
	    rx_buffer.size = 0;
	    rx_buffer.index = 0;
	    rx_buffer.data.scalar = 0;
	    rx_buffer.state = L_IDLE;
	}

	void l_ifc_rx_«iii.name»() {
	    while(«iii.rxDataAvailable») {
	        switch(rx_buffer.state) {
	            case L_BREAK_SYNC: {
	                rx_buffer.state = L_PID;
	                «IF(node instanceof Slave)»
	                «iii.rxBreakSync()»
	                «ENDIF»
	                break;
	            }
	            case L_PID: {
	                rx_buffer.state = L_DATA_CHECKSUM;
	                «IF(node instanceof Slave)»
	                «iii.rxData("rx_buffer.PID")»
	                setup_tx_«iii.name»(rx_buffer.PID);
	                setup_rx_«iii.name»(rx_buffer.PID);
	                l_ifc_tx_«iii.name»();
	                «ENDIF»
	                break;
	            }
	            case L_DATA_CHECKSUM: {
	                if(rx_buffer.index < rx_buffer.size) {
	                    «iii.rxData("rx_buffer.data.array[rx_buffer.index]")»
	                    rx_buffer.index++;
	                }
	                else {
	                    rx_buffer.state = L_IDLE;
	                    if(rx_buffer.size > 0) {
	                        «iii.rxData("rx_buffer.checksum")»
	                        «IF(node instanceof Slave)»
	                        cleanup_rx_«iii.name»();
	                        «ENDIF»
	                    }
	                }
	                break;
	            }
	            case L_IDLE: {
	                // TODO only the slave should eat data that this node does not care about, right?
	                «iii.rxData("l_u8 data")» // Just eat the data that this node does not care about!
	                (void)data; // Shut the compiler up about data not being used.
	                break;
	            }
	        }
	    }
	}

	static inline void setup_tx_«iii.name»(l_u8 PID) {
	    l_u16 checksum = 0;
	    tx_buffer.PID = PID;

	    if(PID == 0xff) {
	        // TODO special
	    }
	«IF(node instanceof Master)»
	    else if(PID == 0x3C) { // Master request frame
	        tx_buffer.state = L_BREAK_SYNC;
	        tx_buffer.size = 8;
	        tx_buffer.index = 0;
	        tx_buffer.data.scalar = master_request_frame.data.scalar;
	    }
	«ELSE»
	    else if(PID == 0x3D) { // Slave response frame
	        tx_buffer.state = L_DATA_CHECKSUM;
	        tx_buffer.size = 8;
	        tx_buffer.index = 0;
	        tx_buffer.data.scalar = slave_response_frame.data.scalar;
	    }
	«ENDIF»
	«FOR frame:getPublishFrames(node)»
		else if(PID == frames[«frame.name»_INDEX].PID) {
		«IF(node instanceof Master)»
			tx_buffer.state = L_BREAK_SYNC;
		«ELSE»
			tx_buffer.state = L_DATA_CHECKSUM;
	    «ENDIF»
			tx_buffer.size = <frame.length>;
			tx_buffer.index = 0;
	        «IF(!frame.getUseClassicChecksum)»checksum = PID;«ENDIF»
			tx_buffer.data.scalar = frames[«frame.name»_INDEX].data.scalar;
		}
	«ENDFOR»
	    else {
	«IF(node instanceof Master)»
	        tx_buffer.state = L_BREAK_SYNC;
	«ELSE»
	        tx_buffer.state = L_IDLE;
	«ENDIF»
	        tx_buffer.size  = 0;
	        tx_buffer.index = 0;
	    }

	    for(l_u8 i=0;i<tx_buffer.size;++i) {
	        checksum += tx_buffer.data.array[i];
	        if(checksum >= 256)
	            checksum -= 255;
	    \}
	    tx_buffer.checksum = (l_u8)~checksum;
	}

	static inline void cleanup_tx_«iii.name»() {
	    «iii.name»_status.reg.PID = tx_buffer.PID;
	    «iii.name»_status.reg.successful_transfer = true;
	    «iii.name»_status.reg.overrun = «iii.name»_status.reg.has_frame;
	    «iii.name»_status.reg.has_frame = true;

	    if(tx_buffer.PID == 0xff) {
	        // TODO special
	    }
	«IF(node instanceof Master)»
	    else if(tx_buffer.PID == 0x3C) {
	        // TODO master request
	        if(tx_buffer.data.PUD.SID >= ASSIGN_NAD && tx_buffer.data.PUD.SID <= ASSIGN_FRAME_IDENTIFIER_RANGE) { // Configuration
	            «iii.name»_configuration_status = LD_REQUEST_FINISHED;
	        }
	    }
	«ENDIF»
	    else if(tx_buffer.PID == 0x3D) {
	        // TODO slave response
	    }
	«FOR frame:getPublishFrames(node)»
		else if(tx_buffer.PID == frames[«frame.name»_INDEX].PID) {
			«frame.name»_flag = true;
		«FOR signal:frame.signals»
			«signal.name»_flag = true;
		«ENDFOR»
		}
	«ENDFOR»
	    tx_buffer.PID = 0xFF;
	    tx_buffer.size = 0;
	    tx_buffer.index = 0;
	    tx_buffer.data.scalar = 0;
	    tx_buffer.state = L_IDLE;
	}

	void l_ifc_tx_«iii.name»() {
	    switch(tx_buffer.state) {
	        case L_BREAK_SYNC: {
	            tx_buffer.state = L_PID;
	            «IF(node instanceof Master)»
	            «iii.txBreakAndSync()»
	            «ENDIF»
	            break;
	        }
	        case L_PID: {
	            tx_buffer.state = L_DATA_CHECKSUM;
	            tx_buffer.index = 0;
	            «IF(node instanceof Master)»
	            «iii.txData("tx_buffer.PID")»  // Send the PID.
	            «ENDIF»
	            break;
	        }
	        case L_DATA_CHECKSUM: {
	            if(tx_buffer.index < tx_buffer.size) {
	                «iii.txData("tx_buffer.data.array[tx_buffer.index]")» // Send the data.
	                tx_buffer.index++;
	            }
	            else {
	                tx_buffer.state = L_IDLE;
	                if(tx_buffer.size > 0) {
	                    tx_buffer.size  = 0;
	                    tx_buffer.index = 0;
	                    «iii.txData("tx_buffer.checksum")» // Send the checksum.
	                    «IF(node instanceof Slave)»
	                    cleanup_tx_«iii.name»();
	                    «ENDIF»
	                }
	            }
	            break;
	        }
	        case L_IDLE: {
	            break;
	        }
	    }
	}

	void l_ifc_aux_«iii.name»() {
	    // TODO implement!
	}

	l_u16 l_ifc_read_status_«iii.name»() {
	    l_u16 r = «iii.name»_status.raw_data;
	    «iii.name»_status.raw_data = 0;
	    return r & 0xFF7F;
	}
	«IF(node instanceof Master)»

	l_u8 ld_is_ready_«iii.name»() {
	    return «iii.name»_configuration_status;
	}

	void ld_check_response_«iii.name»(l_u8* const RSID,l_u8* const error_code) {
	    // TODO implement!
	}

	void ld_assign_frame_id_range_«iii.name»(l_u8 NAD,l_u8 start_index,const l_u8* const PIDs) {
	    // TODO implement!
	    «iii.name»_configuration_status = LD_SERVICE_BUSY;
	    master_request_frame.data.PUD.NAD = NAD;
	    master_request_frame.data.PUD.PCI = 0x06;
	    master_request_frame.data.PUD.SID = ASSIGN_FRAME_IDENTIFIER_RANGE;
	    master_request_frame.data.PUD.D1 = start_index;
	    master_request_frame.data.PUD.D2 = PIDs[0];
	    master_request_frame.data.PUD.D3 = PIDs[1];
	    master_request_frame.data.PUD.D4 = PIDs[2];
	    master_request_frame.data.PUD.D5 = PIDs[3];
	}

	void ld_assign_NAD_«iii.name»(l_u8 initial_NAD,l_u16 supplier_id,l_u16 function_id,l_u8 new_NAD) {
	    // TODO implement!
	    «iii.name»_configuration_status = LD_SERVICE_BUSY;
	    master_request_frame.data.PUD.NAD = initial_NAD;
	    master_request_frame.data.PUD.PCI = 0x06;
	    master_request_frame.data.PUD.SID = ASSIGN_NAD;
	    master_request_frame.data.PUD.D1 = (l_u8)supplier_id;
	    master_request_frame.data.PUD.D2 = (l_u8)(supplier_id \>\> 8);
	    master_request_frame.data.PUD.D3 = (l_u8)function_id;
	    master_request_frame.data.PUD.D4 = (l_u8)(function_id \>\> 8);
	    master_request_frame.data.PUD.D5 = new_NAD;
	}

	void ld_save_configuration_«iii.name»(l_u8 NAD) {
	    // TODO implement!
	    «iii.name»_configuration_status = LD_SERVICE_BUSY;
	    master_request_frame.data.PUD.NAD = NAD;
	    master_request_frame.data.PUD.PCI = 0x01;
	    master_request_frame.data.PUD.SID = SAVE_CONFIGURATION;
	    master_request_frame.data.PUD.D1 = 0xFF;
	    master_request_frame.data.PUD.D2 = 0xFF;
	    master_request_frame.data.PUD.D3 = 0xFF;
	    master_request_frame.data.PUD.D4 = 0xFF;
	    master_request_frame.data.PUD.D5 = 0xFF;
	}

	void ld_conditional_change_NAD_«iii.name»(l_u8 NAD,l_u8 id,l_u8 byte,l_u8 mask,l_u8 invert,l_u8 new_NAD) {
	    // TODO implement!
	    «iii.name»_configuration_status = LD_SERVICE_BUSY;
	    master_request_frame.data.PUD.NAD = NAD;
	    master_request_frame.data.PUD.PCI = 0x06;
	    master_request_frame.data.PUD.SID = CONDITIONAL_CHANGE_NAD;
	    master_request_frame.data.PUD.D1 = id;
	    master_request_frame.data.PUD.D2 = byte;
	    master_request_frame.data.PUD.D3 = mask;
	    master_request_frame.data.PUD.D4 = invert;
	    master_request_frame.data.PUD.D5 = new_NAD;
	}

	void ld_read_by_id_«iii.name»(l_u8 NAD,l_u16 supplier_id,l_u16 function_id,l_u8 id,l_u8* const data) {
	    // TODO implement!
	    «iii.name»_configuration_status = LD_SERVICE_BUSY;
	}
	«ELSE»

	l_u8 ld_read_configuration_«iii.name»(l_u8* const data,l_u8* const length) {
	    // TODO implement!
	}

	l_u8 ld_set_configuration_«iii.name»(const l_u8* const data,l_u16 length) {
	    // TODO implement!
	}
	«ENDIF»

	«FOR frame:getAllFrames(node)»
	«generateTestFlag(frame.name)»
	«FOR signal:frame.signals»

	«generateTestFlag(signal.name)»
	«ENDFOR»
	«ENDFOR»

	«FOR frame:getAllFrames(node)»
	«generateClearFlag(frame.name)»
	«FOR signal:frame.signals»

	«generateClearFlag(signal.name)»
	«ENDFOR»
	«ENDFOR»

	«FOR frame:getAllFrames(node)»
	«FOR signal:frame.signals»

	«generateSignalRead(signal)»
	«ENDFOR»
	«ENDFOR»

	«FOR frame:getPublishFrames(node)»
	«FOR signal:frame.signals»

	«generateSignalWrite(signal)»
	«ENDFOR»
	«ENDFOR»
	'''

	def getInitialNAD(Slave node) {
		if(node.nadSet instanceof NadRange) {
			var range = node.nadSet as NadRange;
			return range.minValue
		}
		else {
			var list = node.nadSet as NadList;
			return list.values.get(0);
		}
	}

	def boolean getUseClassicChecksum(Frame frame) {
		return Double.parseDouble(((frame.eContainer() as Node).protocolVersion.replaceAll("\"",""))) < 2.0;
	}

	def CharSequence generateSignalRead(Signal signal) '''
	«IF(signal.initialValue instanceof ScalorSignalValue)»
	/**
	*
	* @returns current value of «signal.name».
	**/
	«IF(Integer.decode(signal.size) <= 1)»
	l_bool l_bool_rd_«signal.name»() {
		return (l_bool)((frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar >> 0x«Integer.toHexString(Integer.decode(signal.offset))») & 0x«Integer.toHexString(signal.getSignalMask)»ULL);
	}
	«ELSEIF(Integer.decode(signal.size) <= 8)»
	l_u8 l_u8_rd_«signal.name»() {
		return (l_u8)((frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar >> 0x«Integer.toHexString(Integer.decode(signal.offset))») & 0x«Integer.toHexString(signal.getSignalMask)»ULL);
	}
	«ELSEIF(Integer.decode(signal.size) <= 16)»
	l_u16 l_u16_rd_«signal.name»() {
		return (l_u16)((frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar >> 0x«Integer.toHexString(Integer.decode(signal.offset))») & 0x«Integer.toHexString(signal.getSignalMask)»ULL);
	}
	«ENDIF»
	«ELSE»
	void l_bytes_rd_«signal.name»(l_u8 start,l_u8 count,l_u8* const data) {
		// TODO check for count being to large??
	    for(l_u8 i=0;i<count;++i)
	    	data[i] = frames[«(signal.eContainer as Frame).name»_INDEX].data[«signal.offset»/8+start+i];
	}
	«ENDIF»
	'''


    def CharSequence generateSignalWrite(Signal signal) '''
	«IF(signal.initialValue instanceof ScalorSignalValue)»
	«IF(Integer.decode(signal.size) <= 1)»
	void l_bool_wr_«signal.name»(l_bool v) {
		frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar &= ~(0x«Integer.toHexString(signal.getSignalMask)»ULL << 0x«Integer.toHexString(Integer.decode(signal.offset))»);
		frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar |= (((uint64_t)v & 0x«Integer.toHexString(signal.getSignalMask)»ULL) << 0x«Integer.toHexString(Integer.decode(signal.offset))»);
	}
	«ELSEIF(Integer.decode(signal.size) <= 8)»
	void l_u8_wr_«signal.name»(l_u8 v) {
		frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar &= ~(0x«Integer.toHexString(signal.getSignalMask)»ULL << 0x«Integer.toHexString(Integer.decode(signal.offset))»);
		frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar |= (((uint64_t)v & 0x«Integer.toHexString(signal.getSignalMask)»ULL) << 0x«Integer.toHexString(Integer.decode(signal.offset))»);
	}
	«ELSEIF(Integer.decode(signal.size) <= 16)»
	void l_u16_wr_«signal.name»(l_u16 v) {
		frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar &= ~(0x«Integer.toHexString(signal.getSignalMask)»ULL << 0x«Integer.toHexString(Integer.decode(signal.offset))»);
		frames[«(signal.eContainer as Frame).name»_INDEX].data.scalar |= (((uint64_t)v & 0x«Integer.toHexString(signal.getSignalMask)»ULL) << 0x«Integer.toHexString(Integer.decode(signal.offset))»);
	}
	«ENDIF»
	«ELSE»
	void l_bytes_wr_«signal.name»(l_u8 start,l_u8 count,const l_u8* const data) {
		// TODO
	}
	«ENDIF»
    '''

    def int getSignalMask(Signal signal) {
    	(1..Integer.decode(signal.getSize())+1).reduce[value,i | value * 2]-1
    }

    def CharSequence generateTestFlag(String name) '''
	l_bool l_flg_tst_«name»() {
		return «name»_flag;
	}
    '''

    def CharSequence generateClearFlag(String name) '''
	void l_flg_clr_«name»() {
		«name»_flag = false;
	}
	'''

	def CharSequence entryInitialization(ScheduleTableEntry entry) {
		if(entry instanceof MasterReqEntry) {
			return entryInitialization(entry)
		}
		else if(entry instanceof SlaveRespEntry) {
			return entryInitialization(entry)
		}
		else if(entry instanceof FrameEntry) {
			return entryInitialization(entry)
		}
		else {
			throw new UnsupportedOperationException()
		}
	}


	def CharSequence entryInitialization(MasterReqEntry entry) '''{.type = MASTER_REQ_ENTRY,.ticks = «entry.ticks»,.data.frame = &master_request_frame}'''

	def CharSequence entryInitialization(SlaveRespEntry entry) '''{.type = SLAVE_RESP_ENTRY,.ticks = «entry.ticks»,.data.frame = &slave_response_frame}'''

	def CharSequence entryInitialization(FrameEntry entry) '''{.type = UNCONDITIONAL_ENTRY,.ticks = «entry.ticks»,.data.frame = &frames[«entry.frame.name»_INDEX]}'''

	def int ticks(ScheduleTableEntry entry) {
		var scheduleTable = entry.eContainer() as ScheduleTable;
		var master = scheduleTable.eContainer() as Master;
		var frameTime = Double.parseDouble(entry.getFrameTime());
        var timebase = Double.parseDouble(master.getTimebase());
        return Math.ceil(frameTime/timebase).doubleValue as int;
	}
}
