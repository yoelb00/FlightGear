package model.Command;

import java.util.ArrayList;
import java.util.List;


public class BindCommand extends CommandBase {
	@Override
	public int execute(String[] args) throws Exception {

		String[] newArgs = new String[args.length - 1];
		for (int i = 0, j = 0; i < args.length; j++, i++) {
			if (args[i].equals("bind")) {
				j--;
				continue;
			}
			newArgs[j] = args[i];
		}
		int indexOfEqual;
		if (args[0].equals("var"))
			indexOfEqual = 2;
		else
			indexOfEqual = 1;
		List<String> beforEqual = new ArrayList<>();
		List<String> afterBind = new ArrayList<>();

		for (int i = 0; i < indexOfEqual; i++)
			beforEqual.add(newArgs[i]);
		for (int i = indexOfEqual; i < newArgs.length; i++)
			afterBind.add(newArgs[i]);

		if(beforEqual.get(0).equals("var"))
			beforEqual.remove(0);

		String[] arr = new String[1];
		arr[0] = (beforEqual.toString() + afterBind.toString()).replace(",", "").replace("[", "").replace("]", "").replace(" ", "");

		EqualCommand e=new EqualCommand();
		e.execute(arr);

//		Command cmd = Utilities.getCommand("equal");
//		Parser.ParsedData.argsQ.add(arr);
//		Parser.ParsedData.cmdQ.add(cmd);
		return 0;
	}

}
