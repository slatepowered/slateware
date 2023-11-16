package slatepowered.slateware.velocity;

import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import slatepowered.veru.config.ConfigParser;
import slatepowered.veru.config.Section;
import slatepowered.veru.config.TomlConfigParser;
import slatepowered.veru.misc.Throwables;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Create a valid Velocity configuration from the data component.
 */
final class VelocityConfigFactory {

    private static final ConfigParser PARSER = TomlConfigParser.standard();

    private static void setIfAbsent(Section section, String name, Object value) {
        if (section.contains(name))
            return;
        section.set(name, value);
    }

    /**
     * Write the data from the given data component to the given file
     * as a valid Velocity config.
     *
     * @param dataComponent The data.
     * @param file The output file.
     */
    public static void writeConfig(VelocityNodeComponent dataComponent,
                                   Path file) {
        if (Files.exists(file))
            return;

        Section section = dataComponent.configOverride == null ? Section.memory(new HashMap<>()) : dataComponent.configOverride;

        // write options
        setIfAbsent(section, "bind", dataComponent.address + ":" + dataComponent.port);
        setIfAbsent(section, "onlineMode", dataComponent.onlineMode);
        setIfAbsent(section, "player-info-forwarding-mode", dataComponent.forwardingMode.getOptionValue());
        dataComponent.forwardingMode.configure(section);
        setIfAbsent(section, "show-max-players", dataComponent.showMaxPlayers);
        setIfAbsent(section, "motd", GsonComponentSerializer.gson().serialize(dataComponent.motd));

        try {
            // write section to file
            FileWriter writer = new FileWriter(file.toFile());
            PARSER.write(writer, section);
            writer.close();
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
        }
    }

}
