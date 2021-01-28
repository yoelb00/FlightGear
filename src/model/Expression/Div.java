package model.Expression;

public class Div extends BinaryExpression {



	public Div(Expression left, Expression right) {
		super(left, right);
		// TODO Auto-generated constructor stub
	}

	@Override
	public double calculate(){
		// TODO Auto-generated method stub
		if(right.calculate()==0)
			return 0;

		return left.calculate()/right.calculate();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "("+this.left.toString()+"/"+this.right.toString()+")";
	}

	@Override
	public void setNumber(double num) {
		// TODO Auto-generated method stub

	}

}
