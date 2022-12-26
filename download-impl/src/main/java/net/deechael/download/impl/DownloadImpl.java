package net.deechael.download.impl;

import net.deechael.download.Clip;
import net.deechael.download.Download;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownloadImpl implements Download {

    private final URL url;
    private final File destination;

    private final Map<String, String> headers;

    private final Map<String, List<Map.Entry<String, String>>> cookies;

    private final Proxy proxy;
    private final List<Clip> clips;
    private int clipAmount;

    private final String method;

    private long size = -1;

    private final ExecutorService threadPool;

    public DownloadImpl(URL url, File destination, Map<String, String> headers, Map<String, List<Map.Entry<String, String>>> cookies, Proxy proxy, int clips, String method) {
        if (url == null)
            throw new RuntimeException("Url cannot be null!");
        if (destination == null)
            throw new RuntimeException("Destination cannot be null!");
        this.url = url;
        this.destination = destination;
        this.headers = headers;
        this.cookies = cookies;
        this.proxy = proxy;
        this.clips = new ArrayList<>(clips);
        this.clipAmount = clips;
        this.method = method;

        this.threadPool = Executors.newFixedThreadPool(clips);
        this.fetchInfo();
    }

    private void fetchInfo() {
        try {
            HttpURLConnection connection = (HttpURLConnection) (this.proxy == null ? this.url.openConnection() : this.url.openConnection(proxy));
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
            if (this.cookies.size() > 0) {
                connection.addRequestProperty("cookie", Util.processCookie(this.cookies));
            }
            connection.setRequestMethod("HEAD");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.connect();
            Map<String, List<String>> headers = connection.getHeaderFields();
            connection.disconnect();
            headers.keySet().forEach(string -> {
                if (string == null)
                    return;
                if (string.equalsIgnoreCase("Transfer-Encoding")) {
                    headers.get(string).stream().map(String::toLowerCase)
                            .forEach(s -> {
                                if (s.equalsIgnoreCase("chunked"))
                                    try {
                                        throw new IOException("Cannot download this file because it is chunked!");
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                            });
                }
            });
            Map<String, String> remappedHeaders = Util.remap(headers);
            if (remappedHeaders.containsKey("accept-ranges"))
                if (!remappedHeaders.get("accept-ranges").equalsIgnoreCase("bytes"))
                    throw new RuntimeException("Cannot download this file!");
            if (!remappedHeaders.containsKey("content-length"))
                throw new RuntimeException("Cannot download this file!");
            if (!Util.isLong(remappedHeaders.get("content-length")))
                throw new RuntimeException("Cannot download this file!");
            this.size = Long.parseLong(remappedHeaders.get("content-length"));
            if (this.clipAmount > this.size)
                this.clipAmount = (int) this.size;
            long divided = this.size / this.clipAmount;
            for (int i = 0; i < this.clipAmount; i++) {
                if (i == (this.clipAmount - 1)) {
                    this.clips.add(makeClip(i * divided, -1));
                    break;
                }
                this.clips.add(makeClip(i * divided, (i + 1) * divided - 1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull URL url() {
        return this.url;
    }

    @Override
    public boolean ended() {
        return this.threadPool.isTerminated();
    }

    @Override
    public boolean started() {
        return !this.threadPool.isTerminated();
    }

    @Override
    public void interrupt() {
        this.threadPool.shutdownNow();
    }

    @Override
    public void start() {
        this.startAsync();
        this.await();
    }

    @Override
    public void startAsync() {
        Util.initFile(this.destination, this.size);
        this.clips.forEach(Clip::start);
        threadPool.shutdown();
    }

    @Override
    public void await() {
        if (this.ended())
            return;
        try {
            this.threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public @NotNull Path destination() {
        return this.destination.toPath();
    }

    @Override
    public long size() {
        return this.size;
    }

    @Override
    public @NotNull List<? extends Clip> clips() {
        return this.clips;
    }

    private Clip makeClip(long from, long to) {
        return new ClipImpl(this.url, this.destination, this.headers, this.cookies, this.proxy, from, to, this.method, this.threadPool);
    }

}
