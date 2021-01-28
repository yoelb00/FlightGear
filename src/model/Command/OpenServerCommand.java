package model.Command;


import model.Server.DataReaderServer;

public class OpenServerCommand extends CommandBase {

	public DataReaderServer server;

	@Override
	public int execute(String[] args) throws Exception {
		
		server = new DataReaderServer(Integer.parseInt(args[0]));
		return 0;
	}

}
