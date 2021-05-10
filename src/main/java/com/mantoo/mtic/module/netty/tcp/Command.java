package com.mantoo.mtic.module.netty.tcp;


import io.netty.buffer.ByteBufUtil;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author gemi
 */
@Data
@Getter
public class Command implements Serializable {
    private final static long serialVersionUID = 1L;
    private byte[] flag;
    private int length = 0;
    private SubCommand subCommand;
    private byte[] cmd;

    @Override
    public String toString() {
        return "Command{" +
                "flag=" + ByteBufUtil.hexDump(flag) +
                ", length=" + length +
                ", subCommand=" + subCommand +
                "cmd=" + ByteBufUtil.hexDump(cmd) +
                '}';
    }
}
