package xyz.wagyourtail.asm;

import org.objectweb.asm.Type;

public class MemberNameAndDesc {

    public final String name;
    public final Type desc;

    public MemberNameAndDesc(String name, Type desc) {
        this.name = name;
        this.desc = desc;
    }

}
