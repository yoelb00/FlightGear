package model.Command;

import java.util.List;
import java.util.Scanner;

import model.Interpeter.Lexer;
import model.Interpeter.Utilities;

public class WhileCommand extends CommandBase {

	@Override
	public int execute(String[] args) throws Exception {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		for(int k=0; k < args.length; k++)
		{
			str.append(args[k] + " ");
		}

		EqualCommand Equal = new EqualCommand();

		StringBuilder sb = new StringBuilder();
//		sb.append(args[0].charAt(6));
		int i = args[0].indexOf("<");
		for(int j = i+2; j< args[0].indexOf("{")-1; j++)
		{
			sb.append(args[0].charAt(j));
		}
		StringBuilder leftside = new StringBuilder();
		for (int j = 6; j < args[0].indexOf("<")-1; j++) {
			leftside.append(args[0].charAt(j));
		}
		Lexer lexer = new Lexer();
		int rightside = Integer.parseInt(sb.toString());
		
		while(Utilities.getSymbolValue(leftside.toString()).calculate() < rightside)
		{
			for (int j = 1; j < args.length-1; j++)
			{
				StringBuilder line = new StringBuilder(args[j]);
				List<String> s = lexer.lexer(line.toString());
				String[] arr = new String[s.size()];
				StringBuilder temp = new StringBuilder();
				for(int n =0; n < arr.length; n++){
					arr[n] = s.get(n);
					temp.append(arr[n] + " ");
				}
				if(temp.toString().contains("print"))
				{
					Utilities.getCommand("print").execute(arr);
				}
				if(temp.toString().contains("sleep"))
				{
					Utilities.getCommand("sleep").execute(arr);
				}
				if(temp.toString().contains("="))
				{
					Utilities.getCommand("equal").execute(arr);
				}
			}
		}


		return 0;
	}

}
