package com.android.pacificist.ioc;

import android.app.Activity;
import android.view.View;

import com.android.pacificist.ioc.inject.LayoutInject;
import com.android.pacificist.ioc.inject.EventInject;
import com.android.pacificist.ioc.inject.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectUtil {

    public static void injectContent(Activity activity) {
        LayoutInject layoutInject = activity.getClass().getAnnotation(LayoutInject.class);
        if (layoutInject != null) {
            activity.setContentView(layoutInject.value());
        }
    }

    public static void injectViews(Activity activity) throws Exception {
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                View view = activity.findViewById(viewInject.value());

                field.setAccessible(true);
                field.set(activity, view);
            }
        }
    }

    public static void injectEvents(Activity activity) throws Exception {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                EventInject eventInjectAnnotation = annotationType.getAnnotation(EventInject.class);
                if (eventInjectAnnotation != null) {
                    String listenerSetter = eventInjectAnnotation.listenerSetter();
                    Class<?> listenerType = eventInjectAnnotation.listenerType();
                    String methodName = eventInjectAnnotation.methodName();

                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    int[] viewIds = (int[]) valueMethod.invoke(annotation, null);

                    InjectHandler handler = new InjectHandler(activity);
                    handler.addMethod(methodName, method);
                    Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                            new Class<?>[] { listenerType }, handler);

                    for (int viewId : viewIds) {
                        View view = activity.findViewById(viewId);
                        Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        setEventListenerMethod.invoke(view, listener);
                    }
                }
            }
        }
    }
}
