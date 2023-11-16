package slatepowered.slateware.packages;

import slatepowered.slate.packages.PackageAttachment;
import slatepowered.slate.packages.PackageTarget;
import slatepowered.slate.packages.Packages;
import slatepowered.slate.packages.attachment.TargetedPackageAttachment;
import slatepowered.slate.packages.local.LocalFilesPackage;
import slatepowered.veru.data.Pair;
import slatepowered.veru.io.IOUtil;
import slatepowered.veru.misc.Throwables;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class SoftwarePackages {

    /**
     * Reads the text file from the resource, then sets up a
     * downloading package of all the files and sets up the
     * immediate load.
     */
    public static PackageAttachment<LocalFilesPackage> all(Class<?> srcClass, String resource) {
        try {
            InputStream stream = srcClass.getResourceAsStream(resource);
            if (stream == null)
                throw new IllegalArgumentException("Could not find resource: " + resource);

            String content = new String(IOUtil.readAllBytes(stream), StandardCharsets.UTF_8);
            stream.close();

            String[] split; // re-usable variable

            // parse content
            List<Pair<String, String>> files = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            for (String line : content.split("\n")) {
                String url = line.split("=")[0].trim();
                String filename = line.split("=")[1].trim();

                // process `jitpack:` URL specifier
                if (url.startsWith("jitpack:")) {
                    String artifactSpec = url.substring("jitpack:".length());
                    split = artifactSpec.split(":");
                    String artifactName = split[0];
                    String artifactVersion = split[1];
                    split = artifactName.split("\\.");
                    String artifactSimpleName = split[split.length - 1];

                    url = "https://jitpack.io/" + artifactName.replace('.', '/') + "/" + artifactVersion + "/" +
                            (artifactSimpleName + "-" + artifactVersion + ".jar");
                }

                files.add(Pair.of(url, filename));
                fileNames.add(filename);
            }

            return Packages.loadImmediate(Packages.download(files), fileNames.toArray(new String[0]));
        } catch (Throwable t) {
            Throwables.sneakyThrow(t);
            throw new AssertionError();
        }
    }

    /**
     * Downloads and loads the library at the given URL on the
     * node host (cluster).
     *
     * @return The attachment.
     */
    public static TargetedPackageAttachment<LocalFilesPackage> url(String url) {
        return Packages.loadLibrariesImmediate(Packages.download(url, "file"), "file").targeted(PackageTarget.HOST);
    }

    public static PackageAttachment<LocalFilesPackage> jitpack(String group, String artifact, String version, String extra) {
        return url("https://jitpack.io/" + group.replace('.', '/') + "/" + artifact + "/" + version + "/" + artifact + "-" + version + (extra == null ? "" : "-" + extra) + ".jar");
    }

    public static PackageAttachment<LocalFilesPackage> jitpack(String group, String artifact, String version) {
        return jitpack(group, artifact, version, null);
    }

}
