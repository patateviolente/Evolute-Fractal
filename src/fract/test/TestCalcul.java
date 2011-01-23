package fract.test;

public class TestCalcul {

	public static void main(String[] args) {
		new Test();
		System.out.println("-------------");
		new Test2();
		
	}

}

class Test{
	public Test(){
		_z1 = _x = _y = new double[1];
		calculateStepZZero(0, 10, 20);
		System.out.println(_z1[0]);
	}
	public void calculateStepZZero(int thread, int x, int y) {
		this._z1[0] = 0;
		this._x[0] = x;
		this._y[0] = y;
	}
	private double[] _z1, _x, _y;
}


class Test2{
	public Test2(){
		calculateStepZZero(0, 10, 20);
		System.out.println(_z1);
	}
	public void calculateStepZZero(int thread, int x, int y) {
		this._z1 = 0;
		this._x = x;
		this._y = y;
	}
	private double _z1, _y, _x;
}