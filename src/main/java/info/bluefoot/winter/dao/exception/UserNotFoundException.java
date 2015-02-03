package info.bluefoot.winter.dao.exception;

public class UserNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -3682664212541245770L;

    public UserNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
