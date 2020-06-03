package cn.mapscloud.www.mydevelop;

import java.util.List;

/**
 * Created by wangyongcan on 2017/8/27.
 */

class DictationResult {

    private String sn;
    private String ls;
    private String bg;
    private String ed;
    private List<Words> ws;
    public static class Words {
        private String bg;
        private List<Cw> cw;


    public static class Cw {
        private String w;
        private String sc;

        public String getW() {
            return w;
        }

        public String getSc() {
            return sc;
        }

        public void setW(String w) {
            this.w = w;
        }

        public void setSc(String sc) {
            this.sc = sc;
        }

        @Override
        public String toString() {
//            return "Wc{" +
//                    "w='" + w + '\'' +
//                    ", sc='" + sc + '\'' +
//                    '}';

            return w;
        }

    }

        @Override
        public String toString() {
//            return "Words{" +
//                    "bg='" + bg + '\'' +
//                    ", cw=" + cw +
//                    '}';

            String result = "";
            for (Cw cwTmp : cw) {
                result += cwTmp.toString();

            }

            return result;
        }


    }
    @Override
    public String toString() {
//        return "DictationResult{" +
//                "sn='" + sn + '\'' +
//                ", ls='" + ls + '\'' +
//                ", bg='" + bg + '\'' +
//                ", ed='" + ed + '\'' +
//                '}';

        String result="";
            for (Words wsTmp : ws){
                        result += wsTmp.toString();
                        }
                return result;
    }

    public String getLs() {
        return ls;
    }

    public String getBg() {
        return bg;
    }

    public String getEd() {
        return ed;
    }

    public String getSn() {

        return sn;
    }


    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setLs(String ls) {
        this.ls = ls;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public void setEd(String ed) {
        this.ed = ed;
    }


}
