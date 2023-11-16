package slatepowered.slateware.velocity;

import slatepowered.veru.config.Section;

/**
 * Represents a player information forwarding strategy
 * which can be set on a Velocity proxy configuration.
 */
public interface VelocityForwarding {

    // todo: more support

    /**
     * The legacy, BungeeCord-compatible forwarding option.
     */
    static VelocityForwarding legacy() {
        return new VelocityForwarding() {
            @Override
            public String getOptionValue() {
                return "legacy";
            }

            @Override
            public void configure(Section section) {

            }
        };
    }

    /**
     * Get the value to set the `player-info-forwarding-mode` option in
     * the Velocity configuration.
     *
     * @return The option value.
     */
    String getOptionValue();

    /**
     * Configures any auxiliary options in the configuration.
     *
     * @param section The section to configure.
     */
    void configure(Section section);

}
