package slatepowered.slateware.node;

import slatepowered.reco.rpc.objects.ObjectMethod;
import slatepowered.reco.rpc.objects.RemoteObject;
import slatepowered.reco.rpc.objects.UID;
import slatepowered.slate.model.NodeComponent;

import java.util.concurrent.CompletableFuture;

/**
 * Manages node power for one node.
 */
public interface NodePowerControl extends RemoteObject<NodePowerService>, NodeComponent {

    @UID
    default String nodeName() {
        return null;
    }

    /**
     * Start this node.
     */
    @ObjectMethod
    void start();

    @ObjectMethod
    default CompletableFuture<Void> startAsync() {
        return null;
    }

    /**
     * Stop this node.
     */
    @ObjectMethod
    void stop();

    @ObjectMethod
    default CompletableFuture<Void> stopAsync() {
        return null;
    }

}
