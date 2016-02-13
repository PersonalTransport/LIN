package com.ptransportation.LIN;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.ArrayList;
import java.util.List;

public class CompilerOptions {
    private SlaveDriverOptions slaveDriverOptions = new SlaveDriverOptions();
    private MasterDriverOptions masterDriverOptions = new MasterDriverOptions();
    @Parameter(names = {"-h", "--help"}, help = true, description = "Show help this message.")
    private boolean help = false;
    @Parameter(names = {"-t", "--target"}, required = true, description = "The target device.")
    private String targetDevice;
    @Parameter(names = {"-i", "--target-interface"}, required = true, description = "The target device's interface.")
    private String targetInterface;
    private JCommander jCommander;

    public CompilerOptions() {
        jCommander = new JCommander(this);
        jCommander.setProgramName("LIN");
        jCommander.addCommand("slave", slaveDriverOptions);
        jCommander.addCommand("master", masterDriverOptions);
    }

    public void parse(String[] args) {
        jCommander.parse(args);
    }

    public boolean getHelp() {
        return help;
    }

    public String getTargetDevice() {
        return targetDevice;
    }

    public String getTargetInterface() {
        return targetInterface;
    }

    public SlaveDriverOptions getSlaveDriverOptions() {
        return jCommander.getParsedCommand().equals("slave") ? slaveDriverOptions : null;
    }

    public MasterDriverOptions getMasterDriverOptions() {
        return jCommander.getParsedCommand().equals("master") ? masterDriverOptions : null;
    }

    public void usage() {
        if (jCommander.getParsedCommand() != null)
            jCommander.usage(jCommander.getParsedCommand());
        else
            jCommander.usage();

    }

    public class NodeTypeOptions {
        @Parameter(description = "(*.nfc *.ldf)+", required = true)
        private List<String> sources = new ArrayList<String>();

        @Parameter(names = {"-o", "--output"}, description = "Output directory.")
        private String outputDirectory = "gen";

        public List<String> getSources() {
            return sources;
        }

        public String getOutputDirectory() {
            return outputDirectory;
        }
    }

    @Parameters(commandDescription = "Generate the slave C slave driver.")
    public class SlaveDriverOptions extends NodeTypeOptions {
        @Parameter(names = {"-s", "--slave"}, description = "name of slave node to export")
        private String slaveName = "";

        public String getSlaveName() {
            return slaveName;
        }
    }

    @Parameters(commandDescription = "Generate the slave C master driver.")
    public class MasterDriverOptions extends NodeTypeOptions {
    }
}
