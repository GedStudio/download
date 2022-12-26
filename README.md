# download
A lightweight download library

## Todo-List
- Async download
- Reusable

## How to use
### Import
#### Maven
Add repository
```xml
<repository>
    <id>deechael</id>
    <url>https://maven.deechael.net</url>
</repository>
```
Add dependency
```xml
<dependencies>
    <dependency>
        <groupId>net.deechael</groupId>
        <artifactId>download-api</artifactId>
        <version>1.00.0</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>net.deechael</groupId>
        <artifactId>download-impl</artifactId>
        <version>1.00.0</version>
    </dependency>
</dependencies>
```
#### Gradle
Add repository
```groovy
repositories {
    // ...
    maven {
        url 'https://maven.deechael.net'
    }
}
```
Add dependency
```groovy
dependencies {
    // ...
    compileOnly 'net.deechael:download-api:1.00.0'
    implementation 'net.deechael.download-impl:1.00.0'
}
```
### Coding
```java
public class Example {
    public static void main(String[] args) {
        Preparation preparation = Download.prepare(); // Prepare for downloading
        preparation.url("https://xxxxxxx.com/xxx.png") // set download source
                .destination(new File("xxx.png")) // set destination file
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36") // Add header
                .cookie("xxxxxxx.com", "auth", "token xxxxxxxxxx") // Add cookie
                .proxyHttp("127.0.0.1", 1080) // Add http proxy
                .clips(4); // Use 4 threads to download
        Download download = preparation.download(); // Try to create download task
        download.start(); // Start downloading
        
        Preparation another = preparation.clone(); // Clone the preparation but no url and destination in new one
        another.url("https://yyyyyy.net/xxxx.txt")
                .destination("folder/yyyy.txt");
        // The options of new preparation is same to the previous one, but no url and destination set
        Download anotherDownload = another.download(); // Try to download
        anotherDownload.startAsync(); // Download in async
        anotherDownload.interrupt(); // Force stop downloading
    }
}
```