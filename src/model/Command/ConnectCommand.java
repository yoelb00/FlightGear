package model.Command;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import model.Server.DataReaderServer;


public class ConnectCommand implements Command{

	private int port;
	private volatile static boolean stop=false;

	public static Socket server;
	public static PrintWriter out;


	@Override
	public int execute(String[] args) throws Exception {
		this.port = Integer.parseInt(args[1]);
		new Thread(() -> runClient(args)).start();
		return 0;
	}

	private void runClient(String[] args) {
		while (!stop)
		{
			try
			{
				server = new Socket(args[0], port);
				out = new PrintWriter(server.getOutputStream());
				stop=true;
			} catch (IOException e) {}
		}
	}

	public static void close() {
		stop = true;
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		out.close();
	}

    /*public static void Send(String[] data) {
    		System.out.println(data[0] + data[1] + " "+ data[2]);
            out.println(data[0] + data[1]  + " " + data[2]);
            out.flush();
    }*/

	@Override
	public void testArgs(String[] args) throws Exception {
		// TODO Auto-generated method stub

	}


}
