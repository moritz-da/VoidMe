package de.hdmstuttgart.voidme.shared.exceptions;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(String errorMessage) {
        super(errorMessage);
    }
}
