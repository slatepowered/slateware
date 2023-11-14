package slatepowered.slateware.waterfall;

import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.NodeBuilder;
import slatepowered.slate.model.NodeBuilderAdapter;
import slatepowered.slate.model.NodeComponent;
import slatepowered.slate.packages.Packages;
import slatepowered.slateware.node.NodePowerControl;
import slatepowered.slateware.node.NodePowerService;
import slatepowered.slateware.packages.LibraryPackages;

/**
 * Helper for creating Waterfall nodes.
 */
public class WaterfallNodeBuilder extends NodeBuilderAdapter {

    public static WaterfallNodeBuilder waterfall(NodeBuilder nodeBuilder) {
        return new WaterfallNodeBuilder(nodeBuilder);
    }

    protected final WaterfallNodeComponent component;

    protected String waterfallBuild;   // The Waterfall build number
    protected String minecraftVersion; // The Minecraft version for the proxy

    public WaterfallNodeBuilder(NodeBuilder nodeBuilder) {
        super(nodeBuilder);

        // make sure the cluster has the waterfall support
        // library loaded so the components can be deserialized
        nodeBuilder.attach(LibraryPackages.all(WaterfallNodeBuilder.class, "slateware.waterfall.libs.txt"));
        nodeBuilder.attach(component = new WaterfallNodeComponent());

        nodeBuilder.attach(new NodeComponent() {
            @Override
            public boolean attached(ManagedNode node) {
                String url = "https://api.papermc.io/v2/projects/waterfall/versions/" + minecraftVersion + "/builds/" + waterfallBuild + "/downloads/waterfall-" + minecraftVersion + "-" + waterfallBuild + ".jar";

                // attach components
                node.attach(Packages.linkFiles(Packages.download(url, "file"), (dir, p) -> dir.resolve("proxy.jar")));

                return false;
            }
        });
    }

    /**
     * Set the version/build of Waterfall to install on the node.
     *
     * @param build The build.
     * @return This.
     */
    public WaterfallNodeBuilder waterfallBuild(String build) {
        this.waterfallBuild = build;
        return this;
    }

    /**
     * Set the Minecraft version to use for the proxy.
     *
     * @param minecraftVersion The Minecraft version.
     * @return This.
     */
    public WaterfallNodeBuilder minecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
        return this;
    }

}
