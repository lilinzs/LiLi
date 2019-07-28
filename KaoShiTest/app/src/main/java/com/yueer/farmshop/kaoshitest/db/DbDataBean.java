package com.yueer.farmshop.kaoshitest.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DbDataBean {
    @Id(autoincrement = true)
    private Long newId;
    @Property
    private int id;
    @Property
    private String frist_name;
    @Property
    private String shop_name;
    @Property
    private String shop_introd;
    @Property
    private String shop_pirce;
    @Property
    private String shop_image_url;
    @Generated(hash = 1138175504)
    public DbDataBean(Long newId, int id, String frist_name, String shop_name,
            String shop_introd, String shop_pirce, String shop_image_url) {
        this.newId = newId;
        this.id = id;
        this.frist_name = frist_name;
        this.shop_name = shop_name;
        this.shop_introd = shop_introd;
        this.shop_pirce = shop_pirce;
        this.shop_image_url = shop_image_url;
    }
    @Generated(hash = 1667606372)
    public DbDataBean() {
    }
    public Long getNewId() {
        return this.newId;
    }
    public void setNewId(Long newId) {
        this.newId = newId;
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFrist_name() {
        return this.frist_name;
    }
    public void setFrist_name(String frist_name) {
        this.frist_name = frist_name;
    }
    public String getShop_name() {
        return this.shop_name;
    }
    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }
    public String getShop_introd() {
        return this.shop_introd;
    }
    public void setShop_introd(String shop_introd) {
        this.shop_introd = shop_introd;
    }
    public String getShop_pirce() {
        return this.shop_pirce;
    }
    public void setShop_pirce(String shop_pirce) {
        this.shop_pirce = shop_pirce;
    }
    public String getShop_image_url() {
        return this.shop_image_url;
    }
    public void setShop_image_url(String shop_image_url) {
        this.shop_image_url = shop_image_url;
    }

}
