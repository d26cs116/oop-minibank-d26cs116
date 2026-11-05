abstract class Shape {
	public abstract double area();
}

class Circle extends Shape{
	private double radius;
	Circle(double radius) {
		this.radius=radius;
	}
	public double area() {
		return 3.14 * radius * radius;
	}

}

class Rectangle extends Shape{
	private double length;
	private double width;
	Rectangle(double l , double w) {
		length = l;
		width = w;
	}
	public double area() {
		return length * width;
	}
}

class Triangle extends Shape{
	private double base;
	private double height;
	Triangle(double b , double h) {
		base = b;
		height = h;
	}

	public double area() {
		return 0.5 * base * height;
	}
}

public class ShapeDemo {
	public static void main(String[] args){
		Shape []shapes = {new Circle(2.13) , new Rectangle(1.2,3.4), new Triangle(1.2,3.4)};
		double total = 0;
        double largest = 0;

        for (Shape sh : shapes) {

            double currentArea = sh.area();

            System.out.println(
                    sh.getClass().getSimpleName() +
                    " area = " + currentArea
            );

            total += currentArea;

            if (currentArea > largest) {
                largest = currentArea;
            }
        }

        System.out.println("Total area = " + total);
        System.out.println("Largest area = " + largest);

	}
}

