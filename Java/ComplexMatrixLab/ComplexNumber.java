import java.lang.Math;

public class ComplexNumber{
    private double re = 0;
    private double im = 0;

    public ComplexNumber(){
        this.re = 0;
        this.im = 0;
    }

    public ComplexNumber(double re, double im){
        this.re = re;
        this.im = im;
    }

    public ComplexNumber(double re){
        this.re = re;
        this.im = 0;
    }

    public String algebraicForm(){
        String finalForm = "" + this.re;
        if (this.im > 0){
            finalForm += "+" + this.im + "i";
        }
        if (this.im < 0){
            finalForm += this.im + "i";
        }

        return finalForm;
    }

    public ComplexNumber add(ComplexNumber otherComplexNumber){
        ComplexNumber newComplexNumber = new ComplexNumber(this.re, this.im);
        newComplexNumber.re += otherComplexNumber.re;
        newComplexNumber.im += otherComplexNumber.im;
        return newComplexNumber;
    }

    public ComplexNumber mult(ComplexNumber otherComplexNumber){
        ComplexNumber newComplexNumber = new ComplexNumber(this.re, this.im);
        newComplexNumber.re = this.re * otherComplexNumber.re - this.im * otherComplexNumber.im;
        newComplexNumber.im = this.re * otherComplexNumber.im + this.im * otherComplexNumber.re;
        return newComplexNumber;
    }

    public ComplexNumber div(ComplexNumber otherComplexNumber){
        ComplexNumber newComplexNumber = new ComplexNumber(this.re, this.im);
        double denominator = Math.pow(otherComplexNumber.re, 2) + Math.pow(otherComplexNumber.im, 2);
        newComplexNumber.re = (this.re * otherComplexNumber.re + this.im * otherComplexNumber.im) / denominator;
        newComplexNumber.im = (this.im * otherComplexNumber.re - this.re * otherComplexNumber.im) / denominator;
        return newComplexNumber;
    }
    public String trigonometricForm(){
        double length = Math.sqrt(Math.pow(this.re, 2) + Math.pow(this.im,2));
        if (length == 0)
            return "0";
        double angle = Math.acos(this.re/length);;
        return "" + length + " cos(" + angle + ") + " + length + " i*sin(" + angle + ")";

    }

    public static void main(String args[]){
        ComplexNumber z1 = new ComplexNumber(5, -6);
        ComplexNumber z2 = new ComplexNumber(-3, 2);
        ComplexNumber z3;
        z3 = z1.add(z2);
        System.out.println("ADD: " + z3.trigonometricForm());
        z3 = z2.mult(z1);
        System.out.println("MULT: " + z3.algebraicForm());
        z3 = z2.div(z1);
        System.out.println("DIV: " + z3.algebraicForm());
    }
}