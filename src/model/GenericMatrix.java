package model;


import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class GenericMatrix <T>{
    private ArrayList<ArrayList<T>> matrix;

    public GenericMatrix(int rows, int columns) {
        matrix=new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            matrix.add(new ArrayList<>());
            for (int j = 0; j < columns; j++) {
                matrix.get(i).add(null);
            }
        }
    }

    public GenericMatrix(){
        matrix = new ArrayList<>();
    }

    public T get(int row, int column){
        return matrix.get(row).get(column);
    }

    public void set(int row, int column, T value){
        matrix.get(row).set(column,value);
    }

    public int getRows(){
        return matrix.size();
    }

    public int getColumns(){
        return matrix.get(0).size();
    }

    public ArrayList<ArrayList<T>> getMatrix(){
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<T>> matrix){
        this.matrix = matrix;
    }

    public void setSize(int size1, int size2){

        ArrayList<ArrayList<T>> newMatrix = new ArrayList<>();
        for (int i = 0; i < size1; i++) {
            newMatrix.add(new ArrayList<>());
            for (int j = 0; j < size2; j++) {
                newMatrix.get(i).add(null);
            }
        }
        for (int i = 0; i < Math.min(size1, matrix.size()); i++) {
            for (int j = 0; j < Math.min(size2, matrix.get(0).size()); j++) {
                newMatrix.get(i).set(j,matrix.get(i).get(j));
            }
        }
        matrix = newMatrix;
    }
}
