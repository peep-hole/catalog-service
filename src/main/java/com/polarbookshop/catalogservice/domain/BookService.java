package com.polarbookshop.catalogservice.domain;

import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> viewBookList() {
        return bookRepository.findAll();
    }

    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    public Book addBookToCatalog(Book book) {
        if(bookRepository.existsByIsbn(book.isbn())) {
            throw new BookAlreadyExistsException(book.isbn());
        }
        return bookRepository.save(book);
    }

    public void removeBookFromCatalog(String isbn) {
        bookRepository.deleteByIsbn(isbn);
    }

    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn).map(exisitingBook -> {
            Book bookToUpdate = new Book(
                    exisitingBook.id(),
                    exisitingBook.isbn(),
                    book.title(),
                    book.author(),
                    book.price(),
                    book.publisher(),
                    exisitingBook.createdDate(),
                    exisitingBook.lastModifiedDate(),
                    exisitingBook.version()
            );
            return bookRepository.save(bookToUpdate);
        }).orElseGet(() -> addBookToCatalog(book));
    }

}
