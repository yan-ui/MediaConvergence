package cn.tklvyou.mediaconvergence.model;

public class User {


    /**
     * userinfo : {"id":2,"username":"Mason","nickname":"Mason","mobile":"17730212467","avatar":" ","score":0,"token":"f81b1870-dda7-42d6-82e9-bf9b54aab795","user_id":2,"createtime":1564042098,"expiretime":1566634098,"expires_in":2592000}
     */

    private UserinfoBean userinfo;

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class UserinfoBean {
        /**
         * id : 2
         * username : Mason
         * nickname : Mason
         * mobile : 17730212467
         * avatar :
         * score : 0
         * token : f81b1870-dda7-42d6-82e9-bf9b54aab795
         * user_id : 2
         * createtime : 1564042098
         * expiretime : 1566634098
         * expires_in : 2592000
         */

        private int id;
        private String username;
        private String nickname;
        private String mobile;
        private String avatar;
        private String score;
        private String token;
        private int user_id;
        private int createtime;
        private int expiretime;
        private int expires_in;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public int getExpiretime() {
            return expiretime;
        }

        public void setExpiretime(int expiretime) {
            this.expiretime = expiretime;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
