package models;

public class ModelPosts {
    String pid,ptitle,pdescrp,pimage,ptime,uid,uemail,uDp,uname,city;
    public  ModelPosts()
    {

    }

    public ModelPosts(String pid, String ptitle, String pdescrp, String pimage, String ptime, String uid, String uemail, String uDp, String uname,String city) {
        this.pid = pid;
        this.ptitle = ptitle;
        this.pdescrp = pdescrp;
        this.pimage = pimage;
        this.ptime = ptime;
        this.uid = uid;
        this.uemail = uemail;
        this.uDp = uDp;
        this.uname = uname;
        this.city=city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPtitle() {
        return ptitle;
    }

    public void setPtitle(String ptitle) {
        this.ptitle = ptitle;
    }

    public String getPdescrp() {
        return pdescrp;
    }

    public void setPdescrp(String pdescrp) {
        this.pdescrp = pdescrp;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}

