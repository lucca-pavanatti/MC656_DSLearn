package com.mc656.dslearn.validators;

import com.mc656.dslearn.models.enums.Difficulty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 
 * Para validateDifficulty():
 * Classes de Equivalência:
 *   CE1: "Easy", "easy", "EASY", "EaSy" -> Difficulty.Easy
 *   CE2: "Medium", "medium", "MEDIUM" -> Difficulty.Medium
 *   CE3: "Hard", "hard", "HARD" -> Difficulty.Hard
 *   CE4: null ou "" -> null
 *   CE5: valores inválidos -> IllegalArgumentException
 * 
 * Para validateDataStructure() e validateCompany():
 * Classes de Equivalência:
 *   CE1: String não vazia válida -> retorna normalizada
 *   CE2: null -> null
 *   CE3: "" (vazio) -> null
 *   CE4: "   " (apenas espaços) -> null
 */
@DisplayName("Testes de ExerciseFilterValidator - Classes de Equivalência")
class ExerciseFilterValidatorTest {
    @Test
    @DisplayName("CE1: Difficulty = 'Easy' deve retornar Difficulty.Easy")
    void testValidateDifficulty_Easy_ReturnsEasy() {
        assertEquals(Difficulty.Easy, ExerciseFilterValidator.validateDifficulty("Easy"));
    }

    @Test
    @DisplayName("CE1: Difficulty = 'easy' (minúsculo) deve retornar Difficulty.Easy")
    void testValidateDifficulty_LowercaseEasy_ReturnsEasy() {
        assertEquals(Difficulty.Easy, ExerciseFilterValidator.validateDifficulty("easy"));
    }

    @Test
    @DisplayName("CE1: Difficulty = 'EASY' (maiúsculo) deve retornar Difficulty.Easy")
    void testValidateDifficulty_UppercaseEasy_ReturnsEasy() {
        assertEquals(Difficulty.Easy, ExerciseFilterValidator.validateDifficulty("EASY"));
    }

    @Test
    @DisplayName("CE1: Difficulty = 'EaSy' (misto) deve retornar Difficulty.Easy")
    void testValidateDifficulty_MixedCaseEasy_ReturnsEasy() {
        assertEquals(Difficulty.Easy, ExerciseFilterValidator.validateDifficulty("EaSy"));
    }

    @Test
    @DisplayName("CE2: Difficulty = 'Medium' deve retornar Difficulty.Medium")
    void testValidateDifficulty_Medium_ReturnsMedium() {
        assertEquals(Difficulty.Medium, ExerciseFilterValidator.validateDifficulty("Medium"));
    }

    @Test
    @DisplayName("CE2: Difficulty = 'medium' (minúsculo) deve retornar Difficulty.Medium")
    void testValidateDifficulty_LowercaseMedium_ReturnsMedium() {
        assertEquals(Difficulty.Medium, ExerciseFilterValidator.validateDifficulty("medium"));
    }

    @Test
    @DisplayName("CE2: Difficulty = 'MEDIUM' (maiúsculo) deve retornar Difficulty.Medium")
    void testValidateDifficulty_UppercaseMedium_ReturnsMedium() {
        assertEquals(Difficulty.Medium, ExerciseFilterValidator.validateDifficulty("MEDIUM"));
    }

    @Test
    @DisplayName("CE3: Difficulty = 'Hard' deve retornar Difficulty.Hard")
    void testValidateDifficulty_Hard_ReturnsHard() {
        assertEquals(Difficulty.Hard, ExerciseFilterValidator.validateDifficulty("Hard"));
    }

    @Test
    @DisplayName("CE3: Difficulty = 'hard' (minúsculo) deve retornar Difficulty.Hard")
    void testValidateDifficulty_LowercaseHard_ReturnsHard() {
        assertEquals(Difficulty.Hard, ExerciseFilterValidator.validateDifficulty("hard"));
    }

    @Test
    @DisplayName("CE3: Difficulty = 'HARD' (maiúsculo) deve retornar Difficulty.Hard")
    void testValidateDifficulty_UppercaseHard_ReturnsHard() {
        assertEquals(Difficulty.Hard, ExerciseFilterValidator.validateDifficulty("HARD"));
    }

    @Test
    @DisplayName("CE4: Difficulty = null deve retornar null")
    void testValidateDifficulty_Null_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateDifficulty(null));
    }

    @Test
    @DisplayName("CE4: Difficulty = '' (vazio) deve retornar null")
    void testValidateDifficulty_Empty_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateDifficulty(""));
    }

    @Test
    @DisplayName("CE4: Difficulty = '   ' (espaços) deve retornar null")
    void testValidateDifficulty_Whitespace_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateDifficulty("   "));
    }

    @Test
    @DisplayName("CE5: Difficulty = 'Invalid' deve lançar IllegalArgumentException")
    void testValidateDifficulty_Invalid_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ExerciseFilterValidator.validateDifficulty("Invalid")
        );
        assertTrue(exception.getMessage().contains("Dificuldade inválida"));
        assertTrue(exception.getMessage().contains("Invalid"));
    }

    @Test
    @DisplayName("CE5: Difficulty = 'VeryHard' deve lançar IllegalArgumentException")
    void testValidateDifficulty_VeryHard_ThrowsException() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ExerciseFilterValidator.validateDifficulty("VeryHard")
        );
        assertTrue(exception.getMessage().contains("Dificuldade inválida"));
    }

    @Test
    @DisplayName("CE5: Difficulty = '123' deve lançar IllegalArgumentException")
    void testValidateDifficulty_Numeric_ThrowsException() {
        assertThrows(
            IllegalArgumentException.class,
            () -> ExerciseFilterValidator.validateDifficulty("123")
        );
    }

    @Test
    @DisplayName("CE1: Difficulty = '  Easy  ' (com espaços) deve retornar Difficulty.Easy")
    void testValidateDifficulty_EasyWithSpaces_ReturnsEasy() {
        assertEquals(Difficulty.Easy, ExerciseFilterValidator.validateDifficulty("  Easy  "));
    }

    @Test
    @DisplayName("CE1: DataStructure = 'Array' deve retornar 'Array'")
    void testValidateDataStructure_ValidString_ReturnsString() {
        assertEquals("Array", ExerciseFilterValidator.validateDataStructure("Array"));
    }

    @Test
    @DisplayName("CE1: DataStructure = 'Stack' deve retornar 'Stack'")
    void testValidateDataStructure_Stack_ReturnsStack() {
        assertEquals("Stack", ExerciseFilterValidator.validateDataStructure("Stack"));
    }

    @Test
    @DisplayName("CE1: DataStructure = '  Tree  ' (com espaços) deve retornar 'Tree'")
    void testValidateDataStructure_WithSpaces_ReturnsTrimmed() {
        assertEquals("Tree", ExerciseFilterValidator.validateDataStructure("  Tree  "));
    }

    @Test
    @DisplayName("CE2: DataStructure = null deve retornar null")
    void testValidateDataStructure_Null_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateDataStructure(null));
    }

    @Test
    @DisplayName("CE3: DataStructure = '' (vazio) deve retornar null")
    void testValidateDataStructure_Empty_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateDataStructure(""));
    }

    @Test
    @DisplayName("CE4: DataStructure = '   ' (apenas espaços) deve retornar null")
    void testValidateDataStructure_Whitespace_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateDataStructure("   "));
    }

    @Test
    @DisplayName("CE1: DataStructure = 'Binary Tree' (com espaço) deve retornar 'Binary Tree'")
    void testValidateDataStructure_MultiWord_ReturnsString() {
        assertEquals("Binary Tree", ExerciseFilterValidator.validateDataStructure("Binary Tree"));
    }

    @Test
    @DisplayName("CE1: Company = 'Google' deve retornar 'Google'")
    void testValidateCompany_ValidString_ReturnsString() {
        assertEquals("Google", ExerciseFilterValidator.validateCompany("Google"));
    }

    @Test
    @DisplayName("CE1: Company = 'Amazon' deve retornar 'Amazon'")
    void testValidateCompany_Amazon_ReturnsAmazon() {
        assertEquals("Amazon", ExerciseFilterValidator.validateCompany("Amazon"));
    }

    @Test
    @DisplayName("CE1: Company = '  Facebook  ' (com espaços) deve retornar 'Facebook'")
    void testValidateCompany_WithSpaces_ReturnsTrimmed() {
        assertEquals("Facebook", ExerciseFilterValidator.validateCompany("  Facebook  "));
    }

    @Test
    @DisplayName("CE2: Company = null deve retornar null")
    void testValidateCompany_Null_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateCompany(null));
    }

    @Test
    @DisplayName("CE3: Company = '' (vazio) deve retornar null")
    void testValidateCompany_Empty_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateCompany(""));
    }

    @Test
    @DisplayName("CE4: Company = '   ' (apenas espaços) deve retornar null")
    void testValidateCompany_Whitespace_ReturnsNull() {
        assertNull(ExerciseFilterValidator.validateCompany("   "));
    }

    @Test
    @DisplayName("CE1: Company = 'JP Morgan' (com espaço) deve retornar 'JP Morgan'")
    void testValidateCompany_MultiWord_ReturnsString() {
        assertEquals("JP Morgan", ExerciseFilterValidator.validateCompany("JP Morgan"));
    }

    // ========== TESTES PARAMETRIZADOS ==========

    @ParameterizedTest
    @ValueSource(strings = {"Easy", "easy", "EASY", "EaSy", "  Easy  ", " easy "})
    @DisplayName("Teste parametrizado: validateDifficulty com variações de 'Easy'")
    void testValidateDifficulty_EasyVariations(String input) {
        assertEquals(Difficulty.Easy, ExerciseFilterValidator.validateDifficulty(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Medium", "medium", "MEDIUM", "MeDiUm", "  Medium  "})
    @DisplayName("Teste parametrizado: validateDifficulty com variações de 'Medium'")
    void testValidateDifficulty_MediumVariations(String input) {
        assertEquals(Difficulty.Medium, ExerciseFilterValidator.validateDifficulty(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hard", "hard", "HARD", "HaRd", "  Hard  "})
    @DisplayName("Teste parametrizado: validateDifficulty com variações de 'Hard'")
    void testValidateDifficulty_HardVariations(String input) {
        assertEquals(Difficulty.Hard, ExerciseFilterValidator.validateDifficulty(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Invalid", "VeryEasy", "Moderate", "Extreme", "123", "abc123"})
    @DisplayName("Teste parametrizado: validateDifficulty com valores inválidos")
    void testValidateDifficulty_InvalidVariations(String input) {
        assertThrows(IllegalArgumentException.class, 
                    () -> ExerciseFilterValidator.validateDifficulty(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "   ", "\t", "\n"})
    @DisplayName("Teste parametrizado: validateDifficulty com valores nulos/vazios")
    void testValidateDifficulty_NullAndEmpty(String input) {
        assertNull(ExerciseFilterValidator.validateDifficulty(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Array", "Stack", "Queue", "Tree", "Graph", "Linked List"})
    @DisplayName("Teste parametrizado: validateDataStructure com valores válidos")
    void testValidateDataStructure_ValidValues(String input) {
        assertEquals(input, ExerciseFilterValidator.validateDataStructure(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "   ", "\t", "\n"})
    @DisplayName("Teste parametrizado: validateDataStructure com valores nulos/vazios")
    void testValidateDataStructure_NullAndEmpty(String input) {
        assertNull(ExerciseFilterValidator.validateDataStructure(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Google", "Amazon", "Facebook", "Microsoft", "Apple", "Netflix"})
    @DisplayName("Teste parametrizado: validateCompany com valores válidos")
    void testValidateCompany_ValidValues(String input) {
        assertEquals(input, ExerciseFilterValidator.validateCompany(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "   ", "\t", "\n"})
    @DisplayName("Teste parametrizado: validateCompany com valores nulos/vazios")
    void testValidateCompany_NullAndEmpty(String input) {
        assertNull(ExerciseFilterValidator.validateCompany(input));
    }
}
