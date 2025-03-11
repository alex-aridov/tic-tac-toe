package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;

import java.util.List;

public interface StateService {
    List<Symbol> getPlayingField();

    int getNextPosition();

    void step(Symbol symbol, int position);
}
