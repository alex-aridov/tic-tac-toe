package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;

public interface SynchronisationService {
    void changeSymbolToOpositOf(Symbol symbol);

    double getId();

    boolean isSynchronized();

    void setFinished(boolean finished);
}
