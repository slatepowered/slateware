package test.slatepowered.slateware;

import slatepowered.slate.allocation.NodeAllocator;
import slatepowered.slate.logging.Logger;
import slatepowered.slate.logging.Logging;
import slatepowered.slate.master.Master;
import slatepowered.slate.master.MasterBootstrap;
import slatepowered.slate.model.MasterManagedNode;
import slatepowered.slate.model.NodeBuilder;
import slatepowered.slateware.node.NodePowerControl;
import slatepowered.slateware.node.NodePowerService;
import slatepowered.slateware.velocity.VelocityForwarding;
import slatepowered.slateware.velocity.VelocityNodeBuilder;
import slatepowered.veru.runtime.JavaVersion;

public class VelocityMasterTest {

    public static void main(String[] args) {
        MasterBootstrap.start(args);
        MasterBootstrap.awaitCloseAsync();

        final Logger logger = Logging.getLogger("VelocityMasterTest");

        Master master = MasterBootstrap.getMaster();

        // build master proxy
        NodeBuilder proxy = master.master().child("masterProxy");
        VelocityNodeBuilder.velocity(proxy)
                .installLibraries(false)
                .version("3.2.0-SNAPSHOT")
                .buildNumber("294")
                .port(25565)
                .forwarding(VelocityForwarding.legacy())
                .javaVersion(JavaVersion.JAVA_17);
        proxy.attach(master.getIntegratedCluster().getService(NodeAllocator.class));

        MasterManagedNode node = proxy.build();
        node.initialize().whenComplete((__, err) -> {
            if (err != null) {
                // u should handle errors but fuck that
                err.printStackTrace();
                return;
            }

            NodePowerControl nodePowerControl = node.getService(NodePowerService.remote()).forNode(node.getName());
            nodePowerControl.startAsync().whenComplete((__1, err1) -> {
                logger.info("Started node masterProxy");
            });
        });
    }

}
