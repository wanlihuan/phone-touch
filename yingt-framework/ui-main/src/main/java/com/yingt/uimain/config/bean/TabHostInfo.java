package com.yingt.uimain.config.bean;

import java.util.List;

/**
 * Created by laihuan.wan on 2017/5/9 0009.
 */

public class TabHostInfo {

    private String version;
    private String backgroundColor;
    private List<TabListBean> tabList;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public List<TabListBean> getTabList() {
        return tabList;
    }

    public void setTabList(List<TabListBean> tabList) {
        this.tabList = tabList;
    }

    public static class TabListBean {
        private String functionCode;
        private String fragmentName;
        private String title;
        private String norImage;
        private String selectImage;

        public String getFunctionCode() {
            return functionCode;
        }

        public void setFunctionCode(String functionCode) {
            this.functionCode = functionCode;
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public void setFragmentName(String fragmentName) {
            this.fragmentName = fragmentName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNorImage() {
            return norImage;
        }

        public void setNorImage(String norImage) {
            this.norImage = norImage;
        }

        public String getSelectImage() {
            return selectImage;
        }

        public void setSelectImage(String selectImage) {
            this.selectImage = selectImage;
        }
    }
}
