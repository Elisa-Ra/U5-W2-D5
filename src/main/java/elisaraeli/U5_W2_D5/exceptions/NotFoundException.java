package elisaraeli.U5_W2_D5.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(int id) {
        super("L'elemento con id: " + id + " non è stato trovato!");
    }
}