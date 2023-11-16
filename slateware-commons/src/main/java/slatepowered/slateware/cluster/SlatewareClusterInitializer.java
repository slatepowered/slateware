package slatepowered.slateware.cluster;

import slatepowered.slate.cluster.ClusterInstance;
import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.plugin.PluginEntrypoint;
import slatepowered.slate.plugin.SlatePlugin;
import slatepowered.slateware.node.NodePowerControl;
import slatepowered.slateware.node.NodePowerService;

/**
 * The Slateware cluster plugin entrypoint.
 */
public class SlatewareClusterInitializer implements PluginEntrypoint<ClusterInstance> {

    @Override
    public void onInitialize(SlatePlugin plugin, ClusterInstance network) {
        /*
            Service: NodePowerService
         */
        network.ensureService(NodePowerService.KEY, () -> new NodePowerService() {
            @Override
            public void start(String nodeName) {
                forNode(nodeName).start();
            }

            @Override
            public void stop(String nodeName) {
                forNode(nodeName).stop();
            }

            @Override
            public NodePowerControl forNode(String nodeName) {
                // todo: check whether the node exists and is a
                //  valid managed node (so also not null), rn it just throws
                //  some cryptic error which is hard to decode by the caller
                return network.<ManagedNode>getNode(nodeName)
                        .findComponents(NodePowerControl.class)
                        .first().orElse(null);
            }
        });
    }

}
