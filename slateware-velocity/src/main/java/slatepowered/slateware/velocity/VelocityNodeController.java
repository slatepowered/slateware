package slatepowered.slateware.velocity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import slatepowered.slate.action.NodeAllocationAdapter;
import slatepowered.slate.cluster.ClusterInstance;
import slatepowered.slate.model.ClusterManagedNode;
import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.NodeComponent;
import slatepowered.slate.packages.PackageManager;
import slatepowered.slate.packages.local.LocalJavaPackage;
import slatepowered.slateware.node.NodePowerControl;
import slatepowered.veru.misc.Throwables;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This component is installed on the cluster/node host exclusively.
 */
@RequiredArgsConstructor
public class VelocityNodeController implements NodeComponent, NodePowerControl, NodeAllocationAdapter {

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
            LocalJavaPackage javaPackage = node.findComponents(LocalJavaPackage.class).first()
                    .orElseThrow(() -> new IllegalArgumentException("No Java package attached to this node, can not start process"));
            ClusterManagedNode clusterManagedNode = (ClusterManagedNode) node;

            process = new ProcessBuilder()
                    .command("\"" + javaPackage.getJavaBinary().toAbsolutePath() + "\"", "-jar", "proxy.jar")
                    .directory(clusterManagedNode.getAllocation().getDirectory().toFile())
                    .start();
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

    @Override
    public void stop() {
        try {
            if (process.isAlive()) {
                // stop the process
                process.destroy();
            }
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

    @Override
    public void initialize(PackageManager packageManager, ManagedNode node, Path nodePath) {
        try {
            ClusterInstance clusterInstance = node.getNetwork();
            Path librariesShareDirectory = clusterInstance.getCluster().getClusterDirectory().resolve("libraries");
            if (!Files.exists(librariesShareDirectory)) {
                Files.createDirectories(librariesShareDirectory);
            }

            // write config and create/link directories
            VelocityConfigFactory.writeConfig(dataComponent, nodePath.resolve("velocity.toml"));
            Files.createDirectories(nodePath.resolve("plugins"));
            Files.createSymbolicLink(librariesShareDirectory, nodePath.resolve("libraries"));
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

}
