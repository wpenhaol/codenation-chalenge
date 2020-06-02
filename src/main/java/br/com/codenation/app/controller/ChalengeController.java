package br.com.codenation.app.controller;

import br.com.codenation.app.business.ChalengeBusiness;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("${path.req.chalenge.process}")
@ResponseStatus(HttpStatus.OK)
public class ChalengeController {

    @Autowired
    private ChalengeBusiness chalengeBusiness;

    @GetMapping()
    public ResponseEntity<Object> getAdministratorByIdentification() throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(chalengeBusiness.getChalengeInfoToProcess());
    }

}
