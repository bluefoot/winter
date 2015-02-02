package info.bluefoot.watchlater.dao.jdbc;


public class UserNotFound extends RuntimeException {

    private static final long serialVersionUID = -3682664212541245770L;

    public UserNotFound(String msg, Throwable cause) {
        super(msg, cause);
    }

}
