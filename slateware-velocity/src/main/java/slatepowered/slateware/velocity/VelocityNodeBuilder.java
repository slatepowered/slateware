package slatepowered.slateware.velocity;

import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.NodeBuilder;
import slatepowered.slate.model.NodeBuilderAdapter;
import slatepowered.slate.model.NodeComponent;
import slatepowered.slate.packages.Packages;
import slatepowered.slateware.packages.LibraryPackages;
import slatepowered.veru.runtime.JavaVersion;

/**
 * Builds the required components for a Velocity installation
 * on a node to function properly.
 */
public class VelocityNodeBuilder extends NodeBuilderAdapter {

    public static VelocityNodeBuilder velocity(NodeBuilder nodeBuilder) {
        return new VelocityNodeBuilder(nodeBuilder);
    }

    protected final VelocityNodeComponent component;

    protected String velocityVersion;  // The Velocity build number
    protected String buildNumber;      // The specific build number/ID
    protected JavaVersion javaVersion; // The Java version to use

    public VelocityNodeBuilder(NodeBuilder nodeBuilder) {
        super(nodeBuilder);
        javaVersion = JavaVersion.JAVA_16;

        // make sure the cluster has the waterfall support
        // library loaded so the components can be deserialized
        nodeBuilder.attach(LibraryPackages.all(VelocityNodeBuilder.class, "slateware.waterfall.libs.txt"));
        nodeBuilder.attach(Packages.jdk(javaVersion).attachment());
        nodeBuilder.attach(component = new VelocityNodeComponent());

        nodeBuilder.attach(new NodeComponent() {
            @Override
            public boolean attached(ManagedNode node) {
                // todo: fetch latest version and build number if none
                //  are specified explicitly, preferably do this when
                //  the package is installed on the node and not here
                //  this could be done with a ResolvedPackageKeyUnion impl

                String url = "https://api.papermc.io/v2/projects/velocity/versions/" + velocityVersion +
                        "/builds/" + buildNumber + "/downloads/" +
                        "velocity-" + velocityVersion + "-" + buildNumber + ".jar";

                // attach components
                node.attach(Packages.linkFiles(Packages.download(url, "file"), (dir, p) -> dir.resolve("proxy.jar")));

                return false;
            }
        });
    }

    /**
     * Set the version of Velocity to install on the node.
     *
     * @param version The version.
     * @return This.
     */
    public VelocityNodeBuilder version(String version) {
        this.velocityVersion = version;
        return this;
    }

    /**
     * Set the exact build number of Velocity to install on the node.
     *
     * @param buildNumber The build number.
     * @return This.
     */
    public VelocityNodeBuilder buildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
        return this;
    }

    /**
     * Set the Java version which is to be used by the proxy.
     *
     * @param javaVersion The Java version.
     * @return This.
     */
    public VelocityNodeBuilder javaVersion(JavaVersion javaVersion) {
        this.javaVersion = javaVersion;
        return this;
    }

}
