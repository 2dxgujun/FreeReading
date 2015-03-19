package me.gujun.mybook.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import me.gujun.mybook.DisplayImageOptionsProvider;
import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.api.entity.Book;

/**
 * Bookshelf view show books in list mode.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-18 19:42:13
 */
public class BookshelfListView extends ListView {
    private BookshelfListAdapter mBookshelfListAdapter;

    public BookshelfListView(Context context) {
        this(context, null);
    }

    public BookshelfListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBookshelfListAdapter = new BookshelfListAdapter(context, R.layout.bookshelf_list_item);
        setAdapter(mBookshelfListAdapter);
    }

    public void setBookList(List<Book> bookList) {
        mBookshelfListAdapter.setItems(bookList);
    }

    /**
     * Bookshelf list adapter.
     */
    private class BookshelfListAdapter extends SingleTypeAdapter<Book> {
        public BookshelfListAdapter(Context context, int layoutResid) {
            super(context, layoutResid);
        }

        @Override
        protected ViewHolder onCreateViewHolder(View convertView) {
            BookshelfListItemViewHolder holder = new BookshelfListItemViewHolder();
            holder.bookCoverImg = (ImageView) convertView.findViewById(R.id.iv_book_cover);
            holder.bookTitleTxt = (TextView) convertView.findViewById(R.id.tv_book_title);
            holder.bookAuthorTxt = (TextView) convertView.findViewById(R.id.tv_book_author);
            holder.bookTagTxt = (TextView) convertView.findViewById(R.id.tv_book_tag);
            holder.bookIntroTxt = (TextView) convertView.findViewById(R.id.tv_book_intro);
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

            @Override
            protected void update(Book book) {
                ImageLoader.getInstance().displayImage(book.getCoverUrl(), bookCoverImg,
                        DisplayImageOptionsProvider.getDisplayImageOptions());
                bookTitleTxt.setText(book.getTitle());
                bookAuthorTxt.setText(book.getAuthor());
                bookTagTxt.setText(book.getTag());
                bookIntroTxt.setText(book.getIntro());
            }
        }
    }
}