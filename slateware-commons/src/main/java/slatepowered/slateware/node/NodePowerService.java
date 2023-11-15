package slatepowered.slateware.node;

import slatepowered.reco.rpc.RemoteAPI;
import slatepowered.reco.rpc.function.Allow;
import slatepowered.slate.cluster.ClusterInstance;
import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.Network;
import slatepowered.slate.service.Service;
import slatepowered.slate.service.ServiceKey;
import slatepowered.slate.service.remote.LocalRemoteServiceKey;
import slatepowered.slate.service.remote.RemoteServiceKey;

import java.util.concurrent.CompletableFuture;

/**
 * A service for node power control, this is supposed to be
 * implemented by a cluster.
 */
public interface NodePowerService extends Service, RemoteAPI {

    LocalRemoteServiceKey<NodePowerService> KEY = LocalRemoteServiceKey.key(NodePowerService.class);

    static RemoteServiceKey<NodePowerService> remote() {
        return RemoteServiceKey.remote(NodePowerService.class);
    }

    /**
     * Start the node with the given name on this cluster.
     *
     * @param nodeName The name of the node to start.
     */
    @Allow({ "master", "control" })
    void start(String nodeName);

    default CompletableFuture<Void> startAsync(String nodeName) {
        return null;
    }

    /**
     * Stop the node with the given name on this cluster.
     *
     * @param nodeName The name of the node to stop.
     */
    @Allow({ "master", "control" })
    void stop(String nodeName);

    default CompletableFuture<Void> stopAsync(String nodeName) {
        return null;
    }

    /**
     * Get the node power control instance for the given
     * node on this cluster.
     *
     * @param nodeName The name of the node.
     * @return The power control instance.
     */
    NodePowerControl forNode(String nodeName);

    static NodePowerService impl(Network network) {
        return new NodePowerService() {
            @Override
            public void start(String nodeName) {
                ManagedNode node = network.getNode(nodeName);
                node.runVoidAction(NodePowerControl.class, (nodePowerControl, node1) -> CompletableFuture.runAsync(nodePowerControl::start), null);
            }

            @Override
            public void stop(String nodeName) {
                ManagedNode node = network.getNode(nodeName);
                node.runVoidAction(NodePowerControl.class, (nodePowerControl, node1) -> CompletableFuture.runAsync(nodePowerControl::stop), null);
            }

            @Override
            public NodePowerControl forNode(String nodeName) {
                return null;
            }
        };
    }

}
