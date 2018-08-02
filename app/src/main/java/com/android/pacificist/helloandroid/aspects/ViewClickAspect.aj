import android.util.Log;
import android.view.View;

aspect ViewClickAspect {

    pointcut viewClick(View v):
        execution(* android.view.View.OnClickListener.onClick(android.view.View)) &&
        args(v);

    after(View v):
        viewClick(v) {
            Log.i("Aspect", "ViewClick: " + (v == null ? "null" : v.getResources().getResourceEntryName(v.getId())));
        }
}