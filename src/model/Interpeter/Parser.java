package model.Interpeter;

import model.Command.*;
import model.Expression.Number;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Parser {

	public static class ParsedData {
		public static Queue<Command> cmdQ = new LinkedList<>();
		public static Queue<String[]> argsQ = new LinkedList();
		public static List<String> errors = new LinkedList<>();
	}

	public int parse(String[] lines, Lexer lexer) throws IOException {

		ParsedData parsedData = new ParsedData();
		ParsedData.argsQ = new LinkedList();
		ParsedData.cmdQ = new LinkedList<>();
	   	  Utilities.setSymbol("simX", new Number(0));
          Utilities.setSymbol("simY", new Number(0));
          Utilities.setSymbol("simZ", new Number(0));

		String line = null;
		Command cmd;
		String cmdName;
		String args[];

		for (int lineIndex = 0; lineIndex < lines.length; lineIndex++) {

			line = lines[lineIndex];
			if (line.trim().length() == 0)
				continue;
			try {
				List<String> tokens = lexer.lexer(line);
				String[] temp=new String[tokens.size()];
				if (tokens.get(0).equals("while"))
				{
					List<String> loopArr = new ArrayList<String>();
					int i;
					for (i = lineIndex; i < lines.length; i++) {
						loopArr.add(lines[i]);

						if (lines[i].contains("}"))
							break;
					}
					String[] Loop = new String[loopArr.size()];
					for (int j = 0; j < loopArr.size(); j++) {
						Loop[j] = loopArr.get(j);
					}

					lineIndex = i;
					cmd = Utilities.getCommand("while");
					parsedData.argsQ.add(Loop);
					parsedData.cmdQ.add(cmd);
					Utilities.getCommand("while").execute(Loop);
					continue;
				}

				if (tokens.get(0).equals("connect")) {
					String[] ipAndPort = new String[2];
					ipAndPort[0] = tokens.get(1);
					ipAndPort[1] = tokens.get(2);
					parsedData.argsQ.add(ipAndPort);
					parsedData.cmdQ.add(Utilities.getCommand("connect"));
					Utilities.getCommand("connect").execute(ipAndPort);
					continue;
				}
				if(tokens.toString().contains("=")) {
					for (int i = 0; i < tokens.size(); i++)
						temp[i]=tokens.get(i);
					parsedData.argsQ.add(temp);
					parsedData.cmdQ.add(Utilities.getCommand("equal"));
					Utilities.getCommand("equal").execute(temp);
					continue;
				}
				if(tokens.toString().contains("print")){
					String[] print = new String[2];
					print[0] = tokens.get(0);
					print[1] = tokens.get(1);
					parsedData.argsQ.add(print);
					parsedData.cmdQ.add(Utilities.getCommand("print"));
					Utilities.getCommand("print").execute(print);
					continue;
				}
				if(tokens.toString().contains("sleep")){
					String[] sleep = new String[2];
					sleep[0] = tokens.get(0);
					sleep[1] = tokens.get(1);
					parsedData.argsQ.add(sleep);
					parsedData.cmdQ.add(Utilities.getCommand("sleep"));
					Utilities.getCommand("sleep").execute(sleep);
					continue;
				}
				// [x=495] one-cell
				// [var y=x+3] Two cells (with create var)
				// [var y =x+3] or [y = x + 3] More then two cells
				cmdName = tokens.get(0);

				if (!Utilities.isCommandExist(cmdName)) {
					parsedData.errors.add(lineIndex + ": Command is not valid.");
					continue;
				}

				cmd = Utilities.getCommand(cmdName);
				args = tokens.toArray(new String[0]);

				parsedData.argsQ.add(args);
				parsedData.cmdQ.add(cmd);

				if (cmdName.startsWith("return"))
					break;

			} catch (Exception e) {
				parsedData.errors.add(lineIndex + " : " + e.getMessage());
				System.out.println("error " + e.getMessage());
				continue;
			}

		}
		if ((line == null || !line.startsWith("return")) && parsedData.errors.isEmpty())
			parsedData.errors.add("Program must contain a return statement.");

		return 0;
	}

}
