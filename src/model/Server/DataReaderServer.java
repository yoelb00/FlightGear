package model.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import model.Expression.Number;
import model.Interpeter.Utilities;

//to use it without create implements then use static

public class DataReaderServer {
	private int port;
	private volatile static boolean stop=false;
	public static boolean t = false;
	public static ServerSocket server;
	public static BufferedReader in;
	public static Socket client;
	public static Thread thread;

	public DataReaderServer(int port) {
		setPathsCommands();
		this.port = port;
		thread = new Thread(() -> runServer());
		thread.start();
	}

	private void setPathsCommands() {
		Utilities.setSymbol("/controls/flight/speedbrake", new Number(0));
		Utilities.setSymbol("/instrumentation/airspeed-indicator/indicated-speed-kt", new Number(0));
		Utilities.setSymbol("/instrumentation/altimeter/indicated-altitude-ft", new Number(0));
		Utilities.setSymbol("/instrumentation/altimeter/pressure-alt-ft", new Number(0));
		Utilities.setSymbol("/instrumentation/attitude-indicator/indicated-pitch-deg", new Number(0));
		Utilities.setSymbol("/instrumentation/attitude-indicator/indicated-roll-deg", new Number(0));
		Utilities.setSymbol("/instrumentation/attitude-indicator/internal-pitch-deg", new Number(0));
		Utilities.setSymbol("/instrumentation/attitude-indicator/internal-roll-deg", new Number(0));
		Utilities.setSymbol("/instrumentation/encoder/indicated-altitude-ft", new Number(0));
		Utilities.setSymbol("/instrumentation/encoder/pressure-alt-ft", new Number(0));
		Utilities.setSymbol("/instrumentation/gps/indicated-altitude-ft", new Number(0));
		Utilities.setSymbol("/instrumentation/gps/indicated-ground-speed-kt", new Number(0));
		Utilities.setSymbol("/instrumentation/gps/indicated-vertical-speed", new Number(0));
		Utilities.setSymbol("/instrumentation/heading-indicator/indicated-heading-deg", new Number(0));
		Utilities.setSymbol("/instrumentation/magnetic-compass/indicated-heading-deg", new Number(0));
		Utilities.setSymbol("/instrumentation/slip-skid-ball/indicated-slip-skid", new Number(0));
		Utilities.setSymbol("/instrumentation/turn-indicator/indicated-turn-rate", new Number(0));
		Utilities.setSymbol("/instrumentation/vertical-speed-indicator/indicated-speed-fpm", new Number(0));
		Utilities.setSymbol("/controls/flight/aileron", new Number(0));
		Utilities.setSymbol("/controls/flight/elevator", new Number(0));
		Utilities.setSymbol("/controls/flight/rudder", new Number(0));
		Utilities.setSymbol("/controls/flight/flaps", new Number(0));
		Utilities.setSymbol("/controls/engines/current-engine/throttle", new Number(0));
		Utilities.setSymbol("/engines/engine/rpm", new Number(0));
		Utilities.setSymbol("x", new Number(0));
		Utilities.setSymbol("y", new Number(0));
	}

	private void runServer() {

		try {
			server = new ServerSocket(port);
			server.setSoTimeout(1000);

			while (!stop) {
				try {
					client = server.accept();
					in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					String line = null;
					System.out.println("connected to " + port);
					while (!stop)
					{
						line = in.readLine();
						String[] temp = line.split("\t*,\t*");
						bind(temp);
					}

				} catch (SocketTimeoutException e) {}
			}
		} catch (Exception e) { }
	}


	public static void close() {
		stop = true;

		try {in.close();}
		catch (IOException e) {e.printStackTrace();}

		try {client.close();}
		catch (IOException e) {e.printStackTrace();}

		try {server.close();}
		catch (IOException e) {e.printStackTrace();}

	}
	public void bind(String[] b){

		Utilities.getSymbolValue("/instrumentation/airspeed-indicator/indicated-speed-kt").setNumber(Double.parseDouble(b[0]));
		Utilities.getSymbolValue("/instrumentation/altimeter/indicated-altitude-ft").setNumber(Double.parseDouble(b[1]));
		Utilities.getSymbolValue("/instrumentation/altimeter/pressure-alt-ft").setNumber(Double.parseDouble(b[2]));
		Utilities.getSymbolValue("/instrumentation/attitude-indicator/indicated-pitch-deg").setNumber(Double.parseDouble(b[3]));
		Utilities.getSymbolValue("/instrumentation/attitude-indicator/indicated-roll-deg").setNumber(Double.parseDouble(b[4]));
		Utilities.getSymbolValue("/instrumentation/attitude-indicator/internal-pitch-deg").setNumber(Double.parseDouble(b[5]));
		Utilities.getSymbolValue("/instrumentation/attitude-indicator/internal-roll-deg").setNumber(Double.parseDouble(b[6]));
		Utilities.getSymbolValue("/instrumentation/encoder/indicated-altitude-ft").setNumber(Double.parseDouble(b[7]));
		Utilities.getSymbolValue("/instrumentation/encoder/pressure-alt-ft").setNumber(Double.parseDouble(b[8]));
		Utilities.getSymbolValue("/instrumentation/gps/indicated-altitude-ft").setNumber(Double.parseDouble(b[9]));
		Utilities.getSymbolValue("/instrumentation/gps/indicated-ground-speed-kt").setNumber(Double.parseDouble(b[10]));
		Utilities.getSymbolValue("/instrumentation/gps/indicated-vertical-speed").setNumber(Double.parseDouble(b[11]));
		Utilities.getSymbolValue("/instrumentation/heading-indicator/indicated-heading-deg").setNumber(Double.parseDouble(b[12]));
		Utilities.getSymbolValue("/instrumentation/magnetic-compass/indicated-heading-deg").setNumber(Double.parseDouble(b[13]));
		Utilities.getSymbolValue("/instrumentation/slip-skid-ball/indicated-slip-skid").setNumber(Double.parseDouble(b[14]));
		Utilities.getSymbolValue("/instrumentation/turn-indicator/indicated-turn-rate").setNumber(Double.parseDouble(b[15]));
		Utilities.getSymbolValue("/instrumentation/vertical-speed-indicator/indicated-speed-fpm").setNumber(Double.parseDouble(b[16]));
		Utilities.getSymbolValue("/controls/flight/aileron").setNumber(Double.parseDouble(b[17]));
		Utilities.getSymbolValue("/controls/flight/elevator").setNumber(Double.parseDouble(b[18]));
		Utilities.getSymbolValue("/controls/flight/rudder").setNumber(Double.parseDouble(b[19]));
		Utilities.getSymbolValue("/controls/flight/flaps").setNumber(Double.parseDouble(b[20]));
		Utilities.getSymbolValue("/controls/engines/current-engine/throttle").setNumber(Double.parseDouble(b[21]));
		Utilities.getSymbolValue("/engines/engine/rpm").setNumber(Double.parseDouble(b[22]));
		Utilities.getSymbolValue("x").setNumber(Double.parseDouble(b[23]));
		Utilities.getSymbolValue("y").setNumber(Double.parseDouble(b[24]));
	}
}
