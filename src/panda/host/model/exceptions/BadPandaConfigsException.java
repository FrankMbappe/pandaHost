package panda.host.model.exceptions;

public class BadPandaConfigsException extends Exception {
    @Override
    public String getMessage() {
        return "The Panda configuration file is either corrupted or has badly formatted values.";
    }
}
