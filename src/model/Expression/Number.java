package model.Expression;

public class Number implements Expression {

	private double value;

	public Number(double value) {
		this.value =value;
	}

	@Override
	public double calculate() {
		// TODO Auto-generated method stub
		return value;
	}
	public void setNumber(int num) {
		// TODO Auto-generated method stub
		this.value=(double)num;
	}
	public void setNumber(double num) {
		// TODO Auto-generated method stub
		this.value=num;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+value;
	}

}
