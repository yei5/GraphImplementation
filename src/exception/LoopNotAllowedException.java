package exception;

public class LoopNotAllowedException extends Exception{
    public LoopNotAllowedException(String message){
        super(message);
    }
}
