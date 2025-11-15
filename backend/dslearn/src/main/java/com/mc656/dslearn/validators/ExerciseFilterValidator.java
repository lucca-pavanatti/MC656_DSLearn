package com.mc656.dslearn.validators;

import com.mc656.dslearn.models.enums.Difficulty;

/**
 * Validador para filtros de exercícios
 * Implementa validações usando particionamento em classes de equivalência
 */
public class ExerciseFilterValidator {

    /**
     * Valida o parâmetro de dificuldade
     * Classes de equivalência:
     * - CE1: Valores válidos (Easy, Medium, Hard - case insensitive)
     * - CE2: null ou vazio (retorna null, sem filtro)
     * - CE3: Valores inválidos (lança exceção)
     */
    public static Difficulty validateDifficulty(String difficulty) {
        if (difficulty == null || difficulty.trim().isEmpty()) {
            return null;
        }

        try {
            // Normaliza para formato esperado (primeira letra maiúscula)
            String normalized = difficulty.trim();
            normalized = normalized.substring(0, 1).toUpperCase() + 
                        normalized.substring(1).toLowerCase();
            return Difficulty.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dificuldade inválida: " + difficulty + 
                ". Valores válidos: Easy, Medium, Hard");
        }
    }

    /**
     * Valida o parâmetro de estrutura de dados
     * Classes de equivalência:
     * - CE1: String válida não vazia
     * - CE2: null ou vazio (retorna null)
     * - CE3: String com apenas espaços (retorna null)
     */
    public static String validateDataStructure(String dataStructure) {
        if (dataStructure == null || dataStructure.trim().isEmpty()) {
            return null;
        }
        return dataStructure.trim();
    }

    /**
     * Valida o parâmetro de empresa
     * Classes de equivalência:
     * - CE1: String válida não vazia
     * - CE2: null ou vazio (retorna null)
     * - CE3: String com apenas espaços (retorna null)
     */
    public static String validateCompany(String company) {
        if (company == null || company.trim().isEmpty()) {
            return null;
        }
        return company.trim();
    }
}
