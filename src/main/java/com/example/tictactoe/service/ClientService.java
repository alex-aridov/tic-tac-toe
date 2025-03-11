package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;

public interface ClientService {
    void sendStep(Symbol symbol, int position);

    Double sendHeartBeat();
}
