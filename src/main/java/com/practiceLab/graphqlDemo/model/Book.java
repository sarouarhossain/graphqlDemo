package com.practiceLab.graphqlDemo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("books")
@Data
public class Book {
  @Id private int id;
  private String name;
  private int pages;
}
