package cc.dozer.netty.example;

import java.io.IOException;

/**
 * Dozer @ 5/24/15
 * mail@dozer.cc
 * http://www.dozer.cc
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        TcpServer server = new TcpServer(8080);
        TcpClient client = new TcpClient("127.0.0.1", 8080);

        System.out.println("==========Start Server First==========");
        server.init();
        client.init();
        Thread.sleep(2000);

        client.close();
        server.close();
        Thread.sleep(2000);

        System.out.println("==========Start Client First==========");
        client.init();
        server.init();
        Thread.sleep(2000);

        client.close();
        server.close();
        Thread.sleep(2000);

        System.out.println("==========Client Auto Reconnect==========");
        server.init();
        client.init();
        Thread.sleep(2000);

        server.close();
        Thread.sleep(2000);

        server.init();
        Thread.sleep(2000);

        client.close();
        server.close();

        System.in.read();
    }
}
