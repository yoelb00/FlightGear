package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Interpeter.Utilities;
public class SimModel extends Observable implements  Observer {
	private static MyInterpreter myInter;
    private static PrintWriter out;
    private static Socket socket;
	public static BufferedReader inServer;
	public static boolean stopServer;
	private Thread autoPilot;
	int[][] matrix;
	double planeStartX;
	double planeStartY;
	double planeDestX;
	double planeDestY;
	public StringProperty path;
    double currentlocationX;
    double currentlocationY;
    double currentHeading;
    double startX;
    double startY;
    double offset;
    int[][] data;
    double markX;
    double markY;
    double planeX;
    double planeY;
    private static Socket socketPath;
    private  static PrintWriter outPath;
    private  static BufferedReader in;
    public static volatile boolean stop=false;
	public SimModel(){
	this.path = new SimpleStringProperty();
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
	}
	public void autoPilot(List<String> lines){
		this.autoPilot = new Thread(() -> myInter.interpret(lines));
		this.autoPilot.start();
	}
	@SuppressWarnings("deprecation")
	public void StopAutoPilot(){
		if(this.autoPilot != null && this.autoPilot.isAlive()){
			this.autoPilot.stop();
			Utilities.resetVariablesTable();
		}
	}
	public void connect(String ip, int port) {
		while(!stopServer)
		{
	        try
	        {
	            socket = new Socket(ip, port);
	            out = new PrintWriter(socket.getOutputStream());
	            stopServer=true;
	        } catch (IOException e) {}
        System.out.println("Connect to server...");
	}
}
    public void Send(String[] data) {
        for (String string : data) {
            out.println(string);
            out.flush();
            System.out.println(string);
        }

    }
    public static void SendAP(String[] data) {
    	System.out.println(data[0] + data[1] + " "+ data[2]);
        out.println(data[0] + data[1]  + " " + data[2]);
        out.flush();
    }
    public void GetAirPlaneLocation(double startX,double startY, double offset)
    {
        this.offset=offset;
        this.startX=startX;
        this.startY=startY;
		new Thread(() ->
		{
			Socket socket = null;
			try
			{
				socket = new Socket("127.0.0.1", 5402);
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				while (!stop)
				{
					out.println("dump /position");
					out.flush();
					String line;
					List<String> lines = new ArrayList<>();
					while (!(line = br.readLine()).equals("</PropertyList>"))
						if (!line.equals(""))
							lines.add(line);
					String longtitude = lines.get(2);
					String latitude = lines.get(3);
					String[] x = longtitude.split("[<>]");
					String[] y = latitude.split("[<>]");
					br.readLine();
					out.println("get /instrumentation/heading-indicator/indicated-heading-deg");
					out.flush();
					String[] h = br.readLine().split(" ");
					int tmp = h[3].length();
					currentlocationX = Double.parseDouble(x[2]);
					currentlocationY = Double.parseDouble(y[2]);
					currentHeading = Double.parseDouble(h[3].substring(1, tmp - 1));
					String[] data = { "plane", x[2], y[2], h[3].substring(1, tmp - 1) };
					this.setChanged();
					this.notifyObservers(data);
					try
					{Thread.sleep(250);}
					catch (InterruptedException e) {}
				}
				socket.close();
			}
			catch (IOException e)
			{
				try{socket.close();}
				catch (IOException ex) {}
			}
		}).start();
	}
	public void connectPath(String ip, int port)
	{
		try
		{
			socketPath = new Socket(ip, port);
			outPath = new PrintWriter(socketPath.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socketPath.getInputStream()));
		} catch (IOException e) {}
	}
	public void findPlaneGPSPath(int planeX, int planeY, int markX, int markY, int[][] data)
	{
		this.planeX = planeX;
		this.planeY = planeY;
		this.markX = markX;
		this.markY = markY;
		this.data = data;
		new Thread(() ->
		{
			int j, i;
			for (i = 0; i < data.length; i++)
			{
				System.out.print("");
				for (j = 0; j < data[i].length - 1; j++)
					outPath.print(data[i][j] + ",");
				outPath.println(data[i][j]);
			}
			outPath.println("end");
			outPath.println(planeX + "," + planeY);
			outPath.println(markX + "," + markY);
			outPath.flush();
			String theSolution = null;
			try {theSolution = in.readLine();} catch (IOException e) {e.printStackTrace();}
			System.out.println("The path is:");
			System.out.println(theSolution);
			String[] tmp = theSolution.split(",");
			String[] notify = new String[tmp.length + 1];
			notify[0] = "path";
			for (i = 0; i < tmp.length; i++)
				notify[i + 1] = tmp[i];
			this.setChanged();
			this.notifyObservers(notify);
		}).start();
	}
}