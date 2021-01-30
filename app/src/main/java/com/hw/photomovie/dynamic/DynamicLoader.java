package com.hw.photomovie.dynamic;

import android.content.Context;
import android.text.TextUtils;
import com.hw.photomovie.segment.MovieSegment;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class DynamicLoader {
    public static List<MovieSegment> loadSegmentsFromFile(Context context, String path, String classFullName) {
        List<MovieSegment> segmentList = new ArrayList<MovieSegment>();
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(classFullName)) {
            return segmentList;
        }
        final File jarFile = new File(path);
        final File dexOutputDir = context.getCacheDir();
        DexClassLoader mClassLoader = new DexClassLoader(jarFile.getAbsolutePath(), dexOutputDir.getAbsolutePath(), null,
                context.getClassLoader());
        Class libProviderClazz = null;
        try {
            libProviderClazz = mClassLoader.loadClass(classFullName);
            Method[] methods = libProviderClazz.getMethods();
            Method method = null;
            for(int i=0;methods!=null && i<methods.length;i++){
                if("initSegments".equals(methods[i].getName())){
                    method = methods[i];
                    break;
                }
            }
            if(method==null){
                return null;
            }
            return (List<MovieSegment>) method.invoke(null,new Object[0]);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return segmentList;
    }
}
