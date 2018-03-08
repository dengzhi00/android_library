package com.dzm.demo;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceConfigurationError;

/**
 * @author 邓治民
 *         data 2018/3/6 下午3:01
 */

public class MyLibServicesLoader {

    private static HashMap<String,Object> servers = new HashMap<>();

    private static MyLoad myLoad;

    public static <T> T getService(Context context, Class<T> clz){
        T t = (T) servers.get(clz.getName());
        if(null != t)
            return  t;
        return getLoad().load(context,clz);
    }

    public static <T> T getService(Class<T> clz){
        return getService(null,clz);
    }

    private static MyLoad getLoad(){
        if(null == myLoad)
            myLoad = new MyLoad();
        return myLoad;
    }


    private static Map<String,String> parse(URL u)
            throws ServiceConfigurationError
    {
        InputStream in = null;
        BufferedReader r = null;
        Map<String,String> names = new HashMap<>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(r, lc, names)) >= 0);
        } catch (IOException x) {
//            fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
            } catch (IOException y) {
//                fail(service, "Error closing configuration file", y);
            }
        }
        return names;
    }

    private static int parseLine(BufferedReader r, int lc,
                                 Map<String,String> names)
            throws IOException, ServiceConfigurationError
    {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) ln = ln.substring(0, ci);
        ln = ln.trim();

        String lns[] = ln.split(":");
        if(lns.length == 2){
            if(!isJavaIdentifier(lns[0]) || !isJavaIdentifier(lns[1])){
                return -1;
            }
            names.put(lns[0],lns[1]);
        }else {
            return -1;
        }
        return lc + 1;
    }

    private static boolean isJavaIdentifier(String ln){
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                return false;
//                fail(service, u, lc, "Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                return false;
//                fail(service, u, lc, "Illegal provider-class name: " + ln);
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    return false;
//                    fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
        }
        return true;
    }

    private static void fail(Class<?> service, String msg, Throwable cause)
            throws ServiceConfigurationError
    {
        throw new ServiceConfigurationError(service.getName() + ": " + msg,
                cause);
    }

    private static void fail(Class<?> service, String msg)
            throws ServiceConfigurationError
    {
        throw new ServiceConfigurationError(service.getName() + ": " + msg);
    }

    private static void fail(Class<?> service, URL u, int line, String msg)
            throws ServiceConfigurationError
    {
        fail(service, u + ":" + line + ": " + msg);
    }

    static class MyLoad {

        ClassLoader loader;
        Enumeration<URL> configs = null;
        Map<String,String> pending = null;

        MyLoad(){
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if(null != cl){
                loader = cl;
            } else {
                loader = ClassLoader.getSystemClassLoader();
            }
            initLoad();
        }

        private void initLoad(){
            if (configs == null) {
                try {
//                    String fullName = PREFIX + service.getName();
                    String fullName = "assets/services/serviceconfig";
                    if (loader == null)
                        configs = ClassLoader.getSystemResources(fullName);
                    else
                        configs = loader.getResources(fullName);
                } catch (IOException x) {
//                    fail(, "Error locating configuration files", x);
                }
                pending = parse(configs.nextElement());
            }
        }

        <T> T load(Context context,Class<T> server){

                String cn = pending.get(server.getName());
                Class<?> c = null;
                try {
                    c = Class.forName(cn, false, loader);
                } catch (ClassNotFoundException x) {
                    fail(server,
                            // Android-changed: Let the ServiceConfigurationError have a cause.
                            "Provider " + cn + " not found", x);
                    // "Provider " + cn + " not found");
                }
                if (!server.isAssignableFrom(c)) {
                    // Android-changed: Let the ServiceConfigurationError have a cause.
                    ClassCastException cce = new ClassCastException(
                            server.getCanonicalName() + " is not assignable from " + c.getCanonicalName());
                    fail(server,
                            "Provider " + cn  + " not a subtype", cce);
                    // fail(service,
                    //        "Provider " + cn  + " not a subtype");
                }
                try {
                    T p = server.cast(c.newInstance());
//                    if(p instanceof Iprovider){
//                        ((Iprovider) p).init(context);
//                    }
                    return p;
                } catch (Throwable x) {
                    fail(server,
                            "Provider " + cn + " could not be instantiated",
                            x);
                }
                throw new Error();          // This cannot happen
        }
    }

}
