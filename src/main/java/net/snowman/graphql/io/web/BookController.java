package net.snowman.graphql.io.web;

import graphql.ExecutionResult;
import lombok.extern.slf4j.Slf4j;
import net.snowman.graphql.io.service.GraphQLService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping( value = "/api/v1")
public class BookController {

    private final GraphQLService graphQLService;

    public BookController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @PostMapping(value = {"/books"})
    public ResponseEntity<?> getAllBooks(@RequestBody String query){
        ExecutionResult result = graphQLService.getGraphQL().execute(query);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
