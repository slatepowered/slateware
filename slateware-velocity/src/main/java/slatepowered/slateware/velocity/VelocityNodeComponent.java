package slatepowered.slateware.velocity;

import slatepowered.slate.model.ClusterNetwork;
import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.SharedNodeComponent;

/**
 * The data component which holds information about the Velocity installation.
 */
public class VelocityNodeComponent implements SharedNodeComponent {

    @Override
    public boolean attached(ManagedNode node) {
        // check for cluster network, if it is on a
        // cluster attach the node controller
        if (node.getNetwork() instanceof ClusterNetwork) {
            node.attach(new VelocityNodeController(this));
        }

        return true;
    }

}
