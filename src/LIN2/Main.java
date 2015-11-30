package LIN2;


import LIN2.frame.EventTriggeredFrame;
import LIN2.frame.UnconditionalFrame;
import LIN2.signal.ArraySignalValue;
import LIN2.signal.ScalarSignalValue;
import LIN2.signal.Signal;
import LIN2.signal.SignalValue;
import org.stringtemplate.v4.Interpreter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.misc.ObjectModelAdaptor;
import org.stringtemplate.v4.misc.STNoSuchPropertyException;

public class Main {
    public static void main(String[] args) {
        Cluster cluster = new Cluster();
        Master CEM = new Master("CEM", 19.2f , 5.0f, 0.1f);
        CEM.setProtocol("2.2");
        CEM.setChannelName("DB");

        Slave RSM = new Slave("RSM");
        RSM.setProtocol("2.2");
        RSM.setConfiguredNAD(0x20);
        RSM.setSupplier(0x4E4E);
        RSM.setFunction(0x4553);
        RSM.setVariant(1);
        RSM.setP2Min(150f);
        RSM.setSTMin(50f);

        Slave LSM = new Slave("LSM");
        LSM.setProtocol("2.2");
        LSM.setConfiguredNAD(0x21);
        LSM.setInitialNAD(0x01);
        LSM.setSupplier(0x4A4F);
        LSM.setFunction(0x4841);
        LSM.setP2Min(150f);
        LSM.setSTMin(50f);


        /////////////////////////////////////////////////////////////////
        UnconditionalFrame CEM_Frm1 = new UnconditionalFrame("CEM_Frm1");
        CEM_Frm1.setID(0x01);
        CEM_Frm1.setPublisher(CEM);
        CEM_Frm1.setLength(1);

        Signal InternalLightsRequest = new Signal("InternalLightsRequest");
        InternalLightsRequest.setSize(2);
        InternalLightsRequest.setOffset(0);
        InternalLightsRequest.setFrame(CEM_Frm1);
        InternalLightsRequest.setInitialValue(new ScalarSignalValue(0));


        /////////////////////////////////////////////////////////////////
        UnconditionalFrame LSM_Frm1 = new UnconditionalFrame("LSM_Frm1");
        LSM_Frm1.setID(0x02);
        LSM_Frm1.setPublisher(LSM);
        LSM_Frm1.setLength(2);

        Signal LeftIntLightsSwitch = new Signal("LeftIntLightsSwitch");
        LeftIntLightsSwitch.setSize(8);
        LeftIntLightsSwitch.setOffset(8);
        LeftIntLightsSwitch.setFrame(LSM_Frm1);
        LeftIntLightsSwitch.setInitialValue(new ScalarSignalValue(0));


        /////////////////////////////////////////////////////////////////
        UnconditionalFrame LSM_Frm2 = new UnconditionalFrame("LSM_Frm2");
        LSM_Frm2.setID(0x03);
        LSM_Frm2.setPublisher(LSM);
        LSM_Frm2.setLength(1);

        Signal LSMerror = new Signal("LSMerror");
        LSMerror.setSize(1);
        LSMerror.setOffset(0);
        LSMerror.setFrame(LSM_Frm2);
        LSMerror.setInitialValue(new ScalarSignalValue(0));

        Signal IntTest = new Signal("IntTest");
        IntTest.setSize(2);
        IntTest.setOffset(1);
        IntTest.setFrame(LSM_Frm2);
        IntTest.setInitialValue(new ScalarSignalValue(0));


        /////////////////////////////////////////////////////////////////
        UnconditionalFrame RSM_Frm1 = new UnconditionalFrame("RSM_Frm1");
        RSM_Frm1.setID(0x04);
        RSM_Frm1.setPublisher(RSM);
        RSM_Frm1.setLength(2);

        Signal RightIntLightsSwitch = new Signal("RightIntLightsSwitch");
        RightIntLightsSwitch.setSize(8);
        RightIntLightsSwitch.setOffset(8);
        RightIntLightsSwitch.setFrame(RSM_Frm1);
        RightIntLightsSwitch.setInitialValue(new ScalarSignalValue(0));

        /////////////////////////////////////////////////////////////////
        UnconditionalFrame RSM_Frm2 = new UnconditionalFrame("RSM_Frm2");
        RSM_Frm2.setID(0x05);
        RSM_Frm2.setPublisher(RSM);
        RSM_Frm2.setLength(1);

        Signal RSMerror = new Signal("RSMerror");
        RSMerror.setSize(1);
        RSMerror.setOffset(0);
        RSMerror.setFrame(RSM_Frm2);
        RSMerror.setInitialValue(new ScalarSignalValue(0));


        /////////////////////////////////////////////////////////////////
        EventTriggeredFrame Node_Status_Event = new EventTriggeredFrame("Node_Status_Event");
        Node_Status_Event.setID(0x06);
        Node_Status_Event.addAssociatedFrame(RSM_Frm1);
        Node_Status_Event.addAssociatedFrame(LSM_Frm1);
        //TODO Schedule table


        RSM.addFrame(Node_Status_Event);
        RSM.addFrame(CEM_Frm1);
        RSM.addFrame(RSM_Frm1);
        RSM.addFrame(RSM_Frm2);

        LSM.addFrame(Node_Status_Event);
        LSM.addFrame(CEM_Frm1);
        LSM.addFrame(LSM_Frm1);
        LSM.addFrame(LSM_Frm2);

        CEM.addFrame(LSM_Frm1);
        CEM.addFrame(LSM_Frm2);
        CEM.addFrame(RSM_Frm1);
        CEM.addFrame(RSM_Frm2);

        cluster.setMaster(CEM);
        cluster.addSlave(LSM);
        cluster.addSlave(RSM);

        STGroup group = new STGroupFile("LIN2/compiler/generation/template/DescriptionFile.stg");
        /*group.registerModelAdaptor(Entry.class,new ObjectModelAdaptor() {
            @Override
            public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
                if(propertyName.equals("type")) {
                    String name = o.getClass().getSimpleName();
                    name = Character.toLowerCase(name.charAt(0))+name.substring(1);
                    return  name + "Definition";
                }
                return super.getProperty(interp, self, o, property, propertyName);
            }
        });*/
        group.registerModelAdaptor(Integer.class,new ObjectModelAdaptor() {
            @Override
            public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
                Integer value = (Integer)o;
                if(propertyName.equals("hex"))
                    return "0x"+Integer.toHexString(value).toUpperCase();

                return super.getProperty(interp, self, o, property, propertyName);
            }
        });

        group.registerModelAdaptor(SignalValue.class,new ObjectModelAdaptor() {
            @Override
            public synchronized Object getProperty(Interpreter interp, ST self, Object o, Object property, String propertyName) throws STNoSuchPropertyException {
                if(propertyName.equals("values") && o instanceof ArraySignalValue)
                    return ((ArraySignalValue)o).getValues();
                else if(propertyName.equals("value") && o instanceof ScalarSignalValue)
                    return ((ScalarSignalValue)o).getValue();
                return super.getProperty(interp, self, o, property, propertyName);
            }
        });

        ST descriptionFile = group.getInstanceOf("descriptionFile");
        descriptionFile.add("cluster",cluster);
        System.out.println(descriptionFile.render());
    }
}
