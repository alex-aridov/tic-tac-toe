package com.example.tictactoe.controller;

import com.example.tictactoe.dto.Symbol;
import com.example.tictactoe.exceptions.NoWinnerException;
import com.example.tictactoe.exceptions.SynchronizedException;
import com.example.tictactoe.exceptions.WinnerException;
import com.example.tictactoe.service.StateService;
import com.example.tictactoe.service.SynchronisationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController("/api")
public class Controller {
    private final SynchronisationService synchronisationService;
    private final StateService stateService;

    // Get game field
    @GetMapping("/field")
    public List<Symbol> field() {
        if (!synchronisationService.isSynchronized()) {
            throw new SynchronizedException("Synchronization failed");
        }
        return stateService.getPlayingField();
    }

    // Make step
    @PostMapping("/step")
    public void step(@RequestParam Symbol symbol, @RequestParam int position) {
        try {
            stateService.step(symbol, position);
        } catch (WinnerException | NoWinnerException e) {
            synchronisationService.setFinished(true);
            throw e;
        }
        synchronisationService.changeSymbolToOpositOf(symbol);
    }

    @GetMapping("/heart-beat")
    public double heartbeat() {
        return synchronisationService.getId();
    }
}
