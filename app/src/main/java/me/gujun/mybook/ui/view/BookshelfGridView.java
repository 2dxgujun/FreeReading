package me.gujun.mybook.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import me.gujun.mybook.R;
import me.gujun.mybook.SingleTypeAdapter;
import me.gujun.mybook.db.model.Book;
import me.gujun.mybook.util.DisplayImageOptionsProvider;

/**
 * Custom view for display books.
 *
 * @author Jun Gu (http://2dxgujun.com)
 * @version 1.0
 * @since 2015-3-17 8:24:01
 */
public class BookshelfGridView extends GridView {
    private BookshelfGridAdapter mAdapter;

    public BookshelfGridView(Context context) {
        super(context, null);
    }

    public BookshelfGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAdapter = new BookshelfGridAdapter(getContext(),
                R.layout.bookshelf_grid_item);

        post(new Runnable() {
            @Override
            public void run() {
                int cellWidth = getWidth() / 3;
                mAdapter.setCellWidth(cellWidth);
                setAdapter(mAdapter);
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public Book getBook(int position) {
        return mAdapter.getItem(position);
    }

    public void setBookList(List<Book> bookList) {
        mAdapter.setItems(bookList);
    }

    /**
     * Bookshelf grid adapter.
     */
    private class BookshelfGridAdapter extends SingleTypeAdapter<Book> {
        private int mCellWidth;

        public BookshelfGridAdapter(Context context, int layoutResid) {
            super(context, layoutResid);
        }

        public void setCellWidth(int cellWidth) {
            this.mCellWidth = cellWidth;
        }

        @Override
        protected ViewHolder onCreateViewHolder(View convertView) {
            ItemViewHolder holder = new ItemViewHolder();
            holder.coverImg = (ImageView) convertView.findViewById(R.id.iv_cover);
            holder.titleTxt = (TextView) convertView.findViewById(R.id.tv_title);

            holder.coverImg.setLayoutParams(new LinearLayout.LayoutParams(mCellWidth, mCellWidth));
            return holder;
        }

        /**
         * Bookshelf grid item view holder.
         */
        class ItemViewHolder extends ViewHolder {
            ImageView coverImg;
            TextView titleTxt;

            @Override
            protected void update(Book book) {
                ImageLoader.getInstance().displayImage(book.getCoverUrl(), coverImg,
                        DisplayImageOptionsProvider.getDisplayImageOptions());
                titleTxt.setText(book.getTitle());
            }
        }
    }
}