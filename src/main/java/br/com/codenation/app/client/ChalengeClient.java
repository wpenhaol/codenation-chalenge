package br.com.codenation.app.client;

import br.com.codenation.app.domain.model.Chalenge;
import br.com.codenation.app.domain.model.ChalengeResponse;
import br.com.codenation.app.infrastructure.config.ClientConfig;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import feign.form.FormData;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "chalengeclient", url = "${chalenge.request}", configuration = ClientConfig.class)
public interface ChalengeClient {

    @RequestLine("GET /generate-data?token={token}")
    Chalenge getInformationToProcess(@Param(value = "token") String token);

    @RequestLine("POST /submit-solution?token={token}")
    @Headers("Content-Type: multipart/form-data")
    ChalengeResponse sendInformationToProcessSolution(@Param(value = "token") String token, @Param("answer") FormData answer);

}