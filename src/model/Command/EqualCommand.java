package model.Command;


import model.SimModel;
import model.Expression.Number;
import model.Interpeter.Utilities;



public class EqualCommand extends CommandBase {

	@Override
	public int execute(String[] args) throws Exception {
		Utilities.wait = true;

		//Good for the "BIND":
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < args.length; i++)
			sb.append(args[i] + " ");
		//If args contains "var":
		if(args[0].equals("var"))
		{
			String variable="";
			String[] dec=new String[2];
			dec[0]=args[0];
			dec[1]=args[1];
			variable=dec[1];
			model.Interpeter.Utilities.setSymbol(variable, new Number(0));
			//[var,y,=,x,+,3]:
			if(!sb.toString().contains("bind"))
			{
				StringBuilder valueOfEqual=new StringBuilder();
				for (int i = 3; i < args.length; i++) {
					valueOfEqual.append(args[i]);
				}
				int value = (int)model.Interpeter.CalcExpresion.calc(valueOfEqual.toString());
				Utilities.getSymbolValue(variable).setNumber(value);


			}

			if(sb.toString().contains("bind"))
			{
					model.Interpeter.Utilities.setBind(args[1], args[4].replace("\"", ""));
					Utilities.setSymbol(args[1], Utilities.getSymbolValue(args[4]));
			}
		}
		else {
			if(Utilities.getBind(args[0])!= null)
			{
				StringBuilder tmp=new StringBuilder();
				for (int i = sb.indexOf("=")+1; i < sb.length(); i++) {
					tmp.append(sb.charAt(i));
				}
				Double value = model.Interpeter.CalcExpresion.calc(tmp.toString());

				Utilities.getSymbolValue(args[0]).setNumber(value);

				SimModel.SendAP(new String[]{
					"set ",
					Utilities.getBind(args[0]),
					value.toString()
				});
			}
			else{
				Double value = model.Interpeter.CalcExpresion.calc(args[2]);
				Utilities.getSymbolValue(args[0]).setNumber(value);
			}
		}
		Utilities.wait = false;
		return 0;
	}

}
