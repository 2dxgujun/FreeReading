package me.gujun.mybook.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import me.gujun.mybook.DisplayImageOptionsProvider;
import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.api.entity.Book;

/**
 * Custom view for display books.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 8:24:01
 */
public class BookshelfGridView extends GridView {
    private BookshelfGridAdapter mBookshelfGridAdapter;

    public BookshelfGridView(Context context) {
        super(context, null);
    }

    public BookshelfGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVerticalSpacing(6);
        setHorizontalSpacing(6);
        setNumColumns(3);

        mBookshelfGridAdapter = new BookshelfGridAdapter(context, R.layout.bookshelf_grid_item);
        setAdapter(mBookshelfGridAdapter);
    }

    public void setBookList(List<Book> bookList) {
        mBookshelfGridAdapter.setItems(bookList);
    }

    /**
     * Bookshelf grid adapter.
     */
    private class BookshelfGridAdapter extends SingleTypeAdapter<Book> {
        public BookshelfGridAdapter(Context context, int layoutResid) {
            super(context, layoutResid);
        }

        @Override
        protected ViewHolder onCreateViewHolder(View convertView) {
            BookshelfGridItemViewHolder holder = new BookshelfGridItemViewHolder();
            holder.bookCoverImg = (ImageView) convertView.findViewById(R.id.iv_book_cover);
            holder.bookTitleTxt = (TextView) convertView.findViewById(R.id.tv_book_title);
            return holder;
        }

        /**
         * Bookshelf list item view holder.
         */
        class BookshelfGridItemViewHolder extends ViewHolder {
            ImageView bookCoverImg;
            TextView bookTitleTxt;

            @Override
            protected void update(Book book) {
                ImageLoader.getInstance().displayImage(book.getCoverUrl(), bookCoverImg,
                        DisplayImageOptionsProvider.getDisplayImageOptions());
                bookTitleTxt.setText(book.getTitle());
            }
        }
    }
}