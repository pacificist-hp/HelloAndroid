// IBookManager.aidl
package com.android.pacificist.helloandroid;

// Declare any non-default types here with import statements
import com.android.pacificist.helloandroid.Book;

interface IBookManager {
    List<Book> getBookList();
    void addBook(in Book book);
}
