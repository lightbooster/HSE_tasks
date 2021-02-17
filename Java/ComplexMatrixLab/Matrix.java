import java.lang.Exception;

public class Matrix implements Cloneable {
    public class MatrixShape implements Cloneable {
        public int x, y;

        public MatrixShape(int x, int y){
            this.x = x;
            this.y = y;
        }

        public MatrixShape(int x){
            this.x = x;
            this.y = x;
        }

        public MatrixShape clone() throws CloneNotSupportedException{
            return (MatrixShape) super.clone();
        }
    }

    private ComplexNumber[][] matrix;
    private final MatrixShape shape;

    public Matrix (int size_x, int size_y) {
        shape = new MatrixShape(size_x, size_y);
        matrix = new ComplexNumber[size_x][size_y];
        for (int i = 0; i < shape.x; i++){
            for (int j = 0; j < shape.y; j++){
                matrix[i][j] = new ComplexNumber();
            }
        }
    }

    public Matrix (int size_x) {
        shape = new MatrixShape(size_x, size_x);
        matrix = new ComplexNumber[size_x][size_x];
        for (int i = 0; i < shape.x; i++){
            for (int j = 0; j < shape.y; j++){
                matrix[i][j] = new ComplexNumber();
            }
        }
    }

    public Matrix (MatrixShape shape) {
        this.shape = new MatrixShape(shape.x, shape.y);
        matrix = new ComplexNumber[shape.x][shape.y];
        for (int i = 0; i < shape.x; i++){
            for (int j = 0; j < shape.y; j++){
                matrix[i][j] = new ComplexNumber();
            }
        }
    }

    public Matrix clone() throws CloneNotSupportedException{
        return (Matrix) super.clone();
    }

    public MatrixShape getShape(){
        return shape;
    }

    public ComplexNumber getValue(int x, int y){
        if (x < 0 || x > shape.x || y < 0 || y > shape.y)
            throw new ArrayIndexOutOfBoundsException("Invalid matrix index; Use Matrix.getShape()");
        return matrix[x][y];
    }

    public void setValue(ComplexNumber value, int x, int y){
        if (x < 0 || x > shape.x || y < 0 || y > shape.y)
            throw new ArrayIndexOutOfBoundsException("Invalid matrix index; Use Matrix.getShape()");
        matrix[x][y] = value;
    }

    // *** ADD ***

    public Matrix add(Matrix anotherMatrix){
        if (anotherMatrix.getShape().x != shape.x || anotherMatrix.getShape().y != shape.y)
            throw new ArrayIndexOutOfBoundsException("Invalid matrix shape; Use Matrix.getShape()");

        Matrix newMatrix = new Matrix(shape);
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                newMatrix.setValue(matrix[i][j].add(anotherMatrix.getValue(i, j)), i, j);
            }
        }
        return newMatrix;
    }

    public Matrix add(ComplexNumber value) {
        Matrix newMatrix = new Matrix(shape);
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                newMatrix.setValue(matrix[i][j].add(value), i, j);
            }
        }
        return newMatrix;
    }

    public Matrix add(double value) {
        Matrix newMatrix = new Matrix(shape);
        ComplexNumber complexValue = new ComplexNumber (value);
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                newMatrix.setValue(matrix[i][j].add(complexValue), i, j);
            }
        }
        return newMatrix;
    }

    // *** MULTIPLY ***

    public Matrix mult(Matrix anotherMatrix){
        if (anotherMatrix.getShape().x != shape.y)
            throw new ArrayIndexOutOfBoundsException("Matrix shape are invalid for this operation; Use Matrix.getShape()");

        Matrix newMatrix = new Matrix(shape.x, anotherMatrix.getShape().y);

        for (int i = 0; i < shape.x; i++){
            for (int j = 0; j < anotherMatrix.getShape().y; j++){
                ComplexNumber sum = new ComplexNumber();
                for (int n = 0; n < shape.y; n++)
                    sum = sum.add(matrix[i][n].mult(anotherMatrix.getValue(n, j)));
                newMatrix.setValue(sum, i, j);
            }
        }

        return newMatrix;
    }

    public Matrix mult(ComplexNumber value){
        Matrix newMatrix = new Matrix(shape);
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                newMatrix.setValue(matrix[i][j].mult(value), i, j);
            }
        }
        return newMatrix;
    }

    public Matrix mult(double value) {
        Matrix newMatrix = new Matrix(shape);
        ComplexNumber complexValue = new ComplexNumber (value);
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                newMatrix.setValue(matrix[i][j].mult(complexValue), i, j);
            }
        }
        return newMatrix;
    }

    // *** UTILS ***

    public Matrix transposed() {
        Matrix newMatrix = new Matrix(shape);
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                newMatrix.setValue(matrix[j][i], i, j);
            }
        }
        return newMatrix;
    }

    private Matrix getMinor(Matrix targetMatrix, int column) {
        MatrixShape newShape = new MatrixShape(targetMatrix.getShape().x - 1);
        Matrix minor = new Matrix(newShape);
        int n = 0, m = 0;
        for (int i = 1; i < targetMatrix.getShape().x; i++)
            for (int j = 0; j < targetMatrix.getShape().x; j++) {
                if (j == column)
                    continue;
                minor.setValue(targetMatrix.getValue(i, j), n, m);
                if (m < newShape.x - 1)
                    m++;
                else{
                    n++;
                    m = 0;
                }
            }
        return minor;
    }

    public ComplexNumber getDeterminant(){
        if (shape.x != shape.y)
            throw new ArrayIndexOutOfBoundsException("Non square matrix");

        if (shape.x == 1)
            return matrix[0][0];

        ComplexNumber det = new ComplexNumber();
        for (int i = 0; i < shape.x; i++){
            ComplexNumber a_addition = getMinor(this, i).getDeterminant();
            ComplexNumber sign = new ComplexNumber(1 + (i % 2) * -2);
            det = det.add(matrix[0][i].mult(a_addition).mult(sign));
        }
        return det;
    }

    public void printMatrix() {
        for (int i = 0; i < shape.x; i++) {
            for (int j = 0; j < shape.y; j++) {
                System.out.print(this.matrix[i][j].algebraicForm() + "  ");
            }
            System.out.println();
        }
    }

}
