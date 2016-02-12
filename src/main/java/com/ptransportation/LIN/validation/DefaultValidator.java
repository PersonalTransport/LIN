package com.ptransportation.LIN.validation;

import com.ptransportation.LIN.ErrorModel;
import com.ptransportation.LIN.model.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DefaultValidator extends AbstractValidator {
    public static List<Double> LIN_VERSIONS = Arrays.asList(1.0, 1.1, 1.2, 1.3, 2.0, 2.1, 2.2);

    public DefaultValidator(ErrorModel errorModel) {
        super(errorModel);
    }

    @Check
    public void checkThatArraySignalValueHasAtLeastOneValue(ArraySignalValue value) {
        if (value.getValues().size() <= 0)
            error("Array signal values must have a least one value.", value, "values");
    }

    @Check
    public void checkThatArraySignalValuesAreGreaterThanOrEqualToZero(ArraySignalValue value) {
        List<Integer> values = value.getValues();
        for (int i = 0; i < values.size(); ++i)
            if (values.get(i) < 0)
                error("Array signal values must be greater than or equal to zero.", value, "values", i);
    }

    @Check
    void checkThatAssignFrameIdEntrySlaveIsNotNull(AssignFrameIdEntry entry) {
        if (entry.getSlave() == null)
            error("Invalid reference to slave.", entry, "slave");
    }

    @Check
    void checkThatAssignFrameIdEntryFrameIsNotNull(AssignFrameIdEntry entry) {
        if (entry.getFrame() == null)
            error("Invalid reference to frame.", entry, "frame");
    }

    @Check
    void checkThatAssignFrameIdRangeEntrySlaveIsNotNull(AssignFrameIdRangeEntry entry) {
        if (entry.getSlave() == null)
            error("Invalid reference to slave.", entry, "slave");
    }

    @Check
    void checkThatAssignFrameIdRangeEntrySlaveStartIndexIsGreaterThanZero(AssignFrameIdRangeEntry entry) {
        if (entry.getStartIndex() < 0)
            error("Invalid start index must be greater than zero.", entry, "startIndex");
    }

    @Check
    void checkThatAssignFrameIdRangeEntrySlaveStartIndexIsLessThanTheNumberOfFramesInSlave(AssignFrameIdRangeEntry entry) {
        int frameSize = entry.getSlave().getFrames().size();
        if (entry.getStartIndex() < frameSize)
            error("Invalid start index slave '" + entry.getSlave().getName() + "' only has '" + frameSize + "' frames.", entry, "startIndex");
    }

    // TODO checks on AssignFrameIdRangeEntry PIDS

    @Check
    void checkThatAssignNADEntrySlaveIsNotNull(AssignNADEntry entry) {
        if (entry.getSlave() == null)
            error("Invalid reference to slave.", entry, "slave");
    }

    @Check
    public void checkThatAutomaticBitrateMinIsInTheRange1To20(AutomaticBitrate bitrate) {
        if (bitrate.getMinValue() < 1 || bitrate.getMinValue() > 20) // TODO move these to CONSTANTS.
            error("Invalid minimum bitrate '" + bitrate.getMinValue() +
                    "' kbps. Minimum bitrate must be in the range [1,20]kbps.", bitrate, "minValue");
    }

    @Check
    public void checkThatAutomaticBitrateMaxIsInTheRange1To20(AutomaticBitrate bitrate) {
        if (bitrate.getMaxValue() < 1 || bitrate.getMaxValue() > 20) // TODO move these to CONSTANTS.
            error("Invalid maximum bitrate '" + bitrate.getMaxValue() + "' kbps. Maximum bitrate must be in the range [1,20]kbps.",
                    bitrate, "maxValue");
    }

    @Check
    public void checkThatAutomaticBitrateMinIsLessOrEqualToMax(AutomaticBitrate bitrate) {
        if (bitrate.getMinValue() > bitrate.getMaxValue())
            error("Invalid minimum bitrate '" + bitrate.getMinValue() + "' kbps greator than maximum bitrate '" + bitrate.getMaxValue() + "' kbps.",
                    bitrate, "minValue");
    }

    // TODO ConditionalChangeNADEntry checks.

    // TODO DataDumpEntry checks.

    @Check
    public void checkThatFixedBitrateIsInRange1To20(FixedBitrate bitrate) {
        if (bitrate.getValue() < 1 || bitrate.getValue() > 20) // TODO move these to CONSTANTS.
            error("Invalid bitrate '" + bitrate.getValue() + "' kbps. Bitrates must be in the range [1,20]kbps.",
                    bitrate, "value");

    }

    @Check
    public void checkThatFrameNodeIsNodeNull(Frame frame) {
        if (frame.getNode() == null)
            error("Invalid reference to node.", frame, "node");
    }

    @Check
    public void checkThatFrameNameIsNotNull(Frame frame) {
        if (frame.getName() == null)
            error("Invalid frame name.", frame, "name");
    }

    // TODO check that the frame name is a valid identifier.

    @Check
    public void checkThatFrameLengthIsOneToEight(Frame frame) {
        if (frame.getLength() < 1 || frame.getLength() > 8) // TODO move these to CONSTANTS.
            error("Invalid frame length '" + frame.getLength() + "'. The frame length must in the range [1,8].", frame, "length");
    }

    /* TODO what should happen with these?
    @Check
    public void checkThatFrameMinPeriodIsGreaterThanZero(Frame frame) {
        if (frame.getMinPeriod() <= 0)
            error("Invalid frame minimum period '" + frame.getMinPeriod() + "'. The frame minimum period must be greater than zero.", frame, "minPeriod");
    }

    @Check
    public void checkThatFrameMaxPeriodIsGreaterThanZero(Frame frame) {
        if (frame.getMaxPeriod() <= 0)
            error("Invalid frame maximum period '" + frame.getMaxPeriod() + "'. The frame maximum period must be greater than zero.", frame, "maxPeriod");
    }

    @Check
    public void checkThatFrameMaxPeriodIsGreaterThanMinPeriod(Frame frame) {
        if (frame.getMaxPeriod() <= frame.getMinPeriod())
            error("Invalid frame maximum period '" + frame.getMaxPeriod() + "'. The frame maximum period must be greater than frame minimum period.", frame, "maxPeriod");
    }*/

    @Check
    public void checkThatFrameDoesNotContainANullSignal(Frame frame) {
        List<Signal> signals = frame.getSignals();
        for (int i = 0; i < signals.size(); ++i)
            if (signals.get(i) == null)
                error("Frame can not contain a null signal.", frame, "signals", i);
    }

    @Check
    public void checkThatFrameEntryFrameIsNotNull(FrameEntry entry) {
        if (entry.getFrame() == null)
            error("Invalid reference to frame.", entry, "frame");
    }

    // TODO FreeFormatEntry checks.

    // TODO LogicalEncodedValue checks.

    @Check
    public void checkThatMasterTimebaseIsGreaterThanZero(Master master) {
        if (master.getTimebase() <= 0)
            error("Invalid master timebase '" + master.getTimebase() + "' ms. Master timebase must be greater than zero.", master, "timebase");
    }

    @Check
    public void checkThatMasterJitterIsGreaterThanOrEqualToZero(Master master) {
        if (master.getJitter() < 0)
            error("Invalid master jitter '" + master.getJitter() + "' ms. Master jitter must be greater than or equal to zero.", master, "jitter");
    }

    @Check
    public void checkThatMasterTimebaseIsGreaterThanJitter(Master master) {
        // TODO maybe be more strict here.
        if (master.getJitter() >= master.getTimebase())
            error("Invalid master timebase '" + master.getTimebase() + "' ms. Master timebase must be greater than jitter.", master, "jitter");
    }

    @Check
    public void checkThatMasterContainsAtLeastOneSlave(Master master) {
        if (master.getSlaves().size() <= 0)
            error("Master must contain at least one slave.", master, "slaves");
    }

    @Check
    public void checkThatMasterDoesNotContainANullSlave(Master master) {
        List<Slave> slaves = master.getSlaves();
        for (int i = 0; i < slaves.size(); ++i)
            if (slaves.get(i) == null)
                error("Master can not contain a null Slave.", master, "slaves", i);
    }

    @Check
    public void checkThatMasterDoesNotContainANullScheduleTable(Master master) {
        List<ScheduleTable> scheduleTables = master.getScheduleTables();
        for (int i = 0; i < scheduleTables.size(); ++i)
            if (scheduleTables.get(i) == null)
                error("Master can not contain a null ScheduleTable.", master, "scheduleTables", i);
    }

    // TODO MasterReqEntry checks?

    @Check
    public void checkThatNadListContainsAtLeastOneNAD(NadList list) {
        if (list.getValues().size() <= 0)
            error("NAD list must contain a least one NAD.", list, "values");
    }

    @Check
    public void checkThatNadListValuesAreValid(NadList list) {
        List<Integer> values = list.getValues();
        for (int i = 0; i < values.size(); ++i) {
            int value = values.get(i);
            if (value < 0x01 || value > 0x7D) // TODO move these to CONSTANTS.
                error("Invalid slave NAD address '0x" + Integer.toHexString(value) +
                        "'. Slave NAD addresses must be in the range [0x01,0x7D]", list, "values", i);
        }
    }

    @Check
    public void checkThatNadRangeMinIsValid(NadRange range) {
        if (range.getMinValue() < 0x01 || range.getMinValue() > 0x7D) // TODO move these to CONSTANTS.
            error("Invalid minimum slave NAD address '" + range.getMinValue() +
                    "'. Slave NAD addresses must be in the range [0x01,0x7D]", range, "minValue");
    }

    @Check
    public void checkThatNadRangeMaxIsValid(NadRange range) {
        if (range.getMaxValue() < 0x01 || range.getMaxValue() > 0x7D) // TODO move these to CONSTANTS.
            error("Invalid maximum slave NAD address '" + range.getMaxValue() +
                    "'. Slave NAD addresses must be in the range [0x01,0x7D]", range, "maxValue");
    }

    @Check
    public void checkThatNadRangeMaxGreaterThanMin(NadRange range) {
        if (range.getMaxValue() <= range.getMinValue())
            error("Invalid maximum slave NAD address '" + range.getMaxValue() +
                    "'. Maximum slave NAD address must be greater than minimum slave NAD address.", range, "maxValue");
    }

    @Check
    public void checkThatNodeNameIsNotNull(Node node) {
        if (node.getName() == null)
            error("Invalid node name.", node, "name");
    }

    // TODO check that the node name is a valid identifier.

    @Check
    public void checkThatNodeProtocolVersionIsValidLINVersion(Node node) {
        if (!LIN_VERSIONS.contains(node.getProtocolVersion()))
            error("Invalid LIN protocol version '" + node.getProtocolVersion() + "'.", node, "protocolVersion");
    }

    @Check
    public void checkThatNodeSupplierIDWillFitInA16bitInteger(Node node) {
        if (node.getSupplier() < 0 || node.getSupplier() > 0xFFFF) // TODO move these to CONSTANTS.
            error("Invalid supplier ID '" + node.getSupplier() + "'. The supplier ID must be in the range [0x0000,0xFFFF].", node, "supplier");
    }

    @Check
    public void checkThatNodeFunctionIDWillFitInA16bitInteger(Node node) {
        if (node.getFunction() < 0 || node.getFunction() > 0xFFFF) // TODO move these to CONSTANTS.
            error("Invalid function ID '" + node.getFunction() + ". The function ID must be in the range [0x0000,0xFFFF].", node, "function");
    }

    @Check
    public void checkThatNodeVariantIDWillFitInA8bitInteger(Node node) {
        if (node.getVariant() < 0 || node.getVariant() > 0xFF) // TODO move these to CONSTANTS.
            error("Invalid variant ID '" + node.getVariant() + ". The variant ID must be in the range [0x00,0xFF].", node, "variant");
    }

    @Check
    public void checkThatNodeBitrateIsNotNull(Node node) {
        if (node.getBitrate() == null)
            error("Invalid bitrate. Node bitrate must not be null.", node, "bitrate");
    }

    @Check
    public void checkThatNodeDoesNotContainANullFrame(Node node) {
        List<Frame> frames = node.getFrames();
        for (int i = 0; i < frames.size(); ++i)
            if (frames.get(i) == null)
                error("Invalid frame. Node can not contain a null Frame.", node, "frames", i);
    }

    @Check
    public void checkThatNodeDoesNotContainANullEncoding(Node node) {
        List<Encoding> encodings = node.getEncodings();
        for (int i = 0; i < encodings.size(); ++i)
            if (encodings.get(i) == null)
                error("Invalid encoding. Node can not contain a null Encoding.", node, "encodings", i);
    }

    @Check
    public void checkThatNodeCapabilityFileLanguageVersionIsValidLINVersion(NodeCapabilityFile file) {
        if (!LIN_VERSIONS.contains(file.getLanguageVersion()))
            error("Invalid LIN language version '" + file.getLanguageVersion() + "'.", file, "languageVersion");
    }

    @Check
    public void checkThatNodeCapabilityFileNodeIsNotNull(NodeCapabilityFile file) {
        if (file.getNode() == null)
            error("Node capability file must define a node.", file, "node");
    }

    @Check
    public void checkThatPhysicalEncodedValueMinIsGreaterThanOrEqualToZero(PhysicalEncodedValue value) {
        if (value.getMinValue() < 0)
            error("Invalid minimum value '" + value.getMinValue() + ". PhysicalEncodedValue minimum value must be greater than ore equal to zero.", value, "minValue");
    }

    @Check
    public void checkThatPhysicalEncodedValueMaxIsGreaterThanOrEqualToZero(PhysicalEncodedValue value) {
        if (value.getMaxValue() < 0)
            error("Invalid maximum value '" + value.getMaxValue() + ". PhysicalEncodedValue maximum value must be greater than or equal to zero.", value, "maxValue");
    }

    @Check
    public void checkThatPhysicalEncodedValueMaxIsGreaterThanMin(PhysicalEncodedValue value) {
        if (value.getMaxValue() <= value.getMinValue())
            error("Invalid maximum value '" + value.getMaxValue() + ". PhysicalEncodedValue maximum value must be greater than minimum value.", value, "maxValue");
    }

    @Check
    public void checkThatPhysicalEncodedValueScaleIsGreaterThanZero(PhysicalEncodedValue value) {
        if (value.getScale() <= 0)
            error("Invalid scale '" + value.getScale() + ". PhysicalEncodedValue scale must be greater than zero.", value, "scale");
    }

    @Check
    public void checkThatSaveConfigurationEntrySlaveIsNotNull(SaveConfigurationEntry entry) {
        if (entry.getSlave() == null)
            error("Invalid reference to slave.", entry, "slave");
    }

    @Check
    public void checkThatScalarSignalValueIsGreaterThanOrEqualToZero(ScalarSignalValue value) {
        if (value.getValue() < 0)
            error("Scalar signal values must be greater than or equal to zero.", value, "value");
    }

    @Check
    public void checkThatScheduleTableNameIsNotNull(ScheduleTable table) {
        if (table.getName() == null)
            error("Invalid schedule table name.", table, "name");
    }

    // TODO check that the schedule table name is a valid identifier.

    @Check
    public void checkThatScheduleTableMasterIsNotNull(ScheduleTable table) {
        if (table.getMaster() == null)
            error("ScheduleTable master must not be null.", table, "master");
    }

    @Check
    public void checkThatScheduleTableContainsAtLeastOneEntry(ScheduleTable table) {
        if (table.getEntries().size() <= 0)
            error("ScheduleTable master contain at least one entry.", table, "entries");
    }

    @Check
    public void checkThatScheduleTableEntryFrameTimeIsGreaterThanZero(ScheduleTableEntry entry) {
        if (entry.getFrameTime() <= 0)
            error("Invalid frame time '" + entry.getFrameTime() + " ms'. Frame time must be greater than zero ms.",
                    entry, "frameTime");
    }

    // TODO other checks on frame time?

    @Check
    public void checkThatSelectBitrateHasAtLeastOneValue(SelectBitrate bitrate) {
        if (bitrate.getValues().size() <= 0)
            error("SelectBitrate must contain at least one value.", bitrate, "values");
    }

    @Check
    public void checkThatSelectBitrateValuesAreInRange1to20(SelectBitrate bitrate) {
        List<Double> values = bitrate.getValues();
        for (int i = 0; i < values.size(); ++i) {
            double value = values.get(i);
            if (value < 1 || value > 20) // TODO move these to CONSTANTS.
                error("Invalid bitrate '" + value + "' kbps. Bitrates must be in the range [1,20]kbps.", bitrate,
                        "values", i);
        }
    }

    @Check
    public void checkThatSignalNameIsNotNull(Signal signal) {
        if (signal.getName() == null)
            error("Invalid signal name.", signal, "name");
    }

    // TODO check that the signal name is a valid identifier.

    @Check
    public void checkThatSignalFrameIsNotNull(Signal signal) {
        if (signal.getFrame() == null)
            error("Signal frame must not be null.", signal, "frame");
    }

    @Check
    public void checkThatSignalInitialValueIsNotNull(Signal signal) {
        if (signal.getInitialValue() == null)
            error("Signal initial value must not be null.", signal, "initialValue");
    }

    @Check
    public void checkThatScalarSignalSizeIsInValidRange(Signal signal) {
        if (signal.getInitialValue() instanceof ScalarSignalValue) {
            if (signal.getSize() < 0 || signal.getSize() > 16)
                error("Invalid signal size '" + signal.getSize() + "'. The signal size must in the range [0,16].",
                        signal, "size");
        }
    }

    @Check
    public void checkThatArraySignalSizeIsInValidRange(Signal signal) {
        if (signal.getInitialValue() instanceof ArraySignalValue)
            if (signal.getSize() < 0 || signal.getSize() > 64)
                error("Invalid array signal size '" + signal.getSize() + "'. The signal size must in the range [0,64].",
                        signal, "size");
    }

    @Check
    public void checkThatArraySignalSizeIsMultipleOfEight(Signal signal) {
        if (signal.getInitialValue() instanceof ArraySignalValue)
            if (signal.getSize() % 8 != 0)
                error("Invalid array signal size '" + signal.getSize() + "'. An array signal size must be a multiple of 8.",
                        signal, "size");
    }

    @Check
    public void checkThatArraySignalOffsetIsMultipleOfEight(Signal signal) {
        if (signal.getInitialValue() instanceof ArraySignalValue)
            if (signal.getOffset() % 8 != 0)
                error("Invalid array signal offset '" + signal.getOffset() + "'. An array signal offset must be a multiple of 8.",
                        signal, "offset");
    }

    @Check
    public void checkThatSlaveNadSetIsNotNull(Slave slave) {
        if (slave.getNadSet() == null)
            error("Invalid NAD. Must have a way to determine the initial NAD.", slave, "nadSet");
    }

    @Check
    public void checkThatSlaveDiagnosticClassIsOneTwoOrThree(Slave slave) {
        int v = slave.getDiagnosticClass();
        if (v != 1 && v != 2 && v != 3)
            error("Invalid diagnostic class '" + slave.getDiagnosticClass() +
                    "'. The the diagnostic class must be 1, 2, or 3.", slave, "diagnosticClass");
    }

    @Check
    public void checkThatSlaveP2MinIsGreaterThanZero(Slave slave) {
        if (slave.getP2Min() <= 0)
            error("Slave p2 min must be greater than zero.", slave, "p2Min");
    }

    @Check
    public void checkThatSlaveStMinIsGreaterThanOrEqualToZero(Slave slave) {
        if (slave.getStMin() < 0)
            error("Slave st min must be greater than or equal to zero.", slave, "stMin");
    }

    @Check
    public void checkThatSlaveNAsTimeoutIsGreaterThanZero(Slave slave) {
        if (slave.getNAsTimeout() <= 0)
            error("Slave NAs timeout must be greater than zero.", slave, "stMin");
    }

    @Check
    public void checkThatSlaveNCrTimeoutIsGreaterThanZero(Slave slave) {
        if (slave.getNCrTimeout() <= 0)
            error("Slave MCr timeout must be greater than zero.", slave, "stMin");
    }

    @Check
    public void checkThatSlaveSupportSIDsContainB2B7(Slave slave) {
        List<Integer> sids = slave.getSupportedSIDS();
        if (!sids.contains(0xb2) || !sids.contains(0xb7))
            error("Slave must support read by identifier and assign frame identifier range services.", slave, "supportedSIDS");
    }

    @Check
    public void checkThatSlaveSupportedSIDsAreInRange(Slave slave) {
        //for (Integer v : node.getSupportedSIDS()) {
        List<Integer> sids = slave.getSupportedSIDS();
        for (int i = 0; i < sids.size(); ++i) {
            int v = sids.get(i);
            if (v < 0 || v > 0xFF)
                error("Invalid service ID '" + v + "'. The service ID must be in the range [0x00,0xFF].",
                        slave, "supportedSIDS", i);
        }
    }

    @Check
    public void checkThatSlaveMaxMessageLengthIsGreaterThanOrEqualToZero(Slave slave) {
        if (slave.getMaxMessageLength() < 0)
            error("Slave max message length must be greater than or equal to zero.", slave, "maxMessageLength");
    }

    @Check
    public void checkThatSlaveMaxMessageLengthIsLessThan16Bit(Slave slave) {
        if (slave.getMaxMessageLength() > 0xFFFF)
            error("Slave max message length to larger must be 16 bits at most.", slave, "maxMessageLength");
    }

    @Check
    public void checkThatSlaveResponseErrorIsNotNull(Slave slave) {
        if (slave.getResponseError() == null)
            error("Slave response error must not be null.", slave, "responseError");
    }

    @Check
    public void checkThatSlaveDoesNotContainANullFaultStateSignal(Slave slave) {
        List<Signal> signals = slave.getFaultStateSignals();
        for (int i = 0; i < signals.size(); ++i)
            if (signals.get(i) == null)
                error("Save must not contain a null fault state signal.", slave, "faultStateSignals", i);
    }

    @Check
    public void checkThatThereAreNoDuplicatesInFaultStateSignals(Slave slave) {
        List<Signal> signals = slave.getFaultStateSignals();
        HashSet<Signal> seen = new HashSet<Signal>();
        for (int i = 0; i < signals.size(); ++i) {
            Signal signal = signals.get(i);
            if (seen.contains(signal))
                error("Signal '" + slave.getName() + "' is already in fault state signals.",
                        slave, "faultStateSignals", i);
            seen.add(signal);
        }
    }

    // TODO check that nodes are not defined more than once.
    // TODO check that published frames are not defined more than once.
    // TODO check that signals are not defined more than once.
    // TODO check that encodings are not defined more than once.

    /*@Check
    public void warnAboutP2MinNotBeingUsed(Slave node) {
        if(node.p2Min == null)
            return;
        warning("The current implementation does not take this value in to account.",node,
                NodeCapabilityFilePackage.Literals.SLAVE__P2_MIN
        )
    }

    @Check
    public void warnAboutSTMinNotBeingUsed(Slave node) {
        if(node.stMin == null)
            return;
        warning("The current implementation does not take this value in to account.",node,
                NodeCapabilityFilePackage.Literals.SLAVE__ST_MIN
        )
    }

    @Check
    public void warnAboutNAsTimeoutMinNotBeingUsed(Slave node) {
        if(node.NAsTimeout == null)
            return;
        warning("The current implementation does not take this value in to account.",node,
                NodeCapabilityFilePackage.Literals.SLAVE__NAS_TIMEOUT
        )
    }

    @Check
    public void warnAboutNCrTimeoutMinNotBeingUsed(Slave node) {
        if(node.NCrTimeout == null)
            return;
        warning("The current implementation does not take this value in to account.",node,
                NodeCapabilityFilePackage.Literals.SLAVE__NCR_TIMEOUT
        )
    }

    @Check
    public void warnAboutFrameMinPeriodBeingUsed(Frame frame) {
        if(frame.minPeriod == null)
            return;
        warning("The current implementation does not take this value in to account.",frame,
                NodeCapabilityFilePackage.Literals.FRAME__MIN_PERIOD
        )
    }

    @Check
    public void warnAboutFrameMaxPeriodBeingUsed(Frame frame) {
        if(frame.maxPeriod == null)
            return;
        warning("The current implementation does not take this value in to account.",frame,
                NodeCapabilityFilePackage.Literals.FRAME__MAX_PERIOD
        )
    }

    @Check
    public void warnAboutFrameEventTriggeredBeingUsed(Frame frame) {
        if(frame.eventTriggeredFrame == null)
            return;
        warning("The current implementation does not take this value in to account.",frame,
                NodeCapabilityFilePackage.Literals.FRAME__EVENT_TRIGGERED_FRAME
        )
    }

    @Check
    public void checkThatFrameNamesAreUniqueWithinANode(Node node) { // TODO this could be simplified to just make sure that subscribed frames are not published by this node.

        val seen = new HashSet<String>();
        node.frames.forEach [
        if (seen.contains(it.name)) {
            error("Frame '+it.name+' was already defined.", it,
                    NodeCapabilityFilePackage.Literals.FRAME__NAME
            )
        }
        seen.add(it.name);
        ]
    }

    @Check
    public void checkThatSignalNamesAreUniqueWithinANode(Node node) {
        val seen = new HashSet<String>();
        node.frames.forEach [
                it.signals.forEach [ signal |
        if (seen.contains(signal.name)) {
            error("Signal '+signal.name+' was already defined.", signal,
                    NodeCapabilityFilePackage.Literals.SIGNAL__NAME)
        }
        seen.add(signal.name);
        ]
        ]
    }

    @Check
    public void checkThatEncodingNamesAreUniqueWithinANode(Node node) {
        val seen = new HashSet<String>();
        node.encodings.forEach [
        if (seen.contains(it.name)) {
            error("Encoding '+it.name+' was already defined.", it,
                    NodeCapabilityFilePackage.Literals.ENCODING__NAME)
        }
        seen.add(it.name);
        ];
    }



    @Check
    public void warnThatOnlyTheFirstNADInANadListWillBeUsedIfMoreThanOneIsListed(NadList list) {
        if(list.values.size != 1) {
            warning("The current implementation does not support instance generation. The first value in the nad list will be used.",list,
                    NodeCapabilityFilePackage.Literals.NAD_LIST__VALUES);
        }
    }

    @Check
    public void warnThatOnlyTheMinNADInANadRangeWillBeUsed(NadRange range) {
        warning("The current implementation does not support instance generation. The minimum value in the nad range will be used.",range,
                NodeCapabilityFilePackage.Literals.NAD_RANGE__MAX_VALUE);
    }

    @Check
    public void checkThatSlavesAreOnlyListedOnce(Master master) {
        val seen = new HashSet<Slave>();
        master.slaves.forEach[
        if(seen.contains(it)) {
            error("Slave '+it.name+' is already in slave list.", master,
                    NodeCapabilityFilePackage.Literals.MASTER__SLAVES,
                    master.slaves.lastIndexOf(it));
        }
        seen.add(it);
        ];
    }

    @Check
    public void checkThatAFrameIsOnlyDefinedOnce(Master master) {
        val seen = new HashMap<String,Slave>();
        master.slaves.forEach[slave|
                slave.frames.filter[it.publishes != null].forEach[frame|
        if(seen.containsKey(frame.name)) {
            error("Slave '+slave.name+' contains a frame'+frame.name+' that has already been defined by slave '+seen.get(frame.name).name+'.", master,
                    NodeCapabilityFilePackage.Literals.MASTER__SLAVES,
                    master.slaves.lastIndexOf(slave));
        }
        seen.put(frame.name,slave);
        ];
        ];
    }

    @Check
    public void checkThatSignalsAreOnlyDefinedOnce(Master master) {
        val seen = new HashMap<String,Slave>();
        master.slaves.forEach[slave|
                slave.frames.filter[it.publishes != null].forEach[frame|
                        frame.signals.forEach[signal|
        if(seen.containsKey(signal.name)) {
            error("Slave '+slave.name+' contains a signal '+signal.name+' that has already been defined by slave '+seen.get(signal.name).name+'.", master,
                    NodeCapabilityFilePackage.Literals.MASTER__SLAVES,
                    master.slaves.lastIndexOf(slave));
        }
        seen.put(signal.name,slave);
        ];
        ];
        ];
    }

    @Check
    public void checkMasterBitrateIsGreatorOrEqualToAllSaveMinBitrates(Master master) {
        master.slaves.forEach[slave|
                var bitrate = Double.parseDouble(master.bitrate.value);
        if(slave.bitrate instanceof AutomaticBitrate) {
            var min = Double.parseDouble((slave.bitrate as AutomaticBitrate).minValue);
            if(bitrate < min) {
                error("Slave '+slave.name+' can not operate at a bitrate less than '+(slave.bitrate as AutomaticBitrate).minValue+ kbps'.", master,
                        NodeCapabilityFilePackage.Literals.MASTER__BITRATE);
            }
        }
        ]
    }

    @Check
    public void checkMasterBitrateIsLessThanOrEqualToAllSaveMaxBitrates(Master master) {
        master.slaves.forEach[slave|
                var bitrate = Double.parseDouble(master.bitrate.value);
        if(slave.bitrate instanceof AutomaticBitrate) {
            var max = Double.parseDouble((slave.bitrate as AutomaticBitrate).maxValue);
            if(bitrate > max) {
                error("Slave '+slave.name+' can not operate at a bitrate greater than '+(slave.bitrate as AutomaticBitrate).maxValue+ kbps'.", master,
                        NodeCapabilityFilePackage.Literals.MASTER__BITRATE);
            }
        }
        ]
    }

    @Check
    public void checkMasterBitrateMatchesFixedBitrateInAllSlaves(Master master) {
        master.slaves.forEach[slave|
        if(slave.bitrate instanceof FixedBitrate) {
            var bitrate = Double.parseDouble(master.bitrate.value);
            var value = Double.parseDouble((slave.bitrate as FixedBitrate).value);
            if(bitrate != value) {
                error("Slave '+slave.name+' must operate at a bitrate of '+(slave.bitrate as FixedBitrate).value+ kbps'.", master,
                        NodeCapabilityFilePackage.Literals.MASTER__BITRATE);
            }
        }
        ]
    }

    @Check
    public void checkMasterBitrateIsListedInSelectOfAllSlaves(Master master) {
        master.slaves.forEach[slave|
        if(slave.bitrate instanceof SelectBitrate) {
            var bitrate = Double.parseDouble(master.bitrate.value);
            var slaveBitrate = slave.bitrate as SelectBitrate;
            if(!slaveBitrate.values.map[Double.parseDouble(it)].contains(bitrate)) {
                error("Slave '+slave.name+' must operate at a bitrate in the set '+slaveBitrate.values+ kbps'.", master,
                        NodeCapabilityFilePackage.Literals.MASTER__BITRATE);
            }
        }
        ]
    }*/

    // TODO check that real frames are publish and reference frames are subscribe.
}
