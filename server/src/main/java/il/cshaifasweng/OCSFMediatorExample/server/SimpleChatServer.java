package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.MySQL;

import java.io.IOException;


public class SimpleChatServer
{

	private static SimpleServerClass server;
    public static void main( String[] args ) throws IOException
    {
        try {

            MySQL.connectToDB();
            server = new SimpleServerClass(3000);
            System.out.println("server is listening");
            server.listen();
            MySQL.commitToDB();
        }
        catch (Exception e){
            MySQL.handleException(e);
        }
        finally {
            MySQL.finalizeConnection();
        }
    }
}
