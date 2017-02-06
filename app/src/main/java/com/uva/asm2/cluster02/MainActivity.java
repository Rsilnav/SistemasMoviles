package com.uva.asm2.cluster02;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.uva.asm2.cluster02.modelo.ExportActivity;
import com.uva.asm2.cluster02.modelo.ImportActivity;
import com.uva.asm2.cluster02.modelo.Movimiento;
import com.uva.asm2.cluster02.modelo.MovimientoDbHelper;
import com.uva.asm2.cluster02.vista.DetalleFragment;
import com.uva.asm2.cluster02.vista.HomeFragment;
import com.uva.asm2.cluster02.vista.ListaFragment;
import com.uva.asm2.cluster02.vista.MiddleFragment;
import com.uva.asm2.cluster02.vista.NewMovement;
import com.uva.asm2.cluster02.vista.SettingsFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;


public class MainActivity extends AppCompatActivity implements
        BottomNavigation.OnMenuItemSelectionListener,
        ListaFragment.OnFragmentInteractionListener,
        DetalleFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener {

    private Fragment mFragment;
    private Movimiento mov;
    private boolean continuar = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent intent = new Intent(this, CreditosActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String PACKAGE_NAME = getApplicationContext().getPackageName();

        setContentView(R.layout.mainlayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    3333);
        }

        FrameLayout layout = (FrameLayout) findViewById(R.id.fragment_container);

        BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        bottomNavigation.setOnMenuItemClickListener(this);

        if(null == savedInstanceState){
            HomeFragment sample = new HomeFragment();
            android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, sample);
            transaction.commit();
        }

    }

    @Override
    public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {

        mFragment = null;

        // Clear the backstack
        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }

        switch (i1) {
            case 0:
                mFragment = new HomeFragment();
                break;
            case 1:
                mFragment = new NewMovement();
                break;
            case 2:
                mFragment = new MiddleFragment();
                break;
            case 3:
                mFragment = new SettingsFragment();
                break;
            default:
                break;
        }

        if (mFragment == null)
            throw new RuntimeException("No se ha pinchado en un menu correcto: " + Integer.toString(i1));

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();

    }

    @Override
    public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
        mFragment = null;

        // Clear the backstack
        while (getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }

        switch (i1) {
            case 0:
                mFragment = new HomeFragment();
                break;
            case 1:
                mFragment = new NewMovement();
                break;
            case 2:
                mFragment = new MiddleFragment();
                break;
            case 3:
                mFragment = new SettingsFragment();
                break;
            default:
                break;
        }

        if (mFragment == null)
            throw new RuntimeException("No se ha pinchado en un menu correcto: " + Integer.toString(i1));

        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(Movimiento mov) {

        this.mov = mov;
        MiddleFragment middleFragment = (MiddleFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        middleFragment.setDetalle(mov);

    }

    @Override
    public void onDetalleFragmentInteraction(Movimiento mov) {
        MovimientoDbHelper dbHelper = new MovimientoDbHelper(this);
        dbHelper.deleteMovimiento(mov);

        MiddleFragment mFragment = new MiddleFragment();
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, mFragment);
        transaction.commit();

        Toast toast = Toast.makeText(this,
                R.string.movimiento_borrado, Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void onDetalleFragmentInteractionOpenCamera() {
        openImageIntent();
    }

    private Uri outputFileUri;

    private void openImageIntent() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        final Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.selecciona_fuente));

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, 42);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 42) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                }

                MiddleFragment middleFragment = (MiddleFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                MovimientoDbHelper movimientoDbHelper = new MovimientoDbHelper(this);
                movimientoDbHelper.deleteMovimiento(mov);

                mov.setFoto(selectedImageUri.toString());

                movimientoDbHelper.saveMovimiento(mov);

                continuar = true;


            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (continuar) {
            MiddleFragment middleFragment = (MiddleFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            middleFragment.setDetalle(mov);
        }
    }

    @Override
    public void onSettingsFragmentInteraction(View view) {
        Log.d("ASIMOV", "Hola");
        if (view == findViewById(R.id.exportar)) {
            Intent intent = new Intent(this, ExportActivity.class);
            startActivity(intent);
        }
        else if(view == findViewById(R.id.importar)) {
            //Intent intent = new Intent(this, ExportarActivity.class);
            Intent intent = new Intent(this, ImportActivity.class);
            startActivity(intent);
        }

    }
}
