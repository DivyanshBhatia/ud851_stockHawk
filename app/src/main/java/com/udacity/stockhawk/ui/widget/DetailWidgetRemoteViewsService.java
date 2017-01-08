package com.udacity.stockhawk.ui.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.DetailActivity;
import com.udacity.stockhawk.ui.MainActivity;
import com.udacity.stockhawk.ui.utils.FormatChange;

import static com.udacity.stockhawk.ui.MainActivity.DOLLAR_FORMAT;
import static com.udacity.stockhawk.ui.MainActivity.PERCENTAGE_FORMAT;

/**
 * Created by dnbhatia on 1/8/2017.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewsService extends RemoteViewsService {
    public final String LOG_TAG = DetailWidgetRemoteViewsService.class.getSimpleName();

    //Projection Columns will be quote columns


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS,
                        null,
                        null,
                        Contract.Quote.COLUMN_SYMBOL + " ASC");
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {

                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.stock_widget_layout);

                views.setTextViewText(R.id.symbol, data.getString(Contract.Quote.POSITION_SYMBOL).toUpperCase());
                views.setTextViewText(R.id.price, FormatChange.convertToDecimalFormat(data.getFloat(Contract.Quote.POSITION_PRICE), DOLLAR_FORMAT));
                float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                if (rawAbsoluteChange > 0) {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }
                final Intent fillInIntent = new Intent(getApplicationContext(), DetailActivity.class);
                fillInIntent.putExtra(getApplicationContext().getResources().getString(R.string.stock_quote_symbol), data.getString(Contract.Quote.POSITION_SYMBOL));
                fillInIntent.putExtra(getApplicationContext().getResources().getString(R.string.stock_quote_history), data.getString(Contract.Quote.POSITION_HISTORY));
                views.setOnClickFillInIntent(R.id.symbol, fillInIntent);
                String percentage = FormatChange.convertToDecimalFormat(percentageChange / 100, PERCENTAGE_FORMAT);
                views.setTextViewText(R.id.change, percentage);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.stock_widget_layout);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(Contract.Quote.POSITION_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}