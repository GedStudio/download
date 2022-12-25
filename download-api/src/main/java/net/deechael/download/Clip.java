package net.deechael.download;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public interface Clip {

    /**
     * If this clip has ended
     * @return end status
     */
    boolean ended();

    /**
     * If this clip has started
     * @return start status
     */
    boolean started();

    /**
     * Interrupt this clip downloading
     */
    void interrupt();

    /**
     * Start downloading
     */
    void start();

    /**
     * How many bytes this clip has downloaded
     * @return downloaded bytes
     */
    long downloaded();

    /**
     * The rest bytes to be downloaded
     * @return rest bytes
     */
    long rest();

    /**
     * Total size to download
     * @return total bytes
     */
    long size();

}
