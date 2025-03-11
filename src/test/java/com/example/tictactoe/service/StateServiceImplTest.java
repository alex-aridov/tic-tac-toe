package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;
import com.example.tictactoe.exceptions.BadTurnException;
import com.example.tictactoe.exceptions.NoWinnerException;
import com.example.tictactoe.exceptions.WinnerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class StateServiceImplTest {
    @InjectMocks
    private StateServiceImpl stateService;

    @MethodSource
    @ParameterizedTest
    void checkRawResultFinishTest(Symbol s1, Symbol s2, Symbol s3) {
        var result = assertThrows(WinnerException.class, () -> stateService.checkRawResult(s1, s2, s3));
        assertEquals(s1 + " won", result.getMessage());
    }

    static Stream<Arguments> checkRawResultFinishTest() {
        return Stream.of(
                Arguments.of(Symbol.O, Symbol.O, Symbol.O),
                Arguments.of(Symbol.X, Symbol.X, Symbol.X)
        );
    }

    @MethodSource
    @ParameterizedTest
    void checkRawResultNotFinishTest(Symbol s1, Symbol s2, Symbol s3) {
        assertDoesNotThrow(() -> stateService.checkRawResult(s1, s2, s3));
    }

    static Stream<Arguments> checkRawResultNotFinishTest() {
        return Stream.of(
                Arguments.of(null, null, null),
                Arguments.of(Symbol.X, Symbol.O, Symbol.X),
                Arguments.of(Symbol.X, null, Symbol.X),
                Arguments.of(null, Symbol.X, Symbol.X),
                Arguments.of(Symbol.X, Symbol.X, null)
        );
    }

    @Test
    void checkResultNoWinnerTest() {
        List<Symbol> field = List.of(Symbol.X, Symbol.X, Symbol.O,
                Symbol.O, Symbol.O, Symbol.X,
                Symbol.X, Symbol.X, Symbol.O);
        assertThrows(NoWinnerException.class, () -> stateService.checkResult(field));
    }

    @MethodSource
    @ParameterizedTest
    void checkResultWinnerTest(List<Symbol> field) {
        assertThrows(WinnerException.class, () -> stateService.checkResult(field));
    }

    static Stream<Arguments> checkResultWinnerTest() {
        return Stream.of(
                Arguments.of(generate(Symbol.X, Set.of(0, 1, 2))),
                Arguments.of(generate(Symbol.X, Set.of(3, 4, 5))),
                Arguments.of(generate(Symbol.X, Set.of(6, 7, 8))),
                Arguments.of(generate(Symbol.X, Set.of(0, 3, 6))),
                Arguments.of(generate(Symbol.X, Set.of(1, 4, 7))),
                Arguments.of(generate(Symbol.X, Set.of(2, 5, 8))),
                Arguments.of(generate(Symbol.X, Set.of(0, 4, 8))),
                Arguments.of(generate(Symbol.X, Set.of(2, 4, 6)))
        );
    }

    private static List<Symbol> generate(Symbol symbol, Set<Integer> positions) {
        var result = new ArrayList<Symbol>(9);
        for (int i = 0; i < 9; i++) {
            if (positions.contains(i)) {
                result.add(symbol);
            } else {
                result.add(null);
            }
        }
        return result;
    }

    @Test
    void stepWrongSymbolTest() {
        var exception = assertThrows(BadTurnException.class, () -> stateService.step(Symbol.O, 0));
        assertEquals("'X' should make step", exception.getMessage());
    }

    @Test
    void stepWrongPositionTest() {
        stateService.step(Symbol.X, 0);
        var exception = assertThrows(BadTurnException.class, () -> stateService.step(Symbol.O, 0));
        assertEquals("position already occupied", exception.getMessage());
    }

    @Test
    void stepsWonTest() {
        stateService.step(Symbol.X, 0);
        stateService.step(Symbol.O, 3);
        stateService.step(Symbol.X, 1);
        stateService.step(Symbol.O, 4);
        var exception = assertThrows(WinnerException.class, () -> stateService.step(Symbol.X, 2));
        assertEquals("X won", exception.getMessage());
    }
}