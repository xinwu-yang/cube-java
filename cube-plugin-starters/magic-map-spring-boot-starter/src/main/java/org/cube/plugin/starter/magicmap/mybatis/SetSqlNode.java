package org.cube.plugin.starter.magicmap.mybatis;

/**
 * 对应XML中 <set>
 *
 * @author zhangxu
 * @version : 2020-12-05
 */
public class SetSqlNode extends TrimSqlNode {

	public SetSqlNode() {
		this.prefix = "SET";
		this.suffixOverrides = ",";
	}
}
