package com.practiceLab.graphqlDemo.services;

import com.practiceLab.graphqlDemo.model.Book;
import com.practiceLab.graphqlDemo.repositories.BookRepository;
import graphql.schema.DataFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BookService {
  @Autowired private BookRepository bookRepository;

  public DataFetcher<CompletableFuture<Book>> getBook() {
    return env -> {
      int bookId = env.getArgument("id");
      return bookRepository.getBook(bookId).toFuture();
    };
  }

  public DataFetcher<CompletableFuture<List<Book>>> getBooks() {
    return env -> bookRepository.getBooks().collectList().toFuture();
  }
}
