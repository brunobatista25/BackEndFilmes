package com.brunao.brunao.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpRequest.newBuilder;

@RestController
public class WiremockController {

    @ApiOperation(value = "Retorna os dados de um filme externo")
    @GetMapping(path = "/idfilmeExterno/{codigo}")
    public ResponseEntity consultarIdFilmeExterno(@PathVariable("codigo") @ApiParam(name = "codigo", value = "codigo do filme", example = "1") Integer codigo) throws URISyntaxException {
        HttpResponse<String> response = null;
        try {
            var url = "publ/idfilmeExterno/" + codigo;
            HttpRequest request = newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpClient httpClient = HttpClient.newHttpClient();
            response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  ResponseEntity.status(HttpStatus.OK).body(response.body());
    }

    @GetMapping("/validarHeader")
    public ResponseEntity<String> validarHeader(
            @RequestHeader(value = "optional-header", required = false) String optionalHeader) {
            HttpResponse<String> response = null;
            try {
                var url = "http://localhost:8091/validarHeader";
                HttpRequest request = newBuilder()
                        .uri(new URI(url))
                        .header("optional-header",optionalHeader)
                        .GET()
                        .build();

                HttpClient httpClient = HttpClient.newHttpClient();
                response =
                        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response.body());

            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(response);


            if (optionalHeader.equals("12345678"))
                return ResponseEntity.status(HttpStatus.OK).body(response.body());
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.body());
            }
        }

}
