package com.mantoo.mtic.module.netty.tcp;

import io.netty.buffer.ByteBufUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * @author gemi
 */
@Data
public class SubCommand implements Serializable{
    private final static long serialVersionUID=  1L;

    private int length;
    private byte[] data;

    @Override
    public String toString() {
        return "SubCommand{" +
                "length=" + length +
                ", data=" + ByteBufUtil.hexDump(data) +
                '}';
    }
}
