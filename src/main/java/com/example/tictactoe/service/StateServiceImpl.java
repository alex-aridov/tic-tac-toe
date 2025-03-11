package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;
import com.example.tictactoe.exceptions.BadTurnException;
import com.example.tictactoe.exceptions.NoWinnerException;
import com.example.tictactoe.exceptions.WinnerException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Getter
@Service
public class StateServiceImpl implements StateService {
    private final List<Symbol> playingField = new ArrayList<>(Collections.nCopies(9, null));
    // expected next step character
    private Symbol nextStep = Symbol.X;

    // generate next position for step
    @Override
    public int getNextPosition() {
        for (int i = 0; i < 9; i++) {
            if (playingField.get(i) == null) {
                return i;
            }
        };

        throw new RuntimeException("the game should be closed");
    }

    // do new step
    @Override
    public synchronized void step(Symbol symbol, int position) {
        log.info("step symbol: {} position: {}", symbol, position);
        if (symbol != nextStep) {
            throw new BadTurnException(String.format("'%s' should make step", nextStep));
        }
        if (playingField.get(position) != null) {
            throw new BadTurnException("position already occupied");
        }
        // fix step
        playingField.set(position, symbol);

        checkResult(playingField);
        // change expected character
        nextStep = symbol == Symbol.X ? Symbol.O : Symbol.X;
    }

    void checkResult(List<Symbol> field) {
        for (int i = 0; i < 3; i++) {
            checkRawResult(field.get(i), field.get(3 + i), field.get(6 + i));
            checkRawResult(field.get(i * 3), field.get(i * 3 + 1), field.get(i * 3 + 2));
        }
        checkRawResult(field.get(0), field.get(4), field.get(8));
        checkRawResult(field.get(2), field.get(4), field.get(6));
        if (field.stream().allMatch(Objects::nonNull)) {
            log.info("No winner");
            throw new NoWinnerException("No winner");
        }
    }

    void checkRawResult(Symbol s1, Symbol s2, Symbol s3) {
        if (s1 == null || s2 == null || s3 == null ||
                s1 != s2 || s2 != s3 || s1 != s3) {
            return;
        }
        log.info("{} won", s1);
        throw new WinnerException(s1 + " won");
    }
}
