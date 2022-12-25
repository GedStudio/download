package net.deechael.download.test;

import net.deechael.download.Download;
import net.deechael.download.Preparation;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class DownloadTest {

    @Test
    public void download() {
        Preparation readme =  Download.prepare()
                .destination(new File("test/README.md"))
                .url("https://raw.githubusercontent.com/GedStudio/download/master/README.md")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36")
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
                .clips(3);
        Preparation kookPng = readme.clone()
                .url("https://img.kookapp.cn/assets/2022-12/pBuIa1VFtJ073027.png")
                .destination(new File("test/kook.png"));
        Download readmeDownload = readme.download();
        readmeDownload.start();
        Download kookPngDownload = kookPng.download();
        kookPngDownload.start();
    }

}
