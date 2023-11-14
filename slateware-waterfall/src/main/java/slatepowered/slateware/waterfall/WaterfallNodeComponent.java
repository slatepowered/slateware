package slatepowered.slateware.waterfall;

import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.SharedNodeComponent;
import slatepowered.slateware.node.NodePowerControl;
import slatepowered.slateware.node.NodePowerService;

/**
 * The Waterfall node component implementation.
 */
public class WaterfallNodeComponent implements SharedNodeComponent, NodePowerControl {

    /**
     * The node this component is attached to.
     */
    protected transient ManagedNode node;

    @Override
    public boolean attached(ManagedNode node) {
        this.node = node;
        return true;
    }

    @Override
    public String nodeName() {
        return node.getName();
    }

    @Override
    public void start() {
        // todo: start process
    }

    @Override
    public void stop() {
        // todo: stop process
    }

}
