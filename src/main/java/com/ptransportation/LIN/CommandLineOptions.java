package com.ptransportation.LIN;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandLineOptions {
    private JCommander jCommander;

    @Parameter(names = {"-h", "--help"}, help = true, description = "Show help this message.")
    private boolean help = false;

    @Parameter(names = {"-v", "--version"}, help = true, description = "Display compiler version information.")
    private boolean version = false;

    @Parameter(names = {"-t", "--target"}, required = true, description = "The target device.")
    private String targetDevice;

    @Parameter(names = {"-i", "--target-interface"}, required = true, description = "The target device's interface.")
    private String targetInterface;

    @Parameter(names = {"-o", "--output"}, description = "Output directory.")
    private String outputDirectory = "gen";

    @Parameter(names = {"-aoa", "--android-open-accessory"}, description = "Generate PIC24FJXXGBXXX android open accessory.")
    private boolean AOA = false;

    @Parameter(description = "source files...", required = true)
    private List<String> sources = new ArrayList<String>();

    public CommandLineOptions() {
        jCommander = new JCommander(this);
        jCommander.setProgramName("LIN");
    }

    public void parse(String[] args) {
        jCommander.parse(args);
    }

    public String getTargetDevice() {
        return targetDevice;
    }

    public String getTargetInterface() {
        return targetInterface;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

    public boolean isAOA() {
        return AOA;
    }

    public List<String> getSources() {
        return sources;
    }

    public void usage() {
        if (jCommander.getParsedCommand() != null)
            jCommander.usage(jCommander.getParsedCommand());
        else
            jCommander.usage();
    }
}
