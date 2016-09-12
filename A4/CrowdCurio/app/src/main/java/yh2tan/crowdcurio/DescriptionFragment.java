package yh2tan.crowdcurio;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import yh2tan.crowdcurio.dummy.DummyContent;


/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment{

    public static final String ARG_ITEM_ID = "item_id";
    public DummyContent.DummyItem mItem;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle b){
        super.onCreate(b);

        if (getArguments() != null) {
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString("ID"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_description, container, false);
        TextView description = (TextView) rootView.findViewById(R.id.description);
        description.setText(mItem.longcontent);

        // Inflate the layout for this fragment
        return rootView;
    }

}
