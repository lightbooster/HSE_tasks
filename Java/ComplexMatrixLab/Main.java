
public class Main {

    public static void main(String args[]){

        // * COMPLEX *

        ComplexNumber z1 = new ComplexNumber(5, -6);
        ComplexNumber z2 = new ComplexNumber(-3, 2);
        ComplexNumber z3;
        z3 = z1.add(z2);
        System.out.println("ADD: " + z3.trigonometricForm());
        z3 = z2.mult(z1);
        System.out.println("MULT: " + z3.algebraicForm());
        z3 = z2.div(z1);
        System.out.println("DIV: " + z3.algebraicForm());

        // * MATRIX *

        Matrix matrix = new Matrix(2, 2);
        matrix.setValue(new ComplexNumber(1), 0, 0);
        matrix.setValue(new ComplexNumber(2), 0, 1);
        matrix.setValue(new ComplexNumber(3), 1, 0);
        matrix.setValue(new ComplexNumber(4), 1, 1);
        System.out.println("Matrix:");
        matrix.printMatrix();
        Matrix matrix1 = null;
        try {
            matrix1 = matrix.clone();
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Not cloneable object");
        } catch (Exception e_super){
            System.out.println("Some problems have been emerged: " + e_super.getMessage());
        }

        System.out.println("Matrix + Matrix:");
        assert matrix1 != null;
        matrix1.add(matrix).printMatrix();

        System.out.println("Matrix * 3:");
        matrix.mult(3).printMatrix();

        System.out.println("Matrix ^ 2:");
        matrix.mult(matrix1).printMatrix();

        System.out.println("D (Matrix):");
        System.out.println(matrix.getDeterminant().algebraicForm());

        System.out.println("T (Matrix):");
        matrix.transposed().printMatrix();

        Matrix matrix2 = new Matrix(3, 3);
        matrix2.setValue(new ComplexNumber(1), 0, 0);
        matrix2.setValue(new ComplexNumber(2), 0, 1);
        matrix2.setValue(new ComplexNumber(3), 1, 0);
        matrix2.setValue(new ComplexNumber(4), 1, 1);
        matrix2.setValue(new ComplexNumber(5), 2, 2);
        System.out.println("Matrix2: ");
        matrix2.printMatrix();

        System.out.println("D (Matrix2):");
        System.out.println(matrix2.getDeterminant().algebraicForm());
    }
}
