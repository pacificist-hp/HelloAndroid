package com.android.pacificist.helloandroid.matrix;

import android.content.Context;
import android.util.Log;

import com.tencent.matrix.plugin.DefaultPluginListener;
import com.tencent.matrix.report.Issue;

public class MatrixPluginListener extends DefaultPluginListener {

    private static final String TAG = "Matrix";

    public MatrixPluginListener(Context context) {
        super(context);
    }

    @Override
    public void onReportIssue(Issue issue) {
        super.onReportIssue(issue);
        Log.d(TAG, "report: " + issue.toString());
    }
}
