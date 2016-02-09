package com.ptransportation.capability.generator

import com.ptransportation.capability.nodeCapabilityFile.Node

/**
 * Created by aaron on 2/8/16.
 */
interface Interface {
    abstract def CharSequence getName()

    abstract def CharSequence headerIncludes(Node node)
    
    abstract def CharSequence sourceIncludes(Node node)

    abstract def CharSequence headerGlobals(Node node)
    
    abstract def CharSequence sourceGlobals(Node node)
    
    abstract def CharSequence initialization(Node node)
    
    abstract def CharSequence rxBreakSync()
    
    abstract def CharSequence rxDataAvailable()

    abstract def CharSequence rxData(String cVarToStoreIn)

    abstract def CharSequence txData(String cVarToTx)

    abstract def CharSequence txBreakAndSync()
}