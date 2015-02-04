package info.bluefoot.winter.service.impl;

public class PlaylistNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -765568107860469667L;

    public PlaylistNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
