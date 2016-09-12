package yh2tan.crowdcurio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import yh2tan.crowdcurio.MemberFragment.OnListFragmentInteractionListener;
import yh2tan.crowdcurio.dummy.DummyContent;
import yh2tan.crowdcurio.dummy.MemberContent.MemberItem;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MemberItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMemberRecyclerViewAdapter extends RecyclerView.Adapter<MyMemberRecyclerViewAdapter.ViewHolder>{

    private final List<MemberItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMemberRecyclerViewAdapter(List<MemberItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nickname.setText("nickname: " + mValues.get(position).nickname);
        holder.mContentView.setText("email: " + mValues.get(position).email);
        DownloadImage download = new DownloadImage(holder.avatar);
        download.execute(mValues.get(position).avatar);

        holder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public final View mView;
        public final TextView mContentView;
        public final TextView nickname;
        public MemberItem mItem;
        public final ImageView avatar;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            avatar = (ImageView) view.findViewById(R.id.avatarMem);
            nickname = (TextView) view.findViewById(R.id.nickname);
            mContentView = (TextView) view.findViewById(R.id.email);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    class DownloadImage extends AsyncTask<String,Void, Bitmap>{

        final ImageView imageview;

        public DownloadImage(ImageView iv){
            imageview = iv;
        }

        @Override
        public void onPreExecute(){
        }

        @Override
        public Bitmap doInBackground(String... strings){
            try{
                URL urlconnection = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) urlconnection.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap image = BitmapFactory.decodeStream(input);

                return image;
            }catch (Exception e){
                Log.d("fetch image", "Something is wrong");
            }
            return null;
        }

        @Override
        public void onPostExecute(Bitmap result){

            // SetImage
            imageview.setImageBitmap(getCroppedBitmap(result));
        }
    }

    //circular cropping
    public Bitmap getCroppedBitmap(Bitmap bitmap) {

        if (bitmap == null){
            return bitmap;
        }

        int min = bitmap.getHeight() > bitmap.getWidth() ? bitmap.getWidth() : bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, min, min);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }
}
