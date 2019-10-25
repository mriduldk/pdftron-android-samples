package com.pdftron.android.tutorial.customui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pdftron.android.tutorial.customui.custom.CustomAnnotationToolbar;
import com.pdftron.android.tutorial.customui.custom.CustomLinkClick;
import com.pdftron.android.tutorial.customui.custom.CustomQuickMenu;
import com.pdftron.pdf.PDFViewCtrl;
import com.pdftron.pdf.config.PDFViewCtrlConfig;
import com.pdftron.pdf.config.ViewerBuilder;
import com.pdftron.pdf.config.ViewerConfig;
import com.pdftron.pdf.controls.PdfViewCtrlTabHostFragment;
import com.pdftron.pdf.model.FileInfo;
import com.pdftron.pdf.utils.Utils;

import java.io.File;

public class MainActivity extends AppCompatActivity implements PdfViewCtrlTabHostFragment.TabHostListener {

    private PdfViewCtrlTabHostFragment mPdfViewCtrlTabHostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a PdfViewCtrlTabHostFragment with a document Uri
        File f = Utils.copyResourceToLocal(this, R.raw.sample, "sample", ".pdf");
        Uri uri = Uri.fromFile(f);

        PDFViewCtrlConfig pdfViewCtrlConfig = PDFViewCtrlConfig.getDefaultConfig(this);
        pdfViewCtrlConfig.setMaintainZoomEnabled(false);
        ViewerConfig config = new ViewerConfig.Builder()
                .pdfViewCtrlConfig(pdfViewCtrlConfig)
                .build();

        mPdfViewCtrlTabHostFragment = ViewerBuilder.withUri(uri)
                .usingCustomToolbar(new int[] {R.menu.my_custom_options_toolbar})
                .usingNavIcon(R.drawable.ic_star_white_24dp)
                .usingConfig(config)
                .build(this);
        mPdfViewCtrlTabHostFragment.addHostListener(this);

        // Add the fragment to our activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, mPdfViewCtrlTabHostFragment);
        ft.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mPdfViewCtrlTabHostFragment != null && mPdfViewCtrlTabHostFragment.getCurrentPdfViewCtrlFragment() != null) {
            PDFViewCtrl pdfViewCtrl = mPdfViewCtrlTabHostFragment.getCurrentPdfViewCtrlFragment().getPDFViewCtrl();
            if (pdfViewCtrl != null) {
                try {
                    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        pdfViewCtrl.setPageViewMode(PDFViewCtrl.PageViewMode.FIT_WIDTH);
                        pdfViewCtrl.setPageRefViewMode(PDFViewCtrl.PageViewMode.FIT_WIDTH);
                    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        pdfViewCtrl.setPageViewMode(PDFViewCtrl.PageViewMode.FIT_PAGE);
                        pdfViewCtrl.setPageRefViewMode(PDFViewCtrl.PageViewMode.FIT_PAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPdfViewCtrlTabHostFragment != null) {
            mPdfViewCtrlTabHostFragment.removeHostListener(this);
        }
    }

    @Override
    public void onTabDocumentLoaded(String s) {
        new CustomQuickMenu(MainActivity.this, mPdfViewCtrlTabHostFragment).applyCustomization();
        new CustomLinkClick(MainActivity.this, mPdfViewCtrlTabHostFragment).applyCustomization();
        new CustomAnnotationToolbar(MainActivity.this, mPdfViewCtrlTabHostFragment).applyCustomization();
    }

    @Override
    public boolean onToolbarOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_show_toast) {
            Toast.makeText(this, "Show toast is clicked!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onTabHostShown() {

    }

    @Override
    public void onTabHostHidden() {

    }

    @Override
    public void onLastTabClosed() {

    }

    @Override
    public void onTabChanged(String s) {

    }

    @Override
    public void onOpenDocError() {

    }

    @Override
    public void onNavButtonPressed() {

    }

    @Override
    public void onShowFileInFolder(String s, String s1, int i) {

    }

    @Override
    public boolean canShowFileInFolder() {
        return false;
    }

    @Override
    public boolean canShowFileCloseSnackbar() {
        return false;
    }

    @Override
    public boolean onToolbarCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        return false;
    }

    @Override
    public boolean onToolbarPrepareOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public void onStartSearchMode() {

    }

    @Override
    public void onExitSearchMode() {

    }

    @Override
    public boolean canRecreateActivity() {
        return false;
    }

    @Override
    public void onTabPaused(FileInfo fileInfo, boolean b) {

    }

    @Override
    public void onJumpToSdCardFolder() {

    }
}
