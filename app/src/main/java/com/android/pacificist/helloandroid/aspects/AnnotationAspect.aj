import android.util.Log;

aspect Annotation {

    pointcut annotation(String str):
        execution(@com.android.pacificist.helloandroid.annotation.AspectAnnotation * *(..)) &&
        args(str);

    after(String str):
        annotation(str) {
            Log.i("Aspect", str);
        }
}