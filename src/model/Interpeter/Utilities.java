package model.Interpeter;

import model.Command.*;
import model.Expression.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Utilities {

    public static Map<String, Expression> symbolTable = new HashMap<>();
    public static Map<String, Command> commandTable = new HashMap<>();
    public static boolean wait=false;
    public static Set<String> varTypes = new HashSet<>();
    public static Map<String,String> bind = new HashMap<>();


    public static Expression getSymbolValue(String symbol){
        return symbolTable.getOrDefault(symbol.replace("\"", ""), null);
    }
    public static boolean isSymbolExist(String symbol){
        return symbolTable.containsKey(symbol);
    }
    public static void setSymbol(String symbol, Expression value){
        symbolTable.put(symbol, value);

    }
    public static Command getCommand(String name){
        return commandTable.getOrDefault(name, null);
    }

    public static void setCommand(String name, Command cmd){
        commandTable.put(name, cmd);
    }


    public static String getBind(String name){
        return bind.getOrDefault(name.replace("\"", ""), null);
    }

    public static void setBind(String name, String cmd){
        bind.put(name, cmd);
    }
	public static void resetVariablesTable()
	{
		//delete all the variables that not some path to simulator command:
		Set<String> keys = symbolTable.keySet();
		String[] arr=new String[keys.size()];
		int i=0;
		for (String k: keys)
		{
			arr[i]=k;
			i++;
		}
		for (int j=0;j<arr.length;j++)
		{
			if(!arr[j].contains("/"))
			{
				symbolTable.remove(arr[j]);
				if(isSymbolExist(arr[j]))
					System.out.println("yes and not good");
			}
		}
		//Reset the bind table:
		bind=new HashMap<>();
	}

    public static boolean isCommandExist(String name){
        return commandTable.containsKey(name);
    }
}
