package com.ptransportation.LIN.validation;

import com.ptransportation.LIN.model.*;

import java.util.Arrays;
import java.util.List;

public class DefaultValidator extends AbstractValidator {
    public static List<Double> LIN_VERSIONS = Arrays.asList(1.0,1.1,1.2,1.3,2.0,2.1,2.2);

    public void error(String message,Object object,String field) {
        System.err.println(message);
    }

    public void error(String message,Object object,String field,int index) {
        System.err.println(message);
    }

    @Check
    public void checkThatLanguageVersionIsValidLINVersion(NodeCapabilityFile file) {
        if(!LIN_VERSIONS.contains(file.getLanguageVersion()))
            error("Invalid LIN language version '"+file.getLanguageVersion()+"'.", file,"languageVersion");
    }

    @Check
    public void checkThatNodeIsNotNull(NodeCapabilityFile file) {
        if(file.getNode() == null)
            error("Node capability file must define a node.",file,"node");
    }

    @Check
    public void checkThatProtocolVersionIsValidLINVersion(Node node) {
        if(!LIN_VERSIONS.contains(node.getProtocolVersion()))
            error("Invalid LIN protocol version '"+node.getProtocolVersion()+"'.",node,"protocolVersion");
    }

    @Check
    public void checkThatTheSupplierIDWillFitInA16bitInteger(Node node) {
        if(node.getSupplier() < 0 || node.getSupplier() > 0xFFFF)
            error("Invalid supplier ID '"+node.getSupplier()+"'. The supplier ID must be in the range [0x0000,0xFFFF].", node,"supplier");
    }

    @Check
    public void checkThatTheFunctionIDWillFitInA16bitInteger(Node node) {
        if(node.getFunction() < 0 || node.getFunction() > 0xFFFF)
            error("Invalid function ID '"+node.getFunction()+". The function ID must be in the range [0x0000,0xFFFF].", node, "function");
    }

    @Check
    public void checkThatTheVariantIDWillFitInA8bitInteger(Node node) {
        if(node.getVariant() < 0 || node.getVariant() > 0xFF)
            error("Invalid variant ID '"+node.getVariant()+". The variant ID must be in the range [0x00,0xFF].",node,"variant");
    }

    @Check
    public void checkThatDiagnosticClassIsOneTwoOrThree(Slave slave) {
        int v = slave.getDiagnosticClass();
        if(v != 1 && v != 2 && v != 3)
            error("Invalid diagnostic class '"+slave.getDiagnosticClass()+"'. The the diagnostic class must be 1, 2, or 3.",slave,"diagnosticClass");
    }


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
    }*/

    @Check
    public void checkThatTheSupportedServiceIdentifiersAreInRange(Slave node) {
        for (Integer v : node.getSupportedSIDS()) {
            if (v < 0 || v > 0xFF)
                error("Invalid service ID '" + v + "'. The service ID must be in the range [0x00,0xFF].", node,
                        "supportedSIDS", node.getSupportedSIDS().indexOf(v));
        }
    }

    @Check
    public void checkThatTheMaxMessageLengthIsInRange(Slave node) {
        if (node.getMaxMessageLength() < 0 || node.getMaxMessageLength() > 0xFFFF)
            error("Invalid max message length '" + node.getMaxMessageLength() +
                    "'. The message length must be in the range [0x0000,0xFFFF].", node, "maxMessageLength");
    }

    @Check
    public void checkThatTheLengthOfTheFrameIsOneToEight(Frame frame) {
        if(frame.getLength() < 1 || frame.getLength() > 8)
            error("Invalid frame length '"+frame.getLength()+"'. The frame length must in the range [1,8].",frame,"length");
    }

    /*@Check
    public void warnAboutFrameMinPriodBeingUsed(Frame frame) {
        if(frame.minPeriod == null)
            return;
        warning("The current implementation does not take this value in to account.",frame,
                NodeCapabilityFilePackage.Literals.FRAME__MIN_PERIOD
        )
    }

    @Check
    public void warnAboutFrameMaxPriodBeingUsed(Frame frame) {
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
    }*/

    @Check
    public void checkThatScalorSignalSizeIsInValidRange(Signal signal) {
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
    public void checkThatAutomaticBitrateMinIsInTheRange1To20(AutomaticBitrate bitrate) {
        if (bitrate.getMinValue() < 1 || bitrate.getMinValue() > 20)
            error("Invalid minimum bitrate '" + bitrate.getMinValue() + "' kbps. Minimum bitrate must be in the range [1,20]kbps.",
                    bitrate, "minValue");
    }

    @Check
    public void checkThatAutomaticBitrateMaxIsInTheRange1To20(AutomaticBitrate bitrate) {
        if (bitrate.getMaxValue() < 1 || bitrate.getMaxValue() > 20)
            error("Invalid maximum bitrate '" + bitrate.getMaxValue() + "' kbps. Maximum bitrate must be in the range [1,20]kbps.",
                    bitrate, "maxValue");
    }

    @Check
    public void checkThatAutomaticBitrateMinIsLessOrEqualToMax(AutomaticBitrate bitrate) {
        if(bitrate.getMinValue() > bitrate.getMaxValue()) {
            error("Invalid minimum bitrate '"+bitrate.getMinValue()+"' kbps greator than maximum bitrate '"+bitrate.getMaxValue()+"' kbps.",
                    bitrate, "minValue");
        }
    }

    @Check
    public void checkThatSelectBitratesAreInRange1to20(SelectBitrate bitrate) {
        for (Double v : bitrate.getValues()) {
            if (v < 1 || v > 20)
                error("Invalid bitrate '" + v + "' kbps. Bitrates must be in the range [1,20]kbps.", bitrate,
                        "values", bitrate.getValues().indexOf(v));
        }
    }

    @Check
    public void checkThatFixedBitrateIsInRange1To20(FixedBitrate bitrate) {
        if(bitrate.getValue() < 1 || bitrate.getValue() > 20)
            error("Invalid bitrate '"+bitrate.getValue()+"' kbps. Bitrates must be in the range [1,20]kbps.",
                    bitrate, "value");

    }

    @Check
    public void checkThatAllNadsInListAreValid(NadList list) {
        for (Integer v : list.getValues()) {
            if (v < 0x01 || v > 0x7D)
                error("Invalid slave NAD address '"+v+"'. Slave NAD addresses must be in the range [0x01,0x7D]", list,
                        "values", list.getValues().indexOf(v));
        }
    }

    @Check
    public void checkThatMinNADInNadRangeIsValid(NadRange range) {
        if(range.getMinValue() < 0x01 || range.getMinValue() > 0x7D)
            error("Invalid minimum slave NAD address '"+range.getMinValue()+
                    "'. Slave NAD addresses must be in the range [0x01,0x7D]", range, "minValue");
    }

    @Check
    public void checkThatMaxNADInNadRangeIsValid(NadRange range) {
        if(range.getMaxValue() < 0x01 || range.getMaxValue() > 0x7D)
            error("Invalid maximum slave NAD address '"+range.getMaxValue()+"'. Slave NAD addresses must be in the range [0x01,0x7D]",
                    range,"maxValue");
    }





    /*@Check
    public void checkThatThereAreNoDuplicatesInFaultStateSignals(Slave node) {
        val seen = new HashSet<Signal>();
        node.faultStateSignals.forEach [
        if(seen.contains(it)) {
            error("Signal '+it.name+' is already in fault state signals.", node,
                    NodeCapabilityFilePackage.Literals.SLAVE__FAULT_STATE_SIGNALS,
                    node.faultStateSignals.lastIndexOf(it))
        }
        seen.add(it);
        ];
    }

    @Check
    public void checkThatFrameNamesAreUniqueWithinANode(Node node) { // TODO this could be simplefied to just make sure that subsribe frames are not published by this node.

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
    public void checkThatEencodingNamesAreUniqueWithinANode(Node node) {
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

    // TODO add validations that check that subscribed frames,signals,and encodings match published frames,signals,and encodings.

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
