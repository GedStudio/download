package net.deechael.download;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public interface Download {

    /**
     * Download source
     * @return source url
     */
    @NotNull
    URL url();

    /**
     * If the download is ended
     * @return end status
     */
    boolean ended();

    /**
     * If the download has started
     * @return start status
     */
    boolean started();

    /**
     * Interrupt downloading
     */
    void interrupt();

    /**
     * Start downloading
     */
    void start();

    /**
     * Start downloading in async
     */
    void startAsync();

    /**
     * Wait for end while using async downloading
     */
    void await();

    /**
     * The destination to save the file
     * @return a file path not a directory path
     */
    @NotNull
    Path destination();

    /**
     * The size of the file, real size can be got after starting
     * @return -1 before starting
     */
    long size();

    /**
     * Get downloading clips
     * @return clips
     */
    @NotNull
    List<? extends Clip> clips();

    /**
     * Create a preparation to start downloading
     * @return preparation
     */
    @NotNull
    static Preparation prepare() {
        throw new RuntimeException("Not implemented");
    }

}
