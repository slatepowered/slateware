package slatepowered.slateware.velocity;

import net.kyori.adventure.text.Component;
import slatepowered.slate.allocation.NodeAllocator;
import slatepowered.slate.model.NodeBuilder;
import slatepowered.slate.model.NodeBuilderAdapter;
import slatepowered.slate.packages.Packages;
import slatepowered.slateware.packages.SoftwarePackages;
import slatepowered.veru.config.Section;
import slatepowered.veru.data.Pair;
import slatepowered.veru.runtime.JavaVersion;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Builds the required components for a Velocity installation
 * on a node to function properly.
 */
public class VelocityNodeBuilder extends NodeBuilderAdapter {

    public static VelocityNodeBuilder velocity(NodeBuilder nodeBuilder) {
        return new VelocityNodeBuilder(nodeBuilder);
    }

    protected final VelocityNodeComponent component;

    protected boolean installLibraries; // Manually specify whether to install the support plugins/libraries.
    protected String velocityVersion;   // The Velocity build number
    protected String buildNumber;       // The specific build number/ID
    protected JavaVersion javaVersion;  // The Java version to use

    public VelocityNodeBuilder(NodeBuilder nodeBuilder) {
        super(nodeBuilder);
        javaVersion = JavaVersion.JAVA_17;
        installLibraries = true;

        // make sure the cluster has the waterfall support
        // library loaded so the components can be deserialized

        // todo: check if the node allocator is this jvm
        //  and if so dont install the libraries
        nodeBuilder.attach(Packages.jdk(javaVersion).attachment());
        nodeBuilder.attach(component = new VelocityNodeComponent());

        nodeBuilder.then(node -> {
            if (installLibraries) {
                nodeBuilder.attach(SoftwarePackages.all(VelocityNodeBuilder.class, "/slateware.velocity.libs.txt"));
            }

            // todo: fetch latest version and build number if none
            //  are specified explicitly, preferably do this when
            //  the package is installed on the node and not here
            //  this could be done with a ResolvedPackageKeyUnion impl

            String url = "https://api.papermc.io/v2/projects/velocity/versions/" + velocityVersion +
                    "/builds/" + buildNumber + "/downloads/" +
                    "velocity-" + velocityVersion + "-" + buildNumber + ".jar";

            // attach components
            node.attach(Packages.linkFiles(Packages.download(url, "proxy.jar"), Pair.of("proxy.jar", "proxy.jar")));
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

    /**
     * Set the port which the proxy will bind to.
     *
     * @param port The port.
     * @return This.
     */
    public VelocityNodeBuilder port(int port) {
        component.port = port;
        return this;
    }

    /**
     * Set the host address (without the port) to bind to.
     *
     * @param host The address.
     * @return This.
     */
    public VelocityNodeBuilder address(String host) {
        component.address = host;
        return this;
    }

    /**
     * Enable or disable online mode.
     *
     * @param b Whether to enable online mode.
     * @return This.
     */
    public VelocityNodeBuilder onlineMode(boolean b) {
        component.onlineMode = b;
        return this;
    }

    /**
     * Set the player information forwarding strategy Velocity should use.
     *
     * @param forwarding The forwarding strategy.
     * @return This.
     */
    public VelocityNodeBuilder forwarding(VelocityForwarding forwarding) {
        component.forwardingMode = forwarding;
        return this;
    }

    /**
     * Add the given section as configuration overrides.
     *
     * @param section The section.
     * @return This.
     */
    public VelocityNodeBuilder overrides(Section section) {
        component.configOverride.putAll(section.toMap());
        return this;
    }

    /**
     * Get the mutable configuration override section.
     *
     * @return The section.
     */
    public Section overrides() {
        return component.configOverride;
    }

    /**
     * Edit the mutable configuration override section.
     *
     * @param consumer The consumer.
     * @return This.
     */
    public VelocityNodeBuilder overrides(Consumer<Section> consumer) {
        consumer.accept(component.configOverride);
        return this;
    }

    /**
     * Set the *displayed* number of players maximum in the
     * query response, this doesn't actually limit anything it
     * is only for display.
     *
     * @param n The number.
     * @return This.
     */
    public VelocityNodeBuilder displayedMaxPlayers(int n) {
        component.showMaxPlayers = n;
        return this;
    }

    /**
     * Set the initial MOTD for the Velocity configuration.
     *
     * @param text The MOTD text component.
     * @return This.
     */
    public VelocityNodeBuilder motd(Component text) {
        component.motd = text;
        return this;
    }

    public VelocityNodeBuilder installLibraries(boolean installLibraries) {
        this.installLibraries = installLibraries;
        return this;
    }

}
