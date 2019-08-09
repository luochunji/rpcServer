package com.cjluo.rpc.dto;

import java.io.Serializable;

/**
 * @author cj.luo
 * @date 2019/4/10
 */
public class User implements Serializable {

    private static final long serialVersionUID = 9169708774572711804L;

    private String name;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
