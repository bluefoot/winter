package info.bluefoot.winter.dao.exception;

//FIXME, LET SPRING THROW, MOVE CUSTOM EXCEPTIONS TO THE SERVICE LAYER
public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3682664212541245770L;

    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
