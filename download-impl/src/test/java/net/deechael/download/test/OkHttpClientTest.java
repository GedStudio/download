package net.deechael.download.test;

import okhttp3.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;

public class OkHttpClientTest {

    @Test
    public void test() throws IOException {
        OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890))).build();
        Call call = client.newCall(new Request.Builder()
                .get()
                .url("https://raw.githubusercontent.com/GedStudio/download/master/README.md")
                .build());
        Response response = call.execute();
        byte[] bytes = response.body().bytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println(bytes.length);
    }

}
