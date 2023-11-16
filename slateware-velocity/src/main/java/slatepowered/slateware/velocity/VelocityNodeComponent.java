package slatepowered.slateware.velocity;

import lombok.Builder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import slatepowered.slate.model.ClusterNetwork;
import slatepowered.slate.model.ManagedNode;
import slatepowered.slate.model.SharedNodeComponent;
import slatepowered.veru.config.Section;

import java.util.HashMap;

/**
 * The data component which holds information about the Velocity installation.
 */
public class VelocityNodeComponent implements SharedNodeComponent {

    /**
     * Any miscellaneous configuration overrides.
     */
    public Section configOverride = Section.memory(new HashMap<>());

    /**
     * The address host to bind to.
     */
    public String address = "127.0.0.1";

    /**
     * The port the server should bind to.
     */
    public int port;

    /**
     * Whether online mode should be on.
     */
    public boolean onlineMode = true;

    /**
     * The forwarding mode.
     */
    public VelocityForwarding forwardingMode;

    /**
     * The displayed max players in the query response.
     */
    public int showMaxPlayers = 0;

    /**
     * The MOTD component.
     */
    public Component motd = Component.text("A ").color(NamedTextColor.GRAY)
            .append(Component.text("Slate ").color(NamedTextColor.RED))
            .append(Component.text("+ ").color(NamedTextColor.GRAY))
            .append(Component.text("Velocity ").color(NamedTextColor.AQUA))
            .append(Component.text("powered server.").color(NamedTextColor.GRAY));

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
