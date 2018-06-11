package jp.thesaurus.acctmanager;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Constants {
    private Constants() {
    }
    public static final Map<String, Map<String, String>> SERVICE_MAP;
    static {
        Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
        map.put("1", new HashMap<String, String>() {
            {
                put("Amazon", "amazon");
            }
        });
        map.put("2", new HashMap<String, String>() {
            {
                put("Git", "git");
            }
        });
        map.put("3", new HashMap<String, String>() {
            {
                put("Google", "google");
            }
        });
        map.put("4", new HashMap<String, String>() {
            {
                put("はてな", "hatena");
            }
        });
        map.put("5", new HashMap<String, String>() {
            {
                put("みずほ", "mizuho");
            }
        });
        map.put("6", new HashMap<String, String>() {
            {
                put("SBI", "sbi");
            }
        });
        map.put("7", new HashMap<String, String>() {
            {
                put("Yahoo", "yahoo");
            }
        });
        map.put("8", new HashMap<String, String>() {
            {
                put("X−flag", "wakuwaku");
            }
        });
        map.put("9",  new HashMap<String, String>() {
            {
                put("開発関連", "warai");
            }
        });
        map.put("10", new HashMap<String, String>() {
            {
                put("その他", "sonota");
            }
        });
        SERVICE_MAP = Collections.unmodifiableMap(map);
    }
    public static final Map<String, ServiceBean> SERVICE_BEANS;
    static {
        Map<String, ServiceBean> map = new LinkedHashMap<String, ServiceBean>();
        map.put("1", new ServiceBean("Amazon", R.mipmap.s_amazon));
        map.put("2", new ServiceBean("Git", R.mipmap.s_git));
        map.put("3", new ServiceBean("Google", R.mipmap.s_google));
        map.put("4", new ServiceBean("はてな", R.mipmap.s_hatena));
        map.put("5", new ServiceBean("みずほ", R.mipmap.s_mizuho));
        map.put("6", new ServiceBean("SBI", R.mipmap.s_sbi));
        map.put("7", new ServiceBean("Yahoo", R.mipmap.s_yahoo));
        map.put("8", new ServiceBean("X−flag", R.mipmap.s_wakuwaku));
        map.put("9", new ServiceBean("開発関連", R.mipmap.s_warai));
        map.put("10",new ServiceBean("その他", R.mipmap.s_sonota));
        SERVICE_BEANS = Collections.unmodifiableMap(map);
    }
    public static class Character {
        public static final String SLASH = "/";
        public static final String BLANK = " ";
        public static final String CRLF = "¥r¥n";
        public static final String LF = "¥n";
    }
}