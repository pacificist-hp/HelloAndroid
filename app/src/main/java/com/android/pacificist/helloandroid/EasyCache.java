package com.android.pacificist.helloandroid;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.pacificist.helloandroid.cache.DiskLruCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by pacificist on 2019/6/30.
 */
public class EasyCache {

    public static final int DEF_HASH = "".hashCode();
    private static final String DEF_DATA = "";

    private static final int MEM_CACHE_SIZE = 90000;
    private static final int DISK_CACHE_SIZE = 1024 * 1024;

    private final Application mApplication;

    private final LruCache<String, String> mMemCache;
    private DiskLruCache mDiskCache;

    private static EasyCache sCache = null;

    private File getCacheFile() {
        File cacheFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cacheFile = new File(mApplication.getExternalCacheDir(), "easy");
        } else {
            cacheFile = new File(mApplication.getCacheDir(), "easy");
        }

        return cacheFile;
    }

    private EasyCache(Application application) {
        mApplication = application;

        mMemCache = new LruCache<String, String>(MEM_CACHE_SIZE) {
            @Override
            protected int sizeOf(String key, String value) {
                return value == null ? 0 : value.length();
            }
        };

        final File cache = getCacheFile();
        if (cache != null && !cache.exists()) {
            cache.mkdirs();
        }

        if (cache != null && cache.exists()) {
            try {
                mDiskCache = DiskLruCache.open(cache, 1, 1, DISK_CACHE_SIZE);
            } catch (Exception e) {
                mDiskCache = null;
            }
        } else {
            mDiskCache = null;
        }
    }

    public static EasyCache getCache(Application application) {
        if (sCache == null) {
            sCache = new EasyCache(application);
        }
        return sCache;
    }

    public String readCache(final String name) {
        if (TextUtils.isEmpty(name)) {
            return DEF_DATA;
        }

        String cache = mMemCache.get(name);
        if (TextUtils.isEmpty(cache)) {
            cache = readDiskCache(name);
        }
        return cache;
    }

    /* It must run in a non-UI thread */
    private String readDiskCache(String name) {
        String data = DEF_DATA;

        if (mDiskCache != null) {
            try {
                DiskLruCache.Snapshot snapshot = mDiskCache.get(name);
                if (snapshot != null) {
                    InputStream inputStream = snapshot.getInputStream(0);
                    if (inputStream != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while((length = inputStream.read(buffer)) != -1) {
                            stream.write(buffer, 0, length);
                        }
                        data = stream.toString(Charset.defaultCharset().name());
                    }
                }
            } catch (IOException e) {
                data = DEF_DATA;
            }
        }

        if (!TextUtils.isEmpty(data)) {
            mMemCache.put(name, data);
        }

        return data;
    }

    public int readCacheHash(final String name) {
        if (TextUtils.isEmpty(name)) {
            return DEF_HASH;
        }

        SharedPreferences sh = mApplication.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (sh != null) {
            return sh.getInt("hash", DEF_HASH);
        }
        return DEF_HASH;
    }

    /* It must run in a non-UI thread */
    public void saveCache(final String name, final String data) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(data)) {
            return;
        }

        mMemCache.put(name, data);

        SharedPreferences sh = mApplication.getSharedPreferences(name, Context.MODE_PRIVATE);
        if (sh != null) {
            sh.edit().putInt("hash", data.hashCode()).apply();
        }

        if (mDiskCache != null) {
            DiskLruCache.Editor editor = null;
            OutputStream outputStream = null;
            try {
                editor = mDiskCache.edit(name);
                if (editor != null) {
                    outputStream = editor.newOutputStream(0);
                    if (outputStream != null) {
                        outputStream.write(data.getBytes(Charset.defaultCharset()));
                        editor.commit();
                    }
                }
            } catch (IOException e) {
                if (editor != null) {
                    try {
                        editor.abort();
                    } catch (IOException e1) {

                    }
                }
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    mDiskCache.flush();
                } catch (IOException e) {

                }
            }
        }
    }
}
