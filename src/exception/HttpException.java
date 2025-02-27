package exception;

public class HttpException extends Throwable {
    public HttpException(String s) {
       super(s);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

}
