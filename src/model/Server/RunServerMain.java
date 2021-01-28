package model.Server;

import model.Command.OpenServerCommand;

public class RunServerMain {

    public static void main(String[] args) {
    /*	OpenServerCommand ser=new OpenServerCommand();
		try {
			ser.execute(new String[] {"5400","10"});
		} catch (Exception e) {
			//e.printStackTrace();
			}*/
    	Server server = new MySerialServer();
        CacheManager cacheManager = new FileCacheManager();
        MyClientHandler clientHandler = new MyClientHandler(cacheManager);
        server.open(5454 ,new ClientHandlerPath(clientHandler));
    }
}