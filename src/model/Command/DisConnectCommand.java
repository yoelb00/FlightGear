package model.Command;


import model.Server.DataReaderServer;

public class DisConnectCommand extends CommandBase {

	@Override
	public int execute(String[] args) throws Exception {

		ConnectCommand.close();
		DataReaderServer.close();

		return 0;
	}

}
