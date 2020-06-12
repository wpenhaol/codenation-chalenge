package br.com.codenation.app.business;

import br.com.codenation.app.domain.enumeration.AlphabetEnumeration;
import br.com.codenation.app.domain.model.Chalenge;
import br.com.codenation.app.domain.model.ChalengeResponse;
import br.com.codenation.app.service.ChalengeService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import feign.form.FormData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;

@Component
@Slf4j
public class ChalengeBusiness {

    @Value("${chalenge.token}")
    private String token;

    @Autowired
    private ChalengeService chalengeService;

    public ChalengeResponse getChalengeInfoToProcess() throws IOException {

        log.info("[[ REQUEST THIS INFORMATION FOR PROCESS USING THIS TOKEN ]]: {} ...", token);
        Chalenge chalenge = chalengeService.getChalengeToProcess(token);

        log.info("[[ DECODING TEXT ]] ...");
        decodeText(chalenge);

        log.info("[[ ENCRYPTING IN SHA1 ]] ...");
        encodeTextSHA1(chalenge);

        saveJsonWithInformationData(chalenge);
        log.info("[[ DATA OF TO PROCESS ]]: {} ...", chalenge);

        return sendInformationOfChalend(chalenge);

    }

    /**
     * [[ ESCREVER O ARQUIVO JSON PARA PODER SUBMETE-LO]]
     * @param chalenge
     * @throws IOException
     */
    private void saveJsonWithInformationData(Chalenge chalenge) throws IOException {
        Writer writer = new FileWriter("./src/main/resources/answer.json");
        Gson gson = new GsonBuilder().create();
        gson.toJson(chalenge, writer);
        writer.flush();
        writer.close();
    }

    /**
     * [[ DECODIFICANDO O TEXTO ]]
     * @param chalenge
     */
    private void decodeText(Chalenge chalenge){
        final String[] textEncrypted = {chalenge.getCifrado()};
        textEncrypted[0] = textEncrypted[0].toLowerCase();

        Arrays.asList(AlphabetEnumeration.values()).forEach(alphabetEnumeration -> {
            Integer index = getIndexEnumeration(alphabetEnumeration.getIndex(), chalenge.getNumero_casas());
            String code = AlphabetEnumeration.values()[index].name().toLowerCase();
            log.info("INDEX: {}, ENCODE: {}, DECODE: {}", index, code.toUpperCase(), alphabetEnumeration.name());
            textEncrypted[0] = textEncrypted[0].replaceAll(code, alphabetEnumeration.name());
        });

        chalenge.setDecifrado(textEncrypted[0].toLowerCase());
    }

    /**
     * [[ BUSCA O INDEX PARA {DECODE} DA LETRA ATRAVEZ DO INDEX ATUAL + NUMERO DE CASAS ]]
     * @param indexEnum
     * @param variablePosition
     * @return
     */
    private Integer getIndexEnumeration(Integer indexEnum, Integer variablePosition ){
        return (indexEnum+variablePosition >= AlphabetEnumeration.values().length)
                ?  ((indexEnum+variablePosition) - AlphabetEnumeration.values().length)
                    : (indexEnum+variablePosition);
    }

    /**
     * [[ CRIPTOGRAFANDO EM SHA1 ]]
     * @param chalenge
     */
    private void encodeTextSHA1(Chalenge chalenge){
        chalenge.setResumo_criptografico(DigestUtils.sha1Hex(chalenge.getDecifrado().getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * [[ BUSCANDO ARQUIVO ATUALIZADO E ENVIADO PARA VALIDACAO ]]
     * @return
     * @throws IOException
     */
    private ChalengeResponse sendInformationOfChalend(Chalenge chalenge) throws IOException {
        File file = new File("./src/main/resources/answer.json");
        byte[] fileContent = Files.readAllBytes(file.toPath());
        FormData formData = new FormData("text/json", "answer.json", fileContent);
        ChalengeResponse chalengeResponse = chalengeService.sendInformationToProcessSolution(token, formData);
        log.info("[[ RECEIVE THIS RESPONSE IN PROCESS SUBMIT FILE FOR VALIDADE ]] {} ", chalengeResponse);
        chalengeResponse.setChalenge(chalenge);
        return chalengeResponse;
    }

}
