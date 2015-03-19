package me.gujun.mybook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Base class of implementation for an {@link android.widget.BaseAdapter}, it can bind a single
 * type of data and you need to implement the {@link ViewHolder} class as the holder pattern to
 * hold the views to the item in this adapter.
 *
 * @author Jun Gu (2dxgujun@gmail.com)
 * @version 1.0
 * @since 2014/11/11 9:41
 */
public abstract class SingleTypeAdapter<E> extends BaseAdapter {
    private static final Object[] EMPTY = new Object[0];

    private Context context;
    private LayoutInflater inflater;

    private E[] items;

    /**
     * The layout resource id of the list item view.
     */
    private int layoutResid;

    /**
     * Create a single type adapter with empty items.
     *
     * @param context
     * @param layoutResid layout resource id
     */
    public SingleTypeAdapter(Context context, int layoutResid) {
        this(context, null, layoutResid);
    }

    /**
     * Create a single type adapter with given items.
     *
     * @param context
     * @param items       the initial items
     * @param layoutResid layout resource id
     */
    public SingleTypeAdapter(Context context, List<E> items, int layoutResid) {
        this.context = context;
        this.layoutResid = layoutResid;
        this.inflater = LayoutInflater.from(context);
        setItems(items);
    }

    protected Context getContext() {
        return context;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public E getItem(int position) {
        return items[position];
    }

    /**
     * Get a list of all items.
     *
     * @return list of all items.
     */
    public List<E> getItems() {
        List<E> itemList = Arrays.asList(items);
        return itemList;
    }

    /**
     * Set items to display.
     *
     * @param items
     */
    public void setItems(E[] items) {
        if (items != null) {
            this.items = items;
        } else {
            this.items = (E[]) EMPTY;
        }
        notifyDataSetChanged();
    }

    /**
     * Set items to display.
     *
     * @param items
     */
    public void setItems(Collection<E> items) {
        if (items != null && !items.isEmpty()) {
            setItems((E[]) items.toArray());
        } else {
            setItems((E[]) EMPTY);
        }
    }

    @Override
    public long getItemId(int position) {
        return items[position].hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layoutResid, null);
            ViewHolder holder = onCreateViewHolder(convertView);
            onViewHolderCreated(holder);
            convertView.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.update(getItem(position));
        return convertView;
    }

    /**
     * Create the view holder with the list item views.
     * <p/>
     * You should get child view from <var>convertView</var> use {@link View#findViewById(int)}.
     *
     * @param convertView
     * @return
     */
    protected abstract ViewHolder onCreateViewHolder(View convertView);

    /**
     * Called immediately after {@link #onCreateViewHolder(android.view.View)} has returned.
     * <p/>
     * This gives subclasses a chance to initialize the views once they know their view holder
     * has been completely created.
     *
     * @param holder The view holder return by {@link #onCreateViewHolder(android.view.View)}.
     */
    protected void onViewHolderCreated(ViewHolder holder) {
        // Intentionally left blank
    }

    /**
     * Abstract view holder class.
     * <p/>
     * Subclasses should implement there own view holder.
     */
    protected abstract class ViewHolder {
        /**
         * Update view with given item.
         *
         * @param item The current positioned item.
         */
        protected abstract void update(E item);
    }
}