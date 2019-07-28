package cn.tklvyou.mediaconvergence.model;

import java.io.Serializable;
import java.util.List;

public class NewsBean implements Serializable {


    /**
     * id : 70
     * user_id : 2
     * admin_id : 0
     * module : 随手拍
     * module_second :
     * name : 回家啊
     * image : null
     * images : ["http://medium.tklvyou.cn/uploads/20190726/84ad33b65603c7386b5a1c1629cbde3c.jpeg","http://medium.tklvyou.cn/uploads/20190726/72991283ebf4272eb4afa8fdcc1c2eeb.jpeg","http://medium.tklvyou.cn/uploads/20190726/763cdbdb0b7a634055ed7462bf528b21.jpeg"]
     * video : null
     * audio :
     * content : null
     * nickname : Mason
     * avatar :
     * status : normal
     * visit_num : 3
     * comment_num : 0
     * like_num : 0
     * createtime : 1564100817
     * updatetime : 1564100817
     * vote_id : 0
     * weigh : 0
     * time : null
     * begintime : 1天前
     * like_status : 0
     * collect_status : 0
     */

    private int id;
    private int user_id;
    private int admin_id;
    private String module;
    private String module_second;
    private String name;
    private String image;
    private String video;
    private String audio;
    private String content;
    private String nickname;
    private String avatar;
    private String status;
    private int visit_num;
    private int comment_num;
    private int like_num;
    private int createtime;
    private int updatetime;
    private int vote_id;
    private int weigh;
    private String time;
    private List<CommentModel> comment;
    private String begintime;
    private int like_status;
    private int collect_status;
    private List<String> images;
    private boolean isExpand;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getModule_second() {
        return module_second;
    }

    public void setModule_second(String module_second) {
        this.module_second = module_second;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getVisit_num() {
        return visit_num;
    }

    public void setVisit_num(int visit_num) {
        this.visit_num = visit_num;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public int getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(int updatetime) {
        this.updatetime = updatetime;
    }

    public int getVote_id() {
        return vote_id;
    }

    public void setVote_id(int vote_id) {
        this.vote_id = vote_id;
    }

    public int getWeigh() {
        return weigh;
    }

    public void setWeigh(int weigh) {
        this.weigh = weigh;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<CommentModel> getComment() {
        return comment;
    }

    public void setComment(List<CommentModel> comment) {
        this.comment = comment;
    }

    public String getBegintime() {
        return begintime;
    }

    public void setBegintime(String begintime) {
        this.begintime = begintime;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getCollect_status() {
        return collect_status;
    }

    public void setCollect_status(int collect_status) {
        this.collect_status = collect_status;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
