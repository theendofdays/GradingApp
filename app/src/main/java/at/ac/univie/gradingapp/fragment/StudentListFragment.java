package at.ac.univie.gradingapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import at.ac.univie.gardingapp.R;
import at.ac.univie.gradingapp.model.SchoolClass;
import at.ac.univie.gradingapp.model.Student;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class StudentListFragment extends Fragment implements AbsListView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static String schoolClassKey = "ClassKey";
    private OnFragmentInteractionListener mListener;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;
    private SchoolClass mSchoolClass;
    private Button mBulkAddButton;
    private Button mWeightButton;


    // TODO: Rename and change types of parameters
    public static StudentListFragment newInstance() {
        StudentListFragment fragment = new StudentListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static StudentListFragment newInstance(SchoolClass schoolClassPicker) {
        StudentListFragment fragment = new StudentListFragment();
        Bundle args = new Bundle();
        args.putSerializable(schoolClassKey, schoolClassPicker); //schoolClassKey = Name Bundle
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSchoolClass = (SchoolClass) getArguments().getSerializable(schoolClassKey); //Casten!
        }

        if (mSchoolClass != null) {
            mAdapter = new ArrayAdapter<Student>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, mSchoolClass.getStudents());
        } else {
            mAdapter = new ArrayAdapter<Student>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, Student.getAllStudents());
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);

        mBulkAddButton = (Button) view.findViewById(R.id.bulkAddButton);
        mBulkAddButton.setOnClickListener(bulkAddButtonClickListener);

        mWeightButton = (Button) view.findViewById(R.id.weightButton);
        mWeightButton.setOnClickListener(weightButtonClickListener);

        return view;
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Open Popup for adding or showing grades

        final Student student = (Student)parent.getAdapter().getItem(position);
        if(student != null) {
            String selected = student.toString();

            if(selected !=  null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Aktionen für " + selected)
                        .setCancelable(true)
                        .setPositiveButton("Note anzeigen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mListener.onViewGradesClicked(student);
                            }
                        })
                        .setNegativeButton("Note eingeben", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mListener.onAddNewGradeClicked(student);
                            }
                        });
                builder.show();
            }
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Student studentPicker = (Student) parent.getItemAtPosition(position);
        studentPicker.delete();
        if (mSchoolClass != null) {
            mAdapter = new ArrayAdapter<Student>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, mSchoolClass.getStudents());
        } else {
            mAdapter = new ArrayAdapter<Student>(getActivity(),
                    android.R.layout.simple_list_item_1, android.R.id.text1, Student.getAllStudents());
        }

        mListView.setAdapter(mAdapter);
        return false;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onAddNewGradeClicked(Student selectedStudent);
        public void onViewGradesClicked(Student selectedStudent);
        public void onBulkAddClicked(SchoolClass selectedSchoolClass);
        public void onWeightClicked(SchoolClass selectedSchoolClass);
    }

    private View.OnClickListener bulkAddButtonClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mListener != null && mSchoolClass != null) {
                mListener.onBulkAddClicked(mSchoolClass);
            }
        }
    };

    private View.OnClickListener weightButtonClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mListener != null && mSchoolClass != null) {
                mListener.onWeightClicked(mSchoolClass);
            }
        }
    };
}
