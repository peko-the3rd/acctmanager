package jp.thesaurus.acctmanager;

public class ServiceBean {

    private String servicName;

    private int servicMipmap;

    // constructor
    public ServiceBean(){
        this.servicName = "";
        this.servicMipmap = 0;
    }
    public ServiceBean(String servicName,int servicMipmap){
        this.servicName = servicName;
        this.servicMipmap = servicMipmap;
    }

    // setter
    public void setServicName(String servicName){
        this.servicName = servicName;
    }
    // getter
    public String getServicName(){
        return this.servicName;
    }

    // setter
    public void setServicMipmap(int servicMipmap){
        this.servicMipmap = servicMipmap;
    }
    // getter
    public int getServicMipmap(){
        return this.servicMipmap;
    }
}
