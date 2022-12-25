package net.deechael.download;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

public interface Preparation {

    /**
     * Set the source of the location to download
     * @param url the source
     * @return self
     */
    @NotNull
    Preparation url(@NotNull String url);

    /**
     * Set the source of the location to download
     * @param url the source
     * @return self
     */
    @NotNull
    Preparation url(@NotNull URL url);

    /**
     * Set the source of the location to download
     * @param url the source
     * @return self
     */
    @NotNull
    Preparation url(@NotNull URI url);

    /**
     * Where to save the downloaded file
     * @param destination should be a file not a directory
     * @return self
     */
    @NotNull
    Preparation destination(@NotNull String destination);

    /**
     * Where to save the downloaded file
     * @param destination should be a file not a directory
     * @return self
     */
    @NotNull
    Preparation destination(@NotNull Path destination);

    /**
     * Where to save the downloaded file
     * @param destination should be a file not a directory
     * @return self
     */
    @NotNull
    Preparation destination(@NotNull File destination);

    /**
     * Add header
     * @param key header key
     * @param value header value
     * @return self
     */
    @NotNull
    Preparation header(@NotNull String key, @NotNull String value);

    /**
     * Add cookie
     * @param domain domain
     * @param key cookie key
     * @param value cookie value
     * @return self
     */
    @NotNull
    Preparation cookie(@NotNull String domain, @NotNull String key, @NotNull String value);

    /**
     * Set request method
     * @param method request method
     * @return self
     */
    @NotNull
    Preparation method(String method);

    /**
     * Set proxy, null for no proxy
     * @param proxy proxy
     * @return self
     */
    @NotNull
    Preparation proxy(@Nullable Proxy proxy);

    /**
     * Set proxy
     * @param type proxy type
     * @param host proxy host
     * @param port proxy port
     * @return self
     */
    @NotNull
    Preparation proxy(Proxy.Type type, String host, int port);

    /**
     * Set proxy with http type
     * @param host proxy host
     * @param port proxy port
     * @return self
     */
    @NotNull
    default Preparation proxyHttp(String host, int port) {
        return this.proxy(Proxy.Type.HTTP, host, port);
    }

    /**
     * Set proxy with socks type
     * @param host proxy host
     * @param port proxy port
     * @return self
     */
    @NotNull
    default Preparation proxySocks(String host, int port) {
        return this.proxy(Proxy.Type.SOCKS, host, port);
    }

    /**
     * Set how many clips to download the file
     * @param amount clips amount
     * @return self
     */
    @NotNull
    Preparation clips(int amount);

    /**
     * Creating download task
     * @return download task
     */
    @NotNull
    Download download();

    /**
     * Clone a preparation which doesn't have source url and destination
     * @return cloned preparation
     */
    Preparation clone();

}
