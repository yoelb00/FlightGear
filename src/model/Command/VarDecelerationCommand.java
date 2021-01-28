package model.Command;

import model.Interpeter.Utilities;


import model.Expression.Number;


public class VarDecelerationCommand extends CommandBase {



    public VarDecelerationCommand(){
        this.numOfArgs = 2;
    }

    @Override
    public int execute(String[] args) throws Exception {

        this.testArgs(args);

        //[var,x]
        //[var,y=x+3]
        //[var,y,=,x,+,3]
        //[var, x, =, bind, simX]


        EqualCommand Equal = new EqualCommand();
        StringBuilder sb=new StringBuilder();
        String word=new String();
        for (int i = 1; i < args.length; i++) {
			sb.append(args[i]);
		}

        //[x]
        //[y=x+3]
        //[y,=,x,+,3]
        //[x,=,bind,simX]
        word = sb.toString();
        //[x]
        //[y=x+3]
        //[y=x+3]
        //[x=bindsimX]
        //check if stringbuilder to string, recive excalcty this ^^



        if(word.contains("=")) {
        	String[] equal = new String[1];
        	equal[0] = word;
        	Equal.execute(equal);
        }
        else {
        	  Utilities.setSymbol(word, new Number(0));
        }

        return 0;
    }

    @Override
    public void testArgs(String[] args) throws Exception {
        if(args.length < this.numOfArgs)
            throw new Exception("Var Deceleration Command is expecting at least "+this.numOfArgs+" Args, but "+args.length+" were given.");
        String varType = args[0];

//        if(!Utilities.varTypes.contains(varType))
//            throw new Exception("Var type "+varType+" is not supported");


    }
}
