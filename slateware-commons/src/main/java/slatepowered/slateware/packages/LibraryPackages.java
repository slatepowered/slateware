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

public final class LibraryPackages {

    /**
     * Reads the text file from the resource, then sets up a
     * downloading package of all the files and sets up the
     * immediate load.
     */
    public static TargetedPackageAttachment<LocalFilesPackage> all(Class<?> srcClass, String resource) {
        try {
            InputStream stream = srcClass.getResourceAsStream(resource);
            if (stream == null)
                throw new IllegalArgumentException("Could not find resource: " + resource);

            String content = new String(IOUtil.readAllBytes(stream), StandardCharsets.UTF_8);
            stream.close();

            // parse content
            List<Pair<String, String>> files = new ArrayList<>();
            List<String> fileNames = new ArrayList<>();
            for (String line : content.split("\n")) {
                String url = line.split("=")[0];
                String filename = line.split("=")[1];

                files.add(Pair.of(url, filename));
                fileNames.add(filename);
            }

            return Packages.loadLibrariesImmediate(Packages.download(files), fileNames.toArray(new String[0]))
                    .targeted(PackageTarget.HOST);
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
