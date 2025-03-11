package com.example.tictactoe.service;

import com.example.tictactoe.dto.Symbol;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ClientServiceImpl implements ClientService {
    private final RestClient client = RestClient.create();
    private final String opponentUrl = System.getenv("URL");

    @Override
    public void sendStep(Symbol symbol, int position) {
        client.post().uri("http://" + opponentUrl + "/step?symbol=" + symbol + "&position=" + position)
                .exchange((request, response) -> {
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new BadRequestException("heart-beat failed");
                    }
                    return null;
                });
    }

    @Override
    public Double sendHeartBeat() {
        return client.get().uri("http://" + opponentUrl + "/heart-beat")
                .exchange((request, response) -> {
                    if (!response.getStatusCode().is2xxSuccessful()) {
                        throw new BadRequestException("heart-beat failed");
                    }
                    return response.bodyTo(Double.class);
                });
    }
}
