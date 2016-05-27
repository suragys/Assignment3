package edu.scu.suragys.assignment3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<MyObject> myDataset = new ArrayList<>();
    private FloatingActionButton fab;
    private DBHelper mDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         mDbHelper = new DBHelper(getApplicationContext());


        myDataset = mDbHelper.getDataObjects();

//        myDataset.add("surag");
//        myDataset.add("manoj");
//        myDataset.add("mahendra");


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        mAdapter = new RecyclerAdapter(myDataset, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new TouchHelper(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);

        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            fab.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });


    }

//    private void getDataSet(DBHelper mDbHelper) {
//
//        myDataset =
//    }

    public void takePicture() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        file = Uri.fromFile(getOutputMediaFile());
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//        Log.v("File path", "----------------"+file.getPath());
//
//        startActivityForResult(intent, 100);

        Intent intent = new Intent(getApplicationContext(), AddPhotoActivity.class);
        startActivityForResult(intent, 100);
//        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Bundle exts = data.getExtras();
                MyObject o = (MyObject) exts.get("object");
                o.setPosition(myDataset.size());

                o.setId((int) mDbHelper.addData(o));
                myDataset.add(o);
                mAdapter.notifyDataSetChanged();


            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                fab.setEnabled(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id==R.id.add){
            takePicture();
        } else if(id==R.id.uninstall){
            uninstallApp();
        }

        return super.onOptionsItemSelected(item);
    }

    public DBHelper getDbHelper(){
        return mDbHelper;
    }

    @Override
    protected void onPause() {
        super.onPause();
        updatePositions(myDataset);
        mDbHelper.update(myDataset);
    }

    private void updatePositions(ArrayList<MyObject> myDataset) {

        for (int i = 0; i < myDataset.size();i++){
            MyObject o = myDataset.get(i);
            o.setPosition(i);
//            myDataset.
        }
    }

    public void uninstallApp() {
        startActivity(new Intent(Intent.ACTION_DELETE).setData(Uri.parse("package:edu.scu.suragys.assignment3")));
    }

    private void testUpload(){
        int i = 1+1;
    }
}
