package com.ptransportation.capability.scoping

import com.ptransportation.capability.nodeCapabilityFile.Encoding
import com.ptransportation.capability.nodeCapabilityFile.Frame
import com.ptransportation.capability.nodeCapabilityFile.Node
import com.ptransportation.capability.nodeCapabilityFile.Signal
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.naming.DefaultDeclarativeQualifiedNameProvider

class NodeCapabilityFileQualifiedNameProvider extends  DefaultDeclarativeQualifiedNameProvider {

	def qualifiedName(Node node) {
		return QualifiedName.create(node.name);
	}

	def qualifiedName(Frame frame) {
		if(frame.publishes != null)
			return QualifiedName.create(frame.name);
		return null;
	}

	def qualifiedName(Encoding encoding) {
		var node = encoding.eContainer as Node;
		if(node.frames.filter[it.publishes != null].map[it.signals.filter[it.encoding==encoding]].size != 0)
			return QualifiedName.create(encoding.name);
		return null;
	}

	def qualifiedName(Signal signal) {
		var frame = signal.eContainer as Frame;
		if(frame.publishes != null)
			return QualifiedName.create(signal.name);
		return null;
	}
}