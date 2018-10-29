package com.to8to.util;

import com.google.common.base.Throwables;

/**
 * Created by senix.liu on 2018/3/22.
 */
public class StringUtils {

    private static final String COLON = ":";
    private static final String SLASH = "/";
    private static final String SECURE_SCHEMA = "https://";
    private static final String NON_SECURE_SCHEMA = "http://";

    public static String compositeURL(String ip, int port, String contextPath) {
        return compositeURL(ip, port, contextPath, false);
    }

    public static String stripCommonPrefix(String commonPrefix,String src){
        if(!src.startsWith(commonPrefix)){
            return src;
        }

        return src.substring(commonPrefix.length()+1);
    }

    public static String compositeURL(String ip, int port, String contextPath, boolean isSecure) {
        StringBuilder sb = new StringBuilder();

        String schema = isSecure ? SECURE_SCHEMA : NON_SECURE_SCHEMA;

        String ctxPath;
        if (isNullOrEmpty(contextPath)) {
            ctxPath = "";
        } else {
            ctxPath = contextPath.startsWith(SLASH) ? contextPath : SLASH + contextPath;
        }

        sb.append(schema)
                .append(ip)
                .append(COLON)
                .append(port)
                .append(ctxPath);

        return sb.toString();
    }


    public static boolean isNullOrEmpty(String src) {
        return (src == null || "".equals(src));
    }

    public static String combinePath(boolean startWithSlash, boolean endWithSlash, String... parts) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            if (i == parts.length - 1) {
                sb.append(parts[i]);
                continue;
            }

            sb.append(parts[i] + "/");
        }

        String path = sb.toString();

        if (startWithSlash && !path.startsWith(SLASH)) {
            path = SLASH + path;
        }

        if (endWithSlash && !path.endsWith(SLASH)) {
            path = path + SLASH;
        }

        return path;
    }

    public static String getExceptionStack(Throwable e) {
        return Throwables.getStackTraceAsString(e);
    }

}
