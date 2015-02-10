package info.bluefoot.winter.service.sociallogin;


public class EmailAlreadyRegisteredException extends RuntimeException {

    private static final long serialVersionUID = 3141775504710046419L;

    public EmailAlreadyRegisteredException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
