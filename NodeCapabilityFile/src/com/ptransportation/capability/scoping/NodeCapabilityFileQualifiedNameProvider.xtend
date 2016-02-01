package com.ptransportation.capability.scoping

import com.ptransportation.capability.nodeCapabilityFile.Encoding
import com.ptransportation.capability.nodeCapabilityFile.Frame
import com.ptransportation.capability.nodeCapabilityFile.Node
import com.ptransportation.capability.nodeCapabilityFile.Signal
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.naming.QualifiedName

class NodeCapabilityFileQualifiedNameProvider implements IQualifiedNameProvider {
	
	override apply(EObject from) {
		return getFullyQualifiedName(from);
	}
	
	var used = false; // Good old java here every thing has to be an object, and references to native types do not exits.
	override getFullyQualifiedName(EObject ele) {
		if(ele instanceof Signal) {
			var frame = ele.eContainer() as Frame;
			if(frame.publishes != null)
        		return QualifiedName.create(ele.name);
        }
        else if(ele instanceof Frame) {
        	if(ele.publishes != null)
            	return QualifiedName.create(ele.name);
        }
        else if(ele instanceof Encoding) {
        	used = false;
            var node = ele.eContainer as Node;
            node.frames.forEach[ frame |
            	frame.signals.forEach[ signal |
            		if(signal.encoding == ele)
            			used = true;
            	]
            ]
            if(used)
            	return QualifiedName.create(ele.name);
        }
        else if(ele instanceof Node) {
            return QualifiedName.create(ele.name);
        }
        return null;
	}
}