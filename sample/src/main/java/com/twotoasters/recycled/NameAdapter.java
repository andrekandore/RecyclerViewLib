package com.twotoasters.recycled;


import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class NameAdapter extends RecyclerView.Adapter<NameViewHolder> {
    private List<Item> mNames;
    private RecyclerView mRecyclerView;
    private RecycleActivity mRecycleActivity;
    public NameAdapter(List<Item> names) {
        this.mNames = names;
    }
    RecyclerView.ItemAnimator mEntranceItemAnimator = new com.twotoasters.android.support.v7.widget.DefaultItemAnimator();

    public NameAdapter(List<Item> names,RecyclerView mRecyclerView,RecycleActivity recycleActivity) {
        this.mRecycleActivity = recycleActivity;
        this.mRecyclerView = mRecyclerView;
        this.mNames = names;
    }

    /**
     * @param name to be added to the list in a random position.
     */
    public void addToList(Item name) {
        int position = 0;
        if (mNames.size() > 1) {
            // Put the new name in a random place.
            position = new Random().nextInt(mNames.size() - 1);
        }
        mNames.add(position, name);
        notifyItemInserted(position);
    }

    public void removeItemFromList(Item name) {
        int position = mNames.indexOf(name);
        mNames.remove(position);
        notifyItemRemoved(position);
    }


    @Override
    public NameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item, parent, false);
        return new NameViewHolder(view);
    }

    @Override
    public void onViewRecycled(NameViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void onViewAttachedToWindow(NameViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        this.animateAppearance(holder, new Rect(holder.itemView.getLeft(),holder.itemView.getTop(),holder.itemView.getRight(),holder.itemView.getBottom()), holder.itemView.getLeft(), holder.itemView.getTop(),this.mRecycleActivity.currentItemAnimator());
    }

    @Override
    public void onViewDetachedFromWindow(NameViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(NameViewHolder viewHolder, final int position) {
        final Item name = mNames.get(position);
        viewHolder.textView.setText(name.toString());
        viewHolder.textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItemFromList(name);
            }
        });

        if (null != this.mRecyclerView) {
         //   this.mRecyclerView.animateAppearance(viewHolder);
        }
    }

    private void animateAppearance(RecyclerView.ViewHolder itemHolder, Rect beforeBounds, int afterLeft, int afterTop,final RecyclerView.ItemAnimator itemAnimator) {
        View newItemView = itemHolder.itemView;

        if (beforeBounds != null && (beforeBounds.left != afterLeft || beforeBounds.top != afterTop)) {

            itemHolder.setIsRecyclable(false);
            if (itemAnimator.animateMove(itemHolder,
                    beforeBounds.left, beforeBounds.top,
                    afterLeft, afterTop)) {
                postAnimationRunner(itemHolder.itemView,itemAnimator);
            }
        } else {

            itemHolder.setIsRecyclable(false);
            if (itemAnimator.animateAdd(itemHolder)) {
                postAnimationRunner(itemHolder.itemView,itemAnimator);
            }
        }
    }

    private void postAnimationRunner(View recyclerView,final RecyclerView.ItemAnimator itemAnimator) {
          ViewCompat.postOnAnimation(recyclerView, itemAnimatorRunner(itemAnimator));
    }

    private Runnable itemAnimatorRunner(final RecyclerView.ItemAnimator itemAnimator) {

        return new Runnable() {
            @Override
            public void run() {
                if (itemAnimator != null) {
                    itemAnimator.runPendingAnimations();
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }
}
