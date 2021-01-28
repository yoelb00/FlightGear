package model.Command;

import model.Interpeter.Utilities;

public class PrintCommand extends CommandBase {
    @Override
    public int execute(String[] args) throws Exception {
    	if(!Utilities.isSymbolExist(args[1])){
    		System.out.println(args[1]);
    	}
    	else{
    		System.out.print(args[1]+"= "+ model.Interpeter.Utilities.getSymbolValue(args[1])+"\n");
    	}
        return 0;
    }

    @Override
    public void testArgs(String[] args) throws Exception {
        //TODO
    }
}
