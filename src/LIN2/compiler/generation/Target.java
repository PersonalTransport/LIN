package LIN2.compiler.generation;

import org.stringtemplate.v4.STGroup;

public abstract class Target {

    public abstract boolean targetMatches(String targetDevice);

    public abstract Interface getInterface(String targetInterface);

    public void addSourceGroups(STGroup sourceGroup) {}

    public void addHeaderGroups(STGroup headerGroup) {}

    public String getIncludes() { return null; }

    public String getGlobals() { return null; }
}
