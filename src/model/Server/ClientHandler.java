package model.Server;

import java.io.InputStream;
import java.io.OutputStream;

public interface ClientHandler {
	public void handleClient(InputStream in,OutputStream out);
}