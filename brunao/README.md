1 - Passo acessar o projeto do spring para podermos gerar um projeto ja configurado
    - Acessando o site https://start.spring.io/
    - Selecionando a opções `maven project`, `java 11`, `versao 2.7.3`
    - Selecionar as dependencias `spring web`, 'spring data jpa', `h2`, e `lombok`.
    - Selecionar o nome do group `com.example`
    - Selecionar o nome do artifact `nome`
    - Selecionar o nome do pacote `nome`
    - Baixar e descompactar onde quiserem

2 - Passo instalar na maquina 
    - Acessar a pasta do projeto e dar um `mvn clean install`
    - adiconar duas libs para funcionar o swagger

        <!--dependencias para o swagger -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-boot-starter</artifactId>
            <version>3.0.0</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/io.springfox/springfox-swagger-ui -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>3.0.0</version>
        </dependency>

OBS: Para subir o projeto na primeira vez é preciso comentar a biblioteca do jpa para funcionar, porque ela precisa de um banco para funcionar

E preciso editar o properties do projeto com esses valores colocando essas configuracoes o projeto ja roda
pra testar pode acessar o swagger http://localhost:8080/swagger-ui/index.html#/

#DataSource
spring.application.name=demo2
spring.datasource.url=jdbc:h2:mem:testedb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.mvc.pathmatch.matching-strategy = ANT_PATH_MATCHER

#h2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

#JPA
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.defer-datasource-initialization=true

E para validarmos se o banco esta no ar acessamos a url
- http://localhost:8080/h2-console

O nome do console fica gravado na properties
- spring.h2.console.path=/h2-console

E o nome do banco tem que ser o mesmo que esta no properties
- spring.datasource.url=jdbc:h2:mem:testedb

Agora iremos criar o primeiro controller

Vamos criar a classe StatusController dentro de um pacote controller

Para a classe ser identificada como controller e preciso de ter uma annotation na classe como @RestController

A gente consegue documentar o nome da request o que ela faz
com a tag @ApiOperation(value = "Retorna que a api esta de pé")

```
package com.example.demo2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    @ApiOperation(value = "Retorna que a api esta de pé")
    @GetMapping(path = "/status")
    public String statusApi(){
        return "A Aplicação está de pé";
    }

}
```

Agora vamos criar uma classe entity onde o jpa vai pegar essa classe e criar uma tabela baseada nela

Quando eu digo que a classe pertence a um banco de dados ela e uma entidade do banco de dados ou seja uma tabela e preciso colocar a tag @entity
vamos criar o pacote model e a classe Clube

```
package com.brunao.brunao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Filme {
@Id
@Column(name = "codigo", nullable = false)
private Integer codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String sinopse;

    @Column(nullable = false, length = 100)
    private String faixaEtaria;

    @Column(nullable = false, length = 100)
    private String genero;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getFaixaEtaria() {
        return faixaEtaria;
    }

    public void setFaixaEtaria(String faixaEtaria) {
        this.faixaEtaria = faixaEtaria;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }
}
```

Toda classe entity ela vai pedir que tenha um id ou um campo unico ai basta adicionar a tag @id
E para coluna para @Column

agora vamos criar um script pra que sempre que subir o projeto ele ai ja rodar o script
basta criar um arquivo com o nome data.sql com o valor de 
- INSERT INTO USUARIO VALUES(1, 'brunao', 'bruno', '123456');

Consultar
SELECT * FROM FILME ORDER BY CODIGO;

Agora precisamos criar um repository para fazer essas consultar ao banco
criar a pasta repository e o criar uma interface para FilmeRepository

```
package com.brunao.brunao.repository;

import com.brunao.brunao.model.Filme;
import org.springframework.data.repository.CrudRepository;

public interface FilmeRepository extends CrudRepository<Filme, Integer> {
}

```

Agora vamos criar um controller com o nome FilmeController para usar esse banco de dados

Agora vamos criar uma requisicao pra buscar um filme por id

```
package com.brunao.brunao.controller;

import com.brunao.brunao.repository.FilmeRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmeController {

    @Autowired
    private FilmeRepository filmeRepository;

    @ApiOperation(value = "Retorna os dados de um filme")
    @GetMapping(path = "/filme/{codigo}")
    public ResponseEntity consultarFilmePorId(@PathVariable("codigo") @ApiParam(name = "codigo", value = "codigo do filme", example = "1") Integer codigo) {
        return filmeRepository.findById(codigo)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }
}

```
Agora vamos listar todos os filmes adicionar no banco pra consultarTin

```
    @ApiOperation(value = "Retorna todos os filmes")
    @GetMapping(path = "/filmes")
    public ResponseEntity<Iterable<Filme>> consultarTodosOsFilmes() {
        return ResponseEntity.status(HttpStatus.OK).body(filmeRepository.findAll());
    }
```

Agora vamos criar o arquivo ResponseHandler dentro do pacote response para retornar mensagem e status no corpo

```
package com.example.demo2.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
Map<String, Object> map = new HashMap<String, Object>();
map.put("message", message);
map.put("status", status.value());

        return new ResponseEntity<Object>(map,status);
    }
}
```
Agora vamos criar o metodo pra salvar o filme

```
@ApiOperation(value = "Cadastrar filmes")
    @PostMapping(path = "/salvar")
    public ResponseEntity criarFilme(@RequestBody Filme filme) {

        if (filmeRepository.existsById(filme.getCodigo())) {
            return ResponseHandler.generateResponse("Filme já cadastrado!", HttpStatus.CONFLICT);
        }
        var objetoFilme = new Filme();

        if (filme.getFaixaEtaria().equals(null) || filme.getFaixaEtaria().isEmpty() || filme.getFaixaEtaria().isBlank()) {
            return ResponseHandler.generateResponse("Faixa etária é obrigatória!", HttpStatus.BAD_REQUEST);

        }
        else if (filme.getGenero().equals(null) || filme.getGenero().isEmpty() || filme.getGenero().isBlank()) {
            return ResponseHandler.generateResponse("Genêro é obrigatório!", HttpStatus.BAD_REQUEST);

        } else if (filme.getNome().equals(null) || filme.getNome().isEmpty() || filme.getNome().isBlank()) {
            return ResponseHandler.generateResponse("Nome é obrigatório!", HttpStatus.BAD_REQUEST);

        } else if (filme.getSinopse().equals(null) || filme.getSinopse().isEmpty() || filme.getSinopse().isBlank()) {
            return ResponseHandler.generateResponse("Sinopse é obrigatório!", HttpStatus.BAD_REQUEST);

        } else  {
            objetoFilme.setCodigo(filme.getCodigo());
            objetoFilme.setFaixaEtaria(filme.getFaixaEtaria());
            objetoFilme.setSinopse(filme.getSinopse());
            objetoFilme.setGenero(filme.getGenero());
            objetoFilme.setNome(filme.getNome());

            return ResponseEntity.status(HttpStatus.CREATED).body(filmeRepository.save(objetoFilme));
        }
    }
```
@requestBody quer dizer que tenho que mandar o corpo da requisição


Agora vamos deletar o usuario

```

    @ApiOperation(value = "Deletar filmes")
    @DeleteMapping(path = "/filme/{codigo}")
    public void deletarFilmePorId(@PathVariable("codigo") Integer codigo) {
        filmeRepository.findById(codigo)
                .map(record -> {
                    filmeRepository.deleteById(codigo);
                    return Void.TYPE;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "{\n" +
                        "    \"message\": \"Filme não encontrado\",\n" +
                        "}"));
    }
```

Agora vamos atualizar o filme com o put

```
@PutMapping(path = "/filme/{codigo}")
@JsonIgnoreProperties("codigo")
public ResponseEntity editarFilme(@PathVariable("codigo") Integer codigo, @RequestBody @Validated Filme filme) {
Optional<Filme> filmeObject = filmeRepository.findById(filme.getCodigo());
if (!filmeObject.isPresent()) {
return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\n" +
"    \"message\": \"Filme não encontrado\",\n" +
"}");
}

        if (filme.getFaixaEtaria() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Faixa etária é obrigatória\",\n" +
                    "}");
        }
        else if (filme.getGenero() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Genêro é obrigatório\",\n" +
                    "}");
        } else if (filme.getNome() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Nome é obrigatório\",\n" +
                    "}");
        } else if (filme.getSinopse() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\n" +
                    "    \"message\": \"Sinopse é obrigatório\",\n" +
                    "}");
        } else  {
            var filmeEditado = filmeObject.get();
            filmeEditado.getCodigo();
            filmeEditado.setNome(filme.getNome());
            filmeEditado.setSinopse(filme.getSinopse());
            filmeEditado.setGenero(filme.getGenero());
            filmeEditado.setFaixaEtaria(filme.getFaixaEtaria());
            return ResponseEntity.status(HttpStatus.OK).body(filmeRepository.save(filmeEditado));
        }

    }
```

e vamos atualizar com o patch

```
 @PatchMapping(path = "/api/usuario/{codigo}")
    @JsonIgnoreProperties({"codigo", "nome", "login", "senha"})
    public ResponseEntity editarUsuarios(@PathVariable("codigo") Integer codigo, @RequestBody Filme partialFilmeUpdate) {
        Optional<Filme> filme = filmeRepository.findById(codigo);
        var filmeEditado = filme.get();
        filmeEditado.getCodigo();
        if (partialFilmeUpdate.getNome() == null) {
            filmeEditado.getNome();
        } else {
            filmeEditado.setNome(partialFilmeUpdate.getNome());
        }

        if (partialFilmeUpdate.getGenero() == null) {
            filmeEditado.getGenero();
        } else {
            filmeEditado.setGenero(partialFilmeUpdate.getGenero());
        }

        if (partialFilmeUpdate.getSinopse() == null) {
            filmeEditado.getSinopse();
        } else {
            filmeEditado.setSinopse(partialFilmeUpdate.getSinopse());
        }

        if (partialFilmeUpdate.getFaixaEtaria() == null) {
            filmeEditado.getFaixaEtaria();
        } else {
            filmeEditado.setFaixaEtaria(partialFilmeUpdate.getFaixaEtaria());
        }

        return ResponseEntity.status(HttpStatus.OK).body(filmeRepository.save(filmeEditado));
    }
```


Agora vamos adicionar o wiremock para mockar api de terceiros mas com isso vamos subir o wiremock local
com docker-compose up

````
wiremock:
  image: holomekc/wiremock-gui:latest
  ports:
    - "8091:8080"
  volumes:
    - ./local/wiremock/mappings/:/home/wiremock/mappings
    - ./local/wiremock/__files/:/home/wiremock/__files
  environment:
    WIREMOCK_OPTIONS: "--local-response-templating, --root-dir=/home/wiremock"
````

depois disso precisamos crir a pasta local/wiremock/mappings e local/wiremock/__files

body.json
````
{
  "id": "123"
}
````

idDoFilmeExterno.json
```
{
  "request": {
    "urlPathPattern": "/idfilmeExterno/(.*)",
    "method": "GET"
  },
  "response": {
    "status": 200,
    "jsonBody": {
      "id": "12345"
    }
  }
}
```

mockComFiles

```
{
  "request": {
    "url": "/idfilmeExterno/123",
    "method": "GET"
  },
  "response": {
    "status": 200,
    "bodyFileName": "body.json",
    "transformers": ["response-template"]
  }
}
```

mockComHeader

```
{
  "request": {
    "url": "/validarHeader",
    "method": "GET",
    "headers": {
      "optional-header": {
        "equalTo": "12345678"
      }
    }
  },
  "response": {
    "status": 200,
    "jsonBody": {
      "mensagem": "Header válido"
    }
  }
}
```

mockSemHeader

```
{
  "request": {
    "url": "/validarHeader",
    "method": "GET",
    "headers": {
      "optional-header": {
        "equalTo": "12345"
      }
    }
  },
  "response": {
    "status": 404,
    "jsonBody": {
      "mensagem": "Header Inválido"
    }
  }
}
```

agora vamos criar outro controller para validar esses mocks

```
    @ApiOperation(value = "Retorna os dados de um filme externo")
    @GetMapping(path = "/idfilmeExterno/{codigo}")
    public ResponseEntity consultarIdFilmeExterno(@PathVariable("codigo") @ApiParam(name = "codigo", value = "codigo do filme", example = "1") Integer codigo) throws URISyntaxException {
        HttpResponse<String> response = null;
        try {
            var url = "http://localhost:8091/idfilmeExterno/" + codigo;
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

```


acessar o git lab
criar um repositorio pro projeto de back

subir o repositorio

cd existing_folder
git init --initial-branch=main
git remote add origin https://gitlab.com/brunobatista25/BackEndFilmes.git
git add .
git commit -m "Initial commit"
git push -u origin main

clicar em pipeline
https://gitlab.com/brunobatista25/BackEndFilmes/-/ci/editor


adicionar isso no proejt ode testes

        plugin = { "pretty", "json:target/cucumber/report.json", "html:target/cucumber/report.html" }
