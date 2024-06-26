/*
 * Copyright 2016 Luca Martino.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copyFile of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bluetooth.communicatorexample.gui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bluetooth.communicatorexample.R;
import com.bluetooth.communicator.Peer;

import java.util.ArrayList;

public class PeerListAdapter extends BaseAdapter {
    public static final int HOST = 1;
    private final ArrayList<Peer> array;
    private final LayoutInflater inflater;
    private final Callback callback;
    private boolean isClickable = true;
    private boolean showToast = false;

    public PeerListAdapter(Activity activity, ArrayList<Peer> array, Callback callback) {
        this.array = array;
        this.callback = callback;
        if (!array.isEmpty()) {
            callback.onFirstItemAdded();
        }
        notifyDataSetChanged();
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return HOST;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Object item = getItem(position);
        int itemType = getItemViewType(position);
        if (itemType == HOST) {
            String peerName = ((Peer) item).getName();
            if (view == null) {
                view = inflater.inflate(R.layout.component_row, null);
            }
            ((TextView) view.findViewById(R.id.textRow)).setText(peerName);

        }
        return view;
    }

    public synchronized void add(Peer peer) {
        if (array.isEmpty()) {
            callback.onFirstItemAdded();
        }
        array.add(peer);
        notifyDataSetChanged();
    }

    public synchronized void set(int index, Peer item) {
        array.set(index, item);
        notifyDataSetChanged();
    }

    public Peer get(int i) {
        return array.get(i);
    }

    public int indexOfPeer(String uniqueName) {
        for (int i = 0; i < array.size(); i++) {
            Peer peer = array.get(i);
            String uniqueName1 = peer.getUniqueName();
            if (!uniqueName1.isEmpty() && uniqueName1.equals(uniqueName)) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void remove(Peer peer) {
        if (array.remove(peer)) {
            notifyDataSetChanged();
        }
        if (array.isEmpty()) {
            // deleting the listview
            callback.onLastItemRemoved();
        }
    }

    public synchronized void clear() {
        array.clear();
        notifyDataSetChanged();
        if (array.isEmpty()) {
            callback.onLastItemRemoved();
        }
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable, boolean showToast) {
        this.isClickable = clickable;
        this.showToast = showToast;
    }

    public boolean getShowToast() {
        return showToast;
    }

    public Callback getCallback() {
        return callback;
    }

    public static abstract class Callback {
        public void onFirstItemAdded() {
        }

        public void onLastItemRemoved() {
        }

        public void onClickNotAllowed(boolean showToast) {
        }
    }
}
