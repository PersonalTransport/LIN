package com.ptransportation.capability.generator

import com.ptransportation.capability.nodeCapabilityFile.Node

interface Target {
	abstract def boolean targetMatches(String targetDevice);

    abstract def Interface getInterface(String targetInterface);
    
    abstract def CharSequence headerIncludes(Node node)
    
    abstract def CharSequence sourceIncludes(Node node)

    abstract def CharSequence headerGlobals(Node node)
    
    abstract def CharSequence sourceGlobals(Node node)
    
    abstract def String initialization(Node node)
}