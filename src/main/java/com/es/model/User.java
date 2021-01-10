package com.es.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * User
 * @author Hexiaoshu
 * @date 2020-11-28 18:11:57
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = -6170418806018348653L;
    /** id */
    private Long id;

    /** name */
    private String name;

    private Date createTime;

}