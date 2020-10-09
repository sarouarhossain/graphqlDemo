package com.practiceLab.graphqlDemo.repositories;

import com.practiceLab.graphqlDemo.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BookRepository {
  @Autowired private DatabaseClient databaseClient;

  public Mono<Book> getBook(int id) {
    return databaseClient
        .select()
        .from(Book.class)
        .matching(Criteria.where("id").is(id))
        .fetch()
        .one();
  }

  public Flux<Book> getBooks() {
    return databaseClient.select().from(Book.class).fetch().all();
  }
}
