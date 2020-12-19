//package com.bridgelabz.bookStore.utility;
//
//import com.bridgelabz.bookStore.admin.model.Book;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//import java.util.List;
//import java.util.Optional;
//
//@EnableElasticsearchRepositories
//public interface IElasticBookRepository extends ElasticsearchRepository<Book, Integer> {
//    Optional<List<Book>> findBookByBookName(String bookName);
//    Optional<List<Book>> findByAuthorName(String authorName);
//}
