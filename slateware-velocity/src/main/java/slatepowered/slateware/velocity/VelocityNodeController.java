package slatepowered.slateware.velocity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.NodeComponent;
import slatepowered.slateware.node.NodePowerControl;
import slatepowered.veru.misc.Throwables;

/**
 * This component is installed on the cluster/node host exclusively.
 */
@RequiredArgsConstructor
public class VelocityNodeController implements NodeComponent, NodePowerControl {

    /**
     * The Velocity node data component.
     */
    private final VelocityNodeComponent dataComponent;

    /**
     * The managed node this component is attached to.
     */
    private ManagedNode node;

    /** The Java process which is running the proxy server. */
    @Getter
    private Process process;

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
        try {
            // todo: start java process with correct arguments
            //  and the proxy.jar file
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

    @Override
    public void stop() {
        try {
            // todo: stop the java process
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

}
