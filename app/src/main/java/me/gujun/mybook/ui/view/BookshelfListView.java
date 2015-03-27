package me.gujun.mybook.ui.view;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.db.table.BookshelfTable;
import me.gujun.mybook.util.DisplayImageOptionsProvider;

/**
 * Bookshelf view show books in list mode.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-18 19:42:13
 */
public class BookshelfListView extends ListView {
    private BookshelfListAdapter mAdapter;
    private List<MarkableBook> mMarkableBookList;

    public BookshelfListView(Context context) {
        this(context, null);
    }

    public BookshelfListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAdapter = new BookshelfListAdapter(context, R.layout.bookshelf_list_item);
        setAdapter(mAdapter);
    }

    public void setBookList(List<Book> bookList) {
        mMarkableBookList = new ArrayList<>();
        for (Book book : bookList) {
            if (isBookDownloaded(book.getTitle())) {
                mMarkableBookList.add(new MarkableBook(book, true));
            } else {
                mMarkableBookList.add(new MarkableBook(book, false));
            }
        }

        mAdapter.setItems(mMarkableBookList);
    }

    private boolean isBookDownloaded(String title) {
        ContentResolver contentResolver = getContext().getContentResolver();
        Cursor cursor = contentResolver.query(BookshelfTable.CONTENT_URI, null,
                BookshelfTable.TITLE + "=?", new String[]{title}, null);

        return cursor.getCount() > 0;
    }

    public void markedBookAsDownloaded(int position) {
        mMarkableBookList.get(position).setMarked(true);
        mAdapter.notifyDataSetChanged();
    }

    public boolean isBookDownloaded(int position) {
        return mAdapter.getItem(position).isMarked();
    }

    public Book getBook(int position) {
        return mAdapter.getItem(position).getBook();
    }

    /**
     *
     */
    static class MarkableBook {
        private Book book;
        private boolean marked;

        public MarkableBook(Book book, boolean marked) {
            this.book = book;
            this.marked = marked;
        }

        public Book getBook() {
            return book;
        }

        public boolean isMarked() {
            return marked;
        }

        public void setMarked(boolean marked) {
            this.marked = marked;
        }
    }

    /**
     * Bookshelf list adapter.
     */
    private class BookshelfListAdapter extends SingleTypeAdapter<MarkableBook> {
        public BookshelfListAdapter(Context context, int layoutResid) {
            super(context, layoutResid);
        }

        @Override
        protected ViewHolder onCreateViewHolder(View convertView) {
            BookshelfListItemViewHolder holder = new BookshelfListItemViewHolder();
            holder.bookCoverImg = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.bookTitleTxt = (TextView) convertView.findViewById(R.id.tv_title);
            holder.bookAuthorTxt = (TextView) convertView.findViewById(R.id.tv_author);
            holder.bookTagTxt = (TextView) convertView.findViewById(R.id.tv_tag);
            holder.bookIntroTxt = (TextView) convertView.findViewById(R.id.tv_book_intro);
            holder.coverMarkTxt = (TextView) convertView.findViewById(R.id.tv_cover_mark);
            return holder;
        }

        /**
         * Bookshelf list item view holder.
         */
        class BookshelfListItemViewHolder extends ViewHolder {
            ImageView bookCoverImg;
            TextView bookTitleTxt;
            TextView bookAuthorTxt;
            TextView bookTagTxt;
            TextView bookIntroTxt;
            TextView coverMarkTxt;

            @Override
            protected void update(MarkableBook book) {
                ImageLoader.getInstance().displayImage(book.getBook().getCoverUrl(), bookCoverImg,
                        DisplayImageOptionsProvider.getDisplayImageOptions());
                bookTitleTxt.setText(book.getBook().getTitle());
                bookAuthorTxt.setText(book.getBook().getAuthor());
                bookTagTxt.setText(book.getBook().getTag());
                bookIntroTxt.setText(book.getBook().getIntro());
                coverMarkTxt.setVisibility(book.isMarked() ? View.VISIBLE : View.GONE);
            }
        }
    }
}