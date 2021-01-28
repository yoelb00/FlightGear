package model.Command;


public class ReturnCommand extends CommandBase{

	@Override
	public int execute(String[] args) throws Exception {
		// TODO Auto-generated method stub

		StringBuilder math = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			math.append(args[i]);
		}
		return (int)model.Interpeter.CalcExpresion.calc(math.toString());


	}

    @Override
    public void testArgs(String[] args) throws Exception{

    }


}
