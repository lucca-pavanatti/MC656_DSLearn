package com.mc656.dslearn.validators;

/**
 * Validador para parâmetros de paginação
 * Esta classe aplica validações de acordo com classes de equivalência e valores limite
 */
public class PaginationValidator {

    private static final int MIN_PAGE = 0;
    private static final int MAX_PAGE = 10000;
    private static final int MIN_SIZE = 1;
    private static final int MAX_SIZE = 100;
    private static final int DEFAULT_SIZE = 20;

    /**
     * Valida o número da página
     * Classes de equivalência:
     * - Válido: [0, 10000]
     * - Inválido: < 0, > 10000
     * 
     * Valores limite: 0, 1, 9999, 10000
     */
    public static int validatePage(int page) {
        if (page < MIN_PAGE) {
            return MIN_PAGE;
        }
        if (page > MAX_PAGE) {
            return MAX_PAGE;
        }
        return page;
    }

    /**
     * Valida o tamanho da página
     * Classes de equivalência:
     * - Válido: [1, 100]
     * - Inválido: <= 0, > 100
     * 
     * Valores limite: 1, 2, 99, 100
     */
    public static int validateSize(int size) {
        if (size <= 0) {
            return DEFAULT_SIZE;
        }
        if (size > MAX_SIZE) {
            return MAX_SIZE;
        }
        return size;
    }

    /**
     * Valida a direção da ordenação
     * Classes de equivalência:
     * - Válido: "asc", "desc" (case-insensitive)
     * - Inválido: null, vazio, outros valores
     */
    public static String validateSortDirection(String direction) {
        if (direction == null || direction.trim().isEmpty()) {
            return "asc";
        }
        
        String normalized = direction.toLowerCase().trim();
        if ("asc".equals(normalized) || "desc".equals(normalized)) {
            return normalized;
        }
        
        return "asc";
    }

    /**
     * Valida o campo de ordenação
     * Classes de equivalência:
     * - Válido: campos existentes na entidade
     * - Inválido: null, vazio, campos inexistentes
     */
    public static String validateSortBy(String sortBy, String[] allowedFields) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return allowedFields.length > 0 ? allowedFields[0] : "id";
        }
        
        for (String field : allowedFields) {
            if (field.equalsIgnoreCase(sortBy.trim())) {
                return field;
            }
        }
        
        return allowedFields.length > 0 ? allowedFields[0] : "id";
    }
}
