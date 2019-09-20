package net.snowman.graphql.io.service;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.extern.slf4j.Slf4j;
import net.snowman.graphql.io.repository.BookRepository;
import net.snowman.graphql.io.service.datafetcher.AllBooksDataFetcher;
import net.snowman.graphql.io.service.datafetcher.BookDataFetcher;
import net.snowman.graphql.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
@Service
public class GraphQLService {

    private final Resource resource;
    private GraphQL graphQL;
    private final AllBooksDataFetcher allBooksDataFetcher;
    private final BookDataFetcher bookDataFetcher;
    private final BookRepository bookRepository;

    public GraphQLService(
            @Value("classpath:books.graphql") Resource resource, AllBooksDataFetcher allBooksDataFetcher, BookDataFetcher bookDataFetcher, BookRepository bookRepository) {
        this.resource = resource;
        this.allBooksDataFetcher = allBooksDataFetcher;
        this.bookDataFetcher = bookDataFetcher;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    private void loadSchema() throws IOException {
        // Load Data into HSQL
        loadDataIntoHSQL();
        
        // Get schema
        File schemaFile = resource.getFile();

        // Parse schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private void loadDataIntoHSQL() {
        Stream.of(
            new Book("a6196fa5-7067-41c4-baf1-a0f150ec6d5e", "Data Science from Scratch", "O’Reilly Media, Inc.", new String[] {"Joel Grus"}, "Jan 2015"),
            new Book("efe59fe4-50d2-4245-aa4c-a3e14b4040c3", "Python Data Science Handbook: Essential Tools for Working with Data", "O’Reilly Media, Inc.", new String[] {"Jacob T. VanderPlas"}, "Dec 2016"),
            new Book("6cdfdb2c-5425-48c6-867e-a8d9fa4a37c2", "Doing Data Science: Straight Talk from the Frontline", "O’Reilly Media, Inc.", new String[] {"Cathy O'Neil", "Rachel Schutt"}, "Jul 2013"),
            new Book("57ca07b1-38ce-4a77-8eae-b479eae8a2b7", "Practical Statistics for Data Scientists: 50 Essential Concepts", "O’Reilly Media, Inc.", new String[] {"Andrew Bruce", "Peter C. Bruce"}, "May 2017")
        ).forEach(book -> bookRepository.save(book));
    }

    private RuntimeWiring buildRuntimeWiring() {
       return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> {
                            typeWiring
                                    .dataFetcher("allBooks", allBooksDataFetcher)
                                    .dataFetcher("book", bookDataFetcher)
                                    .build();

                            return  typeWiring;
                        }
                )
                .build();
    }

    public GraphQL getGraphQL() {
        return graphQL;
    }

}
