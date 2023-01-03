package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;


public class SimpleChatServer
{

	private static SimpleServerClass server;
    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServerClass(3000);
        System.out.println("server is listening");
        server.listen();
    }
}
