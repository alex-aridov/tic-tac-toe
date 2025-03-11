package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;
import com.example.tictactoe.exceptions.NoWinnerException;
import com.example.tictactoe.exceptions.WinnerException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
@RequiredArgsConstructor
public class SynchronisationServiceImpl implements SynchronisationService {

    @Value("${app.step-interval}")
    private int stepInterval;

    @Getter
    private final double id = new Random().nextDouble();
    private Double opponentId;

    private Symbol mySymbol;
    private int currentStepTime = 0;
    private Symbol currentSterSymbol = Symbol.X;
    private boolean gameFinished = false;

    private final AtomicBoolean synchronised = new AtomicBoolean(false);

    private final StateService stateService;
    private final ClientService clientService;

    @Override
    public void changeSymbolToOpositOf(Symbol symbol) {
        currentSterSymbol = symbol == Symbol.O ? Symbol.X : Symbol.O;
    }

    private void makeStep() {
        int position = stateService.getNextPosition();
        clientService.sendStep(mySymbol, position);
        try {
            stateService.step(mySymbol, position);
        } catch (WinnerException | NoWinnerException e) {
            gameFinished = true;
        }

        changeSymbolToOpositOf(mySymbol);
        currentStepTime = 0;
    }

    @Scheduled(fixedRate = 100)
    void heartBeat() {
        if (gameFinished) {
            System.exit(0);
        }
        Double opponentId;
        try {
            opponentId = clientService.sendHeartBeat();
        } catch (Exception e) {
            log.error("Synchronization failed...");
            synchronised.set(false);
            return;
        }

        // initialization when first time synchronisation
        if (this.opponentId == null && opponentId != null) {
            this.opponentId = opponentId;
            // choose symbol
            this.mySymbol = this.id >= opponentId ? Symbol.X : Symbol.O;
        }

        synchronised.set(true);

        // increment counter if we are waiting to do step
        if (mySymbol == currentSterSymbol) {
            currentStepTime++;
        }
        // make step if it's time
        if (currentStepTime >= stepInterval) {
            makeStep();
        }
    }

    @Override
    public boolean isSynchronized() {
        return synchronised.get();
    }

    @Override
    public void setFinished(boolean finished) {
        gameFinished = finished;
    }
}
