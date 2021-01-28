package model;

import java.io.IOException;
import java.util.List;

import model.Command.*;
import model.Interpeter.*;
import model.Expression.Number;

public class MyInterpreter {

	public static int interpret(List<String> arraylist){
		// call your interpreter here
		String[] lines = new String[arraylist.size()];

		for(int i = 0; i < lines.length; i++){
			lines[i] = arraylist.get(i);
		}

        Parser p = new Parser();
        Lexer l = new Lexer();
        Parser.ParsedData pd;
        int result = 0;


        Utilities.setCommand("return", new ReturnCommand());
        Utilities.setCommand("var", new VarDecelerationCommand());
        Utilities.setCommand("equal", new EqualCommand());
        Utilities.setCommand("openDataServer", new OpenServerCommand());
        Utilities.setCommand("connect", new ConnectCommand());
        Utilities.setCommand("bind", new BindCommand());
        Utilities.setCommand("disconnect", new DisConnectCommand());
        Utilities.setCommand("while", new WhileCommand());
        Utilities.setCommand("sleep", new SleepCommand());
        Utilities.setCommand("print", new PrintCommand());       
		
        
        
    	
        try {
			p.parse(lines, l);


		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return result;
	}
}
