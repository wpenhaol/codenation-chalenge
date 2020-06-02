package br.com.codenation.app.service;

import br.com.codenation.app.client.ChalengeClient;
import br.com.codenation.app.domain.model.Chalenge;
import br.com.codenation.app.domain.model.ChalengeResponse;
import feign.form.FormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChalengeService {

    @Autowired
    private ChalengeClient chalengeClient;

    /**
     * [[ BUSCANDO INFORMACOES PARA PROCESSAMENTO NO CLIENTE CONENATION ]]
     * @param token
     * @return
     */
    public Chalenge getChalengeToProcess(String token){
        return chalengeClient.getInformationToProcess(token);
    }

    /**
     * [[ ENVIANDO INFORMACOES JA PROCESSADAS PARA A API ]]
     * @param token
     * @param answer
     * @return
     */
    public ChalengeResponse sendInformationToProcessSolution(String token, FormData answer){
        return chalengeClient.sendInformationToProcessSolution(token, answer);
    }

}
