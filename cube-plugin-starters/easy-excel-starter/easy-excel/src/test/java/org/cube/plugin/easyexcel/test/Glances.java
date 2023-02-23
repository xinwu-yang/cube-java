package org.cube.plugin.easyexcel.test;

import org.cube.plugin.easyexcel.annotations.Check;
import org.cube.plugin.easyexcel.annotations.Excel;
import org.cube.plugin.easyexcel.constraintvalidators.NoNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述
 *
 * @author xinwuy
 * @version V2.3.x
 * @since 2022/2/18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Glances implements Serializable {

    @Excel("主键")
    private Long id;

    @Excel("时间")
    private Date date;

    @Excel("IP")
    @Check(validated = {NoNull.class},message = "必填")
    private String ip;

    @Excel("KFK总写入（min）")
    private Long kfkIn;
}
