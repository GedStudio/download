package net.deechael.download.impl;

import net.deechael.download.Clip;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ClipImpl implements Clip {

    private final URL url;
    private final File destination;

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, List<Map.Entry<String, String>>> cookies = new HashMap<>();

    private final Proxy proxy;

    private final String method;

    private final long from;
    private final long to;

    private final Thread thread;

    private final File tempFile;

    private final ExecutorService executorService;

    private volatile boolean started;

    private volatile boolean ended;

    private Future<?> task;

    public ClipImpl(URL url, File destination, Map<String, String> headers, Map<String, List<Map.Entry<String, String>>> cookies, Proxy proxy, long from, long to, String method, ExecutorService executorService) {
        this.url = url;
        this.destination = destination;
        this.headers.putAll(headers);
        this.cookies.putAll(Util.cloneCookies(cookies));
        this.proxy = proxy;
        this.method = method;
        this.from = from;
        this.to = to;

        this.headers.put("Range", "bytes=" + from + "-" + (to != -1 ? to : ""));

        this.thread = new Thread(this::download);
        try {
            this.tempFile = File.createTempFile(Util.randomId(16), ".downloadcache");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.executorService = executorService;
    }

    @Override
    public boolean ended() {
        return this.ended;
    }

    @Override
    public boolean started() {
        return this.started;
    }

    @Override
    public void interrupt() {
        if (this.task != null) {
            this.started = false;
            this.ended = true;
            this.task.cancel(true);
        }
    }

    private void download() {
        try {
            HttpURLConnection connection = (HttpURLConnection) (this.proxy == null ? this.url.openConnection() : this.url.openConnection(proxy));
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
            if (this.cookies.size() > 0) {
                connection.addRequestProperty("cookie", Util.processCookie(this.cookies));
            }
            connection.setRequestMethod(this.method);
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            RandomAccessFile randomAccessFile = new RandomAccessFile(this.destination, "rwd");
            randomAccessFile.seek(this.from);
            //OutputStream outputStream = new FileOutputStream(this.tempFile);
            byte[] buffer = new byte[2048];
            int len;
            while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                randomAccessFile.write(buffer, 0, len);
                //outputStream.write(buffer, 0, len);
            }
            inputStream.close();
            //outputStream.flush();
            //outputStream.close();
            randomAccessFile.close();
            connection.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.started = false;
        this.ended = true;
    }

    @Override
    public void start() {
        this.started = true;
        this.task = this.executorService.submit(this.thread);
    }

    @Override
    public synchronized long downloaded() {
        return this.tempFile.length();
    }

    @Override
    public synchronized long rest() {
        return this.size() - this.downloaded();
    }

    @Override
    public synchronized long size() {
        return this.to - this.from + 1;
    }

}
