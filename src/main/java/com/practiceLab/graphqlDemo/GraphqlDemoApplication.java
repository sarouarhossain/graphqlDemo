package com.practiceLab.graphqlDemo;

import com.practiceLab.graphqlDemo.services.BookService;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import java.io.IOException;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.practiceLab.graphqlDemo.repositories")
public class GraphqlDemoApplication {
  @Autowired private BookService bookService;

  public static void main(String[] args) {
    SpringApplication.run(GraphqlDemoApplication.class, args);
  }

  @Bean
  public ConnectionFactoryInitializer connectionFactoryInitializer(
      ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);
    ResourceDatabasePopulator populator =
        new ResourceDatabasePopulator(
            new ClassPathResource("schema.sql"), new ClassPathResource("data.sql"));
    initializer.setDatabasePopulator(populator);
    return initializer;
  }

  @Bean
  public GraphQL graphQL() throws IOException {
    SchemaParser schemaParser = new SchemaParser();
    ClassPathResource schema = new ClassPathResource("schema.graphql");
    TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema.getInputStream());

    RuntimeWiring runtimeWiring =
        RuntimeWiring.newRuntimeWiring()
            .type(
                TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("getBook", bookService.getBook()))
            .type(
                TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("getBooks", bookService.getBooks()))
            .build();

    SchemaGenerator generator = new SchemaGenerator();
    GraphQLSchema graphQLSchema =
        generator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    return GraphQL.newGraphQL(graphQLSchema).build();
  }
}
