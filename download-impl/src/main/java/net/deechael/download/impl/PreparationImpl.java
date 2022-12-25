package net.deechael.download.impl;

import net.deechael.download.Download;
import net.deechael.download.Preparation;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.*;
import java.nio.file.Path;
import java.util.*;

public class PreparationImpl implements Preparation {

    private URL url;

    private File destination;

    private final Map<String, String> headers = new HashMap<>();

    private final Map<String, List<Map.Entry<String, String>>> cookies = new HashMap<>();

    private Proxy proxy;

    private int clips = 1;

    private String method = "GET";

    @Override
    public @NotNull Preparation url(@NotNull String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public @NotNull Preparation url(@NotNull URL url) {
        this.url = url;
        return this;
    }

    @Override
    public @NotNull Preparation url(@NotNull URI url) {
        try {
            this.url = url.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public @NotNull Preparation destination(@NotNull String destination) {
        this.destination = new File(destination);
        return this;
    }

    @Override
    public @NotNull Preparation destination(@NotNull Path destination) {
        this.destination = destination.toFile();
        return this;
    }

    @Override
    public @NotNull Preparation destination(@NotNull File destination) {
        this.destination = destination;
        return this;
    }

    @Override
    public @NotNull Preparation header(@NotNull String key, @NotNull String value) {
        this.headers.put(key, value);
        return this;
    }

    @Override
    public @NotNull Preparation cookie(@NotNull String domain, @NotNull String key, @NotNull String value) {
        if (!this.cookies.containsKey(domain))
            this.cookies.put(domain, new ArrayList<>());
        this.cookies.get(domain).add(new AbstractMap.SimpleEntry<>(key, value));
        return this;
    }

    @Override
    public @NotNull Preparation method(String method) {
        this.method = method;
        return this;
    }

    @Override
    public @NotNull Preparation proxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    public @NotNull Preparation proxy(Proxy.Type type, String host, int port) {
        return this.proxy(new Proxy(type, new InetSocketAddress(host, port)));
    }

    @Override
    public @NotNull Preparation clips(int amount) {
        this.clips = amount;
        return this;
    }

    @Override
    public @NotNull Download download() {
        return new DownloadImpl(this.url, this.destination, this.headers, this.cookies, this.proxy, this.clips, this.method);
    }

    @Override
    public Preparation clone() {
        PreparationImpl preparation = new PreparationImpl();
        preparation.headers.putAll(this.headers);
        preparation.cookies.putAll(Util.cloneCookies(this.cookies));
        return preparation
                .method(this.method)
                .clips(this.clips)
                .proxy(this.proxy);
    }

}
