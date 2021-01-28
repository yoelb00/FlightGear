package model.Command;

public class SleepCommand implements Command {

	@Override
	public int execute(String[] arg) throws Exception {
		// TODO Auto-generated method stub
		Thread.sleep(Integer.parseInt(arg[1]));
		return 0;
	}

	@Override
	public void testArgs(String[] args) throws Exception {
		// TODO Auto-generated method stub

	}

}
