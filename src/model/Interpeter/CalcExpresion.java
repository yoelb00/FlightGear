package model.Interpeter;

import model.Expression.*;
import model.Expression.Number;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;


public class CalcExpresion {

	public static double calc(String exp){
		Queue<String> queue = new LinkedList<String>();
		Stack<String> stack = new Stack<String>();
		Stack<Expression> stackExp = new Stack<Expression>();

		String[] split = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
		for (int i=0; i<split.length;i++){
			if (Utilities.isSymbolExist(split[i].replace(" ", "")))
				split[i] =Utilities.getSymbolValue(split[i].replace(" ", "")).toString();

			if (isDouble(split[i])){
				queue.add(split[i]);
			}
			else{
				switch(split[i]) {
			    case "/":
			    case "*":
			    case "(":
			        stack.push(split[i]);
			        break;
			    case "+":
			    case "-":
			    	while (!stack.empty() && (!stack.peek().equals("("))){
			    		queue.add(stack.pop());
			    	}
			        stack.push(split[i]);
			        break;
			    case ")":
			    	while (!stack.peek().equals("(")){
			    		queue.add(stack.pop());
			    	}
			    	stack.pop();
			        break;
				}
			}
		}
		while(!stack.isEmpty()){
			queue.add(stack.pop());
		}

		for(String str : queue) {
			if (isDouble(str)){
				stackExp.push(new Number(Double.parseDouble(str)));
			}
			else{
				Expression right = stackExp.pop();
				Expression left;
				if(!stackExp.isEmpty()){
					left = stackExp.pop();
				}
				else{
					left = new Number(0);
				}

				switch(str) {
			    case "/":
			    	stackExp.push(new Div(left, right));
			        break;
			    case "*":
			    	stackExp.push(new Mul(left, right));
			        break;
			    case "+":
			    	stackExp.push(new Plus(left, right));
			        break;
			    case "-":
			    	stackExp.push(new Minus(left, right));
			        break;
				}
			}
		}

		return Math.floor((stackExp.pop().calculate() * 1000)) /1000;
	}

	private static boolean isDouble(String val){
		try {
		    Double.parseDouble(val);
		    return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}


	}

//
//
//
//
//public class CalcExpresion {
//	public static Expression parseExpression(String exp){
//		Queue<String> queue = new LinkedList<String>();
//		Stack<String> stack = new Stack<String>();
//		Stack<Expression> stackExp = new Stack<Expression>();
//
//		String[] split = exp.split("(?<=[-+*/()])|(?=[-+*/()])");
//		for (String s : split){
//			if (Utilities.isSymbolExist(s)) {
//                s = "" + Utilities.getSymbolValue(s).calculate();
//            }
//
//			if (isDouble(s)){
//				queue.add(s);
//			}
//
//			else{
//				switch(s) {
//			    case "/":
//			    case "*":
//			    case "(":
//			        stack.push(s);
//			        break;
//			    case "+":
//			    case "-":
//			    	while (!stack.empty() && (!stack.peek().equals("("))){
//			    		queue.add(stack.pop());
//			    	}
//			        stack.push(s);
//			        break;
//			    case ")":
//			    	while (!stack.peek().equals("(")){
//			    		queue.add(stack.pop());
//			    	}
//			    	stack.pop();
//			        break;
//				}
//			}
//		}
//		while(!stack.isEmpty()){
//			queue.add(stack.pop());
//		}
//
//		for(String str : queue) {
//			if (isDouble(str)){
//				stackExp.push(new Number(Double.parseDouble(str)));
//			}
//			else{
//				Expression right = stackExp.pop();
//				Expression left;
//				if(!stackExp.isEmpty()){
//					left = stackExp.pop();
//				}
//				else{
//					left = new Number(0);
//				}
//
//				switch(str) {
//			    case "/":
//			    	stackExp.push(new Div(left, right));
//			        break;
//			    case "*":
//			    	stackExp.push(new Mul(left, right));
//			        break;
//			    case "+":
//			    	stackExp.push(new Plus(left, right));
//			        break;
//			    case "-":
//			    	stackExp.push(new Minus(left, right));
//			        break;
//				}
//			}
//		}
//
//
//		return stackExp.pop();
//	}
//
//	public static double calc (String ex) {
//		Expression exp = parseExpression(ex);
//		return calc(exp);
//	}
//
//	public static double calc(Expression exp) {
//		return Math.floor((exp.calculate() * 1000)) /1000;
//	}
//
//	private static boolean isDouble(String val){
//		try {
//		    Double.parseDouble(val);
//		    return true;
//		} catch (NumberFormatException e) {
//			return false;
//		}
//	}
//
//
//
//}
