package net.snowman.graphql.io.service.datafetcher;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import net.snowman.graphql.io.repository.BookRepository;
import net.snowman.graphql.model.Book;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

@Component
public class BookDataFetcher implements DataFetcher<Book> {

    private final BookRepository bookRepository;

    public BookDataFetcher(BookRepository repository) {
        this.bookRepository = repository;
    }

    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {
        String isn = dataFetchingEnvironment.getArgument("id");
        return bookRepository.findById(isn).get();
    }
}
